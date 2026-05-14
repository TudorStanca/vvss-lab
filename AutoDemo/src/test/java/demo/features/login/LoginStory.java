package demo.features.login;

import demo.steps.serenity.LoginSteps;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("testdata/login_credentials.csv")
public class LoginStory {

    @Managed
    public WebDriver webdriver;

    @Steps
    public LoginSteps loginSteps;

    private String username;
    private String password;
    private String expectedResult;

    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }

    @Test
    public void user_attempts_login() {
        loginSteps.navigates_to_login_page();
        loginSteps.enters_credentials(username, password);
        loginSteps.submits_login_form();
        loginSteps.verifies_result(expectedResult);
    }
}
