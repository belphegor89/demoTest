package data.textstrings.messages;

public class CommonText{

    public static final String EMPTY_DASHBOARD = "There are no results for your given query";

    //<editor-fold desc="Modals">
    public static final String
            MODAL_WARNING_HEADER = "Warning",
            MODAL_ERROR_HEADER = "Error",
            MODAL_CONFIRM_HEADER = "Are you sure?",
            MODAL_SUCCESS_HEADER = "Request successful";

    //</editor-fold>

    //<editor-fold desc="Validation error messages">
    public static final String VALIDATION_REQUIRED_EMPTY = "Please fill in this field.",
            VALIDATION_VALUE_TOO_LOW = "Value must be greater than or equal to ${nmb}.",
            VALIDATION_VALUE_TOO_HIGH = "Value must be less than or equal to ${nmb}.",
            VALIDATION_DECIMAL_INTEGER = "Please enter a valid value. The two nearest valid values are ${low} and ${high}.",
            validationScannerNotEnabled = "The scanner must have one of the options enabled. % of scanned impressions",
            validationScannerImpressionLow = "The % of scanned impressions must be at least 1.";
    //</editor-fold>


}
