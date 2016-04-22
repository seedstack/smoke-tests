/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
    public void testSimpleResource() throws Exception {
        expect()
                .statusCode(200)
                .contentType(MediaType.TEXT_PLAIN)
                .body(Matchers.equalTo("hello"))
                .when()
                .get(url("/hello"));
    }

    @Test
    public void testJsonResource() throws Exception {
        expect()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Matchers.equalTo("{\"key\":\"value\"}"))
                .when()
                .get(url("/json"));
    }
}
