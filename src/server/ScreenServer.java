package server;

import parser.CommandParser;
import screen.TerminalScreen;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The ScreenServer class is responsible for accepting client connections,
 * receiving commands from clients, and executing those commands on a TerminalScreen.
 * It uses a CommandParser to interpret and execute the received commands.
 */
public class ScreenServer implements Runnable {
    private int port = 8000; // Port number for the server
    private TerminalScreen screen; // The screen that will be controlled by client commands
    private CommandParser parser; // The parser for interpreting commands
    private Map<Socket, Boolean> clientStates; // Track the setup state for each client


    /**
     * Constructor to initialize the ScreenServer.
     */
    public ScreenServer() {
        this.parser = new CommandParser();
        this.screen = null; // The screen will be initialized when the setup command is received
        this.clientStates = new ConcurrentHashMap<>(); // Initialize the client state map

    }

    /**
     * The run method serves as the entry point for the server when running in a separate thread.
     * It starts a server socket that listens for incoming client connections and handles each connection.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Screen server is running. Waiting for client connections...");

            // The server runs indefinitely, accepting multiple client connections
            while (true) {
                // Accept incoming client connections and create a socket for communication
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());

                // Initialize the client's setup state to false (not set up yet)
                clientStates.put(socket, false);

                // Handle the client connection (processing commands)
               new Thread(() -> handleClientConnection(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles communication with a single client.
     * This method reads commands from the client, processes them, and updates the screen accordingly.
     *
     * @param socket The socket representing the client connection
     */
    private void handleClientConnection(Socket socket) {
        try (
                InputStream input = socket.getInputStream();
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            byte[] buffer = new byte[256];
            int bytesRead;
            // Reading the line from client and returns the line as a String
            while ((bytesRead = input.read(buffer)) != -1) {
                if (bytesRead < 2) {
                    // Command must have at least a command byte and a length byte
                    writer.println("Error: Invalid command format.");
                    continue;
                }

                // Extract the command byte and length byte
                int commandType = buffer[0] & 0xFF;
                int length = buffer[1] & 0xFF;

                // check if the length is valid
                if (length > bytesRead - 2){
                    writer.println("Error: Invalid length byte.");
                    continue;
                }

                // Extract the bytes
                byte[] data = new byte[length];
                System.arraycopy(buffer, 2, data,0,length);

                System.out.println("Received command: " + commandType);
                System.out.println("Data length: " + length);

                // check the EOF command (0xFF)
                if (commandType == 0xFF){
                    writer.println("End of file reached.Stopping command Processing");
                    System.out.println("End of file reached. Closing connection");
                    break;
                }

                // get the client's current screen setup state
                boolean isClientSetup = clientStates.get(socket);

                if (commandType == 0x1) {

                    if (length != 3){
                        writer.println("Error: Screen setup command requires exactly 3 bytes");
                        continue;
                    }
                    // Screen setup command
                    int width = Byte.toUnsignedInt(data[0]);
                    int height = Byte.toUnsignedInt(data[1]);
                    int colorMode = Byte.toUnsignedInt(data[2]);

                    // Initialize or reinitialize the screen if needed
                    if (screen == null) {
                        screen = TerminalScreen.getInstance(width, height, colorMode);
                    }
                    screen.setupScreen(width, height, colorMode);

                    // Update the client's state to "set up"
                    clientStates.put(socket, true);



                    System.out.println("Screen setup complete: " + screen.getWidth() + " x " +
                            screen.getHeight() + " Color mode: " + screen.getColorMode());

                    writer.println("Screen setup complete.");
                    continue;
                }

                // If the screen is not set up yet, return an error message
                if (screen == null || !isClientSetup) {
                    writer.println("Error: Screen is not set up. Please set up the screen first.");
                    System.out.println("Error: Screen not set up");
                    continue;
                }

                // Process other commands only if the screen is set up
                try {
                    parser.parseAndExecute(commandType, data, screen);
                    screen.renderScreen();
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
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Remove client state when the client disconnects
            clientStates.remove(socket);
        }
    }

    /**
     * The main method that starts the server in a separate thread.
     * It creates an instance of ScreenServer and runs it in a new thread.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create a new instance of ScreenServer and run it on a separate thread
        ScreenServer server = new ScreenServer();
        Thread serverThread = new Thread(server);
        serverThread.start();
    }
}
