package pages;

import elements.BaseElement;

public class BackIssuePopup extends MasterPage {
    public static BaseElement popup = new BaseElement("Back Issue popup", "//div[@id='backissue-modal']");
    public static BaseElement subscriptionOnlyButton = popup.findElement("Subscription Only", "//button", 1);
    public static BaseElement backAndSubscriptionButton = popup.findElement("Back and Subscription", "//button", 2);
}
