package core;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utilities.ReportUtils;

public class ExecutionRetry implements IRetryAnalyzer {
    private int retryCount = 1;

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (iTestResult.isSuccess() || retryCount == 0)
            return false;
        ReportUtils.enableLog();
        ReportUtils.logAll("This test is failed. Retrying it.");
        Browser.clearLocalStorage();
        retryCount--;
        return true;
    }
}
