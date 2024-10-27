package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import data.CommonEnums.regionsEnum;
import data.ScannersEnums.pixalatePrebidTypesEnum;
import data.ScannersEnums.scannerTypesEnum;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScannersPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    final Selectors Select = new Selectors();
    final Waits Wait = new Waits();

    public ScannersPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
    }

    public ScannersPO(){
        this.softAssert = new SoftAssertCustom();
    }

    @Step("Open Supply & Demand Scanners page")
    public void gotoScanners(){
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(scannersLink).click();
        Wait.waitForClickable(saveSettingsButton);
    }

    //<editor-fold desc="Common">
    private static final By
            saveSettingsButton = By.xpath("//label[@for='ScannerManageForm']"),
            scannerBlock = By.xpath("//li[contains(@class,'collapsible-with-switch')]"),
            scannerHeader = By.xpath(".//div[contains(@class, 'collapsible-header')]"),
            relative_scannerEnableToggle = By.xpath(".//input[contains(@name,'[enable]')][not(@id)]");

    @Step("Get scanner section")
    public WebElement getScannerSection(scannerTypesEnum scanner){
        WebElement elementReturn = Select.selectParentByAttributeContains(scannerBlock, relative_scannerEnableToggle, "name", scanner.attributeName());
        hardAssert.assertNotNull(elementReturn, "There is no such scanner on the page");
        return elementReturn;
    }

    @Step("Toggle scanner")
    public void toggleScanner(scannerTypesEnum scanner, boolean toggle){
        WebElement scannerBlock = getScannerSection(scanner);
        clickToggle(scannerBlock, relative_scannerEnableToggle, toggle);
    }

    @Step("Show/hide scanner")
    public void clickScannerWrap(scannerTypesEnum scanner){
        WebElement scannerBlock = getScannerSection(scanner);
        Select.selector(scannerBlock, scannerHeader).click();
        Wait.sleep(750);    //Wait for animation
    }

    @Step("Save scanners")
    public void clickSaveScanners(){
        WebElement staleElement = Select.selector(saveSettingsButton);
        Select.selector(saveSettingsButton).click();
        Wait.waitForNotVisible(preloader);
        Wait.waitForNotVisible(staleElement);
        Wait.waitForClickable(saveSettingsButton);
    }
    //</editor-fold>

    //<editor-fold desc="Pixalate Prebid">
    private static final By
            pixalatePrebidAccountToggle = By.xpath("//input[@name='pixalate-prebid[is_personal_account]']"),
            pixalatePrebidFtpInput = By.xpath("//input[@name='pixalate-prebid[ftp_host]']"),
            pixalatePrebidLoginInput = By.xpath("//input[@name='pixalate-prebid[ftp_login]']"),
            pixalatePrebidPasswordInput = By.xpath("//input[@name='pixalate-prebid[ftp_password]']"),
            relative_scannerSettingsCheckbox = By.xpath(".//div[@class='row checkbox-percents']//input[@type='checkbox']"),
            relative_scannerSettingsInput = By.xpath(".//div[@class='row checkbox-percents']//input[@type='number']");

    @Step("Input Personal account data")
    public void pixalatePrebidInputPersonal(String ftp, String login, String password){
        enterInInput(pixalatePrebidFtpInput, ftp);
        enterInInput(pixalatePrebidLoginInput, login);
        enterInInput(pixalatePrebidPasswordInput, password);
    }

    @Step("Set up Pixalate Prebid scans")
    public void pixalatePrebidSetupScans(Map<pixalatePrebidTypesEnum, Object> scannerSettings){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.PIXALATE_PREBID), scannerCheckbox, scannerInput;
        List<pixalatePrebidTypesEnum> settingsToOff = new ArrayList<>(List.of(pixalatePrebidTypesEnum.values())), existingSettings = new ArrayList<>(scannerSettings.keySet().stream().toList());
        for (Map.Entry<pixalatePrebidTypesEnum, Object> scannerEntry : scannerSettings.entrySet()){
            scannerCheckbox = Select.selectByAttributeContains(scannerBlock, relative_scannerSettingsCheckbox, "name", scannerEntry.getKey().attributeName());
            clickCheckbox(scannerCheckbox, true);
            switch (scannerEntry.getKey()){
                case DEVICE_ID, BUNDLES, IPV4, IPV6, OTT -> {
                    scannerInput = Select.selectByAttributeContains(scannerBlock, relative_scannerSettingsInput, "name", scannerEntry.getKey().attributeName());
                    if (scannerEntry.getValue() != null){
                        enterInInput(scannerInput, scannerEntry.getValue());
                    }
                }
            }
        }
        settingsToOff.removeAll(existingSettings);
        for (pixalatePrebidTypesEnum setting : settingsToOff){
            scannerCheckbox = Select.selectByAttributeContains(scannerBlock, relative_scannerSettingsCheckbox, "name", setting.attributeName());
            clickCheckbox(scannerCheckbox, false);
        }
    }

    @Step("Assert Pixalate Prebid Scanner")
    public void assertPixalatePrebid(boolean isEnabled, String ftp, String account, String password, Map<pixalatePrebidTypesEnum, Object> scannerSettings){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.PIXALATE_PREBID), scannerCheckbox, scannerInput;
        softAssert.assertEquals(Select.selector(scannerBlock, relative_scannerEnableToggle).isSelected(), isEnabled, "Scanner status is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePrebidFtpInput).getAttribute("value"), ftp, "FTP setting is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePrebidLoginInput).getAttribute("value"), account, "Account setting is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePrebidPasswordInput).getAttribute("value"), password, "Password setting is incorrect");
        List<pixalatePrebidTypesEnum> settingsToOff = new ArrayList<>(List.of(pixalatePrebidTypesEnum.values())), existingSettings = new ArrayList<>(scannerSettings.keySet().stream().toList());
        for (Map.Entry<pixalatePrebidTypesEnum, Object> scannerEntry : scannerSettings.entrySet()){
            scannerCheckbox = Select.selectByAttributeContains(scannerBlock, relative_scannerSettingsCheckbox, "name", scannerEntry.getKey().attributeName());
            softAssert.assertEquals(scannerCheckbox.isSelected(), true, "Setting [" + scannerEntry.getKey().publicName() + "] status is incorrect");
            switch (scannerEntry.getKey()){
                case DEVICE_ID, BUNDLES, IPV4, IPV6, OTT -> {
                    scannerInput = Select.selectByAttributeContains(scannerBlock, relative_scannerSettingsInput, "name", scannerEntry.getKey().attributeName());
                    softAssert.assertEquals(scannerInput.getAttribute("value"), scannerEntry.getValue().toString(), "Setting [" + scannerEntry.getKey().publicName() + "] value is incorrect");
                }
            }
        }
        settingsToOff.removeAll(existingSettings);
        for (pixalatePrebidTypesEnum setting : settingsToOff){
            scannerCheckbox = Select.selectByAttributeContains(scannerBlock, relative_scannerSettingsCheckbox, "name", setting.attributeName());
            softAssert.assertEquals(scannerCheckbox.isSelected(), false, "Setting [" + setting.publicName() + "] status is incorrect");
        }
        softAssert.assertAll("Errors in Pixalate Prebid settings");
    }

    //</editor-fold>

    //<editor-fold desc="Pixalate Postbid">
    private static final By
            pixalatePostbidClidInput = By.xpath("//input[@name='pixalate-postbid[clid]']"),
            pixalatePostbidPaidInput = By.xpath("//input[@name='pixalate-postbid[paid]']"),
            pixalatePostbidDailyCheckbox = By.xpath("//input[@name='pixalate-postbid[scanned_impressions][enable]']"),
            pixalatePostbidDailyInput = By.xpath("//input[@name='pixalate-postbid[scanned_impressions][value]']"),
            pixalatePostbidMonthlyCheckbox = By.xpath("//input[@name='pixalate-postbid[limit_scanned_impressions][enable]']"),
            pixalatePostbidMonthlyInput = By.xpath("//input[@name='pixalate-postbid[limit_scanned_impressions][value]']");

    @Step("Set up Pixalate Postbid")
    public void pixalatePostbidSetup(String clid, String paid, boolean dailyLimitToggle, Integer dailyLimitValue, boolean monthlyLimitToggle, Integer monthlyLimitValue){
        enterInInput(pixalatePostbidClidInput, clid);
        enterInInput(pixalatePostbidPaidInput, paid);
        clickCheckbox(pixalatePostbidDailyCheckbox, dailyLimitToggle);
        if (dailyLimitToggle){
            enterInInput(pixalatePostbidDailyInput, dailyLimitValue);
        }
        clickCheckbox(pixalatePostbidMonthlyCheckbox, monthlyLimitToggle);
        if (monthlyLimitToggle){
            enterInInput(pixalatePostbidMonthlyInput, monthlyLimitValue);
        }
    }

    @Step("Assert Pixalate Postbid Scanner")
    public void assertPixalatePostbid(boolean isEnabled, String clid, String paid, Boolean dailyLimitToggle, Integer dailyLimitValue, Boolean monthlyLimitToggle, Integer monthlyLimitValue){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.PIXALATE_POSTBID);
        softAssert.assertEquals(Select.selector(scannerBlock, relative_scannerEnableToggle).isSelected(), isEnabled, "Scanner status is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePostbidClidInput).getAttribute("value"), clid, "CLID setting is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePostbidPaidInput).getAttribute("value"), paid, "PAID setting is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePostbidDailyCheckbox).isSelected(), dailyLimitToggle.booleanValue(), "Daily limit status is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePostbidDailyInput).getAttribute("value"), dailyLimitValue.toString(), "Daily limit value is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePostbidMonthlyCheckbox).isSelected(), monthlyLimitToggle.booleanValue(), "Monthly limit status is incorrect");
        softAssert.assertEquals(Select.selector(pixalatePostbidMonthlyInput).getAttribute("value"), monthlyLimitValue.toString(), "Monthly limit value is incorrect");
        softAssert.assertAll("Errors in Pixalate Postbid settings");
    }

    //</editor-fold>

    //<editor-fold desc="Protected media">
    private static final By
            protectedPrebidCpidInput = By.xpath("//input[@name='protected-prebid[customer_pid]']"),
            protectedPrebidCkeyInput = By.xpath("//input[@name='protected-prebid[customer_key]']"),
            protectedPrebidHostInput = By.xpath("//input[@name='protected-prebid[host]']"),
            protectedPrebidPortInput = By.xpath("//input[@name='protected-prebid[port]']"),
            protectedPostbidAccountInput = By.xpath("//input[@name='protected-postbid[account_id]']");

    @Step("Set up Protected Media Prebid")
    public void protectedPrebidSetup(String cpid, String ckey, String host, Integer port){
        enterInInput(protectedPrebidCpidInput, cpid);
        enterInInput(protectedPrebidCkeyInput, ckey);
        enterInInput(protectedPrebidHostInput, host);
        enterInInput(protectedPrebidPortInput, port);
    }

    @Step("Assert Protected Media Prebid Scanner")
    public void assertProtectedPrebid(boolean isEnabled, String cpid, String ckey, String host, Integer port){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.PROTECTED_PREBID);
        softAssert.assertEquals(Select.selector(scannerBlock, relative_scannerEnableToggle).isSelected(), isEnabled, "Scanner status is incorrect");
        softAssert.assertEquals(Select.selector(protectedPrebidCpidInput).getAttribute("value"), cpid, "Customer PID value is incorrect");
        softAssert.assertEquals(Select.selector(protectedPrebidCkeyInput).getAttribute("value"), ckey, "Customer Key value is incorrect");
        softAssert.assertEquals(Select.selector(protectedPrebidHostInput).getAttribute("value"), host, "Host value is incorrect");
        softAssert.assertEquals(Select.selector(protectedPrebidPortInput).getAttribute("value"), port.toString(), "Port value is incorrect");
        softAssert.assertAll("Errors in Protected Media Prebid settings");
    }

    @Step("Set up Protected Media Postbid")
    public void protectedPostbidSetup(String accountId){
        enterInInput(protectedPostbidAccountInput, accountId);
    }

    @Step("Assert Protected Media Postbid Scanner")
    public void assertProtectedPostbid(boolean isEnabled, String accountId){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.PROTECTED_POSTBID);
        softAssert.assertEquals(Select.selector(scannerBlock, relative_scannerEnableToggle).isSelected(), isEnabled, "Scanner status is incorrect");
        softAssert.assertEquals(Select.selector(protectedPostbidAccountInput).getAttribute("value"), accountId, "Account ID value is incorrect");
        softAssert.assertAll("Errors in Protected Media Postbid settings");
    }


    //</editor-fold>

    //<editor-fold desc="Whiteops">
    private static final By
            whiteOpsAccountInput = By.xpath("//input[@name='whiteops-fraud-sensor[account_id]']"),
            whiteOpsDailyLimitCheckbox = By.xpath("//input[@name='whiteops-fraud-sensor[scanned_impressions][enable]']"),
            whiteOpsDailyLimitInput = By.xpath("//input[@name='whiteops-fraud-sensor[scanned_impressions][value]']"),
            whiteOpsFraudMonthlyLimitCheckbox = By.xpath("//input[@name='whiteops-fraud-sensor[limit_scanned_impressions][enable]']"),
            whiteOpsFraudMonthlyLimitInput = By.xpath("//input[@name='whiteops-fraud-sensor[limit_scanned_impressions][value]']"),
            whiteOpsDnsInput = By.xpath("//textarea[contains(@name, 'endpoint')]"),
            whiteOpsSslCertInput = By.xpath("//textarea[contains(@name, 'ssl_certificate')]"),
            whiteOpsSslKeyInput = By.xpath("//textarea[contains(@name, 'private_key')]"),
            whiteOpsMediaMonthlyLimitCheckbox = By.xpath("//input[@name='whiteops-media-guard[limit_scanned_requests][enable]']"),
            whiteOpsMediaMonthlyLimitInput = By.xpath("//input[@name='whiteops-media-guard[limit_scanned_requests][value]']");

    @Step("Set up WhiteOps Fraud Sensor")
    public void whiteopsFraudSetup(String accountId, boolean dailyLimitToggle, Integer dailyLimitValue, boolean monthlyLimitToggle, Integer monthlyLimitValue){
        enterInInput(whiteOpsAccountInput, accountId);
        clickCheckbox(whiteOpsDailyLimitCheckbox, dailyLimitToggle);
        if (dailyLimitToggle){
            enterInInput(whiteOpsDailyLimitInput, dailyLimitValue);
        }
        clickCheckbox(whiteOpsFraudMonthlyLimitCheckbox, monthlyLimitToggle);
        if (monthlyLimitToggle){
            enterInInput(whiteOpsFraudMonthlyLimitInput, monthlyLimitValue);
        }
    }

    @Step("Assert WhiteOps Fraud Sensor scanner")
    public void assertWhiteopsFraud(boolean isEnabled, String accountId, Boolean dailyLimitToggle, Integer dailyLimitValue, Boolean monthlyLimitToggle, Integer monthlyLimitValue){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR);
        softAssert.assertEquals(Select.selector(scannerBlock, relative_scannerEnableToggle).isSelected(), isEnabled, "Scanner status is incorrect");
        softAssert.assertEquals(Select.selector(whiteOpsAccountInput).getAttribute("value"), accountId, "Account ID setting is incorrect");
        softAssert.assertEquals(Select.selector(whiteOpsDailyLimitCheckbox).isSelected(), dailyLimitToggle.booleanValue(), "Daily limit status is incorrect");
        softAssert.assertEquals(Select.selector(whiteOpsDailyLimitInput).getAttribute("value"), dailyLimitValue.toString(), "Daily limit value is incorrect");
        softAssert.assertEquals(Select.selector(whiteOpsFraudMonthlyLimitCheckbox).isSelected(), monthlyLimitToggle.booleanValue(), "Monthly limit status is incorrect");
        softAssert.assertEquals(Select.selector(whiteOpsFraudMonthlyLimitInput).getAttribute("value"), monthlyLimitValue.toString(), "Monthly limit value is incorrect");
        softAssert.assertAll("Errors in WhiteOps Fraud Sensor settings");
    }

    @Step("Set up WhiteOps Media Guard")
    public void whiteopsMediaSetup(regionsEnum region, String dns, String sslCert, String sslKey, Boolean monthlyCheckbox, Integer monthlyValue){
        WebElement dnsInput = Select.selectByAttributeContains(whiteOpsDnsInput, "name", region.name()),
                certInput = Select.selectByAttributeContains(whiteOpsSslCertInput, "name", region.name()),
                keyInput = Select.selectByAttributeContains(whiteOpsSslKeyInput, "name", region.name());
        enterInInput(dnsInput, dns);
        enterInInput(certInput, sslCert);
        enterInInput(keyInput, sslKey);
        if (monthlyCheckbox != null){
            clickCheckbox(whiteOpsMediaMonthlyLimitCheckbox, monthlyCheckbox);
            Wait.waitForVisibility(whiteOpsMediaMonthlyLimitInput);
        }
        if (monthlyValue != null){
            enterInInput(whiteOpsMediaMonthlyLimitInput, monthlyValue);
        }
    }

    @Step("Assert WhiteOps Media Guard scanner")
    public void assertWhiteopsMedia(boolean status, regionsEnum region, String dns, String sslCert, String sslKey, Boolean monthlyLimitToggle, Integer monthlyLimitValue){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.WHITEOPS_MEDIA_GUARD),
                dnsInput = Select.selectByAttributeContains(whiteOpsDnsInput, "name", region.name()),
                certInput = Select.selectByAttributeContains(whiteOpsSslCertInput, "name", region.name()),
                keyInput = Select.selectByAttributeContains(whiteOpsSslKeyInput, "name", region.name());
        softAssert.assertEquals(Select.selector(scannerBlock, relative_scannerEnableToggle).isSelected(), status, "Scanner status is incorrect");
        softAssert.assertEquals(dnsInput.getAttribute("value"), dns, "Account ID setting is incorrect");
        softAssert.assertEquals(certInput.getAttribute("value").trim(), sslCert, "Account ID setting is incorrect");
        softAssert.assertEquals(keyInput.getAttribute("value").trim(), sslKey, "Account ID setting is incorrect");
        softAssert.assertEquals(Select.selector(whiteOpsMediaMonthlyLimitCheckbox).isSelected(), monthlyLimitToggle.booleanValue(), "Monthly limit status is incorrect");
        softAssert.assertEquals(Select.selector(whiteOpsMediaMonthlyLimitInput).getAttribute("value"), monthlyLimitValue.toString(), "Monthly limit value is incorrect");
        softAssert.assertAll("Errors in WhiteOps Fraud Sensor settings");
    }

    //</editor-fold>

    private static final By
            geoedgeHeaderInput = By.xpath("//textarea[@id='geoedge_header']"),
            geoedgeFooterInput = By.xpath("//textarea[@id='geoedge_footer']");

    @Step("Set up Geoedge scanner")
    public void geoedgeSetup(String header, String footer){
        enterInInput(geoedgeHeaderInput, header);
        enterInInput(geoedgeFooterInput, footer);
    }

    @Step("Assert Geoedge scanner")
    public void assertGeoedge(boolean status, String header, String footer){
        WebElement scannerBlock = getScannerSection(scannerTypesEnum.GEOEDGE);
        softAssert.assertEquals(Select.selector(scannerBlock, relative_scannerEnableToggle).isSelected(), status, "Scanner status is incorrect");
        softAssert.assertEquals(Select.selector(geoedgeHeaderInput).getAttribute("value"), header, "Header setting is incorrect");
        softAssert.assertEquals(Select.selector(geoedgeFooterInput).getAttribute("value"), footer, "Footer setting is incorrect");
        softAssert.assertAll("Errors in Geoedge settings");
    }


}
