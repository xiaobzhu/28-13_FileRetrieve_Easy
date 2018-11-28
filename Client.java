
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;  //client portion of a stream-socket conncetion between client and server
import java.net.Socket;

/**
 * @author xiaobzhu
 * this class is used to build up the connection client part and build up the GUI of the client
 */
public class Client extends JFrame {
    /**
     * enter information from user
     */
    private JTextField input = new JTextField();
    /**
     * display information to user
     */
    private JTextArea result = new JTextArea();
    /**
     * output stream to server
     */
    private ObjectOutputStream outputStream;
    /**
     * input stream to server
     */
    private ObjectInputStream inputStream;
    /**
     * message from server
     */
    private String message = "";
    /**
     * host server for this application
     */
    private String host;
    /**
     *socket used to communicate with server
     */
    private Socket client;
    /**
     * thie is used to set up the port number
     */
    private static final int PORT = 23555;

    /**
     * this is the constructor used to set up the GUI
     * @param host the value pass in the host server for this application
     */
    public Client(String host){
        super("Client");
        this.host = host;        // set server to which this client connects
        input.setEditable(true);
        Handler handler = new Handler();
        input.addActionListener(handler);
        add(input, BorderLayout.NORTH);
        add(new JScrollPane(result), BorderLayout.CENTER);
    }

    /**
     * This is used to connect to server and process messages from server
     */
    public void runClient(){
        try{
            result.append("Attempting connection\n");
            client = new Socket(InetAddress.getByName(host), PORT);//set up output stream for objects
            result.append("Connected to: " + client.getInetAddress().getHostName());
            getStreams();
            do
            {
                try
                {
                    message = (String) inputStream.readObject(); // read new message
                    result.append("\n" + message); // display message
                } catch (ClassNotFoundException classNotFoundException) {
                    result.append("\nUnknown object type received");
                }

            } while (!message.equals("SERVER>>> TERMINATE"));
        }catch (EOFException Exception){
            result.append("\nClient terminated connection");
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }finally {
            result.append("\nClosing connection");
            input.setEnabled(false);
            result.setEnabled(false);
            try {
                client.close(); //close the output stream
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

    }

    /**
     * this class is used to build up the actionlistener that used to send the message to server
     */
    public class Handler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            try // try to send object to server
            {
                outputStream.writeObject(e.getActionCommand());
                outputStream.flush(); // flush data to outputStream
                result.append("\nCLIENT>>> " + e.getActionCommand());
            } catch (IOException ioException) {
                result.append("\nError writing object");
            }
            input.setText("");
        }
    }

    /**
     * This method is used to get steams to send and receive data
     * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
     */
    private void getStreams() throws IOException {
        // set up output stream for objects
        outputStream = new ObjectOutputStream(client.getOutputStream());
        outputStream.flush(); // flush output buffer to send header information

        // set up input stream for objects
        inputStream = new ObjectInputStream(client.getInputStream());

        result.append("\nGot I/O streams\n");
    }
}

