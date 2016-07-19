package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.*;
import sample.model.MySQLConnexion;
import sample.model.Task;
import sample.model.TaskDAO;
import sample.model.UserDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class GanttController {

    //Attributes
    private Main mainApp;
    @FXML
    private GridPane calendarGridPane;

    Calendar calendar = Calendar.getInstance();

    enum Month {
        JANVIER,
        FEVRIER,
        MARS,
        AVRIL,
        MAI,
        JUIN,
        JUILLET,
        AOUT,
        SEPTEMBRE,
        OCTOBRE,
        NOVEMBRE,
        DECEMBRE
    }

    /** NAME TASK OR ID ? */
    enum Step {
        SPRINT_0,
        SPRINT_1,
        SPRINT_2,
        RELEASE_STAGE,
        TEST
    }

    private ObservableList<Task> data =
            FXCollections.observableArrayList(

            );

    /** PARAMETERS COLUMNS */
    int[] years = {calendar.get(Calendar.YEAR)}; //, (calendar.get(Calendar.YEAR)+1)};
    //int[] weeks = {4, 5, 4, 5, 4, 4, 5, 5, 4, 4, 5, 4};
    int[] dayWeeks = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    //int[] weekdays = {1, 2, 3, 4, 5 ,6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
    //int[] days = {1,2,3,4,5,6,7};

    int numberOfWeeks = 366;
    String[][] subSteps = {{"Analysis", "Scoping"}, {"Development", "Showcasing"}, {"Development", "Showcasing"}, {"Training", "Deployment", "Warranty Period"},  {"Training", "Deployment", "Warranty Period"}};

    public void initialize() {
        System.out.println("YEAR : " + calendar.get(Calendar.YEAR));
        System.out.println("MONTH : " + calendar.get(Calendar.MONTH)+1);
        System.out.println("DAY_OF_WEEK_IN_MONTH : " + calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH));
        System.out.println("DAY OF WEEK : " + calendar.get(Calendar.DAY_OF_WEEK));
        System.out.println("DAY OF MONTH : " + calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("WEEK OF YEAR : " + calendar.get(Calendar.WEEK_OF_YEAR));
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("MAX DAY OF MONTH : " + maxDay);
    }

    public void launch(Stage primaryStage) {

        //Size cell day
        final int cellSide = 35;
        int planningRowNumber = 1;

        //Task
        for (final Step step : Step.values()) {
            String[] currentSubSteps = subSteps[step.ordinal()];
            int subStepNumber = currentSubSteps.length;
            planningRowNumber += subStepNumber;
        }

        ////////////////////////////////////////////////////////////////////////
        /**Creation of all our columns */
        final int calendarRowNumber = planningRowNumber + 3;
        int calendarColumnNumber = 366;
            /*for (final int year : years) {
                for (final Month month : Month.values()) {
                    int weeksInMonth = weeks[month.ordinal()];
                    calendarColumnNumber += weeksInMonth;
                    for(final Week week : Week.values()) {
                         int dayWeek = dayWeeks[week.ordinal()];
                         calendarColumnNumber += dayWeek;
                    }
                }
            }*/

        for (int calendarColumnIndex = 0; calendarColumnIndex < calendarColumnNumber; calendarColumnIndex++) {
            calendarGridPane.getColumnConstraints().add(new ColumnConstraints(cellSide, cellSide, cellSide, Priority.NEVER, HPos.CENTER, true));
        }
        for (int calendarRowIndex = 0; calendarRowIndex < calendarRowNumber; calendarRowIndex++) {
            calendarGridPane.getRowConstraints().add(new RowConstraints(cellSide, cellSide, cellSide, Priority.NEVER, VPos.CENTER, true));
        }

        int yearColumn = 0;
        /** PARAMETERS ROW  */
        int yearRow = 0;
        int monthRow = 1;
        int weekRow = 2;

        /** FILL THE COLUMNS WITH PARAMETERS */
        /** YEARS */
        for (final int year : years) {
            final Label yearLabel = new Label(String.valueOf(year));
            yearLabel.getStyleClass().add("planning-cell");
            yearLabel.setAlignment(Pos.CENTER);
            yearLabel.setMaxWidth(Integer.MAX_VALUE);
            //GridPane.setConstraints(yearLabel, 2, 0);
            GridPane.setConstraints(yearLabel, yearColumn, yearRow, numberOfWeeks, 1);
            calendarGridPane.getChildren().add(yearLabel);
            int weekOffset = 0;
            //int dayOffset = 0;

            /** MONTHS */
            int monthColumn = yearColumn;
            int monthNum = 1;
            for (final Month month : Month.values()) {
                final int weeksInMonth = dayWeeks[month.ordinal()];
                final Label monthLabel = new Label(String.valueOf(month));
                monthLabel.getStyleClass().add("planning-cell");
                monthLabel.getStyleClass().add("strong-cell");
                monthLabel.setAlignment(Pos.CENTER);
                monthLabel.setMaxWidth(Integer.MAX_VALUE);
                GridPane.setConstraints(monthLabel, monthColumn, monthRow, weeksInMonth, 1);
                calendarGridPane.getChildren().add(monthLabel);
                monthColumn += weeksInMonth;

                /** DAYS */
                for (int day = 1; day < weeksInMonth+1; day++, weekOffset++) {
                    int weekColumn = yearColumn + weekOffset;
                    final Label dayNumberLabel = new Label(String.valueOf(day));

                    dayNumberLabel.getStyleClass().add("planning-cell");
                    dayNumberLabel.setAlignment(Pos.CENTER);
                    dayNumberLabel.setMaxWidth(Integer.MAX_VALUE);
                    GridPane.setConstraints(dayNumberLabel, weekColumn, weekRow, 1, 1,HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
                    calendarGridPane.getChildren().add(dayNumberLabel);


                    String dateString = String.format("%d-%d-%d", year, monthNum, day);
                    System.out.println(dateString);
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    String dayOfWeek = new SimpleDateFormat("EEEE", Locale.FRENCH).format(date);
                    System.out.println(dayOfWeek);

                    Label dayNameLabel = new Label(String.valueOf(dayOfWeek.substring(0,1).toUpperCase()+dayOfWeek.substring(1,2)));
                    dayNameLabel.getStyleClass().add("planning-cell");
                    dayNameLabel.setAlignment(Pos.CENTER);
                    dayNameLabel.setMaxWidth(Integer.MAX_VALUE);
                    GridPane.setConstraints(dayNameLabel, weekColumn, weekRow+1, 1, 1,HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS);
                    calendarGridPane.getChildren().add(dayNameLabel);
                }
                monthNum++;
            }
            yearColumn += numberOfWeeks;
        }

        for (int calendarRowIndex = 4; calendarRowIndex < calendarRowNumber; calendarRowIndex++) {
            for (int calendarColumnIndex = 0; calendarColumnIndex < calendarColumnNumber; calendarColumnIndex++) {
                Label cell = new Label();
                cell.getStyleClass().add("planning-cell");
                cell.setPrefWidth(cellSide);
                cell.setPrefHeight(cellSide);
                GridPane.setConstraints(cell, calendarColumnIndex, calendarRowIndex);
                calendarGridPane.getChildren().add(cell);
            }
        }

            /*getNodeFromGridPane(calendarGridPane, 4, 4).setStyle("-fx-background-color: #0d151f");
            getNodeFromGridPane(calendarGridPane, 5, 4).setStyle("-fx-background-color: #0d151f");
            getNodeFromGridPane(calendarGridPane, 6, 4).setStyle("-fx-background-color: #0d151f");*/

        //////////////////////////////////
        //final GridPane planning = GridPaneBuilder.create().build();
        /**INFORMATIONS PROJECT TASK */
        final GridPane planning = new GridPane();
        for (int planningColumnIndex = 0; planningColumnIndex < 3; planningColumnIndex++) {
            final Priority hgrow;
            if (planningColumnIndex < 2) {
                hgrow = Priority.NEVER;
            }else {
                hgrow = Priority.ALWAYS;
            }
            int size;
            //if (planningColumnIndex < 5){
            size = 50;
                /*} else {
                    size = -1;
                }*/
            planning.getColumnConstraints().add(new ColumnConstraints(size, size, size, hgrow, HPos.LEFT, true));
        }
        for (int planningRowIndex = 0; planningRowIndex < planningRowNumber; planningRowIndex++) {
            final int size;
            if (planningRowIndex == 0) {
                size = 3 * cellSide;
            } else {
                size = cellSide;
            }
            planning.getRowConstraints().add(new RowConstraints(size, size, size, Priority.NEVER, VPos.TOP,true));
        }
        int agendaRow = 0;
        int agendaColumn = 5;
        final ScrollPane calendarScroll = new ScrollPane(calendarGridPane);
        calendarScroll.getStyleClass().add("planning-scroll");
        calendarScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        calendarScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //Vertical scroll
        //calendarScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        GridPane.setConstraints(calendarScroll, agendaColumn, agendaRow, 4, Integer.MAX_VALUE);
        planning.getChildren().add(calendarScroll);
        int idColumn = 0;
        int taskRow = 2; //Positionnement des etapes
        int nameColumn = 1;
        int durationColumn = 2;
        int resourceColumn = 3;
        //int subStepRow = 2; //Positionnement des sous etapes
        //for (final Step step : Step.values()) {

        /**Diplay Task in GridPane */
        for (Task aData : data) {
            // final Task currentSubSteps = data.get(i);
            //final int subStepNumber = data.size();
            /**Id Column */
            final Label idTitle = new Label("ID");
            planning.setConstraints(idTitle, idColumn, 1, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            planning.getChildren().add(idTitle);
            final Label idLabel = new Label(String.valueOf(aData.getIdTask()));
            idLabel.getStyleClass().add("planning-cell");
            idLabel.getStyleClass().add("strong-cell");
            idLabel.getStyleClass().add("first-cell");
            idLabel.setAlignment(Pos.CENTER);
            idLabel.setMaxWidth(35);
            idLabel.setMaxHeight(35);
            GridPane.setConstraints(idLabel, idColumn, taskRow, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            planning.getChildren().add(idLabel);

            /**Name column */
            final Label nameTitle = new Label("Nom");
            planning.setConstraints(nameTitle, nameColumn, 1, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            planning.getChildren().add(nameTitle);
            final Label nameLabel = new Label(aData.getNameTask());
            nameLabel.getStyleClass().add("planning-cell");
            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setMaxHeight(35);
            nameLabel.setMaxWidth(Integer.MAX_VALUE);
            GridPane.setConstraints(nameLabel, nameColumn, taskRow, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            planning.getChildren().add(nameLabel);

            /**Duration column */
            final Label durationTitle = new Label("Durée");
            planning.setConstraints(durationTitle, durationColumn, 1, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            planning.getChildren().add(durationTitle);
            final Label durationLabel = new Label(String.valueOf(aData.getDurationTask()));
            durationLabel.getStyleClass().add("planning-cell");
            durationLabel.setAlignment(Pos.CENTER);
            durationLabel.setMaxWidth(35);
            durationLabel.setMaxHeight(35);
            GridPane.setConstraints(durationLabel, durationColumn, taskRow, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            planning.getChildren().add(durationLabel);

            /**Resource column */
            final Label resourceTitle = new Label("Ressource");
            planning.setConstraints(resourceTitle, resourceColumn, 1, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            planning.getChildren().add(resourceTitle);
            int idTask = aData.getIdTask();
            Label resourceLabel;
            try {
                TaskDAO taskdao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                int userId = taskdao.findUserTask(idTask);
                UserDAO userdao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
                String userName = userdao.findLoginUser(userId);
                if (!Objects.equals(userName, "")) {
                    resourceLabel = new Label(userName);
                } else {
                    resourceLabel = new Label("Aucune");
                }
                resourceLabel.getStyleClass().add("planning-cell");
                resourceLabel.setAlignment(Pos.CENTER);
                resourceLabel.setMaxHeight(35);
                resourceLabel.setMaxWidth(100);
                GridPane.setConstraints(resourceLabel, resourceColumn, taskRow, 1, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
                planning.getChildren().add(resourceLabel);
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            /** TEST to fill the calendar with task rectangle */
            Rectangle taskRec = new Rectangle();
            taskRec.getStyleClass().add("taskRec");
            taskRec.setHeight(cellSide);
            //Duration
            int cellSize = cellSide * aData.getDurationTask();
            taskRec.setWidth(cellSize);
            System.out.println("cellSize" + cellSide * aData.getDurationTask());
            //Id task
            GridPane.setRowIndex(taskRec, taskRow+2);
            System.out.println("taskRow" + taskRow);
            //Start Date
            Calendar calendarDate = Calendar.getInstance();
            calendarDate.setTime(aData.getEstimateStartDateTask());
            int dayOfYear = calendarDate.get(Calendar.DAY_OF_YEAR);
            System.out.println("aData.getEstimateStartDateTask()" + dayOfYear);
            GridPane.setColumnIndex(taskRec, dayOfYear);
            //GridPane.setConstraints(taskRec, 5, 5, 10, 1, HPos.CENTER, VPos.TOP, Priority.ALWAYS, Priority.ALWAYS);
            calendarGridPane.getChildren().addAll(taskRec);

            taskRow++;
        }

            /*SCROLL BOTTOM WINDOW
            final Slider slider = new Slider();
            slider.valueProperty().addListener((observableValue, oldValue, newValue) -> {
                //double hMin = calendarScroll.getHmin();
                //double hMax = calendarScroll.getHmax();
                //double hValue = calendarScroll.getHvalue();
                //double vWidth = calendarScroll.getWidth();
                // double mWidth = calendar.width();
                //System.out.printf("val %f\tmin %f value %f max %f width %f maxWidth %f", newValue, hMin, hValue, hMax, vWidth).println();
                calendarScroll.setHvalue(newValue.doubleValue() / 100d);
            });*/
        final BorderPane root = new BorderPane();
        root.setCenter(planning);
        //root.setBottom(slider);


        /**Display our specific scene*/
        final Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("../style/style.css").toExternalForm());
        primaryStage.setScene(scene);
        //primaryStage.setWidth(600);
        //primaryStage.setHeight(500);
        primaryStage.show();
        //
//        ScenicView.show(scene);
    }

    @FXML
    public void retrieveTaskProject() {
        int idProject = Main.getMyProject().getProjectId();

        try {
            TaskDAO taskDao = new TaskDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            List<Task> taskList = taskDao.findTaskProject(idProject);
            for (Task aTaskList : taskList) {
                data.add(new Task(aTaskList.getIdTask(), aTaskList.getNameTask(), aTaskList.getDescriptionTask(), aTaskList.getDurationTask(), aTaskList.getIdPriority(), aTaskList.getEstimateStartDateTask(),
                        aTaskList.getEstimateEndDateTask(), aTaskList.getRealStartDateTask(), aTaskList.getRealEndDateTask(), aTaskList.getIdStatus(), aTaskList.getIdPriority(), aTaskList.getNameStatus(), aTaskList.getNamePriority())
                );
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**To retrieve one node */
    private Node getNodeFromGridPane(GridPane gridPane, int col, int row) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    /** Return button */
    @FXML
    private void backProject() {
        try {
            mainApp.showProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainApp(Main mainApp) {
        retrieveTaskProject();
        launch(mainApp.getPrimaryStage());
        this.mainApp = mainApp;
    }

}