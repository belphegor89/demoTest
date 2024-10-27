package suites;

import common.utils.FileUtility;
import common.utils.RandomUtility;
import common.utils.SystemUtility;
import data.CommonEnums.regionsEnum;
import data.ScannersEnums.pixalatePrebidTypesEnum;
import data.ScannersEnums.scannerTypesEnum;
import data.StaticData;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.ScannersPO;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static common.UserSettings.resourcesFolder;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("Supply & Demand Scanners")
public class Scanners extends BaseSuiteClassNew {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("957")
    @Description("Set up Pixalate pre-bid scanner")
    public void setupPixalatePrebid() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        String ftp = "test.rebex.net", user = "demo", pass = "password";
        Map<pixalatePrebidTypesEnum, Object> pixalatePrebid = new HashMap<>() {{
            put(pixalatePrebidTypesEnum.DEVICE_ID, 12);
            put(pixalatePrebidTypesEnum.BUNDLES, 28);
            put(pixalatePrebidTypesEnum.IPV4, 56);
            put(pixalatePrebidTypesEnum.IPV6, 9);
            put(pixalatePrebidTypesEnum.OTT, 34);
            put(pixalatePrebidTypesEnum.USER_AGENT, null);
            put(pixalatePrebidTypesEnum.DOMAINS, null);
            put(pixalatePrebidTypesEnum.DATA_CENTER, null);
            put(pixalatePrebidTypesEnum.DEFASED_APP, null);
        }};
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PIXALATE_PREBID);
        scanners.toggleScanner(scannerTypesEnum.PIXALATE_PREBID, true);
        scanners.pixalatePrebidInputPersonal(ftp, user, pass);
        scanners.pixalatePrebidSetupScans(pixalatePrebid);
        scanners.clickSaveScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PIXALATE_PREBID);
        scanners.assertPixalatePrebid(true, ftp, user, pass, pixalatePrebid);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("962")
    @Description("Set up Pixalate post-bid scanner")
    public void setupPixalatePostbid() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        int dailyLimit = new RandomUtility().getRandomInt(1000, 150000001), monthlyLimit = new RandomUtility().getRandomInt(1000, 150000001);
        String clid = "clid123", paid = "paid567";
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PIXALATE_POSTBID);
        scanners.toggleScanner(scannerTypesEnum.PIXALATE_POSTBID, true);
        scanners.pixalatePostbidSetup(clid, paid, true, dailyLimit, true, monthlyLimit);
        scanners.clickSaveScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PIXALATE_POSTBID);
        scanners.assertPixalatePostbid(true, clid, paid, true, dailyLimit, true, monthlyLimit);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("966")
    @Description("Set up Protected Media pre-bid scanner")
    public void setupProtectedMediaPrebid() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        int port = new RandomUtility().getRandomInt(1, 65536);
        String cpid = "1346777", ckey = "8fff6273915738a25bb4dcf2e5f0defd", host = "test.host.sm";
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PROTECTED_PREBID);
        scanners.toggleScanner(scannerTypesEnum.PROTECTED_PREBID, true);
        scanners.protectedPrebidSetup(cpid, ckey, host, port);
        scanners.clickSaveScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PROTECTED_PREBID);
        scanners.assertProtectedPrebid(true, cpid, ckey, host, port);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("967")
    @Description("Set up Protected Media post-bid scanner")
    public void setupProtectedMediaPostbid() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        String accountId = "8567567";
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PROTECTED_POSTBID);
        scanners.toggleScanner(scannerTypesEnum.PROTECTED_POSTBID, true);
        scanners.protectedPostbidSetup(accountId);
        scanners.clickSaveScanners();
        scanners.clickScannerWrap(scannerTypesEnum.PROTECTED_POSTBID);
        scanners.assertProtectedPostbid(true, accountId);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("961")
    @Description("Set up WhiteOps Media Guard scanner")
    public void setupWhiteopsMediaGuard() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        RandomUtility randomUtil = new RandomUtility();
        FileUtility fileUtil = new FileUtility();
        regionsEnum region = (regionsEnum) randomUtil.getRandomElement(scanners.getRegions());
        int monthlyLimit = randomUtil.getRandomInt(1000, 150000001);
        String dns = "smartyads.com",
                sslCert = fileUtil.readFileToString(new File(SystemUtility.getPathCanonical(resourcesFolder) + "/cert.cert")).trim(),
                sslKey = fileUtil.readFileToString(new File(SystemUtility.getPathCanonical(resourcesFolder) + "/key.key")).trim();
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.clickScannerWrap(scannerTypesEnum.WHITEOPS_MEDIA_GUARD);
        scanners.toggleScanner(scannerTypesEnum.WHITEOPS_MEDIA_GUARD, true);
        scanners.whiteopsMediaSetup(region, dns, sslCert, sslKey, true, monthlyLimit);
        scanners.clickSaveScanners();
        scanners.clickScannerWrap(scannerTypesEnum.WHITEOPS_MEDIA_GUARD);
        scanners.assertWhiteopsMedia(true, region, dns, sslCert, sslKey, true, monthlyLimit);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("3906")
    @Description("Set up WhiteOps Fraud Sensor scanner")
    public void setupWhiteopsFraudSensor() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        int dailyLimit = new RandomUtility().getRandomInt(1000, 150000001), monthlyLimit = new RandomUtility().getRandomInt(1000, 150000001);
        String accountId = "2345234";
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.clickScannerWrap(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR);
        scanners.toggleScanner(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR, true);
        scanners.whiteopsFraudSetup(accountId, true, dailyLimit, true, monthlyLimit);
        scanners.clickSaveScanners();
        scanners.clickScannerWrap(scannerTypesEnum.WHITEOPS_FRAUD_SENSOR);
        scanners.assertWhiteopsFraud(true, accountId, true, dailyLimit, true, monthlyLimit);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("968")
    @Description("Set up Geoedge scanner")
    public void setupGeoedgeScanner() {
        AuthorizationPO login = new AuthorizationPO();
        ScannersPO scanners = new ScannersPO();
        String header = "<div id=\"grumi-container\"><script type=\"text/javascript\" nonce=\"!headerNonce!\">!function(d,i){var p={wver:\"1.1.5\",wtype:\"dfp\",key:\"5005fc41-389d-41f8-876a-6d6165fe0a4a\",meta:{adup:\"%%ADUNIT%%\",dest:\"%%DEST_URL_ESC%%\",w:\"%%WIDTH%%\",h:\"%%HEIGHT%%\",li:\"%eaid!\",adv:\"%eadv!\",ord:\"%ebuy!\",cr:\"%ecid!\",ygIds:\"%_ygIds!\",aduid:\"%epid!\",haduid:\"%esid!\",isAfc:\"%_isAfc!\",isAmp:\"%_isAmp!\",qid:\"%qid!\",cust_imp:\"%cust_imp!\",cust1:\"%cust1!\",cust2:\"%cust2!\",cust3:\"%cust3!\",caid:\"%caid!\",di:\"%DEMAND_ID!\",dn:\"%DEMAND_NAME!\",dcid:\"%DEMAND_CREATIVE_ID!\",pid:\"%PUBLISHER_ID!\",pn:\"%PUBLISHER_NAME!\",adElId:\"%_adElId!\"},sp:\"dfp\",cfg:{},pbAdId:\"%%PATTERN:hb_adid%%\",pbAdIdAst:\"%%PATTERN:hb_adid_appnexusAst%%\",pbBidder:\"%%PATTERN:hb_bidder%%\",hbPb:\"%%PATTERN:hb_pb%%\",hbCid:\"%_hbcid!\",site:\"%%SITE%%\",pimp:\"%_pimp%\"};window.grumi=p}();</script><template style=\"display: none;\" id=\"template0\"><xmp style=\"display: none;\" id=\"xmp0\">",
                footer = "</xmp></template><script type=\"text/javascript\" nonce=\"!footerNonce!\">!function(t){var o=window.grumi.key,r=window.grumi,e=r&&r.wtype&&\"gpt\"===r.wtype,n=window.onerror,i=1*new Date,a=navigator.userAgent&&navigator.userAgent.match(/\\b(MSIE |Trident.*?rv:|Edge\\/)(\\d+)/),w=e&&!a;function d(){var e=function(){for(var e,n=document.getElementsByTagName(\"template\"),t=n.length-1;0<=t;t--)if(\"template0\"===n[t].id){e=n[t];break}return e}();return e.content?e.content.getElementById?e.content.getElementById(\"xmp0\"):e.content.childNodes[0]:e.getElementsByTagName(\"xmp\")[0]}function u(){var e=d();return e&&e.innerHTML}function c(e,n){n=n||!1,top.postMessage&&top.postMessage({evType:e||\"\",key:r.key,adup:r.meta.adup,html:window.grumi?window.grumi.tag:\"\",el:r.meta.adElId,refresh:n},\"*\")}var m=!1;function g(e,n){if(!m){m=!0;var t=\"\",o=a&&\"complete\"===document.readyState;window.grumi&&(window.grumi.fsRan=!0,t=window.grumi.tag),o||(t||(t=u()),w&&window.document.open(),window.document.write(t),window.document.close()),((n=n||!1)||o)&&c(e,o)}}function l(e,t){return function(){var n=setTimeout(function(){var e=document.getElementById(i);e&&null===function(e){if(void 0!==e.nextElementSibling)return e.nextElementSibling;for(var n=e.nextSibling;n&&1!==n.nodeType;)n=n.nextSibling;return n}(e)&&t&&t(),clearTimeout(n)},e)}}l(5e3,function(){g()})(),l(2e3,function(){c(\"slwCl\")})(),window.grumi.tag=u(),window.grumi.scriptHost=t,window.grumi.pbGlobal=window.grumi.cfg&&window.grumi.cfg.pbGlobal||\"pbjs\",window.grumi.onerror=n,window.parent&&window.parent.postMessage&&window.parent.postMessage({iw:!0,key:r.key,adup:r.meta.adup,el:r.meta.adElId},\"*\"),function(){var e=document.createElement(\"script\");e.type=\"text/javascript\",e.src=t+o+\"/grumi.js\",e.className=\"rm\",e.id=i,w&&(e.async=!0);var n=\"_\"+1*new Date;window[n]=function(){g(\"netErr\",!0)},window.grumi.start=1*new Date;try{window.document.write(e.outerHTML.replace(&#039;class=\"rm\"&#039;,&#039;onerror=\"&#039;+n+&#039;();\"&#039;))}catch(e){g()}}(),window.onerror=function(e){\"function\"==typeof n&&n.apply(this,arguments),l(0,g)(),window.onerror=n}}((\"http\"===window.location.protocol.substr(0,4)?window.location.protocol:\"https:\")+\"//rumcdn.geoedge.be/\");</script></div>";
        login.login(StaticData.supportDefaultUser);
        scanners.gotoScanners();
        scanners.clickScannerWrap(scannerTypesEnum.GEOEDGE);
        scanners.toggleScanner(scannerTypesEnum.GEOEDGE, true);
        scanners.geoedgeSetup(header, footer);
        scanners.clickSaveScanners();
        scanners.clickScannerWrap(scannerTypesEnum.GEOEDGE);
        scanners.assertGeoedge(true, header, footer);
    }


}
