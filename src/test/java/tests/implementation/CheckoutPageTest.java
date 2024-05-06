package tests.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import tests.DefaultTest;
import tests.config.pojo.OrderDetails;

import java.util.List;

@DisplayName("Страница оформления заказа")
public class CheckoutPageTest extends DefaultTest {

    public static final String URL = "https://intershop5.skillbox.ru/checkout/";

    // LOCATORS //

    // Обязательные поля
    public static final By FIRST_NAME_INPUT_LOCATOR = By.id("billing_first_name");
    public static final By LAST_NAME_INPUT_LOCATOR = By.id("billing_last_name");
    public static final By COUNTRY_SELECT_LOCATOR = By.id("select2-billing_country-container");
    public static final By COUNTRY_SEARCH_INPUT_LOCATOR = By.className("select2-search__field");
    public static final By ADDRESS_INPUT_LOCATOR = By.id("billing_address_1");
    public static final By CITY_INPUT_LOCATOR = By.id("billing_city");
    public static final By STATE_INPUT_LOCATOR = By.id("billing_state");
    public static final By POSTCODE_INPUT_LOCATOR = By.id("billing_postcode");
    public static final By PHONE_INPUT_LOCATOR = By.id("billing_phone");
    public static final By EMAIL_INPUT_LOCATOR = By.id("billing_email");

    // Оплата
    public static final By BANK_TRANSFER_RADIOBUTTON_LOCATOR = By.id("payment_method_bacs");
    public static final By AFTER_DELIVERY_RADIOBUTTON_LOCATOR = By.id("payment_method_cod");
    public static final By PLACING_ORDER_BUTTON_LOCATOR = By.id("place_order");

    // Информация после оплаты
    public static final By SUCCESSFUL_ORDER_TITLE_LOCATOR = By.xpath("//h2[text()='Заказ получен']");

    // VALID DATA //

    private final String firstName = OrderDetails.getInstance().getFirstName();
    private final String lastName = OrderDetails.getInstance().getLastName();
    private final String country = OrderDetails.getInstance().getCountry();
    private final String address = OrderDetails.getInstance().getAddress();
    private final String city = OrderDetails.getInstance().getCity();
    private final String state = OrderDetails.getInstance().getState();
    private final String postcode = OrderDetails.getInstance().getPostcode();
    private final String phone = OrderDetails.getInstance().getPhone();
    private final String email = AuthorizationPageTest.EMAIL;

    // CONSTRUCTOR //

    public CheckoutPageTest() {
        super(URL);
    }

    // TESTS //

    @ParameterizedTest(name = "{1}")
    @DisplayName("Проверка успешного оформления заказа")
    @CsvSource({
            "BANK_TRANSFER, Выбрав прямой банковский перевод",
            "AFTER_DELIVERY, Выбрав оплату после доставки"
    })
    public void successfulPlacingOrder(String selectedPayment, String displayedName) {
        testFixture.addProductToBasket();
        testFixture.login(AuthorizationPageTest.USERNAME);
        scriptExecutor.navigateBack();

        driver.findElement(FIRST_NAME_INPUT_LOCATOR).clear();
        driver.findElement(FIRST_NAME_INPUT_LOCATOR).sendKeys(firstName);
        driver.findElement(LAST_NAME_INPUT_LOCATOR).clear();
        driver.findElement(LAST_NAME_INPUT_LOCATOR).sendKeys(lastName);

        driver.findElement(COUNTRY_SELECT_LOCATOR).click();
        driver.findElement(COUNTRY_SEARCH_INPUT_LOCATOR).sendKeys(country);
        driver.findElement(COUNTRY_SEARCH_INPUT_LOCATOR).sendKeys(Keys.ENTER);

        driver.findElement(ADDRESS_INPUT_LOCATOR).clear();
        driver.findElement(ADDRESS_INPUT_LOCATOR).sendKeys(address);
        driver.findElement(CITY_INPUT_LOCATOR).clear();
        driver.findElement(CITY_INPUT_LOCATOR).sendKeys(city);
        driver.findElement(STATE_INPUT_LOCATOR).clear();
        driver.findElement(STATE_INPUT_LOCATOR).sendKeys(state);
        driver.findElement(POSTCODE_INPUT_LOCATOR).clear();
        driver.findElement(POSTCODE_INPUT_LOCATOR).sendKeys(postcode);
        driver.findElement(PHONE_INPUT_LOCATOR).clear();
        driver.findElement(PHONE_INPUT_LOCATOR).sendKeys(phone);

        if (selectedPayment.equals("BANK_TRANSFER")) {
            WebElement bankTransferPaymentRadiobutton = driver.findElement(BANK_TRANSFER_RADIOBUTTON_LOCATOR);

            if (!bankTransferPaymentRadiobutton.isSelected()) {
                bankTransferPaymentRadiobutton.click();
            }
        }

        if (selectedPayment.equals("AFTER_DELIVERY")) {
            WebElement afterDeliveryPaymentRadiobutton = driver.findElement(AFTER_DELIVERY_RADIOBUTTON_LOCATOR);

            if (!afterDeliveryPaymentRadiobutton.isSelected()) {
                afterDeliveryPaymentRadiobutton.click();
            }
        }

        driver.findElement(PLACING_ORDER_BUTTON_LOCATOR).click();
        List<WebElement> successTitle = driver.findElements(SUCCESSFUL_ORDER_TITLE_LOCATOR);
        String titleNotExistMessage = "Заголовок об успешном получении заказа не отобразился на странице";
        Assertions.assertFalse(successTitle.isEmpty(), titleNotExistMessage);
    }


    @ParameterizedTest(name = "{1}")
    @DisplayName("Попытка оформления заказа")
    @CsvSource({
            "FIRSTNAME, Без ввода имени",
            "LASTNAME, Без ввода фамилии",
            "COUNTRY, Без ввода страны",
            "ADDRESS, Без ввода адреса",
            "CITY, Без ввода города",
            "STATE, Без ввода области",
            "POSTCODE, Без ввода почтового индекса",
            "PHONE, Без ввода номера телефона",
            "EMAIL, Без ввода адреса электронной почты"

    })
    public void failedPlacingOrder(String choice, String displayedName) {
        testFixture.addProductToBasket();
        testFixture.login(AuthorizationPageTest.USERNAME);
        scriptExecutor.navigateBack();

        driver.findElement(FIRST_NAME_INPUT_LOCATOR).clear();
        driver.findElement(LAST_NAME_INPUT_LOCATOR).clear();
        driver.findElement(ADDRESS_INPUT_LOCATOR).clear();
        driver.findElement(CITY_INPUT_LOCATOR).clear();
        driver.findElement(STATE_INPUT_LOCATOR).clear();
        driver.findElement(POSTCODE_INPUT_LOCATOR).clear();
        driver.findElement(PHONE_INPUT_LOCATOR).clear();
        driver.findElement(EMAIL_INPUT_LOCATOR).clear();

        if (!choice.equals("FIRSTNAME")) {
            driver.findElement(FIRST_NAME_INPUT_LOCATOR).sendKeys(firstName);
        }

        if (!choice.equals("LASTNAME")) {
            driver.findElement(LAST_NAME_INPUT_LOCATOR).sendKeys(lastName);
        }

        if (choice.equals("COUNTRY")) {
            driver.findElement(COUNTRY_SELECT_LOCATOR).click();
            driver.findElement(COUNTRY_SEARCH_INPUT_LOCATOR).sendKeys("Select a country / region");
            driver.findElement(COUNTRY_SEARCH_INPUT_LOCATOR).sendKeys(Keys.ENTER);
        } else {
            driver.findElement(COUNTRY_SELECT_LOCATOR).click();
            driver.findElement(COUNTRY_SEARCH_INPUT_LOCATOR).sendKeys(country);
            driver.findElement(COUNTRY_SEARCH_INPUT_LOCATOR).sendKeys(Keys.ENTER);
        }

        if (!choice.equals("ADDRESS")) {
            driver.findElement(ADDRESS_INPUT_LOCATOR).sendKeys(address);
        }

        if (!choice.equals("CITY")) {
            driver.findElement(CITY_INPUT_LOCATOR).sendKeys(city);
        }

        if (!choice.equals("STATE")) {
            driver.findElement(STATE_INPUT_LOCATOR).sendKeys(state);
        }

        if (!choice.equals("POSTCODE")) {
            driver.findElement(POSTCODE_INPUT_LOCATOR).sendKeys(postcode);
        }

        if (!choice.equals("PHONE")) {
            driver.findElement(PHONE_INPUT_LOCATOR).sendKeys(phone);
        }

        if (!choice.equals("EMAIL")) {
            driver.findElement(EMAIL_INPUT_LOCATOR).sendKeys(email);
        }

        driver.findElement(PLACING_ORDER_BUTTON_LOCATOR).click();
        List<WebElement> successTitle = driver.findElements(SUCCESSFUL_ORDER_TITLE_LOCATOR);
        String titleExistMessage = "Заказ успешно был оформлен, заголовок отобразился";
        Assertions.assertTrue(successTitle.isEmpty(), titleExistMessage);
    }
}
