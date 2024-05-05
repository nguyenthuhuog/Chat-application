package src;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Thread;

public class ChatServer {
    private static List<ClientHandler> clients = new ArrayList<>();
    public static void main(String[] args) throws IOException {
      try (ServerSocket serverSocket = new ServerSocket(50000))
        {
            System.out.println("Server started. Waiting for clients...");

            while(true){
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Spawn a new thread for each client
                ClientHandler clientThread = new ClientHandler(clientSocket);
                clients.add(clientThread);
                new Thread(clientThread).start();
            }
        }catch (IOException e) {e.printStackTrace();} 
  }

    public static class ClientHandler implements Runnable {
        public Socket clientSocket;
        public String id;
        private PrintWriter out;
        private BufferedReader in;
    
        public ClientHandler (Socket socket){
            this.clientSocket = socket;
            try{
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            }catch (IOException e) {e.printStackTrace();} 
            
        }
        public void run() {
            out.println("Enter your ID: ");
            try{this.id = in.readLine();  // Read user input
            }catch (IOException e) {e.printStackTrace();} 
            out.println("Your ID is: " + id + "\nYou can start chatting");
            sendMes("User " + id + " has joined the chat", true);
            
            try{
                String input;
                while ((input = in.readLine()) != null){
                    sendMes(input, false);
                }
            }catch (IOException e) {e.printStackTrace();} 
            finally{
                try{
                    in.close();
                    out.close();
                    clientSocket.close();
                    sendMes("User " + id + " has left the chat", true);
                }catch (IOException e) {e.printStackTrace();} 
            }
        }
        public void sendMes(String mes, boolean fromServer){
            // send the message to everyone else in the group chat
            if (!fromServer) mes = id + ": " + mes;
            for (ClientHandler clientX : clients){
                clientX.out.println(mes);
            }
        }
    }
}