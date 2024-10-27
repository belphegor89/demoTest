package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.RandomUtility;
import data.UserEnum.userRoleEnum;
import data.dataobject.CompanyDO;
import data.textstrings.messages.CommonText;
import data.textstrings.messages.CompanyText;
import io.qameta.allure.Step;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.asserts.Assertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CompanyPO extends CommonElementsPO {
    public static Assertion hardAssert = new Assertion();
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors select = new Selectors();
    private final Waits wait = new Waits();
    private final RandomUtility RandomUtil = new RandomUtility();

    public CompanyPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public CompanyPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Go To Companies Section")
    public void goToCompaniesSection() {
        openAdminMenu();
        clickAdxActivityWrap(true);
        select.selector(companiesLink).click();
        wait.waitForClickable(createCompanyButton);
    }

    //<editor-fold desc="Companies List">
    static final By
            createCompanyButton = By.xpath("//div[@class='page-actions']/a"),
            searchCompanyInput = By.xpath("//input[@data-field='name']"),
            companyTable = By.xpath(("//div[@id='companies-table']//table")),
            companyTableRow = By.xpath(("//div[@id='companies-table']//tbody/tr")),
            companyTableCellId = By.xpath("./td[@data-field='id']"),
            companyTableCellStatusToggle = By.xpath("./td[@data-field='status']//input"),
            companyTableCellName = By.xpath("./td[@data-field='name']"),
            companyTableCellDomain = By.xpath("./td[@data-field='domain']"),
            companyTableCellSellerId = By.xpath("./td[@data-field='sellers_id']"),
            companyTableCellCookieSyncToggle = By.xpath("./td[@data-field='cookie_sync']//input"),
            companyTableCellEditButton = By.xpath("./td[@data-field='actions']//a");

    @Step("Click on the Create New Company Button")
    public void clickCreateNewCompany() {
        select.selector(createCompanyButton).click();
        wait.waitForVisibility(companyNameInput);
    }

    @Step("Get company row")
    public WebElement getCompanyRow(String companyName) {
        WebElement companyRow = select.selectParentByTextEquals(companyTableRow, companyTableCellName, companyName);
        hardAssert.assertNotNull(companyRow, "Company [" + companyName + "] is not present in the list");
        return companyRow;
    }

    @Step("Get company ID")
    public Integer getCompanyIdInTable(String companyName) {
        WebElement companyRow = getCompanyRow(companyName);
        return Integer.parseInt(select.selector(companyRow, companyTableCellId).getText());
    }

    @Step("Click on Company status in table")
    public void clickStatusTable(String companyName, boolean toggle) {
        WebElement row = getCompanyRow(companyName);
        clickToggle(row, companyTableCellStatusToggle, toggle);
        wait.waitForVisibility(confirmMessageToast);
        softAssert.assertEquals(select.selector(confirmMessageToast).getText(), CompanyText.CONFIRM_MESSAGE_STATUS.replace("${companyName}", companyName), "Company status confirmation message is incorrect");
        wait.waitForNotVisible(confirmMessageToast);
    }

    @Step("Click on Company Cookie Sync in table")
    public void clickCookieSyncTable(String companyName, boolean toggle) {
        WebElement row = getCompanyRow(companyName);
        clickToggle(row, companyTableCellCookieSyncToggle, toggle);
        wait.waitForVisibility(confirmMessageToast);
        softAssert.assertEquals(select.selector(confirmMessageToast).getText(), CompanyText.CONFIRM_MESSAGE_COOKIE_SYNC.replace("${companyName}", companyName), "Company Cookie Sync confirmation message is incorrect");
        wait.waitForNotVisible(confirmMessageToast);
    }

    @Step("Open company edit page")
    public void clickCompanyEdit(String companyName) {
        WebElement companyRow = getCompanyRow(companyName);
        select.selector(companyRow, companyTableCellEditButton).click();
        wait.waitForVisibility(companyNameInput);
    }

    @Step("Get random company from list")
    public CompanyDO getRandomCompany() {
        List<WebElement> companies = select.multiSelector(companyTableRow);
        List<String> companyNames = new ArrayList<>();
        for (WebElement item : companies) {
            companyNames.add(select.selector(item, companyTableCellName).getText());
        }
        String companyName = (String) RandomUtil.getRandomElement(companyNames);
        int id = getCompanyIdInTable(companyName);
        return new CompanyDO(companyName).setCompanyId(id);
    }

    @Step("Search company")
    public void searchCompany(String searchString) {
        searchItemInTable(searchString, searchCompanyInput, companyTable);
    }

    @Step("Assert company in table")
    public void assertCompanyRow(CompanyDO companyData) {
        WebElement companyRow = getCompanyRow(companyData.getCompanyName());
        String domainExpected = companyData.getDomain().isEmpty() ? "-" : companyData.getDomain(), sellerIdExpected = companyData.getSellerId().isEmpty() ? "-" : companyData.getSellerId();
        softAssert.assertEquals(select.selector(companyRow, companyTableCellId).getText(), companyData.getCompanyId().toString(), "Company ID is incorrect");
        softAssert.assertEquals(select.selector(companyRow, companyTableCellStatusToggle).isSelected(), companyData.getCompanyStatus(), "Company status is incorrect");
        softAssert.assertEquals(select.selector(companyRow, companyTableCellDomain).getText(), domainExpected, "Domain is incorrect");
        softAssert.assertEquals(select.selector(companyRow, companyTableCellSellerId).getText(), sellerIdExpected, "SellerId is incorrect");
        softAssert.assertEquals(select.selector(companyRow, companyTableCellCookieSyncToggle).isSelected(), companyData.getCookieSyncStatus(), "Cookie Sync status is incorrect");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in company [" + companyData.getCompanyName() + "]");
        }
    }

    @Step("Assert company name is in the search results")
    public void assertCompanySearchByName(String companyNamePart) {
        for (WebElement item : select.multiSelector(companyTableRow)) {
            String companyName = select.selector(item, companyTableCellName).getText();
            softAssert.assertTrue(companyName.contains(companyNamePart), "Company [" + companyName + "] should not be in the search results");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in company list");
        }
    }

    @Step("Assert company ID is in the search results")
    public void assertCompanySearchById(Integer companyId) {
        for (WebElement item : select.multiSelector(companyTableRow)) {
            String companyActualId = select.selector(item, companyTableCellId).getText();
            softAssert.assertEquals(companyActualId, companyId.toString(), "Company [" + companyActualId + "] should not be in the search results");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in company list");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Company page">
    static final By
            companyNameInput = By.xpath("//input[@name='name']"),
            companyDomainInput = By.xpath("//input[@name='domain']"),
            usersWithinCompanySelect = By.xpath("//select[@id='users']"),
            companySellerIdInput = By.xpath("//input[@name ='sellers_id']"),
            syncUrlInput = By.xpath("//input[@name ='sync_url']"),
            syncUrlCheckButton = By.xpath("//a[@data-url]"),
            cookieSyncIframeInput = By.xpath("//textarea[@name = 'sync_url_iframe']"),
            cookieStorageToggle = By.xpath("//input[@id='cookie_storage']"),
            cookieSyncToggle = By.xpath("//input[@id='cookie_sync']"),
            endpointCookieSyncUrlSavedLine = By.xpath("//code[@data-name='endpoint_sync']"),
            companyStatusToggle = By.xpath("//input[@id='status']"),
            existUsersCount = By.xpath("//div[@data-name='exist_users']"),
            newUsersCount = By.xpath("//div[@data-name='new_users']");

    @Step("Set Company data")
    public void setCompanyData(CompanyDO company) {
        enterInInput(companyNameInput, company.getCompanyName());
        enterInInput(companyDomainInput, company.getDomain());
        enterInInput(companySellerIdInput, company.getSellerId());
        clickToggle(companyStatusToggle, company.getCompanyStatus());
        if (!company.getUsersMap().isEmpty()) {
            selectUsers(company.getUsersMap());
        }
        if (company.getCookieSyncStatus()) {
            clickOnCookieSyncToggle(company.getCookieSyncStatus());
            inputCookieSyncData(company.getSyncUrl(), company.getIframeTag());
        }
    }

    @Step("Select random users")
    public Map<String, Boolean> selectUsers(userRoleEnum role, int count) {
        String roleName = role != null ? role.publicName() : "";
        Map<String, Boolean> usersMap = getRandomActiveMultiselectOptionsAsMap(usersWithinCompanySelect, roleName, count);
        select.selector(usersWithinCompanySelect, relative_selectLabel).click();
        for (Map.Entry<String, Boolean> userEntry : usersMap.entrySet()) {
            clickDropdownMultiselectOption(usersWithinCompanySelect, userEntry.getKey(), userEntry.getValue());
        }
        closeMultiselectList(usersWithinCompanySelect);
        return usersMap;
    }

    @Step("Select users")
    public void selectUsers(Map<String, Boolean> usersMap) {
        select.selector(usersWithinCompanySelect, relative_selectLabel).click();
        for (Map.Entry<String, Boolean> userEntry : usersMap.entrySet()) {
            clickDropdownMultiselectOption(usersWithinCompanySelect, userEntry.getKey(), userEntry.getValue());
        }
        closeMultiselectList(usersWithinCompanySelect);
    }

    @Step("Click Save&Exit Button")
    public void clickSaveExitButton() {
        select.selector(saveExitButton).click();
        wait.waitForClickable(createCompanyButton);
        wait.waitForVisibility(companyTableRow);
    }

    @Step("Click on Cookie Sync toggle")
    public void clickOnCookieSyncToggle(boolean status) {
        clickToggle(cookieSyncToggle, status);
        if (status) {
            wait.attributeNotContains(syncUrlInput, "class", "hide");
        } else {
            wait.attributeContains(syncUrlInput, "class", "hide");
        }
    }

    @Step("Input Cookie Sync Data")
    public void inputCookieSyncData(String syncUrl, String syncIframeTag) {
        enterInInput(syncUrlInput, syncUrl);
        enterInInput(cookieSyncIframeInput, syncIframeTag);
    }

    @Step("Click on Check Sync button")
    public void clickCheckSyncUrl() {
        select.selector(syncUrlCheckButton).click();
        wait.waitForClickable(modalConfirmButton);
    }

    @Step("Assert Sync URL modal")
    public void assertSyncUrlModal(boolean success) {
        String link1 = "https://smartyads.atlassian.net/wiki/spaces/ESSSPADXKB/pages/133988548/Cookie+sync+with+supply+partners", link2 = "https://smartyads.atlassian.net/wiki/spaces/ESSSPADXKB/pages/134021359/Cookie+sync+with+demand+partners";
        if (success) {
            softAssert.assertEquals(select.selector(modalHeaderText).getText(), CompanyText.SYNC_URL_SUCCESS, "Sync URL success header is incorrect");
        } else {
            softAssert.assertEquals(select.selector(modalHeaderText).getText(), CommonText.MODAL_ERROR_HEADER, "Sync URL error modal header is incorrect");
            softAssert.assertEquals(select.selector(modalContentText).getText(), CompanyText.SYNC_URL_FAIL, "Sync URL error modal text is incorrect");
            softAssert.assertEquals(select.multiSelector(modalContentText, relative_generalLink).get(0).getAttribute("href"), link1, "Sync URL error modal link #1 is incorrect");
            softAssert.assertEquals(select.multiSelector(modalContentText, relative_generalLink).get(1).getAttribute("href"), link2, "Sync URL error modal link #2 is incorrect");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in Sync URL check");
        }
    }

    @Step("Assert Company is created")
    public void assertCompanyEditPage(CompanyDO companyData) {
        String cookieSyncUrlActual = select.selector(endpointCookieSyncUrlSavedLine).getText();
        String usersString = getExpectedMultiselectText(companyData.getUsersMap(), "Select", 10);
        softAssert.assertEquals(select.selector(companyNameInput).getAttribute("value"), companyData.getCompanyName(), "Company name is incorrect");
        softAssert.assertEquals(select.selector(usersWithinCompanySelect, relative_selectLabel).getText(), usersString, "User is incorrect");
        softAssert.assertEquals(select.selector(companyDomainInput).getAttribute("value"), companyData.getDomain(), "Domain is incorrect");
        softAssert.assertEquals(select.selector(companySellerIdInput).getAttribute("value"), companyData.getSellerId(), "SellerId is incorrect");
        softAssert.assertEquals(select.selector(cookieSyncToggle).isSelected(), companyData.getCookieSyncStatus(), "Cookie Sync status is incorrect");
        if (companyData.getCookieSyncStatus()) {
            softAssert.assertEquals(select.selector(cookieStorageToggle).isSelected(), companyData.getCookieSyncStoragePlatform(), "Cookie Storage status is incorrect");
            softAssert.assertTrue(cookieSyncUrlActual.matches(companyData.getCookieSyncUrl()), "Endpoint Cookie Sync URL is incorrect. Cookie Sync URL Actual: [" + cookieSyncUrlActual + "]. Cookie Sync URL expected: [" + companyData.getCookieSyncUrl() + "]");
            softAssert.assertEquals(select.selector(syncUrlInput).getAttribute("value"), companyData.getSyncUrl(), "Sync URL is incorrect");
            softAssert.assertEquals(select.selector(cookieSyncIframeInput).getAttribute("value"), companyData.getIframeTag(), "Cookie sync iFrame tag is incorrect");
            softAssert.assertEquals(select.selector(existUsersCount).getText(), companyData.getUserSyncRequests().toString(), "User sync requests count is incorrect");
            softAssert.assertEquals(select.selector(newUsersCount).getText(), companyData.getUniqueSyncedUserIds().toString(), "Unique synced user ids count is incorrect");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in company [" + companyData.getCompanyName() + "]");
        }
    }

    //</editor-fold>

    @Step("Get company data from DB")
    public CompanyDO getCompanyDataFromDb(String companyName) {
        Condition condition = DSL.field("name").equal(companyName);
        Result<Record> result = dbUtils.getDbDataResult("companies", condition);
        if (result.isNotEmpty()) {
            CompanyDO company = new CompanyDO(companyName);
            Record resultRecord = result.get(0);
            company.setCompanyId(resultRecord.getValue("id", Integer.class));
            company.setCompanyStatus(resultRecord.getValue("status", Boolean.class));
            company.setCookieSyncStatus(resultRecord.getValue("cookie_sync", Boolean.class));
            company.setSyncUrl(resultRecord.getValue("sync_url", String.class));
            company.setCookieSyncUrl(getRunURL() + resultRecord.getValue("endpoint_sync", String.class).replace("/", ""));
            company.setDomain(resultRecord.getValue("domain", String.class));
            company.setCookieSyncStoragePlatform(Objects.equals(resultRecord.getValue("cookie_storage", String.class), "PLATFORM"));
            company.setIframeTag(resultRecord.getValue("sync_url_iframe", String.class));
            company.setSellerId(resultRecord.getValue("sellers_id", String.class));
            return company;
        }
        return null;
    }


}
