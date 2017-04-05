package pages;

public class SearchResultPage extends MasterPage {
    public static void shouldBeDisplayed() {
        shouldHaveUrl("/search?keyword=");
    }
}
