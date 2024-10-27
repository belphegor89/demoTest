package data.dataobject;

import data.InventoryEnum;
import data.textstrings.messages.InventoryText;

import java.util.*;

public class InventoryDO {
    private String name,
            bundleDomain,
            storeUrl,
            blockedDomains;
    private InventoryEnum.inventoryTypeEnum type;
    private Map<String, Boolean> allowedDsp = new HashMap<>(), blockedDsp = new HashMap<>();
    private boolean ronTraffic;
    private List<InventoryEnum.adCategoryEnum> categoriesSelected, categoriesFiltered;
    private InventoryEnum.inventoryStatusEnum status;
    private Integer id, countBanners = 0, countVideo = 0, countNative = 0;
    private Locale languageLocale;

    public InventoryDO() {
        this.languageLocale = Locale.ENGLISH;
        this.ronTraffic = false;
    }

    public InventoryDO(InventoryEnum.inventoryTypeEnum type, String name, String bundleDomain, List<InventoryEnum.adCategoryEnum> supplyCategories) {
        this.type = type;
        this.name = name;
        this.bundleDomain = bundleDomain;
        this.categoriesSelected = supplyCategories;
        this.categoriesFiltered = new ArrayList<>();
        this.languageLocale = Locale.ENGLISH;
        this.ronTraffic = false;
    }

    //<editor-fold desc="Setters">
    public InventoryDO setName(String name) {
        this.name = name;
        return this;
    }

    public InventoryDO setBundleDomain(String bundleDomain) {
        this.bundleDomain = bundleDomain;
        return this;
    }

    public InventoryDO setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
        return this;
    }

    public InventoryDO setLanguage(Locale languageLocale) {
        this.languageLocale = languageLocale;
        return this;
    }

    public InventoryDO setBlockedDomains(String blockedDomains) {
        this.blockedDomains = blockedDomains;
        return this;
    }

    public InventoryDO setType(InventoryEnum.inventoryTypeEnum type) {
        this.type = type;
        return this;
    }

    public InventoryDO setAllowedDsp(Map<String, Boolean> allowedDsp) {
        this.allowedDsp = allowedDsp;
        return this;
    }

    public InventoryDO setBlockedDsp(Map<String, Boolean> blockedDsp) {
        this.blockedDsp = blockedDsp;
        return this;
    }

    public InventoryDO setRonTraffic(boolean ronTraffic) {
        this.ronTraffic = ronTraffic;
        return this;
    }

    public InventoryDO setCategoriesSelected(List<InventoryEnum.adCategoryEnum> categoriesSelected) {
        this.categoriesSelected = new ArrayList<>(categoriesSelected);
        return this;
    }

    public InventoryDO setCategoriesFiltered(List<InventoryEnum.adCategoryEnum> categoriesFiltered) {
        this.categoriesFiltered = new ArrayList<>(categoriesFiltered);
        return this;
    }

    public InventoryDO setStatus(InventoryEnum.inventoryStatusEnum status) {
        this.status = status;
        return this;
    }

    public InventoryDO setId(int id) {
        this.id = id;
        return this;
    }

    public InventoryDO setCountBanners(int countBanners) {
        this.countBanners = countBanners;
        return this;
    }

    public InventoryDO setCountVideo(int countVideo) {
        this.countVideo = countVideo;
        return this;
    }

    public InventoryDO setCountNative(int countNative) {
        this.countNative = countNative;
        return this;
    }

    //</editor-fold>

    //<editor-fold desc="Getters">
    public String getName() {
        return this.name;
    }

    public String getBundleDomain() {
        return this.bundleDomain;
    }

    public String getStoreUrl() {
        return this.storeUrl;
    }

    public String getLanguage() {
        return this.languageLocale.getDisplayLanguage();
    }

    public Locale getLanguageLocale() {
        return this.languageLocale;
    }

    public String getBlockedDomains() {
        return this.blockedDomains;
    }

    public InventoryEnum.inventoryTypeEnum getType() {
        return this.type;
    }

    public Map<String, Boolean> getAllowedDsp() {
        return this.allowedDsp;
    }

    public Map<String, Boolean> getBlockedDsp() {
        return this.blockedDsp;
    }

    public boolean getRonTraffic() {
        return this.ronTraffic;
    }

    public List<InventoryEnum.adCategoryEnum> getCategoriesSelected() {
        return this.categoriesSelected;
    }

    public List<InventoryEnum.adCategoryEnum> getCategoriesFiltered() {
        return this.categoriesFiltered;
    }

    public InventoryEnum.inventoryStatusEnum getStatus() {
        return this.status;
    }

    public String getPlacementsCountsString() {
        if (this.type == InventoryEnum.inventoryTypeEnum.CTV) {
            if (countVideo > 0) {
                return "Video: " + countVideo;
            }
        } else {
            if (countBanners > 0 || countVideo > 0 || countNative > 0) {
                return "Banner: " + countBanners + ", Video: " + countVideo + " , Native: " + countNative;
            }
        }
        return InventoryText.invPlacementsCntEmpty;
    }

    public Integer getId() {
        return this.id;
    }

    public int getCountBanners() {
        return this.countBanners;
    }

    public int getCountVideo() {
        return this.countVideo;
    }

    public int getCountNative() {
        return this.countNative;
    }

    //</editor-fold>


}
