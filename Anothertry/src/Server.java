import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Server {
    private DatagramSocket socket;
    private byte[] fileData;

    public Server(int port, String filename) throws IOException {
        socket = new DatagramSocket(port);
        readFile(filename);
    }

    private void readFile(String filename) throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }

        fileData = bos.toByteArray();
        fis.close();
    }

    public void start(int numProcesses, double lossProbability, int windowSize) {
        try {
            System.out.println("Waiting for clients to connect...");
            InetAddress[] clients = new InetAddress[numProcesses];
            DatagramPacket[] ackPackets = new DatagramPacket[numProcesses];

            for (int i = 0; i < numProcesses; i++) {
                byte[] initData = ("INIT," + i).getBytes();
                DatagramPacket initPacket = new DatagramPacket(initData, initData.length);
                socket.receive(initPacket);
                clients[i] = initPacket.getAddress();
                ackPackets[i] = new DatagramPacket(new byte[1], 1, clients[i], initPacket.getPort());
            }

            System.out.println("All clients connected. Sending file...");

            int base = 0;
            int nextSeqNum = 0;

            while (base < fileData.length) {
                for (int i = 0; i < windowSize && (base + i) < fileData.length; i++) {
                    byte[] packetData = Arrays.copyOfRange(fileData, base + i, base + i + 1);
                    DatagramPacket packet = new DatagramPacket(packetData, packetData.length);

                    for (int j = 0; j < numProcesses; j++) {
                        socket.send(packet);
                    }
                }

                socket.setSoTimeout(1000);

                try {
                    for (int i = 0; i < numProcesses; i++) {
                        socket.receive(ackPackets[i]);
                        int ackNum = Integer.parseInt(new String(ackPackets[i].getData()));

                        if (ackNum == nextSeqNum) {
                            nextSeqNum++;
                        }
                    }

                    base = nextSeqNum;
                } catch (SocketTimeoutException e) {
                    System.out.println("Timeout. Resending window...");
                }
            }

            System.out.println("File sent successfully.");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
