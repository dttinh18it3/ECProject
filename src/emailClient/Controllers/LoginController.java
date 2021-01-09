package emailClient.Controllers;

import emailClient.Main;
import emailClient.Models.MessageModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import javax.mail.*;
import java.io.IOException;
import java.util.Properties;

import static emailClient.Main.dashboardLayout;
import static emailClient.Main.showAlert;

public class LoginController {

    @FXML
    private TextField tfLoginEmail;
    @FXML
    private TextField tfLoginPassword;

    public static final String HOST_NAME = "smtp.gmail.com";
    public static final int SSL_PORT = 465;
    public static final int TSL_PORT = 587;


    public static Session session;
    public static String login_email = "";
    public static String login_password = "";
    private static Properties properties;

    @FXML
    private void initialize() {
        pressBtnEnter();
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

//  IMAP
        properties.setProperty("mail.store.protocol", "imaps");

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


    public void showHomeLayout() {
        try {
            //init home layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Views/fxml/homeLayout.fxml"));
            AnchorPane homeLayout = (AnchorPane) loader.load();
            //setup home layout into the center of dashboard layout
            dashboardLayout.setCenter(homeLayout);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //button login
    @FXML
    public void handleLoginValidate() {
        if (connectAuthenticatedToServer()) {
            showHomeLayout();
        }
        else {
            showAlert("FAILURE!", "Email or password incorrect!");
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



}
