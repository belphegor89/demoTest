package common;

import common.utils.BrowserUtility;
import common.utils.FileUtility;
import common.utils.SystemUtility;
import io.qameta.allure.Attachment;
import io.qameta.allure.listener.TestLifecycleListener;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.TestResult;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestListener;
import org.testng.ITestResult;
import suites.BaseSuiteClassNew;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;

public class AllureListener extends BaseSuiteClassNew implements TestLifecycleListener, ITestListener {
    BrowserUtility browserUtility = new BrowserUtility();
    FileUtility fileUtility = new FileUtility();
    Set<Cookie> cookies;

    public void beforeTestStop(TestResult result) {
        if (result.getStatus() == Status.FAILED || result.getStatus() == Status.BROKEN) {
            if (getDriver() != null) {
                makeScreenshot((ChromeDriver) getDriver(), result.getFullName() + "_final");
            } else {
                System.err.println("Driver is null in Allure Listener!");
            }
        }
        getLog(LogType.BROWSER, Level.INFO.intValue(), result.getFullName());
    }

    @Attachment(value = "Final screenshot", type = "image/png")
    public byte[] makeScreenshot(ChromeDriver driver, String screenshotName) {
        return browserUtility.takeFullPageScreenshot(driver, screenshotName);
    }

    @Attachment(value = "Cookies", type = "text/plain")
    private String getCookies(String testName) {
        String cookie_ = "";
        cookies = getDriver().manage().getCookies();
        for (Cookie cookie : cookies) {
            cookie_ = cookie_ + "Domain: " + cookie.getDomain() + "\n" + "Path: " + cookie.getPath()
                    + "\n" + "Name: " + cookie.getName() + "\n" + "Value: " + cookie.getValue()
                    + "\n" + "Expires: " + cookie.getExpiry() +
                    "\n==============\n";
        }
        if (!cookie_.equals("")) {
            //            Util.writeFileAppend(UserSettings.screenshotFolder+"Cookies-Persistent.log", "Test: "+testName+"\n"+cookie_+"\n[......]\n");
        }
        System.out.println("Cookies are saved");
        return cookie_;
    }

    @Attachment(value = "{logType} log", type = "text/plain")
    private String getLog(String logType, int logLevel, String testName) {
        String log = "";
        Predicate<LogEntry> filter = entry -> entry.getLevel().intValue() >= logLevel;
        LogEntries entries = getDriver().manage().logs().get(logType);
        List<LogEntry> entryList = entries.getAll().stream().filter(filter).toList();
        if (!entryList.isEmpty()) {
            for (Object logEntry : entryList) {
                log = log + "\n" + logEntry.toString();
            }
        }
        if (testName != null && !log.isEmpty()) {
            fileUtility.writeFileAppend(SystemUtility.getPathCanonical(UserSettings.logsFolder) + "/" + logType + "-" + testName + ".log", log);
            System.out.println(logType + " log:\n" + log);
            return log;
        } else {
            return null;
        }
    }

    private void setTestErrors(ITestResult tResult) {
        Map<String, Integer> errCodes = getErrorCodeConsole();
        if (!errCodes.isEmpty()) {
            Throwable throwable = tResult.getThrowable();
            StringBuilder newMessage = new StringBuilder(throwable.getMessage());
            newMessage.append("\n\n*****\nStatusCodes==>");
            for (Entry<String, Integer> code : errCodes.entrySet()) {
                newMessage.append("<<").append(code.getKey()).append(" |::| ").append(code.getValue()).append(">>");
            }
            newMessage.append("\n\n*****\n");
            try {
                FieldUtils.writeField(throwable, "detailMessage", newMessage.toString(), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, Integer> getErrorCodeConsole() {
        Map<String, Integer> errorCodes = new HashMap<>();
        return errorCodes;
    }

}