package core;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.github.bonigarcia.wdm.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.safari.SafariDriver;
import utilities.ReportUtils;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverManager {
    public final static int objectWait = 15;
    public final static int pageWait = 120;
    private static ThreadLocal<String> platformInfo = new ThreadLocal<>();
    private static ThreadLocal<WebDriver> webDriver = new ThreadLocal<>();

    public static String getPlatformInfo() {
        return platformInfo.get();
    }

    public static WebDriver getDriver() {
        return webDriver.get();
    }

    static public void initDriver(String browser, String remoteIP, String device, String orientation) {
        platformInfo.set(device + "-" + orientation);

        switch (browser) {
            case "safari_ios": // init driver to run with Appium
                remoteIP = remoteIP == null ? System.getProperty("remoteIP") : remoteIP;
                device = device.equals("iPhone") ? "iPhone 6s Plus" : "iPad Air 2";
                webDriver.set(initAppiumDriver(browser, remoteIP, device));
                ((AppiumDriver) webDriver.get()).rotate(orientation.equals("portrait") ?
                        ScreenOrientation.PORTRAIT : ScreenOrientation.LANDSCAPE);
                break;

            case "chrome_emulation": // init driver to run with Chrome in Mobile Emulation Mode
                webDriver.set(initChromeMobileEmulationDriver(device, orientation));
                break;

            case "phantomjs_emulation": // init driver to run with PhantomJS in Mobile Emulation Mode
                webDriver.set(initPhantomJSEmulationDriver(device, orientation));
                break;

            default:
                platformInfo.set("desktop");
                if (remoteIP == null) webDriver.set(initLocalDriver(browser)); // init driver to run on local desktop
                else webDriver.set(initRemoteDriver(browser, remoteIP)); // init driver to run on remote desktop
                break;
        }
        webDriver.get().manage().timeouts().pageLoadTimeout(pageWait, TimeUnit.SECONDS);
    }

    private static WebDriver initLocalDriver(String browser) {
        switch (browser.toLowerCase()) {
            case "firefox":
                FirefoxDriverManager.getInstance().setup();
                return new FirefoxDriver();
            case "chrome":
                ChromeDriverManager.getInstance().setup();
                return new ChromeDriver();
            case "safari":
                return new SafariDriver();
            case "ie":
                InternetExplorerDriverManager.getInstance().setup();
                return new InternetExplorerDriver();
            case "edge":
                EdgeDriverManager.getInstance().setup();
                return new EdgeDriver();
            default:
                if (System.getProperty("phantomjs.binary.path") == null) {
                    PhantomJsDriverManager.getInstance().version("2.1.1").setup();
                }
                Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
                DesiredCapabilities desiredCap = new DesiredCapabilities();
                String[] phantomArgs = new String[]{"--webdriver-loglevel=NONE", "--disk-cache=false"};
                desiredCap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
                return new PhantomJSDriver(desiredCap);
        }
    }

    private static WebDriver initRemoteDriver(String browser, String remoteIP) {
        try {
            DesiredCapabilities cap;
            switch (browser) {
                case "firefox":
                    cap = DesiredCapabilities.firefox();
                    break;
                case "ie":
                    cap = DesiredCapabilities.internetExplorer();
                    break;
                default:
                    cap = DesiredCapabilities.chrome();
                    break;
            }
            return new RemoteWebDriver(new URL("http://" + remoteIP + "/wd/hub"), cap) {
            };
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static WebDriver initAppiumDriver(String browser, String remote_ip, String device) {
        try {
            DesiredCapabilities cap;
            String appiumHost = "http://" + remote_ip + "/wd/hub";
            if (browser.equals("safari_ios")) {
                cap = DesiredCapabilities.iphone();
                cap.setCapability("deviceName", device);
                cap.setCapability("browserName", "safari");
                return new IOSDriver(new URL(appiumHost), cap);
            } else {
                cap = DesiredCapabilities.android();
                cap.setCapability("deviceName", device);
                cap.setCapability("browserName", "chrome");
                return new AndroidDriver(new URL(appiumHost), cap);
            }
        } catch (UnreachableBrowserException ex) {
            ReportUtils.logConsoleFail("\nAPPIUM SERVER IS NOT STARTED OR SERVER ADDRESS IS NOT CORRECT!!!");
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static Dimension getSize(String device, String orientation) {
        switch (device + "-" + orientation) {
            case "iPad-portrait":
                return new Dimension(768, 1024);
            case "iPad-landscape":
                return new Dimension(1024, 768);
            case "iPhone-portrait":
                return new Dimension(375, 667);
            case "iPhone-landscape":
                return new Dimension(667, 375);
            default:
                return null;
        }
    }

    private static String getBrowserAgent(String device) {
        String userAgent = "Mozilla/5.0 ({device}) AppleWebKit/602.3.10 (KHTML, like Gecko) Version/10.0 Mobile/14C5077b Safari/602.1";
        String deviceAgent = device.equals("iPad") ? "iPad; CPU OS 10_2 like Mac OS X" : "iPhone; CPU iPhone OS 10_2 like Mac OS X";
        return userAgent.replace("{device}", deviceAgent);
    }

    private static WebDriver initChromeMobileEmulationDriver(String device, String orientation) {
        ChromeDriverManager.getInstance().setup();
        Dimension size = getSize(device, orientation);

        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", size != null ? size.width : 0);
        deviceMetrics.put("height", size != null ? size.height : 0);

        Map<String, Object> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceMetrics", deviceMetrics);
        mobileEmulation.put("userAgent", getBrowserAgent(device));

        Map<String, Object> chromeOptions = new HashMap<>();
        chromeOptions.put("mobileEmulation", mobileEmulation);
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        WebDriver chromeDriver = new ChromeDriver(capabilities);
        chromeDriver.manage().window().maximize();
        return chromeDriver;
    }

    private static WebDriver initPhantomJSEmulationDriver(String device, String orientation) {
        if (System.getProperty("phantomjs.binary.path") == null)
            PhantomJsDriverManager.getInstance().setup();

        Logger.getLogger(PhantomJSDriverService.class.getName()).setLevel(Level.OFF);
        DesiredCapabilities desiredCap = new DesiredCapabilities();
        String[] phantomArgs = new String[]{"--webdriver-loglevel=NONE"};
        desiredCap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);
        desiredCap.setCapability("phantomjs.page.settings.userAgent", getBrowserAgent(device));
        WebDriver phantomJsDriver = new PhantomJSDriver(desiredCap);
        phantomJsDriver.manage().window().setSize(getSize(device, orientation));
        return phantomJsDriver;
    }
}