package app.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import app.*;
import app.model.MySQLConnexion;
import app.model.ProjectDAO;
import app.model.UserDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    private TextField descriptionProject;
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
    public TableColumn usersColumn;
    @FXML
    public Label labelError;

    Date startDate;
    Date endDate;

    @FXML
    public void handleUpdateProject() {

        try {

            /**Retrieve values of datepickers **/
            LocalDate date1 = startProject.getValue();
            LocalDate date2 = estimateEndProject.getValue();

            /**Transform date to a specific format**/
            Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
            Date varStart1 = Date.from(instant);
            Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
            Date varEnd1 = Date.from(instant2);

            ProjectDAO project = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            try{

                //Update current Project
                Main.getMyProject().setProjectName(nameProject.getText());
                Main.getMyProject().setProjectDesc(descriptionProject.getText());
                Main.getMyProject().setProjectStart(varStart1);
                Main.getMyProject().setProjectEnd(varEnd1);

                project.update(Main.getMyProject());

                //Add users or update them
                for (int i = 0; i < tableViewUpdateProject.getItems().size(); i++) {
                    String name = tableViewUpdateProject.getItems().get(i).getFirstName();
                    String role = tableViewUpdateProject.getItems().get(i).getRoleName();
                    //Retrieve Project ID
                    int projectId = Main.getMyProject().getProjectId();
                    //Retrieve User ID
                    try {
                        UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                        ProjectDAO projectDao = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                        //Retrieve Id user
                        int userId = userDao.find(name);
                        //Retrieve Role ID
                        int roleId = userDao.findRoleId(role);
                        //Insert new users to project
                        projectDao.addUserToProject(userId, roleId, projectId);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        //GO HOME PROJECT
        try {

            ProjectDAO projectDao = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            Main.setMyProject(projectDao.find(String.valueOf(nameProject)));
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
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
        String roleName = String.valueOf(comboBoxRoleName.getValue());
        int userDouble = 0;
        for(int i = 0; i < tableViewUpdateProject.getItems().size(); i++) {
            String name = tableViewUpdateProject.getItems().get(i).getFirstName();
            String role = tableViewUpdateProject.getItems().get(i).getRoleName();
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
            tableViewUpdateProject.setItems(data);
            if ((tableViewUpdateProject.getColumns().size()) == 2) {
                tableViewUpdateProject.getColumns().add(deleteColumn);
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

    /** Retrieve users who are affected to this project**/
    @FXML
    public void retrieveUsers(){
        //Retrieve all users from this project
        try {
            ProjectDAO projectDao = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String[][] users = projectDao.findUsersProject(Main.getMyProject().getProjectId());

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

            tableViewUpdateProject.setItems(data);
            if ((tableViewUpdateProject.getColumns().size()) == 2) {
                tableViewUpdateProject.getColumns().add(deleteColumn);
            }

            for(int i = 0; i < users.length; i++){
                if (!Objects.equals(users[i][0], Main.getMyUser().getUserLogin())) {
                    data.add(new Participant(users[i][0], users[i][1]));
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Populate the tableview with these users

    }

    @FXML
    public void handleBtnBack(){
        try{
            mainApp.showProject();
        }catch(IOException e){
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
            System.out.println("currentuser " + currentUser);
            //Inject users in combobox
            for(int i = 0; i< tab.length; i++){
                String result = "";
                if(!Objects.equals(currentUser, tab[i])){
                    result = tab[i];
                }
                comboBoxSearchUser.getItems().add(
                        result
                );
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Display role available for news users */
    @FXML
    public void listingRole()  {
        try {
            UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String tab[] = userDao.findRole();

            //Inject roles in combobox
            for(int i = 0; i< tab.length; i++){
                String result = tab[i];
                comboBoxRoleName.getItems().add(
                        result
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void getProjectInfos(){
        try {
            ProjectDAO project = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String name = mainApp.getMyProject().getProjectName();
            mainApp.setMyProject(project.find(name));

            nameProject.setText(Main.getMyProject().getProjectName());
            descriptionProject.setText(Main.getMyProject().getProjectDesc());
            startProject.setValue(LocalDate.parse(String.valueOf(Main.getMyProject().getProjectStart())));
            estimateEndProject.setValue(LocalDate.parse(String.valueOf(Main.getMyProject().getProjectEnd())));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Main getMainApp() {
        return mainApp;
    }

    public void setMainApp(Main mainApp) {
        getProjectInfos();
        searchUser();
        listingRole();
        retrieveUsers();
        this.mainApp = mainApp;
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