package drivermanager;

import common.UserSettings;
import common.utils.SystemUtility;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.time.Duration;
import java.util.HashMap;
import java.util.logging.Level;

public class DriverManager {
    public static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static Proxy proxy;

    public static void setProxy(String proxyHost, int proxyPort) {
        if (proxyHost != null && !proxyHost.isEmpty() && proxyPort > 0) {
            proxy = new Proxy();
            proxy.setHttpProxy(proxyHost + ":" + proxyPort);
            proxy.setSslProxy(proxyHost + ":" + proxyPort);
        } else {
            proxy = null;
        }
    }

    public static void createDriver(String browserType, Point browserPosition) {
        WebDriver webDriver;
        switch (browserType.toLowerCase()) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                LoggingPreferences logPrefs = new LoggingPreferences();
                logPrefs.enable(LogType.BROWSER, Level.ALL);
                logPrefs.enable(LogType.DRIVER, Level.ALL);
                logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
                chromeOptions.addArguments("use-fake-ui-for-media-stream");
                chromeOptions.addArguments("--ignore-certificate-errors");
                chromeOptions.addArguments("enable-logging");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--lang=en-US");
                chromeOptions.addArguments("--remote-allow-origins=*");
                chromeOptions.addArguments("--allow-running-insecure-content");
                chromeOptions.setCapability("goog:loggingPrefs", logPrefs);
                chromeOptions.addArguments("--window-position=" + browserPosition.getX() + "," + browserPosition.getY());

                if (proxy != null) {
                    chromeOptions.setCapability(CapabilityType.PROXY, proxy);
                }

                if (UserSettings.showBrowserConsole) {
                    chromeOptions.addArguments("--auto-open-devtools-for-tabs");
                } else if (Boolean.parseBoolean(UserSettings.defaultHeadlessMode)) {
                    chromeOptions.addArguments("--headless=new");
                    chromeOptions.addArguments("--start-maximized");
                }

                HashMap<String, Object> chromePreferences = new HashMap<>();
                chromePreferences.put("download.default_directory", SystemUtility.getPathCanonical(UserSettings.downloadFolder));

                chromeOptions.setExperimentalOption("prefs", chromePreferences);
                webDriver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                webDriver = new FirefoxDriver();
                break;
            default:
                throw new IllegalArgumentException("Unsupported browser type: " + browserType);
        }

        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Long.parseLong(UserSettings.timeoutImplicit)));
        webDriver.manage().window().maximize();
        driver.set(webDriver);
    }

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }

}
