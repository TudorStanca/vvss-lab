package demo.steps.serenity;

import demo.pages.ProductPage;
import demo.pages.SearchPage;
import net.thucydides.core.annotations.Step;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SearchSteps {

    SearchPage searchPage;
    ProductPage productPage;

    @Step
    public void searches_for(String term) {
        searchPage.enterSearchTerm(term);
        searchPage.submitSearch();
    }

    @Step
    public void should_see_search_results() {
        assertThat("Pagina de rezultate cautare nu contine produse", searchPage.hasResults(), is(true));
    }

    @Step
    public void opens_first_result() {
        searchPage.clickFirstResult();
    }

    @Step
    public void should_be_on_product_page() {
        assertThat("Pagina produsului nu este afisata corect", productPage.isProductDisplayed(), is(true));
    }
}
