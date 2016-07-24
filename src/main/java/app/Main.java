package app;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import app.controller.*;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import app.model.Task;
import app.util.PluginsLoader;
import app.model.Project;
import app.model.User;

import java.io.IOException;


public class Main extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;

    private static User myUser = new User();
    public static Project myProject = new Project();
    public static PluginsLoader pluginsLoader;
    private static Task myTask = new Task();
    public  AnchorPane menuProject;
    public  AnchorPane menuHome;

    // Main method
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        initRootLayout();

        //showConnection();
        showChat();
    }

    // Routes
    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/sample.fxml"));
            rootLayout = loader.load();

            // Load Menu Project overview.
            FXMLLoader loaderMenu = new FXMLLoader();
            loaderMenu.setLocation(getClass().getClassLoader().getResource("view/MenuProjectView.fxml"));
            menuProject = loaderMenu.load();

            rootLayout.getChildren().add(menuProject);

            // Load Menu Project overview.
            FXMLLoader loaderMenuHome = new FXMLLoader();
            loaderMenuHome.setLocation(getClass().getClassLoader().getResource("view/MenuHomeView.fxml"));
            menuHome = loaderMenuHome.load();

            rootLayout.getChildren().add(menuHome);

            menuProject.setVisible(false);
            menuHome.setVisible(false);

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            //Load the css file
            scene.getStylesheets().add(getClass().getClassLoader().getResource("style/style.css").toExternalForm());
            // Show the Sharin icon
            scene.getStylesheets().add(getClass().getClassLoader().getResource("style/style.css").toExternalForm());
            primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/logo.png")));
            primaryStage.setTitle("Sharin");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Give the controller access to the main app.
            MenuController controllerMenu = loaderMenu.getController();
            controllerMenu.setMainApp(this);

            // Give the controller access to the main app.
            MenuController controllerMenuHome = loaderMenuHome.getController();
            controllerMenuHome.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConnection() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ConnectionView.fxml"));
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
            loader.setLocation(getClass().getClassLoader().getResource("view/HomeView.fxml"));
            AnchorPane homeOverview = loader.load();


            this.primaryStage.setMinWidth(1280);
            this.primaryStage.setMinHeight(800);
            this.primaryStage.setTitle("Sharin - Home");
            this.primaryStage.setResizable(true);

            menuProject.setVisible(false);
            menuHome.setVisible(true);
            // Set person overview into the center of root layout.
            rootLayout.setCenter(homeOverview);

            // Give the controller access to the main app.
            HomeController controller = loader.getController();
            controller.setMainApp(this);

            //for the button Priority
            menuHome.toFront();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Project
    public void showProject() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ProjectView.fxml"));
            AnchorPane projectOverview = loader.load();

            menuProject.setVisible(true);
            menuHome.setVisible(false);

            this.primaryStage.setMinWidth(1280);
            this.primaryStage.setMinHeight(800);
            this.primaryStage.setTitle("Sharin - Project");



            // Set person overview into the center of root layout.
            rootLayout.setCenter(projectOverview);

            // Give the controller access to the main app.
            ProjectController controller = loader.getController();
            controller.setMainApp(this);
            menuProject.toFront();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Calendar
    public void showCalendar() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/CalendarView.fxml"));
            AnchorPane calendarOverview = loader.load();


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


    //Tchat
    /*public void showTchat() throws IOException {
        try {
            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TchatView.fxml"));
            AnchorPane tchatOverview = loader.load();


            this.primaryStage.setTitle("Sharin - Tchat");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(tchatOverview);

            // Give the controller access to the main app.
            TchatController controller = loader.getController();
            controller.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    // Subscribe
    public void showSubscribe() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/SubscribeView.fxml"));
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

    // Create Project
    public void showCreateProject() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/CreateProjectView.fxml"));
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
            loader.setLocation(getClass().getClassLoader().getResource("view/ManageProjectView.fxml"));
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
            loader.setLocation(getClass().getClassLoader().getResource("view/CreateTaskView.fxml"));
            AnchorPane createTaskOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Créer une nouvelle tâche");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(createTaskOverview);

            // Give the controller access to the main app.
            CreateTaskController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Gantt diagramm
    public void showGantt() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/GanttView.fxml"));
            AnchorPane ganttOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Créez votre propre Gantt");

            this.primaryStage.setFullScreen(true);
            this.primaryStage.setFullScreenExitHint("Pour sortir de Gantt, tapez Echap");

            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

            // Set person overview into the center of root layout.
            rootLayout.setLayoutX(primaryScreenBounds.getMinX());
            rootLayout.setLayoutY(primaryScreenBounds.getMinY());
            rootLayout.setPrefWidth(primaryScreenBounds.getWidth());
            rootLayout.setPrefHeight(primaryScreenBounds.getHeight());
            rootLayout.setCenter(ganttOverview);

            // Give the controller access to the main app.
            GanttController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Update Task
    public void showManageTask() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ManageTaskView.fxml"));
            AnchorPane manageTaskOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Mettez à jour une tâche");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(manageTaskOverview);

            // Give the controller access to the main app.
            ManageTaskController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Sho all Project Tasks
    public void showTask() throws IOException {
        try {
            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/TaskView.fxml"));
            AnchorPane taskOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Liste des tâches");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(taskOverview);

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
            loader.setLocation(getClass().getClassLoader().getResource("view/AccountView.fxml"));
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
            loader.setLocation(getClass().getClassLoader().getResource("view/PluginView.fxml"));
            AnchorPane pluginOverview = loader.load();


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

    public void showSharedFiles() throws IOException{
        try {
            // Load the shared files overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/SharedFiles.fxml"));
            AnchorPane sharedFiles = loader.load();

            this.primaryStage.setTitle("Sharin - Fichiers partagés");
            this.primaryStage.setWidth(700);
            this.primaryStage.setHeight(550);

            rootLayout.setCenter(sharedFiles);

            // Give the controller access to the main app.
            FileController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChat() throws IOException{
        try {
            // Load the shared files overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ChatView.fxml"));
            HBox chat = loader.load();

            this.primaryStage.setTitle("Sharin - Chat");
            this.primaryStage.setResizable(false);
            rootLayout.setCenter(chat);

            // Give the controller access to the main app.
            ChatController controller = loader.getController();
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

    //Task
    public static void setMyTask(Task myTask) {
        Main.myTask = myTask;
    }

    public static Task getMyTask() {
        return myTask;
    }



}