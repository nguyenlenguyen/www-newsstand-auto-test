package pages;

import elements.BaseElement;

public class MyLibraryPage extends MasterPage {

    public static BaseElement publicationName
            = new BaseElement("Publication Name", "//a[starts-with(@href,'/reader')]//*[@class='title']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/my-library");
    }
}
