package app.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import app.*;
import app.model.Task;
import app.model.User;


import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private TextArea descriptionTask;
    @FXML
    private TextField status;
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
    private TableView<Participant> tableViewUpdateTask;
    @FXML
    public TableColumn usersColumn;
    @FXML
    public Label labelError;
    @FXML
    private Button startUpdateTask;
    @FXML
    private Button deleteTask;
    @FXML
    private Button updateTask;
    @FXML
    private TextField priority;
    @FXML
    private Label executant;


    private Stage dialogStage;
    private boolean okClicked = false;

    TableColumn<Participant, Participant> deleteColumn = new TableColumn<>();

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

        String namePriority = String.valueOf(comboBoxPriorityNameTask.getValue());

        ArrayList<User> listAllUser = new ArrayList<>();
        listAllUser = mainApp.taskDAO.findUsersTask(mainApp.getMyTask().getIdTask());
        ArrayList<Integer> listAllNewUser = new ArrayList<>();

        varPriority = mainApp.taskDAO.findIdPriority(namePriority);

        /**Retrieve values of datepickers **/
        LocalDate date1 = startTask.getValue();
        LocalDate date2 = estimateEndTask.getValue();

        /**Transform date to a specific format**/
        Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
        Date varStart1 = Date.from(instant);
        Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
        Date varEnd1 = Date.from(instant2);

        String errorMessage = "";

        if (nameTask.getText() == null || nameTask.getText().length() == 0) {
            errorMessage += "Veuillez saisir le nom de la tache !\n";
        }else{
            //check if project exist
            if(!mainApp.getMyTask().getNameTask().equals(nameTask.getText())) {
                List<Task> testTask = mainApp.taskDAO.findTaskProject(mainApp.getMyProject().getProjectId());
                for (int z = 0; z < testTask.size(); z++) {
                    if (testTask.get(z).getNameTask().equals(nameTask.getText())) {
                        errorMessage += "Nom de tache non disponible!\n";
                        break;
                    }
                }
            }
        }
        if (descriptionTask.getText() == null || descriptionTask.getText().length() == 0) {
            errorMessage += "Veuillez saisir une description de la tache !\n";
        }
        if (comboBoxPriorityNameTask.getValue() == null ) {
            errorMessage += "Veuillez saisir la priorité de la tache !\n";
        }

        if (startTask.getValue() == null) {
            errorMessage += "Veuillez saisir une date de début !\n";
        }
        if (estimateEndTask.getValue() ==null) {
            errorMessage += "Veuillez saisir la date de la deadline !\n";
        }
        if (estimateEndTask.getValue() !=null && estimateEndTask.getValue()==startTask.getValue()) {
            errorMessage += "Veuillez saisir une date de début et de deadline différente!\n";
        }
        if (varStart1.after(varEnd1)){
            errorMessage += "La date de départ est apres la date de la dead line !\n";
        }
        if (errorMessage.equals("")) {




            long temps;
            temps=varEnd1.getTime()-varStart1.getTime();
            temps=temps/1000/60/60/24;
            int varDuration = (int) temps;

            int oldStatus = mainApp.getMyTask().getIdStatus();

            Main.getMyTask().setNameTask(nameTask.getText());
            Main.getMyTask().setDescriptionTask(descriptionTask.getText());

            Main.getMyTask().setDurationTask((varDuration));
            Main.getMyTask().setIdPriority(mainApp.taskDAO.findIdPriority(String.valueOf(comboBoxPriorityNameTask.getValue())));

            //updateStaut
            Main.getMyTask().setIdStatus(mainApp.taskDAO.findIdStatus(String.valueOf(comboBoxStatusNameTask.getValue())));
            if(String.valueOf(comboBoxStatusNameTask.getValue()).equals("En cours") && mainApp.getMyTask().getNameStatus().equals("En attente")){

                //mainApp.taskDAO.updateDateRealStartDate(mainApp.getMyTask().getIdTask());
            }else if(String.valueOf(comboBoxStatusNameTask.getValue()).equals("Terminée")){

                //mainApp.taskDAO.updateDateRealStartDate(mainApp.getMyTask().getIdTask());
            }

            Main.getMyTask().setEstimateEndDateTask(varEnd1);
            Main.getMyTask().setEstimateStartDateTask(varStart1);
            Task task = mainApp.getMyTask();
            mainApp.taskDAO.update(task,oldStatus);

            if (!"".equals(mainApp.getMyTask().getIdTask())) {
                //Retrieve Project ID
                int project = Main.getMyProject().getProjectId();
                int taskId = task.getIdTask();
                /** AFFECT USER TO DO THE TASK */
                for (int i = 0; i < tableViewUpdateTask.getItems().size(); i++) {

                    String name = tableViewUpdateTask.getItems().get(i).getFirstName();
                    int userId = mainApp.userDao.find(name);
                    listAllNewUser.add(userId);
                    //Retrieve Project ID
                    int idTask = Main.getMyTask().getIdTask();


                    //Insert new users to project
                    boolean test = true;
                    for (int j = 0; j < listAllUser.size(); j++) {
                        if (listAllUser.get(j).getUserId() == userId) {

                            test = false;
                            break;
                        }
                    }
                    if (test == true) {
                        mainApp.taskDAO.addUserToTask(userId, idTask);
                    }


                }

                //Delete user
                for (int i = 0; i < listAllUser.size(); i++) {


                    //Insert new users to project
                    boolean test = true;
                    for (int j = 0; j < listAllNewUser.size(); j++) {
                        if (listAllNewUser.get(j) == listAllUser.get(i).getUserId()) {
                            test = false;
                            break;
                        }
                    }
                    if (test == true) {
                        mainApp.taskDAO.deleteExecuteUser(listAllUser.get(i).getUserId(), mainApp.getMyTask().getIdTask());

                    }


                }

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("La tâche " + varName + " vient d'être mise à jour !");
                alert.showAndWait();
                //GO HOME PROJECT
                try {


                    mainApp.showProject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



            //GO HOME
            try {
                mainApp.showProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Modification de la tâche non valide");
            alert.setHeaderText("Veuillez corriger le(s) champ(s) suivant :");
            alert.setContentText(errorMessage);
            alert.showAndWait();
        }
    }

    /**Button to delete the task */
    @FXML
    public void suppressTask() throws ParseException{


        Alert alert = new Alert(AlertType.CONFIRMATION, "Etes-vous sûr de vouloir supprimer la tâche :  " + Main.getMyTask().getNameTask() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {

            mainApp.taskDAO.delete(mainApp.getMyTask().getIdTask());

            List<Task> lTask = new ArrayList<>();
            lTask = mainApp.projectDAO.findTaskProject(mainApp.getMyProject().getProjectId());

            boolean finish = true;
            for (int i =0; i<lTask.size(); i++){
                if (lTask.get(i).getIdStatus() != 3){
                    finish = false;
                    break;
                }
            }

            //if all task finish
            if (finish == true){
                mainApp.projectDAO.updateEtatProjectFinish(mainApp.getMyProject().getProjectId());
            }

            try {
                mainApp.showProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

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
            String tab[] = mainApp.taskDAO.findStatus();

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

    private final ObservableList<Participant> data =
            FXCollections.observableArrayList(

            );

    /** Button to add new user to the project in the tableview */
    @FXML
    public void buttonAddUserToTask() {
        //Retrieve combobox values
        String userName = String.valueOf(comboBoxSearchUserTask.getValue());
        int idNew = mainApp.userDao.find(userName);
        User newUser = mainApp.userDao.find(idNew);

        String userGlobalName = newUser.getUserLogin() ;

        String roleName = String.valueOf(comboBoxSearchUserTask.getValue());
        int userDouble = 0;
        for(int i = 0; i < tableViewUpdateTask.getItems().size(); i++) {
            String name = tableViewUpdateTask.getItems().get(i).getFirstName();
            String role = tableViewUpdateTask.getItems().get(i).getRoleName();

            if(userGlobalName.equals(name)){
                this.labelError.setText("Vous avez déjà attribué cet utilisateur");
                userDouble = 1;
                comboBoxSearchUserTask.setValue("");

            }
        }

        if((userName != "null") && (roleName != "null") && (userName != "") && (roleName != "") && (userDouble != 1)) {
            //Button to delete a user
            TableColumn<Participant, Participant> deleteColumn = new TableColumn<>("");
            deleteColumn.setMinWidth(700000);
            deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            deleteColumn.setCellFactory(param ->
                    new TableCell<Participant, Participant>() {
                        private final Button deleteButton = new Button("Supprimer");

                        @Override
                        protected void updateItem(Participant person, boolean empty) {
                            super.updateItem(person, empty);

                            if (person == null) {

                                setGraphic(null);
                                return;
                            }

                            int idUser = mainApp.userDao.find(person.getFirstName());
                            int idProject = mainApp.getMyProject().getProjectId();
                            setGraphic(deleteButton);
                            deleteButton.setOnAction(event -> data.remove(person));

                        }

                    });

            tableViewUpdateTask.setItems(data);
            if ((tableViewUpdateTask.getColumns().size()) == 1) {

                tableViewUpdateTask.getColumns().add(deleteColumn);
            }

            data.add(new Participant(userGlobalName, roleName, idNew)
            );

            comboBoxSearchUserTask.setValue("");

        }else if(userName == "null" || userName == ""){
            this.labelError.setText("Vous n'avez pas sélectionné d'utilisateur");
        }else if(roleName == "null" || roleName == ""){
            this.labelError.setText("Vous devez attribuer un rôle à cet utilisateur");
        }
    }

    /** Retrieve users who are affected to this project**/
    @FXML
    public void retrieveUsers(){
        //Retrieve all users from this project


        ArrayList<User> users = mainApp.taskDAO.findUsersTask(Main.getMyTask().getIdTask());


        deleteColumn.setMinWidth(40);
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<Participant, Participant>() {
            private final Button deleteButton = new Button("Supprimer");
            @Override
            protected void updateItem(Participant person, boolean empty) {
                super.updateItem(person, empty);
                if (person == null) {
                    setGraphic(null);

                    return;
                }
                if (!person.getRoleName().equals("Chef de projet") && !person.getRoleName().equals("Assistant chef de projet")) {
                    setGraphic(deleteButton);
                }
                deleteButton.setOnAction(event -> data.remove(person));
            }
        });




        tableViewUpdateTask.setItems(data);
        if ((tableViewUpdateTask.getColumns().size()) == 1) {
            tableViewUpdateTask.getColumns().add(deleteColumn);
        }
        String role ="";
        for(int i = 0; i < users.size(); i++){
            role = mainApp.projectDAO.findRole(users.get(i).getUserId(), mainApp.getMyProject().getProjectId());
            data.add(new Participant(users.get(i).getUserLogin(), role, users.get(i).getUserId()));

        }

        //Populate the tableview with these users

    }



    @FXML
    public void getTaskInfos(){

        int idTask = mainApp.getMyTask().getIdTask();
        mainApp.setMyTask(mainApp.taskDAO.find(idTask));

        nameTask.setText(Main.getMyTask().getNameTask());
        descriptionTask.setText(Main.getMyTask().getDescriptionTask());

        comboBoxStatusNameTask.setValue(mainApp.taskDAO.findStatusName(Main.getMyTask().getIdStatus()));
        comboBoxPriorityNameTask.setValue(mainApp.taskDAO.findNamePriority(Main.getMyTask().getIdPriority()));
        startTask.setValue(LocalDate.parse(String.valueOf(Main.getMyTask().getEstimateStartDateTask())));
        estimateEndTask.setValue(LocalDate.parse(String.valueOf(Main.getMyTask().getEstimateEndDateTask())));
        priority.setText(mainApp.taskDAO.findNamePriority(Main.getMyTask().getIdPriority()));
        status.setText(mainApp.taskDAO.findStatusName(Main.getMyTask().getIdStatus()));
    }





    @FXML
    public void handleStartUpdateTask() {

        deleteColumn.setVisible(true);

        //show all
        updateTask.setVisible(true);
        startUpdateTask.setVisible(false);

        comboBoxSearchUserTask.setVisible(true);
        executant.setVisible(true);
        buttonAddUser.setVisible(true);

        status.setVisible(false);
        comboBoxStatusNameTask.setVisible(true);
        priority.setVisible(false);
        comboBoxPriorityNameTask.setVisible(true);

        //enable input
        nameTask.setEditable(true);
        descriptionTask.setEditable(true);
        startTask.setDisable(false);
        estimateEndTask.setDisable(false);

    }

    private void displayAdmProject() {
        String role = mainApp.projectDAO.findRole(mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId());
        if (role.equals("Chef de projet") || role.equals("Assistant chef de projet")){

            deleteTask.setVisible(true);
            startUpdateTask.setVisible(true);


        }
    }

    /** Specific class for users who participate to the project */
    public class Participant {
        private final SimpleStringProperty firstName = new SimpleStringProperty("");
        private final SimpleStringProperty roleName = new SimpleStringProperty("");
        private final SimpleIntegerProperty idUser = new SimpleIntegerProperty();

        public Participant() {
            this("", "", 0);
        }

        public Participant(String firstName, String roleName, int id) {
            setFirstName(firstName);
            setRoleName(roleName);
            setIdUser(id);

        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public int getUserId(){
            return idUser.get();
        }

        public String getRoleName() {
            return roleName.get();
        }

        public void setRoleName(String rName) {
            roleName.set(rName);
        }

        public void setIdUser(int id) {
            idUser.set(id);
        }


    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        tableViewUpdateTask.getItems().clear();
        deleteColumn.setVisible(false);
        displayAdmProject();
        retrieveUsers();
        getTaskInfos();
        searchUser();
        listingPriority();
        listingStatus();

    }
}