/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.smoke;

import java.util.List;

public class TestResults {
    private int ignoreCount;
    private int failureCount;
    private int runCount;
    private long runTime;
    private List<TestFailure> failures;

    public int getIgnoreCount() {
        return ignoreCount;
    }

    public void setIgnoreCount(int ignoreCount) {
        this.ignoreCount = ignoreCount;
    }

    public int getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(int failureCount) {
        this.failureCount = failureCount;
    }

    public int getRunCount() {
        return runCount;
    }

    public void setRunCount(int runCount) {
        this.runCount = runCount;
    }

    public long getRunTime() {
        return runTime;
    }

    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }

    public List<TestFailure> getFailures() {
        return failures;
    }

    public void setFailures(List<TestFailure> failures) {
        this.failures = failures;
    }

    public static class TestFailure {
        private String message;
        private String testHeader;
        private String trace;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTestHeader() {
            return testHeader;
        }

        public void setTestHeader(String testHeader) {
            this.testHeader = testHeader;
        }

        public String getTrace() {
            return trace;
        }

        public void setTrace(String trace) {
            this.trace = trace;
        }
    }
}
