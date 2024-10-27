package pages;

import com.fasterxml.jackson.databind.MappingIterator;
import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.*;
import data.UserEnum;
import data.dataobject.ReportDO;
import data.textstrings.messages.CommonText;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static data.ReportEnum.*;

public class ReportPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final BasicUtility basicUtility = new BasicUtility();
    private final FileUtility FileUtil = new FileUtility();
    private final BrowserUtility BrowserUtil = new BrowserUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);

    public ReportPO(SoftAssertCustom sAssert) {
        isSoftAssertLocal = false;
        this.softAssert = sAssert;
    }

    public ReportPO(){
        isSoftAssertLocal = true;
        this.softAssert = new SoftAssertCustom();
    }

    @Step("Open User Report page")
    public void gotoUserReportSection(){
        Select.actionMouseOver(reportMenuSectionButton);
        Select.selector(reportMenuSectionButton, reportUserAdminPublisherSectionButton).click();
        Wait.waitForClickable(generateReportButton);
    }

    @Step("Open User Report page")
    public void gotoUserReportSection(UserEnum.userRoleEnum userRole){
        switch (userRole){
            case PUBL, ADM -> {
                Select.actionMouseOver(reportMenuSectionButton);
                Select.selector(reportMenuSectionButton, reportUserAdminPublisherSectionButton).click();
            }
            case PUBL_SSP, PUBL_DSP -> Select.selector(reportUserSspDspUserSectionButton).click();
        }
        Wait.waitForClickable(generateReportButton);
    }

    @Step("Open ADX Statistics page")
    public void gotoStatisticsReport(){
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(adxStatisticsLink).click();
        Wait.waitForClickable(generateReportButton);
    }

    @Step("Open Publishers' Report page")
    public void gotoPublishersReport(){
        openAdminMenu();
        clickPubManagementWrap(true);
        Select.selector(pubReportLink).click();
        Wait.waitForClickable(generateReportButton);
    }

    @Step("Refresh report builder page")
    public void refreshBuilderPage(){
        refreshPage(generateReportButton);
    }

    @Step("Set report settings")
    public void setReportData(ReportDO data){
        Map<attributesEnum, String> attrFilters = data.getAttributeFilterMap();
        if (data.getCalendarRange() != null){
            calendarSelectDatePeriod(data.getCalendarRange());
        } else {
            calendarSelectDatePeriod(data.getDateFrom(), data.getDateTo());
        }
        selectTimezoneBuilder(data.getTimeZone());
        selectAggregatedByBuilder(data.getAggregateBy());
        if (data.isSelectAllAttributes()){
            clickSelectAllAttributes(true);
        }
        selectAttribute(data);
        if (!attrFilters.isEmpty()){
            for (Entry<attributesEnum, String> filter : attrFilters.entrySet()){
                setAttributeFilterBuilder(filter.getKey(), filter.getValue(), data.getReportType());
            }
        }
        if (data.isSelectAllMetrics()){
            clickSelectAllMetrics(true);
        }
        selectMetric(data.getMetricsMap());
    }

    @Step("Assert report builder page")
    public void assertBuilderPage(ReportDO reportData){
        List<String> attributesActual = new ArrayList<>(), attributesExpected = new ArrayList<>(), metricsActual = new ArrayList<>(), metricsExpected = new ArrayList<>();
        String errorMessageName = reportData.getPresetName() == null ? "Report:" : "Preset <" + reportData.getPresetName() + ">:";
        for (WebElement attribute : Select.multiSelector(attributeRow, relative_generalCheckbox)){
            if (attribute.isSelected()){
                attributesActual.add(attribute.getAttribute("value"));
            }
        }
        for (attributesEnum attr : reportData.getAttrMap().keySet()){
            attributesExpected.add(attr.attributeName(reportData.getReportType()));
        }
        for (WebElement metric : Select.multiSelector(metricRow, relative_generalCheckbox)){
            if (metric.isSelected()){
                metricsActual.add(metric.getAttribute("value"));
            }
        }
        for (metricsEnum metric : reportData.getMetricsMap().keySet()){
            metricsExpected.add(metric.attributeName());
        }
        if (!reportData.getMetricsFilterMap().isEmpty()){
            for (Entry<metricsEnum, ReportDO.MetricFilter> metric : reportData.getMetricsFilterMap().entrySet()){
                WebElement mRow = Select.selectParentByAttributeExact(metricRow, relative_generalCheckbox, "value", metric.getKey().attributeName()), mSelect = Select.selector(mRow, metricFilterSelect);
                softAssert.assertEquals(Select.selector(mSelect, relative_selectLabel).getText(), metric.getValue().getFilterType().publicName(), errorMessageName + "Metric <" + metric.getKey().attributeName() + "> filter type is incorrect");
                softAssert.assertEquals(Select.selector(mRow, metricFilterInput).getAttribute("value"), metric.getValue().getFilterValue(), errorMessageName + "Metric <" + metric.getKey().attributeName() + "> filter value is incorrect");
            }
        }
        softAssert.assertEqualsNoOrder(attributesActual.toArray(), attributesExpected.toArray(), errorMessageName + " attributes are incorrect. Expected: <" + attributesExpected + ">. Actual: <" + attributesActual + ">");
        softAssert.assertEqualsNoOrder(metricsActual.toArray(), metricsExpected.toArray(), errorMessageName + " metrics are incorrect. Expected: <" + metricsExpected + ">. Actual: <" + metricsActual + ">");
        assertCalendarRange(reportData.getDateFrom(), reportData.getDateTo(), errorMessageName);
        softAssert.assertEquals(Select.selector(timeZoneSelect, relative_selectLabel).getText(), reportData.getTimeZone(), errorMessageName + " timezone is incorrect");
        softAssert.assertTrue(Select.selectByAttributeExact(aggregatedByRadioButton, "value", reportData.getAggregateBy().attributeName()).isSelected(), errorMessageName + " aggregated by is incorrect");
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on report builder page");
        }
    }

    @Step("Open share report link")
    public void openShareLink(String shareLink){
        driver.navigate().to(shareLink);
        Wait.waitForNotVisible(shareButton);
        Wait.waitForClickable(updateButton);
        Wait.waitForNotVisible(reportGraphLoading, 120);
        Wait.waitForNotVisible(reportTableLoading, 120);
    }

    //<editor-fold desc="Presets">
    static final By
            addPresetButton = By.xpath("//div[@id='presetLists']//a[@class='secondary-content trigger-preset']"),
            updatePresetButton = By.xpath("//div[@id='presetLists']//a[@class='secondary-content trigger-preset is-update']"),
            presetRow = By.xpath("//div[@id='presetLists']//label"),
            presetNameString = By.xpath("./span[@title]"),
            deletePresetButton = By.xpath("./span[@href]");

    @Step("Get preset name")
    public WebElement getPresetRow(String presetName){
        WebElement row = Select.selectParentByTextEquals(presetRow, presetNameString, presetName);
        hardAssert.assertNotNull(row, "Preset <" + presetName + "> is not found");
        return row;
    }

    @Step("Click Add preset button")
    public void clickAddPreset(){
        Select.selector(addPresetButton).click();
        Wait.waitForClickable(modalConfirmButton);
    }

    @Step("Input preset name")
    public void inputPresetName(String name){
        enterInInput(modalInputField, name);
    }

    @Step("Rename preset")
    public void renamePreset(String oldName, String newName){
        WebElement presetRow = getPresetRow(oldName);
        Select.actionMouseClickDouble(presetRow);
        inputPresetName(newName);
        clickSavePreset();
    }

    @Step("Save preset")
    public void clickSavePreset(){
        Select.selector(modalConfirmButton).click();
        Wait.waitForNotVisible(modalConfirmButton);
    }

    @Step("Update preset")
    public void clickUpdatePreset(){
        Select.selector(updatePresetButton).click();
        Wait.sleep(2000);   //TODO: there is no indication, if the update was successful, except the network request
    }

    @Step("Delete preset")
    public void deletePreset(String presetName){
        WebElement row = getPresetRow(presetName);
        Select.selector(row, deletePresetButton).click();
        Wait.waitForClickable(modalConfirmButton);
        Select.selector(modalConfirmButton).click();
        Wait.waitForClickable(modalConfirmButton);
        Select.selector(modalConfirmButton).click();
        Wait.waitForClickable(generateReportButton);
        softAssert.assertTrue(AssertUtil.assertNotPresent(Select.selectByTextEquals(presetRow, presetNameString, presetName)), "Preset <" + presetName + "> is not deleted");
    }

    @Step("Close preset modal")
    public void clickCancelPreset(){
        Select.selector(modalCancelButton).click();
        Wait.waitForNotVisible(modalCancelButton);
    }

    @Step("Save preset with errors")
    public void clickSavePresetWithErrors(){
        Select.selector(modalConfirmButton).click();
        Wait.waitForVisibility(modalValidationContentText);
    }

    @Step("Assert preset error")
    public void assertPresetModalError(String errorText){
        hardAssert.assertEquals(Select.selector(modalValidationContentText).getText(), errorText, "Preset modal error is incorrect");
    }

    @Step("Select preset")
    public void selectPreset(String presetName){
        Select.selector(getPresetRow(presetName), presetNameString).click();
    }

    //</editor-fold>

    //<editor-fold desc="Date/Time">
    static final By
            timeZoneSelect = By.xpath("//select[@id='timeZone']"),
            generateReportButton = By.xpath("//div[@class='card-panel']//a"),
            aggregatedByRadioButton = By.xpath("//div[@class='card-panel']//input[@type='radio']");

    @Step("Select timezone in report builder")
    public void selectTimezoneBuilder(String timezone){
        selectSingleSelectDropdownOptionByName(timeZoneSelect, timezone);
    }

    @Step("Select Aggregated By option in report builder")
    public void selectAggregatedByBuilder(aggregateByEnum aggregatedBy){
        clickRadioButton(aggregatedByRadioButton, aggregatedBy.attributeName());
    }

    @Step("Click on Generate report button")
    public void clickGenerateReport(){
        Select.selector(generateReportButton).click();
        Wait.waitForVisibility(modalDialog);
        Wait.waitForNotVisible(modalDialog, 120);
        Wait.waitForClickable(backButton);
        Wait.waitForNotVisible(reportGraphLoading, 120);
        Wait.waitForNotVisible(reportTableLoading, 120);
    }

    @Step("Click on Generate report button with error")
    public void clickGenerateReportWithError(){
        Select.selector(generateReportButton).click();
        Wait.waitForClickable(modalConfirmButton);
    }

    @Step("Assert error message on report generation")
    public void assertReportGenerationError(String errorText){
        softAssert.assertEquals(Select.selector(modalHeaderText).getText(), CommonText.MODAL_ERROR_HEADER, "Report generation error modal header is incorrect");
        softAssert.assertEquals(Select.selector(modalContentText).getText(), errorText, "Report generation error is incorrect");
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on report generation validation modal");
        }
    }


    //</editor-fold>

    //<editor-fold desc="Attributes and Metrics">
    static final By
            attributeSelectAllButton = By.xpath("//a[contains(@class, 'select-all-attributes')]"),
            metricSelectAllButton = By.xpath("//a[contains(@class, 'select-all-metrics')]"),
            attributeRow = By.xpath("//div[@class='collection-item attribute-item']"),
            attributeFilterInput = By.xpath(".//textarea"),
            metricRow = By.xpath("//div[@class='collection-item metric-item']"),
            attrMetricCheckboxLabel = By.xpath(".//label[@for]"),
            attrMetricWrapButton = By.xpath(".//a[contains(@class, 'show-filters-settings')]"),
            attributeDropdownButton = By.xpath(".//div[@role='button']"),
            metricFilterSelect = relative_generalSelect,
            metricFilterInput = By.xpath(".//input[@type='number']"),
            metricTooltipIcon = By.xpath(".//span[@data-tooltip]");

    @Step("Select All Attributes")
    public void clickSelectAllAttributes(boolean toSelect){
        if (toSelect ^ Select.selector(attributeSelectAllButton).getAttribute("class").contains("deselected-all")){
            Select.selector(attributeSelectAllButton).click();
            for (WebElement checkbox : Select.multiSelector(attributeRow, relative_generalCheckbox)){
                if (checkbox.getAttribute("value").equals("deal_dsp_id") || checkbox.getAttribute("value").equals("deal_publisher_id")){
                    continue;
                }
                softAssert.assertTrue(checkbox.isSelected(), "The attribute <" + checkbox.getAttribute("value") + "> is not selected");
            }
            if (isSoftAssertLocal){
                softAssert.assertAll("Errors on selecting all Attributes");
            }
        }
    }

    @Step("Select Attribute")
    public void selectAttribute(Map<attributesEnum, Boolean> attributes){
        WebElement attrElement;
        String attrId;
        for (Entry<attributesEnum, Boolean> attr : attributes.entrySet()){
            if (attr.getKey().equals(attributesEnum.ALL)){
                if (attr.getValue() != Select.selector(attributeSelectAllButton).getAttribute("class").contains("deselected-all")){
                    Select.selector(attributeSelectAllButton).click();
                    for (WebElement checkbox : Select.multiSelector(attributeRow, relative_generalCheckbox)){
                        softAssert.assertTrue(checkbox.isSelected(), "The attribute <" + checkbox.getAttribute("id") + "> is not selected");
                    }
                    if (isSoftAssertLocal){
                        softAssert.assertAll("Errors on selecting all Attributes");
                    }
                }
            } else {
                attrId = attr.getKey().attributeName();
                attrElement = Select.selectParentByAttributeContains(attributeRow, relative_generalCheckbox, "value", attrId);
                if (attr.getKey() != attributesEnum.DATE && attr.getValue() != Select.selector(attrElement, relative_generalCheckbox).isSelected()){
                    Select.selector(attrElement, attrMetricCheckboxLabel).click();
                    if (attr.getValue()){
                        Wait.waitForClickable(Select.selector(attrElement, attrMetricWrapButton));
                    } else {
                        Wait.waitForNotVisible(Select.selector(attrElement, attrMetricWrapButton));
                    }
                }
            }
        }
    }

    @Step("Select Attribute")
    public void selectAttribute(ReportDO reportData){
        WebElement attrElement;
        String attrId;
        for (Entry<attributesEnum, Boolean> attr : reportData.getAttrMap().entrySet()){
            if (attr.getKey().equals(attributesEnum.ALL)){
                if (attr.getValue() != Select.selector(attributeSelectAllButton).getAttribute("class").contains("deselected-all")){
                    Select.selector(attributeSelectAllButton).click();
                    for (WebElement checkbox : Select.multiSelector(attributeRow, relative_generalCheckbox)){
                        softAssert.assertTrue(checkbox.isSelected(), "The attribute <" + checkbox.getAttribute("id") + "> is not selected");
                    }
                    if (isSoftAssertLocal){
                        softAssert.assertAll("Errors on selecting all Attributes");
                    }
                }
            } else {
                attrId = attr.getKey().attributeName(reportData.getReportType());
                attrElement = Select.selectParentByAttributeContains(attributeRow, relative_generalCheckbox, "value", attrId);
                if (attr.getKey() != attributesEnum.DATE && attr.getValue() != Select.selector(attrElement, relative_generalCheckbox).isSelected()){
                    Select.selector(attrElement, attrMetricCheckboxLabel).click();
                    if (attr.getValue()){
                        Wait.waitForClickable(Select.selector(attrElement, attrMetricWrapButton));
                    } else {
                        Wait.waitForNotVisible(Select.selector(attrElement, attrMetricWrapButton));
                    }
                }
            }
        }
    }

    @Step("Set attribute filter in generated report")
    public void setAttributeFilterBuilder(attributesEnum attribute, String filterValue, reportTypeEnum reportType){
        WebElement attrElement = Select.selectParentByAttributeContains(attributeRow, relative_generalCheckbox, "value", attribute.attributeName(reportType));
        switch (attribute){
            case SIZE, DOMAIN_BUNDLE, PUBLISHER_ID, INVENTORY_SSP_ID, INVENTORY_DSP_ID, CRID ->
                    enterInInput(Select.selector(attrElement, attributeFilterInput), filterValue);
            default -> {
                Select.selector(attrElement, attributeDropdownButton).click();
                clickDropdownMultiselectOption(Select.selector(attrElement, relative_generalSelect), filterValue, true);
                closeMultiselectList(Select.selector(attrElement, relative_generalSelect));
            }
        }
    }

    @Step("Assert attribute filter options")
    public void assertAttributeFilter(attributesEnum attribute, List<String> filterOptions){
        WebElement attrElement = Select.selectParentByAttributeContains(attributeRow, relative_generalCheckbox, "value", attribute.attributeName()), attrSelect = Select.selector(attrElement, relative_generalSelect);
        List<String> availableOptions = new ArrayList<>();
        for (WebElement option : Select.multiSelector(attrSelect, relative_multiselectAvailableOption)){
            availableOptions.add(option.getAttribute("title"));
        }
        hardAssert.assertEqualsNoOrder(availableOptions.toArray(), filterOptions.toArray(), "Attribute <" + attribute.attributeName() + "> filter options are incorrect. Expected: " + filterOptions + ", Actual: " + availableOptions);
    }

    @Step("Select All Metrics")
    public void clickSelectAllMetrics(boolean toSelect){
        if (toSelect ^ Select.selector(metricSelectAllButton).getAttribute("class").contains("deselected-all")){
            Select.selector(metricSelectAllButton).click();
            for (WebElement checkbox : Select.multiSelector(metricRow, relative_generalCheckbox)){
                softAssert.assertTrue(checkbox.isSelected(), "The metric <" + checkbox.getAttribute("id") + "> is not selected");
            }
            if (isSoftAssertLocal){
                softAssert.assertAll("Errors on selecting all Attributes");
            }
        }
    }

    @Step("Select Metric")
    public void selectMetric(Map<metricsEnum, Boolean> metrics){
        WebElement metricElement;
        String metricId;
        for (Entry<metricsEnum, Boolean> metric : metrics.entrySet()){
            if (metric.getKey().equals(metricsEnum.ALL)){
                if (metric.getValue() != Select.selector(metricSelectAllButton).getAttribute("class").contains("deselected-all")){
                    Select.selector(metricSelectAllButton).click();
                    for (WebElement checkbox : Select.multiSelector(metricRow, relative_generalCheckbox)){
                        softAssert.assertTrue(checkbox.isSelected(), "The metric <" + checkbox.getAttribute("id") + "> is not selected");
                    }
                    if (isSoftAssertLocal){
                        softAssert.assertAll("Errors on selecting all Metrics");
                    }
                }
            } else {
                metricId = metric.getKey().attributeName();
                metricElement = Select.selectParentByAttributeExact(metricRow, relative_generalCheckbox, "value", metricId);
                if (metric.getValue() != Select.selector(metricElement, relative_generalCheckbox).isSelected()){
                    Select.selector(metricElement, attrMetricCheckboxLabel).click();
                    if (metric.getValue()){
                        Wait.waitForClickable(Select.selector(metricElement, attrMetricWrapButton));
                    } else {
                        Wait.waitForNotVisible(Select.selector(metricElement, attrMetricWrapButton));
                    }
                }
            }
        }
    }

    //</editor-fold>

    //<editor-fold desc="Generated report - Actions">
    static final By
            backButton = By.xpath("//div/a[@id='back_to_report']"),
            generateNewButton = By.xpath("//a[@id='generateNewReport']"),
            shareButton = By.xpath("//span[@id='shareReport']"),
            updateButton = By.xpath("//a[@id='showReport']"),
            exportButton = By.xpath("//a[@id='exportReport']"),
            shareModalLink = By.xpath("//div[@role='dialog']//div[@id='swal2-content']/pre"),
            aggregatedByFilterSelect = By.xpath("//select[@id='groupByDown']"),
            timezoneFilterSelect = By.xpath("//select[@id='timeZone']"),
            rowsNumberFilterSelect = By.xpath("//select[@id='limit']"),
            reportFilterAttributes = By.xpath("//div[@id='attributeChips']//div[@data-attribute]"),
            reportFilterMetrics = By.xpath("//div[@id='metricChips']//div[@data-metric and not(contains(@class, 'hide'))]"),
            reportFilterMetricsDeleteButton = By.xpath(".//div[@class='icon-filter-content']"),
            reportFilterMetricsAddButton = By.xpath("//div[@data-activates='metric-dropdown']/span"),
            reportFilterMetricsAddListOption = By.xpath("//ul[@id='metric-dropdown']//li[not(@class='hide')]"),
            reportFilterName = By.xpath("./div[@class='title-filter-content']"),
            reportGraphLoading = By.xpath("//div[@id='chart']//div[contains(@class, 'highcharts-loading')]"),
            reportGraphDateTime = By.xpath("//div[@id='chart']//*[local-name()='g'][contains(@class, 'xaxis-labels')]/*[local-name()='text']"),
            reportGraphMetricName = By.xpath("//div[@id='chart']//*[local-name()='g'][contains(@class,'highcharts-legend-item')]"),
            reportGeneratedTable = By.xpath("//div[@id='reports']//table"),
            reportTableHeadCell = By.xpath("//div[@id='reports']//table//thead//th"),
            reportTableRow = By.xpath("//div[@id='reports']//table//tbody/tr"),
            reportTableLoading = By.xpath("//div[@class='table-data']//div[@class='loader center']");

    @Step("Click on 'Export CSV' button")
    public File clickExportCSV(reportTypeEnum type){
        String expectedFileName = switch (type){
            case PLATFORM_STATISTICS, USER_SSP, USER_DSP -> "ad_exchange_export.csv";
            case PLATFORM_PUBLISHERS, USER_PUBLISHER -> "export_general.csv";
        };
        Select.selector(exportButton).click();
        return FileUtil.getDownloadedFile(expectedFileName, 20);
    }

    @Step("Click on Back button in generated report")
    public void clickBackToBuilder(){
        Select.selector(backButton).click();
        Wait.waitForClickable(generateReportButton);
    }

    @Step("Click on Generate new report button")
    public void clickGenerateNewReport(){
        Select.selector(generateNewButton).click();
        Wait.waitForClickable(generateReportButton);
    }

    @Step("Click on Share report")
    public void clickShareReport(){
        Select.selector(shareButton).click();
        Wait.waitForClickable(modalConfirmButton);
    }

    @Step("Get report share link")
    public String getReportShareLink(){
        return Select.selector(shareModalLink).getText();
    }

    @Step("Click on Update report")
    public void clickUpdateReport(){
        Select.selector(updateButton).click();
        Wait.waitForVisibility(reportGraphLoading);
        Wait.attributeNotContains(reportGeneratedTable, "class", "disabled");
        Wait.waitForNotVisible(reportGraphLoading, 120);
        Wait.waitForNotVisible(reportTableLoading, 120);
    }

    @Step("Select Aggregated By in generated report")
    public void selectAggregatedByReport(aggregateByEnum aggregatedBy){
        selectSingleSelectDropdownOption(aggregatedByFilterSelect, aggregatedBy.publicName());
    }

    @Step("Select timezone in generated report")
    public void selectTimezoneReport(String timezone){
        selectSingleSelectDropdownOptionByName(timeZoneSelect, timezone);
    }

    @Step("Set attribute filter in generated report")
    public void setAttributeFilterGeneratedReport(Map<attributesEnum, String> filterMap, reportTypeEnum type){
        for (Entry<attributesEnum, String> filter : filterMap.entrySet()){
            WebElement attrElement = Select.selectByAttributeIgnoreCase(reportFilterAttributes, "data-attribute", filter.getKey().attributeName(type));
            switch (filter.getKey()){
                case SIZE, DOMAIN_BUNDLE, PUBLISHER_ID, INVENTORY_SSP_ID, INVENTORY_DSP_ID, CRID ->
                        enterInInput(Select.selector(attrElement, relative_generalInput), filter.getValue());
                default -> {
                    WebElement attributeSelect = Select.selector(attrElement, relative_generalSelect);
                    Select.selector(attributeSelect, relative_selectLabel).click();
                    clickDropdownMultiselectOption(attributeSelect, filter.getValue(), true);
                    closeMultiselectList(attrElement);
                }
            }
        }
    }

    @Step("Select Number of rows in report")
    public void selectNumberOfRows(int number){
        selectSingleSelectDropdownOptionByName(rowsNumberFilterSelect, String.valueOf(number));
    }

    @Step("Add metric on report page")
    public void addMetric(metricsEnum metric){
        WebElement metricElement = Select.selectByAttributeExact(reportFilterMetrics, "data-metric", metric.attributeName());
        selectSingleSelectDropdownOption(Select.selector(metricElement, relative_generalSelect), metric.publicName());
        Wait.waitForClickable(Select.selectByAttributeExact(reportFilterMetrics, "data-metric", metric.attributeName()));
    }

    @Step("Remove metric from report page")
    public void deleteMetric(metricsEnum metric){
        WebElement metricElement = Select.selectByAttributeExact(reportFilterMetrics, "data-metric", metric.attributeName());
        Select.selector(metricElement, reportFilterMetricsDeleteButton).click();
        Wait.waitForNotVisible(metricElement);
    }

    @Step("Get report table value")
    public String getReportTableValue(String columnAttribute, int rowNumber){
        List<WebElement> rows = Select.selectListByAttributeContains(reportTableRow, relative_generalTableBodyCell, "data-field", columnAttribute);
        hardAssert.assertTrue(rows.size() > rowNumber, "Row number <" + rowNumber + "> is out of range");
        return rows.get(rowNumber).getText();
    }

    @Step("Click on column to sort")
    public void clickColumnSorting(String columnAttribute){
        Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", columnAttribute).click();
        Wait.attributeNotContains(reportGeneratedTable, "class", "disabled");
    }

    @Step("Click on endpoint name link")
    public String clickEndpointNameLink(attributesEnum columnToClick, int rowNumber, reportTypeEnum type){
        List<WebElement> rows = Select.selectListByAttributeContains(reportTableRow, relative_generalTableBodyCell, "data-field", columnToClick.attributeName(type));
        rows.removeIf(row -> row.getText().equals("Native Supply #-1"));
        String ret = rows.get(rowNumber).getText();
        Select.selector(rows.get(rowNumber), relative_generalLink).click();
        BrowserUtil.switchTab();
        Wait.waitForClickable(logoutButton);
        return ret;
    }

    //</editor-fold>

    //<editor-fold desc="Generated report - Assertions">
    @Step("Assert deleted metric in the list")
    public void assertReportAddMetricList(metricsEnum metric){
        Select.selector(reportFilterMetricsAddButton).click();
        Wait.sleep(1000); //wait for animation
        List<String> metricsList = new ArrayList<>();
        for (WebElement option : Select.multiSelector(reportFilterMetricsAddListOption)){
            metricsList.add(option.getText());
        }
        WebElement dropdownLine = Select.selector(reportFilterMetricsAddButton), dropdownOption = Select.selector(reportFilterMetricsAddListOption);
        int height = dropdownLine.getSize().height, adjustY = 1 + (height / 2);
        int width = dropdownLine.getSize().width, adjustX = 1 + width;
        Select.actionClickWithAdjust(reportFilterMetricsAddButton, -adjustX, -adjustY);
        Wait.waitForNotVisible(dropdownOption);
        softAssert.assertTrue(metricsList.contains(metric.publicName()), "Metric <" + metric.publicName() + "> is not in the list of deleted metrics");
    }

    @Step("Assert opened endpoint page")
    public void assertEndpointFromReport(attributesEnum type, String attributeString){
        String endpointName = attributeString.substring(0, attributeString.indexOf("#")).trim(), endpointId = attributeString.substring(attributeString.indexOf("#") + 1).trim();
        switch (type){
            case SSP_ENDPOINT:
                SupplyPO ssp = new SupplyPO();
                ssp.assertEndpointName(endpointName);
                ssp.assertEndpointId(Integer.valueOf(endpointId));
                break;
            case DSP_ENDPOINT:
                DemandPO dsp = new DemandPO();
                dsp.assertEndpointName(endpointName);
                dsp.assertEndpointId(Integer.valueOf(endpointId));
                break;
            case INVENTORY_PUBLISHER:
                //softAssert.assertTrue(Utility.assertPresent(demandName), "Demand page is not opened");
                break;
            default:
                hardAssert.fail("Incorrect endpoint type");
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on endpoint/inventory page from report");
        }
    }

    @Step("Assert attributes and metric filters on report settings")
    public void assertReportResultFilters(ReportDO reportData){
        String attributeId, attributeName, metricId, metricName;
        assertCalendarRange(reportData.getDateFrom(), reportData.getDateTo(), "Filter:");
        softAssert.assertEquals(Select.selector(aggregatedByFilterSelect, relative_selectLabel).getText(), reportData.getAggregateBy().publicName(), "Filter: Aggregated by is incorrect");
        softAssert.assertEquals(Select.selector(timezoneFilterSelect, relative_selectLabel).getText(), reportData.getTimeZone(), "Filter: Timezone is incorrect");
        softAssert.assertEquals(Select.selector(rowsNumberFilterSelect, relative_selectLabel).getText(), reportData.getNumberOfRows().toString(), "Filter: Rows number is incorrect");
        for (Entry<attributesEnum, Boolean> attribute : reportData.getAttrMap().entrySet()){
            attributeId = attribute.getKey().attributeName(reportData.getReportType());
            attributeName = attribute.getKey().publicName(reportData.getReportType());
            if (attribute.getValue()){
                softAssert.assertTrue(AssertUtil.assertPresent(Select.selectByAttributeIgnoreCase(reportFilterAttributes, "data-attribute", attributeId)), "Filter: Attribute <" + attributeId + "> IS NOT displayed");
                softAssert.assertEquals(Select.selector(Select.selectByAttributeIgnoreCase(reportFilterAttributes, "data-attribute", attributeId), reportFilterName).getText(), attributeName, "Filter: Attribute <" + attributeId + "> NAME is incorrect");
            } else {
                softAssert.assertTrue(AssertUtil.assertNotPresent(reportFilterAttributes, "data-attribute", attributeId), "Filter: Attribute <" + attributeId + "> IS displayed");
            }
        }
        for (Entry<metricsEnum, Boolean> metric : reportData.getMetricsMap().entrySet()){
            metricId = metric.getKey().attributeName();
            metricName = metric.getKey().publicName(reportData.getReportType());
            if (metric.getValue()){
                softAssert.assertTrue(AssertUtil.assertPresent(Select.selectByAttributeIgnoreCase(reportFilterMetrics, "data-metric", metricId)), "Filter: Metric <" + metricId + "> IS NOT displayed");
                softAssert.assertEquals(Select.selector(Select.selectByAttributeIgnoreCase(reportFilterMetrics, "data-metric", metricId), reportFilterName).getText(), metricName, "Filter: Metric <" + metricId + "> NAME is incorrect");
            } else {
                softAssert.assertTrue(AssertUtil.assertNotPresent(reportFilterMetrics, "data-metric", metricId), "Filter: Metric <" + metricId + "> IS displayed");
            }
        }
    }

    @Step("Assert graph time span")
    public void assertGraphTimeScale(LocalDateTime graphStart, LocalDateTime graphEnd){
        String graphStartString = basicUtility.getDateTimeFormatted(graphStart, "LLL, dd"), graphEndString = basicUtility.getDateTimeFormatted(graphEnd, "LLL, dd");
        List<WebElement> graphDays = Select.multiSelector(reportGraphDateTime);
        softAssert.assertEquals(Select.multiSelector(reportGraphDateTime).get(0).getText(), graphStartString, "Graph: Start date is incorrect");
        softAssert.assertEquals(Select.multiSelector(reportGraphDateTime).get(graphDays.size() - 1).getText(), graphEndString, "Graph: End date is incorrect");
    }

    @Step("Assert attributes and metrics on graph")
    public void assertReportGraphLegend(ReportDO reportData){
        List<String> expectedGraphParams = new ArrayList<>(), actualGraphParam = new ArrayList<>();
        for (Entry<metricsEnum, Boolean> metric : reportData.getMetricsMap().entrySet()){
            if (metric.getValue()){
                expectedGraphParams.add(metric.getKey().publicName(reportData.getReportType()));
            }
        }
        for (WebElement graphParam : Select.multiSelector(reportGraphMetricName)){
            actualGraphParam.add(graphParam.getText());
        }
        actualGraphParam.sort(String::compareToIgnoreCase);
        expectedGraphParams.sort(String::compareToIgnoreCase);
        softAssert.assertEquals(actualGraphParam, expectedGraphParams, "Graph legend: metrics don't match. Expected <" + expectedGraphParams + ">, Actual <" + actualGraphParam + ">");
    }

    @Step("Assert report graph")
    public void assertReportGraph(ReportDO reportData){
        assertGraphTimeScale(reportData.getDateFrom(), reportData.getDateTo());
        assertReportGraphLegend(reportData);
        assertGraphTimeScale(reportData.getDateFrom(), reportData.getDateTo());
        assertReportGraphLegend(reportData);
    }

    @Step("Assert report table for attribute and metric columns")
    public void assertReportTableColumns(ReportDO reportData){
        String attributeId, attributeName, metricId, metricName;
        if (reportData.getAggregateBy() != aggregateByEnum.TOTAL){
            softAssert.assertTrue(AssertUtil.assertPresent(Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", "date_export")), "Table: Attribute <Date> IS NOT displayed");
            softAssert.assertEquals(Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", "date_export").getText(), attributesEnum.DATE.publicName(), "Table: Attribute <Date> name is incorrect");
        }
        for (Entry<attributesEnum, Boolean> attribute : reportData.getAttrMap().entrySet()){
            attributeId = attribute.getKey().attributeName(reportData.getReportType());
            attributeName = attribute.getKey().publicName(reportData.getReportType());
            if (attribute.getValue()){
                softAssert.assertTrue(AssertUtil.assertPresent(Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", attributeId)), "Table: Attribute <" + attributeId + "> IS NOT displayed");
                softAssert.assertEquals(Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", attributeId).getText(), attributeName, "Table: Attribute <" + attributeId + "> name is incorrect");
            } else {
                softAssert.assertTrue(AssertUtil.assertNotPresent(reportTableHeadCell, "data-field", attributeId), "Table: Attribute <" + attributeId + "> IS displayed");
            }
        }
        for (Entry<metricsEnum, Boolean> metric : reportData.getMetricsMap().entrySet()){
            metricId = metric.getKey().attributeName();
            metricName = metric.getKey().publicName(reportData.getReportType());
            if (metric.getValue()){
                softAssert.assertTrue(AssertUtil.assertPresent(Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", metricId)), "Table: Metric <" + metricId + "> IS NOT displayed");
                softAssert.assertEquals(Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", metricId).getText(), metricName, "Table: Metric <" + metricId + "> name is incorrect");
            } else {
                boolean notPresent = AssertUtil.assertNotPresent(reportTableHeadCell, "data-field", metricId), notVisible = true;
                if (!notPresent){
                    notVisible = Select.selectByAttributeIgnoreCase(reportTableHeadCell, "data-field", metricId).getAttribute("class").contains("hide");
                }
                softAssert.assertTrue(notPresent || notVisible, "Table: Metric <" + metricId + "> IS displayed");
            }
        }
        softAssert.assertTrue(AssertUtil.assertPresent(reportTableRow, relative_generalTableBodyCell), "Table: There is no data");
    }

    @Step("Assert report table data in column")
    public void assertReportTableValue(String attrOrMetric, int rowNumber, String expectedValue){
        List<WebElement> rows = Select.selectListByAttributeContains(reportTableRow, relative_generalTableBodyCell, "data-field", attrOrMetric);
        hardAssert.assertFalse(rows.isEmpty(), "There are no rows in the table");
        softAssert.assertEquals(rows.get(rowNumber).getText(), expectedValue, "Table: Value in column <" + attrOrMetric + "> is incorrect");
    }

    @Step("Assert report table data in column")
    public void assertReportTableValue(String attrOrMetric, String expectedValue){
        int rowCnt = 1;
        List<WebElement> rows = Select.selectListByAttributeContains(reportTableRow, relative_generalTableBodyCell, "data-field", attrOrMetric);
        for (WebElement row : rows){
            softAssert.assertEquals(row.getText(), expectedValue, "Table: Value in column <" + attrOrMetric + "> in row [" + rowCnt + "] is incorrect");
            rowCnt++;
        }
    }

    @Step("Assert table rows count")
    public void assertReportTableRowCount(int expectedRows){
        if (AssertUtil.assertPresent(reportTableRow)) {
            int actualCount = Select.multiSelector(reportTableRow).size();
            Map<String, Integer> pagingMap = getPagingEntriesCount();
            softAssert.assertTrue(actualCount <= expectedRows, "Table: Rows count is incorrect. Expected <" + expectedRows + ">, Actual <" + actualCount + ">");
            softAssert.assertEquals(actualCount, pagingMap.get("to").intValue(), "Table: Rows paging count [TO] is incorrect");
        }
    }

    @Step("Assert column names of exported report file")
    public void assertReportExportColumns(ReportDO reportData, File reportFile){
        List<String> columnsExpected = new ArrayList<>(){{
            add(attributesEnum.DATE.publicName(reportData.getReportType()));    //Date is default column
        }};
        for (Entry<attributesEnum, Boolean> attrEntry : reportData.getAttrMap().entrySet()){
            if (attrEntry.getValue()){
                columnsExpected.add(attrEntry.getKey().publicName(reportData.getReportType()));
            }
        }
        for (Entry<metricsEnum, Boolean> metricEntry : reportData.getMetricsMap().entrySet()){
            if (metricEntry.getValue()){
                columnsExpected.add(metricEntry.getKey().publicName(reportData.getReportType()));
            }
        }
        MappingIterator<Map<String, Object>> iterator = FileUtil.readCsvFile(reportFile);    //TODO change the complicated reader with a simpler one (SSP>>assertFileExportCsv)
        List<String> columnsActual = new ArrayList<>(iterator.next().keySet());
        columnsActual.sort(String::compareToIgnoreCase);
        columnsExpected.sort(String::compareToIgnoreCase);
        softAssert.assertEquals(columnsActual, columnsExpected, "Exported Report columns are different. \nActual <" + columnsActual + "> \nExpected <" + columnsExpected + ">");
        if (columnsActual.size() > columnsExpected.size()){
            columnsActual.removeAll(columnsExpected);
            softAssert.fail("Columns, that are not expected, but present in file <" + columnsActual + ">");
        } else if (columnsActual.size() < columnsExpected.size()){
            columnsExpected.removeAll(columnsActual);
            softAssert.fail("Columns, that are expected, but not present in file <" + columnsExpected + ">");
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on exported report file <" + SystemUtility.getPathCanonical(reportFile) + ">");
        }
    }

    @Step("Assert generated report page")
    public void assertDashboardReport(ReportDO reportData){
        assertReportResultFilters(reportData);
        assertReportTableColumns(reportData);
        assertReportTableRowCount(reportData.getNumberOfRows());
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on dashboard report page");
        }
    }

    @Step("Assert generated report page")
    public void assertDashboardInventoryPlacementReport(ReportDO reportData, boolean dataPresent){
        assertReportResultFilters(reportData);
        if (dataPresent){
            assertReportTableColumns(reportData);
            assertReportTableRowCount(reportData.getNumberOfRows());
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on dashboard report page");
        }
    }

    @Step("Assert generated report page")
    public void assertGeneratedReport(ReportDO reportData){
        assertReportResultFilters(reportData);
        if (AssertUtil.assertPresent(reportGraphMetricName)) {
            assertReportGraph(reportData);
        } else {
            softAssert.fail("Graph is not displayed");
        }
        if (AssertUtil.assertPresent(reportGeneratedTable)) {
            assertReportTableColumns(reportData);
            assertReportTableRowCount(reportData.getNumberOfRows());
        } else {
            softAssert.fail("Table is not displayed");
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on generated report page");
        }
    }

    @Step("Assert shared report page")
    public void assertSharedReport(ReportDO reportData){
        softAssert.assertTrue(AssertUtil.assertNotPresent(backButton), "Back button is displayed");
        softAssert.assertTrue(AssertUtil.assertNotPresent(shareButton), "Share button is displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(generateNewButton), "Generate new report button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(updateButton), "Update report button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(exportButton), "Export button is not displayed");
        assertReportResultFilters(reportData);
        assertReportGraphLegend(reportData);
        assertReportTableColumns(reportData);
        if (isSoftAssertLocal){
            softAssert.assertAll("Errors on generated report page");
        }
    }

    //</editor-fold>

}
