package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.UserSettings;
import common.Waits;
import common.utils.*;
import data.CommonEnums;
import data.CommonEnums.*;
import data.StaticData;
import data.UserEnum;
import data.dataobject.EmailDO;
import data.dataobject.EndpointCommonDO;
import io.qameta.allure.Step;
import org.jooq.Condition;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.testng.asserts.Assertion;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Map.Entry;

import static drivermanager.DriverManager.getDriver;

public class CommonElementsPO extends BasePage {
    protected static Assertion hardAssert = new Assertion();
    protected DatabaseUtility dbUtils = new DatabaseUtility();
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final EmailUtility EmailUtil = new EmailUtility();
    private final BasicUtility Util = new BasicUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final RandomUtility RandomUtil = new RandomUtility();
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();


    public CommonElementsPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public CommonElementsPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    //<editor-fold desc="Header menu">
    static final By
            headerLogo = By.xpath("//a[@class='def']"),
            adminLoginAsUserSelect = By.xpath("//select[@id='sudouser']"),
            relative_dropdownGroup = By.xpath("./..//li[@class='group']"),
            relative_dropdownGroupLabel = By.xpath("./label"),
            relative_dropdownOption = By.xpath(".//li"),
            editUserProfileButton = By.xpath("//div[@class='admin-action right']//a[contains(@href, 'users')]"),
            inventoryPendingApprovalButton = By.xpath("//div[@class='admin-action right']//a[contains(@href, 'inventory')]"),
            editMyProfileButton = By.xpath("//a[contains(@href, 'profile')]"),
            logoutButton = By.xpath("//a[contains(@class, 'logout')]");

    @Step("Get user ID from user name in header")
    public Integer getUserIdHeader(){
        String user = Select.selector(editMyProfileButton).getText();
        return Integer.valueOf(user.substring(user.lastIndexOf("#") + 1, user.lastIndexOf(")")));
    }

    @Step("Select any user with role {role} to login as")
    public void loginAsUser(UserEnum.userRoleEnum role){
        Select.selector(adminLoginAsUserSelect, relative_selectLabel).click();
        WebElement userGroup = null;
        for (WebElement group : Select.multiSelector(adminLoginAsUserSelect, relative_dropdownGroup)){
            if (Select.selector(group, relative_dropdownGroupLabel).getText().equalsIgnoreCase(role.publicName())){
                userGroup = group;
                break;
            }
        }
        List<WebElement> usersOfRole = Select.multiSelector(userGroup, relative_dropdownOption);
        int rnd = RandomUtil.getRandomInt(0, usersOfRole.size());
        usersOfRole.get(rnd).click();
        Wait.waitForVisibility(modalDialog);
        Wait.waitForNotVisible(modalDialog);
        Wait.waitForClickable(logoutButton);
    }


    //<editor-fold desc="Main menu">
    static final By
            dashboardSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'dashboard')]"),
            inventorySectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'inventory')]"),
            reportMenuSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'scheduled-reporting')]/../../../a"),
            reportUserAdminPublisherSectionButton = By.xpath("./..//ul[contains(@class, 'dropdown-content')]//a[contains(@href, '/report')]"),
            reportUserSspDspUserSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'report-view')]"),
            reportScheduledSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'scheduled-reporting')]"),
            dealsPublisherSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'deals')]"),
            transactionHistorySectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'history')]"),
            cashoutSettingsSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'billing')][not(contains(@href, 'history'))]"),
            managementAPImenuSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@class,'dropdown-button')]"),
            managementAPIlinkSectionButton = By.xpath("//ul[@class='dropdown-content']//a[contains(@href, 'management-api')]"),
            managementAPIintegrationsSectionButton = By.xpath("//ul[@class='dropdown-content']//a[contains(@href, 'publisher-integration')]"),
            managementAPIgdprSectionButton = By.xpath("//ul[@class='dropdown-content']//a[contains(@href, 'gdpr')]"),
            sspSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'ssp-view')]"),
            sspReportAPISectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'ssp-reporting')]"),
            dspSectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'dsp-view')]"),
            dspReportAPISectionButton = By.xpath("//ul[contains(@class, 'hide')]//a[contains(@href, 'dsp-reporting')]");
    //</editor-fold>

    //<editor-fold desc="Admin menu (sidebar)">
    static final By
            adminMenuButton = By.xpath("//a[@data-activates='mobile_menu_admin_section']"),
            pubManagementButton = By.xpath("//ul[@id='mobile_menu_admin_section']/li[2]/a"),
            adxActivityButton = By.xpath("//ul[@id='mobile_menu_admin_section']/li[3]/a"),
            supportToolsButton = By.xpath("//ul[@id='mobile_menu_admin_section']/li[4]/a"),
            administrationButton = By.xpath("//ul[@id='mobile_menu_admin_section']/li[5]/a"),
    /*Publishers' management*/
    pubReportLink = By.xpath("//li/a[contains(@href, 'publishers-manage/report')]"),
            manageInventoryLink = By.xpath("//li/a[contains(@href, 'publishers-manage/inventory')]"),
            managePaymentsLink = By.xpath("//li/a[contains(@href, 'publishers-manage/payments')]"),
    /*Ad Exchange activity*/
    companiesLink = By.xpath("//li/a[contains(@href, 'ad-exchange/companies')]"),
            filtersLink = By.xpath("//li/a[contains(@href, 'filter-list')]"),
            supplyLink = By.xpath("//li/a[contains(@href, 'ad-exchange/ssp')]"),
            demandLink = By.xpath("//li/a[contains(@href, 'ad-exchange/dsp')]"),
            adaptersLink = By.xpath("//li/a[contains(@href, 'ad-exchange/adapters')]"),
            adxStatisticsLink = By.xpath("//li/a[contains(@href, 'ad-exchange/report') and not(contains(@href, 'scanners'))]"),
            scannersLink = By.xpath("//li/a[contains(@href, 'ad-exchange/scanner-settings')]"),
            sellersJsonLink = By.xpath("//li/a[contains(@href, 'ad-exchange/sellers-json')]"),
            adsTxtPartnersLink = By.xpath("//li/a[contains(@href, 'ad-exchange/ads-txt-partners')]"),
    /*Administration*/
    userManagementLink = By.xpath("//li/a[contains(@href, 'users')]"),
            platformSettingsLink = By.xpath("//li/a[contains(@href, 'administration/settings')]");

    @Step("Open admin menu")
    public void openAdminMenu() {
        Select.selector(adminMenuButton).click();
        Wait.waitForClickable(pubManagementButton);
    }

    @Step("Open Pub Management menu")
    public void clickPubManagementWrap(boolean open){
        if (open ^ Select.selector(pubReportLink).isDisplayed()){
            Select.selector(pubManagementButton).click();
        }
    }

    @Step("Open ADX Activity menu")
    public void clickAdxActivityWrap(boolean open) {
        if (open ^ Select.selector(supplyLink).isDisplayed()) {
            Select.selector(adxActivityButton).click();
        }
    }

    @Step("Open Administration menu")
    public void clickAdministrationWrap(boolean open) {
        if (open ^ Select.selector(userManagementLink).isDisplayed()){
            Select.selector(administrationButton).click();
        }
    }

    //</editor-fold>

    //<editor-fold desc="Common elements">
    static final By
            submitButton = By.xpath("//button[@type='submit']"),
            saveButton = By.xpath("//label[@data-type='save']"),
            saveExitButton = By.xpath("//label[@data-type='save_exit']"),
            toggleInput = By.xpath("//input[@type='checkbox']"),
            titleInput = By.xpath("//input[@id='title']"),
            relative_selectLabel = By.xpath("./../p"),
            relative_selectInput = By.xpath("./..//input[@class='search-txt']"),
            relative_selectOptionWithTitle = By.xpath("./..//li[@title]"),
            relative_selectOption = By.xpath("./..//li[contains(@class, 'opt')]"),
            relative_selectOptionGeneral = By.xpath("./..//li"),
            relative_multiselectCheckbox = By.xpath("./span"),
            relative_multiselectAvailableOption = By.xpath("./..//li[not(contains(@class, 'hide'))][@title]"),
            relative_radioLabel = By.xpath("./../label"),
            relative_toggleLever = By.xpath("./../span[@class='lever']"),
            relative_iconElement = By.xpath("./i"),
            confirmMessageToast = By.xpath("//div[@id='toast-container']//span[not(@class)]"),
            preloader = By.xpath("//div[@class='pre-loader-form fixed']"),
            tableRowGeneral = By.xpath("//tbody/tr"),
            preloaderTable = By.xpath(".//div[@class='table-data-dynamic']//div[contains(@class, 'loaded-data')]"),
            tableEmptyMassageGeneral = By.xpath("//div[@class='table-data']//div[contains(@class, 'no-data')]/h4"),
            preloaderHeader = By.xpath("//div[contains(@class, 'global-loaded')]"),
            breadcrumbsLink = By.xpath("//section[@id='page-title']//nav//a"),
            relative_selectValidationLabel = By.xpath("./..//label[@data-error]"),
            relative_selectValidationLabel2 = By.xpath("./../..//label[@data-error]"),
            relative_selectValidationDiv = By.xpath("./..//div[@data-error]"),
            relative_generalCheckbox = By.xpath(".//input[@type='checkbox']"),
            relative_generalSelect = By.xpath(".//select"),
            relative_generalListItem = By.xpath(".//li"),
            relative_generalInput = By.xpath(".//input[not(@type='checkbox')]"),
            relative_generalLink = By.xpath(".//a"),
            relative_generalTableHeadCell = By.xpath(".//th"),
            relative_generalTableBodyCell = By.xpath(".//td"),
            footerCopyrightLine = By.xpath("//footer//span"),
            footerCompanyNameType = By.xpath("//footer//a[1]"),
            footerTermsOfUse = By.xpath("//footer//div/a[2]"),
            footerAgreement = By.xpath("//footer//div/a[4]"),
            footerPrivacy = By.xpath("//footer//div/a[3]"),
            footerService = By.xpath("//footer//div/a[5]");

    @Step("Clear and enter value in input")
    public void enterInInput(By element, Object text) {
        Select.selector(element).clear();
        if (text != null){
            Select.actionTypeKeys(element, String.valueOf(text));
        }
    }

    @Step("Clear and enter value in input")
    public void enterInInputWithFocus(By element, Object text) {
        Select.actionSelectDeleteAll(element);
        if (text != null) {
            Select.actionTypeKeys(element, String.valueOf(text));
        }
    }

    @Step("Clear and enter value in input")
    public void enterInInput(WebElement element, Object text) {
        element.clear();
        if (text != null){
            Select.actionTypeKeys(element, String.valueOf(text));
        }
    }

    @Step("Get toggle element")
    public WebElement getToggleByName(By toggleSelector, String name){
        return Select.selectByAttributeIgnoreCase(toggleSelector, "name", name);
    }

    @Step("Click toggle button")
    public void clickToggle(By selector, boolean status){
        if (status != Select.selector(selector).isSelected()){
            Select.selector(selector, relative_toggleLever).click();
        }
    }

    @Step("Click toggle button")
    public void clickToggle(WebElement toggleInput, boolean status){
        if (status != toggleInput.isSelected()){
            Select.selector(toggleInput, relative_toggleLever).click();
        }
    }

    @Step("Click toggle button")
    public void clickToggle(WebElement parent, By selector, boolean status){
        WebElement toggleInput = Select.selector(parent, selector);
        if (status != toggleInput.isSelected()){
            Select.selector(toggleInput, relative_toggleLever).click();
        }
    }

    @Step("Click radio button option {value}")
    public void clickRadioButton(By radioSelector, String value){
        WebElement radioOption = Select.selectByAttributeIgnoreCase(radioSelector, "value", value);
        Select.selector(radioOption, relative_radioLabel).click();
    }

    @Step("Click radio button option {value}")
    public void clickRadioButton(WebElement element){
        Select.selector(element, relative_radioLabel).click();
    }

    @Step("Click radio button option {value}")
    public void clickRadioButton(WebElement element, By selector, String value){
        WebElement radioOption = Select.selectByAttributeIgnoreCase(element, selector, "value", value);
        Select.selector(radioOption, relative_radioLabel).click();
    }

    @Step("Click checkbox")
    public void clickCheckbox(WebElement inputCheckbox, boolean status){
        if (status != inputCheckbox.isSelected()){
            Select.selector(inputCheckbox, relative_radioLabel).click();
        }
    }

    @Step("Click checkbox")
    public void clickCheckbox(By inputCheckbox, boolean status){
        WebElement checkbox = Select.selector(inputCheckbox);
        if (status != checkbox.isSelected()){
            Select.selector(checkbox, relative_radioLabel).click();
        }
    }

    @Step("Go back on breadcrumbs by ${steps} steps back")
    public void clickBreadcrumbItem(int steps){
        List<WebElement> breadcrumbs = Select.multiSelector(breadcrumbsLink);
        int oldCount = breadcrumbs.size();
        breadcrumbs.get(steps).click();
        Wait.waitForNumberOfElementsLessThan(breadcrumbsLink, oldCount);
    }

    @Step("Add setting row")
    public void clickOptionRowAdd(By rowSelector, By addRowButton, int count){
        while (count != 0){
            int rowCount = 0;
            if (!AssertUtil.assertNotPresent(rowSelector)) {
                rowCount = Select.multiSelector(rowSelector).size();
            }
            Select.selector(addRowButton).click();
            Wait.waitForNumberOfElementsMoreThan(rowSelector, rowCount);
            count--;
        }
    }

    @Step("Search item in table")
    public void searchItemInTable(String searchRequest, By searchLocator, By tableLocator) {
        By table = tableLocator != null ? tableLocator : tableBlock;
        enterInInput(searchLocator, searchRequest);
        Wait.sleep(500);
        Select.actionPressEnter(searchLocator);
        Wait.attributeNotContains(table, "class", "disabled");
        Wait.attributeContains(preloaderTable, "class", "hide");
        Wait.sleep(500);
    }

    @Step("Assert validation error")
    public void assertValidationError(SoftAssertCustom softAssertion, By inputSelector, String errorMessage) {
        if (errorMessage != null){
            String fieldName = Select.selector(inputSelector).getAttribute("name");
            if (!AssertUtil.assertNotPresent(inputSelector, relative_selectValidationLabel)) {
                softAssertion.assertEquals(Select.selector(inputSelector, relative_selectValidationLabel).getAttribute("data-error"), errorMessage, "Validation: [" + fieldName + "] setting validation text is incorrect.");
            } else {
                softAssertion.fail("Validation: [" + fieldName + "] setting validation text is not displayed");
            }
        }
    }

    @Step("Assert validation error")
    public void assertValidationError(SoftAssertCustom softAssertion, By inputSelector, By labelSelector, String errorMessage) {
        if (errorMessage != null){
            String fieldName = Select.selector(inputSelector).getAttribute("name");
            if (!AssertUtil.assertNotPresent(inputSelector, labelSelector)) {
                softAssertion.assertEquals(Select.selector(inputSelector, labelSelector).getAttribute("data-error"), errorMessage, "Validation: [" + fieldName + "] setting validation text is incorrect");
            } else {
                softAssertion.fail("Validation: [" + fieldName + "] setting validation text is not displayed");
            }
        }
    }

    @Step("Assert validation error")
    public void assertValidationError(SoftAssertCustom softAssertion, WebElement inputSelector, String errorMessage) {
        if (errorMessage != null){
            String fieldName = inputSelector.getAttribute("name");
            if (AssertUtil.assertPresent(inputSelector, relative_selectValidationLabel)) {
                softAssertion.assertEquals(Select.selector(inputSelector, relative_selectValidationLabel).getAttribute("data-error"), errorMessage, "Validation: [" + fieldName + "] validation text is incorrect");
            } else {
                softAssertion.fail("Validation: [" + fieldName + "] validation text is not displayed");
            }
        }
    }

    @Step("Assert validation error")
    public void assertValidationError(SoftAssertCustom softAssertion, WebElement inputSelector, By labelSelector, String errorMessage) {
        if (errorMessage != null){
            String fieldName = inputSelector.getAttribute("name");
            if (AssertUtil.assertPresent(inputSelector, labelSelector)) {
                softAssertion.assertEquals(Select.selector(inputSelector, labelSelector).getAttribute("data-error"), errorMessage, "Validation: [" + fieldName + "] setting validation text is incorrect");
            } else {
                softAssertion.fail("Validation: [" + fieldName + "] setting validation text is not displayed");
            }
        }
    }

    @Step("Click Save Button")
    public void clickSaveButton(){
        Select.selector(saveButton).click();
        Wait.waitForClickable(saveButton);
    }

    @Step("Get Platform Name from footer")
    public String getPlatformNameFooter(){
        return Select.selector(footerCompanyNameType).getAttribute("textContent").split(" ")[0].trim(); //getAttribute("textContent"), because when the modal is opened, getText() is returning empty string
    }

    @Step("Get Platform Type from footer")
    public String getPlatformTypeFooter(){
        return Select.selector(footerCompanyNameType).getAttribute("textContent").split(" ")[1].trim(); //getAttribute("textContent"), because when the modal is opened, getText() is returning empty string
    }

    @Step("Assert footer data")
    public void assertFooter(String platformName, String platformType, String useLink, String agreementLink, String privacyLink, String serviceLink) {
        String name = platformName != null ? platformName : "", type = platformType != null ? platformType : "";
        softAssert.assertEquals(Select.selector(footerCopyrightLine).getText(), "Protected: All work copyright of respective owner, otherwise © "+ Util.getCurrentDateTimeFormatted("YYYY", StaticData.defaultLocale), "Copyright line is not correct");
        softAssert.assertEquals(Select.selector(footerCompanyNameType).getText(), name +" "+type, "Name and Type are not correct");
        softAssert.assertEquals(Select.selector(footerTermsOfUse).getAttribute("href"), useLink, "TOS link not correct.");
        softAssert.assertEquals(Select.selector(footerAgreement).getAttribute("href"), agreementLink, "Agreement link is not correct.");
        softAssert.assertEquals(Select.selector(footerPrivacy).getAttribute("href"), privacyLink, "Privacy link is not correct.");
        softAssert.assertEquals(Select.selector(footerService).getAttribute("href"), serviceLink, "Service link is not correct.");
    }

    //</editor-fold>

    //<editor-fold desc="Dropdown actions">
    //TODO make the Map more versatile and to receive not only <String, Boolean>
    @Step("Assert multiselect placeholder text")
    public String getExpectedMultiselectText(Map<String, Boolean> multiselectMap, String emptyPlaceholder, int maxCount){
        String returnString = emptyPlaceholder;
        List<String> selectedList = new ArrayList<>();
        if (multiselectMap != null && !multiselectMap.isEmpty()){
            for (Entry<String, Boolean> entry : multiselectMap.entrySet()){
                if (entry.getValue()){
                    selectedList.add(entry.getKey());
                }
            }
        }
        if (selectedList.getFirst().contains("#")) {
            selectedList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.substring(o.lastIndexOf("#") + 1))));
        }
        if (!selectedList.isEmpty()){
            if (selectedList.size() < maxCount){
                returnString = String.join(", ", selectedList);
            } else {
                returnString = multiselectMap.size() + " Selected";
            }   //TODO add condition to display All Selected text
        }
        return returnString;
    }

    @Step("Convert selection map into multiselect placeholder text")
    public String getExpectedMultiselectText(Map<String, Boolean> multiselectMap, String emptyPlaceholder, int maxCount, int allCount){
        String returnString = emptyPlaceholder;
        List<String> selectedList = new ArrayList<>();
        if (multiselectMap != null && !multiselectMap.isEmpty()){
            for (Entry<String, Boolean> entry : multiselectMap.entrySet()){
                if (entry.getValue()){
                    selectedList.add(entry.getKey());
                }
            }
        }
        if (selectedList.getFirst().contains("#")) {
            selectedList.sort(Comparator.comparingInt(o -> Integer.parseInt(o.substring(o.lastIndexOf("#") + 1))));
        }
        if (!selectedList.isEmpty() && selectedList.size() != allCount){
            if (selectedList.size() < maxCount){
                returnString = String.join(", ", selectedList);
            } else {
                returnString = multiselectMap.size() + " Selected";
            }
        }
        return returnString;
    }

    @Step("Click dropdown option {value}")
    public void selectSingleSelectDropdownOption(By dropdownSelector, String value){
        if (!Select.selector(dropdownSelector, relative_selectLabel).getAttribute("title").trim().equalsIgnoreCase(value)){
            Select.selector(dropdownSelector, relative_selectLabel).click();
            Select.selectByAttributeIgnoreCase(dropdownSelector, relative_selectOptionWithTitle, "title", value).click();
            Wait.waitForNotVisible(Select.selector(dropdownSelector, relative_selectOptionWithTitle));
        }
    }

    @Step("Click dropdown option {value}")
    public void selectDropdownOption(By dropdownSelector, String value){
        if (!Select.selector(dropdownSelector, relative_selectLabel).getAttribute("title").trim().equalsIgnoreCase(value)){
            Select.selector(dropdownSelector, relative_selectLabel).click();
            Wait.sleep(500); //Wait for options to load
            try {
                Select.selectByAttributeIgnoreCase(dropdownSelector, relative_selectOptionWithTitle, "title", value).click();
            } catch (NullPointerException ex){
                Select.selectByTextEquals(dropdownSelector, relative_selectOption, value).click();
            }
            Wait.waitForNotVisible(Select.selector(dropdownSelector, relative_selectOptionWithTitle));
        }
    }

    @Step("Click dropdown option {value}")
    public void selectDropdownOption(WebElement dropdownSelector, String value){
        if (!Select.selector(dropdownSelector, relative_selectLabel).getAttribute("title").trim().equalsIgnoreCase(value)){
            Select.selector(dropdownSelector, relative_selectLabel).click();
            Wait.sleep(300); //Wait for options to load
            try {
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
                Select.selectByAttributeIgnoreCase(dropdownSelector, relative_selectOptionWithTitle, "title", value).click();
                Wait.waitForNotVisible(Select.selector(dropdownSelector, relative_selectOptionWithTitle));
            } catch (NullPointerException ex){
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(IMPLICIT_TIMEOUT));
                Select.selectByTextEquals(dropdownSelector, relative_selectOption, value).click();
                Wait.waitForNotVisible(Select.selector(dropdownSelector, relative_selectOption));
            }
        }
    }

    @Step("Click dropdown option {value}")
    public void selectSingleSelectDropdownOption(WebElement dropdownElement, String value){
        if (!Select.selector(dropdownElement, relative_selectLabel).getAttribute("title").trim().equalsIgnoreCase(value)){
            Select.selector(dropdownElement, relative_selectLabel).click();
            Select.selectByAttributeIgnoreCase(dropdownElement, relative_selectOptionWithTitle, "title", value).click();
            Wait.waitForNotVisible(Select.selector(dropdownElement, relative_selectOptionWithTitle));
        }
    }

    @Step("Click dropdown option {value}")
    public void selectSingleSelectDropdownOptionByName(By dropdownSelector, String value){
        if (!Select.selector(dropdownSelector, relative_selectLabel).getText().equals(value)){
            Select.selector(dropdownSelector, relative_selectLabel).click();
            Select.selectByTextEquals(dropdownSelector, relative_selectOption, value).click();
            Wait.waitForNotVisible(Select.selector(dropdownSelector, relative_selectOption));
        }
    }

    @Step("Click dropdown option {value}")
    public void selectSingleSelectDropdownOptionByName(WebElement dropdownElement, String value){
        if (!Select.selector(dropdownElement, relative_selectLabel).getText().equals(value)){
            Select.selector(dropdownElement, relative_selectLabel).click();
            Select.selectByTextEquals(dropdownElement, relative_selectOption, value).click();
            Wait.waitForNotVisible(Select.selector(dropdownElement, relative_selectOption));
        }
    }

    @Step("Click multiselect dropdown option {value}")
    public void clickDropdownMultiselectOption(By dropdownSelector, String value, boolean status){
        WebElement option;
        if (value == null) {
            option = Select.selectByTextContains(dropdownSelector, relative_selectOption, "All");
        } else {
            option = Select.selector(dropdownSelector, relative_selectOption);
            if (option.getAttribute("title").contains(value)) {
                option.click();
            } else {
                option = Select.selectByTextContains(dropdownSelector, relative_selectOption, value);
            }
        }
        if (status != option.getAttribute("class").contains("selected")){
            option.click();
        }
    }

    @Step("Click multiselect dropdown option {value}")
    public void clickDropdownMultiselectOption(WebElement selectElement, String value, boolean status){
        WebElement option;
        if (value == null) {
            option = Select.selectByTextEquals(selectElement, relative_selectOption, "All");
        } else {
            option = Select.selector(selectElement, relative_selectOption);
            if (option.getAttribute("title").contains(value)) {
                option.click();
            } else {
                option = Select.selectByTextEquals(selectElement, relative_selectOption, value);
            }
        }
        if (status != option.getAttribute("class").contains("selected")){
            option.click();
        }
    }

    @Step("Select options in multiselection list")
    public void selectMultiselectOptions(By selectElement, Map<String, Boolean> map){
        openMultiselectList(selectElement);
        for (Entry<String, Boolean> entry : map.entrySet()){
            clickDropdownMultiselectOption(selectElement, entry.getKey(), entry.getValue());
        }
        closeMultiselectList(selectElement);
    }

    @Step("Select options in multiselection list")
    public void selectMultiselectOptions(WebElement element, Map<String, Boolean> map){
        Select.selector(element, relative_selectLabel).click();
        for (Entry<String, Boolean> entry : map.entrySet()){
            clickDropdownMultiselectOption(element, entry.getKey(), entry.getValue());
        }
        closeMultiselectList(element);
    }

    @Step("Get random items in multiselect list")
    public LinkedHashMap<String, Boolean> getRandomActiveMultiselectOptionsAsMap(By dropdownSelector, String additionalName, Integer count){
        List<WebElement> optionsList = Select.selectListByAttributeNotContains(dropdownSelector, relative_multiselectAvailableOption, "class", "disabled");
        return getRandomMultiselectOptions(optionsList, additionalName, count);
    }

    @Step("Get random items in multiselect list")
    public LinkedHashMap<String, Boolean> getRandomActiveMultiselectOptionsAsMap(By dropdownSelector, Integer count){
        List<WebElement> optionsList = Select.selectListByAttributeNotContains(dropdownSelector, relative_multiselectAvailableOption, "class", "disabled");
        return getRandomMultiselectOptions(optionsList, null, count);
    }

    @Step("Get random items in multiselect list")
    public LinkedHashMap<String, Boolean> getRandomActiveMultiselectOptionsAsMap(WebElement dropdownSelector, Integer count){
        List<WebElement> optionsList = Select.selectListByAttributeNotContains(dropdownSelector, relative_multiselectAvailableOption, "class", "disabled");
        return getRandomMultiselectOptions(optionsList, null, count);
    }

    @Step("Get random items in multiselect list")
    private LinkedHashMap<String, Boolean> getRandomMultiselectOptions(List<WebElement> optionsList, String additionalName, Integer count){
        List<String> fullList = new ArrayList<>();
        LinkedHashSet<String> selectedList = new LinkedHashSet<>();
        LinkedHashMap<String, Boolean> returnMap = new LinkedHashMap<>();
        if (additionalName != null && !additionalName.isBlank()){
            optionsList.removeIf(list -> !list.getAttribute("title").contains(additionalName));
        }
        optionsList.forEach(opt -> fullList.add(opt.getAttribute("title")));
        int countMax = fullList.size() >= count ? count : fullList.size();
        while (selectedList.size() != countMax){
            selectedList.add(fullList.get(RandomUtil.getRandomInt(0, fullList.size())));
        }
        List<String> selectedSortedList = new ArrayList<>(selectedList);
        selectedSortedList.sort(Comparator.comparing(fullList::indexOf));
        for (String itemName : selectedSortedList){
            returnMap.put(itemName, true);
        }
        return returnMap;
    }

    @Step("Open multiselect list")
    protected void openMultiselectList(By dropdownSelector) {
        Select.selector(dropdownSelector, relative_selectLabel).click();
        Wait.waitForVisibility(dropdownSelector, relative_selectOption);
        Wait.sleep(500);
    }

    @Step("Click multiselect dropdown option {value}")
    public void closeMultiselectList(By dropdownSelector) {
        WebElement dropdownLine = Select.selector(dropdownSelector, relative_selectLabel), dropdownOption = Select.selector(dropdownSelector, relative_selectOption);
        int height = dropdownLine.getSize().height, adjustY = 1 + (height / 2);
        int width = dropdownLine.getSize().width, adjustX = 1 + width;
        Select.actionClickWithAdjust(dropdownSelector, adjustX, -adjustY);
        Wait.waitForNotVisible(dropdownOption);
    }

    @Step("Click multiselect dropdown option {value}")
    public void closeMultiselectList(WebElement dropdownSelector) {
        WebElement dropdownLine, dropdownOption = Select.selector(dropdownSelector, relative_selectOption);
        if (AssertUtil.assertPresent(dropdownSelector, relative_selectLabel)) {
            dropdownLine = Select.selector(dropdownSelector, relative_selectLabel);
        } else {
            dropdownLine = dropdownSelector;    // special case for reports
        }
        int height = dropdownLine.getSize().height, adjustY = 1 + (height / 2);
        int width = dropdownLine.getSize().width, adjustX = 1 + (width / 2);
        Select.actionClickWithAdjust(dropdownSelector, adjustX, -adjustY);
        Wait.waitForNotVisible(dropdownOption);
    }

    //</editor-fold>

    //<editor-fold desc="CalendarSSP">
    static final By
            calendarOpenButton = By.xpath("//div[@id='DateRange']"),
            calendarPredefinedRangeButton = By.xpath("//div[@class='picker__holder']//div[@class='ranges']//li"),
            calendarDateTableCell = By.xpath("//div[@class='picker__holder']//table//tbody//td"),
            calendarApplyButton = By.xpath("//div[@class='picker__holder']//button[@class='applyBtn btn btn-sm btn-success']"),
            calendarFromString = By.xpath("//div[@id='DateRange']/span[@class='from']"),
            calendarToString = By.xpath("//div[@id='DateRange']/span[@class='to']");

    @Step("Select date period")
    public void calendarSelectDatePeriod(LocalDateTime dateFrom, LocalDateTime dateTo){
        Select.selector(calendarOpenButton).click();
        Wait.waitForClickable(calendarApplyButton);
        Select.selectByTextEquals(calendarDateTableCell, String.valueOf(dateFrom.getDayOfMonth())).click();
        Select.selectByTextEquals(calendarDateTableCell, String.valueOf(dateTo.getDayOfMonth())).click();
        Select.selector(calendarApplyButton).click();
        Wait.waitForClickable(calendarOpenButton);
    }

    @Step("Select date period")
    public void calendarSelectDatePeriod(CommonEnums.calendarRangeEnum range){
        Select.selector(calendarOpenButton).click();
        Wait.waitForClickable(calendarApplyButton);
        Select.selectByTextEquals(calendarPredefinedRangeButton, range.publicName()).click();
        Wait.waitForClickable(calendarOpenButton);
    }

    @Step("Assert date period in calendar label")
    public void assertCalendarRange(LocalDateTime dateFrom, LocalDateTime dateTo, String errorMessageName){
        String fromDate = Util.getDateTimeFormatted(dateFrom, "LLL d", Locale.ENGLISH), toDate = Util.getDateTimeFormatted(dateTo, "LLL d", Locale.ENGLISH);
        softAssert.assertEquals(Select.selector(calendarFromString).getText(), fromDate, errorMessageName + " date from is incorrect");
        softAssert.assertEquals(Select.selector(calendarToString).getText(), toDate, errorMessageName + " date to is incorrect");
    }
    //</editor-fold>

    //<editor-fold desc="Bundle\Domain Records interface">
    public static final By recordsAddButton = By.xpath("//a[@id='addParamToLists']"),
            recordsClearButton = By.xpath("//a[@id='clearListsParams']"),
            recordsExportButton = By.xpath("//a[@id='exportParamLists']"),
            recordsImportButton = By.xpath("//label[contains(@class, 'uploadDataLabel')]"),
            recordsImportInput = By.xpath("//input[contains(@class, 'uploadDataParam0')]"),
            recordsExampleButton = By.xpath("//a[contains(@href, 'example')]"),
            recordsRecordLine = By.xpath("//div[@class='collection-item']"),
            recordsRecordInput = By.xpath("//textarea"),
            recordsLoader = By.xpath("//div[@class='center-align']/div[@class='progress']"),
            recordsApplyButton = By.xpath("//label[@data-type='Add']"),
            recordsCancelButton = By.xpath("//label[@data-remove]"),
            recordDeleteButton = By.xpath(".//a/i[@class='fa fa-close']/..");

    @Step("Add records manually")
    public void addFilterRecordsManual(List<String> records){
        for (String record : records){
            Select.selector(recordsAddButton).click();
            Wait.waitForClickable(recordsRecordInput);
            enterInInput(recordsRecordInput, record);
            Select.selector(recordsApplyButton).click();
            Wait.waitForNotVisible(recordsLoader);
            Wait.waitForClickable(modalConfirmButton);
            modalClickConfirm();
        }
    }

    @Step("Add records by import")
    public void addFilterRecordsImport(File uploadFile){
        Select.selector(recordsImportInput).sendKeys(SystemUtility.getPathCanonical(uploadFile));
        Wait.waitForNotVisible(recordsLoader);
    }

    @Step("Assert records")
    public void assertRecords(SoftAssertCustom sAssert, List<String> recordsList) {
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
                sAssert.assertTrue(recordsExpected.contains(actual), "Filter record <" + actual + "> not found in expected list");
            }
        } else {
            sAssert.assertEqualsNoOrder(recordsActual.toArray(), recordsExpected.toArray(), "Filter records are incorrect");
        }
    }
    //</editor-fold>

    //<editor-fold desc="Endpoints">
    public static final By
            syncUrlInput = By.xpath("//input[@id='api_statistic_link']"),
            syncURLCheckButton = By.xpath("//a[@id='refreshLink']"),
            syncUrlDeleteButton = By.xpath("//a[@id='clearLink']"),
            syncUrlCsvColumnSelect = By.xpath("//select[contains(@id, 'api_statistic_map')]"),
            syncStatisticsJsonLine = By.xpath("//div[@class='controls-api-statistic']//pre[1]"),
            syncStatisticsXmlLine = By.xpath("//div[@class='controls-api-statistic']//pre[2]"),
            syncStatisticsCsvLine = By.xpath("//div[@class='controls-api-statistic']//pre[3]"),
            marginTypeSelect = By.xpath("//select[@id='margin_type']"),
            marginFixedMinInput = By.xpath("//input[@id='min_margin']"),
            marginMaxInput = By.xpath("//input[@id='max_margin']");

    @Step("Toggle Ad Formats")
    public void toggleAdFormat(Map<adFormatPlacementTypeEnum, Boolean> adFormats){
        for (Entry<adFormatPlacementTypeEnum, Boolean> adFormatEntry : adFormats.entrySet()){
            WebElement toggle = getToggleByName(toggleInput, adFormatEntry.getKey().attributeName());
            if (adFormatEntry.getValue() != null){
                clickToggle(toggle, adFormatEntry.getValue());
            }
        }
    }

    @Step("Toggle Traffic type")
    public void toggleTrafficType(Map<trafficTypeEnum, Boolean> trafficTypes){
        for (Entry<trafficTypeEnum, Boolean> trafficEntry : trafficTypes.entrySet()){
            WebElement toggle = getToggleByName(toggleInput, trafficEntry.getKey().attributeName());
            clickToggle(toggle, trafficEntry.getValue());
        }
    }

    @Step("Toggle advanced settings")
    public void toggleAdvancedSetting(Map<advancedSettingsEnum, Boolean> advSettings){
        for (Entry<advancedSettingsEnum, Boolean> advSettingsEntry : advSettings.entrySet()){
            WebElement toggle = getToggleByName(toggleInput, advSettingsEntry.getKey().attributeName());
            try{
                clickToggle(toggle, advSettingsEntry.getValue());
            } catch (WebDriverException ex){
                hardAssert.fail("Cannot click on toggle [" + advSettingsEntry.getKey().attributeName() + "]. Exception: " + ex.getMessage());
            }
        }
    }

    @Step("Set Margin settings")
    public void setMargin(EndpointCommonDO.MarginSettings margin){
        marginTypeEnum marginType = margin.getType();
        selectSingleSelectDropdownOption(marginTypeSelect, marginType.publicName());
        switch (marginType){
            case FIXED -> {
                Wait.waitForVisibility(marginFixedMinInput);
                enterInInput(marginFixedMinInput, margin.getFixed());
            }
            case ADAPTIVE -> {
                Wait.waitForNotVisible(marginFixedMinInput);
                Wait.waitForNotVisible(marginMaxInput);
            }
            case RANGE -> {
                Wait.waitForVisibility(marginMaxInput);
                enterInInput(marginFixedMinInput, margin.getMin());
                enterInInput(marginMaxInput, margin.getMax());
            }
        }
    }

    @Step("Select random items in Allowed/Blocked lists")
    public Map<String, Boolean> selectRandomAllowedBlockedListItems(By allowedSelector, By blockedSelector, boolean isAllowed, int listCount){
        By selectedList = isAllowed ? allowedSelector : blockedSelector;
        Map<String, Boolean> optionsMap = getRandomActiveMultiselectOptionsAsMap(selectedList, listCount);
        openMultiselectList(selectedList);
        for (Entry<String, Boolean> optionsEntry : optionsMap.entrySet()){
            clickDropdownMultiselectOption(selectedList, optionsEntry.getKey(), optionsEntry.getValue());
        }
        closeMultiselectList(selectedList);
        return optionsMap;
    }

    @Step("Select items in Allowed/Blocked lists")
    public void selectAllowedBlockedItems(By allowedSelector, By blockedSelector, boolean isAllowed, Map<String, Boolean> optionsMap){
        By selectedList = isAllowed ? allowedSelector : blockedSelector;
        openMultiselectList(selectedList);
        for (Entry<String, Boolean> optionsEntry : optionsMap.entrySet()){
            clickDropdownMultiselectOption(selectedList, optionsEntry.getKey(), optionsEntry.getValue());
        }
        closeMultiselectList(selectedList);
    }

    @Step("Fill in Partner's Statistics API")
    public void inputPartnersApiLink(String apiLink){
        enterInInput(syncUrlInput, apiLink);
    }

    @Step("Click on Check URL")
    public void clickCheckPartnersApiUrl(endpointPartnerApiTypeEnum apiType){
        Select.selector(syncURLCheckButton).click();
        Wait.waitForVisibility(modalDialog);
        switch (apiType){
            case CSV -> Wait.waitForClickable(Select.selector(syncUrlCsvColumnSelect, relative_selectLabel));
            case JSON, XML -> {
                Wait.waitForNotVisible(modalDialog);
                softAssert.assertTrue(AssertUtil.assertNotPresent(syncUrlCsvColumnSelect), "Additional info: CSV column setting is displayed on XML/JSON before saving");
            }
            case INVALID -> Wait.waitForVisibility(modalValidationContentText);
        }
    }

    @Step("Click on Reset URL")
    public void clickResetPartnersApiUrl(){
        Select.selector(syncUrlDeleteButton).click();
        Wait.waitForNotVisible(syncUrlDeleteButton);
    }

    @Step("Set CSV columns for Partner's API link")
    public void setPartnersApiCsvColumns(Map<endpointPartnerApiColumnsEnum, String> csvColumnsMap){
        WebElement dropdown;
        for (Entry<endpointPartnerApiColumnsEnum, String> csvEntry : csvColumnsMap.entrySet()){
            dropdown = Select.selectByAttributeContains(syncUrlCsvColumnSelect, "name", csvEntry.getKey().name().toLowerCase());
            selectSingleSelectDropdownOptionByName(dropdown, csvEntry.getValue());
        }
    }

    @Step("Assert Partner's Statistics API block")
    public void assertApiSync(String partnersUrl, Map<endpointPartnerApiColumnsEnum, String> csvColumnsMap){
        new UserSettings().getUserSettings();
        String jsonUrlActual = Select.selector(syncStatisticsJsonLine).getText(),
                xmlUrlActual = Select.selector(syncStatisticsXmlLine).getText(),
                csvUrlActual = Select.selector(syncStatisticsCsvLine).getText(), urlEscapedExpected,
                urlPatternExpected = UserSettings.projectUrl + "/api/statistic-report-[a-z0-9]{17}-[a-z0-9]{26}.<$fileformat$>\\?from=\\{%Y-m-d%\\}&to=\\{%Y-m-d%\\}";
        softAssert.assertEquals(Select.selector(syncUrlInput).getAttribute("value"), partnersUrl, "Additional Info: Partner's API Link is incorrect");
        if (csvColumnsMap != null){
            WebElement dropdown;
            for (Entry<endpointPartnerApiColumnsEnum, String> csvEntry : csvColumnsMap.entrySet()){
                dropdown = Select.selectByAttributeContains(syncUrlCsvColumnSelect, "name", csvEntry.getKey().name().toLowerCase());
                softAssert.assertEquals(dropdown.getText(), csvEntry.getValue(), "Additional info: CSV column setting " + csvEntry.getKey() + " is incorrect");
            }
        } else {
            softAssert.assertTrue(AssertUtil.assertNotPresent(syncUrlCsvColumnSelect), "Additional info: CSV column setting is displayed on XML/JSON after saving");
        }
        urlEscapedExpected = urlPatternExpected.replace("<$fileformat$>", "json").replace(".", "\\.");
        softAssert.assertTrue(jsonUrlActual.matches(urlEscapedExpected), "Additional info page: Endpoint statistics JSON link is incorrect. Expected: <" + urlEscapedExpected + ">. Actual: <" + jsonUrlActual + ">");
        urlEscapedExpected = urlPatternExpected.replace("<$fileformat$>", "xml").replace(".", "\\.");
        softAssert.assertTrue(xmlUrlActual.matches(urlEscapedExpected), "Additional info page: Endpoint statistics XML link is incorrect. Expected: <" + urlEscapedExpected + ">. Actual: <" + jsonUrlActual + ">");
        urlEscapedExpected = urlPatternExpected.replace("<$fileformat$>", "csv").replace(".", "\\.");
        softAssert.assertTrue(csvUrlActual.matches(urlEscapedExpected), "Additional info page: Endpoint statistics CSV link is incorrect. Expected: <" + urlEscapedExpected + ">. Actual: <" + jsonUrlActual + ">");
    }

    @Step("Assert validation modal in Patner's API")
    public void assertValidationModalApiSync(String expectedMessage){
        softAssert.assertEquals(Select.selector(modalValidationContentText).getText(), expectedMessage, "Validation modal text is incorrect");
    }

    //</editor-fold>

    //<editor-fold desc="Modals">
    static final By
            modalDialog = By.xpath("//div[@role='dialog']"),
            modalInputField = By.xpath("//div[@role='dialog']//input[@type='text']"),
            modalCloseButton = By.xpath(".//div[@class='modal-footer']/a[contains(@class, 'modal-close')]"),
            modalConfirmButton = By.xpath("//div[@role='dialog']//button[contains(@class, 'confirm')]"),
            modalCancelButton = By.xpath("//div[@role='dialog']//button[contains(@class, 'cancel')]"),
            modalHeaderText = By.xpath("//div[@role='dialog']//h2"),
            modalContentText = By.xpath("//div[@role='dialog']//div[@id='swal2-content']"),
            modalValidationContentText = By.xpath(".//div[@id='swal2-validation-message']"),
            macrosModal = By.xpath("//div[@id='viewTagManager']"),
            macrosModalRow = By.xpath("//div[@id='viewTagManager']//tr[@data-name]"),
            macrosModalAddButton = relative_generalLink;

    @Step("Close modal")
    public void modalClickClose(By modalElement) {
        WebElement closeButton = Select.selector(modalElement, modalCloseButton);
        closeButton.click();
        Wait.waitForStaleness(closeButton);
    }

    @Step("Close modal")
    public void modalClickCancel(By modalElement) {
        WebElement cancelButton = Select.selector(modalElement, modalCancelButton);
        cancelButton.click();
        Wait.waitForStaleness(cancelButton);
    }

    @Step("Close modal")
    public void modalClickCancel(){
        WebElement cancelButton = Select.selector(modalCancelButton);
        cancelButton.click();
        Wait.waitForStaleness(cancelButton);
    }

    @Step("Click on OK button on error Pop Up")
    public void modalClickConfirm(By modalElement) {
        WebElement confirmButton = Select.selector(modalElement, modalConfirmButton);
        confirmButton.click();
        Wait.waitForStaleness(confirmButton);
    }

    @Step("Submit modal")
    public void modalClickConfirm(){
        WebElement confirmButton = Select.selector(modalConfirmButton);
        confirmButton.click();
        Wait.waitForStaleness(confirmButton);
    }

    @Step("Assert modal")
    public void assertModal(SoftAssertCustom sAssert, String modalHeader, String modalBody, String modalName) {
        sAssert.assertEquals(Select.selector(modalHeaderText).getText(), modalHeader, modalName+" modal header is incorrect");
        sAssert.assertEquals(Select.selector(modalContentText).getText(), modalBody, modalName + "modal body is incorrect");
    }

    //</editor-fold>

    //<editor-fold desc="Tables">
    static final By
            tableBlock = By.xpath("//table"),
            tableColumnHeaderSortIcon = By.xpath("./span/i");

    @Step("Get table cell")
    public WebElement getTableCell(WebElement row, String attributeValue){
        return Select.selectByAttributeContains(row, relative_generalTableBodyCell, "data-field", attributeValue);
    }

    //</editor-fold>

    //<editor-fold desc="Paging">
    static final By pagingPageNumberButton = By.xpath(".//ul[@class='pagination']/li/a[@data-go][not(@aria-label)]"),
            pagingEntriesCount = By.xpath(".//span[@class='text-line-paginate']"),
            relative_pagingPageNumberParent = By.xpath("./parent::span|li");

    @Step("Go to the selected page of the list")
    public void pagingActionSelect(pagingActionsTypes pagingAction, String pageNumber){
        if (AssertUtil.assertPresent(pagingPageNumberButton)) {
            List<WebElement> pages = Select.multiSelector(pagingPageNumberButton);
            WebElement activePage = Select.selector(pagingPageNumberButton, By.xpath("./../..//li[@class='active']"));
            switch (pagingAction){
                case FIRST -> {
                    pages.get(0).click();
                }
                case LAST -> {
                    WebElement lastPageElement = pages.get(pages.size() - 1);
                    lastPageElement.click();
                    pageNumber = Select.selector(lastPageElement, relative_pagingPageNumberParent).getText();
                    Wait.waitForStaleness(lastPageElement);
                    lastPageElement = Select.selectByTextEquals(pagingPageNumberButton, pageNumber);
                    Wait.attributeToBe(Select.selector(lastPageElement, relative_pagingPageNumberParent), "class", "active");
                }
                case NUMBER -> {
                }
                case FORWARD -> {
                    int currentIndex = Integer.parseInt(activePage.getText()) - 1;
                    WebElement nextPage = pages.get(currentIndex + 1);
                    nextPage.click();
                    Wait.waitForStaleness(nextPage);
                }
                case BACKWARD -> pages.get(0).click();
            }
        } else {
            System.err.println("There are no pages!");
        }
    }

    @Step("Go to the selected page of the list")
    public void pagingActionSelect(WebElement parentBlock, pagingActionsTypes pagingAction, String pageNumber){
        if (AssertUtil.assertPresent(parentBlock, pagingPageNumberButton)) {
            WebElement activePage = Select.selector(Select.selector(parentBlock, pagingPageNumberButton), By.xpath("./ancestor::li[@class='active']"));
            List<WebElement> pages = Select.multiSelector(parentBlock, pagingPageNumberButton);
            pages.removeIf(s -> !s.getText().matches("\\d"));
            switch (pagingAction){
                case FIRST -> {
                    pages.get(0).click();
                }
                case LAST -> {
                    WebElement lastPageElement = pages.get(pages.size() - 1);
                    lastPageElement.click();
                    pageNumber = Select.selector(lastPageElement, relative_pagingPageNumberParent).getText();
                    Wait.waitForStaleness(lastPageElement);
                    lastPageElement = Select.selectByTextEquals(parentBlock, pagingPageNumberButton, pageNumber);
                    Wait.attributeToBe(Select.selector(lastPageElement, relative_pagingPageNumberParent), "class", "active");
                }
                case NUMBER -> {
                }
                case FORWARD -> {
                    int currentIndex = Integer.parseInt(activePage.getText()) - 1;
                    WebElement nextPage = pages.get(currentIndex + 1);
                    nextPage.click();
                    Wait.waitForStaleness(nextPage);
                }
                case BACKWARD -> pages.get(0).click();
            }
        } else {
            System.err.println("There are no pages!");
        }
    }

    @Step("Get number of pages")
    public int getPagingCount(){
        if (AssertUtil.assertPresent(pagingPageNumberButton)) {
            List<WebElement> pages = Select.multiSelector(pagingPageNumberButton);
            pages.removeIf(s -> !s.getText().matches("\\d"));
            return Integer.parseInt(pages.get(pages.size() - 1).getText());
        } else {
            System.err.println("There are no pages!");
            return 0;
        }
    }

    @Step("Get number of current page")
    public Integer getCurrentPage(){
        if (AssertUtil.assertPresent(pagingPageNumberButton)) {
            WebElement activePage = Select.selector(pagingPageNumberButton, By.xpath("./../..//li[@class='active']"));
            return Integer.parseInt(activePage.getText());
        } else {
            return 0;
        }
    }

    @Step("Get number of entries per page")
    public Map<String, Integer> getPagingEntriesCount(){
        Map<String, Integer> entriesCount = new HashMap<>();
        String entriesString = Select.selector(pagingEntriesCount).getText();
        int countFrom, countTo, countTotal;
        countFrom = Integer.parseInt(entriesString.substring(8, entriesString.indexOf("to")).replaceAll("[\\D]", ""));
        countTo = Integer.parseInt(entriesString.substring(entriesString.indexOf("to"), entriesString.indexOf("of")).replaceAll("[\\D]", ""));
        countTotal = Integer.parseInt(entriesString.substring(entriesString.indexOf("of"), entriesString.indexOf("entries")).replaceAll("[\\D]", ""));
        entriesCount.put("from", countFrom);
        entriesCount.put("to", countTo);
        entriesCount.put("total", countTotal);
        return entriesCount;
    }

    //</editor-fold>

    //<editor-fold desc="Database actions">
    @Step("Get available regions")
    public List<regionsEnum> getRegions(){
        List<regionsEnum> regionsReturn = new ArrayList<>();
        Result<org.jooq.Record> results = dbUtils.getDbDataResult("regions", null);
        hardAssert.assertTrue(results.isNotEmpty(), "No regions found in the database");
        for (org.jooq.Record item : results) {
            regionsReturn.add(regionsEnum.valueOf(item.get("title", String.class)));
        }
        return regionsReturn;
    }

    @Step("Get region server address")
    public String getRegionAddress(regionsEnum region){
        Condition condition = DSL.field("title").equal(region.name());
        org.jooq.Record result = dbUtils.getDbDataRecordFirst("_regions", condition);
        hardAssert.assertNotNull(result, "Region <" + region.name() + "> not found");
        return result.getValue("address", String.class);
    }

    @Step("Get random company name from DSP/SSP endpoints")
    public String getRandomCompanyNameFromExistingEndpoints(endpointSideEnum side) {
        String endpointTable = (side == endpointSideEnum.DSP) ? "dsp" : "ssp_endpoint";
        Condition conditionEndpoints = DSL.field("company_id").notEqual(1);
        Result<org.jooq.Record> dbResultEndpoints = dbUtils.getDbDataResult(endpointTable, conditionEndpoints);
        List<Integer> companyIds = dbResultEndpoints.getValues("company_id", Integer.class);
        Integer companyId = (Integer) RandomUtil.getRandomElement(companyIds);
        Condition conditionCompanies = DSL.field("id").equal(companyId);
        org.jooq.Record companyRecord = dbUtils.getDbDataRecordFirst("companies", conditionCompanies);
        return companyRecord.get("name", String.class);
    }

    @Step("Get endpoints by company")
    public List<String> getEndpointsListByCompany(endpointSideEnum side, String companyName){
        List<String> companyIds = new ArrayList<>();
        String endpoint = (side == endpointSideEnum.DSP) ? "dsp" : "ssp_endpoint";
        String[] selectFields = {endpoint + ".id", endpoint + ".name"};
        Condition conditionJoin = DSL.field(endpoint + ".company_id").equal(DSL.field("companies.id")), conditionWhere = DSL.field("companies.name").equal(companyName);
        Result<org.jooq.Record> resultsJoin = dbUtils.getDbDataResultInnerJoin(selectFields, endpoint, "companies", conditionJoin, conditionWhere);
        System.out.println(resultsJoin.getValues(endpoint + ".name", String.class));
        for (org.jooq.Record item : resultsJoin) {
            companyIds.add(item.get(endpoint + ".name", String.class) + " #" + item.get(endpoint + ".id", Integer.class));
        }
        return companyIds;
    }

    //</editor-fold>

    @Step("Reload page")
    public void refreshPage(By elementToWait){
        getDriver().navigate().refresh();
        Wait.waitForVisibility(elementToWait);
    }

    @Step("Assert author info in email")
    public void assertEmailAuthor(String userEmail, String emailSubject, String authorName, String authorEmail){
        EmailDO mail = EmailUtil.getEmailByRecipientTitle(userEmail, emailSubject);
        softAssert.assertEquals(mail.getAuthorName() + " <" + mail.getAuthorEmail() + ">", authorName + " <" + authorEmail + ">", "Field 'FROM' in email is incorrect");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Errors in Email:");
        }
    }

}
