package demo.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

@DefaultUrl("https://carturesti.ro/site/login")
public class CarturestiLoginPage extends PageObject {

    @FindBy(css = "#loginform-email")
    private WebElementFacade emailField;

    @FindBy(css = "#loginform-password")
    private WebElementFacade passwordField;

    @FindBy(css = "button[name='login-button']")
    private WebElementFacade autentificareButton;

    // "parol" and "incorect" are diacritic-free substrings present in all variants
    // of the error message ("parolă incorectă", "parola incorecta", etc.)
    @FindBy(xpath = "//*[contains(text(),'parol') and contains(text(),'incorect')]")
    private WebElementFacade errorMessage;

    public void enterEmail(String email) {
        emailField.withTimeoutOf(15, TimeUnit.SECONDS).waitUntilVisible();
        emailField.click();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        dismissCookieBannerIfPresent();
        passwordField.withTimeoutOf(15, TimeUnit.SECONDS).waitUntilVisible();
        try {
            passwordField.click();
        } catch (Exception e) {
            // Banner may have reappeared; dismiss and use JS click as fallback
            dismissCookieBannerIfPresent();
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", passwordField.getElement());
        }
        passwordField.sendKeys(password);
    }

    private void dismissCookieBannerIfPresent() {
        List<WebElement> banners = getDriver().findElements(By.cssSelector(".cc-window.cc-banner"));
        if (!banners.isEmpty() && banners.get(0).isDisplayed()) {
            List<WebElement> denyButtons = getDriver().findElements(By.cssSelector("a.cc-deny"));
            if (!denyButtons.isEmpty()) {
                denyButtons.get(0).click();
            }
        }
    }

    public void clickAutentificare() {
        autentificareButton.withTimeoutOf(15, TimeUnit.SECONDS).waitUntilClickable();
        autentificareButton.click();
    }

    public boolean hasErrorMessage() {
        try {
            errorMessage.withTimeoutOf(10, TimeUnit.SECONDS).waitUntilVisible();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }
}
