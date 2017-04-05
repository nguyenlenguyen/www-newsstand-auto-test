package testCases.BaseTests;

import core.BaseAssert;
import core.DriverManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import utilities.ReportUtils;

import java.lang.reflect.Method;

public class SmokeTestBase extends BaseTest {
    @AfterMethod
    protected void updateResultToTestRail(ITestResult iTestResult, Method method) {
        try {
            int caseID = Integer.parseInt(method.getName().split("_")[0].replace("C", ""));
            int runID = 0;
            switch (DriverManager.getPlatformInfo()) {
                case "desktop":
                    runID = 102;
                    break;
                case "iPhone-landscape":
                    runID = 0;
                    break;
                case "iPhone-portrait":
                    runID = 0;
                    break;
                case "iPad-landscape":
                    runID = 0;
                    break;
                case "iPad-portrait":
                    runID = 0;
                    break;
            }
            ReportUtils.updateResultToTestRail(runID, caseID, iTestResult.isSuccess() ? 1 : 5, BaseAssert.failedDetails);
        } catch (Exception ex) {
            ReportUtils.logAll("Invalid testcase ID, cannot update result to TestRail.");
        }
    }
}