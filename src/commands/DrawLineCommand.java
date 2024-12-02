package commands;

/**
 * The DrawLineCommand class implements the Command interface and is responsible for drawing a
 * line from one coordinate to another using a specific character on the TerminalScreen.
 */


public class DrawLineCommand implements Command {
    private int x1, y1; // starting point of the line
    private int x2, y2; // ending point of the line
    private char character; // character to draw the line with
    private int colorIndex;// Color index

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
    
     public DrawlineCommand(int x1, int y1, int x2, int y2, char character, int colorIndex){
         this.x1 = x1;
         this.y1 = y1;
         this.x2 = x2;
         this.y2 = y2;
         this.character = character;
         this.colorIndex = colorIndex;
     }


    /**
     * Executes the draw line command by drawing a line from (x1, y1) to (x2, y2) on the provided
     * TerminalScreen using Bresenham's line algorithm.
     *
     * @param screen - The TerminalScreen instance where the line will be drawn.
     */

     @Override
     public void execute(TerminalScreen screen){
         screen.drawline(x1, y1, x2, y2, colorIndex, character);
         System.out.println("Line drawn from (" + x1 + ", " + y1 + ") to (" + x2 + ", " + y2 + ") with character '" + character + "' and color index " + colorIndex);
     }
}

