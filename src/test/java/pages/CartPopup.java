package pages;

import elements.BaseElement;

public class CartPopup extends MasterPage {
    public static BaseElement popup = new BaseElement("Cart Popup", "//div[contains(@class,'popup-cart')]");
    public static BaseElement proceedToCheckoutButton = popup.findElement("Proceed to checkout button", "//button[@id='btn-checkout-cart']");
    public static BaseElement continueShoppingLink = popup.findElement("Continue Shopping link", "//a[@id='btn-continue']");
    public static BaseElement subTotalPrice = popup.findElement("Sub Total price", "//div[@class='price']");
    public static BaseElement itemPrice = popup.findElement("Item price", "//div[@class='price pull-right']");
    public static BaseElement yourCartIsEmptyMessage = popup.findElement("Your Cart is Empty", "//*[.='Your Cart is Empty']");

    public static void removeItem(String itemName) {
        BaseElement removeLink = popup.findElement("Remove link of " + itemName,
                "//div[@class='mag-name' and contains(.,'" + itemName + "')]/following::a[.='remove']");
        removeLink.click();
        removeLink.waitDisappear();
    }

    public static void closeCart() {
        sleep(500);
        continueShoppingLink.click();
        popup.waitDisappear();
    }
}
