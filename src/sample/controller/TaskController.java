package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;
import sample.MySQLConnexion;
import sample.Task;
import sample.TaskDAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/*************************************************************
 *************** Dialog to create a new task *****************
 *************************************************************
 *********** Created by Dorine on 24/04/2016.*****************
 ************************************************************/

public class TaskController {

    //Attributes
    private Main mainApp;

    @FXML
    private TextField nameTask;
    @FXML
    private TextField descriptionTask;
    @FXML
    private ComboBox choiceBoxListPriority;
    @FXML
    private DatePicker estimateStartDateTask;
    @FXML
    private DatePicker estimateEndDateTask;

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
    public void handleOk() {
        //if (isInputValid()) {
        String varName = nameTask.getText();
        String varDesc = descriptionTask.getText();
        String varPrior = choiceBoxListPriority.getValue().toString();
        LocalDate date1 = estimateStartDateTask.getValue();
        LocalDate date2 = estimateEndDateTask.getValue();
        int priority = 0;

        //We translate the id of the priority name
        switch(varPrior) {
            case "Urgent":
                priority = 1;
                break;
            case "Haute":
                priority = 2;
                break;
            case "Normale":
                priority = 3;
                break;
            case "Basse":
                priority = 4;
                break;
            default:
                System.out.println("Erreur, la priorité ne ressemble à aucun nom de la bdd !!! ");
        }

        System.out.print("La priorité est : " + varPrior);
        System.out.print("L'id de la priorité est : " + priority);

        /**Transform date to a specific format**/
        Instant instant = Instant.from(date1.atStartOfDay(ZoneId.systemDefault()));
        Date varStart1 = Date.from(instant);
        Instant instant2 = Instant.from(date2.atStartOfDay(ZoneId.systemDefault()));
        Date varEnd1 = Date.from(instant2);

        /**Specific transformed date to string to retrieve it in sql format **/
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varStart = sdf.format(varStart1);

        java.text.SimpleDateFormat sdf2 = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String varDeadline = sdf2.format(varEnd1);

        Connection connection = null;
        Statement myStmt;
        ResultSet myRs;

        try {
            TaskDAO taskDAO = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            Task task = taskDAO.insert(varName, varDesc, priority, varStart, varDeadline);

            if (!"".equals(task.getIdTask())) {
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
        alert.setTitle("La tâche" + varName + " vient d'être créée !");
        alert.showAndWait();
        //}
    }

    @FXML
    public void listingPriority()  {
        try {
            TaskDAO task = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            String[] list = task.findPriority();
            choiceBoxListPriority.getItems().clear();
            for(int i = 0; i < list.length; i++){
                String result = list[i];
                choiceBoxListPriority.getItems().add(
                        result
                );
            }

        } catch (Exception e) {
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

       if (nameTask.getText() == null || nameTask.getText().length() == 0) {
            errorMessage += "No valid task name !\n";
        }
        if (descriptionTask.getText() == null || descriptionTask.getText().length() == 0) {
            errorMessage += "You must describe your task !\n";
        }
        if (estimateStartDateTask.getTime() == 0) {
            errorMessage += "No valid start date !\n";
        }

        if (estimateEndDateTask.getTime() == 0) {
            errorMessage += "No valid start date !\n";
        }

        if (estimateEndDateTask.getTime() == 0 || estimateStartDateTask.getTime() < estimateEndDateTask.getTime()) {
            errorMessage += "No valid end date !\n";
        }


        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }*/

    @FXML
    public void backProject(){
        try{
            mainApp.showProject();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        this.listingPriority();
    }
}