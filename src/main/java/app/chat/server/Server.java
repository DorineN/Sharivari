package app.chat.server;

import javafx.scene.control.TextArea;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server implements Runnable{
    public static TextArea serverlog;
    public static ServerSocket serverSocket;
    public static Socket clientSocket;
    public static Socket clientSocket2;
    public static Vector<ClientThread> connectedClientsVector;

    public Server(){
        try {
            serverSocket = new ServerSocket(8888);
            connectedClientsVector = new Vector<>();
        }catch(IOException e){
            serverlog.appendText(e.getMessage());
        }
    }

    public void listen(){
        try {
            while(true){
                clientSocket = serverSocket.accept();
                clientSocket2 = serverSocket.accept();
                Identifier id = new Identifier(clientSocket, clientSocket2);
                id.start();
            }
        } catch (Exception e) {}
    }

    class Identifier extends Thread{
        private Socket clientSocket;
        private Socket clientSocket2;
        private DataInputStream dis;
        private String user;
        private int project;

        public Identifier(Socket clientSocket, Socket clientSocket2){
            try {
                this.clientSocket = clientSocket;
                this.clientSocket2 = clientSocket2;
                this.dis = new DataInputStream(this.clientSocket.getInputStream());
            }catch(IOException e){
                serverlog.appendText(e.getMessage());
            }
        }

        public void identify(){
            try {
                String data = dis.readUTF();
                String[] split = data.split(" ");
                project = (int) Integer.valueOf(split[0]);
                user = split[1];
            }catch(IOException e){
                serverlog.appendText(e.getMessage());
            }
        }

        public void run(){
            // Identify the client
            identify();
            serverlog.appendText("Incomming connection from " + user + " (" + clientSocket.getInetAddress() + ")\n");

            // Add new client to the connectedClientsVector (Vector) and start his thread
            ClientThread newClientThread = new ClientThread(clientSocket, clientSocket2, project, user);

            synchronized (connectedClientsVector){
                connectedClientsVector.add(newClientThread);
            }

            newClientThread.start();
        }
    }

    class ClientThread extends Thread{
        ConnectedUsersThread connectedUsersThread = null;
        Socket clientSocket = null;
        Socket clientSocket2 = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        DataOutputStream dos2 = null;
        String user;
        int project;

        public ClientThread(Socket clientSocket, Socket clientSocket2, int project, String user){
            this.clientSocket = clientSocket;
            this.clientSocket2 = clientSocket2;
            this.project = project;
            this.user = user;

            try{
                dis = new DataInputStream(this.clientSocket.getInputStream());
                dos = new DataOutputStream(this.clientSocket.getOutputStream());
                dos2 = new DataOutputStream(this.clientSocket2.getOutputStream());
            }catch(IOException e){
                e.printStackTrace();
            }
        }

        public void connectionMessage(){
            synchronized (connectedClientsVector) {
                for (ClientThread currentClientThread : connectedClientsVector) {
                    if (currentClientThread.project == project) {
                        try {
                            currentClientThread.dos.writeUTF(user + " a rejoint la discussion...\n");
                            currentClientThread.dos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void sendMessage(String msg){
            synchronized (connectedClientsVector) {
                for (ClientThread currentClientThread : connectedClientsVector) {
                    if (currentClientThread.project == project) {
                        try {
                            currentClientThread.dos.writeUTF("[" + user + "] : " + msg);
                            currentClientThread.dos.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        public void userLeave(){
            synchronized (connectedClientsVector) {
                for (ClientThread currentClientThread : connectedClientsVector) {
                    if (currentClientThread.project == project) {
                        try {
                            currentClientThread.dos.writeUTF(user + " a quitté la discussion...\n");
                            currentClientThread.dos.flush();
                        } catch (Exception e) {
                            System.out.println("new");
                        }
                    }
                }
            }
        }

        public void run(){
            connectionMessage();
            connectedUsersThread = new ConnectedUsersThread(this);
            connectedUsersThread.start();
            try{
                while(true){
                    String msg = dis.readUTF();
                    sendMessage(msg);
                }
            }catch(IOException e){
                synchronized (connectedClientsVector) {
                    connectedClientsVector.remove(this);
                }

                userLeave();
            }
        }
    }

    class ConnectedUsersThread extends Thread{
        private ClientThread clientThread;

        public ConnectedUsersThread(ClientThread clientThread){
            this.clientThread = clientThread;
        }

        public void displayConnectedUsers(){
            String connectedUsers = "";

            synchronized (connectedClientsVector) {
                for (ClientThread currentClientThread : connectedClientsVector) {
                    if (currentClientThread.project == clientThread.project) {
                        connectedUsers += currentClientThread.user + "\n";
                    }
                }
            }

            synchronized (connectedClientsVector) {
                for (ClientThread currentClientThread : connectedClientsVector) {
                    if (currentClientThread.project == clientThread.project) {
                        try {
                            clientThread.dos2.writeUTF(connectedUsers);
                            clientThread.dos2.flush();
                        } catch (Exception e) {}
                    }
                }
            }
        }

        public void run(){
            while(true){
                displayConnectedUsers();
            }
        }
    }

    @Override
    public void run(){
        listen();
    }
}