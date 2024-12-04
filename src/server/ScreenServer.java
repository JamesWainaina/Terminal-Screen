package server;

import parser.CommandParser;
import screen.TerminalScreen;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The ScreenServer class is responsible for accepting client connections,
 * receiving command data, and executing them on the TerminalScreen using the CommandParser.
 */
public class ScreenServer implements Runnable {
    private int port = 8000;
    private TerminalScreen screen;
    private CommandParser parser;
    private boolean isScreenSetUp = false;  // Flag to check if the screen is set up

    /**
     * Constructor to initialize the ScreenServer.
     * Initializes the CommandParser.
     */
    public ScreenServer() {
        this.parser = new CommandParser();
    }

    /**
     * The entry point for the server. This method will be run by a separate thread.
     * It starts a server socket to listen for incoming client connections.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Screen server is running. Waiting for client connections...");

            // The server keeps running and accepting client connections
            while (true) {
                // Accept incoming client connections
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());

                // Handle the client connection in a separate method
                handleClientConnection(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method handles communication with a single client.
     * It reads commands from the client, processes them, and updates the screen accordingly.
     *
     * @param socket The socket representing the client connection
     */
    private void handleClientConnection(Socket socket) {
        try (
                // Set up input and output streams for reading and writing to the client
                InputStream input = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            String command;
            // Continuously read commands from the client until the connection is closed
            while ((command = reader.readLine()) != null) {
                System.out.println("Received command: " + command);

                // Split the received command into parts (command byte and data)
                String[] parts = command.split(":");
                int commandType = Integer.parseInt(parts[0].replace("0x", ""), 16);  // Strip the "0x" and parse as hex
                String[] dataParts = parts[1].split(",");
                byte[] data = new byte[dataParts.length];

                // Convert the comma-separated data into a byte array
                for (int i = 0; i < dataParts.length; i++) {
                    data[i] = Byte.parseByte(dataParts[i]);
                }

                // Check for screen setup command (let's assume 0x1 is the setup command)
                if (commandType == 0x1) {
                    // Setup the screen using the provided width, height, and colorMode
                    int width = data[0];
                    int height = data[1];
                    int colorMode = data[2];
                    screen = new TerminalScreen(width, height, colorMode);  // Set up a new screen
                    isScreenSetUp = true;
                    System.out.println("Screen setup complete: " + width + "x" + height + ", Color mode: " + colorMode);
                    writer.println("Screen setup complete.");
                    continue;  // Skip further processing of the screen setup command
                }

                // Ensure the screen is set up before any other command
                if (!isScreenSetUp) {
                    writer.println("Error: Screen not set up. Please set up the screen first.");
                    System.out.println("Error: Screen not set up.");
                    continue;
                }

                // Parse and execute the command using the CommandParser
                try {
                    parser.parseAndExecute(commandType, data, screen);
                    // Render the updated screen after processing the command
                    screen.renderScreen();
                    // Send a response to the client indicating that the command was processed
                    writer.println("Command processed and screen updated.");
                    System.out.println("Command processed and screen updated.");
                } catch (IllegalArgumentException e) {
                    writer.println("Error: " + e.getMessage());
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close the connection after the client disconnects or an error occurs
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The main method that starts the server in a new thread.
     * It creates a new instance of ScreenServer and starts the server thread.
     */
    public static void main(String[] args) {
        // Create a new instance of ScreenServer and start it in a separate thread
        ScreenServer server = new ScreenServer();
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
