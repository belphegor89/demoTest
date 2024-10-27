package data.textstrings.messages;

public class DemandText extends CommonText {
    //<editor-fold desc="Validation - Info page">
    public static final String errorNameShort = "Please lengthen this text to 3 characters or more (you are currently using 2 characters).",
            errorNameFormat = "The name has invalid format",
            errorCompanyEmpty = "The Company field is required.",
            errorAdFormatsEmpty = "It’s required to select at least one ad format type Banner, Video, Audio or Native",
            errorAdFormatsEmptyVast = "It’s required to select at least one ad format type Video or Audio",
            errorAdFormatsEmptyJs = "It’s required to select Banner ad format type to create JS Tag endpoint.",
            errorTrafficTypesEmpty = "It’s required to select at least one traffic type Mobile App, Mobile Web, Desktop or CTV",
            errorTrafficTypesJsEmpty = "It’s required to select at least one traffic type Mobile App, Mobile Web or Desktop",
            errorEndpointUrlEmpty = "The endpoint field is required.",
            errorEndpointUrlInvalid = "The endpoint format is invalid.",
            errorVastTagEmpty = "The VAST tag field is required.",
            errorJsTagInvalid = "The js tag format is invalid.",
            errorJsTagEmpty = "The js tag field is required.",
            errorVastTagInvalid = "The VAST tag format is invalid.",
            errorJsSizeHigh = "Value must be less than or equal to 32767.",
            errorAdapterEmpty = "The Prebid partner setting field is required.",
            errorAdapterNoSetting = "To select this Prebid adapter please add settings in Adapter Control first",
            errorSpendLimitHigh = "Value must be less than or equal to 2147483646.",
            errorCpmLimitLow = "The CPM must be at least 0.1.",
            errorCpmLimitHigh = "The CPM may not be greater than 1100000.",
            errorMarginHigh = "Value must be less than or equal to 100.",
            errorMarginMaxMin = "Value must be greater than or equal to ${minValue}.",
            errorMarginMaxMin2 = "Minimum value (${minVal}) must be less than the maximum value (${maxVal}).",
            errorTmaxHigh = "The Tmax filter may not be greater than 1100000.",
            errorMaxBidfloorHigh = "The max bid may not be greater than 1100000.",
            errorLimitQpsHigh = "Value must be less than or equal to 1100000.",
            geoNoCountry = "Please select an item in the list.",
            geoValueHigh = "Value must be less than or equal to ${valueCap}.";
    //</editor-fold>

    //<editor-fold desc="Validation - Settings page">
    public static final String
            errorMarkupEmpty = "The markup field is required.",

    errorBidPriceHigh = "Value must be less than or equal to 999999.";
    //</editor-fold>


}
