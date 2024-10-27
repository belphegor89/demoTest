package data.dataobject;

import data.CommonEnums.*;
import data.SupplyEnum.jsTagTypeEnum;
import data.SupplyEnum.macroTagsTypes;
import data.SupplyEnum.vastTypeEnum;

import java.util.*;

import static data.SupplyEnum.getMacroTagPlaceholder;

public class EndpointSupplyDO extends EndpointCommonDO {
    private String endpointName;
    private endpointTypeEnum endpointType;

    public EndpointSupplyDO() {
    }

    public EndpointSupplyDO(String name, endpointTypeEnum type) {
        this.endpointName = name;
        setEndpointType(type);
    }

    //<editor-fold desc="Information">
    private String endpointUrl, company;
    private Integer endpointId, jsWidth = 0, jsHeight = 0;
    private Boolean endpointStatus = false, testMode = false;
    private iabSizesEnum jsTagSize = iabSizesEnum.CUSTOM;

    public EndpointSupplyDO setStatus(boolean status) {
        this.endpointStatus = status;
        return this;
    }

    public boolean getStatus() {
        return this.endpointStatus;
    }

    public EndpointSupplyDO setTestMode(boolean mode) {
        this.testMode = mode;
        return this;
    }

    public boolean getTestMode() {
        return this.testMode;
    }

    public EndpointSupplyDO setId(Integer id) {
        this.endpointId = id;
        return this;
    }

    public Integer getId() {
        return this.endpointId;
    }

    public EndpointSupplyDO setEndpointType(endpointTypeEnum type) {
        this.endpointType = type;
        switch (type) {
            case RTB -> {
                //use default settings
            }
            case VAST -> {
                setVastTagType(vastTypeEnum.CUSTOM);
                adFormatStatusMap.put(adFormatPlacementTypeEnum.BANNER, false);
                adFormatStatusMap.put(adFormatPlacementTypeEnum.NATIVE, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.BANNER, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.NATIVE, false);
                adapter = null;
            }
            case JS -> {
                setJsTagType(jsTagTypeEnum.S2S_APP);
                adFormatStatusMap.put(adFormatPlacementTypeEnum.VIDEO, false);
                adFormatStatusMap.put(adFormatPlacementTypeEnum.AUDIO, false);
                adFormatStatusMap.put(adFormatPlacementTypeEnum.NATIVE, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.VIDEO, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.AUDIO, false);
                adFormatAvailabilityMap.put(adFormatPlacementTypeEnum.NATIVE, false);
                trafficStatusMap.put(trafficTypeEnum.CTV, false);
                trafficAvailabilityMap.put(trafficTypeEnum.CTV, false);
                adapter = null;
            }
        }
        return this;
    }

    public endpointTypeEnum getEndpointType() {
        return this.endpointType;
    }

    public EndpointSupplyDO setEndpointName(String name) {
        this.endpointName = name;
        return this;
    }

    public String getEndpointName() {
        return this.endpointName;
    }

    public EndpointSupplyDO setEndpointUrl(String url) {
        this.endpointUrl = url;
        return this;
    }

    public String getEndpointUrl() {
        return this.endpointUrl;
    }

    public EndpointSupplyDO setCompany(String company) {
        this.company = company;
        return this;
    }

    public String getCompany() {
        return this.company;
    }

    //</editor-fold>

    //<editor-fold desc="VAST / JS settings">
    private String jsVastUrlWithMacros, jsBackfill;
    private vastTypeEnum vastTagType;
    private jsTagTypeEnum jsTagType;
    private List<macroTagsTypes> jsVastTagsList = new ArrayList<>();

    public EndpointSupplyDO setVastTagType(vastTypeEnum type) {
        //Order of predefined macros is important
        List<macroTagsTypes> predefinedCustomWeb = new ArrayList<>() {{
            add(macroTagsTypes.IP);
            add(macroTagsTypes.W);
            add(macroTagsTypes.H);
            add(macroTagsTypes.DOMAIN);
            add(macroTagsTypes.PAGE);
            add(macroTagsTypes.PLAYBACKMETHOD);
        }},
                predefinedCustomInApp = new ArrayList<>() {{
                    add(macroTagsTypes.IP);
                    add(macroTagsTypes.W);
                    add(macroTagsTypes.H);
                    add(macroTagsTypes.IFA);
                    add(macroTagsTypes.BUNDLE);
                    add(macroTagsTypes.PLAYBACKMETHOD);
                }},
                predefinedCustomCtv = new ArrayList<>() {{
                    add(macroTagsTypes.IP);
                    add(macroTagsTypes.W);
                    add(macroTagsTypes.H);
                    add(macroTagsTypes.IFA);
                    add(macroTagsTypes.BUNDLE);
                    add(macroTagsTypes.STOREURL);
                    add(macroTagsTypes.PLAYBACKMETHOD);
                }};
        this.vastTagType = type;
        switch (type) {
            case CUSTOM -> setJsVastTagsList(predefinedCustomWeb);
            case CUSTOM_INAPP -> setJsVastTagsList(predefinedCustomInApp);
            case CUSTOM_CTV -> setJsVastTagsList(predefinedCustomCtv);
        }
        return this;
    }

    public vastTypeEnum getVastTagType() {
        return this.vastTagType;
    }

    public EndpointSupplyDO setJsTagType(jsTagTypeEnum type) {
        //Order of predefined macros is important
        List<macroTagsTypes> predefinedJsS2SWeb = new ArrayList<>() {{
            add(macroTagsTypes.IP);
            add(macroTagsTypes.UA);
            add(macroTagsTypes.DOMAIN);
            add(macroTagsTypes.PAGE);
            add(macroTagsTypes.NAME);
            add(macroTagsTypes.W);
            add(macroTagsTypes.H);
            add(macroTagsTypes.DW);
            add(macroTagsTypes.DH);
            add(macroTagsTypes.LAT);
            add(macroTagsTypes.LON);
            add(macroTagsTypes.BIDFLOOR);
            add(macroTagsTypes.GDPR_CONSENT);
        }},
                predefinedJsS2CWeb = new ArrayList<>() {{
                    add(macroTagsTypes.IP);
                    add(macroTagsTypes.UA);
                    add(macroTagsTypes.DOMAIN);
                    add(macroTagsTypes.PAGE);
                    add(macroTagsTypes.W);
                    add(macroTagsTypes.H);
                    add(macroTagsTypes.DW);
                    add(macroTagsTypes.DH);
                    add(macroTagsTypes.LAT);
                    add(macroTagsTypes.LON);
                    add(macroTagsTypes.BIDFLOOR);
                    add(macroTagsTypes.GDPR_CONSENT);
                }},
                predefinedJsS2SInApp = new ArrayList<>() {{
                    add(macroTagsTypes.IP);
                    add(macroTagsTypes.UA);
                    add(macroTagsTypes.BUNDLE);
                    add(macroTagsTypes.STOREURL);
                    add(macroTagsTypes.NAME);
                    add(macroTagsTypes.W);
                    add(macroTagsTypes.H);
                    add(macroTagsTypes.DW);
                    add(macroTagsTypes.DH);
                    add(macroTagsTypes.LAT);
                    add(macroTagsTypes.LON);
                    add(macroTagsTypes.IFA);
                    add(macroTagsTypes.BIDFLOOR);
                    add(macroTagsTypes.GDPR_CONSENT);
                }},
                predefinedJsS2CInApp = new ArrayList<>() {{
                    add(macroTagsTypes.IP);
                    add(macroTagsTypes.UA);
                    add(macroTagsTypes.BUNDLE);
                    add(macroTagsTypes.STOREURL);
                    add(macroTagsTypes.NAME);
                    add(macroTagsTypes.W);
                    add(macroTagsTypes.H);
                    add(macroTagsTypes.DW);
                    add(macroTagsTypes.DH);
                    add(macroTagsTypes.LAT);
                    add(macroTagsTypes.LON);
                    add(macroTagsTypes.IFA);
                    add(macroTagsTypes.BIDFLOOR);
                    add(macroTagsTypes.GDPR_CONSENT);
                }};
        this.jsTagType = type;
        switch (type) {
            case S2S_WEB -> setJsVastTagsList(predefinedJsS2SWeb);
            case S2C_WEB -> setJsVastTagsList(predefinedJsS2CWeb);
            case S2S_APP -> setJsVastTagsList(predefinedJsS2SInApp);
            case S2C_APP -> setJsVastTagsList(predefinedJsS2CInApp);
        }
        return this;
    }

    public jsTagTypeEnum getJsTagType() {
        return this.jsTagType;
    }

    public EndpointSupplyDO setJsVastTagsList(List<macroTagsTypes> list) {
        this.jsVastTagsList.addAll(list);
        addMacrosToJsVastUrl(this.jsVastTagsList);
        return this;
    }

    public EndpointSupplyDO addJsVastTag(macroTagsTypes tag) {
        this.jsVastTagsList.add(tag);
        addMacrosToJsVastUrl(this.jsVastTagsList);
        return this;
    }

    public List<macroTagsTypes> getJsVastTagsList() {
        return this.jsVastTagsList;
    }

    public String addMacrosToJsVastUrl(List<macroTagsTypes> macrosList) {
        for (macroTagsTypes macro : macrosList) {
            jsVastUrlWithMacros += "&" + macro.attributeName() + "=" + getMacroTagPlaceholder(macro, this.vastTagType, null);
        }
        return jsVastUrlWithMacros;
    }

    public EndpointSupplyDO setJsTagSize(iabSizesEnum size) {
        this.jsTagSize = size;
        this.jsWidth = size.getWidth();
        this.jsHeight = size.getHeight();
        return this;
    }

    public iabSizesEnum getJsTagSize() {
        return this.jsTagSize;
    }

    public EndpointSupplyDO setJsTagCustomSize(int width, int height) {
        this.jsWidth = width;
        this.jsHeight = height;
        return this;
    }

    public Integer getJsWidth() {
        return this.jsWidth;
    }

    public Integer getJsHeight() {
        return this.jsHeight;
    }

    public EndpointSupplyDO setJsBackfill(String backfill) {
        this.jsBackfill = backfill;
        return this;
    }

    public String getJsBackfill() {
        return this.jsBackfill;
    }

    //</editor-fold>

    //<editor-fold desc="Settings">
    private Map<String, Boolean> dspAllowedMap = new LinkedHashMap<>(), dspBlockedMap = new LinkedHashMap<>();
    private regionsEnum region;
    private bidPriceTypeEnum bidType = bidPriceTypeEnum.FLOOR;
    private Integer tmax = 0, spendLimit = 0;
    private Double bidPrice = 0d;
    private MarginSettings marginSettings = new MarginSettings().setFixed(0d);
    private adaptersSspRtbEnum adapter = adaptersSspRtbEnum.ANY;

    public EndpointSupplyDO setRegion(regionsEnum region) {
        this.region = region;
        return this;
    }

    public regionsEnum getRegion() {
        return this.region;
    }

    public EndpointSupplyDO setDspAllowedMap(Map<String, Boolean> map) {
        this.dspAllowedMap.putAll(map);
        return this;
    }

    public EndpointSupplyDO addDspToAllowed(String ssp, boolean status) {
        this.dspAllowedMap.put(ssp, status);
        return this;
    }

    public Map<String, Boolean> getDspAllowedMap() {
        return this.dspAllowedMap;
    }

    public EndpointSupplyDO clearDspAllowedMap() {
        this.dspAllowedMap.clear();
        return this;
    }

    public EndpointSupplyDO setDspBlockedMap(Map<String, Boolean> map) {
        this.dspBlockedMap.putAll(map);
        return this;
    }

    public EndpointSupplyDO addDspToBlocked(String ssp, boolean status) {
        this.dspBlockedMap.put(ssp, status);
        return this;
    }

    public Map<String, Boolean> getDspBlockedMap() {
        return this.dspBlockedMap;
    }

    public EndpointSupplyDO clearDspBlockedMap() {
        this.dspBlockedMap.clear();
        return this;
    }

    public EndpointSupplyDO setAdapter(adaptersSspRtbEnum adapter) {
        this.adapter = adapter;
        return this;
    }

    public adaptersSspRtbEnum getAdapter() {
        return this.adapter;
    }

    public EndpointSupplyDO setTmax(int tmax) {
        this.tmax = tmax;
        return this;
    }

    public Integer getTmax() {
        return this.tmax;
    }

    public EndpointSupplyDO setSpendLimit(int limit) {
        this.spendLimit = limit;
        return this;
    }

    public Integer getSpendLimit() {
        return this.spendLimit;
    }

    public EndpointSupplyDO setBidPrice(bidPriceTypeEnum type, double bid) {
        this.bidType = type;
        this.bidPrice = bid;
        return this;
    }

    public Double getBidPrice() {
        return this.bidPrice;
    }

    public bidPriceTypeEnum getBidType() {
        return this.bidType;
    }

    public EndpointSupplyDO setMarginSettings(MarginSettings settings) {
        this.marginSettings = settings;
        return this;
    }

    public MarginSettings getMarginSettings() {
        return this.marginSettings;
    }

    //</editor-fold>

    //<editor-fold desc="Toggle settings">
    private Double dynamicMarginValue;
    private Map<adFormatPlacementTypeEnum, Boolean> adFormatStatusMap = new HashMap<>() {{
        put(adFormatPlacementTypeEnum.BANNER, true);
        put(adFormatPlacementTypeEnum.VIDEO, true);
        put(adFormatPlacementTypeEnum.AUDIO, false);
        put(adFormatPlacementTypeEnum.NATIVE, true);
    }};
    private Map<adFormatPlacementTypeEnum, Boolean> adFormatAvailabilityMap = new HashMap<>() {{
        put(adFormatPlacementTypeEnum.BANNER, true);
        put(adFormatPlacementTypeEnum.VIDEO, true);
        put(adFormatPlacementTypeEnum.AUDIO, true);
        put(adFormatPlacementTypeEnum.NATIVE, true);
    }};
    private Map<trafficTypeEnum, Boolean> trafficStatusMap = new HashMap<>() {{
        put(trafficTypeEnum.WEB, true);
        put(trafficTypeEnum.APP, true);
        put(trafficTypeEnum.MOBILE_WEB, true);
        put(trafficTypeEnum.CTV, true);
    }};
    private Map<trafficTypeEnum, Boolean> trafficAvailabilityMap = new HashMap<>() {{
        put(trafficTypeEnum.WEB, true);
        put(trafficTypeEnum.APP, true);
        put(trafficTypeEnum.MOBILE_WEB, true);
        put(trafficTypeEnum.CTV, true);
    }};
    private Map<advancedSettingsEnum, Boolean> advancedStatusMap = new HashMap<>() {{
        put(advancedSettingsEnum.NURL_SSP, false);
        put(advancedSettingsEnum.IFA, false);
        put(advancedSettingsEnum.DYNAMIC_MARGIN, false);
        put(advancedSettingsEnum.RCPM, false);
    }};
    private Map<advancedSettingsEnum, Boolean> advancedAvailabilityMap = new HashMap<>() {{
        put(advancedSettingsEnum.NURL_SSP, true);
        put(advancedSettingsEnum.IFA, true);
        put(advancedSettingsEnum.DYNAMIC_MARGIN, true);
        put(advancedSettingsEnum.RCPM, true);
    }};

    public EndpointSupplyDO setAdFormatMap(Map<adFormatPlacementTypeEnum, Boolean> map) {
        this.adFormatStatusMap.putAll(map);
        return this;
    }

    public EndpointSupplyDO addAdFormatSetting(adFormatPlacementTypeEnum format, boolean status) {
        this.adFormatStatusMap.put(format, status);
        return this;
    }

    public Map<adFormatPlacementTypeEnum, Boolean> getAdFormatStatusMap() {
        return this.adFormatStatusMap;
    }

    public Map<adFormatPlacementTypeEnum, Boolean> getAdFormatAvailabilityMap() {
        return this.adFormatAvailabilityMap;
    }

    public EndpointSupplyDO setTrafficMap(Map<trafficTypeEnum, Boolean> map) {
        this.trafficStatusMap.putAll(map);
        return this;
    }

    public EndpointSupplyDO addTrafficSetting(trafficTypeEnum traffic, boolean status) {
        this.trafficStatusMap.put(traffic, status);
        return this;
    }

    public Map<trafficTypeEnum, Boolean> getTrafficStatusMap() {
        return this.trafficStatusMap;
    }

    public Map<trafficTypeEnum, Boolean> getTrafficAvailabilityMap() {
        return this.trafficAvailabilityMap;
    }

    public EndpointSupplyDO setAdvancedMap(Map<advancedSettingsEnum, Boolean> map) {
        this.advancedStatusMap.putAll(map);
        return this;
    }

    public EndpointSupplyDO addAdvancedSetting(advancedSettingsEnum setting, boolean status) {
        this.advancedStatusMap.put(setting, status);
        return this;
    }

    public EndpointSupplyDO setDynamicMarginValue(double value) {
        this.dynamicMarginValue = value;
        return this;
    }

    public Double getDynamicMarginValue() {
        return this.dynamicMarginValue;
    }

    public Map<advancedSettingsEnum, Boolean> getAdvancedStatusMap() {
        return this.advancedStatusMap;
    }

    public Map<advancedSettingsEnum, Boolean> getAdvancedAvailabilityMap() {
        return this.advancedAvailabilityMap;
    }

    //</editor-fold>

    //<editor-fold desc="Additional settings">
    private String reportApiLinkPartner, reportApiLinkEndpoint;
    private endpointPartnerApiTypeEnum partnerApiType;
    private Map<endpointPartnerApiColumnsEnum, String> csvColumnsMap = new LinkedHashMap<>();

    public EndpointSupplyDO setReportApiLinkPartner(String link, endpointPartnerApiTypeEnum type) {
        this.reportApiLinkPartner = link;
        this.partnerApiType = type;
        return this;
    }

    public String getReportApiLinkPartner() {
        return this.reportApiLinkPartner;
    }

    public endpointPartnerApiTypeEnum getPartnerApiType() {
        return this.partnerApiType;
    }

    public EndpointSupplyDO setReportApiLinkEndpoint(String link) {
        this.reportApiLinkEndpoint = link;
        return this;
    }

    public String getReportApiLinkEndpoint() {
        return this.reportApiLinkEndpoint;
    }

    public EndpointSupplyDO setCsvColumnsMap(Map<endpointPartnerApiColumnsEnum, String> map) {
        this.csvColumnsMap.putAll(map);
        return this;
    }

    public Map<endpointPartnerApiColumnsEnum, String> getCsvColumnsMap() {
        return this.csvColumnsMap;
    }

    //</editor-fold>

    //<editor-fold desc="Scanners">
    private List<ScannerSetting> scannerSettings = new ArrayList<>();

    public EndpointSupplyDO setScannerSettings(List<ScannerSetting> settings) {
        this.scannerSettings.addAll(settings);
        return this;
    }

    public EndpointSupplyDO addScannerSetting(ScannerSetting setting) {
        this.scannerSettings.add(setting);
        return this;
    }

    public List<ScannerSetting> getScannerSettings() {
        return this.scannerSettings;
    }

    //</editor-fold>

    //<editor-fold desc="Table info">
    private Integer qpsReal = 0, qpsBid = 0;
    private Double revenueYesterday = 0d, revenueToday = 0d, winRate = 0d;

    public EndpointSupplyDO setTableStats(Integer qpsReal, Integer qpsBid, Double spendYesterday, Double spendToday, Double winRate) {
        this.qpsReal = qpsReal;
        this.qpsBid = qpsBid;
        this.revenueYesterday = spendYesterday;
        this.revenueToday = spendToday;
        this.winRate = winRate;
        return this;
    }

    public Integer getQpsReal() {
        return this.qpsReal;
    }

    public Integer getQpsBid() {
        return this.qpsBid;
    }

    public Double getRevenueYesterday() {
        return this.revenueYesterday;
    }

    public Double getRevenueToday() {
        return this.revenueToday;
    }

    public Double getWinRate() {
        return this.winRate;
    }
    //</editor-fold>

}
