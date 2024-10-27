package common;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import suites.BaseSuiteClassNew;

public class TestNGListener extends BaseSuiteClassNew implements ITestListener {
    //    int testCntCurrent, testCntSum;

    @Override
    public void onStart(ITestContext iTestContext) {
        System.out.println("Running " + iTestContext.getName());
        if (getDriver() != null) {
            iTestContext.setAttribute("WebDriver", getDriver());
            //            testCntSum = iTestContext.getSuite().getAllMethods().size();
        } else {
            System.err.println("Driver is not initialized!");
        }
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("Running " + iTestContext.getName() + " is finished");
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        BaseSuiteClassNew.currentTestName = getTestMethodName(iTestResult);
        //        testCntCurrent++;
        //        int cntFailed, cntSuccess;
        //        cntFailed = iTestResult.getTestContext().getFailedTests().size();
        //        cntSuccess = iTestResult.getTestContext().getPassedTests().size();
        //        System.out.println("Test " + getTestMethodName(iTestResult) + " (" + testCntCurrent + "/" + testCntSum + ")" + " started");
        //        System.out.println("Failed/Success rate: (" + cntFailed + "/" + cntSuccess + ")" + " of " + testCntSum);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("[+++] -> Test [" + getTestMethodName(iTestResult) + "] passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.err.println("[!!!] -> Test [" + getTestMethodName(iTestResult) + "] failed!");
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.err.println("[???] -> Test [" + getTestMethodName(iTestResult) + "] skipped");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        System.out.println("[~~~] -> " + getTestMethodName(iTestResult));
    }

    private String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getTestClass().getName() + "." + iTestResult.getMethod().getConstructorOrMethod().getName();
    }

}