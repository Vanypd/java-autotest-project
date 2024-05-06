package tests.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.DefaultTest;

import java.util.List;
import java.util.Random;

@DisplayName("Страница регистрации")
public class RegistrationPageTest extends DefaultTest {

    public static final String URL = "https://intershop5.skillbox.ru/register/";

    // LOCATORS //

    public static final By REGISTRATION_FORM_LOCATOR = By.className("woocommerce-form-register");
    public static final By NAME_INPUT_LOCATOR = By.id("reg_username");
    public static final By EMAIL_INPUT_LOCATOR = By.id("reg_email");
    public static final By PASSWORD_INPUT_LOCATOR = By.id("reg_password");
    public static final By SUBMIT_BUTTON_LOCATOR = By.name("register");

    // VALID DATA //

    private String validName;
    private String validEmail;
    private String validPassword;

    // CONSTRUCTOR //

    public RegistrationPageTest() {
        super(URL);
    }

    // INTERCEPTORS //

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        validName = "Tester" + new Random().nextInt(Integer.MAX_VALUE);
        validEmail = "T" + new Random().nextInt(10000000) + "@mail.ru";
        validPassword = "!Password1255";
    }

    // TESTS //

    @Test
    @DisplayName("Регистрация используя валидные данные")
    public void validRegistration() {
        driver.findElement(NAME_INPUT_LOCATOR).sendKeys(validName);
        driver.findElement(EMAIL_INPUT_LOCATOR).sendKeys(validEmail);
        driver.findElement(PASSWORD_INPUT_LOCATOR).sendKeys(validPassword);
        driver.findElement(SUBMIT_BUTTON_LOCATOR).click();

        List<WebElement> registrationForm = driver.findElements(REGISTRATION_FORM_LOCATOR);
        String errorMessage = "Регистрация не пройдена, форма регистрации всё ещё отображена на странице";
        Assertions.assertTrue(registrationForm.isEmpty(), errorMessage);
    }


    @Test
    @DisplayName("Регистрация без ввода данных")
    public void emptyRegistration() {
        driver.findElement(SUBMIT_BUTTON_LOCATOR).click();
        List<WebElement> registrationForm = driver.findElements(REGISTRATION_FORM_LOCATOR);
        String errorMessage = "Регистрация пройдена, форма регистрации не отображена на странице";
        Assertions.assertFalse(registrationForm.isEmpty(), errorMessage);
    }


    @ParameterizedTest(name = "{1}")
    @DisplayName("Регистрация без ввода")
    @CsvSource({
            "NAME, Имени пользователя",
            "EMAIL, Электронной почты",
            "PASSWORD, Пароля"
    })
    public void failedRegistration(String choice, String displayedName) {

        if (!choice.equals("NAME")) {
            driver.findElement(NAME_INPUT_LOCATOR).sendKeys(validName);
        }

        if (!choice.equals("EMAIL")) {
            driver.findElement(EMAIL_INPUT_LOCATOR).sendKeys(validEmail);
        }

        if (!choice.equals("PASSWORD")) {
            driver.findElement(PASSWORD_INPUT_LOCATOR).sendKeys(validPassword);
        }

        driver.findElement(SUBMIT_BUTTON_LOCATOR).click();
        List<WebElement> registrationForm = driver.findElements(REGISTRATION_FORM_LOCATOR);
        String errorMessage = "Регистрация пройдена, форма регистрации не отображена на странице";
        Assertions.assertFalse(registrationForm.isEmpty(), errorMessage);
    }
}
