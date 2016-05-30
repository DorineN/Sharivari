package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import sample.Main;
import sample.MySQLConnexion;
import sample.ProjectDAO;
import sample.UserDAO;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class HomeController {
    //Attributes
    private Main mainApp;
    private UserDAO user = null;

    @FXML
    private ComboBox listProject;
    @FXML
    private Label notification;

    @FXML
    public void handleBtnCreate() throws NamingException {
        //GO CREATE PROJECT
        try {
            mainApp.showCreateProject();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void listingProject()  {
        try {
            UserDAO userDao = new UserDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());
            int tab[] = userDao.findUserProject();
            ProjectDAO project = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());

            listProject.getItems().clear();
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
    public void goProject() throws ParseException {
        try {
            ProjectDAO project = new ProjectDAO(new MySQLConnexion("jdbc:mysql://localhost/sharin", "root", "sharin").getConnexion());
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

    public void setMainApp(Main mainApp) {
        notification.setText("Bonjour " + mainApp.getMyUser().getUserLogin());
        this.mainApp = mainApp;
        //Display projects of the current user
        this.listingProject();
    }

}
