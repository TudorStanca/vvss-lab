package demo.features.login;

import demo.steps.serenity.LoginSteps;
import demo.steps.serenity.LogoutSteps;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityRunner.class)
public class LogoutStory {

    @Managed
    public WebDriver webdriver;

    @Steps
    public LoginSteps loginSteps;

    @Steps
    public LogoutSteps logoutSteps;

    @Test
    public void logged_in_user_should_be_able_to_logout() {
        loginSteps.navigates_to_login_page();
        loginSteps.enters_credentials("su8kg@wshu.net", "SerenityDBB");
        loginSteps.submits_login_form();
        loginSteps.should_be_logged_in();

        logoutSteps.opens_account_dropdown();
        logoutSteps.clicks_logout();
        logoutSteps.should_be_logged_out();
    }
}
