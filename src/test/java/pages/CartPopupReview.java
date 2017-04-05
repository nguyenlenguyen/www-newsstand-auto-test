package pages;

import elements.BaseElement;

public class CartPopupReview extends MasterPage {
    public static BaseElement popup
            = new BaseElement("Cart Review Popup", "//div[@id='popup-cart-review']//div[@class='order-details']");
    public static BaseElement okThanksButton
            = popup.findElement("OK THANKS button", "//button[@id='btn-checkout-cart']");
}
