package suites;

import common.SoftAssertCustom;
import common.utils.RandomUtility;
import common.utils.SystemUtility;
import data.AdaptersEnums;
import data.AdaptersEnums.adaptersApiEnum;
import data.AdaptersEnums.adaptersPrebidEnum;
import data.CommonEnums.*;
import data.StaticData;
import data.dataobject.AdapterDO;
import data.dataobject.EndpointDemandDO;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AdapterPO;
import pages.AuthorizationPO;
import pages.DemandPO;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static common.UserSettings.uploadFolder;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("API & Prebid Demand")
public class Adapters extends BaseSuiteClassNew {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("1672")
    @Description("Create API adapter setting manually")
    public void apiAdapterCreateManual() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000), settingsCount;
        File uploadFile = new File(uploadFolder + "/domain-bundles.csv");
        AdaptersEnums.adaptersApiEnum adapter = (AdaptersEnums.adaptersApiEnum) randomUtil.getRandomElement(AdaptersEnums.adaptersApiEnum.values());
        Map<adFormatPlacementTypeEnum, Boolean> adFormats = new LinkedHashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.NATIVE, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.REWARDED_VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.AUDIO, randomUtil.getRandomBoolean());
        }};
        Map<trafficTypeEnum, Boolean> traffics = new LinkedHashMap<>() {{
            put(trafficTypeEnum.UNKNOWN, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        List<AdapterDO.SizeSetting> sizes = new ArrayList<>() {{
            add(new AdapterDO.SizeSetting().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))));
            add(new AdapterDO.SizeSetting().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))));
            add(new AdapterDO.SizeSetting().setPredefinedSize((iabSizesEnum) randomUtil.getRandomElement(iabSizesEnum.getValues("endpoint"))));
        }};
        List<AdapterDO.GeoSetting> geos = new ArrayList<>() {{
            add(new AdapterDO.GeoSetting().addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values())))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values()))));
            add(new AdapterDO.GeoSetting().addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values())))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values()))));
            add(new AdapterDO.GeoSetting().addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values())))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values()))));
        }};
        List<String> recordList = new ArrayList<>() {{
            add("record1.com");
            add("com.record3.app");
            add("4265234125");
        }};
        AdapterDO adapterData = new AdapterDO().setApiAdapterType(adapter).setSettingName(adapter.publicName() + "-Manual-" + rnd).setIsImport(false).setTrafficMap(traffics).setAdFormatMap(adFormats).setGeoList(geos).setSizesList(sizes).setRecordList(recordList);
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        adapters.lookForAdapter(adapterData.getAdapterName());
        settingsCount = adapters.getAdapterSettingsCount(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapterData.setOsMap(adapters.getRandomOsMap(5));
        adapters.setupSettingManual(adapterData);
        adapterData.setCustomSettingsMap(adapters.setCustomFieldsRandom());
        adapters.assertBundlesOnCreation();
        adapters.clickSave();
        adapters.addFilterRecordsImport(uploadFile);
        adapters.addFilterRecordsManual(adapterData.getRecordList());
        adapterData.addRecordImport(uploadFile);
        adapters.clickSaveExitButton();
        settingsCount += 1;
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterSettingRow(adapterData);
        adapters.clickEditAdapterSetting(adapterData);
        adapters.assertAdapterSettingEdit(adapterData);
        softAssert.assertAll("Errors in Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("1671")
    @Description("Create Prebid adapter setting manually")
    public void prebidAdapterCreateManual() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000), settingsCount;
        File uploadFile = new File(uploadFolder + "/domain-bundles.csv");
        AdaptersEnums.adaptersPrebidEnum adapter = (AdaptersEnums.adaptersPrebidEnum) randomUtil.getRandomElement(AdaptersEnums.adaptersPrebidEnum.values());
        Map<adFormatPlacementTypeEnum, Boolean> adFormats = new LinkedHashMap<>() {{
            put(adFormatPlacementTypeEnum.BANNER, true);
            put(adFormatPlacementTypeEnum.VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.NATIVE, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.REWARDED_VIDEO, randomUtil.getRandomBoolean());
            put(adFormatPlacementTypeEnum.AUDIO, randomUtil.getRandomBoolean());
        }};
        Map<trafficTypeEnum, Boolean> traffics = new LinkedHashMap<>() {{
            put(trafficTypeEnum.UNKNOWN, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.WEB, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.APP, randomUtil.getRandomBoolean());
            put(trafficTypeEnum.MOBILE_WEB, true);
            put(trafficTypeEnum.CTV, randomUtil.getRandomBoolean());
        }};
        List<AdapterDO.GeoSetting> geos = new ArrayList<>() {{
            add(new AdapterDO.GeoSetting().addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values())))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values()))));
            add(new AdapterDO.GeoSetting().addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values())))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values()))));
            add(new AdapterDO.GeoSetting().addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addAdFormat((adFormatPlacementTypeEnum) randomUtil.getRandomElement(adFormatPlacementTypeEnum.values()))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values())))
                    .addCountry(((countriesEnum) randomUtil.getRandomElement(countriesEnum.values()))));
        }};
        List<String> recordList = new ArrayList<>() {{
            add("record1.com");
            add("com.record3.app");
            add("4265234125");
        }};
        AdapterDO adapterData = new AdapterDO().setPrebidAdapterType(adapter).setSettingName(adapter.publicName() + "-Manual-" + rnd).setIsImport(false).setTrafficMap(traffics).setAdFormatMap(adFormats).setGeoList(geos).setAllSizes(true).setRecordList(recordList);
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        adapters.lookForAdapter(adapterData.getAdapterName());
        settingsCount = adapters.getAdapterSettingsCount(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapterData.setOsMap(adapters.getRandomOsMap(3));
        adapters.setupSettingManual(adapterData);
        adapterData.setCustomSettingsMap(adapters.setCustomFieldsRandom());
        adapters.assertBundlesOnCreation();
        adapters.clickSave();
        adapters.addFilterRecordsImport(uploadFile);
        adapters.addFilterRecordsManual(adapterData.getRecordList());
        adapterData.addRecordImport(uploadFile);
        adapters.clickSaveExitButton();
        adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        settingsCount += 1;
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterSettingRow(adapterData);
        adapters.clickEditAdapterSetting(adapterData);
        adapters.assertAdapterSettingEdit(adapterData);
        softAssert.assertAll("Errors in Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("50034")
    @Description("Create API adapter setting - Import settings")
    public void apiAdapterCreateImport() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        SystemUtility systemUtility = new SystemUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000), settingsCount;
        File exampleFile, uploadFile, exportFile, expectedFile;
        adaptersApiEnum adapter = (adaptersApiEnum) randomUtil.getRandomElement(adaptersApiEnum.values());
        AdapterDO adapterData = new AdapterDO().setApiAdapterType(adapter).setSettingName(adapter.publicName() + "-Import-" + rnd).setIsImport(true);
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        adapters.lookForAdapter(adapterData.getAdapterName());
        settingsCount = adapters.getAdapterSettingsCount(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapters.clickManualImportToggle(true);
        adapters.inputSettingName(adapterData.getSettingName());
        exampleFile = adapters.clickDownloadExample(adapterData.getAdapterName());
        uploadFile = adapters.setImportAdditionalColumns(exampleFile);
        adapters.clickImportNewList(uploadFile);
        adapters.clickSaveExitButton();
        settingsCount += 1;
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterSettingRow(adapterData);
        adapters.clickEditAdapterSetting(adapterData);
        adapters.assertAdapterSettingEdit(adapterData);
        exportFile = adapters.clickDownloadExport(adapterData.getAdapterName(), adapterData.getSettingName());
        expectedFile = adapters.convertExampleList(uploadFile);
        adapters.assertAdapterImportFile(exportFile, expectedFile, adapterData.getAdapterName(), adapterData.getSettingName());
        systemUtility.allureAddFileAttachment("Example file", exampleFile);
        systemUtility.allureAddFileAttachment("Expected file", expectedFile);
        systemUtility.allureAddFileAttachment("Export file", exportFile);
        softAssert.assertAll("Errors in Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("51092")
    @Description("Create Prebid adapter setting - Import settings")
    public void prebidAdapterCreateImport() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        SystemUtility systemUtility = new SystemUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000), settingsCount;
        File exampleFile, uploadFile, exportFile, expectedFile;
        adaptersPrebidEnum adapter = (adaptersPrebidEnum) randomUtil.getRandomElement(adaptersPrebidEnum.values());
        AdapterDO adapterData = new AdapterDO().setPrebidAdapterType(adapter).setSettingName(adapter.publicName() + "-Import-" + rnd).setIsImport(true);
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        adapters.lookForAdapter(adapterData.getAdapterName());
        settingsCount = adapters.getAdapterSettingsCount(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapters.clickManualImportToggle(true);
        adapters.inputSettingName(adapterData.getSettingName());
        exampleFile = adapters.clickDownloadExample(adapterData.getAdapterName());
        uploadFile = adapters.setImportAdditionalColumns(exampleFile);
        adapters.clickImportNewList(uploadFile);
        adapters.clickSaveExitButton();
        adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        settingsCount += 1;
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterSettingRow(adapterData);
        adapters.clickEditAdapterSetting(adapterData);
        adapters.assertAdapterSettingEdit(adapterData);
        exportFile = adapters.clickDownloadExport(adapterData.getAdapterName(), adapterData.getSettingName());
        expectedFile = adapters.convertExampleList(uploadFile);
        adapters.assertAdapterImportFile(exportFile, expectedFile, adapterData.getAdapterName(), adapterData.getSettingName());
        systemUtility.allureAddFileAttachment("Example file", exampleFile);
        systemUtility.allureAddFileAttachment("Expected file", expectedFile);
        systemUtility.allureAddFileAttachment("Export file", exportFile);
        softAssert.assertAll("Errors in Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("51094")
    @Description("Replace the import file settings")
    public void replaceImportFile() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        SystemUtility systemUtility = new SystemUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        boolean isPrebid = randomUtil.getRandomBoolean();
        File example, upload, exportReplace, expectedReplace;
        AdapterDO adapterData = new AdapterDO().setIsImport(true);
        if (isPrebid) {
            adaptersPrebidEnum adapter = (adaptersPrebidEnum) randomUtil.getRandomElement(adaptersPrebidEnum.values());
            adapterData.setPrebidAdapterType(adapter).setSettingName(adapter.publicName() + "-Replace-" + rnd);
        } else {
            adaptersApiEnum adapter = (adaptersApiEnum) randomUtil.getRandomElement(adaptersApiEnum.values());
            adapterData.setApiAdapterType(adapter).setSettingName(adapter.publicName() + "-Replace-" + rnd);
        }
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        if (isPrebid) {
            adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        }
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapters.clickManualImportToggle(true);
        adapters.inputSettingName(adapterData.getSettingName());
        example = adapters.clickDownloadExample(adapterData.getAdapterName());
        upload = adapters.setImportAdditionalColumns(example);
        adapters.clickImportNewList(upload);
        adapters.clickSave();
        systemUtility.allureAddFileAttachment("Original file", upload);
        example = adapters.clickDownloadExample(adapterData.getAdapterName());
        upload = adapters.setImportAdditionalColumns(example);
        adapters.clickImportNewList(upload);
        adapters.clickReplaceModal(true);
        adapters.clickSave();
        systemUtility.allureAddFileAttachment("Replace file", upload);
        expectedReplace = adapters.convertExampleList(upload);
        exportReplace = adapters.clickDownloadExport(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterImportFile(exportReplace, expectedReplace, adapterData.getAdapterName(), adapterData.getSettingName());
        systemUtility.allureAddFileAttachment("Export file", exportReplace);
        softAssert.assertAll("Errors in Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("63714")
    @Description("Replace the import file settings")
    public void additionalImportFile() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        SystemUtility systemUtility = new SystemUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000);
        boolean isPrebid = randomUtil.getRandomBoolean();
        File example, uploadOriginal, uploadAdditional, uploadCombined, exportAdditional, expectedAdditional;
        AdapterDO adapterData = new AdapterDO().setIsImport(true);
        if (isPrebid) {
            adaptersPrebidEnum adapter = (adaptersPrebidEnum) randomUtil.getRandomElement(adaptersPrebidEnum.values());
            adapterData.setPrebidAdapterType(adapter).setSettingName(adapter.publicName() + "-Replace-" + rnd);
        } else {
            adaptersApiEnum adapter = (adaptersApiEnum) randomUtil.getRandomElement(adaptersApiEnum.values());
            adapterData.setApiAdapterType(adapter).setSettingName(adapter.publicName() + "-Additional-" + rnd);
        }
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        if (isPrebid) {
            adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        }
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapters.clickManualImportToggle(true);
        adapters.inputSettingName(adapterData.getSettingName());
        example = adapters.clickDownloadExample(adapterData.getAdapterName());
        uploadOriginal = adapters.setImportAdditionalColumns(example);
        adapters.clickImportNewList(uploadOriginal);
        adapters.clickSave();
        example = adapters.clickDownloadExample(adapterData.getAdapterName());
        uploadAdditional = adapters.setImportAdditionalColumns(example);
        adapters.clickImportAdditional(uploadAdditional);
        uploadCombined = adapters.mergeFilesExpected(uploadOriginal, uploadAdditional);
        adapters.clickSave();
        expectedAdditional = adapters.convertExampleList(uploadCombined);
        exportAdditional = adapters.clickDownloadExport(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterImportFile(exportAdditional, expectedAdditional, adapterData.getAdapterName(), adapterData.getSettingName());
        systemUtility.allureAddFileAttachment("Original file", uploadOriginal);
        systemUtility.allureAddFileAttachment("Additional file", uploadAdditional);
        systemUtility.allureAddFileAttachment("Export file", exportAdditional);
        softAssert.assertAll("Errors in Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("51099")
    @Description("User can delete adapter settings")
    public void deleteSetting() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000), settingsCount;
        boolean isPrebid = randomUtil.getRandomBoolean();
        AdapterDO adapterData = new AdapterDO().setIsImport(false).setAllSizes(true);
        if (isPrebid) {
            adaptersPrebidEnum adapter = (adaptersPrebidEnum) randomUtil.getRandomElement(adaptersPrebidEnum.values());
            adapterData.setPrebidAdapterType(adapter).setSettingName(adapter.publicName() + "-Delete-" + rnd);
        } else {
            adaptersApiEnum adapter = (adaptersApiEnum) randomUtil.getRandomElement(adaptersApiEnum.values());
            adapterData.setApiAdapterType(adapter).setSettingName(adapter.publicName() + "-Delete-" + rnd);
        }
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        if (isPrebid) {
            adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        }
        adapters.lookForAdapter(adapterData.getAdapterName());
        settingsCount = adapters.getAdapterSettingsCount(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapters.setupSettingManual(adapterData);
        adapterData.setCustomSettingsMap(adapters.setCustomFieldsRandom());
        adapters.clickSaveExitButton();
        if (isPrebid) {
            adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        }
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.clickDeleteAdapterSetting(adapterData);
        adapters.confirmDeleteSetting(adapterData, true);
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        softAssert.assertAll("Errors on deleting Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("63722")
    @Description("User can't delete API adapter settings, used in DSP endpoint")
    public void deleteApiSettingConnectedEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        DemandPO dsp = new DemandPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000), settingsCount;
        EndpointDemandDO.QpsSetting qpsSetting = new EndpointDemandDO.QpsSetting().setFixed(23445);
        adaptersApiEnum adapter = (adaptersApiEnum) randomUtil.getRandomElement(adaptersApiEnum.values());
        AdapterDO adapterData = new AdapterDO().setIsImport(false).setAllSizes(true).setApiAdapterType(adapter).setSettingName(adapter.publicName() + "-DeleteEndpoint-" + rnd);
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestRTBAdapterDelete" + rnd, endpointTypeEnum.RTB).setStatus(false).setEndpointUrl("http://testurl." + rnd + ".com").setQpsSettings(qpsSetting).setSpendLimit(1000).setBidFloorCpm(25).setTmax(700).addAdFormatSetting(adFormatPlacementTypeEnum.BANNER, true).addTrafficSetting(trafficTypeEnum.APP, true).setMarginSettings(new EndpointDemandDO.MarginSettings().setAdaptive()).setApiAdapter(adapterData.getApiAdapterType()).setSelectAllAdapterSettings(false).addAdapterSettingName(adapterData.getSettingName());
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        adapters.lookForAdapter(adapterData.getAdapterName());
        settingsCount = adapters.getAdapterSettingsCount(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapters.setupSettingManual(adapterData);
        adapterData.setCustomSettingsMap(adapters.setCustomFieldsRandom());
        adapters.clickSaveExitButton();
        settingsCount += 1;
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickSaveEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        adapters.gotoAdaptersSection();
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.clickDeleteAdapterSetting(adapterData);
        adapters.confirmDeleteSettingBlocked(endpointData.getId());
        adapters.modalClickConfirm();
        adapters.gotoAdaptersSection();
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.assertAdapterSettingRow(adapterData);
        softAssert.assertAll("Errors on deleting Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("63722")
    @Description("User can't delete Prebid adapter settings, used in DSP endpoint")
    public void deletePrebidSettingConnectedEndpoint() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        RandomUtility randomUtil = new RandomUtility();
        AuthorizationPO login = new AuthorizationPO();
        AdapterPO adapters = new AdapterPO(softAssert);
        DemandPO dsp = new DemandPO(softAssert);
        int rnd = randomUtil.getRandomInt(1, 10000), settingsCount;
        EndpointDemandDO.QpsSetting qpsSetting = new EndpointDemandDO.QpsSetting().setFixed(23445);
        adaptersPrebidEnum adapter = (adaptersPrebidEnum) randomUtil.getRandomElement(adaptersPrebidEnum.values());
        AdapterDO adapterData = new AdapterDO().setIsImport(false).setAllSizes(true).setPrebidAdapterType(adapter).setSettingName(adapter.publicName() + "-DeleteEndpoint-" + rnd);
        EndpointDemandDO endpointData = new EndpointDemandDO("autotestPrebidAdapterDelete" + rnd, endpointTypeEnum.PREBID).setStatus(false).setQpsSettings(qpsSetting).setSpendLimit(1000).setBidFloorCpm(25).setTmax(700).addAdFormatSetting(adFormatPlacementTypeEnum.BANNER, true).addTrafficSetting(trafficTypeEnum.APP, true).setMarginSettings(new EndpointDemandDO.MarginSettings().setAdaptive()).setPrebidAdapter(adapterData.getPrebidAdapterType()).setSelectAllAdapterSettings(false).addAdapterSettingName(adapterData.getSettingName());
        login.login(StaticData.supportDefaultUser);
        adapters.gotoAdaptersSection();
        adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        adapters.lookForAdapter(adapterData.getAdapterName());
        settingsCount = adapters.getAdapterSettingsCount(adapterData.getAdapterName());
        adapters.clickAddSetting(adapterData.getAdapterName());
        adapters.setupSettingManual(adapterData);
        adapterData.setCustomSettingsMap(adapters.setCustomFieldsRandom());
        adapters.clickSaveExitButton();
        settingsCount += 1;
        dsp.gotoDemandSection();
        dsp.clickCreateEndpoint();
        endpointData.setCompany(dsp.getRandomCompanyName(true))
                .setRegion(dsp.getRandomRegion());
        dsp.setupGeneralSettingsTab(endpointData);
        dsp.clickSaveEndpoint();
        endpointData.setId(dsp.getEndpointId(endpointData.getEndpointName()));
        adapters.gotoAdaptersSection();
        adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.clickDeleteAdapterSetting(adapterData);
        adapters.confirmDeleteSettingBlocked(endpointData.getId());
        adapters.modalClickConfirm();
        adapters.gotoAdaptersSection();
        adapters.clickAdapterTab(endpointTypeEnum.PREBID);
        adapters.lookForAdapter(adapterData.getAdapterName());
        adapters.clickAdapterWrap(adapterData.getAdapterName(), true);
        adapters.lookForAdapterSetting(adapterData.getAdapterName(), adapterData.getSettingName());
        adapters.assertAdapterRow(adapterData.getAdapterName(), settingsCount);
        adapters.assertAdapterSettingRow(adapterData);
        softAssert.assertAll("Errors on deleting Adapter's [" + adapterData.getAdapterName() + "] setting [" + adapterData.getSettingName() + "]");
    }

}
