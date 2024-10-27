package data.dataobject;

import jakarta.mail.Address;
import jakarta.mail.internet.InternetAddress;

public class EmailDO {
    private String recipientEmail, recipientName;
    private String authorEmail, authorName;
    private String title;
    private String bodyHtml, bodyPlain;

    public EmailDO() {
    }

    public EmailDO(String recipient, String title, String bodyHtml) {
        this.recipientEmail = recipient;
        this.title = title;
        this.bodyHtml = bodyHtml;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public void setRecipient(Address recipientAddress) {
        InternetAddress address = (InternetAddress) recipientAddress;
        this.recipientEmail = address.getAddress();
        this.recipientName = address.getPersonal();
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setAuthor(Address authorAddress) {
        InternetAddress address = (InternetAddress) authorAddress;
        this.authorEmail = address.getAddress();
        this.authorName = address.getPersonal();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getBodyPlain() {
        return bodyPlain;
    }

    public void setBodyPlain(String bodyPlain) {
        this.bodyPlain = bodyPlain;
    }
}
