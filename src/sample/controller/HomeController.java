package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import sample.Main;
import sample.MySQLConnexion;
import sample.ProjectDAO;
import sample.UserDAO;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;

public class HomeController {

    //Attributes
    private Main mainApp;
    Calendar date = Calendar.getInstance();
    int month = date.get(date.MONTH);
    int year = date.get(date.YEAR);
    int maxDay = date.getActualMaximum(Calendar.DAY_OF_MONTH);
    String tMonth[] = {"Janvier","Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Aout", "Septembre", "Octobre", "Novembre", "Décembre"};
    String[] tDays = new String[] { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};


    @FXML
    private GridPane gridPaneWeek;
    @FXML
    private GridPane gridPaneDay;


    @FXML
    private ComboBox listProject;

    @FXML
    private Label notification;

    public HomeController() {
        updateWeek();
    }

    public void updateWeek(){

    }

    @FXML
    public void listingProject()  {
        try {
            UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            int tab[] = userDao.findUserProject();
            ProjectDAO project = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());

            listProject.getItems().clear();
            System.out.println( tab.length);
            for(int i = 0; i< tab.length; i++){
                String result = project.find(tab[i]).getProjectName();
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
            ProjectDAO project = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "").getConnexion());
            if (listProject.getValue() != null && !listProject.getValue().toString().isEmpty()) {

                String name = listProject.getValue().toString();
                System.out.println("Le nom du projet sélectionné est : " + name);
                System.out.println("Le projet : " + project.find(name));
                mainApp.setMyProject(project.find(name));

                listProject.setValue(null);

                //GO PROJECT DASHBOARD
                mainApp.showProject();

            } else {
                notification.setText("Vous n'avez pas sélectionné de projet!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
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
