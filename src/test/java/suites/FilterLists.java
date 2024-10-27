package suites;

import common.SoftAssertCustom;
import common.utils.ParseAndFormatUtility;
import common.utils.RandomUtility;
import data.FilterListEnums.recordType;
import data.dataobject.FilterListDO;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.DemandPO;
import pages.FilterListPO;
import pages.SupplyPO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static common.UserSettings.uploadFolder;
import static data.StaticData.supportDefaultUser;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("Filter lists")
public class FilterLists extends BaseSuiteClassNew {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("1169")
    @Description("Create Bundle/Domain filter list")
    public void createFilterBundleDomain() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        List<String> recordList = new ArrayList<>() {{
            add("record1.com, record2.eu");
            add("com.record3.app");
            add("4265234125");
        }};
        FilterListDO filterData = new FilterListDO("Bundle/Domain AUTO list" + rnd, recordType.DOMAIN_BUNDLE, randomUtil.getRandomBoolean(), recordList);
        File uploadFile = new File(uploadFolder + "/domain-bundles.csv");
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData);
        filter.addFilterRecordsImport(uploadFile);
        filterData.addRecordImport(uploadFile);
        filter.clickSaveFilterList();
        filter.assertFilterInTable(filterData);
        filter.clickFilterEdit(filterData.getFilterName());
        filter.assertFilterEdit(filterData);
        sAssert.assertAll("Errors in " + filterData.getRecordType().publicName() + " filter list: " + filterData.getFilterName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1169")
    @Description("Create Publisher ID filter list")
    public void createFilterPublisherId() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        File uploadFile = new File(uploadFolder + "/app-site-crid-pub.csv");
        List<String> recordList = new ArrayList<>() {{
            add("3fh5ue-4f5h-4f5h-4f5h-4f5h4f5h4f5h");
            add("dwsere3, pibid123");
        }};
        FilterListDO filterData = new FilterListDO("Publisher ID AUTO list" + rnd, recordType.PUB_ID, randomUtil.getRandomBoolean(), recordList);
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData);
        filter.addFilterRecordsImport(uploadFile);
        filterData.addRecordImport(uploadFile);
        filter.clickSaveFilterList();
        filter.assertFilterInTable(filterData);
        filter.clickFilterEdit(filterData.getFilterName());
        filter.assertFilterEdit(filterData);
        sAssert.assertAll("Errors in " + filterData.getRecordType().publicName() + " filter list: " + filterData.getFilterName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1169")
    @Description("Create Site/App ID filter list")
    public void createFilterSiteAppId() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        List<String> recordList = new ArrayList<>() {{
            add("288496df-99b33f8f-75a7ce48-37d1b480, fc741db0a2968c39d9c2a5cc75b05370");
            add("lebVZteiEsdpkncc");
            add("894534");
        }};
        File uploadFile = new File(uploadFolder + "/app-site-crid-pub.csv");
        FilterListDO filterData = new FilterListDO("Site/App ID AUTO list" + rnd, recordType.SITE_APP_ID, randomUtil.getRandomBoolean(), recordList);
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData);
        filter.addFilterRecordsImport(uploadFile);
        filterData.addRecordImport(uploadFile);
        filter.clickSaveFilterList();
        filter.assertFilterInTable(filterData);
        filter.clickFilterEdit(filterData.getFilterName());
        filter.assertFilterEdit(filterData);
        sAssert.assertAll("Errors in " + filterData.getRecordType().publicName() + " filter list: " + filterData.getFilterName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1169")
    @Description("Create CRID filter list")
    public void createFilterCrid() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        File uploadFile = new File(uploadFolder + "/app-site-crid-pub.csv");
        List<String> recordList = new ArrayList<>() {{
            add("288496df-99b33f8f-75a7ce48-37d1b480, fc741db0a2968c39d9c2a5cc75b05370");
            add("lebVZteiEsdpkncc");
            add("894534");
        }};
        FilterListDO filterData = new FilterListDO("CRID AUTO list" + rnd, recordType.CRID, randomUtil.getRandomBoolean(), recordList);
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData);
        filter.addFilterRecordsImport(uploadFile);
        filterData.addRecordImport(uploadFile);
        filter.clickSaveFilterList();
        filter.assertFilterInTable(filterData);
        filter.clickFilterEdit(filterData.getFilterName());
        filter.assertFilterEdit(filterData);
        sAssert.assertAll("Errors in " + filterData.getRecordType().publicName() + " filter list: " + filterData.getFilterName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1169")
    @Description("Create ADomains filter list")
    public void createFilterADomain() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        List<String> recordList = new ArrayList<>() {{
            add("record1.com, record2.eu");
            add("test.com");
            add("test.advert.org");
        }};
        File uploadFile = new File(uploadFolder + "/domain-bundles.csv");
        FilterListDO filterData = new FilterListDO("ADomains AUTO list" + rnd, recordType.ADOMAINS, randomUtil.getRandomBoolean(), recordList);
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData);
        filter.addFilterRecordsImport(uploadFile);
        filterData.addRecordImport(uploadFile);
        filter.clickSaveFilterList();
        filter.assertFilterInTable(filterData);
        filter.clickFilterEdit(filterData.getFilterName());
        filter.assertFilterEdit(filterData);
        sAssert.assertAll("Errors in " + filterData.getRecordType().publicName() + " filter list: " + filterData.getFilterName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1170")
    @Description("Filter list edit")
    public void editFilterList() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        SupplyPO ssp = new SupplyPO();
        DemandPO dsp = new DemandPO();
        RandomUtility randomUtil = new RandomUtility();
        ParseAndFormatUtility parseUtil = new ParseAndFormatUtility();
        int rnd = randomUtil.getRandomInt(0, 10000), sspId, dspId;
        List<String> recordList = new ArrayList<>() {{
            add("test.com");
        }};
        FilterListDO filterDataOriginal = new FilterListDO("Filter EDIT before-" + rnd, (recordType) randomUtil.getRandomElement(recordType.values()), true, recordList),
                filterDataEdited = new FilterListDO("Filter EDIT after-" + rnd, null, false, recordList).addRecord("testEdit.cds").setRecordType(filterDataOriginal.getRecordType());
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterDataOriginal.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterDataOriginal);
        filter.clickSaveFilterList();
        filter.clickFilterEdit(filterDataOriginal.getFilterName());
        filterDataEdited.addSspToList(filterDataOriginal.getSspList().keySet().stream().toList().get(0), false).addDspToList(filterDataOriginal.getDspList().keySet().stream().toList().get(0), false)
                .setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterDataEdited);
        filter.clickSaveFilterList();
        filter.assertFilterInTable(filterDataEdited);
        filter.clickFilterEdit(filterDataEdited.getFilterName());
        filter.assertFilterEdit(filterDataEdited);
        sspId = Integer.parseInt(parseUtil.parseByRegex(filterDataEdited.getSspList().keySet().stream().toList().get(1), "\\d+$", 0));
        dspId = Integer.parseInt(parseUtil.parseByRegex(filterDataEdited.getDspList().keySet().stream().toList().get(1), "\\d+$", 0));
        ssp.gotoEditPage(sspId);
        ssp.clickFilterListsTab();
        filter.assertFilterInTable(filterDataEdited);
        dsp.gotoEditPage(dspId);
        dsp.clickFilterListsTab();
        filter.assertFilterInTable(filterDataEdited);
        sAssert.assertAll("Error in editing Filter List " + filterDataEdited.getFilterName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("51770")
    @Description("Delete filter list")
    public void deleteFilterList() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        FilterListDO filterData = new FilterListDO("DELETE AUTO list" + rnd, recordType.PUB_ID, randomUtil.getRandomBoolean(), List.of("3fh5ue-4f5h-4f5h-4f5h-4f5h4f5h4f5h"));
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData);
        filter.clickSaveFilterList();
        filter.clickFilterDelete(filterData.getFilterName(), true);
        filter.assertFilterListNotInTable(filterData.getFilterName());
        sAssert.assertAll("Error in deleting Filter List");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("3789")
    @Description("Filter list is displayed in selected endpoints")
    public void filterListEndpointLink() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        SupplyPO ssp = new SupplyPO();
        DemandPO dsp = new DemandPO();
        RandomUtility randomUtil = new RandomUtility();
        ParseAndFormatUtility parseUtil = new ParseAndFormatUtility();
        int rnd = randomUtil.getRandomInt(0, 10000), sspId, dspId;
        FilterListDO filterData = new FilterListDO("Filter endpoint link list" + rnd, recordType.DOMAIN_BUNDLE, randomUtil.getRandomBoolean(), List.of("test.com"));
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        sspId = Integer.parseInt(parseUtil.parseByRegex(filterData.getSspList().keySet().stream().toList().get(0), "\\d+$", 0));
        dspId = Integer.parseInt(parseUtil.parseByRegex(filterData.getDspList().keySet().stream().toList().get(0), "\\d+$", 0));
        filter.setupFilterList(filterData);
        filter.clickSaveFilterList();
        ssp.gotoEditPage(sspId);
        ssp.clickFilterListsTab();
        filter.assertFilterInTable(filterData);
        dsp.gotoEditPage(dspId);
        dsp.clickFilterListsTab();
        filter.assertFilterInTable(filterData);
        sAssert.assertAll("Error in linking Filter List " + filterData.getFilterName() + " to endpoints");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("39383")
    @Description("Warning modal is displayed when user creates filter with all DSPs selected")
    public void filterListSelectAllDsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        FilterListDO filterData = new FilterListDO("Filter endpoint select all DSP" + rnd, recordType.DOMAIN_BUNDLE, true, List.of("test.com"));
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filter.setupFilterList(filterData);
        filter.clickSaveFilterListAllDsp(true);
        filter.assertFilterInTable(filterData);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1168")
    @Description("Search filter list by name and record")
    public void searchFilterList() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO(sAssert);
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        List<String> recordList = new ArrayList<>() {{
            add("srch.tst");
            add("l00k.tst");
        }};
        FilterListDO filterData = new FilterListDO("SearchAUTOlist" + rnd, recordType.ADOMAINS, true, recordList);
        String searchName = filterData.getFilterName().substring(0, randomUtil.getRandomInt(1, filterData.getFilterName().length() / 2)).toLowerCase();
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filterData.setSspList(filter.getRandomEndpoints(true, 1)).setDspList(filter.getRandomEndpoints(false, 1));
        filter.setupFilterList(filterData);
        filter.clickSaveFilterList();
        filter.searchFilterInTable(searchName);
        filter.assertFilterInTable(filterData);
        filter.searchFilterInTable((String) randomUtil.getRandomElement(recordList));
        filter.assertFilterInTable(filterData);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1182")
    @Description("User cannot upload empty list")
    public void uploadEmptyRecordList() {
        AuthorizationPO login = new AuthorizationPO();
        FilterListPO filter = new FilterListPO();
        File importFile = new File(uploadFolder + "/filterRecordsEmpty.csv");
        login.login(supportDefaultUser);
        filter.gotoFilterListSection();
        filter.clickCreateFilterList();
        filter.addFilterRecordsImport(importFile);
        filter.assertImportErrorModal();
    }

}
