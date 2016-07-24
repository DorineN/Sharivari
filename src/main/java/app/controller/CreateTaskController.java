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
import app.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private TextArea description;
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


    private boolean okClicked = false;

    UserDAO userDao = null;

    @FXML
    private void initialize() {
        // Empty
    }


    /** Returns true if the user clicked OK, false otherwise. **/
    public boolean isOkClicked() {
        return okClicked;
    }

    /** Called when the user clicks on the button New User*/
    @FXML
    public void handleOk() throws ParseException, SQLException {

        String errorMessage = "";

        /**Retrieve values of datepickers **/
        LocalDate date1 = start.getValue();
        LocalDate date2 = estimateEnd.getValue();

        /**Transform date to a specific format**/
        Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
        Date varStart1 = Date.from(instant);
        Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
        Date varEnd1 = Date.from(instant2);

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "Veuillez saisir le nom de la tache !\n";
        }else{


            //check if project exist
            List<Task> testTask = mainApp.taskDAO.findTaskProject(mainApp.getMyProject().getProjectId());

            for (int z = 0; z < testTask.size(); z++) {
                if (testTask.get(z).getNameTask().equals(name.getText())) {
                    errorMessage += "Nom de tache non disponible!\n";
                    break;
                }
            }

        }
        if (description.getText() == null || description.getText().length() == 0) {
            errorMessage += "Veuillez saisir une description de la tache !\n";
        }
        if (comboBoxPriorityName.getValue() == null ) {
            errorMessage += "Veuillez saisir la priorité de la tache !\n";
        }

        if (start.getValue() == null) {
            errorMessage += "Veuillez saisir une date de début !\n";
        }
        if (estimateEnd.getValue() ==null) {
            errorMessage += "Veuillez saisir la date de la deadline !\n";
        }
        if (estimateEnd.getValue() !=null && estimateEnd.getValue()==start.getValue()) {
            errorMessage += "Veuillez saisir une date de début et de deadline différente!\n";
        }if (varStart1.after(varEnd1)){
            errorMessage += "La date de départ est apres la date de la dead line !\n";
        }

        if (errorMessage.equals("")) {
            int varPriority = 0;
            //if (isInputValid()) {
            String varName = name.getText();
            String varDesc = description.getText();


            String namePriority = String.valueOf(comboBoxPriorityName.getValue());


            varPriority = mainApp.taskDAO.findIdPriority(namePriority);


            long temps;
            temps=varEnd1.getTime()-varStart1.getTime();
            temps=temps/1000/60/60/24;
            int varDuration = (int) temps;


            /**Specific transformed date to string to retrieve it in sql format **/
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String varStart = sdf.format(varStart1);

            java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String varEnd = sdf2.format(varEnd1);
            int taskId = 0;
            //check if date end estimate task > end project
            if (mainApp.getMyProject().getProjectDeadline().before(varEnd1)) {
                Alert alert = new Alert(AlertType.CONFIRMATION, "La date de fin de la tâche est supérieur à celle de la fin du projet. Continuer repoussera la deadline du projet. Continuer?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {

                    try {
                        taskId = mainApp.taskDAO.insert(varName, varDesc, varPriority, varDuration, varStart, varEnd);
                        mainApp.getMyProject().setProjectDeadline(varEnd1);
                        mainApp.projectDAO.updateDeadLine(mainApp.getMyProject().getProjectId(),varEnd);

                    }catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            else {
                taskId = mainApp.taskDAO.insert(varName, varDesc, varPriority, varDuration, varStart, varEnd);
            }


            //recup all chief and adjudent
            int[] idUser = mainApp.projectDAO.findMasterUserProject(mainApp.getMyProject().getProjectId());
            ArrayList<Integer> idUserNew = new ArrayList<>();
            if (taskId > 0) {

                mainApp.getMyProject().setProjectEnd(null);
                mainApp.projectDAO.updateEtatProject(mainApp.getMyProject().getProjectId());
                //Retrieve Project ID
                int project = Main.getMyProject().getProjectId();

                /** ADD USERS TO DO THE TASK */

                for (int i = 0; i < tableView.getItems().size(); i++) {
                    String name = tableView.getItems().get(i).getFirstName();

                    //Retrieve User ID

                    //Retrieve Id user
                    int userId = mainApp.userDao.find(name);
                    idUserNew.add(userId);
                    //Affect new user to the task

                    mainApp.taskDAO.affectUserToTask(userId, taskId);


                }
                //add chef project and assiatant
                for (int j = 0; j < idUser.length; j++) {

                    boolean test = true;
                    if (idUserNew.size() > 0) {
                        for (int x = 0; x < idUserNew.size(); x++) {

                            if (idUser[j] == idUserNew.get(x)) {
                                test = false;
                                break;
                            }

                        }
                    }
                    if (test == true) {

                        mainApp.taskDAO.affectUserToTask(idUser[j], taskId);

                    }

                }
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("La tâche " + varName + " vient d'être créée !");
                alert.showAndWait();

                //GO HOME
                try {
                    mainApp.showProject();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }




        }else{
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Création de la tâche non valide");
            alert.setHeaderText("Veuillez corriger le(s) champ(s) suivant :");
            alert.setContentText(errorMessage);

            alert.showAndWait();
        }
    }

    /**Display users in the combobox **/
    @FXML
    public void searchUser() {

        ArrayList<User> users = mainApp.projectDAO.findUsersProject(Main.getMyProject().getProjectId());
        //Inject users in combobox
        for(int i = 0; i< users.size(); i++){
            String result = "";
            result = users.get(i).getUserLogin();

            comboBoxSearchUserTask.getItems().add(
                    result
            );
        }
    }

    /** Display priority available for the task */
    @FXML
    public void listingPriority()  {
        try {

            String tab[] = mainApp.taskDAO.findPriority();

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



    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        searchUser();
        listingPriority();
        //addListUser();

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