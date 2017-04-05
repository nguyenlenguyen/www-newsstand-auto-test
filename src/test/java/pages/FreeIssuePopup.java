package pages;

import elements.BaseElement;

public class FreeIssuePopup {
    public static BaseElement popup = new BaseElement("Free Issue popup", "//div[@class='popup-subscription on']");
    public static BaseElement coverImage = popup.findElement("Cover Image", "//img");
    public static BaseElement publicationName = popup.findElement("Publication Name", "//*[@class='title-publisher']");
    public static BaseElement issueName = popup.findElement("Issue Name", "//p[@class='count-year']");
    public static BaseElement message = popup.findElement("Message", "//p[@class='description']");
    public static BaseElement continueShoppingButton
            = popup.findElement("Continue Shopping button", "//button[@class='confirm-stop primary']");
    public static BaseElement gotoMyLibraryLink = popup.findElement("Go to my library link", "//a[@href='/my-library']");

    public static void shouldBeDisplayed() {
        FreeIssuePopup.popup.shouldBeDisplayed();
        FreeIssuePopup.coverImage.shouldBeDisplayed();
        FreeIssuePopup.publicationName.shouldBeDisplayed();
        FreeIssuePopup.issueName.shouldBeDisplayed();
        FreeIssuePopup.message.shouldHaveText("This free issue has been added to your library.");
        FreeIssuePopup.continueShoppingButton.shouldBeDisplayed();
        FreeIssuePopup.gotoMyLibraryLink.shouldBeDisplayed();
    }
}
