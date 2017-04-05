package pages;

public class IssuePage extends ProductPage {
    public static void shouldBeDisplayed() {
        shouldHaveUrl("/issue/");
    }
}
