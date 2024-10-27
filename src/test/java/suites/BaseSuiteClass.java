/*package suites;

import common.AllureListener;
import common.TestNGListener;
import common.UserSettings;
import common.utils.BasicUtility;
import common.utils.FileUtility;
import common.utils.RandomUtility;
import common.utils.SystemUtility;
import DriverFactory.DriverManagerAbstractClass;
import DriverFactory.DriverManagerFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Listeners({TestNGListener.class, AllureListener.class})
public class BaseSuiteClass {
    public static String currentTestName;
    public static String runURL, databaseName;
    public static URL projectUrl;
    public static long implicitWaitTimeout, explicitWaitTimeout;
    private static Capabilities cap;
    private static boolean headlessToggle;
    private static final ThreadLocal<WebDriver> driverLocal = new ThreadLocal<>();
    private static final ThreadLocal<DriverManagerAbstractClass> manLocal = new ThreadLocal<>();
    private static final Map<String, String> allureEnvMap = new HashMap<>() {{
        put("URL", "[no data]");
        put("Browser", "[no data]");
        put("Platform", "[no data]");
        put("Project Version", "[no data]");
    }};
    protected static final RandomUtility rndUtil = new RandomUtility();

    @BeforeSuite(alwaysRun = true)
    @Parameters({"targetURL", "headlessMode", "waitImplicit", "waitExplicit"})
    public void initSetup(@Optional String targetURL, @Optional String headlessMode, @Optional String waitImplicit, @Optional String waitExplicit) {
        new UserSettings().getUserSettings();
        String configUrl = UserSettings.runOnStage ? UserSettings.projectUrlStage : UserSettings.projectUrl;
        /*Checking that the passing parameters are not NULL and has valid data, otherwise pass the default values*/
        /*runURL = ((targetURL != null) ? (targetURL.equals("${site}") ? configUrl : targetURL) : configUrl).replaceAll("/$", "");
        headlessToggle = Boolean.parseBoolean((headlessMode != null) ? (headlessMode.equals("${headlessMode}") ? UserSettings.defaultHeadlessMode : headlessMode) : UserSettings.defaultHeadlessMode);
        implicitWaitTimeout = Integer.parseInt((waitImplicit != null) ? (waitImplicit.equals("${waitImplicit}") ? UserSettings.timeoutImplicit : waitImplicit) : UserSettings.timeoutImplicit);
        explicitWaitTimeout = Long.parseLong((waitExplicit != null) ? (waitExplicit.equals("${waitExplicit}") ? UserSettings.timeoutExplicit : waitExplicit) : UserSettings.timeoutExplicit);
        /*Delete old screenshots and logs before test run*/
        /*if (UserSettings.clearScreenshots) {
            SystemUtility.clearFiles(SystemUtility.getPathCanonical(UserSettings.screenshotFolder), "*.png");
        }
        if (UserSettings.clearLogs) {
            SystemUtility.clearFiles(SystemUtility.getPathCanonical(UserSettings.logsFolder), "*.log");
        }
        databaseName = new FileUtility().getProjectConfigDatabaseName();
        projectUrl = new BasicUtility().getUrlFromString(runURL);
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
        List<String> chromeOptions = new ArrayList<>() {{
            add("use-fake-ui-for-media-stream");
            add("--ignore-certificate-errors");
            add("enable-logging");
            add("--no-sandbox");
            add("--lang=en-US");
            add("--remote-allow-origins=*");
            add("--allow-running-insecure-content");
        }};
        if (UserSettings.showBrowserConsole) {
            chromeOptions.add("--auto-open-devtools-for-tabs");
        }
        manLocal.set(new DriverManagerFactory().getManager(chromeOptions, implicitWaitTimeout, browserPosition, headlessToggle, "--window-size=1920,1080", null));
        driverLocal.set(getManager().startDriver());
        cap = ((RemoteWebDriver) getDriver()).getCapabilities();
        //        new BrowserUtils().setNetworkListener((ChromeDriver) getDriver()); //Disabled because we don't use it for now, but it gathers all JSON responses and affect the test performance
        getDriver().navigate().to(projectUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        driverClose();
    }

    @Step("Close browser")
    public void driverClose() {
        getManager().quitDriver();
    }

    public WebDriver getDriver() {
        return driverLocal.get();
    }

    public static DriverManagerAbstractClass getManager() {
        return manLocal.get();
    }

}
*/