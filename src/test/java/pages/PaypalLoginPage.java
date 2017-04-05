package pages;


import elements.BaseElement;
import elements.TextBox;

public class PaypalLoginPage extends MasterPage {
    public static TextBox emailTextBox = new TextBox("Email textbox", "//input[@id='email']");
    public static TextBox passwordTextBox = new TextBox("Password textbox", "//input[@id='password']");
    public static BaseElement loginButton = new BaseElement("Login button", "//button[contains(@class,'loginButton')]");
    public static BaseElement agreeButton = new BaseElement("Agree button", "//button[contains(@class,'agreeButton')]");
}
