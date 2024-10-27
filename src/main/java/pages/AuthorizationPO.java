package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.AssertUtility;
import common.utils.EmailUtility;
import common.utils.ParseAndFormatUtility;
import common.utils.RandomUtility;
import data.dataobject.UserProfileDO;
import data.textstrings.messages.AuthorizationText;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final EmailUtility EmailUtil = new EmailUtility();
    private final Selectors select = new Selectors();
    private final Waits wait = new Waits();
    private final ParseAndFormatUtility ParseUtil = new ParseAndFormatUtility();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final RandomUtility RandomUtil = new RandomUtility();

    public AuthorizationPO(SoftAssertCustom sAssert) {
        super(sAssert);
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public AuthorizationPO() {
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    //<editor-fold desc="Login">
    static final By
            emailInput = By.xpath("//input[@name='email']"),
            emailLabel = By.xpath("//label[@for='email']"),
            passwordInput = By.xpath("//input[@name='password']"),
            rememberMeCheckbox = By.xpath("//input[@name='remember']"),
            rememberMeCheckboxLabel = By.xpath("//label[@for='remember']"),
            forgotPasswordButton = By.xpath("//a[@id='forgot']"),
            signUpButton = By.xpath("//a[@id='signup']"),
            signInErrorLabel = By.xpath("//div[contains(@class, 'center status')]");

    @Step("Login as {mail}")
    public void login(String mail, String pass) {
        enterInInput(emailInput, mail);
        enterInInput(passwordInput, pass);
        select.selector(submitButton).click();
        wait.waitForVisibility(logoutButton);
    }

    @Step("Login as {user}")
    public void login(UserProfileDO user) {
        enterInInput(emailInput, user.getEmail());
        enterInInput(passwordInput, user.getPassword());
        select.selector(submitButton).click();
        wait.waitForVisibility(logoutButton);
    }

    @Step("Input Login Data")
    public void inputLoginData(String mail, String pass) {
        enterInInput(emailInput, mail);
        enterInInput(passwordInput, pass);
    }

    @Step("Login with expected errors")
    public void loginWithErrors(String mail, String pass) {
        enterInInput(emailInput, mail);
        enterInInput(passwordInput, pass);
        select.selector(submitButton).click();
        wait.attributeNotContains(submitButton, "class", "disabled");
    }

    @Step("Assert Sign In Errors")
    public void assertSignInErrors(String errorText) {
        hardAssert.assertEquals(select.selector(signInErrorLabel).getText(), errorText, "Sign in error message incorrect");
    }

    @Step("Assert sign in validation errors")
    public void assertSignInValidationErrors(String emailError, String passwordError, String mainError) {
        assertValidationError(softAssert, emailInput, emailError);
        assertValidationError(softAssert, passwordInput, passwordError);
        softAssert.assertEquals(select.selector(signInErrorLabel).getText(), mainError, "Sign in error message incorrect");
    }

    @Step("Click Sign Up and Wait reCaptcha")
    public void clickSignInWithCaptcha() {
        for (int x = 0; x <= 3; x++) {
            clickSubmitWithErrors();
        }
        clickSubmitWithErrors();
        driver.switchTo().frame(select.selector(recaptchaIFrame));
        wait.waitForVisibility(recaptchaCheckbox);
        driver.switchTo().defaultContent();
    }

    @Step("Log In With Recaptcha")
    public void loginWithRecaptcha(String mail, String pass) {
        enterInInput(emailInput, mail);
        enterInInput(passwordInput, pass);
        clickRecaptcha();
        select.selector(submitButton).click();
        wait.waitForVisibility(logoutButton);
    }

    @Step("Logout")
    public void logout() {
        select.selector(logoutButton).click();
        wait.waitForClickable(signUpButton);
    }

    @Step("Assert login page")
    public void assertLoginPage() {
        softAssert.assertTrue(AssertUtil.assertPresent(emailInput), "Email input is not present");
        softAssert.assertTrue(AssertUtil.assertPresent(passwordInput), "Password input is not present");
        softAssert.assertTrue(AssertUtil.assertPresent(rememberMeCheckboxLabel), "Remember Me checkbox is not present");
        softAssert.assertTrue(AssertUtil.assertPresent(forgotPasswordButton), "Forgot Password button is not present");
        softAssert.assertTrue(AssertUtil.assertPresent(signUpButton), "Sign Up button is not present");
        softAssert.assertTrue(AssertUtil.assertPresent(submitButton), "Login button input is not present");
    }

    //</editor-fold>

    //<editor-fold desc="Registration">

    static final By
            firstNameInput = By.xpath("//input[@name='firstName']"),
            lastNameInput = By.xpath("//input[@name='lastName']"),
            retypePasswordInput = By.xpath("//input[@name='password_confirmation']"),
            companyInput = By.xpath("//input[@name='company']"),
            countrySelect = By.xpath("//select[@id='country']"),
            privacyPolicyCheckbox = By.xpath("//input[@id='tnc']"),
            privacyPolicyCheckboxLabel = By.xpath("//label[@for='tnc']"),
            publisherUserTypeLabel = By.xpath("//label[@for='type_user_publisher']"),
            sspUserTypeLabel = By.xpath("//label[@for='type_user_ssp']"),
            dspUserTypeLabel = By.xpath("//label[@for='type_user_dsp']"),
            signUpConfirmHeader = By.xpath("//h5"),
            signUpConfirmMessage = By.xpath("//p"),
            recaptchaIFrame = By.xpath("//iframe[@title='reCAPTCHA']"),
            recaptchaCheckbox = By.xpath("//span[@id='recaptcha-anchor']"),
            recaptchaCheckmark = By.xpath("//div[@class='recaptcha-checkbox-checkmark'][@style]"),
            backButton = By.xpath("//a[@class='btn-flat left'][1]");

    @Step("Click on the Create Account Button")
    public void clickCreateAccount() {
        select.selector(signUpButton).click();
        wait.waitForVisibility(firstNameInput);
    }

    @Step("Fill In Registration Form")
    public void inputUserFields(UserProfileDO userData, String retypePassword, boolean privacyPolicy) {
        enterInInput(firstNameInput, userData.getFirstName());
        enterInInput(lastNameInput, userData.getLastName());
        enterInInput(emailInput, userData.getEmail());
        enterInInput(passwordInput, userData.getPassword());
        enterInInput(retypePasswordInput, retypePassword);
        if (userData.getCountry() != null) {
            select.selector(countrySelect, relative_selectLabel).click();
            select.selectByAttributeIgnoreCase(countrySelect, relative_selectOptionWithTitle, "title", userData.getCountry()).click();
        }
        switch (userData.getRole()) {
            case PUBL -> select.selector(publisherUserTypeLabel).click();
            case PUBL_SSP -> select.selector(sspUserTypeLabel).click();
            case PUBL_DSP -> select.selector(dspUserTypeLabel).click();
        }
        enterInInput(companyInput, userData.getCompany());
        int elementWidth = select.selector(privacyPolicyCheckboxLabel).getSize().width;
        if (select.selector(privacyPolicyCheckbox).isSelected() != privacyPolicy) {
            select.actionClickWithAdjust(privacyPolicyCheckboxLabel, -elementWidth / 4, 0);
        }
    }

    @Step("Assert Sign Up Confirmation Page")
    public void assertSignUpConfirmation() {
        softAssert.assertEquals(select.selector(signUpConfirmHeader).getText(), AuthorizationText.SIGNUP_SUCCESS_HEADER, "Header isn't correct");
        softAssert.assertEquals(select.selector(signUpConfirmMessage).getText(), AuthorizationText.SIGNUP_SUCCESS_MESSAGE.replace("${platformUrl}", getRunURL()), "Message isn't correct");
        softAssert.assertAll("Errors on registration confirm page");
    }

    @Step("Approve Recaptcha")
    public void clickRecaptcha() {
        wait.waitIFrameToLoadAndSwitch(recaptchaIFrame);
        select.selector(recaptchaCheckbox).click();
        wait.attributeToBe(recaptchaCheckbox, "aria-checked", "true");
        driver.switchTo().defaultContent();
    }

    @Step("Click Sign Up")
    public void clickSignUp() {
        select.selector(submitButton).click();
        wait.waitForVisibility(signUpConfirmHeader);
    }

    @Step("Open registration confirmation link in email")
    public void clickConfirmationLink(String userEmail) {
        String mailBody, parsedURL;
        mailBody = EmailUtil.getEmailByRecipientTitle(userEmail, "Confirmation of registration").getBodyHtml();
        parsedURL = ParseUtil.parseByRegex(mailBody, getRunURL() + "/sign-up/[a-z0-9]{32}", 0);
        hardAssert.assertNotNull(parsedURL, "Confirmation URL not found");
        driver.navigate().to(getRunURL());
        wait.waitForClickable(logoutButton);
    }

    @Step("Click Submit with errors")
    public void clickSubmitWithErrors() {
        select.selector(submitButton).click();
        wait.attributeNotContains(submitButton, "class", "disabled");
    }

    @Step("Assert Sign Up Errors")
    public void assertSignUpErrors() {
        softAssert.assertEquals(select.selector(emailLabel).getAttribute("data-error"), AuthorizationText.SIGNUP_ERROR_ALREADY_REGISTERED, "Email error message incorrect");
        softAssert.assertAll("Sign up errors incorrect");
    }

    @Step("Assert Sign Up Errors")
    public void assertSignUpErrors(String firstNameError, String lastNameError, String emailError, String passwordError, String retypePasswordError, String companyError, String privacyPolicyError, String mainError) {
        assertValidationError(softAssert, firstNameInput, firstNameError);
        assertValidationError(softAssert, lastNameInput, lastNameError);
        assertValidationError(softAssert, emailInput, emailError);
        assertValidationError(softAssert, passwordInput, passwordError);
        assertValidationError(softAssert, retypePasswordInput, retypePasswordError);
        assertValidationError(softAssert, companyInput, companyError);
        assertValidationError(softAssert, privacyPolicyCheckboxLabel, privacyPolicyError);
        softAssert.assertEquals(select.selector(signInErrorLabel).getText(), mainError, "Sign up error message incorrect");
    }

    @Step("Click on Back button")
    public void clickBackButton() {
        select.selector(backButton).click();
        wait.waitForVisibility(forgotPasswordButton);
    }

    @Step("Get random DSP")
    public String getRandomCountry() {
        List<String> countryNames = new ArrayList<>();
        for (WebElement option : select.multiSelector(countrySelect, relative_selectOptionWithTitle)) {
            countryNames.add(option.getAttribute("title"));
        }
        return (String) RandomUtil.getRandomElement(countryNames);
    }
    //</editor-fold>

    //<editor-fold desc="Reset Password">
    @Step("Fill In Mail for Reset Password ")
    public void inputMail(String mail) {
        enterInInput(emailInput, mail);
    }

    @Step("Click Forgot Password")
    public void clickForgotPassword() {
        select.selector(forgotPasswordButton).click();
        wait.waitForNotVisible(passwordInput);
    }

    @Step("Click Reset Password")
    public void clickResetPassword() {
        select.selector(submitButton).click();
        wait.waitForVisibility(signUpConfirmHeader);
    }

    @Step("Assert Password Recovery")
    public void assertPasswordRecovery() {
        String header = "The password recovery!";
        String creatingMessage = "The password recovery link was sent to your email address. To change your password, please follow instructions from the email.";
        softAssert.assertEquals(select.selector(signUpConfirmHeader).getText(), header, "Header isn't correct");
        softAssert.assertEquals(select.selector(signUpConfirmMessage).getText(), creatingMessage, "Message isn't correct");
        softAssert.assertAll("Errors on password recovery page");
    }

    @Step("Fill in new password")
    public void inputNewPassword(String password, String retypePassword) {
        wait.waitForVisibility(retypePasswordInput);
        enterInInput(passwordInput, password);
        enterInInput(retypePasswordInput, retypePassword);
    }

    @Step("Click New Password Button")
    public void clickNewPasswordButton() {
        select.selector(submitButton).click();
        wait.waitForVisibility(dashboardSectionButton);
    }

    @Step("Open change password link in email")
    public void clickResetPasswordLink(String userEmail) {
        String mailBody, parsedURL;
        mailBody = EmailUtil.getEmailByRecipientTitle(userEmail, "Password recovery").getBodyHtml();
        parsedURL = ParseUtil.parseByRegex(mailBody, getRunURL() + "/[a-z0-9]{32}/reset-password", 0);
        hardAssert.assertNotNull(parsedURL, "Restore URL not found");
        driver.get(parsedURL);
        wait.waitForVisibility(retypePasswordInput);
    }

    //</editor-fold>

    @Step("Assert user is in Admin role")
    public void assertUserIsAdmin() {
        softAssert.assertTrue(AssertUtil.assertPresent(adminMenuButton), "Admin button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(select.selector(adminLoginAsUserSelect, relative_selectLabel)), "User selection is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(editUserProfileButton), "'Edit selected user profile' button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(inventoryPendingApprovalButton), "Pending approval button is not displayed");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Admin interface is incorrect:");
        }
    }

    @Step("Assert user is in Publisher role")
    public void assertUserIsPublisher() {
        softAssert.assertTrue(AssertUtil.assertNotPresent(adminMenuButton), "Admin button is displayed for Publisher");
        softAssert.assertTrue(AssertUtil.assertNotPresent(adminLoginAsUserSelect, relative_selectLabel), "User selection is displayed for Publisher");
        softAssert.assertTrue(AssertUtil.assertNotPresent(editUserProfileButton), "'Edit selected user profile' button is displayed for Publisher");
        softAssert.assertTrue(AssertUtil.assertNotPresent(inventoryPendingApprovalButton), "Pending approval button is displayed for Publisher");
        softAssert.assertTrue(AssertUtil.assertPresent(dashboardSectionButton), "Dashboard button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(inventorySectionButton), "Inventory button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(reportMenuSectionButton), "Report button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(dealsPublisherSectionButton), "Deals button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(transactionHistorySectionButton), "Transaction history button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(cashoutSettingsSectionButton), "Cashout settings button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(managementAPImenuSectionButton), "Management API button is not displayed");
        if (isSoftAssertLocal) {
            softAssert.assertAll("Publisher interface is incorrect:");
        }
    }

    @Step("Assert user is in SSP role")
    public void assertUserIsSsp() {
        softAssert.assertTrue(AssertUtil.assertNotPresent(adminMenuButton), "Admin button is displayed for SSP");
        softAssert.assertTrue(AssertUtil.assertNotPresent(adminLoginAsUserSelect, relative_selectLabel), "User selection is displayed for SSP");
        softAssert.assertTrue(AssertUtil.assertNotPresent(editUserProfileButton), "'Edit selected user profile' button is displayed for SSP");
        softAssert.assertTrue(AssertUtil.assertNotPresent(inventoryPendingApprovalButton), "Pending approval button displayed for SSP");
        softAssert.assertTrue(AssertUtil.assertPresent(dashboardSectionButton), "Dashboard button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(sspSectionButton), "SSP button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(reportUserSspDspUserSectionButton), "Report button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(sspReportAPISectionButton), "Report API button is not displayed");
        if (isSoftAssertLocal) {
            softAssert.assertAll("SSP interface is incorrect:");
        }
    }

    @Step("Assert user is in DSP role")
    public void assertUserIsDsp() {
        softAssert.assertTrue(AssertUtil.assertNotPresent(adminMenuButton), "Admin button is displayed for DSP");
        softAssert.assertTrue(AssertUtil.assertNotPresent(adminLoginAsUserSelect, relative_selectLabel), "User selection is displayed for DSP");
        softAssert.assertTrue(AssertUtil.assertNotPresent(editUserProfileButton), "'Edit selected user profile' button is displayed for DSP");
        softAssert.assertTrue(AssertUtil.assertNotPresent(inventoryPendingApprovalButton), "Pending approval button displayed for DSP");
        softAssert.assertTrue(AssertUtil.assertPresent(dashboardSectionButton), "Dashboard button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(dspSectionButton), "DSP button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(reportUserSspDspUserSectionButton), "Report button is not displayed");
        softAssert.assertTrue(AssertUtil.assertPresent(dspReportAPISectionButton), "Report Api button is not displayed");
        if (isSoftAssertLocal) {
            softAssert.assertAll("DSP interface is incorrect:");
        }
    }
}
