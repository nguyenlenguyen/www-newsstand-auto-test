package pages;

import elements.BaseElement;

public class PostRegistrationPage extends MasterPage {

    public static BaseElement doneButton = new BaseElement("Done button", "//div[@class='button']/button");

    // PAGE METHODS ====================================================================================================

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/account-settings/choose-categories");
    }

    public static void selectCategories(String... categories) {
        for (String category : categories) {
            new BaseElement(category, "//*[@class='title' and .='" + category + "']/preceding-sibling::figure").click();
        }
    }
}
