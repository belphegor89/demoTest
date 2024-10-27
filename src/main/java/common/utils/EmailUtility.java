package common.utils;

import common.UserSettings;
import common.Waits;
import data.StaticData;
import data.UtilityEnums;
import data.dataobject.EmailDO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.search.FlagTerm;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.asserts.Assertion;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

import static pages.BasePage.EXPLICIT_TIMEOUT;

public class EmailUtility {
    private final Assertion hardAssert = new Assertion();
    private final BasicUtility Util = new BasicUtility();
    private final ParseAndFormatUtility ParseUtil = new ParseAndFormatUtility();
    private final Waits Wait = new Waits();

    public EmailDO getEmailByRecipientTitle(String recipient, String title) {
        EmailDO mail;
        if (UserSettings.runOnStage) {
            mail = searchEmailByRecipientSubjectGmail(recipient, title);
        } else {
            mail = searchEmailByRecipientSubjectMailhog(recipient, title);
        }
        hardAssert.assertNotNull(mail, "Email with title: [" + title + "] was not found");
        return mail;
    }

    public EmailDO getEmailByTitle(String title) {
        EmailDO mail;
        if (UserSettings.runOnStage) {
            mail = searchEmailBySubjectGmail(title);
        } else {
            mail = searchEmailBySubjectMailhog(title);
        }
        hardAssert.assertNotNull(mail, "Email with title: [" + title + "] was not found");
        return mail;
    }

    private EmailDO searchEmailByRecipientSubjectGmail(String recipient, String searchQuery) {
        String username = StaticData.GMAIL_TEST_EMAIL, password = StaticData.GMAIL_APP_PASSWORD;
        EmailDO mail = new EmailDO();
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        try {
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            for (int i = 0; i < EXPLICIT_TIMEOUT; i++) {
                Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
                Arrays.sort(messages, (m1, m2) -> {
                    try {
                        return m2.getReceivedDate().compareTo(m1.getReceivedDate());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
                for (Message message : messages) {
                    if (Arrays.stream(message.getAllRecipients()).anyMatch(address -> address.toString().contains(recipient)) && message.getSubject().contains(searchQuery)) {
                        mail.setTitle(message.getSubject());
                        mail.setAuthor(message.getFrom()[0]);
                        mail.setRecipientEmail(message.getAllRecipients()[0].toString());
                        mail.setBodyHtml(getTextFromMimeMultipart((MimeMultipart) message.getContent(), "text/html"));
                        mail.setBodyPlain(getTextFromMimeMultipart((MimeMultipart) message.getContent(), "text/plain"));
                        emailFolder.getMessage(message.getMessageNumber()).getContent();
                        emailFolder.close(false);
                        store.close();
                        return mail;
                    }
                }
                Wait.sleep(1000);
            }
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            System.err.println("ERROR:");
            e.printStackTrace();
            e.getMessage();
        }
        return null;
    }

    private EmailDO searchEmailBySubjectGmail(String searchQuery) {
        String username = StaticData.GMAIL_TEST_EMAIL, password = StaticData.GMAIL_APP_PASSWORD;
        EmailDO mail = new EmailDO();
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        try {
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore("imaps");
            store.connect("imap.gmail.com", username, password);
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);
            for (int i = 0; i < EXPLICIT_TIMEOUT; i++) {
                Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.RECENT), false));
                Arrays.sort(messages, (m1, m2) -> {
                    try {
                        return m2.getReceivedDate().compareTo(m1.getReceivedDate());
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
                for (Message message : messages) {
                    if (message.getSubject().contains(searchQuery)) {
                        mail.setTitle(message.getSubject());
                        mail.setAuthor(message.getFrom()[0]);
                        mail.setRecipientEmail(message.getAllRecipients()[0].toString());
                        mail.setBodyHtml(getTextFromMimeMultipart((MimeMultipart) message.getContent(), "text/html"));
                        mail.setBodyPlain(getTextFromMimeMultipart((MimeMultipart) message.getContent(), "text/plain"));
                        emailFolder.getMessage(message.getMessageNumber()).getContent();
                        emailFolder.close(false);
                        store.close();
                        return mail;
                    }
                }
                Wait.sleep(1000);
            }
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
        return null;
    }

    private EmailDO searchEmailByRecipientSubjectMailhog(String recipient, String searchQuery) {
        EmailDO mail = new EmailDO();
        String escapedRecipient = URLEncoder.encode(recipient, StandardCharsets.UTF_8);
        JSONObject searchResult = searchEmailMessages(UtilityEnums.mailhogSearchTypes.TO, escapedRecipient, 0, 10);
        JSONArray itemsArray = (JSONArray) searchResult.get("items");
        for (int i = 0; i < EXPLICIT_TIMEOUT; i++) {
            for (Object item : itemsArray) {
                JSONObject itemObject = (JSONObject) item;
                JSONArray subj = (JSONArray) ((JSONObject) ((JSONObject) itemObject.get("Content")).get("Headers")).get("Subject");
                if (subj.get(0).toString().contains(searchQuery)) {
                    mail.setTitle(subj.get(0).toString());
                    mail.setBodyHtml(ParseUtil.formatHtmlUnescapeCharacters(ParseUtil.parseByJSONPath(itemObject, "$.MIME.Parts[1].Body")));
                    mail.setBodyPlain(ParseUtil.formatHtmlUnescapeCharacters(ParseUtil.parseByJSONPath(itemObject, "$.MIME.Parts[0].Body")));
                    try {
                        String fromString, email, name;
                        fromString = ParseUtil.parseByJSONPath(itemObject, "$.Content.Headers.From[0]");
                        name = fromString.substring(0, fromString.indexOf("<") - 1).trim();
                        email = fromString.substring(fromString.indexOf("<") + 1, fromString.indexOf(">"));
                        mail.setAuthor(new InternetAddress(email, name));
                        mail.setRecipientEmail(ParseUtil.parseByJSONPath(itemObject, "$.Content.Headers.To[0]"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return mail;
                }
            }
            Wait.sleep(1000);
        }
        return null;
    }

    private EmailDO searchEmailBySubjectMailhog(String searchQuery) {
        EmailDO mail = new EmailDO();
        String escapedQuery = URLEncoder.encode(searchQuery, StandardCharsets.UTF_8);
        JSONObject searchResult = searchEmailMessages(UtilityEnums.mailhogSearchTypes.CONTAINING, escapedQuery, 0, 10);
        JSONArray itemsArray = (JSONArray) searchResult.get("items");
        for (int i = 0; i < EXPLICIT_TIMEOUT; i++) {
            for (Object item : itemsArray) {
                JSONObject itemObject = (JSONObject) item;
                JSONArray subj = (JSONArray) ((JSONObject) ((JSONObject) itemObject.get("Content")).get("Headers")).get("Subject");
                if (subj.get(0).toString().contains(searchQuery)) {
                    mail.setTitle(subj.get(0).toString());
                    mail.setBodyHtml(ParseUtil.formatHtmlUnescapeCharacters(ParseUtil.parseByJSONPath(itemObject, "$.MIME.Parts[1].Body")));
                    mail.setBodyPlain(ParseUtil.formatHtmlUnescapeCharacters(ParseUtil.parseByJSONPath(itemObject, "$.MIME.Parts[0].Body")));
                    try {
                        String fromString, email, name;
                        fromString = ParseUtil.parseByJSONPath(itemObject, "$.Content.Headers.From[0]");
                        name = fromString.substring(0, fromString.indexOf("<") - 1).trim();
                        email = fromString.substring(fromString.indexOf("<") + 1, fromString.indexOf(">"));
                        mail.setAuthor(new InternetAddress(email, name));
                        mail.setRecipientEmail(ParseUtil.parseByJSONPath(itemObject, "$.Content.Headers.To[0]"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return mail;
                }
            }
            Wait.sleep(1000);
        }
        return null;
    }

    private JSONObject searchEmailMessages(UtilityEnums.mailhogSearchTypes searchType, String searchQuery, int start, int limit) {
        return Util.apiGetJson("http://localhost:6081/api/v2/search?kind=" + searchType.name().toLowerCase() + "&query=" + searchQuery + "&start=" + start + "&limit=" + limit);
    }

    private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart, String mimeType) throws MessagingException, IOException {
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain") && mimeType.equals("text/plain")) {
                return (String) bodyPart.getContent();
            } else if (bodyPart.isMimeType("text/html") && mimeType.equals("text/html")) {
                return (String) bodyPart.getContent();
            }
        }
        return null;
    }

}
