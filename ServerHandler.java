import java.io.*;
import java.net.*;
import java.util.concurrent.*;



class ServerHandler implements Runnable {
    private DatagramSocket serverSocket;
    private DatagramPacket receivePacket;

    public ServerHandler(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        this.serverSocket = serverSocket;
        this.receivePacket = receivePacket;
    }

    @Override
    public void run() {
        try {
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();
            byte[] data = receivePacket.getData();
            int length = receivePacket.getLength();

            // Process the received data, e.g., file transfer logic
            String receivedData = new String(data, 0, length);
            System.out.println("Received from client: " + receivedData);

            // Send acknowledgment to the client
            String acknowledgment = "ACK";
            byte[] acknowledgmentData = acknowledgment.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(acknowledgmentData, acknowledgmentData.length, clientAddress, clientPort);
            serverSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}