import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * @author xiaobzhu
 * this is the main class for the Server, it used to test the server application
 */
public class ServerTest {
    /**
     * this is the main method of the server
     * @param args parameter in the main method
     */
    public static void main(String args[]){
        Server server = new Server();  // create the server
        server.setDefaultCloseOperation(EXIT_ON_CLOSE);
        server.setSize(300,500);
        server.runServer(); // run the server application
    }
}
