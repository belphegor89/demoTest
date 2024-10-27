package data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class CommonEnums {

    public enum regionsEnum {
        US_EAST,
        US_WEST,
        EU,
        APAC;

        public String publicName(){
            return switch (this){
                case US_EAST -> "US_EAST";
                case US_WEST -> "US_WEST";
                case EU -> "EU";
                case APAC -> "APAC";
            };
        }

        public String attributeName(){
            return switch (this){
                case US_EAST -> "US_EAST";
                case US_WEST -> "US_WEST";
                case EU -> "EU";
                case APAC -> "APAC";
            };
        }
    }

    public enum calendarRangeEnum{
        TODAY,
        YESTERDAY,
        LAST_7_DAYS,
        LAST_30_DAYS,
        THIS_MONTH,
        LAST_MONTH,
        LIFETIME,
        CUSTOM;

        public String publicName(){
            return switch (this){
                case TODAY -> "Today";
                case YESTERDAY -> "Yesterday";
                case LAST_7_DAYS -> "Last 7 Days";
                case LAST_30_DAYS -> "Last 30 Days";
                case THIS_MONTH -> "This Month";
                case LAST_MONTH -> "Last Month";
                case LIFETIME -> "Lifetime";
                case CUSTOM -> "Custom";
            };
        }
    }

    public enum endpointSideEnum{
        SSP,
        DSP
    }

    public enum endpointTypeEnum {
        RTB,
        VAST,
        PREBID,
        JS;

        public String publicName() {
            return switch (this) {
                case RTB -> "RTB";
                case VAST -> "VAST";
                case PREBID -> "Prebid";
                case JS -> "JS Tag";
            };
        }

        public String attributeName() {
            return switch (this) {
                case RTB -> "rtb";
                case VAST -> "vast";
                case PREBID -> "prebid";
                case JS -> "js";
            };
        }

        public static List<endpointTypeEnum> getSspTypes(){
            return Arrays.asList(endpointTypeEnum.RTB, endpointTypeEnum.VAST, endpointTypeEnum.JS);
        }

        public static List<endpointTypeEnum> getDspTypes(){
            return Arrays.asList(endpointTypeEnum.RTB, endpointTypeEnum.VAST, endpointTypeEnum.JS, endpointTypeEnum.PREBID);
        }
    }

    public enum auctionTypeEnum {
        FIRST,
        SECOND;

        public String publicName() {
            return switch (this) {
                case FIRST -> "First Price";
                case SECOND -> "Second Price";
            };
        }
    }

    public enum rtbVersionEnum {
        v23,
        v24,
        v25,
        v26;

        public String publicName() {
            return switch (this) {
                case v23 -> "2.3";
                case v24 -> "2.4";
                case v25 -> "2.5";
                case v26 -> "2.6";
            };
        }
    }

    public enum adFormatPlacementTypeEnum{
        BANNER,
        VIDEO,
        REWARDED_VIDEO,
        AUDIO,
        NATIVE;

        public String attributeName(){
            return switch (this){
                case BANNER -> "banner";
                case VIDEO -> "video";
                case REWARDED_VIDEO -> "rewarded_video";
                case AUDIO -> "audio";
                case NATIVE -> "native";
            };
        }

        public String publicName(){
            return switch (this){
                case BANNER -> "Banner";
                case VIDEO -> "Video";
                case REWARDED_VIDEO -> "Rewarded Video";
                case AUDIO -> "Audio";
                case NATIVE -> "Native";
            };
        }
    }

    public enum trafficTypeEnum {
        WEB,
        APP,
        MOBILE_WEB,
        CTV,
        UNKNOWN;

        public String publicName() {
            return switch (this) {
                case WEB -> "Desktop";
                case APP -> "Mobile App";
                case MOBILE_WEB -> "Mobile Web";
                case CTV -> "CTV";
                case UNKNOWN -> "Unknown";
            };
        }

        public String attributeName() {
            return switch (this) {
                case WEB -> "web";
                case APP -> "app";
                case MOBILE_WEB -> "mobile_web";
                case CTV -> "ctv";
                case UNKNOWN -> "unknown";
            };
        }
    }

    public enum advancedSettingsEnum {
        NURL_SSP,
        NURL_DSP,
        IFA,
        SYNCED,
        COMPRESSED,
        PREMIUM,
        IPV6,
        LAT,
        BID_ANALYSER,
        SCHAIN,
        REWARDED,
        DYNAMIC_MARGIN,
        GDPR,
        PMP,
        RCPM;

        public String attributeName() {
            return switch (this) {
                case NURL_SSP -> "nurl";
                case NURL_DSP -> "billing_event";
                case IFA -> "ifa_required";
                case SYNCED -> "is_only_synced";
                case COMPRESSED -> "compressed";
                case PREMIUM -> "dirty_traffic";
                case IPV6 -> "ipv6_filter";
                case BID_ANALYSER -> "bid_analyser";
                case SCHAIN -> "is_schain";
                case REWARDED -> "is_rewarded";
                case DYNAMIC_MARGIN -> "dynamic_margin";
                case GDPR -> "gdpr_consent";
                case RCPM -> "is_rcpm";
                case PMP -> "is_pmp_traffic";
                case LAT -> "lat_filter";
            };
        }
    }

    public enum bidPriceTypeEnum{
        FLOOR,
        FIXED;

        public String publicName() {
            return switch (this) {
                case FLOOR -> "Bid Floor";
                case FIXED -> "Fixed Bid Price";
            };
        }
    }

    public enum adaptersSspRtbEnum{
        ANY,
        CHARTBOOST,
        FYBER,
        UNITY;

        public String publicName() {
            return switch (this) {
                case ANY -> "Any Adapters";
                case CHARTBOOST -> "Chartboost";
                case FYBER -> "Fyber";
                case UNITY -> "Unity";
            };
        }
    }

    public enum endpointPartnerApiTypeEnum{
        JSON,
        XML,
        CSV,
        INVALID
    }

    public enum endpointPartnerApiColumnsEnum {
        DATE,
        IMPRESSION,
        SPEND
    }

    public enum iabSizesEnum{
        CUSTOM("endpoint, inventoryWeb, inventoryApp"),
        FULLSCREEN("endpoint, inventoryApp"),
        PHONES_STANDARD("endpoint, inventoryApp"),
        PHONES_MEDIUM("inventoryApp"),
        PHONES_LARGE("endpoint, inventoryApp"),
        TABLETS_LEADERBOARD("endpoint, inventoryApp"),
        TABLETS_FULLSIZE("inventoryApp"),
        LEADERBOARD("endpoint, inventoryWeb"),
        SKYSCRAPER("endpoint, inventoryWeb"),
        BUTTON1("endpoint, inventoryWeb"),
        BUTTON2("endpoint, inventoryWeb"),
        BANNER_HALF("endpoint, inventoryWeb"),
        MICRO_BAR("endpoint, inventoryWeb"),
        BUTTON_SQUARE("endpoint, inventoryWeb"),
        BANNER_VERTICAL("endpoint, inventoryWeb"),
        BANNER_FULL("endpoint, inventoryWeb"),
        RECTANGLE("endpoint, inventoryWeb"),
        RECTANGLE_MEDIUM("inventoryWeb, endpoint"),
        RECTANGLE_LARGE("endpoint, inventoryWeb"),
        RECTANGLE_VERTICAL("endpoint, inventoryWeb"),
        POPUP_SQUARE("endpoint, inventoryWeb"),
        POPUNDER("endpoint, inventoryWeb"),
        SKYSCRAPER_WIDE("endpoint, inventoryWeb"),
        RECTANGLE_31("endpoint, inventoryWeb");

        private final String type;

        iabSizesEnum(String s){
            this.type = s;
        }

        private String getType(){
            return this.type;
        }

        public static Stream<iabSizesEnum> getValues(String type){
            return Arrays.stream(iabSizesEnum.values()).filter(t -> t.getType().contains(type));
        }

        public String publicName(){
            return switch (this){
                case CUSTOM -> "Custom";
                case FULLSCREEN -> "Full-screen Banner";
                case PHONES_STANDARD -> "Standard Banner Phones and Tablets (320x50)";
                case PHONES_MEDIUM -> "IAB Medium Rectangle Phones and Tablets (300x250)";
                case PHONES_LARGE -> "Large Banner Phones and Tablets (320x100)";
                case TABLETS_LEADERBOARD -> "IAB Leaderboard Tablets (728x90)";
                case TABLETS_FULLSIZE -> "IAB Full-Size Banner Tablets (468x60)";
                case SKYSCRAPER -> "IAB Skyscraper (120 x 600)";
                case LEADERBOARD -> "IAB Leaderboard (728 x 90)";
                case BUTTON1 -> "IAB Button 1 (120 x 90)";
                case BUTTON2 -> "IAB Button 2 (120 x 60)";
                case BANNER_HALF -> "IAB Half Banner (234 x 60)";
                case MICRO_BAR -> "IAB Micro Bar (88 x 31)";
                case BUTTON_SQUARE -> "IAB Square Button (125 x 125)";
                case BANNER_VERTICAL -> "IAB Vertical Banner (120 x 240)";
                case BANNER_FULL -> "IAB Full Banner (468 x 60)";
                case RECTANGLE -> "IAB Rectangle (180 x 150)";
                case RECTANGLE_MEDIUM -> "IAB Medium Rectangle (300 x 250)";
                case RECTANGLE_LARGE -> "IAB Large Rectangle (336 x 280)";
                case RECTANGLE_VERTICAL -> "IAB Vertical Rectangle (240 x 400)";
                case POPUP_SQUARE -> "IAB Square Pop-up (250 x 250)";
                case POPUNDER -> "IAB Pop-Under (720 x 300)";
                case SKYSCRAPER_WIDE -> "IAB Wide Skyscraper (160 x 600)";
                case RECTANGLE_31 -> "IAB 3:1 Rectangle (300 x 100)";
            };
        }

        public List<Integer> getWidthHeight(){
            List<Integer> widthHeight = new ArrayList<>();
            String sizeName = this.publicName(), measures;
            if (this != iabSizesEnum.CUSTOM && this != iabSizesEnum.FULLSCREEN){
                measures = sizeName.substring(sizeName.indexOf("("), sizeName.indexOf(")")).replace("(", "").replace(")", "");
                widthHeight.add(Integer.valueOf(measures.substring(0, measures.indexOf("x")).trim()));
                widthHeight.add(Integer.valueOf(measures.substring(measures.indexOf("x") + 1).trim()));
            } else {
                widthHeight.add(0);
                widthHeight.add(0);
            }
            return widthHeight;
        }

        public Integer getWidth(){
            String sizeName = this.publicName(), measures;
            if (this != iabSizesEnum.CUSTOM && this != iabSizesEnum.FULLSCREEN){
                measures = sizeName.substring(sizeName.indexOf("("), sizeName.indexOf(")")).replace("(", "").replace(")", "");
                return Integer.valueOf(measures.substring(0, measures.indexOf("x")).trim());
            } else {
                return 0;
            }
        }

        public Integer getHeight(){
            String sizeName = this.publicName(), measures;
            if (this != iabSizesEnum.CUSTOM && this != iabSizesEnum.FULLSCREEN){
                measures = sizeName.substring(sizeName.indexOf("("), sizeName.indexOf(")")).replace("(", "").replace(")", "");
                return Integer.valueOf(measures.substring(measures.indexOf("x") + 1).trim());
            } else {
                return 0;
            }
        }
    }

    public enum videoSizeEnum{
        CUSTOM,
        x720,
        x1024,
        x768,
        x250,
        x320,
        x480,
        x1080;

        public List<videoSizeEnum> getValues(videoSizeEnum exceptValue){
            List<videoSizeEnum> returnList = Arrays.asList(videoSizeEnum.values());
            returnList.remove(exceptValue);
            return returnList;
        }

        public String publicName(){
            return switch (this){
                case CUSTOM -> "Custom";
                case x720 -> "1280x720";
                case x1024 -> "768x1024";
                case x768 -> "1024x768";
                case x250 -> "300x250";
                case x320 -> "480x320";
                case x480 -> "320x480";
                case x1080 -> "1920x1080";
            };
        }

        public Integer getWidth(){
            String sizeName = this.publicName();
            if (this != videoSizeEnum.CUSTOM){
                return Integer.valueOf(sizeName.substring(0, sizeName.indexOf("x")).trim());
            } else {
                return 0;
            }
        }

        public Integer getHeight(){
            String sizeName = this.publicName();
            if (this != videoSizeEnum.CUSTOM){
                return Integer.valueOf(sizeName.substring(sizeName.indexOf("x") + 1).trim());
            } else {
                return 0;
            }
        }
    }

    public enum pagingActionsTypes{
        FIRST,
        LAST,
        NUMBER,
        FORWARD,
        BACKWARD
    }

    public enum marginTypeEnum{
        FIXED,
        ADAPTIVE,
        RANGE;

        public String publicName(){
            return switch (this){
                case FIXED -> "Fixed";
                case ADAPTIVE -> "Adaptive";
                case RANGE -> "Range";
            };
        }
    }

    public enum directnessTypeEnum{
        DIRECT,
        RESELLER,
        BOTH;

        public String publicName(){
            return switch (this){
                case DIRECT -> "DIRECT";
                case RESELLER -> "RESELLER";
                case BOTH -> "BOTH";
            };
        }
    }

    public enum operatingSystemEnum{
        ANY,
        OS,
        JOLI,
        AIX,
        AMIGAOS,
        IOS,
        OS_2,
        BADA,
        SYMBIAN,
        LINUX,
        WINDOWS,
        MAC_OS,
        BLACKBERRY,
        QNX,
        ANDROID,
        SUSE,
        FEDORA,
        SOLARIS,
        UNIX,
        DEBIAN,
        KUBUNTU,
        WINDOWS_PHONE,
        CHROMIUM_OS,
        SAILFISH,
        PLAYSTATION,
        OPENBSD,
        NETBSD,
        HAIKU,
        MINT,
        CENTOS,
        MEEGO,
        RIM_TABLET_OS,
        OPENSOLARIS,
        FREEBSD,
        WINDOWS_PHONE_OS,
        DRAGONFLY,
        GENTOO,
        NINTENDO,
        FIREFOX_OS,
        MAGEIA,
        TIZEN,
        RISC_OS,
        ZENWALK,
        SLACKWARE,
        MANDRIVA,
        OPENSUSE,
        REDHAT,
        BEOS,
        UBUNTU;

        public String publicName(){
            return switch (this){
                case ANY -> "Any";
                case OS -> "os";
                case JOLI -> "Joli";
                case AIX -> "AIX";
                case AMIGAOS -> "AmigaOS";
                case IOS -> "iOS";
                case OS_2 -> "OS/2";
                case BADA -> "BADA";
                case SYMBIAN -> "Symbian";
                case LINUX -> "Linux";
                case WINDOWS -> "Windows";
                case MAC_OS -> "Mac OS";
                case BLACKBERRY -> "BlackBerry";
                case QNX -> "QNX";
                case ANDROID -> "Android";
                case SUSE -> "SUSE";
                case FEDORA -> "Fedora";
                case SOLARIS -> "Solaris";
                case UNIX -> "UNIX";
                case DEBIAN -> "Debian";
                case KUBUNTU -> "Kubuntu";
                case WINDOWS_PHONE -> "Windows Phone";
                case CHROMIUM_OS -> "Chromium OS";
                case SAILFISH -> "Sailfish";
                case PLAYSTATION -> "PlayStation";
                case OPENBSD -> "OpenBSD";
                case NETBSD -> "NetBSD";
                case HAIKU -> "Haiku";
                case MINT -> "Mint";
                case CENTOS -> "CentOS";
                case MEEGO -> "MeeGo";
                case RIM_TABLET_OS -> "RIM Tablet OS";
                case OPENSOLARIS -> "OpenSolaris";
                case FREEBSD -> "FreeBSD";
                case WINDOWS_PHONE_OS -> "Windows Phone OS";
                case DRAGONFLY -> "DragonFly";
                case GENTOO -> "Gentoo";
                case NINTENDO -> "Nintendo";
                case FIREFOX_OS -> "Firefox OS";
                case MAGEIA -> "Mageia";
                case TIZEN -> "Tizen";
                case RISC_OS -> "RISC OS";
                case ZENWALK -> "Zenwalk";
                case SLACKWARE -> "Slackware";
                case MANDRIVA -> "Mandriva";
                case OPENSUSE -> "openSUSE";
                case REDHAT -> "Redhat";
                case BEOS -> "BeOS";
                case UBUNTU -> "Ubuntu";
            };
        }
    }

    public enum countriesEnum{
        AFGHANISTAN("Afghanistan", "AFG"),
        ALBANIA("Albania", "ALB"),
        ALGERIA("Algeria", "DZA"),
        SAMOA_AMERICAN("American Samoa", "ASM"),
        ANDORRA("Andorra", "AND"),
        ANGOLA("Angola", "AGO"),
        ANGUILLA("Anguilla", "AIA"),
        ANTARCTICA("Antarctica", "ATA"),
        ANTIGUA_AND_BARBUDA("Antigua and Barbuda", "ATG"),
        ARGENTINA("Argentina", "ARG"),
        ARMENIA("Armenia", "ARM"),
        ARUBA("Aruba", "ABW"),
        AUSTRALIA("Australia", "AUS"),
        AUSTRIA("Austria", "AUT"),
        AZERBAIJAN("Azerbaijan", "AZE"),
        BAHRAIN("Bahrain", "BHR"),
        BANGLADESH("Bangladesh", "BGD"),
        BARBADOS("Barbados", "BRB"),
        BELARUS("Belarus", "BLR"),
        BELGIUM("Belgium", "BEL"),
        BELIZE("Belize", "BLZ"),
        BENIN("Benin", "BEN"),
        BERMUDA("Bermuda", "BMU"),
        BHUTAN("Bhutan", "BTN"),
        BOLIVIA("Bolivia", "BOL"),
        BOSNIA_AND_HERZEGOVINA("Bosnia and Herzegovina", "BIH"),
        BOTSWANA("Botswana", "BWA"),
        BRAZIL("Brazil", "BRA"),
        BRITISH_INDIAN("British Indian Ocean Territory", "IOT"),
        BRITISH_VIRGIN("British Virgin Islands", "VGB"),
        BRUNEI("Brunei", "BRN"),
        BULGARIA("Bulgaria", "BGR"),
        BURKINA_FASO("Burkina Faso", "BFA"),
        BURUNDI("Burundi", "BDI"),
        CAMBODIA("Cambodia", "KHM"),
        CAMEROON("Cameroon", "CMR"),
        CANADA("Canada", "CAN"),
        CAPE_VERDE("Cape Verde", "CPV"),
        CAYMAN_ISLANDS("Cayman Islands", "CYM"),
        CENTRAL_AFRICAN_REPUBLIC("Central African Republic", "CAF"),
        CHAD("Chad", "TCD"),
        CHILE("Chile", "CHL"),
        CHINA("China", "CHN"),
        CHRISTMAS_ISLAND("Christmas Island", "CXR"),
        COCOS_ISLANDS("Cocos Islands", "CCK"),
        COLOMBIA("Colombia", "COL"),
        COMOROS("Comoros", "COM"),
        CONGO_DEM_REP("Congo, Democratic Republic of the", "COD"),
        CONGO_REP("Congo, Republic of the", "COG"),
        COOK_ISLANDS("Cook Islands", "COK"),
        COSTA_RICA("Costa Rica", "CRI"),
        COTE_D_IVOIRE("Cote d Ivoire", "CIV"),
        CROATIA("Croatia", "HRV"),
        CUBA("Cuba", "CUB"),
        CURACAO("Curacao", "CUW"),
        CYPRUS("Cyprus", "CYP"),
        CZECH("Czech Republic", "CZE"),
        DENMARK("Denmark", "DNK"),
        DJIBOUTI("Djibouti", "DJI"),
        DOMINICA("Dominica", "DMA"),
        DOMINICAN_REPUBLIC("Dominican Republic", "DOM"),
        EAST_TIMOR("East Timor (Timor-Leste)", "TLS"),
        ECUADOR("Ecuador", "ECU"),
        EGYPT("Egypt", "EGY"),
        EL_SALVADOR("El Salvador", "SLV"),
        EQUATORIAL_GUINEA("Equatorial Guinea", "GNQ"),
        ERITREA("Eritrea", "ERI"),
        ESTONIA("Estonia", "EST"),
        ETHIOPIA("Ethiopia", "ETH"),
        FALKLAND_ISLANDS("Falkland Islands", "FLK"),
        FAROE_ISLANDS("Faroe Islands", "FRO"),
        FIJI("Fiji", "FJI"),
        FINLAND("Finland", "FIN"),
        FRANCE("France", "FRA"),
        FRENCH_POLYNESIA("French Polynesia", "PYF"),
        GABON("Gabon", "GAB"),
        GEORGIA("Georgia", "GEO"),
        GERMANY("Germany", "DEU"),
        GHANA("Ghana", "GHA"),
        GIBRALTAR("Gibraltar", "GIB"),
        GREECE("Greece", "GRC"),
        GREENLAND("Greenland", "GRL"),
        GRENADA("Grenada", "GRD"),
        GUAM("Guam", "GUM"),
        GUATEMALA("Guatemala", "GTM"),
        GUERNSEY("Guernsey", "GGY"),
        GUINEA("Guinea", "GIN"),
        GUINEA_BISSAU("Guinea-Bissau", "GNB"),
        GUYANA("Guyana", "GUY"),
        HAITI("Haiti", "HTI"),
        HONDURAS("Honduras", "HND"),
        HONG_KONG("Hong Kong", "HKG"),
        HUNGARY("Hungary", "HUN"),
        ICELAND("Iceland", "ISL"),
        INDIA("India", "IND"),
        INDONESIA("Indonesia", "IDN"),
        IRAN("Iran", "IRN"),
        IRAQ("Iraq", "IRQ"),
        IRELAND("Ireland", "IRL"),
        ISLE_OF_MAN("Isle of Man", "IMN"),
        ISRAEL("Israel", "ISR"),
        ITALY("Italy", "ITA"),
        JAMAICA("Jamaica", "JAM"),
        JAPAN("Japan", "JPN"),
        JERSEY("Jersey", "JEY"),
        JORDAN("Jordan", "JOR"),
        KAZAKHSTAN("Kazakhstan", "KAZ"),
        KENYA("Kenya", "KEN"),
        KIRIBATI("Kiribati", "KIR"),
        KOREA_NORTH("Korea, North", "PRK"),
        KOREA_SOUTH("Korea, South", "KOR"),
        KOSOVO("Kosovo", "XKX"),
        KUWAIT("Kuwait", "KWT"),
        KYRGYZSTAN("Kyrgyzstan", "KGZ"),
        LAOS("Laos", "LAO"),
        LATVIA("Latvia", "LVA"),
        LEBANON("Lebanon", "LBN"),
        LESOTHO("Lesotho", "LSO"),
        LIBERIA("Liberia", "LBR"),
        LIBYA("Libya", "LBY"),
        LIECHTENSTEIN("Liechtenstein", "LIE"),
        LITHUANIA("Lithuania", "LTU"),
        LUXEMBOURG("Luxembourg", "LUX"),
        MACAU("Macau", "MAC"),
        MACEDONIA("Macedonia", "MKD"),
        MADAGASCAR("Madagascar", "MDG"),
        MALAWI("Malawi", "MWI"),
        MALAYSIA("Malaysia", "MYS"),
        MALDIVES("Maldives", "MDV"),
        MALI("Mali", "MLI"),
        MALTA("Malta", "MLT"),
        MARSHALL_ISLANDS("Marshall Islands", "MHL"),
        MAURITANIA("Mauritania", "MRT"),
        MAURITIUS("Mauritius", "MUS"),
        MAYOTTE("Mayotte", "MYT"),
        MEXICO("Mexico", "MEX"),
        MICRONESIA("Micronesia, Federated States of", "FSM"),
        MOLDOVA("Moldova", "MDA"),
        MONACO("Monaco", "MCO"),
        MONGOLIA("Mongolia", "MNG"),
        MONTENEGRO("Montenegro", "MNE"),
        MONTSERRAT("Montserrat", "MSR"),
        MOROCCO("Morocco", "MAR"),
        MOZAMBIQUE("Mozambique", "MOZ"),
        MYANMAR("Myanmar (Burma)", "MMR"),
        NAMIBIA("Namibia", "NAM"),
        NAURU("Nauru", "NRU"),
        NEPAL("Nepal", "NPL"),
        NETHERLANDS("Netherlands", "NLD"),
        NETHERLANDS_ANTILLES("Netherlands Antilles", "ANT"),
        NEW_CALEDONIA("New Caledonia", "NCL"),
        NEW_ZEALAND("New Zealand", "NZL"),
        NICARAGUA("Nicaragua", "NIC"),
        NIGER("Niger", "NER"),
        NIGERIA("Nigeria", "NGA"),
        NIUE("Niue", "NIU"),
        NORTHERN_MARIANA_ISLANDS("Northern Mariana Islands", "MNP"),
        NORWAY("Norway", "NOR"),
        OMAN("Oman", "OMN"),
        PAKISTAN("Pakistan", "PAK"),
        PALAU("Palau", "PLW"),
        PALESTINE("Palestine", "PSE"),
        PANAMA("Panama", "PAN"),
        PAPUA_NEW_GUINEA("Papua New Guinea", "PNG"),
        PARAGUAY("Paraguay", "PRY"),
        PERU("Peru", "PER"),
        PHILIPPINES("Philippines", "PHL"),
        PITCAIRN("Pitcairn", "PCN"),
        POLAND("Poland", "POL"),
        PORTUGAL("Portugal", "PRT"),
        PUERTO_RICO("Puerto-Rico", "PRI"),
        QATAR("Qatar", "QAT"),
        REUNION("Reunion", "REU"),
        ROMANIA("Romania", "ROU"),
        RUSSIA("Russia", "RUS"),
        RWANDA("Rwanda", "RWA"),
        SAINT_BARTHELEMY("Saint Barthelemy", "BLM"),
        SAINT_HELENA("Saint Helena", "SHN"),
        SAINT_KITTS_AND_NEVIS("Saint Kitts and Nevis", "KNA"),
        SAINT_LUCIA("Saint Lucia", "LCA"),
        SAINT_MARTIN("Saint Martin", "MAF"),
        SAINT_PIERRE_AND_MIQUELON("Saint Pierre and Miquelon", "SPM"),
        SAINT_VINCENT_AND_THE_GRENADINES("Saint Vincent and the Grenadines", "VCT"),
        SAMOA("Samoa", "WSM"),
        SAN_MARINO("San Marino", "SMR"),
        SAO_TOME_AND_PRINCIPE("Sao Tome and Principe", "STP"),
        SAUDI_ARABIA("Saudi Arabia", "SAU"),
        SENEGAL("Senegal", "SEN"),
        SERBIA("Serbia", "SRB"),
        SEYCHELLES("Seychelles", "SYC"),
        SIERRA_LEONE("Sierra Leone", "SLE"),
        SINGAPORE("Singapore", "SGP"),
        SINT_MAARTEN("Sint Maarten", "SXM"),
        SLOVAKIA("Slovakia", "SVK"),
        SLOVENIA("Slovenia", "SVN"),
        SOLOMON_ISLANDS("Solomon Islands", "SLB"),
        SOMALIA("Somalia", "SOM"),
        SOUTH_AFRICA("South Africa", "ZAF"),
        SOUTH_SUDAN("South Sudan", "SSD"),
        SPAIN("Spain", "ESP"),
        SRI_LANKA("Sri Lanka", "LKA"),
        SUDAN("Sudan", "SDN"),
        SURINAME("Suriname", "SUR"),
        SVALBARD_AND_JAN_MAYEN("Svalbard and Jan Mayen", "SJM"),
        SWAZILAND("Swaziland", "SWZ"),
        SWEDEN("Sweden", "SWE"),
        SWITZERLAND("Switzerland", "CHE"),
        SYRIA("Syria", "SYR"),
        TAIWAN("Taiwan", "TWN"),
        TAJIKISTAN("Tajikistan", "TJK"),
        TANZANIA("Tanzania", "TZA"),
        THAILAND("Thailand", "THA"),
        THE_BAHAMAS("The Bahamas", "BHS"),
        THE_GAMBIA("The Gambia", "GMB"),
        TOGO("Togo", "TGO"),
        TOKELAU("Tokelau", "TKL"),
        TONGA("Tonga", "TON"),
        TRINIDAD_AND_TOBAGO("Trinidad and Tobago", "TTO"),
        TUNISIA("Tunisia", "TUN"),
        TURKEY("Turkey", "TUR"),
        TURKMENISTAN("Turkmenistan", "TKM"),
        TURKS_AND_CAICOS_ISLANDS("Turks and Caicos Islands", "TCA"),
        TUVALU("Tuvalu", "TUV"),
        US_VIRGIN_ISLANDS("U.S. Virgin Islands", "VIR"),
        UGANDA("Uganda", "UGA"),
        UKRAINE("Ukraine", "UKR"),
        UAE("United Arab Emirates", "ARE"),
        UK("United Kingdom", "GBR"),
        USA("United States of America", "USA"),
        URUGUAY("Uruguay", "URY"),
        UZBEKISTAN("Uzbekistan", "UZB"),
        VANUATU("Vanuatu", "VUT"),
        VATICAN_CITY("Vatican City (Holy See)", "VAT"),
        VENEZUELA("Venezuela", "VEN"),
        VIETNAM("Vietnam", "VNM"),
        WALLIS_AND_FUTUNA("Wallis and Futuna", "WLF"),
        WESTERN_SAHARA("Western Sahara", "ESH"),
        YEMEN("Yemen", "YEM"),
        ZAMBIA("Zambia", "ZMB"),
        ZIMBABWE("Zimbabwe", "ZWE");

        private final String publName, iso3;

        countriesEnum(String name, String iso3){
            this.publName = name;
            this.iso3 = iso3;
        }

        public String publicName(){
            return publName;
        }

        public String getIso3(){
            return iso3;
        }
    }

    public enum abTestEnum{
        RCPM,
        ADAPTIVE_MARGIN;

        public String publicName(){
            return switch (this){
                case RCPM -> "RCPM";
                case ADAPTIVE_MARGIN -> "Adaptive Margin";
            };
        }

        public static abTestEnum getValueByPublicName(String name) {
            for (abTestEnum value : abTestEnum.values()) {
                if (value.publicName().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("No enum constant abTestEnum with publicName '" + name + "'");
        }
    }

}
