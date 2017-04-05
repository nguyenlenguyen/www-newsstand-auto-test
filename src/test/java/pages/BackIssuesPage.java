package pages;

import elements.BaseElement;

public class BackIssuesPage extends MasterPage {
    public static BaseElement buyBackIssueButton
            = new BaseElement("Buy Issue button", "//ul[@id='back-issue']//li[@class='buy-issue']//button");
    public static BaseElement coverImageBackIssue
            = new BaseElement("Cover Image", "//ul[@id='back-issue']//figure/img");
    public static BaseElement buySpecialIssueButton
            = new BaseElement("Buy Issue button", "//ul[@id='special-issue']//li[@class='buy-issue']//button");
    public static BaseElement coverImageSpecialIssue
            = new BaseElement("Cover Image", "//ul[@id='special-issue']//figure/img");
    public static BaseElement specialIssuesTab
            = new BaseElement("Special Issues tab", "//a[starts-with(@class,'special-issue')]");
    public static BaseElement backIssuesTab
            = new BaseElement("Back Issues tab", "//a[starts-with(@class,'back-issue')]");
    public static BaseElement backIssuePrice
            = new BaseElement("Back Issue price", "//ul[@id='back-issue']//p[@class='price']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/back-issues/");
    }
}
