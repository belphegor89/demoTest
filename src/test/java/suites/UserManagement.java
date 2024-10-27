package suites;

import common.SoftAssertCustom;
import common.utils.BasicUtility;
import common.utils.RandomUtility;
import data.SellersAdsEnums;
import data.StaticData;
import data.StaticData.loginData;
import data.UserEnum.additionalSettings;
import data.UserEnum.userRoleEnum;
import data.dataobject.UserProfileDO;
import data.textstrings.messages.AuthorizationText;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.SellersJsonPO;
import pages.UserManagementPO;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import static data.StaticData.getEmailAddress;

@Epic("Admin section")
@Feature("Administration")
@Story("User Management")
public class UserManagement extends BaseSuiteClassNew {
    private UserProfileDO userToDeactivate;

    //<editor-fold desc="User creation">
    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3881")
    @Description("Create Admin user on the 'Manage users' page")
    public void createUserAdmin() {
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO();
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000), usersCountBefore, userId;
        UserProfileDO userData = new UserProfileDO(userRoleEnum.ADM, "firstNameAdm" + rnd, "lastNameAdm" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compAdm" + rnd, "admAddMail" + rnd + "@smarty.tst", "skpAdm" + rnd, "manager comments" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        usersCountBefore = users.getUsersTotalCount();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        String country = users.selectRandomCountry();
        userData.setCountry(country);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime());
        users.assertUsersTotalCount(usersCountBefore + 1);
        userId = users.getUserId(userData.getEmail());
        userData.setUserId(userId);
        users.assertUserRow(userData);
        users.clickUserSettings(userId);
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.clickConfirmationLink(userData.getEmail());
        login.assertUserIsAdmin();
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsAdmin();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3882")
    @Description("Create SSP user on the 'Manage users' page")
    public void createUserSsp() {
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO();
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000), usersCountBefore;
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_SSP, "firstNameSsp" + rnd, "lastNameSsp" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compSsp" + rnd, "", "sspAddMail" + rnd + "@smarty.tst", "skpSsp" + rnd, "manager comments" + rnd).setUserAdditionalSettings(additionalSettings.ADS, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        usersCountBefore = users.getUsersTotalCount();
        users.clickCreateNewUser();
        userData.setCountry(users.selectRandomCountry());
        users.setupUserCreation(userData);
        users.toggleAdditionalSettings(userData.getAdditionalSettings());
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime());
        users.assertUsersTotalCount(usersCountBefore + 1);
        userData.setUserId(users.getUserId(userData.getEmail()));
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserInfoCard(userData);
        users.assertEditPageUserSettings(userData);
        login.logout();
        login.clickConfirmationLink(userData.getEmail());
        login.assertUserIsSsp();
        login.logout();
        login.login(userData);
        login.assertUserIsSsp();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3883")
    @Description("Create DSP user on the 'Manage users' page")
    public void createUserDsp() {
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO();
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000), usersCountBefore, userId;
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_DSP, "firstNameDsp" + rnd, "lastNameDsp" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compDsp" + rnd, "dspAddMail" + rnd + "@smarty.tst", "skpDsp" + rnd, "manager comments" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        usersCountBefore = users.getUsersTotalCount();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        String country = users.selectRandomCountry();
        userData.setCountry(country);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime());
        users.assertUsersTotalCount(usersCountBefore + 1);
        userId = users.getUserId(userData.getEmail());
        userData.setUserId(userId);
        users.assertUserRow(userData);
        users.clickUserSettings(userId);
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.clickConfirmationLink(userData.getEmail());
        login.assertUserIsDsp();
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsDsp();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("3884")
    @Description("Create Publisher user on the 'Manage users' page")
    public void createUserPublisher() {
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO();
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000), usersCountBefore, userId;
        double margin = randomUtil.getRandomDoubleRounded(0, 100, 2);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL, "firstNamePub" + rnd, "lastNamePub" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compPub" + rnd, "pubAddMail" + rnd + "@smarty.tst", "skpPub" + rnd, "manager comments" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true).setUserAdditionalSettings(additionalSettings.MARGIN, true).setDynamicMargin(margin).setBusinessDomain("bus.dom" + rnd + ".com").setSellerId("seLLer-" + rnd + "-pUb");
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        usersCountBefore = users.getUsersTotalCount();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        String country = users.selectRandomCountry();
        userData.setCountry(country);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime());
        users.assertUsersTotalCount(usersCountBefore + 1);
        userId = users.getUserId(userData.getEmail());
        userData.setUserId(userId);
        users.assertUserRow(userData);
        users.clickUserSettings(userId);
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.clickConfirmationLink(userData.getEmail());
        login.assertUserIsPublisher();
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsPublisher();
    }
    //</editor-fold>

    //<editor-fold desc="User activation and disable">
    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3885")
    @Description("Activate registered user from the User profile")
    public void userActivateCreatedByUser() {
        UserManagementPO users = new UserManagementPO();
        AuthorizationPO registration = new AuthorizationPO();
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL, "firstNamePub" + rnd, "lastNamePub" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compPub" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        registration.clickCreateAccount();
        userData.setCountry(registration.getRandomCountry());
        registration.inputUserFields(userData, userData.getPassword(), true);
        registration.clickRecaptcha();
        registration.clickSignUp();
        registration.assertSignUpConfirmation();
        registration.clickBackButton();
        registration.loginWithErrors(userData.getEmail(), userData.getPassword());
        registration.assertSignInErrors(AuthorizationText.SIGNIN_ERROR_NOT_ACTIVATED);
        registration.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        userData.setUserId(users.getUserId(userData.getEmail()));
        userData.setCreateDate(util.getCurrentDateTime());
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserInfoCard(userData);
        users.assertEditPageUserSettings(userData);
        users.clickActivateUser(true);
        userData.setActivated(true);
        users.assertEditPageUserInfoCard(userData);
        registration.logout();
        registration.login(userData.getEmail(), userData.getPassword());
        registration.assertUserIsPublisher();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3896")
    @Description("Activate user, created by Admin, from the User profile")
    public void userActivateCreatedByAdmin() {
        UserManagementPO users = new UserManagementPO();
        AuthorizationPO login = new AuthorizationPO();
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(1, 10000), usersCountBefore;
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_SSP, "firstNameSsp" + rnd, "lastNameSsp" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compSsp" + rnd, "", "sspAddMail" + rnd + "@smarty.tst", "skpSsp" + rnd, "manager comments" + rnd).setUserAdditionalSettings(additionalSettings.ADS, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        usersCountBefore = users.getUsersTotalCount();
        users.clickCreateNewUser();
        userData.setCountry(users.selectRandomCountry());
        users.setupUserCreation(userData);
        users.toggleAdditionalSettings(userData.getAdditionalSettings());
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime());
        users.assertUsersTotalCount(usersCountBefore + 1);
        userData.setUserId(users.getUserId(userData.getEmail()));
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserInfoCard(userData);
        users.assertEditPageUserSettings(userData);
        login.logout();
        login.loginWithErrors(userData.getEmail(), userData.getPassword());
        login.assertSignInErrors(AuthorizationText.SIGNIN_ERROR_NOT_ACTIVATED);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        userData.setActivated(true);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsSsp();
        userData.setLoginCount(1).setLoginAsUser(true).setLastLoginDate(util.getCurrentDateTime());
        userToDeactivate = userData;
    }

    @Test(dependsOnMethods = {"userActivateCreatedByAdmin"})
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2239")
    @Description("User deactivation")
    public void userDeactivate() {
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO();
        BasicUtility util = new BasicUtility();
        LocalDateTime currentDate = util.getCurrentDateTime();
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.assertUserRow(userToDeactivate);
        users.clickUserSettings(userToDeactivate.getUserId());
        users.assertEditPageUserInfoCard(userToDeactivate);
        users.clickDeactivateUser(true);
        userToDeactivate.setDeleteDate(currentDate).setDeleted(true).setActivated(null).setTrafficStatus(false).setLoginAsUser(false);
        users.assertEditPageUserInfoCard(userToDeactivate);
        users.gotoManageUsersSection();
        users.assertUserRow(userToDeactivate);
        login.logout();
        login.loginWithErrors(userToDeactivate.getEmail(), userToDeactivate.getPassword());
        login.assertSignInErrors(AuthorizationText.SIGNIN_ERROR_DEACTIVATED);
    }
    //</editor-fold>

    //<editor-fold desc="Change role">
    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2171")
    @Description("Change user role from SSP to DSP")
    public void changeRoleSspDsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_SSP, "roleChSSP" + rnd, "roleChDSP" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.ADS, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL_DSP).setLoginAsUser(true).setActivated(true);
        userData.setCreateDate(util.getCurrentDateTime());
        userData.setUserId(users.getUserId(userData.getEmail()));
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsDsp();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2172")
    @Description("Change user role from SSP to Publisher")
    public void changeRoleSspPublisher() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_SSP, "roleChSSP" + rnd, "roleChPublisher" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.ADS, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsPublisher();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2173")
    @Description("Change user role from SSP to Administrator")
    public void changeRoleSspAdmin() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_SSP, "roleChSSP" + rnd, "roleChAdmin" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.ADS, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.ADM).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsAdmin();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2174")
    @Description("Change user role from DSP to SSP")
    public void changeRoleDspSsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_DSP, "roleChDSP" + rnd, "roleChSSP" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL_SSP).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsSsp();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2175")
    @Description("Change user role from DSP to Publisher")
    public void changeRoleDspPublisher() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_DSP, "roleChDSP" + rnd, "roleChPublisher" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsPublisher();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2176")
    @Description("Change user role from DSP to Admin")
    public void changeRoleDspAdmin() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL_DSP, "roleChDSP" + rnd, "roleChAdmin" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.ADM).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsAdmin();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2187")
    @Description("Change user role from Publisher to SSP")
    public void changeRolePublisherSsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL, "roleChPublisher" + rnd, "roleChSSP" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd);
        userData.setDynamicMargin(randomUtil.getRandomDoubleRounded(0, 100, 2)).setUserAdditionalSettings(additionalSettings.PREMIUM, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true).setUserAdditionalSettings(additionalSettings.MARGIN, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL_SSP).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsSsp();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2188")
    @Description("Change user role from Publisher to DSP")
    public void changeRolePublisherDsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL, "roleChPublisher" + rnd, "roleChDSP" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd);
        userData.setDynamicMargin(randomUtil.getRandomDoubleRounded(0, 100, 2)).setUserAdditionalSettings(additionalSettings.PREMIUM, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true).setUserAdditionalSettings(additionalSettings.MARGIN, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL_DSP).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsDsp();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2189")
    @Description("Change user role from Publisher to Admin")
    public void changeRolePublisherAdmin() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.PUBL, "roleChPublisher" + rnd, "roleChAdmin" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd);
        userData.setDynamicMargin(randomUtil.getRandomDoubleRounded(0, 100, 2)).setUserAdditionalSettings(additionalSettings.PREMIUM, true).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true).setUserAdditionalSettings(additionalSettings.MARGIN, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.ADM).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsAdmin();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2179")
    @Description("Change user role from Admin to Publisher")
    public void changeRoleAdminPublisher() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.ADM, "roleChAdmin" + rnd, "roleChPub" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsPublisher();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2180")
    @Description("Change user role from Admin to DSP")
    public void changeRoleAdminDsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.ADM, "roleChAdmin" + rnd, "roleChDsp" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL_DSP).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsDsp();
        sAssert.assertAll("Errors in user role change");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2182")
    @Description("Change user role from Admin to SSP")
    public void changeRoleAdminSsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO(sAssert);
        BasicUtility util = new BasicUtility();
        RandomUtility randomUtil = new RandomUtility();
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO userData = new UserProfileDO(userRoleEnum.ADM, "roleChAdmin" + rnd, "roleChSsp" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compchangerole" + rnd).setUserAdditionalSettings(additionalSettings.SUBSCRIBE, true);
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(userData);
        users.clickSaveUser();
        userData.setCreateDate(util.getCurrentDateTime()).setUserId(users.getUserId(userData.getEmail())).setRole(userRoleEnum.PUBL_SSP).setLoginAsUser(true).setActivated(true);
        users.clickUserSettings(userData.getUserId());
        users.clickActivateUser(true);
        users.selectRole(userData.getRole());
        users.clickSaveUser();
        users.assertUserRow(userData);
        users.clickUserSettings(userData.getUserId());
        users.assertEditPageUserSettings(userData);
        users.assertEditPageUserInfoCard(userData);
        login.logout();
        login.login(userData.getEmail(), userData.getPassword());
        login.assertUserIsSsp();
        sAssert.assertAll("Errors in user role change");
    }
    //</editor-fold>

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("50008")
    @Description("Search by user name/email/id on the 'Manage users' page")
    public void searchUsers() {
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO();
        RandomUtility randomUtil = new RandomUtility();
        int rndLength;
        List<UserProfileDO> usersList;
        UserProfileDO user, userForId;
        String queryLine;
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        usersList = users.getUserDataFromDb();
        user = (UserProfileDO) randomUtil.getRandomElement(usersList);
        userForId = usersList.get(usersList.size() - 1);
        rndLength = randomUtil.getRandomInt(1, user.getFirstName().length());
        queryLine = user.getFirstName().substring(0, rndLength);
        users.searchUser(queryLine);
        users.assertSearchResults(false, queryLine);
        rndLength = randomUtil.getRandomInt(1, user.getLastName().length());
        queryLine = user.getLastName().substring(0, rndLength);
        users.searchUser(queryLine);
        users.assertSearchResults(false, queryLine);
        rndLength = randomUtil.getRandomInt(1, user.getEmail().length());
        queryLine = user.getEmail().substring(0, rndLength);
        users.searchUser(queryLine);
        users.assertSearchResults(false, queryLine);
        users.searchUser(userForId.getUserId().toString());
        users.assertSearchResults(true, userForId.getUserId().toString());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("2136")
    @Description("Setting 'Business domain' and 'Seller ID' in the user profile")
    public void setBusinessDomainSellerId() {
        AuthorizationPO login = new AuthorizationPO();
        UserManagementPO users = new UserManagementPO();
        SellersJsonPO sellers = new SellersJsonPO();
        RandomUtility randomUtil = new RandomUtility();
        File sellersFile;
        int rnd = randomUtil.getRandomInt(0, 10000);
        UserProfileDO pubUser = new UserProfileDO(userRoleEnum.PUBL, "busDomPubFirstName" + rnd, "busDomPubLastName" + rnd, getEmailAddress(rnd), loginData.defaultUserPassword, "compPub" + rnd).setBusinessDomain("bus.dom" + rnd + ".com").setSellerId("seLLer-" + rnd + "-pUb");
        login.login(StaticData.supportDefaultUser);
        users.gotoManageUsersSection();
        users.clickCreateNewUser();
        users.setupUserCreation(pubUser);
        users.clickSaveUser();
        pubUser.setUserId(users.getUserId(pubUser.getEmail()));
        users.clickUserSettings(pubUser.getUserId());
        users.clickActivateUser(true);
        sellers.gotoSellersJsonSection();
        sellers.searchSellersJsonTable(pubUser.getSellerId());
        sellers.setConfidentiality(pubUser.getSellerId(), SellersAdsEnums.sellerConfidentialityEnum.PUBLIC);
        sellers.clickSaveButton();
        sellers.searchSellersJsonTable(pubUser.getSellerId());
        sellers.assertSellersJsonTableRecord(pubUser.getRole(), pubUser.getSellerId(), pubUser.getCompany(), pubUser.getBusinessDomain(), SellersAdsEnums.sellerTypeEnum.PUBLISHER, SellersAdsEnums.sellerConfidentialityEnum.PUBLIC, false);
        sellersFile = sellers.clickDownloadButton();
        sellers.assertSellersJsonFileRecord(sellersFile, false, false, pubUser.getSellerId(), pubUser.getCompany(), pubUser.getBusinessDomain(), SellersAdsEnums.sellerTypeEnum.PUBLISHER);
    }

}
