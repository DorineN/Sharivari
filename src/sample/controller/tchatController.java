package sample.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.Main;
import sample.model.MySQLConnexion;
import sample.model.Task;
import sample.model.TaskDAO;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class TchatController {

    private Main mainApp;

    @FXML
    public void initialize() throws ParseException {

    }

  

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }


}