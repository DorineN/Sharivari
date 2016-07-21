package app.chat;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;

/*
 * A chat server that delivers public and private messages.
 */
public class ChatServer {
    // The server socket.
    private static ServerSocket serverSocket = null;
    // The client socket.
    private static Socket clientSocket = null;
    // List of clients connected
    private static ArrayList<String> clientsList = new ArrayList<>();

    // Save all the clients threads
    private static final ArrayList<clientThread> threads = new ArrayList<>();

    private ChatServer(){}

    public static void main(String[] args) {
        // The default port number.
        int portNumber = 2222;
        if (args.length >= 1)
            portNumber = Integer.valueOf(args[0]);

        System.out.println("Chat server started on port " + portNumber + "...");
    /*
     * Open a server socket on the portNumber (default 2222). Note that we can
     * not choose a port less than 1023 if we are not privileged users (root).
     */
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println(e);
        }

    /*
     * Create a client socket for each connection and pass it to a new client
     * thread.
     */
        while (true) {
            try {
                // We save the new user connected
                clientSocket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String name = reader.readLine();

                clientsList.add(name);

                // Save the user thread in the list
                clientThread newClientThread = new clientThread(clientSocket, threads);
                newClientThread.start();
                threads.add(newClientThread);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}

class clientThread extends Thread {
    private BufferedReader br = null;
    private PrintStream os = null;
    private Socket clientSocket = null;
    private final ArrayList<clientThread> threads;

    public clientThread(Socket clientSocket, ArrayList<clientThread> threads) {
        this.clientSocket = clientSocket;
        this.threads = threads;
    }

    public void run() {
        ArrayList<clientThread> threads = this.threads;

        try {
      /*
       * Create input and output streams for this client.
       */
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            os = new PrintStream(clientSocket.getOutputStream());
            String name = "Pseudo";

      /* Start the conversation. */
            while (true) {
                String line = br.readLine();
                if (line.startsWith("/quit")) {
                    break;
                }

                // Display the message to all connected clients
                synchronized (this) {
                    for(clientThread currentClientThread : threads){
                        currentClientThread.os.println("<" + name + "> " + line);
                    }
                }
            }

      /*
       * Clean up. Set the current thread variable to null so that a new client
       * could be accepted by the server.
       */
            synchronized (this) {
                for(clientThread currentClientThread : threads){
                    if(currentClientThread == this)
                        threads.remove(currentClientThread);
                }
            }
      /*
       * Close the output stream, close the input stream, close the socket.
       */
            br.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
        }
    }
}