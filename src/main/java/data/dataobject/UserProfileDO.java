package data.dataobject;

import common.utils.BasicUtility;
import data.UserEnum;
import data.UserEnum.userRoleEnum;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UserProfileDO {
    private final BasicUtility Util = new BasicUtility();
    private String firstName, lastName, email, password, company, emailAdditional = "", skype, comments = "", country = "United States of America", sellerId = "", businessDomain = "";
    private userRoleEnum role;
    private Boolean isDeleted = false, isActivated = false, trafficStatus = true, loginAsUser = null;
    private Integer userId, loginCount, inventoriesCount = 0, placementsCount = 0;
    private Double balance = 0d, revenue = 0d, dynamicMargin = null;
    private LocalDateTime createDate, updateDate, deleteDate, lastLoginDate = null;

    private Map<UserEnum.additionalSettings, Boolean> settings = new HashMap<>(){{
        put(UserEnum.additionalSettings.ADS, false);
        put(UserEnum.additionalSettings.SUBSCRIBE, false);
        put(UserEnum.additionalSettings.PREMIUM, false);
        put(UserEnum.additionalSettings.MARGIN, false);
    }};

    public UserProfileDO(){}

    public UserProfileDO(userRoleEnum role, String firstName, String lastName, String email, String password, String company, String country, String emailAdditional, String skype, String comments){
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.country = country;
        this.emailAdditional = emailAdditional;
        this.skype = skype;
        this.comments = comments;
    }

    public UserProfileDO(userRoleEnum role, String firstName, String lastName, String email, String password, String company, String emailAdditional, String skype, String comments){
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
        this.emailAdditional = emailAdditional;
        this.skype = skype;
        this.comments = comments;
    }

    public UserProfileDO(userRoleEnum role, String firstName, String lastName, String email, String password, String company){
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.company = company;
    }

    //<editor-fold desc="Main info">
    public UserProfileDO setEmail(String email){
        this.email = email;
        return this;
    }

    public String getEmail(){
        return email;
    }

    public UserProfileDO setPassword(String password){
        this.password = password;
        return this;
    }

    public String getPassword(){
        return password;
    }

    public UserProfileDO setFirstName(String name){
        this.firstName = name;
        return this;
    }

    public String getFirstName(){
        return firstName;
    }

    public UserProfileDO setLastName(String name){
        this.lastName = name;
        return this;
    }

    public String getLastName(){
        return lastName;
    }

    public String getFullName(){
        return this.firstName + " " + this.lastName;
    }

    public UserProfileDO setRole(userRoleEnum role){
        this.role = role;
        return this;
    }

    public userRoleEnum getRole(){
        return role;
    }

    public UserProfileDO setUserId(Integer userId){
        this.userId = userId;
        return this;
    }

    public Integer getUserId(){
        return userId;
    }

    public UserProfileDO setCompany(String company){
        this.company = company;
        return this;
    }

    public String getCompany(){
        return company;
    }
    //</editor-fold>

    //<editor-fold desc="Profile counts">

    public UserProfileDO setLoginCount(Integer loginCount){
        this.loginCount = loginCount;
        return this;
    }

    public Integer getLoginCount(){
        return loginCount;
    }

    public UserProfileDO setInventoriesCount(Integer inventoriesCount){
        this.inventoriesCount = inventoriesCount;
        return this;
    }

    public Integer getInventoriesCount(){
        return inventoriesCount;
    }

    public UserProfileDO setPlacementsCount(Integer placementsCount){
        this.placementsCount = placementsCount;
        return this;
    }

    public Integer getPlacementsCount(){
        return placementsCount;
    }

    public UserProfileDO setBalance(Double balance){
        this.balance = balance;
        return this;
    }

    public Double getBalance(){
        return balance;
    }

    public UserProfileDO setRevenue(Double revenue) {
        this.revenue = revenue;
        return this;
    }

    public Double getRevenue() {
        return revenue;
    }
    //</editor-fold>

    //<editor-fold desc="Additional info">
    public UserProfileDO setCountry(String country){
        this.country = country;
        return this;
    }

    public String getCountry(){
        return country;
    }

    public UserProfileDO setSellerId(String sellerId){
        this.sellerId = sellerId;
        return this;
    }

    public String getSellerId(){
        return sellerId;
    }

    public UserProfileDO setBusinessDomain(String businessDomain){
        this.businessDomain = businessDomain;
        return this;
    }

    public String getBusinessDomain(){
        return businessDomain;
    }

    public String getSkype(){
        return skype;
    }

    public String getEmailAdditional(){
        return emailAdditional;
    }

    public String getComments(){
        return comments;
    }

    public UserProfileDO setDynamicMargin(Double margin){
        this.dynamicMargin = margin;
        return this;
    }

    public Double getDynamicMargin(){
        return dynamicMargin;
    }

    public UserProfileDO setUserAdditionalSettings(UserEnum.additionalSettings setting, Boolean value){
        this.settings.replace(setting, value);
        return this;
    }

    public Map<UserEnum.additionalSettings, Boolean> getAdditionalSettings(){
        Map<UserEnum.additionalSettings, Boolean> returnMap = new HashMap<>(settings);
        switch (this.role){
            case PUBL_SSP -> {
                returnMap.remove(UserEnum.additionalSettings.PREMIUM);
                returnMap.remove(UserEnum.additionalSettings.MARGIN);
            }
            case PUBL_DSP -> {
                returnMap.remove(UserEnum.additionalSettings.ADS);
                returnMap.remove(UserEnum.additionalSettings.PREMIUM);
                returnMap.remove(UserEnum.additionalSettings.MARGIN);
            }
            case PUBL -> {
                returnMap.remove(UserEnum.additionalSettings.ADS);
            }
            case ADM -> {
                returnMap.remove(UserEnum.additionalSettings.PREMIUM);
                returnMap.remove(UserEnum.additionalSettings.MARGIN);
                returnMap.remove(UserEnum.additionalSettings.ADS);
            }
        }
        return returnMap;
    }

    public Map<String, Boolean> getAdditionalSettingsAttributes(){
        Map<String, Boolean> returnMap = new HashMap<>();
        UserEnum.userRoleEnum role = this.role;
        switch (role){
            case PUBL_SSP -> {
                returnMap.put(UserEnum.additionalSettings.ADS.attributeName(), settings.get(UserEnum.additionalSettings.ADS));
                returnMap.put(UserEnum.additionalSettings.SUBSCRIBE.attributeName(), settings.get(UserEnum.additionalSettings.SUBSCRIBE));
            }
            case PUBL_DSP -> {
                returnMap.put(UserEnum.additionalSettings.SUBSCRIBE.attributeName(), settings.get(UserEnum.additionalSettings.SUBSCRIBE));
            }
            case PUBL -> {
                returnMap.put(UserEnum.additionalSettings.PREMIUM.attributeName(), settings.get(UserEnum.additionalSettings.PREMIUM));
                returnMap.put(UserEnum.additionalSettings.SUBSCRIBE.attributeName(), settings.get(UserEnum.additionalSettings.SUBSCRIBE));
                returnMap.put(UserEnum.additionalSettings.MARGIN.attributeName(), settings.get(UserEnum.additionalSettings.MARGIN));
            }
            case ADM ->
                    returnMap.put(UserEnum.additionalSettings.SUBSCRIBE.attributeName(), settings.get(UserEnum.additionalSettings.SUBSCRIBE));
        }
        return returnMap;
    }
    //</editor-fold>

    //<editor-fold desc="Dates">
    public UserProfileDO setCreateDate(LocalDateTime dateTime){
        this.createDate = dateTime;
        return this;
    }

    public String getCreateDate(String format, Locale locale){
        return Util.getDateTimeFormatted(createDate, format, locale);
    }

    public String getCreateDate(){
        return Util.getDateTimeFormatted(createDate, "yyyy-MM-dd", Locale.ENGLISH);
    }

    public UserProfileDO setUpdateDate(LocalDateTime dateTime){
        this.updateDate = dateTime;
        return this;
    }

    public String getUpdateDate(String format, Locale locale){
        return Util.getDateTimeFormatted(updateDate, format, locale);
    }

    public UserProfileDO setDeleteDate(LocalDateTime dateTime){
        this.deleteDate = dateTime;
        return this;
    }

    public String getDeleteDate(String format, Locale locale){
        return Util.getDateTimeFormatted(deleteDate, format, locale);
    }

    public String getDeleteDate(){
        return Util.getDateTimeFormatted(deleteDate, "yyyy-MM-dd", Locale.ENGLISH);
    }

    public UserProfileDO setLastLoginDate(LocalDateTime dateTime){
        this.lastLoginDate = dateTime;
        return this;
    }

    public String getLastLoginDate(String format, Locale locale){
        return Util.getDateTimeFormatted(lastLoginDate, format, locale);
    }

    public String getLastLoginDate(){
        if (lastLoginDate == null){
            return null;
        }
        return Util.getDateTimeFormatted(lastLoginDate, "yyyy-MM-dd", Locale.ENGLISH);
    }
    //</editor-fold>

    public UserProfileDO setDeleted(Boolean isDeleted){
        this.isDeleted = isDeleted;
        return this;
    }

    public Boolean getDeleted(){
        return isDeleted;
    }

    public UserProfileDO setActivated(Boolean isActivated){
        this.isActivated = isActivated;
        return this;
    }

    public Boolean getActivated(){
        return isActivated;
    }

    public UserProfileDO setTrafficStatus(Boolean trafficStatus){
        this.trafficStatus = trafficStatus;
        return this;
    }

    public Boolean getTrafficStatus(){
        return trafficStatus;
    }

    public UserProfileDO setLoginAsUser(Boolean loginAsUser){
        this.loginAsUser = loginAsUser;
        return this;
    }

    public Boolean getLoginAsUser(){
        return loginAsUser;
    }

}
