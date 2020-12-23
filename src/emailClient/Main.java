package emailClient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static BorderPane dashboardLayout;
    public static Stage primaryStage;
    public static Stage stageDialog;



    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        primaryStage.setTitle("EC Client");
        primaryStage.getIcons().add(new Image("file:src/emailClient/Views/images/EC_icon.png"));

        initDashboardLayout();
        showLoginLayout();
    }

    public Main() {
    }

    //    init dashboard layout, primary stage
    public void initDashboardLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Views/fxml/dashboard.fxml"));
            dashboardLayout = (BorderPane) loader.load();

            Scene scene = new Scene(dashboardLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

//    show home layout into the center of dashboard layout

//    show login layout into the center of dashboard layout
    public void showLoginLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource(("Views/fxml/loginForm.fxml")));
            AnchorPane loginLayout = (AnchorPane) loader.load();
            //set login layout into the center of dashboard layout
            dashboardLayout.setCenter(loginLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showAlert(String title, String content) {
        Alert alertWarning = new Alert(Alert.AlertType.NONE);
        alertWarning.initOwner(stageDialog);
        alertWarning.setTitle(title);
        alertWarning.setContentText(content);
        alertWarning.getDialogPane().getButtonTypes().add(ButtonType.OK);
        alertWarning.showAndWait();
    }

    public void setDialog(Stage stageDialog) {
        this.stageDialog = stageDialog;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
