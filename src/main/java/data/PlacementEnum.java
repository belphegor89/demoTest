package data;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PlacementEnum {
    public enum placementTypeEnum {
        BANNER,
        VIDEO,
        NATIVE;

        public String attributeName() {
            return switch (this) {
                case BANNER -> "banner";
                case VIDEO -> "video";
                case NATIVE -> "native";
            };
        }

        public String publicName() {
            return switch (this) {
                case BANNER -> "Banner";
                case VIDEO -> "Video";
                case NATIVE -> "Native";
            };
        }
    }

    public enum bannerTagsEnum {
        DIRECT,
        JS,
        ASYNC,
        GOOGLE_DFP,
        APPLOVIN;

        public String publicName() {
            return switch (this) {
                case DIRECT -> "Direct web page tag";
                case JS -> "JS tag";
                case ASYNC -> "aSync Tag";
                case GOOGLE_DFP -> "Google Ad Manager (DFP)";
                case APPLOVIN -> "AppLovin";
            };
        }
    }

    public enum videoTagsEnum {
        VASTVPAID,
        JW,
        DFP,
        LKQD,
        CONNATIX,
        API,
        COLUMN6,
        SPRINGSERVE,
        SPOTX,
        ANIVIEW,
        APPLOVIN,
        AERSERV,
        IRONSRC,
        ROKU;

        public String publicName() {
            return switch (this) {
                case VASTVPAID -> "VAST/VPAID tag";
                case JW -> "JW Player";
                case DFP -> "DFP";
                case LKQD -> "LKQD";
                case CONNATIX -> "Connatix video player";
                case API -> "API";
                case COLUMN6 -> "Column 6";
                case SPRINGSERVE -> "SpringServe";
                case SPOTX -> "SpotX";
                case ANIVIEW -> "Aniview";
                case APPLOVIN -> "AppLovin";
                case AERSERV -> "AerServ";
                case IRONSRC -> "Ironsrc";
                case ROKU -> "Roku";
            };
        }
    }

    public enum adPositionEnum {
        ABOVE,
        BELOW,
        HEADER,
        FOOTER,
        SIDEBAR,
        FULLSCREEN,
        UNKNOWN;

        public String publicName() {
            return switch (this) {
                case ABOVE -> "Above the fold";
                case BELOW -> "Below the fold";
                case HEADER -> "Header";
                case FOOTER -> "Footer";
                case SIDEBAR -> "Sidebar";
                case FULLSCREEN -> "Fullscreen";
                case UNKNOWN -> "Unknown";
            };
        }
    }

    public enum apiFrameworksEnum {
        //Order of elements is the same as on placement page, it's important for assertions
        MRAID1,
        ORMMA,
        MRAID2,
        MRAID3,
        VPAID1,
        VPAID2,
        OMID1;

        public String publicName() {
            return switch (this) {
                case MRAID1 -> "MRAID-1";
                case MRAID2 -> "MRAID-2";
                case MRAID3 -> "MRAID-3";
                case ORMMA -> "ORMMA";
                case OMID1 -> "OMID-1";
                case VPAID1 -> "VPAID 1.0";
                case VPAID2 -> "VPAID 2.0";
            };
        }

        // Get Banner frameworks
        public static List<apiFrameworksEnum> getBannerFrameworks() {
            return Arrays.stream(apiFrameworksEnum.values())
                    .filter(f -> f == MRAID1 || f == MRAID2 || f == MRAID3 || f == ORMMA || f == OMID1)
                    .collect(Collectors.toList());
        }

        // Get Video frameworks
        public static List<apiFrameworksEnum> getVideoFrameworks() {
            return Arrays.stream(values())
                    .filter(f -> f == VPAID1 || f == VPAID2 || f == OMID1)
                    .collect(Collectors.toList());
        }
    }

    public enum videoLinearityTypeEnum {
        LINEAR_INSTREAM,
        NONLINEAR_OUTSTREAM;

        public String publicName() {
            return switch (this) {
                case LINEAR_INSTREAM -> "Linear / In-Stream";
                case NONLINEAR_OUTSTREAM -> "Non-linear / Out-Stream";
            };
        }
    }

    public enum placingTypeEnum {
        INSLIDE,
        INCONTENT,
        INPICTURE;

        public String publicName() {
            return switch (this) {
                case INSLIDE -> "In Slide";
                case INCONTENT -> "In Content";
                case INPICTURE -> "In Picture";
            };
        }
    }

    public static String getPlacingType(placingTypeEnum placings) {
        return switch (placings) {
            case INSLIDE -> "In Slide";
            case INCONTENT -> "In Content";
            case INPICTURE -> "In Picture";
        };
    }

    public enum videoStartDelayTypeEnum {
        MIDROLL,
        PREROLL,
        GENERIC_MIDROLL,
        GENERIC_POSTROLL;

        public String publicName() {
            return switch (this) {
                case MIDROLL -> "Mid-Roll";
                case PREROLL -> "Pre-Roll";
                case GENERIC_MIDROLL -> "Generic Mid-Roll";
                case GENERIC_POSTROLL -> "Generic Post-Roll";
            };
        }
    }

    public enum playbackMethodsTypeEnum {
        AUTOPLAY_SOUNDON,
        AUTOPLAY_SOUNDOFF,
        CLICKPLAY_SOUNDOFF,
        MOUSE_OVER;

        public String publicName() {
            return switch (this) {
                case AUTOPLAY_SOUNDON -> "Auto-Play Sound On";
                case AUTOPLAY_SOUNDOFF -> "Auto-Play Sound Off";
                case CLICKPLAY_SOUNDOFF -> "Click-to-Play";
                case MOUSE_OVER -> "Mouse-Over";
            };
        }
    }

    public static String getPlaybackMethods(playbackMethodsTypeEnum method) {
        return switch (method) {
            case AUTOPLAY_SOUNDON -> "Auto-Play Sound On";
            case AUTOPLAY_SOUNDOFF -> "Auto-Play Sound Off";
            case CLICKPLAY_SOUNDOFF -> "Click-to-Play";
            case MOUSE_OVER -> "Mouse-Over";
        };
    }

    public enum protocolsTypeEnum {
        VAST2_0,
        VAST3_0,
        VAST2_0_WRAPPER,
        VAST3_0_WRAPPER,
        VAST4_0,
        VAST4_0_WRAPPER;

        public String publicName() {
            return switch (this) {
                case VAST2_0 -> "VAST 2.0";
                case VAST3_0 -> "VAST 3.0";
                case VAST2_0_WRAPPER -> "VAST 2.0 Wrapper";
                case VAST3_0_WRAPPER -> "VAST 3.0 Wrapper";
                case VAST4_0 -> "VAST 4.0";
                case VAST4_0_WRAPPER -> "VAST 4.0 Wrapper";
            };
        }
    }

    public static String getProtocols(protocolsTypeEnum protocol) {
        return switch (protocol) {
            case VAST2_0 -> "VAST 2.0";
            case VAST3_0 -> "VAST 3.0";
            case VAST2_0_WRAPPER -> "VAST 2.0 Wrapper";
            case VAST3_0_WRAPPER -> "VAST 3.0 Wrapper";
            case VAST4_0 -> "VAST 4.0";
            case VAST4_0_WRAPPER -> "VAST 4.0 Wrapper";
        };
    }

    public enum apiFrameworkVideoTypeEnum {
        VPAID1_0,
        VPAID2_0,
        OMID_1;

        public String publicName() {
            return switch (this) {
                case VPAID1_0 -> "VPAID 1.0";
                case VPAID2_0 -> "VPAID 2.0";
                case OMID_1 -> "OMID-1";
            };
        }
    }

    public enum mimeTypesEnum {
        VIDEO_MP4,
        VIDEO_OGG,
        VIDEO_WEBM,
        APPLICATION_JAVASCRIPT,
        VIDEO_XFLV,
        APPLICATION_XSHOCKWAVEFLASH;

        public String publicName() {
            return switch (this) {
                case VIDEO_MP4 -> "video/mp4";
                case VIDEO_OGG -> "video/ogg";
                case VIDEO_WEBM -> "video/webm";
                case APPLICATION_JAVASCRIPT -> "application/javascript";
                case VIDEO_XFLV -> "video/x-flv";
                case APPLICATION_XSHOCKWAVEFLASH -> "application/x-shockwave-flash";
            };
        }
    }

    public enum adTypeEnum {
        XHTML_TEXT,
        XHTML_BANNER,
        JAVASCRIPT,
        IFRAME;

        public String publicName() {
            return switch (this) {
                case XHTML_TEXT -> "XHTML Text Ad (usually mobile)";
                case XHTML_BANNER -> "XHTML Banner Ad. (usually mobile)";
                case JAVASCRIPT -> "JavaScript Ad; must be valid XHTML";
                case IFRAME -> "iframe";
            };
        }
    }

    public enum placementAttributeEnum {
        AUDIO_AUTO,
        AUDIO_USER,
        EXPANDABLE_AUTO,
        EXPANDABLE_USER_CLICK,
        EXPANDABLE_USER_ROLL,
        BANNER_AUTO,
        BANNER_USER,
        POP,
        SUGGESTIVE,
        FLASHING,
        SURVEYS,
        TEXT,
        INTERACTIVE,
        ALERT_DIALOG,
        AUDIO_TOGGLE,
        SKIPABLE,
        ADOBE_FLASH;

        public String publicName() {
            return switch (this) {
                case AUDIO_AUTO -> "Audio Ad (Auto-Play)";
                case AUDIO_USER -> "Audio Ad (User Initiated)";
                case EXPANDABLE_AUTO -> "Expandable (Automatic)";
                case EXPANDABLE_USER_CLICK -> "Expandable (User Initiated - Click)";
                case EXPANDABLE_USER_ROLL -> "Expandable (User Initiated - Rollover)";
                case BANNER_AUTO -> "In-Banner Video Ad (Auto-Play)";
                case BANNER_USER -> "In-Banner Video Ad (User Initiated)";
                case POP -> "Pop (e.g., Over, Under, or Upon Exit)";
                case SUGGESTIVE -> "Provocative or Suggestive Imagery";
                case FLASHING -> "Shaky, Flashing, Flickering, Extreme Animation, Smileys";
                case SURVEYS -> "Surveys";
                case TEXT -> "Text Only";
                case INTERACTIVE -> "User Interactive (e.g., Embedded Games)";
                case ALERT_DIALOG -> "Windows Dialog or Alert Style";
                case AUDIO_TOGGLE -> "Has Audio On/Off Button";
                case SKIPABLE -> "Ad Can be Skipped (e.g., Skip Button on Pre-Roll Video)";
                case ADOBE_FLASH -> "Adobe Flash";
            };
        }
    }

    public enum placementAdEnvironmentEnum {
        CONTENT_CENTRIC,
        SOCIAL_CENTRIC,
        PRODUCT;

        public String publicName() {
            return switch (this) {
                case CONTENT_CENTRIC -> "Content-centric context";
                case SOCIAL_CENTRIC -> "Social-centric context";
                case PRODUCT -> "Product context";
            };
        }
    }

    public enum placementAdvancedAdSettingsEnum {
        GENERAL_OR_MIXED,
        PRIMARILY_ARTICLE,
        PRIMARILY_VIDEO,
        PRIMARILY_AUDIO,
        PRIMARILY_IMAGE,
        USER_GENERATED,
        GENERAL_SOCIAL,
        PRIMARILY_EMAIL,
        PRIMARILY_CHAT,
        FOCUSED_ON_SELLING,
        APPLICATION_STORE,
        REVIEWS_SITE;

        public String publicName() {
            return switch (this) {
                case GENERAL_OR_MIXED -> "General or mixed content";
                case PRIMARILY_ARTICLE -> "Primarily article content";
                case PRIMARILY_VIDEO -> "Primarily video content";
                case PRIMARILY_AUDIO -> "Primarily audio content";
                case PRIMARILY_IMAGE -> "Primarily image content";
                case USER_GENERATED -> "User-generated content";
                case GENERAL_SOCIAL -> "General social content";
                case PRIMARILY_EMAIL -> "Primarily email content";
                case PRIMARILY_CHAT -> "Primarily chat/IM content";
                case FOCUSED_ON_SELLING -> "Content focused on selling products";
                case APPLICATION_STORE -> "Application store/marketplace";
                case REVIEWS_SITE -> "Product reviews site primarily";
            };
        }
    }

    public enum placementAdUnitOptionsEnum {
        IN_FEED_OF_CONTENT,
        IN_ATOMIC_UNIT_CONTENT,
        OUTSIDE_THE_CORE_CONTENT,
        RECOMMENDATION_WIDGET;

        public String publicName() {
            return switch (this) {
                case IN_FEED_OF_CONTENT -> "In the feed of content";
                case IN_ATOMIC_UNIT_CONTENT -> "In the atomic unit of the content";
                case OUTSIDE_THE_CORE_CONTENT -> "Outside the core content";
                case RECOMMENDATION_WIDGET -> "Recommendation widget";
            };
        }
    }

    public static String getAdUnitOptionsName(placementAdUnitOptionsEnum adUnitOptions) {
        return switch (adUnitOptions) {
            case IN_FEED_OF_CONTENT -> "In the feed of content";
            case IN_ATOMIC_UNIT_CONTENT -> "In the atomic unit of the content";
            case OUTSIDE_THE_CORE_CONTENT -> "Outside the core content";
            case RECOMMENDATION_WIDGET -> "Recommendation widget";
        };
    }

    public enum placementDataAssetTypeEnum {
        SPONSORED,
        DESC,
        RATING,
        LIKES,
        DOWNLOADS,
        PRICE,
        SALEPRICE,
        PHONE,
        ADDRESS,
        DESC2,
        DISPLAYURL,
        CTATEXT;

        public String publicName() {
            return switch (this) {
                case SPONSORED -> "Sponsored";
                case DESC -> "Desc";
                case RATING -> "Rating";
                case LIKES -> "Likes";
                case DOWNLOADS -> "Downloads";
                case PRICE -> "Price";
                case SALEPRICE -> "Sale price";
                case PHONE -> "Phone";
                case ADDRESS -> "Address";
                case DESC2 -> "Desc2";
                case DISPLAYURL -> "Display URL";
                case CTATEXT -> "CTA text";
            };
        }
    }

    public enum placementImageTypeEnum {
        ICON,
        MAIN;

        public String publicName() {
            return switch (this) {
                case ICON -> "Icon";
                case MAIN -> "Main";
            };
        }
    }

}


