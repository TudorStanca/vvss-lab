package demo.pages;

import net.serenitybdd.core.annotations.findby.FindBy;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class SearchPage extends PageObject {

    @FindBy(css = "#search-input")
    private WebElementFacade searchInput;

    public void enterSearchTerm(String term) {
        searchInput.withTimeoutOf(15, TimeUnit.SECONDS).waitUntilVisible();
        searchInput.click();
        searchInput.clear();
        searchInput.sendKeys(term);
    }

    public void submitSearch() {
        searchInput.sendKeys(Keys.ENTER);
    }

    public boolean hasResults() {
        List<WebElement> results = getDriver().findElements(By.cssSelector("prod-grid-box"));
        return !results.isEmpty();
    }

    public void clickFirstResult() {
        List<WebElement> results = getDriver().findElements(
                By.cssSelector("prod-grid-box a.clean-a.select-item-event"));
        if (!results.isEmpty()) {
            results.get(0).click();
            return;
        }
        List<WebElement> bookLinks = getDriver().findElements(By.cssSelector("a[href*='/carte/']"));
        if (!bookLinks.isEmpty()) {
            bookLinks.get(0).click();
            return;
        }
        throw new RuntimeException("Nu s-au gasit rezultate de cautat pe care sa se faca click");
    }
}
