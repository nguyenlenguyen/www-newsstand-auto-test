package pages;

import elements.BaseElement;

public class SubscriptionsPage extends MasterPage {

    public static BaseElement subscription = new BaseElement("Subscription", "//div[@class='subscription']");
    public static BaseElement emptyMessageTitle = new BaseElement("Message Title", "//div[starts-with(@class,'empty-message')]//strong");
    public static BaseElement emptyMessageLink = new BaseElement("Message Link", "//div[starts-with(@class,'empty-message')]//a");

    // PAGE METHODS ====================================================================================================

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/account-settings/subscriptions");
    }

    public static void shouldShowSubscription(int index, String pubName, String type, String status, String renew) {
        BaseElement section = subscription.getAt(index);
        section.findElement("Cover image", "//img").shouldBeDisplayed();
        section.shouldContainText(pubName);
        section.shouldContainText(type);
        section.shouldContainText(status);
        if (renew != null) section.shouldContainText(renew);
    }

    public static void clickRenewLink(int index) {
        String linkName = subscription.getAt(index).findElement("", "//a").getText();
        subscription.getAt(index).findElement(linkName, "//a").click();
    }
}
