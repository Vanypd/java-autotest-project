package tests;

import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.extensions.CustomAfterTestExecutionCallback;
import tests.handlers.SeleniumHandler;
import tests.utils.TestFixture;
import tests.utils.ScriptExecutor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;


@ExtendWith(CustomAfterTestExecutionCallback.class)
public abstract class DefaultTest {

    protected SeleniumHandler seleniumHandler;
    protected ScriptExecutor scriptExecutor;
    protected TestFixture testFixture;


    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected Actions actions;

    private final String url;

    @Setter
    private static ExtensionContext lastFailedTestContext;

    // CONSTRUCTOR //

    public DefaultTest(String url) {
        this.url = url;


    }

    // INTERCEPTORS //

    @BeforeEach
    public void setUp() {
        seleniumInitialization();
        scriptExecutor = new ScriptExecutor(seleniumHandler, url);
        testFixture = new TestFixture(scriptExecutor);
        driver.manage().window().maximize();
        driver.navigate().to(url);
    }


    @AfterEach
    public void tearDown() {
        makeScreenshotOnTestFail(driver, lastFailedTestContext);
        driver.quit();
    }

    // PRIVATE METHODS //

    private void seleniumInitialization() {
        seleniumHandler = new SeleniumHandler();
        driver = seleniumHandler.getDriver();
        wait = seleniumHandler.getWait();
        js = seleniumHandler.getJs();
        actions = seleniumHandler.getActions();
    }

    // STATIC //

    private static void makeScreenshotOnTestFail(WebDriver driver, ExtensionContext context) {

        if (lastFailedTestContext == null) {
            return;
        }

        File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        String failedTestName = context.getTestMethod().map(Method::getName).orElse("Unknown");
        String path = "src/test/resources/errors-screenshots/";
        String fileName = "failed-" + failedTestName + "-screenshot.png";
        tryToSaveScreenshot(screenshot, path + fileName);
        lastFailedTestContext = null;
    }


    private static void tryToSaveScreenshot(File screenshot, String path) {
        try {
            FileUtils.copyFile(screenshot, new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
