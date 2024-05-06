package tests.utils;

public class TestUtils {

    private TestUtils() {}


    /**
     * Метод передводит полученные с сайта строковые значения цены вида "5000,00₽" в число типа float.
     * @param price String
     * @return float
     */
    public static float parsePriceToFloat(String price) {
        return Float.parseFloat(price.replace("₽", "").replace(",", "."));
    }
}
