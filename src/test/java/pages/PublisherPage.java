package pages;

import elements.BaseElement;

public class PublisherPage extends ProductPage {

    public static BaseElement publisherLogo = new BaseElement("Publisher Logo", "//h1[@class='publisher-title']");

    // PAGE METHODS ====================================================================================================

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/publisher/");
    }
}
