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

@DefaultUrl("https://carturesti.ro")
public class CarturestiHomePage extends PageObject {

    @FindBy(css = "button[data-target='#modalLogin']")
    private WebElementFacade loginHeaderButton;

    @FindBy(css = "#loginTrigger")
    private WebElementFacade utilizatorExistentButton;

    @FindBy(css = "#accountDropdown")
    private WebElementFacade salutButton;

    // Logout link may be nested inside an animated dropdown; located dynamically in clickLogout()
    private static final By LOGOUT_LINK = By.xpath(
            "//a[contains(@href,'logout') or contains(@href,'delogare') or contains(@href,'deconect')]");


    public void acceptCookiesIfPresent() {
        List<WebElement> buttons = getDriver().findElements(By.cssSelector("a.cc-deny"));
        if (!buttons.isEmpty() && buttons.get(0).isDisplayed()) {
            buttons.get(0).click();
        }
    }

    public void clickLoginButton() {
        List<WebElement> buttons = getDriver().findElements(By.cssSelector("button[data-target='#modalLogin']"));
        for (WebElement btn : buttons) {
            if (btn.isDisplayed()) {
                btn.click();
                return;
            }
        }
        throw new RuntimeException("Login button not found or not visible");
    }

    public void clickUtilizatorExistent() {
        utilizatorExistentButton.withTimeoutOf(15, TimeUnit.SECONDS).waitUntilVisible();
        utilizatorExistentButton.click();
    }

    public boolean isUserLoggedIn() {
        // Logout link is present in the DOM iff the user is authenticated
        return !getDriver().findElements(LOGOUT_LINK).isEmpty();
    }

    public void clickSalutDropdown() {
        // Dropdown open not required — clickLogout() uses JS and works regardless of visibility
        if (!getDriver().findElements(By.cssSelector("#accountDropdown")).isEmpty()) {
            getDriver().findElement(By.cssSelector("#accountDropdown")).click();
        }
    }

    public void clickLogout() {
        WebElement link = getDriver().findElement(LOGOUT_LINK);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", link);
    }

    public boolean isLoginButtonVisible() {
        return !getDriver().findElements(By.cssSelector("button[data-target='#modalLogin']")).isEmpty();
    }
}
