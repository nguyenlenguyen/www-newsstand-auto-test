package pages;

import elements.BaseElement;

public class FreeIssuePage extends MasterPage {
    public static BaseElement issueCoverImage
            = new BaseElement("Issue Cover Image", "//a[@data-type='issue']/figure");
    public static BaseElement subscriptionCoverImage
            = new BaseElement("Subscription Cover Image", "//a[@data-type='subscription']/figure");

    public static String getFreeIssueID(String type, int index) {
        return new BaseElement("", "//a[@data-type='" + type + "']").getAt(index).getAttribute("data-id");
    }
}
