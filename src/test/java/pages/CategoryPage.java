package pages;

import elements.BaseElement;
import utilities.StringUtils;

public class CategoryPage extends MasterPage {
    public static BaseElement categoryFilter = new BaseElement("Category Filter", "//p[@data-target='.entertainment-menu']");
    public static BaseElement sortFilter = new BaseElement("Sort Filter", "//p[@data-target='.sort-menu']");
    public static BaseElement languageFilter = new BaseElement("Language Filter", "//p[@data-target='.language-menu']");
    public static BaseElement publicationCover = new BaseElement("Publication Cover", "//figure");
    public static BaseElement publicationName = new BaseElement("Publication Name", "//a/*[@class='title']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/category/");
    }

    public static void selectCategory(String category) {
        categoryFilter.click();
        categoryFilter.findElement(category, "/following::a[.='" + category + "']").click();
    }

    public static void selectSortBy(String sort) {
        sortFilter.click();
        sortFilter.findElement(sort, "/following::a[.='" + sort + "']").click();
    }

    public static void selectLanguage(String language) {
        languageFilter.click();
        languageFilter.findElement(language, "/following::a[.='" + language + "']").click();
    }

    public static String getPublicationID(int index) {
        BaseElement link = new BaseElement("", "//ul[@class='publications']//a").getAt(index);
        return StringUtils.getLastSubString(link.getAttribute("href"), "-");
    }
}
