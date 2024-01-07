import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class UDPServer {
    public static void main(String[] args) {
        try {
            int serverPort = 9876;
            DatagramSocket serverSocket = new DatagramSocket(serverPort);

            ExecutorService executorService = Executors.newFixedThreadPool(10);

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                serverSocket.receive(receivePacket);

                executorService.submit(new ServerHandler(serverSocket, receivePacket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}