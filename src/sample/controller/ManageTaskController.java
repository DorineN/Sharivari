package sample.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sample.*;
import sample.model.Task;
import sample.model.MySQLConnexion;
import sample.model.TaskDAO;
import sample.model.UserDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

/*************************************************************
 ********************** Update a task ************************
 *************************************************************
 *********** Created by Dorine on 01/07/2016.*****************
 ************************************************************/
public class ManageTaskController {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField nameTask;
    @FXML
    private TextField descriptionTask;
    @FXML
    private TextField durationTask;
    @FXML
    private DatePicker startTask;
    @FXML
    private DatePicker estimateEndTask;
    @FXML
    private ComboBox comboBoxSearchUserTask;
    @FXML
    private ComboBox comboBoxPriorityNameTask;
    @FXML
    private ComboBox comboBoxStatusNameTask;
    @FXML
    private Button buttonAddUser;
    @FXML
    private TableView<Executant> tableViewUpdateTask;
    @FXML
    public TableColumn usersColumn;
    @FXML
    public Label labelError;

    private Stage dialogStage;
    private boolean okClicked = false;

    UserDAO userDao = null;

    @FXML
    private void initialize() {
        // Empty
    }

    /** Sets the stage of this dialog.*/
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /** Returns true if the user clicked OK, false otherwise. **/
    public boolean isOkClicked() {
        return okClicked;
    }

    /** Called when the user clicks on the button New User*/
    @FXML
    public void handleOk() throws ParseException {
        int varPriority = 0;
        //if (isInputValid()) {
        String varName = nameTask.getText();
        String varDesc = descriptionTask.getText();
        int varDuration = Integer.parseInt(durationTask.getText());
        String namePriority = String.valueOf(comboBoxPriorityNameTask.getValue());

        TaskDAO taskDao = null;
        try {
            taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            varPriority = taskDao.findIdPriority(namePriority);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        /**Retrieve values of datepickers **/
        LocalDate date1 = startTask.getValue();
        LocalDate date2 = estimateEndTask.getValue();

        /**Transform date to a specific format**/
        Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
        Date varStart1 = Date.from(instant);
        Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
        Date varEnd1 = Date.from(instant2);

        /**Specific transformed date to string to retrieve it in sql format **/
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varStart = sdf.format(varStart1);

        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varEnd = sdf2.format(varEnd1);

        try {

            Task task = Main.getMyTask();
            taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            Main.getMyTask().setNameTask(nameTask.getText());
            Main.getMyTask().setDescriptionTask(descriptionTask.getText());
            Main.getMyTask().setDurationTask(Integer.parseInt(durationTask.getText()));
            Main.getMyTask().setIdPriority(taskDao.findIdPriority(String.valueOf(comboBoxPriorityNameTask.getValue())));
            Main.getMyTask().setIdStatus(taskDao.findIdStatus(String.valueOf(comboBoxStatusNameTask.getValue())));
            Main.getMyTask().setEstimateEndDateTask(varStart1);
            Main.getMyTask().setEstimateStartDateTask(varEnd1);
            taskDao.update(task);

            if (!"".equals(task.getIdTask())) {
                //Retrieve Project ID
                int project = Main.getMyProject().getProjectId();
                int taskId = task.getIdTask();
                /** ADD USERS TO DO THE TASK */
                for(int i = 0; i < tableViewUpdateTask.getItems().size(); i++) {
                    String name = tableViewUpdateTask.getItems().get(i).getFirstName();

                    //Retrieve User ID
                    try {
                        userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                        //Retrieve Id user
                        int userId = userDao.find(name);

                        //Insert new users to project
                        taskDao.affectUserToTask(userId, taskId);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("La tâche" + varName + " vient d'être créée !");
        alert.showAndWait();

        //GO HOME
        try {
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**Button to delete the task */
    @FXML
    public void suppressTask() throws ParseException{
        Task task = Main.getMyTask();

        Alert alert = new Alert(AlertType.CONFIRMATION, "Etes-vous sûr de vouloir supprimer la tâche :  " + Main.getMyTask().getNameTask() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                TaskDAO taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                taskDao.delete(task);

                try {
                    mainApp.showProject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**Display users in the combobox **/
    @FXML
    public void searchUser() {
        try {
            UserDAO userDAO = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String tab[] = userDAO.findUsersName();
            String currentUser = Main.getMyUser().getUserLogin();
            //Inject users in combobox
            for(int i = 0; i< tab.length; i++){
                String result = "";
                if(!Objects.equals(currentUser, tab[i])){
                    result = tab[i];
                }
                comboBoxSearchUserTask.getItems().add(
                        result
                );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Display priority available for the task */
    @FXML
    public void listingPriority()  {
        try {
            TaskDAO taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String tab[] = taskDao.findPriority();

            //Inject roles in combobox
            for(int i = 0; i< tab.length; i++){
                String result = tab[i];
                comboBoxPriorityNameTask.getItems().add(
                        result
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Display status for the task */
    @FXML
    public void listingStatus()  {
        try {
            TaskDAO taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String tab[] = taskDao.findStatus();

            //Inject roles in combobox
            for(int i = 0; i< tab.length; i++){
                String result = tab[i];
                comboBoxStatusNameTask.getItems().add(
                        result
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final ObservableList<Executant> data =
            FXCollections.observableArrayList(

            );

    /** Button to add new user to the project in the tableview */
    @FXML
    public void buttonAddUserToTask() {
        //Retrieve combobox values
        String userName = String.valueOf(comboBoxSearchUserTask.getValue());

        int userDouble = 0;


        for(int i = 0; i < tableViewUpdateTask.getItems().size(); i++) {
            String name = tableViewUpdateTask.getItems().get(i).getFirstName();
            if(userName == name){
                this.labelError.setText("Vous avez déjà attribué cet utilisateur");
                userDouble = 1;
                comboBoxSearchUserTask.setValue("");
            }
        }

        if((userName != "null") && (userDouble != 1)) {

            //Button to delete a user
            TableColumn<Executant, Executant> deleteColumn = new TableColumn<>("");
            deleteColumn.setMinWidth(70);
            deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            deleteColumn.setCellFactory(param -> new TableCell<Executant, Executant>() {
                private final Button deleteButton = new Button("Supprimer");

                @Override
                protected void updateItem(Executant person, boolean empty) {
                    super.updateItem(person, empty);

                    if (person == null) {
                        setGraphic(null);
                        return;
                    }

                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> data.remove(person));
                }
            });
            tableViewUpdateTask.setItems(data);
            if ((tableViewUpdateTask.getColumns().size()) == 1) {
                tableViewUpdateTask.getColumns().add(deleteColumn);
            }

            data.add(new Executant(userName)
            );

            comboBoxSearchUserTask.setValue("");
        }
    }

    /** Retrieve users who are affected to this project**/
    @FXML
    public void retrieveUsers(){
        //Retrieve all users from this project
        try {
            TaskDAO taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            int userId = taskDao.findUserTask(Main.getMyTask().getIdTask());
            String userName = userDao.findLoginUser(userId);

            TableColumn<Executant, Executant> deleteColumn = new TableColumn<>("");
            deleteColumn.setMinWidth(40);
            deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            deleteColumn.setCellFactory(param -> new TableCell<Executant, Executant>() {
                private final Button deleteButton = new Button("Supprimer");
                @Override
                protected void updateItem(Executant person, boolean empty) {
                    super.updateItem(person, empty);
                    if (person == null) {
                        setGraphic(null);
                        return;
                    }
                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> data.remove(person));
                }
            });

            tableViewUpdateTask.setItems(data);
            if ((tableViewUpdateTask.getColumns().size()) == 1) {
                tableViewUpdateTask.getColumns().add(deleteColumn);
            }
            data.add(new Executant(userName));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Populate the tableview with these users

    }

    @FXML
    public void getTaskInfos(){
        try {
            TaskDAO task = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String name = mainApp.getMyTask().getNameTask();
            mainApp.setMyTask(task.find(name));

            nameTask.setText(Main.getMyTask().getNameTask());
            descriptionTask.setText(Main.getMyTask().getDescriptionTask());
            durationTask.setText(String.valueOf(Main.getMyTask().getDurationTask()));
            comboBoxStatusNameTask.setValue(task.findStatusName(Main.getMyTask().getIdStatus()));
            comboBoxPriorityNameTask.setValue(task.findNamePriority(Main.getMyTask().getIdPriority()));
            startTask.setValue(LocalDate.parse(String.valueOf(Main.getMyTask().getEstimateStartDateTask())));
            estimateEndTask.setValue(LocalDate.parse(String.valueOf(Main.getMyTask().getEstimateEndDateTask())));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /** Return button */
    @FXML
    private void backHome() {
        try {
            mainApp.showTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        retrieveUsers();
        getTaskInfos();
        searchUser();
        listingPriority();
        listingStatus();
        this.mainApp = mainApp;
    }


    /** Specific class for users who participate to the project */
    public class Executant {
        private final SimpleStringProperty firstName = new SimpleStringProperty("");

        public Executant() {
            this("");
        }

        public Executant(String firstName) {
            setFirstName(firstName);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

    }
}