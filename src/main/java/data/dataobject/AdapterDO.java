package data.dataobject;

import com.fasterxml.jackson.databind.MappingIterator;
import common.utils.FileUtility;
import data.AdaptersEnums;
import data.CommonEnums.adFormatPlacementTypeEnum;
import data.CommonEnums.countriesEnum;
import data.CommonEnums.iabSizesEnum;
import data.CommonEnums.trafficTypeEnum;

import java.io.File;
import java.util.*;

public class AdapterDO{
    private AdaptersEnums.adaptersApiEnum apiAdapterType;
    private AdaptersEnums.adaptersPrebidEnum prebidAdapterType;
    private String adapterName, settingName;
    private Boolean isImport = false, allSizes = false;
    private final Map<String, Boolean> osMap = new LinkedHashMap<>();
    private final Map<String, String> customSettingsMap = new LinkedHashMap<>();
    private final Map<trafficTypeEnum, Boolean> trafficMap = new LinkedHashMap<>();
    private final Map<adFormatPlacementTypeEnum, Boolean> adFormatMap = new LinkedHashMap<>();
    private final List<String> bundleDomainList = new ArrayList<>();
    private final List<SizeSetting> sizesList = new ArrayList<>();
    private final List<GeoSetting> geoList = new ArrayList<>();

    public AdapterDO(){}

    public AdapterDO setApiAdapterType(AdaptersEnums.adaptersApiEnum apiAdapterType){
        this.apiAdapterType = apiAdapterType;
        this.setAdapterName(apiAdapterType.publicName());
        return this;
    }

    public AdaptersEnums.adaptersApiEnum getApiAdapterType(){
        return apiAdapterType;
    }

    public AdapterDO setPrebidAdapterType(AdaptersEnums.adaptersPrebidEnum prebidAdapterType){
        this.prebidAdapterType = prebidAdapterType;
        this.setAdapterName(prebidAdapterType.publicName());
        return this;
    }

    public AdaptersEnums.adaptersPrebidEnum getPrebidAdapterType(){
        return prebidAdapterType;
    }

    public AdapterDO setAdapterName(String adapterName){
        this.adapterName = adapterName;
        return this;
    }

    public String getAdapterName(){
        return adapterName;
    }

    public AdapterDO setSettingName(String settingName){
        this.settingName = settingName;
        return this;
    }

    public String getSettingName(){
        return settingName;
    }

    public AdapterDO setIsImport(Boolean isImport){
        this.isImport = isImport;
        return this;
    }

    public Boolean getIsImport(){
        return isImport;
    }

    public AdapterDO setCustomSettingsMap(Map<String, String> customSettingsMap){
        this.customSettingsMap.putAll(customSettingsMap);
        return this;
    }

    public Map<String, String> getCustomSettingsMap(){
        return this.customSettingsMap;
    }

    public AdapterDO setAllSizes(Boolean allSizes){
        this.allSizes = allSizes;
        return this;
    }

    public Boolean getAllSizes(){
        return allSizes;
    }

    public AdapterDO setOsMap(Map<String, Boolean> osList){
        this.osMap.putAll(osList);
        return this;
    }

    public AdapterDO clearOsMap(){
        this.osMap.clear();
        return this;
    }

    public Map<String, Boolean> getOsMap(){
        return osMap;
    }

    public AdapterDO addOs(String os){
        this.osMap.put(os, true);
        return this;
    }

    public AdapterDO setTrafficMap(Map<trafficTypeEnum, Boolean> trafficList){
        this.trafficMap.putAll(trafficList);
        return this;
    }

    public AdapterDO clearTrafficMap(){
        this.trafficMap.clear();
        return this;
    }

    public Map<trafficTypeEnum, Boolean> getTrafficMap(){
        return trafficMap;
    }

    public Map<String, Boolean> getTrafficNamesMap(){
        Map<String, Boolean> trafficStringMap = new LinkedHashMap<>();
        for (trafficTypeEnum traffic : trafficMap.keySet()){
            trafficStringMap.put(traffic.publicName(), trafficMap.get(traffic));
        }
        return trafficStringMap;
    }

    public AdapterDO addTraffic(trafficTypeEnum traffic){
        this.trafficMap.put(traffic, true);
        return this;
    }

    public AdapterDO setAdFormatMap(Map<adFormatPlacementTypeEnum, Boolean> adFormatList){
        this.adFormatMap.putAll(adFormatList);
        return this;
    }

    public AdapterDO clearAdFormatMap(){
        this.adFormatMap.clear();
        return this;
    }

    public Map<adFormatPlacementTypeEnum, Boolean> getAdFormatMap(){
        return adFormatMap;
    }

    public Map<String, Boolean> getAdFormatNamesMap(){
        Map<String, Boolean> adFormatStringMap = new LinkedHashMap<>();
        for (adFormatPlacementTypeEnum adFormat : adFormatMap.keySet()){
            adFormatStringMap.put(adFormat.publicName(), adFormatMap.get(adFormat));
        }
        return adFormatStringMap;
    }

    public AdapterDO addAdFormat(adFormatPlacementTypeEnum adFormat){
        this.adFormatMap.put(adFormat, true);
        return this;
    }

    public AdapterDO setSizesList(List<SizeSetting> sizesList){
        this.sizesList.addAll(sizesList);
        return this;
    }

    public AdapterDO clearSizesList(){
        this.sizesList.clear();
        return this;
    }

    public List<SizeSetting> getSizesList(){
        return sizesList;
    }

    public AdapterDO addSize(SizeSetting size){
        this.sizesList.add(size);
        return this;
    }

    public AdapterDO setGeoList(List<GeoSetting> geoList){
        this.geoList.addAll(geoList);
        return this;
    }

    public AdapterDO clearGeoList(){
        this.geoList.clear();
        return this;
    }

    public List<GeoSetting> getGeoList(){
        return geoList;
    }

    public AdapterDO addGeo(GeoSetting geo){
        this.geoList.add(geo);
        return this;
    }

    public List<String> getRecordList(){
        return bundleDomainList;
    }

    public AdapterDO setRecordList(List<String> recordList){
        this.bundleDomainList.addAll(recordList);
        return this;
    }

    public AdapterDO addRecord(String record){
        bundleDomainList.add(record);
        return this;
    }

    public AdapterDO addRecordImport(File importFile){
        MappingIterator<Map<String, Object>> parsedCsv = new FileUtility().readCsvFile(importFile);
        while (parsedCsv.hasNext()){
            for (Object value : parsedCsv.next().values()){
                bundleDomainList.add(value.toString());
            }
        }
        return this;
    }

    public static class SizeSetting{
        private iabSizesEnum SizeSetting;
        private Integer width, height;

        public SizeSetting(){}

        public SizeSetting setPredefinedSize(iabSizesEnum size){
            this.SizeSetting = size;
            this.width = size.getWidth();
            this.height = size.getHeight();
            return this;
        }

        public SizeSetting setCustomSize(int width, int height){
            this.SizeSetting = iabSizesEnum.CUSTOM;
            this.width = width;
            this.height = height;
            return this;
        }

        public SizeSetting setSize(iabSizesEnum size, int width, int height){
            this.SizeSetting = size;
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
            return this.SizeSetting;
        }

    }

    public static class GeoSetting{
        private final Map<adFormatPlacementTypeEnum, Boolean> geoAdFormatMap = new LinkedHashMap<>();
        private final Map<String, Boolean> countriesNamesMap = new TreeMap<>();
        private final Map<countriesEnum, Boolean> countriesEnumMap = new LinkedHashMap<>();

        public GeoSetting setAdFormatList(Map<adFormatPlacementTypeEnum, Boolean> adFormatList){
            this.geoAdFormatMap.putAll(adFormatList);
            return this;
        }

        public GeoSetting clearAdFormatList(){
            this.geoAdFormatMap.clear();
            return this;
        }

        public Map<adFormatPlacementTypeEnum, Boolean> getAdFormatList(){
            return this.geoAdFormatMap;
        }

        public GeoSetting addAdFormat(adFormatPlacementTypeEnum adFormat){
            this.geoAdFormatMap.put(adFormat, true);
            return this;
        }

        public GeoSetting addAdFormat(adFormatPlacementTypeEnum adFormat, Boolean checkOn){
            this.geoAdFormatMap.put(adFormat, checkOn);
            return this;
        }

        public Map<String, Boolean> getCountriesNamesMap(){
            for (Map.Entry<countriesEnum, Boolean> entry : countriesEnumMap.entrySet()){
                countriesNamesMap.put(entry.getKey().publicName(), entry.getValue());
            }
            return countriesNamesMap;
        }

        public GeoSetting setCountriesEnumList(Map<countriesEnum, Boolean> countriesList){
            this.countriesEnumMap.putAll(countriesList);
            return this;
        }

        public GeoSetting clearCountriesEnumList(){
            this.countriesEnumMap.clear();
            return this;
        }

        public Map<countriesEnum, Boolean> getCountriesEnumMap(){
            return this.countriesEnumMap;
        }

        public GeoSetting addCountry(countriesEnum country){
            this.countriesEnumMap.put(country, true);
            return this;
        }

        public GeoSetting addCountry(countriesEnum country, Boolean checkOn){
            this.countriesEnumMap.put(country, checkOn);
            return this;
        }

    }


}
