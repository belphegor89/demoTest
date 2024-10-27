package data;

public class SupplyEnum{

    public enum vastTypeEnum{
        CUSTOM,
        CUSTOM_INAPP,
        CUSTOM_CTV,
        LKQD,
        JW_PLAYER,
        SPRINGSERVE;

        public String attributeName(){
            return switch (this){
                case CUSTOM -> "custom";
                case CUSTOM_INAPP -> "custom_inapp";
                case CUSTOM_CTV -> "custom_ctv";
                case LKQD -> "lkqd";
                case JW_PLAYER -> "jw_player";
                case SPRINGSERVE -> "springserve";
            };
        }
    }

    public enum jsTagTypeEnum{
        S2S_WEB,
        S2S_APP,
        S2C_WEB,
        S2C_APP,
        DIRECT,
        DFP,
        APPLOVIN;

        public String attributeName(){
            return switch (this){
                case S2S_WEB -> "s2s_web";
                case S2S_APP -> "s2s_app";
                case S2C_WEB -> "s2c_web";
                case S2C_APP -> "s2c_app";
                case DIRECT -> "direct";
                case DFP -> "dfp";
                case APPLOVIN -> "applovin";
            };
        }
    }

    public enum macroTagsTypes{
        BUNDLE,
        NAME,
        APPNAME,
        IFA,
        IFA_TYPE,
        IP,
        UA,
        DOMAIN,
        PAGE,
        DNT,
        GDPR,
        CONSENT,
        GDPR_CONSENT,
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
        COPPA,
        PLACEMENTID,
        CB,
        DEVICE_MAKE,
        DEVICE_MODEL,
        SCHAIN,
        PLAYBACKMETHOD,
        CONTENT_LENGTH,
        CONTENT_TITLE,
        CONTENT_SERIES,
        CONTENT_CHANNEL_NAME,
        CONTENT_CONTEXT,
        LMT,
        CONTENT_EPISODE,
        CONTENT_ID,
        CONTENT_KEYWORDS,
        CONTENT_NETWORK,
        CONTENT_PRODUCER,
        CONTENT_LIVESTREAM,
        CONTENT_GENRE,
        CONTENT_RATING,
        CONTENT_REASON,
        API,
        GPP,
        GPP_SID,
        POD_MAX_DUR,
        POD_AD_SLOTS,
        LANGUAGE;

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
                case COPPA -> "coppa";
                case PLACEMENTID -> "placementid";
                case CB -> "cb";
                case DEVICE_MAKE -> "device_make";
                case DEVICE_MODEL -> "device_model";
                case SCHAIN -> "schain";
                case PLAYBACKMETHOD -> "playbackmethod";
                case CONTENT_LENGTH -> "content_length";
                case CONTENT_TITLE -> "content_title";
                case CONTENT_SERIES -> "content_series";
                case CONTENT_CHANNEL_NAME -> "content_channel_name";
                case CONTENT_CONTEXT -> "content_context";
                case LMT -> "lmt";
                case CONTENT_EPISODE -> "content_episode";
                case CONTENT_ID -> "content_id";
                case CONTENT_KEYWORDS -> "content_keywords";
                case CONTENT_NETWORK -> "content_network_name";
                case CONTENT_PRODUCER -> "content_producer_name";
                case API -> "api";
                case GPP -> "gpp";
                case GPP_SID -> "gpp_sid";
                case POD_MAX_DUR -> "pod_max_dur";
                case POD_AD_SLOTS -> "pod_ad_slots";
                case CONTENT_REASON -> "content_reason";
                case IFA_TYPE -> "ifa_type";
                case CONTENT_LIVESTREAM -> "content_livestream";
                case CONTENT_GENRE -> "content_genre";
                case CONTENT_RATING -> "content_rating";
                case LANGUAGE -> "language";
            };
        }
    }

    public static String getMacroTagPlaceholder(macroTagsTypes macroType, vastTypeEnum vastSubtype, jsTagTypeEnum jsSubType){
        switch (macroType){
            case BUNDLE -> {
                if (jsSubType == jsTagTypeEnum.S2S_APP || jsSubType == jsTagTypeEnum.S2C_APP){
                    return "[BUNDLE]";
                } else {
                    return "[APP_BUNDLE]";
                }
            }
            case APPNAME -> {
                return "[APP_NAME]";
            }
            case IFA -> {
                return "[IFA]";
            }
            case IP -> {
                return "[IP]";
            }
            case UA -> {
                return "[UA]";
            }
            case DOMAIN -> {
                return "[DOMAIN]";
            }
            case PAGE -> {
                if (jsSubType == jsTagTypeEnum.S2S_WEB || jsSubType == jsTagTypeEnum.S2C_WEB){
                    return "[PAGE]";
                } else {
                    return "[PAGE_URL]";
                }
            }
            case DNT -> {
                return "[DNT]";
            }
            case GDPR -> {
                return "[GDPR]";
            }
            case GDPR_CONSENT, CONSENT -> {
                return "[GDPR_CONSENT]";
            }
            case CAT -> {
                return "[CATEGORY]";
            }
            case LAT -> {
                if (jsSubType != null){
                    return "[LAT]";
                } else {
                    return "[LOCATION_LAT]";
                }
            }
            case LON -> {
                if (jsSubType != null){
                    return "[LON]";
                } else {
                    return "[LOCATION_LON]";
                }
            }
            case W -> {
                return "[WIDTH]";
            }
            case H -> {
                return "[HEIGHT]";
            }
            case STOREURL -> {
                if (vastSubtype == vastTypeEnum.CUSTOM_CTV || jsSubType == jsTagTypeEnum.S2S_APP || jsSubType == jsTagTypeEnum.S2C_APP){
                    return "[STOREURL]";
                } else {
                    return "[APP_STORE_URL]";
                }
            }
            case MINDUR -> {
                return "[MINIMUM_DURATION]";
            }
            case MAXDUR -> {
                return "[MAXIMUM_DURATION]";
            }
            case COPPA -> {
                return "[COPPA]";
            }
            case PLACEMENTID -> {
                return "[PLACEMENT_ID]";
            }
            case CB -> {
                return "[CACHEBUSTER]";
            }
            case DEVICE_MAKE -> {
                return "[DEVICE_MAKE]";
            }
            case DEVICE_MODEL -> {
                return "[DEVICE_MODEL]";
            }
            case SCHAIN -> {
                return "[SCHAIN]";
            }
            case NAME -> {
                return "[NAME]";
            }
            case DW -> {
                return "[DEVICE_WIDTH]";
            }
            case DH -> {
                return "[DEVICE_HEIGHT]";
            }
            case BIDFLOOR -> {
                return "[BIDFLOOR]";
            }
            case CONTENT_LENGTH -> {
                return "[CONTENT_LENGTH]";
            }
            case CONTENT_TITLE -> {
                return "[CONTENT_TITLE]";
            }
            case CONTENT_SERIES -> {
                return "[CONTENT_SERIES]";
            }
            case CONTENT_CHANNEL_NAME -> {
                return "[CONTENT_CHANNEL_NAME]";
            }
            case CONTENT_CONTEXT -> {
                return "[CONTENT_CONTEXT]";
            }
            case LMT -> {
                return "[LMT]";
            }
            case CONTENT_EPISODE -> {
                return "[CONTENT_EPISODE]";
            }
            case CONTENT_ID -> {
                return "[CONTENT_ID]";
            }
            case CONTENT_KEYWORDS -> {
                return "[CONTENT_KEYWORDS]";
            }
            case CONTENT_NETWORK -> {
                return "[CONTENT_NETWORK_NAME]";
            }
            case CONTENT_PRODUCER -> {
                return "[CONTENT_PRODUCER_NAME]";
            }
            case API -> {
                return "[API]";
            }
            case PLAYBACKMETHOD -> {
                return "[PLAYBACKMETHOD]";
            }
            case GPP -> {
                return "[GPP]";
            }
            case GPP_SID -> {
                return "[GPP_SID]";
            }
            case POD_MAX_DUR -> {
                return "[POD_MAX_DUR]";
            }
            case POD_AD_SLOTS -> {
                return "[POD_AD_SLOTS]";
            }
            case CONTENT_REASON -> {
                return "[CONTENT_REASON]";
            }
            case IFA_TYPE -> {
                return "[IFA_TYPE]";
            }
            case CONTENT_LIVESTREAM -> {
                return "[CONTENT_LIVESTREAM]";
            }
            case CONTENT_GENRE -> {
                return "[CONTENT_GENRE]";
            }
            case CONTENT_RATING -> {
                return "[CONTENT_RATING]";
            }
            case LANGUAGE -> {
                return "[LANGUAGE]";
            }
        }
        return null;
    }

    public enum sspTableColumnsEnum{
        ID,
        NAME,
        REGION,
        STATUS,
        TYPE,
        QPS,
        BID_QPS,
        SPEND_YESTERDAY,
        SPEND_TODAY,
        WIN_RATE,
        COMPANY_ID,
        ALLOWED_DSP,
        SPEND_LIMIT,
        ACTIONS;

        public String attributeName(){
            return switch (this){
                case ID -> "id";
                case NAME -> "name";
                case REGION -> "region_name";
                case STATUS -> "status";
                case TYPE -> "type";
                case QPS -> "qps";
                case BID_QPS -> "bid_qps";
                case SPEND_YESTERDAY -> "spend_today_yesterday";
                case SPEND_TODAY -> "spend_today";
                case WIN_RATE -> "win_rate";
                case COMPANY_ID -> "company_id";
                case ALLOWED_DSP -> "allowed_dsp_general";
                case SPEND_LIMIT -> "spend_limit";
                case ACTIONS -> "action_id";
            };
        }
    }

}
