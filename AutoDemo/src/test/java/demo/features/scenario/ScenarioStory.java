package demo.features.scenario;

import demo.steps.serenity.CartSteps;
import demo.steps.serenity.LoginSteps;
import demo.steps.serenity.LogoutSteps;
import demo.steps.serenity.SearchSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class ScenarioStory {

    @Managed
    public WebDriver webdriver;

    @Steps
    public LoginSteps loginSteps;

    @Steps
    public SearchSteps searchSteps;

    @Steps
    public CartSteps cartSteps;

    @Steps
    public LogoutSteps logoutSteps;

    @Test
    public void user_searches_and_adds_book_to_cart() {
        // 1. Login valid
        loginSteps.navigates_to_login_page();
        loginSteps.enters_credentials("su8kg@wshu.net", "SerenityDBB");
        loginSteps.submits_login_form();
        loginSteps.should_be_logged_in();

        // 2. Cautare carte
        searchSteps.searches_for("Mihai Eminescu");
        searchSteps.should_see_search_results();

        // 3. Click pe primul rezultat
        searchSteps.opens_first_result();
        searchSteps.should_be_on_product_page();

        // 4. Adaugare in cos
        cartSteps.adds_current_product_to_cart();
        cartSteps.should_see_product_added_confirmation();

        // 5. Logout
        logoutSteps.opens_account_dropdown();
        logoutSteps.clicks_logout();
        logoutSteps.should_be_logged_out();
    }
}
