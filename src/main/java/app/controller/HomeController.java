package app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import app.Main;
import app.model.*;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeController {

    //Attributes


    private Main mainApp;
    Calendar date = Calendar.getInstance();
    Calendar dateMonthBefore = Calendar.getInstance();

    int month = date.get(date.MONTH);
    int year = date.get(date.YEAR);
    int today = date.get(date.DAY_OF_MONTH);
    int dayInTheWeek = date.get(date.DAY_OF_WEEK);
    int maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);

    Label[] labelHooverAllTask = new Label[7];
    //green orange red
    public String[] tColorCalendar = new String[] {"#77DD77", "#FFB347", "#F08080" };

    ColumnConstraints colum = new ColumnConstraints();
    RowConstraints row = new RowConstraints();

    Button[] bTask = new Button[7];


    GridPane[] pDays = new GridPane[7];
    ArrayList<Task> myTask;

    @FXML
    private GridPane gridPaneHomeWeek ;

    @FXML
    private Pane hooverTask ;

    @FXML
    private GridPane gridPaneDay;

    @FXML
    private GridPane gridPaneAllTaskDay;

    @FXML
    private Pane hooverAllTaskDay;

    @FXML
    private ComboBox listProject;

    @FXML
    private Label notification;

    @FXML
    private GridPane gridPaneHoover;

    @FXML
    private Label labelNameProject;

    @FXML
    private Label labelNameTask;

    @FXML
    private Label labelEstimateStartDate;

    @FXML
    private Label labelPriority;

    @FXML
    private Label labelEstimateEndDate;

    @FXML
    private Label labelStatus;

    @FXML
    private Text descriptionHoover;



    @FXML
    public void initialize(){
        //  bTask.setOnMouseClicked(event-> goTask());

        gridPaneAllTaskDay.setGridLinesVisible(true);
        labelHooverAllTask[0] = new Label("Projet");
        labelHooverAllTask[1] = new Label("Tache");
        labelHooverAllTask[2] = new Label("Description");
        labelHooverAllTask[3] = new Label("Date début");
        labelHooverAllTask[4] = new Label("Date de fin estimée");
        labelHooverAllTask[5] = new Label("Priorité");
        labelHooverAllTask[6] = new Label("Status");



        for (int i = 0; i<7; i++){
            labelHooverAllTask[i].setStyle("-fx-font-size : 18px;");
        }

        colum.setPrefWidth(275.0);
        row.setMaxHeight(40);
        row.setPrefHeight(40);

    }

    public void hooverMyPaneEntered(Task myTask){


        SimpleDateFormat formater = null;
        formater = new SimpleDateFormat("dd-MM-yyyy");

        labelNameProject.setText(myTask.getNameProject());
        labelNameTask.setText(myTask.getNameTask());
        labelPriority.setText(myTask.getNamePriority());
        labelEstimateStartDate.setText(formater.format(myTask.getEstimateStartDateTask()));
        SimpleDateFormat formater2 = null;
        formater2 = new SimpleDateFormat("dd-MM-yyyy");
        labelEstimateEndDate.setText(formater2.format(myTask.getEstimateEndDateTask()));
        if (myTask.getNameStatus().equals("Terminée")) {
            labelStatus.setText(myTask.getNameStatus() + "(Le "+myTask.getRealEndDateTask()+")");
        }else{
            labelStatus.setText(myTask.getNameStatus());
        }

        descriptionHoover.setText(myTask.getDescriptionTask());
        hooverTask.toFront();
        hooverTask.setVisible(true);




    }


    public void bOk (){
        hooverAllTaskDay.setVisible(false);
    }



    public void hooverAllTaskDay(ArrayList<Task> myTask){

        gridPaneAllTaskDay.getChildren().clear();

        gridPaneAllTaskDay.setGridLinesVisible(true);

        //header
        gridPaneAllTaskDay.add(labelHooverAllTask[0], 0, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[1], 1, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[2], 2, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[3], 3, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[4], 4, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[5], 5, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[6], 6, 0);
        Button buttonCheat = new Button("TEST");
        buttonCheat.setOpacity(0);
        gridPaneAllTaskDay.add(buttonCheat, 6, 0);

        hooverAllTaskDay.setVisible(true);

        SimpleDateFormat formater = null;
        formater = new SimpleDateFormat("dd-MM-yyyy");

        Button[] tButton = new Button[myTask.size()];
        for(int c = 0; c<myTask.size(); c++){


            gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameProject()), 0, c+1);
            gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameTask()),  1,c+1);
            gridPaneAllTaskDay.add(new Text(myTask.get(c).getDescriptionTask()), 2, c+1);

            gridPaneAllTaskDay.add(new Label(formater.format(myTask.get(c).getEstimateStartDateTask())), 3, c+1);

            formater = new SimpleDateFormat("dd-MM-yyyy");
            gridPaneAllTaskDay.add(new Label(formater.format(myTask.get(c).getEstimateEndDateTask())), 4, c+1);

            gridPaneAllTaskDay.add(new Label(myTask.get(c).getNamePriority()),  5,c+1);

            if (myTask.get(c).getNameStatus().equals("Terminée")) {
                String dateEnd = formater.format(myTask.get(c).getRealEndDateTask());
                gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameStatus() + " (Le "+dateEnd +")"),6, c+1) ;
            }else{
                gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameStatus()), 6, c+1);
            }

            tButton[c] = new Button("Voir plus");

            //Obliger de le remettre dans un objet task sinon crash
            Task taskSend = myTask.get(c);

            tButton[c].setOnMouseClicked(event-> clicAndGoManageTask(taskSend));

            gridPaneAllTaskDay.add(tButton[c], 7, c+1);
            hooverAllTaskDay.toFront();


        }



    }

    public void hooverMyPaneExited(){
        hooverTask.setVisible(false);

    }

    public void clicAndGoManageTask(Task myTask) {

        mainApp.setMyProject(mainApp.projectDAO.find(myTask.getIdProject()));
        mainApp.setMyTask(myTask);

        try {
            mainApp.showManageTask();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public HomeController() throws SQLException, ClassNotFoundException {
    }




    public void updateWeek() throws ParseException, SQLException, ClassNotFoundException {
        //To start the calendar at the good day
        String firstDayS = year +"-" +(month+1)+"-"+today;
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        Date firstDay = sdf.parse(firstDayS);
        String day = String.valueOf(firstDay).substring(0,3);

        int cptButtonTooMany = 0;

        //initialise gridpane
        gridPaneHomeWeek.setAlignment(Pos.CENTER);
        dateMonthBefore.set(year, month-1, 1);

        int dayAfterMonth = 1;

        int dayBeforeMonth = dateMonthBefore.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 1; i<=7; i++) {

            pDays[i - 1] = new GridPane();
            pDays[i - 1].getColumnConstraints().add(colum);
            //  pDays[i - 1].getRowConstraints().add(row);



            pDays[i - 1].setStyle("-fx-border-color: #e1e5cd; -fx-background-color : white; -fx-width:100%;");

           /* if (j==7){
                j=0;
                numSemaine++;
            }*/


            //Pour la requete LIKE
            String dateCase;
            String paramMonth = "" + (month + 1);
            if (month + 1 < 10) {
                paramMonth = "0" + (month + 1);
            }
            String paramDay = "" + i;
            if (i < 10) {
                paramDay = "0" + i;
            }



            if((today + (i - (dayInTheWeek - 1)))>maxDay){
                pDays[i - 1].add(new Label(dayAfterMonth + "\n"), 0, 0);

                paramMonth = ""+ (month+2);
                if (month + 2 < 10) {
                    paramMonth = "0" + (month + 2);
                }
                paramDay = "0"+(dayAfterMonth);

                dayAfterMonth++;

            }else if((today + (i - (dayInTheWeek - 1)))<=0) {
                pDays[i - 1].add(new Label(dayBeforeMonth + today +(i - (dayInTheWeek-1)) + "\n"), 0, 0);

                paramMonth = ""+ month;
                if (month < 10) {
                    paramMonth = "0" + (month);
                }
                paramDay = ""+(dayBeforeMonth + today +(i - (dayInTheWeek-1)));


            }else{
                pDays[i - 1].add(new Label((today + ((i - (dayInTheWeek - 1)))) + "\n"), 0, 0);

                paramMonth = ""+ (month+1);
                if ( (month+1) < 10) {
                    paramMonth = "0" +  (month+1);
                }
                paramDay = ""+((today + ((i - (dayInTheWeek - 1)))));
                if((today + ((i - (dayInTheWeek - 1))))<10){
                    paramDay = "0"+((today + ((i - (dayInTheWeek - 1)))));
                }
            }

            dateCase = year + "-" + paramMonth + "-" + paramDay;
            //bug with import menu, necessit TASKDAO in local

            myTask = mainApp.taskDAO.findAllTask(dateCase, mainApp.getMyUser().getUserId());
            Pane[] tPaneCalendar = new Pane[myTask.size()];


            for(int c = 0; c<myTask.size(); c++){
                int indexColor = 2;

                if (myTask.get(c).getNameStatus().equals("En cours")){
                    indexColor = 1;
                }else if(myTask.get(c).getNameStatus().equals("Terminée")){
                    indexColor = 0;
                }

                tPaneCalendar[c] = new Pane();
                if (c<9){
                    String projectName ="";
                    String taskName ="";

                    if (myTask.get(c).getNameProject().length()<15 ) {
                        projectName = myTask.get(c).getNameProject();
                    }else{
                        projectName = myTask.get(c).getNameProject().substring(0,15) + "...";
                    }

                    if (myTask.get(c).getNameTask().length()<15 ) {
                        taskName = myTask.get(c).getNameTask();
                    }else{
                        taskName = myTask.get(c).getNameTask().substring(0,15) + "...";
                    }

                    tPaneCalendar[c].getChildren().add(new Text("\n Projet : " + projectName  + "\n Tâche : " +taskName + "\n" ));
                    tPaneCalendar[c].setStyle("-fx-background-color:"+tColorCalendar[indexColor]+"; -fx-border-color:white");
                    tPaneCalendar[c].setMaxHeight(40);


                    pDays[i-1].add(tPaneCalendar[c], 0,c+1);


                    //Obliger de le remettre dans un objet task sinon crash
                    Task taskSend = myTask.get(c);

                    tPaneCalendar[c].setOnMouseEntered(event-> hooverMyPaneEntered(taskSend));
                    tPaneCalendar[c].setOnMouseExited(event-> hooverMyPaneExited());
                    tPaneCalendar[c].setOnMouseClicked(event-> clicAndGoManageTask(taskSend));

                }else{
                    bTask[cptButtonTooMany] = new Button("...");
                    bTask[cptButtonTooMany].setPrefWidth(163.0);
                    tPaneCalendar[c].getChildren().add(bTask[cptButtonTooMany]);
                    ArrayList<Task> lTaskSend = new ArrayList<>();
                    lTaskSend = myTask;
                    ArrayList<Task> finalLTaskSend = lTaskSend;
                    bTask[cptButtonTooMany].setOnMouseClicked(event-> hooverAllTaskDay(finalLTaskSend));
                    pDays[i-1].add(tPaneCalendar[c], 0,c+1);
                    cptButtonTooMany ++;
                    break;
                }


            }


            //recup task


            gridPaneHomeWeek.add(pDays[i-1], i-1, 0);




        }

    }

    @FXML
    public void listingProject()  {
        try {

            int tab[] = mainApp.userDao.findUserProject(mainApp.getMyUser().getUserId());

            listProject.getItems().clear();
            for(int i = 0; i< tab.length; i++){
                String result = mainApp.projectDAO.find(tab[i]).getProjectName();
                listProject.getItems().add(
                        result
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goProject() throws ParseException, SQLException {

        try {

            if (listProject.getValue() != null && !listProject.getValue().toString().isEmpty()) {

                String name = listProject.getValue().toString();

                mainApp.setMyProject(mainApp.projectDAO.find(name));

                listProject.setValue(null);

                //GO PROJECT DASHBOARD
                mainApp.menuHome.setVisible(false);
                mainApp.menuProject.setVisible(true);
                mainApp.showProject();

            } else {
                notification.setText("Vous n'avez pas sélectionné de projet!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBtnCreate() throws ParseException {
        //GO PROJECT CREATION
        try {
            mainApp.showCreateProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {

        this.mainApp = mainApp;
        //Display projects of the current user
        this.listingProject();
    }

}