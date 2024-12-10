package commands;

import iterface.Command;
import screen.TerminalScreen;

/**
 * The ScreenSetupCommand class implements the iterface.Command interface and is responsible for setting up the screen
 * by defining its dimensions and color mode. This command must be executed before any other commands
 * on the screen.TerminalScreen.
 */

public class ScreenSetupCommand implements Command {
    private int width;
    private int height;
    private int colorMode;

     /**
     * Constructor to initialize the ScreenSetupCommand with the given screen width, height, and color mode.
     *
     * @param width - The width of the screen (in characters).
     * @param height - The height of the screen (in characters).
     * @param colorMode - The color mode (0 for monochrome, 1 for 16 colors, 2 for 256 colors).
     */

    public ScreenSetupCommand(int width, int height, int colorMode){
        this.width = width;
        this.height = height;
        this.colorMode = colorMode;
    }

    /**
     * Executes the screen setup command by setting the screen dimensions and color mode
     * on the provided screen.TerminalScreen.
     *
     * @param screen - The screen.TerminalScreen instance on which the screen setup will be performed.
     */

    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        screen.setupScreen(width, height, colorMode);
        System.out.println("screen setup complete: " + screen.getWidth() + "x" +  screen.getHeight() + ", Color Mode: "+ screen.getColorMode());
    }
}
