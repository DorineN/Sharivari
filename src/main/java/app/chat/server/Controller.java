package app.chat.server;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


public class Controller {
    @FXML
    public VBox vBox;
    @FXML
    public TextArea serverLog;
    @FXML
    public Button startButton;

    private Server server;
    private Thread serverThread;
    private boolean isStarted = false;

    @FXML
    private void initialize(){
        Server.serverlog = serverLog;
    }

    @FXML
    private void handleStartButton(){
        if(!isStarted){
            startServer();
            isStarted = true;
            startButton.setText("Close Server");
        }else{
            closeServer();
            isStarted = false;
            startButton.setText("Start Server");
        }
    }

    public void startServer(){
        server = new Server();
        serverThread = new Thread(server);
        serverThread.start();

        serverLog.appendText("Server started on port 8888...\n");
    }

    public void closeServer(){
        try{
            synchronized (Server.connectedClientsVector) {
                for (Server.ClientThread client : Server.connectedClientsVector) {
                    client.dis.close();
                    client.dos.close();
                    client.dos2.close();

                    if (client.clientSocket != null) {
                        client.clientSocket.close();
                        client.clientSocket = null;
                    }

                    if (client.clientSocket2 != null) {
                        client.clientSocket2.close();
                        client.clientSocket2 = null;
                    }

                    client.connectedUsersThread.stop();
                    client.connectedUsersThread = null;
                    client.stop();
                    client = null;
                }
            }

            if(Server.clientSocket != null) {
                Server.clientSocket.close();
                Server.clientSocket = null;
            }

            if(Server.clientSocket2 != null) {
                Server.clientSocket2.close();
                Server.clientSocket2 = null;
            }

            if(Server.serverSocket != null) {
                Server.serverSocket.close();
                Server.serverSocket = null;
            }

            serverThread.stop();
            serverThread = null;
            server = null;
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            serverLog.appendText("Server is now closed...\n");
        }
    }
}