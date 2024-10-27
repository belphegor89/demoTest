package suites;

import common.SoftAssertCustom;
import common.utils.BasicUtility;
import common.utils.RandomUtility;
import data.CommonEnums;
import data.StaticData;
import data.UserEnum;
import data.dataobject.EndpointSupplyDO;
import data.dataobject.UserProfileDO;
import data.textstrings.messages.EmailText;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.*;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static data.StaticData.getEmailAddress;

@Epic("Admin section")
@Feature("Administration")
@Story("Platform settings")
public class PlatformSettings extends BaseSuiteClassNew {

    @Test
    @TmsLink("65039")
    @Severity(SeverityLevel.NORMAL)
    @Description("Adding platform settings")
    public void addPlatformSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO(softAssert);
        PlatformSettingsPO platSet = new PlatformSettingsPO(softAssert);
        DashboardPO dashboard = new DashboardPO(softAssert);
        CashoutPO cashout = new CashoutPO(softAssert);
        SellersJsonPO sellersjson = new SellersJsonPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        UserProfileDO user = new UserProfileDO(UserEnum.userRoleEnum.PUBL, "PlatMail", "Check", getEmailAddress(rnd), "1234567", "Test");
        String siteName = "testSSP" + rnd, type = "siteType" + rnd, sellerContact = "email" + rnd + "@test.com", useLink = "https://test-doc.link/termsof_use/" + rnd, agreementLink = "http://test-doc.link/agreement_link/" + rnd, privacyLink = "https://test-doc.link/privacy_link/" + rnd, serviceLink = "https://test-doc.link/service_link/" + rnd, mail = getEmailAddress(rnd), supportName = "Some Name" + rnd, supportEmail = "someemail" + rnd + "@example.com", managerName = "Manager Name" + rnd, managerEmail = "manager" + rnd + "@exmaple.com", publishersName = "Publisher Name" + rnd, publisherEmail = "publisher" + rnd + "@example.com";
        File sellerJsonFile;
        int cashOutPeriodDays = randomUtil.getRandomInt(30, 150);
        double payPalMinBalance = randomUtil.getRandomDoubleRounded(100, 1000, 2), wtMinBalance = randomUtil.getRandomDoubleRounded(100, 1000, 2);
        login.login(StaticData.supportDefaultUser);
        platSet.gotoPlatformSettingsSection();
        platSet.setupSiteSettings(siteName, type, sellerContact);
        platSet.setupSupportSettings(supportName, supportEmail);
        platSet.setupMangerSettings(managerName, managerEmail);
        platSet.setupPublisherTeamSettings(publishersName, publisherEmail);
        platSet.setupCashOut(cashOutPeriodDays, payPalMinBalance, wtMinBalance);
        platSet.setupLinks(useLink, agreementLink, privacyLink, serviceLink);
        platSet.clickSaveSettings();
        platSet.assertSiteSettings(siteName, type, sellerContact);
        platSet.assertSupportSettings(supportName, supportEmail);
        platSet.assertManagerSettings(managerName, managerEmail);
        platSet.assertPublisherSettings(publishersName, publisherEmail);
        platSet.assertCashOutSettings(cashOutPeriodDays, payPalMinBalance, wtMinBalance);
        platSet.assertLinks(useLink, agreementLink, privacyLink, serviceLink);
        dashboard.gotoDashboard();
        dashboard.assertFooter(siteName, type, useLink, agreementLink, privacyLink, serviceLink);
        sellersjson.gotoSellersJsonSection();
        sellerJsonFile = sellersjson.clickDownloadButton();
        sellersjson.assertSellersJsonFileInfo(sellerJsonFile, sellerContact, new BasicUtility().getCurrentDateTime());
        cashout.gotoCashoutSection();
        cashout.clickCashoutMethod(true);
        cashout.assertCashoutLimitText(true, payPalMinBalance);
        cashout.clickCashoutMethod(false);
        cashout.assertCashoutLimitText(false, wtMinBalance);
        login.logout();
        login.clickCreateAccount();
        login.inputUserFields(user, user.getPassword(), true);
        login.clickRecaptcha();
        login.clickSignUp();
        login.assertEmailAuthor(mail, EmailText.SIGNUP_SUBJECT_CONFIRMATION, supportName, supportEmail);
        softAssert.assertAll("Errors in editing platform settings:");
    }

    @Test
    @TmsLink("29273")
    @Severity(SeverityLevel.NORMAL)
    @Description("Check GDPR Settings")
    public void checkGdprSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        PlatformSettingsPO platSet = new PlatformSettingsPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        Integer rnd = randomUtil.getRandomInt(1, 100);
        boolean gdprOld;
        login.login(StaticData.supportDefaultUser);
        platSet.gotoPlatformSettingsSection();
        gdprOld = platSet.getGdprStatus();
        platSet.setGdprSettings(!gdprOld, rnd.toString());
        platSet.clickSaveSettings();
        platSet.assertGdprSection(!gdprOld, rnd.toString());
        platSet.setGdprSettings(gdprOld, rnd.toString());
        platSet.clickSaveSettings();
        platSet.assertGdprSection(gdprOld, rnd.toString());
        softAssert.assertAll("Error in GDPR section:");
    }

    @Test
    @TmsLink("49978")
    @Severity(SeverityLevel.NORMAL)
    @Description("Adding Custom domain settings")
    public void addCustomDomainSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        PlatformSettingsPO platSet = new PlatformSettingsPO(softAssert);
        InventoryPO inventoryPO = new InventoryPO(softAssert);
        SupplyPO supplyPO = new SupplyPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 100), userId;
        String domain = "somedomain-" + rnd + ".com", inventoryName;
        List<String> inventoryNames;
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        platSet.gotoPlatformSettingsSection();
        platSet.setCustomDomainInput(domain);
        platSet.clickSaveSettings();
        platSet.assertCustomDomainSettings(domain);
        inventoryPO.gotoInventories();
        inventoryNames = inventoryPO.getInventoryName(true, userId);
        inventoryName = (String) randomUtil.getRandomElement(inventoryNames);
        inventoryPO.searchInventoryPlacement(inventoryName);
        File adsTxt = inventoryPO.clickDownloadAdsTxt(inventoryName);
        inventoryPO.assertAdsTxtDomain(adsTxt, domain);
        supplyPO.gotoSupplySection();
        EndpointSupplyDO endpointData;
        endpointData = supplyPO.getRandomEndpointRowInfo();
        adsTxt = supplyPO.clickDownloadAdsTxt(endpointData.getId());
        supplyPO.assertAdsTxtDomain(adsTxt, domain);
        softAssert.assertAll("Errors in adding custom domain settings in platform settings:");
    }

    @Test
    @TmsLink("63031")
    @Severity(SeverityLevel.NORMAL)
    @Description("Adding A/B Testing tool")
    public void addAbTest() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        PlatformSettingsPO platSet = new PlatformSettingsPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int randomAbTestRate = randomUtil.getRandomInt(1, 99);
        List<CommonEnums.abTestEnum> abTestOptionList = new LinkedList<>(Arrays.asList(CommonEnums.abTestEnum.values()));
        CommonEnums.abTestEnum abTestValueOld, abTestValueNew;
        login.login(StaticData.supportDefaultUser);
        platSet.gotoPlatformSettingsSection();
        abTestValueOld = platSet.getAbTestSelected();
        abTestOptionList.remove(abTestValueOld);
        abTestValueNew = (CommonEnums.abTestEnum) randomUtil.getRandomElement(abTestOptionList);
        platSet.selectAbTestFeature(abTestValueNew);
        platSet.clickABTestToggle(true);
        platSet.setupABTestRate(randomAbTestRate);
        platSet.clickSaveSettings();
        platSet.assertAbTestSection(abTestValueNew, true, randomAbTestRate);
        platSet.selectAbTestFeature(abTestValueOld);
        platSet.assertAbTestSection(abTestValueOld, false, null);
        softAssert.assertAll("Errors in AB Test tool:");
    }

    @Test
    @TmsLink("65038")
    @Severity(SeverityLevel.NORMAL)
    @Description("Check validation on the Platform Settings page")
    public void checkValidation() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        PlatformSettingsPO platSet = new PlatformSettingsPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int cashOutWrongMinDays = randomUtil.getRandomInt(1, 29), cashOutWrongMaxDays = randomUtil.getRandomInt(181, 1000);
        String randomCompanyName = randomUtil.getRandomString(45), randomCompanyType = randomUtil.getRandomString(15), email = "email1513@test.com", sellerWrongEmail = "someemail.com", supportWrongEmail = "supporcom", managerWrongEmail = "manager", publisherTeamWrongEmail = "pubtm.me", url = "test.com";
        login.login(StaticData.supportDefaultUser);
        platSet.gotoPlatformSettingsSection();
        platSet.setupSiteSettings("", "", "");
        platSet.setupSupportSettings("", "");
        platSet.clickSaveWithErrors();
        platSet.assertSiteSettingsRequiredFields();
        platSet.assertSupportSettingsRequiredFields();
        platSet.setupSupportSettings("suname", "suemail@example.com");
        platSet.setupSiteSettings(randomCompanyName, "", email);
        platSet.assertCompanyNameLength(randomCompanyName);
        platSet.setupSiteSettings(randomCompanyName, randomCompanyType, email);
        platSet.assertCompanyTypeLength(randomCompanyType);
        platSet.setupSiteSettings("name", "type", "email@example.com");
        platSet.setupSupportSettings("suname", "suemail@example.com");
        platSet.setupSiteSettings("some_name_1", "some_type_1", sellerWrongEmail);
        platSet.setupSupportSettings("some_name_2", supportWrongEmail);
        platSet.setupMangerSettings("some_name_3", managerWrongEmail);
        platSet.setupPublisherTeamSettings("some_name_4", publisherTeamWrongEmail);
        platSet.clickSaveWithErrors();
        platSet.assertEmailsHasValidations(sellerWrongEmail, supportWrongEmail, managerWrongEmail, publisherTeamWrongEmail);
        platSet.setupSiteSettings("some_name_1", "some_type_1", "someemail1@exmaple.com");
        platSet.setupSupportSettings("some_name_2", "someemail2@example.com");
        platSet.setupMangerSettings("some_name_3", "someemail3@example.com");
        platSet.setupPublisherTeamSettings("some_name_4", "someemail4@example.com");
        platSet.setupCashOut(null, null, null);
        platSet.clickSaveWithErrors();
        platSet.assertCashoutValidation();
        platSet.setupCashOut(cashOutWrongMinDays, 2.0, 2.0);
        platSet.clickSaveWithErrors();
        platSet.assertCashoutPeriodValidation(cashOutWrongMinDays);
        platSet.setupCashOut(cashOutWrongMaxDays, 2.0, 2.0);
        platSet.clickSaveWithErrors();
        platSet.assertCashoutPeriodValidation(cashOutWrongMaxDays);
        platSet.setupCashOut(35, 0.0, 0.0);
        platSet.clickSaveWithErrors();
        platSet.assertCashoutPaymentsValidation();
        platSet.setupCashOut(35, 1.0, 1.0);
        platSet.setCustomDomainInput("test.123231");
        platSet.clickSaveWithErrors();
        platSet.assertCustomDomainValidation();
        platSet.setCustomDomainInput("test.com");
        platSet.setupLinks(url, url, url, url);
        platSet.clickSaveWithErrors();
        platSet.assertLinksValidation();
        softAssert.assertAll("Errors in validating platform settings:");
    }
}
