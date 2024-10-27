package pages;

import common.Selectors;
import common.SoftAssertCustom;
import common.Waits;
import common.utils.AssertUtility;
import common.utils.ParseAndFormatUtility;
import common.utils.RandomUtility;
import data.UserEnum;
import data.dataobject.UserProfileDO;
import io.qameta.allure.Step;
import org.jooq.Record;
import org.jooq.Result;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.*;

import static data.textstrings.messages.UserManagementText.*;

public class UserManagementPO extends CommonElementsPO {
    private final SoftAssertCustom softAssert;
    private final boolean isSoftAssertLocal;
    private final Selectors Select = new Selectors();
    private final Waits Wait = new Waits();
    private final AssertUtility AssertUtil = new AssertUtility(driver);
    private final ParseAndFormatUtility FormatUtil = new ParseAndFormatUtility();
    private final RandomUtility RandomUtil = new RandomUtility();

    public UserManagementPO(SoftAssertCustom sAssert) {
        this.softAssert = sAssert;
        this.isSoftAssertLocal = false;
    }

    public UserManagementPO(){
        this.softAssert = new SoftAssertCustom();
        this.isSoftAssertLocal = true;
    }

    @Step("Open Manage Users page")
    public void gotoManageUsersSection(){
        openAdminMenu();
        clickAdministrationWrap(true);
        Select.selector(userManagementLink).click();
        Wait.waitForClickable(createUserButton);
        Wait.attributeContains(preloaderTable, "class", "hide");
    }

    @Step("Get user data from database")
    public List<UserProfileDO> getUserDataFromDb(){
        List<UserProfileDO> userList = new ArrayList<>();
        Result<Record> dbUsers = dbUtils.getDbDataResult("users", null);
        for (Record user : dbUsers) {
            UserProfileDO userData = new UserProfileDO();
            userData.setUserId(user.getValue("id", Integer.class));
            userData.setEmail(user.getValue("email", String.class));
            userData.setFirstName(user.getValue("firstName", String.class));
            userData.setLastName(user.getValue("lastName", String.class));
            userList.add(userData);
        }
        return userList;
    }

    //<editor-fold desc="Users table">
    static final By
            createUserButton = By.xpath("//div[@class='page-actions']/a"),
            usersTotalCount = By.xpath("//div[@class='page-actions']/span"),
            userListSearchInput = By.xpath("//input[@id='search']"),
            userListRow = By.xpath("//table//tr[@data-id]"),
            userListId = By.xpath("./td[@data-field='id']"),
            userListEmail = By.xpath("./td[@data-field='email']"),
            userListName = By.xpath("./td[@data-field='f_name']"),
            userListCompany = By.xpath("./td[@data-field='company']"),
            userListRole = By.xpath("./td[@data-field='role']"),
            userListLastLogin = By.xpath("./td[@data-field='lastlogin']"),
            userListLoginCount = By.xpath("./td[@data-field='lastlogin']/small"),
            userListBalance = By.xpath("./td[@data-field='balance']"),
            userListRevenue = By.xpath("./td[@data-field='total_revenue']"),
            userListTrafficIcon = By.xpath("./td[@data-field='action']/a[@class='type-link']"),
            userListLoginAsButton = By.xpath("./td[@data-field='action']/a[contains(@class, 'users')]"),
            userListSettingsButton = By.xpath("./td[@data-field='action']/a[contains(@href, 'edit')]"),
            userListComment = By.xpath("./td[@data-field='id']/a"),
            userListCommentModal = By.xpath("//div[@id='comment']"),
            userListCommentModalHeader = By.xpath("//div[@id='comment']//h4"),
            userListCommentModalText = By.xpath("//div[@id='comment']//p"),
            userListCommentModalCancelButton = By.xpath("//div[@id='comment']//a");

    @Step("Get user row")
    public WebElement getUserRow(Integer id){
        WebElement userRow = Select.selectByAttributeExact(userListRow, "data-id", id.toString());
        hardAssert.assertNotNull(userRow, "User with ID <" + id + "> not found");
        return userRow;
    }

    @Step("Get user ID from database")
    public Integer getUserId(String userEmail){
        WebElement row = Select.selectParentByTextContains(userListRow, userListEmail, userEmail);
        Integer userId = Integer.parseInt(Select.selector(row, userListId).getText());
        hardAssert.assertNotNull(userId, "User with email <" + userEmail + "> not found");
        return userId;
    }

    @Step("Search user by name/email/id")
    public void searchUser(String query){
        searchItemInTable(query, userListSearchInput, tableBlock);
    }

    @Step("Assert search results")
    public void assertSearchResults(boolean isSearchById, String query){
        List<WebElement> userRowsInTable = Select.multiSelector(userListRow);
        if (isSearchById){
            List<String> userIds = new ArrayList<>();
            for (WebElement row : userRowsInTable){
                userIds.add(Select.selector(row, userListId).getText());
            }
            softAssert.assertTrue(userIds.contains(query), "Error: User with ID <" + query + "> not found");
        } else {
            for (WebElement row : userRowsInTable){
                String uid = Select.selector(row, userListId).getText();
                softAssert.assertTrue(Select.selector(row, userListEmail).getText().toLowerCase().contains(query.toLowerCase()) || Select.selector(row, userListName).getText().toLowerCase().contains(query.toLowerCase()), "Error for user [" + uid + "]: Search query was not found in email or name");
            }
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Search results are incorrect:");
        }
    }

    @Step("Open Create user page")
    public void clickCreateNewUser(){
        Select.selector(createUserButton).click();
        Wait.waitForClickable(saveUserButton);
    }

    @Step("Open user settings page")
    public void clickUserSettings(Integer userId){
        WebElement row = getUserRow(userId);
        Select.selector(row, userListSettingsButton).click();
        Wait.waitForClickable(saveUserButton);
    }

    @Step("Get total users count")
    public Integer getUsersTotalCount(){
        return Integer.parseInt(Select.selector(usersTotalCount).getText());
    }

    //TODO separate methods of getting each field could be replaced by single method that returns all fields in map
    @Step("Get user name from the list")
    public String getFullNameFromList(int userId){
        WebElement row = getUserRow(userId);
        return Select.selector(row, userListName).getText();
    }

    @Step("Get user email from the list")
    public String getEmailFromList(int userId){
        WebElement row = getUserRow(userId);
        return Select.selector(row, userListEmail).getText();
    }

    @Step("Get user email from the list")
    public String getCompanyFromList(int userId){
        WebElement row = getUserRow(userId);
        return Select.selector(row, userListCompany).getText();
    }

    @Step("Assert total users count")
    public void assertUsersTotalCount(Integer expectedCount){
        hardAssert.assertEquals(getUsersTotalCount(), expectedCount, "Total users count is incorrect");
    }

    @Step("Assert user in the list")
    public void assertUserRow(UserProfileDO userData){
        WebElement row = getUserRow(userData.getUserId());
        String lastLogin = (userData.getLastLoginDate() == null) ? "Never" : userData.getLastLoginDate("yyyy/MM/dd", Locale.ENGLISH);
        softAssert.assertEquals(Select.selector(row, userListId).getText().trim(), userData.getUserId().toString(), "User ID is incorrect");
        softAssert.assertEquals(Select.selector(row, userListEmail).getText(), userData.getEmail(), "User email is incorrect");
        softAssert.assertEquals(Select.selector(row, userListName).getText(), userData.getFullName(), "User name is incorrect");
        softAssert.assertEquals(Select.selector(row, userListCompany).getText(), userData.getCompany(), "User company is incorrect");
        softAssert.assertEquals(Select.selector(row, userListRole).getText(), userData.getRole().publicName(), "User role is incorrect");
        softAssert.assertTrue(Select.selector(row, userListLastLogin).getText().startsWith(lastLogin), "User last login is incorrect, expected: <" + lastLogin + ">");
        if (userData.getLoginCount() != null){
            softAssert.assertEquals(Select.selector(row, userListLoginCount).getText().replace("Login Count:", "").trim(), userData.getLoginCount().toString(), "Login count is incorrect");
        }
        softAssert.assertEquals(Select.selector(row, userListBalance).getText(), "$" + FormatUtil.formatPriceWithZeros(userData.getBalance()), "User balance is incorrect");
        softAssert.assertEquals(Select.selector(row, userListRevenue).getText(), "$" + FormatUtil.formatPriceWithZeros(userData.getRevenue()), "User revenue is incorrect");
        softAssert.assertEquals(!AssertUtil.assertNotPresent(row, userListTrafficIcon), userData.getTrafficStatus().booleanValue(), "Traffic status is incorrect");
        if (userData.getLoginAsUser() != null){
            softAssert.assertEquals(!Select.selector(row, userListLoginAsButton).getAttribute("class").contains("disabled"), userData.getLoginAsUser().booleanValue(), "Login as button status is incorrect");
        } else {
            softAssert.assertTrue(AssertUtil.assertNotPresent(row, userListLoginAsButton), "Login as is displayed");
        }
        softAssert.assertEquals(AssertUtil.assertPresent(row, userListSettingsButton), true, "Edit button is not displayed");
        //TODO asserting comments is unstable, temporary disabled
        //        if (userData.comments != null){
        //            Select.selector(row, userListComment).click();
        //            Wait.waitForClickable(userListCommentModalCancelButton);
        //            softAssert.assertEquals(Select.selector(userListCommentModal, userListCommentModalHeader).getText(), MODAL_HEADER_COMMENT, "Manager comment header is incorrect");
        //            softAssert.assertEquals(Select.selector(userListCommentModal, userListCommentModalText).getText(), userData.comments, "Manager comment text is incorrect");
        //            Select.selector(userListCommentModalCancelButton).click();
        //            Wait.waitForNotVisible(userListCommentModal);
        //        }
        if (isSoftAssertLocal){
            softAssert.assertAll("User info in table is incorrect:");
        }
    }

    //</editor-fold>

    //<editor-fold desc="User creation">
    static final By
            firstNameInput = By.xpath("//input[@name='firstName']"),
            lastNameInput = By.xpath("//input[@name='lastName']"),
            emailInput = By.xpath("//input[@name='email']"),
            companyInput = By.xpath("//input[@name='company']"),
            passwordInput = By.xpath("//input[@name='password']"),
            passwordGenerateButton = By.xpath("//a[@data-set='#password']"),
            countrySelect = By.xpath("//select[@name='country']"),
            roleSelect = By.xpath("//select[@name='role']"),
            skypeInput = By.xpath("//input[@name='contact_skype']"),
            additionalEmailInput = By.xpath("//input[@name='contact_email']"),
            managerCommentsInput = By.xpath("//textarea[@name='manager_comment']"),
            sellerIdInput = By.xpath("//input[@name='seller_id']"),
            businessDomainInput = By.xpath("//input[@name='domain']"),
            dynamicMarginInput = By.xpath("//input[@name='dynamic_margin_value']"),
            apiReportLink = By.xpath("//div[@id='report-api-link']/code"),
            saveUserButton = submitButton;

    @Step("Setup user data on creation")
    public void setupUserCreation(UserProfileDO userData){
        inputFirstName(userData.getFirstName());
        inputLastName(userData.getLastName());
        inputEmail(userData.getEmail());
        inputCompany(userData.getCompany());
        inputPassword(userData.getPassword());
        userData.setCountry(selectRandomCountry());
        selectRole(userData.getRole());
        inputSkype(userData.getSkype());
        inputAdditionalEmail(userData.getEmailAdditional());
        inputManagerComments(userData.getComments());
        toggleAdditionalSettings(userData.getAdditionalSettings());
        if (userData.getRole().equals(UserEnum.userRoleEnum.PUBL)){
            inputSellerId(userData.getSellerId());
            inputBusinessDomain(userData.getBusinessDomain());
            if (userData.getDynamicMargin() != null){
                inputDynamicMargin(userData.getDynamicMargin());
            }
        }
    }

    @Step("Input First name")
    public void inputFirstName(String firstName){
        enterInInput(firstNameInput, firstName);
    }

    @Step("Input Last name")
    public void inputLastName(String lastName){
        enterInInput(lastNameInput, lastName);
    }

    @Step("Input Email")
    public void inputEmail(String email){
        enterInInput(emailInput, email);
    }

    @Step("Input Company")
    public void inputCompany(String company){
        enterInInput(companyInput, company);
    }

    @Step("Input Password")
    public void inputPassword(String password){
        enterInInput(passwordInput, password);
    }

    @Step("Generate Password")
    public void generatePassword(){
        String oldPassword = Select.selector(passwordInput).getAttribute("value");
        Select.selector(passwordGenerateButton).click();
        Wait.attributeNotToBe(passwordInput, "value", oldPassword);
    }

    @Step("Select Country")
    public String selectRandomCountry(){
        String returnCountry;
        List<String> countryNames = new ArrayList<>();
        for (WebElement option : Select.multiSelector(countrySelect, relative_selectOptionWithTitle)) {
            countryNames.add(option.getAttribute("title"));
        }
        returnCountry = (String) RandomUtil.getRandomElement(countryNames);
        selectSingleSelectDropdownOption(countrySelect, returnCountry);
        return returnCountry;
    }

    @Step("Select Role")
    public void selectRole(UserEnum.userRoleEnum role){
        selectSingleSelectDropdownOption(roleSelect, role.publicName());
    }

    @Step("Input Skype")
    public void inputSkype(String skype){
        enterInInput(skypeInput, skype);
    }

    @Step("Input Additional email")
    public void inputAdditionalEmail(String additionalEmail){
        enterInInput(additionalEmailInput, additionalEmail);
    }

    @Step("Input Manager comments")
    public void inputManagerComments(String managerComments){
        enterInInput(managerCommentsInput, managerComments);
    }

    @Step("Input Seller ID")
    public void inputSellerId(String sellerId){
        enterInInput(sellerIdInput, sellerId);
    }

    @Step("Input Business domain")
    public void inputBusinessDomain(String businessDomain){
        enterInInput(businessDomainInput, businessDomain);
    }

    @Step("Input Dynamic Margin")
    public void inputDynamicMargin(Double dynamicMargin){
        enterInInput(dynamicMarginInput, dynamicMargin);
    }

    @Step("Set user additional settings")
    public void toggleAdditionalSettings(Map<UserEnum.additionalSettings, Boolean> settings){
        for (Map.Entry<UserEnum.additionalSettings, Boolean> setting : settings.entrySet()){
            WebElement toggle = getToggleByName(toggleInput, setting.getKey().attributeName());
            clickToggle(toggle, setting.getValue());
            if (setting.getKey().equals(UserEnum.additionalSettings.MARGIN) && setting.getValue()){
                Wait.waitForVisibility(dynamicMarginInput);
            }
        }
    }

    @Step("Save user")
    public void clickSaveUser(){
        Select.selector(saveUserButton).click();
        Wait.waitForVisibility(userListRow);
    }

    //</editor-fold>

    //<editor-fold desc="User editing">
    static final By
            editUserBalanceCount = By.xpath("//div[contains(@class, 'left-card')]/div[contains(@class, 'center-align')][1]//div[1]/div[@class='count']/span"),
            editUserRevenueCount = By.xpath("//div[contains(@class, 'left-card')]/div[contains(@class, 'center-align')][1]//div[2]/div[@class='count']/span"),
            editUserInventoriesCount = By.xpath("//div[contains(@class, 'left-card')]/div[contains(@class, 'center-align')][2]//div[1]/div[@class='count']/span"),
            editUserPlacementsCount = By.xpath("//div[contains(@class, 'left-card')]/div[contains(@class, 'center-align')][2]//div[2]/div[@class='count']/span"),
            editUserActivateButton = By.xpath("//a[contains(@class, 'activate')]"),
            editUserLoginAsButton = By.xpath("//a[contains(@class, 'loginAs')]"),
            editUserResendButton = By.xpath("//a[contains(@class, 'reset-code')]"),
            editUserResetPasswordButton = By.xpath("//a[contains(@class, 'reset-password')]"),
            editUserDeactivateButton = By.xpath("//a[@data-action='delete']"),
            editUserRestoreButton = By.xpath("//a[@data-action = 'restore']"),
            editUserCreatedDate = By.xpath("//div[contains(@class, 'bot')][1]/div/p[1]"),
            editUserDeletedDate = By.xpath("//div[contains(@class, 'bot')][1]/div/p[2]"),
            editUserLastLoginDate = By.xpath("//div[contains(@class, 'bot')][1]/div/p[3]"),
            editUserLoginCount = By.xpath("//div[contains(@class, 'bot')][1]/div/p[4]"),
            editUserInfoCardLine = By.xpath("//div[contains(@class, 'bot')][1]/div/p"),
            editUserInfoCardHeader = By.xpath("//div[contains(@class, 'left-card')][1]//div//span[@class='bb']"),
            editUserAboutHeader = By.xpath("//div[contains(@class, 'left-card')][2]//div//span[@class='bb']"),
            editUserAboutSkypeName = By.xpath("//li[@class='collection-item avatar']/p");

    @Step("Click on \"Activate user\" and confirm modal")
    public void clickActivateUser(boolean activateConfirm){
        Select.selector(editUserActivateButton).click();
        Wait.waitForClickable(modalConfirmButton);
        assertModal(softAssert, MODAL_CONFIRM_HEADER, MODAL_TEXT_ACTIVATION_CONFIRM, "Activation confirm");
        if (activateConfirm){
            Select.selector(modalConfirmButton).click();
            Wait.waitForClickable(modalConfirmButton);
            assertModal(softAssert, MODAL_SUCCESS_HEADER, MODAL_TEXT_ACTIVATION_SUCCESS, "Activation success");
            modalClickConfirm();
            Wait.waitForNotVisible(editUserActivateButton);
        } else {
            modalClickCancel(modalDialog);
            Wait.waitForClickable(editUserActivateButton);
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Modal is incorrect:");
        }
    }

    @Step("Click on \"Deactivate user\" and confirm modal")
    public void clickDeactivateUser(boolean deactivateConfirm){
        Select.selector(editUserDeactivateButton).click();
        Wait.waitForClickable(modalConfirmButton);
        softAssert.assertEquals(Select.selector(modalHeaderText).getText(), MODAL_CONFIRM_HEADER, "Confirm Modal header is incorrect");
        softAssert.assertEquals(Select.selector(modalContentText).getText(), MODAL_TEXT_DEACTIVATION_CONFIRM, "Confirm Modal text is incorrect");
        if (deactivateConfirm){
            modalClickConfirm(modalDialog);
            Wait.waitForClickable(modalConfirmButton);
            softAssert.assertEquals(Select.selector(modalHeaderText).getText(), MODAL_SUCCESS_HEADER, "Success Modal header is incorrect");
            softAssert.assertEquals(Select.selector(modalContentText).getText(), MODAL_TEXT_DEACTIVATION_SUCCESS, "Success Modal text is incorrect");
            modalClickConfirm(modalDialog);
            Wait.waitForNotVisible(editUserDeactivateButton);
        } else {
            modalClickCancel(modalDialog);
            Wait.waitForClickable(editUserDeactivateButton);
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("Modal is incorrect:");
        }
    }

    @Step("Assert user edit page - main info")
    public void assertEditPageUserSettings(UserProfileDO userData){
        Map<String, Boolean> settingsExpected = userData.getAdditionalSettingsAttributes(), settingsActual = new HashMap<>();
        for (WebElement setting : Select.multiSelector(toggleInput)){
            if (Select.selector(setting, relative_toggleLever).isDisplayed()){
                settingsActual.put(setting.getAttribute("name"), setting.isSelected());
            }
        }
        softAssert.assertEquals(Select.selector(roleSelect, relative_selectLabel).getText(), userData.getRole().publicName(), "Role is incorrect");
        softAssert.assertEquals(Select.selector(firstNameInput).getAttribute("value"), userData.getFirstName(), "First name is incorrect");
        softAssert.assertEquals(Select.selector(lastNameInput).getAttribute("value"), userData.getLastName(), "Last name is incorrect");
        softAssert.assertEquals(Select.selector(emailInput).getAttribute("value"), userData.getEmail(), "Email is incorrect");
        softAssert.assertTrue(Select.selector(passwordInput).getAttribute("value").isEmpty(), "Password field is not empty");
        softAssert.assertEquals(Select.selector(companyInput).getAttribute("value"), userData.getCompany(), "Company is incorrect");
        softAssert.assertEquals(Select.selector(countrySelect, relative_selectLabel).getText(), userData.getCountry(), "Country is incorrect");
        softAssert.assertEquals(Select.selector(additionalEmailInput).getAttribute("value"), userData.getEmailAdditional(), "Additional email is incorrect");
        softAssert.assertEquals(Select.selector(managerCommentsInput).getAttribute("value"), userData.getComments(), "Manager comments is incorrect");
        if (userData.getSellerId() != null){
            softAssert.assertEquals(Select.selector(sellerIdInput).getAttribute("value"), userData.getSellerId(), "Seller ID is incorrect");
        }
        if (userData.getBusinessDomain() != null){
            softAssert.assertEquals(Select.selector(businessDomainInput).getAttribute("value"), userData.getBusinessDomain(), "Business domain is incorrect");
        }
        if (userData.getSkype() != null){
            softAssert.assertEquals(Select.selector(editUserAboutHeader).getText(), "About " + userData.getFullName(), "About header is incorrect");
            softAssert.assertEquals(Select.selector(editUserAboutSkypeName).getText(), userData.getSkype(), "Skype name is incorrect");
        }
        softAssert.assertEquals(settingsActual, settingsExpected, "Toggle settings are incorrect");
        for (Map.Entry<UserEnum.additionalSettings, Boolean> setting : userData.getAdditionalSettings().entrySet()){
            if (setting.getKey().equals(UserEnum.additionalSettings.MARGIN) && setting.getValue()){
                softAssert.assertEquals(Select.selector(dynamicMarginInput).getAttribute("value"), FormatUtil.formatPriceNoZeros(userData.getDynamicMargin()), "Dynamic margin is incorrect");
            }
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("User [" + userData.getEmail() + "] edit page is incorrect:");
        }
    }

    @Step("Assert user edit page - info card")
    public void assertEditPageUserInfoCard(UserProfileDO userData){
        softAssert.assertEquals(Select.selector(editUserInfoCardHeader).getText(), userData.getFullName(), "Full name is incorrect");
        softAssert.assertEquals(Select.selector(editUserBalanceCount).getText(), "$" + FormatUtil.formatPriceWithZeros(userData.getBalance()), "Balance is incorrect");
        softAssert.assertEquals(Select.selector(editUserRevenueCount).getText(), "$" + FormatUtil.formatPriceWithZeros(userData.getRevenue()), "Revenue is incorrect");
        softAssert.assertTrue(Select.selectByTextContains(editUserInfoCardLine, "Account created").getText().replace("Account created:", "").trim().startsWith(userData.getCreateDate()), "Created date is incorrect. Expected: <" + userData.getCreateDate() + ">");
        if (userData.getRole() == UserEnum.userRoleEnum.PUBL){
            softAssert.assertEquals(Select.selector(editUserInventoriesCount).getText(), userData.getInventoriesCount().toString(), "Inventories count is incorrect");
            softAssert.assertEquals(Select.selector(editUserPlacementsCount).getText(), userData.getInventoriesCount().toString(), "Placements count is incorrect");
        }
        if (userData.getLastLoginDate() != null){
            softAssert.assertTrue(Select.selectByTextContains(editUserInfoCardLine, "Last login").getText().replace("Last login:", "").trim().startsWith(userData.getLastLoginDate()), "Last login date is incorrect. Expected: <" + userData.getLastLoginDate() + ">");
        }
        if (userData.getLoginCount() != null){
            softAssert.assertEquals(Select.selectByTextContains(editUserInfoCardLine, "Login count").getText().replace("Login count:", "").trim(), userData.getLoginCount().toString(), "Login count is incorrect. Expected: <" + userData.getLoginCount() + ">");
        }
        if (userData.getActivated() != null){
            if (userData.getActivated()){
                softAssert.assertTrue(AssertUtil.assertPresent(editUserLoginAsButton), "Login As button is not present");
                softAssert.assertTrue(AssertUtil.assertPresent(editUserResetPasswordButton), "Reset password button is not present");
                softAssert.assertTrue(AssertUtil.assertPresent(editUserDeactivateButton), "Deactivate button is not present");
            } else {
                softAssert.assertTrue(AssertUtil.assertPresent(editUserActivateButton), "Activate button is not present");
                softAssert.assertTrue(AssertUtil.assertPresent(editUserResendButton), "Resend URL button is not present");
                softAssert.assertTrue(AssertUtil.assertPresent(editUserDeactivateButton), "Deactivate button is not present");
                softAssert.assertTrue(AssertUtil.assertNotPresent(editUserLastLoginDate), "Last login date is displayed");
                softAssert.assertTrue(AssertUtil.assertNotPresent(editUserLoginCount), "Login count is displayed");
                softAssert.assertTrue(AssertUtil.assertNotPresent(editUserDeletedDate), "Deleted date is displayed for not activated user");
            }
        }
        if (userData.getDeleted()){
            softAssert.assertTrue(AssertUtil.assertPresent(editUserRestoreButton), "Restore User button is not present");
            softAssert.assertTrue(Select.selectByTextContains(editUserInfoCardLine, "Account deleted").getText().replace("Account deleted:", "").trim().startsWith(userData.getDeleteDate()), "Deleted date is incorrect. Expected: <" + userData.getDeleteDate() + ">");
            softAssert.assertTrue(AssertUtil.assertNotPresent(editUserLoginAsButton), "Login As button is present");
            softAssert.assertTrue(AssertUtil.assertNotPresent(editUserActivateButton), "Activate button is present");
            softAssert.assertTrue(AssertUtil.assertNotPresent(editUserDeactivateButton), "Deactivate button is present");
        } else {
            softAssert.assertTrue(AssertUtil.assertPresent(editUserDeactivateButton), "Deactivate button is not present");
        }
        if (isSoftAssertLocal){
            softAssert.assertAll("User info card is incorrect:");
        }
    }

    //</editor-fold>

}
