package client;

import validators.CommandValidator;
import validators.CommandValidatorImp;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
                        System.out.println("Invalid command format. Please use '0x3:x1, y1, x2, y2, colorIndex");
                    }
                }

                //handle render text command
                else if (isScreenSetup && input.startsWith("0x4")) {
                    if (commandValidator.isValidRenderTextCommand(input)){
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    }else{
                        System.out.println("Invalid command format.Please use 0x4:row, column , text, colorIndex");
                    }
                }

                // handle move cursor command
                else if (isScreenSetup && input.startsWith("0x5")) {
                    if (commandValidator.isValidMoveCursorCommand(input)){
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    }else {
                        System.out.println("Invalid command format.Please use 0x5:row, column");
                    }
                }

                // handle draw at cursor command
                else if (isScreenSetup && input.startsWith("0x6")) {
                    if (commandValidator.isValidDrawAtCursorCommand(input)){
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    }else {
                        System.out.println("Invalid command format. Please use 0x6:character, colorIndex");
                    }
                }

                // handle clear screen command
                else if (isScreenSetup && input.startsWith("0x7")){
                    if (commandValidator.isValidClearScreenCommand(input)){
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    } else {
                        System.out.println("Invalid command format.Please use 0x7");
                    }
                }

                // handle end of file command
                else if (isScreenSetup && input.startsWith("0xFF")) {
                    if (commandValidator.isValidEndOfFileCommand(input)){
                        byte[] commandBytes = convertToBytes(input);
                        output.write(commandBytes);
                        String response = reader.readLine();
                        System.out.println("Server response: " + response);
                    } else {
                        System.out.println("Invalid command format. Please use 0xFF");
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
            // Split the command into command type and parameters
            String[] parts = command.split(":");
            String commandHex = parts[0];
            // Handle the case where there might be no parameters for certain commands like 0x7
            String[] params = parts.length > 1 ? parts[1].split(",") : new String[0];

            // Convert the command type from hex to integer
            int commandType = Integer.parseInt(commandHex.substring(2), 16);

            // Handle ClearScreenCommand (0x7) - No parameters expected
            if (commandType == 7) {
                // If there are parameters, it's an invalid command
                if (params.length != 0) {
                    System.out.println("Error: ClearScreen command does not take any parameters.");
                    return new byte[0];
                }
                // Return command with no parameters
                return buildCommandBytes(commandType, new ArrayList<>());
            }

            // Handle DrawLineCommand (0x3) - Ensure it has exactly 5 parameters
            if (commandType == 3 && params.length != 5) {
                System.out.println("Error: DrawLine command must have exactly 5 parameters (x1, y1, x2, y2, colorIndex).");
                return new byte[0];
            }

            List<Byte> dataList = new ArrayList<>();
            for (String param : params) {
                param = param.trim();
                if (param.matches("[0-9]+")) {
                    dataList.add(parseNumber(param));
                } else if (param.matches("[A-Za-z\\s\\p{Punct}]+")) {
                    byte[] textBytes = parseText(param);
                    for (byte b : textBytes) {
                        dataList.add(b);
                    }
                } else {
                    System.out.println("Error: Invalid parameter format.");
                    return new byte[0];
                }
            }

            return buildCommandBytes(commandType, dataList);
        } catch (Exception e) {
            System.out.println("Error: Malformed command.");
            return new byte[0];
        }
    }

    // function to parse number
    private static byte parseNumber(String param) {
        int intValue = Integer.parseInt(param);
        if (intValue > 255) {
            System.out.println("Error: Parameter value out of byte range.");
            return 0;
        }
        return (byte) intValue;
    }

    // function to parse text
    private static byte[] parseText(String param) {
        return param.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] buildCommandBytes(int commandType, List<Byte> dataList) {
        byte[] data = new byte[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
        int length = data.length;
        byte[] commandBytes = new byte[2 + length];
        commandBytes[0] = (byte) commandType;
        commandBytes[1] = (byte) length;
        System.arraycopy(data, 0, commandBytes, 2, length);
        return commandBytes;
    }



    public static void main(String[] args) {
        CommandClient client = new CommandClient();
        client.run();
    }
}
