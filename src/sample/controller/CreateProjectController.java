package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

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
    private TableView tableSelectUser;
    @FXML
    private TableColumn column1;
    @FXML
    private TableColumn column2;

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
            ProjectDAO projectDAO = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "mdp").getConnexion());
            Project project = projectDAO.insert(varName, varDesc, varStart, varEnd);

            if (!"".equals(project.getProjectId())) {
                Main.setMyProject(project);
                //GO HOME
                try {
                    mainApp.showProject();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Le projet" + varName + " vient d'être créé !");
        alert.showAndWait();

        //GO HOME
        try {
            mainApp.showHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**COMBOBOX SEARCHABLE and autocomplete with users of Sharin*/
    public class ComboBoxAutoComplete<T> {

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

    }

    /**SEARCH USERS IN COMBOBOX**/
    @FXML
    public void searchUser() {
        //Retrieve users from database
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String tab[] = userDAO.findUsersName();

            //Inject users in combobox
            List<String> items = new ArrayList<String>();
            for(int i = 0; i< tab.length; i++){
                items.add(
                        tab[i]
                );
            }
            comboBoxSearchUser.setTooltip(new Tooltip());
            comboBoxSearchUser.getItems().addAll(items);
            new ComboBoxAutoComplete<String>(comboBoxSearchUser);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddUser() {

        String userName = String.valueOf(comboBoxSearchUser.getValue());
        String roleName = String.valueOf(comboBoxRoleName.getValue());

       /* ObservableList<String> list = FXCollections.observableArrayList("Do", "Test", "X");
        tableSelectUser.setItems(FXCollections.observableArrayList(list));

        tableSelectUser.getItems().clear();
        tableSelectUser.getItems().addAll(list);*/

        /*final Button addButton = new Button("Add");
        addButton.setOnAction((ActionEvent e) -> {
            list.add("Lo", "Test");
        });*/
        //tableSelectUser.getItems().clear();
        //tableSelectUser.getItems().addAll(list);
        //tableSelectUser.getItems().addAll(list);

           /* ADD USERS TO DATABASE
           //Retrieve Project ID
            ProjectDAO projectDAO = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());
            int projectId =  mainApp.getMyProject().getProjectId();

            //Retrieve User ID
            UserDAO userDAO = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());
            int userId = userDAO.find(userName);

            //Retrieve Role ID
            int roleId = userDAO.findRoleId(roleName);

            //Insert new users to project
            projectDAO.addUserToProject(userId, roleId, projectId);*/

    }

    /** Display role available for news users */
    @FXML
    public void listingRole()  {
        try {
            UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "mdp").getConnexion());
            String tab[] = userDao.findRole();

            //comboBoxRoleName.getItems().clear();
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

    /** Return button */
    @FXML
    private void handleBtnBack() {
        try {
            mainApp.showHome();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        listingRole();
        searchUser();
        this.mainApp = mainApp;
    }
}