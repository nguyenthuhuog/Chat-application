package src;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;


public class ChatClientGUI extends JFrame {
    private JTextArea messageArea;
    private JTextField textField;
    private ChatClient client;

    public ChatClientGUI(){
        setTitle("Chat Application");
        setBounds(50, 50, 400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // JPanel p = new JPanel(new BorderLayout()); // Using BorderLayout as layout manager

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        add(new JScrollPane(messageArea), BorderLayout.CENTER);

        textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // System.out.println("Text=" + textField.getText());
                client.sendMes(textField.getText());
                textField.setText("");
            }
            });
        add(textField, BorderLayout.SOUTH);

        this.client = new ChatClient(this::printMes);
        this.client.startClient();
    }
    public void printMes(String mes){
        messageArea.append(mes + "\n");
    }
    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            new ChatClientGUI().setVisible(true);
        });
      }
}
