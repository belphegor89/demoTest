package common.utils;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import common.UserSettings;
import data.StaticData;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.file.FileSystems;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicUtility {

    public Long getTimestamp(){
        return System.currentTimeMillis();
    }

    public LocalDateTime getCurrentDateTime(ZoneId zoneId){
        return LocalDateTime.now().atZone(zoneId).toLocalDateTime();
    }

    public LocalDateTime getCurrentDateTime(){
        return LocalDateTime.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    public String getCurrentDateTimeFormatted(String format, Locale locale){
        /*Date format example - d MMMM yyyy, Locale.ENGLISH = 1 april 2022 */
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(format, locale));
    }

    public String getDateTimeFormatted(LocalDateTime dateTime, String format, Locale locale){
        /*Date format example - d MMMM yyyy, Locale.ENGLISH = 1 april 2022 */
        return dateTime.format(DateTimeFormatter.ofPattern(format, locale));
    }

    public String getDateTimeFormatted(LocalDateTime dateTime, String format){
        /*Date format example - d MMMM yyyy, Locale.ENGLISH = 1 april 2022 */
        return dateTime.format(DateTimeFormatter.ofPattern(format, StaticData.defaultLocale));
    }

    public URL getUrlFromString(String link){
        URL url;
        try{
            url = new URL(link);
        } catch (MalformedURLException e){
            throw new RuntimeException(e);
        }
        return url;
    }

    protected int getFreePort() {
        int localPort = 0;
        try (ServerSocket socket = new ServerSocket(0)){
            socket.setReuseAddress(true);
            localPort = socket.getLocalPort();
            System.out.println("SSH using port " + localPort);
        } catch (Exception e){
            e.printStackTrace();
        }
        return localPort;
    }

    public Session sshSessionStart(String internalIp, int internalPort, int externalPort){
        Session session = null;
        String sshHost = UserSettings.stageLanIp;
        int sshPort = 53473;
        String sshUser = "qaautotest";
        try{
            JSch jsch = new JSch();
            jsch.addIdentity(SystemUtility.getPathCanonical(UserSettings.resourcesFolder) + FileSystems.getDefault().getSeparator() + "qaStageSsh");
            session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setTimeout(15000);
            session.setConfig("PreferredAuthentications", "publickey");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            session.setPortForwardingL(internalPort, internalIp, externalPort);
        } catch (JSchException e){
            if (session != null && session.isConnected()){
                session.disconnect();
            }
            e.printStackTrace();
        }
        return session;
    }

    public void sshSessionStop(Session session){
        try{
            session.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject apiGetJson(String requestUrl) {
        System.out.println("Sending [GET] request on URL [" + requestUrl + "]");
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Response Code : " + responseCode);
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONParser parser = new JSONParser();
                return (JSONObject) parser.parse(response.toString());
            } else {
                System.err.println("Error while sending request. Response code: " + responseCode);
                return null;
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String printCallerLineNumber(int stackNumber) {
        //stackNumber regulates, how high on call levels we should go
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[stackNumber];
        String className = caller.getClassName();
        String methodName = caller.getMethodName();
        int lineNumber = caller.getLineNumber();
        return "Called from method: " + className + "." + methodName + ":: " + lineNumber;
    }

    public Instant timerStart() {
        return Instant.now();
    }

    public Long timerStop(Instant start) {
        Instant finish = Instant.now();
        return Duration.between(start, finish).toMillis();
    }

    public <K extends Enum<K>, V> Map<K, V> sortByEnumOrdinal(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey(Comparator.comparingInt(Enum::ordinal)))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new // Maintain sorted order
                ));
    }


}
