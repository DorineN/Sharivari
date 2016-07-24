package app.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import app.Main;
import app.model.MySQLConnexion;
import app.model.Task;

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
    public Main mainApp;


    Calendar date = Calendar.getInstance();;
    int month = date.get(date.MONTH);
    int year = date.get(date.YEAR);
    int maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
    String tMonth[] = {"Janvier","Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"};
    String[] tDays = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};


    Button[] bTask = new Button[31];
    //green orange red
    public String[] tColorCalendar = new String[] {"#77DD77", "#FFB347", "#F08080" };



    @FXML
    private AnchorPane anchorePane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Label title;

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
    private Label labelPriority;

    @FXML
    private Label labelEstimateStartDate;

    @FXML
    private Label labelEstimateEndDate;

    @FXML
    private Label labelStatus;

    @FXML
    private Text descriptionHoover;


    GridPane[] pDays = new GridPane[31];
    ArrayList<Task> myTask;
    Label[] labelHooverAllTask = new Label[7];

    public CalendarController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    public void initialize() throws ParseException {

        title.setText(tMonth[month] + " " + year);

        gridPaneAllTaskDay.setGridLinesVisible(true);

        labelHooverAllTask[0] = new Label("Tache");
        labelHooverAllTask[1] = new Label("Tache");
        labelHooverAllTask[2] = new Label("Description");
        labelHooverAllTask[3] = new Label("Date début");
        labelHooverAllTask[4] = new Label("Date de fin estimée");
        labelHooverAllTask[5] = new Label("Priorité");
        labelHooverAllTask[6] = new Label("Status");
        Button buttonCheat = new Button("TEST");
        buttonCheat.setOpacity(0);



        for (int i = 0; i<7; i++){
            labelHooverAllTask[i].setStyle("-fx-font-size : 18px;");
        }

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

        hooverTask.toFront();

        hooverTask.setVisible(true);




    }


    public void bOk (){
        hooverAllTaskDay.setVisible(false);
    }

    public void hooverAllTaskDay(ArrayList<Task> myTask){

        hooverAllTaskDay.setVisible(true);
        Button buttonCheat = new Button("TEST");
        buttonCheat.setOpacity(0);


        SimpleDateFormat formater = null;
        formater = new SimpleDateFormat("dd-MM-yyyy");

        gridPaneAllTaskDay.getChildren().clear();
        gridPaneAllTaskDay.add(labelHooverAllTask[1], 0, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[2], 1, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[3], 2, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[4], 3, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[5],4, 0);
        gridPaneAllTaskDay.add(labelHooverAllTask[6],5, 0);
        gridPaneAllTaskDay.add(buttonCheat, 6, 0);
        gridPaneAllTaskDay.setGridLinesVisible(true);

        Button[] tButton = new Button[myTask.size()];

        for(int c = 0; c<myTask.size(); c++){


            gridPaneAllTaskDay.add(new Label(myTask.get(c).getNameTask()),  0,c+1);
            gridPaneAllTaskDay.add(new Text(myTask.get(c).getDescriptionTask()), 1, c+1);

            gridPaneAllTaskDay.add(new Label(formater.format(myTask.get(c).getEstimateStartDateTask())), 2, c+1);

            formater = new SimpleDateFormat("dd-MM-yyyy");
            gridPaneAllTaskDay.add(new Label(formater.format(myTask.get(c).getEstimateEndDateTask())), 3, c+1);


            gridPaneAllTaskDay.add(new Text(myTask.get(c).getDescriptionTask()), 4, c+1);
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

        mainApp.setMyProject(mainApp.projectDAO.find(myTask.getIdProject()));
        mainApp.setMyTask(myTask);

        try {
            mainApp.showManageTask();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
        int cptButtonTooMany = 0;
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
            pDays[i-1].setStyle("-fx-border-color: #e1e5cd; -fx-background-color : white; -fx-width:100%;");

            if (j==7){
                j=0;
                numSemaine++;
            }


            //Pour la requete LIKE
            String dateCase;
            String paramMonth = ""+(month+1);
            if (month+1<10){
                paramMonth = "0"+ (month+1);
            }
            String paramDay = ""+i;
            if (i<10){
                paramDay = "0"+ i;
            }

            dateCase = year +"-" +paramMonth+"-"+paramDay;
            pDays[i-1].add(new Label( i + "\n"),0,0 );


            myTask = mainApp.taskDAO.findTask(dateCase, mainApp.getMyUser().getUserId(), mainApp.getMyProject().getProjectId());
            Pane[] tPaneCalendar = new Pane[myTask.size()];
            Pane[] tPaneHover = new Pane[myTask.size()];

            for(int c = 0; c<myTask.size(); c++){
                int indexColor = 2;

                if (myTask.get(c).getNameStatus().equals("En cours")){
                    indexColor = 1;
                }else if(myTask.get(c).getNameStatus().equals("Terminée")){
                    indexColor = 0;
                }
                tPaneCalendar[c] = new Pane();
                tPaneHover[c] = new Pane();
                if (c<3){
                    String projectName ="";
                    String taskName ="";


                    if (myTask.get(c).getNameTask().length()<15 ) {
                        taskName = " " +myTask.get(c).getNameTask();
                    }else{
                        taskName =" " + myTask.get(c).getNameTask().substring(0,15) + "...";
                    }

                    tPaneCalendar[c].getChildren().add(new Label(taskName));
                    tPaneCalendar[c].setStyle("-fx-background-color:"+tColorCalendar[indexColor]+"; -fx-border-color:white");
                    tPaneCalendar[c].setPrefWidth(150.0);



                    pDays[i-1].add(tPaneCalendar[c], 0,c+1);


                    //Obliger de le remettre dans un objet task sinon crash
                    Task taskSend = myTask.get(c);

                    tPaneCalendar[c].setOnMouseEntered(event-> hooverMyPaneEntered(taskSend));
                    tPaneCalendar[c].setOnMouseExited(event-> hooverMyPaneExited());
                    tPaneCalendar[c].setOnMouseClicked(event-> clicAndGoManageTask(taskSend));

                }else{
                    bTask[cptButtonTooMany] = new Button("...");
                    bTask[cptButtonTooMany].setPrefWidth(141.0);
                    bTask[cptButtonTooMany].setMaxHeight(1);
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


            gridPane.add(pDays[i-1], j, numSemaine);



            j++;

        }

    }

    public void setMainApp(Main mainApp) {

        this.mainApp = mainApp;
        try {
            updateCalendar();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


}