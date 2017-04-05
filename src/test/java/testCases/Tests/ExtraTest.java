package testCases.Tests;

import api.API;
import core.BaseAssert;
import core.Browser;
import elements.BaseElement;
import objects.CreditCard;
import objects.Publication;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pages.*;
import testCases.BaseTests.ExtraTestBase;
import testData.GeneralData;
import testData.SetData;
import utilities.ReportUtils;
import utilities.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ExtraTest extends ExtraTestBase {
    private String email;

    private String createNewUser() {
        email = StringUtils.randomString(10, "@zinio.com");
        String password = StringUtils.randomNumber("P", 10);
        ReportUtils.disableLog("Create new account: " + email + "/" + password);
        HomePage.signUp(email, password);
        HomePage.settingIcon.isDisplayed();
        ReportUtils.enableLog();
        return API.getUserID(email);
    }

    @Test
    public void C28076_VERIFY_CREDIT_CARD_IN_PAYMENT_INFORMATION_PAGE() {
        createNewUser();
        HomePage.openAccountSettings("Payment information");

        // Verify blank credit card info page
        PaymentInfoPage.addPaymentInfoLink.click();
        PaymentInfoPage.waitForBraintreeReady();
        PaymentInfoPage.saveButton.click();
        PaymentInfoPage.checkCreditCardValidation();

        // Click Add Link, edit, then Cancel -> nothing change
        CreditCard validCreditCard = new CreditCard().getValidCard();
        PaymentInfoPage.countrySelectBox.selectOption("Tuvalu");
        PaymentInfoPage.zipCodeTextBox.shouldNotBeDisplayed();
        PaymentInfoPage.setCreditCardInfo(validCreditCard);
        PaymentInfoPage.cancelButton.click();
        PaymentInfoPage.creditCardInfo.shouldContainText("You have no payment information.");

        // Update with invalid credit card -> unsuccessful
        PaymentInfoPage.editButton.click();
        PaymentInfoPage.setCreditCardInfo(new CreditCard().getInvalidCard());
        PaymentInfoPage.setBillingInfo("182 LDH", "HCM", "District 11");
        PaymentInfoPage.saveButton.click();
        PaymentInfoPage.errorMessage.shouldHaveText("Oops, we couldn't save your changes. " +
                "Please check your payment information and try again.");
        PaymentInfoPage.creditCardInfo.shouldContainText("You have no payment information.");

        // Update valid Payment Info & Billing Info -> successful
        PaymentInfoPage.editButton.click();
        PaymentInfoPage.setCreditCardInfo(validCreditCard);
        PaymentInfoPage.setBillingInfo("182 LDH", "HCM", "District 11");
        PaymentInfoPage.saveButton.click();
        PaymentInfoPage.successMessage.shouldHaveText("Changes successfully saved.");
        PaymentInfoPage.checkCreditCardInfo(validCreditCard);
        PaymentInfoPage.checkBillingInfo("182 LDH", "HCM", "District 11");

        endTest();
    }

    @Test
    public void C28077_VERIFY_PAYPAL_IN_PAYMENT_INFORMATION_PAGE() {
        createNewUser();
        HomePage.openAccountSettings("Payment information");

        // Verify blank paypal info page
        PaymentInfoPage.addPaymentInfoLink.click();
        PaymentInfoPage.paypalBoxEdit.click();
        PaymentInfoPage.saveButton.click();
        PaymentInfoPage.paypalInfoEdit.shouldContainText("Please log into Paypal to complete your purchase " +
                "or choose to pay with credit card.");

        // Login PayPal but cancel -> not save
        PaymentInfoPage.loginPaypal("abc@zinio.com", "123456789");
        PaymentInfoPage.cancelButton.click();
        PaymentInfoPage.paypalInfo.shouldNotBeDisplayed();

        // Login Paypal and save -> successful
        PaymentInfoPage.editButton.click();
        PaymentInfoPage.paypalBoxEdit.click();
        PaymentInfoPage.loginPaypal("abc@zinio.com", "123456789");
        PaymentInfoPage.paypalInfoEdit.shouldContainText("abc@zinio.com");
        PaymentInfoPage.setBillingInfo("182 LDH", "HCM", "District 11");
        PaymentInfoPage.saveButton.click();
        PaymentInfoPage.successMessage.shouldHaveText("Changes successfully saved.");
        PaymentInfoPage.paypalInfo.shouldBeDisplayed();
        PaymentInfoPage.checkBillingInfo("182 LDH", "HCM", "District 11");

        endTest();
    }

    @Test
    public void C28078_VERIFY_PAYMENT_INFORMATION_PAGE_AFTER_COMPLETED_A_PURCHASE() {
        createNewUser();

        // Buy an issue and complete the purchase with credit card -> the card info is saved to Payment Info page
        CreditCard creditCard = new CreditCard().getValidCard();
        HomePage.buySubscribeIssue("buy", publicationList[0], true);
        CheckoutPaymentPage.completePurchaseWithCreditCard(creditCard);
        HomePage.openAccountSettings("Payment information");
        PaymentInfoPage.checkCreditCardInfo(creditCard);

        // Buy an issue and complete the purchase with paypal -> the paypal info is saved to Payment Info page
        HomePage.buySubscribeIssue("buy", publicationList[1], true);
        CheckoutPaymentPage.editCreditCardLink.click();
        CheckoutPaymentPage.waitForBraintreeReady();
        CheckoutPaymentPage.paypalRadioBox.click();
        CheckoutPaymentPage.loginPaypal("abc@zinio.com", "123456789");
        CheckoutPaymentPage.completePurchase();
        HomePage.openAccountSettings("Payment information");
        PaymentInfoPage.paypalInfo.shouldBeDisplayed();

        endTest();
    }

    @Test
    public void C28079_VERIFY_PREFERENCES_PAGE() {
        createNewUser();
        HomePage.openAccountSettings("Preferences");

        // Turn on all options
        PreferencesPage.setAllInterests(true);
        PreferencesPage.updatesCheckBox.check();
        PreferencesPage.saveButton.click();
        PreferencesPage.shouldShowMessage("Got it! Your preferences have been updated.");

        // Back to Preferences page -> changes are saved
        HomePage.openAccountSettings("Preferences");
        PreferencesPage.verifyAllInterests(true);
        PreferencesPage.updatesCheckBox.shouldBeSelected();

        // Turn off an interest
        PreferencesPage.interestCheckBox.uncheck();
        PreferencesPage.updatesCheckBox.uncheck();
        PreferencesPage.saveButton.click();
        PreferencesPage.shouldShowMessage("Got it! Your preferences have been updated.");

        // Back to Preferences page -> changes are saved
        HomePage.openAccountSettings("Preferences");
        PreferencesPage.interestCheckBox.shouldBeUnselected();
        PreferencesPage.updatesCheckBox.shouldBeUnselected();

        endTest();
    }

    @Test
    public void C28080_VERIFY_FILTERS_IN_SHOP_CATEGORY_PAGE() {
        // Select a category from shop menu -> go to Category page with correct selected category
        HomePage.openCategoryPage("2000");
        CategoryPage.shouldBeDisplayed();
        CategoryPage.categoryFilter.shouldContainText("Art");

        // Filter to another category
        CategoryPage.selectSortBy("Name");
        String pubName1 = CategoryPage.publicationName.getText();
        CategoryPage.selectCategory("Home");
        String pubName2 = CategoryPage.publicationName.getText();
        BaseAssert.assertEquals("Page is updated", !pubName1.equals(pubName2), true);

        // Filter to all categories
        CategoryPage.selectCategory("All categories");
        String pubName3 = CategoryPage.publicationName.getText();
        BaseAssert.assertEquals("Page is updated", !pubName2.equals(pubName3), true);

        // Filter to another language
        CategoryPage.selectLanguage("Dansk");
        CategoryPage.shouldShowMessage("There are no magazines that match your criteria.");

        endTest();
    }

    @Test
    public void C28081_VERIFY_SWITCHING_NEWSSTAND() {
        // Add a subscription to cart in US newsstand
        String userID = createNewUser();
        Publication usPub = HomePage.buySubscribeIssue("subscribe", publicationList[0], false);
        usPub.getAllInfo();
        CartPopup.closeCart();

        // Switch to AU newsstand -> Popup is displayed, select Stay will stay in current newsstand
        ProductPage.selectNewsstand("Australia");
        SwitchNewsstandPopup.popup.shouldBeDisplayed();
        SwitchNewsstandPopup.stayButton.click();
        Browser.shouldHaveUrl("us.");

        // Switch to AU newsstand -> user is still logged in, cart is back to 0
        ProductPage.selectNewsstand("Australia");
        SwitchNewsstandPopup.continueButton.click();
        HomePage.shouldHaveUrl("au.");
        HomePage.username.shouldContainText(email);
        HomePage.cartCounter.shouldHaveText("0");

        // Subscribe and complete purchase a publication in AU newsstand
        String auPubID = SetData.getPublicationList(GeneralData.AU_NEWSSTAND_ID, 1)[0];
        API.setSubscriptionPrice(auPubID, "100", 10);
        Publication auPub = HomePage.buySubscribeIssue("subscribe", auPubID, true);
        auPub.newsstandID = GeneralData.AU_NEWSSTAND_ID;
        auPub.getAllInfo();
        CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getValidCard());

        // Switch to US newsstand -> cart is back to 1
        ProductPage.selectNewsstand("USA");
        HomePage.cartCounter.shouldHaveText("1");

        // Complete purchase that publication in US newsstand
        HomePage.cartIcon.click();
        CartPopup.proceedToCheckoutButton.click();
        CheckoutPaymentPage.completePurchase();

        // Go to My Library
        API.waitItemInLibrary(userID, usPub.issueID, auPub.issueID);
        ThankYouPage.myLibraryMenu.click();
        MyLibraryPage.shouldShowPublication(usPub.issueID);
        MyLibraryPage.shouldShowPublication(auPub.issueID);

        // Go to Subscriptions page
        MyLibraryPage.openAccountSettings("Subscriptions");
        SubscriptionsPage.subscription.getAt(1).shouldContainText(usPub.name);
        SubscriptionsPage.subscription.getAt(2).shouldContainText(auPub.name);

        endTest();
    }

    @Test
    public void C28082_VERIFY_SWITCHING_CURRENCY() {
        // Switch to another currency
        String currency = "AUD $";
        createNewUser();
        HomePage.selectCurrency("AUD");

        // Article Detail page show new currency
        HomePage.exploreMenu.click();
        ExplorePage.articleCover.click();
        ReadArticlePage.scrollPageDown(100);
        ReadArticlePage.infoPanel_buyPrice.shouldContainText(currency);

        // Product Page show new currency
        HomePage.openProductPage("magazine", publicationList[0]);
        ProductPage.subscribePrice.shouldContainText(currency);
        ProductPage.buyPrice.shouldContainText(currency);

        // Back Issue page show new currency
        ProductPage.viewAllBackIssuesLink.click();
        BackIssuesPage.backIssuePrice.shouldContainText(currency);

        // Cart Popup show new currency
        BackIssuesPage.buyBackIssueButton.click();
        CartPopup.subTotalPrice.shouldContainText(currency);
        CartPopup.itemPrice.shouldContainText(currency);

        // Checkout Payment page show new currency
        CartPopup.proceedToCheckoutButton.click();
        CheckoutPaymentPage.itemPrice.shouldContainText(currency);
        CheckoutPaymentPage.magazinePrice.shouldContainText(currency);
        CheckoutPaymentPage.taxPrice.shouldContainText(currency);
        CheckoutPaymentPage.totalPrice.shouldContainText(currency);

        // User can complete purchase successfully with new currency
        CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getValidCard());
        ThankYouPage.shouldBeDisplayed(true);

        endTest();
    }

    @Test(enabled = false)
    public void C28083_VERIFY_SHOP_MENU_NAVIGATION() {
        // Get all items in Shop Menu
        HomePage.shopSection.click();
        List<WebElement> links = HomePage.shopMenu.findElement("", "//a").getList();
        List<String> linkNames = new ArrayList<>();
        for (WebElement link : links) {
            linkNames.add(link.getText());
        }

        // Click on each items in Shop Menu
        for (int i = 1; i <= links.size(); i++) {
            HomePage.shopSection.click();
            BaseElement itemMenu = HomePage.shopMenu.findElement(linkNames.get(i - 1), "//a", i);
            String linkHref = itemMenu.getAttribute("href");
            itemMenu.click();
            Browser.shouldHaveUrl(linkHref);
        }

        endTest();
    }

    @Test
    public void C28084_VERIFY_FOOTER_NAVIGATION() {
        // Set Footer
        List<String> footerItems = new ArrayList<>();
        footerItems.add("Company::http://corp.zinio.com/company/#about");
        footerItems.add("News::http://corp.zinio.com/news/");
        footerItems.add("Careers::http://corp.zinio.com/company/#career");
        footerItems.add("iOS::https://itunes.apple.com/us/app/zinio-magazine-newsstand-reader/id364297166?mt=8");
        footerItems.add("Android::https://play.google.com/store/apps/details?id=com.zinio.mobile.android.reader");
        footerItems.add("Desktop Reader::http://us.web-newsstand-dev.xen.int/ZR5");
        footerItems.add("Facebook::https://www.facebook.com/zinio");
        footerItems.add("Twitter::https://twitter.com/zinio");
        footerItems.add("FAQs::https://support.zinio.com/hc/en-us");
        footerItems.add("Contact Support::https://support.zinio.com/hc/en-us/requests/new");

        // Click on each items in Footer
        for (int i = 1; i <= HomePage.footer.findElement("", "//a").getList().size(); i++) {
            String linkText = HomePage.footer.findElement("", "//a", i).getText();
            String linkHref = "";
            for (String item : footerItems) {
                if (item.startsWith(linkText)) {
                    linkHref = item.split("::")[1];
                    break;
                }
            }
            HomePage.footer.findElement(linkText, "//a", i).click();
            Browser.shouldHaveUrl(linkHref);
            Browser.goBack();
        }

        endTest();
    }

    @Test
    public void C28085_VERIFY_DOWNLOAD_SECTION_IN_HOME_PAGE() {
        // Verify View All link of Featured section
        HomePage.viewAllFeaturedLink.click();
        Browser.shouldHaveUrl("/featured");

        // Verify desktop icon
        Browser.goBack();
        HomePage.desktopIcon.click();
        Browser.shouldHaveUrl("/ZR5");

        // Verify App Store icon
        Browser.goBack();
        HomePage.appStoreIcon.click();
        Browser.shouldHaveUrl("https://itunes.apple.com/us/app/zinio-magazine-newsstand-reader/id364297166?mt=8");

        // Verify Google Play icon
        Browser.goBack();
        HomePage.googlePlayIcon.click();
        Browser.shouldHaveUrl("https://play.google.com/store/apps/details?id=com.zinio.mobile.android.reader");

        endTest();
    }
}