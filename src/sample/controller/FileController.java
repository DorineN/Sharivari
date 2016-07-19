package sample.controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import sample.Main;

import java.io.*;
import java.util.ArrayList;

public class FileController {
    private Main mainApp;
    private int nbColumns;
    private int nbRows;
    private int maxAuthorizedFiles;
    private ArrayList<String> files;

    private String serveur = "127.0.0.1";
    private int port = 21;
    private String user = "admin";
    private String password = "admin";

    @FXML
    private AnchorPane anchor;
    @FXML
    private Pane pane;
    @FXML
    private GridPane gridPane;
    @FXML
    private Button backButton;
    @FXML
    private Label label;

    public FileController(){
        // Empty
    }

    @FXML
    public void initialize(){
        // Define the max number of files authorized
        nbColumns = gridPane.getColumnConstraints().size();
        nbRows = gridPane.getRowConstraints().size();
        maxAuthorizedFiles = nbColumns * nbRows;

        // Load shared files
        loadFiles();

        // Files DRAG & DROP
        pane.setOnDragOver(event -> mouseDragOver(event));
        pane.setOnDragDropped(event -> mouseDragDropped(event));
        pane.setOnDragExited(event -> pane.setStyle("-fx-border-color: #C6C6C6;"));
    }

    private void addImage(Image i, String name){
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Label filename = new Label(name);
        ImageView imageView = new ImageView();
        int numCol;
        int numRow;
        int nbFiles;

        imageView.setImage(i);
        imageView.setCursor(Cursor.HAND);
        imageView.setFitHeight(70);
        imageView.setFitWidth(70);
        imageView.setPreserveRatio(true);

        files.add(name);
        nbFiles = files.size();
        numRow = nbFiles  / nbColumns;
        if((nbFiles % nbColumns) != 0)
            numCol = (nbFiles - (numRow * nbColumns)) - 1;
        else {
            numRow--;
            numCol = (nbFiles - (numRow * nbColumns)) - 1;
        }

        vbox.getChildren().addAll(imageView, filename);
        gridPane.add(vbox, numCol, numRow);

        imageView.setOnMouseClicked(event -> {
                    if(download(name)){
                        label.setText("Le fichier " + name  + " a bien été téléchargé");
                        label.setTextFill(Color.GREEN);
                        label.setVisible(true);
                    }
                }
        );
    }

    private void mouseDragDropped(DragEvent e) {
        if(files.size() < maxAuthorizedFiles) {
            backButton.setDisable(true);
            final Dragboard db = e.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                success = true;

                // Only get the first file from the list
                File file = db.getFiles().get(0);
                if(!files.contains(file.getName())) {
                    if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) {
                        Image img = new Image("sample/images/IMG.png");
                        addImage(img, file.getName());
                    } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                        Image img = new Image("sample/images/PDF.png");
                        addImage(img, file.getName());
                    } else if (file.getName().toLowerCase().endsWith(".doc") || file.getName().toLowerCase().endsWith(".docx")) {
                        Image img = new Image("sample/images/DOC.png");
                        addImage(img, file.getName());
                    } else if (file.getName().toLowerCase().endsWith(".txt")) {
                        Image img = new Image("sample/images/TXT.png");
                        addImage(img, file.getName());
                    }
                }

                if(upload(file.getAbsolutePath())){
                    label.setText("Le fichier a bien été uploadé !");
                    label.setTextFill(Color.GREEN);
                    label.setVisible(true);
                }

                backButton.setDisable(false);
            }

            e.setDropCompleted(success);
            e.consume();
        }else{
            label.setText("Nombre de fichiers maximum atteint");
            label.setTextFill(Color.RED);
            label.setVisible(true);
        }

    }

    private  void mouseDragOver(DragEvent e) {
        final Dragboard db = e.getDragboard();

        final boolean isAccepted = db.getFiles().get(0).getName().toLowerCase().endsWith(".png")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".txt")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".jpg")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".doc")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".docx")
                || db.getFiles().get(0).getName().toLowerCase().endsWith(".pdf");

        if (db.hasFiles()) {
            if (isAccepted) {
                pane.setStyle("-fx-border-color: lightblue;"
                        + "-fx-border-width: 5;"
                        + "-fx-background-color: #C6C6C6;"
                        + "-fx-border-style: solid;");
                e.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            e.consume();
        }
    }

    private boolean upload(String path){
        boolean res = false;

        FTPClient ftpClient = new FTPClient();

        try {
            // Connection to the FTP server
            ftpClient.connect(serveur, port);
            ftpClient.login(user, password );
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // Check if the current project directory exists, if not we create it
            if(!ftpClient.changeWorkingDirectory("nomDuProjet")) {
                ftpClient.makeDirectory("nomDuProjet");
                ftpClient.changeWorkingDirectory("nomDuProjet");
            }

            File file = new File(path);
            String chemin = file.getName();
            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = ftpClient.storeFileStream(chemin);
            byte[] bytes = new byte[4096];
            int buffer;

            while((buffer = inputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, buffer);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

            // Save the result of the upload
            res = ftpClient.completePendingCommand();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    // Close FTP connection
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return res;
    }

    private void loadFiles(){
        files = new ArrayList<>();
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect(serveur, port);
            ftpClient.login(user, password );
            ftpClient.enterLocalPassiveMode();

            // Check if the current project directory exists, if not we create it
            if(!ftpClient.changeWorkingDirectory("nomDuProjet")) {
                ftpClient.makeDirectory("nomDuProjet");
                ftpClient.changeWorkingDirectory("nomDuProjet");
            }

            for(FTPFile file : ftpClient.listFiles()) {
                if (file.getName().toLowerCase().endsWith(".jpg") || file.getName().toLowerCase().endsWith(".png")) {
                    Image img = new Image("sample/images/IMG.png");
                    addImage(img, file.getName());
                } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                    Image img = new Image("sample/images/PDF.png");
                    addImage(img, file.getName());
                } else if (file.getName().toLowerCase().endsWith(".doc") || file.getName().toLowerCase().endsWith(".docx")) {
                    Image img = new Image("sample/images/DOC.png");
                    addImage(img, file.getName());
                }else if (file.getName().toLowerCase().endsWith(".txt")) {
                    Image img = new Image("sample/images/TXT.png");
                    addImage(img, file.getName());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    // Close FTP connection
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private boolean download(String remoteFile){
        boolean res = false;
        Stage directorySelector = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(directorySelector);

        if(file != null){
            String path = file.getAbsolutePath() + "\\" + remoteFile;
            FTPClient ftpClient = new FTPClient();

            try{
                ftpClient.connect(serveur, port);
                ftpClient.login(user, password);
                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.changeWorkingDirectory("nomDuProjet");

                File newFile = new File(path);
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(newFile));
                res = ftpClient.retrieveFile(remoteFile, outputStream);
                outputStream.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        return res;
    }

    @FXML
    public void getBack(){
        try{
            mainApp.showHome();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public Main getMainApp() {
        return mainApp;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}