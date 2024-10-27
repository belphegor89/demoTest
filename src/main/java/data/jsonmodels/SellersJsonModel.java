package data.jsonmodels;

import java.util.List;

public class SellersJsonModel{
    private String version;
    private String contact_email;
    private String last_update;
    private List<Seller> sellers;

    public String getVersion(){
        return version;
    }

    public String getContact_email(){
        return contact_email;
    }

    public String getLast_update(){
        return last_update;
    }

    public List<Seller> getSellers(){
        return sellers;
    }

    public static class Seller{
        private String seller_id;
        private int is_confidential;
        private String name;
        private String domain;
        private String seller_type;

        public String getSeller_id(){
            return seller_id;
        }

        public int getIs_confidential(){
            return is_confidential;
        }

        public String getName(){
            return name;
        }

        public String getDomain(){
            return domain;
        }

        public String getSeller_type(){
            return seller_type;
        }

        public void setSeller_id(String seller_id){
            this.seller_id = seller_id;
        }

    }

}
