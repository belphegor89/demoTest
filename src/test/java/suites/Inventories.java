package suites;

import common.SoftAssertCustom;
import common.utils.RandomUtility;
import data.CommonEnums;
import data.CommonEnums.bidPriceTypeEnum;
import data.CommonEnums.iabSizesEnum;
import data.dataobject.InventoryDO;
import data.dataobject.PlacementDO;
import data.InventoryEnum;
import data.InventoryEnum.adCategoryEnum;
import data.InventoryEnum.inventoryTypeEnum;
import data.PlacementEnum;
import data.PlacementEnum.*;
import data.StaticData;
import data.textstrings.messages.InventoryText;
import pages.AuthorizationPO;
import pages.InventoryPO;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.TmsLink;
import io.qameta.allure.TmsLinks;
import org.testng.annotations.Test;

import java.util.*;

import static data.StaticData.placementBackfillExample;

@Epic("Inventories")
public class Inventories extends BaseSuiteClassNew {

    //<editor-fold desc="Inventories">
    @Test
    @TmsLinks({@TmsLink("4463"), @TmsLink("4469"), @TmsLink("22048"), @TmsLink("412"), @TmsLink("418")})
    @Description("Create and Edit Web inventory as Admin")
    public void createEditWebInventory() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        int rnd = rndUtil.getRandomInt(1, 10000), userId;
        Map<String, Boolean> allowedDsp, blockedDsp;
        List<adCategoryEnum> categoriesSelectedOriginal = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5));
        List<adCategoryEnum> categoriesSelectedEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        List<adCategoryEnum> categoriesFilteredEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        InventoryDO dataOriginal = new InventoryDO(inventoryTypeEnum.WEB, "Web_inventory_test_" + rnd, "web." + rnd + ".domain.tst", categoriesSelectedOriginal)
                .setRonTraffic(rndUtil.getRandomBoolean())
                .setLanguage(Locale.of("ga"))
                .setCategoriesFiltered(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5))
                .setBlockedDomains("blocked1.com,blocked2.com,blocked3.com")
                .setStatus(InventoryEnum.inventoryStatusEnum.PENDING);
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        inventory.gotoInventories();
        inventory.clickCreateInventory();
        inventory.selectInventoryType(dataOriginal.getType());
        inventory.setupInventory(dataOriginal);
        allowedDsp = inventory.selectRandomAllowedDsp(3);
        blockedDsp = inventory.selectRandomBlockedDsp(3);
        dataOriginal.setBlockedDsp(blockedDsp).setAllowedDsp(allowedDsp);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataOriginal.getName());
        dataOriginal.setId(inventory.getInventoryId(dataOriginal.getName(), userId));
        inventory.assertInventoryRowInfo(dataOriginal);
        inventory.clickEditInventory(dataOriginal);
        inventory.assertInventoryEditPage(dataOriginal);
        InventoryDO dataEdit = dataOriginal.setName("Web_inventory_test_edit_" + rnd)
                .setBundleDomain("web." + rnd + ".edit.tst")
                .setRonTraffic(!dataOriginal.getRonTraffic())
                .setLanguage(Locale.of("om"))
                .setBlockedDomains("blockedit11.org,blockedit22.net,blockedit33.ga");
        //Remove 2 categories from selected and filtered lists and merge with new edit lists
        dataOriginal.getCategoriesSelected().remove(0);
        dataOriginal.getCategoriesSelected().remove(1);
        categoriesSelectedEdit.addAll(dataOriginal.getCategoriesSelected());
        dataEdit.setCategoriesFiltered(categoriesSelectedEdit);
        dataOriginal.getCategoriesFiltered().remove(0);
        dataOriginal.getCategoriesFiltered().remove(1);
        categoriesFilteredEdit.addAll(dataOriginal.getCategoriesFiltered());
        dataEdit.setCategoriesSelected(categoriesFilteredEdit);
        //set all original allowed and blocked DSP values to false and merge with new edit lists
        allowedDsp.forEach((k, v) -> allowedDsp.put(k, false));
        blockedDsp.forEach((k, v) -> blockedDsp.put(k, false));
        allowedDsp.putAll(inventory.selectRandomAllowedDsp(3));
        blockedDsp.putAll(inventory.selectRandomBlockedDsp(3));
        dataEdit.setAllowedDsp(allowedDsp).setBlockedDsp(blockedDsp);
        inventory.setupInventory(dataEdit);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataEdit.getName());
        inventory.assertInventoryRowInfo(dataEdit);
        inventory.clickEditInventory(dataEdit);
        inventory.assertInventoryEditPage(dataEdit);
        sAssert.assertAll("Errors for " + dataOriginal.getType() + " Inventory creation/editing #" + dataOriginal.getId());
    }

    @Test
    @TmsLinks({@TmsLink("4466"), @TmsLink("4470"), @TmsLink("22048"), @TmsLink("412"), @TmsLink("418")})
    @Description("Create and Edit CTV inventory as Admin")
    public void createEditCtvInventory() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        int rnd = rndUtil.getRandomInt(1, 10000), userId;
        Map<String, Boolean> allowedDsp, blockedDsp;
        List<adCategoryEnum> categoriesSelectedOriginal = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5));
        List<adCategoryEnum> categoriesSelectedEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        List<adCategoryEnum> categoriesFilteredEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        InventoryDO dataOriginal = new InventoryDO(inventoryTypeEnum.CTV, "CTV_inventory_test_" + rnd, "ctv." + rnd + ".domain.tst", categoriesSelectedOriginal)
                .setStoreUrl("http://ctv." + rnd + ".store.tst")
                .setRonTraffic(rndUtil.getRandomBoolean())
                .setLanguage(Locale.of("fr"))
                .setCategoriesFiltered(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5))
                .setBlockedDomains("blocked1.com,blocked2.com,blocked3.com")
                .setStatus(InventoryEnum.inventoryStatusEnum.PENDING);
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        inventory.gotoInventories();
        inventory.clickCreateInventory();
        inventory.selectInventoryType(dataOriginal.getType());
        inventory.setupInventory(dataOriginal);
        allowedDsp = inventory.selectRandomAllowedDsp(3);
        blockedDsp = inventory.selectRandomBlockedDsp(3);
        dataOriginal.setBlockedDsp(blockedDsp).setAllowedDsp(allowedDsp);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataOriginal.getName());
        dataOriginal.setId(inventory.getInventoryId(dataOriginal.getName(), userId));
        inventory.assertInventoryRowInfo(dataOriginal);
        inventory.clickEditInventory(dataOriginal);
        inventory.assertInventoryEditPage(dataOriginal);
        InventoryDO dataEdit = dataOriginal.setName("CTV_inventory_test_edit_" + rnd)
                .setBundleDomain("ctv." + rnd + ".edit.tst")
                .setStoreUrl("http://ctv." + rnd + ".edit.store.tst")
                .setRonTraffic(!dataOriginal.getRonTraffic())
                .setLanguage(Locale.of("lv"))
                .setBlockedDomains("blockedit11.org,blockedit22.net,blockedit33.ga");
        //Remove 2 categories from selected and filtered lists and merge with new edit lists
        dataOriginal.getCategoriesSelected().remove(0);
        dataOriginal.getCategoriesSelected().remove(1);
        categoriesSelectedEdit.addAll(dataOriginal.getCategoriesSelected());
        dataEdit.setCategoriesFiltered(categoriesSelectedEdit);
        dataOriginal.getCategoriesFiltered().remove(0);
        dataOriginal.getCategoriesFiltered().remove(1);
        categoriesFilteredEdit.addAll(dataOriginal.getCategoriesFiltered());
        dataEdit.setCategoriesSelected(categoriesFilteredEdit);
        //set all original allowed and blocked DSP values to false and merge with new edit lists
        allowedDsp.forEach((k, v) -> allowedDsp.put(k, false));
        blockedDsp.forEach((k, v) -> blockedDsp.put(k, false));
        allowedDsp.putAll(inventory.selectRandomAllowedDsp(3));
        blockedDsp.putAll(inventory.selectRandomBlockedDsp(3));
        dataEdit.setAllowedDsp(allowedDsp).setBlockedDsp(blockedDsp);
        inventory.setupInventory(dataEdit);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataEdit.getName());
        inventory.assertInventoryRowInfo(dataEdit);
        inventory.clickEditInventory(dataEdit);
        inventory.assertInventoryEditPage(dataEdit);
        sAssert.assertAll("Errors for " + dataOriginal.getType() + " Inventory creation/editing #" + dataOriginal.getId());
    }

    @Test
    @TmsLinks({@TmsLink("4464"), @TmsLink("4467"), @TmsLink("22048"), @TmsLink("412"), @TmsLink("418")})
    @Description("Create and Edit Android inventory as Admin")
    public void createEditAndroidInventory() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        int rnd = rndUtil.getRandomInt(1, 10000), userId;
        Map<String, Boolean> allowedDsp, blockedDsp;
        List<adCategoryEnum> categoriesSelectedOriginal = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5));
        List<adCategoryEnum> categoriesSelectedEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        List<adCategoryEnum> categoriesFilteredEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        InventoryDO dataOriginal = new InventoryDO(inventoryTypeEnum.ANDROID, "Android_inventory_test_" + rnd, "org.android." + rnd + ".bundle", categoriesSelectedOriginal)
                .setRonTraffic(rndUtil.getRandomBoolean())
                .setLanguage(Locale.of("sv"))
                .setCategoriesFiltered(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5))
                .setBlockedDomains("blocked1.com,blocked2.com,blocked3.com")
                .setStatus(InventoryEnum.inventoryStatusEnum.PENDING);
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        inventory.gotoInventories();
        inventory.clickCreateInventory();
        inventory.selectInventoryType(dataOriginal.getType());
        inventory.setupInventory(dataOriginal);
        allowedDsp = inventory.selectRandomAllowedDsp(3);
        blockedDsp = inventory.selectRandomBlockedDsp(3);
        dataOriginal.setBlockedDsp(blockedDsp).setAllowedDsp(allowedDsp);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataOriginal.getName());
        dataOriginal.setId(inventory.getInventoryId(dataOriginal.getName(), userId));
        inventory.assertInventoryRowInfo(dataOriginal);
        inventory.clickEditInventory(dataOriginal);
        inventory.assertInventoryEditPage(dataOriginal);
        InventoryDO dataEdit = dataOriginal.setName("Android_inventory_test_edit_" + rnd)
                .setBundleDomain("edit.android." + rnd + ".bundle")
                .setRonTraffic(!dataOriginal.getRonTraffic())
                .setLanguage(Locale.of("pl"))
                .setBlockedDomains("blockedit11.org,blockedit22.net,blockedit33.ga");
        //Remove 2 categories from selected and filtered lists and merge with new lists
        dataOriginal.getCategoriesSelected().remove(0);
        dataOriginal.getCategoriesSelected().remove(1);
        categoriesSelectedEdit.addAll(dataOriginal.getCategoriesSelected());
        dataEdit.setCategoriesSelected(categoriesSelectedEdit);
        dataOriginal.getCategoriesFiltered().remove(0);
        dataOriginal.getCategoriesFiltered().remove(1);
        categoriesFilteredEdit.addAll(dataOriginal.getCategoriesFiltered());
        dataEdit.setCategoriesFiltered(categoriesFilteredEdit);
        //set all original allowed and blocked DSP values to false and merge with new lists
        allowedDsp.forEach((k, v) -> allowedDsp.put(k, false));
        blockedDsp.forEach((k, v) -> blockedDsp.put(k, false));
        allowedDsp.putAll(inventory.selectRandomAllowedDsp(3));
        blockedDsp.putAll(inventory.selectRandomBlockedDsp(3));
        dataEdit.setAllowedDsp(allowedDsp).setBlockedDsp(blockedDsp);
        inventory.setupInventory(dataEdit);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataEdit.getName());
        inventory.assertInventoryRowInfo(dataEdit);
        inventory.clickEditInventory(dataEdit);
        inventory.assertInventoryEditPage(dataEdit);
        sAssert.assertAll("Errors for " + dataOriginal.getType() + " Inventory creation/editing #" + dataOriginal.getId());
    }

    @Test
    @TmsLinks({@TmsLink("4465"), @TmsLink("4468"), @TmsLink("22048"), @TmsLink("412"), @TmsLink("418")})
    @Description("Create and Edit iOS inventory as Admin")
    public void createEditIosInventory() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        int rnd = rndUtil.getRandomInt(1, 10000), userId;
        Map<String, Boolean> allowedDsp, blockedDsp;
        List<adCategoryEnum> categoriesSelectedOriginal = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5));
        List<adCategoryEnum> categoriesSelectedEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        List<adCategoryEnum> categoriesFilteredEdit = new ArrayList<>(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 3));
        String bundleOriginal = inventory.getRandomAppIdFromFile(), bundleEdit = inventory.getRandomAppIdFromFile();
        InventoryDO dataOriginal = new InventoryDO(inventoryTypeEnum.IOS, "Ios_inventory_test_" + rnd, bundleOriginal, categoriesSelectedOriginal)
                .setRonTraffic(rndUtil.getRandomBoolean())
                .setLanguage(Locale.of("nl"))
                .setCategoriesFiltered(rndUtil.getRandomSublist(Arrays.stream(adCategoryEnum.values()).toList(), 5))
                .setBlockedDomains("blocked1.com,blocked2.com,blocked3.com")
                .setStatus(InventoryEnum.inventoryStatusEnum.PENDING);
        login.login(StaticData.supportDefaultUser);
        userId = login.getUserIdHeader();
        inventory.gotoInventories();
        inventory.clickCreateInventory();
        inventory.selectInventoryType(dataOriginal.getType());
        inventory.setupInventory(dataOriginal);
        allowedDsp = inventory.selectRandomAllowedDsp(3);
        blockedDsp = inventory.selectRandomBlockedDsp(3);
        dataOriginal.setBlockedDsp(blockedDsp).setAllowedDsp(allowedDsp);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataOriginal.getName());
        dataOriginal.setId(inventory.getInventoryId(dataOriginal.getName(), userId));
        inventory.assertInventoryRowInfo(dataOriginal);
        inventory.clickEditInventory(dataOriginal);
        inventory.assertInventoryEditPage(dataOriginal);
        InventoryDO dataEdit = dataOriginal.setName("Ios_inventory_test_edit_" + rnd)
                .setBundleDomain(bundleEdit)
                .setRonTraffic(!dataOriginal.getRonTraffic())
                .setLanguage(Locale.of("es"))
                .setBlockedDomains("blockedit11.org,blockedit22.net,blockedit33.ga");
        //Remove 2 categories from selected and filtered lists and merge with new edit lists
        dataOriginal.getCategoriesSelected().remove(0);
        dataOriginal.getCategoriesSelected().remove(1);
        categoriesSelectedEdit.addAll(dataOriginal.getCategoriesSelected());
        dataEdit.setCategoriesFiltered(categoriesSelectedEdit);
        dataOriginal.getCategoriesFiltered().remove(0);
        dataOriginal.getCategoriesFiltered().remove(1);
        categoriesFilteredEdit.addAll(dataOriginal.getCategoriesFiltered());
        dataEdit.setCategoriesSelected(categoriesFilteredEdit);
        //set all original allowed and blocked DSP values to false and merge with new edit lists
        allowedDsp.forEach((k, v) -> allowedDsp.put(k, false));
        blockedDsp.forEach((k, v) -> blockedDsp.put(k, false));
        allowedDsp.putAll(inventory.selectRandomAllowedDsp(3));
        blockedDsp.putAll(inventory.selectRandomBlockedDsp(3));
        dataEdit.setAllowedDsp(allowedDsp).setBlockedDsp(blockedDsp);
        inventory.setupInventory(dataEdit);
        inventory.clickSaveInventory();
        inventory.searchInventoryPlacement(dataEdit.getName());
        inventory.assertInventoryRowInfo(dataEdit);
        inventory.clickEditInventory(dataEdit);
        inventory.assertInventoryEditPage(dataEdit);
        sAssert.assertAll("Errors for iOS Inventory creation/editing [" + dataEdit.getId() + "]");
    }

    @Test
    @TmsLink("106")
    @Description("Search inventories by name")
    public void searchInventoriesByName() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        List<String> inventoryNames;
        String inventoryName, inventorySearch;
        login.login(StaticData.supportDefaultUser);
        int userid = inventory.getUserIdHeader();
        inventory.gotoInventories();
        inventoryNames = inventory.getInventoryName(true, userid);
        inventoryName = (String) randomUtil.getRandomElement(inventoryNames);
        inventorySearch = inventoryName.substring(0, randomUtil.getRandomInt(2, inventoryName.length())).toLowerCase();
        inventory.searchInventoryPlacement(inventorySearch);
        inventory.assertSearchResult(inventorySearch);
        inventory.searchInventoryPlacement("dGjhhYhV787ghve90YVVc");
        inventory.assertSearchResult(null);
        sAssert.assertAll("Errors on Inventories search by name");
    }

    @Test
    @TmsLink("793")
    @Description("Search by inventory type")
    public void searchInventoryByType() {
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO();
        Map<inventoryTypeEnum, Boolean> websiteMap = new HashMap<>() {{
            put(inventoryTypeEnum.WEB, true);
            put(inventoryTypeEnum.ANDROID, false);
            put(inventoryTypeEnum.IOS, false);
            put(inventoryTypeEnum.CTV, false);
        }};
        Map<inventoryTypeEnum, Boolean> androidAppMap = new HashMap<>() {{
            put(inventoryTypeEnum.WEB, false);
            put(inventoryTypeEnum.ANDROID, true);
            put(inventoryTypeEnum.IOS, false);
            put(inventoryTypeEnum.CTV, false);
        }};
        Map<inventoryTypeEnum, Boolean> iosAppMap = new HashMap<>() {{
            put(inventoryTypeEnum.WEB, false);
            put(inventoryTypeEnum.ANDROID, false);
            put(inventoryTypeEnum.IOS, true);
            put(inventoryTypeEnum.CTV, false);
        }};
        Map<inventoryTypeEnum, Boolean> ctvMap = new HashMap<>() {{
            put(inventoryTypeEnum.WEB, false);
            put(inventoryTypeEnum.ANDROID, false);
            put(inventoryTypeEnum.IOS, false);
            put(inventoryTypeEnum.CTV, true);
        }};
        Map<inventoryTypeEnum, Boolean> threeTypesMap = new HashMap<>() {{
            put(inventoryTypeEnum.WEB, true);
            put(inventoryTypeEnum.ANDROID, false);
            put(inventoryTypeEnum.IOS, true);
            put(inventoryTypeEnum.CTV, true);
        }};
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(websiteMap);
        inventory.assertInventoryFilterType(websiteMap);
        inventory.filterSelectInventoriesType(androidAppMap);
        inventory.assertInventoryFilterType(androidAppMap);
        inventory.filterSelectInventoriesType(iosAppMap);
        inventory.assertInventoryFilterType(iosAppMap);
        inventory.filterSelectInventoriesType(ctvMap);
        inventory.assertInventoryFilterType(ctvMap);
        inventory.filterSelectInventoriesType(threeTypesMap);
        inventory.assertInventoryFilterType(threeTypesMap);
    }

    //</editor-fold>

    //<editor-fold desc="Placements - creation">
    @Test
    @TmsLink("4472")
    @Description("Create Banner placement for Web inventory as Admin")
    public void createBannerPlacementWeb() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        List<adTypeEnum> adTypesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.adTypeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.adTypeEnum.class));
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
        }};
        PlacementDO placementData = new PlacementDO("web_banner_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.BANNER)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class))
                .setSizePreset((iabSizesEnum) rndUtil.getRandomElement(iabSizesEnum.getValues("inventoryWeb").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM)))
                .setApiFrameworks(frameworks)
                .setAdTypesToBlock(adTypesBlock)
                .setAttributesToBlock(attributesBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountBanners(inventoryData.getCountBanners() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4473")
    @Description("Create Banner placement for Android inventory as Admin")
    public void createBannerPlacementAndroid() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.ANDROID);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        List<adTypeEnum> adTypesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.adTypeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.adTypeEnum.class));
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
        }};
        PlacementDO placementData = new PlacementDO("android_banner_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.BANNER)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class, adPositionEnum.ABOVE, adPositionEnum.BELOW))
                .setSizePreset((iabSizesEnum) rndUtil.getRandomElement(iabSizesEnum.getValues("inventoryApp").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM)))
                .setApiFrameworks(frameworks)
                .setAdTypesToBlock(adTypesBlock)
                .setAttributesToBlock(attributesBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountBanners(inventoryData.getCountBanners() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4473")
    @Description("Create Banner placement for iOS inventory as Admin")
    public void createBannerPlacementIos() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.IOS);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        List<adTypeEnum> adTypesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.adTypeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.adTypeEnum.class));
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getBannerFrameworks()), true);
        }};
        PlacementDO placementData = new PlacementDO("ios_banner_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.BANNER)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class, adPositionEnum.ABOVE, adPositionEnum.BELOW))
                .setSizePreset((iabSizesEnum) rndUtil.getRandomElement(iabSizesEnum.getValues("inventoryApp").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM)))
                .setApiFrameworks(frameworks)
                .setAdTypesToBlock(adTypesBlock)
                .setAttributesToBlock(attributesBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountBanners(inventoryData.getCountBanners() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4474")
    @Description("Create Video placement for Web inventory as Admin")
    public void createVideoPlacementWeb() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        Map<protocolsTypeEnum, Boolean> protocols = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        List<mimeTypesEnum> mimeTypesToBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
        }};
        PlacementDO placementData = new PlacementDO("web_video_linear_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.VIDEO)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class))
                .setRewarded(rndUtil.getRandomBoolean())
                .setLinearity(videoLinearityTypeEnum.LINEAR_INSTREAM)
                .setPlacing(rndUtil.getRandomEnumValue(placingTypeEnum.class))
                .setPlaybackMethod(rndUtil.getRandomEnumValue(playbackMethodsTypeEnum.class))
                .setVideoProtocols(protocols)
                .setApiFrameworks(frameworks)
                .setAttributesToBlock(attributesBlock)
                .setVideoMimeTypes(mimeTypesToBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountVideo(inventoryData.getCountVideo() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("3993")
    @Description("Create Non-Linear/Out-Stream video placement with 'Serve via API' setting OFF")
    public void createNonLinearOutStreamVideoPlacementWeb() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        Map<protocolsTypeEnum, Boolean> protocols = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        PlacementDO placementData = new PlacementDO("web_video_nonlinear_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.VIDEO)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class))
                .setRewarded(rndUtil.getRandomBoolean())
                .setLinearity(videoLinearityTypeEnum.NONLINEAR_OUTSTREAM)
                .setPlacing(placingTypeEnum.INPICTURE)
                .setStartDelayType(rndUtil.getRandomEnumValue(videoStartDelayTypeEnum.class, videoStartDelayTypeEnum.MIDROLL))
                .setPlaybackMethod(rndUtil.getRandomEnumValue(playbackMethodsTypeEnum.class))
                .setVideoProtocols(protocols)
                .setApiFrameworks(frameworks)
                .setServeViaApi(false)
                .setAttributesToBlock(attributesBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountVideo(inventoryData.getCountVideo() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("2791")
    @Description("Create video placement with Skip and Start delay settings as Admin")
    public void createVideoPlacementWebWithSkipAndStartDelaySettings() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        Map<protocolsTypeEnum, Boolean> protocols = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        List<mimeTypesEnum> mimeTypesToBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
        }};
        PlacementDO placementData = new PlacementDO("web_video_start_skip_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.VIDEO)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class))
                .setRewarded(rndUtil.getRandomBoolean())
                .setLinearity(videoLinearityTypeEnum.LINEAR_INSTREAM)
                .setStartDelayType(videoStartDelayTypeEnum.MIDROLL)
                .setStartDelayValue(rndUtil.getRandomInt(1, 53600))
                .setPlaybackMethod(rndUtil.getRandomEnumValue(playbackMethodsTypeEnum.class))
                .setVideoProtocols(protocols)
                .setApiFrameworks(frameworks)
                .setAllowSkip(true)
                .setSkipDelay(rndUtil.getRandomInt(0, 53600))
                .setAttributesToBlock(attributesBlock)
                .setVideoMimeTypes(mimeTypesToBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountVideo(inventoryData.getCountVideo() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4475")
    @Description("Create Video placement for iOS inventory as Admin")
    public void createVideoPlacementIos() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.IOS);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        Map<protocolsTypeEnum, Boolean> protocols = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        List<mimeTypesEnum> mimeTypesToBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
        }};
        PlacementDO placementData = new PlacementDO("ios_video_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.VIDEO)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class, adPositionEnum.ABOVE, adPositionEnum.BELOW))
                .setRewarded(rndUtil.getRandomBoolean())
                .setLinearity(videoLinearityTypeEnum.LINEAR_INSTREAM)
                .setStartDelayType(rndUtil.getRandomEnumValue(videoStartDelayTypeEnum.class, videoStartDelayTypeEnum.MIDROLL))
                .setPlaybackMethod(rndUtil.getRandomEnumValue(playbackMethodsTypeEnum.class))
                .setVideoProtocols(protocols)
                .setApiFrameworks(frameworks)
                .setAttributesToBlock(attributesBlock)
                .setVideoMimeTypes(mimeTypesToBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountVideo(inventoryData.getCountVideo() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4475")
    @Description("Create Video placement for CTV inventory as Admin")
    public void createVideoPlacementCTV() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.CTV);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        Map<protocolsTypeEnum, Boolean> protocols = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        List<mimeTypesEnum> mimeTypesToBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
        }};
        PlacementDO placementData = new PlacementDO("ctv_video_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.VIDEO)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class, adPositionEnum.ABOVE, adPositionEnum.BELOW))
                .setRewarded(rndUtil.getRandomBoolean())
                .setLinearity(videoLinearityTypeEnum.LINEAR_INSTREAM)
                .setStartDelayType(rndUtil.getRandomEnumValue(videoStartDelayTypeEnum.class, videoStartDelayTypeEnum.MIDROLL))
                .setPlaybackMethod(rndUtil.getRandomEnumValue(playbackMethodsTypeEnum.class))
                .setVideoProtocols(protocols)
                .setApiFrameworks(frameworks)
                .setAttributesToBlock(attributesBlock)
                .setVideoMimeTypes(mimeTypesToBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountVideo(inventoryData.getCountVideo() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4474")
    @Description("Create Video placement for Android inventory as Admin")
    public void createVideoPlacementAndroid() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.ANDROID);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(CommonEnums.bidPriceTypeEnum.class), rndUtil.getRandomDouble(0, 1000000));
        Map<protocolsTypeEnum, Boolean> protocols = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
            put(rndUtil.getRandomEnumValue(protocolsTypeEnum.class), true);
        }};
        Map<apiFrameworksEnum, Boolean> frameworks = new HashMap<>() {{
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
            put((apiFrameworksEnum) rndUtil.getRandomElement(apiFrameworksEnum.getVideoFrameworks()), true);
        }};
        List<placementAttributeEnum> attributesBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.placementAttributeEnum.class));
        }};
        List<mimeTypesEnum> mimeTypesToBlock = new ArrayList<>() {{
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
            add(rndUtil.getRandomEnumValue(PlacementEnum.mimeTypesEnum.class));
        }};
        PlacementDO placementData = new PlacementDO("android_video_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.VIDEO)
                .setBidPrice(bidPrice)
                .setAdPosition(rndUtil.getRandomEnumValue(adPositionEnum.class, adPositionEnum.ABOVE, adPositionEnum.BELOW))
                .setRewarded(rndUtil.getRandomBoolean())
                .setLinearity(videoLinearityTypeEnum.LINEAR_INSTREAM)
                .setStartDelayType(rndUtil.getRandomEnumValue(videoStartDelayTypeEnum.class, videoStartDelayTypeEnum.MIDROLL))
                .setPlaybackMethod(rndUtil.getRandomEnumValue(playbackMethodsTypeEnum.class))
                .setVideoProtocols(protocols)
                .setApiFrameworks(frameworks)
                .setAttributesToBlock(attributesBlock)
                .setVideoMimeTypes(mimeTypesToBlock)
                .setBackfill(placementBackfillExample);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountVideo(inventoryData.getCountVideo() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4476")
    @Description("Create Native placement for Web inventory as Admin")
    public void createWebNativePlacement() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(bidPriceTypeEnum.class), rndUtil.getRandomDoubleRounded(0, 1000000, 2));
        PlacementDO.Size sizePlacement = new PlacementDO.Size(rndUtil.getRandomInt(300, 32766), rndUtil.getRandomInt(250, 32766));
        Map<placementDataAssetTypeEnum, Boolean> dataAssets = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
        }};
        PlacementDO placementData = new PlacementDO("web_native_test_" + new RandomUtility().getRandomInt(1, 10000), placementTypeEnum.NATIVE)
                .setBidPrice(bidPrice)
                .setSize(sizePlacement)
                .setAdEnvironment(rndUtil.getRandomEnumValue(placementAdEnvironmentEnum.class))
                .setAdvancedAdSettings(rndUtil.getRandomEnumValue(placementAdvancedAdSettingsEnum.class))
                .setAdUnit(rndUtil.getRandomEnumValue(placementAdUnitOptionsEnum.class))
                .setAdTitleStatus(false)
                .setDataAssetsMaps(dataAssets)
                .addAdImage(placementImageTypeEnum.ICON, true);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountNative(inventoryData.getCountNative() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4476")
    @Description("Create Native placement for iOS inventory as Admin")
    public void createIosNativePlacement() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.IOS);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(bidPriceTypeEnum.class), rndUtil.getRandomDoubleRounded(0, 1000000, 2));
        PlacementDO.Size sizePlacement = new PlacementDO.Size(rndUtil.getRandomInt(300, 32766), rndUtil.getRandomInt(250, 32766));
        Map<placementDataAssetTypeEnum, Boolean> dataAssets = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
        }};
        PlacementDO placementData = new PlacementDO("ios_native_test_" + new RandomUtility().getRandomInt(1, 10000), placementTypeEnum.NATIVE)
                .setBidPrice(bidPrice)
                .setSize(sizePlacement)
                .setAdEnvironment(rndUtil.getRandomEnumValue(placementAdEnvironmentEnum.class))
                .setAdvancedAdSettings(rndUtil.getRandomEnumValue(placementAdvancedAdSettingsEnum.class))
                .setAdUnit(rndUtil.getRandomEnumValue(placementAdUnitOptionsEnum.class))
                .setAdTitleStatus(true)
                .setAdTitleLength(rndUtil.getRandomInt(1, 600))
                .setDataAssetsMaps(dataAssets)
                .addAdImage(placementImageTypeEnum.MAIN, true)
                .setNativeMainImageSize(rndUtil.getRandomInt(0, 32766), rndUtil.getRandomInt(0, 32766));
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountNative(inventoryData.getCountNative() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    @Test
    @TmsLink("4476")
    @Description("Create Native placement for Android inventory as Admin")
    public void createAndroidNativePlacement() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.ANDROID);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(rndUtil.getRandomEnumValue(bidPriceTypeEnum.class), rndUtil.getRandomDoubleRounded(0, 1000000, 2));
        PlacementDO.Size sizePlacement = new PlacementDO.Size(rndUtil.getRandomInt(300, 32766), rndUtil.getRandomInt(250, 32766));
        Map<placementDataAssetTypeEnum, Boolean> dataAssets = new HashMap<>() {{
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
            put(rndUtil.getRandomEnumValue(placementDataAssetTypeEnum.class, placementDataAssetTypeEnum.DESC), true);
        }};
        PlacementDO placementData = new PlacementDO("ios_native_test_" + new RandomUtility().getRandomInt(1, 10000), placementTypeEnum.NATIVE)
                .setBidPrice(bidPrice)
                .setSize(sizePlacement)
                .setAdEnvironment(rndUtil.getRandomEnumValue(placementAdEnvironmentEnum.class))
                .setAdvancedAdSettings(rndUtil.getRandomEnumValue(placementAdvancedAdSettingsEnum.class))
                .setAdUnit(rndUtil.getRandomEnumValue(placementAdUnitOptionsEnum.class))
                .setAdTitleStatus(true)
                .setAdTitleLength(rndUtil.getRandomInt(1, 600))
                .setDataAssetsMaps(dataAssets)
                .addAdImage(placementImageTypeEnum.ICON, true)
                .addAdImage(placementImageTypeEnum.MAIN, true)
                .setNativeMainImageSize(rndUtil.getRandomInt(0, 32766), rndUtil.getRandomInt(0, 32766));
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.setupPlacement(placementData);
        inventory.clickSaveInventory();
        inventoryData.setCountNative(inventoryData.getCountNative() + 1);
        placementData.setId(inventory.getPlacementId(inventoryData.getId(), placementData));
        inventory.searchInventoryPlacement(inventoryData.getName());
        inventory.assertInventoryRowInfo(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.clickLastPagePlacements();
        inventory.assertPlacementRow(placementData);
        inventory.clickEditPlacement(placementData);
        inventory.assertPlacementEditPage(placementData);
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + " #" + inventoryData.getId() + "], Placement [" + placementData.getName() + "#" + placementData.getId() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="Placements - validation">
    @Test
    @TmsLink("4477")
    @Description("Validate required Banner settings")
    public void validateRequiredBannerSettings() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO.Size size = new PlacementDO.Size(iabSizesEnum.BANNER_HALF);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(bidPriceTypeEnum.FLOOR, null);
        PlacementDO placementData = new PlacementDO("web_banner_validation_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.BANNER).setBidPrice(new PlacementDO.BidPrice(bidPriceTypeEnum.FLOOR, 35.2));
        String nameInvalid = "% & >> 78",
                nameShort = "12",
                nameLong = "The Ad Exchange permission displays all Ad Exchange interface features and settings in Google Ad Manager. For example, users can access the Billing section in Ad Manager to see Ad Exchange payments information, but will not see Ad Manager billing information AD HB";
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.clickCreatePlacement(inventoryData, placementData.getType());
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(InventoryText.VALIDATION_REQUIRED_EMPTY, InventoryText.VALIDATION_REQUIRED_EMPTY, InventoryText.VALIDATION_REQUIRED_EMPTY, null);
        inventory.selectBannerSize(size);
        inventory.inputName(nameInvalid);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(InventoryText.invalidCharactersValidationErrorMessage, null, null, null);
        inventory.inputName(nameShort);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(InventoryText.placementNameLessThan3Characters, null, null, null);
        inventory.inputName(nameLong);
        //***//
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(InventoryText.placementNameFieldMoreThan255characters, null, null, InventoryText.valueFromTheBidPriceField);
        inventory.inputName(placementData.getName());
        bidPrice.setValue(-0.3);
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(null, null, null, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0"));
        bidPrice.setValue(1000093.99);
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(null, null, null, InventoryText.valueLargerThan999999_99InIheBidPrice);
        inventory.setupBidPrice(placementData.getBidPrice());
        //***//
        inventory.setSizeCustom(-500, -800);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(null, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0"), InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0"), null);
        inventory.setSizeCustom(353363, 353363);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationBannerEditPage(null, InventoryText.valuesBiggerThan32766InTheWidthFields, InventoryText.valuesBiggerThan32766InTheHeightFields, null);
        sAssert.assertAll("Errors on Banner validation. Inventory [" + inventoryData.getName() + "], Placement [" + placementData.getName() + "]");
    }

    @Test
    @TmsLink("4478")
    @Description("Validate required Video settings")
    public void validateRequiredVideoSettings() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO placementValidData = new PlacementDO("web_video_validation_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.VIDEO).setBidPrice(new PlacementDO.BidPrice(bidPriceTypeEnum.FIXED, 23.5)).setStartDelayValue(12);
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(bidPriceTypeEnum.FIXED, null);
        String nameInvalid = "% & >>",
                nameShort = "2c";
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.clickCreatePlacement(inventoryData, placementValidData.getType());
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(InventoryText.VALIDATION_REQUIRED_EMPTY, null, null, null);
        inventory.inputName(nameInvalid);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(InventoryText.invalidCharactersValidationErrorMessage, null, null, null);
        inventory.inputName(nameShort);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(InventoryText.placementNameLessThan3Characters, null, null, null);
        inventory.inputName(placementValidData.getName());
        //***//
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, InventoryText.valueFromTheBidPriceField, null, null);
        bidPrice.setValue(-24.35);
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0"), null, null);
        bidPrice.setValue(1003093.28);
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, InventoryText.valueLargerThan999999_99InIheBidPrice, null, null);
        inventory.setupBidPrice(placementValidData.getBidPrice());
        //***//
        inventory.selectVideoStartDelay(videoStartDelayTypeEnum.MIDROLL);
        inventory.inputStartDelay(null);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, InventoryText.emptyStartDelay, null);
        inventory.inputStartDelay(-1);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, InventoryText.lessThanZeroInTheStartDelay, null);
        inventory.inputStartDelay(34.6);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, InventoryText.VALIDATION_DECIMAL_INTEGER.replace("${low}", "34").replace("${high}", "35"), null);
        inventory.inputStartDelay(553363);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, InventoryText.bigValueMore53600StartDelay, null);
        inventory.inputStartDelay(placementValidData.getStartDelayValue());
        //***//
        inventory.setSkip(true, null);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, null, InventoryText.emptySkipDelay);
        inventory.setSkip(true, -13);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, null, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0"));
        inventory.setSkip(true, 7.3);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, null, InventoryText.VALIDATION_DECIMAL_INTEGER.replace("${low}", "7").replace("${high}", "8"));
        inventory.setSkip(true, 553363);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(null, null, null, InventoryText.VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "456525"));
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + "], Placement [" + placementValidData.getName() + "]");
    }

    @Test
    @TmsLink("4479")
    @Description("Validate required Native settings")
    public void validateRequiredNativeSettings() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO placementValidData = new PlacementDO("web_native_validation_test_" + rndUtil.getRandomInt(1, 10000), placementTypeEnum.NATIVE).setSize(new PlacementDO.Size(450, 300));
        PlacementDO.BidPrice bidPrice = new PlacementDO.BidPrice(bidPriceTypeEnum.FIXED, null);
        String nameInvalid = "% & >>",
                nameShort = "12",
                nameLong = "The Ad Exchange permission displays all Ad Exchange interface features and settings in Google Ad Manager. For example, users can access the Billing section in Ad Manager to see Ad Exchange payments information, but will not see Ad Manager billing information AD HB";
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.clickCreatePlacement(inventoryData, placementValidData.getType());
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationNativeEditPage(InventoryText.VALIDATION_REQUIRED_EMPTY, null, null, null);
        //***//
        inventory.inputName(placementValidData.getName());
        inventory.clickSaveInventoryWithErrors();
        inventory.assertModal(sAssert, InventoryText.MODAL_ERROR_HEADER + "!", InventoryText.nativeModalAdElementsRequired, "Ad elements required");
        inventory.modalClickConfirm();
        inventory.setAdTitle(true, null);
        //***//
        inventory.inputName(nameInvalid);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(InventoryText.invalidCharactersValidationErrorMessage, null, null, null);
        inventory.inputName(nameShort);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationVideoEditPage(InventoryText.placementNameLessThan3Characters, null, null, null);
        inventory.inputName(nameLong);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationNativeEditPage(InventoryText.placementNameFieldMoreThan255characters, null, null, null);
        inventory.inputName(placementValidData.getName());
        //***//
        inventory.setSizeCustom(null, null);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationNativeEditPage(null, InventoryText.VALIDATION_REQUIRED_EMPTY, InventoryText.VALIDATION_REQUIRED_EMPTY, null);
        inventory.setSizeCustom(50, 80);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationNativeEditPage(null, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "300"), InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "250"), null);
        inventory.setSizeCustom(placementValidData.getSize().getWidth(), placementValidData.getSize().getHeight());
        //***//
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationNativeEditPage(null, null, null, InventoryText.valueFromTheBidPriceField);
        bidPrice.setValue(-24.35);
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationNativeEditPage(null, null, null, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0"));
        bidPrice.setValue(1003093.28);
        inventory.setupBidPrice(bidPrice);
        inventory.clickSaveInventoryWithErrors();
        inventory.assertValidationNativeEditPage(null, null, null, InventoryText.valueLargerThan999999_99InIheBidPrice);
        inventory.setupBidPrice(placementValidData.getBidPrice());
        sAssert.assertAll("Errors on Placement creation. Inventory [" + inventoryData.getName() + "], Placement [" + placementValidData.getName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="Placements - tags">
    @Test(priority = 10)
    @TmsLink("1132")
    @Description("Check Web Banner placement tags")
    public void checkWebBannerPlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.BANNER);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertBannerTags(placementData, bannerTagsEnum.DIRECT, inventoryData.getType());
        inventory.clickTagTab(bannerTagsEnum.JS.publicName());
        inventory.assertBannerTags(placementData, bannerTagsEnum.JS, inventoryData.getType());
        inventory.clickTagTab(bannerTagsEnum.ASYNC.publicName());
        inventory.assertBannerTags(placementData, bannerTagsEnum.ASYNC, inventoryData.getType());
        inventory.clickTagTab(bannerTagsEnum.GOOGLE_DFP.publicName());
        inventory.assertBannerTags(placementData, bannerTagsEnum.GOOGLE_DFP, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("829")
    @Description("Check Android Banner placement tags")
    public void checkAndroidBannerPlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.ANDROID);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.BANNER);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertBannerTags(placementData, bannerTagsEnum.APPLOVIN, inventoryData.getType());
        inventory.clickTagTab(bannerTagsEnum.JS.publicName());
        inventory.assertBannerTags(placementData, bannerTagsEnum.JS, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("829")
    @Description("Check iOS Banner placement tags")
    public void checkIosBannerPlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.IOS);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.BANNER);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertBannerTags(placementData, bannerTagsEnum.APPLOVIN, inventoryData.getType());
        inventory.clickTagTab(bannerTagsEnum.JS.publicName());
        inventory.assertBannerTags(placementData, bannerTagsEnum.JS, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("1127")
    @Description("Check Web Video placement tags")
    public void checkWebVideoPlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.VIDEO);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertVideoTag(placementData, videoTagsEnum.VASTVPAID, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.JW.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.JW, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.DFP.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.DFP, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.LKQD.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.LKQD, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.CONNATIX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.CONNATIX, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.API.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.API, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.COLUMN6.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.COLUMN6, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPRINGSERVE.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPRINGSERVE, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPOTX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPOTX, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.ANIVIEW.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.ANIVIEW, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("1119")
    @Description("Check Android Video placement tags")
    public void checkAndroidVideoPlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.ANDROID);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.VIDEO);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertVideoTag(placementData, videoTagsEnum.VASTVPAID, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.APPLOVIN.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.APPLOVIN, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.AERSERV.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.AERSERV, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.IRONSRC.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.IRONSRC, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.LKQD.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.LKQD, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.COLUMN6.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.COLUMN6, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPRINGSERVE.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPRINGSERVE, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.CONNATIX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.CONNATIX, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPOTX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPOTX, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("1119")
    @Description("Check iOS Video placement tags")
    public void checkIosVideoPlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.IOS);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.VIDEO);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertVideoTag(placementData, videoTagsEnum.VASTVPAID, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.APPLOVIN.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.APPLOVIN, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.AERSERV.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.AERSERV, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.IRONSRC.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.IRONSRC, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.LKQD.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.LKQD, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.COLUMN6.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.COLUMN6, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPRINGSERVE.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPRINGSERVE, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.CONNATIX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.CONNATIX, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPOTX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPOTX, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("1125")
    @Description("Check CTV Vide0 placement tags")
    public void checkCtvVideoPlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.CTV);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.VIDEO);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertVideoTag(placementData, videoTagsEnum.VASTVPAID, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.LKQD.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.LKQD, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.COLUMN6.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.COLUMN6, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPRINGSERVE.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPRINGSERVE, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.CONNATIX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.CONNATIX, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.ROKU.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.ROKU, inventoryData.getType());
        inventory.clickTagTab(videoTagsEnum.SPOTX.publicName());
        inventory.assertVideoTag(placementData, videoTagsEnum.SPOTX, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("1121")
    @Description("Check Web Native placement tags")
    public void checkWebNativePlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.WEB);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.NATIVE);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertNativeTags(placementData, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("1125")
    @Description("Check Android Native placement tags")
    public void checkAndroidNativePlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.ANDROID);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.NATIVE);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertNativeTags(placementData, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    @Test(priority = 10)
    @TmsLink("1125")
    @Description("Check iOs Native placement tags")
    public void checkIosNativePlacementTags() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        InventoryDO inventoryData = new InventoryDO().setType(inventoryTypeEnum.IOS);
        PlacementDO placementData = new PlacementDO().setType(placementTypeEnum.NATIVE);
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectInventoriesType(new HashMap<>() {{
            put(inventoryData.getType(), true);
        }});
        inventory.filterSelectPlacementType(new HashMap<>() {{
            put(placementData.getType(), true);
        }});
        inventoryData.setName(inventory.getRandomInventoryName(inventoryData.getType(), InventoryEnum.inventoryStatusEnum.DECLINED));
        inventory.setInventoryDataFromTable(inventoryData);
        inventory.clickPlacementWrap(inventoryData, true);
        inventory.setPlacementDataFromTable(placementData);
        inventory.clickPlacementTagButton(placementData);
        inventory.assertNativeTags(placementData, inventoryData.getType());
        sAssert.assertAll("Errors on Inventory [" + inventoryData.getName() + "]/" + inventoryData.getType().publicName().toUpperCase() + ". Placement [" + placementData.getName() + "]/" + placementData.getType());
    }

    //</editor-fold>

    //<editor-fold desc="Search">

    @Test(priority = 5)
    @TmsLink("51126")
    @Description("Search placements by name")
    public void searchPlacementsByName() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10);
        List<String> placementNames;
        String placementName, placementSearch;
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        placementNames = inventory.getPlacementName(true);
        placementName = (String) randomUtil.getRandomElement(placementNames);
        placementSearch = placementName.substring(1, rnd).toLowerCase();
        inventory.searchInventoryPlacement(placementSearch);
        inventory.assertSearchResult(placementSearch);
        inventory.searchInventoryPlacement("dGjhhYhV787ghve90YVVc");
        inventory.assertSearchResult(null);
    }

    @Test(priority = 5)
    @TmsLink("108")
    @Description("Search by placements type")
    public void searchPlacementByType() {
        AuthorizationPO login = new AuthorizationPO();
        InventoryPO inventory = new InventoryPO();
        List<String> inventoryPlacementNamesList;
        Map<placementTypeEnum, Boolean> bannerMap = new HashMap<>() {{
            put(placementTypeEnum.BANNER, true);
            put(placementTypeEnum.VIDEO, false);
            put(placementTypeEnum.NATIVE, false);
        }};
        Map<placementTypeEnum, Boolean> videoMap = new HashMap<>() {{
            put(placementTypeEnum.BANNER, false);
            put(placementTypeEnum.VIDEO, true);
            put(placementTypeEnum.NATIVE, false);
        }};
        Map<placementTypeEnum, Boolean> nativeMap = new HashMap<>() {{
            put(placementTypeEnum.BANNER, false);
            put(placementTypeEnum.VIDEO, false);
            put(placementTypeEnum.NATIVE, true);
        }};
        Map<placementTypeEnum, Boolean> twoTypesMap = new HashMap<>() {{
            put(placementTypeEnum.BANNER, true);
            put(placementTypeEnum.VIDEO, false);
            put(placementTypeEnum.NATIVE, true);
        }};
        login.login(StaticData.supportDefaultUser);
        inventory.gotoInventories();
        inventory.filterSelectPlacementType(bannerMap);
        inventoryPlacementNamesList = inventory.getInventoriesNames();
        inventory.assertPlacementFilterType(bannerMap, inventoryPlacementNamesList);
        inventory.filterSelectPlacementType(videoMap);
        inventoryPlacementNamesList = inventory.getInventoriesNames();
        inventory.assertPlacementFilterType(videoMap, inventoryPlacementNamesList);
        inventory.filterSelectPlacementType(nativeMap);
        inventoryPlacementNamesList = inventory.getInventoriesNames();
        inventory.assertPlacementFilterType(nativeMap, inventoryPlacementNamesList);
        inventory.filterSelectPlacementType(twoTypesMap);
        inventoryPlacementNamesList = inventory.getInventoriesNames();
        inventory.assertPlacementFilterType(twoTypesMap, inventoryPlacementNamesList);
    }

    //</editor-fold>

}
