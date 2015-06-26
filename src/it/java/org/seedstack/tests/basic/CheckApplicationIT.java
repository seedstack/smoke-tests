/*
 * Creation : 16 juin 2015
 */
/**
 * 
 */
package org.seedstack.tests.basic;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.seedstack.tests.HelloResource;

/**
 * Check if {@link HelloResource} is ok.
 * 
 * @author thierry.bouvet@mpsa.com
 *
 */
public class CheckApplicationIT {

    @Test
    public void testHelloResource() throws Exception {
        Integer port = Integer.valueOf(System.getProperty("docker.port"));
        String uri = "http://localhost:" + port + "/smoke-tests/rest/hello";
        HttpUriRequest request = new HttpGet(uri);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        Assertions.assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        
        final String textMimeType = "text/plain";
        Assertions.assertThat(ContentType.getOrDefault(response.getEntity()).getMimeType()).isEqualTo(textMimeType);
        
        final String textFromResponse = "hello";
        Assertions.assertThat(EntityUtils.toString(response.getEntity())).isEqualTo(textFromResponse);
    }

}
