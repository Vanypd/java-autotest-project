package tests.utils;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.handlers.SeleniumHandler;

public class ScriptExecutor {

    @Getter
    private final SeleniumHandler seleniumHandler;
    private final String url;
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;


    public ScriptExecutor(SeleniumHandler seleniumHandler, String url) {
        this.seleniumHandler = seleniumHandler;
        driver = seleniumHandler.getDriver();
        wait = seleniumHandler.getWait();
        js = seleniumHandler.getJs();
        this.url = url;
    }


    /**
     * Метод выполняет навигацию на основную страницу теста. Основной страницей является
     * url переданный в параметры super() конструктора наследника DefaultTest.
     */
    public void navigateBack() {
        seleniumHandler.getDriver().navigate().to(url);
    }


    /**
     * Метод прокручивает страницу до переданного в параметры элемента.
     * @param element WebElement
     */
    public void scrollToElement(WebElement element) {
        js.executeScript("arguments[0].scrollIntoView();", element);
    }


    /**
     * Метод находит элементы по переданным в аргументы локаторам, а затем ожидает появление текста
     * в этих элементах.
     * @param locators By...
     */
    public void waitForTextPresenceInElement(By... locators) {
        for (By locator : locators)
            waitForTextPresenceInElement(driver.findElement(locator));
    }


    /**
     * Метод выполняет ожидание появления текста переданных в аргрументы элементов.
     * @param elements WebElement...
     */
    public void waitForTextPresenceInElement(WebElement... elements) {
        for (WebElement element : elements)
            wait.until(webDriver -> element.isDisplayed() && !element.getText().isEmpty());
    }
}
