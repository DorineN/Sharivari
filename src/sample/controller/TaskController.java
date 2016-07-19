package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.Main;
import sample.model.MySQLConnexion;
import sample.model.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.model.TaskDAO;

import java.io.IOException;
import java.sql.SQLException;
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



    private ObservableList<Task> data =
            FXCollections.observableArrayList(

            );

    /** Button to add new user to the project in the tableview */
    @FXML
    public void addTaskTable() {
        int idProject = mainApp.getMyProject().getProjectId();

        try {
            TaskDAO taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            List<Task> taskList = taskDao.findTaskProject(idProject);
            for(int i = 0; i < taskList.size(); i++) {
                data.add(new Task(taskList.get(i).getIdTask(), taskList.get(i).getNameTask(), taskList.get(i).getDescriptionTask(), taskList.get(i).getDurationTask(), taskList.get(i).getIdPriority(), taskList.get(i).getEstimateStartDateTask(),
                       taskList.get(i).getEstimateEndDateTask(), taskList.get(i).getRealStartDateTask(), taskList.get(i).getRealEndDateTask(), taskList.get(i).getIdStatus(), taskList.get(i).getIdPriority(), taskList.get(i).getNameStatus(), taskList.get(i).getNamePriority() )
                );
            }
            tableTask.setItems(data);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    private void backProject() {
        try {
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        addTaskTable();
        this.mainApp = mainApp;
    }

}