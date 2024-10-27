package pages;

import com.opencsv.CSVReader;
import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.*;
import data.AdaptersEnums;
import data.ScannersEnums.scannerTypesEnum;
import data.dataobject.EndpointCommonDO.GeoSetting;
import data.dataobject.EndpointCommonDO.ScannerSetting;
import data.dataobject.EndpointDemandDO;
import data.dataobject.EndpointDemandDO.BannerSize;
import data.dataobject.EndpointDemandDO.VideoSize;
import data.textstrings.messages.DemandText;
import io.qameta.allure.Step;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static data.CommonEnums.*;
import static data.CommonEnums.endpointTypeEnum.RTB;
import static data.DemandEnum.*;

public class DemandPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final RandomUtility RandomUtil = new RandomUtility();
    private final FileUtility FileUtil = new FileUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final BrowserUtility BrowserUtil = new BrowserUtility();
    private final ParseAndFormatUtility ParseFormatUtil = new ParseAndFormatUtility();

    public DemandPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public DemandPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Demand dashboard page")
    public void gotoDemandSection() {
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(demandLink).click();
        Wait.waitForClickable(createEndpointButton);
        Wait.attributeNotContains(endpointListTable, "class", "hide");
    }

    @Step("Get endpoint ID from the database")
    public Integer getEndpointId(String endpointName) {
        Condition condition = DSL.field("name").equal(endpointName);
        Record resultRecord = dbUtils.getDbDataRecordFirst("dsp", condition);
        hardAssert.assertNotNull(resultRecord, "DSP Endpoint <" + endpointName + "> not found");
        return resultRecord.getValue("id", Integer.class);
    }

    @Step("Go to endpoint edit page by direct link")
    public void gotoEditPage(int endpointId) {
        driver.navigate().to(getRunURL()+ "/ad-exchange/dsp/" + endpointId + "/edit");
        Wait.waitForVisibility(nameInput);
    }

    //<editor-fold desc="DSP list">
    static final By
            exportCSVButton = By.xpath("//a[@id='exportDspList']"),
            createEndpointButton = By.xpath("//a[contains(@href, 'dsp/create')]"),
            endpointListTable = By.xpath("//div[@id='dspProfile']//table"),
            endpointListColumnHeader = By.xpath("//div[@id='dspProfile']//thead//th"),
            endpointListColumnHeaderSortIcon = By.xpath("./span/i"),
            endpointListColumnTotal = By.xpath("//div[@id='dspProfile']//tfoot//th"),
            endpointListRow = By.xpath("//div[@id='dspProfile']//tbody/tr"),
            endpointListId = By.xpath(".//td[@data-field='id']"),
            endpointListNameLink = By.xpath(".//td[@data-field='name']"),
            endpointListType = By.xpath(".//td[@data-field='endpoint_type']"),
            endpointListStatusToggle = By.xpath(".//td[@data-field='status']//input"),
            endpointListAdFormatIcon = By.xpath(".//td[@data-field]/i/.."),
            endpointListRegion = By.xpath(".//td[@data-field='region_id']"),
            endpointListQPSLimit = By.xpath(".//td[@data-field='max_qps_limit']"),
            endpointListQPSReal = By.xpath(".//td[@data-field='qps']"),
            endpointListQPSBid = By.xpath(".//td[@data-field='bid_qps']"),
            endpointListSpendYesterday = By.xpath(".//td[@data-field='spend_yesterday']"),
            endpointListSpendToday = By.xpath(".//td[@data-field='spend_today']"),
            endpointListSpendLimit = By.xpath(".//td[@data-field='spend_limit']"),
            endpointListWinRate = By.xpath(".//td[@data-field='win_rate']"),
            endpointListCompany = By.xpath(".//td[@data-field='company_id']"),
            endpointListCloneButton = By.xpath(".//td[@data-field='action']/a[@class='clone-endpoint']"),
            endpointListEmptyPlaceholder = By.xpath("//div[@id='dspProfile']//div[contains(@class, 'no-data')]/h4");

    @Step("Open endpoint creation page")
    public void clickCreateEndpoint() {
        Select.selector(createEndpointButton).click();
        Wait.waitForVisibility(nameInput);
        hardAssert.assertTrue(Select.selectByAttributeIgnoreCase(endpointTypeRadioButton, "value", "rtb").isSelected(), "RTB type is not selected by default");
    }

    @Step("Click on Export CSV button")
    public File clickExportCsv() {
        Select.selector(exportCSVButton).click();
        Wait.waitForTabClose();
        return FileUtil.getDownloadedFile("export-dsp.csv", 15);
    }

    @Step("Get endpoint table row")
    public WebElement getEndpointRow(Integer endpointId) {
        WebElement endpoint = Select.selectParentByTextEquals(endpointListRow, endpointListId, endpointId.toString());
        hardAssert.assertNotNull(endpoint, "There is no endpoint with the ID <" + endpointId + ">");
        return endpoint;
    }

    @Step("Get random endpoint ID from the list")
    public Integer getRandomEndpointIdFromList() {
        List<Integer> idList = new ArrayList<>();
        for (WebElement idElement : Select.multiSelector(endpointListRow, endpointListId)) {
            idList.add(Integer.valueOf(idElement.getText()));
        }
        return (Integer) RandomUtil.getRandomElement(idList);
    }

    @Step("Open endpoint edit page")
    public void openEndpointEditPage(Integer endpointId) {
        WebElement endpoint = getEndpointRow(endpointId);
        Select.selector(endpoint, endpointListNameLink).click();
        Wait.waitForVisibility(nameInput);
    }

    @Step("Click on column to sort")
    public void clickColumnSorting(dashboardColumnsEnum columnName) {
        Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", columnName.attributeName()).click();
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
    }

    @Step("Click Clone button")
    public void clickCloneEndpoint(Integer endpointId, boolean cloneConfirm) {
        WebElement endpointRow = getEndpointRow(endpointId);
        String endpointName = Select.selector(endpointRow, endpointListNameLink).getText();
        Select.selector(endpointRow, endpointListCloneButton).click();
        Wait.waitForVisibility(modalConfirmButton);
        softAssert.assertEquals(Select.selector(modalHeaderText).getText(), "Warning", "Cloning modal: header text is incorrect");
        softAssert.assertEquals(Select.selector(modalContentText).getText(), "Are you sure you want to clone the settings of " + endpointName + "?", "Cloning modal: content text is incorrect");
        if (cloneConfirm) {
            Select.selector(modalConfirmButton).click();
            Wait.waitForClickable(modalConfirmButton);
            softAssert.assertEquals(Select.selector(modalHeaderText).getText(), "Endpoint is cloned", "Cloned modal: header text is incorrect");
            softAssert.assertEquals(Select.selector(modalContentText).getText(), "Do you want to edit the endpoint?", "Cloned modal: content text is incorrect");
        } else {
            Select.selector(modalCancelButton).click();
            Wait.waitForNotVisible(modalDialog);
            Wait.waitForClickable(endpointListCloneButton);
        }
    }

    @Step("Open cloned endpoint edit page")
    public void openClonedEndpointEdit(boolean openEdit) {
        if (openEdit) {
            Select.selector(modalConfirmButton).click();
            Wait.waitForVisibility(nameInput);
        } else {
            Select.selector(modalCancelButton).click();
            Wait.waitForNotVisible(modalDialog);
            Wait.waitForClickable(endpointListCloneButton);
        }
    }

    @Step("Assert Endpoint data in the list")
    public void assertEndpointRowInfo(EndpointDemandDO epData) {
        WebElement endpointItem = getEndpointRow(epData.getId()), format;
        softAssert.assertEquals(Select.selector(endpointItem, endpointListNameLink).getText(), epData.getEndpointName(), "Table row: Wrong Endpoint name");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListType).getText(), epData.getEndpointType().publicName(), "Table row: Wrong endpoint Type");
        for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : epData.getAdFormatStatusMap().entrySet()) {
            format = Select.selectByAttributeContains(endpointItem, endpointListAdFormatIcon, "data-field", adFormatEntry.getKey().attributeName());
            if (AssertUtil.assertPresent(format, relative_iconElement)) {
                softAssert.assertEquals(Select.selector(format, relative_iconElement).getAttribute("class").contains("fa-check"), adFormatEntry.getValue().booleanValue(), "Table row: [" + adFormatEntry.getKey().attributeName() + "] format checkmark is incorrect");
            } else {
                softAssert.fail("Table row: [" + adFormatEntry.getKey().attributeName() + "] format checkmark icon is not present");
            }
        }
        softAssert.assertEquals(Select.selector(endpointItem, endpointListRegion).getText(), epData.getRegion().toString(), "Table row: Wrong Region");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListQPSLimit).getText(), ParseFormatUtil.formatBigNoDecimal(epData.getQpsSettings().getMax()), "Table row: Wrong QPS limit");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListQPSReal).getText(), ParseFormatUtil.formatBigNoDecimal(epData.getQpsReal()), "Table row: Wrong Real QPS");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListQPSBid).getText(), ParseFormatUtil.formatBigNoDecimal(epData.getQpsBid()), "Table row: Wrong BID QPS");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListSpendYesterday).getText(), "$" + ParseFormatUtil.formatBigCommaWithDecimal(epData.getSpendYesterday()), "Table row: Wrong Yesterday's Spend");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListSpendToday).getText(), "$" + ParseFormatUtil.formatBigCommaWithDecimal(epData.getSpendToday()), "Table row: Wrong Today's Spend");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListSpendLimit).getText(), "$" + ParseFormatUtil.formatBigCommaWithDecimal(epData.getSpendLimit()), "Table row: Wrong Spend Limit");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListWinRate).getText(), ParseFormatUtil.formatBigCommaWithDecimal(epData.getWinRate()) + "%", "Table row: Wrong Win rate");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListCompany).getText(), epData.getCompany(), "Table row: Wrong Company");
        softAssert.assertTrue(AssertUtil.assertPresent(endpointItem, endpointListCloneButton), "Table row: There is no Clone button");
    }

    @Step("Assert endpoints names on Dashboard list")
    public void assertAllEndpointRowsNames(String nameToContain) {
        for (WebElement dspRowName : Select.multiSelector(endpointListRow, endpointListNameLink)) {
            softAssert.assertTrue(dspRowName.getText().toLowerCase().contains(nameToContain), "Endpoint name <" + dspRowName.getText() + "> does not contain the symbols <" + nameToContain + ">");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in endpoints name search:");
        }
    }

    @Step("Assert Dashboard list is empty")
    public void assertDashboardEmpty() {
        hardAssert.assertEquals(Select.selector(endpointListEmptyPlaceholder).getText(), DemandText.EMPTY_DASHBOARD, "Empty search text is incorrect");
    }

    @Step("Assert endpoint is not present in the Dashboard")
    public void assertEndpointNotPresent(EndpointDemandDO epData) {
        for (WebElement dspRow : Select.multiSelector(endpointListRow)) {
            if (getTableCell(dspRow, dashboardColumnsEnum.NAME.attributeName()).getText().equals(epData.getEndpointName())) {
                softAssert.fail("Endpoint name <" + epData.getEndpointName() + "> is present on the dashboard");
            }
            if (getTableCell(dspRow, dashboardColumnsEnum.ID.attributeName()).getText().equals(epData.getId().toString())) {
                softAssert.fail("Endpoint with id <" + epData.getId() + "> is present on the dashboard");
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Endpoint is present in table:");
        }
    }

    @Step("Assert exported CSV file")
    public void assertFileExportCsv(File exportFile) {
        int rowCnt = 1;
        String[] rowCellsDashboard = new String[17];
        List<String> listColumnsHeadersDashboard = new ArrayList<>();
        List<String[]> parsedCsv = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(exportFile));
            parsedCsv = reader.readAll();
        } catch (Exception e) {
            hardAssert.fail("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        }
        List<WebElement> columnHeaders = Select.multiSelector(endpointListColumnHeader);
        for (int i = 0; i < columnHeaders.size() - 1; i++) {
            listColumnsHeadersDashboard.add(columnHeaders.get(i).getText().replace("’", "'").replace("limit", "Limit").replace("BID", "Bid"));
        }
        //Difference in column order and names is not a bug
        Collections.swap(listColumnsHeadersDashboard, 2, 3);
        Collections.swap(listColumnsHeadersDashboard, 5, 6);
        softAssert.assertEquals(Arrays.stream(parsedCsv.get(0)).toList(), listColumnsHeadersDashboard, "Column names are different");
        for (WebElement tableRow : Select.multiSelector(endpointListRow)) {
            List<String> listRowCellsDashboard, listRowCellsCsv;
            List<WebElement> rowCells = Select.multiSelector(tableRow, relative_generalTableBodyCell);
            for (int k = 0; k < rowCells.size() - 1; k++) {
                switch (k) {
                    //ID, DSP, Type, Region, Company
                    case 0, 1, 3, 8, 16 -> rowCellsDashboard[k] = rowCells.get(k).getText();
                    //Active
                    case 2 -> rowCellsDashboard[k] = Select.selector(tableRow, endpointListStatusToggle).isSelected() ? "1" : "0";
                    //Banner, Video, Native, Audio
                    case 4, 5, 6, 7 -> rowCellsDashboard[k] = Select.selector(rowCells.get(k), relative_iconElement).getAttribute("class").contains("fa-check") ? "1" : "0";
                    //QPS limit, Real QPS, Bid QPS
                    case 9, 10, 11 -> rowCellsDashboard[k] = rowCells.get(k).getText().replaceAll(",", "");
                    //Yesterday's Spend, Today's Spend, Win Rate
                    case 12, 13, 15 ->
                            rowCellsDashboard[k] = rowCells.get(k).getText().replace("$", "").replace("%", "").replaceAll(",", "");
                    //Spend Limit
                    case 14 -> rowCellsDashboard[k] = rowCells.get(k).getText().replace("$", "").replaceAll(",", "").replace(".00", "");
                }
            }
            listRowCellsDashboard = new ArrayList<>(Arrays.asList(rowCellsDashboard));
            Collections.swap(listRowCellsDashboard, 2, 3);
            Collections.swap(listRowCellsDashboard, 5, 6);
            listRowCellsCsv = new ArrayList<>(Arrays.asList(parsedCsv.get(rowCnt)));
            softAssert.assertEquals(listRowCellsCsv, listRowCellsDashboard, "Error on list item #[" + rowCnt + "]");
            rowCnt++;
        }
        int totalCnt = getPagingEntriesCount().get("total");
        softAssert.assertEquals(parsedCsv.size() - 1, totalCnt, "Error with number of entries");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors on file assertion:");
        }
    }

    @Step("Assert endpoints names on Dashboard list")
    public void assertAllEndpointRowsIds(String idToContain) {
        for (WebElement sspRowName : Select.multiSelector(endpointListRow, endpointListId)) {
            softAssert.assertTrue(sspRowName.getText().contains(idToContain), "Endpoint ID <" + sspRowName.getText() + "> does not contain the ID <" + idToContain + ">");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in endpoints ID search:");
        }
    }

    @Step("Assert table sorting by column")
    public void assertTableSorting(dashboardColumnsEnum columnName, boolean isAscending) {
        WebElement column = Select.selectByAttributeExact(endpointListColumnHeader, "data-field", columnName.attributeName());
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", dashboardColumnsEnum.TYPE.attributeName()), endpointListColumnHeaderSortIcon), "Sorting icon is present in column DSP when sorting by [" + columnName + "]");
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", dashboardColumnsEnum.STATUS.attributeName()), endpointListColumnHeaderSortIcon), "Sorting icon is present in column Active when sorting by [" + columnName + "]");
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", dashboardColumnsEnum.BANNER.attributeName()), endpointListColumnHeaderSortIcon), "Sorting icon is present in column Banner when sorting by [" + columnName + "]");
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", dashboardColumnsEnum.NATIVE.attributeName()), endpointListColumnHeaderSortIcon), "Sorting icon is present in column Native when sorting by [" + columnName + "]");
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", dashboardColumnsEnum.VIDEO.attributeName()), endpointListColumnHeaderSortIcon), "Sorting icon is present in column Video when sorting by [" + columnName + "]");
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", dashboardColumnsEnum.AUDIO.attributeName()), endpointListColumnHeaderSortIcon), "Sorting icon is present in column Audio when sorting by [" + columnName + "]");
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", dashboardColumnsEnum.ACTIONS.attributeName()), endpointListColumnHeaderSortIcon), "Sorting icon is present in column Action when sorting by [" + columnName + "]");
        switch (columnName) {
            case ID, REGION, NAME, COMPANY_ID ->
                    softAssert.assertEquals(Select.selector(column, endpointListColumnHeaderSortIcon).getAttribute("class").contains("amount-asc"), isAscending, "Sorting icon of [" + columnName + "] is incorrect");
            case QPS_REAL, QPS_LIMIT, QPS_BID, SPEND_YESTERDAY, SPEND_TODAY, WIN_RATE, SPEND_LIMIT ->
                    softAssert.assertEquals(Select.selector(column, endpointListColumnHeaderSortIcon).getAttribute("class").contains("numeric-asc"), isAscending, "Sorting icon of [" + columnName + "] is incorrect");
        }
        switch (columnName) {
            case ID, QPS_REAL, QPS_LIMIT, QPS_BID -> {
                List<Integer> columnItemsActual = new ArrayList<>();
                for (WebElement cell : Select.selectListByAttributeExact(relative_generalTableBodyCell, "data-field", columnName.attributeName())) {
                    columnItemsActual.add(Integer.valueOf(cell.getText().replaceAll(",", "")));
                }
                List<Integer> columnItemsSorted = new ArrayList<>(columnItemsActual);
                if (isAscending) {
                    Collections.sort(columnItemsSorted);
                } else {
                    Collections.sort(columnItemsSorted);
                    Collections.reverse(columnItemsSorted);
                }
                softAssert.assertEquals(columnItemsActual, columnItemsSorted, "Column [" + columnName + "] sorting is incorrect. isAscending = [" + isAscending + "]");
            }
            case REGION, NAME -> {
                List<String> columnItemsActual = new ArrayList<>();
                for (WebElement cell : Select.selectListByAttributeExact(relative_generalTableBodyCell, "data-field", columnName.attributeName())) {
                    columnItemsActual.add(cell.getText());
                }
                List<String> columnItemsSorted = new ArrayList<>(columnItemsActual);
                if (isAscending) {
                    Collections.sort(columnItemsSorted);
                } else {
                    Collections.sort(columnItemsSorted);
                    Collections.reverse(columnItemsSorted);
                }
                softAssert.assertEquals(columnItemsActual, columnItemsSorted, "Column [" + columnName + "] sorting is incorrect");
            }
            case SPEND_YESTERDAY, SPEND_TODAY, WIN_RATE, SPEND_LIMIT -> {
                List<Double> columnItemsActual = new ArrayList<>();
                for (WebElement cell : Select.selectListByAttributeExact(relative_generalTableBodyCell, "data-field", columnName.attributeName())) {
                    columnItemsActual.add(Double.parseDouble(cell.getText().replace("$", "").replace("%", "").replaceAll(",", "")));
                }
                List<Double> columnItemsSorted = new ArrayList<>(columnItemsActual);
                if (isAscending) {
                    Collections.sort(columnItemsSorted);
                } else {
                    Collections.sort(columnItemsSorted);
                    Collections.reverse(columnItemsSorted);
                }
                softAssert.assertEquals(columnItemsActual, columnItemsSorted, "Column [" + columnName + "] sorting is incorrect");
            }
            case COMPANY_ID -> {    //TODO company sort works by ID, not by name. Since the ID is nowhere to find in html code, we have to resort to this kind of tricks
                List<String> companiesColumnActualList = new ArrayList<>(), companiesExpectedDbList;
                Map<Integer, String> companiesExpectedFilteredMap = new HashMap<>();
                Stream<Entry<Integer, String>> sorted;
                for (WebElement cell : Select.selectListByAttributeExact(relative_generalTableBodyCell, "data-field", columnName.attributeName())) {
                    companiesColumnActualList.add(cell.getText());  //Collect companies from page into List. It is Actual result
                }
                for (Record record : dbUtils.getDbDataResult("companies", null)) {
                    //Filter out companies, that are not present on the page
                    if (companiesColumnActualList.contains(record.getValue("name", String.class))) {
                        companiesExpectedFilteredMap.put(record.getValue("id", Integer.class), record.getValue("name", String.class));
                    }
                }
                if (isAscending) {
                    sorted = companiesExpectedFilteredMap.entrySet().stream().sorted(Entry.comparingByKey()); //Sort DB values by ID ascending
                } else {
                    sorted = companiesExpectedFilteredMap.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByKey()));   //Sort DB values by ID descending
                }
                companiesExpectedDbList = new ArrayList<>(sorted.map(Entry::getValue).toList());    //Get sorted expected company names in the list
                LinkedHashSet<String> columnItemsUniqueSet = new LinkedHashSet<>(companiesColumnActualList);   //Keep only unique values from Actual companies list, so they will correspond to Expected list
                softAssert.assertEquals(columnItemsUniqueSet, companiesExpectedDbList, "Column [" + columnName + "] sorting is incorrect");
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in dashboard sorting when [" + columnName + "]");
        }
    }

    //</editor-fold>

    //<editor-fold desc="DSP list filters">
    static final By openFiltersButton = By.xpath("//a[@data-activates='dsp_filters_options']"),
            filtersSidebarBlock = By.xpath("//ul[@id='dsp_filters_options']"),
            filterStatusSelect = By.xpath("//select[@name='status_param']"),
            filterEndpointTypeSelect = By.xpath("//select[@name='endpoint_type']"),
            filterTrafficTypeSelect = By.xpath("//select[@name='traffic_type']"),
            filterAdFormatSelect = By.xpath("//select[@name='ad_type']"),
            filterSearchInput = By.xpath("//input[@id='searchDsp']");

    @Step("Open Filters options")
    public void clickOpenFilters() {
        Select.selector(openFiltersButton).click();
        Wait.waitForVisibility(filterStatusSelect, relative_selectLabel);
    }

    @Step("Close Filters options")
    public void clickCloseFilters() {
        int adjust = Select.selector(filtersSidebarBlock).getSize().getWidth();
        Select.actionClickWithAdjust(openFiltersButton, adjust + 10, 0);
        Wait.waitForClickable(createEndpointButton);
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
        Wait.waitForNotVisible(preloaderTable);
    }

    @Step("Select filter by Status")
    public void filterSelectStatus(Boolean status) {
        if (status != null) {
            selectDropdownOption(filterStatusSelect, status ? "Active" : "Inactive");
        } else {
            selectDropdownOption(filterStatusSelect, "All");
        }
    }

    @Step("Select filter by Endpoint type")
    public void filterSelectEndpointType(endpointTypeEnum type) {
        if (type != null) {
            selectDropdownOption(filterEndpointTypeSelect, type.publicName());
        } else {
            selectDropdownOption(filterEndpointTypeSelect, "All");
        }
    }

    @Step("Select filter by Traffic type")
    public void filterSelectTrafficType(Map<trafficTypeEnum, Boolean> trafficMap) {
        Select.selector(filterTrafficTypeSelect, relative_selectLabel).click();
        if (trafficMap != null && !trafficMap.isEmpty()) {
            for (Entry<trafficTypeEnum, Boolean> trafficEntry : trafficMap.entrySet()) {
                clickDropdownMultiselectOption(filterTrafficTypeSelect, trafficEntry.getKey().publicName(), trafficEntry.getValue());
                Wait.sleep(500);//Adding wait ot simulate user input, fast selection of options may cause an error
            }
        } else {
            clickDropdownMultiselectOption(filterTrafficTypeSelect, null, true);
        }
        closeMultiselectList(filterTrafficTypeSelect);
        Wait.sleep(1000);   //Added for stability, otherwise there is a console error when the filters are closed
    }

    @Step("Select filter by Ad format")
    public void filterSelectAdFormat(Map<adFormatPlacementTypeEnum, Boolean> adFormatsMap) {
        Select.selector(filterAdFormatSelect, relative_selectLabel).click();
        if (adFormatsMap != null && !adFormatsMap.isEmpty()) {
            for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : adFormatsMap.entrySet()) {
                clickDropdownMultiselectOption(filterAdFormatSelect, adFormatEntry.getKey().publicName(), adFormatEntry.getValue());
            }
        } else {
            selectDropdownOption(filterAdFormatSelect, "All");
        }
        closeMultiselectList(filterAdFormatSelect);
    }

    @Step("Get endpoints names list from the database")
    public List<String> getEndpointNamesList(Boolean endpointStatus) {
        Condition condition = DSL.field("status").in(endpointStatus == null ? (endpointStatus ? 1 : 0) : 0, 1);
        List<String> returnList = new ArrayList<>();
        for (Record record : dbUtils.getDbDataResult("dsp", condition)) {
            returnList.add(record.getValue("name", String.class));
        }
        return returnList;
    }

    @Step("Get endpoints IDs list from the database")
    public List<Integer> getEndpointIdsList(Boolean endpointStatus) {
        List<Integer> returnList = new ArrayList<>();
        Condition condition = DSL.field("status").in(endpointStatus == null ? (endpointStatus ? 1 : 0) : 0, 1);
        for (Record record : dbUtils.getDbDataResult("dsp", condition)) {
            returnList.add(record.getValue("id", Integer.class));
        }
        return returnList;
    }

    @Step("Search endpoint by Name/ID")
    public void searchNameIdInput(String searchRequest) {
        searchItemInTable(searchRequest, filterSearchInput, endpointListTable);
    }

    //todo create general filter assert method, pass all the filter settings and make SQL query based on them to get expected result
    @Step("Assert dashboard filtering by Status")
    public void assertDspFilterStatus(Boolean status) {
        Condition condition = DSL.field("status").in(status == null ? 0 : (status ? 1 : 0));
        Result<Record> resultRecords = dbUtils.getDbDataResult("dsp", condition);
        if (status != null) {
            if (resultRecords.isNotEmpty()) {
                for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                    softAssert.assertEquals(Select.selector(endpointRow, endpointListStatusToggle).isSelected(), status.booleanValue(), "Endpoint status is incorrect in endpoint <#" + Select.selector(endpointRow, endpointListId).getText() + ">");
                }
            } else {
                softAssert.assertEquals(Select.selector(endpointListEmptyPlaceholder).getText(), DemandText.EMPTY_DASHBOARD, "There should be no endpoints with status <" + status + ">");
            }
        } else {
            //todo There is nothing to check, when the status filter is All, really. This assertion is optional and can be deleted or improved
            for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                softAssert.assertNotNull(Select.selector(endpointRow, endpointListStatusToggle), "Endpoint status is not displayed in endpoint <#" + Select.selector(endpointRow, endpointListId).getText() + ">");
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in filtering by status:");
        }
    }

    @Step("Assert dashboard filtering by Endpoint type")
    public void assertDspFilterType(endpointTypeEnum type, Boolean status) {
        if (type != null) {
            //here we suppose that other filters are set to default values
            Condition condition = DSL.field("endpoint_type").equal(type.attributeName()).and(DSL.field("status").in(status != null ? (status ? 1 : 0) : 0, 1));
            Result<Record> dbResults = dbUtils.getDbDataResult("dsp", condition);
            if (dbResults.isNotEmpty()) {
                for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                    softAssert.assertEquals(Select.selector(endpointRow, endpointListType).getText(), type.publicName(), "Endpoint type is incorrect in endpoint <#" + Select.selector(endpointRow, endpointListId).getText() + ">");
                }
            } else {
                softAssert.assertEquals(Select.selector(endpointListEmptyPlaceholder).getText(), DemandText.EMPTY_DASHBOARD, "There should be no endpoints with type <" + type.publicName() + ">");
            }
        } else {
            //There is nothing to check, when the status filter is All, really. This assertion is optional and can be deleted or improved
            for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                softAssert.assertNotNull(Select.selector(endpointRow, endpointListType), "Endpoint type is not displayed in endpoint <#" + Select.selector(endpointRow, endpointListId).getText() + ">");
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in filtering by Endpoint type:");
        }
    }

    @Step("Assert dashboard filtering by Traffic type")
    public void assertDspFilterTraffic(Map<trafficTypeEnum, Boolean> trafficMap, Boolean status) {
        if (trafficMap != null && !trafficMap.isEmpty()) {
            //here we suppose that other filters are set to default values
            Condition condition = DSL.field("status").in(status != null ? (status ? 1 : 0) : 0, 1);
            for (Entry<trafficTypeEnum, Boolean> trafficEntry : trafficMap.entrySet()) {
                if (trafficEntry.getValue()) {
                    condition = condition.and(DSL.field(trafficEntry.getKey().attributeName()).equal(1));
                }
            }
            Result<Record> dbResults = dbUtils.getDbDataResult("dsp", condition);
            if (dbResults.isNotEmpty()) {
                for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                    int endpointId = ParseFormatUtil.parseInteger(Select.selector(endpointRow, endpointListId).getText());
                    softAssert.assertTrue(dbResults.getValues("id", Integer.class).contains(endpointId), "Filtered list does not contain endpoint <#" + endpointId + ">");
                }
            } else {
                softAssert.assertEquals(Select.selector(endpointListEmptyPlaceholder).getText(), DemandText.EMPTY_DASHBOARD, "There should be no endpoints with Traffic types <" + trafficMap + ">");
            }
        } else {
            //There is nothing to check, when the status filter is All, really. This assertion is optional and can be deleted or improved
            for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                softAssert.assertNotNull(Select.selector(endpointRow, endpointListType), "Endpoint type is not displayed in endpoint <#" + Select.selector(endpointRow, endpointListId).getText() + ">");
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in filtering by Traffic type:");
        }
    }

    @Step("Assert dashboard filtering by Ad Format")
    public void assertDspFilterAdFormat(Map<adFormatPlacementTypeEnum, Boolean> adFormatMap, Boolean status) {
        if (adFormatMap != null && !adFormatMap.isEmpty()) {
            Condition condition = DSL.field("status").in(status != null ? (status ? 1 : 0) : 0, 1);
            for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : adFormatMap.entrySet()) {
                if (adFormatEntry.getValue()) {
                    condition = condition.and(DSL.field(adFormatEntry.getKey().attributeName()).equal(1));
                }
            }
            Result<Record> dbResults = dbUtils.getDbDataResult("dsp", condition);
            if (dbResults.isNotEmpty()) {
                for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                    int endpointId = ParseFormatUtil.parseInteger(Select.selector(endpointRow, endpointListId).getText());
                    softAssert.assertTrue(dbResults.getValues("id", Integer.class).contains(endpointId), "Filtered list does not contain endpoint <#" + endpointId + ">");
                }
            } else {
                softAssert.assertEquals(Select.selector(endpointListEmptyPlaceholder).getText(), DemandText.EMPTY_DASHBOARD, "There should be no endpoints with Ad formats <" + adFormatMap + ">");
            }
        } else {
            //There is nothing to check, when the status filter is All, really. This assertion is optional and can be deleted or improved
            for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
                softAssert.assertNotNull(Select.selector(endpointRow, endpointListType), "Endpoint type is not displayed in endpoint <#" + Select.selector(endpointRow, endpointListId).getText() + ">");
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in filtering by Ad format:");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Endpoint page - General settings">
    static final By
            endpointTitleHeader = By.xpath("//h1[@class='title']"),
            settingsSectionItem = By.xpath("//a[@data-toggle]"),
            generalSettingsTab = By.xpath("//a[@data-toggle='#information']"),
            nameInput = By.xpath("//input[@id='name']"),
            companySavedLine = By.xpath("//div[@id='information']//div[@class='row'][3]/div[@class='col s6']"),
            regionSavedLine = By.xpath("//div[@id='information']//div[@class='row'][4]/div[@class='col s6']"),
            endpointTypeSavedLine = By.xpath("//div[@id='information']//div[@class='row'][5]/div[@class='col s6']"),
            statusToggle = By.xpath("//input[@id='status']"),
            companySelect = By.xpath("//select[@id='companyId']"),
            relative_validationErrorDiv = By.xpath("./..//div[@data-error]"),
            regionSelect = By.xpath("//select[@id='region_id']"),
            endpointTypeRadioButton = By.xpath("//input[@name='endpoint_type']"),
            endpointUrlInput = By.xpath("//textarea[@id='endpoint']"),
            macrosListButton = By.xpath("//a[@href='#viewTagManager']"),
            jsTagSizeSelect = By.xpath("//select[@id='iabsize']"),
            jsTagSizeWidthInput = By.xpath("//input[@id='width']"),
            jsTagSizeHeightInput = By.xpath("//input[@id='height']"),
            auctionTypeSelect = By.xpath("//select[@id='at']"),
            limitQPSInput = By.xpath("//input[@id='limit_qps']"),
            qpsLimitTypeSelect = By.xpath("//select[@id='qps_limit_type']"),
            qpsLimitMinInput = By.xpath("//input[@id='min_qps_limit']"),
            qpsLimitMaxInput = By.xpath("//input[@id='max_qps_limit']"),
            qpsAdvancedWrapButton = By.xpath("//a[@id='toggle-qps-advanced']"),
            qpsFrequencySelect = By.xpath("//select[@id='qps_recalculation']"),
            qpsOptimizationSelect = By.xpath("//select[@id='qps_optimization_by']"),
            qpsDirectPublishersToggle = By.xpath("//input[@id='is_prioritize_pub']"),
            qpsDirectSupplyToggle = By.xpath("//input[@id='is_prioritize_direct_ssp']"),
            spendLimitInput = By.xpath("//input[@id='spend_limit']"),
            cpmInput = By.xpath("//input[@id='fixed_bid']"),
            rtbVersionSelect = By.xpath("//select[@id='rtb_version']"),
            defaultTmaxInput = By.xpath("//input[@id='default_tmax']"),
            adapterTypeDropdown = By.xpath("//select[@id='adapterType']"),
            adapterSelectRtb = By.xpath("//select[@id='adapterSettingsRtb']"),
            adapterSelectPrebid = By.xpath("//select[@id='adapterSettingsPrebid']"),
            relative_adapterGroupsOption = By.xpath("./..//ul[@class='options']/li"),
            bidfloorMaxInput = By.xpath("//input[@id='max_bid']");

    @Step("Click on General settings tab")
    public void clickGeneralSettingsTab() {
        Select.selector(generalSettingsTab).click();
        Wait.waitForClickable(nameInput);
    }

    @Step("Save endpoint")
    public void clickSaveEndpoint() {
        Select.selector(saveButton).click();
        Wait.waitForVisibility(preloader);
        Wait.waitForNotVisible(preloader);
        Wait.waitForClickable(saveButton);
    }

    @Step("Save endpoint")
    public void clickSaveEndpointWithErrors() {
        Select.selector(saveButton).click();
        Wait.waitForNotVisible(preloader);
    }

    @Step("Save endpoint and exit to dashboard")
    public void clickSaveExitEndpoint() {
        Select.selector(saveExitButton).click();
        Wait.waitForClickable(createEndpointButton);
        Wait.waitForVisibility(endpointListRow, endpointListNameLink);
    }

    @Step("Set endpoint General settings")
    public void setupGeneralSettingsTab(EndpointDemandDO epData) {
        selectEndpointType(epData.getEndpointType());
        inputName(epData.getEndpointName());
        selectRegion(epData.getRegion());
        toggleStatus(epData.getStatus());
        selectCompany(epData.getCompany());
        setupQpsLimit(epData.getQpsSettings());
        inputSpendLimit(epData.getSpendLimit());
        setMargin(epData.getMarginSettings());
        toggleAdFormat(epData.getAdFormatStatusMap());
        toggleTrafficType(epData.getTrafficStatusMap());
        toggleAdvancedSetting(epData.getAdvancedStatusMap());
        switch (epData.getEndpointType()) {
            case RTB -> {
                inputEndpointUrl(epData.getEndpointUrl());
                selectAuctionType(epData.getAuctionType());
                selectRtbVersion(epData.getRtbVersion());
                if (epData.getAdapterType() != adapterTypeEnum.NONE) {
                    selectAdapterSettings(epData.getEndpointType(), epData.getApiAdapter().publicName(), epData.getAdapterSettingNames(), epData.getAdapterSelectAll());
                }
                inputTmax(epData.getTmax());
                inputBidfloor(epData.getBidFloorCpm());
            }
            case PREBID -> {
                selectAuctionType(epData.getAuctionType());
                selectAdapterSettings(epData.getEndpointType(), epData.getPrebidAdapter().publicName(), epData.getAdapterSettingNames(), epData.getAdapterSelectAll());
                inputTmax(epData.getTmax());
                inputBidfloor(epData.getBidFloorCpm());
            }
            case JS -> {
                inputEndpointUrl(epData.getEndpointUrl());
                selectJsTagSize(epData.getJsTagSize(), epData.getJsWidth(), epData.getJsHeight());
                if (!epData.getJsVastTagsList().isEmpty()) {
                    addMacrosFromModal(epData.getJsVastTagsList());
                }
                inputCpm(epData.getBidFloorCpm());
            }
            case VAST -> {
                inputEndpointUrl(epData.getEndpointUrl());
                if (!epData.getJsVastTagsList().isEmpty()) {
                    addMacrosFromModal(epData.getJsVastTagsList());
                }
                inputCpm(epData.getBidFloorCpm());
            }
        }
    }

    @Step("Input endpoint name")
    public void inputName(String name) {
        enterInInput(nameInput, name);
    }

    @Step("Select company")
    public void selectCompany(String companyName) {
        selectDropdownOption(companySelect, companyName);
    }

    @Step("Get random company")
    public String getRandomCompanyName(Boolean isActive) {
        List<String> companiesNames = new ArrayList<>();
        for (WebElement company : Select.multiSelector(companySelect, relative_selectOptionWithTitle)) {
            companiesNames.add(company.getAttribute("title"));
        }
        if (isActive != null) {
            if (isActive) {
                companiesNames.removeIf(c -> c.endsWith("(inactive)"));
            } else {
                companiesNames.removeIf(c -> !c.endsWith("(inactive)"));
            }
        }
        return companiesNames.get(new Random().nextInt(companiesNames.size()));
    }

    @Step("Change ON/OFF status")
    public void toggleStatus(boolean status) {
        clickToggle(statusToggle, status);
    }

    @Step("Get random region")
    public regionsEnum getRandomRegion() {
        List<regionsEnum> regions = new ArrayList<>();
        for (WebElement reg : Select.multiSelector(regionSelect, relative_selectOptionWithTitle)) {
            regions.add(regionsEnum.valueOf(reg.getAttribute("title")));
        }
        return regions.get(new Random().nextInt(regions.size()));
    }

    @Step("Get region of Native endpoint")
    public regionsEnum getNativeRegion() {
        return regionsEnum.valueOf(Select.multiSelector(regionSelect, relative_selectOptionWithTitle).get(0).getAttribute("title"));
    }

    @Step("Select endpoint region")
    public void selectRegion(regionsEnum region) {
        selectDropdownOption(regionSelect, region.toString());
    }

    @Step("Select endpoint type")
    public void selectEndpointType(endpointTypeEnum type) {
        clickRadioButton(endpointTypeRadioButton, type.toString().toLowerCase());
    }

    @Step("Input endpoint URL")
    public void inputEndpointUrl(String url) {
        enterInInput(endpointUrlInput, url);
    }

    @Step("Select JS Tag size")
    public void selectJsTagSize(iabSizesEnum jsSize, Integer w, Integer h) {
        selectDropdownOption(jsTagSizeSelect, jsSize.publicName());
        if (w != null) {
            enterInInput(jsTagSizeWidthInput, w);
        }
        if (h != null) {
            enterInInput(jsTagSizeHeightInput, h);
        }
    }

    @Step("Select auction type")
    public void selectAuctionType(auctionTypeEnum auction) {
        selectDropdownOption(auctionTypeSelect, auction.publicName());
    }

    @Step("Input Limit QPS")
    public void setupQpsLimit(EndpointDemandDO.QpsSetting qpsSetting) {
        Wait.sleep(1000);   //TODO temp fix to stabilize the dropdown option selection
        selectDropdownOption(qpsLimitTypeSelect, qpsSetting.getType().publicName());
        switch (qpsSetting.getType()) {
            case FIXED -> enterInInput(qpsLimitMaxInput, qpsSetting.getMax());
            case DYNAMIC -> {
                enterInInput(qpsLimitMinInput, qpsSetting.getMin());
                enterInInput(qpsLimitMaxInput, qpsSetting.getMax());
                if (!Select.selector(qpsFrequencySelect, relative_selectLabel).isDisplayed()) {
                    Select.selector(qpsAdvancedWrapButton).click();
                    Wait.waitForVisibility(qpsFrequencySelect, relative_selectLabel);
                }
                selectDropdownOption(qpsFrequencySelect, qpsSetting.getFrequency().publicName());
                selectDropdownOption(qpsOptimizationSelect, qpsSetting.getOptimization().publicName());
            }
            case DIRECT -> {
                enterInInput(qpsLimitMinInput, qpsSetting.getMin());
                enterInInput(qpsLimitMaxInput, qpsSetting.getMax());
                clickToggle(qpsDirectPublishersToggle, qpsSetting.getDirectPublishers());
                clickToggle(qpsDirectSupplyToggle, qpsSetting.getDirectSupply());
            }
        }
    }

    @Step("Input Spend limit")
    public void inputSpendLimit(Integer spendLimit) {
        enterInInput(spendLimitInput, spendLimit);
    }

    @Step("Input CPM")
    public void inputCpm(Double cpm) {
        enterInInput(cpmInput, cpm);
    }

    @Step("Select RTB Version")
    public void selectRtbVersion(rtbVersionEnum version) {
        selectDropdownOption(rtbVersionSelect, version.publicName());
    }

    @Step("Input Default Tmax")
    public void inputTmax(Integer tmax) {
        enterInInput(defaultTmaxInput, tmax);
    }

    @Step("Select adapter type")
    public void selectAdapterType(adapterTypeEnum type) {
        selectDropdownOption(adapterTypeDropdown, type.publicName());
    }

    @Step("Get DSP adapters list")
    public List<Object> getAdaptersList(endpointTypeEnum endpointType, boolean withSettings) {
        List<Object> returnList = new ArrayList<>();
        List<WebElement> adaptersList;
        By adapterSelect = endpointType.equals(RTB) ? adapterSelectRtb : adapterSelectPrebid, adapterTitle;
        Select.selector(adapterSelect, relative_selectLabel).click();
        if (withSettings) {
            adaptersList = Select.selectListByAttributeContains(adapterSelect, relative_adapterGroupsOption, "class", "group-select");
            adapterTitle = By.xpath("./div/label");
        } else {
            adaptersList = Select.selectListByAttributeContains(adapterSelect, relative_adapterGroupsOption, "class", "single-group");
            adapterTitle = By.xpath("./label");
        }
        if (endpointType.equals(RTB)) {
            for (WebElement adapterItem : adaptersList) {
                returnList.add(AdaptersEnums.adaptersApiEnum.getByPublicName(Select.selector(adapterItem, adapterTitle).getText().trim()));
            }
        } else {
            for (WebElement adapterItem : adaptersList) {
                returnList.add(AdaptersEnums.adaptersPrebidEnum.getByPublicName(Select.selector(adapterItem, adapterTitle).getText().trim()));
            }
        }
        closeMultiselectList(adapterSelect);
        return returnList;
    }

    @Step("Get DSP adapter settings list")
    public List<String> getAdapterSettingsList(endpointTypeEnum endpointType, String adapterName) {
        List<String> returnList = new ArrayList<>();
        By adapterSelect = endpointType.equals(RTB) ? adapterSelectRtb : adapterSelectPrebid;
        Select.selector(adapterSelect, relative_selectLabel).click();
        for (WebElement adapterItem : Select.selectListByAttributeContains(adapterSelect, relative_adapterGroupsOption, "class", "group-select")) {
            if (Select.selector(adapterItem, By.xpath("./div/label")).getText().equalsIgnoreCase(adapterName)) {
                for (WebElement settingRow : Select.multiSelector(adapterItem, By.xpath(".//li"))) {
                    returnList.add(settingRow.getText().trim());
                }
                break;
            }
        }
        closeMultiselectList(adapterSelect);
        return returnList;
    }

    @Step("Select adapter")
    public void selectAdapterSettings(endpointTypeEnum endpointType, String adapterName, List<String> settingNames, boolean selectAll) {
        WebElement adapterProvider = null;
        By adapterSelect = endpointType.equals(RTB) ? adapterSelectRtb : adapterSelectPrebid;
        Select.selector(adapterSelect, relative_selectLabel).click();
        List<WebElement> adaptersList = Select.selectListByAttributeContains(adapterSelect, relative_adapterGroupsOption, "class", "group");
        for (WebElement adapterItem : adaptersList) {
            String adapter = adapterItem.getAttribute("title");
            if (adapter.isBlank()) {    //Adapter with settings
                adapter = Select.selector(adapterItem, By.xpath("./div/label")).getText();
                if (adapter.equalsIgnoreCase(adapterName)) {
                    if (selectAll) {
                        adapterItem.click();
                    } else {
                        adapterProvider = adapterItem;
                        break;
                    }
                }
            } else {
                if (adapter.equalsIgnoreCase(adapterName)) {   //Adapter without settings
                    adapterItem.click();
                }
            }
        }
        if (settingNames != null && !settingNames.isEmpty()) {
            for (String settingName : settingNames) {
                for (WebElement row : Select.multiSelector(adapterProvider, relative_generalListItem)) {
                    if (row.getAttribute("title").equals(settingName)) {
                        row.click();
                        break;
                    }
                }
            }
        }
        closeMultiselectList(adapterSelect);
    }

    @Step("Open tag macros modal")
    public void openMacrosModal() {
        Select.selector(macrosListButton).click();
        Wait.waitForVisibility(macrosModalRow);
    }

    @Step("Close tag macros modal")
    public void closeMacrosModal() {
        modalClickClose(macrosModal);
    }

    @Step("Select and add tag macro from modal")
    public void addMacrosFromModal(List<macroTagsTypesEnum> macros) {
        openMacrosModal();
        for (macroTagsTypesEnum macro : macros) {
            WebElement macrosRow = Select.selectByAttributeIgnoreCase(macrosModalRow, "data-name", macro.attributeName());
            Select.actionMouseScroll(macrosRow);
            Select.selector(macrosRow, macrosModalAddButton).click();
        }
        closeMacrosModal();
    }

    @Step("Input Max Bid floor")
    public void inputBidfloor(Double bidFloor) {
        enterInInput(bidfloorMaxInput, bidFloor);
    }

    @Step("Assert endpoint name on info page")
    public void assertEndpointName(String name) {
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), name, "General settings page: Endpoint name is incorrect");
    }

    @Step("Assert endpoint ID on edit page")
    public void assertEndpointId(Integer endpointId) {
        String title = Select.selector(endpointTitleHeader).getText(), endpointIdActual = title.substring(title.indexOf("#") + 1, title.indexOf("|")).trim();
        softAssert.assertEquals(endpointIdActual, endpointId.toString(), "Endpoint ID in header is incorrect");
    }

    @Step("Assert existing endpoint General settings page")
    public void assertEndpointGeneral(EndpointDemandDO epData) {
        By adapterSelect = epData.getEndpointType().equals(RTB) ? adapterSelectRtb : adapterSelectPrebid;
        assertEndpointName(epData.getEndpointName());
        assertEndpointId(epData.getId());
        softAssert.assertEquals(Select.selector(statusToggle).isSelected(), epData.getStatus(), "General settings page: Endpoint status is incorrect");
        softAssert.assertEquals(Select.selector(endpointTypeSavedLine).getText(), epData.getEndpointType().publicName(), "General settings page: Endpoint type is incorrect");
        softAssert.assertEquals(Select.selector(companySavedLine).getText(), epData.getCompany(), "General settings page: Company name is incorrect");
        softAssert.assertEquals(Select.selector(regionSavedLine).getText(), epData.getRegion().toString(), "General settings page: Region is incorrect");
        softAssert.assertEquals(Select.selector(spendLimitInput).getAttribute("value"), epData.getSpendLimit().toString(), "General settings page: Spend limit is incorrect");
        softAssert.assertEquals(Select.selector(marginTypeSelect, relative_selectLabel).getText(), epData.getMarginSettings().getType().publicName(), "General settings page: Margin type is incorrect");
        softAssert.assertEquals(Select.selector(qpsLimitTypeSelect, relative_selectLabel).getText(), epData.getQpsSettings().getType().publicName(), "General settings page: QPS Limit Type is incorrect");
        switch (epData.getQpsSettings().getType()) {
            case FIXED ->
                    softAssert.assertEquals(Select.selector(qpsLimitMaxInput).getAttribute("value"), epData.getQpsSettings().getMax().toString(), "General settings page: Fixed QPS limit is incorrect");
            case DYNAMIC -> {
                if (!Select.selector(qpsFrequencySelect, relative_selectLabel).isDisplayed()) {
                    Select.selector(qpsAdvancedWrapButton).click();
                    Wait.waitForVisibility(qpsFrequencySelect, relative_selectLabel);
                }
                softAssert.assertEquals(Select.selector(qpsLimitMinInput).getAttribute("value"), epData.getQpsSettings().getMin().toString(), "General settings page: Min QPS limit is incorrect");
                softAssert.assertEquals(Select.selector(qpsLimitMaxInput).getAttribute("value"), epData.getQpsSettings().getMax().toString(), "General settings page: Max QPS limit is incorrect");
                softAssert.assertEquals(Select.selector(qpsFrequencySelect, relative_selectLabel).getText(), epData.getQpsSettings().getFrequency().publicName(), "General settings page: Frequency option is incorrect");
                softAssert.assertEquals(Select.selector(qpsOptimizationSelect, relative_selectLabel).getText(), epData.getQpsSettings().getOptimization().publicName(), "General settings page: Optimization option is incorrect");
            }
            case DIRECT -> {
                softAssert.assertEquals(Select.selector(qpsLimitMinInput).getAttribute("value"), epData.getQpsSettings().getMin().toString(), "General settings page: Min QPS limit is incorrect");
                softAssert.assertEquals(Select.selector(qpsLimitMaxInput).getAttribute("value"), epData.getQpsSettings().getMax().toString(), "General settings page: Max QPS limit is incorrect");
                softAssert.assertEquals(Select.selector(qpsDirectPublishersToggle).isSelected(), epData.getQpsSettings().getDirectPublishers(), "General settings page: Direct publishers toggle is incorrect");
                softAssert.assertEquals(Select.selector(qpsDirectSupplyToggle).isSelected(), epData.getQpsSettings().getDirectSupply(), "General settings page: Direct supply toggle is incorrect");
            }
        }
        switch (epData.getMarginSettings().getType()) {
            case FIXED ->
                    softAssert.assertEquals(Select.selector(marginFixedMinInput).getAttribute("value"), epData.getMarginSettings().getFixed().toString(), "General settings page: Fixed margin is incorrect");
            case RANGE -> {
                softAssert.assertEquals(Select.selector(marginFixedMinInput).getAttribute("value"), epData.getMarginSettings().getMin().toString(), "General settings page: Min margin is incorrect");
                softAssert.assertEquals(Select.selector(marginMaxInput).getAttribute("value"), epData.getMarginSettings().getMax().toString(), "General settings page: Max margin is incorrect");
            }
            case ADAPTIVE -> {
                softAssert.assertFalse(Select.selector(marginFixedMinInput).isDisplayed(), "General settings page: Min field is displayed for Adaptive margin");
                softAssert.assertFalse(Select.selector(marginMaxInput).isDisplayed(), "General settings page: Max field is displayed for Adaptive margin");
            }
        }
        switch (epData.getEndpointType()) {
            case RTB -> {
                softAssert.assertEquals(Select.selector(endpointUrlInput).getAttribute("value"), epData.getEndpointUrl(), "General settings page: Endpoint URL is incorrect");
                softAssert.assertEquals(Select.selector(auctionTypeSelect, relative_selectLabel).getText(), epData.getAuctionType().publicName(), "General settings page: Auction type is incorrect");
                softAssert.assertEquals(Select.selector(rtbVersionSelect, relative_selectLabel).getText(), epData.getRtbVersion().publicName(), "General settings page: RTB version is incorrect");
                softAssert.assertEquals(Select.selector(defaultTmaxInput).getAttribute("value"), epData.getTmax().toString(), "General settings page: Default TMAX is incorrect");
                softAssert.assertEquals(ParseFormatUtil.formatBigCommaWithDecimal(Double.valueOf(Select.selector(bidfloorMaxInput).getAttribute("value"))), ParseFormatUtil.formatBigCommaWithDecimal(epData.getBidFloorCpm()), "General settings page: Max BidFloor is incorrect");
                softAssert.assertEquals(Select.selector(adapterTypeDropdown, relative_selectLabel).getText(), epData.getAdapterType().publicName(), "General settings page: Adapter type is incorrect");
                if (epData.getAdapterType() != adapterTypeEnum.NONE) {
                    Select.selector(adapterSelect, relative_selectLabel).click();
                    List<WebElement> adaptersList = Select.selectListByAttributeNotContains(adapterSelect, relative_adapterGroupsOption, "class", "disabled");
                    softAssert.assertEquals(adaptersList.size(), 1, "General settings page: Adapter list size is incorrect");
                    softAssert.assertEquals(Select.selector(adaptersList.get(0), By.xpath("./div//label")).getText(), epData.getApiAdapter().publicName().toUpperCase(), "General settings page: Adapter name is incorrect");
                    List<String> adapterSettingsActual = new ArrayList<>();
                    for (WebElement row : Select.multiSelector(adaptersList.get(0), relative_generalListItem)) {
                        if (row.getAttribute("class").contains("selected")) {
                            adapterSettingsActual.add(row.getAttribute("title"));
                        }
                    }
                    closeMultiselectList(adapterSelect);
                    softAssert.assertEqualsNoOrder(adapterSettingsActual.toArray(), epData.getAdapterSettingNames().toArray(), "General settings page: Adapter settings are incorrect. Actual: " + adapterSettingsActual + ", Expected: " + epData.getAdapterSettingNames());
                } else {
                    softAssert.assertFalse(AssertUtil.assertPresent(adapterSelect), "General settings page: Adapter select should not be displayed");
                }
            }
            case PREBID -> {
                softAssert.assertEquals(Select.selector(auctionTypeSelect, relative_selectLabel).getText(), epData.getAuctionType().publicName(), "General settings page: Auction type is incorrect");
                softAssert.assertEquals(Select.selector(defaultTmaxInput).getAttribute("value"), epData.getTmax().toString(), "General settings page: Default TMAX is incorrect");
                softAssert.assertEquals(ParseFormatUtil.formatBigCommaWithDecimal(Double.valueOf(Select.selector(bidfloorMaxInput).getAttribute("value"))), ParseFormatUtil.formatBigCommaWithDecimal(epData.getBidFloorCpm()), "General settings page: Max BidFloor is incorrect");
                if (epData.getPrebidAdapter() != null) {
                    Select.selector(adapterSelect, relative_selectLabel).click();
                    List<WebElement> adaptersList = Select.selectListByAttributeNotContains(adapterSelect, relative_adapterGroupsOption, "class", "disabled");
                    softAssert.assertEquals(adaptersList.size(), 1, "General settings page: Adapter list size is incorrect");
                    softAssert.assertEquals(Select.selector(adaptersList.get(0), By.xpath("./div//label")).getText(), epData.getPrebidAdapter().publicName().toUpperCase(), "General settings page: Adapter name is incorrect");
                    List<String> adapterSettingsActual = new ArrayList<>();
                    for (WebElement row : Select.multiSelector(adaptersList.get(0), relative_generalListItem)) {
                        if (row.getAttribute("class").contains("selected")) {
                            adapterSettingsActual.add(row.getAttribute("title"));
                        }
                    }
                    closeMultiselectList(adapterSelect);
                    softAssert.assertEqualsNoOrder(adapterSettingsActual.toArray(), epData.getAdapterSettingNames().toArray(), "General settings page: Adapter settings are incorrect. Actual: " + adapterSettingsActual + ", Expected: " + epData.getAdapterSettingNames());
                }
            }
            case JS -> {
                softAssert.assertEquals(Select.selector(endpointUrlInput).getAttribute("value"), epData.getEndpointUrl(), "General settings page: Endpoint URL is incorrect");
                softAssert.assertEquals(Select.selector(jsTagSizeSelect, relative_selectLabel).getText(), epData.getJsTagSize().publicName(), "General settings page: selected JS Tag size is incorrect");
                softAssert.assertEquals(Select.selector(jsTagSizeWidthInput).getAttribute("value"), epData.getJsWidth().toString(), "General settings page: JS Tag Width is incorrect");
                softAssert.assertEquals(Select.selector(jsTagSizeHeightInput).getAttribute("value"), epData.getJsHeight().toString(), "General settings page: JS Tag Height is incorrect");
                softAssert.assertEquals(Select.selector(cpmInput).getAttribute("value"), epData.getBidFloorCpm().toString(), "General settings page: CPM is incorrect");
            }
            case VAST -> {
                softAssert.assertEquals(Select.selector(endpointUrlInput).getAttribute("value"), epData.getEndpointUrl(), "General settings page: Endpoint URL is incorrect");
                softAssert.assertEquals(Select.selector(cpmInput).getAttribute("value"), ParseFormatUtil.formatPriceNoZeros(epData.getBidFloorCpm()), "General settings page: CPM is incorrect");
            }
        }
        for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : epData.getAdFormatStatusMap().entrySet()) {
            WebElement adFormatToggle = getToggleByName(toggleInput, adFormatEntry.getKey().attributeName());
            softAssert.assertEquals(adFormatToggle.isSelected(), adFormatEntry.getValue().booleanValue(), "General settings page: AdFormat [" + adFormatEntry.getKey().attributeName() + "] status is incorrect");
        }
        for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : epData.getAdFormatAvailabilityMap().entrySet()) {
            WebElement adFormatToggle = getToggleByName(toggleInput, adFormatEntry.getKey().attributeName());
            softAssert.assertEquals(adFormatToggle.isEnabled(), adFormatEntry.getValue().booleanValue(), "General settings page: AdFormat [" + adFormatEntry.getKey().attributeName() + "] availability is incorrect");
        }
        for (Entry<trafficTypeEnum, Boolean> trafficEntry : epData.getTrafficStatusMap().entrySet()) {
            WebElement trafficTypeToggle = getToggleByName(toggleInput, trafficEntry.getKey().attributeName());
            softAssert.assertEquals(trafficTypeToggle.isSelected(), trafficEntry.getValue().booleanValue(), "General settings page: Traffic type [" + trafficEntry.getKey().attributeName() + "] status is incorrect");
        }
        for (Entry<trafficTypeEnum, Boolean> trafficEntry : epData.getTrafficAvailabilityMap().entrySet()) {
            WebElement trafficTypeToggle = getToggleByName(toggleInput, trafficEntry.getKey().attributeName());
            softAssert.assertEquals(trafficTypeToggle.isEnabled(), trafficEntry.getValue().booleanValue(), "General settings page: Traffic type [" + trafficEntry.getKey().attributeName() + "] availability is incorrect");
        }
        for (Entry<advancedSettingsEnum, Boolean> advancedEntry : epData.getAdvancedStatusMap().entrySet()) {
            WebElement advancedToggle = getToggleByName(toggleInput, advancedEntry.getKey().attributeName());
            softAssert.assertEquals(advancedToggle.isSelected(), advancedEntry.getValue().booleanValue(), "General settings page: Advanced setting [" + advancedEntry.getKey().attributeName() + "] status is incorrect");
        }
        for (Entry<advancedSettingsEnum, Boolean> advancedEntry : epData.getAdvancedAvailabilityMap().entrySet()) {
            if (advancedEntry.getValue()) {
                softAssert.assertTrue(AssertUtil.assertPresent(Select.selectByAttributeIgnoreCase(toggleInput, "name", advancedEntry.getKey().attributeName())), "General settings page: Advanced setting [" + advancedEntry.getKey().attributeName() + "] should be available, but it's not. ");
            } else {
                softAssert.assertNotNull(Select.selectByAttributeIgnoreCase(toggleInput, "name", advancedEntry.getKey().attributeName()).getAttribute("disabled"), "General settings page: Advanced setting [" + advancedEntry.getKey().attributeName() + "] should not be available, but it is. ");
            }
        }
    }

    @Step("Assert that Name value is shortened")
    public void assertNameValueLength(String originalName) {
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value").length(), 150, "General settings validation: Name input value length is incorrect");
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), originalName.substring(0, 150), "General settings validation: Name input value cut is incorrect");
    }

    @Step("Assert Validation messages of required settings in the General section")
    public void saveAndAssertValidationRequiredInfoPage(String nameErrorText, String companyErrorText, String endpointErrorText, String adapterErrorText, String qpsMinErrorText, String qpsMaxErrorText, String widthErrorText, String heightErrorText, String cpmErrorText) {
        clickSaveEndpointWithErrors();
        assertValidationError(softAssert, nameInput, nameErrorText);
        assertValidationError(softAssert, companySelect, relative_validationErrorDiv, companyErrorText);
        assertValidationError(softAssert, endpointUrlInput, relative_validationErrorDiv, endpointErrorText);
        assertValidationError(softAssert, adapterSelectPrebid, relative_selectValidationLabel2, adapterErrorText);
        assertValidationError(softAssert, qpsLimitMinInput, qpsMinErrorText);
        assertValidationError(softAssert, qpsLimitMaxInput, qpsMaxErrorText);
        assertValidationError(softAssert, cpmInput, cpmErrorText);
        assertValidationError(softAssert, jsTagSizeWidthInput, widthErrorText);
        assertValidationError(softAssert, jsTagSizeHeightInput, heightErrorText);
    }

    @Step("Assert Validation messages of optional settings in the General section")
    public void saveAndAssertValidationOptionalInfoPage(String marginMinErrorText, String marginMaxErrorText, String bidfloorErrorText, String spendLimitErrorText, String tmaxErrorText) {
        clickSaveEndpointWithErrors();
        assertValidationError(softAssert, marginFixedMinInput, marginMinErrorText);
        assertValidationError(softAssert, marginMaxInput, marginMaxErrorText);
        assertValidationError(softAssert, spendLimitInput, spendLimitErrorText);
        assertValidationError(softAssert, defaultTmaxInput, tmaxErrorText);
        assertValidationError(softAssert, bidfloorMaxInput, bidfloorErrorText);
        new Waits().sleep(1500);    //TODO temp Wait for browser popup to disappear
    }
    //</editor-fold>

    //<editor-fold desc="Endpoint page - Geo settings">
    private static final By
            geoSettingsTab = By.xpath("//a[@data-toggle='#geo_settings']"),
            geoWhitelistAddButton = By.xpath("//span[@id='addGeoQps']"),
            geoWhitelistRow = By.xpath("//div[@id='geo_qps-row']/div[@class='row']"),
            geoBlacklistSelect = By.xpath("//select[@id='geo_country_block']"),
            geoBidfloorAddButton = By.xpath("//span[@id='addGeoBidFloor']"),
            geoBidfloorRow = By.xpath("//div[@id='geo_bidfloor-row']/div[@class='row']"),
            relative_rowSettingInput = By.xpath(".//input[@type='number']");

    @Step("Click on Geo settings tab")
    public void clickGeoSettingsTab() {
        Select.selector(geoSettingsTab).click();
        Wait.waitForVisibility(geoWhitelistAddButton);
    }

    @Step("Add Geo whitelist row")
    public void clickGeoWhitelistAdd(int count) {
        clickOptionRowAdd(geoWhitelistRow, geoWhitelistAddButton, count);
    }

    @Step("Set Geo whitelist settings")
    public List<GeoSetting> setupGeoWhitelist(List<Integer> qpsList, Integer countriesCountMax) {
        List<GeoSetting> retList = new ArrayList<>();
        for (Integer qps : qpsList) {
            Map<String, Boolean> selectedCountries;
            clickGeoWhitelistAdd(1);
            int countriesCount = (countriesCountMax == null) ? 0 : RandomUtil.getRandomInt(1, countriesCountMax + 1);
            WebElement countrySelect = Select.selector(Select.multiSelector(geoWhitelistRow).get(0), relative_generalSelect);
            selectedCountries = getRandomActiveMultiselectOptionsAsMap(countrySelect, countriesCount);
            setGeoWhitelistRow(0, selectedCountries, qps);
            retList.add(new GeoSetting().setWhitelistQps(selectedCountries, qps));
        }
        return retList;
    }

    @Step("Set Geo whitelist settings")
    public void setGeoWhitelistRow(Integer rowNumber, Map<String, Boolean> countriesMap, Integer qps) {
        WebElement targetRow = Select.multiSelector(geoWhitelistRow).get(rowNumber), countrySelect = Select.selector(targetRow, relative_generalSelect);
        Select.selector(countrySelect, relative_selectLabel).click();
        for (Entry<String, Boolean> countryEntry : countriesMap.entrySet()) {
            clickDropdownMultiselectOption(countrySelect, countryEntry.getKey(), countryEntry.getValue());
        }
        closeMultiselectList(countrySelect);
        enterInInput(Select.selector(targetRow, relative_rowSettingInput), qps);
    }

    @Step("Delete Geo whitelist row")
    public void clickGeoWhitelistDelete(Integer rowNumber) {
        WebElement targetRow = Select.multiSelector(geoWhitelistRow).get(rowNumber);
        Select.selector(targetRow, relative_generalLink).click();
        Wait.waitForNotVisible(targetRow);
    }

    @Step("Get countries from Geo whitelist row")
    public Map<String, Boolean> getRandomBlacklistCountriesMap(int countriesCountMax) {
        return getRandomActiveMultiselectOptionsAsMap(geoBlacklistSelect, RandomUtil.getRandomInt(1, countriesCountMax + 1));
    }

    @Step("Set Geo Blacklist settings")
    public void setGeoBlacklist(GeoSetting blacklistSettings) {
        Select.selector(geoBlacklistSelect, relative_selectLabel).click();
        for (Entry<String, Boolean> geoEntry : blacklistSettings.getCountriesMap().entrySet()) {
            clickDropdownMultiselectOption(geoBlacklistSelect, geoEntry.getKey(), geoEntry.getValue());
        }
        closeMultiselectList(geoBlacklistSelect);
    }

    @Step("Add Geo Bidfloor row")
    public void clickGeoBidfloorAdd(int count) {
        clickOptionRowAdd(geoBidfloorRow, geoBidfloorAddButton, count);
    }

    @Step("Set Geo Bidfloor settings")
    public List<GeoSetting> setupGeoBidfloorCpm(List<Double> bidfloorCpmList, Integer countriesCountMax) {
        List<GeoSetting> retList = new ArrayList<>();
        for (Double bidfloor : bidfloorCpmList) {
            Map<String, Boolean> selectedCountries;
            clickGeoBidfloorAdd(1);
            int countriesCount = (countriesCountMax == null) ? 0 : RandomUtil.getRandomInt(1, countriesCountMax + 1);
            WebElement countrySelect = Select.selector(Select.multiSelector(geoBidfloorRow).get(0), relative_generalSelect);
            selectedCountries = getRandomActiveMultiselectOptionsAsMap(countrySelect, countriesCount);
            setGeoBidfloorCpmRow(0, selectedCountries, bidfloor);
            retList.add(new GeoSetting().setBidfloorCpm(selectedCountries, bidfloor));
        }
        return retList;
    }

    @Step("Set Geo Bidfloor/CPM settings")
    public void setGeoBidfloorCpmRow(Integer rowNumber, Map<String, Boolean> countriesMap, Double qps) {
        WebElement targetRow = Select.multiSelector(geoBidfloorRow).get(rowNumber), countrySelect = Select.selector(targetRow, relative_generalSelect);
        Select.selector(countrySelect, relative_selectLabel).click();
        for (Entry<String, Boolean> countryEntry : countriesMap.entrySet()) {
            clickDropdownMultiselectOption(countrySelect, countryEntry.getKey(), countryEntry.getValue());
        }
        closeMultiselectList(countrySelect);
        enterInInput(Select.selector(targetRow, relative_rowSettingInput), qps);
    }

    @Step("Delete Geo Bidfloor row")
    public void clickGeoBidfloorDelete(Integer rowNumber) {
        WebElement targetRow = Select.multiSelector(geoBidfloorRow).get(rowNumber);
        Select.selector(targetRow, relative_generalLink).click();
        Wait.waitForNotVisible(targetRow);
    }

    @Step("Assert Geo settings page")
    public void assertEndpointGeoSettings(EndpointDemandDO epData) {
        String blacklistString = getExpectedMultiselectText(epData.getGeoBlacklist().getCountriesMap(), "Select countries", 10);
        softAssert.assertEquals(Select.selector(geoBlacklistSelect, relative_selectLabel).getText(), blacklistString, "Geo settings page: GEO Blacklist is incorrect");
        if (epData.getGeoQpsWhitelist() != null) {
            List<String> wlListActual = new ArrayList<>(), wlListExpected = new ArrayList<>();
            for (GeoSetting geoQps : epData.getGeoQpsWhitelist()) {
                wlListExpected.add(getExpectedMultiselectText(geoQps.getCountriesMap(), "Select countries", 3) + "==" + geoQps.getQps());
            }
            for (WebElement whitelistActualRow : Select.multiSelector(geoWhitelistRow)) {
                int qps = Integer.parseInt(Select.selector(whitelistActualRow, relative_rowSettingInput).getAttribute("value"));
                WebElement countrySelect = Select.selector(whitelistActualRow, relative_generalSelect);
                wlListActual.add(Select.selector(countrySelect, relative_selectLabel).getText() + "==" + qps);
            }
            softAssert.assertEqualsNoOrder(wlListActual.toArray(), wlListExpected.toArray(), "Geo settings page: Geo Whitelist settings are incorrect");
        }
        if (epData.getGeoBidfloorCpmList() != null) {
            List<String> bidfloorCpmListActual = new ArrayList<>(), bidfloorCpmListExpected = new ArrayList<>();
            for (GeoSetting geoQps : epData.getGeoBidfloorCpmList()) {
                bidfloorCpmListExpected.add(getExpectedMultiselectText(geoQps.getCountriesMap(), "Select countries", 3) + "==" + geoQps.getBidfloorCpm());
            }
            for (WebElement bidfloorCpmActualRow : Select.multiSelector(geoBidfloorRow)) {
                double bidfloorCpm = Double.parseDouble(Select.selector(bidfloorCpmActualRow, relative_rowSettingInput).getAttribute("value"));
                WebElement countrySelect = Select.selector(bidfloorCpmActualRow, relative_generalSelect);
                bidfloorCpmListActual.add(Select.selector(countrySelect, relative_selectLabel).getText() + "==" + bidfloorCpm);
            }
            softAssert.assertEqualsNoOrder(bidfloorCpmListActual.toArray(), bidfloorCpmListExpected.toArray(), "Geo settings page: Geo Bidfloor/CPM settings are incorrect");
        }
    }

    @Step("Assert Validation messages of settings in the Geo settings section")
    public void assertValidationGeoPage(Map<Integer, Entry<String, String>> geoWhitelist, Integer qpsMax, Map<Integer, Entry<String, String>> geoBidfloor, Double bidfloorMax) {
        if (geoWhitelist != null) {
            for (Entry<Integer, Entry<String, String>> whitelistEntry : geoWhitelist.entrySet()) {
                WebElement whitelistRow = Select.multiSelector(geoWhitelistRow).get(whitelistEntry.getKey());
                assertValidationError(softAssert, Select.selector(whitelistRow, relative_generalSelect), relative_validationErrorDiv, whitelistEntry.getValue().getKey());
                assertValidationError(softAssert, Select.selector(whitelistRow, relative_rowSettingInput), relative_validationErrorDiv, whitelistEntry.getValue().getValue().replace("${valueCap}", qpsMax.toString()));
            }
        }
        if (geoBidfloor != null) {
            for (Entry<Integer, Entry<String, String>> bidfloorEntry : geoBidfloor.entrySet()) {
                WebElement bidfloorRow = Select.multiSelector(geoBidfloorRow).get(bidfloorEntry.getKey());
                if (bidfloorEntry.getValue().getKey() != null) {
                    assertValidationError(softAssert, Select.selector(bidfloorRow, relative_generalSelect), relative_validationErrorDiv, bidfloorEntry.getValue().getKey());
                }
                if (bidfloorEntry.getValue().getValue() != null) {
                    assertValidationError(softAssert, Select.selector(bidfloorRow, relative_rowSettingInput), relative_validationErrorDiv, bidfloorEntry.getValue().getValue().replace("${valueCap}", ParseFormatUtil.formatPriceNoZeros(bidfloorMax)));
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Endpoint page - Filter lists">
    private static final By
            filterListsTab = By.xpath("//a[@data-toggle='#filter_list_dashboard']"),
            filterlistCreateButton = By.xpath("//a[contains(@href, 'filter-list/create')]");

    @Step("Click on Filter lists tab")
    public void clickFilterListsTab() {
        Select.selector(filterListsTab).click();
        Wait.waitForVisibility(filterlistCreateButton);
    }

    @Step("Click on Create Filter list button")
    public void clickCreateFilter() {
        Select.selector(filterlistCreateButton).click();
        BrowserUtil.switchTab();
        Wait.waitForVisibility(recordsAddButton);
        Wait.waitForNotVisible(recordsLoader);
    }

    //</editor-fold>

    //<editor-fold desc="Endpoint page - Additional info">
    private static final By
            additionalInfoTab = By.xpath("//a[@data-toggle='#additional_info']"),
            bannerSizesAddButton = By.xpath("//span[@id='addSizeWhiteList']"),
            bannerSizesRow = By.xpath("//div[@id='sizes']/div"),
            videoSizesAddButton = By.xpath("//span[@id='addVideoSizeWhiteList']"),
            videoSizesRow = By.xpath("//div[@id='sizeVideo']/div"),
            relative_sizesWidthInput = By.xpath(".//input[contains(@name, 'width')]"),
            relative_sizesHeightInput = By.xpath(".//input[contains(@name, 'height')]"),
            osSelect = By.xpath("//select[@id='os_filter']"),
            seatAddButton = By.xpath("//div[@data-endpoint='rtb']//span[contains(@class, 'add-seat')]"),
            seatRow = By.xpath("//div[@id='seat_lists']/div"),
            relative_seatNameInput = By.xpath(".//input[contains(@name, 'name')]"),
            relative_seatIdInput = By.xpath(".//input[contains(@name, 'value')]"),
            allowedSspSelect = By.xpath("//select[@id='white_endpoints']"),
            blockedSspSelect = By.xpath("//select[@id='black_endpoints']"),
            allowedInventorySelect = By.xpath("//select[@id='white_inventories']"),
            blockedInventorySelect = By.xpath("//select[@id='black_inventories']");

    @Step("Click on Additional info tab")
    public void clickAdditionalInfoTab() {
        Select.selector(additionalInfoTab).click();
        Wait.waitForVisibility(syncUrlInput);
    }

    @Step("Setup Additional info section")
    public void setupAdditionalInfo(EndpointDemandDO epData) {
        if (!epData.getBannerSizesList().isEmpty()) {
            for (BannerSize bSize : epData.getBannerSizesList()) {
                clickBannerSizeAdd(1);
                setBannerSize(bSize.getSize(), bSize.getWidth(), bSize.getHeight());
            }
        }
        if (!epData.getVideoSizesList().isEmpty()) {
            for (VideoSize vSize : epData.getVideoSizesList()) {
                clickVideoSizeAdd(1);
                setVideoSize(vSize.getSize(), vSize.getWidth(), vSize.getHeight());
            }
        }
        if (!epData.getOsMap().isEmpty()) {
            selectOs(epData.getOsMap());
        }
        if (!epData.getSeatMap().isEmpty()) {
            for (Entry<String, String> seatEntry : epData.getSeatMap().entrySet()) {
                clickSeatAdd(1);
                setSeatInfo(seatEntry.getKey(), seatEntry.getValue());
            }
        }
        if (!epData.getSspAllowedMap().isEmpty()) {
            selectAllowedBlockedItems(allowedSspSelect, blockedSspSelect, true, epData.getSspAllowedMap());
        }
        if (!epData.getSspBlockedMap().isEmpty()) {
            selectAllowedBlockedItems(allowedSspSelect, blockedSspSelect, false, epData.getSspBlockedMap());
        }
        if (!epData.getInventoryAllowedMap().isEmpty()) {
            selectAllowedBlockedItems(allowedInventorySelect, blockedInventorySelect, true, epData.getInventoryAllowedMap());
        }
        if (!epData.getInventoryBlockedMap().isEmpty()) {
            selectAllowedBlockedItems(allowedInventorySelect, blockedInventorySelect, false, epData.getInventoryBlockedMap());
        }
        inputPartnersApiLink(epData.getReportApiLinkPartner());
        clickCheckPartnersApiUrl(epData.getPartnerApiType());
        if (epData.getPartnerApiType().equals(endpointPartnerApiTypeEnum.CSV)) {
            setPartnersApiCsvColumns(epData.getCsvColumnsMap());
        }
    }

    @Step("Add banner sizes row")
    public void clickBannerSizeAdd(int count) {
        clickOptionRowAdd(bannerSizesRow, bannerSizesAddButton, count);
    }

    @Step("Select banner size")
    public void setBannerSize(iabSizesEnum size, Integer width, Integer height) {
        selectDropdownOption(Select.selector(bannerSizesRow, relative_generalSelect), size.publicName());
        if (size.equals(iabSizesEnum.CUSTOM)) {
            enterInInput(Select.selector(bannerSizesRow, relative_sizesWidthInput), width);
            enterInInput(Select.selector(bannerSizesRow, relative_sizesHeightInput), height);
        }
    }

    @Step("Delete banner size row")
    public void clickBannerSizeDelete(Integer rowNumber) {
        WebElement targetRow = Select.multiSelector(bannerSizesRow).get(rowNumber);
        Select.selector(targetRow, relative_generalLink).click();
        Wait.waitForNotVisible(targetRow);
    }

    @Step("Add video sizes row")
    public void clickVideoSizeAdd(int count) {
        clickOptionRowAdd(videoSizesRow, videoSizesAddButton, count);
    }

    @Step("Select video size")
    public void setVideoSize(videoSizeEnum size, Integer width, Integer height) {
        selectDropdownOption(Select.selector(videoSizesRow, relative_generalSelect), size.publicName());
        if (size.equals(videoSizeEnum.CUSTOM)) {
            enterInInput(Select.selector(videoSizesRow, relative_sizesWidthInput), width);
            enterInInput(Select.selector(videoSizesRow, relative_sizesHeightInput), height);
        }
    }

    @Step("Delete video size row")
    public void clickVideoSizeDelete(Integer rowNumber) {
        WebElement targetRow = Select.multiSelector(videoSizesRow).get(rowNumber);
        Select.selector(targetRow, relative_generalLink).click();
        Wait.waitForNotVisible(targetRow);
    }

    @Step("Select random OS items")
    public Map<String, Boolean> getRandomOsMap(int count) {
        return getRandomActiveMultiselectOptionsAsMap(osSelect, count);
    }

    @Step("Select random OS items")
    public void selectOs(Map<String, Boolean> osMap) {
        Select.selector(osSelect, relative_selectLabel).click();
        for (Entry<String, Boolean> osEntry : osMap.entrySet()) {
            clickDropdownMultiselectOption(osSelect, osEntry.getKey(), osEntry.getValue());
        }
        closeMultiselectList(osSelect);
    }

    @Step("Add seat settings row")
    public void clickSeatAdd(int count) {
        clickOptionRowAdd(seatRow, seatAddButton, count);
    }

    @Step("Fill in seat settings")
    public void setSeatInfo(String name, String id) {
        enterInInput(Select.selector(seatRow, relative_seatNameInput), name);
        enterInInput(Select.selector(seatRow, relative_seatIdInput), id);
    }

    @Step("Delete seat settings row")
    public void clickSeatDelete(int rowNumber) {
        WebElement targetRow = Select.multiSelector(seatRow).get(rowNumber);
        Select.selector(targetRow, relative_generalLink).click();
        Wait.waitForNotVisible(targetRow);
    }

    @Step("Select random items in Allowed/Blocked SSP list")
    public Map<String, Boolean> selectRandomAllowedBlockedSsp(boolean isAllowed, int sspCount) {
        return selectRandomAllowedBlockedListItems(allowedSspSelect, blockedSspSelect, isAllowed, sspCount);
    }

    @Step("Select item in Allowed/Blocked SSP list")
    public void selectAllowedBlockedSsp(boolean isAllowed, String endpointName, boolean toSelect) {
        By selectedList = isAllowed ? allowedSspSelect : blockedSspSelect;
        Select.selector(selectedList, relative_selectLabel).click();
        clickDropdownMultiselectOption(selectedList, endpointName, toSelect);
        closeMultiselectList(selectedList);
    }

    @Step("Select random items in Allowed/Blocked Inventory list")
    public Map<String, Boolean> selectRandomAllowedBlockedInventory(boolean isAllowed, int inventoryCount) {
        return selectRandomAllowedBlockedListItems(allowedInventorySelect, blockedInventorySelect, isAllowed, inventoryCount);
    }

    @Step("Assert Additional Info page")
    public void assertEndpointAdditionalInfo(EndpointDemandDO epData) {
        WebElement sizeSelect;
        String allowSspString = getExpectedMultiselectText(epData.getSspAllowedMap(), "All SSP", 10),
                allowInventoryString = getExpectedMultiselectText(epData.getInventoryAllowedMap(), "All Inventory", 10),
                blockSspString = getExpectedMultiselectText(epData.getSspBlockedMap(), "Select", 10),
                blockInventoryString = getExpectedMultiselectText(epData.getInventoryBlockedMap(), "Select", 10),
                osString = getExpectedMultiselectText(epData.getOsMap(), "Select", 10);
        if (epData.getBannerSizesList() != null && !epData.getBannerSizesList().isEmpty()) {
            Map<String, String> bannerMapActual = new HashMap<>(), bannerMapExpected = new HashMap<>();
            for (BannerSize bSize : epData.getBannerSizesList()) {
                bannerMapExpected.put(bSize.getSize().publicName(), bSize.getWidth() + "x" + bSize.getHeight());
            }
            for (WebElement bannerActualRow : Select.multiSelector(bannerSizesRow)) {
                sizeSelect = Select.selector(bannerActualRow, relative_generalSelect);
                String size = Select.selector(bannerActualRow, relative_sizesWidthInput).getAttribute("value") + "x" + Select.selector(bannerActualRow, relative_sizesHeightInput).getAttribute("value");
                bannerMapActual.put(Select.selector(sizeSelect, relative_selectLabel).getText(), size);
            }
            softAssert.assertEquals(bannerMapActual, bannerMapExpected, "Additional info page: Banner sizes list is incorrect");
        }
        if (epData.getVideoSizesList() != null && !epData.getVideoSizesList().isEmpty()) {
            Map<String, String> videoMapActual = new HashMap<>(), videoMapExpected = new HashMap<>();
            for (VideoSize vSize : epData.getVideoSizesList()) {
                videoMapExpected.put(vSize.getSize().publicName(), vSize.getWidth() + "x" + vSize.getHeight());
            }
            for (WebElement videoActualRow : Select.multiSelector(videoSizesRow)) {
                sizeSelect = Select.selector(videoActualRow, relative_generalSelect);
                String size = Select.selector(videoActualRow, relative_sizesWidthInput).getAttribute("value") + "x" + Select.selector(videoActualRow, relative_sizesHeightInput).getAttribute("value");
                videoMapActual.put(Select.selector(sizeSelect, relative_selectLabel).getText(), size);
            }
            softAssert.assertEquals(videoMapActual, videoMapExpected, "Additional info page: Video sizes list is incorrect");
        }
        if (epData.getSeatMap() != null) {
            if (!epData.getSeatMap().isEmpty()) {
                Map<String, String> seatMapActual = new HashMap<>();
                for (WebElement seatActualRow : Select.multiSelector(seatRow)) {
                    seatMapActual.put(Select.selector(seatActualRow, relative_seatNameInput).getAttribute("value"), Select.selector(seatActualRow, relative_seatIdInput).getAttribute("value"));
                }
                softAssert.assertEquals(seatMapActual, epData.getSeatMap(), "Additional info page: Seats list is incorrect");
            } else {
                softAssert.assertTrue(AssertUtil.assertNotPresent(seatRow), "Additional info page: Seats list is not empty");
            }
        }
        softAssert.assertEquals(Select.selector(osSelect, relative_selectLabel).getText(), osString, "Additional info page: OS list is incorrect");
        softAssert.assertEquals(Select.selector(allowedSspSelect, relative_selectLabel).getText(), allowSspString, "Additional info page: Allowed SSP list is incorrect");
        softAssert.assertEquals(Select.selector(blockedSspSelect, relative_selectLabel).getText(), blockSspString, "Additional info page: Blocked SSP list is incorrect");
        softAssert.assertEquals(Select.selector(allowedInventorySelect, relative_selectLabel).getText(), allowInventoryString, "Additional info page: Allowed Inventory list is incorrect");
        softAssert.assertEquals(Select.selector(blockedInventorySelect, relative_selectLabel).getText(), blockInventoryString, "Additional info page: Blocked Inventory list is incorrect");
        assertApiSync(epData.getReportApiLinkPartner(), epData.getCsvColumnsMap());
    }

    @Step("Assert presence of Inventory Whitelist/Blacklist")
    public void assertInventoryPresent(boolean isPresent) {
        softAssert.assertEquals(AssertUtil.assertPresent(allowedInventorySelect, relative_selectLabel), isPresent, "Additional info page: Allowed Inventory list presence is incorrect");
        softAssert.assertEquals(AssertUtil.assertPresent(blockedInventorySelect, relative_selectLabel), isPresent, "Additional info page: Blocked Inventory list presence is incorrect");
    }

    //</editor-fold>

    //<editor-fold desc="Endpoint page - Scanners">
    private static final By
            scannersTab = By.xpath("//a[@data-toggle='#scanner']"),
            scannersPageContent = By.xpath("//div[@id='scanner']"),
            scannerSettingsRow = By.xpath("//div[@class='scanner-point']"),
            relative_scannerSettingsScannerError = By.xpath(".//div[@data-error]"),
            relative_scannerSettingsCheckbox = By.xpath(".//input[@type='checkbox']"),
            relative_scannerSettingsInput = By.xpath(".//input[@type='number']"),
            relative_scannerSettingsRadioButton = By.xpath(".//input[@type='radio']");

    @Step("Click on Scanners tab")
    public void clickScannersTab() {
        Select.selector(scannersTab).click();
        Wait.waitForVisibility(scannersPageContent);
    }

    @Step("Get scanner row")
    public WebElement getScannerRow(scannerTypesEnum scanner) {
        WebElement elementReturn = Select.selectParentByAttributeContains(scannerSettingsRow, relative_scannerSettingsCheckbox, "name", scanner.attributeName());
        hardAssert.assertNotNull(elementReturn, "There is no scanner [" + scanner.name() + "] on the page");
        return elementReturn;
    }

    @Step("Setup Scanner settings")
    public void setupScannerSection(List<ScannerSetting> scannersList) {
        for (ScannerSetting scanner : scannersList) {
            setScannerSetting(scanner);
        }
    }

    @Step("Set endpoint scanner settings")
    public void setScannerSetting(ScannerSetting scanner) {
        WebElement scannerRow = getScannerRow(scanner.getType());
        if (scanner.getType() == scannerTypesEnum.GEOEDGE) {
            clickToggle(scannerRow, relative_scannerSettingsCheckbox, scanner.getScannerStatus());
        } else {
            String valueStatus = scanner.getScannerStatus() ? "1" : "0";
            clickRadioButton(scannerRow, relative_scannerSettingsRadioButton, valueStatus);
            if (scanner.getScannerStatus()) {
                WebElement impInput = Select.selector(scannerRow, relative_scannerSettingsInput);
                if (scanner.getImpressionScanStatus() != Select.selector(scannerRow, relative_scannerSettingsCheckbox).isSelected()) {
                    clickRadioButton(scannerRow, relative_scannerSettingsCheckbox, "1");
                }
                if (scanner.getImpressionScanStatus()) {
                    enterInInput(impInput, scanner.getImpressionScanValue());
                }
            }
        }
    }

    @Step("Assert endpoint Scanners page")
    public void assertScannersTab(List<ScannerSetting> scannerList) {
        for (ScannerSetting scanner : scannerList) {
            assertScannerSettings(scanner);
        }
    }

    @Step("Assert scanner settings")
    private void assertScannerSettings(ScannerSetting scannerSettings) {
        String scannerStatusValue;
        WebElement scannerRow = getScannerRow(scannerSettings.getType());
        switch (scannerSettings.getType()) {
            case PROTECTED_POSTBID, PIXALATE_POSTBID, WHITEOPS_FRAUD_SENSOR -> {
                scannerStatusValue = scannerSettings.getScannerStatus() ? "1" : "0";
                softAssert.assertTrue(Select.selectByAttributeIgnoreCase(scannerRow, relative_scannerSettingsRadioButton, "value", scannerStatusValue).isSelected(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner status setting is incorrect");
                if (scannerSettings.getScannerStatus()) {
                    softAssert.assertEquals(Select.selector(scannerRow, relative_scannerSettingsCheckbox).isSelected(), scannerSettings.getImpressionScanStatus(), "Scanners tab: " + scannerSettings.getType().publicName() + " `% of impressions` status is incorrect");
                    softAssert.assertEquals(Select.selector(scannerRow, relative_scannerSettingsInput).getAttribute("value"), scannerSettings.getImpressionScanValue().toString(), "Scanners tab: " + scannerSettings.getType().publicName() + " `% of impressions` value is incorrect");
                }
            }
            case GEOEDGE -> softAssert.assertEquals(Select.selector(scannerRow, relative_scannerSettingsCheckbox).isSelected(), scannerSettings.getScannerStatus().booleanValue(), "Scanners tab: " + scannerSettings.getType().publicName() + " status setting is incorrect");
        }
    }

    @Step("Assert validation error in scanner")
    public void assertScannerError(ScannerSetting scanner, boolean isImpError, String errorText) {
        WebElement scannerRow = getScannerRow(scanner.getType());
        if (isImpError) {
            assertValidationError(softAssert, Select.selector(scannerRow, relative_scannerSettingsInput), errorText);
        } else {
            if (Select.selector(scannerRow, relative_scannerSettingsScannerError) != null) {
                softAssert.assertEquals(Select.selector(scannerRow, relative_scannerSettingsScannerError).getText(), errorText, "Scanners tab: " + scanner.getType().publicName() + " scanner error message is incorrect");
            } else {
                softAssert.fail("Scanner " + scanner.getType().publicName() + " error message is not displayed");
            }
        }
    }

    //</editor-fold>

    @Step("Assert presence of settings section in list")
    public void assertSectionPresent(endpointSettingsSectionsEnum section, boolean isPresent) {
        softAssert.assertEquals(AssertUtil.assertNotPresent(settingsSectionItem, "data-toggle", section.attributeName()), !isPresent, "Endpoint page: " + section + " section presence is incorrect");

    }
}
