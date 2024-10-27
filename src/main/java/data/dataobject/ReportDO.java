package data.dataobject;

import data.CommonEnums;
import data.ReportEnum;
import data.ReportEnum.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ReportDO{
    private String presetName = null;
    private reportTypeEnum reportType = reportTypeEnum.PLATFORM_STATISTICS;
    private LocalDateTime dateFrom, dateTo;
    private CommonEnums.calendarRangeEnum calendarRange;
    private String timeZone = "(UTC +00:00) UTC";
    private aggregateByEnum aggregateBy = aggregateByEnum.DAY;
    private int numberOfRows = 15;
    private Map<attributesEnum, Boolean> attrMap = new HashMap<>();
    private Map<attributesEnum, String> attrFilterMap = new HashMap<>();
    private Map<metricsEnum, Boolean> metricsMap = new HashMap<>();
    private Map<metricsEnum, MetricFilter> metricsFilterMap = new HashMap<>();
    private boolean selectAllAttributes = false, selectAllMetrics = false;

    public ReportDO(reportTypeEnum reportType){
        this.reportType = reportType;
        setCalendarRange(CommonEnums.calendarRangeEnum.LAST_7_DAYS);
    }

    public ReportDO(Map<attributesEnum, Boolean> attrMap, Map<metricsEnum, Boolean> metricsMap, aggregateByEnum aggregateBy, CommonEnums.calendarRangeEnum range, String timeZone){
        setAttrMap(attrMap);
        setMetricsMap(metricsMap);
        setTimeZone(timeZone);
        setAggregateBy(aggregateBy);
        setCalendarRange(range);
    }

    public ReportDO setReportType(reportTypeEnum reportType){
        this.reportType = reportType;
        return this;
    }

    public reportTypeEnum getReportType(){
        return reportType;
    }

    public aggregateByEnum getAggregateBy(){
        return aggregateBy;
    }

    public ReportDO setAggregateBy(aggregateByEnum aggregateBy){
        this.aggregateBy = aggregateBy;
        return this;
    }

    public Integer getNumberOfRows(){
        return numberOfRows;
    }

    public ReportDO setNumberOfRows(int numberOfRows){
        this.numberOfRows = numberOfRows;
        return this;
    }

    public String getPresetName(){
        return presetName;
    }

    public ReportDO setPresetName(String presetName){
        this.presetName = presetName;
        return this;
    }

    //<editor-fold desc="Attributes and metrics">
    public Map<attributesEnum, Boolean> getAttrMap(){
        return attrMap;
    }

    public ReportDO setAttrMap(Map<attributesEnum, Boolean> attrMap){
        this.attrMap.putAll(attrMap);
        return this;
    }

    public ReportDO addAttributeToMap(attributesEnum attr, Boolean value){
        if (attr.equals(attributesEnum.ALL)){
            for (attributesEnum attrEnum : ReportEnum.getAttributesList(this.reportType)){
                this.attrMap.put(attrEnum, value);
            }
            this.selectAllAttributes = value;
        } else {
            this.attrMap.put(attr, value);
        }
        return this;
    }

    public Map<metricsEnum, Boolean> getMetricsMap(){
        return metricsMap;
    }

    public ReportDO setAttributeFilter(attributesEnum attribute, String filterValue){
        this.attrFilterMap.put(attribute, filterValue);
        return this;
    }

    public Map<attributesEnum, String> getAttributeFilterMap(){
        return attrFilterMap;
    }

    public ReportDO setMetricsMap(Map<metricsEnum, Boolean> metrics){
        this.metricsMap.putAll(metrics);
        return this;
    }

    public ReportDO addMetricToMap(metricsEnum metric, Boolean value){
        if (metric.equals(metricsEnum.ALL)){
            for (metricsEnum metrics : ReportEnum.getMetricsList(this.reportType)){
                this.metricsMap.put(metrics, value);
            }
            this.selectAllMetrics = value;
        } else {
            this.metricsMap.put(metric, value);
        }
        return this;
    }

    public boolean isSelectAllAttributes(){
        return this.selectAllAttributes;
    }

    public ReportDO setSelectAllAttributes(boolean selectAllAttributes){
        this.selectAllAttributes = selectAllAttributes;
        return this;
    }

    public boolean isSelectAllMetrics(){
        return selectAllMetrics;
    }

    public ReportDO setSelectAllMetrics(boolean selectAllMetrics){
        this.selectAllMetrics = selectAllMetrics;
        return this;
    }

    public ReportDO setMetricFilter(metricsEnum metric, metricFilterEnum filterType, String filterValue){
        this.metricsFilterMap.put(metric, new MetricFilter(filterType, filterValue));
        return this;
    }

    public Map<metricsEnum, MetricFilter> getMetricsFilterMap(){
        return metricsFilterMap;
    }

    //</editor-fold>

    //<editor-fold desc="Date & Time">
    public LocalDateTime getDateFrom(){
        return dateFrom;
    }

    public ReportDO setDateFrom(LocalDateTime dateFrom){
        this.dateFrom = dateFrom;
        return this;
    }

    public LocalDateTime getDateTo(){
        return dateTo;
    }

    public ReportDO setDateTo(LocalDateTime dateTo){
        this.dateTo = dateTo;
        return this;
    }

    public CommonEnums.calendarRangeEnum getCalendarRange(){
        return calendarRange;
    }

    public ReportDO setCalendarRange(CommonEnums.calendarRangeEnum calendarRange){
        this.calendarRange = calendarRange;
        LocalDateTime now = LocalDateTime.now();
        switch (calendarRange){
            case TODAY:
                this.dateFrom = LocalDateTime.now();
                this.dateTo = LocalDateTime.now();
                break;
            case YESTERDAY:
                this.dateFrom = LocalDateTime.now().minusDays(1);
                this.dateTo = LocalDateTime.now().minusDays(1);
                break;
            case LAST_7_DAYS:
                this.dateFrom = LocalDateTime.now().minusDays(6);
                this.dateTo = LocalDateTime.now();
                break;
            case LAST_30_DAYS:
                this.dateFrom = LocalDateTime.now().minusDays(29);
                this.dateTo = LocalDateTime.now();
                break;
            case THIS_MONTH:
                this.dateFrom = now.minusDays(now.getDayOfMonth() - 1);
                this.dateTo = LocalDateTime.now();
                break;
            case LAST_MONTH:
                this.dateFrom = now.minusMonths(1).minusDays(now.minusMonths(1).getDayOfMonth() - 1);
                this.dateTo = now.minusMonths(1).minusDays(now.minusMonths(1).getDayOfMonth() - 1).plusMonths(1).minusDays(1);
                break;
            case LIFETIME:
                this.dateFrom = LocalDateTime.now().minusYears(1);  //TODO take from config
                this.dateTo = LocalDateTime.now();
                break;
        }
        return this;
    }

    public String getTimeZone(){
        return timeZone;
    }

    public ReportDO setTimeZone(String timeZone){
        this.timeZone = timeZone;
        return this;
    }
    //</editor-fold>

    public static class MetricFilter{
        private metricFilterEnum filterType;
        private String filterValue;

        public MetricFilter(metricFilterEnum filterType, String filterValue){
            this.filterType = filterType;
            this.filterValue = filterValue;
        }

        public metricFilterEnum getFilterType(){
            return filterType;
        }

        public String getFilterValue(){
            return filterValue;
        }
    }

}
