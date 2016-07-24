package app.chat.client;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ChatClient implements Runnable{
    public static TextArea messages;
    public static TextArea onlineUsers;
    public static TextField input;
    public static Button sendMessageButton;

    private Socket clientSocket = null;
    private Socket clientSocket2 = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private int project;
    private String username;

    public ChatClient(int project, String username){
        this.project = project;
        this.username = username;
    }

    public void process(){
        try{
            while(true) {
                messages.appendText(dis.readUTF());
            }
        }catch(Exception e){
            messages.setStyle("-fx-text-fill : #DF0101;");
            messages.setText("Le serveur est maintenant fermé...\n");
            sendMessageButton.setDisable(true);
            onlineUsers.setText("");
        }
    }

    public void sendMessage(String msg){
        try {
            dos.writeUTF(msg);
            dos.flush();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    class ConnectedUserThread extends Thread{
        private Socket clientSocket = null;
        private DataInputStream dis = null;

        public ConnectedUserThread(Socket clientSocket){
            this.clientSocket = clientSocket;

            try{
                dis = new DataInputStream(this.clientSocket.getInputStream());
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void run(){
            try{
                while(true) {
                    onlineUsers.setText(dis.readUTF());
                }
            }catch(IOException e){}
        }
    }

    public void run(){
        try {
            clientSocket = new Socket("localhost", 8888);
            clientSocket2 = new Socket("localhost", 8888);
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());

            dos.writeUTF(Integer.toString(project) + " " + username);
            dos.flush();

            ConnectedUserThread connectedUserThread = new ConnectedUserThread(clientSocket2);
            connectedUserThread.start();

            process();
        }catch(IOException e){
            messages.setStyle("-fx-text-fill : #DF0101;");
            messages.setText("Le serveur est actuellement fermé...");
        }finally {
            try {
                if(clientSocket != null)
                    clientSocket.close();

                if(clientSocket2 != null)
                    clientSocket2.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}