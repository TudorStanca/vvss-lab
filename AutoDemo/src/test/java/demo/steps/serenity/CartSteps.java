package demo.steps.serenity;

import demo.pages.ProductPage;
import net.thucydides.core.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CartSteps {

    ProductPage productPage;

    @Step
    public void adds_current_product_to_cart() {
        productPage.clickAddToCart();
    }

    @Step
    public void should_see_product_added_confirmation() {
        assertThat("Confirmarea adaugarii in cos nu este vizibila",
                productPage.isAddToCartConfirmationVisible(), is(true));
    }
}
