package commands;


/**
 * The CursorMovementCommand class implements the Command interface and is responsible for
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
     * TerminalScreen instance.
     *
     * @param screen - The TerminalScreen instance where the cursor will be moved.
     */
    @Override
    public void execute(TerminalScreen screen){
        screen.moveCursor(x, y);
        System.out.println("cursor moved to position (" + x + "," + y + ").");
    }
}


