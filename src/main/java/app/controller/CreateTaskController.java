package app.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import app.*;
import app.model.MySQLConnexion;
import app.model.Task;
import app.model.TaskDAO;
import app.model.UserDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
/*************************************************************
 *************** Dialog to create a new task **************
 *************************************************************
 *********** Created by Dorine on 25/05/2016.*****************
 ************************************************************/
public class CreateTaskController {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField name;
    @FXML
    private TextField description;
    @FXML
    private TextField duree;
    @FXML
    private DatePicker start;
    @FXML
    private DatePicker estimateEnd;
    @FXML
    private ComboBox comboBoxSearchUserTask;
    @FXML
    private ComboBox comboBoxPriorityName;
    @FXML
    private Button buttonAddUser;
    @FXML
    private TableView<Executant> tableView;
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
        String varName = name.getText();
        String varDesc = description.getText();
        int varDuration = Integer.parseInt(duree.getText());
        String namePriority = String.valueOf(comboBoxPriorityName.getValue());

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
        LocalDate date1 = start.getValue();
        LocalDate date2 = estimateEnd.getValue();

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
            taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            Task task = taskDao.insert(varName, varDesc, varPriority, varDuration, varStart, varEnd);

            if (!"".equals(task.getIdTask())) {
                //Retrieve Project ID
                int project = Main.getMyProject().getProjectId();
                int taskId = task.getIdTask();
                /** ADD USERS TO DO THE TASK */
                for(int i = 0; i < tableView.getItems().size(); i++) {
                    String name = tableView.getItems().get(i).getFirstName();

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
                comboBoxPriorityName.getItems().add(
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


        for(int i = 0; i < tableView.getItems().size(); i++) {
            String name = tableView.getItems().get(i).getFirstName();
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
            tableView.setItems(data);
            if ((tableView.getColumns().size()) == 1) {
                tableView.getColumns().add(deleteColumn);
            }

            data.add(new Executant(userName)
            );

            comboBoxSearchUserTask.setValue("");
        }
    }

    /** Return button */
    @FXML
    private void backHome() {
        try {
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        searchUser();
        listingPriority();
        //addListUser();
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