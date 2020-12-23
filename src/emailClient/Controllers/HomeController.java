package emailClient.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import emailClient.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import emailClient.Models.MessageModel;


public class HomeController {

    public static final String HOST_NAME = "smtp.gmail.com";
    public static final int SSL_PORT = 465;
    public static final int TSL_PORT = 587;


    private final ObservableList<String> optionMail = FXCollections.observableArrayList("TO", "CC", "BCC");

    @FXML
    private TextField tfRecipient;
    @FXML
    private TextField tfSubject;
    @FXML
    private TextField tfLoginEmail;
    @FXML
    private TextField tfLoginPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private final ChoiceBox<String> optionMailChoiceBox = new ChoiceBox<>();
    @FXML
    private FontAwesomeIcon iconErrorRecipient = new FontAwesomeIcon();
    @FXML
    private FontAwesomeIcon iconErrorSubject = new FontAwesomeIcon();
    @FXML
    private HTMLEditor mailContent = new HTMLEditor();
    @FXML
    private TableView<MessageModel> messageTable = new TableView<>();
    @FXML
    public static TableColumn<MessageModel, String> emailSubjectColunm = new TableColumn<>("emailSubject");


    private Stage stageDialog;
    private static Stage dialogStage;
    private Main mainApp;
    private static Session session;
    public static String login_email = "";
    public static String login_password = "";
    private static Properties properties;

    public HomeController() {
    }

    @FXML
    private void initialize() {
        loadOptionMail();
    }

    public boolean connectAuthenticatedToServer() {
//SMTP
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST_NAME);
        properties.put("mail.smtp.socketFactory.port", SSL_PORT);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", SSL_PORT);
//POP3
        properties.put("mail.pop3s.host", "pop.gmail.com");
        properties.put("mail.pop3s.port", "995");
        properties.put("mail.pop3s.starttls.enable", "true");

        login_email = tfLoginEmail.getText().trim();
        login_password = tfLoginPassword.getText().trim();
        try {
            // set Session
            session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login_email, login_password);
                }
            });
            /* login with google account */
            Transport transport = null;
            System.out.println(session);
            transport = session.getTransport("smtps");
            transport.connect (HOST_NAME, SSL_PORT, login_email, login_password);
            System.out.println("Login successfully !!");
            /* login successfully => show home layout */
            mainApp.showHomeLayout();
            return true;
        } catch (NoSuchProviderException e) {
            System.out.println("Login failure 1 !! ");
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            //login failure
            System.out.println("Login failure 2 !!");
            e.printStackTrace();
            return false;
        }
    }

    public void getInbox() {
        // create the POP3 store object and connect with the pop server
        try {
            Store store = session.getStore("pop3s");
            store.connect();

            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                String recipient = String.valueOf(message.getFrom()[0]);
                Main.messageData.add(new MessageModel(i, recipient, message.getSubject(), message.getContent().toString()));
//                System.out.println(Main.messageData.get(i).getMailSubject());
/*
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());
*/
            }
            // close the store and folder objects

            emailFolder.close(false);
            store.close();
        } catch (NoSuchProviderException e) {
            System.out.println("Catch session get store");
            e.printStackTrace();
        } catch (MessagingException e) {
            System.out.println("Catch store connect");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Catch add messagedata");
            e.printStackTrace();
        }
    }



    //checkbox option mail (TO/CC/BCC)
    private void loadOptionMail() {
        optionMailChoiceBox.setItems(optionMail);
        optionMailChoiceBox.setValue("TO");
    }

    //button send mail
    @FXML
    private void handleSendMail() {
        if (validateData()) {
            String recipientValue = tfRecipient.getText().trim();
            String subjectValue = tfSubject.getText().trim();
            System.out.println(recipientValue + " | " + subjectValue);
            try {
                sendMessage(session);
                dialogStage.close();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            showAlert("Cảnh báo!", "Vui lòng nhập đầy đủ thông tin thư!");
        }
    }
    //button login
    @FXML
    public void handleLoginValidate() {
        if (connectAuthenticatedToServer()) {
            getInbox();
            showInboxSubject();
            mainApp.showHomeLayout();
        }
        else {
            showAlert("Đăng nhập thất bại!", "Sai tài khoản hoặc mật khẩu!");
        }
    }


    //    Press Enter button
    public void pressBtnEnter() {
        tfLoginPassword.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    handleLoginValidate();
                }
            }
        });
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
    private void showAlert(String title, String content) {
        Alert alertWarning = new Alert(AlertType.NONE);
        alertWarning.initOwner(stageDialog);
        alertWarning.setTitle(title);
        alertWarning.setContentText(content);
        alertWarning.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alertWarning.showAndWait();
    }
    
    @FXML
    private void handleCreateMessageDialog() {
        showCreateMailDialog();
    }

    private void showCreateMailDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(HomeController.class.getResource("../Views/fxml/sendMailDialog.fxml"));
            AnchorPane mailDialogPane = (AnchorPane) loader.load();

            dialogStage = new Stage();
            dialogStage.setTitle("Soạn thư");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(Main.primaryStage);
            Scene scene = new Scene(mailDialogPane);
            dialogStage.setScene(scene);
//            HomeController controller = loader.getController();
//            controller.setDialog(dialogStage);
            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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

    public void setDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }

    public void setMainApp(Main main) {
        this.mainApp = main;
        messageTable.setItems(mainApp.getMessageData());
    }


    public void showInboxSubject() {
        System.out.println(Main.messageData.get(1).mailSubjectProperty());
        emailSubjectColunm.setCellValueFactory(messageModelStringCellDataFeatures
                -> messageModelStringCellDataFeatures.getValue().mailSubjectProperty());
    }
}
