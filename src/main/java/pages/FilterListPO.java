package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.AssertUtility;
import data.CommonEnums;
import data.FilterListEnums.tableColumnsEnum;
import data.dataobject.FilterListDO;
import data.textstrings.messages.CommonText;
import data.textstrings.messages.FilterListText;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static data.textstrings.messages.CommonText.MODAL_WARNING_HEADER;
import static data.textstrings.messages.FilterListText.modalConfirmDelete;
import static data.textstrings.messages.FilterListText.modalWarningAllDsp;

public class FilterListPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final AssertUtility AssertUtil = new AssertUtility(driver);

    public FilterListPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public FilterListPO(){
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Filter Lists page")
    public void gotoFilterListSection(){
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(filtersLink).click();
        Wait.waitForClickable(createFilterButton);
        Wait.attributeNotContains(filterListTable, "class", "disabled");
    }

    //<editor-fold desc="Filter lists table">
    static final By
            createFilterButton = By.xpath("//a[contains(@href, 'create')]"),
            searchFilterInput = By.xpath("//input[@id='searchList']"),
            filterListTable = By.xpath("//div[@id='filterListDashboard']//table"),
            filterListTableEmpty = By.xpath("//div[@id='getFilterList']//div[contains(@class, 'no-data')]/h4"),
            filterListTableHeaderCell = By.xpath("//div[@id='filterListDashboard']//table//th"),
            filterListTableRow = tableRowGeneral,
            filterListRowName = By.xpath("./td[@data-field='name']"),
            filterListRowFilterType = By.xpath("./td[@data-field='type']"),
            filterListRowRecordType = By.xpath("./td[@data-field='record_type']"),
            filterListRowCreated = By.xpath("./td[@data-field='create_at']"),
            filterListRowUpdated = By.xpath("./td[@data-field='updated_at']"),
            filterListRowActionEdit = By.xpath("./td[@data-field='action']//a[contains(@href, 'manage')]"),
            filterListRowActionDelete = By.xpath("./td[@data-field='action']//a[@href='#' and @title]");

    @Step("Get filter list row by name")
    public WebElement getFilterListRow(String filterName){
        WebElement row = Select.selectParentByTextEquals(filterListTableRow, filterListRowName, filterName);
        hardAssert.assertNotNull(row, "Filter list <" + filterName + "> not found");
        return row;
    }

    @Step("Open Create filter list page")
    public void clickCreateFilterList(){
        Select.selector(createFilterButton).click();
        Wait.waitForVisibility(filterPageNameInput);
        Wait.waitForNotVisible(recordsLoader);
    }

    @Step("Search filter list in table")
    public void searchFilterInTable(String query){
        searchItemInTable(query, searchFilterInput, filterListTable);
    }

    @Step("Open filter list edit")
    public void clickFilterEdit(String filterName){
        WebElement row = getFilterListRow(filterName);
        Select.selector(row, filterListRowActionEdit).click();
        Wait.waitForVisibility(filterPageNameInput);
    }

    @Step("Delete filter list")
    public void clickFilterDelete(String filterName, boolean confirm){
        WebElement row = getFilterListRow(filterName);
        Select.selector(row, filterListRowActionDelete).click();
        Wait.waitForClickable(modalConfirmButton);
        assertModal(softAssert, modalConfirmDelete, "", "Filter list delete");
        if (confirm){
            modalClickConfirm(modalDialog);
            Wait.waitForNotVisible(modalConfirmButton);
            Wait.waitForNotVisible(preloader);
            Wait.waitForClickable(createFilterButton);
            Wait.attributeNotContains(filterListTable, "class", "disabled");
        } else {
            modalClickCancel(modalDialog);
            Wait.waitForNotVisible(modalCancelButton);
        }
    }

    @Step("Click on column to sort")
    public void clickColumnSorting(tableColumnsEnum column){
        Select.selectByAttributeExact(filterListTableHeaderCell, "data-field", column.attributeName()).click();
        Wait.attributeNotContains(filterListTable, "class", "disabled");
    }

    @Step("Assert filter list in table")
    public void assertFilterInTable(FilterListDO filterData){
        WebElement filterRow = getFilterListRow(filterData.getFilterName());
        String createdActualString = Select.selector(filterRow, filterListRowCreated).getText(), updatedActualString = Select.selector(filterRow, filterListRowUpdated).getText();
        String listType = filterData.getListType() ? "Black" : "White";
        softAssert.assertEquals(Select.selector(filterRow, filterListRowFilterType).getText(), listType, "Filter list Type is incorrect");
        softAssert.assertEquals(Select.selector(filterRow, filterListRowRecordType).getText(), filterData.getRecordType().publicName(), "Filter list Record type is incorrect");
        softAssert.assertTrue(createdActualString.startsWith(filterData.getCreatedDate()), "Filter list Created date <" + createdActualString + "> is not <" + filterData.getCreatedDate() + ">");
        softAssert.assertTrue(updatedActualString.startsWith(filterData.getUpdatedDate()), "Filter list Updated  date <" + updatedActualString + "> is not <" + filterData.getUpdatedDate() + ">");
        if (isSoftAssertLocal){
            softAssert.assertAll("Filter list <" + filterData.getFilterName() + "> data in table is incorrect");
        }
    }

    @Step("Assert filter list is deleted from table")
    public void assertFilterListNotInTable(String filterName){
        hardAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectParentByTextEquals(filterListTableRow, filterListRowName, filterName)), "Filter list <" + filterName + "> is present in table");
    }

    @Step("Assert table sorting by column")
    public void assertFilterTableSorting(tableColumnsEnum column, boolean isAscending){
        WebElement columnHeader = Select.selectByAttributeExact(filterListTableHeaderCell, "data-field", column.attributeName());
        List<String> columnItemsActual = new ArrayList<>();
        for (WebElement cell : Select.selectListByAttributeExact(filterListTableRow, relative_generalTableBodyCell, "data-field", column.attributeName())){
            columnItemsActual.add(cell.getText());
        }
        List<String> columnItemsSorted = new ArrayList<>(columnItemsActual);
        if (isAscending){
            Collections.sort(columnItemsSorted);
        } else {
            Collections.sort(columnItemsSorted);
            Collections.reverse(columnItemsSorted);
        }
        softAssert.assertEquals(Select.selector(columnHeader, tableColumnHeaderSortIcon).getAttribute("class").contains("amount-asc"), isAscending, "Sorting icon of [" + column.publicName() + "] is incorrect");
        softAssert.assertEquals(columnItemsActual, columnItemsSorted, "Column [" + column.publicName() + "] sorting is incorrect. isAscending = " + isAscending);
    }

    @Step("Assert Filter lists table contains only searched names")
    public void assertFilterTableNames(String nameToContain){
        if (!Select.selectListByAttributeExact(filterListTableRow, relative_generalTableBodyCell, "data-field", tableColumnsEnum.FILTER_NAME.attributeName()).isEmpty()){
            for (WebElement nameRow : Select.selectListByAttributeExact(filterListTableRow, relative_generalTableBodyCell, "data-field", tableColumnsEnum.FILTER_NAME.attributeName())){
                softAssert.assertTrue(nameRow.getText().contains(nameToContain), "Endpoint name <" + nameRow.getText() + "> does not contain the symbols <" + nameToContain + ">");
            }
        } else {
            softAssert.fail("No filter lists found");
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors in filter list name search:");
        }
    }

    @Step("Assert Dashboard list is empty")
    public void assertFilterTableEmpty(String message){
        softAssert.assertEquals(Select.selector(filterListTableEmpty).getText(), message, "Message for empty table is incorrect");
    }

    //</editor-fold>

    //<editor-fold desc="Filter list page">
    static final By filterPageNameInput = By.xpath("//input[@name='name']"),
            filterPageSspSelect = By.xpath("//select[@id='listsSSP']"),
            filterPageDspSelect = By.xpath("//select[@id='listsDSP']"),
            filterPageRecordTypeSelect = By.xpath("//select[@id='recordType']"),
            filterPageTypeToggle = By.xpath("//input[@name='type']"),
            filterPageSaveButton = By.xpath("//label[@for='submitHideInit']");

    @Step("Set general Filter list settings")
    public void setupFilterList(FilterListDO filterData){
        enterInInput(filterPageNameInput, filterData.getFilterName());
        if (filterData.getSspList() != null){
            Select.selector(filterPageSspSelect, relative_selectLabel).click();
            for (Map.Entry<String, Boolean> sspEntry : filterData.getSspList().entrySet()){
                clickDropdownMultiselectOption(filterPageSspSelect, sspEntry.getKey(), sspEntry.getValue());
            }
            closeMultiselectList(filterPageSspSelect);
        }
        if (filterData.getDspList() != null){
            Select.selector(filterPageDspSelect, relative_selectLabel).click();
            for (Map.Entry<String, Boolean> dspEntry : filterData.getDspList().entrySet()){
                clickDropdownMultiselectOption(filterPageDspSelect, dspEntry.getKey(), dspEntry.getValue());
            }
            closeMultiselectList(filterPageDspSelect);
        }
        if (filterData.getRecordType() != null){
            selectSingleSelectDropdownOptionByName(filterPageRecordTypeSelect, filterData.getRecordType().publicName());
        }
        clickToggle(filterPageTypeToggle, filterData.getListType());
        if (filterData.getRecordList() != null){
            addFilterRecordsManual(filterData.getRecordList());
        }
    }

    @Step("Save filter list")
    public void clickSaveFilterList(){
        Select.selector(filterPageSaveButton).click();
        Wait.waitForVisibility(filterListTableRow);
    }

    @Step("Save filter list")
    public void clickSaveFilterListAllDsp(boolean confirm){
        Select.selector(filterPageSaveButton).click();
        Wait.waitForClickable(modalConfirmButton);
        assertModal(softAssert, MODAL_WARNING_HEADER, modalWarningAllDsp, "'Save all DSP'");
        if (confirm){
            Select.selector(modalConfirmButton).click();
            Wait.waitForNotVisible(modalConfirmButton);
            Wait.waitForVisibility(filterListTableRow);
        } else {
            Select.selector(modalCancelButton).click();
            Wait.waitForNotVisible(modalCancelButton);
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Modal texts are incorrect:");
        }
    }

    @Step("Get random records from SSP/DSP list")
    public Map<String, Boolean> getRandomEndpoints(boolean isSsp, int count){
        Map<String, Boolean> returnMap = getRandomActiveMultiselectOptionsAsMap(isSsp ? filterPageSspSelect : filterPageDspSelect, count);
        for (Map.Entry<String, Boolean> entry : returnMap.entrySet()){
            if (entry.getKey().equals("Native Supply #-1")){
                returnMap.remove(entry.getKey());
                if (returnMap.isEmpty()){
                    returnMap = getRandomActiveMultiselectOptionsAsMap(isSsp ? filterPageSspSelect : filterPageDspSelect, count);
                }
            }
        }
        return returnMap;
    }

    @Step("Assert filter list edit page")
    public void assertFilterEdit(FilterListDO filterData){
        String sspListPlaceholder = getExpectedMultiselectText(filterData.getSspList(), "All SSP", 10), dspListPlaceholder = getExpectedMultiselectText(filterData.getDspList(), "All DSP", 10);
        softAssert.assertEquals(Select.selector(filterPageNameInput).getAttribute("value"), filterData.getFilterName(), "Filter list Name is incorrect");
        softAssert.assertEquals(Select.selector(filterPageSspSelect, relative_selectLabel).getText(), sspListPlaceholder, "Filter list SSP list is incorrect");
        softAssert.assertEquals(Select.selector(filterPageDspSelect, relative_selectLabel).getText(), dspListPlaceholder, "Filter list DSP list is incorrect");
        softAssert.assertEquals(Select.selector(filterPageRecordTypeSelect, relative_selectLabel).getText(), filterData.getRecordType().publicName(), "Filter list Record type is incorrect");
        softAssert.assertFalse(Select.selector(filterPageRecordTypeSelect).isEnabled(), "Filter list Record type is not disabled");
        softAssert.assertEquals(Select.selector(filterPageTypeToggle).isSelected(), filterData.getListType().booleanValue(), "Filter list Type is incorrect");
        assertRecords(softAssert, filterData.getRecordList());
        if (isSoftAssertLocal){
            softAssert.assertAll("Filter list edit page is incorrect");
        }
    }

    @Step("Assert error modal on records upload")
    public void assertImportErrorModal(){
        assertModal(softAssert, CommonText.MODAL_ERROR_HEADER, FilterListText.ERROR_MODAL_IMPORT_EMPTY, "Records import error");
        if (isSoftAssertLocal){
            softAssert.assertAll("Warning modal is incorrect:");
        }
    }

    //</editor-fold>


    @Step("Clear filter lists")
    public void clearFilterLists(){
        boolean toRun = true, pageRun = true;
        while (toRun){
            while (pageRun){
                int i = 0;
                for (; i < Select.multiSelector(filterListTableRow).size(); i++){
                    List<WebElement> rows = Select.multiSelector(filterListTableRow);
                    String filterName = Select.selector(rows.get(i), filterListRowName).getText();
                    if (filterName.contains("AUTO list") || filterName.contains("AUTOlist") || filterName.contains("FilterFromDsp") || filterName.contains("FilterFromSsp") || filterName.contains("SearchAUTOlist") || filterName.contains("Filter EDIT") || filterName.contains("Filter endpoint link list") || filterName.contains("Filter endpoint select all")){
                        clickFilterDelete(filterName, true);
                        break;
                    }
                }
                if (Select.multiSelector(filterListTableRow).size() == i){
                    pageRun = false;
                }
            }
            if (getCurrentPage() != getPagingCount()){
                pagingActionSelect(CommonEnums.pagingActionsTypes.FORWARD, null);
                pageRun = true;
            } else {
                toRun = false;
            }
        }
    }

}
