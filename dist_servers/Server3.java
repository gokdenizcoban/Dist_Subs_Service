import java.io.*;
import java.net.*;

public class Server3 {
    private static final int PORT = 5003;
    private static final int SERVER1_PORT = 5001;
    private static final int SERVER2_PORT = 5002;

    public static void main(String[] args) {
        try {
            // Server3'ün kendi soketi
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server3 başlatıldı. Port: " + PORT);

            // Server1 ve Server2'ye bağlantı kurma girişimi
            try {
                Socket socketToServer1 = new Socket("localhost", SERVER1_PORT);
                System.out.println("Server1'e bağlantı kuruldu.");
            } catch (ConnectException e) {
                System.out.println("Server1'e bağlantı kurulamadı. (Henüz başlatılmamış olabilir)");
            }

            try {
                Socket socketToServer2 = new Socket("localhost", SERVER2_PORT);
                System.out.println("Server2'ye bağlantı kuruldu.");
            } catch (ConnectException e) {
                System.out.println("Server2'ye bağlantı kurulamadı. (Henüz başlatılmamış olabilir)");
            }

            // Diğer serverlardan gelecek bağlantıları bekle
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Yeni bağlantı kabul edildi: " +
                        clientSocket.getInetAddress() + ":" + clientSocket.getPort());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
