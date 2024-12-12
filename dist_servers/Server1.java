import java.io.*;
import java.net.*;
import com.google.protobuf.*; // Ensure you have the Protobuf dependency

// Import generated Protobuf classes
import myprotobuf.ConfigurationOuterClass.Configuration;
import myprotobuf.MessageOuterClass.Message;
import myprotobuf.CapacityOuterClass.Capacity;

public class Server1 {
    private static final int PORT = 5001;
    private static final String[] OTHER_SERVERS = {"localhost:5002", "localhost:5003"};

    public static void main(String[] args) {
        new Thread(() -> startServer(PORT)).start();
        connectToOtherServers(OTHER_SERVERS);
    }

    private static void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server1 started on port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Server1 accepted connection from: " + clientSocket.getInetAddress().getHostAddress());

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Error in Server1: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                InputStream input = clientSocket.getInputStream();
                OutputStream output = clientSocket.getOutputStream()
        ) {
            // Deserialize incoming Protobuf message
            Message request = Message.parseFrom(input);
            System.out.println("Server1 received command: " + request.getDemand());

            Message response;
            if (request.getDemand() == Message.DemandType.STRT) {
                response = Message.newBuilder()
                        .setDemand(Message.DemandType.STRT)
                        .setResponse(Message.ResponseType.YEP)
                        .build();
                System.out.println("Server1 sent response: YEP");
            } else {
                response = Message.newBuilder()
                        .setDemand(request.getDemand())
                        .setResponse(Message.ResponseType.NOP)
                        .build();
                System.out.println("Server1 sent response: NOP");
            }

            // Serialize response message
            response.writeTo(output);
        } catch (IOException e) {
            System.err.println("Error in Server1 handling client: " + e.getMessage());
        }
    }

    private static void connectToOtherServers(String[] servers) {
        for (String server : servers) {
            String[] parts = server.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            try (Socket socket = new Socket(host, port)) {
                System.out.println("Server1 connected to " + host + ":" + port);

                // Send a test Protobuf message to the other server
                Message message = Message.newBuilder()
                        .setDemand(Message.DemandType.STRT)
                        .build();
                message.writeTo(socket.getOutputStream());
            } catch (IOException e) {
                System.err.println("Server1 failed to connect to " + host + ":" + port + " - " + e.getMessage());
            }
        }
    }
}
