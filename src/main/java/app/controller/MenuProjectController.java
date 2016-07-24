package app.controller;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import app.Main;

import java.io.IOException;


/**
 * Created by Loïc on 18/07/2016.
 **/

public class MenuProjectController {

    private Main mainApp;


    @FXML
    public GridPane gridPaneMenu;


    @FXML
    private ComboBox listProject;

    @FXML
    public void initialize() {

    }

    @FXML
    public void displayAdmin() {

    }

    @FXML
    public void addButtonPlugin(Button myButton, int index){
        myButton.setStyle("-fx-background-color: #f3872c; ");
        myButton.setPrefHeight(30.0);
        myButton.setPrefWidth(150.0);
        gridPaneMenu.setHgap(15);
        gridPaneMenu.setVgap(15);
        myButton.setAlignment(Pos.CENTER);
        gridPaneMenu.add(myButton, 0, (7+index));
        gridPaneMenu.setHalignment(myButton, HPos.CENTER);

    }

    public void updateList(){
        try {

            listProject.setPromptText(mainApp.getMyProject().getProjectName());
            int tab[] = mainApp.userDao.findUserProject(mainApp.getMyUser().getUserId());

            for(int i = 0; i< tab.length; i++){
                String result = mainApp.projectDAO.find(tab[i]).getProjectName();
                boolean newItem = true;
                for (int x = 0; x<listProject.getItems().size(); x++) {
                    if(result.equals(listProject.getItems().get(x))){
                        newItem= false;
                        break;
                    }

                }

                if (newItem == true){
                    listProject.getItems().add(
                            result
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleChangeList(){
        try {

            if (listProject.getValue() != null && !listProject.getValue().toString().equals(mainApp.getMyProject().getProjectName())) {

                String name = listProject.getValue().toString();


                mainApp.setMyProject(mainApp.projectDAO.find(name));

                listProject.setValue(null);


                //GO PROJECT DASHBOARD
                mainApp.menuHome.setVisible(false);
                mainApp.menuProject.setVisible(true);
                mainApp.showProject();

            }
        } catch (IOException e) {

        }
    }

    @FXML
    public void handleMenuDashboard() {
        //GO HOME
        try {
            mainApp.showProject();
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
    public void handleMenuHome(){
        try{
            mainApp.menuProject.setVisible(false);
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
        try{
            mainApp.showTchat();
        }catch(IOException e){
            e.printStackTrace();
        }
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
    public void handleManageProject(){
        try{
            mainApp.showManageProject();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

}