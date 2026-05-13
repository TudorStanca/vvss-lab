package demo.steps.serenity;

import demo.pages.CarturestiHomePage;
import demo.pages.CarturestiLoginPage;
import net.thucydides.core.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class LoginSteps {

    CarturestiLoginPage loginPage;
    CarturestiHomePage homePage;

    @Step
    public void navigates_to_login_page() {
        homePage.open();
        homePage.acceptCookiesIfPresent();
        homePage.clickLoginButton();
        homePage.clickUtilizatorExistent();
    }

    @Step
    public void enters_credentials(String username, String password) {
        loginPage.enterEmail(username);
        loginPage.enterPassword(password);
    }

    @Step
    public void submits_login_form() {
        loginPage.clickAutentificare();
    }

    @Step
    public void verifies_result(String expectedResult) {
        if ("success".equals(expectedResult)) {
            should_be_logged_in();
        } else {
            should_see_error_message(expectedResult);
        }
    }

    @Step
    public void should_be_logged_in() {
        assertThat(homePage.isUserLoggedIn(), is(true));
    }

    @Step
    public void should_see_error_message(String expectedError) {
        assertThat(loginPage.hasErrorMessage(), is(true));
        assertThat(loginPage.getErrorMessage(), containsString(expectedError));
    }
}
