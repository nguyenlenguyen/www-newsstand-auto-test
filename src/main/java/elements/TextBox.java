package elements;

import core.BaseAssert;
import core.Browser;
import org.openqa.selenium.Keys;
import utilities.ReportUtils;

public class TextBox extends BaseElement {
    public TextBox(String controlDescription, String xpath) {
        super(controlDescription, xpath);
    }

    private boolean waitTextToBe(String value) {
        long baseTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - baseTime < timeout * 1000) {
            if (getText().equals(value)) {
                return true;
            } else {
                Browser.sleep(1000);
            }
        }
        return false;
    }

    @Override
    public String getText() {
        return this.getAttribute("value");
    }

    public TextBox setText(String value) {
        waitElement().clear();
        if (!value.equals("")) {
            waitElement().sendKeys(value);
            ReportUtils.logAll("Enter [" + value + "] into [" + controlDescription + "]");
        } else {
            ReportUtils.logAll("Clear text from [" + controlDescription + "]");
        }
        return this;
    }

    @Override
    public void shouldHaveText(String text) {
        waitTextToBe(text);
        if (text.isEmpty())
            BaseAssert.assertEquals("Check [" + controlDescription + "] is blank", this.getText(), "");
        else {
            BaseAssert.assertEquals("Check [" + controlDescription + "] displays [" + text + "]", getText(), text);
        }
    }

    public void pressKey(Keys keys) {
        waitElement().sendKeys(keys);
    }

    public void shouldBeInRedBorder() {
        String description = "[" + this.controlDescription + "] should be in red border";
        BaseAssert.assertEquals(description, getAttribute("class").contains("invalid"), true);
    }
}
