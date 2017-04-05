package pages;

import elements.BaseElement;

public class ThankYouPage extends MasterPage {
    public static BaseElement bigCoverImage = new BaseElement("Big Cover image", "//div[@class='magazine-cover-img']/img");
    public static BaseElement smallCoverImage = new BaseElement("Small Cover image", "//figure/img");
    public static BaseElement startReadingLink = new BaseElement("START READING link", "//a[@id='btn-start-reading']");
    public static BaseElement continueShoppingButton = new BaseElement("Continue Shopping button", "//a[@id='btn-continue']");

    public static void shouldBeDisplayed(boolean successful) {
        shouldHaveUrl("/thank-you/");
        shouldShowMessage(successful ?
                "Thank you for your purchase." :
                "There was a problem processing your card. Review your payment information.");
    }

    public static void shouldBeDisplayed(boolean successful, int quantity) {
        shouldHaveUrl("/thank-you/");
        shouldShowMessage(successful ?
                "Thank you for your purchase." :
                "There was a problem processing your card. Review your payment information.");
        if (quantity == 1)
            bigCoverImage.shouldHaveQuantity(1);
        else
            smallCoverImage.shouldHaveQuantity(quantity);
    }
}