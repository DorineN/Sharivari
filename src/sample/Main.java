package sample;

import sample.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sample.modele.User;


import java.io.IOException;

public class Main extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;
    public static User myUser;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        initRootLayout();

        showConnexion();

    }

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
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////////////////////////////////////////////////   CONNECTION   //////////////////////////////////////////////////////////////////////
    public void showConnexion() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/View_Connexion.fxml"));
            AnchorPane ConnexionOverview = (AnchorPane) loader.load();

            this.primaryStage.setTitle("Tempo - Connexion");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(ConnexionOverview);

            // Give the controller access to the main app.
            Controller_Connexion controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////   HOME   //////////////////////////////////////////////////////////////////////
    public void showHome() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/View_Home.fxml"));
            AnchorPane HomeOverview = (AnchorPane) loader.load();

            this.primaryStage.setTitle("Tempo - Accueil");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(HomeOverview);

            // Give the controller access to the main app.
            Controller_Home controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ////////////////////////////////////////////////////////////////////////////   SUBSCRIBE   //////////////////////////////////////////////////////////////////////

    /**
     * Opens a dialog to create a new user. If the user clicks Confirm
     * the fields are checked and if it's valid the user created are saved
     * and injected in the database and true is returned.
     **/

    public void showSubscribe() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Controller_Subscribe.fxml"));
            AnchorPane subscribeOverview = (AnchorPane) loader.load();

            this.primaryStage.setTitle("Subscribe to Sharin");
            // Set person overview into the center of root layout.
            rootLayout.setCenter(subscribeOverview);

            // Give the controller access to the main app.
            Controller_Subscribe controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
