package commands;

import iterface.Command;
import screen.TerminalScreen;

public class EndOfFileCommand implements Command {
    @Override
    public void execute(TerminalScreen screen, byte[] data) {

        if (data.length > 0) {
            throw new IllegalArgumentException("Data should be empty for EndOfFileCommand.");
        }
        screen.endOfFile();
        System.out.println("End of file");
    }
}
