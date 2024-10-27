package common.utils;

import io.qameta.allure.Allure;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;

public class SystemUtility {

    //<editor-fold desc="Allure">
    public static void allureReport(boolean generateOpen, boolean clearAfter) {
        String[] copyEnvironment, copyCategories, copyHistory, reportGenerate, reportOpen;
        ProcessBuilder pb = new ProcessBuilder();
        Process process;
        String allureEnv = getPathCanonical(new File("environment.properties")),
                allureCategories = getPathCanonical(new File("categories.json"));
        String resultRoot = getPathCanonical(new File("./RESULTS/Allure/results")),
                reportFolder = getPathCanonical(new File("./RESULTS/Allure/reports/"
                        + new BasicUtility().getCurrentDateTimeFormatted("yyyy-MM-dd", Locale.ENGLISH) + "/"
                        + new BasicUtility().getCurrentDateTimeFormatted("HH-mm", Locale.ENGLISH)));
        if (org.apache.commons.lang3.SystemUtils.IS_OS_UNIX) {
            copyEnvironment = new String[]{"cp", allureEnv, resultRoot};
            copyCategories = new String[]{"cp", allureCategories, resultRoot};
            copyHistory = new String[]{"cp", "-a", reportFolder + "/history/.", resultRoot + "/history/"};
            reportGenerate = new String[]{"allure", "generate", "-c", resultRoot, "--output", reportFolder};
            reportOpen = new String[]{"allure", "open", reportFolder};
        } else {
            copyEnvironment = new String[]{"cmd", "/c", "copy", "/y", allureEnv, resultRoot};
            copyCategories = new String[]{"cmd", "/c", "copy", "/y", allureCategories, resultRoot};
            copyHistory = new String[]{"cmd", "/c", "xcopy", "/y", reportFolder + "\\history\\", resultRoot + "\\history\\"};
            reportGenerate = new String[]{"cmd", "/c", "allure", "generate", "-c", resultRoot, "--output", reportFolder};
            reportOpen = new String[]{"cmd", "/c", "allure", "open", reportFolder};
        }
        try {
            process = pb.command(copyEnvironment).start();
            process.waitFor();
            process.destroy();
            process = pb.command(copyCategories).start();
            process.waitFor();
            process.destroy();
            if (generateOpen) {
                /*Create a report from the raw results*/
                process = pb.command(reportGenerate).start();
                process.waitFor();
                process.destroy();
                /*Saving history from the report to the results to keep for the future reports*/
                process = pb.command(copyHistory).start();
                process.waitFor();
                process.destroy();
                System.out.println("Report is ready and saved: " + reportFolder);
                /*Open the generated report*/
                pb.command(reportOpen).start();
            }
            if (clearAfter) {
                /*Clear the results*/
                clearFiles(resultRoot, "*-attachment");
                clearFiles(resultRoot, "*.properties");
                clearFiles(resultRoot, "*.json");
            }
        } catch (IOException | InterruptedException exp) {
            exp.printStackTrace();
        }
    }

    public static String getPathCanonical(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void allureAddFileAttachment(String attachmentName, File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            Allure.addAttachment(attachmentName, is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void allureAttachConsoleLogRecord(String logTitle, Exception exception) {
        String exceptionMessage = exception != null ? exception.getMessage() : "";
        String logBody = logTitle + "\n" + exceptionMessage;
        System.err.println(logBody);
        allureAddLogAttachment("Exception log: " + logTitle, logBody);
    }

    public void allureAddLogAttachment(String logName, String logText) {
        Allure.addAttachment(logName, "text/plain", logText, "txt");
    }

    //</editor-fold>

    //<editor-fold desc="Commandline operations">
    public static void clearFiles(String directoryCanonical, String fileNamePattern) {
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Path.of(directoryCanonical), fileNamePattern)) {
            for (Path newDirectoryStreamItem : directoryStream) {
                Files.delete(newDirectoryStreamItem);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public Object getClipboardContent(DataFlavor flavor) {
        Object clipboardData;
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            clipboardData = cb.getContents(null).getTransferData(flavor);
        } catch (UnsupportedFlavorException | IOException e) {
            clipboardData = e.getMessage() + "\n" + Arrays.toString(e.getStackTrace());
            e.printStackTrace();
        }
        return clipboardData;
    }

    private static void cmdReader(Process process) throws IOException {
        String s;
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        System.out.println("Commandline message:\n");
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }
    }

    //</editor-fold>

}
