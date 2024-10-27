package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.UserSettings;
import common.utils.AssertUtility;
import common.utils.FileUtility;
import common.utils.ParseAndFormatUtility;
import common.utils.RandomUtility;
import common.Waits;
import data.CommonEnums;
import data.dataobject.InventoryDO;
import data.dataobject.PlacementDO;
import data.InventoryEnum;
import data.InventoryEnum.adCategoryEnum;
import data.InventoryEnum.inventoryTypeEnum;
import data.textstrings.messages.InventoryText;
import com.fasterxml.jackson.databind.MappingIterator;
import io.qameta.allure.Step;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import static data.CommonEnums.iabSizesEnum;
import static data.PlacementEnum.*;

public class InventoryPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final FileUtility FileUtils = new FileUtility();
    private final RandomUtility RandomUtils = new RandomUtility();
    private final ParseAndFormatUtility FormatUtil = new ParseAndFormatUtility();

    public InventoryPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public InventoryPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Inventories page")
    public void gotoInventories() {
        Select.selector(inventorySectionButton).click();
        Wait.waitForClickable(createInventoryButton);
        Wait.attributeContains(preloaderHeader, "class", "hide");
    }

    //<editor-fold desc="Common steps and methods">
    @Step("Select values in two-column filter")
    private void selectBlockListParameters(By leftColumn, By rightColumn, List<String> valuesInRight) {
        List<String> valuesSelectedInRightColumn = new ArrayList<>();
        if (AssertUtil.assertPresent(rightColumn)) { // check if there are any selected categories
            for (WebElement itemSelected : Select.multiSelector(rightColumn)) {
                valuesSelectedInRightColumn.add(itemSelected.getAttribute("title"));
            }
            for (String itemName : valuesSelectedInRightColumn) { // deselect if valuesSelectedInRightColumn has values that are not in catToSelect
                if (!valuesInRight.contains(itemName)) {
                    Select.selectByAttributeExact(rightColumn, "title", itemName).click();
                }
            }
        }
        for (String itemToSelect : valuesInRight) { // select categories from catToSelect
            if (!valuesSelectedInRightColumn.contains(itemToSelect)) {
                Select.selectByAttributeExact(leftColumn, "title", itemToSelect).click();
            }
        }
    }

    @Step("Get random appstore id from the file")
    public String getRandomAppIdFromFile() {
        List<String> recordList = new ArrayList<>();
        MappingIterator<Map<String, Object>> parsedCsv = FileUtils.readCsvFile(new File(UserSettings.resourcesFolder + "/appIds.csv"));
        while (parsedCsv.hasNext()) {
            for (Object value : parsedCsv.next().values()) {
                recordList.add(value.toString());
            }
        }
        return (String) RandomUtils.getRandomElement(recordList);
    }
    //</editor-fold>

    //<editor-fold desc="Inventories dashboard">

    //<editor-fold desc="Filters and Markup">
    static final By
            filterSearchInput = By.xpath("//input[@name='searchInventory']"),
            inventoryListEmptyPlaceholder = By.xpath("//div[@id='contentLoadedInventories']//div[contains(@class, 'center-align')]/h2"),
            createInventoryButton = By.xpath("//a[contains(@href, 'inventory/create')]");

    @Step("Get Inventory Name")
    public List<String> getInventoryName(Boolean inventoryStatus, Integer userId) {
        Condition condition = DSL.field("status").in(inventoryStatus != null ? (inventoryStatus ? 1 : 0) : 0, 1).and(DSL.field("user_id").equal(userId));
        Result<Record> results = dbUtils.getDbDataResult("inventory", condition);
        return results.getValues("title", String.class);
    }

    @Step("Get inventory Domain")
    public String getInventoryDomain(String inventoryName) {
        Condition condition = DSL.field("title").equal(inventoryName);
        org.jooq.Record data = dbUtils.getDbDataRecordFirst("inventory", condition);
        hardAssert.assertNotNull(data, "Inventory <" + inventoryName + "> not found");
        return data.get("address", String.class);
    }

    @Step("Get Placement Name")
    public List<String> getPlacementName(Boolean placementStatus) {
        Condition condition = DSL.field("status").in(placementStatus != null ? (placementStatus ? 1 : 0) : 0, 1);
        Result<Record> results = dbUtils.getDbDataResult("placement", condition);
        return results.getValues("title", String.class);
    }

    @Step("Input symbols in Inventory/Placement search field")
    public void searchInventoryPlacement(String searchRequest) {
        enterInInput(filterSearchInput, searchRequest);
        Select.actionPressEnter(filterSearchInput);
        Wait.attributeContains(preloaderHeader, "class", "hide");
    }

    @Step("Assert Inventory and Placement name search")
    public void assertSearchResult(String nameToContain) {
        if (nameToContain != null) {
            for (WebElement inventoryItem : Select.multiSelector(inventoryRow)) {
                String inventoryNameString = Select.selector(inventoryItem, relative_inventoryNameId).getText().substring(0, inventoryItem.getText().lastIndexOf("#")).trim();
                boolean isInventory = inventoryNameString.toLowerCase().contains(nameToContain);
                if (AssertUtil.assertPresent(inventoryItem, relative_placementsRollupButton)) {
                    clickPlacementWrap(inventoryNameString, true);
                    List<WebElement> placementRowList = Select.multiSelector(placementRow, placementNameString);
                    hardAssert.assertFalse(placementRowList.isEmpty(), "No search results");
                    for (WebElement placementRowName : placementRowList) {
                        boolean isPlacement = placementRowName.getText().toLowerCase().contains(nameToContain);
                        softAssert.assertTrue(isInventory || isPlacement, "Inventory name <" + inventoryNameString + "> and Placement name <" + placementRowName.getText() + "> do not contain the symbols <" + nameToContain + ">");
                    }
                    clickPlacementWrap(inventoryNameString, false);
                }
            }
        } else {
            softAssert.assertEquals(Select.selector(inventoryListEmptyPlaceholder).getText(), InventoryText.EMPTY_PAGE_SEARCH, "Empty search text is incorrect");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in search results:");
        }
    }

    @Step("Select filter by Inventory type")
    public void filterSelectInventoriesType(Map<inventoryTypeEnum, Boolean> typesMap) {
        Select.selector(inventoriesTypeSelect, relative_selectLabel).click();
        if (typesMap != null && !typesMap.isEmpty()) {
            for (Entry<inventoryTypeEnum, Boolean> typeEntry : typesMap.entrySet()) {
                clickDropdownMultiselectOption(inventoriesTypeSelect, typeEntry.getKey().publicName(), typeEntry.getValue());
            }
        } else {
            selectSingleSelectDropdownOptionByName(inventoriesTypeSelect, "All");
        }
        Select.selector(filterSearchInput).click();
        Wait.attributeContains(preloaderHeader, "class", "hide");
    }

    @Step("Select filter by Placement type")
    public void filterSelectPlacementType(Map<placementTypeEnum, Boolean> typesMap) {
        Select.selector(placementsTypeSelect, relative_selectLabel).click();
        if (typesMap != null && !typesMap.isEmpty()) {
            for (Entry<placementTypeEnum, Boolean> typeEntry : typesMap.entrySet()) {
                clickDropdownMultiselectOption(placementsTypeSelect, typeEntry.getKey().publicName(), typeEntry.getValue());
            }
        } else {
            selectSingleSelectDropdownOptionByName(placementsTypeSelect, "All");
        }
        Select.selector(filterSearchInput).click();
        Wait.attributeContains(preloaderHeader, "class", "hide");
    }

    @Step("Assert inventories Filter Type on Inventory list")
    public void assertInventoryFilterType(Map<inventoryTypeEnum, Boolean> typesMap) {
        List<String> platformTypes = new ArrayList<>();
        for (Entry<inventoryTypeEnum, Boolean> inventoryEntry : typesMap.entrySet()) {
            if (inventoryEntry.getValue()) {
                platformTypes.add(inventoryEntry.getKey().attributeName());
            }
        }
        Condition condition = DSL.field("platform").in(platformTypes);
        Result<Record> results = dbUtils.getDbDataResult("inventory", condition);
        if (results.isNotEmpty()) {
            for (WebElement inventoryRow : Select.multiSelector(inventoryRow)) {
                String inventoryNameId = Select.selector(inventoryRow, relative_inventoryNameId).getText();
                String inventoryName = inventoryNameId.substring(0, inventoryNameId.lastIndexOf("#")).trim();
                softAssert.assertTrue(results.getValues("title", String.class).contains(inventoryName), "Filtered list does not contain inventory <" + inventoryName + ">");
            }
        } else {
            softAssert.assertEquals(Select.selector(inventoryListEmptyPlaceholder).getText(), InventoryText.EMPTY_PAGE_SEARCH, "There should be no inventories with selected types.");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in filtering inventories by type:");
        }
    }

    @Step("Assert Placement Filter List  in the Inventory list")
    public void assertPlacementFilterType(Map<placementTypeEnum, Boolean> typesMap, List<String> inventory) {
        List<String> placementsTypes = new ArrayList<>();
        for (Entry<placementTypeEnum, Boolean> placementEntry : typesMap.entrySet()) {
            if (placementEntry.getValue()) {
                placementsTypes.add("'" + placementEntry.getKey().attributeName() + "'");
            }
        }
        Condition condition = DSL.field("type").in(placementsTypes);
        Result<Record> results = dbUtils.getDbDataResult("placement", condition);
        if (results.isNotEmpty()) {
            for (String inventoryNameString : inventory) {
                clickPlacementWrap(inventoryNameString, true);
                List<WebElement> placementRowList = Select.multiSelector(placementRow);
                hardAssert.assertFalse(placementRowList.isEmpty(), "No Placements under [" + inventoryNameString + "] inventory");
                for (WebElement placementRow : placementRowList) {
                    String placementName = Select.selector(placementRow, placementNameString).getText();
                    softAssert.assertTrue(results.getValues("title", String.class).contains(placementName), "Placement name <" + placementName + "> does not exist in inventory <" + inventoryNameString + ">");
                }
                clickPlacementWrap(inventoryNameString, false);
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in filtering placements by type:");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Inventories list">
    static final By
            inventoryRow = By.xpath("//div[@class='inventory']"),
            relative_inventoryType = By.xpath(".//div[@class='logo']/i"),
            relative_inventoryNameId = By.xpath(".//div[@class='title']"),
            relative_inventoryStatus = By.xpath(".//div[contains(@class, 'first-status')]"),
            relative_fileAdxTxt = By.xpath(".//div[contains(@class, 'first-status')]/a[contains(@href,'ads.txt')]"),
            relative_placementsRollupButton = By.xpath(".//div[contains(@class, 'show-placement')]"),
            relative_placementCounts = By.xpath(".//div[@class='status']"),
            relative_inventoryEditButton = By.xpath(".//a[contains(@href, 'inventory/edit')]"),
            relative_earningsButton = By.xpath("//button[@type='submit']"),
            relative_bannerCreateButton = By.xpath(".//a[contains(@href, 'banner')]"),
            relative_videoCreateButton = By.xpath(".//a[contains(@href, 'video')]"),
            relative_nativeCreateButton = By.xpath(".//a[contains(@href, 'native')]"),
            inventoryPagingNumberButton = By.xpath(".//div[contains(@class,'pagination')]//a"),
            inventoriesTypeSelect = By.xpath("//select[@id='platform']"),
            placementsTypeSelect = By.xpath("//select[@id='typeInventory']");

    @Step("Find the inventory row by name")
    private WebElement getInventoryRow(String inventoryName) {
        WebElement inventory = Select.selectParentByAttributeContains(inventoryRow, relative_inventoryNameId, "title", inventoryName + " #");
        hardAssert.assertNotNull(inventory, "There is no inventory with the name <" + inventoryName + ">");
        return inventory;
    }

    @Step("Find the inventory row by ID")
    private WebElement getInventoryRow(InventoryDO data) {
        WebElement inventory;
        if (data.getId() != null) {
            inventory = Select.selectParentByAttributeContains(inventoryRow, relative_inventoryNameId, "title", "#" + data.getId());
        } else {
            inventory = Select.selectParentByAttributeContains(inventoryRow, relative_inventoryNameId, "title", data.getName() + " #");
        }
        hardAssert.assertNotNull(inventory, "There is no inventory <" + data.getName() + " #" + data.getId() + ">");
        return inventory;
    }

    @Step("Get inventory ID")
    public int getInventoryId(String inventoryName, int userID) {
        Condition condition = DSL.field("title").equal(inventoryName).and(DSL.field("user_id").equal(userID));
        org.jooq.Record data = dbUtils.getDbDataRecordFirst("inventory", condition);
        hardAssert.assertNotNull(data, "Inventory <" + inventoryName + "> of user <" + userID + "> not found");
        return data.get("id", Integer.class);
    }

    @Step("Open inventory creation page")
    public void clickCreateInventory() {
        Select.selector(createInventoryButton).click();
        Wait.waitForClickable(createTypeWeb);
    }

    @Step("Assert Inventory data in the list")
    public void assertInventoryRowInfo(InventoryDO data) {
        WebElement inventoryItem = getInventoryRow(data);
        String typeActual = Select.selector(inventoryItem, relative_inventoryType).getAttribute("class").replaceAll("link-text", "").trim();
        String statusLineExpected = data.getStatus() == InventoryEnum.inventoryStatusEnum.ACTIVE ? data.getStatus().publicName() + " Get ads.txt" : data.getStatus().publicName();
        softAssert.assertEquals(typeActual.toLowerCase(), data.getType().attributeName(), "List: Inventory type is incorrect");
        softAssert.assertEquals(Select.selector(inventoryItem, relative_inventoryNameId).getText(), data.getName() + " #" + data.getId(), "List: Inventory Name ID is incorrect");
        softAssert.assertEquals(Select.selector(inventoryItem, relative_inventoryStatus).getText(), statusLineExpected, "List: Inventory status is incorrect");
        softAssert.assertEquals(Select.selector(inventoryItem, relative_placementCounts).getText(), data.getPlacementsCountsString(), "List: Inventory placements string is incorrect");
        softAssert.assertEquals(AssertUtil.assertNotPresent(inventoryItem, relative_placementsRollupButton), data.getPlacementsCountsString().equals(InventoryText.invPlacementsCntEmpty), "List: Placements rollup button display is incorrect");
        softAssert.assertTrue(AssertUtil.assertPresent(Select.selector(inventoryItem, relative_earningsButton)), "List: Earnings button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(Select.selector(inventoryItem, relative_inventoryEditButton)), "List: Edit button is not displayed");
        if (data.getType() != inventoryTypeEnum.CTV) {
            softAssert.assertTrue(AssertUtil.assertPresent(inventoryItem, relative_bannerCreateButton), "List: Banner create button is not displayed");
            softAssert.assertTrue(AssertUtil.assertPresent(inventoryItem, relative_videoCreateButton), "List: Video create button is not displayed");
            softAssert.assertTrue(AssertUtil.assertPresent(inventoryItem, relative_nativeCreateButton), "List: Native create button is not displayed");
        } else {
            softAssert.assertFalse(AssertUtil.assertPresent(inventoryItem, relative_bannerCreateButton), "List: Banner create button is displayed");
            softAssert.assertTrue(AssertUtil.assertPresent(inventoryItem, relative_videoCreateButton), "List: Video create button is not displayed");
            softAssert.assertFalse(AssertUtil.assertPresent(inventoryItem, relative_nativeCreateButton), "List: Native create button is displayed");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Inventory data in the list has errors");
        }
    }

    @Step("Get inventory data from page and save to inventory object")
    public void setInventoryDataFromTable(InventoryDO data) {
        WebElement inventoryItem = getInventoryRow(data);
        String placementsString = Select.selector(inventoryItem, relative_placementCounts).getText();
        String status = Select.selector(inventoryItem, relative_inventoryStatus).getText();
        String nameId = Select.selector(inventoryItem, relative_inventoryNameId).getText();
        String type = Select.selector(inventoryItem, relative_inventoryType).getAttribute("class").replaceAll("link-text", "").trim();
        data.setName(nameId.substring(0, nameId.lastIndexOf("#")).trim());
        data.setId(FormatUtil.parseInteger(nameId.substring(nameId.lastIndexOf("#") + 1)));
        data.setStatus(InventoryEnum.inventoryStatusEnum.valueOf(status.replace("Get ads.txt", "").trim().toUpperCase()));
        data.setType(inventoryTypeEnum.valueOf(type.toUpperCase()));
        if (placementsString.contains("have")) {
            data.setCountBanners(0).setCountVideo(0).setCountNative(0);
        } else {
            if (data.getType() == inventoryTypeEnum.CTV) {
                data.setCountVideo(FormatUtil.parseInteger(placementsString));
            } else {
                String[] placementsArray = placementsString.split(",");
                data.setCountBanners(FormatUtil.parseInteger(placementsArray[0]));
                data.setCountVideo(FormatUtil.parseInteger(placementsArray[1]));
                data.setCountNative(FormatUtil.parseInteger(placementsArray[2]));
            }
        }
    }

    @Step("Assert Inventory Status in the list")
    public void assertInventoryStatusInfo(String name, InventoryEnum.inventoryStatusEnum status) {
        WebElement inventoryItem = getInventoryRow(name);
        String inventoryStatus = Select.selector(inventoryItem, relative_inventoryStatus).getText();
        softAssert.assertTrue(inventoryStatus.startsWith(status.publicName()), "List: Inventory status is incorrect. Actual: [" + inventoryStatus + "]. Expected: [" + status.publicName() + "]");
        if (status == InventoryEnum.inventoryStatusEnum.ACTIVE) {
            softAssert.assertTrue(AssertUtil.assertPresent(Select.selector(inventoryItem, relative_fileAdxTxt)), "Get adx.txt file is not displayed");
        }
    }

    @Step("Go to placement creation page")
    public void clickCreatePlacement(InventoryDO data, placementTypeEnum type) {
        WebElement inventory = getInventoryRow(data);
        switch (type) {
            case BANNER -> {
                Select.selector(inventory, relative_bannerCreateButton).click();
                Wait.waitForClickable(sizeHeightInput);
            }
            case VIDEO -> {
                Select.selector(inventory, relative_videoCreateButton).click();
                Wait.waitForVisibility(linearitySelect, relative_selectLabel);
            }
            case NATIVE -> {
                Select.selector(inventory, relative_nativeCreateButton).click();
                Wait.waitForVisibility(adEnvironmentSelect, relative_selectLabel);
            }
        }
    }

    @Step("Get random inventory name")
    public String getRandomInventoryName(inventoryTypeEnum type, InventoryEnum.inventoryStatusEnum status) {
        List<WebElement> inventories = Select.multiSelector(inventoryRow);
        inventories.removeIf(inv -> Select.selector(inv, relative_inventoryStatus).getText().contains(status.publicName()));
        inventories.removeIf(inv -> !Select.selector(inv, relative_inventoryType).getAttribute("class").replaceAll("link-text", "").trim().equalsIgnoreCase(type.toString()));
        WebElement selectedRow = (WebElement) RandomUtils.getRandomElement(inventories);
        String invName = Select.selector(selectedRow, relative_inventoryNameId).getAttribute("title");
        return invName.substring(0, invName.lastIndexOf("#")).trim();
    }

    @Step("Click Download Ads.txt button")
    public File clickDownloadAdsTxt(String inventoryName) {
        WebElement inventoryRow = getInventoryRow(inventoryName);
        Select.selector(inventoryRow, relative_fileAdxTxt).click();
        return FileUtils.getDownloadedFile("ads.txt", 15);
    }

    @Step("Assert Ads.txt required lines")
    public void assertAdsTxtRequired(File file, String inventoryDomain, String sellerId, String directness, String sellerDomain, String sellerHashKey) {
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#Ads.txt ")) {
                    softAssert.assertEquals(line.strip(), "#Ads.txt " + inventoryDomain, "Ads.txt: first line is incorrect");
                } else {
                    String[] parts = line.split(",\\s*");
                    if (parts.length >= 4) {
                        String domain = parts[0].trim();
                        String id = parts[1].trim();
                        String dir = parts[2].trim();
                        String hashKey = parts[3].trim();
                        if (domain.equals(sellerDomain) && id.equals(sellerId) && hashKey.equals(sellerHashKey) && dir.equals(directness)) {
                            found = true;
                            break;
                        }
                    }
                }
            }
            softAssert.assertTrue(found, "Ads.txt: No matching line found for seller details.");
            if (isSoftAssertLocal) {
                softAssert.assertAll("Ads.txt has error");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Assert Ads.txt custom domain")
    public void assertAdsTxtDomain(File file, String sellerDomain) {
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (!line.isEmpty()) {
                    String domain = parts[0].trim();
                    if (domain.equals(sellerDomain)) {
                        found = true;
                        break;
                    }
                }
            }
            softAssert.assertTrue(found, "Ads.txt: No matching line found for seller details.");
            if (isSoftAssertLocal) {
                softAssert.assertAll("Ads.txt has error");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Assert Ads.txt deleted lines")
    public void assertAdsTxtDeletedLine(File file, String inventoryDomain, String sellerId, String directness, String sellerDomain, String sellerHashKey) {
        boolean found = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#Ads.txt ")) {
                    softAssert.assertEquals(line.strip(), "#Ads.txt " + inventoryDomain, "Ads.txt: first line is incorrect");
                } else {
                    String[] parts = line.split(",\\s*");
                    if (parts.length >= 4) {
                        String domain = parts[0].trim();
                        String id = parts[1].trim();
                        String dir = parts[2].trim();
                        String hashKey = parts[3].trim();
                        if (domain.equals(sellerDomain) && id.equals(sellerId) && hashKey.equals(sellerHashKey) && dir.equals(directness)) {
                            found = true;
                            break;
                        }
                    }
                }
            }
            softAssert.assertFalse(found, "Ads.txt: Deleted line is found.");
            if (isSoftAssertLocal) {
                softAssert.assertAll("Ads.txt deletion check has errors.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Click on last page")
    public void pagingActionSelect(CommonEnums.pagingActionsTypes pagingAction, String pageNumber) {
        //todo refactor to comply with common
        clickPagingInventoriesLast();
    }

    @Step("Go to the selected page of the list")
    public void clickPagingInventoriesLast() {
        if (AssertUtil.assertPresent(inventoryPagingNumberButton)) {
            String pageNumber;
            List<WebElement> pages = Select.multiSelector(inventoryPagingNumberButton);
            pages.removeIf(s -> !s.getText().matches("\\d"));
            WebElement lastPageElement = pages.get(pages.size() - 1);
            pageNumber = lastPageElement.getText();
            lastPageElement.click();
            Wait.waitForStaleness(lastPageElement);
            lastPageElement = Select.selectByTextEquals(inventoryPagingNumberButton, pageNumber);
            Wait.attributeToBe(Select.selector(lastPageElement, By.xpath("./parent::span|li")), "class", "active");
        } else {
            System.err.println("There are no pages!");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Placements list">
    static final By
            placementRow = By.xpath(".//tbody/tr"),
            placementTypeIcon = By.xpath(".//span[contains(@class, 'link-text')]"),
            placementNameString = By.xpath(".//span[2]"),
            placementSizeStreamString = By.xpath(".//span[contains(@class, 'light-text')]"),
            placementIdString = By.xpath("./td[1]/div"),
            placementStatusString = By.xpath("./td[2]/span"),
            placementPriceString = By.xpath("./td[3]/span"),
            placementCountryCpmIcon = By.xpath(".//a[@data-tooltip-id='pl_country']"),
            placementTagsButton = By.xpath(".//a[contains(@class, 'tag_code')]"),
            placementEditButton = By.xpath(".//a[contains(@class, 'edit-placement')]");

    @Step("Get placement row")
    public WebElement getPlacementRow(PlacementDO data) {
        WebElement placement;
        if (data.getId() != null) {
            placement = Select.selectParentByTextEquals(placementRow, placementIdString, "ID#" + data.getId());
        } else {
            placement = Select.selectParentByTextEquals(placementRow, placementNameString, data.getName());
        }
        hardAssert.assertNotNull(placement, "There is no placement <" + data.getName() + " #" + data.getId() + ">");
        return placement;
    }

    @Step("Look for Placement on pages")
    public void clickLastPagePlacements() {
        if (AssertUtil.assertPresent(inventoryRow, inventoryPagingNumberButton)) {
            WebElement lastPageElement = Select.multiSelector(inventoryRow, inventoryPagingNumberButton).getLast();
            lastPageElement.click();
            Wait.waitForStaleness(lastPageElement);
            lastPageElement = Select.multiSelector(inventoryRow, inventoryPagingNumberButton).getLast();
            Wait.attributeToBe(Select.selector(lastPageElement, relative_pagingPageNumberParent), "class", "active");
        }
    }

    @Step("Assert Placement data in the list")
    public void assertPlacementRow(PlacementDO data) {
        WebElement placementItem = getPlacementRow(data);
        String typeAttr = Select.selector(placementItem, placementTypeIcon).getAttribute("class").replaceAll("link-text", "").trim();
        String sizeLinearity;
        String statusText = data.getStatus() ? "Active" : "Not Active";
        if (data.getType() == placementTypeEnum.VIDEO) {
            sizeLinearity = "(" + data.getLinearity().publicName().substring(data.getLinearity().publicName().indexOf("/") + 1).trim() + ")";
        } else {
            if (data.getSize().getPreset() == iabSizesEnum.FULLSCREEN) {
                sizeLinearity = "(Fullscreen)";
            } else {
                sizeLinearity = "(" + data.getSize().getWidth() + "x" + data.getSize().getHeight() + ")";
            }
        }
        softAssert.assertEquals(typeAttr, data.getType().attributeName(), "List: Placement type is incorrect");
        softAssert.assertEquals(Select.selector(placementItem, placementSizeStreamString).getText().trim(), sizeLinearity, "List: Placement Size/Linearity is incorrect");
        softAssert.assertEquals(Select.selector(placementItem, placementIdString).getText(), "ID#" + data.getId(), "List: Placement id is incorrect");
        softAssert.assertEquals(Select.selector(placementItem, placementStatusString).getText(), statusText, "List: Placement status is incorrect");
        softAssert.assertEquals(Select.selector(placementItem, placementPriceString).getText(), "$" + FormatUtil.formatBigSpaceWithDecimal(data.getBidPrice().getValue()), "List: Placement price is incorrect");
        softAssert.assertEquals(AssertUtil.assertPresent(placementItem, placementCountryCpmIcon), data.getCountryCpm(), "List: Placement Country CPM setting is incorrect");
    }

    @Step("Get Inventories List on the page")
    public List<String> getInventoriesNames() {
        String inventoryNameId, inventoryName;
        List<WebElement> inventoryRowList = Select.multiSelector(inventoryRow, relative_inventoryNameId);
        hardAssert.assertFalse(inventoryRowList.isEmpty(), "No Inventories on the page");
        List<String> inventoryNamesList = new ArrayList<>();
        for (WebElement inventoryRowName : inventoryRowList) {
            inventoryNameId = inventoryRowName.getText();
            inventoryName = inventoryNameId.substring(0, inventoryNameId.lastIndexOf("#")).trim();
            inventoryNamesList.add(inventoryName);
        }
        return inventoryNamesList;
    }

    //TODO removing this step requires rework of the search test
    @Step("Click on placement wrap button on inventory")
    public void clickPlacementWrap(String inventoryName, boolean toOpen) {
        WebElement inventoryItem = getInventoryRow(inventoryName);
        if (toOpen != AssertUtil.assertPresent(inventoryItem, placementRow)) {
            Select.selector(inventoryItem, relative_placementsRollupButton).click();
        }
    }

    @Step("Click on placement wrap button on inventory")
    public void clickPlacementWrap(InventoryDO inventoryData, boolean toOpen) {
        WebElement inventoryItem = getInventoryRow(inventoryData);
        if (toOpen != AssertUtil.assertPresent(inventoryItem, placementRow)) {
            Select.selector(inventoryItem, relative_placementsRollupButton).click();
        }
    }

    @Step("Open placement edit page")
    public void clickEditPlacement(PlacementDO pd) {
        Select.selector(getPlacementRow(pd), placementEditButton).click();
        Wait.waitForClickable(nameInput);
    }

    @Step("Get random placement from opened inventory")
    public void setPlacementDataFromTable(PlacementDO data) {
        List<WebElement> placements = Select.multiSelector(placementRow);
        placements.removeIf(plc -> !Select.selector(plc, placementTypeIcon).getAttribute("class").contains(data.getType().publicName().toLowerCase()));
        hardAssert.assertTrue(!placements.isEmpty(), "No placements of type [" + data.getType() + "] in the list");
        WebElement selectedRow = (WebElement) RandomUtils.getRandomElement(placements);
        String sizeLinearity = Select.selector(selectedRow, placementSizeStreamString).getText().replace("(", "").replace(")", "").trim();
        data.setName(Select.selector(selectedRow, placementNameString).getText())
                .setId(Integer.valueOf(Select.selector(selectedRow, placementIdString).getText().replace("ID#", "")));
        if (data.getType() == placementTypeEnum.VIDEO) {
            data.setLinearity(sizeLinearity);
        } else {
            PlacementDO.Size size;
            if (!sizeLinearity.equals("Fullscreen")) {
                size = new PlacementDO.Size(
                        Integer.valueOf(sizeLinearity.substring(0, sizeLinearity.indexOf("x"))),
                        Integer.valueOf(sizeLinearity.substring(sizeLinearity.indexOf("x") + 1)));
            } else {
                size = new PlacementDO.Size(0, 0);
            }
            data.setSize(size);
        }
    }

    @Step("Get placement ID")
    public int getPlacementId(Integer inventoryId, PlacementDO pData) {
        Condition condition = DSL.field("inventory_id").equal(inventoryId).and(DSL.field("type").equal(pData.getType().attributeName())).and(DSL.field("title").equal(pData.getName()));
        org.jooq.Record data = dbUtils.getDbDataRecordFirst("placement", condition);
        hardAssert.assertNotNull(data, pData.getType() + " placement <" + pData.getName() + "> of inventory <" + inventoryId + "> not found");
        return data.get("id", Integer.class);
    }

    //</editor-fold>

    //<editor-fold desc="Placement tags">
    static final By
            placementTagModalActive = By.xpath("//div[contains(@class, 'open')]"),
            placementTagTab = By.xpath(".//li[@class='tab']/a"),
            placementTagText1 = By.xpath(".//div[@class='active']/p[1]"),
            placementTagText2 = By.xpath(".//div[@class='active']/p[2]"),
            placementTagList = By.xpath(".//div[@class='active']/ul"),
            placementTagTextQuote = By.xpath(".//div[@class='active']/blockquote"),
            placementTagCode1 = By.xpath(".//div[@class='active']/pre[1]"),
            placementTagCode2 = By.xpath(".//div[@class='active']/pre[2]"),
            placementTagCode3 = By.xpath(".//div[@class='active']/pre[3]"),
            tagTextGeneral = By.xpath(".//div/p"),
            scriptTagGeneral = By.xpath(".//div/pre");

    @Step("Click on Placement Banner tag code button")
    public void clickPlacementTagButton(PlacementDO data) {
        Select.selector((getPlacementRow(data)), placementTagsButton).click();
        Wait.waitForVisibility(placementTagModalActive, scriptTagGeneral);//Inapp native modals differ from others, there is no Close button
    }

    @Step("Select Banner tag tab")
    public void clickTagTab(String tagName) {
        WebElement tagTab = Select.selectByTextEquals(placementTagModalActive, placementTagTab, tagName);
        tagTab.click();
        Wait.attributeToBe(tagTab, "class", "active");
    }

    @Step("Assert Banner Tags data on the Modal Window")
    public void assertBannerTags(PlacementDO data, bannerTagsEnum tagName, inventoryTypeEnum inventoryType) {
        switch (tagName) {
            case DIRECT -> {
                String tag = InventoryText.bannerWebDirectTagCode.replace("${placementId}", data.getId().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText(), InventoryText.bannerWebDirectTagText, "Direct tag: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), tag, "Direct tag: Script is incorrect");
            }
            case JS -> {
                String tag = inventoryType.equals(inventoryTypeEnum.WEB) ? InventoryText.bannerWebJsTagCode : InventoryText.bannerInAppJsCode,
                        text = inventoryType.equals(inventoryTypeEnum.WEB) ? InventoryText.bannerWebJsTagText : InventoryText.bannerInAppJsText;
                tag = FormatUtil.formatEscapeRegex(tag.replace("${placementId}", data.getId().toString()));
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText(), text, "JS tag: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), tag, "JS tag: Script is incorrect");
            }
            case GOOGLE_DFP -> {
                String tag = InventoryText.bannerWebGoogleTagCode.replace("${placementId}", data.getId().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText(), InventoryText.bannerWebGoogleTagText, "Google (DFP): Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), tag, "Google (DFP): Script is incorrect");
            }
            case ASYNC -> {
                String tag = InventoryText.bannerWebAsyncTagCode_ads
                        .replace("${placementId}", data.getId().toString())
                        .replace("${bannerWidth}", data.getSize().getWidth().toString())
                        .replace("${bannerHeight}", data.getSize().getHeight().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText(), InventoryText.bannerWebAsyncTagText_head, "Async tag: Text 1 is incorrect");
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText2).getText(), InventoryText.bannerWebAsyncTagText_body, "Async tag: Text 2 is incorrect");
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagList).getText(), InventoryText.bannerWebAsyncTagText_values, "Async tag: Field values are incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), InventoryText.bannerWebAsyncTagCode_head.replace("${placementId}", data.getId().toString()), "Async tag: Script <head> is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode2).getText(), InventoryText.bannerWebAsyncTagCode_body.replace("${placementId}", data.getId().toString()), "Async tag: Script <body> is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode3).getText(), tag, "Async tag: Script <add ads> is incorrect");
            }
            case APPLOVIN -> {
                String tag = InventoryText.bannerInAppApplovinCode
                        .replace("${placementId}", data.getId().toString())
                        .replace("${bw}", data.getSize().getWidth().toString())
                        .replace("${bh}", data.getSize().getHeight().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText(), InventoryText.bannerInAppApplovinText, "Applovin: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), tag, "Applovin: Script is incorrect");
            }
        }
    }

    @Step("Assert Video Tags data on the Modal Window")
    public void assertVideoTag(PlacementDO data, videoTagsEnum tagName, inventoryTypeEnum inventoryType) {
        String tag, text;
        switch (tagName) {
            case VASTVPAID -> {
                if (inventoryType.equals(inventoryTypeEnum.WEB)) {
                    tag = InventoryText.videoWebVastCode;
                    text = InventoryText.videoWebVastText_1;
                } else if (inventoryType.equals(inventoryTypeEnum.CTV)) {
                    tag = InventoryText.videoCtvVastCode;
                    text = InventoryText.videoInAppVastText;
                } else {
                    tag = InventoryText.videoInAppVastCode;
                    text = InventoryText.videoInAppVastText;
                }
                tag = FormatUtil.formatEscapeRegex(tag.replace("${placementId}", data.getId().toString()));
                if (!inventoryType.equals(inventoryTypeEnum.CTV)) {
                    softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), text, "VAST/VPAID: Text 1 is incorrect");
                }
                if (inventoryType.equals(inventoryTypeEnum.WEB)) {
                    softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText2).getText().trim(), InventoryText.videoWebVastText_2, "VAST/VPAID: Text 2 is incorrect");
                }
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "VAST/VPAID: Script is incorrect");
            }
            case JW -> {
                tag = InventoryText.videoWebJwCode.replace("${placementId}", data.getId().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoWebJwText, "JW Player: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "JW Player: Script is incorrect");
            }
            case DFP -> {
                tag = InventoryText.videoWebDfpCode.replace("${placementId}", data.getId().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoWebDfpText_1, "Google (DFP): Text 1 is incorrect");
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText2).getText().trim(), InventoryText.videoWebDfpText_2, "Google (DFP): Text 2 is incorrect");
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagTextQuote).getText().trim(), InventoryText.videoWebDfpText_note, "Google (DFP): Text of Note is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "Google (DFP): Script is incorrect");
            }
            case LKQD -> {
                if (inventoryType.equals(inventoryTypeEnum.WEB)) {
                    tag = InventoryText.videoWebLkqdCode;
                } else if (inventoryType.equals(inventoryTypeEnum.CTV)) {
                    tag = InventoryText.videoCtvLkqdCode;
                } else {
                    tag = InventoryText.videoInAppLkqdCode;
                }
                tag = FormatUtil.formatEscapeRegex(tag.replace("${placementId}", data.getId().toString()));
                if (!inventoryType.equals(inventoryTypeEnum.CTV)) {
                    softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoWebLkqdText, "LKQD: Text is incorrect");
                }
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "LKQD: Script is incorrect");
            }
            case CONNATIX -> {
                tag = InventoryText.videoWebConnatixCode.replace("${placementId}", data.getId().toString());
                text = inventoryType.equals(inventoryTypeEnum.WEB) ? InventoryText.videoWebConnatixText : InventoryText.videoInAppConnatixText;
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), text, "Connatix: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "Connatix: Script is incorrect");
            }
            case API -> {
                tag = InventoryText.videoWebApiCode.replace("${placementId}", data.getId().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoWebApiText, "API: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "API: Script is incorrect");
            }
            case COLUMN6 -> {
                tag = inventoryType.equals(inventoryTypeEnum.WEB) ? InventoryText.videoWebColumn6Code : InventoryText.videoInAppColumn6Code;
                tag = FormatUtil.formatEscapeRegex(tag.replace("${placementId}", data.getId().toString()));
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText(), InventoryText.videoWebColumn6Text, "Column6: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), tag, "Column6: Script is incorrect");
            }
            case SPRINGSERVE -> {
                tag = inventoryType.equals(inventoryTypeEnum.WEB) ? InventoryText.videoWebSpringServeCode : InventoryText.videoInAppSpringServeCode;
                tag = FormatUtil.formatEscapeRegex(tag.replace("${placementId}", data.getId().toString()));
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoWebSpringServeText, "Spring Serve: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), tag, "Spring Serve: Script is incorrect");
            }
            case SPOTX -> {
                tag = inventoryType.equals(inventoryTypeEnum.WEB) ? InventoryText.videoWebSpotxCode : InventoryText.videoInAppSpotxCode;
                tag = FormatUtil.formatEscapeRegex(tag.replace("${placementId}", data.getId().toString()));
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoWebSpotxText, "SpotX: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "SpotX: Script is incorrect");
            }
            case ANIVIEW -> {
                tag = InventoryText.videoWebAniviewCode.replace("${placementId}", data.getId().toString());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoWebAniviewText, "Aniview: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "Aniview: Script is incorrect");
            }
            case APPLOVIN -> {
                tag = InventoryText.videoInAppApplovinCode
                        .replace("${placementId}", data.getId().toString())
                        .replace("${projectName}", getPlatformNameFooter());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoInAppApplovinText, "Applovin: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "Applovin: Script is incorrect");
            }
            case IRONSRC -> {
                tag = InventoryText.videoInAppIronsrcCode
                        .replace("${placementId}", data.getId().toString())
                        .replace("${projectName}", getPlatformNameFooter());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoInAppIronsrcText, "Ironsrc: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "Ironsrc: Script is incorrect");
            }
            case AERSERV -> {
                tag = InventoryText.videoInAppAerServCode
                        .replace("${placementId}", data.getId().toString())
                        .replace("${projectName}", getPlatformNameFooter());
                tag = FormatUtil.formatEscapeRegex(tag);
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText().trim(), InventoryText.videoInAppAerServText, "AerServ: Text is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText().trim(), tag, "AerServ: Script is incorrect");
            }
            case ROKU -> {
                String rokuCtvVideoTagScriptReplaced = InventoryText.videoCtvRokuCode.replace("${placementId}", data.getId().toString());
                softAssert.assertEquals(Select.selector(placementTagModalActive, placementTagText1).getText(), InventoryText.videoCtvRokuText, "Text Roku tag is incorrect");
                softAssert.assertMatches(Select.selector(placementTagModalActive, placementTagCode1).getText(), rokuCtvVideoTagScriptReplaced, "Script Roku tag is incorrect");
            }
        }
    }

    @Step("Assert Native Tags data on the Modal Window")
    public void assertNativeTags(PlacementDO data, inventoryTypeEnum inventoryType) {
        String tag = inventoryType == inventoryTypeEnum.WEB ? InventoryText.nativeWebTagCode : InventoryText.nativeInAppTagCode,
                text = inventoryType == inventoryTypeEnum.WEB ? InventoryText.nativeWebTagText : InventoryText.nativeInAppTagText;
        tag = FormatUtil.formatEscapeRegex(tag.replace("${placementId}", data.getId().toString()));
        softAssert.assertEquals(Select.selector(placementTagModalActive, tagTextGeneral).getText().trim(), text, "Native tag: Text is incorrect");
        softAssert.assertMatches(Select.selector(placementTagModalActive, scriptTagGeneral).getText().trim(), tag, "Native tag: Script is incorrect");
    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="Inventory create/edit">
    private static final By
            nameInput = titleInput,
            createTypeAndroid = By.xpath("//a[@data-platform='android']"),
            createTypeIOS = By.xpath("//a[@data-platform='ios']"),
            createTypeWeb = By.xpath("//a[@data-platform='web']"),
            createTypeCTV = By.xpath("//a[@data-platform='ctv']"),
            domainInput = By.xpath("//input[@id='address']"),
            storeUrlInput = By.xpath("//input[@id='storeurl']"),
            languageSelect = By.xpath("//select[@id='language']"),
            allowedDspSelect = By.xpath("//select[@id='allowed_dsp']"),
            blockedDspSelect = By.xpath("//select[@id='blocked_dsp']"),
            ronToggle = By.xpath("//input[@name='ron_traffic']"),
            supplyCategoriesList = By.xpath("//div[@id='ms-iab_categories']/div[@class='ms-selectable']//li[@title][not(@style)]"),
            supplyCategoriesSelected = By.xpath("//div[@id='ms-iab_categories']/div[@class='ms-selection']//li[contains(@class, 'ms-selected')]"),
            filterCategoriesList = By.xpath("//div[@id='ms-blocked_iab_categories']/div[@class='ms-selectable']//li[@title][not(@style)]"),
            filterCategoriesSelected = By.xpath("//div[@id='ms-blocked_iab_categories']/div[@class='ms-selection']//li[contains(@class, 'ms-selected')]"),
            blockedDomainsInput = By.xpath("//textarea[@id='blocked_domains']");

    @Step("Input name")
    public void inputName(String name) {
        enterInInput(nameInput, name);
    }

    @Step("Save inventory")
    public void clickSaveInventory() {
        Select.selector(submitButton).click();
        Wait.waitForClickable(createInventoryButton);
        Wait.attributeContains(preloaderHeader, "class", "hide");
    }

    @Step("Save inventory with Errors")
    public void clickSaveInventoryWithErrors() {
        Select.selector(submitButton).click();
        Wait.attributeNotContains(submitButton, "class", "disabled");
    }

    @Step("Setup inventory settings")
    public void setupInventory(InventoryDO data) {
        inputName(data.getName());
        inputInventoryDomain(data.getBundleDomain());
        if (data.getType().equals(inventoryTypeEnum.CTV)) {
            inputInventoryStoreURL(data.getStoreUrl());
        }
        selectLanguage(data.getLanguage());
        if (!data.getAllowedDsp().isEmpty()) {
            selectAllowedDsp(data.getAllowedDsp());
        }
        if (!data.getBlockedDsp().isEmpty()) {
            selectBlockedDsp(data.getBlockedDsp());
        }
        clickToggle(ronToggle, data.getRonTraffic());
        selectCategoriesSupply(data.getCategoriesSelected());
        selectCategoriesFilter(data.getCategoriesFiltered());
        inputBlockedDomain(data.getBlockedDomains());
    }

    @Step("Select inventory type")
    public void selectInventoryType(inventoryTypeEnum type) {
        switch (type) {
            case WEB -> Select.selector(createTypeWeb).click();
            case IOS -> Select.selector(createTypeIOS).click();
            case ANDROID -> Select.selector(createTypeAndroid).click();
            case CTV -> Select.selector(createTypeCTV).click();
        }
        Wait.waitForClickable(nameInput);
        Wait.sleep(700); //To compensate for modal closing animation
    }

    @Step("Input inventory domain")
    public void inputInventoryDomain(String domain) {
        enterInInput(domainInput, domain);
    }

    @Step("Input inventory Store URL")
    public void inputInventoryStoreURL(String storeURL) {
        enterInInput(storeUrlInput, storeURL);
    }

    @Step("Select language")
    public void selectLanguage(String language) {
        selectSingleSelectDropdownOption(languageSelect, language);
    }

    @Step("Select Allowed DSP")
    public void selectAllowedDsp(Map<String, Boolean> dspNames) {
        openMultiselectList(allowedDspSelect);
        selectMultiselectOptions(allowedDspSelect, dspNames);
        closeMultiselectList(allowedDspSelect);
    }

    @Step("Select random Allowed DSP")
    public Map<String, Boolean> selectRandomAllowedDsp(int count) {
        return selectRandomAllowedBlockedListItems(allowedDspSelect, blockedDspSelect, true, count);
    }

    @Step("Select Blocked DSP")
    public void selectBlockedDsp(Map<String, Boolean> dspNames) {
        openMultiselectList(blockedDspSelect);
        selectMultiselectOptions(blockedDspSelect, dspNames);
        closeMultiselectList(blockedDspSelect);
    }

    @Step("Select random Blocked DSP")
    public Map<String, Boolean> selectRandomBlockedDsp(int count) {
        return selectRandomAllowedBlockedListItems(allowedDspSelect, blockedDspSelect, false, count);
    }

    @Step("Set values in Supply categories right column")
    public void selectCategoriesSupply(List<adCategoryEnum> catToSelect) {
        List<String> catExpectedNames = catToSelect.stream().map(adCategoryEnum::publicName).toList();
        selectBlockListParameters(supplyCategoriesList, supplyCategoriesSelected, catExpectedNames);
    }

    @Step("Set values in Filter categories right column")
    public void selectCategoriesFilter(List<adCategoryEnum> catToSelect) {
        List<String> catExpectedNames = catToSelect.stream().map(adCategoryEnum::publicName).toList();
        selectBlockListParameters(filterCategoriesList, filterCategoriesSelected, catExpectedNames);
    }

    @Step("Input Blocked Domain")
    public void inputBlockedDomain(String domainName) {
        enterInInput(blockedDomainsInput, domainName);
    }

    @Step("Open inventory edit page")
    public void clickEditInventory(InventoryDO data) {
        Select.selector(getInventoryRow(data), relative_inventoryEditButton).click();
        Wait.waitForClickable(nameInput);
    }

    @Step("Assert Inventory data on the edit page")
    public void assertInventoryEditPage(InventoryDO data) {
        String allowString = getExpectedMultiselectText(data.getAllowedDsp(), "All Active", 10),
                blockString = getExpectedMultiselectText(data.getBlockedDsp(), "Select", 10);
        //using sets to assert without order
        Set<String> supplyActual = new HashSet<>(), supplyExpected = new HashSet<>(), filterActual = new HashSet<>(), filterExpected = new HashSet<>();
        for (WebElement sup : Select.multiSelector(supplyCategoriesSelected)) {
            supplyActual.add(sup.getText());
        }
        for (adCategoryEnum cat : data.getCategoriesSelected()) {
            supplyExpected.add(cat.publicName());
        }
        for (WebElement fil : Select.multiSelector(filterCategoriesSelected)) {
            filterActual.add(fil.getText());
            }
        for (adCategoryEnum cat : data.getCategoriesFiltered()) {
            filterExpected.add(cat.publicName());
        }
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), data.getName(), "Edit page: Name is incorrect");
        softAssert.assertEquals(Select.selector(domainInput).getAttribute("value"), data.getBundleDomain(), "Edit page: Domain is incorrect");
        if (data.getType() == inventoryTypeEnum.CTV) {
            softAssert.assertEquals(Select.selector(storeUrlInput).getAttribute("value"), data.getStoreUrl(), "Edit page: Store URL is incorrect");
        }
        softAssert.assertEquals(Select.selector(languageSelect, relative_selectLabel).getText().trim(), data.getLanguage(), "Edit page: Language is incorrect");
        softAssert.assertEquals(Select.selector(languageSelect, relative_selectLabel).getText().trim(), data.getLanguageLocale().getDisplayLanguage(), "Edit page: Language Locale is incorrect");
        softAssert.assertEquals(Select.selector(allowedDspSelect, relative_selectLabel).getText(), allowString, "Edit page: Allowed DSP list is incorrect");
        softAssert.assertEquals(Select.selector(blockedDspSelect, relative_selectLabel).getText(), blockString, "Edit page: Blocked DSP list is incorrect");
        softAssert.assertEquals(Select.selector(ronToggle).isSelected(), data.getRonTraffic(), "Edit page: RON traffic parameter is incorrect");
        softAssert.assertEquals(supplyActual, supplyExpected, "Edit page: Selected supply categories are incorrect. Expected: " + supplyExpected + ". Actual: " + supplyActual);
        softAssert.assertEquals(filterActual, filterExpected, "Edit page: Selected filter categories are incorrect. Expected: " + filterExpected + ". Actual: " + filterActual);
        softAssert.assertEquals(Select.selector(blockedDomainsInput).getText(), data.getBlockedDomains(), "Edit page: Blocked domains list is incorrect");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Inventory data on the edit page has errors");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Placement create/edit">

    @Step("Select Ad types to block")
    public void selectAdTypesToBlock(List<adTypeEnum> types) {
        List<String> adToBlock = types.stream().map(adTypeEnum::publicName).toList();
        selectBlockListParameters(adTypesAllowedList, adTypesBlockedList, adToBlock);
    }

    @Step("Select Placement attributes to block")
    public void selectPlacementAttributesToBlock(List<placementAttributeEnum> attributes) {
        List<String> attrToBlock = attributes.stream().map(placementAttributeEnum::publicName).toList();
        selectBlockListParameters(allowedAttributesList, allowedAttributesSelected, attrToBlock);
    }

    @Step("Assert placement edit page")
    public void assertPlacementEditPage(PlacementDO data) {
        switch (data.getType()) {
            case BANNER -> assertBannerEditPage(data);
            case VIDEO -> assertVideoEditPage(data);
            case NATIVE -> assertNativeEditPage(data);
        }
    }

    //<editor-fold desc="Placement create/edit - Common">
    private static final By //Common
            bidPriceSelect = By.xpath("//select[@id='price_type']"),
            bidPriceInput = By.xpath("//input[@id='cpm_field']"),
            testToggle = By.xpath("//input[@name='placement[test_mode]']"),
            apiFrameworkSelect = By.xpath("//select[@id='vpaidApi']"),
            adTypesAllowedList = By.xpath("//div[@id='ms-blocked_ad_type_f']/div[@class='ms-selectable']//li[@title][not(@style)]"),
            adTypesBlockedList = By.xpath("//div[@id='ms-blocked_ad_type_f']/div[@class='ms-selection']//li[contains(@class, 'ms-selected')]"),
            allowedAttributesList = By.xpath("//div[contains(@id, 'blocked_attr')]/div[@class='ms-selectable']//li[@title][not(@style)]"),
            allowedAttributesSelected = By.xpath("//div[contains(@id, 'ms-blocked_attr')]/div[@class='ms-selection']//li[contains(@class, 'ms-selected')]"),
            backfillInput = By.xpath("//textarea[contains(@id, 'backfill')]");

    @Step("Setup placement")
    public void setupPlacement(PlacementDO data) {
        inputName(data.getName());
        setupBidPrice(data.getBidPrice());
        toggleTestMode(data.getTestMode());
        switch (data.getType()) {
            case BANNER -> setupPlacementBanner(data);
            case VIDEO -> setupPlacementVideo(data);
            case NATIVE -> setupPlacementNative(data);
        }
    }

    @Step("Clock test Mode toggle")
    public void toggleTestMode(boolean isTestMode) {
        clickToggle(testToggle, isTestMode);
    }

    @Step("Setup Bid price in Placement")
    public void setupBidPrice(PlacementDO.BidPrice bp) {
        selectSingleSelectDropdownOption(bidPriceSelect, bp.getType().publicName());
        enterInInput(bidPriceInput, bp.getValue());
    }

    @Step("Set Custom size values")
    public void setSizeCustom(Integer sizeW, Integer sizeH) {
        enterInInput(sizeWidthInput, sizeW);
        enterInInput(sizeHeightInput, sizeH);
    }

    @Step("Select placement position")
    public void selectPosition(adPositionEnum position) {
        Select.selector(positionSelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(positionSelect, relative_selectOptionWithTitle, "title", position.publicName()).click();
        Select.selector(nameInput).click();
    }

    @Step("Select API framework")
    public void selectApiFramework(Map<apiFrameworksEnum, Boolean> apiMap) {
        Map<String, Boolean> apiStringMap = new HashMap<>();
        for (Entry<apiFrameworksEnum, Boolean> entry : apiMap.entrySet()) {
            apiStringMap.put(entry.getKey().publicName(), entry.getValue());
        }
        selectMultiselectOptions(apiFrameworkSelect, apiStringMap);
    }

    @Step("Input Backfill")
    public void inputBackfill(String backfill) {
        enterInInput(backfillInput, backfill);
    }

    //</editor-fold>

    //<editor-fold desc="Placement create/edit - Banner">
    private static final By
            positionSelect = By.xpath("//select[contains(@name, 'foldpos_id')]"),
            sizeSelect = By.xpath("//select[@id='iabsize']"),
            sizeWidthInput = By.xpath("//input[@id='width']"),
            sizeHeightInput = By.xpath("//input[@id='height']");

    @Step("Setup Banner placement")
    private void setupPlacementBanner(PlacementDO bd) {
        selectBannerSize(bd.getSize());
        selectPosition(bd.getAdPosition());
        selectApiFramework(bd.getApiFrameworks());
        selectAdTypesToBlock(bd.getBlockedAdTypes());
        selectPlacementAttributesToBlock(bd.getBlockedAttributes());
        inputBackfill(bd.getBackfill());
    }

    @Step("Select banner size")
    public void selectBannerSize(PlacementDO.Size size) {
        iabSizesEnum sizePreset = size.getPreset();
        selectSingleSelectDropdownOption(sizeSelect, sizePreset.publicName());
        if (sizePreset == iabSizesEnum.CUSTOM) {
            softAssert.assertTrue(Select.selector(sizeWidthInput).getText().isEmpty(), "Width input is not empty");
            softAssert.assertTrue(Select.selector(sizeHeightInput).getText().isEmpty(), "Height input is not empty");
            setSizeCustom(size.getWidth(), size.getHeight());
        } else if (sizePreset == iabSizesEnum.FULLSCREEN) {
            softAssert.assertTrue(AssertUtil.assertNotPresent(sizeWidthInput), "Width input is displayed");
            softAssert.assertTrue(AssertUtil.assertNotPresent(sizeHeightInput), "Height input is displayed");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors on Size selection:");
        }
    }

    @Step("Assert Banner data on the edit page")
    public void assertBannerEditPage(PlacementDO data) {
        Set<String> adTypesBlockedActual = new HashSet<>(), adTypesBlockedExpected = new HashSet<>(), placementAttributesActual = new HashSet<>(), placementAttributesExpected = new HashSet<>();
        List<String> frameworksActual = new ArrayList<>(), frameworksExpected = new ArrayList<>();
        if (!data.getApiFrameworks().isEmpty()) {
            for (Entry<apiFrameworksEnum, Boolean> entry : data.getApiFrameworks().entrySet()) {
                if (entry.getValue()) {
                    frameworksExpected.add(entry.getKey().publicName());
                }
            }
        } else {
            frameworksExpected.add("Select params for API framework");
        }
        for (WebElement type : Select.multiSelector(adTypesBlockedList)) {
            adTypesBlockedActual.add(type.getText());
        }
        for (adTypeEnum type : data.getBlockedAdTypes()) {
            adTypesBlockedExpected.add(type.publicName());
        }
        for (WebElement attr : Select.multiSelector(allowedAttributesSelected)) {
            placementAttributesActual.add(attr.getText());
        }
        for (placementAttributeEnum attribute : data.getBlockedAttributes()) {
            placementAttributesExpected.add(attribute.publicName());
        }
        for (String s : Select.selector(apiFrameworkSelect, relative_selectLabel).getText().split(",")) {
            frameworksActual.add(s.trim());
        }
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), data.getName(), "Name is incorrect");
        softAssert.assertEquals(Select.selector(bidPriceSelect, relative_selectLabel).getText(), data.getBidPrice().getType().publicName(), "Bid price type is incorrect");
        softAssert.assertEquals(Select.selector(bidPriceInput).getAttribute("value"), FormatUtil.formatPriceNoZeros(data.getBidPrice().getValue()), "Bid price is incorrect");
        softAssert.assertEquals(Select.selector(testToggle).isSelected(), data.getTestMode(), "Test mode toggle is incorrect");
        softAssert.assertEquals(Select.selector(positionSelect, relative_selectLabel).getText().trim(), data.getAdPosition().publicName(), "Position is incorrect");
        softAssert.assertEquals(Select.selector(sizeSelect, relative_selectLabel).getText().trim(), data.getSize().getPreset().publicName(), "Size preset is incorrect");
        if (data.getSize().getPreset() == iabSizesEnum.FULLSCREEN) {
            softAssert.assertTrue(AssertUtil.assertNotPresent(sizeWidthInput), "Width input is displayed");
            softAssert.assertTrue(AssertUtil.assertNotPresent(sizeHeightInput), "Height input is displayed");
        } else {
            softAssert.assertEquals(Select.selector(sizeWidthInput).getAttribute("value"), data.getSize().getWidth().toString(), "Width is incorrect");
            softAssert.assertEquals(Select.selector(sizeHeightInput).getAttribute("value"), data.getSize().getHeight().toString(), "Height is incorrect");
        }
        softAssert.assertEquals(frameworksActual, frameworksExpected, "API Framework placeholder text is incorrect");
        softAssert.assertEqualsNoOrder(adTypesBlockedActual.toArray(), adTypesBlockedExpected.toArray(), "Blocked Ad types are incorrect");
        softAssert.assertEqualsNoOrder(placementAttributesActual.toArray(), placementAttributesExpected.toArray(), "Selected Placement attributes are incorrect");
        softAssert.assertEquals(Select.selector(backfillInput).getText(), data.getBackfill(), "Backfill is incorrect");
    }

    @Step("Assert Validation on the Banner edit page")
    public void assertValidationBannerEditPage(String namePlacement, String width, String height, String bidPrice) {
        assertValidationError(softAssert, nameInput, namePlacement);
        assertValidationError(softAssert, sizeWidthInput, width);
        assertValidationError(softAssert, sizeHeightInput, height);
        assertValidationError(softAssert, bidPriceInput, bidPrice);
    }

    //</editor-fold>

    //<editor-fold desc="Placement create/edit - Video">
    private static final By
            rewardedToggle = By.xpath("//input[@name='video[is_rewarded]']"),
            linearitySelect = By.xpath("//select[@id='linearity']"),
            startDelayInSecondsInput = By.xpath("//input[@id='startdelay']"),
            placingSelect = By.xpath("//select[@id='placing']"),
            videoStartDelaySelect = By.xpath("//select[@id='startdelay-select']"),
            playbackMethodsSelect = By.xpath("//select[@id='playbackmethod_id']"),
            protocolSelect = By.xpath("//select[@id='protocols']"),
            allowSkipToggle = By.xpath("//input[@id='skip']"),
            skipDelayInput = By.xpath("//input[@id='skipmin']"),
            serveAdsViaApiToggle = By.xpath("//input[@name='video[player]']"),
            allowedVideoAttributesList = By.xpath("//div[@id='ms-blocked_attr']/div[@class='ms-selectable']//li[@title][not(@style)]"),
            allowedVideoAttributesSelected = By.xpath("//div[@id='ms-blocked_attr']/div[@class='ms-selection']//li[contains(@class,'ms-selected')]"),
            mimeVideoList = By.xpath("//div[@id='ms-mimes']/div[@class='ms-selectable']//li[@title][not(@style)]"),
            mimeVideoSelected = By.xpath("//div[@id='ms-mimes']/div[@class='ms-selection']//li[contains(@class,'ms-selected')]");

    @Step("Setup Video placement")
    private void setupPlacementVideo(PlacementDO vd) {
        selectPosition(vd.getAdPosition());
        toggleRewarded(vd.getRewarded());
        selectVideoLinearityType(vd.getLinearity());
        if (vd.getLinearity() == videoLinearityTypeEnum.LINEAR_INSTREAM) {
            selectVideoStartDelay(vd.getStartDelayType());
            if (vd.getStartDelayType().equals(videoStartDelayTypeEnum.MIDROLL)) {
                inputStartDelay(vd.getStartDelayValue());
            }
        } else {
            selectVideoPlacingType(vd.getPlacing());
            toggleServeViaApi(vd.getServeViaApi());
        }
        selectPlaybackMethods(vd.getPlaybackMethod());
        selectProtocols(vd.getVideoProtocols());
        selectApiFramework(vd.getApiFrameworks());
        setSkip(vd.getAllowSkip(), vd.getSkipDelay());
        selectPlacementAttributesToBlock(vd.getBlockedAttributes());
        selectMimeToBlock(vd.getVideoMimeTypes());
        inputBackfill(vd.getBackfill());
    }

    @Step("Set Rewarded toggle")
    public void toggleRewarded(boolean isRewarded) {
        clickToggle(rewardedToggle, isRewarded);
    }

    @Step("Select Video Linearity")
    public void selectVideoLinearityType(videoLinearityTypeEnum videoLinearity) {
        String videoLinearityName = videoLinearity.publicName();
        Select.selector(linearitySelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(linearitySelect, relative_selectOptionWithTitle, "title", videoLinearityName).click();
        softAssert.assertEquals(Select.selector(linearitySelect, relative_selectLabel).getText(), videoLinearityName, "Video Linearity name on the label is wrong");
        Select.selector(nameInput).click();
    }

    @Step("Select Video Placing")
    public void selectVideoPlacingType(placingTypeEnum placing) {
        String placingTypeName = getPlacingType(placing);
        Select.selector(placingSelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(placingSelect, relative_selectOptionWithTitle, "title", placingTypeName).click();
        softAssert.assertEquals(Select.selector(placingSelect, relative_selectLabel).getText(), placingTypeName, "Placing name on the label is wrong");
        Select.selector(nameInput).click();
    }

    @Step("Set Video Start Delay")
    public void selectVideoStartDelay(videoStartDelayTypeEnum videoStartDelay) {
        Select.selector(videoStartDelaySelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(videoStartDelaySelect, relative_selectOptionWithTitle, "title", videoStartDelay.publicName()).click();
        softAssert.assertEquals(Select.selector(videoStartDelaySelect, relative_selectLabel).getText(), videoStartDelay.publicName(), "Video Start Delay name on the label is wrong");
        Select.selector(nameInput).click();
    }

    @Step("Input Start Delay in the seconds")
    public void inputStartDelay(Number startD) {
        enterInInput(startDelayInSecondsInput, startD);
    }

    @Step("Set Playback Methods")
    public void selectPlaybackMethods(playbackMethodsTypeEnum playbackMethods) {
        String playbackMethodsName = getPlaybackMethods(playbackMethods);
        Select.selector(playbackMethodsSelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(playbackMethodsSelect, relative_selectOptionWithTitle, "title", playbackMethodsName).click();
        softAssert.assertEquals(Select.selector(playbackMethodsSelect, relative_selectLabel).getText(), playbackMethodsName, "playback Methods name on the label is wrong");
        Select.selector(nameInput).click();
    }

    @Step("Select Protocols")
    public void selectProtocols(Map<protocolsTypeEnum, Boolean> protocolsMap) {
        String apiName;
        WebElement listOption;
        Select.selector(protocolSelect, relative_selectLabel).click();
        for (Entry<protocolsTypeEnum, Boolean> apiEntry : protocolsMap.entrySet()) {
            apiName = getProtocols(apiEntry.getKey());
            listOption = Select.selectByAttributeIgnoreCase(protocolSelect, relative_selectOptionWithTitle, "title", apiName);
            if (apiEntry.getValue() ^ listOption.getAttribute("class").contains("selected")) {
                Select.selector(listOption, relative_multiselectCheckbox).click();
            }
        }
        Select.selector(nameInput).click();
    }

    @Step("Set Skip Delay")
    public void setSkip(boolean isAllowed, Number delayValue) {
        clickToggle(allowSkipToggle, isAllowed);
        if (isAllowed) {
            enterInInput(skipDelayInput, delayValue);
        }
    }

    @Step("Click on Serve ads via API(VAST) link Toggle")
    public void toggleServeViaApi(boolean isAllowed) {
        clickToggle(serveAdsViaApiToggle, isAllowed);
    }

    @Step("Select Mime to block")
    public void selectMimeToBlock(List<mimeTypesEnum> types) {
        List<String> mimesToBlock = new ArrayList<>();
        types.forEach(type -> mimesToBlock.add(type.publicName()));
        selectBlockListParameters(mimeVideoList, mimeVideoSelected, mimesToBlock);
    }

    @Step("Assert Video data on the edit page")
    public void assertVideoEditPage(PlacementDO data) {
        Map<String, Boolean> protocolsMap = new LinkedHashMap<>(), frameworksMap = new LinkedHashMap<>();
        data.getVideoProtocols().forEach((k, v) -> protocolsMap.put(k.publicName(), v));
        data.getApiFrameworks().forEach((k, v) -> frameworksMap.put(k.publicName(), v));
        String apiPlaceholder = getExpectedMultiselectText(frameworksMap, "Select params for Protocols", 5),
                protocolsPlaceholder = getExpectedMultiselectText(protocolsMap, "Select params for API framework", 5);
        List<String> mimeActualNames = new ArrayList<>(), mimeExpectedNames = new ArrayList<>(), attributesActualNames = new ArrayList<>(), attributesExpectedNames = new ArrayList<>();
        data.getBlockedAttributes().forEach(attr -> attributesExpectedNames.add(attr.publicName()));
        data.getVideoMimeTypes().forEach(mime -> mimeExpectedNames.add(mime.publicName()));
        for (WebElement attr : Select.multiSelector(allowedVideoAttributesSelected)) {
            attributesActualNames.add(attr.getText());
        }
        for (WebElement mimeVideo : Select.multiSelector(mimeVideoSelected)) {
            mimeActualNames.add(mimeVideo.getText());
        }
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), data.getName(), "Name is incorrect");
        softAssert.assertEquals(Select.selector(bidPriceSelect, relative_selectLabel).getText(), data.getBidPrice().getType().publicName(), "Bid price type is incorrect");
        softAssert.assertEquals(Select.selector(bidPriceInput).getAttribute("value"), FormatUtil.formatPriceNoZeros(data.getBidPrice().getValue()), "Bid price is incorrect");
        softAssert.assertEquals(Select.selector(positionSelect, relative_selectLabel).getText(), data.getAdPosition().publicName(), "Position is incorrect");
        softAssert.assertEquals(Select.selector(testToggle).isSelected(), data.getTestMode(), "Test mode toggle is incorrect");
        softAssert.assertEquals(Select.selector(rewardedToggle).isSelected(), data.getRewarded(), "Rewarded toggle is incorrect");
        softAssert.assertEquals(Select.selector(linearitySelect, relative_selectLabel).getText(), data.getLinearity().publicName(), "Video linearity type is incorrect");
        if (data.getLinearity() == videoLinearityTypeEnum.LINEAR_INSTREAM) {
            softAssert.assertEquals(Select.selector(videoStartDelaySelect, relative_selectLabel).getText(), data.getStartDelayType().publicName(), "Video Start Delay type is incorrect");
            softAssert.assertEquals(Select.selector(videoStartDelaySelect, relative_selectLabel).getText(), data.getStartDelayType().publicName(), "Video Start Delay type is incorrect");
            if (data.getStartDelayType().equals(videoStartDelayTypeEnum.MIDROLL)) {
                softAssert.assertEquals(Select.selector(startDelayInSecondsInput).getAttribute("value"), data.getStartDelayValue().toString(), "Start Delay value is incorrect");
            }
        } else {    //Non-Linear/Out-Stream
            softAssert.assertEquals(Select.selector(placingSelect, relative_selectLabel).getText(), data.getPlacing().publicName(), "Placing type is incorrect");
            softAssert.assertEquals(Select.selector(serveAdsViaApiToggle).isSelected(), data.getServeViaApi(), "Serve Ads Via API is incorrect");
            softAssert.assertEquals(Select.selector(mimeVideoSelected).getAttribute("class").contains("disabled"), !data.getServeViaApi(), "Mime types disable status is incorrect");
            if (!data.getServeViaApi()) {
                for (mimeTypesEnum mime : mimeTypesEnum.values()) {
                    mimeExpectedNames.add(mime.publicName());
                }
            }
        }
        softAssert.assertEquals(Select.selector(playbackMethodsSelect, relative_selectLabel).getText(), data.getPlaybackMethod().publicName(), "Playback Methods type is incorrect");
        softAssert.assertEquals(Select.selector(protocolSelect, relative_selectLabel).getText(), protocolsPlaceholder, "Protocols placeholder text is incorrect");
        softAssert.assertEquals(Select.selector(apiFrameworkSelect, relative_selectLabel).getText(), apiPlaceholder, "API Framework placeholder text is incorrect");
        softAssert.assertEquals(Select.selector(allowSkipToggle).isSelected(), data.getAllowSkip(), "Allow skip toggle value is incorrect");
        if (data.getAllowSkip()) {
            softAssert.assertEquals(Select.selector(skipDelayInput).getAttribute("value"), data.getSkipDelay().toString(), "Skip Delay value is incorrect");
        }
        softAssert.assertEqualsNoOrder(mimeActualNames.toArray(), mimeExpectedNames.toArray(), "Mime types are incorrect. Actual: " + mimeActualNames + " Expected: " + mimeExpectedNames);
        softAssert.assertEqualsNoOrder(attributesActualNames.toArray(), attributesExpectedNames.toArray(), "Selected Placement attributes are incorrect. Actual: " + attributesActualNames + " Expected: " + attributesExpectedNames);
        softAssert.assertEquals(Select.selector(backfillInput).getAttribute("value"), data.getBackfill(), "Backfill is incorrect");
    }

    @Step("Assert Validation on the Video edit page")
    public void assertValidationVideoEditPage(String namePlacement, String bidPrice, String startDelay, String skipDelay) {
        assertValidationError(softAssert, nameInput, namePlacement);
        if (bidPrice != null) {  //custom check instead of common, because in this case .isDisplayed for the label returns false (probably because the calculated height of the label is 0)
            if (Select.selector(bidPriceInput, relative_selectValidationLabel).getAttribute("class").contains("active")) {
                softAssert.assertEquals(Select.selector(bidPriceInput, relative_selectValidationLabel).getAttribute("data-error"), bidPrice, "Validation: [Bid price] setting validation text is incorrect");
            } else {
                softAssert.fail("Validation: [Bid price] setting validation text is not displayed");
            }
        }
        assertValidationError(softAssert, startDelayInSecondsInput, startDelay);
        assertValidationError(softAssert, skipDelayInput, skipDelay);
    }

    //</editor-fold>

    //<editor-fold desc="Placement create/edit - Native">
    private static final By
            adEnvironmentSelect = By.xpath("//select[@id='context_id']"),
            advancedAdSettingsSelect = By.xpath("//select[@id='contextsubtype_id']"),
            adUnitOptionsSelect = By.xpath("//select[@id='plcmttype_id']"),
            adTitleToggle = By.xpath("//input[@type='checkbox'][contains(@name, 'title')]"),
            adTitleInput = By.xpath("//input[@id='ad_title']"),
            dataAssetTypeSelect = By.xpath("//select[@id='asset_type']"),
            imageTypeSelect = By.xpath("//select[@id='ad_img']"),
            descriptionLengthInput = By.xpath("//input[@id='ad_data']"),
            minWidthInput = By.xpath("//input[@name='element[image][main][width]']"),
            minHeightInput = By.xpath("//input[@name='element[image][main][height]']"),
            setSelfIDsForElementsButton = By.xpath("//a[@href='#setUpId']"),
            selfIdModalSaveButton = By.xpath(".//div[@id='setUpId']//a[contains(@class,'modal-close')]"),
            selfIdModal = By.xpath("//div[@id='setUpId']");

    @Step("Setup Native placement")
    public void setupPlacementNative(PlacementDO data) {
        setSizeCustom(data.getSize().getWidth(), data.getSize().getHeight());
        selectAdEnvironment(data.getAdEnvironment());
        selectAdvancedAdSettings(data.getAdvancedAdSettings());
        selectAdUnitOptions(data.getAdUnit());
        setAdTitle(data.getAdTitleStatus(), data.getAdTitleLength());
        selectDataAssetType(data);
        selectAdImages(data);
        setElementIds(data.getElementsIdMap());
    }

    @Step("Select Ad Environment")
    public void selectAdEnvironment(placementAdEnvironmentEnum adEnvironment) {
        Select.selector(adEnvironmentSelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(adEnvironmentSelect, relative_selectOptionWithTitle, "title", adEnvironment.publicName()).click();
        softAssert.assertEquals(Select.selector(adEnvironmentSelect, relative_selectLabel).getText(), adEnvironment.publicName(), "Ad Enviroment Name on the label is wrong");
        Select.selector(nameInput).click();
    }

    @Step("Select Advanced Ad Settings")
    public void selectAdvancedAdSettings(placementAdvancedAdSettingsEnum advancedAdSettings) {
        Select.selector(advancedAdSettingsSelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(advancedAdSettingsSelect, relative_selectOptionWithTitle, "title", advancedAdSettings.publicName()).click();
        softAssert.assertEquals(Select.selector(advancedAdSettingsSelect, relative_selectLabel).getText(), advancedAdSettings.publicName(), "Advanced Ad Settings Name on the label is wrong");
        Select.selector(nameInput).click();
    }

    @Step("Select Ad Unit Options")
    public void selectAdUnitOptions(placementAdUnitOptionsEnum adUnitOptions) {
        String adUnitOptionsName = getAdUnitOptionsName(adUnitOptions);
        Select.selector(adUnitOptionsSelect, relative_selectLabel).click();
        Select.selectByAttributeIgnoreCase(adUnitOptionsSelect, relative_selectOptionWithTitle, "title", adUnitOptionsName).click();
        softAssert.assertEquals(Select.selector(adUnitOptionsSelect, relative_selectLabel).getText(), adUnitOptionsName, "Ad Unit Options Name on the label is wrong");
        Select.selector(nameInput).click();
    }

    @Step("Click on Ad Title Toggle")
    public void setAdTitle(boolean status, Integer value) {
        clickToggle(adTitleToggle, status);
        if (status) {
            enterInInputWithFocus(adTitleInput, value);
        }
    }

    @Step("Select Data Asset Type")
    public void selectDataAssetType(PlacementDO pd) {
        Map<String, Boolean> assetNamesMap = new HashMap<>();
        pd.getDataAssetsMaps().forEach((k, v) -> assetNamesMap.put(k.publicName(), v));
        //** Using somewhat custom code instead of common selectMultiselectOptions, because dataAssetTypeSelect is the only select on the platform, that has no size and no coordinates, so test is failing when tries to close it
        openMultiselectList(dataAssetTypeSelect);
        for (Entry<String, Boolean> entry : assetNamesMap.entrySet()) {
            clickDropdownMultiselectOption(dataAssetTypeSelect, entry.getKey(), entry.getValue());
        }
        closeMultiselectList(Select.selector(dataAssetTypeSelect, relative_selectLabel));
        //**//
        if (pd.getDataAssetsMaps().containsKey(placementDataAssetTypeEnum.DESC)) {
            if (pd.getDataAssetsMaps().get(placementDataAssetTypeEnum.DESC)) {
                enterInInputWithFocus(descriptionLengthInput, pd.getDescriptionLength());
            }
        }
    }

    @Step("Select Image Type")
    public void selectAdImages(PlacementDO pd) {
        Map<String, Boolean> imagesNamesMap = new HashMap<>();
        pd.getAdImagesMap().forEach((k, v) -> imagesNamesMap.put(k.publicName(), v));
        selectMultiselectOptions(imageTypeSelect, imagesNamesMap);
        if (pd.getAdImagesMap().containsKey(placementImageTypeEnum.MAIN)) {
            if (pd.getAdImagesMap().get(placementImageTypeEnum.MAIN)) {
                enterInInputWithFocus(minWidthInput, pd.getNativeMainImageSize().getWidth());
                enterInInputWithFocus(minHeightInput, pd.getNativeMainImageSize().getHeight());
            }
        }
    }

    @Step("Click on Set self ID's for elements button")
    public void clickSetSelfIdsForElementsButton() {
        Select.selector(setSelfIDsForElementsButton).click();
        Wait.waitForClickable(selfIdModalSaveButton);
    }

    @Step("Click on Save button on Self ID's modal")
    public void clickSaveElementIdModal() {
        Select.actionMouseScroll(Select.selector(selfIdModal, modalCloseButton));
        Wait.waitForClickable(Select.selector(selfIdModal, modalCloseButton));
        modalClickClose(selfIdModal);
    }

    @Step("Set Self ID's for Elements")
    public void setElementIds(Map<String, PlacementDO.SelfId> elementIdsMap) {
        if (!elementIdsMap.isEmpty()) {
            clickSetSelfIdsForElementsButton();
            for (Entry<String, PlacementDO.SelfId> entry : elementIdsMap.entrySet()) {
                WebElement row = Select.selectParentByTextEquals(tableRowGeneral, relative_generalTableBodyCell, entry.getKey());
                clickToggle(Select.selector(row, relative_generalCheckbox), entry.getValue().getRequired());
                enterInInput(Select.selector(row, relative_generalInput), entry.getValue().getId());
            }
            clickSaveElementIdModal();
        }
    }

    @Step("Assert Native data on the edit page")
    public void assertNativeEditPage(PlacementDO data) {
        Map<String, Boolean> dataAssetTypeMap = new LinkedHashMap<>(), imageTypeMap = new LinkedHashMap<>();
        data.getDataAssetsMaps().forEach((k, v) -> dataAssetTypeMap.put(k.publicName(), v));
        data.getAdImagesMap().forEach((k, v) -> imageTypeMap.put(k.publicName(), v));
        String dataAssetTypePlaceholder = getExpectedMultiselectText(dataAssetTypeMap, "Select...", 10);
        String adImagesPlaceholder = getExpectedMultiselectText(imageTypeMap, "Select...", 3);
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), data.getName(), "Name is incorrect");
        softAssert.assertEquals(Select.selector(sizeWidthInput).getAttribute("value"), data.getSize().getWidth().toString(), "Width is incorrect");
        softAssert.assertEquals(Select.selector(sizeHeightInput).getAttribute("value"), data.getSize().getHeight().toString(), "Height is incorrect");
        softAssert.assertEquals(Select.selector(bidPriceSelect, relative_selectLabel).getText(), data.getBidPrice().getType().publicName(), "Bid price type is incorrect");
        softAssert.assertEquals(Select.selector(bidPriceInput).getAttribute("value"), FormatUtil.formatPriceNoZeros(data.getBidPrice().getValue()), "Bid price is incorrect");
        softAssert.assertEquals(Select.selector(testToggle).isSelected(), data.getTestMode(), "Test mode toggle is incorrect");
        softAssert.assertEquals(Select.selector(adEnvironmentSelect, relative_selectLabel).getText(), data.getAdEnvironment().publicName(), "Ad Enviroment type is incorrect");
        softAssert.assertEquals(Select.selector(advancedAdSettingsSelect, relative_selectLabel).getText(), data.getAdvancedAdSettings().publicName(), "Advanced Ad Settings type is incorrect");
        softAssert.assertEquals(Select.selector(adUnitOptionsSelect, relative_selectLabel).getText(), data.getAdUnit().publicName(), "Ad Unit Options type is incorrect");
        softAssert.assertEquals(Select.selector(adTitleToggle).isSelected(), data.getAdTitleStatus(), "Ad Title toggle is incorrect");
        if (data.getAdTitleStatus()) {
            softAssert.assertEquals(Select.selector(adTitleInput).getAttribute("value"), data.getAdTitleLength().toString(), "Ad Title Length is incorrect");
        }
        softAssert.assertEquals(Select.selector(dataAssetTypeSelect, relative_selectLabel).getText(), dataAssetTypePlaceholder, "Data Asset Type placeholder text is incorrect");
        if (data.getDataAssetsMaps().containsKey(placementDataAssetTypeEnum.DESC)) {
            if (data.getDataAssetsMaps().get(placementDataAssetTypeEnum.DESC)) {
                softAssert.assertEquals(Select.selector(descriptionLengthInput).getAttribute("value"), data.getDescriptionLength().toString(), "Description Length is incorrect");
            }
        }
        softAssert.assertEquals(Select.selector(imageTypeSelect, relative_selectLabel).getText(), adImagesPlaceholder, "Image Type placeholder text is incorrect");
        if (data.getAdImagesMap().containsKey(placementImageTypeEnum.MAIN)) {
            if (data.getAdImagesMap().get(placementImageTypeEnum.MAIN)) {
                softAssert.assertEquals(Select.selector(minWidthInput).getAttribute("value"), data.getNativeMainImageSize().getWidth().toString(), "Min Width is incorrect");
                softAssert.assertEquals(Select.selector(minHeightInput).getAttribute("value"), data.getNativeMainImageSize().getHeight().toString(), "Min Height is incorrect");
            }
        }
        clickSetSelfIdsForElementsButton();
        if (!data.getElementsIdMap().isEmpty()) {
            for (Entry<String, PlacementDO.SelfId> entry : data.getElementsIdMap().entrySet()) {
                WebElement row = Select.selectParentByTextEquals(tableRowGeneral, relative_generalTableBodyCell, entry.getKey());
                if (row != null) {
                    softAssert.assertEquals(Select.selector(row, relative_generalCheckbox).isSelected(), entry.getValue().getRequired(), "Required toggle is incorrect");
                    softAssert.assertEquals(Select.selector(row, relative_generalInput).getAttribute("value"), entry.getValue().getIdForAssertion().toString(), "Id is incorrect");
                } else {
                    softAssert.fail("Element with name: " + entry.getKey() + " is not found");
                }
            }
            Select.actionMouseScrollClick(Select.selector(selfIdModal, modalCloseButton));
        }
    }

    @Step("Assert Validation on the Native edit page")
    public void assertValidationNativeEditPage(String namePlacement, String width, String height, String bidPrice) {
        if (namePlacement != null) {
            softAssert.assertEquals(Select.selector(nameInput, relative_selectValidationLabel).getAttribute("data-error"), namePlacement, "Validation: Required Placement name is incorrect");
        }
        if (width != null) {
            softAssert.assertEquals(Select.selector(sizeWidthInput, relative_selectValidationLabel).getAttribute("data-error"), width, "Validation: Required Width is incorrect");
        }
        if (height != null) {
            softAssert.assertEquals(Select.selector(sizeHeightInput, relative_selectValidationLabel).getAttribute("data-error"), height, "Validation: Required Height is incorrect");
        }
        if (bidPrice != null) {
            softAssert.assertEquals(Select.selector(bidPriceInput, relative_selectValidationLabel).getAttribute("data-error"), bidPrice, "Validation: Required Bid Price is incorrect");
        }
    }

    //</editor-fold>

    //</editor-fold>

}
