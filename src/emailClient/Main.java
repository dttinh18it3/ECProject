package emailClient;

import emailClient.Controllers.HomeController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.IIOException;
import java.io.IOException;

public class Main extends Application {
    private BorderPane dashboardLayout;
    public static Stage primaryStage;


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
    public void showHomeLayout() {
        try {
            //init home layout
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("Views/fxml/homeLayout.fxml"));
            AnchorPane homeLayout = (AnchorPane) loader.load();
            //setup home layout into the center of dashboard layout
            dashboardLayout.setCenter(homeLayout);
            HomeController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    show login layout into the center of dashboard layout
    public void showLoginLayout() {
        try {
            FXMLLoader loadder = new FXMLLoader();
            loadder.setLocation(Main.class.getResource(("Views/fxml/loginForm.fxml")));
            AnchorPane loginLayout = (AnchorPane) loadder.load();
            //set login layout into the center of dashboard layout
            dashboardLayout.setCenter(loginLayout);
            HomeController controller = loadder.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
