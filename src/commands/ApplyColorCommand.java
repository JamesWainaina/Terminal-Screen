package commands;

import iterface.Command;
import screen.TerminalScreen;

public class ApplyColorCommand implements Command {
    /**
     * Executes the apply color command on the given screen. It extracts color index from the data array
     * and applies it to the terminal screen.
     *
     * @param screen The TerminalScreen on which the command should operate.
     * @param data   The byte array containing the color index (and possibly additional data).
     */

    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        if (data.length > 0) {
            int colorIndex = data[0];
            screen.applyColor(colorIndex);
        } else {
            System.out.println("No color data provided");
        }
    }
}
