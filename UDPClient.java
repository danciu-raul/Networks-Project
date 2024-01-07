import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java UDPClient <serverAddress> <serverPort> <filename> <probability>");
            return;
        }

        String serverAddress = args[0];
        int serverPort = Integer.parseInt(args[1]);
        String filename = args[2];
        double probability = Double.parseDouble(args[3]);

        try {
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            DatagramSocket clientSocket = new DatagramSocket();

            // Simulate file transfer
            String fileContent = "This is the content of the file.";

            byte[] fileData = fileContent.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(fileData, fileData.length, serverInetAddress, serverPort);
            clientSocket.send(sendPacket);

            // Receive acknowledgment from the server
            byte[] acknowledgmentData = new byte[1024];
            DatagramPacket acknowledgmentPacket = new DatagramPacket(acknowledgmentData, acknowledgmentData.length);
            clientSocket.receive(acknowledgmentPacket);

            String acknowledgment = new String(acknowledgmentPacket.getData(), 0, acknowledgmentPacket.getLength());
            System.out.println("Received acknowledgment from server: " + acknowledgment);

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
