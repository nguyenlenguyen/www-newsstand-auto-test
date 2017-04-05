package pages;

import elements.BaseElement;
import elements.TextBox;

public class SignUpPage extends MasterPage {

    public static TextBox emailTextBox = new TextBox("Email textbox", "//input[@id='email_new']");
    public static TextBox passwordTextBox = new TextBox("Password textbox", "//input[@id='password_new']");
    public static TextBox confirmPasswordTextBox = new TextBox("Confirm Password textbox", "//input[@id='confirm_password']");
    public static BaseElement signUpButton = new BaseElement("Sign Up button", "//div[contains(@class,'sign-up-btn')]/button");
    public static BaseElement errorHint
            = new BaseElement("Error description", "//div[starts-with(@class,'feedback warning')]/div[@class='description']");
    public static BaseElement emailHint
            = new BaseElement("Email Error message", "//div[@class='email-hint']");
    public static BaseElement passwordHint
            = new BaseElement("Password Error message", "//input[@id='password_new']/following-sibling::div[@class='password-hint']");
    public static BaseElement confirmPasswordHint
            = new BaseElement("Confirm Password Error message", "//input[@id='confirm_password']/following-sibling::div[@class='password-hint']");

    // PAGE METHODS ====================================================================================================

    public static void shouldBeDisplayed() {
        shouldHaveUrl("/create-user");
    }

    public static void signUp(String email, String password, String confirmPassword) {
        emailTextBox.setText(email);
        passwordTextBox.setText(password);
        confirmPasswordTextBox.setText(confirmPassword);
        signUpButton.click();
    }
}