package pages;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.BasicUtility;
import common.utils.FileUtility;
import data.SellersAdsEnums;
import data.UserEnum;
import data.dataobject.SellerJsonDO;
import data.jsonmodels.SellersJsonModel;
import io.qameta.allure.Step;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class SellersJsonPO extends CommonElementsPO {
    private final boolean isSoftAssertLocal;
    private final SoftAssertCustom softAssert;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final BasicUtility Utils = new BasicUtility();
    private final FileUtility FileUtil = new FileUtility();

    public SellersJsonPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public SellersJsonPO(){
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Sellers.json page")
    public void gotoSellersJsonSection(){
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(sellersJsonLink).click();
        Wait.waitForClickable(saveFileButton);
        Wait.attributeNotContains(sellersListTable, "class", "hide");
    }

    private static final By
            autoUpdateCheckbox = By.xpath("//input[@id='autoReportCheckBox']"),
            downloadFileButton = By.xpath("//a[@id='downloadSellerJson']"),
            updateFileButton = By.xpath("//a[@id='generateUpdateSellersJson']"),
            saveFileButton = By.xpath("//a[@id='updateSellerJson']"),
            searchInput = By.xpath("//input[@id='search-by-name']"),
            sellersListTable = By.xpath("//div[@id='tableSellerJsonManage']//table"),
            sellersListRow = By.xpath("//div[@id='tableSellerJsonManage']//tbody//tr"),
            sellersListTypeCell = By.xpath("./td[@data-field='type']"),
            sellersListIdCell = By.xpath("./td[@data-field='seller_orig_id']"),
            sellersListNameCell = By.xpath("./td[@data-field='name']"),
            sellersListDomainCell = By.xpath("./td[@data-field='domain']"),
            sellersListSellerTypeCell = By.xpath("./td[@data-field='seller_type']"),
            sellersListConfidentialityCell = By.xpath("./td[@data-field='is_confidentiality']"),
            sellersListHideCheckbox = By.xpath("./td[@data-field='hide']//input"),
            sellersListCellValue = By.xpath(".//span"),
            sellersListCellInput = By.xpath(".//input"),
            sellersListCellDropdown = By.xpath(".//select");

    @Step("Get sellers.json table row")
    public WebElement getSellersTableRow(String sellerId){
        WebElement row = Select.selectParentByTextEquals(sellersListRow, sellersListIdCell, sellerId);
        hardAssert.assertNotNull(row, "There is no record with the ID <" + sellerId + ">");
        return row;
    }

    @Step("Select Auto-update checkbox")
    public void selectAutoupdateCheckbox(boolean status){
        if (status != Select.selector(autoUpdateCheckbox).isSelected()){
            Select.selector(autoUpdateCheckbox).click();
        }
    }

    @Step("Click Download button")
    public File clickDownloadButton(){
        Select.selector(downloadFileButton).click();
        Wait.waitForTabClose();
        return FileUtil.getDownloadedFile("sellers.json", 15);
    }

    @Step("Click Update button")
    public void clickUpdateButton(){
        Select.selector(updateFileButton).click();
        Wait.waitForVisibility(modalDialog);
        Wait.waitForClickable(modalConfirmButton);
        Select.selector(modalConfirmButton).click();
        Wait.waitForNotVisible(modalDialog);
    }

    @Step("Click Save button")
    public void clickSaveButton(){
        Select.selector(saveFileButton).click();
        Wait.waitForVisibility(modalDialog);
        Wait.waitForClickable(saveFileButton);
        Wait.waitForNotVisible(modalDialog);
        Wait.attributeNotContains(sellersListTable, "class", "hide");
    }

    @Step("Search in Sellers.json table")
    public void searchSellersJsonTable(String search){
        searchItemInTable(search, searchInput, sellersListTable);
    }

    @Step("Set record's confidentiality")
    public void setConfidentiality(String sellerId, SellersAdsEnums.sellerConfidentialityEnum confidentiality){
        WebElement row = getSellersTableRow(sellerId), cell = Select.selector(row, sellersListConfidentialityCell);
        Select.selector(cell, sellersListCellValue).click();
        selectSingleSelectDropdownOption(Select.selector(cell, sellersListCellDropdown), confidentiality.publicName());
    }

    @Step("Assert sellers.json record in table")
    public void assertSellersJsonTableRecord(UserEnum.userRoleEnum userType, String sellerId, String name, String domain, SellersAdsEnums.sellerTypeEnum sellerType, SellersAdsEnums.sellerConfidentialityEnum confidentiality, boolean hide){
        WebElement row = getSellersTableRow(sellerId);
        softAssert.assertEquals(Select.selector(row, sellersListTypeCell).getText(), userType.publicName(), "Record type is not correct");
        softAssert.assertEquals(Select.selector(row, sellersListNameCell).getText(), name, "Record Seller name is not correct");
        softAssert.assertEquals(Select.selector(row, sellersListDomainCell).getText(), domain, "Record domain is not correct");
        softAssert.assertEquals(Select.selector(row, sellersListSellerTypeCell).getText(), sellerType.name(), "Record Seller type is not correct");
        softAssert.assertEquals(Select.selector(row, sellersListConfidentialityCell).getText(), confidentiality.publicName(), "Record Confidentiality is not correct");
        softAssert.assertEquals(Select.selector(row, sellersListHideCheckbox).isSelected(), hide, "Record Hide status is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Record data in table is not correct");
        }
    }

    @Step("Assert sellers.json record in file")
    public void assertSellersJsonFileRecord(File sellersJson, boolean isHidden, boolean isConfidential, String sellerId, String name, String domain, SellersAdsEnums.sellerTypeEnum sellerType){
        String jsonFile = FileUtil.readFileToString(sellersJson);
        boolean found = false;
        try{
            ObjectMapper mapper = new ObjectMapper();
            SellersJsonModel jsonResponse = mapper.readValue(jsonFile, SellersJsonModel.class);
            List<SellersJsonModel.Seller> sellers = jsonResponse.getSellers();
            for (SellersJsonModel.Seller seller : sellers){
                if (seller.getSeller_id().equals(sellerId)){
                    found = true;
                    if (isHidden){
                        break;
                    }
                    if (isConfidential){
                        softAssert.assertEquals(seller.getIs_confidential(), 1, "Seller is not confidential");
                        softAssert.assertEquals(seller.getSeller_type(), sellerType.name(), "Seller type is not correct");
                    } else {
                        softAssert.assertEquals(seller.getName(), name, "Seller name is not correct");
                        softAssert.assertEquals(seller.getDomain(), domain, "Seller domain is not correct");
                        softAssert.assertEquals(seller.getSeller_type(), sellerType.name(), "Seller type is not correct");
                        break;
                    }
                }
            }
            if (isHidden){
                softAssert.assertFalse(found, "Seller with ID <" + sellerId + "> is found");
            } else {
                softAssert.assertTrue(found, "Seller with ID <" + sellerId + "> is not found");
            }
            softAssert.assertAll("Errors in asserting Sellers.json file");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Step("Assert sellers.json record in file")
    public void assertSellersJsonFileInfo(File sellersJson, String contactEmail, LocalDateTime lastUpdate){
        String jsonFile = FileUtil.readFileToString(sellersJson);
        try{
            ObjectMapper mapper = new ObjectMapper();
            SellersJsonModel jsonResponse = mapper.readValue(jsonFile, SellersJsonModel.class);
            softAssert.assertEquals(jsonResponse.getContact_email(), contactEmail, "Sellers.json contact email is not correct");
            softAssert.assertTrue(jsonResponse.getLast_update().startsWith(Utils.getDateTimeFormatted(lastUpdate, "yyy-MM-dd")), "Sellers.json last update is not correct");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //<editor-fold desc="Editor group">

    //</editor-fold>

    @Step("Get sellers setting from DB")
    public SellerJsonDO getSellerSettingFromDb(boolean isCompany, int id){
        String type = isCompany ? "company" : "publisher";
        Condition condition = DSL.field("id").equal(id).and(DSL.field("type").equal(type));
        Record data = dbUtils.getDbDataRecordFirst("seller_setting", condition);
        hardAssert.assertNotNull(data, "There is no record in the DB for the seller with type <" + type + "> and ID <" + id + ">");
        SellerJsonDO seller = new SellerJsonDO().setUserType(type).setUserId(id);
        seller.setSellerName(data.get("name", String.class))
                .setSellerType(SellersAdsEnums.sellerTypeEnum.valueOf(data.get("seller_type", String.class).toUpperCase()))
                .setIsConfidential(data.get("is_confidential", Boolean.class))
                .setIsHidden(data.get("is_hidden", Boolean.class));
        return seller;
    }

}
