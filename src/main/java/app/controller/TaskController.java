package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import app.Main;
import app.model.MySQLConnexion;
import app.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import app.model.TaskDAO;
import app.model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TaskController {

    //Attributes
    private Main mainApp;
    @FXML
    private TableView<Task> tableTask;
    @FXML
    private TableColumn nameColumnTask;
    @FXML
    private TableColumn descColumnTask;
    @FXML
    private TableColumn durationColumnTask;
    @FXML
    private TableColumn startColumnTask;
    @FXML
    private TableColumn deadlineColumnTask;
    @FXML
    private TableColumn realStartColumnTask;
    @FXML
    private TableColumn endColumnTask;
    @FXML
    private TableColumn userColumnTask;
    @FXML
    private TableColumn statusColumnTask;
    @FXML
    private TableColumn priorityColumnTask;
    @FXML
    private Button btnCreateTask;


    private ObservableList<Task> data =
            FXCollections.observableArrayList(

            );

    /** Button to add new user to the project in the tableview */
    @FXML
    public void addTaskTable() {
        int idProject = mainApp.getMyProject().getProjectId();
        List<Task> taskList = null;
        String role = mainApp.projectDAO.findRole(mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId());
        if (role.equals("Membre")) {
            taskList = mainApp.taskDAO.findMyTask(idProject, mainApp.getMyUser().getUserId());
        }else{
            taskList = mainApp.taskDAO.findTaskProject(idProject);
        }

        //recup all user



        for(int i = 0; i < taskList.size(); i++) {


            data.add(taskList.get(i));

        }
        //tableTask.setItems(data);
        tableTask.setItems(data);

        //Go to update the task when double clicked on a row
        tableTask.setRowFactory( tv -> {
            TableRow<Task> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Task rowData = row.getItem();

                    try {
                        //Retrieve the Task of the row and set the Task before go to the Manage window
                        Main.setMyTask(rowData);
                        mainApp.showManageTask();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });
    }


    /** Return button */
    @FXML
    private void goCreateTask() {
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

        if (mainApp.projectDAO.findRole(mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId()).equals("Chef de projet") || mainApp.projectDAO.findRole(mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId()).equals("Assistant chef de projet")){
            btnCreateTask.setVisible(true);
        }
        addTaskTable();

    }

}