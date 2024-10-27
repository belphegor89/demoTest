package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.*;
import data.InventoryEnum;
import data.dataobject.EmailDO;
import data.textstrings.messages.EmailText;
import io.qameta.allure.Step;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryManagePO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final BasicUtility basicUtilityTwo = new BasicUtility();
    private final EmailUtility EmailUtil = new EmailUtility();
    private final FileUtility FileUtil = new FileUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final ParseAndFormatUtility ParseUtil = new ParseAndFormatUtility();

    public InventoryManagePO(SoftAssertCustom softAssert) {
        this.softAssert = softAssert;
        this.isSoftAssertLocal = false;
    }

    public InventoryManagePO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    //<editor-fold desc="Inventory Manage list">
    static final By
            inventoryManageRow = By.xpath("//div[@class='table-data']//tbody/tr"),
            inventoryListTable = By.xpath("//div[@id='infoInventory']//table"),
            approveInventoryButton = By.xpath("//div[@class='right']/a[contains(@class, 'approve-selected')]"),
            declineInventoryButton = By.xpath("//div[@class='right']/a[contains(@class, 'declined-selected')]"),
            inventoryManageCheckbox = By.xpath("./td[@data-field='checkbox_type']/input"),
            inventoryManageNameID = By.xpath("./td[@data-field='id']"),
            inventoryManageType = By.xpath("./td[@data-field='platform']"),
            inventoryManageUserName = By.xpath("/td[@data-field='user_name']"),
            inventoryManageDomain = By.xpath("./td[@data-field='address']"),
            inventoryManageTitle = By.xpath("./td[@data-field='title']"),
            inventoryManageStatus = By.xpath(".//td[@data-field='status']"),
            inventoryManageActions = By.xpath(".//td[@data-field='actions']"),
            inventoryApproveActionButton = By.xpath(".//a[@data-tooltip-id='approve_inventory']"),
            inventoryDeclineActionButton = By.xpath(".//a[@data-tooltip-id='decline_inventory']"),
            inventoryEditActionButton = By.xpath(".//a[@data-tooltip-id='edit_inventory']");

    @Step("Open Manage Inventory dashboard page")
    public void gotoManageInventorySection() {
        openAdminMenu();
        clickPubManagementWrap(true);
        Select.selector(manageInventoryLink).click();
        Wait.attributeNotContains(inventoryListTable, "class", "hide");
    }

    @Step("Find the inventory row by Name")
    public WebElement getInventoryRow(String inventoryName) {
        WebElement inventory = Select.selectParentByTextEquals(inventoryManageRow, inventoryManageTitle, inventoryName);
        hardAssert.assertNotNull(inventory, "There is no inventory with the Name <" + inventoryName + ">");
        return inventory;
    }

    @Step("Get Inventory Name")
    public List<String> getInventoryName(boolean inventoryStatus, Integer userId) {
        List<String> returnList = new ArrayList<>();
        Condition condition = DSL.field("user_id").equal(userId).and(DSL.field("status").equal(inventoryStatus ? 0 : -1));
        for (Record record : dbUtils.getDbDataResult("inventory", condition)) {
            returnList.add(record.get("title", String.class));
        }
        return returnList;
    }

    @Step("Click on Inventory checkbox")
    public void clickInventoryManageCheckbox(String inventoryName) {
        clickCheckbox(Select.selector(getInventoryRow(inventoryName), inventoryManageCheckbox), true);
    }

    @Step("Click on Multiple Approve button")
    public void clickApproveMultipleInventoryButton() {
        Select.selector(approveInventoryButton).click();
        Wait.waitForVisibility(modalDialog);
        Wait.waitForNotVisible(modalDialog);
        //TODO This waits are unstable
        Wait.sleep(1000);
        //        Wait.attributeContains(inventoryListTable, "class", "disabled");
        Wait.attributeNotContains(inventoryListTable, "class", "disabled");
    }

    @Step("Click on Single Approve button")
    public void clickApproveSingleInventoryButton(String inventoryName) {
        Select.selector(getInventoryRow(inventoryName), inventoryApproveActionButton).click();
        Wait.waitForVisibility(modalDialog);
        Wait.waitForNotVisible(modalDialog);
        //TODO This waits are unstable
        Wait.sleep(1000);
        //        Wait.attributeContains(inventoryListTable, "class", "disabled");
        Wait.attributeNotContains(inventoryListTable, "class", "disabled");
    }

    @Step("Assert Approved Inventory Status in the list")
    public void assertApprovedInventoryStatus(String inventoryName, InventoryEnum.manageInventoryStatusEnum status) {
        WebElement inventoryItem = getInventoryRow(inventoryName);
        softAssert.assertEquals(Select.selector(inventoryItem, inventoryManageStatus).getText(), status.publicName(), "Inventory status is incorrect");
        softAssert.assertTrue(AssertUtil.assertNotPresent(inventoryItem, inventoryApproveActionButton), "Approve action button is not displayed");
    }
    //</editor-fold>

    //<editor-fold desc="Manage Inventory page - Filters">
    static final By
            openFilterButton = By.xpath(".//a[@data-activates='inventory-filter-options']"),
            perPageFilterSelect = By.xpath("//select[@id='per-page']"),
            inventoryStatusSelect = By.xpath("//select[@name='status']"),
            searchByInventoryFilterInput = By.xpath("//input[@id='search']"),
            filtersSidebarBlock = By.xpath("//ul[@id='inventory-filter-options']");

    @Step("Click on Filter Button")
    public void clickFilterButton() {
        Select.selector(openFilterButton).click();
        Wait.waitForVisibility(searchByInventoryFilterInput);
    }

    @Step("Select how many Inventories are displayed per page")
    public void selectQuantityPerPage(InventoryEnum.manageInventoryPerPageEnum perPage) {
        selectSingleSelectDropdownOptionByName(perPageFilterSelect, perPage.publicName());
    }

    @Step("Select Inventories Pending Status")
    public void selectInventoryStatus(Map<InventoryEnum.manageInventoryStatusEnum, Boolean> inventoryStatusMap) {
        Select.selector(inventoryStatusSelect, relative_selectLabel).click();
        if (inventoryStatusMap != null && !inventoryStatusMap.isEmpty()) {
            for (Map.Entry<InventoryEnum.manageInventoryStatusEnum, Boolean> adFormatEntry : inventoryStatusMap.entrySet()) {
                clickDropdownMultiselectOption(inventoryStatusSelect, adFormatEntry.getKey().publicName(), adFormatEntry.getValue());
            }
        } else {
            selectSingleSelectDropdownOptionByName(inventoryStatusSelect, "All");
        }
        closeMultiselectList(inventoryStatusSelect);
    }

    @Step("Close Filters options")
    public void clickCloseFilters() {
        int adjust = Select.selector(filtersSidebarBlock).getSize().getWidth();
        Select.actionClickWithAdjust(openFilterButton, adjust + 10, 0);
        Wait.attributeContains(inventoryListTable, "class", "disabled");
        Wait.attributeNotContains(inventoryListTable, "class", "disabled");
    }

    @Step("Open the mail box and find the email titled \"Notification: Inventory Approved\" for multiple inventories")
    public void assertMultipleNotificationEmail(String inventoryName1, String inventoryName2, String firstName, String lastName) {
        LocalDateTime currentDateTime = basicUtilityTwo.getCurrentDateTime();
        String projectName = getPlatformNameFooter(), projectType = getPlatformTypeFooter(), currentYear = String.valueOf(currentDateTime.getYear()), projectEmail = FileUtil.getProjectEmail(), inventoryApproveTextReplaced = ParseUtil.formatHtmlUnescapeCharacters(EmailText.MULTIPLE_INVENTORIES_APPROVE_PLAIN)
                .replace("${InventoryName1}", inventoryName1)
                .replace("${InventoryName2}", inventoryName2)
                .replace("${currentYear}", currentYear)
                .replace("${nameProject}", projectName)
                .replace("${typeProject}", projectType)
                .replace("${emailProject}", projectEmail)
                .replace("${firstName}", firstName)
                .replace("${lastName}", lastName);
        EmailDO email = EmailUtil.getEmailByTitle("Inventory Approved");
        softAssert.assertEquals(email.getTitle(), "Notification: Inventory Approved - " + projectName, "Email Title is not correct");
        softAssert.assertEquals(email.getBodyPlain(), inventoryApproveTextReplaced, "Email Body is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in Inventory Approve Email");
        }
    }

    @Step("Open the mail box and find the email titled \"Notification: Inventory Approved\" for single inventory")
    public void assertNotificationSingleApproveEmail(String inventoryName, String firstName, String lastName) {
        LocalDateTime currentDateTime = basicUtilityTwo.getCurrentDateTime();
        String projectName = getPlatformNameFooter(), projectType = getPlatformTypeFooter(), currentYear = String.valueOf(currentDateTime.getYear()), projectEmail = FileUtil.getProjectEmail();
        String inventoryApproveTextReplaced = ParseUtil.formatHtmlUnescapeCharacters(EmailText.SINGLE_INVENTORY_APPROVE_PLAIN)
                .replace("${InventoryName}", inventoryName)
                .replace("${nameProject}", projectName)
                .replace("${currentYear}", currentYear)
                .replace("${typeProject}", projectType)
                .replace("${emailProject}", projectEmail)
                .replace("${firstName}", firstName)
                .replace("${lastName}", lastName);
        EmailDO email = EmailUtil.getEmailByTitle("Inventory Approved");
        softAssert.assertEquals(email.getTitle(), "Notification: Inventory Approved - " + projectName, "Email Title is not correct");
        softAssert.assertEquals(email.getBodyPlain(), inventoryApproveTextReplaced, "Email Body is not correct");

        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in Inventory Approve Email");
        }
    }
    //</editor-fold>
}

