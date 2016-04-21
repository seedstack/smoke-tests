/*
 * Creation : 2 sept. 2015
 */
package org.seedstack.smoke.utils;

import jodd.props.Props;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONObject;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.seedstack.smoke.tests.BaseSmokeTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.seedstack.smoke.utils.NameUtils.humanizeTestMethod;

/**
 * Test listener to create a JSON file with tests result.
 * If the property properties.file is set, this listener push to Github the results.
 * Properties file examples :
 * <pre>
 * {@code
 * [github]
 * url = <https github url>
 * name = <user to use to commit & push>
 * mail = <mail to use to commit & push>
 * token = <github token to push>
 * dry-run = <set to true to test the push>
 *
 * [http.proxy]
 * host = <http proxy if needed. Do not set if no proxy connection is needed>
 * port = <http proxy port>
 * user = <http proxy user if needed>
 * password = <http proxy password if needed>
 * }
 * </pre>
 *
 * @author thierry.bouvet@mpsa.com
 */
public class JsonWriterListener extends RunListener {
    private Map<String, List<String>> successfulTests = new HashMap<String, List<String>>();
    private Map<String, List<String>> failedTests = new HashMap<String, List<String>>();

    @Override
    public void testFinished(Description description) throws Exception {
        if (BaseSmokeTest.class.isAssignableFrom(description.getTestClass())) {
            List<String> failed = failedTests.get(getTestGroup(description));
            if (failed == null || !failed.contains(humanizeTestMethod(description.getMethodName()))) {
                putTest(description, successfulTests);
            }
        }
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        if (BaseSmokeTest.class.isAssignableFrom(failure.getDescription().getTestClass())) {
            putTest(failure.getDescription(), failedTests);
        }
    }

    private void putTest(Description description, Map<String, List<String>> map) {
        String key = getTestGroup(description);
        List<String> tests;
        if ((tests = map.get(key)) == null) {
            map.put(key, tests = new ArrayList<String>());
        }
        tests.add(humanizeTestMethod(description.getMethodName()));
    }

    private String getTestGroup(Description description) {
        return description.getTestClass().getPackage().getName().substring(BaseSmokeTest.class.getPackage().getName().length() + 1);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        final String serverName = System.getProperty("server.name");
        final String serverVersion = System.getProperty("server.version");

        generateJSONFile(result, serverName == null ? "unknown" : serverName, serverVersion);
        updateGitRepository(serverName);
    }

    private void generateJSONFile(Result result, String serverName, String serverVersion) throws IOException {
        JSONObject obj = new JSONObject();
        obj.putOnce("serverName", serverName);
        obj.putOnce("serverVersion", serverVersion);
        if (result.wasSuccessful()) {
            obj.putOnce("result", "SUCCESS");
        } else {
            obj.putOnce("result", "FAILURE");
        }

        obj.putOnce("successful", successfulTests);
        obj.putOnce("failed", failedTests);

        File resultFile = new File(String.format("target/%s.json", serverName.toLowerCase()));
        FileWriter file = new FileWriter(resultFile);
        file.write(obj.toString());
        file.close();
    }


    private void updateGitRepository(String serverName) throws Exception {
        if (System.getProperty("properties.file") == null) {
            return;
        }

        Props props = loadProps();

        configureProxy(props);

        Git git = initializeGitRepository(props);

        commitChanges(git, props, serverName);

        pushChanges(props, Boolean.valueOf(props.getValue("github.dry-run")), git);

        git.close();
    }

    private Props loadProps() throws IOException {
        Props props = new Props();
        props.load(new File(System.getProperty("properties.file")));
        return props;
    }

    private Git initializeGitRepository(Props props) throws IOException, GitAPIException {
        File siteDirectory = new File("target/website");
        if (siteDirectory.exists()) {
            FileUtils.deleteDirectory(siteDirectory);
        }
        FileUtils.forceMkdir(siteDirectory);
        return cloneRepo(getRemoteUrl(props), siteDirectory);
    }

    private Git cloneRepo(String remoteUrl, File localPath) throws GitAPIException {
        FileUtils.deleteQuietly(localPath);
        log("Cloning from %s to %s", remoteUrl, localPath);
        return Git.cloneRepository().setURI(remoteUrl).setDirectory(localPath).setBranch("gh-pages").call();
    }

    private void log(String pattern, Object... values) {
        System.out.println(String.format("[GIT] " + pattern, values));
    }

    private String getRemoteUrl(Props props) {
        return props.getValue("github.url");
    }

    private void pushChanges(Props props, boolean dryRun, Git git) throws GitAPIException {
        if (!dryRun) {
            log("Pushing results to GitHub");
        }

        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(props.getValue("github.token"), "");
        git.push().setRemote(getRemoteUrl(props)).setCredentialsProvider(credentialsProvider).setDryRun(dryRun).call();
    }


    private void commitChanges(Git git, Props props, String serverName) throws GitAPIException {
        git.add().addFilepattern(".").call();
        git.commit().setAuthor(props.getValue("github.name"), props.getValue("github.mail")).setMessage("Built for " + serverName).call();
    }

    private void configureProxy(final Props props) {
        final String httpProxyHost = props.getValue("http.proxy.host");
        if (httpProxyHost != null) {
            int port = 80;
            if (props.getValue("http.proxy.port") != null) {
                port = Integer.valueOf(props.getValue("http.proxy.port"));
            }
            final int httpProxyPort = port;
            final String httpProxyUser = props.getValue("http.proxy.user");
            final String httpProxyPassword = props.getValue("http.proxy.password");

            ProxySelector.setDefault(buildProxySelector(httpProxyHost, httpProxyPort));

            if (httpProxyUser != null) {
                Authenticator.setDefault(buildProxyAuthenticator(httpProxyUser, httpProxyPassword));
            }
        }
    }

    private ProxySelector buildProxySelector(final String httpProxyHost, final int httpProxyPort) {
        return new ProxySelector() {
            final ProxySelector delegate = ProxySelector.getDefault();

            @Override
            public List<Proxy> select(URI uri) {
                // Filter the URIs to be proxied
                if (uri.getHost().contains("github") && uri.getScheme().contains("http")) {
                    return Collections.singletonList(new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(httpProxyHost, httpProxyPort)));
                }
                // revert to the default behavior
                return delegate == null ? Collections.singletonList(Proxy.NO_PROXY) : delegate.select(uri);
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                if (uri == null || sa == null || ioe == null) {
                    throw new IllegalArgumentException("Arguments can't be null.");
                }

            }
        };
    }

    private Authenticator buildProxyAuthenticator(final String httpProxyUser, final String httpProxyPassword) {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (getRequestorType() == RequestorType.PROXY) {
                    return new PasswordAuthentication(httpProxyUser, httpProxyPassword.toCharArray());
                }
                return super.getPasswordAuthentication();
            }

        };
    }
}
