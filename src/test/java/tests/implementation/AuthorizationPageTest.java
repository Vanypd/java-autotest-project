package tests.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.DefaultTest;
import tests.config.pojo.Credentials;

import java.util.List;

@DisplayName("Страница авторизации")
public class AuthorizationPageTest extends DefaultTest {

    public static final String URL = "https://intershop5.skillbox.ru/my-account/";

    // LOCATORS //

    public static final By LOGIN_FORM_LOCATOR = By.className("woocommerce-form-login");
    public static final By NAME_OR_EMAIL_INPUT_LOCATOR = By.id("username");
    public static final By PASSWORD_INPUT_LOCATOR = By.id("password");
    public static final By REMEMBER_ME_CHECKBOX_LOCATOR = By.name("rememberme");
    public static final By LOGIN_BUTTON_LOCATOR = By.name("login");

    public static final By TO_REGISTRATION_BUTTON_LOCATOR = By.className("custom-register-button");
    public static final By LOST_PASSWORD_BUTTON_LOCATOR = By.cssSelector(".lost_password a");

    // DATA //

    public static final String USERNAME = Credentials.getInstance().getUsername();
    public static final String EMAIL = Credentials.getInstance().getEmail();
    public static final String PASSWORD = Credentials.getInstance().getPassword();

    // CONSTRUCTOR //

    public AuthorizationPageTest() {
        super(URL);
    }

    // TESTS //

    @Test
    @DisplayName("Переход на страницу регистрации")
    public void transitionToRegistrationPage() {
        driver.findElement(TO_REGISTRATION_BUTTON_LOCATOR).click();
        String errorMessage = "URL страницы, отличается от URL страницы регистрации";
        Assertions.assertEquals(RegistrationPageTest.URL, driver.getCurrentUrl(), errorMessage);
    }


    @Test
    @DisplayName("Переход на страницу восстановления пароля")
    public void transitionToPasswordRecoveryPage() {
        driver.findElement(LOST_PASSWORD_BUTTON_LOCATOR).click();
        String errorMessage = "URL страницы, отличается от URL страницы восстановления пароля";
        Assertions.assertEquals(PasswordRecoveryPageTest.URL, driver.getCurrentUrl(), errorMessage);
    }


    @ParameterizedTest(name = "{1}")
    @DisplayName("Проверка успешной авторизации")
    @CsvSource({
            "NAME, Используя имя пользователя",
            "EMAIL, Используя адрес электронной почты"
    })
    public void authorization(String choice, String displayedName) {
        String enteringData = "";

        switch (choice) {
            case "NAME" -> enteringData = USERNAME;
            case "EMAIL" -> enteringData = EMAIL;
        }

        testFixture.login(enteringData);
    }


    @ParameterizedTest(name = "{1}")
    @DisplayName("Попытка авторизации")
    @CsvSource({
            "NAME&EMAIL, Без ввода имени или электронной почты",
            "PASSWORD, Без ввода пароля"
    })
    public void authorizationWithoutUsernameOrEmail(String choice, String displayedName) {

        if (!choice.equals("NAME&EMAIL")) {
            driver.findElement(NAME_OR_EMAIL_INPUT_LOCATOR).sendKeys(USERNAME);
        }

        if (!choice.equals("PASSWORD")) {
            driver.findElement(PASSWORD_INPUT_LOCATOR).sendKeys(PASSWORD);
        }

        driver.findElement(LOGIN_BUTTON_LOCATOR).click();
        List<WebElement> loginForm = driver.findElements(LOGIN_FORM_LOCATOR);
        String errorMessage = "Авторизация пройдена, форма авторизации отсутствует на странице";
        Assertions.assertFalse(loginForm.isEmpty(), errorMessage);
    }
}
