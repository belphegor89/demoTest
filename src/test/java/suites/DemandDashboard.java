package suites;

import common.SoftAssertCustom;
import common.utils.BrowserUtility;
import common.utils.RandomUtility;
import data.*;
import data.CommonEnums.*;
import data.DemandEnum.*;
import data.dataobject.EndpointCommonDO;
import data.dataobject.EndpointCommonDO.ScannerSetting;
import data.dataobject.EndpointDemandDO;
import data.dataobject.FilterListDO;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.DemandPO;
import pages.FilterListPO;
import pages.ScannersPO;

import java.io.File;
import java.util.*;

import static data.textstrings.messages.DemandText.*;
import static data.textstrings.messages.SupplyText.emptyFilterListSection;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("Demand dashboard")
public class DemandDashboard extends BaseSuiteClassNew {

    //<editor-fold desc="RTB endpoints">
    @Test
    @TmsLink("4263")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create RTB endpoint with basic settings")
    public void createBasicRtb() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, true);
            put(adFormatPlacementTypeEnum.AUDIO, false);
            put(adFormatPlacementTypeEnum.NATIVE, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, false);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_DSP, true);
            put(advancedSettingsEnum.SYNCED, true);
            put(advancedSettingsEnum.GDPR, true);
            put(advancedSettingsEnum.PREMIUM, true);
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestRTB" + rnd, endpointTypeEnum.RTB).setStatus(true).setEndpointUrl("http://testurl." + rnd + ".com").setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true));
        endpointData.setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1572")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create RTB endpoint with Adapter")
    public void createRtbAdapter() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, false);
            put(adFormatPlacementTypeEnum.VIDEO, true);
            put(adFormatPlacementTypeEnum.AUDIO, true);
            put(adFormatPlacementTypeEnum.NATIVE, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, false);
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.COMPRESSED, true);
            put(advancedSettingsEnum.GDPR, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestRTBAdapter" + rnd, endpointTypeEnum.RTB).setStatus(true).setEndpointUrl("http://testurl." + rnd + ".com").setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setRtbVersion(rtbVersionEnum.v23).setAuctionType(auctionTypeEnum.FIRST);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.selectAdapterType(adapterTypeEnum.API);
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion())
                .setApiAdapter((AdaptersEnums.adaptersApiEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointData.getEndpointType(), true)))
                .addAdapterSettingName((String) randomUtil.getRandomElement(dsp.getAdapterSettingsList(endpointData.getEndpointType(), endpointData.getApiAdapter().publicName())));
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("4444")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create RTB endpoint with 'Additional info' settings")
    public void createRtbAdditional() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 550000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setAdaptive();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, false);
            put(adFormatPlacementTypeEnum.VIDEO, true);
            put(adFormatPlacementTypeEnum.AUDIO, true);
            put(adFormatPlacementTypeEnum.NATIVE, false);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, false);
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_DSP, true);
            put(advancedSettingsEnum.COMPRESSED, true);
            put(advancedSettingsEnum.IPV6, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        List<EndpointDemandDO.BannerSize> bannerSizesList = new ArrayList<>() {{
            add(new EndpointDemandDO.BannerSize().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM))));
            add(new EndpointDemandDO.BannerSize().setSize(iabSizesEnum.CUSTOM, 222, 444));
        }};
        List<EndpointDemandDO.VideoSize> videoSizeList = new ArrayList<>() {{
            add(new EndpointDemandDO.VideoSize().setPredefinedSize((videoSizeEnum) randomUtil.getRandomElement(Arrays.stream(videoSizeEnum.values()).toList().stream().filter(x -> x != videoSizeEnum.CUSTOM))));
            add(new EndpointDemandDO.VideoSize().setSize(videoSizeEnum.CUSTOM, 555, 888));
        }};
        Map<String, String> seatMap = new LinkedHashMap<>() {{
            put("seat2", "asdf567");
            put("seat3", "fab567-df12a2-cba357");
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestRTBAdditional" + rnd, endpointTypeEnum.RTB).setStatus(true).setEndpointUrl("http://testurl." + rnd + ".com").setRtbVersion(rtbVersionEnum.v23).setAuctionType(auctionTypeEnum.FIRST).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setBannerSizeList(bannerSizesList).setVideoSizeList(videoSizeList).setSeatMap(seatMap).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.json?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.JSON).addSspToAllowed("Native Supply #-1", true);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickAdditionalInfoTab();
        endpointData.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointData);
        endpointData.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 1))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 1))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1219")
    @Description("Create RTB endpoint with 'Geo Settings'")
    public void createRtbGeo() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 30, 2), randomUtil.getRandomDoubleRounded(40, 90, 2));
        double endpointBidfloor = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> bidfloorList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
        }};
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, true);
            put(adFormatPlacementTypeEnum.AUDIO, true);
            put(adFormatPlacementTypeEnum.NATIVE, false);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.REWARDED, true);
            put(advancedSettingsEnum.GDPR, true);
            put(advancedSettingsEnum.IPV6, true);
            put(advancedSettingsEnum.COMPRESSED, true);
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestRtbGeo" + rnd, endpointTypeEnum.RTB).setStatus(true).setEndpointUrl("http://testurl." + rnd + ".com").setRtbVersion(rtbVersionEnum.v26).setAuctionType(auctionTypeEnum.FIRST).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointBidfloor).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true));
        endpointData.setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickGeoSettingsTab();
        endpointData.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(6));
        dsp.setGeoBlacklist(endpointData.getGeoBlacklist());
        endpointData.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 3));
        endpointData.setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(bidfloorList, 4));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1225")
    @Description("Validate RTB endpoint required settings")
    public void validateRtbSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        double bidfloor = 12;
        int qpsFixed = 3344;
        String company, endpointUrl = "http://testurl.com/valid?q=t&e=pol", nameLong = "emulatekindbloominevolvefabricatesupposedisposaldifficultcommunityexcitedindulgencedesiredtemperamentsignatureaccordingsuperficialcomparablehistoricallylaw";
        EndpointDemandDO.QpsSetting qpsTemplate = new EndpointDemandDO.QpsSetting();
        EndpointCommonDO.MarginSettings marginTemplate = new EndpointDemandDO.MarginSettings();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.APP, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(qpsFixed + 1);
        }};
        List<Double> bidfloorList = new ArrayList<>() {{
            add(bidfloor + 1);
        }};
        Map<Integer, Map.Entry<String, String>> geoWhitelistErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, geoValueHigh));
        }};
        Map<Integer, Map.Entry<String, String>> geoBidfloorErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, geoValueHigh));
        }};
        ScannerSetting protectedPostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(false, 1),
                pixalatePostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 0),
                whiteopsFraudSensor = new ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 123);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        company = dsp.getRandomCompanyName(true);
        dsp.saveAndAssertValidationRequiredInfoPage(VALIDATION_REQUIRED_EMPTY, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), null, null, null);
        dsp.inputName("12");
        dsp.setupQpsLimit(qpsTemplate.setFixed(null));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(1100001));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, errorLimitQpsHigh, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(null, null, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(1100001, 1100002, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, errorLimitQpsHigh, errorMarginMaxMin2.replace("${minVal}", "1100002").replace("${maxVal}", "1100000"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(5555, 2222, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "5556"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(0, 0, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(null, null, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(0, 0, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(7474, 2525, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "7475"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(qpsFixed));
        dsp.inputName("<;:[]{}>?~");
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameFormat, errorCompanyEmpty, errorEndpointUrlEmpty, null, null, null, null, null, null);
        dsp.inputName(nameLong);
        dsp.assertNameValueLength(nameLong);
        dsp.selectCompany(company);
        dsp.inputEndpointUrl("ui.localhost.com");
        dsp.saveAndAssertValidationRequiredInfoPage(null, null, errorEndpointUrlInvalid, null, null, null, null, null, null);
        dsp.inputEndpointUrl(endpointUrl);
        dsp.saveAndAssertValidationRequiredInfoPage(errorAdFormatsEmpty, null, null, null, null, null, null, null, null);
        dsp.toggleAdFormat(adFormatsMap);
        dsp.saveAndAssertValidationRequiredInfoPage(errorTrafficTypesEmpty, null, null, null, null, null, null, null, null);
        dsp.toggleTrafficType(trafficTypesMap);
        dsp.setMargin(marginTemplate.setFixed(101d));
        dsp.inputSpendLimit(2147483647);
        dsp.saveAndAssertValidationOptionalInfoPage(errorMarginHigh, null, null, errorSpendLimitHigh, null);
        dsp.setMargin(marginTemplate.setRange(102d, 101d));
        dsp.saveAndAssertValidationOptionalInfoPage(VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"), errorMarginMaxMin2.replace("${minVal}", "102").replace("${maxVal}", "100"), null, errorSpendLimitHigh, null);
        dsp.setMargin(marginTemplate.setRange(70d, 50d));
        dsp.inputSpendLimit(134);
        dsp.saveAndAssertValidationOptionalInfoPage(VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "70"), null, null, null);
        dsp.setMargin(marginTemplate.setAdaptive());
        dsp.inputTmax(1100001);
        dsp.inputBidfloor(1100001d);
        dsp.saveAndAssertValidationOptionalInfoPage(null, null, errorMaxBidfloorHigh, null, errorTmaxHigh);
        dsp.inputTmax(256);
        dsp.inputBidfloor(bidfloor);
        dsp.clickGeoSettingsTab();
        dsp.clickGeoWhitelistAdd(1);
        dsp.clickGeoBidfloorAdd(1);
        dsp.setupGeoWhitelist(qpsList, null);
        dsp.setupGeoBidfloorCpm(bidfloorList, null);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertValidationGeoPage(geoWhitelistErrors, qpsFixed, geoBidfloorErrors, bidfloor);
        dsp.clickGeoWhitelistDelete(0);
        dsp.clickGeoBidfloorDelete(0);
        dsp.clickScannersTab();
        dsp.setScannerSetting(protectedPostbid);
        dsp.setScannerSetting(pixalatePostbid);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(protectedPostbid, false, validationScannerNotEnabled);
        dsp.assertScannerError(pixalatePostbid, true, validationScannerImpressionLow);
        dsp.setScannerSetting(whiteopsFraudSensor);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(whiteopsFraudSensor, true, VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"));
        softAssert.assertAll("Errors in RTB DSP Validation messages:");
    }

    @Test
    @Issue("WP-2442")
    @TmsLink("2642")
    @Description("Clone DSP RTB Endpoint")
    public void cloneDspRtbEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        double endpointBidfloor = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setDynamic(randomUtil.getRandomInt(1, 550000), randomUtil.getRandomInt(560000, 1100000), (DemandEnum.qpsFrequencyEnum) randomUtil.getRandomElement(DemandEnum.qpsFrequencyEnum.values()), (DemandEnum.qpsOptimizationEnum) randomUtil.getRandomElement(DemandEnum.qpsOptimizationEnum.values()));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.AUDIO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.NATIVE, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> bidfloorList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
        }};
        List<EndpointDemandDO.BannerSize> bannerSizesList = new ArrayList<>() {{
            add(new EndpointDemandDO.BannerSize().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM))));
            add(new EndpointDemandDO.BannerSize().setSize(iabSizesEnum.CUSTOM, 222, 444));
        }};
        List<EndpointDemandDO.VideoSize> videoSizeList = new ArrayList<>() {{
            add(new EndpointDemandDO.VideoSize().setPredefinedSize((videoSizeEnum) randomUtil.getRandomElement(Arrays.stream(videoSizeEnum.values()).toList().stream().filter(x -> x != videoSizeEnum.CUSTOM))));
            add(new EndpointDemandDO.VideoSize().setSize(videoSizeEnum.CUSTOM, 555, 888));
        }};
        Map<String, String> seatMap = new LinkedHashMap<>() {{
            put("seat2", "asdf567");
            put("seat3", "fab567-df12a2-cba357");
        }};
        List<EndpointDemandDO.ScannerSetting> scanners = new ArrayList<>() {{
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, 11));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 13));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 32));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.GEOEDGE, true));
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_DSP, true);
            put(advancedSettingsEnum.SYNCED, true);
            put(advancedSettingsEnum.GDPR, true);
            put(advancedSettingsEnum.PREMIUM, true);
        }};
        EndpointDemandDO endpointOriginal = new EndpointDemandDO("autotestDspRtbClone" + rnd, endpointTypeEnum.RTB).setStatus(true).setRegion(regionsEnum.US_EAST).setEndpointUrl("http://testurl." + rnd + ".com").setAuctionType(auctionTypeEnum.SECOND).setRtbVersion(rtbVersionEnum.v26).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointBidfloor).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setBannerSizeList(bannerSizesList).setVideoSizeList(videoSizeList).setSeatMap(seatMap).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.xml?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.XML).addSspToAllowed("Native Supply #-1", true).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        //*General*//
        endpointOriginal.setCompany(dsp.getRandomCompanyName(true));
        dsp.setupGeneralSettingsTab(endpointOriginal);
        //*GEO*//
        dsp.clickGeoSettingsTab();
        endpointOriginal.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(6));
        dsp.setGeoBlacklist(endpointOriginal.getGeoBlacklist());
        endpointOriginal.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 4))
                .setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(bidfloorList, 4));
        //*ADDITIONAL*//
        dsp.clickAdditionalInfoTab();
        endpointOriginal.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointOriginal);
        endpointOriginal.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 2))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 2))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        //*SCANNERS*//
        dsp.clickScannersTab();
        dsp.setupScannerSection(endpointOriginal.getScannerSettings());
        dsp.clickSaveExitEndpoint();
        endpointOriginal.setId(dsp.getEndpointId(endpointOriginal.getEndpointName()));
        dsp.searchNameIdInput(endpointOriginal.getEndpointName());
        dsp.clickCloneEndpoint(endpointOriginal.getId(), true);
        dsp.openClonedEndpointEdit(true);
        EndpointDemandDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointCloned.getEndpointName() + " copy1")
                .setStatus(false)
                .setId(dsp.getEndpointId(endpointCloned.getEndpointName()))
                .clearSeatMap().clearSspAllowedMap().clearSspBlockedMap().clearInventoryAllowedMap().clearInventoryBlockedMap().setReportApiLinkPartner("", null);
        dsp.assertEndpointGeneral(endpointCloned);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointCloned);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointCloned);
        dsp.clickScannersTab();
        dsp.assertScannersTab(endpointCloned.getScannerSettings());
        dsp.clickGeneralSettingsTab();
        dsp.toggleStatus(true);
        dsp.clickSaveExitEndpoint();
        dsp.searchNameIdInput(endpointCloned.getEndpointName());
        dsp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    @Test
    @Issue("WP-2442")
    @TmsLink("49982")
    @Description("Clone DSP RTB API Adapter Endpoint")
    public void cloneDspRtbAdapterEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        double endpointBidfloor = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1000, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.AUDIO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.NATIVE, randomUtil.getRandomBoolean());
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> bidfloorList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
        }};
        List<EndpointDemandDO.BannerSize> bannerSizesList = new ArrayList<>() {{
            add(new EndpointDemandDO.BannerSize().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM))));
            add(new EndpointDemandDO.BannerSize().setSize(iabSizesEnum.CUSTOM, 222, 444));
        }};
        List<EndpointDemandDO.VideoSize> videoSizeList = new ArrayList<>() {{
            add(new EndpointDemandDO.VideoSize().setPredefinedSize((videoSizeEnum) randomUtil.getRandomElement(Arrays.stream(videoSizeEnum.values()).toList().stream().filter(x -> x != videoSizeEnum.CUSTOM))));
            add(new EndpointDemandDO.VideoSize().setSize(videoSizeEnum.CUSTOM, 555, 888));
        }};
        Map<String, String> seatMap = new LinkedHashMap<>() {{
            put("seat2", "asdf567");
            put("seat3", "fab567-df12a2-cba357");
        }};
        List<EndpointDemandDO.ScannerSetting> scanners = new ArrayList<>() {{
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, 56));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 1));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 82));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.GEOEDGE, true));
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.IPV6, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.COMPRESSED, true);
        }};
        EndpointDemandDO endpointOriginal = new EndpointDemandDO("autotestDspAdapterClone" + rnd, endpointTypeEnum.RTB).setStatus(true).setEndpointUrl("http://testurl." + rnd + ".com").setAuctionType(auctionTypeEnum.SECOND).setRtbVersion(rtbVersionEnum.v26).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointBidfloor).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setBannerSizeList(bannerSizesList).setVideoSizeList(videoSizeList).setSeatMap(seatMap).setReportApiLinkPartner("https://ui.videowalldirect.com/api/statistic-report-d4dcc828f39c93665-s4f83e600769897e8813c9863p.json?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.JSON).addSspToAllowed("Native Supply #-1", true).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        //*General*//
        dsp.selectAdapterType(adapterTypeEnum.API);
        endpointOriginal.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion())
                .setApiAdapter((AdaptersEnums.adaptersApiEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointOriginal.getEndpointType(), true)))
                .addAdapterSettingName((String) randomUtil.getRandomElement(dsp.getAdapterSettingsList(endpointOriginal.getEndpointType(), endpointOriginal.getApiAdapter().publicName())));
        dsp.setupGeneralSettingsTab(endpointOriginal);
        //*GEO*//
        dsp.clickGeoSettingsTab();
        endpointOriginal.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(6));
        dsp.setGeoBlacklist(endpointOriginal.getGeoBlacklist());
        endpointOriginal.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 4))
                .setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(bidfloorList, 4));
        //*ADDITIONAL*//
        dsp.clickAdditionalInfoTab();
        endpointOriginal.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointOriginal);
        endpointOriginal.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 2))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 2))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        //*SCANNERS*//
        dsp.clickScannersTab();
        dsp.setupScannerSection(endpointOriginal.getScannerSettings());
        //*Save and Clone*//
        dsp.clickSaveExitEndpoint();
        endpointOriginal.setId(dsp.getEndpointId(endpointOriginal.getEndpointName()));
        dsp.searchNameIdInput(endpointOriginal.getEndpointName());
        dsp.clickCloneEndpoint(endpointOriginal.getId(), true);
        dsp.openClonedEndpointEdit(true);
        EndpointDemandDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointCloned.getEndpointName() + " copy1").setStatus(false).setId(dsp.getEndpointId(endpointCloned.getEndpointName()))
                .clearSeatMap().clearSspAllowedMap().clearSspBlockedMap().clearInventoryAllowedMap().clearInventoryBlockedMap().setReportApiLinkPartner("", null);
        dsp.assertEndpointGeneral(endpointCloned);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointCloned);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointCloned);
        dsp.clickScannersTab();
        dsp.assertScannersTab(endpointCloned.getScannerSettings());
        dsp.clickGeneralSettingsTab();
        dsp.toggleStatus(true);
        dsp.clickSaveExitEndpoint();
        dsp.searchNameIdInput(endpointCloned.getEndpointName());
        dsp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="Prebid endpoints">
    @Test
    @TmsLink("1207")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create Prebid endpoint with Adapter")
    public void createPrebidEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setDynamic(randomUtil.getRandomInt(1, 550000), randomUtil.getRandomInt(560000, 1100000), qpsFrequencyEnum.HOUR, qpsOptimizationEnum.CLICKS);
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.AUDIO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.NATIVE, randomUtil.getRandomBoolean());
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.IPV6, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.PREMIUM, true);
            put(advancedSettingsEnum.REWARDED, true);
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestPrebid" + rnd, endpointTypeEnum.PREBID).setStatus(true).setAuctionType(auctionTypeEnum.FIRST).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.selectEndpointType(endpointData.getEndpointType());
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion())
                .setPrebidAdapter((AdaptersEnums.adaptersPrebidEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointData.getEndpointType(), true)))
                .addAdapterSettingName((String) randomUtil.getRandomElement(dsp.getAdapterSettingsList(endpointData.getEndpointType(), endpointData.getPrebidAdapter().publicName())));
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("4442")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create Prebid endpoint with 'Additional info' settings")
    public void createPrebidAdditional() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 45, 2), randomUtil.getRandomDoubleRounded(50, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, true);
            put(adFormatPlacementTypeEnum.AUDIO, false);
            put(adFormatPlacementTypeEnum.NATIVE, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, false);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_DSP, true);
            put(advancedSettingsEnum.PREMIUM, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        List<EndpointDemandDO.BannerSize> bannerSizesList = new ArrayList<>() {{
            add(new EndpointDemandDO.BannerSize().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM))));
            add(new EndpointDemandDO.BannerSize().setSize(iabSizesEnum.CUSTOM, 222, 444));
        }};
        List<EndpointDemandDO.VideoSize> videoSizeList = new ArrayList<>() {{
            add(new EndpointDemandDO.VideoSize().setPredefinedSize((videoSizeEnum) randomUtil.getRandomElement(Arrays.stream(videoSizeEnum.values()).toList().stream().filter(x -> x != videoSizeEnum.CUSTOM))));
            add(new EndpointDemandDO.VideoSize().setSize(videoSizeEnum.CUSTOM, 555, 888));
        }};
        Map<endpointPartnerApiColumnsEnum, String> csvColumnsMap = new HashMap<>() {{
            put(endpointPartnerApiColumnsEnum.DATE, "date_export");
            put(endpointPartnerApiColumnsEnum.IMPRESSION, "imps");
            put(endpointPartnerApiColumnsEnum.SPEND, "ssp_price");
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestPrebidAdditional" + rnd, endpointTypeEnum.PREBID).setStatus(true).setAuctionType(auctionTypeEnum.FIRST).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setBannerSizeList(bannerSizesList).setVideoSizeList(videoSizeList).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.csv?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.CSV).setCsvColumnsMap(csvColumnsMap).addSspToAllowed("Native Supply #-1", true);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.selectEndpointType(endpointData.getEndpointType());
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion())
                .setPrebidAdapter((AdaptersEnums.adaptersPrebidEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointData.getEndpointType(), true)))
                .addAdapterSettingName((String) randomUtil.getRandomElement(dsp.getAdapterSettingsList(endpointData.getEndpointType(), endpointData.getPrebidAdapter().publicName())));
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickAdditionalInfoTab();
        endpointData.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointData);
        endpointData.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 1))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 1))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("30106")
    @Description("Create Prebid endpoint with 'Geo Settings'")
    public void createPrebidGeo() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        double endpointBidfloor = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setAdaptive();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, true);
            put(adFormatPlacementTypeEnum.AUDIO, false);
            put(adFormatPlacementTypeEnum.NATIVE, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, false);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.REWARDED, true);
            put(advancedSettingsEnum.NURL_DSP, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> bidfloorList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestPrebidGeo" + rnd, endpointTypeEnum.PREBID).setStatus(true).setAuctionType(auctionTypeEnum.SECOND).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointBidfloor).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.selectEndpointType(endpointData.getEndpointType());
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion())
                .setPrebidAdapter((AdaptersEnums.adaptersPrebidEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointData.getEndpointType(), true)))
                .addAdapterSettingName((String) randomUtil.getRandomElement(dsp.getAdapterSettingsList(endpointData.getEndpointType(), endpointData.getPrebidAdapter().publicName())));
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickGeoSettingsTab();
        endpointData.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(6));
        dsp.setGeoBlacklist(endpointData.getGeoBlacklist());
        endpointData.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 3));
        endpointData.setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(bidfloorList, 4));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1819")
    @Severity(SeverityLevel.NORMAL)
    @Description("Validate Prebid endpoint required settings")
    public void validatePrebidSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        double bidfloor = 45.4;
        int qps = 4269;
        EndpointDemandDO.QpsSetting qpsTemplate = new EndpointDemandDO.QpsSetting();
        EndpointCommonDO.MarginSettings marginTemplate = new EndpointDemandDO.MarginSettings();
        String name = "autotestPrebidValidation", company, adapterName, nameLong = "emulatekindbloominevolvefabricatesupposedisposaldifficultcommunityexcitedindulgencedesiredtemperamentsignatureaccordingsuperficialcomparablehistoricallylaw";
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.MOBILE_WEB, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(qps + 1);
        }};
        List<Double> bidfloorList = new ArrayList<>() {{
            add(bidfloor + 1);
        }};
        Map<Integer, Map.Entry<String, String>> geoWhitelistErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, geoValueHigh));
        }};
        Map<Integer, Map.Entry<String, String>> geoBidfloorErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, geoValueHigh));
        }};
        ScannerSetting protectedPostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(false, 1),
                pixalatePostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 0),
                whiteopsFraudSensor = new ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 123);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.selectEndpointType(endpointTypeEnum.PREBID);
        company = dsp.getRandomCompanyName(true);
        dsp.saveAndAssertValidationRequiredInfoPage(VALIDATION_REQUIRED_EMPTY, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), null, null, null);
        dsp.inputName("12");
        dsp.setupQpsLimit(qpsTemplate.setFixed(null));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(1100001));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, errorLimitQpsHigh, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(null, null, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(1100001, 1100002, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, errorLimitQpsHigh, errorMarginMaxMin2.replace("${minVal}", "1100002").replace("${maxVal}", "1100000"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(5555, 2222, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "5556"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(0, 0, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(null, null, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(0, 0, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(7474, 2525, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "7475"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(qps));
        dsp.inputName("<;:[]{}>?~");
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameFormat, errorCompanyEmpty, null, errorAdapterEmpty, null, null, null, null, null);
        dsp.inputName(nameLong);
        dsp.assertNameValueLength(nameLong);
        dsp.selectCompany(company);
        adapterName = ((AdaptersEnums.adaptersPrebidEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointTypeEnum.PREBID, false))).publicName();
        dsp.selectAdapterSettings(endpointTypeEnum.PREBID, adapterName, null, false);
        dsp.saveAndAssertValidationRequiredInfoPage(null, null, null, errorAdapterNoSetting, null, null, null, null, null);
        dsp.inputName(name);
        dsp.selectAdapterSettings(endpointTypeEnum.PREBID, adapterName, null, false);
        adapterName = ((AdaptersEnums.adaptersPrebidEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointTypeEnum.PREBID, true))).publicName();
        dsp.selectAdapterSettings(endpointTypeEnum.PREBID, adapterName, null, true);
        dsp.saveAndAssertValidationRequiredInfoPage(errorAdFormatsEmpty, null, null, null, null, null, null, null, null);
        dsp.toggleAdFormat(adFormatsMap);
        dsp.saveAndAssertValidationRequiredInfoPage(errorTrafficTypesEmpty, null, null, null, null, null, null, null, null);
        dsp.toggleTrafficType(trafficTypesMap);
        dsp.setMargin(marginTemplate.setFixed(101d));
        dsp.inputSpendLimit(2147483647);
        dsp.saveAndAssertValidationOptionalInfoPage(errorMarginHigh, null, null, errorSpendLimitHigh, null);
        dsp.setMargin(marginTemplate.setRange(102d, 101d));
        dsp.saveAndAssertValidationOptionalInfoPage(VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"), errorMarginMaxMin2.replace("${minVal}", "102").replace("${maxVal}", "100"), null, errorSpendLimitHigh, null);
        dsp.setMargin(marginTemplate.setRange(70d, 50d));
        dsp.inputSpendLimit(8564);
        dsp.saveAndAssertValidationOptionalInfoPage(VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "70"), null, null, null);
        dsp.setMargin(marginTemplate.setAdaptive());
        dsp.inputTmax(1100001);
        dsp.inputBidfloor(1100001d);
        dsp.saveAndAssertValidationOptionalInfoPage(null, null, errorMaxBidfloorHigh, null, errorTmaxHigh);
        dsp.inputTmax(256);
        dsp.inputBidfloor(bidfloor);
        dsp.clickGeoSettingsTab();
        dsp.clickGeoWhitelistAdd(1);
        dsp.clickGeoBidfloorAdd(1);
        dsp.setupGeoWhitelist(qpsList, null);
        dsp.setupGeoBidfloorCpm(bidfloorList, null);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertValidationGeoPage(geoWhitelistErrors, qps, geoBidfloorErrors, bidfloor);
        dsp.clickGeoWhitelistDelete(0);
        dsp.clickGeoBidfloorDelete(0);
        dsp.clickScannersTab();
        dsp.setScannerSetting(protectedPostbid);
        dsp.setScannerSetting(pixalatePostbid);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(protectedPostbid, false, validationScannerNotEnabled);
        dsp.assertScannerError(pixalatePostbid, true, validationScannerImpressionLow);
        dsp.setScannerSetting(whiteopsFraudSensor);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(whiteopsFraudSensor, true, VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"));
        softAssert.assertAll("Errors in Prebid DSP Validation messages:");
    }

    @Test
    @Issue("WP-2442")
    @TmsLink("49980")
    @Description("Clone DSP Prebid Endpoint")
    public void cloneDspPrebidEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        double endpointBidfloor = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setDynamic(randomUtil.getRandomInt(1, 550000), randomUtil.getRandomInt(560000, 1100000), (DemandEnum.qpsFrequencyEnum) randomUtil.getRandomElement(DemandEnum.qpsFrequencyEnum.values()), (DemandEnum.qpsOptimizationEnum) randomUtil.getRandomElement(DemandEnum.qpsOptimizationEnum.values()));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 40, 2), randomUtil.getRandomDoubleRounded(45, 80, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.AUDIO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.NATIVE, randomUtil.getRandomBoolean());
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.IPV6, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.PREMIUM, true);
            put(advancedSettingsEnum.REWARDED, true);
        }};
        List<EndpointDemandDO.BannerSize> bannerSizesList = new ArrayList<>() {{
            add(new EndpointDemandDO.BannerSize().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint").toList().stream().filter(x -> x != iabSizesEnum.CUSTOM))));
            add(new EndpointDemandDO.BannerSize().setSize(iabSizesEnum.CUSTOM, 222, 444));
        }};
        List<EndpointDemandDO.VideoSize> videoSizeList = new ArrayList<>() {{
            add(new EndpointDemandDO.VideoSize().setPredefinedSize((videoSizeEnum) randomUtil.getRandomElement(Arrays.stream(videoSizeEnum.values()).toList().stream().filter(x -> x != videoSizeEnum.CUSTOM))));
            add(new EndpointDemandDO.VideoSize().setSize(videoSizeEnum.CUSTOM, 555, 888));
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> bidfloorList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointBidfloor, 2));
        }};
        List<EndpointDemandDO.ScannerSetting> scanners = new ArrayList<>() {{
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, 53));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 22));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 75));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.GEOEDGE, true));
        }};
        EndpointDemandDO endpointOriginal = new EndpointDemandDO("autotestDspPrebidClone" + rnd, endpointTypeEnum.PREBID).setStatus(true).setAuctionType(auctionTypeEnum.FIRST).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointBidfloor).setTmax(700).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setBannerSizeList(bannerSizesList).setVideoSizeList(videoSizeList).setReportApiLinkPartner("https://ui.videowalldirect.com/api/statistic-report-d3c1fe29b1f0362e5-sa7f6890b10002273e83ed4f3p.xml?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.XML).addSspToAllowed("Native Supply #-1", true).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        //*General*//
        dsp.selectEndpointType(endpointOriginal.getEndpointType());
        endpointOriginal.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion())
                .setPrebidAdapter((AdaptersEnums.adaptersPrebidEnum) randomUtil.getRandomElement(dsp.getAdaptersList(endpointOriginal.getEndpointType(), true)))
                .addAdapterSettingName((String) randomUtil.getRandomElement(dsp.getAdapterSettingsList(endpointOriginal.getEndpointType(), endpointOriginal.getPrebidAdapter().publicName())));
        dsp.setupGeneralSettingsTab(endpointOriginal);
        //*GEO*//
        dsp.clickGeoSettingsTab();
        endpointOriginal.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(6));
        dsp.setGeoBlacklist(endpointOriginal.getGeoBlacklist());
        endpointOriginal.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 3))
                .setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(bidfloorList, 4));
        //*ADDITIONAL*//
        dsp.clickAdditionalInfoTab();
        endpointOriginal.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointOriginal);
        endpointOriginal.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 1))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 1))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        //*SCANNERS*//
        dsp.clickScannersTab();
        dsp.setupScannerSection(endpointOriginal.getScannerSettings());
        //*Save and Clone*//
        dsp.clickSaveExitEndpoint();
        endpointOriginal.setId(dsp.getEndpointId(endpointOriginal.getEndpointName()));
        dsp.searchNameIdInput(endpointOriginal.getEndpointName());
        dsp.clickCloneEndpoint(endpointOriginal.getId(), true);
        dsp.openClonedEndpointEdit(true);
        EndpointDemandDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointCloned.getEndpointName() + " copy1").setStatus(false).setId(dsp.getEndpointId(endpointCloned.getEndpointName()))
                .clearSeatMap().clearSspAllowedMap().clearSspBlockedMap().clearInventoryAllowedMap().clearInventoryBlockedMap().setReportApiLinkPartner("", null);
        dsp.assertEndpointGeneral(endpointCloned);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointCloned);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointCloned);
        dsp.clickScannersTab();
        dsp.assertScannersTab(endpointCloned.getScannerSettings());
        dsp.clickGeneralSettingsTab();
        dsp.toggleStatus(true);
        dsp.clickSaveExitEndpoint();
        dsp.searchNameIdInput(endpointCloned.getEndpointName());
        dsp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="VAST endpoints">
    @Test
    @TmsLink("4455")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create VAST endpoint")
    public void createVastEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        List<macroTagsTypesEnum> vastTagsList = new ArrayList<>() {{
            add(macroTagsTypesEnum.PAGE);
            add(macroTagsTypesEnum.SCHAIN);
            add(macroTagsTypesEnum.PLACEMENTID);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.GDPR, true);
            put(advancedSettingsEnum.IPV6, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestVAST" + rnd, endpointTypeEnum.VAST).setStatus(true).setEndpointUrl("https://vasturl" + rnd + ".com/?c=rtb&m=vast&key=5f7189533da4e2795a09944b22b0433c&ip=[IP]&w=[WIDTH]&h=[HEIGHT]").setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setJsVastUrl().setJsVastTagsList(vastTagsList);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true));
        endpointData.setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        endpointData.setEndpointUrl(endpointData.getVastUrl());
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("4440")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create VAST endpoint with 'Additional info' settings")
    public void createVastAdditional() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setAdaptive();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        List<macroTagsTypesEnum> vastTagsList = new ArrayList<>() {{
            add(macroTagsTypesEnum.COPPA);
            add(macroTagsTypesEnum.CAT);
            add(macroTagsTypesEnum.IFA);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.REWARDED, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.IPV6, true);
            put(advancedSettingsEnum.SYNCED, true);
        }};
        List<EndpointDemandDO.VideoSize> videoSizeList = new ArrayList<>() {{
            add(new EndpointDemandDO.VideoSize().setPredefinedSize((videoSizeEnum) randomUtil.getRandomElement(Arrays.stream(videoSizeEnum.values()).toList().stream().filter(x -> x != videoSizeEnum.CUSTOM))));
            add(new EndpointDemandDO.VideoSize().setSize(videoSizeEnum.CUSTOM, 555, 888));
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestVASTAdditional" + rnd, endpointTypeEnum.VAST).setStatus(true).setEndpointUrl("https://vasturl" + rnd + ".com/?c=rtb&m=vast&key=5f7189533da4e2795a09944b22b0433c&ip=[IP]&page=[PAGE_URL]&appname=[APP_NAME]&consent=[GDPR_CONSENT]").setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setJsVastUrl().setJsVastTagsList(vastTagsList).setVideoSizeList(videoSizeList).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.json?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.JSON).addSspToAllowed("Native Supply #-1", true);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        endpointData.setEndpointUrl(endpointData.getVastUrl());
        dsp.clickAdditionalInfoTab();
        endpointData.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointData);
        endpointData.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 1))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 1))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("30104")
    @Description("Create VAST endpoint with 'Geo Settings'")
    public void createVastGeo() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        double endpointCpm = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.GDPR, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.PREMIUM, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> cpmList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointCpm, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointCpm, 2));
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestVASTGeo" + rnd, endpointTypeEnum.VAST).setStatus(true).setEndpointUrl("https://vasturl" + rnd + ".com/?c=rtb&m=vast&key=5f7189533da4e2795a09944b22b0433f&device_make=[DEVICE_MAKE]&bundle=[APP_BUNDLE]&appname=[APP_NAME]&placementid=[PLACEMENT_ID]").setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointCpm).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickGeoSettingsTab();
        endpointData.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(4));
        dsp.setGeoBlacklist(endpointData.getGeoBlacklist());
        endpointData.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 3))
                .setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(cpmList, 4));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1224")
    @Description("Validate VAST endpoint required settings")
    public void validateVastSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int qps = 32000, spendLimit = randomUtil.getRandomInt(0, 2147483646);
        double cpm = randomUtil.getRandomDoubleRounded(0, 1100000, 2);
        String nameLong = "emulatekindbloominevolvefabricatesupposedisposaldifficultcommunityexcitedindulgencedesiredtemperamentsignatureaccordingsuperficialcomparablehistoricallylaw", company, endpointUrl = "https://vasturl.com/?c=rtb&m=vast&key=5f7189533da4e2795a09944b22b0433c&ip=[IP]&w=[WIDTH]&h=[HEIGHT]";
        EndpointDemandDO.QpsSetting qpsTemplate = new EndpointDemandDO.QpsSetting();
        EndpointCommonDO.MarginSettings marginTemplate = new EndpointDemandDO.MarginSettings();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.APP, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(qps + 1);
        }};
        List<Double> cpmList = new ArrayList<>() {{
            add(cpm + 1);
        }};
        Map<Integer, Map.Entry<String, String>> geoWhitelistErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, geoValueHigh));
        }};
        Map<Integer, Map.Entry<String, String>> geoCPMErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, null));
        }};
        ScannerSetting protectedPostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(false, 1),
                pixalatePostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 0),
                whiteopsFraudSensor = new ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 123);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.selectEndpointType(endpointTypeEnum.VAST);
        company = dsp.getRandomCompanyName(true);
        dsp.saveAndAssertValidationRequiredInfoPage(VALIDATION_REQUIRED_EMPTY, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), null, null, null);
        dsp.inputName("12");
        dsp.setupQpsLimit(qpsTemplate.setFixed(null));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(1100001));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, errorLimitQpsHigh, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(null, null, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(1100001, 1100002, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, errorLimitQpsHigh, errorMarginMaxMin2.replace("${minVal}", "1100002").replace("${maxVal}", "1100000"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(5555, 2222, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "5556"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(0, 0, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(null, null, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(0, 0, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(7474, 2525, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "7475"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(qps));
        dsp.inputName("<;:[]{}>?~");
        dsp.inputCpm(0d);
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameFormat, errorCompanyEmpty, errorVastTagEmpty, null, null, null, null, null, errorCpmLimitLow);
        dsp.inputCpm(1100001d);
        dsp.inputName(nameLong);
        dsp.assertNameValueLength(nameLong);
        dsp.selectCompany(company);
        dsp.inputEndpointUrl("vasturl.com");
        dsp.saveAndAssertValidationRequiredInfoPage(errorAdFormatsEmptyVast, null, errorVastTagInvalid, null, null, null, null, null, errorCpmLimitHigh);
        dsp.inputEndpointUrl(endpointUrl);
        dsp.inputCpm(cpm);
        dsp.toggleAdFormat(adFormatsMap);
        dsp.saveAndAssertValidationRequiredInfoPage(errorTrafficTypesEmpty, null, null, null, null, null, null, null, null);
        dsp.toggleTrafficType(trafficTypesMap);
        dsp.setMargin(marginTemplate.setFixed(101d));
        dsp.inputSpendLimit(2147483647);
        dsp.saveAndAssertValidationOptionalInfoPage(errorMarginHigh, null, null, errorSpendLimitHigh, null);
        dsp.inputSpendLimit(spendLimit);
        dsp.setMargin(marginTemplate.setRange(102d, 101d));
        dsp.saveAndAssertValidationOptionalInfoPage(VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"), errorMarginMaxMin2.replace("${minVal}", "102").replace("${maxVal}", "100"), null, errorSpendLimitHigh, null);
        dsp.setMargin(marginTemplate.setRange(70d, 50d));
        dsp.saveAndAssertValidationOptionalInfoPage(null, errorMarginMaxMin.replace("${minValue}", "70"), null, null, null);
        dsp.setMargin(marginTemplate.setAdaptive());
        dsp.clickGeoSettingsTab();
        dsp.clickGeoWhitelistAdd(1);
        dsp.clickGeoBidfloorAdd(1);
        dsp.setupGeoWhitelist(qpsList, null);
        dsp.setupGeoBidfloorCpm(cpmList, null);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertValidationGeoPage(geoWhitelistErrors, qps, geoCPMErrors, cpm);
        dsp.clickGeoWhitelistDelete(0);
        dsp.clickGeoBidfloorDelete(0);
        dsp.clickScannersTab();
        dsp.setScannerSetting(protectedPostbid);
        dsp.setScannerSetting(pixalatePostbid);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(protectedPostbid, false, validationScannerNotEnabled);
        dsp.assertScannerError(pixalatePostbid, true, validationScannerImpressionLow);
        dsp.setScannerSetting(whiteopsFraudSensor);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(whiteopsFraudSensor, true, VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"));
        softAssert.assertAll("Errors in VAST DSP Validation messages:");
    }

    @Test
    @Issue("WP-2442")
    @TmsLink("49979")
    @Description("Clone DSP VAST Endpoint")
    public void cloneDspVastEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        double endpointCpm = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 50, 2), randomUtil.getRandomDoubleRounded(50, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, true);
        }};
        List<macroTagsTypesEnum> vastTagsList = new ArrayList<>() {{
            add(macroTagsTypesEnum.PAGE);
            add(macroTagsTypesEnum.SCHAIN);
            add(macroTagsTypesEnum.PLACEMENTID);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.REWARDED, true);
            put(advancedSettingsEnum.GDPR, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> cpmList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointCpm, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointCpm, 2));
        }};
        List<EndpointDemandDO.VideoSize> videoSizeList = new ArrayList<>() {{
            add(new EndpointDemandDO.VideoSize().setPredefinedSize((videoSizeEnum) randomUtil.getRandomElement(Arrays.stream(videoSizeEnum.values()).toList().stream().filter(x -> x != videoSizeEnum.CUSTOM))));
            add(new EndpointDemandDO.VideoSize().setSize(videoSizeEnum.CUSTOM, 555, 888));
        }};
        List<EndpointDemandDO.ScannerSetting> scanners = new ArrayList<>() {{
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, 21));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 57));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 33));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.GEOEDGE, true));
        }};
        EndpointDemandDO endpointOriginal = new EndpointDemandDO("autotestDspVastClone" + rnd, endpointTypeEnum.VAST).setStatus(true).setEndpointUrl("https://vasturl" + rnd + ".com/?c=rtb&m=vast&key=5f7189533da4e2795a09944b22b0433c&ip=[IP]&w=[WIDTH]&h=[HEIGHT]").setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointCpm).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setJsVastUrl().setJsVastTagsList(vastTagsList).setVideoSizeList(videoSizeList).setReportApiLinkPartner("https://smartssp.iqzone.com/api/statistic-report-dfff31f7eb95b142c-s106dbce7b760a26e786996dfp.json?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.JSON).addSspToAllowed("Native Supply #-1", true).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        //*General*//
        endpointOriginal.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion());
        dsp.setupGeneralSettingsTab(endpointOriginal);
        endpointOriginal.setEndpointUrl(endpointOriginal.getVastUrl());
        //*GEO*//
        dsp.clickGeoSettingsTab();
        endpointOriginal.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(4));
        dsp.setGeoBlacklist(endpointOriginal.getGeoBlacklist());
        endpointOriginal.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 3))
                .setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(cpmList, 4));
        //*ADDITIONAL*//
        dsp.clickAdditionalInfoTab();
        endpointOriginal.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointOriginal);
        endpointOriginal.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 1))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 1))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        //*SCANNERS*//
        dsp.clickScannersTab();
        dsp.setupScannerSection(endpointOriginal.getScannerSettings());
        //*Save and Clone*//
        dsp.clickSaveExitEndpoint();
        endpointOriginal.setId(dsp.getEndpointId(endpointOriginal.getEndpointName()));
        dsp.searchNameIdInput(endpointOriginal.getEndpointName());
        dsp.clickCloneEndpoint(endpointOriginal.getId(), true);
        dsp.openClonedEndpointEdit(true);
        EndpointDemandDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointCloned.getEndpointName() + " copy1").setStatus(false).setId(dsp.getEndpointId(endpointCloned.getEndpointName()))
                .clearSspAllowedMap().clearSspBlockedMap().clearInventoryAllowedMap().clearInventoryBlockedMap().setReportApiLinkPartner("", null);
        dsp.assertEndpointGeneral(endpointCloned);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointCloned);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointCloned);
        dsp.clickScannersTab();
        dsp.assertScannersTab(endpointCloned.getScannerSettings());
        dsp.clickGeneralSettingsTab();
        dsp.toggleStatus(true);
        dsp.clickSaveExitEndpoint();
        dsp.searchNameIdInput(endpointCloned.getEndpointName());
        dsp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="JS Tag endpoints">
    @Test
    @TmsLink("1614")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Create Js Tag endpoint")
    public void createJsTagEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setDynamic(randomUtil.getRandomInt(1, 550000), randomUtil.getRandomInt(560000, 1100000), (DemandEnum.qpsFrequencyEnum) randomUtil.getRandomElement(DemandEnum.qpsFrequencyEnum.values()), (DemandEnum.qpsOptimizationEnum) randomUtil.getRandomElement(DemandEnum.qpsOptimizationEnum.values()));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.SYNCED, true);
            put(advancedSettingsEnum.COMPRESSED, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        List<macroTagsTypesEnum> jsTagsList = new ArrayList<>() {{
            add(macroTagsTypesEnum.PAGE);
            add(macroTagsTypesEnum.SCHAIN);
            add(macroTagsTypesEnum.UA);
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestJsTag" + rnd, endpointTypeEnum.JS).setStatus(true).setEndpointUrl("https://jstagurl" + rnd + ".com/?c=rtb&m=js&type=s2s&key=791549b1169bd161f1433b793e5f3e5e&ifa=[IFA]&bidfloor=[BIDFLOOR]").setJsTagCustomSize(128, 367).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setJsVastUrl().setJsVastTagsList(jsTagsList);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        endpointData.setEndpointUrl(endpointData.getVastUrl())
                .addAdFormatSetting(adFormatPlacementTypeEnum.BANNER, true);
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("4443")
    @Severity(SeverityLevel.NORMAL)
    @Description("Create Js Tag endpoint with 'Additional info' settings")
    public void createJsTagAdditional() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setDynamic(randomUtil.getRandomInt(1, 550000), randomUtil.getRandomInt(560000, 1100000), qpsFrequencyEnum.MIN30, qpsOptimizationEnum.SPEND);
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setAdaptive();
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.REWARDED, true);
            put(advancedSettingsEnum.COMPRESSED, true);
            put(advancedSettingsEnum.GDPR, true);
        }};
        List<macroTagsTypesEnum> jsTagsList = new ArrayList<>() {{
            add(macroTagsTypesEnum.SCHAIN);
            add(macroTagsTypesEnum.IP);
            add(macroTagsTypesEnum.BIDFLOOR);
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestJsTagAdditional" + rnd, endpointTypeEnum.JS).setStatus(true).setEndpointUrl("https://jstagurl" + rnd + ".com/?c=rtb&m=js&type=s2s&key=791549b1169bd161f1433b793e5f3e5e&consent=[GDPR_CONSENT]&lat=[LOCATION_LAT]&lon=[LOCATION_LON]").setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setJsVastUrl().setJsVastTagsList(jsTagsList).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.json?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.JSON).addSspToAllowed("Native Supply #-1", true);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        endpointData.setEndpointUrl(endpointData.getVastUrl())
                .addAdFormatSetting(adFormatPlacementTypeEnum.BANNER, true);
        dsp.clickAdditionalInfoTab();
        endpointData.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointData);
        endpointData.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 1))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 1))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("30105")
    @Description("Create Js Tag endpoint with 'Geo Settings'")
    public void createJsTagGeo() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        Double endpointCpm = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setDynamic(randomUtil.getRandomInt(1, 550000), randomUtil.getRandomInt(560000, 1100000), (DemandEnum.qpsFrequencyEnum) randomUtil.getRandomElement(DemandEnum.qpsFrequencyEnum.values()), (DemandEnum.qpsOptimizationEnum) randomUtil.getRandomElement(DemandEnum.qpsOptimizationEnum.values()));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 40, 2), randomUtil.getRandomDoubleRounded(41, 100, 2));
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.GDPR, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.PREMIUM, true);
        }};
        List<macroTagsTypesEnum> jsTagsList = new ArrayList<>() {{
            add(macroTagsTypesEnum.LAT);
            add(macroTagsTypesEnum.LON);
            add(macroTagsTypesEnum.W);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> cpmList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, endpointCpm.intValue(), 2));
            add(randomUtil.getRandomDoubleRounded(0, endpointCpm.intValue(), 2));
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestJsTagGeo" + rnd, endpointTypeEnum.JS).setStatus(true).setEndpointUrl("https://jstagurl" + rnd + ".com/?c=rtb&m=vast&key=5f7189533da4e2795a09944b22b0433f&device_make=[DEVICE_MAKE]&appname=[APP_NAME]").setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setJsVastUrl().setJsVastTagsList(jsTagsList);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        endpointData.setEndpointUrl(endpointData.getVastUrl())
                .addAdFormatSetting(adFormatPlacementTypeEnum.BANNER, true);
        dsp.clickGeoSettingsTab();
        endpointData.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(4));
        dsp.setGeoBlacklist(endpointData.getGeoBlacklist());
        endpointData.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 3))
                .setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(cpmList, 4));
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.assertEndpointRowInfo(endpointData);
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.assertEndpointGeneral(endpointData);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1818")
    @Description("Validate Js Tag endpoint required settings")
    public void validateJsTagSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int qps = 32333, spendLimit = randomUtil.getRandomInt(0, 2147483646);
        double cpm = randomUtil.getRandomDoubleRounded(0, 1100000, 2);
        String nameLong = "emulatekindbloominevolvefabricatesupposedisposaldifficultcommunityexcitedindulgencedesiredtemperamentsignatureaccordingsuperficialcomparablehistoricallylaw", company, endpointUrl = "https://jstagurl.com/?c=rtb&m=vast&key=5f7189533da4e2795a09944b22b0433c&ip=[IP]&w=[WIDTH]&h=[HEIGHT]";
        EndpointDemandDO.QpsSetting qpsTemplate = new EndpointDemandDO.QpsSetting();
        EndpointCommonDO.MarginSettings marginTemplate = new EndpointDemandDO.MarginSettings();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, false);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.APP, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(qps + 1);
        }};
        List<Double> cpmList = new ArrayList<>() {{
            add(cpm + 1);
        }};
        Map<Integer, Map.Entry<String, String>> geoWhitelistErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, geoValueHigh));
        }};
        Map<Integer, Map.Entry<String, String>> geoCPMErrors = new HashMap<>() {{
            put(0, new AbstractMap.SimpleEntry<>(geoNoCountry, null));
        }};
        ScannerSetting protectedPostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(false, 1),
                pixalatePostbid = new ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 0),
                whiteopsFraudSensor = new ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 123);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.selectEndpointType(endpointTypeEnum.JS);
        company = dsp.getRandomCompanyName(true);
        dsp.saveAndAssertValidationRequiredInfoPage(VALIDATION_REQUIRED_EMPTY, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), null, null, null);
        dsp.inputName("12");
        dsp.setupQpsLimit(qpsTemplate.setFixed(null));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(1100001));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, errorLimitQpsHigh, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(null, null, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(1100001, 1100002, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, errorLimitQpsHigh, errorMarginMaxMin2.replace("${minVal}", "1100002").replace("${maxVal}", "1100000"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(5555, 2222, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "5556"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDynamic(0, 0, qpsFrequencyEnum.MIN15, qpsOptimizationEnum.RCPM));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(null, null, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_REQUIRED_EMPTY, VALIDATION_REQUIRED_EMPTY, null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(0, 0, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "1"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "2"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setDirect(7474, 2525, true, true));
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameShort, null, null, null, null, VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "7475"), null, null, null);
        dsp.setupQpsLimit(qpsTemplate.setFixed(qps));
        dsp.inputName("<;:[]{}>?~");
        dsp.saveAndAssertValidationRequiredInfoPage(errorNameFormat, errorCompanyEmpty, errorJsTagEmpty, null, null, null, null, null, errorCpmLimitLow);
        dsp.inputCpm(0d);
        dsp.inputName(nameLong);
        dsp.assertNameValueLength(nameLong);
        dsp.selectCompany(company);
        dsp.inputEndpointUrl(endpointUrl);
        dsp.toggleAdFormat(adFormatsMap);
        dsp.saveAndAssertValidationRequiredInfoPage(errorAdFormatsEmptyJs, null, null, null, null, null, null, null, errorCpmLimitLow);
        dsp.inputCpm(1100001d);
        adFormatsMap.replace(adFormatPlacementTypeEnum.BANNER, true);
        dsp.toggleAdFormat(adFormatsMap);
        dsp.saveAndAssertValidationRequiredInfoPage(errorTrafficTypesJsEmpty, null, null, null, null, null, null, null, errorCpmLimitHigh);
        dsp.inputCpm(cpm);
        dsp.toggleTrafficType(trafficTypesMap);
        dsp.setMargin(marginTemplate.setFixed(101d));
        dsp.inputSpendLimit(2147483647);
        dsp.saveAndAssertValidationOptionalInfoPage(errorMarginHigh, null, null, errorSpendLimitHigh, null);
        dsp.setMargin(marginTemplate.setRange(102d, 101d));
        dsp.saveAndAssertValidationOptionalInfoPage(VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"), errorMarginMaxMin2.replace("${minVal}", "102").replace("${maxVal}", "100"), null, errorSpendLimitHigh, null);
        dsp.inputSpendLimit(spendLimit);
        dsp.setMargin(marginTemplate.setRange(70d, 50d));
        dsp.saveAndAssertValidationOptionalInfoPage(VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"), VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "70"), null, null, null);
        dsp.setMargin(marginTemplate.setAdaptive());
        dsp.clickGeoSettingsTab();
        dsp.clickGeoWhitelistAdd(1);
        dsp.clickGeoBidfloorAdd(1);
        dsp.setupGeoWhitelist(qpsList, null);
        dsp.setupGeoBidfloorCpm(cpmList, null);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertValidationGeoPage(geoWhitelistErrors, qps, geoCPMErrors, cpm);
        dsp.clickGeoWhitelistDelete(0);
        dsp.clickGeoBidfloorDelete(0);
        dsp.clickScannersTab();
        dsp.setScannerSetting(protectedPostbid);
        dsp.setScannerSetting(pixalatePostbid);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(protectedPostbid, false, validationScannerNotEnabled);
        dsp.assertScannerError(pixalatePostbid, true, validationScannerImpressionLow);
        dsp.setScannerSetting(whiteopsFraudSensor);
        dsp.clickSaveEndpointWithErrors();
        dsp.assertScannerError(whiteopsFraudSensor, true, VALIDATION_VALUE_TOO_HIGH.replace("${nmb}", "100"));
        softAssert.assertAll("Errors in JS DSP Validation messages:");
    }

    @Test
    @Issue("WP-2442")
    @TmsLink("49981")
    @Description("Clone DSP JS Tag Endpoint")
    public void cloneDspJsTagEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        double endpointCpm = randomUtil.getRandomDoubleRounded(1, 1100000, 2);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setDynamic(randomUtil.getRandomInt(1, 550000), randomUtil.getRandomInt(560000, 1100000), (DemandEnum.qpsFrequencyEnum) randomUtil.getRandomElement(DemandEnum.qpsFrequencyEnum.values()), (DemandEnum.qpsOptimizationEnum) randomUtil.getRandomElement(DemandEnum.qpsOptimizationEnum.values()));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 50, 2), randomUtil.getRandomDoubleRounded(50, 100, 2));
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        List<macroTagsTypesEnum> jsTagList = new ArrayList<>() {{
            add(macroTagsTypesEnum.IFA);
            add(macroTagsTypesEnum.SCHAIN);
            add(macroTagsTypesEnum.PAGE);
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_DSP, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.COMPRESSED, true);
        }};
        List<Integer> qpsList = new ArrayList<>() {{
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
            add(randomUtil.getRandomInt(1, qpsSettings.getMax() / 2));
        }};
        List<Double> cpmList = new ArrayList<>() {{
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointCpm, 2));
            add(randomUtil.getRandomDoubleRounded(0, (int) endpointCpm, 2));
        }};
        List<EndpointDemandDO.ScannerSetting> scanners = new ArrayList<>() {{
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, 21));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, 57));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, 33));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.GEOEDGE, true));
        }};
        EndpointDemandDO endpointOriginal = new EndpointDemandDO("autotestDspJsTagClone" + rnd, endpointTypeEnum.JS).setStatus(true).setEndpointUrl("https://jstag.clone-" + rnd + ".com/?c=rtb&m=vast&key=791549b1169bd161f1433b793e5f3e5e&consent=[GDPR_CONSENT]&lat=[LOCATION_LAT]&lon=[LOCATION_LON]").setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(endpointCpm).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setJsVastUrl().setJsVastTagsList(jsTagList).setReportApiLinkPartner("https://smartssp.iqzone.com/api/statistic-report-dfff31f7eb95b142c-s106dbce7b760a26e786996dfp.json?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.JSON).addSspToAllowed("Native Supply #-1", true).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        //*General*//
        endpointOriginal.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getNativeRegion());
        dsp.setupGeneralSettingsTab(endpointOriginal);
        endpointOriginal.setEndpointUrl(endpointOriginal.getVastUrl())
                .addAdFormatSetting(adFormatPlacementTypeEnum.BANNER, true);
        //*GEO*//
        dsp.clickGeoSettingsTab();
        endpointOriginal.setGeoBlacklist(dsp.getRandomBlacklistCountriesMap(4));
        dsp.setGeoBlacklist(endpointOriginal.getGeoBlacklist());
        endpointOriginal.setGeoQpsWhitelist(dsp.setupGeoWhitelist(qpsList, 3))
                .setGeoBidfloorCpm(dsp.setupGeoBidfloorCpm(cpmList, 4));
        //*ADDITIONAL*//
        dsp.clickAdditionalInfoTab();
        endpointOriginal.setOsMap(dsp.getRandomOsMap(4));
        dsp.setupAdditionalInfo(endpointOriginal);
        endpointOriginal.setSspAllowedMap(dsp.selectRandomAllowedBlockedSsp(true, 1))
                .setSspBlockedMap(dsp.selectRandomAllowedBlockedSsp(false, 1))
                .setInventoryAllowedMap(dsp.selectRandomAllowedBlockedInventory(true, 2))
                .setInventoryBlockedMap(dsp.selectRandomAllowedBlockedInventory(false, 2));
        //*SCANNERS*//
        dsp.clickScannersTab();
        dsp.setupScannerSection(endpointOriginal.getScannerSettings());
        //*Save and Clone*//
        dsp.clickSaveExitEndpoint();
        endpointOriginal.setId(dsp.getEndpointId(endpointOriginal.getEndpointName()));
        dsp.searchNameIdInput(endpointOriginal.getEndpointName());
        dsp.clickCloneEndpoint(endpointOriginal.getId(), true);
        dsp.openClonedEndpointEdit(true);
        EndpointDemandDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointCloned.getEndpointName() + " copy1").setStatus(false).setId(dsp.getEndpointId(endpointCloned.getEndpointName()))
                .clearSspAllowedMap().clearSspBlockedMap().clearInventoryAllowedMap().clearInventoryBlockedMap().setReportApiLinkPartner("", null);
        dsp.assertEndpointGeneral(endpointCloned);
        dsp.clickGeoSettingsTab();
        dsp.assertEndpointGeoSettings(endpointCloned);
        dsp.clickAdditionalInfoTab();
        dsp.assertEndpointAdditionalInfo(endpointCloned);
        dsp.clickScannersTab();
        dsp.assertScannersTab(endpointCloned.getScannerSettings());
        dsp.clickGeneralSettingsTab();
        dsp.toggleStatus(true);
        dsp.clickSaveExitEndpoint();
        dsp.searchNameIdInput(endpointCloned.getEndpointName());
        dsp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="DSP List">
    @Test(priority = 3)
    @TmsLink("1222")
    @Description("Search SSP by Name/ID")
    public void searchDSPByNameId() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 5), dspId;
        List<String> dspNames;
        List<Integer> dspIds;
        String dspName, dspSearch;
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dspNames = dsp.getEndpointNamesList(true);
        dspName = (String) randomUtil.getRandomElement(dspNames);
        dspSearch = dspName.substring(0, rnd).toLowerCase();
        dsp.searchNameIdInput(dspSearch);
        dsp.assertAllEndpointRowsNames(dspSearch);
        dsp.searchNameIdInput("dGjhhyhv787ghve");
        dsp.assertDashboardEmpty();
        dspIds = dsp.getEndpointIdsList(true);
        dspId = (Integer) randomUtil.getRandomElement(dspIds);
        dsp.searchNameIdInput(String.valueOf(dspId));
        dsp.assertAllEndpointRowsIds(String.valueOf(dspId));
        softAssert.assertAll("Errors in DSP Dashboard search");
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("39390")
    @Description("Export DSP list as CSV")
    public void exportDspList() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        File dspExport;
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsFilterMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
        }};
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dspExport = dsp.clickExportCsv();
        dsp.assertFileExportCsv(dspExport);
        dsp.clickOpenFilters();
        dsp.filterSelectAdFormat(adFormatsFilterMap);
        dsp.clickCloseFilters();
        dspExport = dsp.clickExportCsv();
        dsp.assertFileExportCsv(dspExport);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1217")
    @Description("Endpoints table check and sorting")
    public void sortDspList() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.assertTableSorting(dashboardColumnsEnum.ID, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.ID);
        dsp.assertTableSorting(dashboardColumnsEnum.ID, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.NAME);
        dsp.assertTableSorting(dashboardColumnsEnum.NAME, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.NAME);
        dsp.assertTableSorting(dashboardColumnsEnum.NAME, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.REGION);    // TODO Will not work for the projects with 1 region
        dsp.assertTableSorting(dashboardColumnsEnum.REGION, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.REGION);
        dsp.assertTableSorting(dashboardColumnsEnum.REGION, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.QPS_LIMIT);
        dsp.assertTableSorting(dashboardColumnsEnum.QPS_LIMIT, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.QPS_LIMIT);
        dsp.assertTableSorting(dashboardColumnsEnum.QPS_LIMIT, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.QPS_REAL);
        dsp.assertTableSorting(dashboardColumnsEnum.QPS_REAL, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.QPS_REAL);
        dsp.assertTableSorting(dashboardColumnsEnum.QPS_REAL, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.QPS_BID);
        dsp.assertTableSorting(dashboardColumnsEnum.QPS_BID, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.QPS_BID);
        dsp.assertTableSorting(dashboardColumnsEnum.QPS_BID, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.SPEND_YESTERDAY);
        dsp.assertTableSorting(dashboardColumnsEnum.SPEND_YESTERDAY, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.SPEND_YESTERDAY);
        dsp.assertTableSorting(dashboardColumnsEnum.SPEND_YESTERDAY, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.SPEND_TODAY);
        dsp.assertTableSorting(dashboardColumnsEnum.SPEND_TODAY, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.SPEND_TODAY);
        dsp.assertTableSorting(dashboardColumnsEnum.SPEND_TODAY, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.SPEND_LIMIT);
        dsp.assertTableSorting(dashboardColumnsEnum.SPEND_LIMIT, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.SPEND_LIMIT);
        dsp.assertTableSorting(dashboardColumnsEnum.SPEND_LIMIT, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.WIN_RATE);
        dsp.assertTableSorting(dashboardColumnsEnum.WIN_RATE, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.WIN_RATE);
        dsp.assertTableSorting(dashboardColumnsEnum.WIN_RATE, true);
        dsp.clickColumnSorting(dashboardColumnsEnum.COMPANY_ID);
        dsp.assertTableSorting(dashboardColumnsEnum.COMPANY_ID, false);
        dsp.clickColumnSorting(dashboardColumnsEnum.COMPANY_ID);
        dsp.assertTableSorting(dashboardColumnsEnum.COMPANY_ID, true);
        softAssert.assertAll("Errors in DSP Dashboard sorting");
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3231")
    @Description("Filter DSP dashboard by Status")
    public void filterDspByStatus() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.assertDspFilterStatus(true);
        dsp.clickOpenFilters();
        dsp.filterSelectStatus(null);
        dsp.clickCloseFilters();
        dsp.assertDspFilterStatus(null);
        dsp.clickOpenFilters();
        dsp.filterSelectStatus(false);
        dsp.clickCloseFilters();
        dsp.assertDspFilterStatus(false);
        softAssert.assertAll("Errors in DSP Dashboard Status filter");
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3268")
    @Description("Filter DSP dashboard by Endpoint type")
    public void filterDspByType() {
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO();
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickOpenFilters();
        //Removing the Status filter
        dsp.filterSelectStatus(null);
        dsp.filterSelectEndpointType(endpointTypeEnum.RTB);
        dsp.clickCloseFilters();
        dsp.assertDspFilterType(endpointTypeEnum.RTB, null);
        dsp.clickOpenFilters();
        dsp.filterSelectEndpointType(endpointTypeEnum.VAST);
        dsp.clickCloseFilters();
        dsp.assertDspFilterType(endpointTypeEnum.VAST, null);
        dsp.clickOpenFilters();
        dsp.filterSelectEndpointType(endpointTypeEnum.JS);
        dsp.clickCloseFilters();
        dsp.assertDspFilterType(endpointTypeEnum.JS, null);
        dsp.clickOpenFilters();
        dsp.filterSelectEndpointType(endpointTypeEnum.PREBID);
        dsp.clickCloseFilters();
        dsp.assertDspFilterType(endpointTypeEnum.PREBID, null);
        dsp.clickOpenFilters();
        dsp.filterSelectEndpointType(null);
        dsp.clickCloseFilters();
        dsp.assertDspFilterType(null, null);
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1221")
    @Description("Filter DSP dashboard by Traffic type")
    public void filterDspByTraffic() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        Map<trafficTypeEnum, Boolean> desktopMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, false);
            put(trafficTypeEnum.MOBILE_WEB, false);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<trafficTypeEnum, Boolean> mobileWebMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, false);
            put(trafficTypeEnum.APP, false);
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<trafficTypeEnum, Boolean> mobileAppMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, false);
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, false);
            put(trafficTypeEnum.CTV, false);
        }};
        Map<trafficTypeEnum, Boolean> ctvMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, false);
            put(trafficTypeEnum.MOBILE_WEB, false);
            put(trafficTypeEnum.CTV, false);
        }};
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickOpenFilters();
        //Removing the Status filter
        dsp.filterSelectStatus(null);
        dsp.filterSelectTrafficType(desktopMap);
        dsp.clickCloseFilters();
        dsp.assertDspFilterTraffic(desktopMap, null);
        dsp.clickOpenFilters();
        dsp.filterSelectTrafficType(mobileWebMap);
        dsp.clickCloseFilters();
        dsp.assertDspFilterTraffic(mobileWebMap, null);
        dsp.clickOpenFilters();
        dsp.filterSelectTrafficType(mobileAppMap);
        dsp.clickCloseFilters();
        dsp.assertDspFilterTraffic(mobileAppMap, null);
        dsp.clickOpenFilters();
        dsp.filterSelectTrafficType(ctvMap);
        dsp.clickCloseFilters();
        dsp.assertDspFilterTraffic(ctvMap, null);
        dsp.clickOpenFilters();
        dsp.filterSelectTrafficType(null);
        dsp.clickCloseFilters();
        dsp.assertDspFilterTraffic(null, null);
        softAssert.assertAll("Errors in DSP Dashboard Traffic filter");
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1220")
    @Description("Filter DSP dashboard by Ad format")
    public void filterDspByAdFormat() {
        Map<adFormatPlacementTypeEnum, Boolean> bannerMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, false);
            put(adFormatPlacementTypeEnum.NATIVE, false);
        }};
        Map<adFormatPlacementTypeEnum, Boolean> videoMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, false);
            put(adFormatPlacementTypeEnum.NATIVE, false);
        }};
        Map<adFormatPlacementTypeEnum, Boolean> nativeMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, false);
            put(adFormatPlacementTypeEnum.NATIVE, false);
        }};
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO();
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickOpenFilters();
        //Removing the Status filter
        dsp.filterSelectStatus(null);
        dsp.filterSelectAdFormat(bannerMap);
        dsp.clickCloseFilters();
        dsp.assertDspFilterAdFormat(bannerMap, null);
        dsp.clickOpenFilters();
        dsp.filterSelectAdFormat(videoMap);
        dsp.clickCloseFilters();
        dsp.assertDspFilterAdFormat(videoMap, null);
        dsp.clickOpenFilters();
        dsp.filterSelectAdFormat(nativeMap);
        dsp.clickCloseFilters();
        dsp.assertDspFilterAdFormat(nativeMap, null);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("46490")
    @Description("Check for Native Demand #1 in the endpoint table")
    public void checkNativeDemandInDspTable() {
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO();
        EndpointDemandDO data = new EndpointDemandDO().setEndpointName("Native Demand").setId(1);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickOpenFilters();
        dsp.filterSelectStatus(null);
        dsp.clickCloseFilters();
        dsp.assertEndpointNotPresent(data);
    }

    //</editor-fold>

    //<editor-fold desc="Additional parameters">
    @Test
    @Issue("WP-1968")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3794")
    @Description("Check Filter list section in endpoint")
    public void checkFilterListTable() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        FilterListPO filter = new FilterListPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        BrowserUtility browserUtil = new BrowserUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        FilterListDO filterData1 = new FilterListDO("FilterFromDsp_1" + rnd, FilterListEnums.recordType.SITE_APP_ID, false, List.of("t567-dsf-fgdfsg-eef-45de")),
                filterData2 = new FilterListDO("FilterFromDsp_2_" + rnd, FilterListEnums.recordType.CRID, true, List.of("3fh5ue-4f5h-4f5h-4f5h-4f5h4f5h4f5h"));
        String search = filterData1.getFilterName().substring(0, randomUtil.getRandomInt(1, filterData1.getFilterName().length()));
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.clickFilterListsTab();
        dsp.clickCreateFilter();
        filter.setupFilterList(filterData1);
        filter.clickSaveFilterListAllDsp(true);
        filter.clickCreateFilterList();
        filter.setupFilterList(filterData2);
        filter.clickSaveFilterListAllDsp(true);
        browserUtil.closeTab();
        dsp.clickBreadcrumbItem(1);
        dsp.clickCreateEndpoint();
        dsp.clickFilterListsTab();
        filter.assertFilterInTable(filterData1);
        filter.assertFilterInTable(filterData2);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.UPDATED_DATE, false);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.UPDATED_DATE);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.UPDATED_DATE, true);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.FILTER_NAME);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.FILTER_NAME, false);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.FILTER_NAME);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.FILTER_NAME, true);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.LIST_TYPE);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.LIST_TYPE, false);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.LIST_TYPE);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.LIST_TYPE, true);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.RECORD_TYPE);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.RECORD_TYPE, false);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.RECORD_TYPE);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.RECORD_TYPE, true);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.CREATED_DATE);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.CREATED_DATE, false);
        filter.clickColumnSorting(FilterListEnums.tableColumnsEnum.CREATED_DATE);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.CREATED_DATE, true);
        filter.searchFilterInTable(search);
        filter.assertFilterTableNames(search);
        filter.searchFilterInTable("fdgdfgvfxr");
        filter.assertFilterTableEmpty(emptyFilterListSection);
        filter.searchFilterInTable(null);
        filter.assertFilterTableSorting(FilterListEnums.tableColumnsEnum.CREATED_DATE, true);
        softAssert.assertAll("Errors in Filter List section");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2253")
    @Description("Set up 'Scanners' settings for endpoint")
    public void createEndpointWithScanners() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        EndpointDemandDO.QpsSetting qpsSettings = new EndpointDemandDO.QpsSetting().setFixed(randomUtil.getRandomInt(1000, 1100000));
        EndpointDemandDO.MarginSettings marginSettings = new EndpointDemandDO.MarginSettings().setAdaptive();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.AUDIO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.NATIVE, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        List<EndpointDemandDO.ScannerSetting> scanners = new ArrayList<>() {{
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new EndpointDemandDO.ScannerSetting(ScannersEnums.scannerTypesEnum.GEOEDGE, true));
        }};
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestDspRtbScanners" + rnd, endpointTypeEnum.RTB).setStatus(true).setEndpointUrl("http://testurl." + rnd + ".com").setQpsSettings(qpsSettings).setSpendLimit(randomUtil.getRandomInt(1, 2147483646)).setBidFloorCpm(randomUtil.getRandomDoubleRounded(1, 1100000, 2)).setTmax(randomUtil.getRandomInt(500, 2500)).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setMarginSettings(marginSettings).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setRegion(dsp.getRandomRegion())
                .setCompany(dsp.getRandomCompanyName(true));
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickScannersTab();
        dsp.setupScannerSection(endpointData.getScannerSettings());
        dsp.clickSaveExitEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        dsp.searchNameIdInput(endpointData.getEndpointName());
        dsp.openEndpointEditPage(endpointData.getId());
        dsp.clickScannersTab();
        dsp.assertScannersTab(endpointData.getScannerSettings());
        dsp.clickGeneralSettingsTab();
        softAssert.assertAll("Errors with scanners in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("50011")
    @Description("Inventory Whitelist/Blacklist is displayed when the Allowed SSP is #-1 Native Supply")
    public void checkInventoryNativeSupply() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DemandPO dsp = new DemandPO(softAssert);
        List<regionsEnum> regionsList = dsp.getRegions();
        login.login(StaticData.supportDefaultUser);
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        regionsEnum nativeRegion = dsp.getNativeRegion();
        dsp.selectRegion(nativeRegion);
        dsp.clickAdditionalInfoTab();
        dsp.assertInventoryPresent(true);
        dsp.selectAllowedBlockedSsp(true, "Native Supply #-1", true);
        dsp.assertInventoryPresent(true);
        dsp.selectAllowedBlockedSsp(true, "Native Supply #-1", false);
        dsp.selectAllowedBlockedSsp(false, "Native Supply #-1", true);
        dsp.assertInventoryPresent(true);
        dsp.selectAllowedBlockedSsp(false, "Native Supply #-1", false);
        regionsList.remove(nativeRegion);
        for (regionsEnum region : regionsList) {
            dsp.clickGeneralSettingsTab();
            dsp.selectRegion(region);
            dsp.clickAdditionalInfoTab();
            dsp.assertInventoryPresent(false);
        }
        softAssert.assertAll("Errors in checking Inventory Whitelist/Blacklist for Native Supply:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2252")
    @Description("Scanners section is not displayed in the DSP endpoint when all scanners are turned OFF")
    public void checkEmptyScannersDsp() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        DemandPO dsp = new DemandPO();
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.toggleScanner(ScannersEnums.scannerTypesEnum.GEOEDGE, false);
        scanners.toggleScanner(ScannersEnums.scannerTypesEnum.PROTECTED_POSTBID, false);
        scanners.toggleScanner(ScannersEnums.scannerTypesEnum.PIXALATE_POSTBID, false);
        scanners.toggleScanner(ScannersEnums.scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, false);
        scanners.clickSaveScanners();
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        dsp.assertSectionPresent(endpointSettingsSectionsEnum.SCANNERS, false);
    }

    //</editor-fold>

}
