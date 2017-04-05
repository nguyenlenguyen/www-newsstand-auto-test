package pages;

import elements.BaseElement;
import elements.TextBox;

public class SignInPage extends MasterPage {

    public static TextBox emailTextBox = new TextBox("Email textbox", "//input[@id='email']");
    public static TextBox passwordTextBox = new TextBox("Password textbox", "//input[@id='password']");
    public static BaseElement signInButton = new BaseElement("Sign In button", "//div[contains(@class,'sign-in-btn')]/button");
    public static BaseElement createZinioAccountLink = new BaseElement("Create Zinio Account link", "//a[@href='/create-user']");
    public static BaseElement forgotPasswordLink = new BaseElement("Forgot Password link", "//a[@href='/forgot-password']");
    public static BaseElement errorTitle
            = new BaseElement("Error title", "//div[starts-with(@class,'feedback warning')]/div[@class='title']");
    public static BaseElement errorDescription
            = new BaseElement("Error description", "//div[starts-with(@class,'feedback warning')]/div[@class='description']");
    public static BaseElement emailHint = new BaseElement("Email hint", "//div[@class='email-hint']");
    public static BaseElement passwordHint = new BaseElement("Password hint", "//div[@class='password-hint']");

    // PAGE METHODS ====================================================================================================

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/sign-in");
    }

    public static void signIn(String email, String password) {
        emailTextBox.setText(email);
        passwordTextBox.setText(password);
        signInButton.click();
    }
}