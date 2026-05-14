package demo.pages;

import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProductPage extends PageObject {

    private static final By PRODUCT_TITLE = By.cssSelector("h1, .product-title, .titlu-produs, [class*='product-name']");

    private static final By ADD_TO_CART_BUTTON = By.cssSelector("a[title='Adaugă în coș']");

    private static final By CART_CONFIRMATION = By.cssSelector("md-toast, .md-toast-content, [class*='toast']");

    public boolean isProductDisplayed() {
        List<WebElement> titles = getDriver().findElements(PRODUCT_TITLE);
        return !titles.isEmpty() && titles.get(0).isDisplayed();
    }

    public String getProductTitle() {
        List<WebElement> titles = getDriver().findElements(PRODUCT_TITLE);
        if (!titles.isEmpty()) {
            return titles.get(0).getText();
        }
        return "";
    }

    public void clickAddToCart() {
        List<WebElement> buttons = getDriver().findElements(ADD_TO_CART_BUTTON);
        for (WebElement btn : buttons) {
            if (btn.isDisplayed()) {
                try {
                    btn.click();
                } catch (Exception e) {
                    ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", btn);
                }
                return;
            }
        }
        throw new RuntimeException("Butonul 'Adauga in cos' nu a fost gasit pe pagina produsului");
    }

    public boolean isAddToCartConfirmationVisible() {
        // Asteapta pana la 10 secunde ca sa apara confirmarea
        long deadline = System.currentTimeMillis() + 10_000;
        while (System.currentTimeMillis() < deadline) {
            List<WebElement> confirmations = getDriver().findElements(CART_CONFIRMATION);
            for (WebElement el : confirmations) {
                if (el.isDisplayed()) {
                    return true;
                }
            }
            // Verifica si cresterea counter-ului la cos ca fallback
            List<WebElement> cartCounts = getDriver().findElements(
                    By.cssSelector(".cart-count, .cart-qty, #cart-total, .badge-cart, [class*='cart-count']"));
            for (WebElement el : cartCounts) {
                String text = el.getText().trim();
                if (!text.isEmpty() && !text.equals("0")) {
                    return true;
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}
        }
        return false;
    }
}
