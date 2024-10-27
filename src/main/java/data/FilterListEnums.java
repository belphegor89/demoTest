package data;

public class FilterListEnums{
    public enum recordType{
        DOMAIN_BUNDLE,
        PUB_ID,
        SITE_APP_ID,
        CRID,
        ADOMAINS;

        public String publicName(){
            return switch (this){
                case DOMAIN_BUNDLE -> "Domain/Bundle";
                case PUB_ID -> "Publisher ID";
                case SITE_APP_ID -> "Site/App ID";
                case CRID -> "CRID";
                case ADOMAINS -> "ADomains";
            };
        }
    }

    public enum tableColumnsEnum{
        FILTER_NAME,
        LIST_TYPE,
        RECORD_TYPE,
        CREATED_DATE,
        UPDATED_DATE,
        CONTROL;

        public String publicName(){
            return switch (this){
                case FILTER_NAME -> "Filter Name";
                case RECORD_TYPE -> "Record Type";
                case LIST_TYPE -> "List type";
                case CREATED_DATE -> "Created";
                case UPDATED_DATE -> "Updated";
                case CONTROL -> "Control";
            };
        }

        public String attributeName(){
            return switch (this){
                case FILTER_NAME -> "name";
                case RECORD_TYPE -> "record_type";
                case LIST_TYPE -> "type";
                case CREATED_DATE -> "create_at";
                case UPDATED_DATE -> "updated_at";
                case CONTROL -> "action";
            };
        }
    }


}
