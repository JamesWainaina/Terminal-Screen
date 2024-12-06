package client;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class CommandClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 8000;
    private static boolean isScreenSetup = false;

    public void run(){
        try (
                // Establish socket connection to the server
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output,true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
                ) {
            System.out.println("Connected to the server at " + SERVER_ADDRESS);

            boolean running = true;

            while (running) {
                System.out.println("Enter command: (eg., '0x2:10,5,65,2' or 'exit', to quit):");

                // Get User input
                String input = scanner.nextLine();

                //Exit the lop if the user types 'exit'
                if (input.equalsIgnoreCase("exit")) {
                    running = false;
                } else{
                    // validate the command format
                    if (isValidCommand(input)) {
                        writer.println(input);

                        // Read and print the server's response
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    } else  {
                        System.out.println("Invalid command format. Please follow the format '0x2:10,5,65,2'.");
                    }
                }
            }
            // close the socket and scanner
            socket.close();
            scanner.close();
            System.out.println("Goodbye.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to validate the command format
    private static boolean isValidCommand(String command){
        String regex = "^0x[0-9A-Fa-f]+(:[0-9]+(?:,[0-9]+)*)$";
        return command.matches(regex);
    }

    public static void main(String[] args) {
        CommandClient client = new CommandClient();
        client.run();
    }
}
