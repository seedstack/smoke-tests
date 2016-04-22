/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.smoke.tests;

import org.seedstack.smoke.TestResults;

import javax.ws.rs.core.MediaType;

import static com.jayway.restassured.RestAssured.expect;
import static org.assertj.core.api.Assertions.assertThat;

public class BaseSmokeTest {
    private final String baseUrl;

    public BaseSmokeTest() {
        String port = System.getProperty("docker.port");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        String host = System.getProperty("docker.host");
        if (host == null || host.isEmpty()) {
            host = "localhost";
        }

        baseUrl = "http://" + host + ":" + Integer.parseInt(port) + "/smoke-tests/";
    }

    protected String url(String path) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        return baseUrl + path;
    }

    protected TestResults run(Class<?> testClass) {
        return expect()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(url("/tests/" + testClass.getCanonicalName()))
                .as(TestResults.class);
    }

    protected TestResults expectSuccess(Class<?> testClass) {
        TestResults result = run(testClass);
        assertThat(result.getFailureCount()).as("Test %s failed", testClass.getCanonicalName()).isEqualTo(0);
        return result;
    }
}
