package pages;

import api.API;
import core.Browser;
import core.DriverManager;
import elements.BaseElement;
import elements.TextBox;
import objects.Publication;
import org.openqa.selenium.Keys;
import testData.GeneralData;
import utilities.ApiUtils;
import utilities.ReportUtils;

public class MasterPage extends Browser {

    private static String cacheRefresh = "?cache-refresh";

    public static BaseElement username = new BaseElement("Username", "//span[@class='username']");
    public static BaseElement signInLink = new BaseElement("Sign In link", "//a[starts-with(@href,'/sign-in')]");
    public static BaseElement settingIcon = new BaseElement("Setting icon", "//span[@class='icon-gear active']");
    public static BaseElement settingMenu = new BaseElement("Setting menu", "//span[@class='icon-gear active']/../ul");
    public static BaseElement currencyIcon = new BaseElement("Currency icon", "//div[contains(@class,'currency')]/span");
    public static BaseElement currencyMenu = new BaseElement("Currency menu", "//div[contains(@class,'currency')]/ul");
    public static BaseElement languageIcon = new BaseElement("Language icon", "//div[contains(@class,'languages')]/span");
    public static BaseElement languageMenu = new BaseElement("Language menu", "//div[contains(@class,'languages')]/ul");
    public static BaseElement newsstandIcon = new BaseElement("Newsstand icon", "//div[contains(@class,'countries')]/span");
    public static BaseElement newsstandMenu = new BaseElement("Newsstand menu", "//div[contains(@class,'countries')]/ul");
    public static BaseElement zinioLogo = new BaseElement("Zinio logo", "//a[@class='toplogo']");
    public static BaseElement shopSection = new BaseElement("SHOP section", "//div[@class='shop-wrapper']/span");
    public static BaseElement shopMenu = new BaseElement("SHOP menu", "//div[@class='shop-wrapper']/div");
    public static BaseElement myLibraryMenu = new BaseElement("MY LIBRARY menu", "//a[@href='/my-library']");
    public static BaseElement exploreMenu = new BaseElement("EXPLORE menu", "//a[@href='/explore']");
    public static BaseElement heroBanner = new BaseElement("Hero banner", "//div[@id='banner']/img");
    public static BaseElement footer = new BaseElement("Footer", "//footer//div[contains(@class,'footer-top')]");
    public static BaseElement menuBar = new BaseElement("Menu bar", "//div[@class='menu-sroll']");
    public static BaseElement mobileMenuButton = new BaseElement("Mobile menu button", "//span[@class='navbar-tablet-mobile']");
    public static TextBox searchTextBox = new TextBox("Search textbox", "//input[@name='keyword']");
    public static BaseElement searchIcon = new BaseElement("Search icon", "//span[@class='search-icon-black']");
    public static BaseElement searchResult = new BaseElement("Search result", "//div[@id='search-results']");
    public static BaseElement privacyPolicyLink = new BaseElement("Privacy Policy link", "//a[.='Privacy Policy']");
    public static BaseElement cartIcon = new BaseElement("Cart Icon", "//div[starts-with(@class,'web-nav')]//a[@id='cart-modal']");
    public static BaseElement cartCounter = cartIcon.findElement("Cart Counter", "//span[@class='cart-count']");
    public static BaseElement loadingIcon = new BaseElement("Loading icon", "//div[@id='loading']");

    // PAGE METHODS ====================================================================================================

    public static void navigateToTestSite() {
        maximize();
        navigateToUrl(System.getProperty("siteUrl"));
        HomePage.cartIcon.isDisplayed();
    }

    public static void signIn(String email, String password) {
        ReportUtils.disableLog("Sign in with account: " + email + "/" + password);
        signInLink.click();
        SignInPage.signIn(email, password);
        ReportUtils.enableLog();
    }

    public static void signUp(String email, String password) {
        signInLink.click();
        SignInPage.createZinioAccountLink.click();
        SignUpPage.signUp(email, password, password);
    }

    public static void logOut() {
        settingIcon.click();
        settingMenu.findElement("Logout link", "//a[.='Log Out']").click();
        signInLink.isDisplayed();
    }

    public static void selectMenu(String menuPath) {
        if (mobileMenuButton.isDisplayed(1)) mobileMenuButton.click();
        String menuItem = menuPath.split(">")[1].trim();
        shopSection.click();
        shopMenu.findElement(menuItem, "//a[.='" + menuItem + "']").click();
    }

    public static void openMainShopPage() {
        ReportUtils.logAll("Go to Main Shop page");
        DriverManager.getDriver().navigate().to(getCurrentDomainName() + "/browse/shop" + cacheRefresh);
    }

    public static void openProductPage(String pageType, String id) {
        String messageWithCache = "Open [" + pageType + "] page of id [<id>] with cache-refresh";
        String messageWithoutCache = "Open [" + pageType + "] page of id [<id>]";
        String productUrl = getCurrentDomainName() + "/" + pageType + "/id-" + id;

        if (id.contains(cacheRefresh)) {
            ReportUtils.disableLog(messageWithCache.replace("<id>", id.replace(cacheRefresh, "")));
            navigateToUrl(productUrl);
        } else {
            if (GeneralData.alreadyCacheRefresh.contains(id)) {
                ReportUtils.disableLog(messageWithoutCache.replace("<id>", id));
                navigateToUrl(productUrl);
            } else {
                ReportUtils.disableLog(messageWithCache.replace("<id>", id));
                navigateToUrl(productUrl + cacheRefresh);
                GeneralData.alreadyCacheRefresh.add(id);
            }
        }
        ReportUtils.enableLog();
    }

    public static void openCategoryPage(String categoryID) {
        navigateToUrl(getCurrentDomainName() + "/category/id-" + categoryID + "?sort=name");
    }

    public static void openAccountSettings(String tab) {
        ReportUtils.disableLog("Open Account Settings > " + tab);
        settingIcon.click();
        settingMenu.findElement("Account Settings", "//a[.='Account Settings']").click();

        if (!tab.startsWith("Account")) {
            if (DriverManager.getPlatformInfo().equals("iPhone-portrait"))
                new BaseElement("Tab Dropdown", "//span[@class='get-link-active']").click();
            AccountInfoPage.clickLinkByText(tab);
        }
        ReportUtils.enableLog();
    }

    public static void openFreeIssuesPage() {
        ReportUtils.logAll("Open the Free Issues page");
        DriverManager.getDriver().navigate().to(getCurrentDomainName() + "/free-magazines");
    }

    public static void search(String keyword, boolean submit) {
        if (!searchTextBox.isDisplayed(1))
            searchIcon.click();
        searchTextBox.setText(keyword);
        if (submit)
            searchTextBox.pressKey(Keys.ENTER);
    }

    public static void selectCurrency(String currency) {
        currencyIcon.click();
        currencyMenu.findElement(currency, "//a[.='" + currency + "']").click();
    }

    public static void selectLanguage(String language) {
        languageIcon.click();
        languageMenu.findElement(language, "//a[.='" + language + "']").click();
    }

    public static void selectNewsstand(String newsstand) {
        newsstandIcon.click();
        newsstandMenu.findElement(newsstand, "//a[.='" + newsstand + "']").click();
    }

    public static void initAccount(String email, String password, boolean login) {
        ApiUtils apiUtils = new ApiUtils("/identity/v1/users?directory_id=25&email=" + email);
        if (apiUtils.sendGET().get("data").getAsJsonArray().size() == 0) {
            signUp(email, password);
            PostRegistrationPage.selectCategories("Art", "Home", "Automotive");
            PostRegistrationPage.doneButton.click();
            if (!login) logOut();
        } else {
            if (login) {
                HomePage.signIn(email, password);
                HomePage.settingIcon.isDisplayed();
            }
        }
    }

    public static void setAllowAdultContent(boolean value) {
        ReportUtils.disableLog("Set Allow Adult Content in Account Settings to " + value);
        openAccountSettings("Preferences");
        if (value)
            PreferencesPage.allowAdultContent.click();
        else
            PreferencesPage.notAllowAdultContent.click();
        PreferencesPage.saveButton.click();
        ReportUtils.enableLog();
    }

    public static void shouldShowPublication(String publicationID) {
        BaseElement publication = new BaseElement("Publication " + publicationID, "//a[contains(@href,'" + publicationID + "')]");
        publication.shouldBeDisplayed();
    }

    public static void shouldNotShowPublication(String publicationID) {
        BaseElement publication = new BaseElement("Publication " + publicationID, "//a[contains(@href,'" + publicationID + "')]");
        publication.shouldNotBeDisplayed();
    }

    public static void verifyAdultContentOnPages(boolean allowAdultContent, String publicationID, String issuesID, String categoryID) {
        // Verify Product Page
        HomePage.openProductPage("magazine", publicationID + cacheRefresh);
        if (allowAdultContent) ProductPage.shouldBeDisplayed();
        else HomePage.shouldBeDisplayed();

        // Verify Publisher Page
        HomePage.openProductPage("publisher", publicationID + cacheRefresh);
        if (allowAdultContent) PublisherPage.shouldBeDisplayed();
        else HomePage.shouldBeDisplayed();

        // Verify Issue Page
        HomePage.openProductPage("issue", issuesID + cacheRefresh);
        if (allowAdultContent) IssuePage.shouldBeDisplayed();
        else HomePage.shouldBeDisplayed();

        // Verify Back Issues Page
        HomePage.openProductPage("back-issues", publicationID + cacheRefresh);
        if (allowAdultContent) BackIssuesPage.shouldBeDisplayed();
        else HomePage.shouldBeDisplayed();

        // Verify Publisher Back Issues Page
        HomePage.openProductPage("publisher-back-issues", publicationID + cacheRefresh);
        if (allowAdultContent) PublisherBackIssuesPage.shouldBeDisplayed();
        else HomePage.shouldBeDisplayed();

        // Verify Category Page
        HomePage.openCategoryPage(categoryID);
        if (allowAdultContent) CategoryPage.shouldShowPublication(publicationID);
        else CategoryPage.shouldNotShowPublication(publicationID);

        // Verify Search Suggestion
        String pubName = API.getPublicationInfo(publicationID, "name");
        HomePage.search(pubName, false);
        HomePage.sleep(3000);
        if (allowAdultContent) HomePage.searchResult.shouldContainText(pubName);
        else HomePage.searchResult.shouldNotBeDisplayed();

        // Verify Search Result Page
        HomePage.searchTextBox.pressKey(Keys.ENTER);
        if (allowAdultContent) SearchResultPage.shouldShowPublication(publicationID);
        else SearchResultPage.shouldNotShowPublication(publicationID);
    }

    public static Publication buySubscribeIssue(String type, String publicationID, boolean proceedToCheckout) {
        HomePage.openProductPage("magazine", publicationID);
        String pubName = ProductPage.publicationName.getText();
        String issueName = ProductPage.issueName.getText();
        if (type.equals("buy")) ProductPage.buyIssueButton.click();
        else ProductPage.subscribeButton.click();

        loadingIcon.waitDisappear();
        sleep(500);
        if (proceedToCheckout)
            CartPopup.proceedToCheckoutButton.click();
        return new Publication(publicationID, pubName, issueName);
    }

    public static void cacheRefresh() {
        ReportUtils.disableLog("Do cache-refresh for current page");
        Browser.navigateToUrl(Browser.getCurrentUrl() + cacheRefresh);
        ReportUtils.enableLog();
    }
}