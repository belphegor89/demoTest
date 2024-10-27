package testrailscripts;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import testrailscripts.testrail.APIClient;
import testrailscripts.testrail.APIException;

import java.io.*;
import java.util.*;



public class Main{

    public static void main(String[] args) throws Exception{
        Integer dspRoot = 3, sspRoot = 4, allRoot = null;
        Integer masterBranch = 1, admanBranch = 15;
        APIClient client = new APIClient("https://smartyads.testrail.net/");
        client.setUser("maksym.kozachenko@smartyads.com");
        client.setPassword("Ruo2GE159951");
        List<Integer> filterEpics = new ArrayList<>();
//        filterEpics.add(1); //Krush Media
//        filterEpics.add(3); //Apptimus
//        filterEpics.add(5); //Admanmedia
//        filterEpics.add(6); //zMessenger
//        filterEpics.add(7); //Sabacell
//        filterEpics.add(9); //Thrive Labs
//        filterEpics.add(12); //Advenue
//        filterEpics.add(13); //Viralize
//        filterEpics.add(16); //Adprime
//        filterEpics.add(18); //Boldwin
//        filterEpics.add(19); //WLS SSP+ADX box
//        filterEpics.add(23); //MobFox
//        filterEpics.add(24); //Luna Media
//        filterEpics.add(26); //Logan
//        filterEpics.add(27); //WLS DSP box
//        filterEpics.add(30); //Bidlab
//        filterEpics.add(32); //ADL
//        filterEpics.add(33); //Admazing
//        filterEpics.add(34); //Content Exchange
//        filterEpics.add(35); //Colossus/HuddledMasses
//        filterEpics.add(36); //IQzone
//        filterEpics.add(37); //Brightcom
//        filterEpics.add(39); //Sayollo
//        filterEpics.add(40); //TMV Media
//        filterEpics.add(41); //Hyve
//        filterEpics.add(42); //Acuity Ads
//        filterEpics.add(43); //AndBeyondMedia

        /* Old and unsupported projects
        filterEpics.add(2);
        filterEpics.add(4);
        filterEpics.add(8);
        filterEpics.add(10);
        filterEpics.add(11);
        filterEpics.add(14);
        filterEpics.add(15); //Smarty SSP
        filterEpics.add(17);
        filterEpics.add(20); //Hax Media
        filterEpics.add(21);
        filterEpics.add(22);
        filterEpics.add(25);
        filterEpics.add(28); //Reface
        filterEpics.add(29);
        filterEpics.add(31);
        filterEpics.add(38); //KaiOS
         */

        /*Скрипты для работы с тест кейсами. Закомментируйте те вызовы скриптов, которые вам сейчас не нужны и оставьте те, которые должны отработать.*/
        //        getSummaryEpicLinks(filterEpics, masterBranch, sspRoot, client);
        //        getClearEpicLinks(filterEpics, masterBranch, sspRoot, client);
        //        clearEpicLinks(filterEpics, masterBranch, sspRoot, client)
        //        Map<Integer, String> tmsMap = checkTmsLinks("src/test/java/Suites/Core/Authorization.java", client);
        //        Map<Integer, String> tmsMap2 = checkTmsLinks("src/test/java/Suites/Core/DSP.java", client);
        //        Map<Integer, String> tmsMap3 = checkTmsLinks("src/test/java/Suites/Core/Inventories.java", client);
        //        Map<Integer, String> tmsMap4 = checkTmsLinks("src/test/java/Suites/Core/ReportUser.java", client);
        //        Map<Integer, String> tmsMap5 = checkTmsLinks("src/test/java/Suites/Core/SupplyDashboard.java", client);

        Map<String, String> tmsMap = checkTmsLinks("src/test/java/Suites/Core/Authorization.java", client);
        Map<String, String> tmsMapComp = checkTmsLinks("src/test/java/Suites/Core/Companies.java", client);
        Map<String, String> tmsMap2 = checkTmsLinks("src/test/java/Suites/Core/DSP.java", client);
        Map<String, String> tmsMap3 = checkTmsLinks("src/test/java/Suites/Core/Inventories.java", client);
        Map<String, String> tmsMap4 = checkTmsLinks("src/test/java/Suites/Core/ReportUser.java", client);
        Map<String, String> tmsMap5 = checkTmsLinks("src/test/java/Suites/Core/SupplyDashboard.java", client);

        System.out.println(tmsMap);
        System.out.println(tmsMapComp);
        System.out.println(tmsMap2);
        System.out.println(tmsMap3);
        System.out.println(tmsMap4);
        System.out.println(tmsMap5);
        /**/
        //        System.out.println(getCase(52, client));
        //        System.out.println(getProject(1, client));
        //        System.out.println(getSuites(1, client));
    }


    //<editor-fold desc="Scripts">

    private static Set<Integer> getClearEpicLinks(List<Integer> filterEpics, Integer branchId, Integer rootSection, APIClient client){
        Integer caseId;
        Set<Integer> sectionList = getSections(rootSection, client);
        int limit = 250, offset = 0, size;
        boolean isNext = true;
        Set<Integer> casesIdsToClean = new HashSet<>();
        List<Object> casesIdsSingleDelete = new ArrayList<>();
        while (isNext){
            System.out.println("Limit: " + limit);
            System.out.println("Offset: " + offset);
            try{
                JSONObject responseObject = (JSONObject) client.sendGet("get_cases/1&suite_id=" + branchId + "&limit=" + limit + "&offset=" + offset);
                isNext = ((JSONObject) responseObject.get("_links")).get("next") != null;
                size = Integer.parseInt(responseObject.get("size").toString());
                System.out.println("Size: " + size);
                JSONArray casesArray = (JSONArray) responseObject.get("cases");
                for (Object obj : casesArray){
                    JSONObject caseObj = (JSONObject) obj;
                    if (sectionList.contains(((Long) caseObj.get("section_id")).intValue())){
                        caseId = ((Long) caseObj.get("id")).intValue();
                        JSONArray epicsArray = (JSONArray) caseObj.get("custom_epic_name");
                        List<Object> customEpicLinks = new ArrayList<>();
                        List<Object> customEpicFull = new ArrayList<>();
                        List<Integer> epicIdsToDelete = new ArrayList<>();
                        List<Integer> epicIdsToSave = new ArrayList<>();
                        try{
                            customEpicLinks = Arrays.asList(epicsArray.toArray());
                        } catch (NullPointerException ex){
                            //                ex.printStackTrace();
                        }
                        if (customEpicLinks.isEmpty()){
                            System.out.println("Case ID [" + caseId + "] EPIC LINKS EMPTY");
                        } else {
                            for (Object epicObj : customEpicLinks){
                                Integer epic = ((Long) epicObj).intValue();
                                customEpicFull.add(epic);
                                if (filterEpics.contains(epic)){
                                    epicIdsToDelete.add(epic);
                                } else {
                                    epicIdsToSave.add(epic);
                                }
                            }
                            if (!epicIdsToDelete.isEmpty()){
                                System.out.println("Case ID [" + caseId + "] to DELETE: " + epicIdsToDelete);
                                casesIdsToClean.add(caseId);
                            }
                            if (!epicIdsToSave.isEmpty()){
                                System.out.println("Case ID [" + caseId + "] to SAVE: " + epicIdsToSave);
                            }
                        }
                        if (customEpicFull.equals(epicIdsToDelete)){
                            casesIdsSingleDelete.add(caseId);
                        }
                    }
                }
            } catch (IOException | APIException e){
                throw new RuntimeException(e);
            }
            offset += limit;
        }
        System.out.println("Cases to clean: " + casesIdsToClean);
        //        System.out.println("Cases with single epic to delete: " + casesIdsSingleDelete);
        return casesIdsToClean;
    }

    private static Map<Integer, List<Integer>> getSummaryEpicLinks(List<Integer> filterEpics, Integer branchId, Integer rootSection, APIClient client){
        Integer caseId;
        Set<Integer> sectionList = getSections(rootSection, client);
        int limit = 250, offset = 0, size;
        boolean isNext = true;
        Map<Integer, List<Integer>> casesIdsToClean = new HashMap<>();
        //        Set<Integer> casesIdsToClean = new HashSet<>();
        List<Object> casesIdsSingleDelete = new ArrayList<>();
        while (isNext){
            System.out.println("Limit: " + limit);
            System.out.println("Offset: " + offset);
            try{
                JSONObject responseObject = (JSONObject) client.sendGet("get_cases/1&suite_id=" + branchId + "&limit=" + limit + "&offset=" + offset);
                isNext = ((JSONObject) responseObject.get("_links")).get("next") != null;
                size = Integer.parseInt(responseObject.get("size").toString());
                System.out.println("Size: " + size);
                JSONArray casesArray = (JSONArray) responseObject.get("cases");
                for (Object obj : casesArray){
                    JSONObject caseObj = (JSONObject) obj;
                    if (sectionList.contains(((Long) caseObj.get("section_id")).intValue())){
                        caseId = ((Long) caseObj.get("id")).intValue();
                        JSONArray epicsArray = (JSONArray) caseObj.get("custom_epic_name");
                        List<Object> customEpicLinks = new ArrayList<>();
                        List<Object> customEpicFull = new ArrayList<>();
                        List<Integer> epicIdsToDelete = new ArrayList<>();
                        List<Integer> epicIdsToSave = new ArrayList<>();
                        try{
                            customEpicLinks = Arrays.asList(epicsArray.toArray());
                        } catch (NullPointerException ex){
                            //                ex.printStackTrace();
                        }
                        if (customEpicLinks.isEmpty()){
                            System.out.println("Case ID [" + caseId + "] EPIC LINKS EMPTY");
                        } else {
                            for (Object epicObj : customEpicLinks){
                                Integer epic = ((Long) epicObj).intValue();
                                customEpicFull.add(epic);
                                if (filterEpics.contains(epic)){
                                    epicIdsToDelete.add(epic);
                                } else {
                                    epicIdsToSave.add(epic);
                                }
                            }
                            if (!epicIdsToDelete.isEmpty()){
                                System.out.println("Case ID [" + caseId + "] to DELETE: " + epicIdsToDelete);
                                casesIdsToClean.putIfAbsent(caseId, epicIdsToDelete);
                            }
                            if (!epicIdsToSave.isEmpty()){
                                System.out.println("Case ID [" + caseId + "] to SAVE: " + epicIdsToSave);
                            }
                        }
                        if (customEpicFull.equals(epicIdsToDelete)){
                            casesIdsSingleDelete.add(caseId);
                        }
                    }
                }
            } catch (IOException | APIException e){
                throw new RuntimeException(e);
            }
            offset += limit;
        }
        System.out.println("Cases to clean: " + casesIdsToClean);
        return casesIdsToClean;
    }

    private static void deleteEpicLink(Integer caseId, List<Integer> filterEpics, APIClient client){
        JSONObject responseObject1, responseObject2;
        List<Object> casesIdsToClean = new ArrayList<>();
        List<Object> customEpicLinks = new ArrayList<>();
        List<Integer> epicIds = new ArrayList<>();
        List<Integer> epicIdsToDelete = new ArrayList<>();
        List<Integer> epicIdsToSave = new ArrayList<>();
        try{
            responseObject1 = (JSONObject) client.sendGet("get_case/" + caseId);
            JSONArray epicsArray = (JSONArray) responseObject1.get("custom_epic_name");
            try{
                customEpicLinks = Arrays.asList(epicsArray.toArray());
            } catch (NullPointerException ex){
            }
            if (customEpicLinks.isEmpty()){
                System.out.println("Case ID [" + responseObject1.get("id") + "] EPIC LINKS EMPTY");
            } else {
                for (Object epicObj : customEpicLinks){
                    Integer epic = ((Long) epicObj).intValue();
                    epicIds.add(epic);
                    if (filterEpics.contains(epic)){
                        epicIdsToDelete.add(epic);
                    } else {
                        epicIdsToSave.add(epic);
                    }
                }
                if (!epicIdsToDelete.isEmpty()){
                    System.out.println("Case ID [" + responseObject1.get("id") + "] to DELETE: " + epicIdsToDelete);
                    casesIdsToClean.add(responseObject1.get("id"));
                }
                if (!epicIdsToSave.isEmpty()){
                    System.out.println("Case ID [" + responseObject1.get("id") + "] to SAVE: " + epicIdsToSave);
                }
            }
        } catch (IOException | APIException e){
            throw new RuntimeException(e);
        }
        //        org.json.JSONObject requestObject = new org.json.JSONObject("{\"custom_epic_name\": " + epicIdsToSave + "}");
        //        System.out.println(requestObject);
        //        try{
        //            responseObject2 = (JSONObject) client.sendPost("update_case/" + caseId, requestObject);
        //        } catch (IOException | APIException e){
        //            throw new RuntimeException(e);
        //        }
        //        System.out.println(responseObject2);
    }

    private static void clearEpicLinks(List<Integer> filterEpics, Integer branchId, Integer rootSection, APIClient client){
        Set<Integer> sectionList = getSections(rootSection, client);
        int caseId, limit = 250, offset = 0, size;
        boolean isNext = true;
        Set<Integer> casesIdsToClean = new HashSet<>();
        while (isNext){
            System.out.println("Limit: " + limit);
            System.out.println("Offset: " + offset);
            try{
                JSONObject responseObject = (JSONObject) client.sendGet("get_cases/1&suite_id=" + branchId + "&limit=" + limit + "&offset=" + offset);
                isNext = ((JSONObject) responseObject.get("_links")).get("next") != null;
                size = Integer.parseInt(responseObject.get("size").toString());
                System.out.println("Size: " + size);
                JSONArray casesArray = (JSONArray) responseObject.get("cases");
                for (Object obj : casesArray){
                    JSONObject caseObj = (JSONObject) obj;
                    if (sectionList.contains(((Long) caseObj.get("section_id")).intValue())){
                        caseId = ((Long) caseObj.get("id")).intValue();
                        JSONArray epicsArray = (JSONArray) caseObj.get("custom_epic_name");
                        List<Object> customEpicLinks = new ArrayList<>();
                        List<Integer> epicIdsToDelete = new ArrayList<>();
                        List<Integer> epicIdsToSave = new ArrayList<>();
                        try{
                            customEpicLinks = Arrays.asList(epicsArray.toArray());
                        } catch (NullPointerException ex){
                            //                ex.printStackTrace();
                        }
                        if (customEpicLinks.isEmpty()){
                            System.out.println("Case ID [" + caseId + "] EPIC LINKS EMPTY");
                        } else {
                            for (Object epicObj : customEpicLinks){
                                Integer epic = ((Long) epicObj).intValue();
                                if (filterEpics.contains(epic)){
                                    epicIdsToDelete.add(epic);
                                } else {
                                    epicIdsToSave.add(epic);
                                }
                            }
                            if (!epicIdsToSave.isEmpty()){
                                System.out.println("Case ID [" + caseId + "] to SAVE: " + epicIdsToSave);
                            }
                            if (!epicIdsToDelete.isEmpty()){
                                System.out.println("Case ID [" + caseId + "] to DELETE: " + epicIdsToDelete);
                                casesIdsToClean.add(caseId);
                                //                                org.json.JSONObject requestObject = new org.json.JSONObject("{\"custom_epic_name\": " + epicIdsToSave + "}");
                                //                                System.out.println("UPDATING case [" + caseId + "] with REQUEST: " + requestObject);
                                JSONObject responseObject2;
                                //                                try{
                                //                                    responseObject2 = (JSONObject) client.sendPost("update_case/" + caseId, requestObject);
                                //                                } catch (IOException | APIException e){
                                //                                    throw new RuntimeException(e);
                                //                                }
                                //                                System.out.println(responseObject2);
                            }
                        }
                    }
                }
            } catch (IOException | APIException e){
                throw new RuntimeException(e);
            }
            System.out.println("Cases to clean: " + casesIdsToClean);
            offset += limit;
        }
    }

    private static void getEmptyEpicLinks(APIClient client){
        Long caseId;
        int limit = 250, offset = 0, size;
        boolean isNext = true;
        List<Object> casesWithEmptyEpics = new ArrayList<>();
        while (isNext){
            System.out.println("Limit: " + limit);
            System.out.println("Offset: " + offset);
            JSONObject responseObject;
            try{
                responseObject = (JSONObject) client.sendGet("get_cases/1&suite_id=1&limit=" + limit + "&offset=" + offset);
            } catch (IOException | APIException e){
                throw new RuntimeException(e);
            }
            isNext = ((JSONObject) responseObject.get("_links")).get("next") != null;
            size = Integer.parseInt(responseObject.get("size").toString());
            System.out.println("Size: " + size);
            JSONArray casesArray = (JSONArray) responseObject.get("cases");
            for (Object obj : casesArray){
                JSONObject caseObj = (JSONObject) obj;
                caseId = (Long) caseObj.get("id");
                JSONArray epicsArray = (JSONArray) caseObj.get("custom_epic_name");
                List<Object> customEpicLinks = new ArrayList<>();
                List<Integer> epicIds = new ArrayList<>();
                try{
                    customEpicLinks = Arrays.asList(epicsArray.toArray());
                } catch (NullPointerException ex){
                }
                if (customEpicLinks.isEmpty()){
                    System.out.println("Case ID [" + caseId + "] EPIC LINKS EMPTY");
                    casesWithEmptyEpics.add(caseId);
                }
            }
            offset += limit;
        }
        System.out.println("EMPTY EPICS: " + casesWithEmptyEpics);
    }

    private static Map<String, String> checkTmsLinks(String suitePath, APIClient client){
        Map<String, String> testIdList = new HashMap<>();
        List<String> fileLines = parseFileLines(suitePath);
        List<String> annotationBlock = getTestAnnotationBlock(fileLines);
        Map<String, String> tmsMap = getTmsTestMap(annotationBlock);
        JSONObject caseObj;
        for (Map.Entry<String, String> tmsEntry : tmsMap.entrySet()){
            int caseId = Integer.parseInt(tmsEntry.getKey()), automationStatus;
            String testName = tmsEntry.getValue();
            System.out.println("Case id: " + caseId);
            try{
                caseObj = getCase(caseId, client);
                automationStatus = ((Long) caseObj.get("custom_automation_status")).intValue();
                switch (automationStatus){
                    case 1:
                        testIdList.put(testName, "Not automated");
                        break;
                    case 2:
                        testIdList.put(testName, "Automated");
                        break;
                    case 3:
                        testIdList.put(testName, "Can't automate");
                }
            } catch (APIException | IOException exc){
                testIdList.put(testName, "Wrong ID");
                System.err.println("API Error: no such test case ID [" + caseId + "]");
            } catch (NullPointerException ex){
                testIdList.put(testName, "Not automated");
            }
        }
        return testIdList;
    }

    //</editor-fold>

    //<editor-fold desc="Methods">
    private static JSONObject getCase(Integer caseId, APIClient client) throws APIException, IOException{
        //        try{
        //            return (JSONObject) client.sendGet("get_case/" + caseId);
        //        } catch (IOException | APIException e){
        //            throw new RuntimeException(e);
        //        }
        return (JSONObject) client.sendGet("get_case/" + caseId);
    }

    private static JSONObject getProject(Integer projectId, APIClient client){
        try{
            return (JSONObject) client.sendGet("get_project/" + projectId);
        } catch (IOException | APIException e){
            throw new RuntimeException(e);
        }
    }

    private static String getSuites(Integer projectId, APIClient client){
        try{
            JSONArray suites = (JSONArray) client.sendGet("get_suites/" + projectId);
            return suites.toString();
        } catch (IOException | APIException e){
            throw new RuntimeException(e);
        }
    }

    private static Set<Integer> getSections(Integer rootSection, APIClient client){
        Integer parentId, cnt;
        Set<Integer> sectionList = new HashSet<>();
        sectionList.add(rootSection);
        try{
            JSONObject responseObject = (JSONObject) client.sendGet("get_sections/1&suite_id=1");
            JSONArray sectionsArray = (JSONArray) responseObject.get("sections");
            cnt = ((Long) responseObject.get("size")).intValue();
            while (cnt > 0){
                for (Object obj : sectionsArray){
                    JSONObject sectionObj = (JSONObject) obj;
                    try{
                        parentId = ((Long) sectionObj.get("parent_id")).intValue();
                    } catch (NullPointerException nex){
                        parentId = null;
                    }
                    if (sectionList.contains(parentId)){
                        sectionList.add(Integer.valueOf(sectionObj.get("id").toString()));
                    }
                }
                cnt--;
            }
            System.out.println("Collected sections::");
            System.out.println(sectionList);
            System.out.println(sectionList.size());
        } catch (IOException | APIException e){
            throw new RuntimeException(e);
        }
        return sectionList;
    }

    private static List<String> parseFileLines(String filePath){
        ArrayList<String> lines = new ArrayList<>();
        String strLine;
        FileInputStream stream = null;
        try{
            stream = new FileInputStream(filePath);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        try{
            while ((strLine = reader.readLine()) != null){
                lines.add(strLine);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        try{
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        return lines;
    }

    private static List<String> getTestAnnotationBlock(List<String> fileLines){
        ArrayList<String> annotationBlock = new ArrayList<>();
        int sublistBegin = 0, sublistEnd;
        String line;
        for (int i = 0; i < fileLines.size(); i++){
            line = fileLines.get(i);
            if (line.contains("@Test")){
                sublistBegin = i;
            }
            if (line.contains("public void")){
                sublistEnd = i + 1;
                annotationBlock.addAll(fileLines.subList(sublistBegin, sublistEnd));
            }
        }
        return annotationBlock;
    }

    private static Map<String, String> getTmsTestMap(List<String> annotations){
        Map<String, String> tmsTestMap = new HashMap<>();
        List<String> tmsLinks = new ArrayList<>();
        List<String> testNames = new ArrayList<>();
        for (String tmsLink : annotations){
            if (tmsLink.contains("@TmsLink")){
                tmsLinks.add(tmsLink.substring(tmsLink.indexOf("(\"") + 2, tmsLink.lastIndexOf("\")")).trim());
            }
        }
        for (String testName : annotations){
            if (testName.contains("public void")){
                testNames.add(testName.substring(testName.indexOf("void") + 4, testName.lastIndexOf("(")).trim());
            }
        }
        for (int i = 0; i < tmsLinks.size(); i++){
            tmsTestMap.put(tmsLinks.get(i), testNames.get(i));
        }
        return tmsTestMap;
    }
    //</editor-fold>


}
