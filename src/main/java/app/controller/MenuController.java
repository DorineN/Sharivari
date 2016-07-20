package app.controller;

import javafx.fxml.FXML;
import app.Main;


import java.io.IOException;
import java.text.ParseException;


/**
 * Created by Loïc on 18/07/2016.
 **/

public class MenuController {

    private Main mainApp;


    @FXML
    public void handleMenuAccount(){
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
    public void handleManageProject() throws ParseException {
        //GO HOME
        try {
            mainApp.showManageProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuCalendar(){
        //GO HOME
        try {
            mainApp.showCalendar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuPlugin(){
        //GO HOME
        try {
            mainApp.showPlugin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuHome(){
        try{
            mainApp.showHome();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuTask(){
        try{
            mainApp.showTask();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuTchat(){
       /*try{
            mainApp.showTchat();
        }catch(IOException e){
            e.printStackTrace();
        }*/
    }

    @FXML
    public void handleMenuDocument(){
        try{
            mainApp.showSharedFiles();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuGantt(){
        try{
            mainApp.showGantt();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}
