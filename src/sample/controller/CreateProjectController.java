package sample.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import sample.*;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;

/*************************************************************
 *************** Dialog to create a new project *****************
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
    private ComboBox comboBoxPriorityName;
    @FXML
    private Button buttonAddUser;
    @FXML
    private TreeTableView tableSelectUser;
    @FXML
    private TreeTableColumn columnUserName;
    @FXML
    private TreeTableColumn columnPriorityName;
    @FXML
    private TreeTableColumn columnSuppressUser;

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
            ProjectDAO projectDAO = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());
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

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    /*private boolean isInputValid() {
        String errorMessage = "";

        if (name.getText() == null || name.getText().length() == 0) {
            errorMessage += "No valid project name !\n";
        }
        if (description.getText() == null || description.getText().length() == 0) {
            errorMessage += "You must describe your project !\n";
        }
        if (start.getValue() == 0) {
            errorMessage += "No valid start date !\n";
        }

        if (estimateEnd.getValue() == 0 || estimateEnd.getValue() < start.getValue() ) {
            errorMessage += "No valid end date !\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Champs invalides");
            alert.setHeaderText("Veuillez corriger les champs invalides");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }*/

    @FXML
    public void searchUser() {

        final List<String> items = Arrays.asList(new String[] {
                "aardvark", "apple", "application", "banana", "orangutang", "orange"
        });

        comboBoxSearchUser.setEditable(true);
        comboBoxSearchUser = new ComboBox<>(
                FXCollections.observableArrayList(items));
        comboBoxSearchUser.setEditable(true);
        comboBoxSearchUser.getEditor().textProperty()
                .addListener((observable, oldValue, newValue) -> {
                    filterItems(newValue, comboBoxSearchUser, items);
                });
        comboBoxSearchUser.setOnAction(event -> {
            // Reset so all options are available:
            comboBoxSearchUser.setItems(FXCollections.observableArrayList(items));
        });

    }

    private <T> void filterItems(String filter, ComboBox<T> comboBox,
                                 List<T> items) {
        List<T> filteredItems = new ArrayList<>();
        for (T item : items) {
            if (item.toString().toLowerCase().startsWith(filter.toLowerCase())) {
                filteredItems.add(item);
            }
        }
        comboBox.setItems(FXCollections.observableArrayList(filteredItems));
    }

    //Display role available for news users
    @FXML
    public void listingRole()  {
        try {
            UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());
            String tab[] = userDao.findPriority();

            comboBoxPriorityName.getItems().clear();
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