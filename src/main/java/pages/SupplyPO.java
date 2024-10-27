package pages;

import com.opencsv.CSVReader;
import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.*;
import data.dataobject.EndpointCommonDO;
import data.dataobject.EndpointCommonDO.ScannerSetting;
import data.dataobject.EndpointSupplyDO;
import data.textstrings.messages.SupplyText;
import io.qameta.allure.Step;
import org.jooq.Condition;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static data.CommonEnums.*;
import static data.ScannersEnums.pixalatePrebidTypesEnum;
import static data.ScannersEnums.scannerTypesEnum;
import static data.SupplyEnum.*;

public class SupplyPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final FileUtility FileUtil = new FileUtility();
    private final BrowserUtility BrowserUtil = new BrowserUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final ParseAndFormatUtility ParseUtil = new ParseAndFormatUtility();
    private final RandomUtility RandomUtil = new RandomUtility();

    public SupplyPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public SupplyPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Supply dashboard page")
    public void gotoSupplySection() {
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(supplyLink).click();
        Wait.waitForClickable(createEndpointButton);
        Wait.attributeNotContains(endpointListTable, "class", "hide");  //SSP List is always displayed, because there is a default Native Supply
    }

    @Step("Get endpoint ID from the database")
    public Integer getEndpointId(String endpointName) {
        Condition condition = DSL.field("name").equal(endpointName);
        org.jooq.Record resultRecord = dbUtils.getDbDataRecordFirst("ssp_endpoint", condition);
        hardAssert.assertNotNull(resultRecord, "SSP Endpoint <" + endpointName + "> not found");
        return resultRecord.getValue("id", Integer.class);
    }

    //<editor-fold desc="SSP List">
    static final By
            exportCSVButton = By.xpath("//a[@id='exportSspList']"),
            createEndpointButton = By.xpath("//a[contains(@href, 'ssp/create')]"),
            endpointListTable = By.xpath("//div[@id='sspDashboardList']//table"),
            endpointListColumnHeader = By.xpath("//div[@id='sspDashboardList']//thead//th"),
            endpointListColumnTotal = By.xpath("//div[@id='sspDashboardList']//tfoot//th"),
            endpointListRow = By.xpath("//div[@id='sspDashboardList']//tbody/tr"),
            endpointListId = By.xpath(".//td[@data-field='id']"),
            endpointListNameLink = By.xpath(".//td[@data-field='name']"),
            endpointListRegion = By.xpath(".//td[@data-field='region_name']"),
            endpointListStatusToggle = By.xpath(".//td[@data-field='status']//input"),
            endpointListType = By.xpath(".//td[@data-field='type']"),
            endpointListQPS = By.xpath(".//td[@data-field='qps']"),
            endpointListQPSBid = By.xpath(".//td[@data-field='bid_qps']"),
            endpointListRevenueYesterday = By.xpath(".//td[@data-field='spend_today_yesterday']"),
            endpointListRevenueToday = By.xpath(".//td[@data-field='spend_today']"),
            endpointListWinRate = By.xpath(".//td[@data-field='win_rate']"),
            endpointListCompany = By.xpath(".//td[@data-field='company_id']"),
            endpointListAllowedDSP = By.xpath(".//td[@data-field='allowed_dsp_general']"),
            endpointListSpendLimit = By.xpath(".//td[@data-field='spend_limit']"),
            endpointListCloneButton = By.xpath(".//td[@data-field='action_id']/a[@class='clone-endpoint']"),
            endpointListAdsTxtButton = By.xpath(".//td[@data-field='action_id']/a[@class='button-users']"),
            endpointListEmptyPlaceholder = By.xpath("//div[@id='sspDashboardList']//div[contains(@class, 'no-data')]/h4");

    @Step("Open endpoint creation page")
    public void clickCreateEndpoint() {
        Select.selector(createEndpointButton).click();
        Wait.waitForVisibility(nameInput);
        softAssert.assertTrue(Select.selectByAttributeIgnoreCase(endpointTypeRadioButton, "value", "rtb").isSelected(), "RTB type is not selected by default");
    }

    @Step("Click on Export CSV button")
    public File clickExportCsv() {
        Select.selector(exportCSVButton).click();
        Wait.waitForTabClose();
        return FileUtil.getDownloadedFile("export-ssp.csv", 15);
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
            String id = idElement.getText();
            if (!id.equals("-1")) {
                idList.add(Integer.valueOf(id));
            }
        }
        return (Integer) RandomUtil.getRandomElement(idList);
    }

    @Step("Get random endpoint row info")
    public EndpointSupplyDO getRandomEndpointRowInfo() {
        EndpointSupplyDO retDO = new EndpointSupplyDO();
        WebElement targetRow = getEndpointRow(getRandomEndpointIdFromList());
        endpointTypeEnum type = null;
        for (endpointTypeEnum eType : endpointTypeEnum.values()) {
            if (Select.selector(targetRow, endpointListType).getText().equalsIgnoreCase(eType.publicName())) {
                type = eType;
                break;
            }
        }
        retDO.setId(Integer.valueOf(Select.selector(targetRow, endpointListId).getText()))
                .setEndpointName(Select.selector(targetRow, endpointListNameLink).getText())
                .setRegion(regionsEnum.valueOf(Select.selector(targetRow, endpointListRegion).getText().toUpperCase()))
                .setStatus(Select.selector(targetRow, endpointListStatusToggle).isSelected())
                .setEndpointType(type)
                .setSpendLimit((int) Double.parseDouble(Select.selector(targetRow, endpointListSpendLimit).getText().replace("$", "").replaceAll(",", "")))
                .setCompany(Select.selector(targetRow, endpointListCompany).getText())
                .setTableStats(Integer.valueOf(Select.selector(targetRow, endpointListQPS).getText().replaceAll(",", "")),
                        Integer.valueOf(Select.selector(targetRow, endpointListQPSBid).getText().replaceAll(",", "")),
                        Double.parseDouble(Select.selector(targetRow, endpointListRevenueYesterday).getText().replace("$", "").replaceAll(",", "")),
                        Double.parseDouble(Select.selector(targetRow, endpointListRevenueToday).getText().replace("$", "").replaceAll(",", "")),
                        Double.parseDouble(Select.selector(targetRow, endpointListWinRate).getText().replace("%", "")));
        return retDO;
    }

    @Step("Open endpoint edit page")
    public void openEndpointEditPage(Integer endpointId) {
        WebElement endpoint = getEndpointRow(endpointId);
        Select.selectByAttributeIgnoreCase(endpoint, relative_generalTableBodyCell, "data-field", sspTableColumnsEnum.NAME.attributeName()).click();
        Wait.waitForVisibility(nameInput);
    }

    @Step("Click on column to sort")
    public void clickColumnSorting(sspTableColumnsEnum columnName) {
        Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", columnName.attributeName()).click();
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
    }

    @Step("Click Clone button")
    public void clickCloneEndpoint(Integer endpointId, boolean cloneConfirm) {
        WebElement endpointRow = getEndpointRow(endpointId);
        String endpointName = Select.selectByAttributeIgnoreCase(endpointRow, relative_generalTableBodyCell, "data-field", sspTableColumnsEnum.NAME.attributeName()).getText();
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

    @Step("Click Download Ads.txt button")
    public File clickDownloadAdsTxt(int endpointId) {
        File retFile;
        WebElement endpointRow = getEndpointRow(endpointId);
        Select.selector(endpointRow, endpointListAdsTxtButton).click();
        retFile = FileUtil.getDownloadedFile("ads.txt", 15);
        return retFile;
    }

    @Step("Assert Ads.txt required lines")
    public void assertAdsTxtRequired(File file, String sellerId, directnessTypeEnum directness) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < 3; i++) {
                String line = br.readLine();
                if (i == 0) {
                    softAssert.assertEquals(line.strip(), "#Ads.txt", "Ads.txt: first line is incorrect");
                } else if (i == 2) {
                    softAssert.assertEquals(line.strip(), getProjectUrl().getHost() + ", " + sellerId + ", " + directness.publicName(), "Ads.txt: default line is incorrect");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Assert Ads.txt custom domain")
    public void assertAdsTxtDomain(File file, String domain) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            for (int i = 0; i < 3; i++) {
                String line = br.readLine();
                if (i == 2) {
                    String[] parts = line.split(",\\s*");
                    softAssert.assertEquals(parts[0], domain, "Custom domain is not correct");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Step("Assert Endpoint data in the list")
    public void assertEndpointRowInfo(EndpointSupplyDO epData) {
        WebElement endpointItem = getEndpointRow(epData.getId());
        softAssert.assertEquals(Select.selector(endpointItem, endpointListNameLink).getText(), epData.getEndpointName(), "Table row: wrong endpoint Name");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListRegion).getText(), epData.getRegion().publicName(), "Table row: wrong Region");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListStatusToggle).isSelected(), epData.getStatus(), "Table row: wrong status");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListType).getText(), epData.getEndpointType().publicName(), "Table row: wrong endpoint Type");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListQPS).getText(), ParseUtil.formatBigNoDecimal(epData.getQpsReal()), "Table row: wrong QPS count");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListQPSBid).getText(), ParseUtil.formatBigNoDecimal(epData.getQpsBid()), "Table row: wrong Bid QPS");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListRevenueYesterday).getText(), "$" + ParseUtil.formatBigCommaWithDecimal(epData.getRevenueYesterday()), "Table row: wrong Yesterday's Revenue");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListRevenueToday).getText(), "$" + ParseUtil.formatBigCommaWithDecimal(epData.getRevenueToday()), "Table row: wrong Today's Spend");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListWinRate).getText(), ParseUtil.formatBigCommaWithDecimal(epData.getWinRate()) + "%", "Table row: wrong Win rate");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListCompany).getText(), epData.getCompany(), "Table row: wrong Company");
        softAssert.assertEquals(Integer.valueOf(Select.selector(endpointItem, endpointListAllowedDSP).getText()).intValue(), epData.getDspAllowedMap().size(), "Table row: wrong Allowed DSP count");
        softAssert.assertEquals(Select.selector(endpointItem, endpointListSpendLimit).getText(), "$" + ParseUtil.formatBigCommaWithDecimal(epData.getSpendLimit()), "Table row: wrong Spend Limit");
        softAssert.assertTrue(AssertUtil.assertPresent(endpointItem, endpointListCloneButton), "Table row: There is no Clone button");
        softAssert.assertTrue(AssertUtil.assertPresent(endpointItem, endpointListAdsTxtButton), "Table row: There is no Ads.txt button");
    }

    @Step("Assert companies on Dashboard list")
    public void assertAllEndpointRowsCompanies(Map<Integer, String> sspCompanyMapExpected) {
        Map<Integer, String> sspCompanyMapActual = new HashMap<>();
        softAssert.assertTrue(AssertUtil.assertPresent(endpointListRow), "Filtered table by company is wrong. There are no endpoints");
        for (WebElement endpointRow : Select.multiSelector(endpointListRow)) {
            sspCompanyMapActual.put(Integer.valueOf(Select.selector(endpointRow, endpointListId).getText()), Select.selector(endpointRow, endpointListCompany).getText());
        }
        softAssert.assertTrue(sspCompanyMapExpected.entrySet().containsAll(sspCompanyMapActual.entrySet()), "Filtered table by company is wrong. Expected companies from the page\n" + sspCompanyMapActual + "\n to be in the list from the database \n" + sspCompanyMapExpected);
    }

    @Step("Assert endpoints names on Dashboard list")
    public void assertAllEndpointRowsNames(String nameToContain) {
        for (WebElement sspRowName : Select.multiSelector(endpointListRow, endpointListNameLink)) {
            softAssert.assertTrue(sspRowName.getText().toLowerCase().contains(nameToContain), "Endpoint name <" + sspRowName.getText() + "> does not contain the symbols <" + nameToContain + ">");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in endpoints name search:");
        }
    }

    @Step("Assert endpoints names on Dashboard list")
    public void assertAllEndpointRowsIds(String idToContain) {
        for (WebElement sspRowId : Select.multiSelector(endpointListRow)) {
            boolean isName = Select.selector(sspRowId, endpointListNameLink).getText().contains(idToContain),
                    isId = Select.selector(sspRowId, endpointListId).getText().contains(idToContain);
            softAssert.assertTrue(isName | isId, "Endpoint ID <" + sspRowId.getText() + "> does not contain the ID <" + idToContain + ">");
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in endpoints ID search:");
        }
    }

    @Step("Assert Dashboard list is empty")
    public void assertDashboardEmpty() {
        softAssert.assertEquals(Select.selector(endpointListEmptyPlaceholder).getText(), SupplyText.EMPTY_DASHBOARD, "Empty search text is incorrect");
    }

    @Step("Assert exported CSV file")
    public void assertFileExportCsv(File exportFile) {
        System.out.println("SSP Export File: " + exportFile.getAbsolutePath());
        int rowCnt = 1;
        String[] columnsHeadersExpected = new String[13];
        String[] rowCellsExpected = new String[13];
        try {
            CSVReader reader = new CSVReader(new FileReader(exportFile));
            List<String[]> parsedCsv = reader.readAll();
            List<WebElement> columnHeaders = Select.multiSelector(endpointListColumnHeader);
            for (int i = 0; i < columnHeaders.size() - 1; i++) {
                columnsHeadersExpected[i] = columnHeaders.get(i).getText().replace("’", "'").replace("name", "Name");
            }
            softAssert.assertEquals(parsedCsv.get(0), columnsHeadersExpected, "Column names are different");
            for (WebElement tableRow : Select.multiSelector(endpointListRow)) {
                List<WebElement> rowCells = Select.multiSelector(tableRow, relative_generalTableBodyCell);
                for (int k = 0; k < rowCells.size() - 1; k++) {
                    switch (k) {
                        //ID, SSP, Region, Type, QPS, Bid QPS, Company name, Allowed DSPs
                        case 0, 1, 2, 4, 5, 6, 10, 11 -> rowCellsExpected[k] = rowCells.get(k).getText();
                        //Active
                        case 3 ->
                                rowCellsExpected[k] = Select.selector(tableRow, endpointListStatusToggle).isSelected() ? "1" : "0";
                        //Yesterday's Revenue, Today's Revenue, Win Rate
                        case 7, 8, 9 ->
                                rowCellsExpected[k] = rowCells.get(k).getText().replace("$", "").replace("%", "").replaceAll(",", "");
                        //Spend Limit
                        case 12 ->
                                rowCellsExpected[k] = rowCells.get(k).getText().replace("$", "").replaceAll(",", "").replace(".00", "");
                    }
                }
                softAssert.assertEquals(parsedCsv.get(rowCnt), rowCellsExpected, "Error on list item #[" + rowCnt + "]");
                rowCnt++;
            }
            int totalCnt = getPagingEntriesCount().get("total");
            softAssert.assertEquals(parsedCsv.size() - 1, totalCnt, "Error with number of entries");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors on file assertion:");
        }
    }

    @Step("Assert table sorting by column")
    public void assertTableSorting(sspTableColumnsEnum columnName, boolean isAscending) {
        By columnCell = null;
        switch (columnName) {
            case ID -> columnCell = endpointListId;
            case REGION -> columnCell = endpointListRegion;
            case TYPE -> columnCell = endpointListType;
            case QPS -> columnCell = endpointListQPS;
            case BID_QPS -> columnCell = endpointListQPSBid;
            case SPEND_YESTERDAY -> columnCell = endpointListRevenueYesterday;
            case SPEND_TODAY -> columnCell = endpointListRevenueToday;
            case WIN_RATE -> columnCell = endpointListWinRate;
            case COMPANY_ID -> columnCell = endpointListCompany;
            case ALLOWED_DSP -> columnCell = endpointListAllowedDSP;
            case SPEND_LIMIT -> columnCell = endpointListSpendLimit;
        }
        WebElement column = Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", columnName.attributeName());
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", sspTableColumnsEnum.STATUS.attributeName()), tableColumnHeaderSortIcon), "Sorting icon is present in column Active");
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByAttributeIgnoreCase(endpointListColumnHeader, "data-field", sspTableColumnsEnum.ACTIONS.attributeName()), tableColumnHeaderSortIcon), "Sorting icon is present in column Action");
        switch (columnName) {
            case ID, NAME, REGION, TYPE, COMPANY_ID, ALLOWED_DSP ->
                    softAssert.assertEquals(Select.selector(column, tableColumnHeaderSortIcon).getAttribute("class").contains("amount-asc"), isAscending, "Sorting icon of [" + columnName + "] is incorrect");
            case SPEND_YESTERDAY, SPEND_TODAY, WIN_RATE, SPEND_LIMIT ->
                    softAssert.assertEquals(Select.selector(column, tableColumnHeaderSortIcon).getAttribute("class").contains("numeric-asc"), isAscending, "Sorting icon of [" + columnName + "] is incorrect");
        }
        switch (columnName) {
            case ID, QPS, BID_QPS, ALLOWED_DSP -> {
                List<Integer> columnItemsActual = new ArrayList<>();
                for (WebElement cell : Select.multiSelector(columnCell)) {
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
            case REGION, TYPE -> {
                List<String> columnItemsActual = new ArrayList<>();
                for (WebElement cell : Select.multiSelector(columnCell)) {
                    columnItemsActual.add(cell.getText());
                }
                List<String> columnItemsSorted = new ArrayList<>(columnItemsActual);
                if (isAscending) {
                    Collections.sort(columnItemsSorted);
                } else {
                    Collections.sort(columnItemsSorted);
                    Collections.reverse(columnItemsSorted);
                }
                softAssert.assertEquals(columnItemsActual, columnItemsSorted, "Column [" + columnName + "] sorting is incorrect. isAscending = " + isAscending);
            }
            case SPEND_YESTERDAY, SPEND_TODAY, WIN_RATE, SPEND_LIMIT -> {
                List<Double> columnItemsActual = new ArrayList<>();
                for (WebElement cell : Select.multiSelector(columnCell)) {
                    columnItemsActual.add(Double.parseDouble(cell.getText().replace("$", "").replace("%", "").replaceAll(",", "")));
                }
                List<Double> columnItemsSorted = new ArrayList<>(columnItemsActual);
                if (isAscending) {
                    Collections.sort(columnItemsSorted);
                } else {
                    Collections.sort(columnItemsSorted);
                    Collections.reverse(columnItemsSorted);
                }
                softAssert.assertEquals(columnItemsActual, columnItemsSorted, "Column [" + columnName + "] sorting is incorrect. isAscending = " + isAscending);
            }
            case COMPANY_ID -> {    //TODO company sort works by ID, not by name. Since the ID is nowhere to find in html code, we have to resort to this kind of tricks
                List<String> companiesColumnActualList = new ArrayList<>(), companiesExpectedDbList;
                Map<Integer, String> companiesExpectedFilteredMap = new HashMap<>();
                Stream<Entry<Integer, String>> sorted;
                for (WebElement cell : Select.multiSelector(columnCell)) {
                    companiesColumnActualList.add(cell.getText());  //Collect companies from page into List. It is Actual result
                }
                for (org.jooq.Record item : dbUtils.getDbDataResult("companies", null)) {
                    //Filter out companies, that are not present on the page
                    if (companiesColumnActualList.contains(item.getValue("name", String.class))) {
                        companiesExpectedFilteredMap.put(item.getValue("id", Integer.class), item.getValue("name", String.class));
                    }
                }
                if (isAscending) {
                    sorted = companiesExpectedFilteredMap.entrySet().stream().sorted(Entry.comparingByKey()); //Sort DB values by ID ascending
                } else {
                    sorted = companiesExpectedFilteredMap.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByKey()));   //Sort DB values by ID descending
                }
                companiesExpectedDbList = new ArrayList<>(sorted.map(Entry::getValue).toList());    //Get sorted expected company names in the list
                LinkedHashSet<String> columnItemsUniqueSet = new LinkedHashSet<>(companiesColumnActualList);   //Keep only unique values from Actual companies list, so they will correspond to Expected list
                softAssert.assertEquals(columnItemsUniqueSet, companiesExpectedDbList, "Column [" + columnName + "] sorting is incorrect. isAscending = " + isAscending);
            }
        }
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in dashboard sorting");
        }
    }

    //</editor-fold>

    //<editor-fold desc="SSP list filters">
    static final By filterStatusSelect = By.xpath("//select[@name='status_param']"),
            filterTypeSelect = By.xpath("//select[@name='type_endpoint']"),
            filterRegionsSelect = By.xpath("//select[@name='regions']"),
            filterCompaniesSelect = By.xpath("//select[@name='companies']"),
            filterSearchInput = By.xpath("//input[@id='searchSsp']");

    @Step("Get SSP Companies")
    public Map<Integer, String> getSspCompanies(String companyName, Boolean endpointStatus) {
        Map<Integer, String> outMap = new HashMap<>();
        String[] joinFieldsSelect = {"ssp_endpoint.id", "companies.name"};
        Condition conditionJoin = DSL.field("ssp_endpoint.company_id").equal(DSL.field("companies.id")), conditionWhere = null;
        if (companyName != null) {
            conditionWhere = DSL.field("companies.name").equal(companyName);
        }
        if (conditionWhere != null) {
            conditionWhere = conditionWhere.and(DSL.field("ssp_endpoint.status").in(endpointStatus != null ? (endpointStatus ? 1 : 0) : 0, 1));
        } else {
            conditionWhere = DSL.field("ssp_endpoint.status").in(endpointStatus != null ? (endpointStatus ? 1 : 0) : 0, 1);
        }
        Result<org.jooq.Record> dbJoinResult = dbUtils.getDbDataResultInnerJoin(joinFieldsSelect, "ssp_endpoint", "companies", conditionJoin, conditionWhere);
        for (org.jooq.Record record : dbJoinResult) {
            outMap.put(record.get("ssp_endpoint.id", Integer.class), record.get("companies.name", String.class));
        }
        return outMap;
    }

    @Step("Get endpoints names list from the database")
    public List<String> getEndpointNamesList(Boolean endpointStatus) {
        Condition condition = DSL.field("status").in(endpointStatus != null ? (endpointStatus ? 1 : 0) : 0, 1);
        Result<org.jooq.Record> result = dbUtils.getDbDataResult("ssp_endpoint", condition);
        return new ArrayList<>(result.getValues("name", String.class));
    }

    @Step("Get endpoints IDs list from the database")
    public List<Integer> getEndpointIdsList(Boolean endpointStatus) {
        Condition condition = DSL.field("status").in(endpointStatus != null ? (endpointStatus ? 1 : 0) : 0, 1);
        Result<org.jooq.Record> result = dbUtils.getDbDataResult("ssp_endpoint", condition);
        return new ArrayList<>(result.getValues("id", Integer.class));
    }

    @Step("Select filter by Status")
    public void filterSelectStatus(Boolean status) {
        if (status != null) {
            selectSingleSelectDropdownOptionByName(filterStatusSelect, status ? "Active" : "Not Active");
        } else {
            selectSingleSelectDropdownOptionByName(filterStatusSelect, "All");
        }
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
    }

    @Step("Select filter by Region")
    public void filterSelectRegion(regionsEnum region) {
        Select.selector(filterRegionsSelect, relative_selectLabel).click();
        clickDropdownMultiselectOption(filterRegionsSelect, region.toString(), true);
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
        Select.selector(filterRegionsSelect, relative_selectLabel).click();
    }

    @Step("Select filter by Type")
    public void selectFilterType(endpointTypeEnum type) {
        selectSingleSelectDropdownOptionByName(filterTypeSelect, type.publicName());
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
    }

    @Step("Companies Filter - click to open")
    public void openFilterCompanies() {
        Select.selector(filterCompaniesSelect, relative_selectLabel).click();
        Wait.waitForVisibility(filterCompaniesSelect, relative_selectOptionWithTitle);
    }

    @Step("Get companies names from list")
    public List<String> getFilterCompanyNames() {
        List<String> returnList = new ArrayList<>();
        for (WebElement companyName : Select.multiSelector(filterCompaniesSelect, relative_selectOptionWithTitle)) {
            returnList.add(companyName.getAttribute("title"));
        }
        return returnList;
    }

    @Step("Companies Filter - input name in search")
    public void filterCompaniesInputName(String searchRequest) {
        Select.selector(filterCompaniesSelect, relative_selectInput).sendKeys(searchRequest);
    }

    @Step("Companies Filter - select company")
    public void filterCompaniesSelect(String companyName, boolean toSelect) {
        clickDropdownMultiselectOption(filterCompaniesSelect, companyName, toSelect);
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
        Wait.attributeContains(preloaderTable, "class", "hide");
    }

    @Step("Companies Filter - select random company")
    public String filterCompaniesSelectRandom() {
        String companyName = (String) RandomUtil.getRandomElement(getFilterCompanyNames());
        Select.selector(filterCompaniesSelect, relative_selectLabel).click();
        clickDropdownMultiselectOption(filterCompaniesSelect, companyName, true);
        Wait.attributeNotContains(endpointListTable, "class", "disabled");
        return companyName;
    }

    @Step("Input symbols in Name/Id search line")
    public void searchNameIdInput(String searchRequest) {
        searchItemInTable(searchRequest, filterSearchInput, endpointListTable);
    }

    //</editor-fold>

    //<editor-fold desc="Endpoint page - Information">
    static final By
            endpointTitleHeader = By.xpath("//h1[@class='title']"),
            informationTab = By.xpath("//a[@data-toggle='#information']"),
            nameInput = By.xpath("//input[@id='name']"),
            statusToggle = By.xpath("//input[@id='status']"),
            testModeToggle = By.xpath("//input[@id='test_mode']"),
            companySelect = By.xpath("//select[@id='companyID']"),
            endpointTypeRadioButton = By.xpath("//input[@name='type']"),
            companySavedLine = By.xpath("//div[@id='information']//div[@data-id='company']//div[2]"),
            regionSavedLine = By.xpath("//div[@id='information']//div[@data-id='region']//div[2]"),
            endpointRtbUrlSavedLine = By.xpath("//div[@id='information']//div[@data-id='rtb-url']//div[2]"),
            endpointTypeSavedLine = By.xpath("//div[@id='information']//div[@data-id='endpoint-type']//a"),
            endpointVastTypeRadioButton = By.xpath("//input[@data-type='vast']"),
            endpointJsTypeRadioButton = By.xpath("//input[@data-type='js']"),
            endpointVastJsUrlLine = By.xpath("//div[@data-id='vast-js-url']//span[@id='print_link']"),
            endpointVastJsUrlCopyButton = By.xpath("//div[@data-id='vast-js-url']//span[@class='copy-link']"),
            addMacrosSelect = By.xpath("//select[contains(@name,'key_tag')]"),
            addMacrosInput = By.xpath("//input[@name='macros_vast_tag']"),
            addMacrosButton = By.xpath("//a[contains(@class, 'check-tag')]"),
            macrosRow = By.xpath("//div[@id='listTagsMacros']/div[@data-key-row]"),
            macrosNameLine = By.xpath("./div[1]/input"),
            macrosValueLine = By.xpath("./div[2]/input"),
            macrosDeleteButton = By.xpath(".//a[@data-key]"),
            macrosListButton = By.xpath("//div[@id='btnMacrolist']"),
            jsTagSizeSelect = By.xpath("//select[@id='iabsize']"),
            jsTagSizeWidthInput = By.xpath("//input[@id='width']"),
            jsTagSizeHeightInput = By.xpath("//input[@id='height']"),
            jsTagBackfillInput = By.xpath("//textarea[@id='backFillJsTag']");

    @Step("Click on Information tab")
    public void clickInformationTab() {
        Select.selector(informationTab).click();
        Wait.waitForClickable(nameInput);
    }

    @Step("Setup Information tab")
    public void setupInformationTab(EndpointSupplyDO epData) {
        toggleEndpointStatus(epData.getStatus());
        toggleTestMode(epData.getTestMode());
        inputName(epData.getEndpointName());
        selectCompany(epData.getCompany());
        selectEndpointType(epData.getEndpointType());
        if (epData.getEndpointType() == endpointTypeEnum.VAST) {
            selectVastSubType(epData.getVastTagType());
        }
        if (epData.getEndpointType() == endpointTypeEnum.JS) {
            selectJsTagSubType(epData.getJsTagType());
            setJsTagSize(epData.getJsTagSize(), epData.getJsWidth(), epData.getJsHeight());
            inputJsTagBackfill(epData.getJsBackfill());
        }
    }

    @Step("Input endpoint name")
    public void inputName(String name) {
        enterInInput(nameInput, name);
    }

    @Step("Select company")
    public void selectCompany(String companyName) {
        selectSingleSelectDropdownOption(companySelect, companyName);
    }

    @Step("Get random company")
    public String getRandomCompanyName(Boolean isActive) {
        List<String> companiesNames = new ArrayList<>();
        for (WebElement option : Select.multiSelector(companySelect, relative_selectOptionWithTitle)) {
            companiesNames.add(option.getAttribute("title"));
        }
        if (isActive != null) {
            if (isActive) {
                companiesNames.removeIf(c -> c.endsWith("(inactive)"));
            } else {
                companiesNames.removeIf(c -> !c.endsWith("(inactive)"));
            }
        }
        return (String) RandomUtil.getRandomElement(companiesNames);
    }

    @Step("Change ON/OFF status")
    public void toggleEndpointStatus(boolean status) {
        clickToggle(statusToggle, status);
    }

    @Step("Change Test mode status")
    public void toggleTestMode(boolean testStatus) {
        clickToggle(testModeToggle, testStatus);
    }

    @Step("Select endpoint type")
    public void selectEndpointType(endpointTypeEnum type) {
        clickRadioButton(endpointTypeRadioButton, type.attributeName());
    }

    @Step("Select VAST subtype")
    public void selectVastSubType(vastTypeEnum type) {
        clickRadioButton(endpointVastTypeRadioButton, type.attributeName());
    }

    @Step("Select JS subtype")
    public void selectJsTagSubType(jsTagTypeEnum type) {
        clickRadioButton(endpointJsTypeRadioButton, type.attributeName());
    }

    @Step("Select JS Tag size")
    public void setJsTagSize(iabSizesEnum size, Integer width, Integer height) {
        selectSingleSelectDropdownOption(jsTagSizeSelect, size.publicName());
        enterInInput(jsTagSizeWidthInput, width);
        enterInInput(jsTagSizeHeightInput, height);
    }

    @Step("Fill in backfill")
    public void inputJsTagBackfill(String backfill) {
        enterInInput(jsTagBackfillInput, backfill);
    }

    @Step("Assert endpoint name on info page")
    public void assertEndpointName(String name) {
        softAssert.assertEquals(Select.selector(nameInput).getAttribute("value"), name, "Info page: Endpoint name is incorrect");
    }

    @Step("Assert endpoint ID on edit page")
    public void assertEndpointId(Integer endpointId) {
        String title = Select.selector(endpointTitleHeader).getText(), endpointIdActual = title.substring(title.indexOf("#") + 1, title.indexOf("|")).trim();
        softAssert.assertEquals(endpointIdActual, endpointId.toString(), "Endpoint ID in header is incorrect");
    }

    @Step("Assert endpoint Information page")
    public void assertInformationTab(EndpointSupplyDO epData) {
        assertEndpointId(epData.getId());
        assertEndpointName(epData.getEndpointName());
        softAssert.assertEquals(Select.selector(statusToggle).isSelected(), epData.getStatus(), "Info page: Endpoint status is incorrect");
        softAssert.assertEquals(Select.selector(testModeToggle).isSelected(), epData.getTestMode(), "Info page: Endpoint test mode status is incorrect");
        softAssert.assertEquals(Select.selector(endpointTypeSavedLine).getText(), epData.getEndpointType().publicName(), "Info page: Endpoint type is incorrect");
        softAssert.assertEquals(Select.selector(companySavedLine).getText(), epData.getCompany(), "Info page: Company name is incorrect");
        softAssert.assertEquals(Select.selector(regionSavedLine).getText(), epData.getRegion().publicName(), "Info page: Region is incorrect");
        switch (epData.getEndpointType()) {
            case RTB -> {
                String endpointUrlActual = Select.selector(endpointRtbUrlSavedLine).getText(), endpointUrlExpected = "http://" + getRegionAddress(epData.getRegion()) + "/?c=rtb&m=req&key=" + epData.getId();
                softAssert.assertEquals(endpointUrlActual, endpointUrlExpected, "Info page: Endpoint URL is incorrect.");
            }
            case VAST -> {
                vastTypeEnum endpointSubtype = epData.getVastTagType();
                Map<String, String> macrosMapActual = new HashMap<>(), macrosMapExpected = new HashMap<>();
                String endpointUrlActual = Select.selector(endpointVastJsUrlLine).getText(),
                        endpointUrlExpected = "https:\\/\\/" + getRegionAddress(epData.getRegion()) + "\\/\\?c=rtb&m=vast&key=[a-z0-9]{32}", tagsCollection = "";
                switch (endpointSubtype) {
                    case LKQD ->
                            tagsCollection = "&dnt=$$dnt$$&domain=$$domain$$&h=$$height$$&w=$$width$$&ip=$$ip$$&lat=$$loclat$$&lon=$$loclong$$&page=$$pageurl$$&ua=$$useragent$$&bundle=$$bundleid$$&appname=$$appname$$&ifa=$$userid$$&storeurl=$$appstoreurl$$&cat=$$categoryiab$$&bidfloor=$$cpm$$&gdpr=$$gdpr$$&consent=$$gdprcs$$&placementid=$$creativeid$$&playbackmethod=$$playbackmethod$$";
                    case JW_PLAYER ->
                            tagsCollection = "&domain=__domain__&page=__page-url__&h=__player-height__&w=__player-width__";
                    case SPRINGSERVE ->
                            tagsCollection = "&bundle={{APP_BUNDLE}}&appname={{APP_NAME}}&storeurl={{APP_STORE_URL}}&ifa={{DEVICE_ID}}&domain={{DOMAIN}}&page={{ENCODED_URL}}&ip={{IP}}&ua={{USER_AGENT}}&dnt={{DNT}}&gdpr={{GDPR}}&consent={{CONSENT}}&cat={{IAB_CATEGORY}}&lat={{LAT}}&lon={{LON}}&w={{WIDTH}}&h={{HEIGHT}}&mindur={{MINIMUM_DURATION}}&maxdur={{MAXIMUM_DURATION}}&coppa={{COPPA}}&placementid={{SUPPLY_TAG_ID}}&playbackmethod={{PLAYBACKMETHOD}}&pod_max_dur={{POD_MAX_DUR}}&pod_ad_slots={{POD_AD_SLOTS}}&cb={{CACHEBUSTER}}&content_id={{CONTENT_ID}}&content_episode={{CONTENT_EPISODE}}&content_title={{CONTENT_TITLE}}&content_series={{CONTENT_SERIES}}&content_season={{CONTENT_SEASON}}&channel_name={{CHANNEL_NAME}}&lmt={{LMT}}&device_make={{DEVICE_MAKE}}&device_model={{DEVICE_MODEL}}&ifa_type={{IFA_TYPE}}&content_livestream={{CONTENT_LIVESTREAM}}&content_genre={{CONTENT_GENRE}}&content_rating={{RATING}}&language={{LANGUAGE}}&content_keywords={{KEYWORDS}}&schain={{SCHAIN}}&network_name={{NETWORK_NAME}}";
                    case CUSTOM, CUSTOM_INAPP, CUSTOM_CTV -> {
                        for (macroTagsTypes tag : epData.getJsVastTagsList()) {
                            tagsCollection += "&" + tag.attributeName() + "=" + getMacroTagPlaceholder(tag, endpointSubtype, null);
                            macrosMapExpected.put(tag.attributeName(), getMacroTagPlaceholder(tag, endpointSubtype, null));
                        }
                        for (WebElement tagRow : Select.multiSelector(macrosRow)) {
                            macrosMapActual.put(Select.selector(tagRow, macrosNameLine).getAttribute("value"), Select.selector(tagRow, macrosValueLine).getAttribute("value"));
                        }
                    }
                }
                tagsCollection = tagsCollection.replace("[", "\\[").replace("]", "\\]").replace("$", "\\$").replace("{", "\\{").replace("}", "\\}").replace(".", "\\.");
                /*Assertions*/
                softAssert.assertTrue(Select.selectByAttributeIgnoreCase(endpointVastTypeRadioButton, "value", endpointSubtype.attributeName()).isSelected(), "Info page: correct VAST type is not selected");
                softAssert.assertTrue(endpointUrlActual.matches(endpointUrlExpected + tagsCollection), "Info page: Endpoint URL is incorrect. Expected: <" + endpointUrlExpected + tagsCollection + ">. Actual: <" + endpointUrlActual + ">");
                if (endpointSubtype == vastTypeEnum.CUSTOM || endpointSubtype == vastTypeEnum.CUSTOM_INAPP || endpointSubtype == vastTypeEnum.CUSTOM_CTV) {
                    softAssert.assertEquals(macrosMapActual, macrosMapExpected, "Info page: Macros tag list is incorrect");
                }
            }
            case JS -> {
                /*Data prepare*/
                jsTagTypeEnum endpointSubtype = epData.getJsTagType();
                regionsEnum region = epData.getRegion();
                Map<String, String> macrosMapActual = new HashMap<>(), macrosMapExpected = new HashMap<>();
                String urlActual = Select.selector(endpointVastJsUrlLine).getText(), endpointUrlExpected = "NO URL", widthExpected, heightExpected, tagsCollection = "";
                iabSizesEnum size = epData.getJsTagSize();
                widthExpected = epData.getJsWidth().toString();
                heightExpected = epData.getJsHeight().toString();
                switch (endpointSubtype) {
                    case DIRECT ->
                            endpointUrlExpected = "<script async type=\"text\\/javascript\" src=\"https:\\/\\/" + getRegionAddress(region) + "\\/\\?c=rtb&m=direct&key=[a-z0-9]{32}\"><\\/script>";
                    case DFP ->
                            endpointUrlExpected = "<script type=\"text\\/javascript\" src=\"https:\\/\\/" + getRegionAddress(region) + "\\/\\?c=rtb&m=js&type=dfp&key=[a-z0-9]{32}&domain=%%SITE%%&page=%%PATTERN:url%%&clickurl=%%CLICK_URL_ESC%%&w=%%WIDTH%%&h=%%HEIGHT%%\"><\\/script>";
                    case APPLOVIN ->
                            endpointUrlExpected = "<script src=\"https:\\/\\/" + getRegionAddress(region) + "\\/\\?c=rtb&m=js&type=applovin&key=[a-z0-9]{32}&sizes=%%SIZES%%&bundle=%%BUNDLE%%&ifa=%%ADVERTISING_ID_IFA%%&ifv=%%ADVERTISING_ID_IFV%%&lat=%%LATITUDE%%&lon=%%LONGITUDE%%&lltype=2&dnt=%%DNT%%&cb=%%CACHEBUSTER%%\"></script>";
                    case S2S_APP, S2S_WEB -> {
                        for (macroTagsTypes tag : epData.getJsVastTagsList()) {
                            tagsCollection += "&" + tag.attributeName() + "=" + getMacroTagPlaceholder(tag, null, endpointSubtype);
                            macrosMapExpected.put(tag.attributeName(), getMacroTagPlaceholder(tag, null, endpointSubtype));
                        }
                        for (WebElement tagRow : Select.multiSelector(macrosRow)) {
                            macrosMapActual.put(Select.selector(tagRow, macrosNameLine).getAttribute("value"), Select.selector(tagRow, macrosValueLine).getAttribute("value"));
                        }
                        tagsCollection = tagsCollection.replace("[", "\\[").replace("]", "\\]").replace("$", "\\$").replace("{", "\\{").replace("}", "\\}");
                        endpointUrlExpected = "http:\\/\\/" + getRegionAddress(region) + "\\/\\?c=rtb&m=js&type=s2s&key=[a-z0-9]{32}" + tagsCollection;
                    }
                    case S2C_APP, S2C_WEB -> {
                        for (macroTagsTypes tag : epData.getJsVastTagsList()) {
                            tagsCollection += "&" + tag.attributeName() + "=" + getMacroTagPlaceholder(tag, null, endpointSubtype);
                            macrosMapExpected.put(tag.attributeName(), getMacroTagPlaceholder(tag, null, endpointSubtype));
                        }
                        for (WebElement tagRow : Select.multiSelector(macrosRow)) {
                            macrosMapActual.put(Select.selector(tagRow, macrosNameLine).getAttribute("value"), Select.selector(tagRow, macrosValueLine).getAttribute("value"));
                        }
                        tagsCollection = tagsCollection.replace("[", "\\[").replace("]", "\\]").replace("$", "\\$").replace("{", "\\{").replace("}", "\\}");
                        endpointUrlExpected = "<script type=\"text\\/javascript\" src=\"https:\\/\\/" + getRegionAddress(region) + "\\/\\?c=rtb&m=js&type=s2c&key=[a-z0-9]{32}" + tagsCollection + "\"><\\/script>";
                    }
                }
                /*Assertions*/
                softAssert.assertTrue(Select.selectByAttributeIgnoreCase(endpointJsTypeRadioButton, "value", endpointSubtype.attributeName()).isSelected(), "Info page: correct JS Tag type is not selected");
                softAssert.assertEquals(Select.selector(jsTagSizeSelect, relative_selectLabel).getText(), size.publicName(), "Info page: selected JS Tag size is incorrect");
                softAssert.assertEquals(Select.selector(jsTagSizeWidthInput).getAttribute("value"), widthExpected, "Info page: JS Tag Width is incorrect");
                softAssert.assertEquals(Select.selector(jsTagSizeHeightInput).getAttribute("value"), heightExpected, "Info page: JS Tag Height is incorrect");
                softAssert.assertEquals(Select.selector(jsTagBackfillInput).getAttribute("value"), epData.getJsBackfill(), "Info page: JS Tag Backfill is incorrect");
                softAssert.assertTrue(urlActual.matches(endpointUrlExpected), "Info page: Endpoint URL is incorrect. Expected: [" + endpointUrlExpected + "]. Actual: [" + urlActual + "]");
                if (endpointSubtype == jsTagTypeEnum.S2S_APP || endpointSubtype == jsTagTypeEnum.S2S_WEB || endpointSubtype == jsTagTypeEnum.S2C_APP || endpointSubtype == jsTagTypeEnum.S2C_WEB) {
                    softAssert.assertEquals(macrosMapActual, macrosMapExpected, "Info page: Macros tag list is incorrect");
                }
            }
        }
    }

    @Step("Select and add tag macro from list")
    public void addMacrosFromList(macroTagsTypes macros) {
        int rowcount = Select.multiSelector(macrosRow).size();
        selectSingleSelectDropdownOption(addMacrosSelect, macros.name());
        Select.selector(addMacrosButton).click();
        Wait.waitForNumberOfElementsToBe(macrosRow, rowcount + 1);
    }

    @Step("Select and add tag macros from list")
    public void addMacrosFromList(List<macroTagsTypes> macros) {
        for (macroTagsTypes macro : macros) {
            addMacrosFromList(macro);
        }
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
    public void addMacrosFromModal(macroTagsTypes macros) {
        WebElement macrosRow = Select.selectByAttributeIgnoreCase(macrosModalRow, "data-name", macros.attributeName());
        Select.selector(macrosRow, macrosModalAddButton).click();
    }

    @Step("Select and add tag macros from modal")
    public void addMacrosFromModal(List<macroTagsTypes> macrosList) {
        openMacrosModal();
        for (macroTagsTypes macro : macrosList) {
            addMacrosFromModal(macro);
        }
        closeMacrosModal();
    }

    @Step("Assert Validation messages in the Information section")
    public void assertValidationInfoPage(String nameErrorText, String companyErrorText, String macrosErrorText) {
        assertValidationError(softAssert, nameInput, nameErrorText);
        assertValidationError(softAssert, companySelect, relative_selectValidationLabel2, companyErrorText);
        assertValidationError(softAssert, addMacrosInput, macrosErrorText);
        softAssert.assertAll("Errors in Information section");
    }

    @Step("Go to endpoint edit page by direct link")
    public void gotoEditPage(int endpointId) {
        driver.navigate().to(getRunURL() + "/ad-exchange/ssp/" + endpointId + "/edit");
        Wait.waitForVisibility(nameInput);
    }

    //</editor-fold>

    //<editor-fold desc="Endpoint page - Settings">
    static final By
            settingsTab = By.xpath("//a[@data-toggle='#settings']"),
            regionSelect = By.xpath("//select[@id='regionId']"),
            allowedDspSelect = By.xpath("//select[@id='white_list_dsp']"),
            blockedDspSelect = By.xpath("//select[@id='block_list_dsp']"),
            adapterSelect = By.xpath("//select[@name='adapter_id']"),
            spendLimitInput = By.xpath("//input[@id='spendLimit']"),
            defaultTmaxInput = By.xpath("//input[@id='defaultTMax']"),
            bidPriceSelect = By.xpath("//select[@id='flag_bid_price']"),
            bidPriceInput = By.xpath("//input[@id='bid_price']"),
            advancedSettingsDynamicMarginInput = By.xpath("//input[@id='dynamic_margin_value']");

    @Step("Click on Settings tab")
    public void clickSettingsTab() {
        Select.selector(settingsTab).click();
        Wait.waitForClickable(bidPriceInput);
    }

    @Step("Setup Settings tab")
    public void setupSettingsTab(EndpointSupplyDO epData) {
        selectRegion(epData.getRegion());
        if (!epData.getDspAllowedMap().isEmpty()) {
            selectAllowedBlockedItems(allowedDspSelect, blockedDspSelect, true, epData.getDspAllowedMap());
        }
        if (!epData.getDspBlockedMap().isEmpty()) {
            selectAllowedBlockedItems(allowedDspSelect, blockedDspSelect, false, epData.getDspBlockedMap());
        }
        if (epData.getAdapter() != null) {
            selectAdapter(epData.getAdapter());
        }
        inputSpendLimit(epData.getSpendLimit());
        inputTmax(epData.getTmax());
        setMargin(epData.getMarginSettings());
        setBidPrice(epData.getBidType(), epData.getBidPrice());
        toggleAdFormat(epData.getAdFormatStatusMap());
        toggleTrafficType(epData.getTrafficStatusMap());
        toggleAdvancedSettingSsp(epData.getAdvancedStatusMap(), epData.getDynamicMarginValue());
    }

    @Step("Get random region")
    public regionsEnum getRandomRegion() {
        List<regionsEnum> regions = new ArrayList<>();
        for (WebElement reg : Select.multiSelector(regionSelect, relative_selectOptionWithTitle)) {
            regions.add(regionsEnum.valueOf(reg.getAttribute("title")));
        }
        return regions.get(new Random().nextInt(regions.size()));
    }

    @Step("Select endpoint region")
    public void selectRegion(regionsEnum region) {
        selectSingleSelectDropdownOption(regionSelect, region.toString());
    }

    //TODO the map could be <String, String> and store the values of 'selected', 'disabled', 'hide' and 'show'
    @Step("Get Allowed DSP list")
    public Map<String, Boolean> getAllowedDspList() {
        Map<String, Boolean> returnMap = new LinkedHashMap<>();
        if (Select.selector(allowedDspSelect, relative_selectLabel).getText().equals("All DSP")) {
            for (WebElement listElement : Select.multiSelector(allowedDspSelect, relative_multiselectAvailableOption)) {
                if (!listElement.getAttribute("class").contains("disabled")) {
                    returnMap.put(listElement.getAttribute("title"), false);
                }
            }
        } else {
            for (WebElement listElement : Select.multiSelector(allowedDspSelect, relative_multiselectAvailableOption)) {
                if (listElement.getAttribute("class").contains("selected")) {
                    returnMap.put(listElement.getAttribute("title"), true);
                }
            }
        }
        return returnMap;
    }

    @Step("Select random items in Allowed/Blocked DSP list")
    public Map<String, Boolean> selectAllowedBlockedDsp(boolean isAllowed, int dspCount) {
        return selectRandomAllowedBlockedListItems(allowedDspSelect, blockedDspSelect, isAllowed, dspCount);
    }

    @Step("Input Spend limit")
    public void inputSpendLimit(Integer spendLimit) {
        enterInInput(spendLimitInput, spendLimit);
    }

    @Step("Input Default Tmax")
    public void inputTmax(Integer tmax) {
        enterInInput(defaultTmaxInput, tmax);
    }

    @Step("Select adapter")
    public void selectAdapter(adaptersSspRtbEnum adapterName) {
        selectSingleSelectDropdownOptionByName(adapterSelect, adapterName.publicName());
    }

    @Step("Select Bid price type")
    public void selectBidPriceType(bidPriceTypeEnum bidPrice) {
        selectSingleSelectDropdownOption(bidPriceSelect, bidPrice.publicName());
    }

    @Step("Input Max Bid floor")
    public void inputBidPrice(Double bidFloor) {
        enterInInput(bidPriceInput, bidFloor);
    }

    @Step("Set Bid Price")
    public void setBidPrice(bidPriceTypeEnum bidType, Double bidPrice) {
        selectBidPriceType(bidType);
        inputBidPrice(bidPrice);
    }

    @Step("Toggle advanced settings")
    public void toggleAdvancedSettingSsp(Map<advancedSettingsEnum, Boolean> advancedSettings, Double dynamicMarginValue) {
        toggleAdvancedSetting(advancedSettings);
        if (advancedSettings.containsKey(advancedSettingsEnum.DYNAMIC_MARGIN) && advancedSettings.get(advancedSettingsEnum.DYNAMIC_MARGIN)) {
            enterInInput(advancedSettingsDynamicMarginInput, dynamicMarginValue);
        }
    }

    @Step("Assert existing endpoint Settings page")
    public void assertSettingsTab(EndpointSupplyDO epData) {
        String allowString = getExpectedMultiselectText(epData.getDspAllowedMap(), "All DSP", 10), blockString = getExpectedMultiselectText(epData.getDspBlockedMap(), "Select", 10);
        double bidPriceActual = Double.parseDouble(Select.selector(bidPriceInput).getAttribute("value"));
        softAssert.assertEquals(Select.selector(allowedDspSelect, relative_selectLabel).getText(), allowString, "Settings page: Allowed DSP list is incorrect");
        softAssert.assertEquals(Select.selector(blockedDspSelect, relative_selectLabel).getText(), blockString, "Settings page: Blocked DSP list is incorrect");
        if (epData.getAdapter() != null) {
            softAssert.assertEquals(Select.selector(adapterSelect, relative_selectLabel).getText(), epData.getAdapter().publicName(), "Settings page: RTB version is incorrect");
        }
        softAssert.assertEquals(Select.selector(spendLimitInput).getAttribute("value"), epData.getSpendLimit().toString(), "Settings page: Spend limit is incorrect");
        softAssert.assertEquals(Select.selector(defaultTmaxInput).getAttribute("value"), epData.getTmax().toString(), "Settings page: Default TMAX is incorrect");
        softAssert.assertEquals(Select.selector(bidPriceSelect, relative_selectLabel).getText(), epData.getBidType().publicName(), "Settings page: Bid price type is incorrect");
        softAssert.assertEquals(ParseUtil.formatBigCommaWithDecimal(bidPriceActual), ParseUtil.formatBigCommaWithDecimal(epData.getBidPrice()), "Settings page: Bid price is incorrect");
        softAssert.assertEquals(Select.selector(marginTypeSelect, relative_selectLabel).getText(), epData.getMarginSettings().getType().publicName(), "Settings page: Margin type is incorrect");
        switch (epData.getMarginSettings().getType()) {
            case FIXED ->
                    softAssert.assertEquals(Select.selector(marginFixedMinInput).getAttribute("value"), epData.getMarginSettings().getFixed().toString(), "Settings page: Fixed margin is incorrect");
            case RANGE -> {
                softAssert.assertEquals(Select.selector(marginFixedMinInput).getAttribute("value"), epData.getMarginSettings().getMin().toString(), "Settings page: Min margin is incorrect");
                softAssert.assertEquals(Select.selector(marginMaxInput).getAttribute("value"), epData.getMarginSettings().getMax().toString(), "Settings page: Max margin is incorrect");
            }
            case ADAPTIVE -> {
                softAssert.assertFalse(Select.selector(marginFixedMinInput).isDisplayed(), "Settings page: Min field is displayed for Adaptive margin");
                softAssert.assertFalse(Select.selector(marginMaxInput).isDisplayed(), "Settings page: Max field is displayed for Adaptive margin");
            }
        }
        for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : epData.getAdFormatStatusMap().entrySet()) {
            WebElement adFormatToggle = getToggleByName(toggleInput, adFormatEntry.getKey().attributeName());
            softAssert.assertEquals(adFormatToggle.isSelected(), adFormatEntry.getValue().booleanValue(), "Settings page: AdFormat [" + adFormatEntry.getKey().attributeName() + "] status is incorrect");
        }
        for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : epData.getAdFormatAvailabilityMap().entrySet()) {
            WebElement adFormatToggle = getToggleByName(toggleInput, adFormatEntry.getKey().attributeName());
            softAssert.assertEquals(adFormatToggle.isEnabled(), adFormatEntry.getValue().booleanValue(), "Settings page: AdFormat [" + adFormatEntry.getKey().attributeName() + "] availability is incorrect");
        }
        for (Entry<trafficTypeEnum, Boolean> trafficEntry : epData.getTrafficStatusMap().entrySet()) {
            WebElement trafficTypeToggle = getToggleByName(toggleInput, trafficEntry.getKey().attributeName());
            softAssert.assertEquals(trafficTypeToggle.isSelected(), trafficEntry.getValue().booleanValue(), "Settings page: Traffic type [" + trafficEntry.getKey().attributeName() + "] status is incorrect");
        }
        for (Entry<trafficTypeEnum, Boolean> trafficEntry : epData.getTrafficAvailabilityMap().entrySet()) {
            WebElement trafficTypeToggle = getToggleByName(toggleInput, trafficEntry.getKey().attributeName());
            softAssert.assertEquals(trafficTypeToggle.isEnabled(), trafficEntry.getValue().booleanValue(), "Settings page: Traffic type [" + trafficEntry.getKey().attributeName() + "] availability is incorrect");
        }
        for (Entry<advancedSettingsEnum, Boolean> advancedEntry : epData.getAdvancedStatusMap().entrySet()) {
            WebElement advancedToggle = getToggleByName(toggleInput, advancedEntry.getKey().attributeName());
            softAssert.assertEquals(advancedToggle.isSelected(), advancedEntry.getValue().booleanValue(), "Settings page: Advanced setting [" + advancedEntry.getKey().attributeName() + "] status is incorrect");
        }
        for (Entry<advancedSettingsEnum, Boolean> advancedEntry : epData.getAdvancedAvailabilityMap().entrySet()) {
            if (advancedEntry.getValue()) {
                softAssert.assertTrue(AssertUtil.assertPresent(Select.selectByAttributeIgnoreCase(toggleInput, "name", advancedEntry.getKey().attributeName())), "Settings page: Advanced setting [" + advancedEntry.getKey().attributeName() + "] should be available, but it's not. ");
            } else {
                softAssert.assertNotNull(Select.selectByAttributeIgnoreCase(toggleInput, "name", advancedEntry.getKey().attributeName()).getAttribute("disabled"), "Settings page: Advanced setting [" + advancedEntry.getKey().attributeName() + "] should not be available, but it is. ");
            }
        }
        if (epData.getDynamicMarginValue() != null) {
            softAssert.assertEquals(ParseUtil.parseDouble(Objects.requireNonNull(Select.selector(advancedSettingsDynamicMarginInput).getAttribute("value"))), epData.getDynamicMarginValue(), "Settings page: Advanced setting Dynamic Margin value is incorrect");
        }
    }

    @Step("Assert Validation messages in the Settings section")
    public void assertValidationSettingsPage(String spendErrorText, String tmaxErrorText, String marginMinFixedErrorText, String marginMaxErrorText, String bidPriceErrorText) {
        assertValidationError(softAssert, spendLimitInput, spendErrorText);
        assertValidationError(softAssert, defaultTmaxInput, tmaxErrorText);
        assertValidationError(softAssert, marginFixedMinInput, marginMinFixedErrorText);
        assertValidationError(softAssert, marginMaxInput, marginMaxErrorText);
        assertValidationError(softAssert, bidPriceInput, bidPriceErrorText);
        softAssert.assertAll("Errors in Information section");
    }

    @Step("Assert if DSP endpoint is NOT present in list")
    public void assertDspNotPresentInList(boolean isAllowed, String dspName) {
        By dspList = isAllowed ? allowedDspSelect : blockedDspSelect;
        for (WebElement dsp : Select.multiSelector(dspList, relative_selectOptionWithTitle)) {
            softAssert.assertFalse(dsp.getAttribute("title").contains(dspName), "DSP [" + dspName + "] is present in the " + Select.selector(dspList).getAttribute("id") + " list");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Endpoint page - Filter settings">
    static final By
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
        Wait.waitForVisibility(recordsLoader);
        Wait.waitForNotVisible(recordsLoader);
    }

    //</editor-fold>

    //<editor-fold desc="Endpoint page - Additional info">
    static final By
            additionalInfoTab = By.xpath("//a[@data-toggle='#additionalInfo']");

    @Step("Click on Additional info tab")
    public void clickAdditionalInfoTab() {
        Select.selector(additionalInfoTab).click();
        Wait.waitForVisibility(syncUrlInput);
    }

    //</editor-fold>

    //<editor-fold desc="Scanners">
    static final By
            scannersTab = By.xpath("//a[@data-toggle='#scannerSetting']"),
            scannersPageContent = By.xpath("//div[@id='scannerSetting']"),
            scannerSettingsRow = By.xpath("//div[@class='scanner-point']"),
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

    @Step("Set up endpoint scanner")
    public void setupScanner(EndpointCommonDO.ScannerSetting scanner) {
        String scannerStatusValue;
        WebElement scannerRow = getScannerRow(scanner.getType());
        switch (scanner.getType()) {
            case GEOEDGE, PROTECTED_PREBID ->
                    clickToggle(scannerRow, relative_scannerSettingsCheckbox, scanner.getScannerStatus());
            case PROTECTED_POSTBID, PIXALATE_POSTBID, WHITEOPS_MEDIA_GUARD, WHITEOPS_FRAUD_SENSOR -> {
                WebElement impInput;
                scannerStatusValue = scanner.getScannerStatus() ? "1" : "0";
                clickRadioButton(scannerRow, relative_scannerSettingsRadioButton, scannerStatusValue);
                if (scanner.getScannerStatus()) {
                    impInput = Select.selector(scannerRow, relative_scannerSettingsInput);
                    if (scanner.getImpressionScanStatus() != Select.selector(scannerRow, relative_scannerSettingsCheckbox).isSelected()) {
                        clickRadioButton(scannerRow, relative_scannerSettingsCheckbox, "1");
                    }
                    if (scanner.getImpressionScanStatus()) {
                        enterInInput(impInput, scanner.getImpressionScanValue());
                    }
                }
            }
            case PIXALATE_PREBID -> {
                Integer settingValue;
                Boolean settingStatus;
                WebElement settingCheckbox, settingInput;
                ScannerSetting.PixalatePrebidSetting setting;
                scannerStatusValue = (scanner.getScannerStatus() != null) ? (scanner.getScannerStatus() ? "1" : "-1") : "0"; // true = 1 - custom, false = -1 - off, null = 0 - default
                clickRadioButton(scannerRow, relative_scannerSettingsRadioButton, scannerStatusValue);
                if (scannerStatusValue.equals("1")) {
                    for (Entry<pixalatePrebidTypesEnum, ScannerSetting.PixalatePrebidSetting> pixalateSetting : scanner.getPixalatePrebidMap().entrySet()) {
                        setting = pixalateSetting.getValue();
                        settingStatus = setting.getStatus();
                        settingCheckbox = Select.selectByAttributeContains(scannerRow, relative_scannerSettingsCheckbox, "name", pixalateSetting.getKey().attributeName());
                        switch (pixalateSetting.getKey()) {
                            case DEVICE_ID, BUNDLES, IPV4, IPV6, OTT -> {
                                if (settingStatus != null) {
                                    if (settingStatus != settingCheckbox.isSelected()) {
                                        clickRadioButton(settingCheckbox);
                                    }
                                    if (settingStatus) {
                                        settingInput = Select.selectByAttributeContains(scannerRow, relative_scannerSettingsInput, "name", pixalateSetting.getKey().attributeName());
                                        settingValue = setting.getValue();
                                        enterInInput(settingInput, settingValue);
                                    }
                                }
                            }
                            case USER_AGENT, DATA_CENTER, DOMAINS, DEFASED_APP -> {
                                if (settingStatus != null && settingStatus != settingCheckbox.isSelected()) {
                                    clickRadioButton(settingCheckbox);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Step("Setup Scanner tab")
    public void setupScannerSection(List<EndpointCommonDO.ScannerSetting> scannerSettingList) {
        for (EndpointCommonDO.ScannerSetting scannerSetting : scannerSettingList) {
            setupScanner(scannerSetting);
        }
    }

    @Step("Get default Pixalate Prebid settings")
    public Map<pixalatePrebidTypesEnum, ScannerSetting.PixalatePrebidSetting> getPixalatePrebidDefaultSettings() {
        Map<pixalatePrebidTypesEnum, ScannerSetting.PixalatePrebidSetting> pixalatePrebidReturn = new LinkedHashMap<>();
        Condition condition = DSL.field("type").equal("pixalate-prebid");
        Result<org.jooq.Record> results = dbUtils.getDbDataResult("scanner_settings", condition);
        List<String> keysList = new ArrayList<>(results.getValues("key", String.class));
        List<String> valuesList = new ArrayList<>(results.getValues("value", String.class));
        for (pixalatePrebidTypesEnum scannerType : pixalatePrebidTypesEnum.values()) {
            ScannerSetting.PixalatePrebidSetting setting = new ScannerSetting.PixalatePrebidSetting();
            setting.setStatus(valuesList.get(keysList.indexOf(scannerType.attributeName() + ".enable")).equals("1"));
            if (scannerType == pixalatePrebidTypesEnum.DEVICE_ID || scannerType == pixalatePrebidTypesEnum.BUNDLES || scannerType == pixalatePrebidTypesEnum.IPV4 || scannerType == pixalatePrebidTypesEnum.IPV6 || scannerType == pixalatePrebidTypesEnum.OTT) {
                setting.setValue((int) (Double.parseDouble(valuesList.get(keysList.indexOf(scannerType.attributeName() + ".value"))) * 100));
            } else {
                setting.setValue(null);
            }
            pixalatePrebidReturn.put(scannerType, setting);
        }
        return pixalatePrebidReturn;
    }

    @Step("Assert endpoint Scanners page")
    public void assertScannersTab(List<ScannerSetting> scannerList) {
        for (ScannerSetting scanner : scannerList) {
            assertScannerSettings(scanner);
        }
    }

    @Step("Assert scanner settings")
    private void assertScannerSettings(ScannerSetting scannerSettings) {
        Boolean scannerStatus = scannerSettings.getScannerStatus();
        String scannerStatusValue;
        WebElement scannerRow = getScannerRow(scannerSettings.getType());
        switch (scannerSettings.getType()) {
            case GEOEDGE, PROTECTED_PREBID ->
                    softAssert.assertEquals(Select.selector(scannerRow, relative_scannerSettingsCheckbox).isSelected(), scannerStatus.booleanValue(), "Scanners tab: " + scannerSettings.getType().publicName() + " status setting is incorrect");
            case PROTECTED_POSTBID, PIXALATE_POSTBID, WHITEOPS_MEDIA_GUARD, WHITEOPS_FRAUD_SENSOR -> {
                scannerStatusValue = scannerStatus ? "1" : "0";
                softAssert.assertTrue(Select.selectByAttributeIgnoreCase(scannerRow, relative_scannerSettingsRadioButton, "value", scannerStatusValue).isSelected(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner status setting is incorrect");
                if (scannerStatus) {
                    softAssert.assertEquals(Select.selector(scannerRow, relative_scannerSettingsCheckbox).isSelected(), scannerSettings.getImpressionScanStatus(), "Scanners tab: " + scannerSettings.getType().publicName() + " `% of impressions` status is incorrect");
                    softAssert.assertEquals(Select.selector(scannerRow, relative_scannerSettingsInput).getAttribute("value"), scannerSettings.getImpressionScanValue().toString(), "Scanners tab: " + scannerSettings.getType().publicName() + " `% of impressions` value is incorrect");
                }
            }
            case PIXALATE_PREBID -> {
                WebElement settingCheckbox, settingInput;
                scannerStatusValue = (scannerStatus != null) ? (scannerStatus ? "1" : "-1") : "0";// true = 1 - custom, false = -1 - off, null = 0 - default
                softAssert.assertTrue(Select.selectByAttributeIgnoreCase(scannerRow, relative_scannerSettingsRadioButton, "value", scannerStatusValue).isSelected(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner status setting is incorrect");
                if (!scannerStatusValue.equals("-1")) {
                    for (Entry<pixalatePrebidTypesEnum, ScannerSetting.PixalatePrebidSetting> pixalateSetting : scannerSettings.getPixalatePrebidMap().entrySet()) {
                        settingCheckbox = Select.selectByAttributeContains(scannerRow, relative_scannerSettingsCheckbox, "name", pixalateSetting.getKey().attributeName());
                        switch (pixalateSetting.getKey()) {
                            case USER_AGENT, DOMAINS, DATA_CENTER, DEFASED_APP -> {
                                if (scannerStatusValue.equals("0")) {
                                    softAssert.assertFalse(settingCheckbox.isEnabled(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner's <" + pixalateSetting.getKey().publicName() + "> checkbox enabled/disabled status is incorrect");
                                } else {
                                    softAssert.assertEquals(settingCheckbox.isSelected(), pixalateSetting.getValue().getStatus().booleanValue(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner's <" + pixalateSetting.getKey().publicName() + "> on/off status is incorrect");
                                }
                            }
                            case DEVICE_ID, BUNDLES, IPV4, IPV6, OTT -> {
                                settingInput = Select.selectByAttributeContains(scannerRow, relative_scannerSettingsInput, "name", pixalateSetting.getKey().attributeName());
                                softAssert.assertEquals(settingInput.getAttribute("value"), pixalateSetting.getValue().getValue().toString(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner's <" + pixalateSetting.getKey().publicName() + "> setting value is incorrect");
                                if (scannerStatusValue.equals("0")) {
                                    softAssert.assertFalse(settingCheckbox.isEnabled(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner's <" + pixalateSetting.getKey().publicName() + "> checkbox enabled/disabled status is incorrect");
                                    softAssert.assertFalse(settingInput.isEnabled(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner's <" + pixalateSetting.getKey().publicName() + "> input enabled/disabled status is incorrect");
                                } else {
                                    softAssert.assertEquals(settingCheckbox.isSelected(), pixalateSetting.getValue().getStatus().booleanValue(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner's <" + pixalateSetting.getKey().publicName() + "> on/off status is incorrect");
                                    softAssert.assertTrue(settingInput.isEnabled(), "Scanners tab: " + scannerSettings.getType().publicName() + " scanner's <" + pixalateSetting.getKey().publicName() + "> input enabled/disabled status is incorrect");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //</editor-fold>

    @Step("Save endpoint")
    public void clickSaveEndpoint() {
        Select.selector(saveButton).click();
        Wait.waitForVisibility(preloader);
        Wait.waitForClickable(nameInput);
    }

    @Step("Save endpoint with validation errors")
    public void clickSaveEndpointWithErrors() {
        Select.selector(saveButton).click();
        Wait.sleep(1500);   //The focus is jumping between the sections, so the Sleep is the only simple and working solution here
        Wait.waitForNotVisible(preloader);
    }

    @Step("Save endpoint and exit to dashboard")
    public void clickSaveExitEndpoint() {
        Select.selector(saveExitButton).click();
        Wait.waitForClickable(createEndpointButton);
        Wait.waitForVisibility(endpointListRow);
    }

}
