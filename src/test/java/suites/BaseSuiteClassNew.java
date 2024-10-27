package suites;

import common.AllureListener;
import common.TestNGListener;
import common.UserSettings;
import common.utils.RandomUtility;
import common.utils.SettingsHelper;
import common.utils.SystemUtility;
import drivermanager.DriverManager;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;
import org.openqa.selenium.Point;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static common.UserSettings.browser;

@Listeners({TestNGListener.class, AllureListener.class})
public class BaseSuiteClassNew {
    public static String currentTestName;
    public static String runURL, databaseName;
    public static URL projectUrl;
    private static Capabilities cap;
    private static final Map<String, String> allureEnvMap = new HashMap<>() {{
        put("URL", "[no data]");
        put("Browser", "[no data]");
        put("Platform", "[no data]");
    }};
    protected static final RandomUtility rndUtil = new RandomUtility();

    @BeforeSuite(alwaysRun = true)
    @Parameters({"targetURL", "proxyHost", "proxyPort"})
    public void initSetup(@Optional String targetURL, @Optional String proxyHost, @Optional String proxyPort) {
        new UserSettings().getUserSettings();
        SettingsHelper.initializeUrls(targetURL);
        /*Set proxy if required*/
        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort != null && !proxyPort.isEmpty()) {
            try {
                int port = Integer.parseInt(proxyPort);
                DriverManager.setProxy(proxyHost, port);
            } catch (NumberFormatException e) {
                System.err.println("Invalid proxy port number: " + proxyPort);
            }
        }
        /*Delete old screenshots and logs before test run*/
        if (UserSettings.clearScreenshots) {
            SystemUtility.clearFiles(SystemUtility.getPathCanonical(UserSettings.screenshotFolder), "*.png");
        }
        if (UserSettings.clearLogs) {
            SystemUtility.clearFiles(SystemUtility.getPathCanonical(UserSettings.logsFolder), "*.log");
        }
        runURL = SettingsHelper.getRunURL();
        projectUrl = SettingsHelper.getProjectUrl();
        databaseName = SettingsHelper.getDatabaseName();
        System.out.println("Run URL: " + runURL);
        System.out.println("Project URL: " + projectUrl);
    }

    @AfterSuite
    public void report() {
        allureEnvMap.put("URL", runURL);
        allureEnvMap.put("Browser", cap.getBrowserName() + " v. " + cap.getBrowserVersion());
        allureEnvMap.put("Platform", System.getProperty("os.name").toLowerCase() + " (v. " + System.getProperty("os.version") + ")");
        new UserSettings().setEnvironment(allureEnvMap);
        SystemUtility.allureReport(UserSettings.allureGenerateOpen, UserSettings.allureClearAfter);
    }

    @BeforeMethod(alwaysRun = true)
    public void driverRun() {
        int posX = Integer.parseInt(UserSettings.browserPosition.substring(0, UserSettings.browserPosition.indexOf("x"))),
                posY = Integer.parseInt(UserSettings.browserPosition.substring(UserSettings.browserPosition.indexOf("x") + 1));
        Point browserPosition = new Point(posX, posY);
        DriverManager.createDriver(browser, browserPosition);
        cap = ((RemoteWebDriver) getDriver()).getCapabilities();
        DriverManager.getDriver().get(String.valueOf(projectUrl));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverManager.quitDriver();
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }

}
