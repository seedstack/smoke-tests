/*
 * Creation : 2 sept. 2015
 */
package org.seedstack.tests;

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
import java.util.Arrays;
import java.util.List;

import jodd.props.Props;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.json.JSONObject;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

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
 *
 */
public class JsonWrtiterListener extends RunListener {

    @Override
    public void testRunFinished(Result result) throws Exception {
        updateGitRepository(result);
    }


    private void updateGitRepository(Result result) throws Exception {
        
        if ( System.getProperty("properties.file") == null ) {
            // No Git properties so nothing to do
            return;
        }
        
        // Create JSON result
        JSONObject obj = new JSONObject();
        final String serverName = System.getProperty("server.name");
        final String serverVersion = System.getProperty("server.version");
        obj.putOnce("Name", serverName);
        obj.putOnce("Version", serverVersion);
        if (result.wasSuccessful()) {
            obj.putOnce("Result", "SUCCESS");
        } else {
            obj.putOnce("Result", "FAILED");
        }
        

        // Git props
        Props props = new Props();
        props.load(new File(System.getProperty("properties.file")));

        addProxyConfiguration(props);
        
        //Initialize GIT repository
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(props.getValue("github.token"), "");
        String remoteUrl = props.getValue("github.url");
        File siteDirectory = new File("target/website");
        if (siteDirectory.exists()) {
            FileUtils.deleteDirectory(siteDirectory);
        }
        FileUtils.forceMkdir(siteDirectory);
        Git git = cloneRepo(remoteUrl, siteDirectory);

        // Add json result file for the server
        File resultFile = new File(siteDirectory + "/" + serverName + ".json");
        FileWriter file = new FileWriter(resultFile);
        file.write(obj.toString());
        file.close();

        // Update Github
        git.add().addFilepattern(".").call();
        git.commit().setAuthor(props.getValue("github.name"), props.getValue("github.mail")).setMessage("Built for " + serverName).call();
        Boolean dryRun = Boolean.valueOf(props.getValue("github.dry-run")) ;
        if ( ! dryRun ) {
            System.out.println("[GIT] Push results to github");
        }
        git.push().setRemote(remoteUrl).setCredentialsProvider(credentialsProvider).setDryRun(dryRun).call();
        git.close();
        
    }

    
    private void addProxyConfiguration(final Props props) {
        final String httpProxyHost = props.getValue("http.proxy.host");
        if ( httpProxyHost != null) {
            int port = 80 ;
            if ( props.getValue("http.proxy.port") != null ) {
                port = Integer.valueOf(props.getValue("http.proxy.port"));
            }
            final int httpProxyPort = port;
            final String httpProxyUser = props.getValue("http.proxy.user");
            final String httpProxyPassword = props.getValue("http.proxy.password");
            ProxySelector.setDefault(new ProxySelector() {
                final ProxySelector delegate = ProxySelector.getDefault();

                @Override
                public List<Proxy> select(URI uri) {
                    // Filter the URIs to be proxied
                    if (uri.getHost().contains("github") && uri.getScheme().contains("http")) {
                        return Arrays.asList(new Proxy(Type.HTTP, InetSocketAddress.createUnresolved(httpProxyHost, httpProxyPort)));
                    }
                    // revert to the default behavior
                    return delegate == null ? Arrays.asList(Proxy.NO_PROXY) : delegate.select(uri);
                }

                @Override
                public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
                    if (uri == null || sa == null || ioe == null) {
                        throw new IllegalArgumentException("Arguments can't be null.");
                    }

                }
            });
            if ( httpProxyUser != null ) {
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        if (getRequestorType() == RequestorType.PROXY) {
                            return new PasswordAuthentication(httpProxyUser, httpProxyPassword.toCharArray());
                        }
                        return super.getPasswordAuthentication();
                    }

                });
            }
        }
    }

    private Git cloneRepo(String remoteUrl, File localPath) throws GitAPIException {
        localPath.delete();
        System.out.println("[GIT] Cloning from " + remoteUrl + " to " + localPath);
        return Git.cloneRepository().setURI(remoteUrl).setDirectory(localPath).setBranch("gh-pages").call();
    }

}
