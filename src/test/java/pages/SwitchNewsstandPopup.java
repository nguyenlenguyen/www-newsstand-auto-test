package pages;

import elements.BaseElement;

public class SwitchNewsstandPopup extends MasterPage {
    public static BaseElement popup
            = new BaseElement("Switch Newsstand popup", "//div[@id='modalSwitchNewsstand']//div[@class='modal-popup']");
    public static BaseElement stayButton = popup.findElement("STAY button", "//button[@id='btnStay']");
    public static BaseElement continueButton = popup.findElement("CONTINUE button", "//button[@id='btnContinue']");
}
