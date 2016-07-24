package app.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
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
import java.util.Objects;

/*************************************************************
 ********************* Update a project **********************
 *************************************************************
 *********** Created by Dorine on 30/04/2016.*****************
 ************************************************************/
public class ManageProjectController {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField nameProject;
    @FXML
    private TextArea descriptionProject;
    @FXML
    private DatePicker startProject;
    @FXML
    private DatePicker estimateEndProject;

    @FXML
    private ComboBox comboBoxSearchUser;
    @FXML
    private ComboBox comboBoxRoleName;
    @FXML
    private Button buttonAddUser;
    @FXML
    private TableView<Participant> tableViewUpdateProject;
    @FXML
    private TableColumn usersColumn;
    @FXML
    private Label labelError;
    @FXML
    private Button deleteProject;
    @FXML
    private Button startUpdateProject;
    @FXML
    private Button updateProject;
    @FXML
    private Label labelChooseRole;
    @FXML
    private Label labelChooseUser;


    TableColumn<Participant, Participant> deleteColumn = new TableColumn<>("");

    Date startDate;
    Date endDate;


    @FXML
    public void handleUpdateProject() {
        String errorMessage ="";
        /**Retrieve values of datepickers **/
        LocalDate date1 = startProject.getValue();
        LocalDate date2 = estimateEndProject.getValue();

        /**Transform date to a specific format**/
        Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
        Date varStart1 = Date.from(instant);
        Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
        Date varEnd1 = Date.from(instant2);

        ArrayList<User> listAllUser = new ArrayList<>();
        listAllUser = mainApp.projectDAO.findUsersProject(mainApp.getMyProject().getProjectId());
        ArrayList<Integer> listAllNewUser = new ArrayList<>();



        if (nameProject.getText() == null || nameProject.getText().length() == 0) {
            errorMessage += "Veuillez saisir le nom du projet !\n";
        }else{
            //check if project exist
            Project testProject = mainApp.projectDAO.find(nameProject.getText());
            if (!nameProject.getText().equals(mainApp.getMyProject().getProjectName()) && testProject!=null ){
                errorMessage += "Nom de projet non disponible!\n";
            }
        }
        if (descriptionProject.getText() == null || descriptionProject.getText().length() == 0) {
            errorMessage += "Veuillez saisir une description du projet !\n";
        }

        if (startProject.getValue() == null) {
            errorMessage += "Veuillez saisir une date de début !\n";
        }
        if (estimateEndProject.getValue() ==null) {
            errorMessage += "Veuillez saisir la date de la deadline !\n";
        }
        if (estimateEndProject.getValue() !=null && estimateEndProject.getValue()==startProject.getValue()) {
            errorMessage += "Veuillez saisir une date de début et de deadline différente!\n";
        } if (varStart1.after(varEnd1)){
            errorMessage += "La date de départ est apres la date de la dead line !\n";
        }


        if (errorMessage.equals("")) {


            try {



                //Update current Project
                Main.getMyProject().setProjectName(nameProject.getText());
                Main.getMyProject().setProjectDesc(descriptionProject.getText());
                Main.getMyProject().setProjectStart(varStart1);
                Main.getMyProject().setProjectDeadline(varEnd1);

                mainApp.projectDAO.update(Main.getMyProject());

                //Add users or update them
                for (int i = 0; i < tableViewUpdateProject.getItems().size(); i++) {

                    String name = tableViewUpdateProject.getItems().get(i).getFirstName();
                    String role = tableViewUpdateProject.getItems().get(i).getRoleName();
                    int userId = tableViewUpdateProject.getItems().get(i).getUserId();
                    listAllNewUser.add(userId);
                    //Retrieve Project ID
                    int projectId = Main.getMyProject().getProjectId();
                    //Retrieve User ID

                    //Retrieve Id user

                    //Retrieve Role ID
                    int roleId = mainApp.userDao.findRoleId(role);
                    //Insert new users to project
                    boolean test = true;
                    for (int j = 0; j < listAllUser.size(); j++) {
                        if (listAllUser.get(j).getUserId() == userId) {
                            mainApp.projectDAO.updateToProject(userId, roleId, projectId);
                            test = false;
                            break;
                        }
                    }
                    if (test == true) {
                        mainApp.projectDAO.addUserToProject(userId, roleId, projectId);
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
                        mainApp.userDao.deleteExecute(listAllUser.get(i).getUserId());
                        mainApp.userDao.deleteParticipate(listAllUser.get(i).getUserId());
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            //GO HOME PROJECT
            try {


                mainApp.showProject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Modification du projet non valide");
            alert.setHeaderText("Veuillez corriger le(s) champ(s) suivant :");
            alert.setContentText(errorMessage);

            alert.showAndWait();
        }
    }

    private final ObservableList<Participant> data =
            FXCollections.observableArrayList(

            );

    /** Button to add new user to the project in the tableview */
    @FXML
    public void updateUser() {
        //Retrieve combobox values
        String userName = String.valueOf(comboBoxSearchUser.getValue());
        int idNew = mainApp.userDao.find(userName);
        User newUser = mainApp.userDao.find(idNew);

        String userGlobalName = newUser.getUserLogin() + " (" +newUser.getUserName()+ " "+newUser.getUserFirstName()+ ")";

        String roleName = String.valueOf(comboBoxRoleName.getValue());
        int userDouble = 0;
        for(int i = 0; i < tableViewUpdateProject.getItems().size(); i++) {
            String name = tableViewUpdateProject.getItems().get(i).getFirstName();
            String role = tableViewUpdateProject.getItems().get(i).getRoleName();

            if(userGlobalName.equals(name)){
                this.labelError.setText("Vous avez déjà attribué cet utilisateur");
                userDouble = 1;
                comboBoxSearchUser.setValue("");
                comboBoxRoleName.setValue("");
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

            tableViewUpdateProject.setItems(data);
            if ((tableViewUpdateProject.getColumns().size()) == 1) {

                tableViewUpdateProject.getColumns().add(deleteColumn);
            }

            data.add(new Participant(userGlobalName, roleName, idNew)
            );

            comboBoxSearchUser.setValue("");
            comboBoxRoleName.setValue("");
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


        ArrayList<User> users = mainApp.projectDAO.findUsersProject(Main.getMyProject().getProjectId());


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
                if (!person.getRoleName().equals("Chef de projet")) {
                    setGraphic(deleteButton);
                }
                deleteButton.setOnAction(event -> data.remove(person));
            }
        });




        tableViewUpdateProject.setItems(data);
        if ((tableViewUpdateProject.getColumns().size()) == 2) {
            tableViewUpdateProject.getColumns().add(deleteColumn);
        }
        String role ="";
        for(int i = 0; i < users.size(); i++){
            role = mainApp.projectDAO.findRole(users.get(i).getUserId(), mainApp.getMyProject().getProjectId());
            data.add(new Participant(users.get(i).getUserLogin() + " (" +users.get(i).getUserName()+ " "+users.get(i).getUserFirstName()+ ")", role, users.get(i).getUserId()));

        }

        //Populate the tableview with these users

    }



    /**Display users in the combobox **/
    @FXML
    public void searchUser() {

        String tab[] = mainApp.userDao.findUsersName();


        //Inject users in combobox
        for(int i = 0; i< tab.length; i++){
            String result = "";

            result = tab[i];

            comboBoxSearchUser.getItems().add(
                    result
            );
        }
    }

    /** Display role available for news users */
    @FXML
    public void listingRole()  {
        try {

            String tab[] = mainApp.userDao.findRole();

            //Inject roles in combobox
            for(int i = 0; i< tab.length; i++){
                String result = tab[i];
                if (!result.equals("Chef de projet")) {
                    comboBoxRoleName.getItems().add(
                            result
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getProjectInfos(){

        String name = mainApp.getMyProject().getProjectName();
        mainApp.setMyProject(mainApp.projectDAO.find(name));

        nameProject.setText(Main.getMyProject().getProjectName());
        descriptionProject.setText(Main.getMyProject().getProjectDesc());
        startProject.setValue(LocalDate.parse(String.valueOf(Main.getMyProject().getProjectStart())));
        estimateEndProject.setValue(LocalDate.parse(String.valueOf(Main.getMyProject().getProjectDeadline())));

    }

    /**Button to delete the project */
    @FXML
    public void deleteProject() throws ParseException {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Etes-vous sûr de vouloir supprimer définitivement le projet :  " + Main.getMyProject().getProjectName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                mainApp.projectDAO.delete(mainApp.getMyProject().getProjectId());
                try {
                    mainApp.menuProject.setVisible(false);
                    mainApp.showHome();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }





    @FXML
    public void handleStartUpdateProject() {
        //show all


        deleteColumn.setVisible(true);

        updateProject.setVisible(true);
        startUpdateProject.setVisible(false);
        labelChooseUser.setVisible(true);
        comboBoxSearchUser.setVisible(true);
        labelChooseRole.setVisible(true);
        buttonAddUser.setVisible(true);
        comboBoxRoleName.setVisible(true);

        //enable input
        nameProject.setEditable(true);
        descriptionProject.setEditable(true);
        startProject.setDisable(false);
        estimateEndProject.setDisable(false);

    }

    private void displayAdmProject() {
        String role = mainApp.projectDAO.findRole(mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId());
        if (role.equals("Chef de projet")){

            deleteProject.setVisible(true);
            startUpdateProject.setVisible(true);


        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        getProjectInfos();
        searchUser();
        listingRole();
        retrieveUsers();
        displayAdmProject();
        deleteColumn.setVisible(false);

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

}