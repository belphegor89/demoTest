package suites;

import common.SoftAssertCustom;
import data.InventoryEnum;
import data.StaticData;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.InventoryManagePO;
import pages.InventoryPO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Epic("Admin section")
@Feature("Manage Publishers")
@Story("Manage Inventories")
public class InventoryManage extends BaseSuiteClassNew {

    //<editor-fold desc=" Manage Inventories">
    @Test
    @TmsLink("37956")
    @Description("Approve multiple inventories")
    public void approveMultipleInventories() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(softAssert);
        InventoryManagePO inventoryManage = new InventoryManagePO(softAssert);
        Map<InventoryEnum.manageInventoryStatusEnum, Boolean> approvedInventoryStatusMap = new HashMap<>() {{
            put(InventoryEnum.manageInventoryStatusEnum.PENDING_APPROVAL, false);
            put(InventoryEnum.manageInventoryStatusEnum.APPROVED, true);
        }};
        Map<InventoryEnum.manageInventoryStatusEnum, Boolean> pendingInventoryStatusMap = new HashMap<>() {{
            put(InventoryEnum.manageInventoryStatusEnum.PENDING_APPROVAL, true);
            put(InventoryEnum.manageInventoryStatusEnum.APPROVED, false);
        }};
        int userId;
        String inventoryName1, inventoryName2;
        List<String> inventoryNames;
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        inventoryManage.gotoManageInventorySection();
        inventoryManage.clickFilterButton();
        inventoryManage.selectQuantityPerPage(InventoryEnum.manageInventoryPerPageEnum.ONE_HUNDRED);
        inventoryManage.selectInventoryStatus(pendingInventoryStatusMap);
        inventoryManage.clickCloseFilters();
        inventoryNames = inventoryManage.getInventoryName(true, userId);
        inventoryName1 = inventoryNames.get(inventoryNames.size() - 2);
        inventoryName2 = inventoryNames.get(inventoryNames.size() - 1);
        inventoryManage.clickInventoryManageCheckbox(inventoryName1);
        inventoryManage.clickInventoryManageCheckbox(inventoryName2);
        inventoryManage.clickApproveMultipleInventoryButton();
        inventoryManage.clickFilterButton();
        inventoryManage.selectInventoryStatus(approvedInventoryStatusMap);
        inventoryManage.clickCloseFilters();
        inventoryManage.assertApprovedInventoryStatus(inventoryName1, InventoryEnum.manageInventoryStatusEnum.APPROVED);
        inventoryManage.assertApprovedInventoryStatus(inventoryName2, InventoryEnum.manageInventoryStatusEnum.APPROVED);
        inventory.gotoInventories();
        inventory.getInventoryId(inventoryName1, userId);
        inventory.searchInventoryPlacement(inventoryName1);
        inventory.assertInventoryStatusInfo(inventoryName1, InventoryEnum.inventoryStatusEnum.ACTIVE);
        inventory.searchInventoryPlacement(inventoryName2);
        inventory.getInventoryId(inventoryName2, userId);
        inventory.assertInventoryStatusInfo(inventoryName2, InventoryEnum.inventoryStatusEnum.ACTIVE);
        inventoryManage.assertMultipleNotificationEmail(inventoryName1, inventoryName2, StaticData.supportDefaultUser.getFirstName(), StaticData.supportDefaultUser.getLastName());
        softAssert.assertAll("Errors in inventory Management");
    }

    @Test
    @TmsLink("42528")
    @Description("Approve Single inventory")
    public void approveSingleInventory() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(softAssert);
        InventoryManagePO inventoryManage = new InventoryManagePO(softAssert);
        Map<InventoryEnum.manageInventoryStatusEnum, Boolean> approvedInventoryStatusMap = new HashMap<>() {{
            put(InventoryEnum.manageInventoryStatusEnum.PENDING_APPROVAL, false);
            put(InventoryEnum.manageInventoryStatusEnum.APPROVED, true);
        }};
        Map<InventoryEnum.manageInventoryStatusEnum, Boolean> pendingInventoryStatusMap = new HashMap<>() {{
            put(InventoryEnum.manageInventoryStatusEnum.PENDING_APPROVAL, true);
            put(InventoryEnum.manageInventoryStatusEnum.APPROVED, false);
        }};
        int userId;
        String inventoryName;
        List<String> inventoryNames;
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        inventoryManage.gotoManageInventorySection();
        inventoryManage.clickFilterButton();
        inventoryManage.selectQuantityPerPage(InventoryEnum.manageInventoryPerPageEnum.ONE_HUNDRED);
        inventoryManage.selectInventoryStatus(pendingInventoryStatusMap);
        inventoryManage.clickCloseFilters();
        inventoryNames = inventoryManage.getInventoryName(true, 1);
        inventoryName = inventoryNames.get(inventoryNames.size() - 1);
        inventoryManage.clickApproveSingleInventoryButton(inventoryName);
        inventoryManage.clickFilterButton();
        inventoryManage.selectInventoryStatus(approvedInventoryStatusMap);
        inventoryManage.clickCloseFilters();
        inventoryManage.assertApprovedInventoryStatus(inventoryName, InventoryEnum.manageInventoryStatusEnum.APPROVED);
        inventory.gotoInventories();
        inventory.getInventoryId(inventoryName, userId);
        inventory.searchInventoryPlacement(inventoryName);
        inventory.assertInventoryStatusInfo(inventoryName, InventoryEnum.inventoryStatusEnum.ACTIVE);
        inventoryManage.assertNotificationSingleApproveEmail(inventoryName, StaticData.supportDefaultUser.getFirstName(), StaticData.supportDefaultUser.getLastName());
        softAssert.assertAll("Errors in inventory Management");
    }

    //</editor-fold>
}
