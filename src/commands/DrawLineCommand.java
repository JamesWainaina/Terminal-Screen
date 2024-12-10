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
    private char character; // character to draw the line with


    /**
     * Constructor to initialize the DrawLineCommand with the start and end coordinates, the character to draw,
     * and the color index.
     *
     * @param x1 - The starting x-coordinate of the line.
     * @param y1 - The starting y-coordinate of the line.
     * @param x2 - The ending x-coordinate of the line.
     * @param y2 - The ending y-coordinate of the line.
     * @param character - The ASCII character to be used for drawing the line.
     * @param colorIndex - The color index
     */
    
     public DrawLineCommand(int x1, int y1, int x2, int y2,int colorIndex, char character){
         this.x1 = x1;
         this.y1 = y1;
         this.x2 = x2;
         this.y2 = y2;
         this.colorIndex = colorIndex;
         this.character = character;
     }


    /**
     * Executes the draw line command by drawing a line from (x1, y1) to (x2, y2) on the provided
     * screen.TerminalScreen using Bresenham's line algorithm.
     *
     * @param screen - The screen.TerminalScreen instance where the line will be drawn.
     */

    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        // Extract values from the data byte array
        int x1 = Byte.toUnsignedInt(data[0]);
        int y1 = Byte.toUnsignedInt(data[1]);
        int x2 = Byte.toUnsignedInt(data[2]);
        int y2 = Byte.toUnsignedInt(data[3]);
        char character = (char) data[4];
        int colorIndex = Byte.toUnsignedInt(data[5]);

        if (x1 < 0 || x1 >= screen.getWidth() || x2 < 0 || x2 >= screen.getWidth() ||
                y1 < 0 || y1 >= screen.getHeight() || y2 < 0 || y2 >= screen.getHeight()) {
            throw new IllegalArgumentException("Coordinates out of bounds.");
        }
        if (character < 32 || character > 126) {
            throw new IllegalArgumentException("Invalid character. Only printable ASCII characters are allowed.");
        }

        if (colorIndex < 0 || colorIndex > 255) {
            throw new IllegalArgumentException("Invalid color index. It must be between 0 and 255.");
        }

        screen.drawLine(x1, y1, x2, y2, colorIndex, character);

        System.out.println("Line drawn from (" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ") with character '" + character + "' and color index " + colorIndex);
    }
}

