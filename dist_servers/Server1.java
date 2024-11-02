import java.io.*;
import java.net.*;

public class Server1 {

    public static void main(String[] args) {
        new Server1().startServer();
    }

    public void startServer() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(5001)) {
                System.out.println("Server1 port üzerinde dinlemede: " + 5001);

                connectToServers();

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    Thread serverThread = new Thread(new ClientHandler(clientSocket));
                    serverThread.start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void connectToServers() {
        connectToServer("localhost", 5002);
        connectToServer("localhost", 5003);
    }

    private void connectToServer(String host, int port) {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = new Socket(host, port);
                    System.out.println("Bu sunucuya baglandı " + host + ":" + port);
                    break;
                } catch (IOException e) {
                    System.out.println("Sunucu ile baglanti kurulamadı " + host + ":" + port);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }).start();
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                String request;
                while ((request = in.readLine()) != null) {
                    if (request.equals("STRT")) {
                        System.out.println("Server1 komut almaya hazır");
                        out.println("Server1 baslatildi");
                    } else {
                        System.out.println("Server1'e ulasan mesaj " + request);
                        out.println("Mesaj Server1'e ulasti");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
