package common.utils;

import common.UserSettings;

import java.net.URL;

public class SettingsHelper {
    private static String runURL;
    private static String databaseName;
    private static URL projectUrl;

    public static void initializeUrls(String targetURL) {
        String configUrl = UserSettings.runOnStage ? UserSettings.projectUrlStage : UserSettings.projectUrl;
        setRunURL(((targetURL != null) ? (targetURL.equals("${site}") ? configUrl : targetURL) : configUrl).replaceAll("/$", ""));
        setProjectUrl(new BasicUtility().getUrlFromString(getRunURL()));
        setDatabaseName(new FileUtility().getProjectConfigDatabaseName());
    }

    public static synchronized void setRunURL(String url) {
        runURL = url;
    }

    public static synchronized void setDatabaseName(String name) {
        databaseName = name;
    }

    public static synchronized void setProjectUrl(URL url) {
        projectUrl = url;
    }

    public static String getRunURL() {
        return runURL;
    }

    public static String getDatabaseName() {
        return databaseName;
    }

    public static  URL getProjectUrl() {
        return projectUrl;
    }
}
