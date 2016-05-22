package sample.controller;

import sample.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;
import sample.MySQLConnexion;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/*************************************************************
 *************** Dialog to create a new project *****************
 *************************************************************
 *********** Created by iPlowPlow on 04/05/2016.*****************
 ************************************************************/
public class CalendarController {

    //Attributes
    private Main mainApp;
    Calendar date = Calendar.getInstance();;
    int month = date.get(date.MONTH);
    int year = date.get(date.YEAR);
    int maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
    String tMonth[] = {"Janvier","Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"};
    String[] tDays = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    @FXML
    private AnchorPane anchorePane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label title;

    //pane for calendar :
    Pane pane1 = new Pane();
    Pane pane2 = new Pane();
    Pane pane3 = new Pane();
    Pane pane4 = new Pane();
    Pane pane5 = new Pane();
    Pane pane6 = new Pane();
    Pane pane7 = new Pane();
    Pane pane8 = new Pane();
    Pane pane9 = new Pane();
    Pane pane10 = new Pane();
    Pane pane11 = new Pane();
    Pane pane12 = new Pane();
    Pane pane13 = new Pane();
    Pane pane14 = new Pane();
    Pane pane15 = new Pane();
    Pane pane16 = new Pane();
    Pane pane17 = new Pane();
    Pane pane18 = new Pane();
    Pane pane19 = new Pane();
    Pane pane20 = new Pane();
    Pane pane21 = new Pane();
    Pane pane22 = new Pane();
    Pane pane23 = new Pane();
    Pane pane24 = new Pane();
    Pane pane25 = new Pane();
    Pane pane26 = new Pane();
    Pane pane27 = new Pane();
    Pane pane28 = new Pane();
    Pane pane29 = new Pane();
    Pane pane30 = new Pane();
    Pane pane31 = new Pane();

    Pane[] pDays = new Pane[] { pane1,pane2,pane3,pane4,pane5,pane6,pane7,pane8,pane9,pane10,pane11,pane12,pane13,pane14,pane15,pane16,pane17,pane18,pane19,pane20,pane21,pane22,pane23,pane24,pane25,pane26,pane27,pane28,pane29,pane30,pane31};

    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    public void initialize() throws ParseException {


        //System.out.println(maxDay);
        //System.out.println(year);
        //System.out.println(month);
        System.out.println(mainApp.myProject.getProjectId());


        title.setText(tMonth[month] + " " + year);


        updateCalendar();

    }

    @FXML
    public void next() throws ParseException {
        month++;
        if(month==12){
            month = 0;
            year++;
        }

        date.set(year, month,1);
        maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(maxDay);

        title.setText(tMonth[month] + " " + year);

        gridPane.getChildren().clear();

        updateCalendar();

    }

    @FXML
    public void previous() throws ParseException {
        month--;
        if(month==-1){
            month = 11;
            year--;
        }

        date.set(year, month,1);
        maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println(maxDay);

        title.setText(tMonth[month] + " " + year);

        gridPane.getChildren().clear();

        updateCalendar();
    }

    public void updateCalendar() throws ParseException {

        //To start the calendar at the good day
        String firstDayS = year +"-" +(month+1)+"-1";
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        Date firstDay = sdf.parse(firstDayS);
        String day = String.valueOf(firstDay).substring(0,3);

        int j = 0;

        for (int i = 0; i<tDays.length; i++){
            if (day.equals(tDays[i])){
                j = i;
                break;
            }
        }

        //initialise gridpane
        int numSemaine = 0;
        gridPane.setAlignment(Pos.CENTER);
        for (int i = 1; i<=maxDay; i++){

            if (j==7){
                j=0;
                numSemaine++;
            }

            pDays[i-1].getChildren().add(new Label("" + i));

            //recup event

            pDays[i-1].setStyle("fx-background-color : white");
            pDays[i-1].setStyle("fx-border-color : black");
            gridPane.add(pDays[i-1], j, numSemaine);



            j++;

        }

    }




    @FXML
    public void backHome(){
        try{
            mainApp.showHome();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


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
    @FXML
    private void handleMenuCalendar() throws NamingException {
        //GO HOME
        try {
            mainApp.showCalendar();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}