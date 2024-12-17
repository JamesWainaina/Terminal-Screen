package commands;

import iterface.Command;
import screen.TerminalScreen;

public class EndOfFileCommand implements Command {
    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        // call the screen for end of file implementation
        screen.endOfFile();
        System.out.println("End of file");
    }
}
