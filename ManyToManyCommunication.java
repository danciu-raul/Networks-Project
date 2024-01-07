import java.io.*;
import java.net.*;

public class ManyToManyCommunication {
    public static void main(String[] args) {
       

        try {
            int id = Integer.parseInt(args[0]);
            int numProcesses = Integer.parseInt(args[1]);
            String filename = args[2];
            double lossProbability = Double.parseDouble(args[3]);
            String protocol = args[4];
            int windowSize = Integer.parseInt(args[5]);
            int port = 9876;

            if (id == 0) {
                Server server = new Server(port, filename);
                server.start(numProcesses, lossProbability, windowSize);
            } else {
                Client client = new Client(id, "localhost", port);
                client.start();
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
