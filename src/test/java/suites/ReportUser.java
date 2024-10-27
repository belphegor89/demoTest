package suites;

import common.SoftAssertCustom;
import common.utils.BasicUtility;
import common.utils.RandomUtility;
import data.CommonEnums;
import data.ReportEnum;
import data.ReportEnum.aggregateByEnum;
import data.ReportEnum.attributesEnum;
import data.ReportEnum.metricsEnum;
import data.ReportEnum.reportTypeEnum;
import data.UserEnum;
import data.dataobject.ReportDO;
import data.textstrings.messages.ReportText;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.ReportPO;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static data.StaticData.supportDefaultUser;

@Epic("User reports")
public class ReportUser extends BaseSuiteClassNew {

    //<editor-fold desc="Admin/Publisher">
    @Test
    @TmsLinks({@TmsLink("170"), @TmsLink("4327")})
    @Severity(SeverityLevel.CRITICAL)
    @Description("User Publisher/Admin can generate report with any metrics and attributes and download csv file")
    public void generateAndExportReportPublisher() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        File csvExport;
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_PUBLISHER).addAttributeToMap(attributesEnum.ALL, true).addAttributeToMap(attributesEnum.DEAL_PUBLISHER, false).addAttributeToMap(attributesEnum.DEAL_PUBLISHER_HASH, false).addMetricToMap(metricsEnum.ALL, true).addMetricToMap(metricsEnum.DEAL_SSP_REQUESTS, false).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(aggregateByEnum.MONTH);
        login.login(supportDefaultUser);
        report.gotoUserReportSection();
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        csvExport = report.clickExportCSV(reportData.getReportType());
        report.assertReportExportColumns(reportData, csvExport);
        sAssert.assertAll("Errors with Publisher User Report:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("4310")
    @Description("Preset CRUD - Admin/Publisher")
    public void presetCrudPublisher() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        RandomUtility randomUtil = new RandomUtility();
        Map<attributesEnum, Boolean> attrMap1 = new HashMap<>() {{
            put(ReportEnum.attributesEnum.COUNTRY, true);
        }}, attrMap2 = new HashMap<>() {{
            put(ReportEnum.attributesEnum.COUNTRY, true);
            put(attributesEnum.AD_FORMAT, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap1 = new HashMap<>() {{
            put(ReportEnum.metricsEnum.CLICKS, true);
        }}, metricsMap2 = new HashMap<>() {{
            put(ReportEnum.metricsEnum.CLICKS, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.CTR, true);
        }};
        ReportDO preset1 = new ReportDO(reportTypeEnum.USER_PUBLISHER).setAttrMap(attrMap1).setMetricsMap(metricsMap1).setPresetName("pubUserTestPreset_1_" + randomUtil.getRandomInt(1, 10000)), preset2 = new ReportDO(attrMap2, metricsMap2, ReportEnum.aggregateByEnum.MONTH, CommonEnums.calendarRangeEnum.LAST_7_DAYS, "(UTC +01:00) Madrid").setPresetName("pubUserTestPreset_2_" + randomUtil.getRandomInt(1, 10000)).setReportType(reportTypeEnum.USER_PUBLISHER);
        login.login(supportDefaultUser);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL);
        report.selectAttribute(preset1);
        report.clickAddPreset();
        report.clickSavePresetWithErrors();
        report.assertPresetModalError(ReportText.PRESET_ERROR_NO_NAME);
        report.inputPresetName(preset1.getPresetName());
        report.clickSavePresetWithErrors();
        report.assertPresetModalError(ReportText.PRESET_ERROR_NO_METRIC);
        report.clickCancelPreset();
        report.selectMetric(preset1.getMetricsMap());
        report.clickAddPreset();
        report.inputPresetName(preset1.getPresetName());
        report.clickSavePreset();
        report.refreshBuilderPage();
        report.selectPreset(preset1.getPresetName());
        report.assertBuilderPage(preset1);
        report.renamePreset(preset1.getPresetName(), preset2.getPresetName());
        report.setReportData(preset2);
        report.clickUpdatePreset();
        report.refreshBuilderPage();
        report.selectPreset(preset2.getPresetName());
        report.assertBuilderPage(preset2);
        report.deletePreset(preset2.getPresetName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("169")
    @Description("Publisher User can't generate report without metrics")
    public void validationNoMetricsPublisher() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.AD_FORMAT, true);
        }};
        login.login(supportDefaultUser);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL);
        report.clickGenerateReportWithError();
        report.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
        report.modalClickConfirm();
        report.selectAttribute(attrMap);
        report.clickGenerateReportWithError();
        report.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("182")
    @Description("Publisher User can generate reports only with metrics")
    public void generateOnlyMetricsPublisher() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_PUBLISHER).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        login.login(supportDefaultUser);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        sAssert.assertAll("Errors on generated report page");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58216")
    @Description("Publisher User is able to generate new report on report page")
    public void clickNewReportPublisher() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_PUBLISHER).addAttributeToMap(attributesEnum.AD_FORMAT, true).addMetricToMap(ReportEnum.metricsEnum.IMPRESSIONS, true).setAggregateBy(ReportEnum.aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LIFETIME), builderEmpty = new ReportDO(reportTypeEnum.USER_PUBLISHER);
        login.login(supportDefaultUser);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.clickGenerateNewReport();
        report.assertBuilderPage(builderEmpty);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58217")
    @Description("Publisher User can share generated report by Share report button")
    public void shareReportPublisher() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        String shareLink;
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.COUNTRY, true);
            put(attributesEnum.AD_FORMAT, true);
            put(attributesEnum.SIZE, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.DSP_WINS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_PUBLISHER).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.MONTH);
        login.login(supportDefaultUser);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        report.clickShareReport();
        shareLink = report.getReportShareLink();
        report.openShareLink(shareLink);
        report.assertSharedReport(reportData);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58218")
    @Description("Update created report settings")
    public void updateReportPublisher() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        BasicUtility util = new BasicUtility();
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.INVENTORY_PUBLISHER, true);
            put(attributesEnum.COUNTRY, true);
            put(attributesEnum.AD_FORMAT, true);
            put(attributesEnum.TRAFFIC_TYPE, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.DSP_WINS, true);
            put(metricsEnum.SSP_ECPM, true);
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_PUBLISHER).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        String dateValueByDay = util.getDateTimeFormatted(reportData.getDateTo(), "yyyy-MM-dd"), dateValueByMonth;
        login.login(supportDefaultUser);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertReportTableValue(ReportEnum.attributesEnum.DATE.attributeName(), 0, dateValueByDay);
        report.assertGeneratedReport(reportData);
        report.selectAggregatedByReport(ReportEnum.aggregateByEnum.MONTH);
        report.clickUpdateReport();
        reportData.setAggregateBy(ReportEnum.aggregateByEnum.MONTH);
        dateValueByMonth = util.getDateTimeFormatted(reportData.getDateTo(), "yyyy-MM");
        report.assertReportTableValue(ReportEnum.attributesEnum.DATE.attributeName(), 0, dateValueByMonth);
        report.assertGeneratedReport(reportData);
        report.deleteMetric(ReportEnum.metricsEnum.DSP_SPEND);
        report.deleteMetric(ReportEnum.metricsEnum.DSP_BID_RESPONSES);
        reportData.addMetricToMap(ReportEnum.metricsEnum.DSP_SPEND, false).addMetricToMap(ReportEnum.metricsEnum.DSP_BID_RESPONSES, false);
        report.clickUpdateReport();
        report.assertReportAddMetricList(ReportEnum.metricsEnum.DSP_SPEND);
        report.assertReportAddMetricList(ReportEnum.metricsEnum.DSP_BID_RESPONSES);
        report.assertGeneratedReport(reportData);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58219")
    @Description("Filters by chosen attributes are available to Publisher user on report page")
    public void reportFiltersPublisher() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(softAssert);
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.COUNTRY, true);
            put(attributesEnum.AD_FORMAT, true);
            put(attributesEnum.DOMAIN_BUNDLE, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.SSP_REQUESTS, true);
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_PUBLISHER).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.THIS_MONTH).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        String filterAdFormat, filterDomainBundle;
        login.login(supportDefaultUser);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL);
        report.setReportData(reportData);
        report.clickGenerateReport();
        filterAdFormat = report.getReportTableValue(ReportEnum.attributesEnum.AD_FORMAT.attributeName__old(), 0);
        filterDomainBundle = report.getReportTableValue(ReportEnum.attributesEnum.DOMAIN_BUNDLE.attributeName(), 0);
        reportData.setAttributeFilter(ReportEnum.attributesEnum.AD_FORMAT, filterAdFormat).setAttributeFilter(ReportEnum.attributesEnum.DOMAIN_BUNDLE, filterDomainBundle);
        report.setAttributeFilterGeneratedReport(reportData.getAttributeFilterMap(), reportData.getReportType());
        report.clickUpdateReport();
        report.assertReportTableValue(ReportEnum.attributesEnum.AD_FORMAT.attributeName(), filterAdFormat);
        report.assertReportTableValue(ReportEnum.attributesEnum.DOMAIN_BUNDLE.attributeName(), filterDomainBundle);
        report.assertGeneratedReport(reportData);
        softAssert.assertAll("Errors after setting filters in generated report:");
        report.clickBackToBuilder();
        report.setAttributeFilterBuilder(ReportEnum.attributesEnum.AD_FORMAT, filterAdFormat, reportData.getReportType());
        report.setAttributeFilterBuilder(ReportEnum.attributesEnum.DOMAIN_BUNDLE, filterDomainBundle, reportData.getReportType());
        report.clickGenerateReport();
        report.assertReportTableValue(ReportEnum.attributesEnum.AD_FORMAT.attributeName__old(), filterAdFormat);
        report.assertReportTableValue(ReportEnum.attributesEnum.DOMAIN_BUNDLE.attributeName__old(), filterDomainBundle);
        report.assertGeneratedReport(reportData);
        softAssert.assertAll("Errors after setting filters in report builder:");
    }

    //</editor-fold>

    //<editor-fold desc="SSP/DSP">
    @Test(priority = 1)//(groups = {"ssp"})
    @TmsLinks({@TmsLink("170"), @TmsLink("4326")})
    @Severity(SeverityLevel.CRITICAL)
    @Description("User SSP can generate report with any metrics and attributes and download csv file")
    public void generateAndExportReportSsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        File csvExport;
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_SSP).addAttributeToMap(attributesEnum.ALL, true).addMetricToMap(metricsEnum.ALL, true).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(aggregateByEnum.MONTH);
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_SSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_SSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        csvExport = report.clickExportCSV(reportData.getReportType());
        report.assertReportExportColumns(reportData, csvExport);
        sAssert.assertAll("Errors with Publisher User Report:");
    }

    @Test(dependsOnMethods = {"generateAndExportReportSsp"})//(groups = {"dsp"})
    @TmsLinks({@TmsLink("170"), @TmsLink("4325")})
    @Severity(SeverityLevel.CRITICAL)
    @Description("User DSP can generate report with any metrics and attributes and download csv file")
    public void generateAndExportReportDsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        File csvExport;
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_SSP).addAttributeToMap(attributesEnum.ALL, true).addMetricToMap(metricsEnum.ALL, true).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(aggregateByEnum.MONTH);
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_DSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_DSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        csvExport = report.clickExportCSV(reportData.getReportType());
        report.assertReportExportColumns(reportData, csvExport);
        sAssert.assertAll("Errors with Publisher User Report:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("4310")
    @Description("Preset CRUD - SSP/DSP")
    public void presetCrudSspDsp() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        RandomUtility randomUtil = new RandomUtility();
        Map<attributesEnum, Boolean> attrMap1 = new HashMap<>() {{
            put(attributesEnum.SSP_ENDPOINT, true);
        }}, attrMap2 = new HashMap<>() {{
            put(attributesEnum.SSP_ENDPOINT, true);
            put(ReportEnum.attributesEnum.OS, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap1 = new HashMap<>() {{
            put(ReportEnum.metricsEnum.CLICKS, true);
        }}, metricsMap2 = new HashMap<>() {{
            put(ReportEnum.metricsEnum.CLICKS, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.CTR, true);
        }};
        ReportDO preset1 = new ReportDO(reportTypeEnum.USER_SSP).setAttrMap(attrMap1).setMetricsMap(metricsMap1).setPresetName("sspDspUserTestPreset_1_" + randomUtil.getRandomInt(1, 10000)), preset2 = new ReportDO(attrMap2, metricsMap2, ReportEnum.aggregateByEnum.MONTH, CommonEnums.calendarRangeEnum.LAST_7_DAYS, "(UTC +01:00) Madrid").setPresetName("sspDspUserTestPreset_2_" + randomUtil.getRandomInt(1, 10000)).setReportType(reportTypeEnum.USER_SSP);
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_SSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_SSP);
        report.selectAttribute(preset1);
        report.clickAddPreset();
        report.clickSavePresetWithErrors();
        report.assertPresetModalError(ReportText.PRESET_ERROR_NO_NAME);
        report.inputPresetName(preset1.getPresetName());
        report.clickSavePresetWithErrors();
        report.assertPresetModalError(ReportText.PRESET_ERROR_NO_METRIC);
        report.clickCancelPreset();
        report.selectMetric(preset1.getMetricsMap());
        report.clickAddPreset();
        report.inputPresetName(preset1.getPresetName());
        report.clickSavePreset();
        report.refreshBuilderPage();
        report.selectPreset(preset1.getPresetName());
        report.assertBuilderPage(preset1);
        report.renamePreset(preset1.getPresetName(), preset2.getPresetName());
        report.setReportData(preset2);
        report.clickUpdatePreset();
        report.refreshBuilderPage();
        report.selectPreset(preset2.getPresetName());
        report.assertBuilderPage(preset2);
        report.deletePreset(preset2.getPresetName());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58220")
    @Description("DSP name link to the edit page")
    public void reportDspEndpointNameLink() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_SSP).addAttributeToMap(ReportEnum.attributesEnum.DSP_ENDPOINT, true).addMetricToMap(ReportEnum.metricsEnum.IMPRESSIONS, true).setAggregateBy(ReportEnum.aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS);
        String endpointString;
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_DSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_DSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        endpointString = report.clickEndpointNameLink(ReportEnum.attributesEnum.DSP_ENDPOINT, 0, reportData.getReportType());
        report.assertEndpointFromReport(ReportEnum.attributesEnum.DSP_ENDPOINT, endpointString);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58221")
    @Description("SSP name link to the edit page")
    public void reportSspEndpointNameLink() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_SSP).addAttributeToMap(attributesEnum.SSP_ENDPOINT, true).addMetricToMap(ReportEnum.metricsEnum.IMPRESSIONS, true).setAggregateBy(ReportEnum.aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS);
        String endpointString;
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_SSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_SSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        endpointString = report.clickEndpointNameLink(attributesEnum.SSP_ENDPOINT, 0, reportData.getReportType());
        report.assertEndpointFromReport(attributesEnum.SSP_ENDPOINT, endpointString);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("169")
    @Description("Ssp/Dsp User can't generate report without metrics")
    public void validationNoMetricsSspDsp() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.OS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_DSP).setAttrMap(attrMap);
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_DSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_DSP);
        report.clickGenerateReportWithError();
        report.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
        report.modalClickConfirm();
        report.selectAttribute(reportData);
        report.clickGenerateReportWithError();
        report.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("182")
    @Description("Ssp/Dsp User can generate reports only with metrics")
    public void generateOnlyMetricsSspDsp() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_SSP).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_SSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_SSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        sAssert.assertAll("Errors on generated report page");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58216")
    @Description("Ssp/Dsp User is able to generate new report on report page")
    public void clickNewReportSspDsp() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_DSP).addAttributeToMap(attributesEnum.OS, true).addMetricToMap(ReportEnum.metricsEnum.IMPRESSIONS, true).setAggregateBy(ReportEnum.aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LIFETIME), builderEmpty = new ReportDO(reportTypeEnum.USER_DSP);
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_DSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_DSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.clickGenerateNewReport();
        report.assertBuilderPage(builderEmpty);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58218")
    @Description("Dsp/Ssp user report - update created report settings")
    public void updateReportSspDsp() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        BasicUtility util = new BasicUtility();
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.SSP_COMPANY, true);
            put(attributesEnum.OS, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.SSP_WINS, true);
            put(metricsEnum.SSP_REVENUE, true);
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_SSP).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        String dateValueByDay = util.getDateTimeFormatted(reportData.getDateTo(), "yyyy-MM-dd"), dateValueByMonth;
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_SSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_SSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertReportTableValue(ReportEnum.attributesEnum.DATE.attributeName(), 0, dateValueByDay);
        report.assertGeneratedReport(reportData);
        report.selectAggregatedByReport(ReportEnum.aggregateByEnum.MONTH);
        report.clickUpdateReport();
        reportData.setAggregateBy(ReportEnum.aggregateByEnum.MONTH);
        dateValueByMonth = util.getDateTimeFormatted(reportData.getDateTo(), "yyyy-MM");
        report.assertReportTableValue(ReportEnum.attributesEnum.DATE.attributeName(), 0, dateValueByMonth);
        report.assertGeneratedReport(reportData);
        report.deleteMetric(ReportEnum.metricsEnum.DSP_SPEND);
        report.deleteMetric(ReportEnum.metricsEnum.DSP_BID_RESPONSES);
        reportData.addMetricToMap(ReportEnum.metricsEnum.DSP_SPEND, false).addMetricToMap(ReportEnum.metricsEnum.DSP_BID_RESPONSES, false);
        report.clickUpdateReport();
        report.assertReportAddMetricList(ReportEnum.metricsEnum.DSP_SPEND);
        report.assertReportAddMetricList(ReportEnum.metricsEnum.DSP_BID_RESPONSES);
        report.assertGeneratedReport(reportData);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58219")
    @Description("Filters by chosen attributes are available to SSP/DSP user on report page")
    public void reportFiltersSspDsp() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(softAssert);
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.DSP_ENDPOINT, true);
            put(attributesEnum.OS, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.BID_REQUESTS, true);
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_DSP).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        String filterOs;
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_DSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_DSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        filterOs = report.getReportTableValue(attributesEnum.OS.attributeName__old(), 0);
        reportData.setAttributeFilter(ReportEnum.attributesEnum.OS, filterOs);
        report.setAttributeFilterGeneratedReport(reportData.getAttributeFilterMap(), reportData.getReportType());
        report.clickUpdateReport();
        report.assertReportTableValue(ReportEnum.attributesEnum.OS.attributeName(), filterOs);
        report.assertGeneratedReport(reportData);
        softAssert.assertAll("Errors after setting filters in generated report:");
        report.clickBackToBuilder();
        report.setAttributeFilterBuilder(ReportEnum.attributesEnum.OS, filterOs, reportData.getReportType());
        report.clickGenerateReport();
        report.assertReportTableValue(ReportEnum.attributesEnum.OS.attributeName__old(), filterOs);
        report.assertGeneratedReport(reportData);
        softAssert.assertAll("Errors after setting filters in report builder:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58217")
    @Description("SSP/DSP User can share generated report by Share report button")
    public void shareReportSspDsp() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        String shareLink;
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.SSP_REQUESTS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.USER_SSP).addAttributeToMap(attributesEnum.ALL, true).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.MONTH);
        login.login(supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_SSP);
        report.gotoUserReportSection(UserEnum.userRoleEnum.PUBL_SSP);
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.clickShareReport();
        shareLink = report.getReportShareLink();
        report.openShareLink(shareLink);
        report.assertSharedReport(reportData);
    }

    //</editor-fold>

}
