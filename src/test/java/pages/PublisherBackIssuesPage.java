package pages;

import elements.BaseElement;

public class PublisherBackIssuesPage extends BackIssuesPage {
    public static BaseElement publisherLogo = new BaseElement("Publisher Logo", "//h1[@class='publisher-title']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/publisher-back-issues/");
        zinioLogo.shouldNotBeDisplayed();
        menuBar.shouldNotBeDisplayed();
        searchTextBox.shouldNotBeDisplayed();
        searchIcon.shouldNotBeDisplayed();
        publisherLogo.shouldBeDisplayed();
        cartIcon.shouldBeDisplayed();
    }
}
