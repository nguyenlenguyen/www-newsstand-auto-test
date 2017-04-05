package pages;

import elements.BaseElement;

public class SubscriptionsPopup extends MasterPage {
    public static BaseElement popup
            = new BaseElement("Subscription Popup", "//div[@class='popup-subscription on']//div[@class='inner-content']");
    public static BaseElement confirmButton
            = new BaseElement("Confirm button", "//button[@class='confirm-stop primary']");
    public static BaseElement cancelButton
            = new BaseElement("Cancel button", "//a[@class='link-cancel']");
}
