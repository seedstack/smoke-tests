/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 2 sept. 2015
 */
package org.seedstack.smoke.utils;

import org.json.JSONObject;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.seedstack.smoke.tests.BaseSmokeTest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test listener to create a JSON file with tests result.
 *
 * @author thierry.bouvet@mpsa.com
 */
public class JsonWriterListener extends RunListener {
    private Map<String, Map<String, Result>> results = new HashMap<String, Map<String, Result>>();

    @Override
    public void testFinished(Description description) throws Exception {
        putTest(description, true, null);
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        putTest(failure.getDescription(), false, failure.getMessage());
    }

    private void putTest(Description description, boolean success, String comment) {
        if (BaseSmokeTest.class.isAssignableFrom(description.getTestClass())) {
            String key = getTestGroup(description);
            Map<String, Result> tests;
            if ((tests = results.get(key)) == null) {
                results.put(key, tests = new HashMap<String, Result>());
            }
            if (!tests.containsKey(description.getMethodName())) {
                Result result = new Result(success);
                if (comment != null) {
                    result.setComment(comment);
                }
                tests.put(description.getMethodName(), result);
            }
        }
    }

    private String getTestGroup(Description description) {
        return description.getTestClass().getPackage().getName().substring(BaseSmokeTest.class.getPackage().getName().length() + 1);
    }

    @Override
    public void testRunFinished(org.junit.runner.Result result) throws Exception {
        final String serverName = System.getProperty("server.name");
        final String serverVersion = System.getProperty("server.version");
        generateJSONFile(result, serverName == null ? "unknown" : serverName, serverVersion);
    }

    private void generateJSONFile(org.junit.runner.Result result, String serverName, String serverVersion) throws IOException {
        JSONObject obj = new JSONObject();
        obj.putOnce("serverName", serverName);
        obj.putOnce("serverVersion", serverVersion);
        if (result.wasSuccessful()) {
            obj.putOnce("result", "SUCCESS");
        } else {
            obj.putOnce("result", "FAILURE");
        }

        obj.putOnce("results", results);

        File directory = new File("target/results");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new RuntimeException("Unable to write results to " + directory.getAbsolutePath());
            }
        }

        File resultFile = new File(directory, String.format("%s.json", serverName.toLowerCase()));
        FileWriter file = new FileWriter(resultFile);
        file.write(obj.toString());
        file.close();
    }

    public static class Result {
        private boolean success;
        private String comment;

        private Result(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }
}
