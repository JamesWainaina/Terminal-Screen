package commands;

import screen.TerminalScreen;

/**
 * The ClearScreenCommand class implements the Command interface and is responsible for clearing
 * the terminal screen. It resets the entire screen buffer to a default state, effectively clearing
 * the terminal's display.
 * This command does not require any additional data and simply triggers the screen's clear functionality.
 */
public class ClearScreenCommand implements Command {

    /**
     * Executes the clear screen command, which resets all characters in the screen buffer to a space (' ').
     * This clears the entire terminal screen and is typically used when the screen needs to be reset to a clean state.
     *
     * @param screen - The TerminalScreen instance where the screen will be cleared.
     * @param data   - The byte array associated with the clear screen command. Since this command does not
     *                require any data, the array will be ignored.
     */
    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        // Call the clearScreen method of TerminalScreen to reset the screen buffer
        screen.clearScreen();
    }
}
