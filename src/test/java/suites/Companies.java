package suites;

import common.SoftAssertCustom;
import common.utils.RandomUtility;
import data.CommonEnums.pagingActionsTypes;
import data.StaticData;
import data.UserEnum.userRoleEnum;
import data.dataobject.CompanyDO;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.CompanyPO;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("Companies")
public class Companies extends BaseSuiteClassNew {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("21147")
    @Description("Create company without cookie sync")
    public void createCompanyWithoutCookieSync() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        CompanyPO company = new CompanyPO(softAssert);
        int rnd = rndUtil.getRandomInt(1, 10000), usersRnd = rndUtil.getRandomInt(1, 2);
        CompanyDO companyData = new CompanyDO("CompanyNoCookieSync" + rnd, rnd + "test.com", "sellerId" + rnd);
        login.login(StaticData.supportDefaultUser);
        company.goToCompaniesSection();
        company.clickCreateNewCompany();
        company.setCompanyData(companyData);
        companyData.setUsersMap(company.selectUsers(userRoleEnum.PUBL_SSP, usersRnd));
        company.clickSaveExitButton();
        company.pagingActionSelect(pagingActionsTypes.LAST, null);
        companyData.setCompanyId(company.getCompanyIdInTable(companyData.getCompanyName()));
        company.assertCompanyRow(companyData);
        company.clickCompanyEdit(companyData.getCompanyName());
        company.assertCompanyEditPage(companyData);
        softAssert.assertAll("Errors in Company creation without cookie sync:");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("1961")
    @Description("Create company with cookie sync")
    public void createCompanyWithCookieSync() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        CompanyPO company = new CompanyPO(softAssert);
        int rnd = rndUtil.getRandomInt(1, 10000), usersRnd = rndUtil.getRandomInt(1, 3);
        CompanyDO companyData = new CompanyDO("CompanyWithCookieSync" + rnd, rnd + "test.com", "sellerId" + rnd).setCompanyStatus(true).setCookieSyncStatus(true).setSyncUrl("https://x.bidswitch.net/sync?buid=[25565]").setIframeTag("<iframe src=\"https://public.servenobid.com/sync.html?gdpr=[GDPR]=&usp_consent=[CCPA]&redirect=https://sa-cs.deliverimp.com/d5671bf7d3482acb2da68206c2d4da28.gif?puid=$UID\" async=\"async\" style=\"display: none;\"></iframe>");
        login.login(StaticData.supportDefaultUser);
        company.goToCompaniesSection();
        company.clickCreateNewCompany();
        company.setCompanyData(companyData);
        companyData.setUsersMap(company.selectUsers(userRoleEnum.PUBL_DSP, usersRnd));
        company.clickSaveExitButton();
        company.pagingActionSelect(pagingActionsTypes.LAST, null);
        companyData.setCompanyId(company.getCompanyIdInTable(companyData.getCompanyName()));
        company.assertCompanyRow(companyData);
        company.clickCompanyEdit(companyData.getCompanyName());
        company.assertCompanyEditPage(companyData);
        softAssert.assertAll("Errors in Company creation without cookie sync:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("51091")
    @Description("User can check Sync URL")
    public void syncUrlValidation() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        CompanyPO company = new CompanyPO(softAssert);
        login.login(StaticData.supportDefaultUser);
        company.goToCompaniesSection();
        company.clickCreateNewCompany();
        company.clickOnCookieSyncToggle(true);
        company.inputCookieSyncData("https://image8.pubmatic", "");
        company.clickCheckSyncUrl();
        company.assertSyncUrlModal(false);
        company.modalClickConfirm();
        company.inputCookieSyncData("https://image8.pubmatic.com/AdServer/ImgSync?p=156451&gdpr=[GDPR]&gdpr_consent=[GDPR_CONSENT]", "");
        company.clickCheckSyncUrl();
        company.assertSyncUrlModal(true);
        softAssert.assertAll("Errors in Sync URL check:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1117")
    @Description("Search company by name")
    public void searchCompany() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        CompanyPO company = new CompanyPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int nameStart, nameStop;
        String companyNameFull, companyNamePart;
        login.login(StaticData.supportDefaultUser);
        company.goToCompaniesSection();
        CompanyDO companyData = company.getRandomCompany();
        companyNameFull = companyData.getCompanyName();
        nameStart = randomUtil.getRandomInt(0, companyNameFull.length() / 2);
        nameStop = randomUtil.getRandomInt(companyNameFull.length() / 2 + 1, companyNameFull.length());
        companyNamePart = companyNameFull.substring(nameStart, nameStop);
        company.searchCompany(companyNamePart);
        company.assertCompanySearchByName(companyNamePart);
        company.searchCompany(companyData.getCompanyId().toString());
        company.assertCompanySearchById(companyData.getCompanyId());
        softAssert.assertAll("Errors in Company search results:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("51090")
    @Description("Edit company from table")
    public void editCompanyTable() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        CompanyPO company = new CompanyPO(softAssert);
        int rnd = new RandomUtility().getRandomInt(1, 10000);
        String syncUrl = "https://x.bidswitch.net/sync?buid=[25565]", iFrame = "<iframe src=\"https://public.servenobid.com/sync.html?gdpr=[GDPR]=&usp_consent=[CCPA]&redirect=https://sa-cs.deliverimp.com/d5671bf7d3482acb2da68206c2d4da28.gif?puid=$UID\" async=\"async\" style=\"display: none;\"></iframe>";
        CompanyDO companyData = new CompanyDO("CompanyEditTable" + rnd, rnd + "test.com", "sellerId" + rnd).setCompanyStatus(true).setCookieSyncStatus(true).setSyncUrl(syncUrl).setIframeTag(iFrame);
        login.login(StaticData.supportDefaultUser);
        company.goToCompaniesSection();
        company.clickCreateNewCompany();
        company.setCompanyData(companyData);
        company.clickSaveExitButton();
        company.pagingActionSelect(pagingActionsTypes.LAST, null);
        companyData.setCompanyId(company.getCompanyIdInTable(companyData.getCompanyName())).setCompanyStatus(false).setCookieSyncStatus(false).setSyncUrl("").setIframeTag("");
        company.clickStatusTable(companyData.getCompanyName(), false);
        company.clickCookieSyncTable(companyData.getCompanyName(), false);
        company.clickCompanyEdit(companyData.getCompanyName());
        company.assertCompanyEditPage(companyData);
        softAssert.assertAll("Errors after setting toggles OFF in table:");
        company.clickBreadcrumbItem(1);
        company.pagingActionSelect(pagingActionsTypes.LAST, null);
        company.clickStatusTable(companyData.getCompanyName(), true);
        company.clickCookieSyncTable(companyData.getCompanyName(), true);
        companyData.setCompanyStatus(true).setCookieSyncStatus(true).setSyncUrl(syncUrl).setIframeTag(iFrame);
        company.clickCompanyEdit(companyData.getCompanyName());
        company.assertCompanyEditPage(companyData);
        softAssert.assertAll("Errors after setting toggles ON in table:");
    }

}

