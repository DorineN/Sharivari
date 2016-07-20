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
import app.model.ProjectDAO;
import app.model.UserDAO;

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
    private TextField description;
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

    UserDAO userDAO = null;

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

        //if (isInputValid()) {
        String varName = name.getText();
        String varDesc = description.getText();

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
            ProjectDAO projectDAO = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            Project project = projectDAO.insert(varName, varDesc, varStart, varEnd);

            if (!"".equals(project.getProjectId())) {
                Main.setMyProject(project);
                /** ADD USERS  */
                for(int i = 0; i < tableView.getItems().size(); i++) {
                    String name = tableView.getItems().get(i).getFirstName();
                    String role = tableView.getItems().get(i).getRoleName();
                    //Retrieve Project ID
                    int projectId = project.getProjectId();

                    //Retrieve User ID
                    try {
                        UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                        //Retrieve Id user
                        int userId = userDao.find(name);
                        //Retrieve Role ID
                        int roleId = userDao.findRoleId(role);

                        //Insert new users to project
                        projectDAO.addUserToProject(userId, roleId, projectId);
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


        //GO HOME
        try {
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**COMBOBOX SEARCHABLE and autocomplete with users of Sharin*/
   /* public class ComboBoxAutoComplete<T> {

        private ComboBox<T> cmb;
        String filter = "";
        private ObservableList<T> originalItems;

        public ComboBoxAutoComplete(ComboBox<T> cmb) {
            this.cmb = cmb;
            originalItems = FXCollections.observableArrayList(cmb.getItems());
            cmb.setTooltip(new Tooltip());
            cmb.setOnKeyPressed(this::handleOnKeyPressed);
            cmb.setOnHidden(this::handleOnHiding);
        }

        private void handleOnHiding(Event event) {
            filter = "";
            cmb.getTooltip().hide();
            T s = cmb.getSelectionModel().getSelectedItem();
            cmb.getItems().setAll(originalItems);
            cmb.getSelectionModel().select(s);
        }

        public void handleOnKeyPressed(KeyEvent e) {
            ObservableList<T> filteredList = FXCollections.observableArrayList();
            KeyCode code = e.getCode();

            if (code.isLetterKey()) {
                filter += e.getText();
            }
            if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
                filter = filter.substring(0, filter.length() - 1);
                cmb.getItems().setAll(originalItems);
            }
            if (code == KeyCode.ESCAPE) {
                filter = "";
            }
            if (filter.length() == 0) {
                filteredList = originalItems;
                cmb.getTooltip().hide();
            } else {
                Stream<T> itens = cmb.getItems().stream();
                String txtUsr = filter.toString().toLowerCase();
                itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
                cmb.getTooltip().setText(txtUsr);
                Window stage = cmb.getScene().getWindow();
                double posX = stage.getX() + cmb.localToScene(cmb.getBoundsInLocal()).getMinX();
                double posY = stage.getY() + cmb.localToScene(cmb.getBoundsInLocal()).getMinY();
                cmb.getTooltip().show(stage, posX, posY);
                cmb.show();
            }
            cmb.getItems().setAll(filteredList);
        }

    }*/

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
        searchUser();
        listingRole();

        //addListUser();
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