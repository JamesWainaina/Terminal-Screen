import java.util.Scanner;

/**
 *  The TerminalApp class is the entry point of the application. It sets up the terminal screen,
 * parses user input or commands, and triggers the execution of various terminal operations.
 */


public class TerminalApp {
    private static TerminalScreen screen;
    private static CommandParser parser;
    

    public static void main(String[] args){
        screen = new TerminalScreen(80, 25, 1); //default screen with color mode 1
        parser = new CommandParser();

        // seting up the screen
        screen.setupScreen(80, 25, 1);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("terminal App started. Type your commnads.");
        // main loop for command input
        while (running){
            System.out.print("enter command byte (1 byte); ");
            int commandByte = scanner.nextInt();
            scanner.nextLine(); // consume the newLine

            System.out.print("Enter length byte (1 byte):");
            int lengthByte = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter commanf data (comma-seperated values): ");
            String dataInput = scanner.nextLine();
            byte[] data = processDataInput(dataInput, lengthByte);

            // End of file command
            if  (commandByte == 0xFF) {
                System.out.println("Exiting...");
                running = false;
            } else {
                try{
                    parser.parseAndExecute(commandByte, lengthByte, data, screen);
                    screen.renderScreen();
                }catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        scanner.close();
    }

     /**
     * Converts a comma-separated string into a byte array for command data.
     * This method ensures that the correct number of data bytes are created based on the length byte.
     *
     * @param dataInput - The input string containing comma-separated values.
     * @param length - The length of the data (given by the length byte).
     * @return byte[] - The byte array representation of the input data.
     */
    private static byte[] processDataInput(String dataInput, int length){
        String[] tokens = dataInput.split(",");
        byte[] data = new byte[length];

        for (int i = 0; i < length; i++){
            if (i < tokens.length){
                data[i] = Byte.parseByte(tokens[i].trim());
            }else {
                data[i] = 0;
            }
        }
        return data;
    }
}

