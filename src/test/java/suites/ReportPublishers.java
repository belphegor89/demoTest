package suites;

import common.SoftAssertCustom;
import common.utils.BasicUtility;
import common.utils.RandomUtility;
import data.CommonEnums;
import data.ReportEnum;
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

@Epic("Admin section")
@Feature("Manage Publishers")
@Story("Publisher Report")
public class ReportPublishers extends BaseSuiteClassNew {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("1114")
    @Description("User can generate report with any metrics and attributes and download csv file")
    public void generateAndExportReport() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        ReportDO reportData = new ReportDO(ReportEnum.reportTypeEnum.PLATFORM_PUBLISHERS).setAggregateBy(ReportEnum.aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LIFETIME).addAttributeToMap(ReportEnum.attributesEnum.ALL, true).addAttributeToMap(ReportEnum.attributesEnum.DEAL_DSP, false).addAttributeToMap(ReportEnum.attributesEnum.DEAL_PUBLISHER, false).addMetricToMap(ReportEnum.metricsEnum.ALL, true).addMetricToMap(ReportEnum.metricsEnum.DEAL_SSP_REQUESTS, false).addMetricToMap(ReportEnum.metricsEnum.DEAL_DSP_REQUESTS, false);
        File csvExport;
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        csvExport = report.clickExportCSV(reportData.getReportType());
        report.assertReportExportColumns(reportData, csvExport);
        sAssert.assertAll("Errors with Publishers' Report:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("21148")
    @Description("Preset CRUD")
    public void presetCrud() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        RandomUtility randomUtil = new RandomUtility();
        Map<ReportEnum.attributesEnum, Boolean> attrMap1 = new HashMap<>() {{
            put(ReportEnum.attributesEnum.COUNTRY, true);
        }}, attrMap2 = new HashMap<>() {{
            put(ReportEnum.attributesEnum.COUNTRY, true);
            put(ReportEnum.attributesEnum.OS, true);
            put(ReportEnum.attributesEnum.DSP_ENDPOINT, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap1 = new HashMap<>() {{
            put(ReportEnum.metricsEnum.CLICKS, true);
        }}, metricsMap2 = new HashMap<>() {{
            put(ReportEnum.metricsEnum.CLICKS, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.CTR, true);
        }};
        ReportDO preset1 = new ReportDO(ReportEnum.reportTypeEnum.PLATFORM_PUBLISHERS).setAttrMap(attrMap1).setMetricsMap(metricsMap1).setPresetName("pubTestPreset_1_" + randomUtil.getRandomInt(1, 10000)), preset2 = new ReportDO(attrMap2, metricsMap2, ReportEnum.aggregateByEnum.MONTH, CommonEnums.calendarRangeEnum.LAST_7_DAYS, "(UTC +01:00) Madrid").setPresetName("pubTestPreset_2_" + randomUtil.getRandomInt(1, 10000)).setReportType(ReportEnum.reportTypeEnum.PLATFORM_PUBLISHERS);
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
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
    @TmsLink("1095")
    @Description("User can't generate report without metrics")
    public void validationNoMetrics() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.AD_FORMAT, true);
        }};
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
        report.clickGenerateReportWithError();
        report.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
        report.modalClickConfirm();
        report.selectAttribute(attrMap);
        report.clickGenerateReportWithError();
        report.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1109")
    @Description("User can generate reports only with metrics")
    public void generateOnlyMetrics() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.BID_REQUESTS, true);
        }};
        ReportDO reportData = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertGeneratedReport(reportData);
        sAssert.assertAll("Errors on generated report page");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1108")
    @Description("User is able to generate new report on report page")
    public void clickNewReport() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        ReportDO reportData = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).addAttributeToMap(ReportEnum.attributesEnum.INVENTORY_PUBLISHER, true).addMetricToMap(ReportEnum.metricsEnum.IMPRESSIONS, true).setAggregateBy(ReportEnum.aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LIFETIME), builderEmpty = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER);
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.clickGenerateNewReport();
        report.assertBuilderPage(builderEmpty);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("39455")
    @Description("DSP name link to the edit page")
    public void reportEndpointNameLink() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        ReportDO reportData = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).addAttributeToMap(ReportEnum.attributesEnum.DSP_ENDPOINT, true).addMetricToMap(ReportEnum.metricsEnum.IMPRESSIONS, true).setAggregateBy(ReportEnum.aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS);
        String endpointString;
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.clickColumnSorting(ReportEnum.attributesEnum.DSP_ENDPOINT.attributeName__old());
        endpointString = report.clickEndpointNameLink(ReportEnum.attributesEnum.DSP_ENDPOINT, 0, reportData.getReportType());
        report.assertEndpointFromReport(ReportEnum.attributesEnum.DSP_ENDPOINT, endpointString);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58206")
    @Description("User can share generated report by Share report button")
    public void shareReport() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO();
        String shareLink;
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.DSP_COMPANY, true);
            put(ReportEnum.attributesEnum.DSP_ENDPOINT, true);
            put(ReportEnum.attributesEnum.COUNTRY, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.SSP_REQUESTS, true);
            put(ReportEnum.metricsEnum.DSP_BID_RESPONSES, true);
            put(ReportEnum.metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(ReportEnum.reportTypeEnum.PLATFORM_PUBLISHERS).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.MONTH);
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
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
    @TmsLink("58207")
    @Description("Update created report settings")
    public void updateReport() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(sAssert);
        BasicUtility util = new BasicUtility();
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.DSP_ENDPOINT, true);
            put(ReportEnum.attributesEnum.OS, true);
            put(ReportEnum.attributesEnum.COUNTRY, true);
            put(ReportEnum.attributesEnum.AD_FORMAT, true);
            put(ReportEnum.attributesEnum.TRAFFIC_TYPE, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.DSP_SPEND, true);
            put(ReportEnum.metricsEnum.DSP_BID_RESPONSES, true);
            put(ReportEnum.metricsEnum.DSP_WINS, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(ReportEnum.reportTypeEnum.PLATFORM_PUBLISHERS).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_MONTH).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        String dateValueByDay = util.getDateTimeFormatted(reportData.getDateTo(), "yyyy-MM-dd"), dateValueByMonth;
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
        report.setReportData(reportData);
        report.clickGenerateReport();
        report.assertReportTableValue(ReportEnum.attributesEnum.DATE.attributeName(), 0, dateValueByDay);
        report.assertGeneratedReport(reportData);
        report.calendarSelectDatePeriod(CommonEnums.calendarRangeEnum.LAST_30_DAYS);
        report.selectAggregatedByReport(ReportEnum.aggregateByEnum.MONTH);
        report.clickUpdateReport();
        reportData.setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.MONTH);
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
        sAssert.assertAll("Errors after updating report:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("58208")
    @Description("Filters by chosen attributes are available to user on report page")
    public void reportFilters() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO report = new ReportPO(softAssert);
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.DSP_ENDPOINT, true);
            put(ReportEnum.attributesEnum.AD_FORMAT, true);
            put(ReportEnum.attributesEnum.DOMAIN_BUNDLE, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.BID_REQUESTS, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(ReportEnum.reportTypeEnum.PLATFORM_PUBLISHERS).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_MONTH).setAggregateBy(ReportEnum.aggregateByEnum.DAY);
        String filterAdFormat, filterDomainBundle;
        login.login(supportDefaultUser);
        report.gotoPublishersReport();
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

}
