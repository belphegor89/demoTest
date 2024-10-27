package data.dataobject;

import common.utils.FileUtility;

import java.util.HashMap;
import java.util.Map;

public class CompanyDO{
    private String companyName, domain, sellerId, syncUrl, iframeTag, cookieSyncUrl = new FileUtility().getCookieSyncUrl() + "/[a-z0-9]{32}.gif\\?puid=\\[UID\\]&redir=\\[RED\\]&gdpr=\\[GDPR\\]&gdpr_consent=\\[GDPR_CONSENT\\]&coppa=\\[COPPA\\]&gpp=\\[GPP\\]&gpp_sid=\\[GPP_SID\\]";
    private boolean companyStatus = false, cookieSyncStatus = false, cookieSyncStoragePlatform = false;
    private int companyId, userSyncRequests = 0, uniqueSyncedUserIds = 0;
    Map<String, Boolean> usersWithinCompany = new HashMap<>();

    public CompanyDO(){}

    public CompanyDO(String companyName){
        this.companyName = companyName;
    }

    public CompanyDO(String companyName, String domain, String sellerId){
        this.companyName = companyName;
        this.domain = domain;
        this.sellerId = sellerId;
    }

    public String getCompanyName(){
        return companyName;
    }

    public CompanyDO setCompanyName(String companyName){
        this.companyName = companyName;
        return this;
    }

    public String getDomain(){
        return domain;
    }

    public CompanyDO setDomain(String domain){
        this.domain = domain;
        return this;
    }

    public String getSellerId(){
        return sellerId;
    }

    public CompanyDO setSellerId(String sellerId){
        this.sellerId = sellerId;
        return this;
    }

    public String getCookieSyncUrl(){
        return cookieSyncUrl;
    }

    public CompanyDO setCookieSyncUrl(String cookieSyncUrl){
        this.cookieSyncUrl = cookieSyncUrl;
        return this;
    }

    public String getSyncUrl(){
        return syncUrl;
    }

    public CompanyDO setSyncUrl(String syncUrl){
        this.syncUrl = syncUrl;
        return this;
    }

    public String getIframeTag(){
        return iframeTag;
    }

    public CompanyDO setIframeTag(String iframeTag){
        this.iframeTag = iframeTag;
        return this;
    }

    public boolean getCompanyStatus(){
        return companyStatus;
    }

    public CompanyDO setCompanyStatus(boolean companyStatus){
        this.companyStatus = companyStatus;
        return this;
    }

    public boolean getCookieSyncStatus(){
        return cookieSyncStatus;
    }

    public CompanyDO setCookieSyncStatus(boolean cookieSyncStatus){
        this.cookieSyncStatus = cookieSyncStatus;
        return this;
    }

    public boolean getCookieSyncStoragePlatform(){
        return cookieSyncStoragePlatform;
    }

    public CompanyDO setCookieSyncStoragePlatform(boolean cookieSyncStoragePlatform){
        this.cookieSyncStoragePlatform = cookieSyncStoragePlatform;
        return this;
    }

    public Integer getCompanyId(){
        return companyId;
    }

    public CompanyDO setCompanyId(int companyId){
        this.companyId = companyId;
        return this;
    }

    public Integer getUserSyncRequests(){
        return userSyncRequests;
    }

    public CompanyDO setUserSyncRequests(int userSyncRequests){
        this.userSyncRequests = userSyncRequests;
        return this;
    }

    public Integer getUniqueSyncedUserIds(){
        return uniqueSyncedUserIds;
    }

    public CompanyDO setUniqueSyncedUserIds(int uniqueSyncedUserIds){
        this.uniqueSyncedUserIds = uniqueSyncedUserIds;
        return this;
    }

    public Map<String, Boolean> getUsersMap(){
        return usersWithinCompany;
    }

    public CompanyDO setUsersMap(Map<String, Boolean> users){
        this.usersWithinCompany = users;
        return this;
    }

}
