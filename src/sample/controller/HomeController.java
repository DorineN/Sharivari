package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import sample.Main;

import javax.naming.NamingException;
import java.io.IOException;

public class HomeController {
    //Attributes
    private Main mainApp;

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
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}
