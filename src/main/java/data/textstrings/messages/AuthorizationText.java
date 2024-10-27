package data.textstrings.messages;

public class AuthorizationText{
    public static final String SIGNUP_SUCCESS_HEADER = "Account was created!",
            SIGNUP_SUCCESS_MESSAGE = "Thank you for creating an account at ${platformUrl}. To complete your registration, please follow instructions from the email.",
            SIGNUP_ERROR_ALREADY_REGISTERED = "email is already registered. Try to restore access through \"Forgot Password\"",
            SIGNIN_ERROR_WRONG_CREDENTIALS = "The email or password is incorrect",
            SIGNIN_ERROR_NOT_ACTIVATED = "Your account is waiting for confirmation from the mail. Re-send email",
            SIGNIN_ERROR_DEACTIVATED = "Your account is deleted. Please contact with manager",
            RECAPTCHA_ERROR_REQUIRED = "The CAPTCHA field is required.",
            EMAIL_VALIDATION_ERROR_NO_AT = "Please include an '@' in the email address. '${emailAddress}' is missing an '@'.",
            EMAIL_VALIDATION_ERROR_NO_NAME = "Please enter a part followed by '@'. '${emailAddress}' is incomplete.",
            EMAIL_VALIDATION_ERROR_NOT_VALID = "The email must be a valid email address.",
            EMAIL_VALIDATION_ERROR_NO_EMAIL = "Please enter an email address.",
            EMAIL_VALIDATION_ERROR_EMAIL_REQUIRED = "The email field is required.",
            EMAIL_VALIDATION_ERROR_SYMBOL_FOLLOWING = "A part following '@' should not contain the symbol '${symbol}'.",
            EMAIL_VALIDATION_ERROR_SYMBOL_FOLLOWED = "A part followed by '@' should not contain the symbol '${symbol}'.",
            PASSWORD_VALIDATION_ERROR_NO_PASSWORD = "The password field is required.",
            PASSWORD_VALIDATION_ERROR_SHORT_PASSWORD = "The password must be at least 7 characters.",
            SIGN_IN_VALIDATION_ERROR_FILL_IN = "Fill in the required fields",
            FIRST_NAME_VALIDATION_REQUIRED = "The first name field is required.",
            FIRST_NAME_VALIDATION_FORMAT = "The first name has invalid format",
            LAST_NAME_VALIDATION_REQUIRED = "The last name field is required.",
            LAST_NAME_VALIDATION_FORMAT = "The last name has invalid format",
            COMPANY_VALIDATION_REQUIRED = "The company field is required.",
            COMPANY_VALIDATION_FORMAT = "The company has invalid format",
            PASSWORD_VALIDATION_NO_CONFIRMATION = "The password confirmation does not match.",
            TOS_VALIDATION_REQUIRED = "The agree to the Terms of Service & Privacy Policy field is required.";

}
