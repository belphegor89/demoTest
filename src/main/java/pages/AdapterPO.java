package pages;

import com.fasterxml.jackson.databind.MappingIterator;
import common.Selectors;
import common.SoftAssertCustom;
import common.UserSettings;
import common.Waits;
import common.utils.AssertUtility;
import common.utils.BasicUtility;
import common.utils.FileUtility;
import common.utils.SystemUtility;
import data.AdaptersEnums.adaptersTableColumns;
import data.CommonEnums.*;
import data.dataobject.AdapterDO;
import data.textstrings.messages.AdapterText;
import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static data.textstrings.messages.CommonText.MODAL_ERROR_HEADER;

public class AdapterPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final BasicUtility Utils = new BasicUtility();
    private final FileUtility FileUtil = new FileUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);

    public AdapterPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public AdapterPO(){
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open API & Prebid Demand page")
    public void gotoAdaptersSection(){
        openAdminMenu();
        clickAdxActivityWrap(true);
        Select.selector(adaptersLink).click();
        Wait.waitForVisibility(searchInput);
        Wait.attributeNotContains(adaptersTable, "class", "hide");
    }

    //<editor-fold desc="Adapters list">
    private static final By searchInput = By.xpath("//input[@data-field='name']"),
            adaptersTab = By.xpath("//li[@class='tab']/a"),
            adaptersTable = By.xpath("//div[@id='adapters-table']//table"),
            adaptersTableRow = By.xpath("//div[@id='adapters-table']//table//tbody/tr"),
            relative_expandedSettingsRow = By.xpath("./following-sibling::tr[contains(@class, 'settings')]"),
            relative_expandedSettingTableRow = By.xpath("./following-sibling::tr[contains(@class, 'settings')]//tbody/tr"),
            adaptersAddSettingButton = By.xpath(".//a[contains(@href, 'create')]"),
            adaptersEditSettingButton = By.xpath(".//a[not(contains(@href, 'delete'))]"),
            adaptersDeleteSettingButton = By.xpath(".//a[contains(@href, 'delete')]");

    @Step("Click adapter type tab in table")
    public void clickAdapterTab(endpointTypeEnum adapterType){
        String type = adapterType.equals(endpointTypeEnum.RTB) ? "api" : "prebid";
        Select.selectByAttributeExact(adaptersTab, "data-id", type).click();
        Wait.sleep(500);
        Wait.attributeNotContains(adaptersTable, "class", "hide");
    }

    @Step("Get Adapter row")
    public WebElement getAdapterRow(String adapterName){
        WebElement adapterRow = null;
        for (WebElement row : Select.multiSelector(adaptersTableRow)){
            if (getTableCell(row, adaptersTableColumns.NAME.attributeName()).getText().equals(adapterName)){
                adapterRow = row;
                break;
            }
        }
        hardAssert.assertNotNull(adapterRow, "Adapter with name " + adapterName + " not found");
        return adapterRow;
    }

    @Step("Look up for adapter")
    public void lookForAdapter(String adapterName){
        for (int i = 0; i < getPagingCount(); i++){
            try{
                getAdapterRow(adapterName);
                break;
            } catch (AssertionError e){
                pagingActionSelect(pagingActionsTypes.FORWARD, null);
                System.err.println("Adapter with name " + adapterName + " not found on page " + i);
            }
        }
    }

    @Step("Click 'Add setting' for adapter")
    public void clickAddSetting(String adapterName){
        WebElement adapter = getAdapterRow(adapterName);
        Select.selector(adapter, adaptersAddSettingButton).click();
        Wait.waitForVisibility(settingNameInput);
    }

    @Step("Click on adapters wrap button")
    public void clickAdapterWrap(String adapterName, boolean toOpen){
        WebElement adapter = getAdapterRow(adapterName), wrapCell = getTableCell(adapter, adaptersTableColumns.EXPAND.attributeName());
        if (toOpen ^ adapter.getAttribute("class").equals("expanded")){
            wrapCell.click();
        }
        if (toOpen){
            WebElement settingsTable = Select.selector(adapter, relative_expandedSettingsRow);
            Wait.attributeContains(Select.selector(settingsTable, preloaderTable), "class", "hide");
        }
    }

    @Step("Click on Edit button on setting")
    public void clickEditAdapterSetting(AdapterDO settingData){
        WebElement setting = getAdapterSettingsRow(settingData.getAdapterName(), settingData.getSettingName());
        Select.selector(setting, adaptersEditSettingButton).click();
        Wait.waitForVisibility(settingNameInput);
    }

    @Step("Click on Delete button on setting")
    public void clickDeleteAdapterSetting(AdapterDO settingData){
        WebElement setting = getAdapterSettingsRow(settingData.getAdapterName(), settingData.getSettingName());
        Select.selector(setting, adaptersDeleteSettingButton).click();
        Wait.waitForClickable(modalConfirmButton);
        assertModal(softAssert, AdapterText.MODAL_CONFIRM_HEADER, AdapterText.modalDeleteText, "Delete adapter setting");
    }

    @Step("Click {confirm} button in deletion confirm modal")
    public void confirmDeleteSetting(AdapterDO settingData, boolean confirm){
        WebElement setting = getAdapterSettingsRow(settingData.getAdapterName(), settingData.getSettingName());
        if (confirm){
            try{
                modalClickConfirm();
                Wait.waitForStaleness(setting);
                setting = getAdapterSettingsRow(settingData.getAdapterName(), settingData.getSettingName());
            } catch (AssertionError as){
                setting = null;
                System.err.println("Setting was deleted");
            }
            softAssert.assertNull(setting, "Setting was not deleted");
        } else {
            modalClickCancel();
        }
    }

    @Step("Click {confirm} button in deletion confirm modal, when setting connected to endpoint")
    public void confirmDeleteSettingBlocked(Integer endpointId){
        Select.selector(modalConfirmButton).click();
        Wait.waitForClickable(modalConfirmButton);
        assertModal(softAssert, MODAL_ERROR_HEADER, AdapterText.modalDeleteBlockedText.replace("${epId}", endpointId.toString()), "Delete adapter setting");
    }

    @Step("Get adapter settings count")
    public int getAdapterSettingsCount(String adapterName){
        WebElement adapter = getAdapterRow(adapterName), counter = getTableCell(adapter, adaptersTableColumns.SETTING_COUNT.attributeName());
        return counter.getText().equals("-") ? 0 : Integer.parseInt(counter.getText());
    }

    @Step("Look up for adapter setting")
    public void lookForAdapterSetting(String adapterName, String settingName){
        WebElement settingsTable = Select.selector(getAdapterRow(adapterName), relative_expandedSettingsRow);
        try{
            getAdapterSettingsRow(adapterName, settingName);
        } catch (AssertionError e){
            pagingActionSelect(settingsTable, pagingActionsTypes.FORWARD, null);
        }
    }

    @Step("Get adapter setting row")
    public WebElement getAdapterSettingsRow(String adapterName, String settingName){
        WebElement adapter = getAdapterRow(adapterName), setting = null;
        for (WebElement row : Select.multiSelector(adapter, relative_expandedSettingTableRow)){
            if (getTableCell(row, adaptersTableColumns.NAME.attributeName()).getText().equals(settingName)){
                setting = row;
                break;
            }
        }
        hardAssert.assertNotNull(setting, "Setting with name " + adapterName + " not found in adapter " + adapterName);
        return setting;
    }

    @Step("Assert adapter row in table")
    public void assertAdapterRow(String adapterName, Integer settingsCount){
        WebElement adapter = getAdapterRow(adapterName);
        String countExpected = settingsCount == 0 ? "-" : settingsCount.toString();
        softAssert.assertEquals(getTableCell(adapter, adaptersTableColumns.SETTING_COUNT.attributeName()).getText(), countExpected, "Settings count in adapter row is incorrect");
    }

    @Step("Assert adapter setting row in table")
    public void assertAdapterSettingRow(AdapterDO settingData){
        WebElement settingRow = getAdapterSettingsRow(settingData.getAdapterName(), settingData.getSettingName());
        String typeExpected = settingData.getIsImport() ? "Import" : "Manual";
        softAssert.assertEquals(getTableCell(settingRow, adaptersTableColumns.SETTING_TYPE.attributeName()).getText(), typeExpected, "Type in setting row is incorrect");
        if (!settingData.getIsImport()){
            TreeSet<String> isoCountriesExpected = new TreeSet<>(),
                    isoCountriesActual = new TreeSet<>(Arrays.asList(getTableCell(settingRow, adaptersTableColumns.COUNTRIES.attributeName()).getText().split(", "))),
                    trafficExpected = new TreeSet<>(Arrays.asList(getExpectedMultiselectText(settingData.getTrafficNamesMap(), "All", 5, 5).toLowerCase().split(", "))),
                    trafficActual = new TreeSet<>(Arrays.asList(getTableCell(settingRow, adaptersTableColumns.TRAFFIC.attributeName()).getText().toLowerCase().split(", "))),
                    adFormatsExpected = new TreeSet<>(Arrays.asList(getExpectedMultiselectText(settingData.getAdFormatNamesMap(), "All", 5, 5).toLowerCase().split(", "))),
                    adFormatsActual = new TreeSet<>(Arrays.asList(getTableCell(settingRow, adaptersTableColumns.AD_FORMATS.attributeName()).getText().toLowerCase().split(", "))),
                    osExpected = new TreeSet<>(Arrays.asList(getExpectedMultiselectText(settingData.getOsMap(), "All", 100).toLowerCase().split(", "))),
                    osActual = new TreeSet<>(Arrays.asList(getTableCell(settingRow, adaptersTableColumns.OS.attributeName()).getText().toLowerCase().split(", "))),
                    sizesExpected = new TreeSet<>(),
                    sizesActual = new TreeSet<>(Arrays.asList(getTableCell(settingRow, adaptersTableColumns.SIZES.attributeName()).getText().split(", ")));
            if (settingData.getGeoList().isEmpty()){
                isoCountriesExpected.add("All");
            } else {
                for (AdapterDO.GeoSetting geo : settingData.getGeoList()){
                    for (Map.Entry<countriesEnum, Boolean> country : geo.getCountriesEnumMap().entrySet()){
                        isoCountriesExpected.add(country.getKey().getIso3());
                    }
                }
            }
            if (settingData.getAllSizes()){
                sizesExpected.add("All");
            } else {
                for (AdapterDO.SizeSetting size : settingData.getSizesList()){
                    sizesExpected.add(size.getSize().getWidth() + "x" + size.getSize().getHeight());
                }
            }
            softAssert.assertEquals(osActual, osExpected, "OS in setting row is incorrect. Actual: " + osActual.stream().toList() + ". Expected: " + osExpected.stream().toList());
            softAssert.assertEquals(trafficActual, trafficExpected, "Traffic in setting row is incorrect. Actual: " + trafficActual.stream().toList() + ". Expected: " + trafficExpected.stream().toList());
            softAssert.assertEquals(adFormatsActual, adFormatsExpected, "Ad format in setting row is incorrect. Actual: " + adFormatsActual.stream().toList() + ". Expected: " + adFormatsExpected.stream().toList());
            softAssert.assertEquals(sizesActual, sizesExpected, "Sizes in setting row is incorrect. Actual: " + sizesActual.stream().toList() + ". Expected: " + sizesExpected.stream().toList());
            softAssert.assertEquals(isoCountriesActual, isoCountriesExpected, "Countries in setting row is incorrect. Actual: " + isoCountriesActual.stream().toList() + ". Expected: " + isoCountriesExpected.stream().toList());
        } else {
            softAssert.assertEquals(getTableCell(settingRow, adaptersTableColumns.OS.attributeName()).getText(), "-", "OS in setting row is incorrect");
            softAssert.assertEquals(getTableCell(settingRow, adaptersTableColumns.TRAFFIC.attributeName()).getText(), "-", "Traffic in setting row is incorrect");
            softAssert.assertEquals(getTableCell(settingRow, adaptersTableColumns.AD_FORMATS.attributeName()).getText(), "-", "Ad format in setting row is incorrect");
            softAssert.assertEquals(getTableCell(settingRow, adaptersTableColumns.SIZES.attributeName()).getText(), "-", "Sizes in setting row is incorrect");
            softAssert.assertEquals(getTableCell(settingRow, adaptersTableColumns.COUNTRIES.attributeName()).getText(), "-", "Countries in setting row is incorrect");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Adapter setting page">
    private static final By
            settingManualImportToggle = By.xpath("//input[@id='is_file']"),
            settingNameInput = By.xpath("//input[@id='name']"),
            settingCredentialsInput = By.xpath("//input[contains(@name, 'credentials')]"),
            settingCustomFieldInput = By.xpath("//input[contains(@name, 'fields')]"),
            settingAllSizesToggle = By.xpath("//input[@name='is_all_sizes']"),
            settingOsSelect = By.xpath("//select[@name='operation_systems[]']"),
            settingTrafficSelect = By.xpath("//select[@name='platforms[]']"),
            settingAdFormatSelect = By.xpath("//select[@name='traffics[]']"),
            settingCustomTagIdInput = By.xpath(""),
            addSizeButton = By.xpath("//a[@id='addSize']"),
            sizeRow = By.xpath("//div[@class='append-sizes']/div"),
            sizeSelect = By.xpath(".//select"),
            sizeWidthInput = By.xpath(".//input[contains(@name, 'width')]"),
            sizeHeightInput = By.xpath(".//input[contains(@name, 'height')]"),
            addGeoButton = By.xpath("//a[@id='addGeo']"),
            geoRow = By.xpath("//div[@class='geo-settings']/div"),
            geoAdFormatSelect = By.xpath(".//select[contains(@name, 'traffic')]"),
            geoCountriesSelect = By.xpath(".//select[contains(@name, 'country')]"),
            bundlesSection = By.xpath("//div[@id='bundles-disabled-list']"),
            recordsAddButton = By.xpath("//a[@id='add-bundles']"),
            recordsClearButton = By.xpath("//a[@id='clear-bundles']"),
            recordsExportButton = By.xpath("//a[@id='export-bundles']"),
            recordsImportButton = By.xpath("//label[@id='import-bundles']"),
            recordsImportInput = By.xpath("//input[@id='upload-bundles']"),
            recordsRecordLine = By.xpath("//div[@class='collection-item']"),
            recordsLoader = By.xpath("//div[@class='center-align']/div[@class='progress']"),
            importNewButton = By.xpath("//label[@for='fileImportConfig']"),
            importNewInput = By.xpath("//input[@id='fileImportConfig']"),
            importAdditionalButton = By.xpath("//label[@for='fileAdditionalImportConfig']"),
            importAdditionalInput = By.xpath("//input[@id='fileAdditionalImportConfig']"),
            exportListButton = By.xpath("//a[contains(@class, 'export')]"),
            exportExampleButton = By.xpath("//a[contains(@class, 'example')]");

    @Step("Click on Manual/Import toggle")
    public void clickManualImportToggle(boolean isImport){
        clickToggle(settingManualImportToggle, isImport);
        if (isImport){
            Wait.waitForClickable(exportExampleButton);
        } else {
            Wait.waitForClickable(addGeoButton);
        }
    }

    @Step("Setup adapter setting manually")
    public void setupSettingManual(AdapterDO data){
        inputSettingName(data.getSettingName());
        toggleAllSizes(data.getAllSizes());
        selectOs(data.getOsMap());
        selectTraffic(data.getTrafficMap());
        selectAdFormat(data.getAdFormatMap());
        if (!data.getAllSizes()){
            for (AdapterDO.SizeSetting size : data.getSizesList()){
                addSizeRow(1);
                setSize(0, size.getSize(), size.getWidth(), size.getHeight());
            }
        }
        for (AdapterDO.GeoSetting geo : data.getGeoList()){
            addGeoRow(1);
            setGeo(0, geo.getAdFormatList(), geo.getCountriesNamesMap());
        }
    }

    @Step("Input setting name")
    public void inputSettingName(String name){
        enterInInput(settingNameInput, name);
    }

    @Step("Click 'All sizes' toggle")
    public void toggleAllSizes(boolean isAllSizes){
        clickToggle(settingAllSizesToggle, isAllSizes);
        if (isAllSizes){
            softAssert.assertTrue(AssertUtil.assertNotPresent(addSizeButton), "Adapter setting creation: Sizes are displayed when 'All sizes' is ON");
        } else {
            softAssert.assertTrue(AssertUtil.assertPresent(addSizeButton), "Adapter setting creation: Sizes are displayed when 'All sizes' is OFF");
        }
    }

    @Step("Get random OS from list")
    public Map<String, Boolean> getRandomOsMap(int count){
        return getRandomActiveMultiselectOptionsAsMap(settingOsSelect, count);
    }

    @Step("Select Operating Systems")
    public void selectOs(Map<String, Boolean> osMap){
        selectMultiselectOptions(settingOsSelect, osMap);
    }

    @Step("Select Traffic types")
    public void selectTraffic(Map<trafficTypeEnum, Boolean> trafficMap){
        Map<String, Boolean> selectMap = new LinkedHashMap<>();
        for (trafficTypeEnum format : trafficMap.keySet()){
            if (format.equals((trafficTypeEnum.MOBILE_WEB)) || format.equals((trafficTypeEnum.APP))){
                selectMap.put(format.publicName().toLowerCase(), trafficMap.get(format));
            } else {
                selectMap.put(format.publicName(), trafficMap.get(format));
            }
        }
        selectMultiselectOptions(settingTrafficSelect, selectMap);
    }

    @Step("Select Ad formats")
    public void selectAdFormat(Map<adFormatPlacementTypeEnum, Boolean> formatsMap){
        Map<String, Boolean> selectMap = new LinkedHashMap<>();
        for (adFormatPlacementTypeEnum format : formatsMap.keySet()){
            selectMap.put(format.publicName().toLowerCase(), formatsMap.get(format));
        }
        selectMultiselectOptions(settingAdFormatSelect, selectMap);
    }

    @Step("Add size setting row")
    public void addSizeRow(int count){
        clickOptionRowAdd(sizeRow, addSizeButton, count);
    }

    @Step("Set size setting in row")
    public void setSize(int rowNumber, iabSizesEnum size, Integer width, Integer height){
        WebElement row = Select.multiSelector(sizeRow).get(rowNumber);
        selectSingleSelectDropdownOptionByName(Select.selector(row, relative_generalSelect), size.publicName());
        if (size.equals(iabSizesEnum.CUSTOM)){
            enterInInput(Select.selector(row, sizeWidthInput), width);
            enterInInput(Select.selector(row, sizeHeightInput), height);
        }
    }

    @Step("Add geo setting row")
    public void addGeoRow(int count){
        clickOptionRowAdd(geoRow, addGeoButton, count);
    }

    @Step("Get random countries from Geo list")
    public Map<String, Boolean> getRandomGeoCountries(int count){
        return getRandomActiveMultiselectOptionsAsMap(geoCountriesSelect, count);
    }

    @Step("Set geo setting in row")
    public void setGeo(int rowNumber, Map<adFormatPlacementTypeEnum, Boolean> formatMap, Map<String, Boolean> countriesMap){
        WebElement row = Select.multiSelector(geoRow).get(rowNumber);
        Map<String, Boolean> selectMap = new LinkedHashMap<>();
        for (adFormatPlacementTypeEnum format : formatMap.keySet()){
            selectMap.put(format.publicName().toLowerCase(), formatMap.get(format));
        }
        selectMultiselectOptions(Select.selector(row, geoAdFormatSelect), selectMap);
        selectMultiselectOptions(Select.selector(row, geoCountriesSelect), countriesMap);
    }

    @Step("Set custom fields in Adapter setting")
    public Map<String, String> setCustomFieldsRandom(){
        Map<String, String> retMap = new HashMap<>();
        if (AssertUtil.assertPresent(settingCredentialsInput)) {
            for (WebElement field : Select.multiSelector(settingCredentialsInput)){
                String value = RandomStringUtils.random(10, true, true);
                if (field.getAttribute("name").contains("email")){
                    value += "@test.ga";
                }
                enterInInput(field, value);
                retMap.put(field.getAttribute("name"), value);
            }
        }
        if (AssertUtil.assertPresent(settingCustomFieldInput)) {
            for (WebElement field : Select.multiSelector(settingCustomFieldInput)){
                String value = RandomStringUtils.random(15, true, true);
                enterInInput(field, value);
                retMap.put(field.getAttribute("name"), value);
            }
        }
        return retMap;
    }

    @Step("Click Save Button")
    public void clickSave(){
        clickSaveButton();
        Wait.waitForVisibility(confirmMessageToast);
        Wait.waitForNotVisible(confirmMessageToast);
    }

    @Step("Click Save&Exit Button")
    public void clickSaveExitButton(){
        Select.selector(saveExitButton).click();
        Wait.waitForVisibility(searchInput);
        Wait.attributeNotContains(adaptersTable, "class", "hide");
    }

    @Step("Assert there is no Bundles UI on settings creation")
    public void assertBundlesOnCreation(){
        softAssert.assertEquals(Select.selector(bundlesSection).getText(), AdapterText.bundlesOnSettingCreation, "No message on bundles section");
        softAssert.assertTrue(AssertUtil.assertNotPresent(recordsAddButton), "Bundles are displayed on settings creation");
    }

    @Step("Add records manually")
    public void addFilterRecordsManual(List<String> records){
        for (String record : records){
            Select.selector(recordsAddButton).click();
            Wait.waitForClickable(recordsApplyButton);
            enterInInput(recordsRecordInput, record);
            Select.selector(recordsApplyButton).click();
            Wait.waitForClickable(modalConfirmButton);
            modalClickConfirm();
            Wait.waitForVisibility(recordsRecordLine);
        }
    }

    @Step("Add records by import")
    public void addFilterRecordsImport(File uploadFile){
        Select.selector(recordsImportInput).sendKeys(SystemUtility.getPathCanonical(uploadFile));
        Wait.waitForClickable(modalConfirmButton);
        modalClickConfirm();
        Wait.waitForVisibility(recordsRecordLine);
    }

    @Step("Assert records")
    public void assertRecords(List<String> recordsList){
        List<String> recordsExpected = new ArrayList<>(), recordsActual = new ArrayList<>();
        for (WebElement recordLine : Select.multiSelector(recordsRecordLine)){
            recordsActual.add(recordLine.getText());
        }
        for (String record : recordsList){
            for (String recordPart : record.split(",")){
                recordsExpected.add(recordPart.trim());
            }
        }
        if (recordsExpected.size() > recordsActual.size()){
            for (String actual : recordsActual){
                softAssert.assertTrue(recordsExpected.contains(actual), "Filter record <" + actual + "> not found in expected list");
            }
        } else {
            softAssert.assertEqualsNoOrder(recordsActual.toArray(), recordsExpected.toArray(), "Filter records are incorrect");
        }
    }

    @Step("Download example file for Import")
    public File clickDownloadExample(String adapterName){
        Select.selector(exportExampleButton).click();
        return FileUtil.getDownloadedFile("example_file_adapter_" + adapterName + ".csv", 5);
    }

    @Step("Upload new list")
    public void clickImportNewList(File upload){
        Select.selector(importNewInput).sendKeys(SystemUtility.getPathCanonical(upload));
        if (!AssertUtil.assertNotPresent(exportListButton)) {
            Wait.waitForClickable(modalConfirmButton);
            softAssert.assertEquals(Select.selector(modalHeaderText).getText(), AdapterText.modalReplaceText, "Replace modal text is incorrect");
        }
    }

    @Step("Upload additional list")
    public void clickImportAdditional(File upload){
        Select.selector(importAdditionalInput).sendKeys(SystemUtility.getPathCanonical(upload));
    }

    @Step("Click on 'Export List' button")
    public File clickDownloadExport(String adapterName, String settingName){
        Select.selector(exportListButton).click();
        return FileUtil.getDownloadedFile("export_file_adapter_" + adapterName + "_" + settingName + ".csv", 5);
    }

    @Step("Close import replace modal")
    public void clickReplaceModal(boolean confirm){
        if (confirm){
            modalClickConfirm();
        } else {
            modalClickCancel();
        }
    }

    @Step("Set additional columns in file")
    public File setImportAdditionalColumns(File original){
        File result;
        Set<String> defaultColumns = new HashSet<>(){{
            add("operation_systems");
            add("ad_format");
            add("traffic_type");
            add("sizes");
            add("geo");
            add("bundles");
        }};
        try{
            MappingIterator<Map<String, Object>> parsedCsv = FileUtil.readCsvFile(original);
            List<Map<String, Object>> csvList = parsedCsv.readAll();
            List<Map<String, Object>> resultCsvList = new ArrayList<>();
            if (!csvList.isEmpty() && csvList.get(0).keySet().equals(defaultColumns)){
                result = original;
            } else {
                result = new File(UserSettings.downloadFolder + "/" + original.getName().replace(".csv", "_rewrite.csv"));
                for (Map<String, Object> row : csvList){
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    for (Map.Entry<String, Object> entry : row.entrySet()){
                        if (defaultColumns.contains(entry.getKey())){
                            resultMap.put(entry.getKey(), entry.getValue());
                        } else {
                            String value = RandomStringUtils.random(15, true, true);
                            if (entry.getKey().equals("email")){
                                value += "@test.ga";
                            }
                            resultMap.put(entry.getKey(), value);
                        }
                    }
                    resultCsvList.add(resultMap);
                }
                FileUtil.writeCsvFile(result, resultCsvList);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    @Step("Convert example file into expected list")
    public File convertExampleList(File example){
        File result = null;
        try{
            MappingIterator<Map<String, Object>> parsedCsv = FileUtil.readCsvFile(example);
            List<Map<String, Object>> csvList = parsedCsv.readAll(), resultCsvList = new ArrayList<>();
            if (!csvList.isEmpty()){
                result = new File(UserSettings.downloadFolder + "/" + example.getName().replace(".csv", "_" + Utils.getTimestamp() + "_expected.csv"));
                for (Map<String, Object> row : csvList){
                    boolean addRow = true;
                    Map<String, Object> resultMap = new LinkedHashMap<>();
                    for (Map.Entry<String, Object> cell : row.entrySet()){
                        String value = cell.getValue().toString();
                        resultMap.put(cell.getKey(), value);
                    }
                    for (Map.Entry<String, Object> cell : resultMap.entrySet()){
                        if (cell.getKey().equals("bundles") && cell.getValue().toString().contains(",")){
                            String[] bundlesActual = cell.getValue().toString().split(",");
                            for (String s : bundlesActual){
                                resultMap.put(cell.getKey(), s);
                                resultCsvList.add(new LinkedHashMap<>(resultMap));
                                addRow = false; //this line is to prevent adding the last row again
                            }
                        }
                    }
                    if (addRow){
                        resultCsvList.add(resultMap);
                    }
                }
                FileUtil.writeCsvFile(result, resultCsvList);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    @Step("Merge expected files")
    public File mergeFilesExpected(File original, File additional){
        File result = null;
        try{
            MappingIterator<Map<String, Object>> parsedCsv = FileUtil.readCsvFile(original);
            List<Map<String, Object>> csvList = parsedCsv.readAll();
            MappingIterator<Map<String, Object>> parsedCsvAdd = FileUtil.readCsvFile(additional);
            List<Map<String, Object>> csvListAdd = parsedCsvAdd.readAll();
            if (!csvList.isEmpty() && !csvListAdd.isEmpty()){
                result = new File(UserSettings.downloadFolder + "/" + original.getName().replace(".csv", "_merged.csv"));
                List<Map<String, Object>> resultCsvList = new ArrayList<>(csvList);
                resultCsvList.addAll(csvListAdd);
                FileUtil.writeCsvFile(result, resultCsvList);
            }
        } catch (Exception e){
            throw new RuntimeException(e);
        }
        return result;
    }

    @Step("Assert adapter setting data on edit page")
    public void assertAdapterSettingEdit(AdapterDO settingData){
        softAssert.assertTrue(AssertUtil.assertNotPresent(settingManualImportToggle), "Adapter setting edit: Import toggle is displayed");
        softAssert.assertEquals(Select.selector(settingNameInput).getAttribute("value"), settingData.getSettingName(), "Adapter setting edit: Setting name is incorrect");
        if (settingData.getIsImport()){
            softAssert.assertTrue(AssertUtil.assertPresent(importNewButton), "Adapter setting edit: Import new button is not displayed");
            softAssert.assertTrue(AssertUtil.assertPresent(importAdditionalButton), "Adapter setting edit: Import additional button is not displayed");
            softAssert.assertTrue(AssertUtil.assertPresent(exportListButton), "Adapter setting edit: Export button is not displayed");
            softAssert.assertTrue(AssertUtil.assertPresent(exportExampleButton), "Adapter setting edit: Example button is not displayed");
        } else {
            String osExpected = getExpectedMultiselectText(settingData.getOsMap(), "All", 10),
                    trafficExpected = getExpectedMultiselectText(settingData.getTrafficNamesMap(), "All traffic", 5, 5),
                    adFormatExpected = getExpectedMultiselectText(settingData.getAdFormatNamesMap(), "All Ad formats", 5, 5);
            softAssert.assertEquals(Select.selector(settingAllSizesToggle).isSelected(), settingData.getAllSizes().booleanValue(), "Adapter setting edit: All sizes toggle is incorrect");
            softAssert.assertEquals(Select.selector(settingOsSelect, relative_selectLabel).getText(), osExpected, "Adapter setting edit: OS multiselect is incorrect");
            softAssert.assertEquals(Select.selector(settingTrafficSelect, relative_selectLabel).getText().toLowerCase(), trafficExpected.toLowerCase(), "Adapter setting edit: Traffic multiselect is incorrect");
            softAssert.assertEquals(Select.selector(settingAdFormatSelect, relative_selectLabel).getText().toLowerCase(), adFormatExpected.toLowerCase(), "Adapter setting edit: Ad format multiselect is incorrect");
            if (!settingData.getCustomSettingsMap().isEmpty()){
                for (Map.Entry<String, String> customField : settingData.getCustomSettingsMap().entrySet()){
                    softAssert.assertEquals(Select.selectByAttributeExact(relative_generalInput, "name", customField.getKey()).getAttribute("value"), customField.getValue(), "Adapter setting edit: Custom field <" + customField.getKey() + "> is incorrect");
                }
            }
            if (!settingData.getAllSizes()){
                if (!settingData.getSizesList().isEmpty()){
                    Map<String, String> sizesExpected = new HashMap<>(), sizesActual = new HashMap<>();
                    for (AdapterDO.SizeSetting size : settingData.getSizesList()){
                        sizesExpected.put(size.getSize().publicName(), size.getWidth() + "x" + size.getHeight());
                    }
                    for (WebElement actualSizeRow : Select.multiSelector(sizeRow)){
                        sizesActual.put(Select.selector(Select.selector(actualSizeRow, sizeSelect), relative_selectLabel).getText(), Select.selector(actualSizeRow, sizeWidthInput).getAttribute("value") + "x" + Select.selector(actualSizeRow, sizeHeightInput).getAttribute("value"));
                    }
                    softAssert.assertEquals(sizesActual, sizesExpected, "Adapter setting edit: Sizes are incorrect");
                }
            } else {
                softAssert.assertTrue(AssertUtil.assertNotPresent(addSizeButton), "Adapter setting edit: Sizes are displayed when 'All sizes' is ON");
            }
            if (!settingData.getGeoList().isEmpty()){
                Map<String, String> countriesExpected = new HashMap<>(), countriesActual = new HashMap<>();
                List<adFormatPlacementTypeEnum> formats = new ArrayList<>(){{
                    add(adFormatPlacementTypeEnum.BANNER);
                    add(adFormatPlacementTypeEnum.VIDEO);
                    add(adFormatPlacementTypeEnum.NATIVE);
                    add(adFormatPlacementTypeEnum.REWARDED_VIDEO);
                    add(adFormatPlacementTypeEnum.AUDIO);
                }};
                for (AdapterDO.GeoSetting geo : settingData.getGeoList()){
                    Map<String, Boolean> adformatsStringMap = new LinkedHashMap<>();
                    for (adFormatPlacementTypeEnum format : formats){
                        for (Map.Entry<adFormatPlacementTypeEnum, Boolean> formatEntry : geo.getAdFormatList().entrySet()){
                            if (formatEntry.getKey().equals(format)){
                                adformatsStringMap.put(formatEntry.getKey().publicName().toLowerCase(), formatEntry.getValue());
                            }
                        }
                    }
                    countriesExpected.put(getExpectedMultiselectText(geo.getCountriesNamesMap(), "Select Here", 10), getExpectedMultiselectText(adformatsStringMap, "Select Here", 5));
                }
                for (WebElement actualGeoRow : Select.multiSelector(geoRow)){
                    countriesActual.put(Select.selector(Select.selector(actualGeoRow, geoCountriesSelect), relative_selectLabel).getText(), Select.selector(Select.selector(actualGeoRow, geoAdFormatSelect), relative_selectLabel).getText());
                }
                softAssert.assertEquals(countriesActual, countriesExpected, "Adapter setting edit: Countries are incorrect");
            }
            assertRecords(settingData.getRecordList());
            if (isSoftAssertLocal){
                softAssert.assertAll("Adapter record edit page is incorrect");
            }
        }
    }

    @Step("Assert adapter setting Import file")
    public void assertAdapterImportFile(File actualImport, File expectedImport, String adapterName, String settingName){
        String time = Utils.getTimestamp().toString().substring(0, 7), expectedName = "export_file_adapter_" + adapterName + "_" + settingName + "-" + time + "\\d{3}.csv";
        softAssert.assertTrue(actualImport.getName().matches(expectedName), "Adapter setting import: file name is incorrect. Expected: [" + expectedName + "]. Actual: [" + actualImport.getName() + "]");
        try{
            MappingIterator<Map<String, Object>> actualCsv = FileUtil.readCsvFile(actualImport), expectedCsv = FileUtil.readCsvFile(expectedImport);
            List<Map<String, Object>> actualList = actualCsv.readAll(), expectedList = expectedCsv.readAll();
            softAssert.assertEquals(actualList.size(), expectedList.size(), "Adapter setting import: files have different row count");
            if (!actualList.isEmpty()){
                for (Map<String, Object> rowActual : actualList){
                    int expectedRowCount = 0;
                    String bundlesActual = rowActual.get("bundles").toString();
                    for (Map<String, Object> rowExpected : expectedList){
                        if (rowExpected.get("bundles").toString().equals(bundlesActual)){
                            expectedRowCount = expectedList.indexOf(rowExpected);
                            break;
                        }
                    }
                    Map<String, Object> rowExpected = expectedList.get(expectedRowCount);
                    for (Map.Entry<String, Object> cellActual : rowActual.entrySet()){
                        String valueActual = cellActual.getValue().toString(), valueExpected = rowExpected.get(cellActual.getKey()).toString();
                        if (cellActual.getKey().equals("geo")){
                            String[] geoActual = valueActual.split("\\|"), geoExpected = valueExpected.split("\\|"), geoExpectedCombined = combineGeo(geoExpected);
                            softAssert.assertEqualsNoOrder(geoActual, geoExpectedCombined, "Adapter setting import: expected row <" + expectedRowCount + ">, cell [" + cellActual.getKey() + "]. Actual: [" + valueActual + "]. Expected: [" + String.join("|", geoExpectedCombined) + "]");
                        } else if (cellActual.getKey().equals("sizes")){
                            String[] sizesActual = valueActual.split(","), sizesExpected = valueExpected.split(",");
                            softAssert.assertEqualsNoOrder(sizesActual, sizesExpected, "Adapter setting import: expected row <" + expectedRowCount + ">, cell [" + cellActual.getKey() + "]");
                        } else {
                            softAssert.assertEquals(valueActual, valueExpected, "Adapter setting import: expected row <" + expectedRowCount + ">, cell [" + cellActual.getKey() + "]. Actual: [" + valueActual + "]. Expected: [" + valueExpected + "]");
                        }
                    }
                    expectedList.remove(expectedRowCount);
                }
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private String[] combineGeo(String[] original){
        List<String> list = new ArrayList<>();
        Map<String, String> geoMap = new HashMap<>();
        if (original.length <= 1){
            return original;
        } else {
            for (String geo : original){
                String[] geoParts = geo.split(":");
                if (geoMap.containsKey(geoParts[0])){
                    Set<String> geoList = new HashSet<>(Arrays.asList(geoMap.get(geoParts[0]).split(",")));
                    geoList.addAll(Arrays.asList(geoParts[1].split(",")));
                    List<String> sortedGeoList = new ArrayList<>(geoList);
                    Collections.sort(sortedGeoList);
                    geoMap.put(geoParts[0], String.join(",", sortedGeoList));
                } else {
                    geoMap.put(geoParts[0], geoParts[1]);
                }
            }
            for (Map.Entry<String, String> geo : geoMap.entrySet()){
                list.add(geo.getKey() + ":" + geo.getValue());
            }
        }
        return list.toArray(new String[0]);
    }

    //</editor-fold>

}
