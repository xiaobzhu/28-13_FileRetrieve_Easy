import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;  // Server portion of a client/server stream-socket connection
import java.net.Socket;      // Server portion of a client/server stream-socket connection
import java.util.Scanner;

/**
 * @author xiaobzhu
 * This class is used to create the GUI of the Server and build the connection things at here
 */
public class Server extends JFrame {
    /**
     * this private static value is used to set the port number to be 23555,the reason why set it on the top because it can easily to fix it
     */
    private static final int PORT = 23555;
    /**
     * this is used to create a enterField
     */
    private JTextArea enterField;
    /**
     * this is used to create the socket to communicate with server
     */
    private Socket connection;
    /**
     * this is used to create the inputstream for objects
     */
    private ObjectInputStream input;
    /**
     * this private value is used to create the output stream for objects
     */
    private ObjectOutputStream output;

    /**
     * this is the constructor of the Server Class, it used to set up the GUI
     */
    public Server(){
        super("Server");
        enterField = new JTextArea();  // create the enterField
        add(new JScrollPane(enterField));
        enterField.setEnabled(false);  // set the enderField is not able to edit
        setVisible(true);
    }

    /**
     * this method is used to set up and run the server
     */
    public void runServer(){
        try
        {
            try {
                ServerSocket server = new ServerSocket(PORT, 100);   //this is used to create the sockets
                enterField.append("Waiting connection\n"); // set the enterField as the message
                connection = server.accept();  //allow the server to accept the connection
                enterField.append("connected!\n");
                getStreams();
                String message = "Connection successful";
                sendData(message);
                do // process messages sent from client
                {
                    try // read message and display it
                    {
                        message = (String) input.readObject(); // read new message
                        try {
                            Scanner reader = new Scanner(new FileReader(message)); //this scanner is used to read the file line by line
                            while (reader.hasNextLine()) {
                                sendData(reader.nextLine());
                            }
                        }catch (IOException e) {
                            sendData(message + " does not exist");
                        }

                    } catch (ClassNotFoundException classNotFoundException) {
                        enterField.append("\nUnknown object type received");
                    }

                } while (!message.equals("CLIENT>>> TERMINATE"));
            } catch (EOFException eofException) {
                enterField.append("\nServer terminated connect");
            }finally {
                closeConection();
            }
        }catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * thie getStreams method is used to send and receive the data from the client
     * @throws IOException Signals that an I/O exception of some sort has occurred. This class is the general class of exceptions produced by failed or interrupted I/O operations.
     */
    public void getStreams()throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream()); //set up output stream for objects
        output.flush();  //flush output buffer to send header information
        input = new ObjectInputStream(connection.getInputStream()); // set up the input stream for objects
        enterField.append("\nGot I/O streams\n");
    }

    /**
     * this method is used to send message to the client
     * @param message the message is the string value pass in that need to send to the client
     */
    public void sendData(String message){
        try{
            output.writeObject("SERVER>>> "+ message);  // the line88 - line89 are the working as send the object to the client
            output.flush();
        }catch (IOException ioException) {
            enterField.append("\nError writing object");
        }
    }

    /**
     * this method is used to close the stream and socket
     */
    public void closeConection(){
        enterField.append("\nTerminating connect\n");
        try {
            connection.close(); // close the socket
            input.close(); // close the input stream
            output.close(); // close the socket
        } catch (IOException ioException) {
            ioException.printStackTrace();  //printStackTrace class is used to help the developer to understand where the actual problem occurred
        }
    }
}
