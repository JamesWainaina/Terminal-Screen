package commands;


import screen.TerminalScreen;

/**
 * The CursorMovementCommand class implements the commands.Command interface and is responsible for
 * moving the cursor to a specified location on the terminal screen.
 */

public class CursorMovementCommand implements Command {
    private int x; // x-coordinate 
    private int y; // y-coordinate

    /**
     * Constructor for the CursorMovementCommand.
     *
     * @param x - The x-coordinate to move the cursor to.
     * @param y - The y-coordinate to move the cursor to.
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
        // The 'data' array is expected to contain the coordinated of the cursor
        // Extract coordinates from the data byte array

        // extract coordinates from the data byte array
        int x = data[0];
        int y = data[1];

        // move the cursor to the specified position
        screen.moveCursor(x, y);

    }
}


