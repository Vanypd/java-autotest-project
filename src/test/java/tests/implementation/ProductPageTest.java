package tests.implementation;

import org.junit.jupiter.api.DisplayName;
import org.openqa.selenium.By;

@DisplayName("Страница товара")
public class ProductPageTest {
    public static final By PRODUCT_TITLE_LOCATOR = By.className("product_title");
    public static final By PRODUCT_NEW_PRICE_LOCATOR = By.cssSelector(".summary .price ins bdi");
}
