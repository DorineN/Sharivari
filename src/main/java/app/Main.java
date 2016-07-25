package app;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import app.controller.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import app.model.*;
import app.util.PluginsLoader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;


public class Main extends Application {

    public Stage primaryStage;
    public static BorderPane rootLayout;

    public Scene scene;

    private static User myUser = new User();
    public static Project myProject = new Project();
    public static PluginsLoader pluginsLoader;

    public  AnchorPane menuProject;
    public  AnchorPane menuHome;

    private ChatController controllerChat = null;

    private static Task myTask = new Task();

    //Global for plugin
    public MenuProjectController controllerMenuProject;
    MenuHomeController controllerMenuHome;

    //Prepare request
    public Main() throws SQLException, ClassNotFoundException {
    }

    public static UserDAOInterface userDao = null;
    public UserDAO userDAOPlugin = new UserDAO(new MySQLConnexion().getConnexion());
    public static ProjectDAOInterface projectDAO = null;
    public ProjectDAO projectDAOPlugin = new ProjectDAO(new MySQLConnexion().getConnexion());
    public static TaskDAOInterface taskDAO = null;
    public TaskDAO taskDAOPlugin = new TaskDAO(new MySQLConnexion().getConnexion());
    public static PluginDAO pluginDAO = null;


    // Main method
    public static void main(String[] args){
        try {
            userDao = UserDAOFactory.newInstance(new MySQLConnexion().getConnexion());
            projectDAO = ProjectDAOFactory.newInstance(new MySQLConnexion().getConnexion());
            taskDAO = TaskDAOFactory.newInstance(new MySQLConnexion().getConnexion());
            pluginDAO = new PluginDAO(new MySQLConnexion().getConnexion());
        }catch(Exception e){}

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
        projectDAO.setMainApp(this);
        taskDAO.setMainApp(this);
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/sample.fxml"));
            rootLayout = loader.load();

            // Show the scene containing the root layout.
            scene = new Scene(rootLayout);

            //Load the css file
            scene.getStylesheets().add(getClass().getClassLoader().getResource("style/style.css").toExternalForm());

            // Show the Sharin icon
            primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/logo.png")));
            primaryStage.setTitle("Sharin");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showConnection() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ConnectionView.fxml"));
            AnchorPane connectionOverview = loader.load();


            this.primaryStage.setResizable(false);
            this.primaryStage.setTitle("Sharin - Connection");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(connectionOverview);

            // Load Menu Project overview.
            FXMLLoader loaderMenu = new FXMLLoader();
            loaderMenu.setLocation(getClass().getClassLoader().getResource("view/MenuProjectView.fxml"));
            menuProject = loaderMenu.load();
            menuProject.setVisible(false);

            // Give the controller access to the main app.
            controllerMenuProject = loaderMenu.getController();
            controllerMenuProject.setMainApp(this);

            rootLayout.getChildren().add(menuProject);


            // Load Menu Home overview.
            FXMLLoader loaderMenuHome = new FXMLLoader();
            loaderMenuHome.setLocation(getClass().getClassLoader().getResource("view/MenuHomeView.fxml"));
            menuHome = loaderMenuHome.load();
            menuHome.setVisible(false);
            rootLayout.getChildren().add(menuHome);

            // Give the controller access to the main app.
            controllerMenuHome = loaderMenuHome.getController();
            controllerMenuHome.setMainApp(this);


            //inialize plugin
            pluginsLoader = new PluginsLoader();
            pluginsLoader.setMainApp(this);
            pluginsLoader.checkAndLoadPlug();
            pluginsLoader.loadAllControllerPlugins();
            pluginsLoader.loadAllModelPlugins();
            pluginsLoader.loadAllViewPlugins();


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
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            //Update menu for adm
            controllerMenuHome.displayAdmin();
            controllerMenuProject.displayAdmin();

            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/HomeView.fxml"));
            AnchorPane homeOverview = loader.load();


            this.primaryStage.setMinWidth(1280);
            this.primaryStage.setMinHeight(800);
            this.primaryStage.setWidth(1280);
            this.primaryStage.setHeight(800);

            this.primaryStage.setResizable(false);
            this.primaryStage.setTitle("Sharin - Home");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(homeOverview);

            // Give the controller access to the main app.
            HomeController controller = loader.getController();
            controller.setMainApp(this);
            try {
                controller.updateWeek();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            menuHome.setVisible(true);
            //for the button Priority
            menuHome.toFront();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Project
    public void showProject() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ProjectView.fxml"));
            AnchorPane projectOverview = loader.load();


            menuHome.setVisible(false);
            menuProject.setVisible(true);


            //full screen
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setMinWidth(bounds.getWidth());
            primaryStage.setMinHeight(bounds.getHeight());
            this.primaryStage.setResizable(true);

            this.primaryStage.setTitle("Sharin - Project");



            // Set person overview into the center of root layout.
            rootLayout.setCenter(projectOverview);



            // Give the controller access to the main app.
            ProjectController controller = loader.getController();
            try {
                controller.setMainApp(this);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            menuProject.toFront();


        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    // Calendar
    public void showCalendar() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load home overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/CalendarView.fxml"));
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
    public void showTchat() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load the shared files overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ChatView.fxml"));
            HBox chat = loader.load();

            this.primaryStage.setTitle("Sharin - Chat");
            this.primaryStage.setResizable(false);
            rootLayout.setCenter(chat);

            // Give the controller access to the main app.
            controllerChat = loader.getController();
            controllerChat.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Subscribe
    public void showSubscribe() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

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

    // Task
    public void showCreateProject() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/CreateProjectView.fxml"));
            AnchorPane projectOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Create Project");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(projectOverview);

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
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

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
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

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

    // My Account
    public void showMyAccount() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

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
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

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
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load the shared files overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/SharedFiles.fxml"));
            AnchorPane sharedFiles = loader.load();

            this.primaryStage.setTitle("Sharin - Fichiers partagés");

            rootLayout.setCenter(sharedFiles);

            // Give the controller access to the main app.
            FileController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Update Task
    public void showManageTask() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ManageTaskView.fxml"));
            AnchorPane manageTaskOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Mettez à jour une tâche");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(manageTaskOverview);

            //full screen
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setMinWidth(bounds.getWidth());
            primaryStage.setMinHeight(bounds.getHeight());

            this.primaryStage.setResizable(true);
            // Give the controller access to the main app.
            ManageTaskController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Show all Project Tasks
    public void showTask() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/TaskView.fxml"));
            AnchorPane taskOverview = loader.load();

            this.primaryStage.setTitle("Sharin - Liste des tâches");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(taskOverview);

            //full screen
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();

            primaryStage.setX(bounds.getMinX());
            primaryStage.setY(bounds.getMinY());
            primaryStage.setMinWidth(bounds.getWidth());
            primaryStage.setMinHeight(bounds.getHeight());
            this.primaryStage.setResizable(true);


            // Give the controller access to the main app.
            TaskController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showManagAccount() throws IOException {
        try {
            if(controllerChat != null){
                controllerChat.closeChatClient();
                controllerChat = null;
            }

            // Load connexion overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("view/ManageAccountView.fxml"));
            AnchorPane pluginOverview = loader.load();


            this.primaryStage.setTitle("Sharin - Gestion des comptes utilisateurs");

            // Set person overview into the center of root layout.
            rootLayout.setCenter(pluginOverview);

            // Give the controller access to the main app.
            ManageAccountController controller = loader.getController();
            controller.setMainApp(this);
            controller.loadAllUser();
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



    //Part for plugin

    @FXML
    public void addButton(Button myButton, int nb) {


        controllerMenuProject.addButtonPlugin(myButton, nb);


    }

    public void showPlug(String title, AnchorPane myAnchorePane)throws IOException {

        this.primaryStage.setTitle("Sharin - "+title);
        // Set person overview into the center of root layout.
        rootLayout.setCenter(myAnchorePane);


    }

}
