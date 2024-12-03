package client;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class CommandClient {
    public static void main(String[] args){
        try {
            // server information
            String serverAddress = "localhost";
            int port = 8000;

            // Establish socket connection to the server
            Socket socket  = new Socket(serverAddress, port);
            System.out.println("Connected to the server." + serverAddress);

            //Output stream for sending data
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            // input stream for receiving response from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Scanner for user input
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            // main loop for accepting commands
            while(running){
                // Print prompt and wait for user input
                System.out.println("Enter command (e.g., '0x2:10,5,65,2' or 'exit' to quit):");

                // Get User input
                String input = scanner.nextLine();

                // Exit the loop if the user types 'exit'
                if (input.equalsIgnoreCase("exit")){
                    running = false;
                } else {
                    // validate the command format
                    if (isValidCommand(input)){
                        writer.println(input);

                        // Read and print the server's response
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    } else {
                        System.out.println("Invalid command format. Please follow the format '0x2:10,5,65,2'.");
                    }

                }
            }

            // close the socket and scanner
            socket.close();
            scanner.close();
            System.out.println("Goodbye.");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // Helper method to validate the command format
    private static boolean isValidCommand(String command){
        String regex = "^0x[0-9A-Fa-f]+(:[0-9]+(?:,[0-9]+)*)$";
        return command.matches(regex);
    }
}
