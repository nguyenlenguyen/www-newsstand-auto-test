package pages;

import elements.BaseElement;

public class MainShopPage extends MasterPage {
    public static BaseElement publicationCover = new BaseElement("Publication Cover", "//div[@class='carousel-group']//img");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/browse/shop");
    }
}