package emailClient.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import emailClient.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaException;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomeController {

    public static final String HOST_NAME = "smtp.gmail.com";
    public static final int SSL_PORT = 465;
    public static final int TSL_PORT = 587;


    private ObservableList<String> optionMail = FXCollections.observableArrayList("TO", "CC", "BCC");

    @FXML
    private TextField tfRecipient;
    @FXML
    private TextField tfSubject;
    @FXML
    private TextField tfLoginEmail;
    @FXML
    private TextField tfLoginPassword;
    @FXML
    private ChoiceBox<String> optionMailChoiceBox = new ChoiceBox<>();
    @FXML
    private FontAwesomeIcon iconErrorRecipient = new FontAwesomeIcon();
    @FXML
    private FontAwesomeIcon iconErrorSubject = new FontAwesomeIcon();
    @FXML
    private HTMLEditor mailContent = new HTMLEditor();



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
        properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST_NAME);
        properties.put("mail.smtp.socketFactory.port", SSL_PORT);
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.port", SSL_PORT);

        login_email = tfLoginEmail.getText().trim();
        login_password = tfLoginPassword.getText().trim();


        try {
            // set Session
            session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(login_email, login_password);
                }
            });

            // login with google account
            Transport transport = null;
            System.out.println(session);
            transport = session.getTransport("smtps");
            transport.connect (HOST_NAME, SSL_PORT, login_email, login_password);
            System.out.println("Login successfully !!");
            // login successfully => show home layout
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
            mainApp.showHomeLayout();

        }
        else {
            showAlert("Đăng nhập thất bại!", "Sai tài khoản hoặc mật khẩu!");
        }
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
        this.stageDialog = stageDialog;
    }

    public void setMainApp(Main main) {
        this.mainApp = main;
    }
}
