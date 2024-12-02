package commands;


/**
 * The ClearScreenCommand class implements the Command interface and is responsible for
 * clearing the terminal screen, setting all characters in the buffer to a blank space (' ').
 */

public class ClearScreenCommand implements Command {

    /**
     * Executes the clear screen command by invoking the clearScreen() method on the provided
     * TerminalScreen instance.
     *
     * @param screen - The TerminalScreen instance to be cleared.
     */

    @Override
    public void execute(TerminalScreen screen){
        screen.clearScreen();
        System.out.println("Screen cleared.");
    }
}
