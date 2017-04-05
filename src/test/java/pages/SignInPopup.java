package pages;

import elements.BaseElement;
import elements.TextBox;

public class SignInPopup extends MasterPage {
    public static BaseElement popup = new BaseElement("Sign In popup", "//div[@id='login-modal']//div[@class='modal-content']");
    public static TextBox emailTextBox = new TextBox("Email textbox", "//input[@name='email']");
    public static TextBox passwordTextBox = new TextBox("Password textbox", "//input[@name='password']");
    public static BaseElement signInButton = popup.findElement("Sign In button", "//button[@class='fixed primary signin']");
    public static BaseElement errorTitle
            = popup.findElement("Error title", "//div[starts-with(@class,'feedback warning')]/div[@class='title']");
    public static BaseElement errorDescription
            = popup.findElement("Error description", "//div[starts-with(@class,'feedback warning')]/div[@class='description']");

    // PAGE METHODS ====================================================================================================

    public static void signIn(String email, String password) {
        emailTextBox.setText(email);
        passwordTextBox.setText(password);
        signInButton.click();
    }
}