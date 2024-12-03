package server;


import parser.CommandParser;
import screen.TerminalScreen;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ScreenServer implements Runnable{
    private int port = 8000;
    private TerminalScreen screen;
    private CommandParser parser;

    /**
     * Constructor to initialize the ScreenServer.
     * Initializes the TerminalScreen with dimensions (80x24) and monochrome color mode (0),
     * and creates an instance of CommandParser.
     */
    public ScreenServer(){
        this.screen = new TerminalScreen(80, 24, 0);
        this.parser = new CommandParser();
    }

    /**
     * The entry point for the server. This method will be run by a separate thread.
     * It starts a server socket to listen for incoming client connections.
     */
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Screen server is running. Waiting for client connections...");

            // The server keeps running and accepting client connections
            while (true){
                // Accept incoming client connections
                Socket socket = serverSocket.accept();
                System.out.println("Accepted connection from " + socket.getRemoteSocketAddress());

                // handle the client connection in a separate method
                handleClientConnection(socket);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method handles communication with a single client.
     * It reads commands from the client, processes them, and updates the screen accordingly.
     *
     * @param socket The socket representing the client connection
     */

    private  void  handleClientConnection(Socket socket){
        try (
            // Set up input and output streams for reading and writing to the client
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
        ) {
            String command;
            // continously read commands from the client until the connection is closed
            while ((command = reader.readLine()) != null){
                System.out.println("Received command: " + command);

                // split the received command into parts(command byte and data)
                String[] parts = command.split(":");
                int commandType = Integer.parseInt(parts[0], 16);
                String[] dataParts = parts[1].split(",");
                byte[] data = new byte[dataParts.length];

                // convert the comma-seperated data into a byte array
                for (int i = 0; i < dataParts.length; i++){
                    data[i] =Byte.parseByte(dataParts[i]);
                }

                // Parse and execute the command using the CommandParser
                parser.parseAndExecute(commandType, data, screen);

                // render the updated screen command using the commandParser
                screen.renderScreen();

                // send a response to the client indicating that the command was processed
                writer.println("Command processed and screen updated.");
            }
        } catch (IOException e){
            e.printStackTrace();

        }finally {
            try {
                // close the connection after the client disconnects or error occurs
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
        // create a new instance of screenServer and start it in a separate thread
        ScreenServer server = new ScreenServer();
        Thread serverThread = new Thread(server);
        serverThread.start();

    }
}

