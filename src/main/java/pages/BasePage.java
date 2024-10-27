package pages;

import common.UserSettings;
import common.utils.SettingsHelper;
import drivermanager.DriverManager;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public class BasePage {
    protected WebDriver driver;
    public static final int EXPLICIT_TIMEOUT = getExplicitTimeout();
    public static final int IMPLICIT_TIMEOUT = getImplicitTimeout();

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    protected static int getExplicitTimeout(){
        new UserSettings().getUserSettings();
        return Integer.parseInt(UserSettings.timeoutExplicit);
    }

    protected static int getImplicitTimeout(){
        new UserSettings().getUserSettings();
        return Integer.parseInt(UserSettings.timeoutImplicit);
    }

    protected static String getRunURL() {
        return SettingsHelper.getRunURL();
    }

    protected static String getDatabaseName() {
        return SettingsHelper.getDatabaseName();
    }

    protected static URL getProjectUrl() {
        return SettingsHelper.getProjectUrl();
    }
}