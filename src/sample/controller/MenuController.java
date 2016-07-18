package sample.controller;

import javafx.fxml.FXML;
import sample.Main;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Loïc on 18/07/2016.
 **/

public class MenuController {

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
    public void handleMenuProject() {
        //GO HOME
        try {
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleMenuCalendar() throws NamingException {
        //GO HOME

        try {
            mainApp.showCalendar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuPlugin() throws NamingException {
        //GO HOME

        try {
            mainApp.showPlugin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}
