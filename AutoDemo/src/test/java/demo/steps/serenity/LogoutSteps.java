package demo.steps.serenity;

import demo.pages.CarturestiHomePage;
import net.thucydides.core.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LogoutSteps {

    CarturestiHomePage homePage;

    @Step
    public void opens_account_dropdown() {
        homePage.clickSalutDropdown();
    }

    @Step
    public void clicks_logout() {
        homePage.clickLogout();
    }

    @Step
    public void should_be_logged_out() {
        assertThat(homePage.isLoginButtonVisible(), is(true));
    }
}
