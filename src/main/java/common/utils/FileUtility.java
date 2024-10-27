package common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import common.UserSettings;
import common.Waits;
import data.StaticData;
import io.qameta.allure.Step;
import org.testng.asserts.Assertion;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FileUtility {

    //<editor-fold desc="File operations">
    public void writeFileAppend(String filePath, Object input) {
        BufferedWriter logFile = null;
        try {
            File dir = new File(filePath.substring(0, filePath.lastIndexOf("/")));
            System.out.println("Create directory [" + dir.getCanonicalPath() + "]: " + dir.mkdirs());
            File file = new File(filePath);
            if (file.exists()) {
                logFile = new BufferedWriter(new FileWriter(filePath, true));
            } else {
                logFile = new BufferedWriter(new FileWriter(filePath));
            }
            logFile.write(input.toString());
            logFile.newLine();
            logFile.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (logFile != null) {
                try {
                    logFile.close();
                } catch (IOException ioe2) {
                    // just ignore it
                }
            }
        }
    }

    @Step("Set reCaptcha to test keys")
    public void setRecaptchaToTest() {
        if (!UserSettings.runOnStage) {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File appFile = new File(UserSettings.projectFolder + "/config/application.yml");
            try {
                System.out.println("Setting recaptcha keys to test: " + StaticData.recaptchaTestKeysMap);
                InputStream inputStream = new FileInputStream(appFile);
                Map<String, Object> yamlMap = yaml.load(inputStream);
                Map<String, Object> app = (Map<String, Object>) yamlMap.get("application");
                app.put("google.captcha", StaticData.recaptchaTestKeysMap);
                yamlMap.put("application", app);
                PrintWriter writer = new PrintWriter(appFile);
                yaml.dump(yamlMap, writer);
                System.out.println("Recaptcha keys successfully set to test");
            } catch (IOException ex) {
                System.err.println("Unable to save file " + appFile + "\n");
                ex.printStackTrace();
            }
        } else {
            System.err.println("RUNNING ON STAGE, NO CAPTCHA KEYS WERE CHANGED");
        }
    }

    @Step("Restore production reCaptcha keys from backup")
    public void restoreRecaptchaFromBackup() {
        if (!UserSettings.runOnStage) {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File projectFile = new File(UserSettings.projectFolder + "/config/application.yml"),
                    backupFile = new File(SystemUtility.getPathCanonical(UserSettings.backupFolder) + "/" + UserSettings.projectName + "/config/application.yml");
            try {
                System.out.println("Getting recaptcha keys from backup:");
                InputStream inputStreamBackup = new FileInputStream(backupFile);
                Map<String, Object> yamlMapBackup = yaml.load(inputStreamBackup);
                Map<String, Object> appBackup = (Map<String, Object>) yamlMapBackup.get("application");
                Map<String, String> recaptchaKeysBackup = (Map<String, String>) appBackup.get("google.captcha");
                System.out.println(recaptchaKeysBackup);
                System.out.println("Restoring recaptcha keys");
                InputStream inputStreamProject = new FileInputStream(projectFile);
                Map<String, Object> yamlMapProject = yaml.load(inputStreamProject);
                Map<String, Object> appProject = (Map<String, Object>) yamlMapProject.get("application");
                appProject.put("google.captcha", recaptchaKeysBackup);
                yamlMapProject.put("application", appProject);
                PrintWriter writer = new PrintWriter(projectFile);
                yaml.dump(yamlMapProject, writer);
                System.out.println("Recaptcha keys successfully restored");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.err.println("RUNNING ON STAGE, NO CAPTCHA KEYS WERE CHANGED");
        }
    }

    public String getProjectConfigDatabaseName() {
        String dbNameResult = "no default database found";
        if (UserSettings.runOnStage) {
            dbNameResult = UserSettings.stageDbName;
        } else {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File projectFile = new File(UserSettings.projectFolder + "/config/database.yml");
            try {
                InputStream inputStreamYaml = new FileInputStream(projectFile);
                Map<String, Object> yamlMap = yaml.load(inputStreamYaml);
                Map<String, Object> dbMap = (Map<String, Object>) yamlMap.get("databases");
                Map<String, Object> dbDefaultMap = (Map<String, Object>) dbMap.get("default");
                dbNameResult = (String) dbDefaultMap.get("database");
                System.out.println("Getting database name from default: " + dbNameResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return dbNameResult;
    }

    public String getCookieSyncUrl() {
        String cookieSyncResult = "no cookie sync";
        if (UserSettings.runOnStage) {
            cookieSyncResult = UserSettings.stageCsUrl;
        } else {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File projectFile = new File(UserSettings.projectFolder + "/config/application.yml");
            try {
                InputStream inputStream = new FileInputStream(projectFile);
                Map<String, Object> yamlMap = yaml.load(inputStream);
                Map<String, Object> applicationYaml = (Map<String, Object>) yamlMap.get("application");
                Map<String, Object> applicationSettingsYaml = (Map<String, Object>) applicationYaml.get("settings");
                cookieSyncResult = "https://" + applicationSettingsYaml.get("cookie_sync");
                System.out.println("Getting cookie sync url: " + cookieSyncResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return cookieSyncResult;
    }

    public String getNodeUrl() {
        String cookieSyncResult = "no cookie sync";
        if (UserSettings.runOnStage) {
            cookieSyncResult = UserSettings.stageNodeUrl;
        } else {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File projectFile = new File(UserSettings.projectFolder + "/config/application.yml");
            try {
                InputStream inputStream = new FileInputStream(projectFile);
                Map<String, Object> yamlMap = yaml.load(inputStream);
                Map<String, Object> applicationYaml = (Map<String, Object>) yamlMap.get("application");
                Map<String, Object> applicationSettingsYaml = (Map<String, Object>) applicationYaml.get("settings");
                cookieSyncResult = "https://" + applicationSettingsYaml.get("node_id");
                System.out.println("Getting cookie sync url: " + cookieSyncResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (cookieSyncResult.endsWith("/")) {
            cookieSyncResult = cookieSyncResult.substring(0, cookieSyncResult.length() - 1);
        }
        return cookieSyncResult;
    }

    public String getProjectName() {
        String projectNameResult = "no project Name";
        if (UserSettings.runOnStage) {
            projectNameResult = UserSettings.stageProjectName;
        } else {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File projectFile = new File(UserSettings.projectFolder + "/config/client.yml");
            try {
                InputStream inputStream = new FileInputStream(projectFile);
                Map<String, Object> yamlMap = yaml.load(inputStream);
                projectNameResult = (String) yamlMap.get("name");
                System.out.println("Getting project Name : " + projectNameResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return projectNameResult;
    }

    public String getProjectType() {
        String projectTypeResult = "no project Type";
        if (UserSettings.runOnStage) {
            projectTypeResult = UserSettings.stageProjectType;
        } else {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File projectFile = new File(UserSettings.projectFolder + "/config/client.yml");
            try {
                InputStream inputStream = new FileInputStream(projectFile);
                Map<String, Object> yamlMap = yaml.load(inputStream);
                projectTypeResult = (String) yamlMap.get("type");
                System.out.println("Getting project Type : " + projectTypeResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return projectTypeResult;
    }

    public String getProjectEmail() {
        String projectEmailResult = "no project Email";
        if (UserSettings.runOnStage) {
            projectEmailResult = UserSettings.stageProjectEmail;
        } else {
            File projectFile = new File(UserSettings.projectFolder + "/config/client.yml");
            try {
                InputStream inputStream = new FileInputStream(projectFile);
                Yaml yaml = new Yaml();
                Map<String, Object> yamlMap = yaml.load(inputStream);
                if (yamlMap.containsKey("emails")) {
                    Map<String, Map<String, String>> emailsMap = (Map<String, Map<String, String>>) yamlMap.get("emails");
                    if (emailsMap != null && emailsMap.containsKey("publisher")) {
                        Map<String, String> publisherInfo = emailsMap.get("publisher");
                        if (publisherInfo != null && publisherInfo.containsKey("email")) {
                            projectEmailResult = publisherInfo.get("email");
                        }
                    }
                }
                System.out.println("Getting project Email : " + projectEmailResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return projectEmailResult;
    }

    public String getAsyncName() {
        String asyncResult = "no async";
        if (UserSettings.runOnStage) {
            asyncResult = UserSettings.stageAsyncName;
        } else {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            options.setPrettyFlow(true);
            Yaml yaml = new Yaml(options);
            File projectFile = new File(UserSettings.projectFolder + "/config/application.yml");
            try {
                InputStream inputStream = new FileInputStream(projectFile);
                Map<String, Object> yamlMap = yaml.load(inputStream);
                Map<String, Object> applicationYaml = (Map<String, Object>) yamlMap.get("application");
                Map<String, Object> applicationSettingsYaml = (Map<String, Object>) applicationYaml.get("settings");
                asyncResult = (String) applicationSettingsYaml.get("async_name");
                System.out.println("Getting async name: " + asyncResult);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return asyncResult;
    }

    public File getDownloadedFile(String fileToDownload, int timeSeconds) {
        String fileName = fileToDownload.substring(0, fileToDownload.lastIndexOf('.')), fileFormat = fileToDownload.substring(fileToDownload.lastIndexOf('.'));
        int cnt = 0;
        File file, returnFile = null, dir = UserSettings.downloadFolder;
        FilenameFilter filter = (dir1, name) -> name.matches(fileName + ".*" + fileFormat);
        List<String> childrenList;
        whileLoop:
        while (cnt < timeSeconds) {
            childrenList = Arrays.asList(Objects.requireNonNull(dir.list(filter)));
            if (!childrenList.isEmpty()) {
                for (String child : childrenList) {
                    file = new File(SystemUtility.getPathCanonical(dir) + "/" + child);
                    if (file.lastModified() > (System.currentTimeMillis() - 3 * 1000L)) { //Searching for the file, modified in the last 3 seconds
                        returnFile = file;
                        break whileLoop;
                    }
                }
            }
            new Waits().sleep(1000); //Repeat search every 1 second
            cnt++;
        }
        new Assertion().assertNotNull(returnFile, "File [" + fileToDownload + "] is not present in the [" + UserSettings.downloadFolder + "]");
        new SystemUtility().allureAddFileAttachment(fileToDownload, returnFile);
        return returnFile;
    }

    public String readFileToString(File file) {
        String returnContent = "NO CONTENT";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            reader.close();
            returnContent = stringBuilder.toString();
        } catch (IOException fnf) {
            System.err.println("Error on file reading: " + fnf.getMessage());
            fnf.printStackTrace();
        }
        return returnContent;
    }

    public MappingIterator<Map<String, Object>> readCsvFile(File file) {
        try {
            CsvMapper csvMapper = new CsvMapper().enable(CsvParser.Feature.FAIL_ON_MISSING_COLUMNS);
            CsvSchema csvSchema = csvMapper.typedSchemaFor(Map.class).withColumnSeparator(',').withHeader();
            return csvMapper.readerFor(Map.class).with(csvSchema).readValues(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void writeCsvFile(File file, List<Map<String, Object>> data) {
        try {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema.Builder csvSchemaBuilder = CsvSchema.builder().setUseHeader(true);
            if (!data.isEmpty()) {
                for (String key : data.get(0).keySet()) {
                    csvSchemaBuilder.addColumn(key);
                }
            }
            CsvSchema csvSchema = csvSchemaBuilder.build();
            ObjectWriter writer = csvMapper.writerFor(new TypeReference<List<Map<String, Object>>>() {
            }).with(csvSchema);
            writer.writeValue(file, data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void backupProjectConfig() {
        if (!UserSettings.runOnStage) {
            File source = new File(UserSettings.projectFolder + "/config/"),
                    targetDir = new File(SystemUtility.getPathCanonical(UserSettings.backupFolder) + "/" + UserSettings.projectName + "/config/");
            try {
                if (!targetDir.exists()) {
                    targetDir.mkdirs();
                }
                DirectoryStream<Path> files = Files.newDirectoryStream(Path.of(SystemUtility.getPathCanonical(source)));
                for (Path f : files) {
                    String targetPath = SystemUtility.getPathCanonical(targetDir) + System.getProperty("file.separator") + f.getFileName();
                    File target = new File(targetPath);
                    if (!target.exists()) {
                        Files.copy(f, Paths.get(targetDir.getCanonicalPath()).resolve(f.getFileName()));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("RUNNING ON STAGE, NO BACKUP WAS CREATED");
        }
    }

    //</editor-fold>


}
