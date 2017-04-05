package pages;

import elements.BaseElement;

public class ExplorePage extends MasterPage {
    public static BaseElement firstArticle = new BaseElement("First Article", "//div[@id='article_first']/a");
    public static BaseElement articleItem
            = new BaseElement("Article Item", "//div[@class='container-item']");
    public static BaseElement articleCover = articleItem.findElement("Cover Image", "//figure/img");
    public static BaseElement articleTitle = articleItem.findElement("Article Title", "//*[@class='article-title']/a");
    public static BaseElement articlePubName
            = articleItem.findElement("Publication Name", "//*[@class='article-footer']/span[@class='pub-name']");
    public static BaseElement articleIssueName
            = articleItem.findElement("Issue Name", "//*[@class='article-footer']/span[@class='date']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/explore");
    }
}
