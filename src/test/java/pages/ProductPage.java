package pages;

import elements.BaseElement;

public class ProductPage extends MasterPage {

    public static BaseElement publicationName = new BaseElement("Publication Name", "//div[@class='product']/h2[@class='title']");
    public static BaseElement issueName = new BaseElement("Issue Name", "//div[@class='product']/div[@class='date']");
    public static BaseElement buyIssueButton = new BaseElement("Buy Issue button", "//div[starts-with(@class,'buy-issue')]/button");
    public static BaseElement subscribeButton = new BaseElement("Subscribe button", "//div[starts-with(@class,'subscribe')]/button");
    public static BaseElement rightProceedButton = new BaseElement("Right Proceed button", "//div[@class='right']/div[@class='confirm']/button");
    public static BaseElement rightViewCartLink = new BaseElement("Right View Cart link", "//div[@class='right']/div[@class='confirm']/a");
    public static BaseElement leftProceedButton = new BaseElement("Left Proceed button", "//div[@class='left']/div[@class='confirm']/button");
    public static BaseElement leftViewCartLink = new BaseElement("Left View Cart link", "//div[@class='left']/div[@class='confirm']/a");
    public static BaseElement viewAllBackIssuesLink
            = new BaseElement("View All Back Issues link", "//div[@class='carousel-header' and contains(.,'RECENT ISSUES')]//a");
    public static BaseElement viewAllSpecialIssuesLink
            = new BaseElement("View All Special Issues link", "//div[@class='carousel-header' and contains(.,'SPECIAL ISSUES')]//a");
    public static BaseElement subscribePrice = new BaseElement("Subscribe Price", "//div[starts-with(@class,'subscribe')]/div");
    public static BaseElement buyPrice = new BaseElement("Buy Issue Price", "//div[starts-with(@class,'buy-issue')]/div");
    public static BaseElement couponInfo = new BaseElement("Coupon Info", "//div[@class='special']");
    public static BaseElement backIssueCover = new BaseElement("Back Issue cover", "//div[@id='block-recent-issues']//img");
    public static BaseElement subscriptionCredits = new BaseElement("Subscription credits", "//div[@id='subscription-number-issues']");

    // PAGE METHODS ====================================================================================================

    public static void shouldBeDisplayed() {
        shouldHaveTitle(ProductPage.publicationName.getText());
    }
}