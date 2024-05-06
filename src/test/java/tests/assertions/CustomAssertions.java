package tests.assertions;

import org.junit.jupiter.api.AssertionFailureBuilder;

public class CustomAssertions {

    private CustomAssertions() {}

    public static void assertContainsIgnoreCase(String expected, String actual, String message) {
        if (!actual.toLowerCase().contains(expected.toLowerCase())) {
            AssertionFailureBuilder.assertionFailure().message(message).expected(expected).actual(actual).buildAndThrow();
        }
    }


    public static void assertEqualsIgnoreCase(String expected, String actual, String message) {
        if (!actual.equalsIgnoreCase(expected)) {
            AssertionFailureBuilder.assertionFailure().message(message).expected(expected).actual(actual).buildAndThrow();
        }
    }
}
