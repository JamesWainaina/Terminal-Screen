package commands;

import screen.TerminalScreen;

/**
 * The RenderTextCommand class implements the commands.Command interface and is responsible for
 * rendering a string of text at a given coordinate (x, y) on the terminal screen.
 */

public class RenderTextCommand implements Command {
    private int x; // x- coordinate where the text will start
    private int y; // y - coordinte where the text will start
    private String text; // the text to be rendered
    private int colorIndex; // the color index for future color implementations

    /**
     * Constructor for the RenderTextCommand.
     *
     * @param x - The x-coordinate where the text rendering will start.
     * @param y - The y-coordinate where the text rendering will start.
     * @param text - The string of text to be rendered on the screen.
     * @param colorIndex - The color index (this is used for color in the future).
     */
    public RenderTextCommand(int x, int y, String text, int colorIndex){
        this.x = x;
        this.y = y;
        this.text = text;
        this.colorIndex = colorIndex;
    }


    /**
     * Executes the render text command by invoking the renderText() method on the provided
     * screen.TerminalScreen instance.
     *
     * @param screen - The screen.TerminalScreen instance where the text will be rendered.
     */

    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        screen.renderText(x, y, colorIndex, text);
        System.out.println("Text '" + text + "' rendered at position (" + x + ", " + y + ") with color index " + colorIndex + ".");
    }
}



