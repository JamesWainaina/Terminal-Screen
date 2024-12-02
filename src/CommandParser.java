import commands.*;

import java.util.HashMap;
import java.util.Map;

/**
 * The CommandParser class is responsible for interpreting the binary command input and invoking
 * the appropriate command. It uses a map to associate command bytes with specific command implementations.
 */

public class CommandParser{
    private Map<Integer, Command > commandMap;


    /**
     * Constructor that initializes the command map.
     */
    public CommandParser(){
        commandMap = new HashMap<>();
        // Register the commands with their respective command byte values
        commandMap.put(0x1, new ScreenSetupCommand());
        commandMap.put(0x2, new DrawCharacterCommand());
        commandMap.put(0x3, new DrawLineCommand());
        commandMap.put(0x4, new RenderTextCommand());
        commandMap.put(0x5, new CursorMovementCommand());
        commandMap.put(0x6, new DrawAtCursorCommand());
        commandMap.put(0x7, new CommandParser());
    }

      /**
     * Parse the command byte and execute the corresponding command.
     *
     * @param commandByte - The byte that specifies the command.
     * @param data - The associated data for the command (depending on the command type).
     * @param screen - The TerminalScreen instance where the command will be executed.
     *
     * @throws IllegalArgumentException if the command byte is not recognized.
     */
    public void parseAndExecute(int commandByte,int lengthByte, byte[] data, TerminalScreen screen){
        Command command = commandMap.get(commandByte);
        if(command == null){
            throw new IllegalArgumentException("Unknown command byte: " + commandByte);
        }
        // Excute the command
        command.execute(screen, data);
    }
}
