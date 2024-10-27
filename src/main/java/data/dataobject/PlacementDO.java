package data.dataobject;

import common.utils.BasicUtility;
import common.utils.ParseAndFormatUtility;
import data.CommonEnums;
import data.PlacementEnum.*;

import java.util.*;

public class PlacementDO {
    private BasicUtility bu = new BasicUtility();

    protected String name, backfill;
    protected Integer id;
    protected boolean status = true,
            testMode = false,
            isCountryCpm = false;
    protected Size size;
    private adPositionEnum adPosition;
    protected BidPrice bidPrice;
    private placementTypeEnum type;
    private Map<apiFrameworksEnum, Boolean> apiFrameworks = new LinkedHashMap<>();
    private Set<adTypeEnum> blockedAdTypes = new LinkedHashSet<>(); // Use LinkedHashSet to avoid duplicates and maintain insertion order
    private Set<placementAttributeEnum> blockedAttributes = new LinkedHashSet<>();
    //Video
    private boolean rewarded = false,
            allowSkip = false,
            serveViaApi = true;
    private Integer skipDelay = 0,
            startDelay = 1;
    private videoLinearityTypeEnum linearity = videoLinearityTypeEnum.LINEAR_INSTREAM;
    private placingTypeEnum placingType = placingTypeEnum.INSLIDE;
    private videoStartDelayTypeEnum startDelayType = videoStartDelayTypeEnum.PREROLL;
    private playbackMethodsTypeEnum playbackMethod = playbackMethodsTypeEnum.AUTOPLAY_SOUNDOFF;
    private Map<protocolsTypeEnum, Boolean> videoProtocols = new LinkedHashMap<>();
    private Set<mimeTypesEnum> videoMimeTypes = new LinkedHashSet<>();  // Use LinkedHashSet to avoid duplicates and maintain insertion order
    //Native
    private placementAdEnvironmentEnum adEnvironment;
    private placementAdvancedAdSettingsEnum advancedAdSettings;
    private placementAdUnitOptionsEnum adUnit;
    private boolean adTitleStatus = false;
    private Integer adTitleLength = 60, descriptionLength = 256;
    private Map<placementDataAssetTypeEnum, Boolean> dataAssetsMaps = new LinkedHashMap<>();
    private Map<placementImageTypeEnum, Boolean> adImagesMap = new LinkedHashMap<>();
    private Size nativeMainImageSize = new Size(108, 89);
    private Map<String, SelfId> elementIdMap = new HashMap<>();

    protected static ParseAndFormatUtility FormatUtil = new ParseAndFormatUtility();

    public PlacementDO() {
    }

    public PlacementDO(String name, placementTypeEnum type) {
        this.name = name;
        this.type = type;
        this.bidPrice = new BidPrice(CommonEnums.bidPriceTypeEnum.FLOOR, 0.0);
    }

    //<editor-fold desc="Common Setters">
    public PlacementDO setName(String name) {
        this.name = name;
        return this;
    }

    public PlacementDO setTestMode(boolean testMode) {
        this.testMode = testMode;
        return this;
    }

    public PlacementDO setId(Integer id) {
        this.id = id;
        return this;
    }

    public PlacementDO setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public PlacementDO setBidPrice(BidPrice bidPrice) {
        this.bidPrice = bidPrice;
        return this;
    }

    public PlacementDO setCountryCpm(boolean isCountryCpm) {
        this.isCountryCpm = isCountryCpm;
        return this;
    }

    public PlacementDO setType(placementTypeEnum type) {
        this.type = type;
        return this;
    }

    public PlacementDO setSize(Size size) {
        this.size = size;
        return this;
    }

    public PlacementDO setSizePreset(CommonEnums.iabSizesEnum iabSize) {
        this.size = new Size(iabSize);
        return this;
    }

    public PlacementDO setAdPosition(adPositionEnum position) {
        this.adPosition = position;
        return this;
    }

    public PlacementDO setApiFrameworks(Map<apiFrameworksEnum, Boolean> frameworksMap) {
        // Sort the map by enum's declaration order using ordinal. The order is important for the dropdown label assertions
        Map<apiFrameworksEnum, Boolean> sortedMap = frameworksMap.entrySet()
                .stream().sorted(Map.Entry.comparingByKey(Comparator.comparingInt(Enum::ordinal)))
                .collect(
                        LinkedHashMap::new, // Maintain order after sorting
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        LinkedHashMap::putAll
                );
        this.apiFrameworks.putAll(sortedMap);
        return this;
    }

    public PlacementDO setBackfill(String backfill) {
        this.backfill = backfill;
        return this;
    }

    public PlacementDO setAdTypesToBlock(List<adTypeEnum> blockedAdTypes) {
        this.blockedAdTypes.addAll(blockedAdTypes);
        return this;
    }

    public PlacementDO setAttributesToBlock(List<placementAttributeEnum> blockedAttributes) {
        this.blockedAttributes.addAll(blockedAttributes);
        return this;
    }

    //*********//
    public PlacementDO setLinearity(videoLinearityTypeEnum linearity) {
        this.linearity = linearity;
        return this;
    }

    public PlacementDO setLinearity(String linearity) {
        for (videoLinearityTypeEnum type : videoLinearityTypeEnum.values()) {
            if (type.publicName().equals(linearity)) {
                this.linearity = type;
                break;
            }
        }
        return this;
    }


    public PlacementDO setPlacing(placingTypeEnum placingType) {
        this.placingType = placingType;
        return this;
    }

    public PlacementDO setStartDelayType(videoStartDelayTypeEnum startDelayType) {
        this.startDelayType = startDelayType;
        return this;
    }

    public PlacementDO setStartDelayValue(Integer startDelay) {
        this.startDelay = startDelay;
        return this;
    }

    public PlacementDO setSkipDelay(Integer skipDelay) {
        this.skipDelay = skipDelay;
        return this;
    }

    public PlacementDO setRewarded(boolean rewarded) {
        this.rewarded = rewarded;
        return this;
    }

    public PlacementDO setAllowSkip(boolean allowSkip) {
        this.allowSkip = allowSkip;
        return this;
    }

    public PlacementDO setServeViaApi(boolean serveViaApi) {
        this.serveViaApi = serveViaApi;
        return this;
    }

    public PlacementDO setPlaybackMethod(playbackMethodsTypeEnum playbackMethod) {
        this.playbackMethod = playbackMethod;
        return this;
    }

    public PlacementDO setVideoProtocols(Map<protocolsTypeEnum, Boolean> videoProtocols) {
        Map<protocolsTypeEnum, Boolean> sortedMap = bu.sortByEnumOrdinal(videoProtocols);
        this.videoProtocols.putAll(sortedMap);
        return this;
    }

    public PlacementDO setVideoMimeTypes(List<mimeTypesEnum> videoMimeTypes) {
        this.videoMimeTypes.addAll(videoMimeTypes);
        return this;
    }

    //**********//
    public PlacementDO setAdEnvironment(placementAdEnvironmentEnum adEnvironment) {
        this.adEnvironment = adEnvironment;
        return this;
    }

    public PlacementDO setAdvancedAdSettings(placementAdvancedAdSettingsEnum advancedAdSettings) {
        this.advancedAdSettings = advancedAdSettings;
        return this;
    }

    public PlacementDO setAdUnit(placementAdUnitOptionsEnum adUnit) {
        this.adUnit = adUnit;
        return this;
    }

    public PlacementDO setAdTitleStatus(boolean adTitleStatus) {
        this.adTitleStatus = adTitleStatus;
        if (adTitleStatus) {
            addElementId("Title", false, null);
        }
        return this;
    }

    public PlacementDO setAdTitleLength(Integer adTitleLength) {
        this.adTitleLength = adTitleLength;
        return this;
    }

    public PlacementDO setDescriptionLength(Integer descriptionLength) {
        this.descriptionLength = descriptionLength;
        return this;
    }

    public PlacementDO setDataAssetsMaps(Map<placementDataAssetTypeEnum, Boolean> dataAssetsMaps) {
        //If Sale price is present, the Price is added automatically in the same status
        if (dataAssetsMaps.containsKey(placementDataAssetTypeEnum.SALEPRICE)) {
            dataAssetsMaps.put(placementDataAssetTypeEnum.PRICE, dataAssetsMaps.get(placementDataAssetTypeEnum.SALEPRICE));
        }
        Map<placementDataAssetTypeEnum, Boolean> sortedMap = bu.sortByEnumOrdinal(dataAssetsMaps);
        this.dataAssetsMaps.putAll(sortedMap);
        for (placementDataAssetTypeEnum element : dataAssetsMaps.keySet()) {
            addElementId(element.publicName(), false, null);
        }
        return this;
    }

    public PlacementDO setAdImagesMap(Map<placementImageTypeEnum, Boolean> adImagesMap) {
        Map<placementImageTypeEnum, Boolean> sortedMap = bu.sortByEnumOrdinal(adImagesMap);
        this.adImagesMap.putAll(sortedMap);
        for (placementImageTypeEnum imageType : adImagesMap.keySet()) {
            addElementId(imageType.publicName(), false, null);
        }
        return this;
    }

    public PlacementDO addAdImage(placementImageTypeEnum imageType, boolean status) {
        this.adImagesMap.put(imageType, status);
        if (status) {
            addElementId(imageType.publicName(), false, null);
        }
        return this;
    }

    public PlacementDO setNativeMainImageSize(Integer width, Integer height) {
        this.nativeMainImageSize = new Size(width, height);
        return this;
    }

    public PlacementDO addElementId(String element, boolean required, Integer id) {
        this.elementIdMap.put(element, new SelfId(element, required, id));
        return this;
    }

    public PlacementDO addElementId(String element) {
        addElementId(element, false, null);
        return this;
    }

    //</editor-fold>

    //<editor-fold desc="Common Getters">

    public String getName() {
        return name;
    }

    public boolean getTestMode() {
        return testMode;
    }

    public Integer getId() {
        return id;
    }

    public boolean getStatus() {
        return status;
    }

    public BidPrice getBidPrice() {
        return bidPrice;
    }

    public placementTypeEnum getType() {
        return type;
    }

    public boolean getCountryCpm() {
        return isCountryCpm;
    }

    public Size getSize() {
        return size;
    }

    public adPositionEnum getAdPosition() {
        return adPosition;
    }

    public Map<apiFrameworksEnum, Boolean> getApiFrameworks() {
        //sort map by keys in the order of apiFrameworksEnum


        return apiFrameworks;
    }

    public String getBackfill() {
        return backfill;
    }

    public List<adTypeEnum> getBlockedAdTypes() {
        return blockedAdTypes.stream().toList();
    }

    public List<placementAttributeEnum> getBlockedAttributes() {
        return blockedAttributes.stream().toList();
    }

    //****Video*****//

    public videoLinearityTypeEnum getLinearity() {
        return linearity;
    }

    public placingTypeEnum getPlacing() {
        return placingType;
    }

    public videoStartDelayTypeEnum getStartDelayType() {
        return startDelayType;
    }

    public Integer getStartDelayValue() {
        return startDelay;
    }

    public Integer getSkipDelay() {
        return skipDelay;
    }

    public boolean getRewarded() {
        return rewarded;
    }

    public boolean getAllowSkip() {
        return allowSkip;
    }

    public boolean getServeViaApi() {
        return serveViaApi;
    }

    public playbackMethodsTypeEnum getPlaybackMethod() {
        return playbackMethod;
    }

    public Map<protocolsTypeEnum, Boolean> getVideoProtocols() {
        return videoProtocols;
    }

    public List<mimeTypesEnum> getVideoMimeTypes() {
        return videoMimeTypes.stream().toList();
    }

    //****Native****//

    public placementAdEnvironmentEnum getAdEnvironment() {
        return adEnvironment;
    }

    public placementAdvancedAdSettingsEnum getAdvancedAdSettings() {
        return advancedAdSettings;
    }

    public placementAdUnitOptionsEnum getAdUnit() {
        return adUnit;
    }

    public boolean getAdTitleStatus() {
        return adTitleStatus;
    }

    public Integer getAdTitleLength() {
        return adTitleLength;
    }

    public Integer getDescriptionLength() {
        return descriptionLength;
    }

    public Map<placementDataAssetTypeEnum, Boolean> getDataAssetsMaps() {
        return dataAssetsMaps;
    }

    public Map<placementImageTypeEnum, Boolean> getAdImagesMap() {
        return adImagesMap;
    }

    public Size getNativeMainImageSize() {
        return nativeMainImageSize;
    }

    public Map<String, SelfId> getElementsIdMap() {
        return elementIdMap;
    }

    //</editor-fold>

    public static class BidPrice {
        private Double value;
        private CommonEnums.bidPriceTypeEnum type;

        public BidPrice(CommonEnums.bidPriceTypeEnum type, Double value) {
            this.type = type;
            this.value = value;
        }

        //<editor-fold desc="Getters">
        public Double getValue() {
            if (this.value == null) {
                return null;
            }
            return FormatUtil.roundDouble(this.value, 2);
        }

        public CommonEnums.bidPriceTypeEnum getType() {
            return type;
        }
        //</editor-fold>

        //<editor-fold desc="Setters">
        public BidPrice setValue(Double value) {
            this.value = value;
            return this;
        }

        public void setType(CommonEnums.bidPriceTypeEnum type) {
            this.type = type;
        }
        //</editor-fold>
    }

    public static class Size {
        private Integer width;
        private Integer height;
        private CommonEnums.iabSizesEnum sizePreset;

        public Size(Integer width, Integer height) {
            this.sizePreset = CommonEnums.iabSizesEnum.CUSTOM;
            this.width = width;
            this.height = height;
        }

        public Size(CommonEnums.iabSizesEnum sizePreset) {
            this.sizePreset = sizePreset;
            this.width = sizePreset.getWidth();
            this.height = sizePreset.getHeight();
        }

        //<editor-fold desc="Getters">

        public Integer getWidth() {
            return width;
        }

        public Integer getHeight() {
            return height;
        }

        public CommonEnums.iabSizesEnum getPreset() {
            return sizePreset;
        }
        //</editor-fold>

        //<editor-fold desc="Setters">
        public void setWidth(Integer width) {
            this.width = width;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        public void setSize(CommonEnums.iabSizesEnum sizePreset) {
            this.sizePreset = sizePreset;
            this.width = sizePreset.getWidth();
            this.height = sizePreset.getHeight();
        }

        //</editor-fold>

    }

    public class SelfId {
        private String elementName;
        private boolean required;
        private Integer elId;

        public SelfId(String element, boolean required, Integer id) {
            this.elementName = element;
            this.required = required;
            this.elId = id;
        }

        //<editor-fold desc="Setters">

        public void setElementName(String elementName) {
            this.elementName = elementName;
        }

        public void setRequired(boolean required) {
            this.required = required;
        }

        public void setId(Integer id) {
            this.elId = id;
        }

        //</editor-fold>

        //<editor-fold desc="Getters">
        public String getElementName() {
            return elementName;
        }

        public boolean getRequired() {
            return required;
        }

        public Integer getId() {
            return elId;
        }

        public Integer getIdForAssertion() {
            if (elId != null) {
                return elId;
            } else {
                int defaultId = switch (elementName) {
                    case "Title" -> 1;
                    case "Icon" -> 2;
                    case "Main" -> 3;
                    case "Desc" -> 4;
                    case "Rating" -> 5;
                    case "Likes" -> 6;
                    case "Downloads" -> 7;
                    case "Price" -> 8;
                    case "Sale price" -> 9;
                    case "Phone" -> 10;
                    case "Address" -> 11;
                    case "Desc2" -> 12;
                    case "Display URL" -> 13;
                    case "CTA text" -> 14;
                    case "Sponsored" -> 15;
                    default -> throw new IllegalStateException("Unexpected value: " + elementName);
                };
                return Integer.parseInt(String.valueOf(PlacementDO.this.getId()) + String.valueOf(defaultId));
            }
        }

        //</editor-fold>
    }
}
