package pages;

import core.BaseAssert;
import elements.BaseElement;

public class HomePage extends MasterPage {
    public static BaseElement publicationCover = new BaseElement("Publication Cover", "//figure");
    public static BaseElement desktopIcon = new BaseElement("Desktop icon", "//li[@class='device_desktop']//a");
    public static BaseElement appStoreIcon = new BaseElement("App Store icon", "//li[@class='device_ios']//a");
    public static BaseElement googlePlayIcon = new BaseElement("Google Play icon", "//li[@class='device_android']//a");
    public static BaseElement viewAllFeaturedLink
            = new BaseElement("View all Featured link", "//h2[.='FEATURED']/following-sibling::a");

    public static void shouldBeDisplayed() {
        String homeUrl = System.getProperty("siteUrl") + "/";
        BaseAssert.assertEquals("Check Home page is displayed", getCurrentUrl(), homeUrl);
    }
}
