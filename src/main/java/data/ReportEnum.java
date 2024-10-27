package data;

import java.util.ArrayList;
import java.util.List;

public class ReportEnum{

    public enum reportTypeEnum {
        USER_PUBLISHER,
        USER_SSP,
        USER_DSP,
        PLATFORM_PUBLISHERS,
        PLATFORM_STATISTICS
    }

    public enum aggregateByEnum {
        DAY,
        HOUR,
        MONTH,
        TOTAL;

        public String publicName(){
            return switch (this){
                case DAY -> "Day";
                case HOUR -> "Hour";
                case MONTH -> "Month";
                case TOTAL -> "Total";
            };
        }

        public String attributeName(){
            return switch (this){
                case DAY -> "day";
                case HOUR -> "hour";
                case MONTH -> "month";
                case TOTAL -> "total";
            };
        }
    }

    public enum attributesEnum {
        ALL,
        DATE,
        SSP_COMPANY,
        DSP_COMPANY,
        SSP_ENDPOINT,
        DSP_ENDPOINT,
        SSP_TYPE,
        DSP_TYPE,
        DEAL_PUBLISHER,
        DEAL_SSP,
        DEAL_DSP,
        DEAL_PUBLISHER_HASH,
        COUNTRY,
        TRAFFIC_TYPE,
        SEAT,
        AD_FORMAT,
        SIZE,
        DOMAIN_BUNDLE,
        OS,
        PUBLISHER,
        PUBLISHER_ID,
        INVENTORY_PUBLISHER,
        PLACEMENT,
        INVENTORY_DSP_ID,
        INVENTORY_SSP_ID,
        CRID,
        AB_TEST;

        //ADX statistics was refactored and some attribute IDs were changed. This method is used for pub/user statistics only, waiting for the completion of https://smartyads.atlassian.net/browse/ESG-115
        public String attributeName(reportTypeEnum type){
            return switch (this){
                case DATE -> "date_export";
                case SSP_COMPANY -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "ssp_company";
                    } else {
                        yield "ssp_company_id";
                    }
                }
                case DSP_COMPANY -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "dsp_company";
                    } else {
                        yield "dsp_company_id";
                    }
                }
                case SSP_ENDPOINT -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "ssp_name";
                    } else {
                        yield "endpoint_id";
                    }
                }
                case DSP_ENDPOINT -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "dsp_name";
                    } else {
                        yield "dsp_id";
                    }
                }
                case SSP_TYPE -> "endpoint_type_ssp";
                case DSP_TYPE -> "endpoint_type_dsp";
                case DEAL_PUBLISHER -> "deal_publisher_id";
                case DEAL_SSP -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "deal_ssp_name";
                    } else {
                        yield "deal_ssp_id";
                    }
                }
                case DEAL_DSP -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "deal_dsp_name";
                    } else {
                        yield "deal_dsp_id";
                    }
                }
                case DEAL_PUBLISHER_HASH -> "deal_publisher_hash";
                case COUNTRY -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "country_name";
                    } else {
                        yield "country_id";
                    }
                }
                case TRAFFIC_TYPE -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "platform_name";
                    } else {
                        yield "platform_id";
                    }
                }
                case SEAT -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "seat_name";
                    } else {
                        yield "seat_id";
                    }
                }
                case AD_FORMAT -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "traffic_name";
                    } else {
                        yield "traffic_id";
                    }
                }
                case SIZE -> "size";
                case DOMAIN_BUNDLE -> "inventory_key";
                case OS -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "os_name";
                    } else {
                        yield "os_id";
                    }
                }
                case PUBLISHER -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "user_name";
                    } else {
                        yield "user_id";
                    }
                }
                case PUBLISHER_ID -> "publisher_id";
                case INVENTORY_PUBLISHER -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "inventory_name";
                    } else {
                        yield "placement_id";
                    }
                }
                case PLACEMENT -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "placement_name";
                    } else {
                        yield "inventory_id";
                    }
                }
                case INVENTORY_DSP_ID -> "inventory_dsp_id";
                case INVENTORY_SSP_ID -> "inventory_ssp_id";
                case CRID -> "crid";
                case ALL -> "all";
                case AB_TEST -> "ab_test_attribute";
            };
        }

        public String attributeName__old(){
            return switch (this){
                case DATE -> "date_export";
                case SSP_COMPANY -> "ssp_company_id";
                case DSP_COMPANY -> "dsp_company_id";
                case SSP_ENDPOINT -> "endpoint_id";
                case DSP_ENDPOINT -> "dsp_id";
                case SSP_TYPE -> "endpoint_type_ssp";
                case DSP_TYPE -> "endpoint_type_dsp";
                case DEAL_PUBLISHER -> "deal_publisher_id";
                case DEAL_SSP -> "deal_ssp_id";
                case DEAL_DSP -> "deal_dsp_id";
                case DEAL_PUBLISHER_HASH -> "deal_publisher_hash";
                case COUNTRY -> "country_id";
                case TRAFFIC_TYPE -> "platform_id";
                case SEAT -> "seat_id";
                case AD_FORMAT -> "traffic_id";
                case SIZE -> "size";
                case DOMAIN_BUNDLE -> "inventory_key";
                case OS -> "os_id";
                case PUBLISHER -> "user_id";
                case PUBLISHER_ID -> "publisher_id";
                case INVENTORY_PUBLISHER -> "inventory_id";
                case PLACEMENT -> "placement_id";
                case INVENTORY_DSP_ID -> "inventory_dsp_id";
                case INVENTORY_SSP_ID -> "inventory_ssp_id";
                case CRID -> "crid";
                case ALL -> "all";
                case AB_TEST -> "ab_test_attribute";
            };
        }

        public String attributeName(){
            return switch (this){
                case DATE -> "date_export";
                case SSP_COMPANY -> "ssp_company";
                case DSP_COMPANY -> "dsp_company";
                case SSP_ENDPOINT -> "ssp_name";
                case DSP_ENDPOINT -> "dsp_name";
                case SSP_TYPE -> "endpoint_type_ssp";
                case DSP_TYPE -> "endpoint_type_dsp";
                case DEAL_PUBLISHER -> "deal_publisher_id";
                case DEAL_SSP -> "deal_ssp_name";
                case DEAL_DSP -> "deal_dsp_name";
                case DEAL_PUBLISHER_HASH -> "deal_publisher_hash";
                case COUNTRY -> "country_name";
                case TRAFFIC_TYPE -> "platform_name";
                case SEAT -> "seat_name";
                case AD_FORMAT -> "traffic_name";
                case SIZE -> "size";
                case DOMAIN_BUNDLE -> "inventory_key";
                case OS -> "os_name";
                case PUBLISHER -> "user_name";
                case PUBLISHER_ID -> "publisher_name";
                case INVENTORY_PUBLISHER -> "inventory_name";
                case PLACEMENT -> "placement_name";
                case INVENTORY_DSP_ID -> "inventory_dsp_id";
                case INVENTORY_SSP_ID -> "inventory_ssp_id";
                case CRID -> "crid";
                case ALL -> "all";
                case AB_TEST -> "ab_test_attribute";
            };
        }

        public String publicName(){
            return switch (this){
                case ALL -> "All";
                case DATE -> "Date";
                case SSP_COMPANY -> "SSP Company";
                case DSP_COMPANY -> "DSP Company";
                case SSP_ENDPOINT -> "SSP Endpoint";
                case DSP_ENDPOINT -> "DSP Endpoint";
                case SSP_TYPE -> "SSP Endpoint Type";
                case DSP_TYPE -> "DSP Endpoint Type";
                case DEAL_PUBLISHER -> "Publisher deal";
                case DEAL_SSP -> "SSP deal";
                case DEAL_DSP -> "DSP deal";
                case DEAL_PUBLISHER_HASH -> "Publisher Deal Hash";
                case COUNTRY -> "Country";
                case TRAFFIC_TYPE -> "Traffic Type";
                case SEAT -> "Seat";
                case AD_FORMAT -> "Ad Format";
                case SIZE -> "Size";
                case DOMAIN_BUNDLE -> "Domain / Bundle";
                case OS -> "OS";
                case PUBLISHER -> "Publisher";
                case PUBLISHER_ID -> "Publisher ID";
                case INVENTORY_PUBLISHER -> "Inventory";
                case PLACEMENT -> "Placement";
                case INVENTORY_DSP_ID -> "DSP Inventory ID";
                case INVENTORY_SSP_ID -> "SSP Inventory ID";
                case CRID -> "CRID";
                case AB_TEST -> "A/B Test Attribute";
            };
        }

        public String publicName(reportTypeEnum type){
            return switch (this){
                case ALL -> "All";
                case DATE -> "Date";
                case SSP_COMPANY -> "SSP Company";
                case DSP_COMPANY -> "DSP Company";
                case SSP_ENDPOINT -> "SSP Endpoint";
                case DSP_ENDPOINT -> "DSP Endpoint";
                case SSP_TYPE -> "SSP Endpoint Type";
                case DSP_TYPE -> "DSP Endpoint Type";
                case DEAL_PUBLISHER -> "Publisher deal";
                case DEAL_SSP -> "SSP deal";
                case DEAL_DSP -> "DSP deal";
                case DEAL_PUBLISHER_HASH -> "Publisher Deal Hash";
                case COUNTRY -> "Country";
                case TRAFFIC_TYPE -> "Traffic Type";
                case SEAT -> "Seat";
                case AD_FORMAT -> "Ad Format";
                case SIZE -> "Size";
                case DOMAIN_BUNDLE -> "Domain / Bundle";
                case OS -> "OS";
                case PUBLISHER -> "Publisher";
                case PUBLISHER_ID -> "Publisher ID";
                case INVENTORY_PUBLISHER -> "Inventory";
                case PLACEMENT -> "Placement";
                case INVENTORY_DSP_ID -> "DSP Inventory ID";
                case INVENTORY_SSP_ID -> "SSP Inventory ID";
                case CRID -> "CRID";
                case AB_TEST -> "A/B Test Attribute";
            };
        }
    }

    public static List<attributesEnum> getAttributesList(reportTypeEnum reportTypeEnum){
        List<attributesEnum> attributesList = new ArrayList<>();
        switch (reportTypeEnum){
            case USER_PUBLISHER -> {
                attributesList.add(attributesEnum.DEAL_PUBLISHER);
                attributesList.add(attributesEnum.DEAL_PUBLISHER_HASH);
                attributesList.add(attributesEnum.COUNTRY);
                attributesList.add(attributesEnum.TRAFFIC_TYPE);
                attributesList.add(attributesEnum.AD_FORMAT);
                attributesList.add(attributesEnum.SIZE);
                attributesList.add(attributesEnum.DOMAIN_BUNDLE);
                attributesList.add(attributesEnum.INVENTORY_PUBLISHER);
                attributesList.add(attributesEnum.PLACEMENT);
                attributesList.add(attributesEnum.CRID);
            }
            case USER_SSP -> {
                attributesList.add(attributesEnum.SSP_COMPANY);
                attributesList.add(attributesEnum.SSP_ENDPOINT);
                attributesList.add(attributesEnum.OS);
            }
            case USER_DSP -> {
                attributesList.add(attributesEnum.DSP_COMPANY);
                attributesList.add(attributesEnum.DSP_ENDPOINT);
                attributesList.add(attributesEnum.OS);
            }
            case PLATFORM_PUBLISHERS -> {
                attributesList.add(attributesEnum.DSP_COMPANY);
                attributesList.add(attributesEnum.DSP_ENDPOINT);
                attributesList.add(attributesEnum.DEAL_PUBLISHER);
                attributesList.add(attributesEnum.DEAL_DSP);
                attributesList.add(attributesEnum.COUNTRY);
                attributesList.add(attributesEnum.TRAFFIC_TYPE);
                attributesList.add(attributesEnum.AD_FORMAT);
                attributesList.add(attributesEnum.SIZE);
                attributesList.add(attributesEnum.DOMAIN_BUNDLE);
                attributesList.add(attributesEnum.OS);
                attributesList.add(attributesEnum.PUBLISHER);
                attributesList.add(attributesEnum.INVENTORY_PUBLISHER);
                attributesList.add(attributesEnum.PLACEMENT);
                attributesList.add(attributesEnum.CRID);
            }
            case PLATFORM_STATISTICS -> {
                attributesList.add(attributesEnum.SSP_COMPANY);
                attributesList.add(attributesEnum.SSP_ENDPOINT);
                attributesList.add(attributesEnum.DSP_COMPANY);
                attributesList.add(attributesEnum.DSP_ENDPOINT);
                attributesList.add(attributesEnum.SSP_TYPE);
                attributesList.add(attributesEnum.DSP_TYPE);
                attributesList.add(attributesEnum.DEAL_DSP);
                attributesList.add(attributesEnum.DEAL_SSP);
                attributesList.add(attributesEnum.COUNTRY);
                attributesList.add(attributesEnum.TRAFFIC_TYPE);
                attributesList.add(attributesEnum.SEAT);
                attributesList.add(attributesEnum.AD_FORMAT);
                attributesList.add(attributesEnum.SIZE);
                attributesList.add(attributesEnum.DOMAIN_BUNDLE);
                attributesList.add(attributesEnum.OS);
                attributesList.add(attributesEnum.PUBLISHER_ID);
                attributesList.add(attributesEnum.INVENTORY_DSP_ID);
                attributesList.add(attributesEnum.INVENTORY_SSP_ID);
                attributesList.add(attributesEnum.CRID);
                attributesList.add(attributesEnum.AB_TEST);
            }
        }
        return attributesList;
    }

    public enum metricsEnum {
        ALL,
        SSP_REQUESTS,
        SSP_REQUESTS_IFA,
        SSP_REQUESTS_IDFA_PERCENT,
        SSP_REQUESTS_USER_ID,
        COOKIES_SYNC_SSP,
        BID_REQUESTS,
        BID_REQUESTS_IFA,
        DEAL_SSP_REQUESTS,
        DEAL_DSP_REQUESTS,
        DSP_BID_RESPONSES,
        DSP_WINS,
        DSP_WIN_RATE,
        SSP_WINS,
        SSP_WIN_RATE,
        IMPRESSIONS,
        FILL_RATE,
        CLICKS,
        CTR,
        RENDER_RATE,
        SSP_REVENUE,
        SSP_ECPM,
        SSP_BID_PRICE_AVG,
        SSP_BID_FLOOR_AVG,
        DSP_SPEND,
        DSP_ECPM,
        DSP_BID_PRICE_AVG,
        DSP_BID_FLOOR_AVG,
        PROFIT_PLATFORM,
        PROFIT_PUBLISHER,
        MARGIN,
        BID_RATE,
        TIMEOUT_RATE,
        VIDEO_FIRST_QUARTILE,
        VIDEO_MIDPOINT,
        VIDEO_THIRD_QUARTILE,
        VIDEO_COMPLETE,
        VCR,
        VCR_RATE,
        SSP_SRCPM,
        DSP_SRCPM,
        DSP_SYNC_RATE,
        SSP_SYNC_RATE;

        public String attributeName(){
            return switch (this){
                case SSP_REQUESTS -> "ssp_requests";
                case SSP_REQUESTS_IFA -> "ssp_idfa_requests";
                case SSP_REQUESTS_IDFA_PERCENT -> "ssp_idfa_requests_percent";
                case SSP_REQUESTS_USER_ID -> "ssp_cookie_requests";
                case COOKIES_SYNC_SSP -> "platform_cookies";
                case BID_REQUESTS -> "requests";
                case BID_REQUESTS_IFA -> "cookie_requests";
                case DEAL_SSP_REQUESTS -> "deal_ssp_request";
                case DEAL_DSP_REQUESTS -> "deal_dsp_request";
                case DSP_BID_RESPONSES -> "responses";
                case DSP_WINS -> "wins";
                case DSP_WIN_RATE -> "dsp_win_rate";
                case SSP_WINS -> "ssp_wins";
                case SSP_WIN_RATE -> "ssp_win_rate";
                case IMPRESSIONS -> "imps";
                case FILL_RATE -> "fill_rate";
                case CLICKS -> "clicks";
                case CTR -> "ctr";
                case RENDER_RATE -> "render_rate";
                case SSP_REVENUE -> "ssp_price";
                case SSP_ECPM -> "ssp_ecpm";
                case SSP_BID_PRICE_AVG -> "avg_ssp_bid_price";
                case SSP_BID_FLOOR_AVG -> "avg_ssp_bid_floor";
                case DSP_SPEND -> "dsp_price";
                case DSP_ECPM -> "dsp_ecpm";
                case DSP_BID_PRICE_AVG -> "avg_dsp_bid_price";
                case DSP_BID_FLOOR_AVG -> "avg_dsp_bid_floor";
                case PROFIT_PLATFORM -> "platform_revenue";
                case PROFIT_PUBLISHER -> "total_earn";
                case MARGIN -> "margin";
                case BID_RATE -> "bid_rate";
                case TIMEOUT_RATE -> "timeout_rate";
                case VIDEO_FIRST_QUARTILE -> "video_events_first_quartile";
                case VIDEO_MIDPOINT -> "video_events_midpoint";
                case VIDEO_THIRD_QUARTILE -> "video_events_third_quartile";
                case VIDEO_COMPLETE -> "video_events_complete";
                case VCR -> "vcr";
                case VCR_RATE -> "vcr_rate";
                case DSP_SRCPM -> "dsp_srcpm";
                case SSP_SRCPM -> "ssp_srcpm";
                case DSP_SYNC_RATE -> "dsp_sync_rate";
                case SSP_SYNC_RATE -> "ssp_sync_rate";
                case ALL -> "all";
            };
        }

        public String publicName(){
            return switch (this){
                case ALL -> "All";
                case SSP_REQUESTS -> "SSP Requests";
                case SSP_REQUESTS_USER_ID -> "SSP requests with User ID";
                case SSP_REQUESTS_IFA -> "Requests with IFA";
                case SSP_REQUESTS_IDFA_PERCENT -> "% of requests with IDFA";
                case COOKIES_SYNC_SSP -> "Synced cookies with SSP";
                case BID_REQUESTS -> "Bid Requests";
                case BID_REQUESTS_IFA -> "Buyer ids";
                case DEAL_SSP_REQUESTS -> "SSP Deal Request";
                case DEAL_DSP_REQUESTS -> "DSP Deal Request";
                case DSP_BID_RESPONSES -> "DSP Bid Responses";
                case DSP_WINS -> "DSP Wins";
                case DSP_WIN_RATE -> "DSP Win Rate, %";
                case SSP_WINS -> "SSP Wins";
                case SSP_WIN_RATE -> "SSP Win Rate, %";
                case IMPRESSIONS -> "Impressions";
                case FILL_RATE -> "Fill Rate, %";
                case RENDER_RATE -> "Render Rate, %";
                case CLICKS -> "Clicks";
                case CTR -> "CTR, %";
                case SSP_REVENUE -> "SSP Revenue, $";
                case SSP_ECPM -> "SSP eCPM, $";
                case SSP_BID_PRICE_AVG -> "AVG. SSP Bid Price";
                case SSP_BID_FLOOR_AVG -> "AVG. SSP Bid Floor";
                case DSP_SPEND -> "DSP Spend, $";
                case DSP_ECPM -> "DSP eCPM, $";
                case DSP_BID_PRICE_AVG -> "AVG. DSP Bid Price";
                case DSP_BID_FLOOR_AVG -> "AVG. DSP Bid Floor";
                case PROFIT_PLATFORM -> "Profit, $";
                case PROFIT_PUBLISHER -> "Profit, $";
                case MARGIN -> "Margin, %";
                case BID_RATE -> "Bid Rate, %";
                case TIMEOUT_RATE -> "Timeout Rate, %";
                case VIDEO_FIRST_QUARTILE -> "Video First Quartile";
                case VIDEO_MIDPOINT -> "Video Midpoint";
                case VIDEO_THIRD_QUARTILE -> "Video Third Quartile";
                case VIDEO_COMPLETE -> "Video Complete";
                case VCR -> "VCR, %";
                case VCR_RATE -> "VCR, %";
                case DSP_SRCPM -> "DSP sRCPM";
                case SSP_SRCPM -> "SSP sRCPM";
                case DSP_SYNC_RATE -> "DSP Sync Rate, %";
                case SSP_SYNC_RATE -> "SSP Sync Rate, %";
            };
        }

        public String publicName(reportTypeEnum type){
            return switch (this){
                case ALL -> "All";
                case SSP_REQUESTS -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS || type == reportTypeEnum.USER_SSP){
                        yield "SSP Requests";
                    } else {
                        yield "Requests";
                    }
                }
                case SSP_REQUESTS_IFA -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "SSP Requests with IFA";
                    } else {
                        yield "Requests with IFA";
                    }
                }
                case SSP_REQUESTS_IDFA_PERCENT -> "% of requests with IDFA";
                case SSP_REQUESTS_USER_ID -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "SSP requests with User ID";
                    } else {
                        yield "Requests with User IDs";
                    }
                }
                case COOKIES_SYNC_SSP -> "Synced cookies with SSP";
                case BID_REQUESTS -> "Bid Requests";
                case BID_REQUESTS_IFA -> "Bid requests with synced cookies or ifa";
                case DEAL_SSP_REQUESTS -> "SSP Deal Request";
                case DEAL_DSP_REQUESTS -> "DSP Deal Request";
                case DSP_BID_RESPONSES -> "DSP Bid Responses";
                case DSP_WINS -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS || type == reportTypeEnum.USER_DSP){
                        yield "DSP Wins";
                    } else {
                        yield "Wins";
                    }
                }
                case DSP_WIN_RATE -> {
                    if (type == reportTypeEnum.USER_PUBLISHER){
                        yield "Win Rate, %";
                    } else {
                        yield "DSP Win Rate, %";
                    }
                }
                case SSP_WINS -> "SSP Wins";
                case SSP_WIN_RATE -> "SSP Win Rate, %";
                case IMPRESSIONS -> "Impressions";
                case FILL_RATE -> "Fill Rate, %";
                case RENDER_RATE -> "Render Rate, %";
                case CLICKS -> "Clicks";
                case CTR -> "CTR, %";
                case SSP_REVENUE -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS || type == reportTypeEnum.USER_SSP){
                        yield "SSP Revenue, $";
                    } else if (type == reportTypeEnum.USER_PUBLISHER){
                        yield "Revenue, $";
                    } else {
                        yield "Publisher Revenue, $";
                    }
                }
                case SSP_ECPM -> {
                    if (type == reportTypeEnum.PLATFORM_STATISTICS){
                        yield "SSP eCPM, $";
                    } else {
                        yield "eCPM, $";
                    }
                }
                case SSP_BID_PRICE_AVG -> "AVG. SSP Bid Price";
                case SSP_BID_FLOOR_AVG -> "AVG. SSP Bid Floor";
                case DSP_SPEND -> "DSP Spend, $";
                case DSP_ECPM -> "DSP eCPM, $";
                case DSP_BID_PRICE_AVG -> "AVG. DSP Bid Price";
                case DSP_BID_FLOOR_AVG -> "AVG. DSP Bid Floor";
                case PROFIT_PLATFORM -> "Profit, $";
                case PROFIT_PUBLISHER -> "Profit, $";
                case MARGIN -> "Margin, %";
                case BID_RATE -> "Bid Rate, %";
                case TIMEOUT_RATE -> "Timeout Rate, %";
                case VIDEO_FIRST_QUARTILE -> "Video First Quartile";
                case VIDEO_MIDPOINT -> "Video Midpoint";
                case VIDEO_THIRD_QUARTILE -> "Video Third Quartile";
                case VIDEO_COMPLETE -> "Video Complete";
                case VCR -> "VCR, %";
                case VCR_RATE -> "VCR, %";
                case DSP_SRCPM -> "DSP sRCPM";
                case SSP_SRCPM -> "SSP sRCPM";
                case DSP_SYNC_RATE -> "DSP Sync Rate, %";
                case SSP_SYNC_RATE -> "SSP Sync Rate, %";
            };
        }
    }

    public static List<metricsEnum> getMetricsList(reportTypeEnum reportTypeEnum){
        List<metricsEnum> metricsList = new ArrayList<>();
        switch (reportTypeEnum){
            case USER_PUBLISHER -> {
                metricsList.add(metricsEnum.SSP_REQUESTS);
                metricsList.add(metricsEnum.DEAL_SSP_REQUESTS);
                metricsList.add(metricsEnum.DSP_WINS);
                metricsList.add(metricsEnum.DSP_WIN_RATE);
                metricsList.add(metricsEnum.IMPRESSIONS);
                metricsList.add(metricsEnum.FILL_RATE);
                metricsList.add(metricsEnum.RENDER_RATE);
                metricsList.add(metricsEnum.CLICKS);
                metricsList.add(metricsEnum.CTR);
                metricsList.add(metricsEnum.SSP_REVENUE);
                metricsList.add(metricsEnum.SSP_ECPM);
                metricsList.add(metricsEnum.VIDEO_COMPLETE);
                metricsList.add(metricsEnum.VCR);
            }
            case USER_SSP -> {
                metricsList.add(metricsEnum.SSP_REQUESTS);
                metricsList.add(metricsEnum.SSP_WINS);
                metricsList.add(metricsEnum.SSP_WIN_RATE);
                metricsList.add(metricsEnum.IMPRESSIONS);
                metricsList.add(metricsEnum.FILL_RATE);
                metricsList.add(metricsEnum.CLICKS);
                metricsList.add(metricsEnum.CTR);
                metricsList.add(metricsEnum.RENDER_RATE);
                metricsList.add(metricsEnum.SSP_REVENUE);
            }
            case USER_DSP -> {
                metricsList.add(metricsEnum.BID_REQUESTS);
                metricsList.add(metricsEnum.DSP_BID_RESPONSES);
                metricsList.add(metricsEnum.DSP_WINS);
                metricsList.add(metricsEnum.DSP_WIN_RATE);
                metricsList.add(metricsEnum.IMPRESSIONS);
                metricsList.add(metricsEnum.CLICKS);
                metricsList.add(metricsEnum.CTR);
                metricsList.add(metricsEnum.DSP_SPEND);
            }
            case PLATFORM_PUBLISHERS -> {
                metricsList.add(metricsEnum.SSP_REQUESTS);
                metricsList.add(metricsEnum.SSP_REQUESTS_IFA);
                metricsList.add(metricsEnum.SSP_REQUESTS_IDFA_PERCENT);
                metricsList.add(metricsEnum.SSP_REQUESTS_USER_ID);
                metricsList.add(metricsEnum.BID_REQUESTS_IFA);
                metricsList.add(metricsEnum.BID_REQUESTS);
                metricsList.add(metricsEnum.DEAL_SSP_REQUESTS);
                metricsList.add(metricsEnum.DEAL_DSP_REQUESTS);
                metricsList.add(metricsEnum.DSP_BID_FLOOR_AVG);
                metricsList.add(metricsEnum.DSP_BID_RESPONSES);
                metricsList.add(metricsEnum.DSP_BID_PRICE_AVG);
                metricsList.add(metricsEnum.DSP_WINS);
                metricsList.add(metricsEnum.DSP_WIN_RATE);
                metricsList.add(metricsEnum.IMPRESSIONS);
                metricsList.add(metricsEnum.FILL_RATE);
                metricsList.add(metricsEnum.RENDER_RATE);
                metricsList.add(metricsEnum.CLICKS);
                metricsList.add(metricsEnum.CTR);
                metricsList.add(metricsEnum.SSP_REVENUE);
                metricsList.add(metricsEnum.DSP_SPEND);
                metricsList.add(metricsEnum.DSP_ECPM);
                metricsList.add(metricsEnum.SSP_ECPM);
                metricsList.add(metricsEnum.PROFIT_PUBLISHER);
                metricsList.add(metricsEnum.VIDEO_COMPLETE);
                metricsList.add(metricsEnum.VCR);
            }
            case PLATFORM_STATISTICS -> {
                metricsList.add(metricsEnum.SSP_REQUESTS);
                metricsList.add(metricsEnum.SSP_REQUESTS_USER_ID);
                metricsList.add(metricsEnum.SSP_REQUESTS_IFA);
                metricsList.add(metricsEnum.SSP_REQUESTS_IDFA_PERCENT);
                metricsList.add(metricsEnum.COOKIES_SYNC_SSP);
                metricsList.add(metricsEnum.BID_REQUESTS);
                metricsList.add(metricsEnum.BID_REQUESTS_IFA);
                metricsList.add(metricsEnum.DSP_BID_RESPONSES);
                metricsList.add(metricsEnum.DEAL_SSP_REQUESTS);
                metricsList.add(metricsEnum.DEAL_DSP_REQUESTS);
                metricsList.add(metricsEnum.DSP_BID_FLOOR_AVG);
                metricsList.add(metricsEnum.SSP_BID_FLOOR_AVG);
                metricsList.add(metricsEnum.DSP_BID_PRICE_AVG);
                metricsList.add(metricsEnum.SSP_BID_PRICE_AVG);
                metricsList.add(metricsEnum.DSP_WINS);
                metricsList.add(metricsEnum.DSP_WIN_RATE);
                metricsList.add(metricsEnum.SSP_WINS);
                metricsList.add(metricsEnum.SSP_WIN_RATE);
                metricsList.add(metricsEnum.IMPRESSIONS);
                metricsList.add(metricsEnum.FILL_RATE);
                metricsList.add(metricsEnum.RENDER_RATE);
                metricsList.add(metricsEnum.CLICKS);
                metricsList.add(metricsEnum.CTR);
                metricsList.add(metricsEnum.SSP_REVENUE);
                metricsList.add(metricsEnum.DSP_SPEND);
                metricsList.add(metricsEnum.DSP_ECPM);
                metricsList.add(metricsEnum.SSP_ECPM);
                metricsList.add(metricsEnum.PROFIT_PLATFORM);
                metricsList.add(metricsEnum.MARGIN);
                metricsList.add(metricsEnum.BID_RATE);
                metricsList.add(metricsEnum.TIMEOUT_RATE);
                metricsList.add(metricsEnum.VIDEO_FIRST_QUARTILE);
                metricsList.add(metricsEnum.VIDEO_MIDPOINT);
                metricsList.add(metricsEnum.VIDEO_THIRD_QUARTILE);
                metricsList.add(metricsEnum.VIDEO_COMPLETE);
                metricsList.add(metricsEnum.VCR_RATE);
                metricsList.add(metricsEnum.SSP_SRCPM);
                metricsList.add(metricsEnum.DSP_SRCPM);
                metricsList.add(metricsEnum.DSP_SYNC_RATE);
                metricsList.add(metricsEnum.SSP_SYNC_RATE);
            }
        }
        return metricsList;
    }

    public enum metricFilterEnum {
        MORE,
        LESS,
        EQUAL;

        public String attributeName(){
            return switch (this){
                case MORE -> "more";
                case LESS -> "less";
                case EQUAL -> "equal";
            };
        }

        public String publicName(){
            return switch (this){
                case MORE -> "more than";
                case LESS -> "less than";
                case EQUAL -> "is 0";
            };
        }
    }

}
