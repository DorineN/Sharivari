package app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import app.Main;
import app.model.Task;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/*************************************************************
 *************** Dialog to create a new project *****************
 *************************************************************
 *********** Created by Dorine on 23/04/2016.*****************
 ************************************************************/
public class ProjectController {

    //Attributes
    private Main mainApp;

    private Calendar date = Calendar.getInstance();
    private Calendar dateMonthBefore = Calendar.getInstance();

    private int month = date.get(date.MONTH);
    private int year = date.get(date.YEAR);
    private int today = date.get(date.DAY_OF_MONTH);
    private int dayInTheWeek = date.get(date.DAY_OF_WEEK);
    private int maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);

    private Label[] labelHooverAllTask = new Label[7];

    //green orange red
    public String[] tColorCalendar = new String[] {"#77DD77", "#FFB347", "#F08080" };

    ColumnConstraints colum = new ColumnConstraints();
    RowConstraints row = new RowConstraints();

    Button[] bTask = new Button[7];


    GridPane[] pDays = new GridPane[7];
    ArrayList<Task> myTask;

    @FXML
    private Label nameProjectLabel;
    @FXML
    private TextField name;
    @FXML
    private TextArea descriptionProjectLabel;
    @FXML
    private Label deadlineProjectLabel;
    @FXML
    private TextArea postitTextArea;
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
    private Label labelPriority;
    @FXML
    private Label labelNameTask;

    @FXML
    private Label labelEstimateStartDate;

    @FXML
    private Label labelEstimateEndDate;

    @FXML
    private Label labelStatus;

    @FXML
    private Text descriptionHoover;



    @FXML
    public void initialize(){
        //  bTask.setOnMouseClicked(event-> goTask());
        hooverAllTaskDay.toFront();
        hooverTask.toFront();
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

        hooverTask.setVisible(true);




    }


    public void bOk (){
        hooverAllTaskDay.setVisible(false);
    }


    public void hooverAllTaskDay(ArrayList<Task> myTask){

        hooverAllTaskDay.setVisible(true);
        Button buttonCheat = new Button("TEST");
        buttonCheat.setOpacity(0);

        gridPaneAllTaskDay.getChildren().clear();

        gridPaneAllTaskDay.setGridLinesVisible(true);
        SimpleDateFormat formater = null;
        formater = new SimpleDateFormat("dd-MM-yyyy");

        gridPaneAllTaskDay.add( labelHooverAllTask[1], 0, 0);
        gridPaneAllTaskDay.add( labelHooverAllTask[2], 1, 0);
        gridPaneAllTaskDay.add( labelHooverAllTask[3], 2, 0);
        gridPaneAllTaskDay.add( labelHooverAllTask[4], 3, 0);
        gridPaneAllTaskDay.add( labelHooverAllTask[5], 4, 0);
        gridPaneAllTaskDay.add( labelHooverAllTask[6], 5, 0);
        gridPaneAllTaskDay.add(buttonCheat, 6, 0);

        Button[] tButton = new Button[myTask.size()];

        for(int c = 0; c<myTask.size(); c++){


            gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameTask()),  0,c+1);
            gridPaneAllTaskDay.add(new Text(myTask.get(c).getDescriptionTask()), 1, c+1);

            gridPaneAllTaskDay.add(new Label(formater.format(myTask.get(c).getEstimateStartDateTask())), 2, c+1);



            formater = new SimpleDateFormat("dd-MM-yyyy");
            gridPaneAllTaskDay.add(new Label(formater.format(myTask.get(c).getEstimateEndDateTask())), 3, c+1);
            gridPaneAllTaskDay.add(new Label(myTask.get(c).getNamePriority()), 4, c+1);
            if (myTask.get(c).getNameStatus().equals("Terminée")) {
                String dateEnd = formater.format(myTask.get(c).getRealEndDateTask());
                gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameStatus() + " (Le "+dateEnd+")"),5, c+1);
            }else{
                gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameStatus()), 5, c+1);
            }

            tButton[c] = new Button("Voir plus");

            //Obliger de le remettre dans un objet task sinon crash
            Task taskSend = myTask.get(c);

            tButton[c].setOnMouseClicked(event-> clicAndGoManageTask(taskSend));

            gridPaneAllTaskDay.add(tButton[c], 6, c+1);
            hooverAllTaskDay.toFront();


        }



    }

    public void hooverMyPaneExited(){
        hooverTask.setVisible(false);

    }

    public void clicAndGoManageTask(Task myTask) {


        mainApp.setMyTask(myTask);

        try {
            mainApp.showManageTask();
        } catch (IOException e) {
            e.printStackTrace();
        }


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

            myTask = mainApp.taskDAO.findTask(dateCase, mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId());
            Pane[] tPaneCalendar = new Pane[myTask.size()];


            for(int c = 0; c<myTask.size(); c++){
                int indexColor = 2;

                if (myTask.get(c).getNameStatus().equals("En cours")){

                    indexColor = 1;
                }else if(myTask.get(c).getNameStatus().equals("Terminée")){
                    indexColor = 0;
                }

                tPaneCalendar[c] = new Pane();
                if (c<12){
                    if (myTask.get(c).getNameTask().length()<20 ) {
                        tPaneCalendar[c].getChildren().add(new Text("\n " + myTask.get(c).getNameTask()));
                    }else{
                        tPaneCalendar[c].getChildren().add(new Text("\n " + myTask.get(c).getNameTask().substring(0,20) + "..."));

                    }
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
    public void editPostit() {
        mainApp.userDao.updatePostitUser(postitTextArea.getText(), mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId());
    }



    public void setMainApp(Main mainApp) throws ParseException, SQLException, ClassNotFoundException {
        this.mainApp = mainApp;
        this.mainApp.controllerMenuProject.updateList();
        SimpleDateFormat formater = null;
        formater = new SimpleDateFormat("dd-MM-yyyy");
        postitTextArea.setText(mainApp.userDao.findPostItUser(mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId()));
        descriptionProjectLabel.setText(mainApp.getMyProject().getProjectDesc());

        deadlineProjectLabel.setText(formater.format(mainApp.getMyProject().getProjectDeadline()));
        nameProjectLabel.setText(mainApp.getMyProject().getProjectName());
        updateWeek();

    }


}