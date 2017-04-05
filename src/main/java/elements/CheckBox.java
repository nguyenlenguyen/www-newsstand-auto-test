package elements;

import core.BaseAssert;

public class CheckBox extends BaseElement {
    public CheckBox(String controlDescription, String xpath) {
        super(controlDescription, xpath);
    }

    public void check() {
        if (!this.waitElement().isSelected())
            this.click();
    }

    public void uncheck() {
        if (this.waitElement().isSelected())
            this.click();
    }

    public void shouldBeSelected() {
        BaseAssert.assertEquals("Check [" + controlDescription + "] is selected", waitElement().isSelected(), true);
    }

    public void shouldBeUnselected() {
        BaseAssert.assertEquals("Check [" + controlDescription + "] is unselected", !waitElement().isSelected(), true);
    }
}
