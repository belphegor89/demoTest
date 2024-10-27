package common;

import common.utils.SystemUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class UserSettings {
    public static final File screenshotFolder = new File("./RESULTS/Screenshots/"),
            downloadFolder = new File("./RESULTS/Downloads/"),
            logsFolder = new File("./RESULTS/Logs/"),
            backupFolder = new File("./RESULTS/Backups/"),
            resourcesFolder = new File("src/test/resources"),
            uploadFolder = new File(resourcesFolder + "/Upload/");

    public static String browser, projectName, smartyDockerFolder, smartyProjectsFolder, projectUrl, projectUrlStage, timeoutExplicit, timeoutImplicit, browserPosition, defaultHeadlessMode, projectFolder, stageLanIp, stageDbDockerIp, stageDbName, stageDbUser, stageDbPassword, stageCsUrl, stageNodeUrl, stageProjectName, stageProjectType, stageProjectEmail, stageAsyncName;
    public static boolean clearScreenshots, clearLogs, prepareTests, allureGenerateOpen, allureClearAfter, runOnStage, showBrowserConsole;

    public void getUserSettings() {
        Properties userProps = getProperties(SystemUtility.getPathCanonical(resourcesFolder) + "/settings.properties");
        browser = userProps.getProperty("browser");
        projectName = userProps.getProperty("currentProject");
        smartyProjectsFolder = userProps.getProperty("smartyProjectsFolder");
        projectFolder = smartyProjectsFolder + projectName;
        smartyDockerFolder = userProps.getProperty("smartyDockerFolder");
        projectUrl = userProps.getProperty("projectUrl");
        projectUrlStage = userProps.getProperty("projectUrlStage");
        prepareTests = Boolean.parseBoolean(userProps.getProperty("prepareTests"));
        clearScreenshots = Boolean.parseBoolean(userProps.getProperty("clearScreenshots"));
        clearLogs = Boolean.parseBoolean(userProps.getProperty("clearLogs"));
        allureGenerateOpen = Boolean.parseBoolean(userProps.getProperty("allureGenerateOpen"));
        allureClearAfter = Boolean.parseBoolean(userProps.getProperty("allureClearResultsAfterGenerate"));
        timeoutExplicit = userProps.getProperty("timeoutExplicit");
        timeoutImplicit = userProps.getProperty("timeoutImplicit");
        defaultHeadlessMode = userProps.getProperty("defaultHeadlessMode");
        showBrowserConsole = Boolean.parseBoolean(userProps.getProperty("showBrowserConsole"));
        browserPosition = userProps.getProperty("browserPosition");
        //Stage settings
        runOnStage = Boolean.parseBoolean(userProps.getProperty("runOnStage"));
        stageLanIp = userProps.getProperty("stageLanIp");
        stageDbDockerIp = userProps.getProperty("stageDbDockerIp");
        stageDbName = userProps.getProperty("stageDbName");
        stageDbUser = userProps.getProperty("stageDbUser");
        stageDbPassword = userProps.getProperty("stageDbPassword");
        stageCsUrl = userProps.getProperty("stageCsUrl");
        stageNodeUrl = userProps.getProperty("stageNodeUrl");
        stageProjectName = userProps.getProperty("stageProjectName");
        stageProjectType = userProps.getProperty("stageProjectType");
        stageProjectEmail = userProps.getProperty("stageProjectEmail");
        stageAsyncName = userProps.getProperty("stageAsyncName");
    }

    public void setEnvironment(Map<String, String> properties) {
        String fileName = "environment.properties";
        File envFile = new File(fileName);
        Properties allureProps = getProperties(fileName);
        try {
            FileOutputStream out = new FileOutputStream(envFile);
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                allureProps.setProperty(entry.getKey(), entry.getValue());
            }
            allureProps.store(new FileOutputStream(envFile), null);
            out.close();
        } catch (IOException ex) {
            System.err.println("Unable to save file " + fileName + "\n");
            ex.printStackTrace();
        }
    }

    private Properties getProperties(String file) {
        File path = new File(file);
        Properties properties = new Properties();
        try {
            FileInputStream propsFile = new FileInputStream(file);
            properties.load(propsFile);
            propsFile.close();
        } catch (IOException ex) {
            System.err.println("Unable to read file " + path.getAbsolutePath() + "\n");
            ex.printStackTrace();
        }
        return properties;
    }

    public void setProperties(String fileName, Map<String, String> properties) {
        File propFile = new File(fileName);
        Properties allureProps = getProperties(fileName);
        try {
            FileOutputStream out = new FileOutputStream(propFile);
            for (Map.Entry<String, String> entry : properties.entrySet()) {
                allureProps.setProperty(entry.getKey(), entry.getValue());
            }
            allureProps.store(new FileOutputStream(propFile), null);
            out.close();
        } catch (IOException ex) {
            System.err.println("Unable to save file " + fileName + "\n");
            ex.printStackTrace();
        }
    }

    public void setProperty(String fileName, String settingName, String settingValue) {
        File propFile = new File(fileName);
        Properties allureProps = getProperties(fileName);
        try {
            FileOutputStream out = new FileOutputStream(propFile);
            allureProps.setProperty(settingName, settingValue);
            allureProps.store(new FileOutputStream(propFile), null);
            out.close();
        } catch (IOException ex) {
            System.err.println("Unable to save file " + fileName + "\n");
            ex.printStackTrace();
        }
    }

}
