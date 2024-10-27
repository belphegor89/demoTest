package suites;

import common.SoftAssertCustom;
import common.utils.BrowserUtility;
import common.utils.RandomUtility;
import data.CommonEnums.*;
import data.FilterListEnums;
import data.ScannersEnums.pixalatePrebidTypesEnum;
import data.ScannersEnums.scannerTypesEnum;
import data.StaticData;
import data.SupplyEnum.jsTagTypeEnum;
import data.SupplyEnum.macroTagsTypes;
import data.SupplyEnum.sspTableColumnsEnum;
import data.SupplyEnum.vastTypeEnum;
import data.dataobject.CompanyDO;
import data.dataobject.EndpointCommonDO.MarginSettings;
import data.dataobject.EndpointCommonDO.ScannerSetting;
import data.dataobject.EndpointSupplyDO;
import data.dataobject.FilterListDO;
import data.dataobject.SellerJsonDO;
import data.textstrings.messages.SupplyText;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.*;

import java.io.File;
import java.util.*;

import static data.textstrings.messages.CommonText.VALIDATION_REQUIRED_EMPTY;
import static data.textstrings.messages.SupplyText.*;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("Supply dashboard")
public class SupplyDashboard extends BaseSuiteClassNew {

    //<editor-fold desc="RTB Endpoints">
    @Test
    @TmsLink("1140")
    @Description("Create basic SSP RTB endpoint")
    public void createRtbBasic() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
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
            put(advancedSettingsEnum.NURL_SSP, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestRTB" + rnd, endpointTypeEnum.RTB).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setAdapter((adaptersSspRtbEnum) randomUtil.getRandomElement(adaptersSspRtbEnum.values()));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.searchNameIdInput(endpointData.getEndpointName());
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("2898")
    @Description("Validate SSP RTB endpoint required settings")
    public void validateRtbSettings() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        String name = "autotestRtbValidation", company, adTrafficCombinedError = SupplyText.errorTrafficTypesEmpty + "\n" + SupplyText.errorAdFormatsEmpty;
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, false);
            put(adFormatPlacementTypeEnum.VIDEO, false);
            put(adFormatPlacementTypeEnum.AUDIO, false);
            put(adFormatPlacementTypeEnum.NATIVE, false);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, false);
            put(trafficTypeEnum.APP, false);
            put(trafficTypeEnum.MOBILE_WEB, false);
            put(trafficTypeEnum.CTV, false);
        }};
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        company = ssp.getRandomCompanyName(true);
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationInfoPage(VALIDATION_REQUIRED_EMPTY, null, null);
        ssp.inputName("<;:[]{}>?~");
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationInfoPage(errorNameFormat, errorCompanyEmpty, null);
        ssp.inputName("12");
        ssp.selectCompany(company);
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationInfoPage(errorNameShort, null, null);
        ssp.inputName("emulatekindbloominevolvefabricatesupposedisposaldifficultcommunityexcitedindulgencedesiredtemperamentsignatureaccordingsuperficialcomparablehistoricallylawyercounselingsatisfiedmediatorpuzzlethoughtfullyaffectionatefamiliarizationexcitementcommittedconvers");
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationInfoPage(errorNameLong, null, null);
        ssp.inputName(name);
        ssp.clickSettingsTab();
        ssp.setMargin(new MarginSettings().setFixed(101d));
        ssp.inputSpendLimit(20000001);
        ssp.inputTmax(20000001);
        ssp.inputBidPrice(1000000d);
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationSettingsPage(errorSpendLimitHigh, errorTmaxHigh, errorMarginHigh, null, null);
        ssp.inputSpendLimit(345);
        ssp.inputTmax(789);
        ssp.setMargin(new MarginSettings().setRange(101d, 101d));
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationSettingsPage(null, null, errorMarginHigh, errorMarginHigh, null);
        ssp.setMargin(new MarginSettings().setRange(70d, 50d));
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationSettingsPage(null, null, errorMarginHigh, errorMarginMaxMin.replace("${minValue}", "70"), null);
        ssp.setMargin(new MarginSettings().setAdaptive());
        ssp.assertValidationSettingsPage(null, null, null, null, errorBidPriceHigh);
        ssp.inputBidPrice(223d);
        ssp.toggleAdFormat(adFormatsMap);
        ssp.toggleTrafficType(trafficTypesMap);
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationInfoPage(adTrafficCombinedError, null, null);
        ssp.clickSettingsTab();
        trafficTypesMap.put(trafficTypeEnum.WEB, true);
        ssp.toggleTrafficType(trafficTypesMap);
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationInfoPage(errorAdFormatsEmpty, null, null);
        ssp.clickSettingsTab();
        adFormatsMap.put(adFormatPlacementTypeEnum.BANNER, true);
        trafficTypesMap.put(trafficTypeEnum.WEB, false);
        ssp.toggleAdFormat(adFormatsMap);
        ssp.toggleTrafficType(trafficTypesMap);
        ssp.clickSaveEndpointWithErrors();
        ssp.assertValidationInfoPage(errorTrafficTypesEmpty, null, null);
        softAssert.assertAll("Errors in SSP Validation messages:");
    }

    @Test
    @TmsLink("2671")
    @Description("Clone SSP RTB Endpoint")
    public void cloneSspRtbEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
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
            put(advancedSettingsEnum.NURL_SSP, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.DYNAMIC_MARGIN, true);
        }};
        List<ScannerSetting> scanners = new ArrayList<>() {{
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_POSTBID, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_POSTBID, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_MEDIA_GUARD, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_PREBID, null).setPixalatePrebidMap(ssp.getPixalatePrebidDefaultSettings()));
            add(new ScannerSetting(scannerTypesEnum.GEOEDGE, randomUtil.getRandomBoolean()));
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_PREBID, randomUtil.getRandomBoolean()));
        }};
        EndpointSupplyDO endpointOriginal = new EndpointSupplyDO("autotestRTBClone" + rnd, endpointTypeEnum.RTB).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setDynamicMarginValue(randomUtil.getRandomDoubleRounded(1, 100, 2)).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setAdapter((adaptersSspRtbEnum) randomUtil.getRandomElement(adaptersSspRtbEnum.values())).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.xml?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.XML).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        //*General*//
        endpointOriginal.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointOriginal);
        //*Settings*//
        ssp.clickSettingsTab();
        endpointOriginal.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointOriginal.getRegion());
        ssp.setupSettingsTab(endpointOriginal);
        endpointOriginal.setDspAllowedMap(ssp.selectAllowedBlockedDsp(true, 3)).setDspBlockedMap(ssp.selectAllowedBlockedDsp(false, 3));
        //*Additional*//
        ssp.clickAdditionalInfoTab();
        ssp.inputPartnersApiLink(endpointOriginal.getReportApiLinkPartner());
        ssp.clickCheckPartnersApiUrl(endpointOriginal.getPartnerApiType());
        //*Scanners*//
        ssp.clickScannersTab();
        ssp.setupScannerSection(endpointOriginal.getScannerSettings());
        //*Clone and assert*//
        ssp.clickSaveExitEndpoint();
        endpointOriginal.setId(ssp.getEndpointId(endpointOriginal.getEndpointName()));
        ssp.searchNameIdInput(endpointOriginal.getEndpointName());
        ssp.clickCloneEndpoint(endpointOriginal.getId(), true);
        ssp.openClonedEndpointEdit(true);
        EndpointSupplyDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointOriginal.getEndpointName() + " copy1").setStatus(false).setId(ssp.getEndpointId(endpointCloned.getEndpointName())).clearDspAllowedMap().clearDspBlockedMap().setReportApiLinkPartner("", null);
        ssp.assertInformationTab(endpointCloned);
        endpointCloned.setStatus(true);
        ssp.toggleEndpointStatus(endpointCloned.getStatus());
        ssp.clickSettingsTab();
        endpointCloned.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.assertSettingsTab(endpointCloned);
        ssp.clickAdditionalInfoTab();
        ssp.assertApiSync(endpointCloned.getReportApiLinkPartner(), endpointCloned.getCsvColumnsMap());
        ssp.clickScannersTab();
        ssp.assertScannersTab(endpointCloned.getScannerSettings());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointCloned.getEndpointName());
        ssp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="Create VAST endpoints">
    @Test
    @TmsLink("1142")
    @Description("Create VAST Custom Web endpoint")
    public void createVastCustomWeb() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
        List<macroTagsTypes> vastTagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.STOREURL);
            add(macroTagsTypes.APPNAME);
            add(macroTagsTypes.CONSENT);
            add(macroTagsTypes.CONTENT_EPISODE);
        }};
        List<macroTagsTypes> vastTagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.LAT);
            add(macroTagsTypes.LON);
            add(macroTagsTypes.DNT);
            add(macroTagsTypes.CONTENT_ID);
            add(macroTagsTypes.CONTENT_TITLE);
        }};
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestVastCustomWeb" + rnd, endpointTypeEnum.VAST).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.addMacrosFromList(vastTagsListRowAdd);
        ssp.addMacrosFromModal(vastTagsListModalAdd);
        endpointData.setJsVastTagsList(vastTagsListRowAdd).setJsVastTagsList(vastTagsListModalAdd);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("362")
    @Description("Create VAST Custom InApp endpoint")
    public void createVastCustomInApp() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
        List<macroTagsTypes> vastTagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.STOREURL);
            add(macroTagsTypes.DEVICE_MAKE);
            add(macroTagsTypes.CONTENT_CONTEXT);
            add(macroTagsTypes.CONTENT_EPISODE);
        }};
        List<macroTagsTypes> vastTagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.SCHAIN);
            add(macroTagsTypes.DEVICE_MODEL);
            add(macroTagsTypes.DNT);
            add(macroTagsTypes.CONTENT_ID);
            add(macroTagsTypes.CONTENT_TITLE);
        }};
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.AUDIO, true);
            put(adFormatPlacementTypeEnum.VIDEO, false);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.DYNAMIC_MARGIN, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestVastCustomInApp" + rnd, endpointTypeEnum.VAST).setVastTagType(vastTypeEnum.CUSTOM_INAPP).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setDynamicMarginValue(randomUtil.getRandomDoubleRounded(0, 100, 2)).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FLOOR, randomUtil.getRandomDoubleRounded(1, 100, 2));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.addMacrosFromList(vastTagsListRowAdd);
        ssp.addMacrosFromModal(vastTagsListModalAdd);
        endpointData.setJsVastTagsList(vastTagsListRowAdd).setJsVastTagsList(vastTagsListModalAdd);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1143")
    @Description("Create VAST Custom CTV endpoint")
    public void createVastCustomCTV() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
        List<macroTagsTypes> vastTagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.UA);
            add(macroTagsTypes.DEVICE_MAKE);
            add(macroTagsTypes.CB);
            add(macroTagsTypes.CONTENT_EPISODE);
        }};
        List<macroTagsTypes> vastTagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.SCHAIN);
            add(macroTagsTypes.DEVICE_MODEL);
            add(macroTagsTypes.CONTENT_CHANNEL_NAME);
            add(macroTagsTypes.CONTENT_PRODUCER);
        }};
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.AUDIO, false);
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.DYNAMIC_MARGIN, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestVastCustomCTV" + rnd, endpointTypeEnum.VAST).setVastTagType(vastTypeEnum.CUSTOM_CTV).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setDynamicMarginValue(randomUtil.getRandomDoubleRounded(0, 100, 2)).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FLOOR, randomUtil.getRandomDoubleRounded(1, 100, 2));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.addMacrosFromList(vastTagsListRowAdd);
        ssp.addMacrosFromModal(vastTagsListModalAdd);
        endpointData.setJsVastTagsList(vastTagsListRowAdd).setJsVastTagsList(vastTagsListModalAdd);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1877")
    @Description("Create VAST LKQD endpoint")
    public void createVastLKQD() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.AUDIO, false);
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_SSP, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestVastLKQD" + rnd, endpointTypeEnum.VAST).setVastTagType(vastTypeEnum.LKQD).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("40952")
    @Description("Create VAST JW Player endpoint")
    public void createVastJwPlayer() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 40, 2), randomUtil.getRandomDoubleRounded(41, 90, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.AUDIO, false);
            put(adFormatPlacementTypeEnum.VIDEO, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_SSP, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestVastJWPlayer" + rnd, endpointTypeEnum.VAST).setVastTagType(vastTypeEnum.JW_PLAYER).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FLOOR, randomUtil.getRandomDoubleRounded(1, 100, 2));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("40953")
    @Description("Create VAST Springserve endpoint")
    public void createVastSpringserve() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 40, 2), randomUtil.getRandomDoubleRounded(41, 90, 2));
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.AUDIO, true);
            put(adFormatPlacementTypeEnum.VIDEO, false);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_SSP, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestVastSpringserve" + rnd, endpointTypeEnum.VAST).setVastTagType(vastTypeEnum.SPRINGSERVE).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FLOOR, randomUtil.getRandomDoubleRounded(1, 100, 2));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("2671")
    @Description("Clone SSP VAST Endpoint")
    public void cloneSspVastEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 55, 2), randomUtil.getRandomDoubleRounded(55, 100, 2));
        Map<pixalatePrebidTypesEnum, ScannerSetting.PixalatePrebidSetting> pixalateSettingsMap = new HashMap<>() {{
            put(pixalatePrebidTypesEnum.DEVICE_ID, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.BUNDLES, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.IPV4, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.IPV6, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.OTT, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.USER_AGENT, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
            put(pixalatePrebidTypesEnum.DATA_CENTER, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
            put(pixalatePrebidTypesEnum.DOMAINS, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
            put(pixalatePrebidTypesEnum.DEFASED_APP, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
        }};
        List<macroTagsTypes> vastTagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.STOREURL);
            add(macroTagsTypes.DEVICE_MAKE);
            add(macroTagsTypes.CONTENT_CONTEXT);
            add(macroTagsTypes.CONTENT_EPISODE);
        }};
        List<macroTagsTypes> vastTagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.SCHAIN);
            add(macroTagsTypes.DEVICE_MODEL);
            add(macroTagsTypes.DNT);
            add(macroTagsTypes.CONTENT_ID);
            add(macroTagsTypes.CONTENT_TITLE);
        }};
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.VIDEO, true);
            put(adFormatPlacementTypeEnum.AUDIO, false);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.NURL_SSP, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        List<ScannerSetting> scanners = new ArrayList<>() {{
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_POSTBID, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_POSTBID, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_MEDIA_GUARD, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_PREBID, true).setPixalatePrebidMap(pixalateSettingsMap));
            add(new ScannerSetting(scannerTypesEnum.GEOEDGE, randomUtil.getRandomBoolean()));
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_PREBID, randomUtil.getRandomBoolean()));
        }};
        EndpointSupplyDO endpointOriginal = new EndpointSupplyDO("autotestVastClone" + rnd, endpointTypeEnum.VAST).setVastTagType(vastTypeEnum.CUSTOM_INAPP).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FLOOR, randomUtil.getRandomDoubleRounded(1, 100, 2)).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.xml?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.XML).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        //*General*//
        endpointOriginal.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointOriginal);
        ssp.addMacrosFromList(vastTagsListRowAdd);
        ssp.addMacrosFromModal(vastTagsListModalAdd);
        endpointOriginal.setJsVastTagsList(vastTagsListRowAdd).setJsVastTagsList(vastTagsListModalAdd);
        //*Settings*//
        ssp.clickSettingsTab();
        endpointOriginal.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointOriginal.getRegion());
        ssp.setupSettingsTab(endpointOriginal);
        endpointOriginal.setDspAllowedMap(ssp.selectAllowedBlockedDsp(true, 3)).setDspBlockedMap(ssp.selectAllowedBlockedDsp(false, 3));
        //*Additional*//
        ssp.clickAdditionalInfoTab();
        ssp.inputPartnersApiLink(endpointOriginal.getReportApiLinkPartner());
        ssp.clickCheckPartnersApiUrl(endpointOriginal.getPartnerApiType());
        //*Scanners*//
        ssp.clickScannersTab();
        ssp.setupScannerSection(endpointOriginal.getScannerSettings());
        //*Clone and assert*//
        ssp.clickSaveExitEndpoint();
        endpointOriginal.setId(ssp.getEndpointId(endpointOriginal.getEndpointName()));
        ssp.searchNameIdInput(endpointOriginal.getEndpointName());
        ssp.clickCloneEndpoint(endpointOriginal.getId(), true);
        ssp.openClonedEndpointEdit(true);
        EndpointSupplyDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointOriginal.getEndpointName() + " copy1").setStatus(false).setId(ssp.getEndpointId(endpointCloned.getEndpointName())).clearDspAllowedMap().clearDspBlockedMap().setReportApiLinkPartner("", null);
        ssp.assertInformationTab(endpointCloned);
        endpointCloned.setStatus(true);
        ssp.toggleEndpointStatus(endpointCloned.getStatus());
        ssp.clickSettingsTab();
        endpointCloned.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.assertSettingsTab(endpointCloned);
        ssp.clickAdditionalInfoTab();
        ssp.assertApiSync(endpointCloned.getReportApiLinkPartner(), endpointCloned.getCsvColumnsMap());
        ssp.clickScannersTab();
        ssp.assertScannersTab(endpointCloned.getScannerSettings());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointCloned.getEndpointName());
        ssp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="Create JS Tag endpoints">
    @Test
    @TmsLink("1675")
    @Description("Create JS Tag Custom S2S Web endpoint")
    public void createJsTagS2SWeb() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        List<macroTagsTypes> tagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.CB);
            add(macroTagsTypes.BUNDLE);
        }};
        List<macroTagsTypes> tagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.SCHAIN);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestJsS2SWeb" + rnd, endpointTypeEnum.JS).setJsTagType(jsTagTypeEnum.S2S_WEB).setStatus(true).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setJsTagCustomSize(128, 367).setJsBackfill("<script type=\"text/javascript\" charset=\"utf-8\">window.location=\"applovin://failLoad\";</script>");
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.addMacrosFromList(tagsListRowAdd);
        ssp.addMacrosFromModal(tagsListModalAdd);
        endpointData.setJsVastTagsList(tagsListRowAdd).setJsVastTagsList(tagsListModalAdd);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1676")
    @Description("Create JS Tag Custom S2S InApp endpoint")
    public void createJsTagS2SInApp() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
        List<macroTagsTypes> tagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.DOMAIN);
            add(macroTagsTypes.SCHAIN);
        }};
        List<macroTagsTypes> tagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.CB);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, true);
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.DYNAMIC_MARGIN, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestJsS2SInApp" + rnd, endpointTypeEnum.JS).setStatus(true).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setDynamicMarginValue(randomUtil.getRandomDoubleRounded(0, 100, 2)).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setJsBackfill("<script type=\"text/javascript\" charset=\"utf-8\">window.location=\"applovin://failLoad\";</script>");
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.addMacrosFromList(tagsListRowAdd);
        ssp.addMacrosFromModal(tagsListModalAdd);
        endpointData.setJsVastTagsList(tagsListRowAdd).setJsVastTagsList(tagsListModalAdd);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1677")
    @Description("Create JS Tag Custom S2C Web endpoint")
    public void createJsTagS2CWeb() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 60, 2), randomUtil.getRandomDoubleRounded(60, 100, 2));
        List<macroTagsTypes> tagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.BUNDLE);
        }};
        List<macroTagsTypes> tagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.NAME);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.NURL_SSP, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestJsS2CWeb" + rnd, endpointTypeEnum.JS).setJsTagType(jsTagTypeEnum.S2C_WEB).setStatus(true).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FLOOR, randomUtil.getRandomDoubleRounded(1, 100, 2)).setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setJsBackfill("<ins class='dcmads' style='display:inline-block;width:300px;height:250px'    data-dcm-placement='N950.1964509JUNGROUP/B28601979.346114382'    data-dcm-rendering-mode='script'    data-dcm-https-only    data-dcm-gdpr-applies='gdpr=${GDPR}'    data-dcm-gdpr-consent='gdpr_consent=${GDPR_CONSENT_755}'    data-dcm-addtl-consent='addtl_consent=${ADDTL_CONSENT}'    data-dcm-ltd='false'    data-dcm-resettable-device-id=''    data-dcm-app-id=''>  <script src='https://www.googletagservices.com/dcm/dcmads.js'></script></ins>");
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.addMacrosFromList(tagsListRowAdd);
        ssp.addMacrosFromModal(tagsListModalAdd);
        endpointData.setJsVastTagsList(tagsListRowAdd).setJsVastTagsList(tagsListModalAdd);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1678")
    @Description("Create JS Tag Custom S2C InApp endpoint")
    public void createJsTagS2CInApp() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
        List<macroTagsTypes> tagsListRowAdd = new ArrayList<>() {{
            add(macroTagsTypes.PAGE);
        }};
        List<macroTagsTypes> tagsListModalAdd = new ArrayList<>() {{
            add(macroTagsTypes.SCHAIN);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.NURL_SSP, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestJsS2CInApp" + rnd, endpointTypeEnum.JS).setJsTagType(jsTagTypeEnum.S2C_APP).setStatus(true).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FLOOR, randomUtil.getRandomDoubleRounded(1, 100, 2)).setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setJsBackfill("<p>No fill, sorry.</p>");
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.addMacrosFromList(tagsListRowAdd);
        ssp.addMacrosFromModal(tagsListModalAdd);
        endpointData.setJsVastTagsList(tagsListRowAdd).setJsVastTagsList(tagsListModalAdd);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1792")
    @Description("Create JS Tag Direct web page endpoint")
    public void createJsTagDirectWebPage() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setRange(randomUtil.getRandomDoubleRounded(1, 60, 2), randomUtil.getRandomDoubleRounded(60, 100, 2));
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.DYNAMIC_MARGIN, true);
            put(advancedSettingsEnum.NURL_SSP, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestJsDirectWeb" + rnd, endpointTypeEnum.JS).setJsTagType(jsTagTypeEnum.DIRECT).setStatus(true).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setDynamicMarginValue(randomUtil.getRandomDoubleRounded(0, 100, 2)).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setJsBackfill("<ins class='dcmads' style='display:inline-block;width:300px;height:250px'>  <script src='https://www.googletagservices.com/dcm/dcmads.js'></script></ins>");
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("40964")
    @Description("Create JS Tag Google Ad manager (DFP) endpoint")
    public void createJsTagGoogleDfp() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
            put(advancedSettingsEnum.DYNAMIC_MARGIN, true);
            put(advancedSettingsEnum.NURL_SSP, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestJsGoogleDfp" + rnd, endpointTypeEnum.JS).setJsTagType(jsTagTypeEnum.DFP).setStatus(true).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setDynamicMarginValue(randomUtil.getRandomDoubleRounded(0, 100, 2)).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setJsBackfill("<script async type=\"text/javascript\" src=\"https://test.com/?c=rtb&a=sorry\"></script>");
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("40965")
    @Description("Create JS Tag AppLovin endpoint")
    public void createJsTagApplovin() {
        RandomUtility randomUtil = new RandomUtility();
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.RCPM, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestJsGoogleApplovin" + rnd, endpointTypeEnum.JS).setJsTagType(jsTagTypeEnum.APPLOVIN).setStatus(true).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setJsBackfill("<script async type=\"text/javascript\" src=\"https://test.com/?c=rtb&a=sorry\"></script>");
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.assertInformationTab(endpointData);
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("2671")
    @Description("Clone SSP JS Tag Endpoint")
    public void cloneSspJsEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
        Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap = new HashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
        }};
        Map<trafficTypeEnum, Boolean> trafficTypesMap = new HashMap<>() {{
            put(trafficTypeEnum.WEB, true);
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, randomUtil.getRandomBoolean());
        }};
        Map<advancedSettingsEnum, Boolean> advancedSettingsMap = new HashMap<>() {{
            put(advancedSettingsEnum.IFA, true);
            put(advancedSettingsEnum.RCPM, true);
        }};
        List<ScannerSetting> scanners = new ArrayList<>() {{
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_POSTBID, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_MEDIA_GUARD, randomUtil.getRandomBoolean()).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_PREBID, false));
            add(new ScannerSetting(scannerTypesEnum.GEOEDGE, randomUtil.getRandomBoolean()));
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_PREBID, randomUtil.getRandomBoolean()));
        }};
        EndpointSupplyDO endpointOriginal = new EndpointSupplyDO("autotestJsTagClone" + rnd, endpointTypeEnum.JS).setJsTagType(jsTagTypeEnum.DFP).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.xml?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.XML).setJsTagSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))).setJsBackfill("<script async type=\"text/javascript\" src=\"https://test.com/?c=rtb&a=sorry\"></script>").setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        //*General*//
        endpointOriginal.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointOriginal);
        //*Settings*//
        ssp.clickSettingsTab();
        endpointOriginal.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointOriginal.getRegion());
        ssp.setupSettingsTab(endpointOriginal);
        endpointOriginal.setDspAllowedMap(ssp.selectAllowedBlockedDsp(true, 3)).setDspBlockedMap(ssp.selectAllowedBlockedDsp(false, 3));
        //*Additional*//
        ssp.clickAdditionalInfoTab();
        ssp.inputPartnersApiLink(endpointOriginal.getReportApiLinkPartner());
        ssp.clickCheckPartnersApiUrl(endpointOriginal.getPartnerApiType());
        //*Scanners*//
        ssp.clickScannersTab();
        ssp.setupScannerSection(endpointOriginal.getScannerSettings());
        //*Clone and assert*//
        ssp.clickSaveExitEndpoint();
        endpointOriginal.setId(ssp.getEndpointId(endpointOriginal.getEndpointName()));
        ssp.searchNameIdInput(endpointOriginal.getEndpointName());
        ssp.clickCloneEndpoint(endpointOriginal.getId(), true);
        ssp.openClonedEndpointEdit(true);
        EndpointSupplyDO endpointCloned = endpointOriginal;
        endpointCloned.setEndpointName(endpointOriginal.getEndpointName() + " copy1").setStatus(false).setId(ssp.getEndpointId(endpointCloned.getEndpointName())).clearDspAllowedMap().clearDspBlockedMap().setReportApiLinkPartner("", null);
        ssp.assertInformationTab(endpointCloned);
        endpointCloned.setStatus(true);
        ssp.toggleEndpointStatus(endpointCloned.getStatus());
        ssp.clickSettingsTab();
        endpointCloned.setDspAllowedMap(ssp.getAllowedDspList());
        ssp.assertSettingsTab(endpointCloned);
        ssp.clickAdditionalInfoTab();
        ssp.assertApiSync(endpointCloned.getReportApiLinkPartner(), endpointCloned.getCsvColumnsMap());
        ssp.clickScannersTab();
        ssp.assertScannersTab(endpointCloned.getScannerSettings());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointCloned.getEndpointName());
        ssp.assertEndpointRowInfo(endpointCloned);
        softAssert.assertAll("Errors in CLONED endpoint #" + endpointCloned.getId() + " - [" + endpointCloned.getEndpointName() + "]");
    }

    //</editor-fold>

    //<editor-fold desc="SSP List">
    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1157")
    @Description("SSP dashboard table check and sorting")
    public void sortSspList() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.assertTableSorting(sspTableColumnsEnum.ID, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.ID);
        ssp.assertTableSorting(sspTableColumnsEnum.ID, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.NAME);
        ssp.assertTableSorting(sspTableColumnsEnum.NAME, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.NAME);
        ssp.assertTableSorting(sspTableColumnsEnum.NAME, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.REGION);    // TODO Will not work for the projects with 1 region
        ssp.assertTableSorting(sspTableColumnsEnum.REGION, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.REGION);
        ssp.assertTableSorting(sspTableColumnsEnum.REGION, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.TYPE);
        ssp.assertTableSorting(sspTableColumnsEnum.TYPE, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.TYPE);
        ssp.assertTableSorting(sspTableColumnsEnum.TYPE, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.QPS);
        ssp.assertTableSorting(sspTableColumnsEnum.QPS, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.QPS);
        ssp.assertTableSorting(sspTableColumnsEnum.QPS, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.BID_QPS);
        ssp.assertTableSorting(sspTableColumnsEnum.BID_QPS, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.BID_QPS);
        ssp.assertTableSorting(sspTableColumnsEnum.BID_QPS, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.SPEND_YESTERDAY);
        ssp.assertTableSorting(sspTableColumnsEnum.SPEND_YESTERDAY, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.SPEND_YESTERDAY);
        ssp.assertTableSorting(sspTableColumnsEnum.SPEND_YESTERDAY, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.SPEND_TODAY);
        ssp.assertTableSorting(sspTableColumnsEnum.SPEND_TODAY, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.SPEND_TODAY);
        ssp.assertTableSorting(sspTableColumnsEnum.SPEND_TODAY, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.WIN_RATE);
        ssp.assertTableSorting(sspTableColumnsEnum.WIN_RATE, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.WIN_RATE);
        ssp.assertTableSorting(sspTableColumnsEnum.WIN_RATE, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.COMPANY_ID);
        ssp.assertTableSorting(sspTableColumnsEnum.COMPANY_ID, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.COMPANY_ID);
        ssp.assertTableSorting(sspTableColumnsEnum.COMPANY_ID, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.ALLOWED_DSP);
        ssp.assertTableSorting(sspTableColumnsEnum.ALLOWED_DSP, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.ALLOWED_DSP);
        ssp.assertTableSorting(sspTableColumnsEnum.ALLOWED_DSP, true);
        ssp.clickColumnSorting(sspTableColumnsEnum.SPEND_LIMIT);
        ssp.assertTableSorting(sspTableColumnsEnum.SPEND_LIMIT, false);
        ssp.clickColumnSorting(sspTableColumnsEnum.SPEND_LIMIT);
        ssp.assertTableSorting(sspTableColumnsEnum.SPEND_LIMIT, true);
        softAssert.assertAll("Errors in SSP Dashboard sorting:");
    }

    //TODO test case is checking only the first page, think about implementing the full check (may be slow). Review and rework this test
    @Test
    @TmsLink("37957")
    @Description("Search SSP by Company Name")
    public void searchSSPByCompany() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        List<String> companiesNames;
        String company;
        Map<Integer, String> sspCompaniesMap;
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.openFilterCompanies();
        companiesNames = ssp.getSspCompanies(null, true).values().stream().toList();
        company = (String) randomUtil.getRandomElement(companiesNames);
        //select any
        sspCompaniesMap = ssp.getSspCompanies(company, null);
        ssp.filterCompaniesSelect(company, true);
        ssp.assertAllEndpointRowsCompanies(sspCompaniesMap);
        ssp.filterCompaniesSelect(company, false);
        //search and select any
        company = (String) randomUtil.getRandomElement(companiesNames);
        sspCompaniesMap = ssp.getSspCompanies(company, null);
        ssp.filterCompaniesInputName(company.substring(0, 3));
        ssp.filterCompaniesSelect(company, true);
        ssp.assertAllEndpointRowsCompanies(sspCompaniesMap);
        sAssert.assertAll("Errors in SSP search by Company Name");
    }

    //TODO refactor test and make it make more sense
    @Test
    @TmsLink("39427")
    @Description("Search SSP by Name/ID")
    public void searchSspByNameId() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 5), sspId;
        List<String> sspNames;
        List<Integer> sspIds;
        String sspName, sspSearch;
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        sspNames = ssp.getEndpointNamesList(true);
        sspName = (String) randomUtil.getRandomElement(sspNames);
        sspSearch = sspName.substring(0, rnd).toLowerCase();
        ssp.searchNameIdInput(sspSearch);
        ssp.assertAllEndpointRowsNames(sspSearch);
        ssp.searchNameIdInput("mtqpxylsbepluxcdq");
        ssp.assertDashboardEmpty();
        sspIds = ssp.getEndpointIdsList(true);
        sspId = (Integer) randomUtil.getRandomElement(sspIds);
        ssp.searchNameIdInput(String.valueOf(sspId));
        ssp.assertAllEndpointRowsIds(String.valueOf(sspId));
        sAssert.assertAll("Errors in SSP search by Name/ID");
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("39391")
    @Description("Export SSP list as CSV")
    public void exportSspList() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        File sspExport = ssp.clickExportCsv();
        ssp.assertFileExportCsv(sspExport);
        ssp.selectFilterType((endpointTypeEnum) randomUtil.getRandomElement(endpointTypeEnum.getSspTypes()));
        ssp.filterSelectRegion((regionsEnum) randomUtil.getRandomElement(ssp.getRegions()));
        sspExport = ssp.clickExportCsv();
        ssp.assertFileExportCsv(sspExport);
        softAssert.assertAll("Errors in SSP export CSV");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("48211")
    @Description("Download Ads.txt file in SSP dashboard")
    public void checkAdsTxtFileSsp() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        CompanyPO company = new CompanyPO();
        SellersJsonPO sellers = new SellersJsonPO();
        SellerJsonDO sellerData;
        CompanyDO companyData;
        EndpointSupplyDO endpointData;
        File adsTxt;
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        endpointData = ssp.getRandomEndpointRowInfo();
        companyData = company.getCompanyDataFromDb(endpointData.getCompany());
        sellerData = sellers.getSellerSettingFromDb(true, companyData.getCompanyId());
        adsTxt = ssp.clickDownloadAdsTxt(endpointData.getId());
        ssp.assertAdsTxtRequired(adsTxt, companyData.getSellerId(), sellerData.getDirectnessType());
        softAssert.assertAll("Errors in SSP Ads.txt file");
    }

    //</editor-fold>

    //<editor-fold desc="Additional parameters">
    //TODO refactor this test - either make 2 more tests or add a random selection to this case to test saving with other API link types
    @Test
    @TmsLink("3991")
    @Description("Create SSP RTB endpoint with Partner's API Link")
    public void createRtbPartnersApiLink() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
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
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestRTBPartnersAPILink" + rnd, endpointTypeEnum.RTB).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setReportApiLinkPartner("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.json?from={%Y-m-d%}&to={%Y-m-d%}", endpointPartnerApiTypeEnum.JSON);
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.setupSettingsTab(endpointData);
        ssp.clickAdditionalInfoTab();
        ssp.inputPartnersApiLink("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.xml?from={%Y-m-d%}&to={%Y-m-d%}");
        ssp.clickCheckPartnersApiUrl(endpointPartnerApiTypeEnum.XML);
        ssp.inputPartnersApiLink("https://ssp.e-volution.ai/api/statistic-report-s307944fd8e7cdce6-sc632526ea7134a618cfe08e7p.csv?from={%Y-m-d%}&to={%Y-m-d%}");
        ssp.clickCheckPartnersApiUrl(endpointPartnerApiTypeEnum.CSV);
        ssp.inputPartnersApiLink(endpointData.getReportApiLinkPartner());
        ssp.clickCheckPartnersApiUrl(endpointData.getPartnerApiType());
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.clickAdditionalInfoTab();
        ssp.assertApiSync(endpointData.getReportApiLinkPartner(), null);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("2123")
    @Description("Create SSP RTB endpoint with Scanners settings")
    public void createRtbScanners() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setFixed(randomUtil.getRandomDoubleRounded(1, 100, 2));
        Map<pixalatePrebidTypesEnum, ScannerSetting.PixalatePrebidSetting> pixalateSettingsMap = new LinkedHashMap() {{
            put(pixalatePrebidTypesEnum.DEVICE_ID, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.BUNDLES, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.IPV4, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.IPV6, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.OTT, new ScannerSetting.PixalatePrebidSetting().setSetting(true, randomUtil.getRandomInt(1, 100)));
            put(pixalatePrebidTypesEnum.USER_AGENT, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
            put(pixalatePrebidTypesEnum.DATA_CENTER, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
            put(pixalatePrebidTypesEnum.DOMAINS, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
            put(pixalatePrebidTypesEnum.DEFASED_APP, new ScannerSetting.PixalatePrebidSetting().setSetting(true, null));
        }};
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
        List<ScannerSetting> scanners = new ArrayList<>() {{
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_POSTBID, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_POSTBID, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.WHITEOPS_MEDIA_GUARD, true).setImpressionScan(true, randomUtil.getRandomInt(1, 100)));
            add(new ScannerSetting(scannerTypesEnum.PIXALATE_PREBID, true).setPixalatePrebidMap(pixalateSettingsMap));
            add(new ScannerSetting(scannerTypesEnum.GEOEDGE, true));
            add(new ScannerSetting(scannerTypesEnum.PROTECTED_PREBID, true));
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestRTBClone" + rnd, endpointTypeEnum.RTB).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setScannerSettings(scanners);
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.setupSettingsTab(endpointData);
        ssp.clickScannersTab();
        ssp.setupScannerSection(endpointData.getScannerSettings());
        ssp.clickSaveExitEndpoint();
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.searchNameIdInput(endpointData.getEndpointName());
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.clickScannersTab();
        ssp.assertScannersTab(endpointData.getScannerSettings());
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @TmsLink("1794")
    @Description("Create basic SSP RTB endpoint")
    public void createEndpointAllowedBlocked() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        MarginSettings marginSettings = new MarginSettings().setAdaptive();
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
            put(advancedSettingsEnum.NURL_SSP, true);
            put(advancedSettingsEnum.IFA, true);
        }};
        EndpointSupplyDO endpointData = new EndpointSupplyDO("autotestAllowedBlocked" + rnd, endpointTypeEnum.RTB).setStatus(true).setAdFormatMap(adFormatsMap).setTrafficMap(trafficTypesMap).setAdvancedMap(advancedSettingsMap).setMarginSettings(marginSettings).setSpendLimit(randomUtil.getRandomInt(1, 100000)).setTmax(randomUtil.getRandomInt(1, 1000)).setBidPrice(bidPriceTypeEnum.FIXED, randomUtil.getRandomDoubleRounded(1, 100, 2)).setAdapter((adaptersSspRtbEnum) randomUtil.getRandomElement(adaptersSspRtbEnum.values()));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        endpointData.setCompany(ssp.getRandomCompanyName(true));
        ssp.setupInformationTab(endpointData);
        ssp.clickSettingsTab();
        endpointData.setRegion(ssp.getRandomRegion());
        ssp.selectRegion(endpointData.getRegion());
        ssp.setupSettingsTab(endpointData);
        endpointData.setDspAllowedMap(ssp.selectAllowedBlockedDsp(true, 3)).setDspBlockedMap(ssp.selectAllowedBlockedDsp(false, 3));
        ssp.clickSaveExitEndpoint();
        ssp.searchNameIdInput(endpointData.getEndpointName());
        endpointData.setId(ssp.getEndpointId(endpointData.getEndpointName()));
        ssp.assertEndpointRowInfo(endpointData);
        ssp.openEndpointEditPage(endpointData.getId());
        ssp.clickSettingsTab();
        ssp.assertSettingsTab(endpointData);
        softAssert.assertAll("Errors in endpoint #" + endpointData.getId() + " - [" + endpointData.getEndpointName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("41133")
    @Description("Validate Partner's API Link")
    public void validatePartnerLink() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        ssp.clickAdditionalInfoTab();
        ssp.inputPartnersApiLink("http://login.showcasead.com/publisher/svc?action=outcsv&login=Convertise2020&password=Lwf0gD&channel=ZoneReports&dim=date&f.zone=94390&f.date={%Y-m-d%}_{%Y-m-d%}&appType=CPM");
        ssp.clickCheckPartnersApiUrl(endpointPartnerApiTypeEnum.INVALID);
        ssp.assertValidationModalApiSync(errorApiLinkUnsupported);
        softAssert.assertAll("Errors in Partner's API Link validation");
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("46491")
    @Description("Check for Native Demand #1 in Allowed/Blocked DSP")
    public void checkNativeDemand() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        List<regionsEnum> regions = ssp.getRegions();
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        ssp.clickSettingsTab();
        for (regionsEnum region : regions) {
            ssp.selectRegion(region);
            ssp.assertDspNotPresentInList(true, "Native Demand");
            ssp.assertDspNotPresentInList(false, "Native Demand");
            softAssert.assertAll("Native Demand is present in the Allowed/Blocked DSP list in region: " + region);
        }
    }

    @Test
    @Issue("WP-1968")
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3780")
    @Description("Check Filter list section in endpoint")
    public void checkFilterListTable() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        SupplyPO ssp = new SupplyPO(softAssert);
        FilterListPO filter = new FilterListPO(softAssert);
        RandomUtility randomUtil = new RandomUtility();
        BrowserUtility browserUtil = new BrowserUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        FilterListDO filterData1 = new FilterListDO("FilterFromSsp_1_" + rnd, FilterListEnums.recordType.DOMAIN_BUNDLE, true, List.of("test.com")),
                filterData2 = new FilterListDO("FilterFromSsp_2_" + rnd, FilterListEnums.recordType.PUB_ID, false, List.of("3fh5ue-4f5h-4f5h-4f5h-4f5h4f5h4f5h"));
        String search = filterData1.getFilterName().substring(0, randomUtil.getRandomInt(1, filterData1.getFilterName().length()));
        login.login(StaticData.supportDefaultUser);
        ssp.gotoSupplySection();
        ssp.clickCreateEndpoint();
        ssp.clickFilterListsTab();
        ssp.clickCreateFilter();
        filterData1.setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData1);
        filter.clickSaveFilterList();
        filter.clickCreateFilterList();
        filterData2.setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData2);
        filter.clickSaveFilterList();
        browserUtil.closeTab();
        ssp.clickBreadcrumbItem(1);
        ssp.clickCreateEndpoint();
        ssp.clickFilterListsTab();
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

    //</editor-fold>
}
