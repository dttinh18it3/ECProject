package emailClient.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.activation.DataHandler;
import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Enumeration;

public class MessageModel {
    private final IntegerProperty mailID;
    private final StringProperty recipient;
    private final StringProperty mailSubject;
    private final StringProperty mailContent;


    public MessageModel(Integer mailID, String recipient, String mailSubject, String mailContent) {
        this.mailID = new SimpleIntegerProperty(mailID);
        this.recipient = new SimpleStringProperty(recipient);
        this.mailSubject = new SimpleStringProperty(mailSubject);
        this.mailContent = new SimpleStringProperty(mailContent);
    }

    public int getMailID() {
        return mailID.get();
    }

    public IntegerProperty mailIDProperty() {
        return mailID;
    }

    public void setMailID(int mailID) {
        this.mailID.set(mailID);
    }

    public String getRecipient() {
        return recipient.get();
    }

    public StringProperty recipientProperty() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient.set(recipient);
    }

    public String getMailSubject() {
        return mailSubject.get();
    }

    public StringProperty mailSubjectProperty() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject.set(mailSubject);
    }

    public String getMailContent() {
        return mailContent.get();
    }

    public StringProperty mailContentProperty() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent.set(mailContent);
    }

}
