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
    public static Project myProject = new Project();
    public static PluginsLoader pluginsLoader;

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
            //Load the css file
            scene.getStylesheets().add(getClass().getResource("style/style.css").toExternalForm());
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


            this.primaryStage.setResizable(false);
            this.primaryStage.setTitle("Sharin - Connection");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(connectionOverview);

            pluginsLoader = new PluginsLoader();
            pluginsLoader.loadAllStringPlugins();


            // Give the controller access to the main app.
            ConnectionController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
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

            this.primaryStage.setResizable(true);
            this.primaryStage.setMinWidth(1280);
            this.primaryStage.setMinHeight(800);
            this.primaryStage.setTitle("Sharin - Home");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(homeOverview);

            // Give the controller access to the main app.
            HomeController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Project
    public void showProject() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ProjectView.fxml"));
            AnchorPane projectOverview = loader.load();

            this.primaryStage.setResizable(true);
            this.primaryStage.setMinWidth(1280);
            this.primaryStage.setMinHeight(800);
            this.primaryStage.setTitle("Sharin - Project");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(projectOverview);

            // Give the controller access to the main app.
            ProjectController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Project
    public void showCalendar() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CalendarView.fxml"));
            AnchorPane calendarOverview = loader.load();

            this.primaryStage.setResizable(true);
            this.primaryStage.setMinWidth(1280);
            this.primaryStage.setMinHeight(800);
            this.primaryStage.setTitle("Sharin - Calendar");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(calendarOverview);

            // Give the controller access to the main app.
            CalendarController controller = loader.getController();
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


            this.primaryStage.setResizable(false);
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

    // Create Project
    public void showCreateProject() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CreateProjectView.fxml"));
            AnchorPane createProjectOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Créer votre projet");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(createProjectOverview);

            // Give the controller access to the main app.
            CreateProjectController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update Project
    public void showManageProject() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ManageProjectView.fxml"));
            AnchorPane manageProjectOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Créer votre projet");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(manageProjectOverview);

            // Give the controller access to the main app.
            ManageProjectController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create Task
    public void showCreateTask() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CreateTaskView.fxml"));
            AnchorPane projectOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Créer une nouvelle tâche");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(projectOverview);

            // Give the controller access to the main app.
            TaskController controller = loader.getController();
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
            loader.setLocation(Main.class.getResource("view/AccountView.fxml"));
            AnchorPane myAccountOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Mon compte");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(myAccountOverview);

            // Give the controller access to the main app.
            AccountController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPlugin() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PluginView.fxml"));
            AnchorPane pluginOverview = loader.load();


            this.primaryStage.setResizable(false);
            this.primaryStage.setTitle("Sharin - Plugin");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(pluginOverview);

            // Give the controller access to the main app.
            PluginController controller = loader.getController();
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

    //User
    public static User getMyUser() {
        return myUser;
    }

    public static void setMyUser(User myUser) {
        Main.myUser = myUser;
    }

    //Project
    public static void setMyProject(Project myProject) {
        Main.myProject = myProject;
    }

    public static Project getMyProject() {
        return myProject;
    }


}