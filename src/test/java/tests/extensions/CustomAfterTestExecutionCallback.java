package tests.extensions;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import tests.DefaultTest;

public class CustomAfterTestExecutionCallback implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        boolean testFailed = context.getExecutionException().isPresent();

        if (testFailed) {
            DefaultTest.setLastFailedTestContext(context);
        }
    }
}
