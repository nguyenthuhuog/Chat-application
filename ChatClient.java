package src;
import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class ChatClient {
  private Socket socket = null;
  private BufferedReader inputConsole = null; // get input from keyboard
  private PrintWriter out = null;
  private BufferedReader in = null;
  private Consumer<String> printMessage;

  public ChatClient(Consumer<String> printMes){
    try{ 
      // init
      socket = new Socket("127.0.0.1", 50000);
      System.out.println("Connected to server.");
      inputConsole = new BufferedReader(new InputStreamReader(System.in));
      out = new PrintWriter(socket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.printMessage = printMes;

    } catch (UnknownHostException u) {
      System.out.println("Host unknown: " + u.getMessage());
    } catch (IOException i) {
      System.out.println("Unexpected exception: " + i.getMessage());
    }
  }

  public void sendMes(String line){
    // while (!line.equals("exit"))
      out.println(line);
  } 
  public void startClient(){
    // a thread to show server message
    new Thread(() -> {
      try{
        String sysMes;
        while((sysMes = in.readLine()) != null){
          printMessage.accept(sysMes);
        }
      }catch (IOException e) {e.printStackTrace();} 

      try{
        socket.close();
        inputConsole.close();
        out.close();
        in.close();
      }catch (IOException e) {e.printStackTrace();} 
    }).start();
    
  }
  public static void main(String[] args) throws IOException {
    ChatClient client = new ChatClient(str -> {});
  }
}