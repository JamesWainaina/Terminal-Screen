package commands;


import iterface.Command;
import screen.TerminalScreen;

/**
 * The CursorMovementCommand class implements the iterface.Command interface and is responsible for
 * moving the cursor to a specified location on the terminal screen.
 */

public class CursorMovementCommand implements Command {
    private int x; // x-coordinate 
    private int y; // y-coordinate


    /**
     * Constructor for the CursorMovementCommand.
     *
     * @param x         - The x-coordinate to move the cursor to.
     * @param y         - The y-coordinate to move the cursor to.
     */

    public CursorMovementCommand(int x , int y){
        this.x = x;
        this.y = y;

    }

    /**
     * Executes the cursor movement command by invoking the moveCursor() method on the provided
     * screen.TerminalScreen instance.
     *
     * @param screen - The screen.TerminalScreen instance where the cursor will be moved.
     */

    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        // validate the length of the data
        if (data.length < 2) {
            throw new IllegalArgumentException("Invalid data: expected at least 2 bytes for x and y coordinates.");
        }

        // move the cursor to the specified position
        screen.moveCursor(x, y);
    }

}


