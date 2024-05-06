package tests.handlers;

import lombok.Getter;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


@Getter
public class SeleniumHandler {

    private static final int SECONDS_OF_IMPLICIT_WAIT = 5;
    private static final int SECONDS_OF_EXPLICIT_WAIT = 10;

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;
    private final Actions actions;


    public SeleniumHandler() {
        this(new ChromeDriver());
    }


    public SeleniumHandler(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_OF_EXPLICIT_WAIT));
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(SECONDS_OF_IMPLICIT_WAIT));
    }
}
