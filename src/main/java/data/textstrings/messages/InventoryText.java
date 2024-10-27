package data.textstrings.messages;

public class InventoryText extends CommonText {

    public static final String EMPTY_PAGE_SEARCH = "You don't have inventories",
            invPlacementsCntEmpty = "You don't have any placements";

    //<editor-fold desc="Validation - placements">
    public static final String
            invalidCharactersValidationErrorMessage = "The Placement title has invalid format",
            placementNameLessThan3Characters = "The Placement title must be at least 3 characters.",
            placementNameFieldMoreThan255characters = "The Placement title may not be greater than 255 characters.",
            valueFromTheBidPriceField = "The Floor Price field is required.",
            valueLargerThan999999_99InIheBidPrice = "The Floor Price may not be greater than 999999.99.",
            valuesBiggerThan32766InTheWidthFields = "The Width may not be greater than 32766.",
            valuesBiggerThan32766InTheHeightFields = "The Height may not be greater than 32766.",
            emptyStartDelay = "The Start delay in seconds field is required",
            lessThanZeroInTheStartDelay = "The Start delay in seconds must be at least 0.",
            bigValueMore53600StartDelay = "The Start delay in seconds may not be greater than 53600.",
            emptySkipDelay = "The Skip Delay required when allow skip is enable",
            nativeModalAdElementsRequired = "It is required to fill at least one Ad Element";
    //</editor-fold>

    //<editor-fold desc="Placement tags - Banners">

    //<editor-fold desc="Texts">
    public static final String
            bannerWebDirectTagText = "This tag is designed for you to copy and paste into the web page where you want to run ads. For more details please refer to JS Tag Banner Integration, Direct Web Page Tag.",
            bannerWebJsTagText = "Please use it if you want to pass customized macros values or if you are traffic aggregator. Also fits for adserver integrations. Please make sure that you swap out the macros with the values in order to get ads. For more details please refer to Adserver.",
            bannerInAppJsText = "Please use it if you want to pass customized macros values or if you are traffic aggregator. Also fits for adserver integrations. Please make sure that you swap out the macros with the values in order to get ads. For more details please refer to specific type of integration",
            bannerWebGoogleTagText = "This tag is for web format only. DFP tags do not work for inapp. You can use it as custom tag in Google Ad Manager UI. For more details please refer to specific type of integration.",
            bannerWebAsyncTagText_head = "If you want to load the web page content before ads is loaded there, please use this tag. For more details please refer to JS Tag Banner Integration, Async Tag",
            bannerWebAsyncTagText_body = "Here are the steps to reproduce in order to embed it to the page:",
            bannerWebAsyncTagText_values = "code - String id of the block to place ads in;\nplacement_id - Integerid of the block to place ads in;\nsizes: [width<Integer>, height<Integer>] – sizes. The first number stands for width and the second one for height.",
            bannerInAppApplovinText = "If you use AppLovin SDK and want to send the requests for ads from there, please use this tag. For more details please refer to specific type of integration";
    //</editor-fold>

    //<editor-fold desc="Code">
    public static final String
            bannerWebDirectTagCode = "<script type=\"text/javascript\" src=\"https://.+/?c=b&m=s&placementId=${placementId}\"></script>",
            bannerWebJsTagCode = "<script type=\"text/javascript\" src=\"https://.+/?c=b&m=tag&placementId=${placementId}&ip=[IP]&ua=[UA]&domain=[DOMAIN]&page=[PAGE]&secure=[SECURE]&language=[BROWSER_LANGUAGE]&bidfloor=[BIDFLOOR]&gdpr_consent=[GDPR_CONSENT]&gpp=[GPP]&gpp_sid=[GPP_SID]\"></script>",
            bannerInAppJsCode = "<script type=\"text/javascript\" src=\"https://.+?c=b&m=tag&app=1&res=json&placementId=${placementId}&ip=[IP]&ua=[UA]&name=[APP_NAME]&bundle=[APP_BUNDLE]&ver=[APP_VERSION]&os=[DEVICE_OS]&osv=[DEVICE_OS_VERSION]&make=[DEVICE_MAKE]&model=[DEVICE_MODEL]&w=[DEVICE_W]&h=[DEVICE_H]&hwv=[DEVICE_HW_VERSION]&ifa=[IFA]&js=[JS]&carrier=[CARRIER]&paid=[PAID]&country=[GEO_COUNTRY]&geo_type=[GEO_TYPE]&city=[GEO_CITY]&lat=[GEO_LAT]&lon=[GEO_LON]&bidfloor=[BIDFLOOR]&gdpr_consent=[GDPR_CONSENT]&gpp=[GPP]&gpp_sid=[GPP_SID]\"></script>",
            bannerInAppApplovinCode = "<script src=\"https://.+?c=b&m=api&app=1&res=applovin&placementId=${placementId}&sizes=${bw}x${bh}&bundle=%%BUNDLE%%&ifa=%%ADVERTISING_ID_IFA%%&ifv=%%ADVERTISING_ID_IFV%%&lat=%%LATITUDE%%&lon=%%LONGITUDE%%&lltype=2&dnt=%%DNT%%&cb=%%CACHEBUSTER%%\"></script>",
            bannerWebGoogleTagCode = "<script type=\"text/javascript\" src=\"https://.+/?c=b&m=api&res=js&placementId=${placementId}&domain=%%SITE%%&page=%%PATTERN:url%%&clickurl=%%CLICK_URL_ESC%%\"></script>",
            bannerWebAsyncTagCode_head = "<script src=\"https://.+/?c=res&m=async\"></script>",
            bannerWebAsyncTagCode_body = "<div id=\"block_${placementId}\"></div>",
            bannerWebAsyncTagCode_ads = "<script type=\"text/javascript\">\\n\\s+var adUnits = [\\n\\s+{\\n\\s+code: 'block_${placementId}',\\n\\s+placement_id: ${placementId},\\n\\s+sizes: [${bannerWidth}, ${bannerHeight}]\\n\\s+}\\n\\s+];\\n\\s+.+\\.buildUnits(adUnits);\\n</script>";

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="Placement tags - Video">

    //<editor-fold desc="Texts">
    public static final String
            videoWebVastText_1 = "Please select VAST or VPAID based on what you want to receive in the response. Please make sure that you swap out the macros with the values in order to get ads.",
            videoWebVastText_2 = "For more details please refer to VAST tag for Web video post or VPAID tag for Web video post",
            videoInAppVastText = "Please select VAST or VPAID based on what you want to receive in the response. Please make sure that you swap out the macros with the values in order to get ads. For more details please refer to VAST Tag for InApp Video post or VPAID Tag for InApp Video post",
            videoInAppApplovinText = "If you use AppLovin SDK and want to send the requests for ads from there, please use this tag. For more details please refer to specific type of integration.",
            videoWebJwText = "If you use JW player on the web-site, you can match it with our SSP. For more details please refer to Mediation Tags For JW player users",
            videoWebDfpText_1 = "This tag is for web format only. DFP tags do not work for inapp. You can use it as custom tag in Google Ad Manager UI.",
            videoWebDfpText_2 = "For more details please refer to specific type of integration.",
            videoWebDfpText_note = "Note: Please use this tag only if you have been certified with DFP.",
            videoWebLkqdText = "If you use LKQD platform and want to send the requests for ads from there, please use this tag. For more details please refer to specific type of integration.",
            videoWebConnatixText = "For Connatix Video Player Users, feel free to adjust macros if necessary or refer to Publisher Integration Guide.",
            videoInAppConnatixText = "For Connatix Video player Users, feel free to adjust macros if necessary or refer to Publisher Integration Guide.",
            videoInAppAerServText = "If you use Aerserve platform and want to send the requests for ads from there, please use this tag. For more details please refer to specific type of integration.",
            videoWebApiText = "The tag works for Ad server integrations. For additional information please refer to Publisher Integration Guide.",
            videoWebColumn6Text = "For Column 6 Users, feel free to adjust macros if necessary or refer to Publisher Integration Guide.",
            videoWebSpringServeText = "For SpringServe Users, feel free to adjust macros if necessary or refer to Publisher Integration Guide.",
            videoWebSpotxText = "For SpotX Users, feel free to adjust macros if necessary or refer to Publisher Integration Guide.",
            videoWebAniviewText = "For Aniview Users, feel free to adjust macros if necessary or refer to Publisher Integration Guide.",
            videoInAppIronsrcText = "If you use IronSource platform and want to send the requests for ads from there, please use this tag. For more details please refer to specific type of integration.",
            videoCtvRokuText = "The tag works for Roku platform.";
    //</editor-fold>

    //<editor-fold desc="Code">
    public static final String
            videoWebVastCode = "https://.+/?c=v&m=tag&placementId=${placementId}&videotype=[VIDEO_TYPE]&wPlayer=[VIDEO_W]&hPlayer=[VIDEO_H]&ip=[IP]&ua=[UA]&domain=[DOMAIN]&page=[PAGE]&secure=[SECURE]&language=[BROWSER_LANGUAGE]&bidfloor=[BIDFLOOR]&gdpr_consent=[GDPR_CONSENT]&gpp=[GPP]&gpp_sid=[GPP_SID]",
            videoInAppVastCode = "https://.+/?c=v&m=tag&app=1&placementId=${placementId}&videotype=[VIDEO_TYPE]&ip=[IP]&ua=[UA]&name=[APP_NAME]&bundle=[APP_BUNDLE]&ver=[APP_VERSION]&os=[DEVICE_OS]&osv=[DEVICE_OS_VERSION]&make=[DEVICE_MAKE]&model=[DEVICE_MODEL]&w=[DEVICE_W]&h=[DEVICE_H]&wPlayer=[PLAYER_W]&hPlayer=[PLAYER_H]&hwv=[DEVICE_HW_VERSION]&ifa=[IFA]&js=[JS]&carrier=[CARRIER]&paid=[PAID]&country=[GEO_COUNTRY]&geo_type=[GEO_TYPE]&city=[GEO_CITY]&lat=[GEO_LAT]&lon=[GEO_LON]&bidfloor=[BIDFLOOR]&gdpr_consent=[GDPR_CONSENT]&gpp=[GPP]&gpp_sid=[GPP_SID]",
            videoCtvVastCode = "https://.+/?c=v&m=tag&app=1&placementId=${placementId}&videotype=[VIDEO_TYPE]&ip=[IP]&ua=[UA]&name=[APP_NAME]&bundle=[APP_BUNDLE]&ver=[APP_VERSION]&os=[DEVICE_OS]&osv=[DEVICE_OS_VERSION]&make=[DEVICE_MAKE]&model=[DEVICE_MODEL]&w=[DEVICE_W]&h=[DEVICE_H]&wPlayer=[PLAYER_W]&hPlayer=[PLAYER_H]&hwv=[DEVICE_HW_VERSION]&ifa=[IFA]&js=[JS]&carrier=[CARRIER]&paid=[PAID]&country=[GEO_COUNTRY]&geo_type=[GEO_TYPE]&city=[GEO_CITY]&lat=[GEO_LAT]&lon=[GEO_LON]&bidfloor=[BIDFLOOR]&gdpr_consent=[GDPR_CONSENT]&storeurl=[STOREURL]&gpp=[GPP]&gpp_sid=[GPP_SID]",
            videoInAppApplovinCode = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<VAST version=\"2.0\">\\n\\s+<Ad id=\"22\">\\n\\s+<Wrapper>\\n\\s+<AdSystem><![CDATA[${projectName}]]></AdSystem>\\n\\s+<VASTAdTagURI><![CDATA[https://.+/?c=v&m=api&res=xml&app=1&dnt=%%DNT%%&ip=%%IPADDRESS%%&ua=%%USERAGENT%%&lat=%%LATITUDE%%&lon=%%LONGITUDE%%&ifa=%%ADVERTISING_ID_IFA%%&bundle=%%BUNDLE%%&cachebuster=%%CACHEBUSTER%%&placementId=${placementId}]]></VASTAdTagURI>\\n\\s+</Wrapper>\\n\\s+</Ad>\\n</VAST>",
            videoWebJwCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&domain=__domain__&page=__page-url__&cachebuster=__random-number__&wPlayer=__player-width__&hPlayer=__player-height__",
            videoWebDfpCode = "https://.+/?c=v&m=dfp&placementId=${placementId}&vast=2.0",
            videoWebLkqdCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&cachebuster=$$cachebuster$$&bidfloor=$$cpm$$&dnt=$$dnt$$&domain=$$domain$$&hPlayer=$$height$$&wPlayer=$$width$$&ip=$$ip$$&lat=$$loclat$$&lon=$$loclong$$&os=$$osplatform$$&osv=$$osversion$$&page=$$pageurl$$&ua=$$useragent$$",
            videoInAppLkqdCode = "https://.+/?c=v&m=api&res=xml&app=1&placementId=${placementId}&bundle=$$bundleid$$&name=$$appname$$&cachebuster=$$cachebuster$$&bidfloor=$$cpm$$&make=$$devicemake$$&model=$$devicemodel$$&dnt=$$dnt$$&ifa=$$idfa$$&hPlayer=$$height$$&wPlayer=$$width$$&ip=$$ip$$&lat=$$loclat$$&lon=$$loclong$$&os=$$osplatform$$&osv=$$osversion$$&ua=$$useragent$$",
            videoCtvLkqdCode = "https://.+/?c=v&m=api&res=xml&app=1&placementId=${placementId}&bundle=$$bundleid$$&name=$$appname$$&cachebuster=$$cachebuster$$&bidfloor=$$cpm$$&make=$$devicemake$$&model=$$devicemodel$$&dnt=$$dnt$$&ifa=$$idfa$$&hPlayer=$$height$$&wPlayer=$$width$$&ip=$$ip$$&lat=$$loclat$$&lon=$$loclong$$&os=$$osplatform$$&osv=$$osversion$$&ua=$$useragent$$&storeurl=$$appstoreurl$$",
            videoInAppAerServCode = "https://.+/?c=v&m=api&res=xml&app=1&placementId=${placementId}&ip=$[ip]&name=$[eappname]&bundle=$[bundleid]&ifa=$[adid]&dnt=$[dnt]&make=$[enc_make]&model=$[enc_model]&os=$[os]&osv=$[enc_osv]&ua=$[eua]&lat=$[lat]&lon=$[long]&w=$[dw]&h=$[dh]",
            videoWebConnatixCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&domain=[DOMAIN]&page=[PAGE_URL]&ip=[IP]&ua=[UA]&wPlayer=[WIDTH]&hPlayer=[HEIGHT]",
            videoWebApiCode = "https://.+/?c=v&m=tag&placementId=${placementId}&videotype=[VIDEO_TYPE]&wPlayer=[VIDEO_W]&hPlayer=[VIDEO_H]&ip=[IP]&ua=[UA]&domain=[DOMAIN]&page=[PAGE]&secure=[SECURE]&language=[BROWSER_LANGUAGE]&bidfloor=[BIDFLOOR]&gpp=[GPP]&gpp_sid=[GPP_SID]",
            videoWebColumn6Code = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&domain=$$domain$$&page=$$pageurl$$&cachebuster=$$cachebuster$$&bidfloor=$$cpm$$&dnt=$$dnt$$&hPlayer=$$height$$&wPlayer=$$width$$&ip=$$ip$$&ua=$$useragent$$",
            videoInAppColumn6Code = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&ip=[geo_ip]&ua=[ua]&wPlayer=[player_width]&hPlayer=[player_height]&app=1&name=[app_name]&bundle=[app_id]&os=[osplatform]&osv=[osv]&make=[devicemake]&model=[devicemodel]&ifa=[idfa]&country=[geo_co]&lat=[geo_lat]&lon=[geo_long]&schain=[supply_chain]",
            videoWebSpringServeCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&domain={{DOMAIN}}&page={{ENCODED_URL}}&ip={{IP}}&ua={{USER_AGENT}}&wPlayer={{WIDTH}}&hPlayer={{HEIGHT}}&us_privacy={{US_PRIVACY}}&schain={{SCHAIN}}",
            videoInAppSpringServeCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&ip={{IP}}&ua={{USER_AGENT}}&w={{DETECTED_WIDTH}}&h={{DETECTED_HEIGHT}}&app=1&name={{APP_NAME}}&bundle={{APP_BUNDLE}}&make={{DEVICE_MAKE}}&model={{DEVICE_MODEL}}&ifa={{UUID}}&lat={{LAT}}&lon={{LON}}&bidfloor={{BID_PRICE}}&us_privacy={{US_PRIVACY}}&schain={{SCHAIN}}",
            videoWebSpotxCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&domain=$HOST&ip=$IP_ADX&ua=$USER_AGENT_ENCODED&wPlayer=$PLAYER_WIDTH&hPlayer=$PLAYER_HEIGHT&us_privacy=${US_PRIVACY}&schain=$SCHAIN",
            videoInAppSpotxCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&cachebuster=$CACHE_BUSTER&dnt=$DEVICE_DNT&ip=$IP_ADX&ua=$USER_AGENT_ENCODED&app=1&name=$APP_NAME&bundle=$APP_ID&ifa=$DEVICE_IFA&make=$DEVICE_MAKE&model=$DEVICE_MODEL&w=$PLAYER_WIDTH&h=$PLAYER_HEIGHT&osv=$DEVICE_OSV&os=$DEVICE_OS&lat=$GEO_LAT&lon=$GEO_LON&secure=$SECURE",
            videoWebAniviewCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&domain=[AV_DOMAIN]&page=[AV_URL]&ip=[AV_IP]&ua=[AV_USERAGENT]&wPlayer=[AV_WIDTH]&hPlayer=[AV_HEIGHT]&schain=[AV_SCHAIN]",
            videoInAppIronsrcCode = "https://.+/?c=v&m=api&res=xml&app=1&placementId=${placementId}&ip=[IP]&ua=[UA]&ifa=[IFA]&os=[OS]&model=[DEVICE_MODEL]&wPlayer=[WIDTH]&hPlayer=[HEIGHT]&bundle=[APP_BUNDLE_ID]&name=[APP_NAME]&osv=[OS_VERSION]&carrier=[CARRIER]",
            videoCtvRokuCode = "https://.+/?c=v&m=api&res=xml&placementId=${placementId}&cachebuster=ROKU_ADS_CACHE_BUSTER&dnt=ROKU_ADS_LIMIT_TRACKING&ip=ROKU_ADS_EXTERNAL_IP&ua=ROKU_ADS_USER_AGENT&app=1&&bundle=ROKU_ADS_APP_ID&ifa=ROKU_ADS_TRACKING_ID&model=ROKU_ADS_DEVICE_MODEL&w=ROKU_ADS_DISPLAY_WIDTH&h=ROKU_ADS_DISPLAY_HEIGHT";
    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="Placement tags - Native">
    public static final String
            nativeWebTagText = "If you want to monetize native traffic, please use this tag and refer to Native Tag Integration for Web post",
            nativeInAppTagText = "If you want to monetize native traffic, please use this tag and refer to Native Tag Integration for InApp",
            nativeWebTagCode = "https://.+/?c=n&m=tag&placementId=${placementId}&ip=[IP]&ua=[UA]&domain=[DOMAIN]&page=[PAGE]&secure=[SECURE]&language=[BROWSER_LANGUAGE]&bidfloor=[BIDFLOOR]&gdpr_consent=[GDPR_CONSENT]&gpp=[GPP]&gpp_sid=[GPP_SID]",
            nativeInAppTagCode = "https://.+/?c=n&m=tag&app=1&placementId=${placementId}&ip=[IP]&ua=[UA]&name=[APP_NAME]&bundle=[APP_BUNDLE]&ver=[APP_VERSION]&os=[DEVICE_OS]&osv=[DEVICE_OS_VERSION]&make=[DEVICE_MAKE]&model=[DEVICE_MODEL]&w=[DEVICE_W]&h=[DEVICE_H]&hwv=[DEVICE_HW_VERSION]&ifa=[IFA]&js=[JS]&carrier=[CARRIER]&paid=[PAID]&country=[GEO_COUNTRY]&geo_type=[GEO_TYPE]&city=[GEO_CITY]&lat=[GEO_LAT]&lon=[GEO_LON]&bidfloor=[BIDFLOOR]&gdpr_consent=[GDPR_CONSENT]&gpp=[GPP]&gpp_sid=[GPP_SID]";
    //</editor-fold>

}
