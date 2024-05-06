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

import java.util.ArrayList;
import java.util.List;

@DisplayName("Страница каталога")
public class CatalogPageTest extends DefaultTest {

    public static final String URL = "https://intershop5.skillbox.ru/product-category/catalog/";

    // LOCATORS //

    public static final By CATALOG_TITLE_LOCATOR = By.className("entry-title");
    public static final By CATEGORY_LINKS_LOCATOR = By.cssSelector(".cat-item a");
    public static final By PRODUCTS_FROM_LIST_LOCATOR = By.cssSelector(".wc-products li");
    public static final By IN_STOCK_PRODUCT_ITEMS_LOCATOR = By.cssSelector("li.product.instock");

    // CONSTRUCTOR //

    public CatalogPageTest() {
        super(URL);
    }

    // TESTS //

    @Test
    @DisplayName("Выбор категории товаров")
    public void testSelectCategory() {
        WebElement firstCategory = driver.findElement(CATEGORY_LINKS_LOCATOR);
        scriptExecutor.waitForTextPresenceInElement(firstCategory);
        String categoryName = firstCategory.getText();

        firstCategory.click();
        scriptExecutor.waitForTextPresenceInElement(CATALOG_TITLE_LOCATOR);
        String catalogTitle = driver.findElement(CATALOG_TITLE_LOCATOR).getText();
        String errorMessage = "Название категории отличается от заголовка каталога, к которому был сделан переход";
        CustomAssertions.assertEqualsIgnoreCase(categoryName, catalogTitle, errorMessage);
    }


    @ParameterizedTest(name = "{1}")
    @DisplayName("Переход на товар из списка товаров")
    @CsvSource({
            "IMAGE, Нажатие на изображение",
            "TITLE, Нажатие на название товара"
    })
    public void transitionToGoodsFromList(String elementName, String displayedName) {
        By transitionTargetLocator = null;
        By imageLocator = By.cssSelector(".inner-img a");
        By titleLocator = By.cssSelector(".collection_desc h3");

        switch (elementName) {
            case "IMAGE" -> transitionTargetLocator = imageLocator;
            case "TITLE" -> transitionTargetLocator = titleLocator;
        }

        WebElement firstProductFromList = driver.findElement(PRODUCTS_FROM_LIST_LOCATOR);
        scriptExecutor.waitForTextPresenceInElement(firstProductFromList.findElement(titleLocator));
        String nameOnCatalogPage = firstProductFromList.findElement(titleLocator).getText();
        firstProductFromList.findElement(transitionTargetLocator).click();

        String nameOnProductPage = driver.findElement(ProductPageTest.PRODUCT_TITLE_LOCATOR).getText();
        String namesNotEqualsError = "Названия отличаются";
        CustomAssertions.assertEqualsIgnoreCase(nameOnCatalogPage, nameOnProductPage, namesNotEqualsError);
    }


    @Test
    @DisplayName("Переключение страницы списка товаров")
    public void switchProductListPage() {
        By secondPageButtonLocator = By.cssSelector("ul.page-numbers li:nth-of-type(2) a");
        List<String> firstPageProductsId = new ArrayList<>();
        List<String> secondPageProductsId = new ArrayList<>();
        List<WebElement> firstPageProducts = driver.findElements(PRODUCTS_FROM_LIST_LOCATOR);

        firstPageProducts.forEach(item ->
            firstPageProductsId.add(item.getAttribute("class"))
        );

        driver.findElement(secondPageButtonLocator).click();
        List<WebElement> secondPageProducts = driver.findElements(PRODUCTS_FROM_LIST_LOCATOR);

        secondPageProducts.forEach(item ->
                secondPageProductsId.add(item.getAttribute("class"))
        );

        String errorMessage = "После переключения страницы список товаров не изменился";
        Assertions.assertNotEquals(firstPageProductsId, secondPageProductsId, errorMessage);
    }


    @Test
    @DisplayName("Добавление товара в корзину")
    public void addingItemToBasket() {
        By addButtonLocator = By.className("add_to_cart_button");
        By detailsButtonLocator = By.className("added_to_cart");
        By productTitleLocator = By.tagName("h3");
        WebElement item = driver.findElement(IN_STOCK_PRODUCT_ITEMS_LOCATOR);
        WebElement addButton = item.findElement(addButtonLocator);
        scriptExecutor.waitForTextPresenceInElement(item.findElement(productTitleLocator));
        String nameOnCatalogPage = item.findElement(productTitleLocator).getText();

        item.findElement(addButtonLocator).click();
        wait.until(driver -> addButton.getAttribute("class").contains("added"));
        item.findElement(detailsButtonLocator).click();
        scriptExecutor.waitForTextPresenceInElement(BasketPageTest.ITEM_NAMES_LOCATOR);

        String nameOnBasketPage = driver.findElement(BasketPageTest.ITEM_NAMES_LOCATOR).getText();
        String namesNotEqualsError = "Названия отличаются";
        CustomAssertions.assertEqualsIgnoreCase(nameOnCatalogPage, nameOnBasketPage, namesNotEqualsError);
    }
}
