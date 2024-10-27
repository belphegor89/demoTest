package data.dataobject;

import data.CommonEnums;
import data.SellersAdsEnums;

public class SellerJsonDO{
    private Integer id;
    private String userType;
    private String sellerId, sellerName, domain;
    private SellersAdsEnums.sellerTypeEnum sellerType;
    private Boolean isConfidential, isHidden;

    public SellerJsonDO(){}

    public SellerJsonDO setUserType(String userType){
        this.userType = userType;
        return this;
    }

    public SellerJsonDO setSellerId(String sellerId){
        this.sellerId = sellerId;
        return this;
    }

    public SellerJsonDO setUserId(int id){
        this.id = id;
        return this;
    }

    public SellerJsonDO setSellerName(String sellerName){
        this.sellerName = sellerName;
        return this;
    }

    public SellerJsonDO setDomain(String domain){
        this.domain = domain;
        return this;
    }

    public SellerJsonDO setSellerType(SellersAdsEnums.sellerTypeEnum sellerType){
        this.sellerType = sellerType;
        return this;
    }

    public SellerJsonDO setIsConfidential(Boolean isConfidential){
        this.isConfidential = isConfidential;
        return this;
    }

    public SellerJsonDO setIsHidden(Boolean isHidden){
        this.isHidden = isHidden;
        return this;
    }

    public String getUserType(){
        return userType;
    }

    public String getSellerId(){
        return sellerId;
    }

    public Integer getUserId(){
        return id;
    }

    public String getSellerName(){
        return sellerName;
    }

    public String getDomain(){
        return domain;
    }

    public SellersAdsEnums.sellerTypeEnum getSellerType(){
        return sellerType;
    }

    public CommonEnums.directnessTypeEnum getDirectnessType(){
        CommonEnums.directnessTypeEnum directnessType = null;
        switch (sellerType){
            case PUBLISHER -> directnessType = CommonEnums.directnessTypeEnum.DIRECT;
            case INTERMEDIARY -> directnessType = CommonEnums.directnessTypeEnum.RESELLER;
            case BOTH -> directnessType = CommonEnums.directnessTypeEnum.BOTH;
        }
        return directnessType;
    }

    public Boolean getIsConfidential(){
        return isConfidential;
    }

    public Boolean getIsHidden(){
        return isHidden;
    }

}
