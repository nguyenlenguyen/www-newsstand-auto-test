package utilities;

import com.codepine.api.testrail.TestRail;
import com.codepine.api.testrail.model.Result;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import core.BaseAssert;
import core.DriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;

public class ReportUtils {
    private static boolean enableLog = true;
    private static ExtentReports extentReports;
    private static ExtentTest extentTest;
    private static String extentReportFile;

    public static void disableLog(String message) {
        logAll(message);
        enableLog = false;
    }

    public static void enableLog() {
        enableLog = true;
    }

    public static void logAll(String message) {
        logConsole(message);
        logExtent(LogStatus.INFO, message);
    }

    public static void logExtent(LogStatus status, String message) {
        if (enableLog && extentTest != null) extentTest.log(status, message);
    }

    public static void logConsole(String message) {
        if (enableLog) Reporter.log(message, true);
    }

    public static void logConsoleFail(String message) {
        if (enableLog) {
            Reporter.log(message);
            System.err.println(message);
        }
    }

    public static String captureScreenshot() {
        try {
            if (System.getProperty("screenshot").equals("false")) return "";
            File srcFile = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.FILE);
            String sep = System.getProperty("file.separator");
            String dateFolder = DateUtils.getCurrentDateTime("MM_dd_yyyy");
            String fileName = StringUtils.randomString("Screenshot_", 10) + ".jpg";
            String screenshotFolder = System.getProperty("user.home") + sep + "Documents" + sep + "Automation_Screenshots";
            String filePath = screenshotFolder + sep + dateFolder + sep + fileName;
            FileUtils.copyFile(srcFile, new File(filePath));
            logConsole("Screenshot: " + filePath);
            addScreenshotToExtentReport(filePath);
            return filePath;
        } catch (Exception ex) {
            ReportUtils.logAll(ex.getMessage());
            ReportUtils.logAll("Something is wrong. Cannot capture screenshot");
            ReportUtils.logAll(ex.getMessage());
            return "";
        }
    }

    public static void updateResultToTestRail(int runID, int caseID, int result, String comment) {
        try {
            String testRailUrl = "https://zinio.testrail.net";
            String testRailUser = "nguyen.nguyen@audiencemedia.com";
            String testRailPasswordKey = ".UOIUozN/d/mkW.45vRl-Chp0oOLBRprgJ7g2soIy";

            if (System.getProperty("testrail").equals("false"))
                return;

            TestRail testRail = TestRail.builder(testRailUrl, testRailUser, testRailPasswordKey).build();
            testRail.results().addForCase(runID, caseID, new Result().setStatusId(result).setComment(comment),
                    testRail.resultFields().list().execute()).execute();
        } catch (Exception ex) {
            ReportUtils.logConsole("Something is wrong, cannot update result to TestRail.");
        }
    }

    public static void createExtentReport(String fileName) {
        String sep = System.getProperty("file.separator");
        fileName = fileName + "_" + DateUtils.getCurrentDateTime("yyyy_MM_dd_HH_mm") + ".html";
        String reportFolder = System.getProperty("user.home") + sep + "Documents" + sep + "Automation_Reports";
        extentReports = new ExtentReports(reportFolder + sep + fileName, true);
        extentReportFile = reportFolder + sep + fileName;
    }

    public static void startExtentTest(String testName, String category) {
        ReportUtils.logConsole("\n\n===== TEST CASE: " + testName + " =====\n");
        BaseAssert.resetAssert();
        if (extentReports != null) {
            extentTest = extentReports.startTest(testName).assignCategory(category);
        }
    }

    public static void endExtentTest(ITestResult iTestResult) {
        if (extentReports != null) {
            if (iTestResult.getStatus() == ITestResult.SKIP)
                extentTest.log(LogStatus.SKIP, "This test is skipped.");
            extentReports.endTest(extentTest);
            extentReports.flush();
        }
    }

    public static void finalizeExtentReport() {
        if (extentReports != null) {
            extentReports.close();
            logConsole("\n\n===== END EXECUTION =====");
            logConsole("\n\nPlease see the HTML report at: " + extentReportFile);
        }
    }

    private static void addScreenshotToExtentReport(String screenshot) {
        if (extentReports != null) {
            extentTest.log(LogStatus.INFO, extentTest.addScreenCapture(screenshot));
        }
    }
}