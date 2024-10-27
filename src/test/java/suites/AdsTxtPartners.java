package suites;

import common.SoftAssertCustom;
import common.utils.RandomUtility;
import data.CommonEnums;
import data.StaticData;
import data.dataobject.EndpointSupplyDO;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AdsTxtPartnersPO;
import pages.AuthorizationPO;
import pages.InventoryPO;
import pages.SupplyPO;

import java.io.File;
import java.util.List;
import java.util.Map;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("Ads Txt Partners")
public class AdsTxtPartners extends BaseSuiteClassNew {

    //<editor-fold desc="Ads Txt Partners">
    @Test
    @TmsLink("58233")
    @Description("Create Ads Txt Partners as Admin")
    public void createAdsTxtPartners() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        AdsTxtPartnersPO adsTxtPartner = new AdsTxtPartnersPO(softAssert);
        InventoryPO inventory = new InventoryPO(softAssert);
        SupplyPO ssp = new SupplyPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        String inventoryName;
        int rnd = randomUtil.getRandomInt(0, 1000);
        String inventoryDomain;
        List<String> inventoryNames;
        String sellerName = "Seller - " + rnd,
                sellerId = "9" + rnd,
                sellerDomain = "domain2.com",
                hashKey = "TeSt" + rnd + "ABc" + rnd + "dEf102#",
                sellerComment = rnd + " C" + rnd + " Comment";
        Map<String, Boolean> dspMap;
        login.login(StaticData.supportDefaultUser);
        adsTxtPartner.gotoAdsTxtPartnersSection();
        adsTxtPartner.clickAddAdsTxtPartnerButton();
        adsTxtPartner.inputSellerId(sellerId);
        adsTxtPartner.inputName(sellerName);
        adsTxtPartner.inputDomain(sellerDomain);
        adsTxtPartner.inputComment(sellerComment);
        adsTxtPartner.inputHashKey(hashKey);
        dspMap = adsTxtPartner.selectDsps(false);
        adsTxtPartner.clickRadiobutton(CommonEnums.directnessTypeEnum.DIRECT);
        adsTxtPartner.clickSaveSeller();
        adsTxtPartner.lookForAdsTxtPartner(sellerId, sellerName);
        adsTxtPartner.assertAdsTxtPartnerRow(sellerId, sellerName, sellerDomain, CommonEnums.directnessTypeEnum.DIRECT.publicName(), sellerComment, dspMap.size());
        adsTxtPartner.clickEditAdsTxtPartnerButton(sellerId);
        adsTxtPartner.assertSellerDetail(sellerId, sellerName, sellerDomain, sellerComment, CommonEnums.directnessTypeEnum.DIRECT.publicName(), hashKey, dspMap);
        inventory.gotoInventories();
        inventoryNames = inventory.getInventoryName(true, inventory.getUserIdHeader());
        inventoryName = (String) randomUtil.getRandomElement(inventoryNames);
        inventoryDomain = inventory.getInventoryDomain(inventoryName);
        inventory.searchInventoryPlacement(inventoryName);
        File adsTxt;
        adsTxt = inventory.clickDownloadAdsTxt(inventoryName);
        inventory.assertAdsTxtRequired(adsTxt, inventoryDomain, sellerId, CommonEnums.directnessTypeEnum.DIRECT.publicName(), sellerDomain, hashKey);
        ssp.gotoSupplySection();
        EndpointSupplyDO endpointData;
        endpointData = ssp.getRandomEndpointRowInfo();
        adsTxt = ssp.clickDownloadAdsTxt(endpointData.getId());
        ssp.assertAdsTxtRequired(adsTxt, sellerId, CommonEnums.directnessTypeEnum.DIRECT);
        softAssert.assertAll("Errors in Seller [" + sellerName + "] #" + sellerId);
    }

    @Test
    @TmsLink("58233")
    @Description("Edit Ads Txt Partners as Admin")
    public void editAdsTxtPartners() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdsTxtPartnersPO adsTxtPartner = new AdsTxtPartnersPO(softAssert);
        InventoryPO inventory = new InventoryPO(softAssert);
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(0, 100), userId;
        String inventoryName;
        List<String> inventoryNames;
        String inventoryDomain;
        String sellerNameOld = "Seller EDIT before-" + rnd, sellerNameNew = "Seller - " + rnd,
                sellerDomainOld = "test.com", sellerDomainNew = "domain.com",
                hashKeyOld = "TeSt" + rnd + "ABc" + rnd + "dEf10", hashKeyNew = "TeSt" + rnd + "ABc" + rnd + "dEf10",
                sellerIdOld = rnd + "890", sellerIdNew = rnd + "12",
                sellerCommentOld = rnd + " Old" + rnd + " Comment", sellerCommentNew = rnd + " C" + rnd + " Comment";
        Map<String, Boolean> dspMap;
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        adsTxtPartner.gotoAdsTxtPartnersSection();
        adsTxtPartner.clickAddAdsTxtPartnerButton();
        adsTxtPartner.inputSellerId(sellerIdOld);
        adsTxtPartner.inputName(sellerNameOld);
        adsTxtPartner.inputDomain(sellerDomainOld);
        adsTxtPartner.inputComment(sellerCommentOld);
        adsTxtPartner.inputHashKey(hashKeyOld);
        dspMap = adsTxtPartner.selectDsps(false);
        adsTxtPartner.clickRadiobutton(CommonEnums.directnessTypeEnum.RESELLER);
        adsTxtPartner.clickSaveSeller();
        adsTxtPartner.lookForAdsTxtPartner(sellerIdOld, sellerNameOld);
        adsTxtPartner.assertAdsTxtPartnerRow(sellerIdOld, sellerNameOld, sellerDomainOld, CommonEnums.directnessTypeEnum.RESELLER.publicName(), sellerCommentOld, dspMap.size());
        adsTxtPartner.clickEditAdsTxtPartnerButton(sellerIdOld);
        adsTxtPartner.assertSellerDetail(sellerIdOld, sellerNameOld, sellerDomainOld, sellerCommentOld, CommonEnums.directnessTypeEnum.DIRECT.publicName(), hashKeyOld, dspMap);
        adsTxtPartner.inputSellerId(sellerIdNew);
        adsTxtPartner.inputName(sellerNameNew);
        adsTxtPartner.inputDomain(sellerDomainNew);
        adsTxtPartner.inputComment(sellerCommentNew);
        adsTxtPartner.inputHashKey(hashKeyNew);
        adsTxtPartner.clickRadiobutton(CommonEnums.directnessTypeEnum.DIRECT);
        adsTxtPartner.clickSaveSeller();
        adsTxtPartner.lookForAdsTxtPartner(sellerIdNew, sellerNameNew);
        adsTxtPartner.assertAdsTxtPartnerRow(sellerIdNew, sellerNameNew, sellerDomainNew, CommonEnums.directnessTypeEnum.DIRECT.publicName(), sellerCommentNew, dspMap.size());
        adsTxtPartner.clickEditAdsTxtPartnerButton(sellerIdNew);
        adsTxtPartner.assertSellerDetail(sellerIdNew, sellerNameNew, sellerDomainNew, sellerCommentNew, CommonEnums.directnessTypeEnum.DIRECT.publicName(), hashKeyNew, dspMap);
        inventory.gotoInventories();
        inventoryNames = inventory.getInventoryName(true, userId);
        inventoryName = (String) randomUtil.getRandomElement(inventoryNames);
        inventoryDomain = inventory.getInventoryDomain(inventoryName);
        inventory.searchInventoryPlacement(inventoryName);
        File adsTxt;
        adsTxt = inventory.clickDownloadAdsTxt(inventoryName);
        inventory.assertAdsTxtRequired(adsTxt, inventoryDomain, sellerIdNew, CommonEnums.directnessTypeEnum.DIRECT.publicName(), sellerDomainNew, hashKeyNew);
        ssp.gotoSupplySection();
        EndpointSupplyDO endpointData;
        endpointData = ssp.getRandomEndpointRowInfo();
        adsTxt = ssp.clickDownloadAdsTxt(endpointData.getId());
        ssp.assertAdsTxtRequired(adsTxt, sellerIdNew, CommonEnums.directnessTypeEnum.DIRECT);
        softAssert.assertAll("Errors in Seller [" + sellerNameNew + "] #" + sellerIdNew);
    }

    @Test
    @TmsLink("63014")
    @Description("Delete Ads Txt Partners as Admin")
    public void deleteAdsTxtPartners() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdsTxtPartnersPO adsTxtPartner = new AdsTxtPartnersPO(softAssert);
        InventoryPO inventory = new InventoryPO(softAssert);
        SupplyPO ssp = new SupplyPO(softAssert);
        String inventoryName;
        int rnd = randomUtil.getRandomInt(0, 100), userId;
        String inventoryDomain;
        List<String> inventoryNames;
        String sellerName = "Seller Name- " + rnd,
                sellerId = "84567" + rnd,
                sellerDomain = "test.com",
                hashKey = "TeSt" + rnd + "ABc" + rnd + "dEf102#",
                sellerComment = rnd + " C" + rnd + " Comment";
        Map<String, Boolean> dspMap;
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        adsTxtPartner.gotoAdsTxtPartnersSection();
        adsTxtPartner.clickAddAdsTxtPartnerButton();
        adsTxtPartner.inputSellerId(sellerId);
        adsTxtPartner.inputName(sellerName);
        adsTxtPartner.inputDomain(sellerDomain);
        adsTxtPartner.inputComment(sellerComment);
        adsTxtPartner.inputHashKey(hashKey);
        dspMap = adsTxtPartner.selectDsps(false);
        adsTxtPartner.clickRadiobutton(CommonEnums.directnessTypeEnum.BOTH);
        adsTxtPartner.clickSaveSeller();
        adsTxtPartner.lookForAdsTxtPartner(sellerId, sellerName);
        adsTxtPartner.assertAdsTxtPartnerRow(sellerId, sellerName, sellerDomain, CommonEnums.directnessTypeEnum.BOTH.publicName(), sellerComment, dspMap.size());
        adsTxtPartner.clickDeleteAdsTxtPartnerButton(sellerName, sellerId, true);
        adsTxtPartner.assertDeletedSellerNameRow(sellerName, sellerId);
        inventory.gotoInventories();
        inventoryNames = inventory.getInventoryName(true, userId);
        inventoryName = (String) randomUtil.getRandomElement(inventoryNames);
        inventoryDomain = inventory.getInventoryDomain(inventoryName);
        inventory.searchInventoryPlacement(inventoryName);
        File adsTxt;
        adsTxt = inventory.clickDownloadAdsTxt(inventoryName);
        inventory.assertAdsTxtDeletedLine(adsTxt, inventoryDomain, sellerId, CommonEnums.directnessTypeEnum.BOTH.publicName(), sellerDomain, hashKey);
        ssp.gotoSupplySection();
        EndpointSupplyDO endpointData;
        endpointData = ssp.getRandomEndpointRowInfo();
        adsTxt = ssp.clickDownloadAdsTxt(endpointData.getId());
        inventory.assertAdsTxtDeletedLine(adsTxt, inventoryDomain, sellerId, CommonEnums.directnessTypeEnum.BOTH.publicName(), sellerDomain, hashKey);
        softAssert.assertAll("Errors in Seller [" + sellerName + "] #" + sellerId);
    }


}

