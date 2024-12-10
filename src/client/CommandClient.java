package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * CommandClient is responsible for connecting to the ScreenServer and sending commands.
 * It interacts with the server by sending commands like screen setup and other control commands.
 * The client only allows other commands to be executed once the screen is successfully set up.
 */
public class CommandClient {
    // Address of the server (localhost in this case)
    private static final String SERVER_ADDRESS = "localhost";

    // Port number where the server is listening for connections
    private static final int PORT = 8000;

    // Flag to track if the screen has been successfully set up
    private static boolean isScreenSetup = false;

    /**
     * The run method establishes a connection to the server, sends commands,
     * and processes server responses. It continuously reads user input, validates it,
     * and sends the input to the server. If the screen is not set up, only the screen setup
     * command is allowed. Once the screen is set up, the client can send other commands.
     */
    public void run() {
        try (
                // Establish the socket connection to the server
                Socket socket = new Socket(SERVER_ADDRESS, PORT);

                // Set up streams to send data to and receive data from the server
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);  // Auto-flush enabled
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Scanner to get user input from the terminal
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server at " + SERVER_ADDRESS);

            // Flag to indicate if the client should keep running
            boolean running = true;

            // Main loop to continuously get user input and interact with the server
            while (running) {
                System.out.println("Enter command: (e.g., '0x1:60,20,0' for setup, or 'exit' to quit):");

                // Read user input
                String input = scanner.nextLine();

                // Check if the user wants to exit
                if (input.equalsIgnoreCase("exit")) {
                    running = false;
                } else {
                    // If the screen is not set up, only allow the screen setup command (0x1)
                    if (!isScreenSetup) {
                        if (input.startsWith("0x1")) {
                            // Check if the input command is a valid screen setup command
                            if (isValidCommand(input)) {
                                // Send the screen setup command to the server
                                writer.println(input);

                                // Read the server's response
                                String response = reader.readLine();
                                System.out.println("Server response: " + response);

                                // Check if the screen setup was successful
                                if (response.contains("Screen setup complete")) {
                                    isScreenSetup = true;  // Mark the screen as set up
                                    System.out.println("Screen setup status: " + isScreenSetup);
                                } else {
                                    System.out.println("Error: Screen setup failed.");
                                }
                            } else {
                                // Inform the user if the command format is incorrect
                                System.out.println("Invalid command format. Please use '0x1:width,height,colorMode'.");
                            }
                        } else {
                            // If the screen is not set up, reject any non-setup command
                            System.out.println("Error: Screen is not set up. Please send the screen setup command first.");
                        }
                    } else {
                        // If the screen is set up, allow any other command
                        if (isValidCommand(input)) {
                            // Send the command to the server
                            writer.println(input);

                            // Read the server's response
                            String response = reader.readLine();
                            System.out.println("Server response: " + response);
                        } else {
                            // Inform the user if the command format is invalid
                            System.out.println("Invalid command format. Please follow the format '0x2:10,5,65,2'.");
                        }
                    }
                }
            }

            // Clean up: close the socket and scanner after exiting the loop
            socket.close();
            scanner.close();
            System.out.println("Goodbye.");
        } catch (IOException e) {
            // Print the stack trace if an I/O error occurs
            e.printStackTrace();
        }
    }

    /**
     * Helper method to validate the format of the command.
     * It ensures that the command starts with '0x' followed by a hexadecimal number,
     * followed by a colon, and then a list of integers separated by commas.
     *
     * @param command The command string to validate
     * @return true if the command is valid, false otherwise
     */
    private static boolean isValidCommand(String command) {
        // Regular expression to match the valid command format:
        // Starts with '0x' followed by a hexadecimal number, then ':' and integers
        String regex = "^0x[0-9A-Fa-f]+(:[0-9]+(,[0-9]+)*|(,[A-Za-z]))*$";
        return command.matches(regex);
    }

    /**
     * The main method to start the client application.
     * It creates a CommandClient instance and runs it.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        // Create a new instance of the CommandClient and start the run loop
        CommandClient client = new CommandClient();
        client.run();
    }
}
