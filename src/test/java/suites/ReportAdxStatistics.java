package suites;

import common.SoftAssertCustom;
import common.utils.BasicUtility;
import common.utils.BrowserUtility;
import common.utils.RandomUtility;
import data.CommonEnums;
import data.ReportEnum.*;
import data.dataobject.ReportDO;
import data.textstrings.messages.ReportText;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.ReportPO;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static data.StaticData.supportDefaultUser;

@Epic("Admin section")
@Feature("Ad Exchange Activity")
@Story("Statistics")
public class ReportAdxStatistics extends BaseSuiteClassNew {

    @Test
    @Issues({@Issue("WP-1823"), @Issue("WP-1793")})
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("1278")
    @Description("User can generate report with any metrics and attributes and download csv file")
    public void generateAndExportReport() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO();
        ReportDO reportData = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setAggregateBy(aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).addAttributeToMap(attributesEnum.ALL, true).addAttributeToMap(attributesEnum.DEAL_DSP, false).addAttributeToMap(attributesEnum.DEAL_SSP, false).addMetricToMap(metricsEnum.ALL, true).addMetricToMap(metricsEnum.DEAL_SSP_REQUESTS, false).addMetricToMap(metricsEnum.DEAL_DSP_REQUESTS, false);
        File csvExport;
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.setReportData(reportData);
        statistics.clickGenerateReport();
        csvExport = statistics.clickExportCSV(reportData.getReportType());
        statistics.assertReportExportColumns(reportData, csvExport);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1342")
    @Description("Preset CRUD")
    public void presetCrud() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO();
        RandomUtility randomUtil = new RandomUtility();
        Map<attributesEnum, Boolean> attrMap1 = new HashMap<>() {{
            put(attributesEnum.COUNTRY, true);
        }}, attrMap2 = new HashMap<>() {{
            put(attributesEnum.COUNTRY, true);
            put(attributesEnum.OS, true);
            put(attributesEnum.DSP_ENDPOINT, true);
        }};
        Map<metricsEnum, Boolean> metricsMap1 = new HashMap<>() {{
            put(metricsEnum.CLICKS, true);
        }}, metricsMap2 = new HashMap<>() {{
            put(metricsEnum.CLICKS, true);
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.CTR, true);
        }};
        ReportDO preset1 = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setAttrMap(attrMap1).setMetricsMap(metricsMap1).setPresetName("adxTestPreset_1_" + randomUtil.getRandomInt(1, 10000)), preset2 = new ReportDO(attrMap2, metricsMap2, aggregateByEnum.MONTH, CommonEnums.calendarRangeEnum.LAST_7_DAYS, "(UTC +01:00) Madrid").setPresetName("adxTestPreset_2_" + randomUtil.getRandomInt(1, 10000)).setReportType(reportTypeEnum.PLATFORM_STATISTICS);
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.selectAttribute(preset1);
        statistics.clickAddPreset();
        statistics.clickSavePresetWithErrors();
        statistics.assertPresetModalError(ReportText.PRESET_ERROR_NO_NAME);
        statistics.inputPresetName(preset1.getPresetName());
        statistics.clickSavePresetWithErrors();
        statistics.assertPresetModalError(ReportText.PRESET_ERROR_NO_METRIC);
        statistics.clickCancelPreset();
        statistics.selectMetric(preset1.getMetricsMap());
        statistics.clickAddPreset();
        statistics.inputPresetName(preset1.getPresetName());
        statistics.clickSavePreset();
        statistics.refreshBuilderPage();
        statistics.selectPreset(preset1.getPresetName());
        statistics.assertBuilderPage(preset1);
        statistics.renamePreset(preset1.getPresetName(), preset2.getPresetName());
        statistics.setReportData(preset2);
        statistics.clickUpdatePreset();
        statistics.refreshBuilderPage();
        statistics.selectPreset(preset2.getPresetName());
        statistics.assertBuilderPage(preset2);
        statistics.deletePreset(preset2.getPresetName());
    }

    @Test
    @Issues({@Issue("WP-1828"), @Issue("WP-1829")})
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("57655")
    @Description("Check the default Presets for ADX report")
    public void statisticsDefaultPresets() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO(sAssert);
        ReportDO mainPlatformMetrics = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setPresetName("Main platform metrics").setAggregateBy(aggregateByEnum.TOTAL).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS).addMetricToMap(metricsEnum.SSP_REQUESTS, true).addMetricToMap(metricsEnum.BID_REQUESTS, true).addMetricToMap(metricsEnum.DSP_BID_RESPONSES, true).addMetricToMap(metricsEnum.DSP_WINS, true).addMetricToMap(metricsEnum.SSP_WINS, true).addMetricToMap(metricsEnum.IMPRESSIONS, true).addMetricToMap(metricsEnum.SSP_REVENUE, true).addMetricToMap(metricsEnum.DSP_SPEND, true),
                bidRatio = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setPresetName("Bid ratio").setAggregateBy(aggregateByEnum.TOTAL).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS).addAttributeToMap(attributesEnum.SSP_ENDPOINT, true).addMetricToMap(metricsEnum.SSP_REQUESTS, true).addMetricToMap(metricsEnum.BID_REQUESTS, true),
                inactiveDsp = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setPresetName("Inactive DSP's").setAggregateBy(aggregateByEnum.TOTAL).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS).addAttributeToMap(attributesEnum.DSP_ENDPOINT, true).addMetricToMap(metricsEnum.BID_REQUESTS, true).addMetricToMap(metricsEnum.DSP_BID_RESPONSES, true).setMetricFilter(metricsEnum.BID_REQUESTS, metricFilterEnum.MORE, "10000").setMetricFilter(metricsEnum.DSP_BID_RESPONSES, metricFilterEnum.EQUAL, "0"),
                dspPartners = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setPresetName("DSP Partner's performance").setAggregateBy(aggregateByEnum.TOTAL).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS).addAttributeToMap(attributesEnum.DSP_ENDPOINT, true).addMetricToMap(metricsEnum.BID_REQUESTS, true).addMetricToMap(metricsEnum.DSP_BID_RESPONSES, true).addMetricToMap(metricsEnum.DSP_WINS, true).addMetricToMap(metricsEnum.DSP_WIN_RATE, true).addMetricToMap(metricsEnum.SSP_WINS, true).addMetricToMap(metricsEnum.SSP_WIN_RATE, true).addMetricToMap(metricsEnum.IMPRESSIONS, true).addMetricToMap(metricsEnum.FILL_RATE, true).addMetricToMap(metricsEnum.DSP_SPEND, true).addMetricToMap(metricsEnum.PROFIT_PLATFORM, true),
                sspPartners = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setPresetName("SSP Partner's performance").setAggregateBy(aggregateByEnum.TOTAL).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS).addAttributeToMap(attributesEnum.SSP_ENDPOINT, true).addMetricToMap(metricsEnum.BID_REQUESTS, true).addMetricToMap(metricsEnum.DSP_BID_RESPONSES, true).addMetricToMap(metricsEnum.DSP_WINS, true).addMetricToMap(metricsEnum.DSP_WIN_RATE, true).addMetricToMap(metricsEnum.SSP_WINS, true).addMetricToMap(metricsEnum.SSP_WIN_RATE, true).addMetricToMap(metricsEnum.IMPRESSIONS, true).addMetricToMap(metricsEnum.FILL_RATE, true).addMetricToMap(metricsEnum.SSP_REVENUE, true).addMetricToMap(metricsEnum.PROFIT_PLATFORM, true);
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.selectPreset(mainPlatformMetrics.getPresetName());
        statistics.assertBuilderPage(mainPlatformMetrics);
        statistics.selectPreset(bidRatio.getPresetName());
        statistics.assertBuilderPage(bidRatio);
        statistics.selectPreset(inactiveDsp.getPresetName());
        statistics.assertBuilderPage(inactiveDsp);
        statistics.selectPreset(dspPartners.getPresetName());
        statistics.assertBuilderPage(dspPartners);
        statistics.selectPreset(sspPartners.getPresetName());
        statistics.assertBuilderPage(sspPartners);
        sAssert.assertAll("Errors with default presets:");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1347")
    @Description("User can't generate report without metrics")
    public void validationNoMetrics() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO();
        Map<attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.COUNTRY, true);
        }};
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.clickGenerateReportWithError();
        statistics.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
        statistics.modalClickConfirm();
        statistics.selectAttribute(attrMap);
        statistics.clickGenerateReportWithError();
        statistics.assertReportGenerationError(ReportText.REPORT_VALIDATION_NO_METRIC);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1368")
    @Description("User can generate reports only with metrics")
    public void generateOnlyMetrics() {
        SoftAssertCustom sAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO(sAssert);
        Map<metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.BID_REQUESTS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LIFETIME).setAggregateBy(aggregateByEnum.DAY);
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.setReportData(reportData);
        statistics.clickGenerateReport();
        statistics.assertReportResultFilters(reportData);
        statistics.assertReportGraphLegend(reportData);
        statistics.assertReportTableColumns(reportData);
        sAssert.assertAll("Errors on generated report page");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1367")
    @Description("User is able to generate new report on report page")
    public void clickNewReport() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO();
        ReportDO reportData = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).addAttributeToMap(attributesEnum.SSP_COMPANY, true).addMetricToMap(metricsEnum.SSP_REQUESTS, true).setAggregateBy(aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LIFETIME), builderEmpty = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS);
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.setReportData(reportData);
        statistics.clickGenerateReport();
        statistics.clickGenerateNewReport();
        statistics.assertBuilderPage(builderEmpty);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1381")
    @Description("Displayed SSP/DSP endpoints are related to Selected SSP/DSP company")
    public void companyEndpointFilterRelation() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO();
        String sspCompany = statistics.getRandomCompanyNameFromExistingEndpoints(CommonEnums.endpointSideEnum.SSP), dspCompany = statistics.getRandomCompanyNameFromExistingEndpoints(CommonEnums.endpointSideEnum.DSP);
        List<String> sspEndpoints = statistics.getEndpointsListByCompany(CommonEnums.endpointSideEnum.SSP, sspCompany), dspEndpoints = statistics.getEndpointsListByCompany(CommonEnums.endpointSideEnum.DSP, dspCompany);
        Map<attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.SSP_COMPANY, true);
            put(attributesEnum.DSP_COMPANY, true);
            put(attributesEnum.SSP_ENDPOINT, true);
            put(attributesEnum.DSP_ENDPOINT, true);
        }};
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.selectAttribute(attrMap);
        statistics.setAttributeFilterBuilder(attributesEnum.SSP_COMPANY, sspCompany, reportTypeEnum.PLATFORM_STATISTICS);
        statistics.assertAttributeFilter(attributesEnum.SSP_ENDPOINT, sspEndpoints);
        statistics.setAttributeFilterBuilder(attributesEnum.DSP_COMPANY, dspCompany, reportTypeEnum.PLATFORM_STATISTICS);
        statistics.assertAttributeFilter(attributesEnum.DSP_ENDPOINT, dspEndpoints);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("39459")
    @Description("DSP and SSP names link to the edit page")
    public void reportEndpointNameLink() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO();
        BrowserUtility browserUtil = new BrowserUtility();
        ReportDO reportData = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).addAttributeToMap(attributesEnum.SSP_ENDPOINT, true).addAttributeToMap(attributesEnum.DSP_ENDPOINT, true).addMetricToMap(metricsEnum.IMPRESSIONS, true).setAggregateBy(aggregateByEnum.MONTH).setCalendarRange(CommonEnums.calendarRangeEnum.LIFETIME);
        String endpointString;
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.setReportData(reportData);
        statistics.clickGenerateReport();
        statistics.clickColumnSorting(attributesEnum.DSP_ENDPOINT.attributeName());
        endpointString = statistics.clickEndpointNameLink(attributesEnum.SSP_ENDPOINT, 0, reportData.getReportType());
        statistics.assertEndpointFromReport(attributesEnum.SSP_ENDPOINT, endpointString);
        browserUtil.closeTab();
        endpointString = statistics.clickEndpointNameLink(attributesEnum.DSP_ENDPOINT, 0, reportData.getReportType());
        statistics.assertEndpointFromReport(attributesEnum.DSP_ENDPOINT, endpointString);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1376")
    @Description("User can share generated report by Share report button")
    public void shareReport() {
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO();
        String shareLink;
        Map<attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.SSP_COMPANY, true);
            put(attributesEnum.SSP_ENDPOINT, true);
            put(attributesEnum.COUNTRY, true);
        }};
        Map<metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.SSP_REQUESTS, true);
            put(metricsEnum.DSP_BID_RESPONSES, true);
            put(metricsEnum.SSP_WINS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(aggregateByEnum.MONTH);
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.setReportData(reportData);
        statistics.clickGenerateReport();
        statistics.assertGeneratedReport(reportData);
        statistics.clickShareReport();
        shareLink = statistics.getReportShareLink();
        statistics.openShareLink(shareLink);
        statistics.assertSharedReport(reportData);
    }

    @Test
    @Issues({@Issue("WP-1793"), @Issue("WP-1810"), @Issue("WP-1811")})
    @Severity(SeverityLevel.NORMAL)
    @TmsLinks({@TmsLink("1373"), @TmsLink("1382")})
    @Description("Update created report settings")
    public void updateReport() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO(softAssert);
        BasicUtility util = new BasicUtility();
        Map<attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.DSP_ENDPOINT, true);
            put(attributesEnum.SSP_ENDPOINT, true);
            put(attributesEnum.COUNTRY, true);
            put(attributesEnum.AD_FORMAT, true);
            put(attributesEnum.TRAFFIC_TYPE, true);
        }};
        Map<metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.SSP_REQUESTS, true);
            put(metricsEnum.DSP_BID_RESPONSES, true);
            put(metricsEnum.SSP_WINS, true);
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_MONTH).setAggregateBy(aggregateByEnum.DAY);
        String dateValueByDay = util.getDateTimeFormatted(reportData.getDateTo(), "yyyy-MM-dd"), dateValueByMonth;
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.setReportData(reportData);
        statistics.clickGenerateReport();
        statistics.assertReportTableValue(attributesEnum.DATE.attributeName(), 0, dateValueByDay);
        statistics.assertGeneratedReport(reportData);
        statistics.calendarSelectDatePeriod(CommonEnums.calendarRangeEnum.LAST_30_DAYS);
        statistics.selectAggregatedByReport(aggregateByEnum.MONTH);
        statistics.clickUpdateReport();
        reportData.setCalendarRange(CommonEnums.calendarRangeEnum.LAST_30_DAYS).setAggregateBy(aggregateByEnum.MONTH);
        dateValueByMonth = util.getDateTimeFormatted(reportData.getDateTo(), "yyyy-MM");
        statistics.assertReportTableValue(attributesEnum.DATE.attributeName(), 0, dateValueByMonth);
        statistics.assertGeneratedReport(reportData);
        statistics.deleteMetric(metricsEnum.SSP_REQUESTS);
        statistics.deleteMetric(metricsEnum.DSP_BID_RESPONSES);
        reportData.addMetricToMap(metricsEnum.SSP_REQUESTS, false).addMetricToMap(metricsEnum.DSP_BID_RESPONSES, false);
        statistics.clickUpdateReport();
        statistics.assertReportAddMetricList(metricsEnum.SSP_REQUESTS);
        statistics.assertReportAddMetricList(metricsEnum.DSP_BID_RESPONSES);
        statistics.assertGeneratedReport(reportData);
        softAssert.assertAll("Errors in ADX Statistics update");
    }

    @Test
    @Issues({@Issue("WP-1812")})
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("1369")
    @Description("Filters by chosen attributes are available to user on report page")
    public void reportFilters() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        ReportPO statistics = new ReportPO(softAssert);
        Map<attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(attributesEnum.SSP_ENDPOINT, true);
            put(attributesEnum.COUNTRY, true);
            put(attributesEnum.AD_FORMAT, true);
            put(attributesEnum.DOMAIN_BUNDLE, true);
        }};
        Map<metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(metricsEnum.BID_REQUESTS, true);
            put(metricsEnum.IMPRESSIONS, true);
            put(metricsEnum.CLICKS, true);
        }};
        ReportDO reportData = new ReportDO(reportTypeEnum.PLATFORM_STATISTICS).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_MONTH).setAggregateBy(aggregateByEnum.DAY);
        String filterAdFormat, filterDomainBundle;
        login.login(supportDefaultUser);
        statistics.gotoStatisticsReport();
        statistics.setReportData(reportData);
        statistics.clickGenerateReport();
        filterAdFormat = statistics.getReportTableValue(attributesEnum.AD_FORMAT.attributeName(), 0);
        filterDomainBundle = statistics.getReportTableValue(attributesEnum.DOMAIN_BUNDLE.attributeName(), 0);
        reportData.setAttributeFilter(attributesEnum.AD_FORMAT, filterAdFormat).setAttributeFilter(attributesEnum.DOMAIN_BUNDLE, filterDomainBundle);
        statistics.setAttributeFilterGeneratedReport(reportData.getAttributeFilterMap(), reportData.getReportType());
        statistics.clickUpdateReport();
        statistics.assertReportTableValue(attributesEnum.AD_FORMAT.attributeName(), filterAdFormat);
        statistics.assertReportTableValue(attributesEnum.DOMAIN_BUNDLE.attributeName(), filterDomainBundle);
        statistics.assertGeneratedReport(reportData);
        softAssert.assertAll("Errors after setting filters in generated report:");
        statistics.clickBackToBuilder();
        statistics.setAttributeFilterBuilder(attributesEnum.AD_FORMAT, filterAdFormat, reportData.getReportType());
        statistics.setAttributeFilterBuilder(attributesEnum.DOMAIN_BUNDLE, filterDomainBundle, reportData.getReportType());
        statistics.clickGenerateReport();
        statistics.assertReportTableValue(attributesEnum.AD_FORMAT.attributeName(), filterAdFormat);
        statistics.assertReportTableValue(attributesEnum.DOMAIN_BUNDLE.attributeName(), filterDomainBundle);
        statistics.assertGeneratedReport(reportData);
        softAssert.assertAll("Errors after setting filters in report builder:");
    }

}
