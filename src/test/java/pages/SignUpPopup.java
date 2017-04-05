package pages;

import elements.BaseElement;
import elements.TextBox;

public class SignUpPopup extends MasterPage {
    public static BaseElement popup = new BaseElement("Sign Up popup", "//div[@id='create-modal']//div[@class='modal-content']");
    public static BaseElement signInLink = popup.findElement("Sign In link", "//a[@id='link_signin']");
    public static TextBox emailTextBox = new TextBox("Email textbox", "//input[@id='email_new']");
    public static TextBox passwordTextBox = new TextBox("Password textbox", "//input[@id='password_new']");
    public static TextBox confirmPasswordTextBox = new TextBox("Confirm Password textbox", "//input[@id='confirm_password']");
    public static BaseElement createButton = popup.findElement("Create button", "//button[@class='fixed primary create']");
    public static BaseElement errorTitle
            = popup.findElement("Error title", "//div[starts-with(@class,'feedback warning')]/div[@class='title']");
    public static BaseElement errorDescription
            = popup.findElement("Error description", "//div[starts-with(@class,'feedback warning')]/div[@class='description']");
    public static BaseElement emailErrorMessage
            = popup.findElement("Email Error message", "//label[@id='email_new-error']");
    public static BaseElement passwordErrorMessage
            = popup.findElement("Password Error message", "//label[@id='password_new-error']");
    public static BaseElement confirmPasswordErrorMessage
            = popup.findElement("Confirm Password Error message", "//label[@id='confirm_password-error']");

    public static void signUp(String email, String password, String confirmPassword) {
        emailTextBox.setText(email);
        passwordTextBox.setText(password);
        confirmPasswordTextBox.setText(confirmPassword);
        createButton.click();
    }
}
