package app.controller;

import app.Main;
import app.chat.ChatClient;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML
    private TextArea messages;
    @FXML
    private TextArea onlineUsers;
    @FXML
    private TextField input;
    @FXML
    private Button sendMessageButton;

    private Main mainApp;
    private ChatClient chatClient;
    private Thread threadClient;
    private String username = "Az";
    private int project = 5;

    @FXML
    private void initialize(){
        ChatClient.messages = messages;
        ChatClient.input = input;
        ChatClient.onlineUsers = onlineUsers;

        chatClient = new ChatClient(project, username);
        threadClient = new Thread(chatClient);
        threadClient.start();
    }

    public Main getMainApp() {
        return mainApp;
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }
}