package tests.implementation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import tests.DefaultTest;
import tests.utils.TestUtils;

@DisplayName("Страница корзины")
public class BasketPageTest extends DefaultTest {

    public static final String URL = "https://intershop5.skillbox.ru/cart/";

    // LOCATORS //

    // Пустая корзина
    public static final By EMPTY_CART_MESSAGE_BLOCK_LOCATOR = By.className("cart-empty");
    public static final By ITEM_RESTORE_BUTTON_LOCATOR = By.className("restore-item");

    // Продукты
    public static final By ITEMS_LOCATOR = By.className("woocommerce-cart-form__cart-item");
    public static final By ITEM_DELETE_BUTTON_LOCATOR = By.className("remove");
    public static final By ITEM_NAMES_LOCATOR = By.cssSelector(".cart_item .product-name a");
    public static final By ITEM_PRICES_LOCATOR = By.cssSelector(".cart_item .product-price bdi");
    public static final By ITEM_QUANTITIES_INPUT_LOCATOR = By.cssSelector(".input-text.qty");
    public static final By ITEM_TOTAL_COSTS_LOCATOR = By.cssSelector(".product-subtotal bdi");

    // Купоны
    public static final By COUPON_CODE_INPUT_LOCATOR = By.name("coupon_code");
    public static final By APPLY_CODE_BUTTON_LOCATOR = By.name("apply_coupon");

    // Итоги
    public static final By REMOVE_COUPON_BUTTON_LOCATOR = By.className("woocommerce-remove-coupon");
    public static final By CART_DISCOUNT_LINE_LOCATOR = By.className("cart-discount");
    public static final By ORDER_TOTAL_COST_LOCATOR = By.cssSelector(".order-total bdi");
    public static final By CHECKOUT_BUTTON_LOCATOR = By.className("checkout-button");

    // PAGE URL //

    public BasketPageTest() {
        super(URL);
    }

    // TESTS //

    @Test
    @DisplayName("Удаление и возврат товара")
    public void deletingAndRestoreProduct() {
        testFixture.addProductToBasket();
        scriptExecutor.navigateBack();
        driver.findElement(ITEM_DELETE_BUTTON_LOCATOR).click();
        wait.until(driver -> driver.findElements(ITEMS_LOCATOR).isEmpty());

        String itemIsDisplayedError = "Корзина содержит товары";
        String blockNotDisplayedError = "Блок с уведомлением что корзина пуста не отобразился";
        Assertions.assertTrue(driver.findElements(ITEMS_LOCATOR).isEmpty(), itemIsDisplayedError);
        Assertions.assertTrue(driver.findElement(EMPTY_CART_MESSAGE_BLOCK_LOCATOR).isDisplayed(), blockNotDisplayedError);

        driver.findElement(ITEM_RESTORE_BUTTON_LOCATOR).click();
        wait.until(driver -> !driver.findElements(ITEMS_LOCATOR).isEmpty());

        String itemIsNotDisplayedError = "Корзина пуста";
        Assertions.assertFalse(driver.findElements(ITEMS_LOCATOR).isEmpty(), itemIsNotDisplayedError);
    }


    @Test
    @DisplayName("Изменение количества товара в корзине")
    public void quantityChanging() {
        testFixture.addProductToBasket();
        scriptExecutor.navigateBack();

        WebElement input = driver.findElement(ITEM_QUANTITIES_INPUT_LOCATOR);
        String totalCountBaseValue = driver.findElement(ITEM_PRICES_LOCATOR).getText();
        int quantity = 3;
        input.clear();
        input.sendKeys(String.valueOf(quantity));
        input.sendKeys(Keys.ENTER);
        wait.until(driver -> !driver.findElement(ITEM_TOTAL_COSTS_LOCATOR).getText().equals(totalCountBaseValue));

        float priceFloat = TestUtils.parsePriceToFloat(driver.findElement(ITEM_PRICES_LOCATOR).getText());
        float totalPriceFloat = TestUtils.parsePriceToFloat(driver.findElement(ITEM_TOTAL_COSTS_LOCATOR).getText());
        String errorMessage = "Общая стоимость не соответствует цене умноженной на количество";
        Assertions.assertEquals(totalPriceFloat, priceFloat * quantity, errorMessage);
    }


    @Test
    @DisplayName("Применение скидочного купона")
    public void applyingCoupon() {
        testFixture.addProductToBasket();
        scriptExecutor.navigateBack();

        String coupon = "sert500";
        int couponDiscount = 500;
        WebElement productTotalCost = driver.findElement(ITEM_TOTAL_COSTS_LOCATOR);
        float priceBeforeDiscount = TestUtils.parsePriceToFloat(productTotalCost.getText());

        driver.findElement(COUPON_CODE_INPUT_LOCATOR).sendKeys(coupon);
        driver.findElement(APPLY_CODE_BUTTON_LOCATOR).click();
        wait.until(driver -> !productTotalCost.getText().equals(driver.findElement(ITEM_TOTAL_COSTS_LOCATOR).getText()));
        WebElement cartTotalCost = driver.findElement(ORDER_TOTAL_COST_LOCATOR);
        float priceAfterDiscount = TestUtils.parsePriceToFloat(cartTotalCost.getText());

        String errorMessage = "Цена после применения купона отличается от ожидаемой";
        Assertions.assertEquals(priceBeforeDiscount - couponDiscount, priceAfterDiscount, errorMessage);
    }


    @Test
    @DisplayName("Удаление скидочного купона")
    public void deletingCoupon() {
        testFixture.addProductToBasket();
        scriptExecutor.navigateBack();

        String coupon = "sert500";
        String baseTotalCost = driver.findElement(ORDER_TOTAL_COST_LOCATOR).getText();
        driver.findElement(COUPON_CODE_INPUT_LOCATOR).sendKeys(coupon);
        driver.findElement(APPLY_CODE_BUTTON_LOCATOR).click();
        wait.until(driver -> !driver.findElement(ORDER_TOTAL_COST_LOCATOR).getText().equals(baseTotalCost));
        driver.findElement(REMOVE_COUPON_BUTTON_LOCATOR).click();
        wait.until(driver -> driver.findElement(ORDER_TOTAL_COST_LOCATOR).getText().equals(baseTotalCost));

        String errorMessage = "Итоги всё ещё содержат строку со скидкой";
        Assertions.assertTrue(driver.findElements(CART_DISCOUNT_LINE_LOCATOR).isEmpty(), errorMessage);
    }


    @Test
    @DisplayName("Переход на страницу оформления заказа")
    public void transitionToCheckoutPage() {
        testFixture.addProductToBasket();
        scriptExecutor.navigateBack();
        driver.findElement(CHECKOUT_BUTTON_LOCATOR).click();
        Assertions.assertEquals(CheckoutPageTest.URL, driver.getCurrentUrl());
    }
}
