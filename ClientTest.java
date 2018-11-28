import javax.swing.*;

/**
 * @author xiaobzhu
 * This class is the main class for the client part, used to run the client application and do the test at here
 */
public class ClientTest {
    public static void main(String[] args){
        Client client; // declare client application
        if (args.length == 0) // if thno command line args
            client = new Client("127.0.0.1"); // connect to localhost
        else
            client = new Client(args[0]); // use args to connect
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.setSize(300,500);
        client.setVisible(true);
        client.runClient();//operate connection
    }
}
