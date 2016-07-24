package app.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import app.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import app.model.MySQLConnexion;
import app.model.Project;

import static java.lang.Integer.parseInt;


/*************************************************************
 *************** Dialog to create a new project **************
 *************************************************************
 *********** Created by Dorine on 23/04/2016.*****************
 ************************************************************/
public class CreateProjectController {

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
    private ComboBox comboBoxSearchUser;
    @FXML
    private ComboBox comboBoxRoleName;
    @FXML
    private Button buttonAddUser;
    @FXML
    private TableView<Participant> tableView;
    @FXML
    public TableColumn usersColumn;
    @FXML
    public Label labelError;

    private Stage dialogStage;
    private boolean okClicked = false;



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
        String errorMessage = "";

        /**Retrieve values of datepickers **/
        LocalDate date1 = start.getValue();
        LocalDate date2 = estimateEnd.getValue();


        Date varStart1 = null;
        Date varEnd1 = null;
        if(date1!=null && date2!=null) {
            /**Transform date to a specific format**/
            Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
            varStart1 = Date.from(instant);
            Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
            varEnd1 = Date.from(instant2);
        }

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "Veuillez saisir le nom du projet !\n";
        }else{
            //check if project exist
            Project testProject = mainApp.projectDAO.find(name.getText());
            if (testProject!=null){
                errorMessage += "Nom de projet non disponible!\n";
            }
        }
        if (description.getText() == null || description.getText().length() == 0) {
            errorMessage += "Veuillez saisir une description du projet !\n";
        }

        if (start.getValue() == null) {
            errorMessage += "Veuillez saisir une date de début !\n";
        }
        if (estimateEnd.getValue() ==null) {
            errorMessage += "Veuillez saisir la date de la deadline !\n";
        }
        if (estimateEnd.getValue() !=null && estimateEnd.getValue()==start.getValue()) {
            errorMessage += "Veuillez saisir une date de début et de deadline différente!\n";
        }
        if (estimateEnd.getValue() !=null && estimateEnd.getValue()==start.getValue() && varStart1.after(varEnd1)){
            errorMessage += "La date de départ est apres la date de la dead line !\n";
        }


        if (errorMessage.equals("")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Création d'un projet");
            alert.setHeaderText("Valider la création du projet ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                insertMyProject(varStart1, varEnd1);
            }
        }else{
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Création du projet non valide");
            alert.setHeaderText("Veuillez corriger le(s) champ(s) suivant :");
            alert.setContentText(errorMessage);

            alert.showAndWait();
        }
    }


    public void insertMyProject (Date varStart1,Date varEnd1){

        String varName = name.getText();
        String varDesc = description.getText();





        /**Specific transformed date to string to retrieve it in sql format **/
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varStart = sdf.format(varStart1);

        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varEnd = sdf2.format(varEnd1);


        try {

            int idProject = mainApp.projectDAO.insert(varName, varDesc, varStart, varEnd);

            if (idProject != 0) {

                /** ADD USERS  */
                for (int i = 0; i < tableView.getItems().size(); i++) {
                    String name = tableView.getItems().get(i).getFirstName();
                    String role = tableView.getItems().get(i).getRoleName();
                    //Retrieve Project ID


                    //Retrieve User ID

                    //Retrieve Id user
                    int userId = mainApp.userDao.find(name);
                    //Retrieve Role ID
                    int roleId = mainApp.userDao.findRoleId(role);

                    //Insert new users to project
                    mainApp.projectDAO.addUserToProject(userId, roleId, idProject);

                }

                //add creator
                mainApp.projectDAO.addUserToProject(mainApp.getMyUser().getUserId(), 1, idProject);
                mainApp.setMyProject(mainApp.projectDAO.find(idProject));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


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

        String tab[] = mainApp.userDao.findUsersName();
        String currentUser = Main.getMyUser().getUserLogin();

        //Inject users in combobox
        for(int i = 0; i< tab.length; i++){
            String result = "";
            if(!Objects.equals(currentUser, tab[i])){
                result = tab[i];
                comboBoxSearchUser.getItems().add(
                        result
                );
            }

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

    private final ObservableList<Participant> data =
            FXCollections.observableArrayList(

            );

    /** Button to add new user to the project in the tableview */
    @FXML
    public void handleAddUser() {
        //Retrieve combobox values
        String userName = String.valueOf(comboBoxSearchUser.getValue());
        String roleName = String.valueOf(comboBoxRoleName.getValue());

        int userDouble = 0;


        for(int i = 0; i < tableView.getItems().size(); i++) {
            String name = tableView.getItems().get(i).getFirstName();
            String role = tableView.getItems().get(i).getRoleName();
            if(userName == name){
                this.labelError.setText("Vous avez déjà attribué cet utilisateur");
                userDouble = 1;
                comboBoxSearchUser.setValue("");
                comboBoxRoleName.setValue("");
            }
        }

        if((userName != "null") && (roleName != "null") && (userName != "") && (roleName != "") && (userDouble != 1)) {

            //Button to delete a user
            TableColumn<Participant, Participant> deleteColumn = new TableColumn<>("");
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

                    setGraphic(deleteButton);
                    deleteButton.setOnAction(event -> data.remove(person));
                }
            });
            tableView.setItems(data);
            if ((tableView.getColumns().size()) == 2) {
                tableView.getColumns().add(deleteColumn);
            }

            data.add(new Participant(userName, roleName)
            );

            comboBoxSearchUser.setValue("");
            comboBoxRoleName.setValue("");
        }else if(userName == "null" || userName == ""){
            this.labelError.setText("Vous n'avez pas sélectionné d'utilisateur");
        }else if(roleName == "null" || roleName == ""){
            this.labelError.setText("Vous devez attribuer un rôle à cet utilisateur");
        }
    }

    /** Return button */
    @FXML
    private void backHome() {
        try {
            mainApp.showHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {


        //addListUser();
        this.mainApp = mainApp;
        searchUser();
        listingRole();
    }

    /** Specific class for users who participate to the project */
    public class Participant {
        private final SimpleStringProperty firstName = new SimpleStringProperty("");
        private final SimpleStringProperty roleName = new SimpleStringProperty("");

        public Participant() {
            this("", "");
        }

        public Participant(String firstName, String roleName) {
            setFirstName(firstName);
            setRoleName(roleName);
        }

        public String getFirstName() {
            return firstName.get();
        }

        public void setFirstName(String fName) {
            firstName.set(fName);
        }

        public String getRoleName() {
            return roleName.get();
        }

        public void setRoleName(String rName) {
            roleName.set(rName);
        }

    }
}