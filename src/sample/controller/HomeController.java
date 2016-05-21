package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import sample.Main;

import javax.naming.NamingException;
import java.io.IOException;
import java.text.ParseException;

public class HomeController {
    //Attributes
    private Main mainApp;

    @FXML
    private Label projectName;

    @FXML
    public void showAccount(){
        try{
            mainApp.showMyAccount();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuProject() throws NamingException {
        //GO HOME
        try {
            mainApp.showCreateProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTask() throws NamingException {
        //GO TO CREATE NEW TASK
        try {
            mainApp.showCreateTask();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        projectName.setText(mainApp.getMyProject().getProjectName());
        this.mainApp = mainApp;
    }

    @FXML
    public void handleBtnCreate() throws ParseException {
        try {
            mainApp.showCreateProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
