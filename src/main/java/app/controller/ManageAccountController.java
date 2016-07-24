package app.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import app.Main;
import app.model.User;
import app.util.PluginsLoader;

import javax.naming.NamingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Created by Loïc on 29/05/2016.
 */
public class ManageAccountController {

    // Attributes
    private Main mainApp;

    @FXML
    GridPane gridPane;

    @FXML
    Label msgValidate;

    @FXML
    public void initialize() {

    }

    public void loadAllUser() {
        ArrayList<User> lUser = new ArrayList();
        lUser = mainApp.userDao.findAllUserExeptOne(mainApp.getMyUser().getUserId());
        Pane[] tPane = new Pane[lUser.size()];
        Button[] tButtonReset = new Button[lUser.size()];
        Button[] tButtonDelete = new Button[lUser.size()];
        Button[] tButtonUpdate = new Button[lUser.size()];
        for (int i = 0; i<lUser.size(); i++){
            tPane[i] = new Pane();
            tButtonReset[i] = new Button("Réinitialiser mot de passe");
            tButtonUpdate[i] = new Button("Modifier type compte");
            tButtonDelete[i] = new Button("Supprimer");


            tPane[i].getChildren().add(new Label(lUser.get(i).getUserLogin() + "(type compte : "+lUser.get(i).getUserType()+")"));
            tPane[i].setStyle("-fx-border-color: #e1e5cd; -fx-background-color : white; -fx-padding : 10px; -fx-font-size:16px;");
            gridPane.add(tPane[i], 0, i);
            gridPane.add(tButtonReset[i], 1, i);
            gridPane.add(tButtonUpdate[i], 2, i);
            gridPane.add(tButtonDelete[i], 3, i);


            int finalI = i;
            User user = lUser.get(i);
            tButtonReset[i].setOnMouseClicked(event-> resetMpd(user));
            tButtonDelete[i].setOnMouseClicked(event-> deleteAccount(user));
            tButtonUpdate[i].setOnMouseClicked(event-> updateType(user));

        }

    }

    private void updateType(User user) {
        String msg;
        String type ="user";
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mise a jour du compte");
        if (user.getUserType().equals("adm")){
            msg = "Voulez vous passer le compte de "+user.getUserLogin()+" en compte utilisateur ?";

        }else{
            msg = "Voulez vous passer le compte de "+user.getUserLogin()+" en compte admin ?";
            type ="adm";
        }
        alert.setHeaderText(msg);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            mainApp.userDao.updateTypeAccount(user.getUserId(), type);
            msgValidate.setText("Compte modifié");
            gridPane.getChildren().clear();
            loadAllUser();
        } else {

        }
    }


    private void deleteAccount(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppression de compte");
        alert.setHeaderText("Voulez vous supprimer le compte de " + user.getUserLogin() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            mainApp.userDao.deleteExecute(user.getUserId());
            mainApp.userDao.deleteParticipate(user.getUserId());
            mainApp.userDao.deleteAccount(user.getUserId());
            msgValidate.setText("Compte supprimé");
            gridPane.getChildren().clear();
            loadAllUser();
        } else {

        }
    }

    private void resetMpd(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Réinitialisation de mot de passe");
        alert.setHeaderText("Voulez vous réinitialiser le mot de passe du compte de " + user.getUserLogin() + "? Le nouveau mot de passe sera : p@ssw0rd ");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            mainApp.userDao.resetPassword(user.getUserId());
            msgValidate.setText("Mot de passe réinitialisé");
        } else {

        }


    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }



}