package core;

import com.relevantcodes.extentreports.LogStatus;
import org.testng.Assert;
import utilities.ReportUtils;

public class BaseAssert {
    public static String failedDetails = "";
    private static int failedCount = 0;

    public static void resetAssert() {
        failedCount = 0;
        failedDetails = "";
    }

    public static void assertEquals(String description, Object actualValue, Object expectedValue) {
        if (actualValue.equals(expectedValue)) {
            ReportUtils.logExtent(LogStatus.PASS, description);
            ReportUtils.logConsole(">>> " + description);
        } else {
            String message = description + " - Actual: " + actualValue;
            ReportUtils.logExtent(LogStatus.FAIL, message);
            ReportUtils.logConsoleFail(">>> " + message);
            ReportUtils.captureScreenshot();
            failedCount++;
            failedDetails += failedCount + ". " + message + "\n";
        }
    }

    public static void assertContains(String description, String original, String fragment) {
        if (original.contains(fragment)) {
            ReportUtils.logExtent(LogStatus.PASS, description);
            ReportUtils.logConsole(">>> " + description);
        } else {
            String message = description + " - Actual: " + original;
            ReportUtils.logExtent(LogStatus.FAIL, message);
            ReportUtils.logConsoleFail(">>> " + message);
            ReportUtils.captureScreenshot();
            failedCount++;
            failedDetails += failedCount + ". " + message + "\n";
        }
    }

    public static void assertNotContain(String description, String original, String fragment) {
        if (!original.contains(fragment)) {
            ReportUtils.logExtent(LogStatus.PASS, description);
            ReportUtils.logConsole(">>> " + description);
        } else {
            String message = description + " - Actual: " + original;
            ReportUtils.logExtent(LogStatus.FAIL, message);
            ReportUtils.logConsoleFail(">>> " + message);
            ReportUtils.captureScreenshot();
            failedCount++;
            failedDetails += failedCount + ". " + message + "\n";
        }
    }

    public static void assertFail(String message) {
        ReportUtils.logExtent(LogStatus.FAIL, message);
        ReportUtils.logConsoleFail(">>> " + message);
        ReportUtils.captureScreenshot();
        failedCount++;
        failedDetails += failedCount + ". " + message + "\n";
    }

    public static void collectAsserts() {
        if (failedCount > 0) {
            failedCount = 0;
            Assert.fail("This test is FAILED!!! See failures summary below:\n\n" + failedDetails);
        }
    }
}