package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.AssertUtility;
import data.CommonEnums;
import data.textstrings.messages.AuthorizationText;
import data.textstrings.messages.InventoryText;
import data.textstrings.messages.PlatformSettingsText;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static data.textstrings.messages.CommonText.VALIDATION_REQUIRED_EMPTY;

public class PlatformSettingsPO extends CommonElementsPO {
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;

    public PlatformSettingsPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public PlatformSettingsPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Go to Platform Settings")
    public void gotoPlatformSettingsSection() {
        openAdminMenu();
        clickAdministrationWrap(true);
        Select.selector(platformSettingsLink).click();
        Wait.waitForClickable(submitButton);
    }

    static final By
            companyNameInput = By.xpath("//input[@id='site_name']"),
            companyTypeInput = By.xpath("//input[@id='site_type']"),
            sellersJsonEmailInput = By.xpath("//input[@id='seller_contact_email']"),
            touInput = By.xpath("//input[@name='client[links][terms_of_use]']"),
            pubAgreeInput = By.xpath("//input[@name='client[links][publishers_agreement]']"),
            privacyInput = By.xpath("//input[@name='client[links][privacy_policy]']"),
            tosInput = By.xpath("//input[@name='client[links][terms_of_service]']"),
            supportNameInput = By.xpath("//input[@id='support_name']"),
            supportEmailInput = By.xpath("//input[@id='support_email']"),
            managerNameInput = By.xpath("//input[@id='manager_name']"),
            managerEmailInput = By.xpath("//input[@id='manager_email']"),
            publishersNameInput = By.xpath("//input[@id='publishers_name']"),
            publishersEmailInput = By.xpath("//input[@id='publishers_email']"),
            cashOutPeriodInput = By.xpath("//input[@id='setting_cashout_period']"),
            minBalancePayPalInput = By.xpath("//input[@id='setting_min_balance_paypal']"),
            minBalanceWTInput = By.xpath("//input[@id='setting_min_balance_wt']"),
            featureForABTestDropDown = By.xpath("//select[@id='ab_test_feature']"),
            hasABTestToggle = By.xpath("//input[@id='has_ab_test']"),
            abTestRate = By.xpath("//input[@id='ab_test_rate']"),
            abTestRateBlock = By.xpath("//div[@id='ab_test_rate_block']"),
            abTestRateValue = By.xpath("//span[@id='ab_test_rate_value']"),
            customDomainInput = By.xpath("//input[@id='custom_domain']"),
            gdprToggle = By.xpath("//input[@id='is_gdpr_on']"),
            gdprVendor = By.xpath("//div[@id='gdpr_vendor']//input[@id='vendor_id']");

    @Step("SetUp Site Settings")
    public void setupSiteSettings(String name, String type, String email) {
        enterInInput(companyNameInput, name);
        enterInInput(companyTypeInput, type);
        enterInInput(sellersJsonEmailInput, email);
    }

    @Step("SetUp Links")
    public void setupLinks(String useLink, String agreementLink, String privacyLink, String serviceLink) {
        enterInInput(touInput, useLink);
        enterInInput(pubAgreeInput, agreementLink);
        enterInInput(privacyInput, privacyLink);
        enterInInput(tosInput, serviceLink);
    }

    @Step("Setup support settings")
    public void setupSupportSettings(String supportName, String supportEmail) {
        enterInInput(supportNameInput, supportName);
        enterInInput(supportEmailInput, supportEmail);
    }

    @Step("Setup manager settings")
    public void setupMangerSettings(String managerName, String managerEmail) {
        enterInInput(managerNameInput, managerName);
        enterInInput(managerEmailInput, managerEmail);
    }

    @Step("Setup publisher team settings")
    public void setupPublisherTeamSettings(String publishersName, String publishersEmail) {
        enterInInput(publishersNameInput, publishersName);
        enterInInput(publishersEmailInput, publishersEmail);
    }

    @Step("Setup cash out settings")
    public void setupCashOut(Integer cashOutPeriodDays, Double payPalMinBalance, Double wtMinBalance) {
        enterInInput(cashOutPeriodInput, cashOutPeriodDays);
        enterInInput(minBalancePayPalInput, payPalMinBalance);
        enterInInput(minBalanceWTInput, wtMinBalance);
    }

    @Step("Save platform settings")
    public void clickSaveSettings() {
        Select.selector(submitButton).click();
        Wait.waitForVisibility(preloader);
        Wait.waitForNotVisible(preloader);
    }

    @Step("Assert Site Settings section")
    public void assertSiteSettings(String name, String type, String email) {
        softAssert.assertEquals(Select.selector(companyNameInput).getAttribute("value"), name, "Platform settings: Site name is not correct");
        softAssert.assertEquals(Select.selector(companyTypeInput).getAttribute("value"), type, "Site type is not correct");
        softAssert.assertEquals(Select.selector(sellersJsonEmailInput).getAttribute("value"), email, "Sellers.json contact email is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Site settings section is not correct");
        }
    }

    @Step("Assert Links section")
    public void assertLinks(String useLink, String agreementLink, String privacyLink, String serviceLink) {
        softAssert.assertEquals(Select.selector(touInput).getAttribute("value"), useLink, "ToU link is not correct");
        softAssert.assertEquals(Select.selector(pubAgreeInput).getAttribute("value"), agreementLink, "Pub agreement links not correct");
        softAssert.assertEquals(Select.selector(privacyInput).getAttribute("value"), privacyLink, "Privacy link is not correct");
        softAssert.assertEquals(Select.selector(tosInput).getAttribute("value"), serviceLink, "ToS link is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Links section is not correct");
        }
    }

    @Step("Assert cash out section")
    public void assertCashOutSettings(int cashOutPeriodDays, double minBalancePayPal, double minBalanceWT) {
        softAssert.assertEquals(Select.selector(cashOutPeriodInput).getAttribute("value"), String.valueOf(cashOutPeriodDays), "CashOut period days is not correct");
        softAssert.assertEquals(Select.selector(minBalancePayPalInput).getAttribute("value"), String.valueOf(minBalancePayPal), "Min balance pay pal is not correct");
        softAssert.assertEquals(Select.selector(minBalanceWTInput).getAttribute("value"), String.valueOf(minBalanceWT), "Min balance WT is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Cashout settings section is not correct");
        }
    }

    @Step("Assert support settings")
    public void assertSupportSettings(String supportName, String supportEmail) {
        softAssert.assertEquals(Select.selector(supportNameInput).getAttribute("value"), supportName, "Support name is not correct");
        softAssert.assertEquals(Select.selector(supportEmailInput).getAttribute("value"), supportEmail, "Support email is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Support settings section is not correct");
        }
    }

    @Step("Assert manager settings")
    public void assertManagerSettings(String managerName, String managerEmail) {
        softAssert.assertEquals(Select.selector(managerNameInput).getAttribute("value"), managerName, "Manager name is not correct");
        softAssert.assertEquals(Select.selector(managerEmailInput).getAttribute("value"), managerEmail, "Manager email is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Manager settings section is not correct");
        }
    }

    @Step("Assert publisher settings")
    public void assertPublisherSettings(String publishersName, String publisherEmail) {
        softAssert.assertEquals(Select.selector(publishersNameInput).getAttribute("value"), publishersName, "Publisher name is not correct");
        softAssert.assertEquals(Select.selector(publishersEmailInput).getAttribute("value"), publisherEmail, "Publisher email is not correct");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Publisher settings section is not correct");
        }
    }

    @Step("Get toggle status")
    public boolean getGdprStatus() {
        return Select.selector(gdprToggle).isSelected();
    }

    @Step("Set gdpr vendor value")
    public void setGdprSettings(boolean status, String vendorValue) {
        clickToggle(gdprToggle, status);
        if (status) {
            enterInInput(gdprVendor, vendorValue);
        }
    }

    @Step("Assert vendor value")
    public void assertGdprSection(boolean statusExpected, String vendorValueExpected) {
        softAssert.assertEquals(Select.selector(gdprToggle).isSelected(), statusExpected, "GDPR Status is not valid");
        softAssert.assertEquals(Select.selector(gdprVendor).getAttribute("value"), vendorValueExpected, "GDPR Vendor value is not valid");
    }

    @Step("SetUp Custom Domain Settings")
    public void setCustomDomainInput(String domain) {
        enterInInput(customDomainInput, domain);
    }

    @Step("Assert custom domain")
    public void assertCustomDomainSettings(String domain) {
        softAssert.assertEquals(Select.selector(customDomainInput).getAttribute("value"), domain, "Custom domain is not correct");
    }

    @Step("Click on AB toggle")
    public void clickABTestToggle(boolean enable) {
        clickToggle(hasABTestToggle, enable);
    }

    @Step("Move test rate")
    public void setupABTestRate(int newRate) {
        String value = Select.selector(abTestRate).getAttribute("value");
        int currentRate = Integer.parseInt(value);
        if (currentRate < newRate) {
            for (int i = currentRate; i < newRate; i++) {
                Select.selector(abTestRate).sendKeys(Keys.RIGHT);
            }
        } else {
            for (int i = currentRate; i > newRate; i--) {
                Select.selector(abTestRate).sendKeys(Keys.LEFT);
            }
        }
    }

    public CommonEnums.abTestEnum getAbTestSelected() {
        return CommonEnums.abTestEnum.getValueByPublicName(Select.selector(featureForABTestDropDown, relative_selectLabel).getText());
    }

    @Step("Change feature for AB test")
    public void selectAbTestFeature(CommonEnums.abTestEnum value) {
        selectSingleSelectDropdownOption(featureForABTestDropDown, value.publicName());
    }

    @Step("Assert A/B testing tool")
    public void assertAbTestSection(CommonEnums.abTestEnum selectedFeature, boolean status, Integer rate) {
        softAssert.assertEquals(Select.selector(featureForABTestDropDown, relative_selectLabel).getText(), selectedFeature.publicName(), "selected feature is incorrect");
        softAssert.assertEquals(Select.selector(hasABTestToggle).isSelected(), status, "Status incorrect");
        if (status) {
            if (AssertUtil.assertPresent(Select.selector(abTestRate))) {
                softAssert.assertEquals(Select.selector(abTestRateValue).getText(), rate + "%", "Rate value is incorrect");
            } else {
                softAssert.fail("AB Test Rate is not present");
            }
        } else {
            softAssert.assertTrue(AssertUtil.assertPresent(Select.selector(abTestRateValue)), "Rate value is present");
            softAssert.assertTrue(AssertUtil.assertPresent(Select.selector(abTestRate)), "Rate input is present");
        }
    }

    @Step("Click save button with invalid data")
    public void clickSaveWithErrors() {
        Select.selector(submitButton).click();
        Wait.waitForNotVisible(preloader);
    }

    @Step("Assert Required Site Settings")
    public void assertSiteSettingsRequiredFields() {
        softAssert.assertTrue(Select.selector(companyNameInput).getAttribute("class").contains("invalid"), "Seller json validation is not displayed");
        softAssert.assertTrue(Select.selector(sellersJsonEmailInput).getAttribute("class").contains("invalid"), "Seller contact email validation id not displayed");
        assertValidationError(softAssert, companyNameInput, VALIDATION_REQUIRED_EMPTY);
        assertValidationError(softAssert, sellersJsonEmailInput, VALIDATION_REQUIRED_EMPTY);
    }

    @Step("Assert Required Support Settings")
    public void assertSupportSettingsRequiredFields() {
        softAssert.assertTrue(Select.selector(supportNameInput).getAttribute("class").contains("invalid"), "Support name validation is not displayed");
        softAssert.assertTrue(Select.selector(supportEmailInput).getAttribute("class").contains("invalid"), "Support email validation is not displayed");
        assertValidationError(softAssert, supportNameInput, VALIDATION_REQUIRED_EMPTY);
        assertValidationError(softAssert, supportEmailInput, VALIDATION_REQUIRED_EMPTY);
    }

    @Step("Assert Company Name length")
    public void assertCompanyNameLength(String wrongLengthName) {
        String nameInInput = Select.selector(companyNameInput).getAttribute("value");
        softAssert.assertEquals(nameInInput, wrongLengthName.substring(0, 40), "Company name has wrong length");
    }

    @Step("Assert Company Type Length")
    public void assertCompanyTypeLength(String wrongLengthName) {
        String nameInInput = Select.selector(companyTypeInput).getAttribute("value");
        softAssert.assertEquals(nameInInput, wrongLengthName.substring(0, 10), "Company type has wrong length");
    }

    @Step("Assert Emails")
    public void assertEmailsHasValidations(String sellersJsonEmail, String supportEmail, String managerEmail, String publisherEmail) {
        String sellerJsonEmailClasses = Select.selector(sellersJsonEmailInput).getAttribute("class");
        String supportEmailClasses = Select.selector(supportEmailInput).getAttribute("class");
        String managerEmailClasses = Select.selector(managerEmailInput).getAttribute("class");
        String publisherTeamClasses = Select.selector(publishersEmailInput).getAttribute("class");
        softAssert.assertTrue(sellerJsonEmailClasses.contains("invalid"), "Sellers json email validation is not displayed");
        softAssert.assertTrue(supportEmailClasses.contains("invalid"), "Support email validation is not displayed");
        softAssert.assertTrue(managerEmailClasses.contains("invalid"), "Manager email validation is not displayed");
        softAssert.assertTrue(publisherTeamClasses.contains("invalid"), "Publisher team email validation is not displayed");
        String sellersJsonErrorMsg = AuthorizationText.EMAIL_VALIDATION_ERROR_NO_AT.replace("${emailAddress}", sellersJsonEmail);
        String supportEmailErrorMsg = AuthorizationText.EMAIL_VALIDATION_ERROR_NO_AT.replace("${emailAddress}", supportEmail);
        String managerEmailErrorMsg = AuthorizationText.EMAIL_VALIDATION_ERROR_NO_AT.replace("${emailAddress}", managerEmail);
        String publisherEmailErrorMsg = AuthorizationText.EMAIL_VALIDATION_ERROR_NO_AT.replace("${emailAddress}", publisherEmail);
        assertValidationError(softAssert, sellersJsonEmailInput, sellersJsonErrorMsg);
        assertValidationError(softAssert, supportEmailInput, supportEmailErrorMsg);
        assertValidationError(softAssert, managerEmailInput, managerEmailErrorMsg);
        assertValidationError(softAssert, publishersEmailInput, publisherEmailErrorMsg);
    }

    @Step("Assert CashOut field")
    public void assertCashoutValidation() {
        String cashoutPeriodClasses = Select.selector(cashOutPeriodInput).getAttribute("class");
        String minBalancePayPalClasses = Select.selector(minBalancePayPalInput).getAttribute("class");
        String minBalanceWTClasses = Select.selector(minBalanceWTInput).getAttribute("class");
        softAssert.assertTrue(cashoutPeriodClasses.contains("invalid"), "CashOut period validation is not displayed");
        softAssert.assertTrue(minBalancePayPalClasses.contains("invalid"), "CashOut payPal min balance is not displayed");
        softAssert.assertTrue(minBalanceWTClasses.contains("invalid"), "CashOut WT min balance validation is not displayed");
        assertValidationError(softAssert, cashOutPeriodInput, PlatformSettingsText.CASHEOUT_PERIOD_REQUIRED);
        assertValidationError(softAssert, minBalancePayPalInput, PlatformSettingsText.PAYPAL_CASHEOUT_MIN_BALANCE);
        assertValidationError(softAssert, minBalanceWTInput, PlatformSettingsText.WT_CASHEOUT_MIN_BALANCE);
    }

    @Step("Assert CashOut Period")
    public void assertCashoutPeriodValidation(int cashoutValue) {
        String cashoutPeriodClasses = Select.selector(cashOutPeriodInput).getAttribute("class");
        softAssert.assertTrue(cashoutPeriodClasses.contains("invalid"), "CashOut period validation is not displayed");
        if (cashoutValue < 30) {
            assertValidationError(softAssert, cashOutPeriodInput, PlatformSettingsText.CACHEOUT_MIN);
        } else {
            assertValidationError(softAssert, cashOutPeriodInput, PlatformSettingsText.CACHEOUT_MAX);
        }
    }

    @Step("Assert CashOut Payments")
    public void assertCashoutPaymentsValidation() {
        String minBalancePayPalClasses = Select.selector(minBalancePayPalInput).getAttribute("class");
        String minBalanceWTClasses = Select.selector(minBalanceWTInput).getAttribute("class");
        softAssert.assertTrue(minBalancePayPalClasses.contains("invalid"), "CashOut payPal min balance validation is not displayed");
        softAssert.assertTrue(minBalanceWTClasses.contains("invalid"), "CashOut WT min balance validation is not displayed");
        assertValidationError(softAssert, minBalancePayPalInput, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0,01"));
        assertValidationError(softAssert, minBalanceWTInput, InventoryText.VALIDATION_VALUE_TOO_LOW.replace("${nmb}", "0,01"));
    }

    @Step("Assert Links")
    public void assertLinksValidation() {
        String touClasses = Select.selector(touInput).getAttribute("class");
        String privacyClasses = Select.selector(privacyInput).getAttribute("class");
        String pubClasses = Select.selector(pubAgreeInput).getAttribute("class");
        String tosClasses = Select.selector(tosInput).getAttribute("class");
        softAssert.assertTrue(touClasses.contains("invalid"), "ToU validation is not displayed");
        softAssert.assertTrue(privacyClasses.contains("invalid"), "Privacy validation is not displayed");
        softAssert.assertTrue(pubClasses.contains("invalid"), "Pub validation is not displayed");
        softAssert.assertTrue(tosClasses.contains("invalid"), "ToS validation is not displayed");
        assertValidationError(softAssert, touInput, relative_selectValidationDiv, PlatformSettingsText.URL_REQUIRED);
        assertValidationError(softAssert, privacyInput, relative_selectValidationDiv, PlatformSettingsText.URL_REQUIRED);
        assertValidationError(softAssert, pubAgreeInput, relative_selectValidationDiv, PlatformSettingsText.URL_REQUIRED);
        assertValidationError(softAssert, tosInput, relative_selectValidationDiv, PlatformSettingsText.URL_REQUIRED);
    }

    @Step("Assert custom domain")
    public void assertCustomDomainValidation() {
        assertValidationError(softAssert, customDomainInput, relative_selectValidationDiv, PlatformSettingsText.VALID_DOMAIN_NAME);
    }
}
