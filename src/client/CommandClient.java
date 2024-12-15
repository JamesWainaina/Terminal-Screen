package client;

import validators.CommandValidator;
import validators.CommandValidatorImp;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class CommandClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int PORT = 8000;
    private static boolean isScreenSetup = false;

    private CommandValidator commandValidator;

    public CommandClient() {
        this.commandValidator = new CommandValidatorImp();
    }
    public void run() {
        try (
                Socket socket = new Socket(SERVER_ADDRESS, PORT);
                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server at " + SERVER_ADDRESS);

            boolean running = true;

            while (running) {
                System.out.println("Enter command: (e.g., '0x1:60,20,0' for setup, '0x2:10,15,A,0' for drawCharacter, or 'exit' to quit):");
                String input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    writer.println("exit");
                    running = false;
                    continue;
                }

                // Handle screen setup command
                if (!isScreenSetup && input.startsWith("0x1")) {
                    if (commandValidator.isValidSetupCommand(input)) {
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);

                        String response = reader.readLine();
                        System.out.println("Server response: " + response);

                        if (response.contains("Screen setup complete")) {
                            isScreenSetup = true;
                            System.out.println("Screen setup complete.");
                        } else {
                            System.out.println("Error: Screen setup failed.");
                        }
                    } else {
                        System.out.println("Invalid command format. Please use '0x1:width,height,colorMode'.");
                    }
                }
                // Handle drawCharacter command after screen setup
                else if (isScreenSetup && input.startsWith("0x2")) {
                    if (commandValidator.isValidDrawCharacterCommand(input)) {
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);

                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    } else {
                        System.out.println("Invalid command format. Please use '0x2:row,column,character,colorIndex'.");
                    }
                }

                // handle drawLine command
                else if (isScreenSetup && input.startsWith("0x3")) {
                    if (commandValidator.isValidDrawLineCommand(input)){
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    } else {
                        System.out.println("Invalid command format. Please use '0x3:x1,y1,x2,y2,colorIndex for a dashed line.");
                    }
                }

                // If the screen is not set up, prevent other commands
                else {
                    System.out.println("Error: Screen is not set up. Please send the screen setup command first.");
                }
            }

            System.out.println("Goodbye.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Convert the command string to the correct byte format
    private static byte[] convertToBytes(String command) {
        try {
            String[] parts = command.split(":");
            String commandHex = parts[0];
            String[] params = parts[1].split(",");

            // Remove the "0x" prefix from the command type (if it exists) and parse as a hexadecimal number
            int commandType = Integer.parseInt(commandHex.substring(2), 16);

            // Prepare the byte data for parameters
            byte[] data = new byte[params.length];
            for (int i = 0; i < params.length; i++) {
                String param = params[i].trim();
                if (param.matches("[0-9]+")) { // if it's a number
                    int intValue = Integer.parseInt(param);
                    if (intValue > 255) {
                        System.out.println("Error: Parameter value out of byte range.");
                        return new byte[0];
                    }
                    data[i] = (byte) intValue;
                } else if (param.matches("[A-Za-z]")) { // if it's a character
                    data[i] = (byte) param.charAt(0);
                } else {
                    System.out.println("Error: Invalid parameter format.");
                    return new byte[0];
                }
            }

            // The length byte is the number of parameters
            int length = data.length;

            // Create a byte array with the structure: [commandType, length, data...]
            byte[] commandBytes = new byte[2 + length];
            commandBytes[0] = (byte) commandType;  // Command type byte
            commandBytes[1] = (byte) length;       // Length byte
            System.arraycopy(data, 0, commandBytes, 2, length);  // Data bytes

            return commandBytes;

        } catch (Exception e) {
            System.out.println("Error: Malformed command.");
            return new byte[0];
        }
    }


    public static void main(String[] args) {
        CommandClient client = new CommandClient();
        client.run();
    }
}
