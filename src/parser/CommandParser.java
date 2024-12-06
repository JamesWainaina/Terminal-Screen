package parser;

import commands.*;
import iterface.Command;
import screen.TerminalScreen;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The CommandParser class is responsible for parsing command bytes and invoking the appropriate command.
 * It utilizes the Factory Pattern to dynamically create instances of different command classes
 * based on the provided command byte. It then executes the corresponding command on the terminal screen.
 *
 * This approach allows easy extensibility, maintainability, and clear separation of concerns between
 * the command execution logic and the parsing/command creation logic.
 */
public class CommandParser {
    // A map that associates command bytes with command factories
    private Map<Integer, Function<byte[], Command>> commandMap;

    /**
     * Constructor that initializes the command map with different command factories.
     * Each command byte is mapped to a corresponding factory that creates a command instance.
     */
    public CommandParser() {
        commandMap = new HashMap<>();

        // Mapping command byte to command factory methods
        commandMap.put(0x1, this::createScreenSetupCommand);
        commandMap.put(0x2, this::createDrawCharacterCommand);
        commandMap.put(0x3, this::createDrawLineCommand);
        commandMap.put(0x4, this::createRenderTextCommand);
        commandMap.put(0x5, this::createCursorMovementCommand);
        commandMap.put(0x6, this::createDrawAtCursorCommand);
        commandMap.put(0x7, this::createClearScreenCommand);
        commandMap.put(0xFF, this::createEndOfFileCommand);
    }

    /**
     * Parses the command byte and executes the corresponding command.
     * If the byte doesn't match a known command, an exception is thrown.
     *
     * @param commandByte - The byte representing the command type.
     * @param data - The data byte array containing command arguments.
     * @param screen - The TerminalScreen instance where the command will be executed.
     * @throws IllegalArgumentException if the command byte is not recognized.
     */
    public void parseAndExecute(int commandByte, byte[] data, TerminalScreen screen) {
        if (screen == null){
            throw new IllegalArgumentException("TerminalScreen is not set up");
        }
        // check if the screen is set up before executing any commands
        if (commandByte != 0x1 && !screen.isSetup()){
            throw new IllegalArgumentException("Error: Screen is not setup");
        }
        // Retrieve the command factory corresponding to the command byte
        Function<byte[], Command> commandFactory = commandMap.get(commandByte);

        // If the command byte is not valid, throw an exception
        if (commandFactory == null) {
            throw new IllegalArgumentException("Unknown command byte: " + commandByte);
        }

        // Create the command instance using the factory and execute it
        Command command = commandFactory.apply(data);
        command.execute(screen, data);
    }

    // Helper methods for creating command instances from data

    /**
     * Creates an instance of DrawCharacterCommand using the provided data array.
     *
     * @param data - The byte array containing the command arguments (x, y, character, colorIndex).
     * @return A new instance of DrawCharacterCommand.
     * @throws IllegalArgumentException if the data array is invalid.
     */
    private DrawCharacterCommand createDrawCharacterCommand(byte[] data) {
        if (data.length != 4) {
            throw new IllegalArgumentException("Data array must have exactly 4 elements for DrawCharacterCommand");
        }
        int x = data[0];
        int y = data[1];
        char character = (char) data[2];
        int colorIndex = data[3];

        return new DrawCharacterCommand(x, y, character, colorIndex);
    }

    /**
     * Creates an instance of DrawLineCommand using the provided data array.
     *
     * @param data - The byte array containing the command arguments (x1, y1, x2, y2, colorIndex).
     * @return A new instance of DrawLineCommand.
     * @throws IllegalArgumentException if the data array is invalid.
     */
    private DrawLineCommand createDrawLineCommand(byte[] data) {
        if (data.length != 6) {
            throw new IllegalArgumentException("Data array must have exactly 6 elements for DrawLineCommand");
        }
        int x1 = data[0];
        int y1 = data[1];
        int x2 = data[2];
        int y2 = data[3];
        int colorIndex = data[4];
        char character = (char) data[5];

        return new DrawLineCommand(x1, y1, x2, y2, colorIndex, character);
    }

    /**
     * Creates an instance of RenderTextCommand using the provided data array.
     *
     * @param data - The byte array containing the command arguments (x, y, text, colorIndex).
     * @return A new instance of RenderTextCommand.
     * @throws IllegalArgumentException if the data array is invalid.
     */
    private RenderTextCommand createRenderTextCommand(byte[] data) {
        if (data.length < 4) {
            throw new IllegalArgumentException("Data array must have at least 4 elements for RenderTextCommand");
        }
        int x = data[0];
        int y = data[1];
        int colorIndex = data[data.length - 1]; // Color index is the last byte

        // Convert the remaining bytes (from index 2 to the second-last byte) into a string
        String text = new String(data, 2, data.length - 3);
        return new RenderTextCommand(x, y, text, colorIndex);
    }

    /**
     * Creates an instance of CursorMovementCommand using the provided data array.
     *
     * @param data - The byte array containing the command arguments (x, y).
     * @return A new instance of CursorMovementCommand.
     * @throws IllegalArgumentException if the data array is invalid.
     */
    private CursorMovementCommand createCursorMovementCommand(byte[] data) {
        if (data.length != 2) {
            throw new IllegalArgumentException("Data array must have exactly 2 elements for CursorMovementCommand");
        }
        int x = data[0];
        int y = data[1];

        return new CursorMovementCommand(x, y);
    }

    /**
     * Creates an instance of DrawAtCursorCommand using the provided data array.
     *
     * @param data - The byte array containing the command arguments (character, colorIndex).
     * @return A new instance of DrawAtCursorCommand.
     * @throws IllegalArgumentException if the data array is invalid.
     */
    private DrawAtCursorCommand createDrawAtCursorCommand(byte[] data) {
        if (data.length != 2) {
            throw new IllegalArgumentException("Data array must have exactly 2 elements for DrawAtCursorCommand");
        }
        char character = (char) data[0];
        int colorIndex = data[1];

        return new DrawAtCursorCommand(character, colorIndex);
    }

    /**
     * Creates an instance of ScreenSetupCommand using the provided data array.
     *
     * @param data - The byte array containing the command arguments (width, height, colorMode).
     * @return A new instance of ScreenSetupCommand.
     * @throws IllegalArgumentException if the data array is invalid.
     */
    private ScreenSetupCommand createScreenSetupCommand(byte[] data){
        if (data.length != 3){
            throw new IllegalArgumentException("Data array must have exactly 3 elements  for ScreenSetupCommand");
        }

        int width = data[0];
        int height = data[1];
        int colorMode = data[2];

        return new ScreenSetupCommand(width, height, colorMode);
    }


    private ClearScreenCommand createClearScreenCommand(byte[] data) {
        if (data.length != 3){
            throw new IllegalArgumentException("Data array should have exactly 3 elements for clearing the screen");
        }

        return  new ClearScreenCommand();
    }

    private EndOfFileCommand createEndOfFileCommand(byte[] data) {
        if (data.length != 3) {
            throw new IllegalArgumentException("Data array must have exactly 3 elements for EndOfFileCommand");
        }
        return  new EndOfFileCommand();
    }
}
