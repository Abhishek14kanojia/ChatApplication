import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

public class Client extends JFrame{
    

    Socket socket;
    BufferedReader br;
    PrintWriter out;
    //Component
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);


    //Constructor
    public  Client() {
        try {
            System.out.println("Sending to request to server");
            socket = new Socket("127.0.0.1",4444);
            System.out.println("Connection done..");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();

        } catch (Exception e) {
        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if (e.getKeyCode() == 10) {
                    String contentTOSend = messageInput.getText();
                    messageArea.append("Me : " + contentTOSend + "\n");
                    out.println(contentTOSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();


                }

            }
            
        });

    }

    private void createGUI(){
        //gui
        this.setTitle("Client Messager[End]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //components..
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("ChatIcon3.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);


        this.setLayout(new BorderLayout());
        //Adding the Components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);


        this.setVisible(true);

    }

    public void startReading() {
        //thread1 --
        Runnable r1 = ()->{

            System.out.println("reader started...");
            while(true){
                try{
                String msg = br.readLine();
                if(msg.equals("Exit")){
                    System.out.println("Client terminated the chat");
                    JOptionPane.showMessageDialog(this, "Client terminated the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println("Client : " + msg);
                messageArea.append("Server : " + msg + "\n");

            }catch(Exception e){
              //  e.printStackTrace();
            System.out.println("Connection Closed...");

            }


        }

    };

    new Thread(r1).start();
}
public void startWriting() {
    //thread2 --
    Runnable r2 = () -> {
        System.out.println("Writer started..");
        while (true && !socket.isClosed()){
            try {
                
                BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                
                String content = br1.readLine();
                out.println(content);
                out.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };

    new Thread(r2).start();
}
    public static void main(String[] args) {
        
        System.out.println("This is Client");
        new Client();
    }
}
