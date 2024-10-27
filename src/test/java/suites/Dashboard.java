package suites;

import common.SoftAssertCustom;
import data.CommonEnums;
import data.ReportEnum;
import data.StaticData;
import data.UserEnum;
import data.dataobject.ReportDO;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.AuthorizationPO;
import pages.DashboardPO;
import pages.ReportPO;

import java.util.HashMap;
import java.util.Map;

@Epic("Dashboard")
public class Dashboard extends BaseSuiteClassNew {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("4324")
    @Description("Dashboard check for Admin user")
    public void dashboardAdmin() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DashboardPO dashboard = new DashboardPO(softAssert);
        ReportPO report = new ReportPO(softAssert);
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.SSP_REQUESTS, true);
            put(ReportEnum.metricsEnum.DSP_WINS, true);
            put(ReportEnum.metricsEnum.DSP_WIN_RATE, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.FILL_RATE, true);
            put(ReportEnum.metricsEnum.RENDER_RATE, true);
            put(ReportEnum.metricsEnum.CLICKS, true);
            put(ReportEnum.metricsEnum.CTR, true);
            put(ReportEnum.metricsEnum.SSP_REVENUE, true);
            put(ReportEnum.metricsEnum.SSP_ECPM, true);
            put(ReportEnum.metricsEnum.VIDEO_COMPLETE, true);
            put(ReportEnum.metricsEnum.VCR, true);
        }};
        ReportDO adminReport = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.TODAY).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL),
                inventoryReport = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).addAttributeToMap(ReportEnum.attributesEnum.INVENTORY_PUBLISHER, true).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.TODAY).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL),
                placementReport = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).addAttributeToMap(ReportEnum.attributesEnum.PLACEMENT, true).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.TODAY).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL);
        String inventory, placement;
        boolean invData, plcData;
        login.login(StaticData.supportDefaultUser);
        dashboard.assertDashboard(UserEnum.userRoleEnum.ADM);
        softAssert.assertAll("Errors with Admin dashboard widgets");
        dashboard.clickWidgetReport(0);
        report.assertDashboardReport(adminReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(1);
        report.assertDashboardReport(adminReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(2);
        report.assertDashboardReport(adminReport);
        dashboard.gotoDashboard();
        //TODO assert the billing history table
        softAssert.assertAll("Errors with Admin dashboard reports");
        dashboard.clickInventoryName();
        dashboard.gotoDashboard();
        dashboard.clickPlacementName();
        dashboard.gotoDashboard();
        softAssert.assertAll("Errors with Admin dashboard Inventory and Placement links");
        invData = dashboard.checkInventoryImpressionPresent();
        inventory = dashboard.clickInventoryReport();
        inventoryReport.setAttributeFilter(ReportEnum.attributesEnum.INVENTORY_PUBLISHER, inventory);
        report.assertDashboardInventoryPlacementReport(inventoryReport, invData);
        dashboard.gotoDashboard();
        plcData = dashboard.checkPlacementImpressionPresent();
        placement = dashboard.clickPlacementReport();
        placementReport.setAttributeFilter(ReportEnum.attributesEnum.PLACEMENT, placement);
        report.assertDashboardInventoryPlacementReport(placementReport, plcData);
        softAssert.assertAll("Errors with Admin dashboard Inventory and Placement reports");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @TmsLink("97")
    @Description("Dashboard check for Publisher user")
    public void dashboardPublisher() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DashboardPO dashboard = new DashboardPO(softAssert);
        ReportPO report = new ReportPO(softAssert);
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.SSP_REQUESTS, true);
            put(ReportEnum.metricsEnum.DSP_WINS, true);
            put(ReportEnum.metricsEnum.DSP_WIN_RATE, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.FILL_RATE, true);
            put(ReportEnum.metricsEnum.RENDER_RATE, true);
            put(ReportEnum.metricsEnum.CLICKS, true);
            put(ReportEnum.metricsEnum.CTR, true);
            put(ReportEnum.metricsEnum.SSP_REVENUE, true);
            put(ReportEnum.metricsEnum.SSP_ECPM, true);
            put(ReportEnum.metricsEnum.VIDEO_COMPLETE, true);
            put(ReportEnum.metricsEnum.VCR, true);
        }};
        ReportDO pubReport = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.TODAY).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL),
                inventoryReport = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).addAttributeToMap(ReportEnum.attributesEnum.INVENTORY_PUBLISHER, true).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.TODAY).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL),
                placementReport = new ReportDO(ReportEnum.reportTypeEnum.USER_PUBLISHER).addAttributeToMap(ReportEnum.attributesEnum.PLACEMENT, true).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.TODAY).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL);
        String inventory, placement;
        boolean invData, plcData;
        login.login(StaticData.supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL);
        dashboard.assertDashboard(UserEnum.userRoleEnum.PUBL);
        softAssert.assertAll("Errors with Publisher dashboard widgets");
        dashboard.clickWidgetReport(0);
        report.assertDashboardReport(pubReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(1);
        report.assertDashboardReport(pubReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(2);
        report.assertDashboardReport(pubReport);
        dashboard.gotoDashboard();
        //TODO assert the billing history table
        softAssert.assertAll("Errors with Publisher dashboard reports");
        dashboard.clickInventoryName();
        dashboard.gotoDashboard();
        dashboard.clickPlacementName();
        dashboard.gotoDashboard();
        softAssert.assertAll("Errors with Publisher dashboard Inventory and Placement links");
        invData = dashboard.checkInventoryImpressionPresent();
        inventory = dashboard.clickInventoryReport();
        inventoryReport.setAttributeFilter(ReportEnum.attributesEnum.INVENTORY_PUBLISHER, inventory);
        report.assertDashboardInventoryPlacementReport(inventoryReport, invData);
        dashboard.gotoDashboard();
        plcData = dashboard.checkPlacementImpressionPresent();
        placement = dashboard.clickPlacementReport();
        placementReport.setAttributeFilter(ReportEnum.attributesEnum.PLACEMENT, placement);
        report.assertDashboardInventoryPlacementReport(placementReport, plcData);
        softAssert.assertAll("Errors with Publisher dashboard Inventory and Placement reports");
    }

    @Test(enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("22005")
    @Description("Dashboard check for SSP user")
    public void dashboardSsp() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DashboardPO dashboard = new DashboardPO(softAssert);
        ReportPO report = new ReportPO(softAssert);
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.SSP_COMPANY, true);
            put(ReportEnum.attributesEnum.SSP_ENDPOINT, true);
            put(ReportEnum.attributesEnum.OS, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.SSP_REQUESTS, true);
            put(ReportEnum.metricsEnum.SSP_WINS, true);
            put(ReportEnum.metricsEnum.SSP_WIN_RATE, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.FILL_RATE, true);
            put(ReportEnum.metricsEnum.RENDER_RATE, true);
            put(ReportEnum.metricsEnum.CLICKS, true);
            put(ReportEnum.metricsEnum.SSP_REVENUE, true);
        }};
        ReportDO sspReport = new ReportDO(ReportEnum.reportTypeEnum.USER_SSP).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL);
        login.login(StaticData.supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_SSP);
        dashboard.assertDashboard(UserEnum.userRoleEnum.PUBL_SSP);
        softAssert.assertAll("Errors with SSP dashboard widgets");
        dashboard.clickWidgetReport(0);
        report.assertGeneratedReport(sspReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(1);
        report.assertGeneratedReport(sspReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(2);
        report.assertGeneratedReport(sspReport);
        //TODO assert the billing history table
        softAssert.assertAll("Errors with SSP dashboard reports");
    }

    @Test(enabled = false)
    @Severity(SeverityLevel.NORMAL)
    @TmsLink("22006")
    @Description("Dashboard check for DSP user")
    public void dashboardDsp() {
        SoftAssertCustom softAssert = new SoftAssertCustom();
        AuthorizationPO login = new AuthorizationPO();
        DashboardPO dashboard = new DashboardPO(softAssert);
        ReportPO report = new ReportPO(softAssert);
        Map<ReportEnum.attributesEnum, Boolean> attrMap = new HashMap<>() {{
            put(ReportEnum.attributesEnum.DSP_COMPANY, true);
            put(ReportEnum.attributesEnum.DSP_ENDPOINT, true);
            put(ReportEnum.attributesEnum.OS, true);
        }};
        Map<ReportEnum.metricsEnum, Boolean> metricsMap = new HashMap<>() {{
            put(ReportEnum.metricsEnum.BID_REQUESTS, true);
            put(ReportEnum.metricsEnum.DSP_BID_RESPONSES, true);
            put(ReportEnum.metricsEnum.DSP_WINS, true);
            put(ReportEnum.metricsEnum.DSP_WIN_RATE, true);
            put(ReportEnum.metricsEnum.IMPRESSIONS, true);
            put(ReportEnum.metricsEnum.CLICKS, true);
            put(ReportEnum.metricsEnum.CTR, true);
            put(ReportEnum.metricsEnum.DSP_SPEND, true);
        }};
        ReportDO dspReport = new ReportDO(ReportEnum.reportTypeEnum.USER_DSP).setAttrMap(attrMap).setMetricsMap(metricsMap).setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS).setAggregateBy(ReportEnum.aggregateByEnum.TOTAL);
        login.login(StaticData.supportDefaultUser);
        login.loginAsUser(UserEnum.userRoleEnum.PUBL_DSP);
        dashboard.assertDashboard(UserEnum.userRoleEnum.PUBL_DSP);
        softAssert.assertAll("Errors with DSP dashboard widgets");
        dashboard.clickWidgetReport(0);
        report.assertGeneratedReport(dspReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(1);
        report.assertGeneratedReport(dspReport);
        dashboard.gotoDashboard();
        dashboard.clickWidgetReport(2);
        report.assertGeneratedReport(dspReport);
        //TODO assert the billing history table
        softAssert.assertAll("Errors with DSP dashboard reports");
    }

}
