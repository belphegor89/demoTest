package data.dataobject;

import data.CommonEnums;
import data.ScannersEnums.pixalatePrebidTypesEnum;
import data.ScannersEnums.scannerTypesEnum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EndpointCommonDO {

    public static class MarginSettings{
        private CommonEnums.marginTypeEnum type = CommonEnums.marginTypeEnum.FIXED;
        private Double fixed = 0d, min, max;

        public MarginSettings setFixed(Double fixed){
            this.type = CommonEnums.marginTypeEnum.FIXED;
            this.fixed = fixed;
            return this;
        }

        public MarginSettings setAdaptive(){
            this.type = CommonEnums.marginTypeEnum.ADAPTIVE;
            return this;
        }

        public MarginSettings setRange(Double min, Double max){
            this.type = CommonEnums.marginTypeEnum.RANGE;
            this.min = min;
            this.max = max;
            return this;
        }

        public CommonEnums.marginTypeEnum getType(){
            return this.type;
        }

        public Double getFixed(){
            return this.fixed;
        }

        public Double getMin(){
            return this.min;
        }

        public Double getMax(){
            return this.max;
        }

    }

    public static class GeoSetting{
        private List<String> countriesList = new ArrayList<>();
        private Map<String, Boolean> countriesMap = new LinkedHashMap<>();
        private Integer qps;
        private Double bidfloorCpm;

        public GeoSetting setGeoBlacklist(){
            this.countriesList = new ArrayList<>();
            return this;
        }

        public GeoSetting setGeoBlacklist(Map<String, Boolean> blacklistMap){
            this.countriesMap.putAll(blacklistMap);
            return this;
        }

        public GeoSetting addGeoBlacklist(String country, boolean status){
            this.countriesMap.put(country, status);
            return this;
        }

        public GeoSetting setWhitelistQps(List<String> list, Integer qps){
            this.countriesList = list;
            this.qps = qps;
            return this;
        }

        public GeoSetting setWhitelistQps(Map<String, Boolean> map, Integer qps){
            this.countriesMap = map;
            this.qps = qps;
            return this;
        }

        public GeoSetting setGeoBidfloorCpm(List<String> list, Double bidfloorCpm){
            this.countriesList = list;
            this.bidfloorCpm = bidfloorCpm;
            return this;
        }

        public GeoSetting setBidfloorCpm(Map<String, Boolean> map, Double bidfloorCpm){
            this.countriesMap = map;
            this.bidfloorCpm = bidfloorCpm;
            return this;
        }

        public List<String> getCountriesList(){
            return this.countriesList;
        }

        public Map<String, Boolean> getCountriesMap(){
            return this.countriesMap;
        }

        public Integer getQps(){
            return this.qps;
        }

        public Double getBidfloorCpm(){
            return this.bidfloorCpm;
        }

    }

    public static class ScannerSetting{
        private scannerTypesEnum type;
        private Boolean status, impressionScanStatus;
        private Integer impressionScanValue;
        private Map<pixalatePrebidTypesEnum, PixalatePrebidSetting> pixalatePrebidMap = new LinkedHashMap<>();

        public ScannerSetting(scannerTypesEnum type, Boolean status){
            this.type = type;
            this.status = status;
        }

        public ScannerSetting setStatus(Boolean status){
            this.status = status;
            return this;
        }

        public ScannerSetting setImpressionScan(boolean status, int value){
            this.impressionScanStatus = status;
            this.impressionScanValue = value;
            return this;
        }

        public scannerTypesEnum getType(){
            return this.type;
        }

        public Boolean getScannerStatus(){
            return this.status;
        }

        public boolean getImpressionScanStatus(){
            return this.impressionScanStatus;
        }

        public Integer getImpressionScanValue(){
            return this.impressionScanValue;
        }

        public ScannerSetting setPixalatePrebidMap(Map<pixalatePrebidTypesEnum, PixalatePrebidSetting> map){
            this.pixalatePrebidMap = map;
            return this;
        }

        public Map<pixalatePrebidTypesEnum, PixalatePrebidSetting> getPixalatePrebidMap(){
            return this.pixalatePrebidMap;
        }

        public static class PixalatePrebidSetting{
            private Boolean status;
            private Integer value;

            public PixalatePrebidSetting(){}

            public PixalatePrebidSetting setSetting(Boolean status, Integer value){
                this.status = status;
                this.value = value;
                return this;
            }

            public PixalatePrebidSetting setStatus(Boolean status){
                this.status = status;
                return this;
            }

            public Boolean getStatus(){
                return this.status;
            }

            public PixalatePrebidSetting setValue(Integer value){
                this.value = value;
                return this;
            }

            public Integer getValue(){
                return this.value;
            }

        }

    }

}
