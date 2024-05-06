package tests.implementation;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import tests.DefaultTest;
import tests.assertions.CustomAssertions;

@DisplayName("Главная страница")
public class MainPageTest extends DefaultTest {

    public static final String URL = "https://intershop5.skillbox.ru";

    // LOCATORS //

    // Блок категорий
    public static final By CATEGORY_ITEMS_LOCATOR = By.cssSelector("#promo-section1 a");
    public static final By CATEGORY_ITEM_TITLES_LOCATOR = By.cssSelector("#promo-section1 h4");

    // Блок 'Распродажа'
    public static final By DISPLAYED_SALE_PRODUCTS_LOCATOR = By.cssSelector("#product1 .slick-active");
    public static final By DISPLAYED_SALE_PRODUCT_IMAGES_LOCATOR = By.cssSelector("#product1 .slick-active .item-img a[title]");
    public static final By DISPLAYED_SALE_PRODUCT_DESCRIPTIONS_LOCATOR = By.cssSelector("#product1 .slick-active > a");
    public static final By DISPLAYED_SALE_PRODUCT_NAMES_LOCATOR = By.cssSelector("#product1 .slick-active h3");
    public static final By DISPLAYED_SALE_PRODUCT_NEW_PRICES_LOCATOR = By.cssSelector("#product1 .slick-active ins > .woocommerce-Price-amount");
    public static final By DISPLAYED_SALE_PRODUCT_BASKET_BUTTONS_LOCATOR = By.cssSelector("#product1 .slick-active .add_to_cart_button");
    public static final By DISPLAYED_SALE_PRODUCT_DETAIL_BUTTONS_LOCATOR = By.cssSelector("#product1 .slick-active .added_to_cart");

    // Блок промо-баннера
    public static final By PROMO_BANNER_LOCATOR = By.cssSelector("#promo-section2 .promo-image");
    public static final By PROMO_BANNER_PRODUCT_NAME_LOCATOR = By.className("promo-desc-title");

    // Блок 'Просмотренные товары'
    public static final By VIEWED_PRODUCTS_BLOCK_LOCATOR = By.id("woocommerce_recently_viewed_products-2");
    public static final By VIEWED_PRODUCT_NAMES_LOCATOR = By.cssSelector(".ap-cat-list .product-title");
    public static final By VIEWED_PRODUCT_NEW_PRICES_LOCATOR = By.cssSelector(".ap-cat-list ins bdi");

    // ERROR MESSAGES //

    public static final String NAMES_NOT_EQUALS_ERROR = "Названия отличаются";
    public static final String PRICES_NOT_EQUALS_ERROR = "Цены отличаются";

    // CONSTRUCTOR //

    public MainPageTest() {
        super(URL);
    }

    // TESTS //

    @Test
    @DisplayName("Переход на выбранную категорию товаров")
    public void transitionTOnSelectedCategory() {
        scriptExecutor.waitForTextPresenceInElement(CATEGORY_ITEM_TITLES_LOCATOR);
        String titleOnHomePage = driver.findElement(CATEGORY_ITEM_TITLES_LOCATOR).getText();
        driver.findElement(CATEGORY_ITEMS_LOCATOR).click();

        String titleOnCatalogPage = driver.findElement(CatalogPageTest.CATALOG_TITLE_LOCATOR).getText();
        String errorMessage = "Название в заголовке каталога отличается от указанного на карточке товара";
        Assertions.assertEquals(titleOnCatalogPage, titleOnHomePage, errorMessage);
    }


    @ParameterizedTest(name = "{1}")
    @DisplayName("Переход на акционный товар из блока 'Распродажа'")
    @CsvSource({
            "IMAGE, Нажатие на изображение",
            "DESCRIPTION, Нажатие на описание"
    })
    public void transitionToSpecialOfferGoods(String elementName, String displayedName) {
        By locator = null;

        switch (elementName) {
            case "IMAGE" -> locator = DISPLAYED_SALE_PRODUCT_IMAGES_LOCATOR;
            case "DESCRIPTION" -> locator = DISPLAYED_SALE_PRODUCT_DESCRIPTIONS_LOCATOR;
        }

        WebElement productItem = driver.findElement(DISPLAYED_SALE_PRODUCTS_LOCATOR);
        WebElement targetElement = driver.findElement(locator);
        scriptExecutor.scrollToElement(productItem);
        wait.until(driver -> !productItem.getDomAttribute("class").contains("animated"));
        scriptExecutor.waitForTextPresenceInElement(DISPLAYED_SALE_PRODUCT_NAMES_LOCATOR, DISPLAYED_SALE_PRODUCT_NEW_PRICES_LOCATOR);

        String nameOnHomePage = driver.findElement(DISPLAYED_SALE_PRODUCT_NAMES_LOCATOR).getText();
        String priceOnHomePage = driver.findElement(DISPLAYED_SALE_PRODUCT_NEW_PRICES_LOCATOR).getText();
        targetElement.click();
        String nameOnProductPage = driver.findElement(ProductPageTest.PRODUCT_TITLE_LOCATOR).getText();
        String priceOnProductPage = driver.findElement(ProductPageTest.PRODUCT_NEW_PRICE_LOCATOR).getText();

        CustomAssertions.assertEqualsIgnoreCase(nameOnHomePage, nameOnProductPage, NAMES_NOT_EQUALS_ERROR);
        Assertions.assertEquals(priceOnHomePage, priceOnProductPage, PRICES_NOT_EQUALS_ERROR);
    }


    @Test
    @DisplayName("Добавление товара в корзину")
    public void addingItemToBasket() {
        testFixture.addProductToBasket();

        String nameOnHomePage = driver.findElement(DISPLAYED_SALE_PRODUCT_NAMES_LOCATOR).getText();
        String priceOnHomePage = driver.findElement(DISPLAYED_SALE_PRODUCT_NEW_PRICES_LOCATOR).getText();
        driver.findElement(DISPLAYED_SALE_PRODUCT_DETAIL_BUTTONS_LOCATOR).click();
        scriptExecutor.waitForTextPresenceInElement(BasketPageTest.ITEM_NAMES_LOCATOR, BasketPageTest.ITEM_PRICES_LOCATOR);

        String nameOnBasketPage = driver.findElement(BasketPageTest.ITEM_NAMES_LOCATOR).getText();
        String priceOnBasketPage = driver.findElement(BasketPageTest.ITEM_PRICES_LOCATOR).getText();
        CustomAssertions.assertEqualsIgnoreCase(nameOnHomePage, nameOnBasketPage, NAMES_NOT_EQUALS_ERROR);
        Assertions.assertEquals(priceOnHomePage, priceOnBasketPage, PRICES_NOT_EQUALS_ERROR);
    }


    @Test
    @DisplayName("Переход на страницу товара через рекламный баннер")
    public void transitionFromPromoBanner() {
        WebElement banner = driver.findElement(PROMO_BANNER_LOCATOR);
        WebElement productNameElement = driver.findElement(PROMO_BANNER_PRODUCT_NAME_LOCATOR);
        String errorMessage = "Название продукта, на который был сделан переход, не содержит указанной на баннере строки";

        scriptExecutor.scrollToElement(banner);
        wait.until(driver -> banner.isDisplayed() && productNameElement.isDisplayed());
        String nameOnHomePage = productNameElement.getText();

        banner.click();
        String nameOnProductPage = driver.findElement(ProductPageTest.PRODUCT_TITLE_LOCATOR).getText();
        CustomAssertions.assertContainsIgnoreCase(nameOnHomePage, nameOnProductPage, errorMessage);
    }


    @Test
    @DisplayName("Проверка появления товаров в блоке 'Просмотренные товары'")
    public void testViewedProductsAppearance() {
        WebElement productItem = driver.findElement(DISPLAYED_SALE_PRODUCTS_LOCATOR);
        WebElement productImageElement = driver.findElement(DISPLAYED_SALE_PRODUCT_IMAGES_LOCATOR);
        scriptExecutor.scrollToElement(productItem);
        wait.until(driver -> !productItem.getDomAttribute("class").contains("animated"));
        scriptExecutor.waitForTextPresenceInElement(DISPLAYED_SALE_PRODUCT_NAMES_LOCATOR, DISPLAYED_SALE_PRODUCT_NEW_PRICES_LOCATOR);

        String nameOnSaleItem = driver.findElement(DISPLAYED_SALE_PRODUCT_NAMES_LOCATOR).getText();
        String priceOnSaleItem = driver.findElement(DISPLAYED_SALE_PRODUCT_NEW_PRICES_LOCATOR).getText();
        productImageElement.click();
        scriptExecutor.navigateBack();

        String blockNotDisplayedError = "Блок просмотренных товаров не появился";
        Assertions.assertTrue(driver.findElement(VIEWED_PRODUCTS_BLOCK_LOCATOR).isDisplayed(), blockNotDisplayedError);
        scriptExecutor.waitForTextPresenceInElement(VIEWED_PRODUCT_NAMES_LOCATOR, VIEWED_PRODUCT_NEW_PRICES_LOCATOR);
        String nameOnViewedItem = driver.findElement(VIEWED_PRODUCT_NAMES_LOCATOR).getText();
        String priceOnViewedItem = driver.findElement(VIEWED_PRODUCT_NEW_PRICES_LOCATOR).getText();
        CustomAssertions.assertEqualsIgnoreCase(nameOnSaleItem, nameOnViewedItem, NAMES_NOT_EQUALS_ERROR);
        Assertions.assertEquals(priceOnSaleItem, priceOnViewedItem, PRICES_NOT_EQUALS_ERROR);
    }
}
