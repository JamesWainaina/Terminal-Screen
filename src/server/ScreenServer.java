package server;

import parser.CommandParser;
import screen.TerminalScreen;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The ScreenServer class is responsible for accepting client connections,
 * receiving commands from clients, and executing those commands on a TerminalScreen.
 * It uses a CommandParser to interpret and execute the received commands.
 */
public class ScreenServer implements Runnable {
    // Port number on which the server will listen for connections
    private int port = 8000;

    // The screen that will be controlled and updated by client commands
    private TerminalScreen screen;

    // The parser responsible for interpreting and executing commands
    private CommandParser parser;

    private boolean isSetup = false;

    /**
     * Constructor to initialize the ScreenServer.
     * It initializes the CommandParser and the screen as null (not yet set up).
     */
    public ScreenServer() {
        this.parser = new CommandParser();
        this.screen = null; // The screen will be initialized when the setup command is received
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

                // Handle the client connection (processing commands) in a separate method
                handleClientConnection(socket);
            }
        } catch (IOException e) {
            // Print the stack trace if an I/O error occurs while running the server
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
                BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true)
        ) {
            String command;
            // Reading the line from client and returns the line as a String
            while ((command = reader.readLine()) != null) {
                System.out.println("Received command: " + command);

                // split the data
                String[] parts = command.split(":");
                // convert the command into a hexadecimal value
                int commandType = Integer.parseInt(parts[0].replace("0x", ""), 16);
                // split the data into individual values
                String[] dataParts = parts[1].split(",");
                byte[] data = new byte[dataParts.length];

                for (int i = 0; i < dataParts.length; i++) {
                    data[i] = Byte.parseByte(dataParts[i].trim(), 16);
                }

                System.out.println("Parsed command: " + commandType);
                for (byte b : data) {
                    System.out.println(b + " ");
                }
                System.out.println();

                if (commandType == 0x1) {
                    int width = data[0];
                    int height = data[1];
                    int colorMode = data[2];


                    if (screen == null){
                        screen = TerminalScreen.getInstance(width, height, colorMode);
                    } else {
                        screen.setupScreen(width, height, colorMode);
                    }

                    isSetup = true;
                    System.out.println("Screen setup " + isSetup);


                    System.out.println("Screen setup complete: " + screen.getWidth() + " x " +
                            screen.getHeight() + " Color mode: " + screen.getColorMode());

                    writer.println("Screen setup complete.");
                    continue;
                }

                if (screen == null) {
                    writer.println("Error: Screen is not set up. Please set up the screen first.");
                    System.out.println("Error: Screen not set up");
                    continue;
                }

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
