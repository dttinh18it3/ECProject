package emailClient.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import emailClient.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

import static emailClient.Controllers.HomeController.dialogStage;
import static emailClient.Controllers.LoginController.login_email;
import static emailClient.Main.showAlert;

public class SendMessageController {

    @FXML
    private FontAwesomeIcon iconErrorRecipient = new FontAwesomeIcon();
    @FXML
    private FontAwesomeIcon iconErrorSubject = new FontAwesomeIcon();
    @FXML
    private HTMLEditor mailContent = new HTMLEditor();
    @FXML
    private TextField tfRecipient;
    @FXML
    private TextField tfSubject;

    private final ObservableList<String> optionMail = FXCollections.observableArrayList("TO", "CC", "BCC");

    @FXML
    private ChoiceBox<String> optionMailChoiceBox;





    private Message sendMessage(Session session)  {
        try {
            String recipient = tfRecipient.getText();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(login_email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(tfSubject.getText());
            message.setContent(mailContent.getHtmlText(), "text/html");
            Transport.send(message);
        } catch (Exception e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, String.valueOf(e), e);
        }
        return null;
    }

    //validate fields data of send mail dialog
    private boolean validateData() {
        String recipientValue = tfRecipient.getText().trim();
        String subjectValue = tfSubject.getText().trim();
        if (!recipientValue.isEmpty() && !subjectValue.isEmpty()) {
            iconErrorRecipient.getStyleClass().add("iconErrorRecipient");
            iconErrorSubject.getStyleClass().add("iconErrorSubject");
            return true;
        }
        else {
            if(recipientValue.isEmpty()) {
                iconErrorRecipient.getStyleClass().clear();
            } else {
                iconErrorRecipient.getStyleClass().add("iconErrorRecipient");
            }
            if(subjectValue.isEmpty()) {
                iconErrorSubject.getStyleClass().clear();
            } else {
                iconErrorSubject.getStyleClass().add("iconErrorSubject");
            }
            return false;
        }
    }

    //button send mail
    @FXML
    private void handleSendMail() {
        if (validateData()) {
            String recipientValue = tfRecipient.getText().trim();
            String subjectValue = tfSubject.getText().trim();
            System.out.println(recipientValue + " | " + subjectValue);
            try {
                sendMessage(LoginController.session);
                dialogStage.close();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            showAlert("Cảnh báo!", "Vui lòng nhập đầy đủ thông tin thư!");
        }
    }

    //checkbox option mail (TO/CC/BCC)
    private void loadOptionMail() {
        optionMailChoiceBox.setItems(optionMail);
        optionMailChoiceBox.setValue("TO");
    }

}
