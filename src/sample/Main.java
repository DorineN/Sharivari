package sample;

import javafx.scene.image.Image;
import sample.controller.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    private static User myUser = new User();

    // Main method
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initRootLayout();

        showConnection();
    }

    // Routes
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/sample.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            // Show the Sharin icon
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images/logo.png")));
            primaryStage.setTitle("Sharin");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConnection() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ConnectionView.fxml"));
            AnchorPane connectionOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Connexion");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(connectionOverview);

            // Give the controller access to the main app.
            ConnectionController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Home
    public void showHome() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/HomeView.fxml"));
            AnchorPane homeOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Accueil");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(homeOverview);

            // Give the controller access to the main app.
            HomeController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Subscribe
    public void showSubscribe() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/SubscribeView.fxml"));
            AnchorPane inscriptionOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Inscription");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(inscriptionOverview);

            // Give the controller access to the main app.
            SubscribeController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // My Account
    public void showMyAccount() throws IOException {
        try {
            // Load my account overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/MyAccountView.fxml"));
            AnchorPane myAccountOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Mon compte");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(myAccountOverview);

            // Give the controller access to the main app.
            MyAccountController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters & Setters
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public static User getMyUser() {
        return myUser;
    }

    public static void setMyUser(User myUser) {
        Main.myUser = myUser;
    }
}