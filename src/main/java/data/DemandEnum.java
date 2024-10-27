package data;

public class DemandEnum{
    public enum macroTagsTypesEnum{
        BUNDLE,
        NAME,
        APPNAME,
        IFA,
        IP,
        UA,
        DOMAIN,
        PAGE,
        DNT,
        GDPR,
        CONSENT,
        GDPR_CONSENT,
        CCPA_CONSENT,
        CAT,
        LAT,
        LON,
        W,
        H,
        DW,
        DH,
        BIDFLOOR,
        STOREURL,
        MINDUR,
        MAXDUR,
        CCPA,
        COPPA,
        PLACEMENTID,
        CB,
        DEVICE_MAKE,
        DEVICE_MODEL,
        SCHAIN;

        public String attributeName(){
            return switch (this){
                case BUNDLE -> "bundle";
                case NAME -> "name";
                case APPNAME -> "appname";
                case IFA -> "ifa";
                case IP -> "ip";
                case UA -> "ua";
                case DOMAIN -> "domain";
                case PAGE -> "page";
                case DNT -> "dnt";
                case GDPR -> "gdpr";
                case CONSENT -> "consent";
                case GDPR_CONSENT -> "gdpr_consent";
                case CCPA_CONSENT -> "ccpa_consent";
                case CAT -> "cat";
                case LAT -> "lat";
                case LON -> "lon";
                case W -> "w";
                case H -> "h";
                case DW -> "dw";
                case DH -> "dh";
                case BIDFLOOR -> "bidfloor";
                case STOREURL -> "storeurl";
                case MINDUR -> "mindur";
                case MAXDUR -> "maxdur";
                case CCPA -> "ccpa";
                case COPPA -> "coppa";
                case PLACEMENTID -> "placementid";
                case CB -> "cb";
                case DEVICE_MAKE -> "device_make";
                case DEVICE_MODEL -> "device_model";
                case SCHAIN -> "schain";
            };
        }

        public String tagName(){
            return switch (this){
                case BUNDLE -> "[APP_BUNDLE]";
                case APPNAME -> "[APP_NAME]";
                case IFA -> "[IFA]";
                case IP -> "[IP]";
                case UA -> "[UA]";
                case DOMAIN -> "[DOMAIN]";
                case PAGE -> "[PAGE_URL]";
                case DNT -> "[DNT]";
                case GDPR -> "[GDPR]";
                case GDPR_CONSENT, CONSENT -> "[GDPR_CONSENT]";
                case CAT -> "[CATEGORY]";
                case LAT -> "[LOCATION_LAT]";
                case LON -> "[LOCATION_LON]";
                case W -> "[WIDTH]";
                case H -> "[HEIGHT]";
                case STOREURL -> "[APP_STORE_URL]";
                case MINDUR -> "[MINIMUM_DURATION]";
                case MAXDUR -> "[MAXIMUM_DURATION]";
                case CCPA -> "[CCPA]";
                case COPPA -> "[COPPA]";
                case PLACEMENTID -> "[PLACEMENT_ID]";
                case CB -> "[CACHEBUSTER]";
                case DEVICE_MAKE -> "[DEVICE_MAKE]";
                case DEVICE_MODEL -> "[DEVICE_MODEL]";
                case SCHAIN -> "[SCHAIN]";
                case NAME -> "[NAME]";
                case CCPA_CONSENT -> "[CCPA_CONSENT]";
                case DW -> "[DEVICE_WIDTH]";
                case DH -> "[DEVICE_HEIGHT]";
                case BIDFLOOR -> "[BIDFLOOR]";
            };
        }
    }

    public enum dashboardColumnsEnum{
        ID,
        NAME,
        TYPE,
        STATUS,
        BANNER,
        VIDEO,
        NATIVE,
        AUDIO,
        REGION,
        QPS_REAL,
        QPS_LIMIT,
        QPS_BID,
        SPEND_YESTERDAY,
        SPEND_TODAY,
        SPEND_LIMIT,
        WIN_RATE,
        COMPANY_ID,
        ACTIONS;

        public String attributeName(){
            return switch (this){
                case ID -> "id";
                case NAME -> "name";
                case BANNER -> "banner";
                case VIDEO -> "video";
                case NATIVE -> "native";
                case AUDIO -> "audio";
                case REGION -> "region_id";
                case STATUS -> "status";
                case TYPE -> "endpoint_type";
                case QPS_REAL -> "qps";
                case QPS_LIMIT -> "limit_qps";
                case QPS_BID -> "bid_qps";
                case SPEND_YESTERDAY -> "spend_yesterday";
                case SPEND_TODAY -> "spend_today";
                case WIN_RATE -> "win_rate";
                case COMPANY_ID -> "company_id";
                case SPEND_LIMIT -> "spend_limit";
                case ACTIONS -> "action";
            };
        }
    }

    public enum endpointSettingsSectionsEnum{
        GENERAL,
        GEO,
        FILTERS,
        ADDITIONAL,
        SCANNERS;

        public String attributeName(){
            return switch (this){
                case GENERAL -> "#information";
                case GEO -> "#geo_settings";
                case FILTERS -> "#filter_list_dashboard";
                case ADDITIONAL -> "#additional_info";
                case SCANNERS -> "#scanner";
            };
        }
    }

    public enum adapterTypeEnum{
        NONE,
        API,
        CUSTOM;

        public String attributeName(){
            return switch (this){
                case NONE -> null;
                case API -> "api";
                case CUSTOM -> "custom";
            };
        }

        public String publicName(){
            return switch (this){
                case NONE -> "None";
                case API -> "API adapter";
                case CUSTOM -> "Custom RTB adapter";
            };
        }
    }

    public enum qpsLimitType {
        FIXED,
        DYNAMIC,
        DIRECT;

        public String attributeName(){
            return switch (this){
                case FIXED -> "fixed";
                case DYNAMIC -> "dynamic";
                case DIRECT -> "direct";
            };
        }

        public String publicName(){
            return switch (this){
                case FIXED -> "Fixed";
                case DYNAMIC -> "Dynamic";
                case DIRECT -> "Prioritize Direct Traffic";
            };
        }
    }

    public enum qpsFrequencyEnum {
        MIN15,
        MIN30,
        HOUR;

        public String publicName(){
            return switch (this){
                case MIN15 -> "15 min";
                case MIN30 -> "30 min";
                case HOUR -> "1 hour";
            };
        }
    }

    public enum qpsOptimizationEnum {
        RCPM,
        SPEND,
        CLICKS;

        public String publicName(){
            return switch (this){
                case RCPM -> "RCPM";
                case SPEND -> "Spend";
                case CLICKS -> "Clicks";
            };
        }
    }

}
