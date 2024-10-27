package data;

public class SellersAdsEnums{

    public enum sellerTypeEnum{
        PUBLISHER,
        INTERMEDIARY,
        BOTH
    }

    public enum sellerConfidentialityEnum{
        PUBLIC,
        PRIVATE;

        public String publicName(){
            return switch (this){
                case PUBLIC -> "Not confidential";
                case PRIVATE -> "Is confidential";
            };
        }
    }

}
