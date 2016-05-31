package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import sample.Main;

import java.io.IOException;

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
    public void handleUpdateProject(){

       /* Main.getMyProject().setProjectName(nameProject);
        Main.getMyUser().setUserFirstName(descriptionProject);
        Main.getMyUser().setUserName(startProject);
        Main.getMyUser().setUserMail(estimateEndProject);
        try {
            ProjectDAO project = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "mdp").getConnexion());
            project.update(Main.getMyProject());
        }catch(ClassNotFoundException | SQLException e) {
            System.out.println("Error controller !");
        }*/
    }


    @FXML
    public void handleBtnBack(){
        try{
            mainApp.showHome();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void setMainApp(Main mainApp) {

        nameProject.setText(mainApp.getMyProject().getProjectName());
        descriptionProject.setText(mainApp.getMyProject().getProjectDesc());
        //startProject.setValue(mainApp.getMyProject().getProjectStart());
        //estimateEndProject.setChronology(mainApp.getMyProject().getProjectDeadline());

        this.mainApp = mainApp;
    }
}