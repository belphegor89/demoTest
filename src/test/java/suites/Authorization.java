package suites;

import common.SoftAssertCustom;
import common.utils.RandomUtility;
import data.StaticData;
import data.UserEnum;
import data.dataobject.UserProfileDO;
import data.textstrings.messages.AuthorizationText;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;

import static data.StaticData.getEmailAddress;

@Epic("Authorization")
public class Authorization extends BaseSuiteClassNew {
    static String resetMail = "";

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Login as admin")
    public void loginAsAdmin() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO(softAssert);
        login.login(StaticData.supportDefaultUser);
        login.assertUserIsAdmin();
        softAssert.assertAll("Errors on Admin sign in:");
    }

    @Test
    @TmsLink("496")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Sign up as Publisher user")
    public void signUpAsPublisherUser() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO registration = new AuthorizationPO(softAssert);
        int rnd = new RandomUtility().getRandomInt(1, 10000);
        UserProfileDO user = new UserProfileDO().setEmail(getEmailAddress(rnd)).setPassword("1234567").setRole(UserEnum.userRoleEnum.PUBL).setCompany("Test company").setFirstName("FirstPub").setLastName("LastPub");
        resetMail = user.getEmail();
        registration.clickCreateAccount();
        user.setCountry(registration.getRandomCountry());
        registration.inputUserFields(user, user.getPassword(), true);
        registration.clickRecaptcha();
        registration.clickSignUp();
        registration.assertSignUpConfirmation();
        registration.clickConfirmationLink(user.getEmail());
        registration.assertUserIsPublisher();
        //TODO Assert Welcome email
        softAssert.assertAll("Errors on Publisher user registration:");
    }

    @Test
    @TmsLink("4313")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Sign up as SSP user")
    public void signUpAsSspUser() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO registration = new AuthorizationPO(softAssert);
        int rnd = new RandomUtility().getRandomInt(1, 10000);
        UserProfileDO user = new UserProfileDO().setEmail(getEmailAddress(rnd)).setPassword("1234567").setRole(UserEnum.userRoleEnum.PUBL_SSP).setCompany("Test company").setFirstName("FirstSsp").setLastName("LastSsp");
        registration.clickCreateAccount();
        user.setCountry(registration.getRandomCountry());
        registration.inputUserFields(user, user.getPassword(), true);
        registration.clickRecaptcha();
        registration.clickSignUp();
        registration.assertSignUpConfirmation();
        registration.clickConfirmationLink(user.getEmail());
        registration.assertUserIsSsp();
        //TODO Assert Welcome email
        softAssert.assertAll("Errors on SSP User registration:");
    }

    @Test
    @TmsLink("4314")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Sign up as DSP user")
    public void signUpAsDspUser() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO registration = new AuthorizationPO(softAssert);
        int rnd = new RandomUtility().getRandomInt(1, 10000);
        UserProfileDO user = new UserProfileDO().setEmail(getEmailAddress(rnd)).setPassword("1234567").setRole(UserEnum.userRoleEnum.PUBL_DSP).setCompany("Test company").setFirstName("FirstDsp").setLastName("LastDsp");
        registration.clickCreateAccount();
        user.setCountry(registration.getRandomCountry());
        registration.inputUserFields(user, user.getPassword(), true);
        registration.clickRecaptcha();
        registration.clickSignUp();
        registration.assertSignUpConfirmation();
        registration.clickConfirmationLink(user.getEmail());
        registration.assertUserIsDsp();
        //TODO Assert Welcome email
        softAssert.assertAll("Errors on DSP User registration:");
    }

    @Test(dependsOnMethods = {"signUpAsPublisherUser"})
    @TmsLink("491")
    @Description("Reset password by 'Forgot password?' button")
    public void resetPasswordByForgotPasswordButton() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO resetPassword = new AuthorizationPO(softAssert);
        String newPassword = "7654321";
        resetPassword.inputMail(resetMail);
        resetPassword.clickForgotPassword();
        resetPassword.clickRecaptcha();
        resetPassword.clickResetPassword();
        resetPassword.assertPasswordRecovery();
        resetPassword.clickResetPasswordLink(resetMail);
        resetPassword.inputNewPassword(newPassword, newPassword);
        resetPassword.clickRecaptcha();
        resetPassword.clickNewPasswordButton();
        resetPassword.assertUserIsPublisher();
        resetPassword.logout();
        resetPassword.login(resetMail, newPassword);
        softAssert.assertAll("Errors on Password reset:");
    }

    @Test
    @TmsLink("493")
    @Description("Redirect to Sign in page after logout")
    public void redirectToLoginAfterLogout() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO(softAssert);
        login.login(StaticData.supportDefaultUser);
        login.logout();
        login.assertLoginPage();
        softAssert.assertAll("Errors on redirect after logout:");
    }

    @Test
    @TmsLink("4044")
    @Description("Sign up with existing email")
    public void signUpWithExistingEmail() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO registration = new AuthorizationPO(softAssert);
        UserProfileDO user = new UserProfileDO().setEmail(StaticData.supportDefaultUser.getEmail()).setPassword("1234567").setRole(UserEnum.userRoleEnum.PUBL).setCompany("Test company").setFirstName("FirstPub").setLastName("LastPub");
        registration.clickCreateAccount();
        user.setCountry(registration.getRandomCountry());
        registration.inputUserFields(user, user.getPassword(), true);
        registration.clickRecaptcha();
        registration.clickSubmitWithErrors();
        registration.assertSignUpErrors();
        softAssert.assertAll("Errors on User registration with existing email:");
    }

    @Test
    @TmsLink("488")
    @Description("reCAPTCHA displays after unsuccessful sign-in attempts")
    public void recaptchaDisplaysAfterUnsuccessfulLogin() {
        AuthorizationPO login = new AuthorizationPO();
        login.inputLoginData(StaticData.supportDefaultUser.getEmail(), "5555555");
        login.clickSignInWithCaptcha();
        login.loginWithRecaptcha(StaticData.supportDefaultUser.getEmail(), StaticData.supportDefaultUser.getPassword());
    }

    @Test
    @TmsLink("4315")
    @Description("User can go Back On Registration Page")
    public void goBackOnRegistrationPage() {
        AuthorizationPO login = new AuthorizationPO();
        login.clickCreateAccount();
        login.clickBackButton();
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("483")
    @Description("Sign in form validation")
    public void signInValidation() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO(softAssert);
        String password = "1234567";
        login.loginWithErrors("valid@mail.com", "");
        login.assertSignInValidationErrors(null, AuthorizationText.PASSWORD_VALIDATION_ERROR_NO_PASSWORD, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("valid@mail.com", "123456");
        login.assertSignInValidationErrors(null, AuthorizationText.PASSWORD_VALIDATION_ERROR_SHORT_PASSWORD, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("email.example.com", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_NO_AT.replace("${emailAddress}", "email.example.com"), null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("@example.com", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_NO_NAME.replace("${emailAddress}", "@example.com"), null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors(".email@example.com", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_NOT_VALID, null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("email.@example.com", password);
        login.clickRecaptcha();
        login.loginWithErrors("email.@example.com", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_NOT_VALID, null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("email@example@example.com", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_SYMBOL_FOLLOWING.replace("${symbol}", "@"), null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("Abc..123@example.com)", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_SYMBOL_FOLLOWING.replace("${symbol}", ")"), null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("email@-example.com", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_NO_EMAIL, null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        login.loginWithErrors("Joe Smith <email@example.com>", password);
        login.assertSignInValidationErrors(AuthorizationText.EMAIL_VALIDATION_ERROR_SYMBOL_FOLLOWED.replace("${symbol}", " "), null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        softAssert.assertAll("Errors with validation on login page:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("501")
    @Description("Sign up form validation")
    public void signUpValidation() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO registration = new AuthorizationPO(softAssert);
        UserProfileDO user = new UserProfileDO().setRole(UserEnum.userRoleEnum.PUBL).setCountry(null);
        registration.clickCreateAccount();
        registration.inputUserFields(user, "", false);
        registration.clickSubmitWithErrors();
        registration.assertSignUpErrors(AuthorizationText.FIRST_NAME_VALIDATION_REQUIRED, AuthorizationText.LAST_NAME_VALIDATION_REQUIRED, AuthorizationText.EMAIL_VALIDATION_ERROR_EMAIL_REQUIRED, AuthorizationText.PASSWORD_VALIDATION_ERROR_NO_PASSWORD, null, AuthorizationText.COMPANY_VALIDATION_REQUIRED, AuthorizationText.TOS_VALIDATION_REQUIRED, AuthorizationText.RECAPTCHA_ERROR_REQUIRED);
        user = new UserProfileDO(UserEnum.userRoleEnum.PUBL, "}{:<*!@#$%", "}{:<*!@#$%", "email.@example.com", "123456", "}{:<*!@#$%").setCountry(null);
        registration.inputUserFields(user, "", true);
        registration.clickRecaptcha();
        registration.clickSubmitWithErrors();
        registration.assertSignUpErrors(AuthorizationText.FIRST_NAME_VALIDATION_FORMAT, AuthorizationText.LAST_NAME_VALIDATION_FORMAT, AuthorizationText.EMAIL_VALIDATION_ERROR_NOT_VALID, AuthorizationText.PASSWORD_VALIDATION_ERROR_SHORT_PASSWORD + AuthorizationText.PASSWORD_VALIDATION_NO_CONFIRMATION, null, AuthorizationText.COMPANY_VALIDATION_FORMAT, null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        user = new UserProfileDO(UserEnum.userRoleEnum.PUBL, "test", "test", "email@example.com", "12345678", "test").setCountry(null);
        registration.inputUserFields(user, "87654321", true);
        registration.clickRecaptcha();
        registration.clickSubmitWithErrors();
        registration.assertSignUpErrors(null, null, null, AuthorizationText.PASSWORD_VALIDATION_NO_CONFIRMATION, null, null, null, AuthorizationText.SIGN_IN_VALIDATION_ERROR_FILL_IN);
        softAssert.assertAll("Errors with validation on registration page:");
    }
}
