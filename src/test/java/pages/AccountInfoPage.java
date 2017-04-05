package pages;

import elements.BaseElement;
import elements.SelectBox;
import elements.TextBox;

public class AccountInfoPage extends MasterPage {

    public static TextBox firstNameTextBox = new TextBox("First Name textbox", "//input[@name='account[first_name]']");
    public static TextBox lastNameTextBox = new TextBox("Last Name textbox", "//input[@name='account[last_name]']");
    public static TextBox emailTextBox = new TextBox("Email Address textbox", "//input[@name='account[email]']");
    public static SelectBox genderSelectBox = new SelectBox("Gender selectbox", "//select[@id='sex']");
    public static SelectBox monthSelectBox = new SelectBox("Month selectbox", "//select[@name='birthday[month]']");
    public static SelectBox daySelectBox = new SelectBox("Day selectbox", "//select[@name='birthday[day]']");
    public static SelectBox yearSelectBox = new SelectBox("Year selectbox", "//select[@name='birthday[year]']");
    public static BaseElement saveProfileButton = new BaseElement("Save Profile button", "(//button[.='Save'])[1]");
    public static TextBox currentPasswordTextBox = new TextBox("Current Password textbox", "//input[@name='current_password']");
    public static TextBox newPasswordTextBox = new TextBox("New Password textbox", "//input[@name='new_password']");
    public static TextBox confirmPasswordTextBox = new TextBox("Confirm Password textbox", "//input[@name='new_password_confirm']");
    public static BaseElement saveLoginInfoButton = new BaseElement("Save Login Info button", "(//button[.='Save'])[2]");
    public static BaseElement successMessage = new BaseElement("Success message", "//div[@class='messagebox success']");
    public static BaseElement errorMessage = new BaseElement("Error message", "//div[@class='messagebox error']");
    public static BaseElement currentPasswordError = new BaseElement("Current Password Error", "//label[@id='current_password-error']");
    public static BaseElement newPasswordError = new BaseElement("New Password Error", "//label[@id='new_password-error']");
    public static BaseElement confirmPasswordError = new BaseElement("Confirm Password Error", "//label[@id='new_password_confirm-error']");

    public static void shouldBeDisplayed() {
        shouldHaveTitle("Account Settings");
    }

    public static void setProfileDetails(String fn, String ln, String email, String gender, String month, String day, String year) {
        if (fn != null) firstNameTextBox.setText(fn);
        if (ln != null) lastNameTextBox.setText(ln);
        if (email != null) emailTextBox.setText(email);
        if (gender != null) genderSelectBox.selectOption(gender);
        if (month != null) monthSelectBox.selectOption(month);
        if (day != null) daySelectBox.selectOption(day);
        if (year != null) yearSelectBox.selectOption(year);
        saveProfileButton.click();
    }

    public static void checkProfileDetails(String fn, String ln, String email, String gender, String month, String day, String year) {
        firstNameTextBox.shouldHaveText(fn);
        lastNameTextBox.shouldHaveText(ln);
        emailTextBox.shouldHaveText(email);
        genderSelectBox.shouldHaveText(gender);
        monthSelectBox.shouldHaveText(month);
        daySelectBox.shouldHaveText(day);
        yearSelectBox.shouldHaveText(year);
    }

    public static void setLoginInfo(String oldPassword, String newPassword, String confirmPassword) {
        if (oldPassword != null) currentPasswordTextBox.setText(oldPassword);
        if (newPassword != null) newPasswordTextBox.setText(newPassword);
        if (confirmPassword != null) confirmPasswordTextBox.setText(confirmPassword);
        saveLoginInfoButton.click();
    }
}
