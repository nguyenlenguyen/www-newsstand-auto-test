package pages;

import core.BaseAssert;
import elements.BaseElement;
import elements.CheckBox;
import org.openqa.selenium.WebElement;
import utilities.ReportUtils;

public class PreferencesPage extends MasterPage {
    public static BaseElement allowAdultContent
            = new BaseElement("Allow Adult Content", "//input[@name='allow_adult' and @value='1']");
    public static BaseElement notAllowAdultContent
            = new BaseElement("Not Allow Adult Content", "//input[@name='allow_adult' and @value='0']");
    public static BaseElement saveButton = new BaseElement("Save button", "//button[.='Save']");
    public static CheckBox updatesCheckBox = new CheckBox("Updates checkbox", "//input[@name='subscribes[]']");
    public static CheckBox interestCheckBox = new CheckBox("Interest checkbox", "//div[@class='interests']//input");

    public static void setAllInterests(boolean value) {
        ReportUtils.disableLog((value ? "Select" : "Unselect") + " all the interests.");
        for (WebElement element : interestCheckBox.getList())
            if (element.isSelected() != value)
                element.click();
        ReportUtils.enableLog();
    }

    public static void verifyAllInterests(boolean value) {
        String verify = value ? "selected" : "unselected";
        for (WebElement element : interestCheckBox.getList()) {
            String name = element.getAttribute("value");
            BaseAssert.assertEquals("Check interest [" + name + "] is " + verify, element.isSelected(), value);
        }
    }
}