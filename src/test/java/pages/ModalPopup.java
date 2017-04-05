package pages;

import elements.BaseElement;

public class ModalPopup extends MasterPage {
    public static BaseElement popup = new BaseElement("Modal Popup", "//div[@class='modal-popup']");
    public static BaseElement okThanksButton = popup.findElement("OK THANKS button", "//button[@id='btn-ok']");
}
