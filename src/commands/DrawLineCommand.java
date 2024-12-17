package commands;
import iterface.Command;
import screen.TerminalScreen;

/**
 * The DrawLineCommand class implements the iterface.Command interface and is responsible for drawing a
 * line from one coordinate to another using a specific character on the screen.TerminalScreen.
 */

public class DrawLineCommand implements Command {
    private int x1, y1; // starting point of the line
    private int x2, y2;// ending point of the line
    private int colorIndex; // Color index


    /**
     * Constructor to initialize the DrawLineCommand with the start and end coordinates, and the color index.
     *
     * @param x1 - The starting x-coordinate of the line.
     * @param y1 - The starting y-coordinate of the line.
     * @param x2 - The ending x-coordinate of the line.
     * @param y2 - The ending y-coordinate of the line.
     * @param colorIndex - The color index
     */

    public DrawLineCommand(int x1, int y1, int x2, int y2, int colorIndex) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.colorIndex = colorIndex;
    }

    /**
     * Executes the draw line command by drawing a line from (x1, y1) to (x2, y2) on the provided
     * screen.TerminalScreen using Bresenham's line algorithm.
     *
     * @param screen - The screen.TerminalScreen instance where the line will be drawn.
     */

    @Override
    public void execute(TerminalScreen screen, byte[] data) {

        if (x1 < 0 || x1 >= screen.getWidth() || x2 < 0 || x2 >= screen.getWidth() ||
                y1 < 0 || y1 >= screen.getHeight() || y2 < 0 || y2 >= screen.getHeight()) {
            throw new IllegalArgumentException("Coordinates out of bounds.");
        }

        if (colorIndex < 0 || colorIndex > 255) {
            throw new IllegalArgumentException("Invalid color index. It must be between 0 and 255.");
        }

        // Call the drawLine method of the screen without a specific character
        screen.drawLine(x1, y1, x2, y2, colorIndex);

        System.out.println("Line drawn from (" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ") with color index " + colorIndex);
    }
}
