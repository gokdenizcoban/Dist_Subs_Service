import java.io.*;
import java.net.*;

public class Server2 {
    private static final int PORT = 5002;
    private static final String[] OTHER_SERVERS = {"localhost:5001", "localhost:5003"};

    public static void main(String[] args) {
        new Thread(() -> startServer(PORT)).start();
        connectToOtherServers(OTHER_SERVERS);
    }

    private static void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server2 started on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server2 accepted connection from: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error in Server2: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String command = in.readLine();
            System.out.println("Server2 received command: " + command);

            if ("STRT".equals(command)) {
                out.println("YEP");
                System.out.println("Server2 sent response: YEP");
            } else {
                out.println("NOP");
                System.out.println("Server2 sent response: NOP");
            }
        } catch (IOException e) {
            System.err.println("Error in Server2 handling client: " + e.getMessage());
        }
    }

    private static void connectToOtherServers(String[] servers) {
        for (String server : servers) {
            String[] parts = server.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            try (Socket socket = new Socket(host, port)) {
                System.out.println("Server2 connected to " + host + ":" + port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Hello from Server2 to " + host + ":" + port);
            } catch (IOException e) {
                System.err.println("Server2 failed to connect to " + host + ":" + port + " - " + e.getMessage());
            }
        }
    }
}
