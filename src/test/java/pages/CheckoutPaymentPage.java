package pages;

import core.Browser;
import core.DriverManager;
import elements.BaseElement;
import elements.TextBox;
import objects.CreditCard;
import utilities.ReportUtils;
import utilities.StringUtils;

public class CheckoutPaymentPage extends MasterPage {

    public static TextBox firstNameTextBox = new TextBox("First Name textbox", "//input[@id='firstname']");
    public static TextBox lastNameTextBox = new TextBox("First Name textbox", "//input[@id='lastname']");
    public static TextBox cardNumberTextBox = new TextBox("Card Number textbox", "//input[@id='credit-card-number']");
    public static TextBox monthTextBox = new TextBox("Month textbox", "//input[@id='expiration-month']");
    public static TextBox yearTextBox = new TextBox("Year textbox", "//input[@id='expiration-year']");
    public static TextBox cvvTextBox = new TextBox("CVV textbox", "//input[@id='cvv']");
    public static TextBox postalCodeTextBox = new TextBox("Postal Code textbox", "//input[@id='payment-zip-code']");
    public static BaseElement completePurchaseButton
            = new BaseElement("Complete Purchase button", "//button[@id='btn-checkout-payinfo']");
    public static BaseElement orderDetailSection
            = new BaseElement("Order Detail section", "//div[@class='payinfo-main']//div[@class='order-details']");
    public static BaseElement itemPrice = orderDetailSection.findElement("Item Price", "//div[starts-with(@class,'price')]");
    public static BaseElement magazinePrice = new BaseElement("Magazine Price", "//div[@id='subtotal']");
    public static BaseElement taxPrice = new BaseElement("Tax Price", "//div[@id='taxes']");
    public static BaseElement totalPrice
            = new BaseElement("Total Price", "//div[@class='total name']/following-sibling::div[@class='price']");
    public static BaseElement existingCreditCardSection = new BaseElement("Existing Credit Card section", "//div[@class='existing-cc']");
    public static BaseElement editCreditCardLink = new BaseElement("Edit Credit Card link", "//a[@class='edit-payment']");
    public static BaseElement continueShoppingLinkCartEmpty
            = new BaseElement("Continue Shopping link", "//div[@class='continue-content']//a[@href='/browse/shop']");
    public static BaseElement paypalRadioBox = new BaseElement("PayPal radio", "//input[@id='paypal']");
    public static BaseElement paypalButton = new BaseElement("PayPal button", "//img[contains(@src,'/pay-with-paypal.png')]");
    public static BaseElement paypalInfo = new BaseElement("Paypal Info", "//div[@id='braintree-paypal-loggedin']");
    public static TextBox promoCodeTextBox = new TextBox("Promo Code textbox", "//input[@id='txt-promo']");
    public static BaseElement applyPromoCodeButton = new BaseElement("Apply promo code button", "//a[@id='a-apply']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/checkout/payment");
    }

    public static void setCreditCardInfo(CreditCard creditCard) {
        ReportUtils.disableLog(String.format("Set credit card: %s, %s, %s, %s, %s, %s, %s, %s",
                creditCard.firstName, creditCard.lastName, creditCard.cardNumber, creditCard.expireMonth,
                creditCard.expireYear, creditCard.cvv, creditCard.country, creditCard.postalCode));

        waitForBraintreeReady();

        firstNameTextBox.setText(creditCard.firstName);
        lastNameTextBox.setText(creditCard.lastName);
        postalCodeTextBox.setText(creditCard.postalCode);

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-number");
        cardNumberTextBox.setText(creditCard.cardNumber);
        DriverManager.getDriver().switchTo().defaultContent();

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-expirationMonth");
        monthTextBox.setText(creditCard.expireMonth);
        DriverManager.getDriver().switchTo().defaultContent();

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-expirationYear");
        yearTextBox.setText(creditCard.expireYear);
        DriverManager.getDriver().switchTo().defaultContent();

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-cvv");
        cvvTextBox.setText(creditCard.cvv);
        DriverManager.getDriver().switchTo().defaultContent();

        ReportUtils.enableLog();
    }

    public static void checkCreditCardValidation() {
        firstNameTextBox.shouldBeInRedBorder();
        lastNameTextBox.shouldBeInRedBorder();
        postalCodeTextBox.shouldBeInRedBorder();

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-number");
        cardNumberTextBox.shouldBeInRedBorder();
        DriverManager.getDriver().switchTo().defaultContent();

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-expirationMonth");
        monthTextBox.shouldBeInRedBorder();
        DriverManager.getDriver().switchTo().defaultContent();

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-expirationYear");
        yearTextBox.shouldBeInRedBorder();
        DriverManager.getDriver().switchTo().defaultContent();

        DriverManager.getDriver().switchTo().frame("braintree-hosted-field-cvv");
        cvvTextBox.shouldBeInRedBorder();
        DriverManager.getDriver().switchTo().defaultContent();
    }

    public static void removeItem(String itemName) {
        new BaseElement("Remove link of " + itemName,
                "//div[@class='payinfo-main']//div[@class='mag-name' and .='" + itemName + "']/following::a[.='remove']")
                .click();
    }

    public static void completePurchaseWithCreditCard(CreditCard creditCard) {
        ReportUtils.disableLog("Complete purchase with credit card: " + creditCard.cardNumber);
        setCreditCardInfo(creditCard);
        completePurchaseButton.click();
        loadingIcon.isDisplayed();
        loadingIcon.waitDisappear();
        Browser.waitPageDisplay("/thank-you/");
        ReportUtils.enableLog();
    }

    public static void completePurchase() {
        loadingIcon.waitDisappear();
        completePurchaseButton.click();
        Browser.waitPageDisplay("/thank-you/");
        Browser.waitPageLoadComplete();
    }

    public static void loginPaypal(String email, String password) {
        ReportUtils.disableLog("Login to paypal with email: " + email);
        String currentWindow = DriverManager.getDriver().getWindowHandle();
        sleep(1000);
        paypalButton.click();
        sleep(3000);
        for (String window : DriverManager.getDriver().getWindowHandles())
            DriverManager.getDriver().switchTo().window(window);
        PaypalLoginPage.emailTextBox.setText(email);
        PaypalLoginPage.passwordTextBox.setText(password);
        PaypalLoginPage.loginButton.click();
        PaypalLoginPage.agreeButton.click();
        sleep(3000);
        DriverManager.getDriver().switchTo().window(currentWindow);
        ReportUtils.enableLog();
    }

    public static void shouldShowExistingCreditCard(CreditCard creditCard) {
        existingCreditCardSection.shouldContainText("credit card ending in " + StringUtils.getRightString(creditCard.cardNumber, 4));
        existingCreditCardSection.shouldContainText("Expires:" + creditCard.expireMonth + "/" + creditCard.expireYear);
        existingCreditCardSection.shouldContainText(creditCard.fullName);
    }

    public static void waitForBraintreeReady() {
        loadingIcon.waitDisappear();
        new BaseElement("iframe", "//iframe").isDisplayed();
        sleep(1000);
    }
}