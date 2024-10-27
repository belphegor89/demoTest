package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.AssertUtility;
import common.utils.ParseAndFormatUtility;
import common.utils.RandomUtility;
import data.CommonEnums.auctionTypeEnum;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

import static data.textstrings.messages.CommonText.EMPTY_DASHBOARD;

public class DealsPublisherPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final ParseAndFormatUtility ParseFormatUtil = new ParseAndFormatUtility();
    private final RandomUtility RandomUtil = new RandomUtility();

    public DealsPublisherPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public DealsPublisherPO(){
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Publisher Deals page")
    public void gotoDealsPublisherSection(){
        Select.selector(dealsPublisherSectionButton).click();
        Wait.waitForClickable(addDealButton);
        Wait.attributeContains(preloaderTable, "class", "hide");
    }

    //<editor-fold desc="Deals list">
    private static final By
            addDealButton = By.xpath("//a[contains(@href, 'create')]"),
            dealTableRow = tableRowGeneral,
            dealTableSearchInput = By.xpath("//input[@data-param='hash']"),
            dealTableIdCell = By.xpath("./td[@data-field='id']"),
            dealTableStatusToggle = By.xpath("./td[@data-field='status']//input"),
            dealTableNameCell = By.xpath("./td[@data-field='title']"),
            dealTableHashCell = By.xpath("./td[@data-field='hash']"),
            dealTablePriceCell = By.xpath("./td[@data-field='cpm']"),
            dealTableAuctionCell = By.xpath("./td[@data-field='at']"),
            dealTableImpressionsCell = By.xpath("./td[@data-field='impressions']"),
            dealTableRevenueCell = By.xpath("./td[@data-field='revenue']"),
            dealTableEditButton = By.xpath("./td[@data-field='actions']//a[not(@data-id)]"),
            dealTableDeleteButton = By.xpath("./td[@data-field='actions']//a[@data-id]");

    @Step("Get deal row by name")
    public WebElement getDealRow(String dealName){
        WebElement deal = Select.selectParentByTextEquals(dealTableRow, dealTableNameCell, dealName);
        hardAssert.assertNotNull(deal, "There is no publisher deal with the name <" + dealName + ">");
        return deal;
    }

    @Step("Search deal")
    public void searchDeal(String dealHash) {
        WebElement dealRowOld = Select.selector(dealTableRow);
        searchItemInTable(dealHash, dealTableSearchInput, tableBlock);
        Wait.waitForStaleness(dealRowOld);
    }

    @Step("Click Create Deal button")
    public void clickCreateDeal(){
        Select.selector(addDealButton).click();
        Wait.waitForClickable(saveExitButton);
    }

    @Step("Open deal")
    public void clickEditDeal(String dealName){
        WebElement deal = getDealRow(dealName);
        Select.selector(deal, dealTableEditButton).click();
        Wait.waitForClickable(saveExitButton);
    }

    @Step("Get deal hash from table")
    public String getHashFromTable(String dealName) {
        WebElement deal = getDealRow(dealName);
        return Select.selector(deal, dealTableHashCell).getText();
    }

    @Step("Assert deal in table")
    public void assertDealRow(String name, boolean status, Double floorPrice, auctionTypeEnum auction, Integer impressionsCap, Double revenueCap){
        WebElement dealRow = getDealRow(name);
        String hashExpected = "DSP-[a-zA-Z0-9]{3}-[a-zA-Z0-9]{15}";
        softAssert.assertEquals(Select.selector(dealRow, dealTableStatusToggle).isSelected(), status, "Table: Status is not correct");
        softAssert.assertEquals(Select.selector(dealRow, dealTableNameCell).getText(), name, "Table: Title is not correct");
        softAssert.assertTrue(Select.selector(dealRow, dealTableHashCell).getText().matches(hashExpected), "Table: Hash is not correct");
        softAssert.assertEquals(Select.selector(dealRow, dealTablePriceCell).getText(), floorPrice.toString(), "Table: Floor price is not correct");
        softAssert.assertEquals(Select.selector(dealRow, dealTableAuctionCell).getText(), auction.publicName(), "Table: Auction type is not correct");
        softAssert.assertEquals(Select.selector(dealRow, dealTableImpressionsCell).getText(), impressionsCap.toString(), "Table: Impressions is not correct");
        softAssert.assertEquals(Select.selector(dealRow, dealTableRevenueCell).getText(), ParseFormatUtil.formatPriceNoZeros(revenueCap), "Table: Revenue is not correct");
        if (isSoftAssertLocal) {
        softAssert.assertAll("Deal row is incorrect: ");
        }
    }

    @Step("Assert search results")
    public void assertSearchResults(String dealHash) {
        if (dealHash != null) {
            for (WebElement dealRow : Select.multiSelector(dealTableRow)) {
                String actualHash = Select.selector(dealRow, dealTableHashCell).getText();
                softAssert.assertTrue(actualHash.contains(dealHash), "Search results are not correct. Deal [" + actualHash + "] does not contain [" + dealHash + "]");
            }
        } else {
            softAssert.assertTrue(AssertUtil.assertNotPresent(dealTableRow), "Search results are not empty");
            softAssert.assertEquals(Select.selector(tableEmptyMassageGeneral).getText(), EMPTY_DASHBOARD, "Empty search results text is incorrect");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Search results are incorrect: ");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Deal settings">
    private static final By
            editPageHashLine = By.xpath("//div//strong"),
            floorPriceInput = By.xpath("//input[@id='cpm']"),
            placementsSelect = By.xpath("//select[@id='placement-ids']"),
            auctionSelect = By.xpath("//select[@id='at']"),
            editPageStatusToggle = By.xpath("//input[@name='status']"),
            impressionsInput = By.xpath("//input[@id='impressions']"),
            revenueInput = By.xpath("//input[@id='revenue']");

    @Step("Input deal name")
    public void inputName(String name){
        enterInInput(titleInput, name);
    }

    @Step("Get random placements from list")
    public Map<String, Boolean> selectPlacements(boolean selectAll){
        Map<String, Boolean> placementsMap = new HashMap<>();
        Select.selector(placementsSelect, relative_selectLabel).click();
        if (selectAll){
            clickDropdownMultiselectOption(placementsSelect, null, true);
        } else {
            int placementsCount = Select.multiSelector(placementsSelect, relative_selectOptionWithTitle).size(), count = RandomUtil.getRandomInt(1, placementsCount);
            placementsMap = getRandomActiveMultiselectOptionsAsMap(placementsSelect, count);

            for (Map.Entry<String, Boolean> placementEntry : placementsMap.entrySet()){
                clickDropdownMultiselectOption(placementsSelect, placementEntry.getKey(), placementEntry.getValue());
            }
        }
        closeMultiselectList(placementsSelect);
        return placementsMap;
    }

    @Step("Input Floor price")
    public void inputFloorPrice(Double price){
        enterInInput(floorPriceInput, price);
    }

    @Step("Select Auction type")
    public void selectAuctionType(auctionTypeEnum type){
        selectSingleSelectDropdownOptionByName(auctionSelect, type.publicName());
    }

    @Step("Input Impressions")
    public void inputImpressions(Integer impressions){
        enterInInput(impressionsInput, impressions);
    }

    @Step("Input Revenue")
    public void inputRevenue(Double revenue){
        enterInInput(revenueInput, revenue);
    }

    @Step("Click Save Deal button")
    public void clickSaveExitDeal(){
        Select.selector(saveExitButton).click();
        Wait.waitForClickable(addDealButton);
        Wait.attributeContains(preloaderTable, "class", "hide");
    }

    @Step("Change deal status")
    public void clickDealStatus(boolean status){
        clickToggle(editPageStatusToggle, status);
    }

    @Step("Assert Deal edit page")
    public void assertDealEditPage(String name, Boolean status, String hash, Map<String, Boolean> placementsMap, Double floorPrice, auctionTypeEnum auctionType, Integer impressionsCap, Double revenueCap){
        String placementsExpected = getExpectedMultiselectText(placementsMap, "All Placements", 10);
        softAssert.assertEquals(Select.selector(titleInput).getAttribute("value"), name, "Deal edit: Name is not correct");
        softAssert.assertEquals(Select.selector(placementsSelect, relative_selectLabel).getText(), placementsExpected, "Deal edit: placements list is not correct");
        softAssert.assertEquals(Select.selector(editPageStatusToggle).isSelected(), status.booleanValue(), "Deal edit: status is not correct");
        softAssert.assertEquals(Select.selector(editPageHashLine).getText(), hash, "Deal edit: Hash is not correct");
        softAssert.assertEquals(Select.selector(floorPriceInput).getAttribute("value"), floorPrice.toString(), "Deal edit: floor price is not correct");
        softAssert.assertEquals(Select.selector(auctionSelect, relative_selectLabel).getText(), auctionType.publicName(), "Deal edit: auction type is not correct");
        softAssert.assertEquals(Select.selector(impressionsInput).getAttribute("value"), impressionsCap.toString(), "Deal edit: Impressions setting is not correct");
        softAssert.assertEquals(Select.selector(revenueInput).getAttribute("value"), ParseFormatUtil.formatPriceNoZeros(revenueCap), "Deal edit: Revenue setting is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Deal edit page is incorrect: ");
        }
    }

    @Step("Click on Delete deal button")
    public void clickDeleteDeal(String dealName, boolean deleteConfirm){
        WebElement dealRow = getDealRow(dealName);
        Select.selector(dealRow, dealTableDeleteButton).click();
        Wait.waitForClickable(modalConfirmButton);
        softAssert.assertEquals(Select.selector(modalHeaderText).getText(), "Do you want to delete the deal?", "Delete modal: header text is incorrect");
        if (deleteConfirm){
            Select.selector(modalConfirmButton).click();
            Wait.waitForStaleness(dealRow);
        } else {
            Select.selector(modalCancelButton).click();
            Wait.waitForNotVisible(modalDialog);
        }
    }

    //</editor-fold>

}
