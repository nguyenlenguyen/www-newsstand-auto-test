package pages;

import core.DriverManager;
import elements.BaseElement;
import elements.SelectBox;
import elements.TextBox;
import objects.CreditCard;
import utilities.ReportUtils;
import utilities.StringUtils;

public class PaymentInfoPage extends MasterPage {

    public static BaseElement addPaymentInfoLink = new BaseElement("Add payment method link", "//a[@class='add-payment']");
    public static BaseElement creditCardInfo = new BaseElement("Credit Card Info", "//div[@id='creditContentView']");
    public static BaseElement paypalInfo = new BaseElement("PayPal Info", "//div[@id='paypalContentView']");
    public static BaseElement paypalInfoEdit = new BaseElement("PayPal Info", "//div[@id='paypalContent']");
    public static BaseElement billingInfo = new BaseElement("Billing Info", "//div[@id='paymentCreditCard']");
    public static BaseElement creditCardBox = new BaseElement("Credit Card Box", "//input[@id='creditCardView']");
    public static BaseElement paypalBox = new BaseElement("PayPal Box", "//input[@id='paypalView']");
    public static BaseElement paypalBoxEdit = new BaseElement("PayPal Box", "//input[@id='paypal']");
    public static BaseElement editButton = new BaseElement("EDIT button", "//button[contains(@class,'btn-edit')]");
    public static BaseElement saveButton = new BaseElement("SAVE button", "//button[contains(@class,'btn-save')]");
    public static BaseElement cancelButton = new BaseElement("CANCEL button", "//button[contains(@class,'btn-cancel')]");
    public static BaseElement paypalButton = new BaseElement("PayPal button", "//img[contains(@src,'/pay-with-paypal.png')]");
    public static SelectBox countrySelectBox = new SelectBox("Country selectbox", "//select[@id='payment-country']");
    public static TextBox firstNameTextBox = new TextBox("First Name textbox", "//input[@id='payment-first-name']");
    public static TextBox lastNameTextBox = new TextBox("Last Name textbox", "//input[@id='payment-last-name']");
    public static TextBox cardNumberTextBox = new TextBox("Card Number textbox", "//input[@id='credit-card-number']");
    public static TextBox monthTextBox = new TextBox("Month textbox", "//input[@id='expiration-month']");
    public static TextBox yearTextBox = new TextBox("Year textbox", "//input[@id='expiration-year']");
    public static TextBox cvvTextBox = new TextBox("CVV textbox", "//input[@id='cvv']");
    public static TextBox zipCodeTextBox = new TextBox("Zip Code textbox", "//input[@id='payment-zip-code']");
    public static TextBox addressTextBox = new TextBox("Address textbox", "//input[@id='payment-address']");
    public static TextBox cityTextBox = new TextBox("City textbox", "//input[@id='payment-city']");
    public static TextBox stateTextBox = new TextBox("State textbox", "//input[@id='payment-state']");
    public static BaseElement successMessage = new BaseElement("Success message", "//div[@class='messagebox success']");
    public static BaseElement errorMessage = new BaseElement("Error message", "//div[@class='messagebox error']");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/account-settings/payment-information");
    }

    public static void setBillingInfo(String address, String city, String state) {
        addressTextBox.setText(address);
        cityTextBox.setText(city);
        stateTextBox.setText(state);
    }

    public static void checkCreditCardInfo(CreditCard creditCard) {
        String cardNumber = "xxxx-xxxx-xxxx-" + StringUtils.getRightString(creditCard.cardNumber, 4);
        creditCardInfo.findElement("Card Name", "//dd[1]").shouldHaveText(creditCard.fullName);
        creditCardInfo.findElement("Card Number", "//dd[2]").shouldHaveText(cardNumber);
        creditCardInfo.findElement("Card Expire Date", "//dd[3]").shouldHaveText(creditCard.expireMonth + "-" + creditCard.expireYear);
        creditCardInfo.findElement("Card Country", "//dd[4]").shouldHaveText(creditCard.country);
    }

    public static void checkBillingInfo(String address, String city, String state) {
        billingInfo.findElement("Address", "//dd[1]").shouldHaveText(address);
        billingInfo.findElement("City", "//dd[2]").shouldHaveText(city);
        billingInfo.findElement("State", "//dd[3]").shouldHaveText(state);
    }

    public static void setCreditCardInfo(CreditCard creditCard) {
        ReportUtils.disableLog(String.format("Set credit card: %s, %s, %s, %s, %s, %s, %s, %s",
                creditCard.firstName, creditCard.lastName, creditCard.cardNumber, creditCard.expireMonth,
                creditCard.expireYear, creditCard.cvv, creditCard.country, creditCard.postalCode));

        waitForBraintreeReady();
        firstNameTextBox.setText(creditCard.firstName);
        lastNameTextBox.setText(creditCard.lastName);
        countrySelectBox.selectOption(creditCard.country);
        zipCodeTextBox.setText(creditCard.postalCode);

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
        zipCodeTextBox.shouldBeInRedBorder();

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

    public static void loginPaypal(String email, String password) {
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
        DriverManager.getDriver().switchTo().window(currentWindow);
    }

    public static void waitForBraintreeReady() {
        new BaseElement("iframe", "//iframe[@id='braintree-hosted-field-number']").isDisplayed();
        sleep(3000);
    }
}
