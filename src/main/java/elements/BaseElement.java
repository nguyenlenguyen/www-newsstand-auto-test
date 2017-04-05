package elements;

import core.BaseAssert;
import core.Browser;
import core.DriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ReportUtils;

import java.util.List;

public class BaseElement {
    protected String xpath;
    protected String controlDescription;
    protected int timeout;

    public BaseElement(String controlDescription, String xpath) {
        this.xpath = xpath;
        this.controlDescription = controlDescription;
        this.timeout = DriverManager.objectWait;
    }

    // GET METHODS =====================================================================================================

    WebElement waitElement() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
            ((JavascriptExecutor) DriverManager.getDriver()).executeScript("arguments[0].scrollIntoView(false);", element);
            return element;
        } catch (Exception ex) {
            BaseAssert.assertFail("ERROR: Cannot find [" + controlDescription + "]. Automation stopped here.");
            ReportUtils.logAll(ex.getMessage());
            BaseAssert.collectAsserts();
            return null;
        }
    }

    public List<WebElement> getList() {
        waitElement();
        return DriverManager.getDriver().findElements(By.xpath(xpath));
    }

    public BaseElement findElement(String description, String xpath) {
        return new BaseElement(description, this.xpath + xpath);
    }

    public BaseElement findElement(String description, String xpath, int position) {
        return new BaseElement(description, "(" + this.xpath + xpath + ")[" + position + "]");
    }

    public void click() {
        try {
            ReportUtils.logAll("Click [" + controlDescription + "]");
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
            wait.until(ExpectedConditions.elementToBeClickable(waitElement())).click();
            Browser.waitPageLoadComplete();
        } catch (Exception ex) {
            BaseAssert.assertFail("ERROR: " + ex.getMessage());
            BaseAssert.collectAsserts();
        }
    }

    public void hoverAndClick() {
        ReportUtils.logAll("Click [" + controlDescription + "]");
        new Actions(DriverManager.getDriver()).moveToElement(waitElement()).click().build().perform();
        Browser.waitPageLoadComplete();
    }

    public BaseElement getAt(int position) {
        return new BaseElement(this.controlDescription + " (" + position + ")", "(" + this.xpath + ")[" + position + "]");
    }

    public String getAttribute(String attribute) {
        return waitElement().getAttribute(attribute);
    }

    public Point getLocation() {
        return waitElement().getLocation();
    }

    public Dimension getSize() {
        return waitElement().getSize();
    }

    public String getText() {
        return waitElement().getText().trim();
    }

    public boolean isDisplayed() {
        return isDisplayed(timeout);
    }

    public boolean isDisplayed(long timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeoutInSeconds);
            return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath))).isDisplayed();
        } catch (Exception ex) {
            return false;
        }
    }

    // WAIT METHODS ====================================================================================================

    public boolean waitDisappear() {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
            return wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(xpath)));
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean waitAttributeToBe(String attribute, String value) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
            return wait.until(ExpectedConditions.attributeToBe(waitElement(), attribute, value));
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean waitAttributeContains(String attribute, String value) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
            return wait.until(ExpectedConditions.attributeContains(waitElement(), attribute, value));
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean waitTextToBe(String value) {
        try {
            WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), timeout);
            return wait.until(ExpectedConditions.textToBe(By.xpath(xpath), value));
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean waitTextContain(String value) {
        long baseTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - baseTime < timeout * 1000) {
            if (getText().contains(value)) {
                return true;
            } else {
                Browser.sleep(1000);
            }
        }
        return false;
    }

    private boolean waitTextNotContain(String value) {
        long baseTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - baseTime < timeout * 1000) {
            if (!getText().contains(value)) {
                return true;
            } else {
                Browser.sleep(1000);
            }
        }
        return false;
    }

    // CHECK METHODS ===================================================================================================

    public void shouldBeDisplayed() {
        BaseAssert.assertEquals("Check [" + controlDescription + "] is displayed", isDisplayed(), true);
    }

    public void shouldNotBeDisplayed() {
        BaseAssert.assertEquals("Check [" + controlDescription + "] is NOT displayed", waitDisappear(), true);
    }

    public void shouldHaveAttribute(String attribute, String value) {
        String description = "Check [" + attribute + "] of [" + controlDescription + "] is [" + value + "]";
        waitAttributeToBe(attribute, value);
        BaseAssert.assertEquals(description, getAttribute(attribute), value);
    }

    public void shouldHaveText(String text) {
        if (text.equals(""))
            BaseAssert.assertEquals("Check [" + controlDescription + "] is blank", this.getText(), "");
        else {
            waitTextToBe(text);
            BaseAssert.assertEquals("Check [" + controlDescription + "] displays [" + text + "]", getText(), text);
        }
    }

    public void shouldContainText(String text) {
        String description = "Check [" + controlDescription + "] contains the text [" + text + "]";
        waitTextContain(text);
        BaseAssert.assertContains(description, getText(), text);
    }

    public void shouldNotContainText(String text) {
        String description = "Check [" + controlDescription + "] does NOT contain the text [" + text + "]";
        waitTextNotContain(text);
        BaseAssert.assertNotContain(description, getText(), text);
    }

    public void shouldBeActive() {
        String description = "Check [" + controlDescription + "] is active";
        waitAttributeContains("class", "active");
        BaseAssert.assertEquals(description, getAttribute("class").contains("active"), true);
    }

    public void shouldHaveQuantity(int quantity) {
        String description = "Check [" + quantity + " " + this.controlDescription + "] is displayed";
        BaseAssert.assertEquals(description, isDisplayed() ? getList().size() : 0, quantity);
    }
}