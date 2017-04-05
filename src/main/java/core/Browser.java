package core;

import elements.BaseElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ReportUtils;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Browser {
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void waitPageLoadComplete() {
        long baseTime = System.currentTimeMillis();
        JavascriptExecutor js = (JavascriptExecutor) DriverManager.getDriver();
        while ((System.currentTimeMillis() - baseTime) < (DriverManager.pageWait * 1000)) {
            if (js.executeScript("return document.readyState").equals("complete")) return;
            else sleep(1000);
        }
    }

    public static void clearLocalStorage() {
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("window.localStorage.clear();");
        sleep(1000);
    }

    public static void navigateToUrl(String url) {
        ReportUtils.logAll("Navigate to url: " + url);
        DriverManager.getDriver().navigate().to(url);
        waitPageLoadComplete();
    }

    public static void maximize() {
        if (DriverManager.getPlatformInfo().equals("desktop")) {
            try {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                DriverManager.getDriver().manage().window().setSize(
                        new org.openqa.selenium.Dimension(screenSize.width, screenSize.height));
                DriverManager.getDriver().manage().window().maximize();
            } catch (Exception ex) {
                DriverManager.getDriver().manage().window().maximize();
            }
        }
    }

    public static void goBack() {
        DriverManager.getDriver().navigate().back();
    }

    public static String getCurrentUrl() {
        return DriverManager.getDriver().getCurrentUrl();
    }

    public static String getCurrentDomainName() {
        try {
            URL fullUrl = new URL(getCurrentUrl());
            return fullUrl.getProtocol() + "://" + fullUrl.getHost();
        } catch (Exception ex) {
            return null;
        }
    }

    public static String getTitle() {
        return DriverManager.getDriver().getTitle();
    }

    public static void switchToNewTab() {
        List<String> windowHandles = new ArrayList<>(DriverManager.getDriver().getWindowHandles());
        DriverManager.getDriver().switchTo().window(windowHandles.get(1));
    }

    public static void closeNewTab() {
        List<String> windowHandles = new ArrayList<>(DriverManager.getDriver().getWindowHandles());
        DriverManager.getDriver().close();
        DriverManager.getDriver().switchTo().window(windowHandles.get(0));
    }

    public static void clickLinkByText(String text) {
        BaseElement element = new BaseElement(text, "//a[.='" + text + "']");
        element.click();
    }

    public static void shouldShowMessage(String message) {
        BaseElement messageItem = new BaseElement("Message '" + message + "'", "//*[.='" + message + "']");
        messageItem.shouldBeDisplayed();
    }

    public static void shouldNotShowMessage(String message) {
        BaseElement messageItem = new BaseElement("Message '" + message + "'", "//*[contains(.,'" + message + "')]");
        messageItem.shouldNotBeDisplayed();
    }

    public static void shouldHaveTitle(String title) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), DriverManager.objectWait);
            wait.until(ExpectedConditions.titleContains(title));
        } catch (Exception ex) {
            //do nothing
        } finally {
            BaseAssert.assertContains("Check [" + title + "] page is displayed", getTitle(), title);
        }
    }

    public static void shouldHaveUrl(String url) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), DriverManager.objectWait);
            wait.until(ExpectedConditions.urlContains(url));
        } catch (Exception ex) {
            //do nothing
        } finally {
            BaseAssert.assertContains("Check page [" + url + "] is displayed", getCurrentUrl(), url);
        }
    }

    public static void waitPageDisplay(String pageUrl) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), DriverManager.objectWait);
            wait.until(ExpectedConditions.urlContains(pageUrl));
        } catch (Exception ex) {
            //do nothing
        }
    }

    public static void scrollPageDown(int height) {
        ReportUtils.logAll("Scroll the page down");
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("window.scrollTo(0, " + height + ")");
    }
}