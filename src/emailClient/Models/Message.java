package emailClient.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Message {
    private StringProperty receiveMail;
    private StringProperty mailSubject;
    private StringProperty mailContent;


    public Message(String receiveMail, String mailSubject, String mailContent) {
        this.receiveMail = new SimpleStringProperty(receiveMail);
        this.mailSubject = new SimpleStringProperty(mailSubject);
        this.mailContent = new SimpleStringProperty(mailContent);
    }

    public String getReceiveMail() {
        return receiveMail.get();
    }

    public StringProperty receiveMailProperty() {
        return receiveMail;
    }

    public void setReceiveMail(String receiveMail) {
        this.receiveMail.set(receiveMail);
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
