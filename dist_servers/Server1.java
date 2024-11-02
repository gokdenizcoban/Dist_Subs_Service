import java.io.*;
import java.net.*;

public class Server1 {
    private static final int PORT = 5001;
    private static final int SERVER2_PORT = 5002;
    private static final int SERVER3_PORT = 5003;

    public static void main(String[] args) {
        try {
            // Server1'in kendi soketi
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server1 başlatıldı. Port: " + PORT);

            // Server2 ve Server3'e bağlantı kurma girişimi
            try {
                Socket socketToServer2 = new Socket("localhost", SERVER2_PORT);
                System.out.println("Server2'ye bağlantı kuruldu.");
            } catch (ConnectException e) {
                System.out.println("Server2'ye bağlantı kurulamadı. (Henüz başlatılmamış olabilir)");
            }

            try {
                Socket socketToServer3 = new Socket("localhost", SERVER3_PORT);
                System.out.println("Server3'e bağlantı kuruldu.");
            } catch (ConnectException e) {
                System.out.println("Server3'e bağlantı kurulamadı. (Henüz başlatılmamış olabilir)");
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
