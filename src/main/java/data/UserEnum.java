package data;

public class UserEnum {

    public enum userRoleEnum{
        ADM,
        PUBL,
        PUBL_SSP,
        PUBL_DSP;

        public String publicName(){
            return switch (this){
                case ADM -> "Admin";
                case PUBL -> "Publisher";
                case PUBL_SSP -> "SSP";
                case PUBL_DSP -> "DSP";
            };
        }
    }

    public enum additionalSettings{
        FRAUD,
        SUBSCRIBE,
        PREMIUM,
        MARGIN,
        ADS;

        public String attributeName(){
            return switch (this){
                case FRAUD -> "fraud";
                case SUBSCRIBE -> "subscribe";
                case PREMIUM -> "dirty_traffic";
                case MARGIN -> "dynamic_margin";
                case ADS -> "setting[is_direct_ads_txt]";
            };
        }
    }

}
