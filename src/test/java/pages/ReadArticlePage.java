package pages;

import elements.BaseElement;

public class ReadArticlePage extends MasterPage {
    public static BaseElement publicationName = new BaseElement("Publication Name", "//div[@class='magazine-name']");
    public static BaseElement articleTitle = new BaseElement("Article Title", "//*[@class='title-article']");
    public static BaseElement infoPanel = new BaseElement("Info Panel", "//div[@class='issue-related fixed']");
    public static BaseElement infoPanel_CoverImage
            = infoPanel.findElement("Info Panel - Cover Image", "//img");
    public static BaseElement infoPanel_Description
            = infoPanel.findElement("Info Panel - Description", "//p[@class='describe']");
    public static BaseElement infoPanel_PublicationName
            = infoPanel.findElement("Info Panel - Publication Name", "//*[@class='name-issue']");
    public static BaseElement infoPanel_IssueName
            = infoPanel.findElement("Info Panel - Issue Name", "//*[@class='date-issue']");
    public static BaseElement infoPanel_BuyIssueButton
            = infoPanel.findElement("Info Panel - Buy Issue button", "//button[@class='btn buy']");
    public static BaseElement infoPanel_SubscribeButton
            = infoPanel.findElement("Info Panel - Subscribe button", "//button[@class='btn subscribe']");
    public static BaseElement infoPanel_buyPrice
            = infoPanel.findElement("Info Panel - Buy Price", "//span[@class='price']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/explore/");
    }
}
