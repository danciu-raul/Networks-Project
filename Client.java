import java.io.*;
import java.net.*;

public class Client {
    private DatagramSocket socket;
    private InetAddress serverAddress;
    private int id;

    public Client(int id, String serverHostname, int serverPort) throws IOException {
        this.id = id;
        this.serverAddress = InetAddress.getByName(serverHostname);
        socket = new DatagramSocket();
        socket.setSoTimeout(1000);
        sendInitData(serverPort);
    }

    private void sendInitData(int serverPort) throws IOException {
        byte[] initData = ("INIT," + id).getBytes();
        DatagramPacket initPacket = new DatagramPacket(initData, initData.length, serverAddress, serverPort);
        socket.send(initPacket);
    }

    public void start() {
        try {
            while (true) {
                byte[] data = new byte[1];
                DatagramPacket packet = new DatagramPacket(data, data.length);

                socket.receive(packet);
                processPacket(packet);
                sendAck(packet);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }

    private void processPacket(DatagramPacket packet) {
        System.out.println("Client " + id + " received: " + packet.getData()[0]);
    }

    private void sendAck(DatagramPacket receivedPacket) throws IOException {
        int ackNum = Integer.parseInt(new String(receivedPacket.getData()));
        byte[] ackData = Integer.toString(ackNum).getBytes();
        DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, serverAddress, receivedPacket.getPort());
        socket.send(ackPacket);
    }
}
