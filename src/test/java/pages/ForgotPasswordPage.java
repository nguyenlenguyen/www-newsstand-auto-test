package pages;

import elements.BaseElement;
import elements.TextBox;

public class ForgotPasswordPage extends MasterPage {

    public static TextBox emailTextBox = new TextBox("Email textbox", "//input[@name='email']");
    public static BaseElement resetPasswordButton = new BaseElement("Reset Password button", "//button[@id='reset_button']");
    public static BaseElement message = new BaseElement("Message", "//p[@class='message_text']");
    public static BaseElement checkEmailForm = new BaseElement("Check Email form", "//div[@id='checkEmail']");
    public static BaseElement okayButton = new BaseElement("OKAY button", "//button[contains(@class,'okayButton')]");

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/forgot-password");
    }

    public static void resetPassword(String email) {
        emailTextBox.setText(email);
        resetPasswordButton.click();
    }
}
