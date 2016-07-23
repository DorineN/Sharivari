package app.chat;

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

    private Socket clientSocket = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;
    private int project;
    private String username;

    public ChatClient(int project, String username){
        this.project = project;
        this.username = username;
    }

    public void run(){
        try {
            clientSocket = new Socket("localhost", 8888);
            dos = new DataOutputStream(clientSocket.getOutputStream());
            dis = new DataInputStream(clientSocket.getInputStream());

            dos.writeUTF("5 " + username);
            dos.flush();

            messages.appendText(dis.readUTF());
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}