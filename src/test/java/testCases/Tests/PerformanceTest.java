package testCases.Tests;

import elements.BaseElement;
import objects.CreditCard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import pages.*;
import testCases.BaseTests.BaseTest;
import utilities.ReportUtils;
import utilities.StringUtils;

import java.util.List;

public class PerformanceTest extends BaseTest {
    private String publicationID = "8378";
    private long baseTime = 0;

    private void startBenchmark() {
        baseTime = System.currentTimeMillis();
    }

    private void endBenchmark(String message) {
        ReportUtils.enableLog();
        ReportUtils.disableLog(message + ":\t" + (System.currentTimeMillis() - baseTime));
    }

    private void prepareData(int runCount) {
        int itemsInCart = 5;
        ReportUtils.enableLog();
        ReportUtils.disableLog("\n========== RUN #" + runCount + " with " + itemsInCart + " items in cart ==========\n");
        HomePage.openProductPage("magazine", publicationID);
        ProductPage.viewAllBackIssuesLink.click();
        List<WebElement> buyIssueButtons = BackIssuesPage.buyBackIssueButton.getList();
        for (int i = 0; i < itemsInCart; i++) {
            buyIssueButtons.get(i).click();
            CartPopup.closeCart();
        }
    }

    @Test
    public void VERIFY_PERFORMANCE_OF_PAGES() {
        for (int i = 0; i < 2; i++) {
            // Prepare Data
            prepareData(i + 1);

            // Home Page
            startBenchmark();
            MasterPage.zinioLogo.click();
            endBenchmark("Home Page");

            // Main Shop Page
            startBenchmark();
            HomePage.selectMenu("Shop > Shop All");
            endBenchmark("Main Shop Page");

            // Category Page
            startBenchmark();
            MainShopPage.selectMenu("Shop > Art");
            endBenchmark("Category Page");

            // Publisher Page
            startBenchmark();
            MainShopPage.openProductPage("publisher", publicationID);
            endBenchmark("Publisher Page");

            // Publisher Back Issues / Special Issues Page
            startBenchmark();
            PublisherPage.viewAllBackIssuesLink.click();
            endBenchmark("Publisher Back Issues Page");

            // Publication Page
            startBenchmark();
            PublisherBackIssuesPage.openProductPage("magazine", publicationID);
            endBenchmark("Publication Page");

            // Back Issues / Special Issues Page
            startBenchmark();
            ProductPage.viewAllBackIssuesLink.click();
            endBenchmark("Back Issues Page");

            // Sign In Page
            startBenchmark();
            BackIssuesPage.signInLink.click();
            endBenchmark("Sign In Page");

            // Forgot Password Page
            startBenchmark();
            SignInPage.forgotPasswordLink.click();
            endBenchmark("Forgot Password Page");

            // Create Account Page
            ForgotPasswordPage.goBack();
            SignInPage.createZinioAccountLink.click();
            endBenchmark("Create Account Page");

            // Post Registration Page
            startBenchmark();
            SignUpPage.signUp(StringUtils.randomString(10, "@zinio.com"), "123456789", "123456789");
            PostRegistrationPage.doneButton.isDisplayed();
            endBenchmark("Post Registration Page");

            // Search Suggestion
            PostRegistrationPage.searchTextBox.setText("art");
            startBenchmark();
            new BaseElement("suggestion", "//a[@class='search-result']").isDisplayed();
            endBenchmark("Search Suggestion");

            // Search Result Page
            startBenchmark();
            PostRegistrationPage.searchTextBox.pressKey(Keys.ENTER);
            endBenchmark("Search Result Page");

            // Account Information Page
            SearchResultPage.settingIcon.click();
            startBenchmark();
            SearchResultPage.clickLinkByText("Account Settings");
            endBenchmark("Account Info Page");

            // Payment Information Page
            startBenchmark();
            AccountInfoPage.clickLinkByText("Payment information");
            endBenchmark("Payment Info Page");

            // Subscriptions Page
            startBenchmark();
            AccountInfoPage.clickLinkByText("Subscriptions");
            endBenchmark("Subscriptions Page");

            // Preferences Page
            startBenchmark();
            AccountInfoPage.clickLinkByText("Preferences");
            endBenchmark("Preferences Page");

            // Checkout Payment Page
            AccountInfoPage.cartIcon.click();
            startBenchmark();
            CartPopup.proceedToCheckoutButton.click();
            endBenchmark("Checkout Payment Page");

            // Order Processing & Thank You Page
            startBenchmark();
            CheckoutPaymentPage.completePurchaseWithCreditCard(new CreditCard().getValidCard());
            endBenchmark("Order Processing & Thank You Page");

            // My Library Page
            startBenchmark();
            ThankYouPage.myLibraryMenu.click();
            endBenchmark("My Library Page");

            // Legal Page
            startBenchmark();
            MyLibraryPage.navigateToUrl(System.getProperty("siteUrl") + "/legal/terms");
            endBenchmark("Zinio Legal Notices Page");

            // Error Page
            startBenchmark();
            MasterPage.openProductPage("magazine", StringUtils.randomNumber("", 10));
            endBenchmark("Error Page");

            // Logout for the next run
            MasterPage.logOut();
        }
    }
}
