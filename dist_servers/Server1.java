package org.example;
import java.io.*;
import java.net.*;
import java.util.HashMap;

public class Server1 {
    private static HashMap<String, String> data = new HashMap<>();
    private static HashMap<String, Integer> replicationCount = new HashMap<>();
    private static int float_tolerance = 1; // tolerans değeri

    public static void main(String[] args) {
        data.put("key1", "value1");
        data.put("key2", "value2");

        int port = 5000;
        Thread serverThread = new Thread(() -> startServer(port));
        serverThread.start();

        while (true) {
            sendDataToOtherServer("localhost", 6000); // Server2
            sendDataToOtherServer("localhost", 7000); // Server3
            try {
                Thread.sleep(5000); // 5 saniye bekle
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void startServer(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server1 is listening on port " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection established with another server!");

                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String receivedMessage = input.readLine();
                System.out.println("Received message: " + receivedMessage);

                parseAndAddData(receivedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseAndAddData(String message) {
        String[] entries = message.split(";");
        for (String entry : entries) {
            String[] keyValue = entry.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                replicationCount.putIfAbsent(key, 0);
                int currentReplication = replicationCount.get(key);

                if (currentReplication < float_tolerance) {
                    replicationCount.put(key, currentReplication + 1);
                    data.put(key, value);
                    System.out.println("Added key: " + key + " with value: " + value);
                }
            }
        }
        System.out.println("Updated map: " + data);
    }

    private static void sendDataToOtherServer(String host, int port) {
        try (Socket socket = new Socket(host, port);
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
            StringBuilder mapString = new StringBuilder();
            for (String key : data.keySet()) {
                mapString.append(key).append("=").append(data.get(key)).append(";");
            }
            output.println(mapString.toString());
            System.out.println("Sent data to server at " + host + ":" + port + " -> " + mapString);
        } catch (IOException e) {
            System.out.println("Failed to send data to " + host + ":" + port);
        }
    }
}
