package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import app.Main;

import java.io.IOException;


/**
 * Created by Loïc on 18/07/2016.
 **/

public class MenuHomeController {

    private Main mainApp;

    @FXML
    Button managAllCompte;

    @FXML
    public void initialize() {

        if (mainApp.getMyUser().getUserType().equals("adm") && managAllCompte!=null){
            managAllCompte.setVisible(true);
        }

    }

    @FXML
    public void displayAdmin() {

        if (mainApp.getMyUser().getUserType().equals("adm") && managAllCompte!=null){
            managAllCompte.setVisible(true);
        }

    }

    @FXML
    public void handleMenuAccount(){
        try{
            mainApp.showMyAccount();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void handleMenuManagAccount(){
        try{
            mainApp.showManagAccount();
        }catch(IOException e){
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


    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}