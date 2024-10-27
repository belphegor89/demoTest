package common.utils;

import common.Selectors;
import common.UserSettings;
import common.Waits;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.DevToolsException;
import org.openqa.selenium.devtools.v129.network.Network;
import org.openqa.selenium.devtools.v129.network.model.Headers;
import org.openqa.selenium.devtools.v129.network.model.RequestId;
import org.openqa.selenium.devtools.v129.network.model.Response;
import org.openqa.selenium.devtools.v129.page.Page;
import pages.BasePage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

import static drivermanager.DriverManager.getDriver;

public class BrowserUtility extends BasePage {
    private static final Map<String, BrowserResponse> browserResponseMap = new HashMap<>();
    private final SystemUtility SysUtil = new SystemUtility();
    private final ParseAndFormatUtility FormatUtil = new ParseAndFormatUtility();

    //<editor-fold desc="Network listener">
    public void setNetworkListener(ChromeDriver driver) {
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        devTools.addListener(Network.responseReceived(), responseReceived -> {
            Response response = responseReceived.getResponse();
            BrowserResponse br = new BrowserResponse(response.getUrl(), response);
            if (response.getMimeType().contains("application/json")) {
                Network.GetResponseBodyResponse responseBody = getResponseBodyWithRetry(devTools, responseReceived.getRequestId(), 10, 1000);
                if (responseBody != null && responseBody.getBody() != null) {
                    br.setBody(responseBody.getBody());
                    browserResponseMap.put(response.getUrl(), br);
                } else {
                    System.out.println("No body available for this response.");
                }
            }
        });
    }

    public String getResponseBody(String url) {
        BrowserResponse br = getBrowserResponse(url);
        return FormatUtil.formatJsonUnescapeCharacters(br.getBody());
    }

    public BrowserResponse getBrowserResponse(String url) {
        for (Map.Entry<String, BrowserResponse> entry : browserResponseMap.entrySet()) {
            if (entry.getKey().contains(url)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public static Network.GetResponseBodyResponse getResponseBodyWithRetry(DevTools devTools, RequestId requestId, int maxRetries, int delayMillis) {
        int retryCount = 0;
        while (retryCount < maxRetries) {
            try {
                Network.GetResponseBodyResponse responseBody = devTools.send(Network.getResponseBody(requestId));
                if (responseBody != null && responseBody.getBody() != null) {
                    return responseBody;
                }
            } catch (DevToolsException e) {
                System.out.println("Failed to fetch response body, retrying... " + (retryCount + 1));
            }
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            retryCount++;
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="Tabs">
    @Step("Switch to the tab [{index}]")
    public void switchTab(int index) {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(index));
    }

    @Step("Switch to the last tab")
    public void switchTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        int sizeTabs = tabs.size();
        driver.switchTo().window(tabs.get(sizeTabs - 1));
    }

    @Step("Close current tab")
    public void closeTab() {
        driver.close();
        switchTab();
    }
    //</editor-fold>

    //<editor-fold desc="Cookies">
    public void setCookie(WebDriver driver, String name, String value) {
        Cookie cookieFound = null;
        try {
            driver.manage().addCookie(new Cookie(name, value));
        } catch (UnableToSetCookieException | InvalidCookieDomainException ckExc) {
            System.err.println("--->!!!!! Error while adding cookie: ");
            ckExc.printStackTrace();
        } finally {
            for (Cookie cookie : driver.manage().getCookies()) {
                if (cookie.getName().equals(name)) {
                    cookieFound = cookie;
                    break;
                }
            }
        }
        if (cookieFound != null) {
            System.out.println("---> +++ Cookie added:\n[<" + cookieFound.getName() + ">;<" + cookieFound.getValue() + ">;<" + cookieFound.getDomain() + ">" + "]");
        } else {
            System.out.println("---> !!! Cookie not added: [" + name + " : " + value + "]");
        }
    }

    public Map<String, String> getCookie(WebDriver driver, String name) {
        Map<String, String> cookie = new HashMap<>();
        cookie.put("DEFAULT_KEY", "DEFAULT_VALUE");
        for (Cookie availableCookie : driver.manage().getCookies()) {
            if (availableCookie.getName().equalsIgnoreCase(name)) {
                cookie.put(availableCookie.getName(), availableCookie.getValue());
                break;
            }
        }
        return cookie;
    }
    //</editor-fold>

    //<editor-fold desc="JS scripts">
    public void highlightLastElementInteracted(WebDriver driver, WebElement element) {
        try {
            if (driver != null && element != null && element.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.outline='3px dashed red'", element);
                new Waits().sleep(500);
            } else {
                System.err.println("Can't locate the element");
            }
        } catch (Exception ex) {
            SysUtil.allureAttachConsoleLogRecord("Error on element highlight:", ex);
        }
    }

    public void unhighlightLastElementInteracted(WebDriver driver, WebElement element) {
        try {
            if (driver != null && element != null && element.isDisplayed()) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].style.outline='none'", element);
                new Waits().sleep(500);
            } else {
                System.err.println("Can't scroll to the element");
            }
        } catch (Exception ex) {
            SysUtil.allureAttachConsoleLogRecord("Error on element highlight:", ex);
        }
    }

    //</editor-fold>

    public byte[] takeFullPageScreenshot(ChromeDriver driver, String screenshotName) {
        File destination = new File(UserSettings.screenshotFolder + "/" + screenshotName + ".png");
        DevTools chromeDevTools = driver.getDevTools();
        chromeDevTools.createSession();
        highlightLastElementInteracted(driver, Selectors.lastElement);
        try {
            String dataString = chromeDevTools.send(Page.captureScreenshot(Optional.of(Page.CaptureScreenshotFormat.PNG), Optional.of(100), Optional.empty(), Optional.of(true), Optional.of(true), Optional.of(true)));
            unhighlightLastElementInteracted(driver, Selectors.lastElement);
            byte[] screenBytes = Base64.getDecoder().decode(dataString);
            BufferedImage o = ImageIO.read(new ByteArrayInputStream(screenBytes));
            System.out.printf("Full screenshot dimensions: %dx%d%n", o.getWidth(), o.getHeight());
            destination.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.write(screenBytes);
            fileOutputStream.close();
            return screenBytes;
        } catch (Exception e) {
            System.err.println("Exception saving full screenshot (ignored): " + e.getMessage());
            return null;
        }
    }

    public byte[] takeFullPageScreenshot(String screenshotName) {
        ChromeDriver driver = (ChromeDriver) getDriver();
        File destination = new File(UserSettings.screenshotFolder + "/" + screenshotName + ".png");
        DevTools chromeDevTools = driver.getDevTools();
        chromeDevTools.createSession();
        highlightLastElementInteracted(driver, Selectors.lastElement);
        try {
            String dataString = chromeDevTools.send(Page.captureScreenshot(Optional.of(Page.CaptureScreenshotFormat.PNG), Optional.of(100), Optional.empty(), Optional.of(true), Optional.of(true), Optional.of(true)));
            unhighlightLastElementInteracted(driver, Selectors.lastElement);
            byte[] screenBytes = Base64.getDecoder().decode(dataString);
            BufferedImage o = ImageIO.read(new ByteArrayInputStream(screenBytes));
            System.out.printf("Full screenshot dimensions: %dx%d%n", o.getWidth(), o.getHeight());
            destination.getParentFile().mkdirs();
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.write(screenBytes);
            fileOutputStream.close();
            return screenBytes;
        } catch (Exception e) {
            System.err.println("Exception saving full screenshot (ignored): " + e.getMessage());
            return null;
        }
    }

    public static class BrowserResponse {
        private String url, body;
        private Response response;
        private RequestId requestId;

        public BrowserResponse() {
        }

        public BrowserResponse(String url, Response response) {
            this.url = url;
            this.response = response;
        }

        public String getUrl() {
            return url;
        }

        public BrowserResponse setUrl(String url) {
            this.url = url;
            return this;
        }

        public Response getResponse() {
            return response;
        }

        public BrowserResponse setResponse(Response response) {
            this.response = response;
            return this;
        }

        public String getBody() {
            return body;
        }

        public BrowserResponse setBody(String body) {
            this.body = body;
            return this;
        }

        public int getCode() {
            return response.getStatus();
        }

        public String getMime() {
            return response.getMimeType();
        }

        public Headers getHeaders() {
            return response.getHeaders();
        }

        public RequestId getRequestId() {
            return requestId;
        }

        public BrowserResponse setRequestId(RequestId requestId) {
            this.requestId = requestId;
            return this;
        }

    }
}
