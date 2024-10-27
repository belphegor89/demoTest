package suites;

import common.SoftAssertCustom;
import common.utils.RandomUtility;
import data.CommonEnums.auctionTypeEnum;
import data.StaticData;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.DealsPublisherPO;

import java.util.Map;

@Epic("Deals Publisher")
public class DealsPublisher extends BaseSuiteClassNew {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("4441")
    @Description("Create publisher deal")
    public void createDealPublisher(){
        AuthorizationPO login = new AuthorizationPO();
        DealsPublisherPO deal = new DealsPublisherPO();
        RandomUtility randomUtil = new RandomUtility();
        String dealName = "Deal Publisher " + randomUtil.getRandomInt(1, 10000), hash;
        int dealImpressions = randomUtil.getRandomInt(1, 999999999);
        double dealFloorPrice = randomUtil.getRandomDoubleRounded(1, 22220, 2), dealRevenue = randomUtil.getRandomInt(1, 22220);
        auctionTypeEnum auction = (auctionTypeEnum) randomUtil.getRandomElement(auctionTypeEnum.values());
        Map<String, Boolean> placementsMap;
        login.login(StaticData.supportDefaultUser);
        deal.gotoDealsPublisherSection();
        deal.clickCreateDeal();
        deal.inputName(dealName);
        placementsMap = deal.selectPlacements(false);
        deal.inputFloorPrice(dealFloorPrice);
        deal.selectAuctionType(auction);
        deal.inputImpressions(dealImpressions);
        deal.inputRevenue(dealRevenue);
        deal.clickSaveExitDeal();
        deal.assertDealRow(dealName, false, dealFloorPrice, auction, dealImpressions, dealRevenue);
        hash = deal.getHashFromTable(dealName);
        deal.clickEditDeal(dealName);
        deal.assertDealEditPage(dealName, false, hash, placementsMap, dealFloorPrice, auction, dealImpressions, dealRevenue);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("39444")
    @Description("Edit publisher deal")
    public void editDealPublisher(){
        AuthorizationPO login = new AuthorizationPO();
        DealsPublisherPO deal = new DealsPublisherPO();
        RandomUtility randomUtil = new RandomUtility();
        String dealNameOld = "Deal Publisher " + randomUtil.getRandomInt(1, 100), dealNameNew = dealNameOld + "_EDIT", hash;
        int dealImpressionsOld = randomUtil.getRandomInt(1, 999999999), dealImpressionsNew = randomUtil.getRandomInt(1, 999999999);
        double dealFloorPriceOld = randomUtil.getRandomDoubleRounded(1, 22220, 2), dealRevenueOld = randomUtil.getRandomInt(1, 22220), dealFloorPriceNew = randomUtil.getRandomDoubleRounded(1, 22220, 2), dealRevenueNew = randomUtil.getRandomInt(1, 22220);
        auctionTypeEnum auctionOld = auctionTypeEnum.SECOND, auctionNew = auctionTypeEnum.FIRST;
        Map<String, Boolean> placementsMapNew;
        login.login(StaticData.supportDefaultUser);
        deal.gotoDealsPublisherSection();
        deal.clickCreateDeal();
        deal.inputName(dealNameOld);
        deal.inputFloorPrice(dealFloorPriceOld);
        deal.selectAuctionType(auctionOld);
        deal.inputImpressions(dealImpressionsOld);
        deal.inputRevenue(dealRevenueOld);
        deal.clickSaveExitDeal();
        hash = deal.getHashFromTable(dealNameOld);
        deal.clickEditDeal(dealNameOld);
        deal.clickDealStatus(true);
        deal.inputName(dealNameNew);
        deal.selectPlacements(true);
        placementsMapNew = deal.selectPlacements(false);
        deal.inputFloorPrice(dealFloorPriceNew);
        deal.selectAuctionType(auctionNew);
        deal.inputImpressions(dealImpressionsNew);
        deal.inputRevenue(dealRevenueNew);
        deal.clickSaveExitDeal();
        deal.assertDealRow(dealNameNew, true, dealFloorPriceNew, auctionNew, dealImpressionsNew, dealRevenueNew);
        deal.clickEditDeal(dealNameNew);
        deal.assertDealEditPage(dealNameNew, true, hash, placementsMapNew, dealFloorPriceNew, auctionNew, dealImpressionsNew, dealRevenueNew);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("39453")
    @Description("Edit publisher deal")
    public void deleteDealPublisher(){
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DealsPublisherPO deal = new DealsPublisherPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        String dealName = "Deal Publisher " + randomUtil.getRandomInt(1, 10000), hash;
        int dealImpressions = randomUtil.getRandomInt(1, 999999999);
        double dealFloorPrice = randomUtil.getRandomDoubleRounded(1, 22220, 2), dealRevenue = randomUtil.getRandomInt(1, 22220);
        auctionTypeEnum auction = (auctionTypeEnum) randomUtil.getRandomElement(auctionTypeEnum.values());
        login.login(StaticData.supportDefaultUser);
        deal.gotoDealsPublisherSection();
        deal.clickCreateDeal();
        deal.inputName(dealName);
        deal.selectPlacements(false);
        deal.inputFloorPrice(dealFloorPrice);
        deal.selectAuctionType(auction);
        deal.inputImpressions(dealImpressions);
        deal.inputRevenue(dealRevenue);
        deal.clickSaveExitDeal();
        hash = deal.getHashFromTable(dealName);
        deal.clickDeleteDeal(dealName, true);
        deal.searchDeal(hash);
        deal.assertSearchResults(null);
        softAssert.assertAll("Delete deal failed");
    }

}
