package sample.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;
import sample.model.MySQLConnexion;
import sample.model.Task;
import sample.model.TaskDAO;

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

    TaskDAO taskDAO = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin?autoReconnect=true&useSSL=false", "root", "root").getConnexion());
    private Main mainApp;
    Calendar date = Calendar.getInstance();;
    int month = date.get(date.MONTH);
    int year = date.get(date.YEAR);
    int maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
    String tMonth[] = {"Janvier","Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"};
    String[] tDays = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    public String[] tColorCalendar = new String[] {"#77DD77", "#B39EB5", "#FFB347", "#779ECB"};



    @FXML
    private AnchorPane anchorePane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label title;



    GridPane[] pDays = new GridPane[31];
    ArrayList<Task> myTask;

    private Stage dialogStage;
    private boolean okClicked = false;

    public CalendarController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    public void initialize() throws ParseException {


        //System.out.println(maxDay);
        //System.out.println(year);
        //System.out.println(month);
       // System.out.println(mainApp.myProject.getProjectId());

        //System.out.print(date);
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
        //System.out.println(maxDay);
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
        //System.out.println(maxDay);

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
        //compare the day with my table tDays  to find the first day of the month
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

            pDays[i-1] = new GridPane();
            pDays[i-1].setStyle("-fx-border-color: #e1e5cd; -fx-background-color : white; -fx-padding : 10px; -fx-width:100%;");

            if (j==7){
                j=0;
                numSemaine++;
            }

            String dateCase = year +"-" +(month+1)+"-"+i;

            pDays[i-1].add(new Label( i + "\n"),0,0 );


            myTask = taskDAO.findTask(dateCase, mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId());
            Pane[] tPaneCalendar = new Pane[myTask.size()];
            for(int c = 0; c<myTask.size(); c++){
                tPaneCalendar[c] = new Pane();
                tPaneCalendar[c].getChildren().add(new Label(myTask.get(c).getNameTask() + "\n" ));
                tPaneCalendar[c].setStyle("-fx-background-color:"+tColorCalendar[c]+";");
                pDays[i-1].add(tPaneCalendar[c], 0,c+1);
            }


            //recup task


            gridPane.add(pDays[i-1], j, numSemaine);



            j++;

        }

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}