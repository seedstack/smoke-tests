package org.seedstack.smoke.utils;

public final class NameUtils {
    private NameUtils() {
    }

    public static String humanizeCamelCase(String s) {
        String result = s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
        return result.substring(0, 1) + result.substring(1).toLowerCase();
    }

    public static String humanizeTestMethod(String methodName) {
        if (methodName.startsWith("test")) {
            methodName = methodName.substring(4);
        }
        return humanizeCamelCase(methodName);
    }

    public static String humanizeTestClass(Class<?> testClass) {
        String name = testClass.getSimpleName();
        if (name.endsWith("IT")) {
            name = name.substring(0, name.length() - 2);
        }

        return humanizeCamelCase(name);
    }
}
