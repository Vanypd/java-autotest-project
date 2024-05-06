package tests.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import tests.DefaultTest;

@DisplayName("Страница восстановления пароля")
public class PasswordRecoveryPageTest extends DefaultTest {

    public static final String URL = "https://intershop5.skillbox.ru/my-account/lost-password/";

    // LOCATORS //

    public static final By RECOVERY_FORM_LOCATOR = By.className("woocommerce-ResetPassword");
    public static final By RECOVERY_INPUT_LOCATOR = By.id("user_login");
    public static final By SUBMIT_RECOVERY_BUTTON_LOCATOR = By.cssSelector("button[value='Reset password']");

    // CONSTRUCTOR //

    public PasswordRecoveryPageTest() {
        super(URL);
    }

    // TESTS //

    @ParameterizedTest(name = "{1}")
    @DisplayName("Проверка успешного восстановления пароля")
    @CsvSource({
        "NAME, Используя имя пользователя",
        "EMAIL, Используя адрес электронной почты"
    })
    public void successfulPasswordRecovery(String choice, String displayedName) {

        if (choice.equals("NAME")) {
            driver.findElement(RECOVERY_INPUT_LOCATOR).sendKeys(AuthorizationPageTest.USERNAME);
        }

        if (choice.equals("EMAIL")) {
            driver.findElement(RECOVERY_INPUT_LOCATOR).sendKeys(AuthorizationPageTest.EMAIL);
        }

        driver.findElement(SUBMIT_RECOVERY_BUTTON_LOCATOR).click();
        boolean formIsNotDisplayed = driver.findElements(RECOVERY_FORM_LOCATOR).isEmpty();
        String errorMessage = "Форма восстановления пароля до сих пор отображена на странице";
        Assertions.assertTrue(formIsNotDisplayed, errorMessage);
    }


    @Test
    @DisplayName("Попытка отправки незаполненной формы")
    public void emptyPasswordRecovery() {
        driver.findElement(SUBMIT_RECOVERY_BUTTON_LOCATOR).click();
        boolean formIsDisplayed = !driver.findElements(RECOVERY_FORM_LOCATOR).isEmpty();
        String errorMessage = "Форма восстановления пароля не отображена на странице";
        Assertions.assertTrue(formIsDisplayed, errorMessage);
    }
}
