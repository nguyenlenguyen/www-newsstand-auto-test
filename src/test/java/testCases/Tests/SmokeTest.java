package testCases.Tests;

import api.API;
import core.BaseAssert;
import core.Browser;
import elements.BaseElement;
import objects.Campaign;
import objects.CreditCard;
import objects.Publication;
import org.openqa.selenium.Keys;
import org.testng.annotations.Test;
import pages.*;
import testCases.BaseTests.SmokeTestBase;
import testData.GeneralData;
import utilities.ReportUtils;
import utilities.StringUtils;

public class SmokeTest extends SmokeTestBase {
    private String email;
    private String password;

    private String createNewUser() {
        email = StringUtils.randomString(10, "@zinio.com");
        password = StringUtils.randomNumber("P", 10);
        ReportUtils.disableLog("Create new account: " + email + "/" + password);
        HomePage.signUp(email, password);
        HomePage.settingIcon.isDisplayed();
        ReportUtils.enableLog();
        return API.getUserID(email);
    }

    @Test
    public void C12879_VERIFY_SIGN_IN_PAGE() {
        // Click on Sign In / Sign Up link on top header -> See Sign In Page
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, false);
        HomePage.openProductPage("magazine", publicationList[0]);
        ProductPage.signInLink.click();
        SignInPage.shouldBeDisplayed();

        // Enter invalid email & password -> see error hint
        SignInPage.emailTextBox.setText("zinio").pressKey(Keys.TAB);
        SignInPage.passwordTextBox.setText("123").pressKey(Keys.TAB);
        SignInPage.emailHint.shouldHaveText("Please enter a valid email address.");
        SignInPage.passwordHint.shouldHaveText("Password must be between 8 and 30 characters long. Please try again.");

        // Sign In with incorrect info -> error message
        SignInPage.signIn(GeneralData.USER_EMAIL, StringUtils.randomNumber("P", 10));
        SignInPage.errorDescription.shouldHaveText("Sorry. The password you entered does not match the one for your account. " +
                "Please try again, or click \"Forgot Your Password?\" for assistance...");

        // Sign In with correct info -> back to Product page with signed in user
        SignInPage.signIn(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD);
        ProductPage.shouldBeDisplayed();
        ProductPage.username.shouldHaveText(GeneralData.USER_EMAIL);

        endTest();
    }

    @Test
    public void C12880_VERIFY_SIGN_IN_POPUP() {
        // Buy an issue to see the create account popup
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, false);
        HomePage.buySubscribeIssue("buy", publicationList[0], true);

        // Click Sign In link on Create Account popup -> see Sign In Popup
        SignUpPopup.signInLink.click();
        SignInPopup.popup.shouldBeDisplayed();

        // Sign In with incorrect info -> error message
        SignInPopup.signIn(GeneralData.USER_EMAIL, StringUtils.randomNumber("P", 10));
        SignInPopup.errorTitle.shouldHaveText("Login failed");
        SignInPopup.errorDescription.shouldHaveText("Sorry. The password you entered does not match the one for your account. " +
                "Please try again, or click \"Forgot Your Password?\" for assistance...");

        // Sign In with correct info -> go to Checkout Payment page
        SignInPopup.signIn(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD);
        CheckoutPaymentPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12881_VERIFY_CREATE_ACCOUNT_PAGE() {
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, false);
        HomePage.signInLink.click();
        SignInPage.createZinioAccountLink.click();

        // Enter invalid values
        SignUpPage.emailTextBox.setText("abc").pressKey(Keys.TAB);
        SignUpPage.passwordTextBox.setText("12345 6789").pressKey(Keys.TAB);
        SignUpPage.confirmPasswordTextBox.setText("123456789").pressKey(Keys.TAB);

        // Verify error message
        SignUpPage.emailHint.shouldHaveText("Please enter a valid email address.");
        SignUpPage.passwordHint.shouldHaveText(
                "Password must be between 8 and 30 characters long and cannot contain spaces. Please try again.");
        SignUpPage.confirmPasswordHint.shouldHaveText("Passwords must match.");

        // Verify existing email
        SignUpPage.signUp(GeneralData.USER_EMAIL, "123456789", "123456789");
        SignUpPage.errorHint.shouldHaveText("Uh-oh. A user with the email address you entered already exists. " +
                "Please try entering a different email address.");

        // Verify create successful
        String email = StringUtils.randomString(10, "@zinio.com");
        SignUpPage.signUp(email, "123456789", "123456789");
        PostRegistrationPage.shouldBeDisplayed();
        PostRegistrationPage.username.shouldHaveText(email);

        endTest();
    }

    @Test
    public void C12882_VERIFY_CREATE_ACCOUNT_POPUP() {
        // Buy an issue to see the create account popup
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, false);
        HomePage.buySubscribeIssue("buy", publicationList[0], true);
        SignUpPopup.popup.shouldBeDisplayed();

        // Enter invalid values
        SignUpPopup.emailTextBox.setText("abc").pressKey(Keys.TAB);
        SignUpPopup.passwordTextBox.setText("12345 6789").pressKey(Keys.TAB);
        SignUpPopup.confirmPasswordTextBox.setText("123456789").pressKey(Keys.TAB);

        // Verify error message
        SignUpPopup.emailErrorMessage.shouldHaveText("Please enter a valid email address.");
        SignUpPopup.passwordErrorMessage.shouldHaveText(
                "Password must be between 8 and 30 characters long and cannot contain spaces. Please try again.");
        SignUpPopup.confirmPasswordErrorMessage.shouldHaveText("Passwords must match.");

        // Verify existing email
        SignUpPopup.signUp(GeneralData.USER_EMAIL, "123456789", "123456789");
        SignUpPopup.errorTitle.shouldHaveText("Error creating user");
        SignUpPopup.errorDescription.shouldHaveText("Uh-oh. A user with the email address you entered already exists. " +
                "Please try entering a different email address.");

        // Verify create successful
        SignUpPopup.signUp(StringUtils.randomString(10, "@zinio.com"), "123456789", "123456789");
        CheckoutPaymentPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12883_VERIFY_FORGOT_PASSWORD_PAGE() {
        // create new user
        email = StringUtils.randomString(10, "@zinio.com");
        password = StringUtils.randomNumber("P", 10);
        API.createNewUser(email, password);

        // Able to open forgot password page
        HomePage.signInLink.click();
        SignInPage.forgotPasswordLink.click();
        ForgotPasswordPage.shouldBeDisplayed();

        // Verify invalid email
        ForgotPasswordPage.resetPassword("abc");
        ForgotPasswordPage.message.shouldHaveText("Sorry, that email is invalid.");

        // Verify non-existing email
        ForgotPasswordPage.resetPassword(StringUtils.randomString(10, "@zinio.com"));
        ForgotPasswordPage.message.shouldHaveText("User does not exist");

        // Verify reset password successfully
        ForgotPasswordPage.resetPassword(this.email);
        ForgotPasswordPage.checkEmailForm.shouldContainText("We've sent you an email with instructions on how to reset your password.");
        ForgotPasswordPage.okayButton.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12884_VERIFY_PROFILE_DETAILS_IN_ACCOUNT_INFORMATION_PAGE() {
        // Go to account settings page
        createNewUser();
        HomePage.openAccountSettings("Account information");

        // Set Profile Details to an existing email
        AccountInfoPage.setProfileDetails("FN", "LN", GeneralData.USER_EMAIL, "Male", "December", "31", "2000");
        AccountInfoPage.errorMessage.shouldHaveText("A user with the email address you entered already exists. " +
                "Please try entering a different email address.");

        // Set Profile Details
        String newEmail = StringUtils.randomString(10, "@zinio.com");
        AccountInfoPage.setProfileDetails("FN", "LN", newEmail, "Male", "December", "31", "2000");
        AccountInfoPage.successMessage.shouldHaveText("Got it! Your Zinio login has been updated to " + newEmail);

        // Logout and re-login with old email -> cannot login
        AccountInfoPage.logOut();
        HomePage.signIn(email, password);
        SignInPage.errorDescription.shouldHaveText("Sorry. The password you entered does not match the one for your account. " +
                "Please try again, or click \"Forgot Your Password?\" for assistance...");

        // Login with new email -> can login
        SignInPage.signIn(newEmail, password);
        HomePage.username.shouldContainText("FN LN");

        // Account information page keeps correct info
        HomePage.openAccountSettings("Account information");
        AccountInfoPage.checkProfileDetails("FN", "LN", newEmail, "Male", "December", "31", "2000");

        endTest();
    }

    @Test
    public void C12885_VERIFY_CHANGE_PASSWORD_IN_ACCOUNT_INFORMATION_PAGE() {
        // Go to account settings page
        createNewUser();
        HomePage.openAccountSettings("Account information");

        // Set incorrect old password
        AccountInfoPage.setLoginInfo("987654321", "123456789", "123456789");
        AccountInfoPage.errorMessage.shouldHaveText("Old password incorrect.");

        // Set new password is the same to old password
        AccountInfoPage.setLoginInfo(password, password, password);
        AccountInfoPage.errorMessage.shouldHaveText("New password cannot be the same as the old password.");

        // Set invalid new password
        AccountInfoPage.setLoginInfo("123", "123", "123");
        String message = "Password must be between 8 and 30 characters long and cannot contain spaces. Please try again.";
        AccountInfoPage.currentPasswordError.shouldHaveText(message);
        AccountInfoPage.newPasswordError.shouldHaveText(message);
        AccountInfoPage.confirmPasswordError.shouldHaveText(message);

        // Set mismatch passwords
        AccountInfoPage.setLoginInfo(password, "123456789", "123456780");
        AccountInfoPage.confirmPasswordError.shouldHaveText("Passwords must match.");

        // Set valid login info
        String newPassword = StringUtils.randomNumber("P", 10);
        AccountInfoPage.setLoginInfo(password, newPassword, newPassword);
        AccountInfoPage.successMessage.shouldHaveText("Got it! Your password has been updated.");

        // Logout and re-login -> login successfully
        AccountInfoPage.logOut();
        HomePage.signIn(email, newPassword);
        HomePage.username.shouldContainText(email);

        endTest();
    }

    @Test
    public void C12886_VERIFY_SEARCH_FUNCTION() {
        // Click on a category result -> go to category page
        String categoryName = "Art";
        HomePage.search(categoryName, false);
        HomePage.searchResult.findElement("Category result", "//a[starts-with(@href,'/category/')]").hoverAndClick();
        CategoryPage.shouldBeDisplayed();
        CategoryPage.categoryFilter.shouldContainText(categoryName);

        // Click on a publication result -> go to Product page
        String publicationName = "Game";
        CategoryPage.search(publicationName, false);
        HomePage.searchResult.findElement("Publication result", "//a[starts-with(@href,'/magazine/')]").hoverAndClick();
        ProductPage.shouldBeDisplayed();

        // Search keyword and press enter
        HomePage.search(publicationName, true);
        SearchResultPage.shouldBeDisplayed();
        SearchResultPage.shouldShowMessage("You searched for \"" + publicationName + "\"");

        // Click on a result -> go to product page
        new BaseElement("Publication result", "//a[starts-with(@href,'/magazine/')]").click();
        ProductPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12887_VERIFY_BUY_ISSUE_BUTTON_IN_PUBLICATION_PAGE() {
        // Go to product page and buy issue
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);
        Publication publication = HomePage.buySubscribeIssue("buy", publicationList[0], false);
        CartPopup.popup.shouldContainText(publication.name);
        CartPopup.popup.shouldContainText("1 issue - " + publication.issueName);
        CartPopup.closeCart();
        CartPopup.popup.shouldNotBeDisplayed();
        ProductPage.cartCounter.shouldHaveText("1");
        ProductPage.buyIssueButton.shouldNotBeDisplayed();
        ProductPage.subscribeButton.shouldBeDisplayed();
        ProductPage.rightProceedButton.shouldBeDisplayed();
        ProductPage.rightViewCartLink.shouldBeDisplayed();

        // Verify the view cart link
        ProductPage.rightViewCartLink.click();
        CartPopup.popup.shouldBeDisplayed();

        // Verify the proceed checkout button
        CartPopup.closeCart();
        ProductPage.rightProceedButton.click();
        CheckoutPaymentPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12888_VERIFY_SUBSCRIBE_BUTTON_IN_PUBLICATION_PAGE() {
        // Go to product page and subscribe issue
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);
        Publication publication = HomePage.buySubscribeIssue("subscribe", publicationList[0], false).getAllInfo();

        CartPopup.popup.shouldContainText(publication.name);
        CartPopup.popup.shouldContainText(publication.subscriptionCredits + " issues");
        CartPopup.closeCart();
        CartPopup.popup.shouldNotBeDisplayed();
        ProductPage.cartCounter.shouldHaveText("1");
        ProductPage.buyIssueButton.shouldBeDisplayed();
        ProductPage.subscribeButton.shouldNotBeDisplayed();
        ProductPage.leftProceedButton.shouldBeDisplayed();
        ProductPage.leftViewCartLink.shouldBeDisplayed();

        // Verify the view cart link
        ProductPage.leftViewCartLink.click();
        CartPopup.popup.shouldBeDisplayed();

        // Verify the proceed checkout button
        CartPopup.closeCart();
        ProductPage.leftProceedButton.click();
        CheckoutPaymentPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12889_VERIFY_BUY_ISSUE_BUTTON_IN_PUBLISHER_PAGE() {
        // Go to Publisher page
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);
        HomePage.openProductPage("publisher", GeneralData.PUBLICATION_SPECIAL);
        PublisherPage.shouldBeDisplayed();
        PublisherPage.zinioLogo.shouldNotBeDisplayed();
        PublisherPage.menuBar.shouldNotBeDisplayed();
        PublisherPage.searchTextBox.shouldNotBeDisplayed();
        PublisherPage.searchIcon.shouldNotBeDisplayed();
        PublisherPage.shouldNotShowMessage("You may also like");
        PublisherPage.publisherLogo.shouldBeDisplayed();
        PublisherPage.cartIcon.shouldBeDisplayed();

        // Verify Buy issue
        String issueName = PublisherPage.issueName.getText();
        PublisherPage.buyIssueButton.click();
        CartPopup.popup.shouldContainText("1 issue - " + issueName);
        CartPopup.closeCart();
        CartPopup.popup.shouldNotBeDisplayed();
        PublisherPage.cartCounter.shouldHaveText("1");
        PublisherPage.buyIssueButton.shouldNotBeDisplayed();
        PublisherPage.subscribeButton.shouldBeDisplayed();
        PublisherPage.rightProceedButton.shouldBeDisplayed();
        PublisherPage.rightViewCartLink.shouldBeDisplayed();

        // Verify the view cart link
        PublisherPage.rightViewCartLink.click();
        CartPopup.popup.shouldBeDisplayed();

        // Verify the proceed checkout button
        CartPopup.closeCart();
        PublisherPage.rightProceedButton.click();
        CheckoutPaymentPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12890_VERIFY_SUBSCRIBE_BUTTON_IN_PUBLISHER_PAGE() {
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);

        // Go to Publisher page
        HomePage.openProductPage("publisher", GeneralData.PUBLICATION_SPECIAL);
        PublisherPage.shouldBeDisplayed();
        PublisherPage.zinioLogo.shouldNotBeDisplayed();
        PublisherPage.menuBar.shouldNotBeDisplayed();
        PublisherPage.searchTextBox.shouldNotBeDisplayed();
        PublisherPage.searchIcon.shouldNotBeDisplayed();
        PublisherPage.shouldNotShowMessage("You may also like");
        PublisherPage.publisherLogo.shouldBeDisplayed();
        PublisherPage.cartIcon.shouldBeDisplayed();

        // Verify Subscribe
        String subscriptionInfo = PublisherPage.subscriptionCredits.getText().replace("(", "").replace(")", "");
        PublisherPage.subscribeButton.click();
        CartPopup.popup.shouldContainText(subscriptionInfo);
        CartPopup.closeCart();
        CartPopup.popup.shouldNotBeDisplayed();
        PublisherPage.cartCounter.shouldHaveText("1");
        PublisherPage.buyIssueButton.shouldBeDisplayed();
        PublisherPage.subscribeButton.shouldNotBeDisplayed();
        PublisherPage.leftProceedButton.shouldBeDisplayed();
        PublisherPage.leftViewCartLink.shouldBeDisplayed();

        // Verify the view cart link
        PublisherPage.leftViewCartLink.click();
        CartPopup.popup.shouldBeDisplayed();

        // Verify the proceed checkout button
        CartPopup.closeCart();
        PublisherPage.leftProceedButton.click();
        CheckoutPaymentPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12891_VERIFY_VALIDATION_BETWEEN_BUY_AND_SUBSCRIBE_BUTTON() {
        createNewUser();

        // Buy a latest issue, then subscribe -> issue is replaced by the subscription
        Publication publication = HomePage.buySubscribeIssue("buy", publicationList[0], false);
        publication.subscriptionCredits = ProductPage.subscriptionCredits.getText().replace("(", "").replace(")", "");
        CartPopup.closeCart();
        ProductPage.subscribeButton.click();
        CartPopup.popup.shouldContainText(publication.name);
        CartPopup.popup.shouldContainText(publication.subscriptionCredits);
        CartPopup.popup.shouldNotContainText(publication.issueName);

        // Try to buy the latest issue again -> cannot buy
        CartPopup.closeCart();
        ProductPage.sleep(1000);
        ProductPage.cartCounter.shouldHaveText("1");
        ProductPage.buyIssueButton.click();
        ModalPopup.popup.shouldContainText("No need to purchase this issue.");
        ModalPopup.popup.shouldContainText("Your subscription includes the current issue.");
        ModalPopup.okThanksButton.shouldBeDisplayed();

        // Buy an older issue
        ModalPopup.okThanksButton.click();
        ProductPage.backIssueCover.click();
        ProductPage.cacheRefresh();
        String issueName = ProductPage.issueName.getText();
        ProductPage.buyIssueButton.click();
        CartPopup.popup.shouldContainText(issueName);
        CartPopup.closeCart();
        ProductPage.viewAllBackIssuesLink.click();
        BackIssuesPage.buyBackIssueButton.click();
        ModalPopup.popup.shouldContainText("No need to purchase this issue.");
        ModalPopup.popup.shouldContainText("This issue is already in your cart.");

        endTest();
    }

    @Test
    public void C12892_VERIFY_SHOPPING_CART_POPUP() {
        // Open the Product Page of an issue that is not latest
        HomePage.openProductPage("magazine", publicationList[0]);
        ProductPage.backIssueCover.getAt(2).click();
        ProductPage.cacheRefresh();
        Publication publication1 = new Publication(publicationList[0], ProductPage.publicationName.getText(),
                ProductPage.issueName.getText());
        publication1.subscriptionCredits = ProductPage.subscriptionCredits.getText().replace("(", "").replace(")", "");

        // Click on Subscribe button -> Back Issue popup appear
        ProductPage.subscribeButton.click();
        BackIssuePopup.popup.shouldBeDisplayed();

        // Click Subscription Only -> only subscription is added
        BackIssuePopup.subscriptionOnlyButton.click();
        CartPopup.popup.shouldContainText(publication1.subscriptionCredits);
        CartPopup.popup.shouldNotContainText(publication1.issueName);
        CartPopup.cartCounter.shouldHaveText("1");

        // Remove all items -> cart is empty
        CartPopup.removeItem(publication1.name);
        CartPopup.yourCartIsEmptyMessage.shouldBeDisplayed();
        CartPopup.cartCounter.shouldHaveText("0");

        // Click Continue Shopping link
        CartPopup.closeCart();
        CartPopup.popup.shouldNotBeDisplayed();

        // Click on Subscribe button again, now select Back + Subscription -> both are added to cart
        ProductPage.subscribeButton.click();
        BackIssuePopup.backAndSubscriptionButton.click();
        String oldPrice1 = CartPopup.subTotalPrice.getText();
        CartPopup.popup.shouldContainText(publication1.subscriptionCredits);
        CartPopup.popup.shouldContainText(publication1.issueName);
        CartPopup.cartCounter.shouldHaveText("2");

        // Buy 1 other item -> add to cart, total price update
        Publication publication2 = HomePage.buySubscribeIssue("buy", publicationList[1], false);
        String oldPrice2 = CartPopup.subTotalPrice.getText();
        CartPopup.popup.shouldContainText(publication2.name);
        CartPopup.cartCounter.shouldHaveText("3");
        BaseAssert.assertEquals("Sub Total is updated", oldPrice2.equals(oldPrice1), false);

        // Remove 1 item -> remove from cart, total price update
        CartPopup.removeItem(publication2.name);
        CartPopup.popup.shouldNotContainText(publication2.name);
        CartPopup.cartCounter.shouldHaveText("2");
        BaseAssert.assertEquals("Sub Total is updated", CartPopup.subTotalPrice.getText().equals(oldPrice2), false);

        endTest();
    }

    @Test
    public void C12893_VERIFY_CREDIT_CARD_SECTION_IN_ADD_EDIT_BILLING_INFO_PAGE() {
        CreditCard creditCard1 = new CreditCard().getValidCard();

        // Complete purchase without entering any billing info
        createNewUser();
        HomePage.buySubscribeIssue("buy", publicationList[0], true);
        CheckoutPaymentPage.waitForBraintreeReady();
        CheckoutPaymentPage.completePurchaseButton.click();
        CheckoutPaymentPage.checkCreditCardValidation();

        // Complete the order with valid billing info
        // Buy another issue -> billing info is remember
        CheckoutPaymentPage.completePurchaseWithCreditCard(creditCard1);
        HomePage.buySubscribeIssue("buy", publicationList[1], true);
        CheckoutPaymentPage.shouldShowExistingCreditCard(creditCard1);

        endTest();
    }

    @Test
    public void C12894_VERIFY_PAYPAL_SECTION_IN_ADD_EDIT_BILLING_INFO_PAGE() {
        createNewUser();
        HomePage.buySubscribeIssue("buy", publicationList[0], true);

        // Login with PayPal
        String email = StringUtils.randomString(10, "@zinio.com");
        CheckoutPaymentPage.paypalRadioBox.click();
        CheckoutPaymentPage.loginPaypal(email, "abc");
        CheckoutPaymentPage.paypalInfo.shouldContainText(email);

        // Complete Purchase
        CheckoutPaymentPage.completePurchaseButton.click();
        ThankYouPage.shouldBeDisplayed(true);

        endTest();
    }

    @Test
    public void C12895_VERIFY_ORDER_DETAILS_SECTION_IN_ADD_EDIT_BILLING_INFO_PAGE() {
        HomePage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);

        // Buy & subscribe some PUBLICATIONS and proceed to checkout
        Publication publication1 = HomePage.buySubscribeIssue("buy", publicationList[0], false);
        Publication publication2 = HomePage.buySubscribeIssue("subscribe", publicationList[1], true);

        // Try to remove an item from cart
        String oldPrice = CheckoutPaymentPage.magazinePrice.getText();
        CheckoutPaymentPage.removeItem(publication1.name);
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(publication1.name);
        BaseAssert.assertEquals("Total Price is updated", !CheckoutPaymentPage.magazinePrice.getText().equals(oldPrice), true);

        // Remove all items from cart
        CheckoutPaymentPage.removeItem(publication2.name);
        CheckoutPaymentPage.shouldShowMessage("Your Cart is Empty");
        CheckoutPaymentPage.continueShoppingLinkCartEmpty.shouldBeDisplayed();

        // Continue Shopping link shows Main Shop page
        CheckoutPaymentPage.continueShoppingLinkCartEmpty.click();
        MainShopPage.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C12896_VERIFY_BUYING_BACK_SPECIAL_ISSUES_FROM_BACK_ISSUE_PAGE() {
        createNewUser();
        HomePage.openProductPage("magazine", GeneralData.PUBLICATION_SPECIAL);

        // Buy back issue
        ProductPage.viewAllBackIssuesLink.click();
        BackIssuesPage.buyBackIssueButton.click();
        CartPopup.popup.shouldBeDisplayed();
        CartPopup.cartCounter.shouldHaveText("1");

        // Buy special issue
        CartPopup.closeCart();
        BackIssuesPage.specialIssuesTab.click();
        BackIssuesPage.buySpecialIssueButton.click();
        CartPopup.popup.shouldBeDisplayed();
        CartPopup.cartCounter.shouldHaveText("2");

        endTest();
    }

    @Test
    public void C12897_VERIFY_BUYING_BACK_SPECIAL_ISSUES_FROM_PRODUCT_PAGE() {
        createNewUser();

        // Buy back issue from Product page
        HomePage.openProductPage("magazine", GeneralData.PUBLICATION_SPECIAL);
        ProductPage.viewAllBackIssuesLink.click();
        BackIssuesPage.backIssuesTab.shouldBeActive();
        BackIssuesPage.coverImageBackIssue.click();
        String issueName1 = ProductPage.issueName.getText();
        ProductPage.buyIssueButton.click();
        CartPopup.popup.shouldContainText(issueName1);
        CartPopup.cartCounter.shouldHaveText("1");

        // Buy special issue from Product page
        CartPopup.closeCart();
        ProductPage.viewAllSpecialIssuesLink.click();
        BackIssuesPage.specialIssuesTab.shouldBeActive();
        BackIssuesPage.coverImageSpecialIssue.click();
        String issueName2 = ProductPage.issueName.getText();
        ProductPage.cacheRefresh();
        ProductPage.buyIssueButton.click();
        CartPopup.popup.shouldContainText(issueName1);
        CartPopup.popup.shouldContainText(issueName2);
        CartPopup.cartCounter.shouldHaveText("2");

        endTest();
    }

    @Test
    public void C12898_VERIFY_BUYING_BACK_SPECIAL_ISSUES_FROM_PUBLISHER_BACK_ISSUES_PAGE() {
        createNewUser();
        HomePage.openProductPage("publisher", GeneralData.PUBLICATION_SPECIAL);

        // Click on View All link of Special Issues
        PublisherPage.viewAllSpecialIssuesLink.click();
        PublisherBackIssuesPage.shouldBeDisplayed();
        PublisherBackIssuesPage.specialIssuesTab.shouldBeActive();

        // Click on View All link of Recent Issues
        PublisherBackIssuesPage.goBack();
        PublisherPage.viewAllBackIssuesLink.click();
        PublisherBackIssuesPage.shouldBeDisplayed();
        PublisherBackIssuesPage.backIssuesTab.shouldBeActive();

        // Buy back issue
        PublisherBackIssuesPage.buyBackIssueButton.click();
        CartPopup.popup.shouldBeDisplayed();
        CartPopup.cartCounter.shouldHaveText("1");

        // Buy special issue
        CartPopup.closeCart();
        PublisherBackIssuesPage.specialIssuesTab.click();
        PublisherBackIssuesPage.buySpecialIssueButton.click();
        CartPopup.popup.shouldBeDisplayed();
        CartPopup.cartCounter.shouldHaveText("2");

        endTest();
    }

    @Test
    public void C12899_VERIFY_PREVENT_PURCHASING_AN_ALREADY_PURCHASED_ISSUE() {
        // Complete the purchase of 2 subscriptions
        createNewUser();
        HomePage.buySubscribeIssue("subscribe", publicationList[0], false);
        HomePage.buySubscribeIssue("buy", publicationList[1], true);
        CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getValidCard());

        // Try to buy the latest issue of subscribed publication -> failed at add to cart
        HomePage.buySubscribeIssue("buy", publicationList[0], false);
        ModalPopup.popup.shouldContainText("No need to purchase this issue.");
        ModalPopup.popup.shouldContainText("This issue is already in your library.");

        // Try to buy the already bought issue again -> failed at add to cart
        HomePage.buySubscribeIssue("buy", publicationList[1], false);
        ModalPopup.popup.shouldContainText("No need to purchase this issue.");
        ModalPopup.popup.shouldContainText("This issue is already in your library.");

        // Logout and try to buy the issues again
        ModalPopup.okThanksButton.click();
        ProductPage.logOut();
        Publication publication1 = HomePage.buySubscribeIssue("buy", publicationList[0], false);
        Publication publication2 = HomePage.buySubscribeIssue("buy", publicationList[1], false);
        Publication publication3 = HomePage.buySubscribeIssue("buy", publicationList[2], true);
        SignUpPopup.signInLink.click();
        SignInPopup.signIn(email, password);

        // Popup will be displayed and show already purchased items
        CartPopupReview.popup.shouldBeDisplayed();
        CartPopupReview.popup.shouldContainText("No need to purchase these items. " +
                "The following items are already in your library and will be removed from your cart.");
        CartPopupReview.popup.shouldContainText(publication1.name);
        CartPopupReview.popup.shouldContainText(publication2.name);
        CartPopupReview.popup.shouldNotContainText(publication3.name);

        // Click OK THANKS button -> purchased items will be removed from cart
        CartPopupReview.okThanksButton.click();
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(publication1.name);
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(publication2.name);
        CheckoutPaymentPage.orderDetailSection.shouldContainText(publication3.name);

        endTest();
    }

    @Test
    public void C12900_VERIFY_THANK_YOU_AND_LIBRARY_PAGE() {
        createNewUser();

        // Verify thank you single fail
        HomePage.buySubscribeIssue("buy", publicationList[0], true);
        CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getInvalidCard());
        ThankYouPage.shouldBeDisplayed(false, 1);

        // Verify thank you multiple fail
        HomePage.buySubscribeIssue("subscribe", publicationList[1], true);
        CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getInvalidCard());
        ThankYouPage.shouldBeDisplayed(false, 2);

        // Verify thank you multiple successful
        ThankYouPage.cartIcon.click();
        CartPopup.proceedToCheckoutButton.click();
        CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getValidCard());
        ThankYouPage.shouldBeDisplayed(true, 2);

        // Verify thank you single successful
        HomePage.buySubscribeIssue("buy", publicationList[2], true);
        CheckoutPaymentPage.completePurchaseButton.click();
        ThankYouPage.shouldBeDisplayed(true, 1);

        // Continue Shopping button go to Main Shop page
        ThankYouPage.continueShoppingButton.click();
        MainShopPage.shouldBeDisplayed();

        // Start Reading link go to Library page
        MainShopPage.goBack();
        ThankYouPage.startReadingLink.click();
        MyLibraryPage.shouldBeDisplayed();
        MyLibraryPage.publicationName.shouldHaveQuantity(3);

        // Click on an item in Library show reader page
        MyLibraryPage.publicationName.click();
        MyLibraryPage.switchToNewTab();
        MyLibraryPage.shouldHaveTitle("Reader");

        endTest();
    }

    @Test
    public void C12902_VERIFY_SUBSCRIPTIONS_PAGE() {
        createNewUser();

        // Go to Subscriptions page -> see blank page with message
        HomePage.openAccountSettings("Subscriptions");
        SubscriptionsPage.shouldBeDisplayed();
        SubscriptionsPage.emptyMessageTitle.shouldHaveText("You don't have any subscriptions yet.");
        SubscriptionsPage.emptyMessageLink.shouldHaveText("Browse the shop for your favorite titles and subscribe to never miss an issue.");
        SubscriptionsPage.subscription.shouldNotBeDisplayed();

        // Subscribe a publication -> appears in Subscriptions page
        Publication publication = HomePage.buySubscribeIssue("subscribe", publicationList[0], true).getAllInfo();
        String countBase = publication.subscriptionCredits + " issues";
        CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getValidCard());
        ThankYouPage.openAccountSettings("Subscriptions");
        SubscriptionsPage.shouldShowSubscription(1, publication.name, countBase, "Active", "Auto Renew on");

        // Turn off auto renew will show confirmation
        SubscriptionsPage.clickRenewLink(1);
        SubscriptionsPopup.popup.shouldBeDisplayed();

        // Click Cancel will just close the popup
        SubscriptionsPopup.cancelButton.click();
        SubscriptionsPopup.popup.shouldNotBeDisplayed();
        SubscriptionsPage.shouldShowSubscription(1, publication.name, countBase, "Active", "Auto Renew on");

        // Click Confirm will stop auto renew
        SubscriptionsPage.clickRenewLink(1);
        SubscriptionsPopup.confirmButton.click();
        SubscriptionsPage.shouldShowSubscription(1, publication.name, countBase, "Active", "Start Auto Renew");

        // Click Start Auto Renew will turn on auto renew
        SubscriptionsPage.clickRenewLink(1);
        SubscriptionsPage.shouldShowSubscription(1, publication.name, countBase, "Active", "Auto Renew on");

        // Subscribe the same publication again will add a pending one
        HomePage.buySubscribeIssue("subscribe", publicationList[0], true);
        CheckoutPaymentPage.completePurchase();
        ThankYouPage.openAccountSettings("Subscriptions");
        SubscriptionsPage.shouldShowSubscription(1, publication.name, countBase, "Pending", null);
        SubscriptionsPage.shouldShowSubscription(2, publication.name, countBase, "Active", "Auto Renew on");

        endTest();
    }

    @Test
    public void C12903_VERIFY_HOME_MAIN_SHOP_CATEGORY_PAGE() {
        // Verify Legal Notice page
        HomePage.privacyPolicyLink.click();
        Browser.switchToNewTab();
        Browser.shouldHaveTitle("Zinio - Legal");
        Browser.closeNewTab();

        // Verify open publication from Home Page
        HomePage.heroBanner.shouldBeDisplayed();
        HomePage.publicationCover.click();
        ProductPage.shouldBeDisplayed();

        // Verify open publication from Main Shop Page
        ProductPage.openMainShopPage();
        MainShopPage.publicationCover.click();
        ProductPage.shouldBeDisplayed();

        // Verify open publication from Category Page
        ProductPage.openCategoryPage(API.getPublicationInfo(publicationList[0], "category_id"));
        CategoryPage.publicationCover.click();
        ProductPage.shouldBeDisplayed();

        // Verify changing currency
        String currency = "AUD";
        ProductPage.openProductPage("magazine", publicationList[0]);
        ProductPage.selectCurrency(currency);
        ProductPage.subscribePrice.shouldContainText(currency);
        ProductPage.buyPrice.shouldContainText(currency);

        // Verify changing newsstand
        ProductPage.selectNewsstand("Australia");
        ProductPage.newsstandIcon.shouldHaveAttribute("class", "current-country au");

        // Verify changing language
        ProductPage.selectLanguage("Italiano");
        ProductPage.languageIcon.shouldHaveText("Italiano");

        endTest();
    }

    @Test
    public void C12904_VERIFY_ALL_PAGES_WHEN_ALLOW_ADULT_CONTENT_IS_ON() {
        String categoryID = "1900";
        HomePage.openCategoryPage(categoryID);
        String publicationID = CategoryPage.getPublicationID(1);
        String latestIssueID = API.getPublicationInfo(publicationID, "latest_issue_id");

        API.setNewsstandInfo(GeneralData.US_NEWSSTAND_ID, "content_rating_max", "40");
        API.setPublicationInfo(publicationID, "content_rating", "20");

        // Login user and turn ON the allow adult content of user -> should see adult content
        MasterPage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);
        MasterPage.setAllowAdultContent(true);
        MasterPage.verifyAdultContentOnPages(true, publicationID, latestIssueID, categoryID);

        // reset data
        API.setPublicationInfo(publicationID, "content_rating", "15");
        endTest();
    }

    @Test
    public void C12905_VERIFY_ALL_PAGES_WHEN_ALLOW_ADULT_CONTENT_IS_OFF() {
        String categoryID = "1900";
        HomePage.openCategoryPage(categoryID);
        String publicationID = CategoryPage.getPublicationID(1);
        String latestIssueID = API.getPublicationInfo(publicationID, "latest_issue_id");

        API.setNewsstandInfo(GeneralData.US_NEWSSTAND_ID, "content_rating_max", "40");
        API.setPublicationInfo(publicationID, "content_rating", "20");

        // Login user and turn OFF the allow adult content -> should not see adult content
        MasterPage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);
        MasterPage.setAllowAdultContent(false);
        MasterPage.verifyAdultContentOnPages(false, publicationID, latestIssueID, categoryID);

        // Logout user -> should see adult content
        MasterPage.logOut();
        MasterPage.verifyAdultContentOnPages(true, publicationID, latestIssueID, categoryID);

        // reset data
        API.setPublicationInfo(publicationID, "content_rating", "15");
        endTest();
    }

    @Test
    public void C12906_VERIFY_ALL_PAGES_WHEN_ALLOW_ADULT_CONTENT_IS_ON_BUT_RATING_IS_OVER_MAX_RATING() {
        String categoryID = "1900";
        HomePage.openCategoryPage(categoryID);
        String publicationID = CategoryPage.getPublicationID(1);
        String latestIssueID = API.getPublicationInfo(publicationID, "latest_issue_id");

        API.setNewsstandInfo(GeneralData.US_NEWSSTAND_ID, "content_rating_max", "40");
        API.setPublicationInfo(publicationID, "content_rating", "50");

        // Login user and turn ON the allow adult content of user -> still NOT see adult content
        MasterPage.initAccount(GeneralData.USER_EMAIL, GeneralData.USER_PASSWORD, true);
        MasterPage.setAllowAdultContent(true);
        MasterPage.verifyAdultContentOnPages(false, publicationID, latestIssueID, categoryID);

        // Logout user -> still not see adult content
        MasterPage.logOut();
        MasterPage.verifyAdultContentOnPages(false, publicationID, latestIssueID, categoryID);

        // reset data
        API.setPublicationInfo(publicationID, "content_rating", "15");
        endTest();
    }

    @Test
    public void C13267_VERIFY_FREE_ISSUES_PAGE_WITH_LOGGED_IN_USER() {
        String userID = createNewUser();
        HomePage.openFreeIssuesPage();

        // Click on a free issue -> popup appears
        String issueID1 = FreeIssuePage.getFreeIssueID("issue", 1);
        FreeIssuePage.issueCoverImage.getAt(1).click();
        FreeIssuePopup.shouldBeDisplayed();
        FreeIssuePopup.popup.shouldContainText("This free issue has been added to your library.");

        // Click on Continue Shopping button -> popup disappears
        FreeIssuePopup.continueShoppingButton.click();
        FreeIssuePopup.popup.shouldNotBeDisplayed();

        // Click on the free issue again -> message already purchased
        API.waitItemInLibrary(userID, issueID1);
        FreeIssuePage.issueCoverImage.getAt(1).click();
        ModalPopup.shouldShowMessage("This issue is already in your library.");

        // Close the popup and click on free subscription -> popup is displayed
        ModalPopup.okThanksButton.click();
        String pubID = FreeIssuePage.getFreeIssueID("subscription", 1);
        String issueID2 = API.getPublicationInfo(pubID, "latest_issue_id");
        String pubName = API.getPublicationInfo(pubID, "name");
        FreeIssuePage.subscriptionCoverImage.getAt(1).click();
        FreeIssuePopup.shouldBeDisplayed();
        FreeIssuePopup.popup.shouldContainText("This free issue has been added to your library.");

        // Click on Go to my library -> issues appear in Library
        API.waitItemInLibrary(userID, issueID1, issueID2);
        FreeIssuePopup.gotoMyLibraryLink.click();
        MyLibraryPage.shouldShowPublication(issueID1);
        MyLibraryPage.shouldShowPublication(issueID2);

        // Go to Subscriptions page in Account Settings -> should see the subscription
        MyLibraryPage.openAccountSettings("Subscriptions");
        SubscriptionsPage.subscription.shouldContainText(pubName);

        endTest();
    }

    @Test
    public void C13271_VERIFY_FREE_ISSUES_PAGE_WITH_SIGN_IN_POPUP() {
        // Create new user
        email = StringUtils.randomString(10, "@zinio.com");
        password = StringUtils.randomNumber("P", 10);
        API.createNewUser(email, password);

        // Click on a free issue -> Sign Up Popup appears
        HomePage.openFreeIssuesPage();
        FreeIssuePage.issueCoverImage.getAt(1).click();
        SignUpPopup.popup.shouldBeDisplayed();

        // Sign in -> Free Issue popup appears
        SignUpPopup.signInLink.click();
        SignInPopup.signIn(email, password);
        FreeIssuePopup.shouldBeDisplayed();

        // Sign out, then click on a subscription -> Sign Up Popup appears
        FreeIssuePopup.continueShoppingButton.click();
        FreeIssuePage.logOut();
        HomePage.openFreeIssuesPage();
        FreeIssuePage.subscriptionCoverImage.getAt(1).click();
        SignUpPopup.popup.shouldBeDisplayed();

        // Sign in -> Free Issue popup appears
        SignUpPopup.signInLink.click();
        SignInPopup.signIn(email, password);
        FreeIssuePopup.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C13272_VERIFY_FREE_ISSUES_PAGE_WITH_CREATE_USER_POPUP() {
        HomePage.openFreeIssuesPage();

        // Click on a free issue -> Sign Up Popup appears
        FreeIssuePage.issueCoverImage.getAt(1).click();
        SignUpPopup.popup.shouldBeDisplayed();

        // Sign up new user -> Free Issue popup appears
        email = StringUtils.randomString(10, "@zinio.com");
        password = StringUtils.randomNumber("P", 10);
        SignUpPopup.signUp(email, password, password);
        FreeIssuePopup.shouldBeDisplayed();

        // Sign out, then click on a subscription -> Sign Up Popup appears
        FreeIssuePopup.continueShoppingButton.click();
        FreeIssuePage.logOut();
        HomePage.openFreeIssuesPage();
        FreeIssuePage.subscriptionCoverImage.getAt(1).click();
        SignUpPopup.popup.shouldBeDisplayed();

        // Sign in -> Free Issue popup appears
        email = StringUtils.randomString(10, "@zinio.com");
        password = StringUtils.randomNumber("P", 10);
        SignUpPopup.signUp(email, password, password);
        FreeIssuePopup.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C13274_VERIFY_COUNTRY_RESTRICTION_FOR_SUBSCRIBE_PUBLICATIONS() {
        // Set country restriction for some publications
        API.addCountryRestriction(publicationList[8], "US");
        API.addCountryRestriction(publicationList[9], "US");

        // Login with a user with an existing , subscribe some restricted publications, then proceed to checkout
        createNewUser();
        Publication restrictPub1 = HomePage.buySubscribeIssue("subscribe", publicationList[8], false);
        Publication allowPub = HomePage.buySubscribeIssue("subscribe", publicationList[0], true);
        CheckoutPaymentPage.setCreditCardInfo(new CreditCard().getValidCard());
        CheckoutPaymentPage.completePurchaseButton.click();

        // Checkout Review popup is displayed
        CartPopupReview.popup.shouldBeDisplayed();
        CartPopupReview.popup.shouldContainText("This item is not available for purchase in the country associated " +
                "with your credit card. It will be removed from your order.");
        CartPopupReview.popup.shouldContainText(restrictPub1.name);
        CartPopupReview.popup.shouldNotContainText(allowPub.name);
        CartPopupReview.okThanksButton.shouldBeDisplayed();

        // Close popup -> item is removed
        CartPopupReview.okThanksButton.click();
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(restrictPub1.name);
        CheckoutPaymentPage.orderDetailSection.shouldContainText(allowPub.name);

        // Subscribe those publications again and proceed to checkout -> see popup
        HomePage.buySubscribeIssue("subscribe", publicationList[8], false);
        Publication restrictPub2 = HomePage.buySubscribeIssue("subscribe", publicationList[9], true);
        CartPopupReview.popup.shouldBeDisplayed();
        CartPopupReview.popup.shouldContainText("These items are not available for purchase in the country associated " +
                "with your credit card. They will be removed from your order.");
        CartPopupReview.popup.shouldContainText(restrictPub1.name);
        CartPopupReview.popup.shouldContainText(restrictPub2.name);
        CartPopupReview.popup.shouldNotContainText(allowPub.name);
        CartPopupReview.okThanksButton.shouldBeDisplayed();

        // Close popup -> items are removed
        CartPopupReview.okThanksButton.click();
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(restrictPub1.name);
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(restrictPub2.name);
        CheckoutPaymentPage.orderDetailSection.shouldContainText(allowPub.name);

        // reset data
        API.removeCountryRestriction(publicationList[8], "US");
        API.removeCountryRestriction(publicationList[9], "US");

        endTest();
    }

    @Test
    public void C13275_VERIFY_COUNTRY_RESTRICTION_FOR_PURCHASE_SINGLE_ISSUE() {
        // Set country restriction for some publications
        API.addCountryRestriction(publicationList[8], "US");
        API.addCountryRestriction(publicationList[9], "US");

        // Login with a user with an existing , subscribe some restricted publications, then proceed to checkout
        createNewUser();
        Publication restrictPub1 = HomePage.buySubscribeIssue("buy", publicationList[8], false);
        Publication allowPub = HomePage.buySubscribeIssue("buy", publicationList[0], true);
        CheckoutPaymentPage.setCreditCardInfo(new CreditCard().getValidCard());
        CheckoutPaymentPage.completePurchaseButton.click();

        // Checkout Review popup is displayed
        CartPopupReview.popup.shouldBeDisplayed();
        CartPopupReview.popup.shouldContainText("This item is not available for purchase in the country associated " +
                "with your credit card. It will be removed from your order.");
        CartPopupReview.popup.shouldContainText(restrictPub1.name);
        CartPopupReview.popup.shouldNotContainText(allowPub.name);
        CartPopupReview.okThanksButton.shouldBeDisplayed();

        // Close popup -> item is removed
        CartPopupReview.okThanksButton.click();
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(restrictPub1.name);
        CheckoutPaymentPage.orderDetailSection.shouldContainText(allowPub.name);

        // Subscribe those publications again and proceed to checkout -> see popup
        HomePage.buySubscribeIssue("buy", publicationList[8], false);
        Publication restrictPub2 = HomePage.buySubscribeIssue("buy", publicationList[9], true);
        CartPopupReview.popup.shouldBeDisplayed();
        CartPopupReview.popup.shouldContainText("These items are not available for purchase in the country associated " +
                "with your credit card. They will be removed from your order.");
        CartPopupReview.popup.shouldContainText(restrictPub1.name);
        CartPopupReview.popup.shouldContainText(restrictPub2.name);
        CartPopupReview.popup.shouldNotContainText(allowPub.name);
        CartPopupReview.okThanksButton.shouldBeDisplayed();

        // Close popup -> items are removed
        CartPopupReview.okThanksButton.click();
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(restrictPub1.name);
        CheckoutPaymentPage.orderDetailSection.shouldNotContainText(restrictPub2.name);
        CheckoutPaymentPage.orderDetailSection.shouldContainText(allowPub.name);

        // reset data
        API.removeCountryRestriction(publicationList[8], "US");
        API.removeCountryRestriction(publicationList[9], "US");

        endTest();
    }

    @Test
    public void C13374_VERIFY_PURCHASE_WITH_PUBLIC_COUPON() {
        // Get Campaign Info
        Campaign campaign = new Campaign(GeneralData.CAMPAIGN_PUBLIC).getCampaignInfo();
        String newPrice = "USD $"
                + Integer.toString((int) (Integer.parseInt(GeneralData.subscriptionPrice) * (1 - campaign.percentNumber)));

        // Open the Product Page -> the public coupon is displayed
        createNewUser();
        HomePage.openProductPage("magazine", campaign.publicationID);
        ProductPage.couponInfo.shouldHaveText("SPECIAL: " + campaign.couponName);
        ProductPage.subscribePrice.shouldContainText(newPrice);

        // Subscribe -> price is correct in the cart popup
        ProductPage.subscribeButton.click();
        CartPopup.subTotalPrice.shouldContainText(newPrice);

        // Proceed to checkout -> price is correct in Checkout page
        CartPopup.proceedToCheckoutButton.click();
        CheckoutPaymentPage.magazinePrice.shouldContainText(newPrice);
        CheckoutPaymentPage.itemPrice.shouldContainText(newPrice);

        endTest();
    }

    @Test
    public void C13375_VERIFY_PURCHASE_WITH_PRIVATE_COUPON() {
        // Get Campaign Info
        Campaign campaign = new Campaign(GeneralData.CAMPAIGN_PRIVATE).getCampaignInfo();
        String originalPrice = "USD $" + GeneralData.subscriptionPrice;
        String newPrice = "USD $"
                + Integer.toString((int) (Integer.parseInt(GeneralData.subscriptionPrice) * (1 - campaign.percentNumber)));

        // Open the Product Page -> the public coupon is displayed
        createNewUser();
        HomePage.openProductPage("magazine", campaign.publicationID);
        ProductPage.couponInfo.shouldNotBeDisplayed();
        ProductPage.subscribePrice.shouldContainText(originalPrice);

        // Subscribe -> price is correct in the cart popup
        ProductPage.subscribeButton.click();
        CartPopup.subTotalPrice.shouldContainText(originalPrice);

        // Proceed to checkout -> price is correct in Checkout page
        CartPopup.proceedToCheckoutButton.click();
        CheckoutPaymentPage.magazinePrice.shouldContainText(originalPrice);
        CheckoutPaymentPage.itemPrice.shouldContainText(originalPrice);

        // Apply the private coupon code -> price is reduced correctly
        CheckoutPaymentPage.promoCodeTextBox.setText(GeneralData.CAMPAIGN_PRIVATE);
        CheckoutPaymentPage.applyPromoCodeButton.click();
        CheckoutPaymentPage.magazinePrice.shouldContainText(newPrice);
        CheckoutPaymentPage.itemPrice.shouldContainText(newPrice);

        endTest();
    }

    @Test
    public void C13717_VERIFY_EXPLORE_READ_ARTICLE_PAGE() {
        // Click Explore menu -> Explore Page is displayed
        HomePage.exploreMenu.click();
        ExplorePage.shouldBeDisplayed();
        ExplorePage.firstArticle.shouldHaveQuantity(1);
        ExplorePage.articleCover.shouldBeDisplayed();
        ExplorePage.articleTitle.shouldBeDisplayed();
        ExplorePage.articlePubName.shouldBeDisplayed();
        ExplorePage.articleIssueName.shouldBeDisplayed();

        // Click on an article
        int position = 1;
        String title = ExplorePage.articleTitle.getAt(position).getText();
        String pubName = ExplorePage.articlePubName.getAt(position).getText();
        String issueName = ExplorePage.articleIssueName.getAt(position).getText();
        ExplorePage.articleCover.getAt(position).click();

        // Read Article page is displayed with correct content
        ReadArticlePage.shouldBeDisplayed();
        ReadArticlePage.articleTitle.shouldHaveText(title);
        ReadArticlePage.publicationName.shouldContainText(pubName);
        ReadArticlePage.infoPanel.shouldNotBeDisplayed();

        // Scroll down a little bit -> Info Panel should display correct info of the issue
        ReadArticlePage.scrollPageDown(100);
        ReadArticlePage.infoPanel_CoverImage.shouldBeDisplayed();
        ReadArticlePage.infoPanel_Description.shouldHaveText("Buy Issue or Subscribe to read full article");
        ReadArticlePage.infoPanel_PublicationName.shouldHaveText(pubName);
        ReadArticlePage.infoPanel_IssueName.shouldHaveText(issueName);
        ReadArticlePage.infoPanel_BuyIssueButton.shouldBeDisplayed();
        ReadArticlePage.infoPanel_SubscribeButton.shouldBeDisplayed();

        // Click on Buy Issue button -> Product Page should be displayed
        ReadArticlePage.infoPanel_BuyIssueButton.click();
        ProductPage.shouldBeDisplayed();
        ProductPage.shouldHaveUrl("/issue/");

        // Click on Subscribe button -> Product Page should be displayed
        ProductPage.goBack();
        ReadArticlePage.scrollPageDown(100);
        ReadArticlePage.infoPanel_SubscribeButton.click();
        ProductPage.shouldBeDisplayed();
        ProductPage.shouldHaveUrl("/issue/");

        endTest();
    }
}