package data.dataobject;

import com.fasterxml.jackson.databind.MappingIterator;
import common.utils.BasicUtility;
import common.utils.FileUtility;
import data.FilterListEnums;
import data.FilterListEnums.recordType;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class FilterListDO{
    private final BasicUtility Util = new BasicUtility();
    private final FileUtility FileUtil = new FileUtility();
    private String filterName;
    private recordType recordType;
    private boolean isBlackList;
    private LocalDateTime createdDate, updatedDate;
    private Map<String, Boolean> dspList = new LinkedHashMap<>(), sspList = new LinkedHashMap<>();
    private List<String> recordList = new ArrayList<>();

    public FilterListDO(){}

    public FilterListDO(String filterName, recordType recordType, Boolean isBlackList, List<String> recordList){
        this.filterName = filterName;
        this.recordType = recordType;
        this.isBlackList = isBlackList;
        this.createdDate = Util.getCurrentDateTime(ZoneId.of("UTC"));
        this.updatedDate = Util.getCurrentDateTime(ZoneId.of("UTC"));
        this.recordList.addAll(recordList);
    }

    public FilterListDO(String filterName, recordType recordType, Boolean isBlackList, LocalDateTime createdDate, LocalDateTime updatedDate, Map<String, Boolean> dspList, Map<String, Boolean> sspList, List<String> recordList){
        this.filterName = filterName;
        this.recordType = recordType;
        this.isBlackList = isBlackList;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.dspList = dspList;
        this.sspList = sspList;
        this.recordList.addAll(recordList);
    }

    public String getFilterName(){
        return filterName;
    }

    public FilterListDO setFilterName(String filterName){
        this.filterName = filterName;
        return this;
    }

    public FilterListEnums.recordType getRecordType(){
        return recordType;
    }

    public FilterListDO setRecordType(FilterListEnums.recordType recordType){
        this.recordType = recordType;
        return this;
    }

    public Boolean getListType(){
        return isBlackList;
    }

    public FilterListDO setListType(Boolean blackList){
        isBlackList = blackList;
        return this;
    }

    public String getCreatedDate(){
        return Util.getDateTimeFormatted(createdDate, "yyyy-MM-dd", Locale.ENGLISH);
    }

    public String getCreatedDate(String format, Locale locale){
        return Util.getDateTimeFormatted(createdDate, format, locale);
    }

    public FilterListDO setCreatedDate(LocalDateTime createdDate){
        this.createdDate = createdDate;
        return this;
    }

    public String getUpdatedDate(){
        return Util.getDateTimeFormatted(updatedDate, "yyyy-MM-dd", Locale.ENGLISH);
    }

    public String getUpdatedDate(String format, Locale locale){
        return Util.getDateTimeFormatted(updatedDate, format, locale);
    }

    public FilterListDO setUpdatedDate(LocalDateTime updatedDate){
        this.updatedDate = updatedDate;
        return this;
    }

    public Map<String, Boolean> getDspList(){
        return dspList;
    }

    public FilterListDO setDspList(Map<String, Boolean> dspList){
        this.dspList.putAll(dspList);
        return this;
    }

    public FilterListDO addDspToList(String dsp, Boolean isBlackList){
        this.dspList.put(dsp, isBlackList);
        return this;
    }

    public Map<String, Boolean> getSspList(){
        return sspList;
    }

    public FilterListDO setSspList(Map<String, Boolean> sspList){
        this.sspList.putAll(sspList);
        return this;
    }

    public FilterListDO addSspToList(String ssp, Boolean isBlackList){
        this.sspList.put(ssp, isBlackList);
        return this;
    }

    public List<String> getRecordList(){
        return recordList;
    }

    public FilterListDO setRecordList(List<String> recordList){
        this.recordList.addAll(recordList);
        return this;
    }

    public FilterListDO addRecord(String record){
        recordList.add(record);
        return this;
    }

    public FilterListDO addRecordImport(File importFile){
        MappingIterator<Map<String, Object>> parsedCsv = FileUtil.readCsvFile(importFile);
        while (parsedCsv.hasNext()){
            for (Object value : parsedCsv.next().values()){
                recordList.add(value.toString());
            }
        }
        return this;
    }

}
