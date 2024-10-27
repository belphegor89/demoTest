package data.textstrings.messages;

public class SupplyText extends CommonText {
    //<editor-fold desc="Validation - Info page">
    public static final String errorNameShort = "The name must be at least 3 characters.",
            errorNameLong = "The name may not be greater than 255 characters.",
            errorNameFormat = "The name has invalid format",
            errorCompanyEmpty = "The Company field is required.",
            errorAdFormatsEmpty = "It’s required to select at least one ad format: Banner, Video, Audio or Native",
            errorTrafficTypesEmpty = "It’s required to select at least one traffic type: Desktop, Mobile App, Mobile Web or CTV",
            errorMacrosVastWebEmpty = "It's required to add macros [DOMAIN] for Web VAST tag",
            errorMacrosVastInappEmpty = "It's required to add macros [APP_BUNDLE] and [IFA] for Inapp VAST tag",
            errorMacrosVastCtvEmpty = "It's required to add macros [APP_BUNDLE], [IFA], [WIDTH], [HEIGHT], [STOREURL] and [IP] for Inapp VAST tag",
            errorMacrosJsWebS2SEmpty = "It's required to add macros ip, ua, domain, w and h for JS tag",
            errorMacrosJsInappS2SEmpty = "It's required to add macros ip, ua, bundle, storeurl, w and h for JS tag",
            errorMacrosJsWebS2CEmpty = "It's required to add macros domain, w and h for JS tag",
            errorMacrosJsInappS2CEmpty = "It's required to add macros bundle, storeurl, w and h for JS tag";
    //</editor-fold>

    //<editor-fold desc="Validation - Settings page">
    public static final String errorSpendLimitHigh = "Value must be less than or equal to 20000000.",
            errorTmaxHigh = "Value must be less than or equal to 20000000.",
            errorMarginHigh = "Value must be less than or equal to 100.",
            errorMarginMaxMin = "The max margin must be greater than or equal ${minValue}.",
            errorBidPriceHigh = "Value must be less than or equal to 999999.";
    //</editor-fold>

    public static final String errorApiLinkUnsupported = "Unsupported format of response, we can support only `json`, `csv` or `xml`",
            emptyFilterListSection = "No filter lists found";
}
