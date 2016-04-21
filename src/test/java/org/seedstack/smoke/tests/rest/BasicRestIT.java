/*
 * Creation : 16 juin 2015
 */
/**
 *
 */
package org.seedstack.smoke.tests.rest;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.seedstack.smoke.fixtures.rest.HelloResource;
import org.seedstack.smoke.tests.BaseSmokeTest;

import javax.ws.rs.core.MediaType;

import static com.jayway.restassured.RestAssured.expect;

/**
 * Check if {@link HelloResource} is ok.
 *
 * @author thierry.bouvet@mpsa.com
 */
public class BasicRestIT extends BaseSmokeTest {
    @Test
    public void testHelloResource() throws Exception {
        expect()
                .statusCode(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body(Matchers.equalTo("hello"))
                .when()
                .get(url("/hello"));
    }
}
