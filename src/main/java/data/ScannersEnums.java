package data;

public class ScannersEnums{
    public enum scannerTypesEnum{
        PIXALATE_PREBID,
        PIXALATE_POSTBID,
        PROTECTED_PREBID,
        PROTECTED_POSTBID,
        WHITEOPS_MEDIA_GUARD,
        WHITEOPS_FRAUD_SENSOR,
        GEOEDGE;

        public String attributeName(){
            return switch (this){
                case PIXALATE_PREBID -> "pixalate-prebid";
                case PIXALATE_POSTBID -> "pixalate-postbid";
                case PROTECTED_PREBID -> "protected-prebid";
                case PROTECTED_POSTBID -> "protected-postbid";
                case WHITEOPS_MEDIA_GUARD -> "whiteops-media-guard";
                case WHITEOPS_FRAUD_SENSOR -> "whiteops-fraud-sensor";
                case GEOEDGE -> "geoedge";
            };
        }

        public String publicName(){
            return switch (this){
                case PIXALATE_PREBID -> "Pixalate pre-bid scanner";
                case PIXALATE_POSTBID -> "Pixalate post-bid scanner";
                case PROTECTED_PREBID -> "Protected media";
                case PROTECTED_POSTBID -> "Protected Media post bid scanner";
                case WHITEOPS_MEDIA_GUARD -> "WhiteOps Media Guard";
                case WHITEOPS_FRAUD_SENSOR -> "WhiteOps Fraud Sensor";
                case GEOEDGE -> "GeoEdge scanner";
            };
        }
    }

    public enum pixalatePrebidTypesEnum{
        DEVICE_ID,
        BUNDLES,
        IPV4,
        IPV6,
        OTT,
        USER_AGENT,
        DOMAINS,
        DEFASED_APP,
        DATA_CENTER;

        public String publicName(){
            return switch (this){
                case DEVICE_ID -> "Device IDs";
                case BUNDLES -> "Bundles";
                case IPV4 -> "IPv4 addresses";
                case IPV6 -> "IPv6 addresses";
                case OTT -> "OTT";
                case USER_AGENT -> "User-Agent";
                case DOMAINS -> "Domains";
                case DEFASED_APP -> "Defased Apps";
                case DATA_CENTER -> "Data Center";
            };
        }

        public String attributeName(){
            return switch (this){
                case DEVICE_ID -> "device_id";
                case BUNDLES -> "bundles";
                case IPV4 -> "ipv4";
                case IPV6 -> "ipv6";
                case OTT -> "ott";
                case USER_AGENT -> "user_agent";
                case DOMAINS -> "domains";
                case DEFASED_APP -> "defased_app";
                case DATA_CENTER -> "data_center";
            };
        }
    }

}
