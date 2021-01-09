package emailClient.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MessageModel {
    private final IntegerProperty mailID;
    private final StringProperty recipient;
    private final StringProperty mailSubject;
    private final StringProperty mailContent;
    private final StringProperty dateReceived;


    public MessageModel(Integer mailID, String recipient, String mailSubject, String mailContent, String dateReceived) {
        this.mailID = new SimpleIntegerProperty(mailID);
        this.recipient = new SimpleStringProperty(recipient);
        this.mailSubject = new SimpleStringProperty(mailSubject);
        this.mailContent = new SimpleStringProperty(mailContent);
        this.dateReceived = new SimpleStringProperty(dateReceived);
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

    public String getDateReceived() {
        return dateReceived.get();
    }

    public StringProperty dateReceivedProperty() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived.set(dateReceived);
    }

    public void setMailContent(String mailContent) {
        this.mailContent.set(mailContent);
    }

}
