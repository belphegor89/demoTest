package data.dataobject;

import data.AdaptersEnums.adaptersApiEnum;
import data.AdaptersEnums.adaptersPrebidEnum;
import data.CommonEnums.*;
import data.DemandEnum.*;

import java.util.*;

public class EndpointDemandDO extends EndpointCommonDO {
    private String endpointName;
    private endpointTypeEnum endpointType;

    public EndpointDemandDO(){}

    public EndpointDemandDO(String name, endpointTypeEnum type){
        this.endpointName = name;
        setEndpointType(type);
    }

    //<editor-fold desc="General settings">
    private String endpointUrl, company;
    private regionsEnum region;
    private Boolean endpointStatus = false, adapterAllSelected = false;
    private auctionTypeEnum auctionType = auctionTypeEnum.SECOND;
    private Integer endpointId, qpsLimit = 0, tmax = 0, spendLimit = 0, jsWidth = 0, jsHeight = 0;
    private rtbVersionEnum rtbVersion = rtbVersionEnum.v25;
    private Double bidFloorCpm = 0d;
    private QpsSetting qpsSettings = new QpsSetting().setFixed(0);
    private MarginSettings marginSettings = new MarginSettings().setFixed(0d);
    private adapterTypeEnum adapterType = adapterTypeEnum.NONE;
    private adaptersApiEnum apiAdapter;
    private adaptersPrebidEnum prebidAdapter;
    private String adapterProviderName;
    private List<String> adapterSettingNames = new ArrayList<>();
    private iabSizesEnum jsTagSize = iabSizesEnum.CUSTOM;

    public EndpointDemandDO setStatus(boolean status){
        this.endpointStatus = status;
        return this;
    }

    public boolean getStatus(){
        return this.endpointStatus;
    }

    public EndpointDemandDO setId(Integer id){
        this.endpointId = id;
        return this;
    }

    public Integer getId(){
        return this.endpointId;
    }

    public EndpointDemandDO setEndpointType(endpointTypeEnum type){
        this.endpointType = type;
        switch (type){
            case RTB, PREBID -> {
                //use default settings
            }
            case VAST -> {
                adFormatStatusMap.put(adFormatPlacementTypeEnum.BANNER, false);
                adFormatStatusMap.put(adFormatPlacementTypeEnum.NATIVE, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.BANNER, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.NATIVE, false);
                advancedAvailabilityMap.put(advancedSettingsEnum.NURL_DSP, false);
                advancedAvailabilityMap.put(advancedSettingsEnum.COMPRESSED, false);
            }
            case JS -> {
                adFormatStatusMap.put(adFormatPlacementTypeEnum.BANNER, null);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.VIDEO, false);
                //                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.AUDIO, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.NATIVE, false);
                trafficAvailabilityMap.put(trafficTypeEnum.CTV, false);
            }
        }
        return this;
    }

    public endpointTypeEnum getEndpointType(){
        return this.endpointType;
    }

    public EndpointDemandDO setEndpointName(String name){
        this.endpointName = name;
        return this;
    }

    public String getEndpointName(){
        return this.endpointName;
    }

    public EndpointDemandDO setEndpointUrl(String url){
        this.endpointUrl = url;
        return this;
    }

    public String getEndpointUrl(){
        return this.endpointUrl;
    }

    public EndpointDemandDO setCompany(String company){
        this.company = company;
        return this;
    }

    public String getCompany(){
        return this.company;
    }

    public EndpointDemandDO setRegion(regionsEnum region){
        this.region = region;
        return this;
    }

    public regionsEnum getRegion(){
        return this.region;
    }

    public EndpointDemandDO setAuctionType(auctionTypeEnum type){
        this.auctionType = type;
        return this;
    }

    public auctionTypeEnum getAuctionType(){
        return this.auctionType;
    }

    public EndpointDemandDO setTmax(int tmax){
        this.tmax = tmax;
        return this;
    }

    public Integer getTmax(){
        return this.tmax;
    }

    public EndpointDemandDO setSpendLimit(int limit){
        this.spendLimit = limit;
        return this;
    }

    public Integer getSpendLimit(){
        return this.spendLimit;
    }

    public EndpointDemandDO setRtbVersion(rtbVersionEnum version){
        this.rtbVersion = version;
        return this;
    }

    public rtbVersionEnum getRtbVersion(){
        return this.rtbVersion;
    }

    public EndpointDemandDO setBidFloorCpm(double floor){
        this.bidFloorCpm = floor;
        return this;
    }

    public Double getBidFloorCpm(){
        return this.bidFloorCpm;
    }

    public EndpointDemandDO setQpsSettings(QpsSetting settings){
        this.qpsSettings = settings;
        return this;
    }

    public QpsSetting getQpsSettings(){
        return this.qpsSettings;
    }

    public EndpointDemandDO setMarginSettings(MarginSettings settings){
        this.marginSettings = settings;
        return this;
    }

    public MarginSettings getMarginSettings(){
        return this.marginSettings;
    }

    public EndpointDemandDO setAdapterType(adapterTypeEnum type){
        this.adapterType = type;
        return this;
    }

    public adapterTypeEnum getAdapterType(){
        return this.adapterType;
    }

    public EndpointDemandDO setApiAdapter(adaptersApiEnum adapter){
        this.apiAdapter = adapter;
        this.adapterType = adapterTypeEnum.API;
        return this;
    }

    public adaptersApiEnum getApiAdapter(){
        return this.apiAdapter;
    }

    public EndpointDemandDO setPrebidAdapter(adaptersPrebidEnum adapter){
        this.prebidAdapter = adapter;
        return this;
    }

    public adaptersPrebidEnum getPrebidAdapter(){
        return this.prebidAdapter;
    }

    public EndpointDemandDO setSelectAllAdapterSettings(boolean select){
        this.adapterAllSelected = select;
        return this;
    }

    public String getAdapterProviderName(){
        return this.adapterProviderName;
    }

    public Boolean getAdapterSelectAll(){
        return this.adapterAllSelected;
    }

    public EndpointDemandDO setAdapterSettingNames(List<String> names){
        this.adapterSettingNames = names;
        return this;
    }

    public EndpointDemandDO addAdapterSettingName(String name){
        this.adapterSettingNames.add(name);
        return this;
    }

    public List<String> getAdapterSettingNames(){
        return this.adapterSettingNames;
    }

    //<editor-fold desc="JS settings">
    public EndpointDemandDO setJsTagSize(iabSizesEnum size){
        this.jsTagSize = size;
        this.jsWidth = size.getWidth();
        this.jsHeight = size.getHeight();
        return this;
    }

    public iabSizesEnum getJsTagSize(){
        return this.jsTagSize;
    }

    public EndpointDemandDO setJsTagCustomSize(int width, int height){
        this.jsWidth = width;
        this.jsHeight = height;
        return this;
    }

    public Integer getJsWidth(){
        return this.jsWidth;
    }

    public Integer getJsHeight(){
        return this.jsHeight;
    }
    //</editor-fold>

    //<editor-fold desc="VAST settings">
    private String jsVastUrlWithMacros;
    private List<macroTagsTypesEnum> jsVastTagsList = new ArrayList<>();

    public EndpointDemandDO setJsVastTagsList(List<macroTagsTypesEnum> list){
        this.jsVastTagsList.addAll(list);
        addMacrosToJsVastUrl(this.jsVastTagsList);
        return this;
    }

    public EndpointDemandDO addJsVastTag(macroTagsTypesEnum tag){
        this.jsVastTagsList.add(tag);
        addMacrosToJsVastUrl(this.jsVastTagsList);
        return this;
    }

    public List<macroTagsTypesEnum> getJsVastTagsList(){
        return this.jsVastTagsList;
    }

    public String addMacrosToJsVastUrl(List<macroTagsTypesEnum> macrosList){
        for (macroTagsTypesEnum macro : macrosList){
            jsVastUrlWithMacros += "&" + macro.attributeName() + "=" + macro.tagName();
        }
        return jsVastUrlWithMacros;
    }

    public EndpointDemandDO setJsVastUrl(String url){
        this.jsVastUrlWithMacros = url;
        return this;
    }

    public EndpointDemandDO setJsVastUrl(){
        this.jsVastUrlWithMacros = this.endpointUrl;
        return this;
    }

    public String getVastUrl(){
        return this.jsVastUrlWithMacros;
    }

    //</editor-fold>

    //<editor-fold desc="Toggle settings">
    private Map<adFormatPlacementTypeEnum, Boolean> adFormatStatusMap = new HashMap<>(){{
        put(adFormatPlacementTypeEnum.BANNER, false);
        put(adFormatPlacementTypeEnum.VIDEO, false);
        //        put(adFormatPlacementTypeEnum.AUDIO, false);
        put(adFormatPlacementTypeEnum.NATIVE, false);
    }};
    private Map<adFormatPlacementTypeEnum, Boolean> adFormatAvailabilityMap = new HashMap<>(){{
        put(adFormatPlacementTypeEnum.BANNER, true);
        put(adFormatPlacementTypeEnum.VIDEO, true);
        //        put(adFormatPlacementTypeEnum.AUDIO, true);
        put(adFormatPlacementTypeEnum.NATIVE, true);
    }};
    private Map<trafficTypeEnum, Boolean> trafficStatusMap = new HashMap<>(){{
        put(trafficTypeEnum.WEB, false);
        put(trafficTypeEnum.APP, false);
        put(trafficTypeEnum.MOBILE_WEB, false);
        put(trafficTypeEnum.CTV, false);
    }};
    private Map<trafficTypeEnum, Boolean> trafficAvailabilityMap = new HashMap<>(){{
        put(trafficTypeEnum.WEB, true);
        put(trafficTypeEnum.APP, true);
        put(trafficTypeEnum.MOBILE_WEB, true);
        put(trafficTypeEnum.CTV, true);
    }};
    private Map<advancedSettingsEnum, Boolean> advancedStatusMap = new HashMap<>(){{
        put(advancedSettingsEnum.NURL_DSP, false);
        put(advancedSettingsEnum.PREMIUM, false);
        put(advancedSettingsEnum.SYNCED, false);
        put(advancedSettingsEnum.RCPM, false);
        put(advancedSettingsEnum.COMPRESSED, false);
        put(advancedSettingsEnum.REWARDED, false);
        put(advancedSettingsEnum.IPV6, false);
        put(advancedSettingsEnum.LAT, false);
        put(advancedSettingsEnum.IFA, false);
        put(advancedSettingsEnum.SCHAIN, false);
        put(advancedSettingsEnum.GDPR, false);
        put(advancedSettingsEnum.PMP, false);
    }};
    private Map<advancedSettingsEnum, Boolean> advancedAvailabilityMap = new HashMap<>(){{
        put(advancedSettingsEnum.NURL_DSP, true);
        put(advancedSettingsEnum.PREMIUM, true);
        put(advancedSettingsEnum.SYNCED, true);
        put(advancedSettingsEnum.RCPM, true);
        put(advancedSettingsEnum.COMPRESSED, true);
        put(advancedSettingsEnum.REWARDED, true);
        put(advancedSettingsEnum.IPV6, true);
        put(advancedSettingsEnum.LAT, true);
        put(advancedSettingsEnum.IFA, true);
        put(advancedSettingsEnum.SCHAIN, true);
        put(advancedSettingsEnum.GDPR, true);
        put(advancedSettingsEnum.PMP, true);
    }};

    public EndpointDemandDO setAdFormatMap(Map<adFormatPlacementTypeEnum, Boolean> map){
        this.adFormatStatusMap.putAll(map);
        return this;
    }

    public EndpointDemandDO addAdFormatSetting(adFormatPlacementTypeEnum format, boolean status){
        this.adFormatStatusMap.put(format, status);
        return this;
    }

    public Map<adFormatPlacementTypeEnum, Boolean> getAdFormatStatusMap(){
        return this.adFormatStatusMap;
    }

    public Map<adFormatPlacementTypeEnum, Boolean> getAdFormatAvailabilityMap(){
        return this.adFormatAvailabilityMap;
    }

    public EndpointDemandDO setTrafficMap(Map<trafficTypeEnum, Boolean> map){
        this.trafficStatusMap.putAll(map);
        return this;
    }

    public EndpointDemandDO addTrafficSetting(trafficTypeEnum traffic, boolean status){
        this.trafficStatusMap.put(traffic, status);
        return this;
    }

    public Map<trafficTypeEnum, Boolean> getTrafficStatusMap(){
        return this.trafficStatusMap;
    }

    public Map<trafficTypeEnum, Boolean> getTrafficAvailabilityMap(){
        return this.trafficAvailabilityMap;
    }

    public EndpointDemandDO setAdvancedMap(Map<advancedSettingsEnum, Boolean> map){
        this.advancedStatusMap.putAll(map);
        return this;
    }

    public EndpointDemandDO addAdvancedSetting(advancedSettingsEnum setting, boolean status){
        this.advancedStatusMap.put(setting, status);
        return this;
    }

    public Map<advancedSettingsEnum, Boolean> getAdvancedStatusMap(){
        return this.advancedStatusMap;
    }

    public Map<advancedSettingsEnum, Boolean> getAdvancedAvailabilityMap(){
        return this.advancedAvailabilityMap;
    }

    //</editor-fold>

    //</editor-fold>

    //<editor-fold desc="Geo settings">
    private final List<GeoSetting> geoQpsList = new LinkedList<>();
    private final List<GeoSetting> geoBidfloorCpmList = new LinkedList<>();
    private GeoSetting geoBlacklist = new GeoSetting();

    public EndpointDemandDO addGeoQpsWhitelist(GeoSetting setting){
        this.geoQpsList.add(setting);
        return this;
    }

    public EndpointDemandDO setGeoQpsWhitelist(List<GeoSetting> geoQpsList){
        this.geoQpsList.addAll(geoQpsList);
        return this;
    }

    public List<GeoSetting> getGeoQpsWhitelist(){
        return this.geoQpsList;
    }

    public EndpointDemandDO addGeoBidfloorCpm(GeoSetting setting){
        this.geoBidfloorCpmList.add(setting);
        return this;
    }

    public EndpointDemandDO setGeoBidfloorCpm(List<GeoSetting> geoBidfloorCpmList){
        this.geoBidfloorCpmList.addAll(geoBidfloorCpmList);
        return this;
    }

    public List<GeoSetting> getGeoBidfloorCpmList(){
        return this.geoBidfloorCpmList;
    }

    public EndpointDemandDO setGeoBlacklist(GeoSetting setting){
        this.geoBlacklist = setting;
        return this;
    }

    public EndpointDemandDO setGeoBlacklist(Map<String, Boolean> blacklistMap){
        this.geoBlacklist.setGeoBlacklist(blacklistMap);
        return this;
    }

    public EndpointDemandDO addGeoBlacklist(String country, boolean status){
        this.geoBlacklist.addGeoBlacklist(country, status);
        return this;
    }

    public GeoSetting getGeoBlacklist(){
        return this.geoBlacklist;
    }

    //</editor-fold>

    //<editor-fold desc="Additional settings">
    private List<BannerSize> bannerSizesList = new ArrayList<>();
    private List<VideoSize> videoSizesList = new ArrayList<>();
    private Map<String, String> seatMap = new LinkedHashMap<>();
    private Map<String, Boolean> osMap = new LinkedHashMap<>(), sspAllowedMap = new LinkedHashMap<>(), sspBlockedMap = new LinkedHashMap<>(), inventoryAllowedMap = new LinkedHashMap<>(), inventoryBlockedMap = new LinkedHashMap<>();
    private String reportApiLinkPartner, reportApiLinkEndpoint;
    private endpointPartnerApiTypeEnum partnerApiType;
    private Map<endpointPartnerApiColumnsEnum, String> csvColumnsMap = new LinkedHashMap<>();

    public EndpointDemandDO addBannerSize(BannerSize size){
        this.bannerSizesList.add(size);
        return this;
    }

    public EndpointDemandDO setBannerSizeList(List<BannerSize> list){
        this.bannerSizesList.addAll(list);
        return this;
    }

    public List<BannerSize> getBannerSizesList(){
        return this.bannerSizesList;
    }

    public EndpointDemandDO addVideoSize(VideoSize size){
        this.videoSizesList.add(size);
        return this;
    }

    public EndpointDemandDO setVideoSizeList(List<VideoSize> list){
        this.videoSizesList.addAll(list);
        return this;
    }

    public List<VideoSize> getVideoSizesList(){
        return this.videoSizesList;
    }

    public EndpointDemandDO setOsMap(Map<String, Boolean> map){
        this.osMap.putAll(map);
        return this;
    }

    public Map<String, Boolean> getOsMap(){
        return this.osMap;
    }

    public EndpointDemandDO setSeatMap(Map<String, String> map){
        this.seatMap.putAll(map);
        return this;
    }

    public Map<String, String> getSeatMap(){
        return this.seatMap;
    }

    public EndpointDemandDO clearSeatMap(){
        this.seatMap.clear();
        return this;
    }

    public EndpointDemandDO setSspAllowedMap(Map<String, Boolean> map){
        this.sspAllowedMap.putAll(map);
        return this;
    }

    public EndpointDemandDO addSspToAllowed(String ssp, boolean status){
        this.sspAllowedMap.put(ssp, status);
        return this;
    }

    public Map<String, Boolean> getSspAllowedMap(){
        return this.sspAllowedMap;
    }

    public EndpointDemandDO clearSspAllowedMap(){
        this.sspAllowedMap.clear();
        return this;
    }

    public EndpointDemandDO setSspBlockedMap(Map<String, Boolean> map){
        this.sspBlockedMap.putAll(map);
        return this;
    }

    public EndpointDemandDO addSspToBlocked(String ssp, boolean status){
        this.sspBlockedMap.put(ssp, status);
        return this;
    }

    public Map<String, Boolean> getSspBlockedMap(){
        return this.sspBlockedMap;
    }

    public EndpointDemandDO clearSspBlockedMap(){
        this.sspBlockedMap.clear();
        return this;
    }

    public EndpointDemandDO setInventoryAllowedMap(Map<String, Boolean> map){
        this.inventoryAllowedMap.putAll(map);
        return this;
    }

    public Map<String, Boolean> getInventoryAllowedMap(){
        return this.inventoryAllowedMap;
    }

    public EndpointDemandDO clearInventoryAllowedMap(){
        this.inventoryAllowedMap.clear();
        return this;
    }

    public EndpointDemandDO setInventoryBlockedMap(Map<String, Boolean> map){
        this.inventoryBlockedMap.putAll(map);
        return this;
    }

    public Map<String, Boolean> getInventoryBlockedMap(){
        return this.inventoryBlockedMap;
    }

    public EndpointDemandDO clearInventoryBlockedMap(){
        this.inventoryBlockedMap.clear();
        return this;
    }

    public EndpointDemandDO setReportApiLinkPartner(String link, endpointPartnerApiTypeEnum type){
        this.reportApiLinkPartner = link;
        this.partnerApiType = type;
        return this;
    }

    public String getReportApiLinkPartner(){
        return this.reportApiLinkPartner;
    }

    public endpointPartnerApiTypeEnum getPartnerApiType(){
        return this.partnerApiType;
    }

    public EndpointDemandDO setReportApiLinkEndpoint(String link){
        this.reportApiLinkEndpoint = link;
        return this;
    }

    public String getReportApiLinkEndpoint(){
        return this.reportApiLinkEndpoint;
    }

    public EndpointDemandDO setCsvColumnsMap(Map<endpointPartnerApiColumnsEnum, String> map){
        this.csvColumnsMap.putAll(map);
        return this;
    }

    public Map<endpointPartnerApiColumnsEnum, String> getCsvColumnsMap(){
        return this.csvColumnsMap;
    }


    //</editor-fold>

    //<editor-fold desc="Scanners">
    private List<ScannerSetting> scannerSettings = new ArrayList<>();

    public EndpointDemandDO setScannerSettings(List<ScannerSetting> settings){
        this.scannerSettings.addAll(settings);
        return this;
    }

    public EndpointDemandDO addScannerSetting(ScannerSetting setting){
        this.scannerSettings.add(setting);
        return this;
    }

    public List<ScannerSetting> getScannerSettings(){
        return this.scannerSettings;
    }

    //</editor-fold>

    //<editor-fold desc="Table info">
    private Integer qpsReal = 0, qpsBid = 0;
    private Double spendYesterday = 0d, spendToday = 0d, winRate = 0d;

    public EndpointDemandDO setTableStats(Integer qpsReal, Integer qpsBid, Double spendYesterday, Double spendToday, Double winRate){
        this.qpsReal = qpsReal;
        this.qpsBid = qpsBid;
        this.spendYesterday = spendYesterday;
        this.spendToday = spendToday;
        this.winRate = winRate;
        return this;
    }

    public Integer getQpsReal(){
        return this.qpsReal;
    }

    public Integer getQpsBid(){
        return this.qpsBid;
    }

    public Double getSpendYesterday(){
        return this.spendYesterday;
    }

    public Double getSpendToday(){
        return this.spendToday;
    }

    public Double getWinRate(){
        return this.winRate;
    }
    //</editor-fold>

    public static class BannerSize{
        private iabSizesEnum bannerSize;
        private Integer width, height;

        public BannerSize(){}

        public BannerSize setPredefinedSize(iabSizesEnum size){
            this.bannerSize = size;
            this.width = size.getWidth();
            this.height = size.getHeight();
            return this;
        }

        public BannerSize setCustomSize(int width, int height){
            this.bannerSize = iabSizesEnum.CUSTOM;
            this.width = width;
            this.height = height;
            return this;
        }

        public BannerSize setSize(iabSizesEnum size, int width, int height){
            this.bannerSize = size;
            this.width = width;
            this.height = height;
            return this;
        }

        public Integer getWidth(){
            return this.width;
        }

        public Integer getHeight(){
            return this.height;
        }

        public iabSizesEnum getSize(){
            return this.bannerSize;
        }

    }

    public static class VideoSize{
        private videoSizeEnum videoSize;
        private Integer width, height;

        public VideoSize(){}

        public VideoSize setPredefinedSize(videoSizeEnum size){
            this.videoSize = size;
            this.width = size.getWidth();
            this.height = size.getHeight();
            return this;
        }

        public VideoSize setCustomSize(int width, int height){
            this.videoSize = videoSizeEnum.CUSTOM;
            this.width = width;
            this.height = height;
            return this;
        }

        public VideoSize setSize(videoSizeEnum size, int width, int height){
            this.videoSize = size;
            this.width = width;
            this.height = height;
            return this;
        }

        public Integer getWidth(){
            return this.width;
        }

        public Integer getHeight(){
            return this.height;
        }

        public videoSizeEnum getSize(){
            return this.videoSize;
        }

    }

    public static class QpsSetting {
        private qpsLimitType type = qpsLimitType.FIXED;
        private Integer min = 0, max = 0;
        private boolean directSupply = true, directPublishers = true;
        private qpsFrequencyEnum frequency = qpsFrequencyEnum.MIN15;
        private qpsOptimizationEnum optimization = qpsOptimizationEnum.RCPM;

        public QpsSetting setFixed(Integer fixed){
            this.type = qpsLimitType.FIXED;
            this.max = fixed;
            return this;
        }

        public QpsSetting setDynamic(Integer min, Integer max, qpsFrequencyEnum frequency, qpsOptimizationEnum optimization){
            this.type = qpsLimitType.DYNAMIC;
            this.min = min;
            this.max = max;
            this.frequency = frequency;
            this.optimization = optimization;
            return this;
        }

        public QpsSetting setDirect(Integer min, Integer max, boolean supply, boolean publishers){
            this.type = qpsLimitType.DIRECT;
            this.min = min;
            this.max = max;
            this.directSupply = supply;
            this.directPublishers = publishers;
            return this;
        }

        public qpsLimitType getType(){
            return this.type;
        }

        public Integer getMin(){
            return this.min;
        }

        public Integer getMax(){
            return this.max;
        }

        public boolean getDirectSupply(){
            return this.directSupply;
        }

        public boolean getDirectPublishers(){
            return this.directPublishers;
        }

        public qpsFrequencyEnum getFrequency(){
            return this.frequency;
        }

        public qpsOptimizationEnum getOptimization(){
            return this.optimization;
        }

    }

}
