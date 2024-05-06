package tests.utils;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.implementation.AuthorizationPageTest;
import tests.implementation.MainPageTest;

import java.util.List;

import static tests.implementation.AuthorizationPageTest.*;
import static tests.implementation.MainPageTest.*;

public class TestFixture {

    private final ScriptExecutor scriptExecutor;
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;


    public TestFixture(ScriptExecutor scriptExecutor) {
        this.scriptExecutor = scriptExecutor;
        this.driver = scriptExecutor.getSeleniumHandler().getDriver();
        this.wait = scriptExecutor.getSeleniumHandler().getWait();
        this.actions = scriptExecutor.getSeleniumHandler().getActions();
    }


    /**
     * Метод при необходимости переходит на главную страницу, а затем добавляет в корзину
     * первый видимый продукт из блока "Распродажа".
     */
    public void addProductToBasket() {

        if (!driver.getCurrentUrl().equals(MainPageTest.URL)) {
            driver.navigate().to(MainPageTest.URL);
        }

        WebElement productItem = driver.findElement(DISPLAYED_SALE_PRODUCTS_LOCATOR);
        WebElement productImageElement = driver.findElement(DISPLAYED_SALE_PRODUCT_IMAGES_LOCATOR);
        scriptExecutor.scrollToElement(productItem);
        wait.until(webDriver -> !productItem.getDomAttribute("class").contains("animated"));
        scriptExecutor.waitForTextPresenceInElement(DISPLAYED_SALE_PRODUCT_NAMES_LOCATOR, DISPLAYED_SALE_PRODUCT_NEW_PRICES_LOCATOR);

        actions.moveToElement(productImageElement).perform();
        WebElement addToBasketButton = driver.findElement(DISPLAYED_SALE_PRODUCT_BASKET_BUTTONS_LOCATOR);
        addToBasketButton.click();
        wait.until(webDriver -> addToBasketButton.getDomAttribute("class").contains("added"));
    }


    /**
     * Метод выполняющий авторизацию на сайте
     * @param usernameOrEmail Строка имени пользователя либо адреса электронной почты
     */
    public void login(String usernameOrEmail) {

        if (!driver.getCurrentUrl().equals(AuthorizationPageTest.URL)) {
            driver.navigate().to(AuthorizationPageTest.URL);
        }

        String authorizationErrorMessage = "Форма авторизации до сих пор отображена на странице";
        String checkboxSelectErrorMessage = "Чекбокс не выделен";

        driver.findElement(NAME_OR_EMAIL_INPUT_LOCATOR).sendKeys(usernameOrEmail);
        driver.findElement(PASSWORD_INPUT_LOCATOR).sendKeys(PASSWORD);
        WebElement checkbox = driver.findElement(REMEMBER_ME_CHECKBOX_LOCATOR);
        checkbox.click();
        Assertions.assertTrue(checkbox.isSelected(), checkboxSelectErrorMessage);

        driver.findElement(LOGIN_BUTTON_LOCATOR).click();
        List<WebElement> loginForm = driver.findElements(LOGIN_FORM_LOCATOR);
        Assertions.assertTrue(loginForm.isEmpty(), authorizationErrorMessage);
    }
}