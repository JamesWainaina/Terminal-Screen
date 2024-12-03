package commands;

import screen.TerminalScreen;

/**
 * The DrawCharacterCommand class implements the commands.Command interface and is responsible for drawing a
 * specific character at a given (x, y) coordinate on the screen.TerminalScreen.
 */

public class DrawCharacterCommand implements Command {
   private int x;
   private int y;
   private char character;
   private int colorIndex;

   /**
     * Constructor to initialize the DrawCharacterCommand with the given position, character, and color index.
     *
     * @param x - The x-coordinate where the character will be drawn.
     * @param y - The y-coordinate where the character will be drawn.
     * @param character - The ASCII character to be drawn on the screen.
     * @param colorIndex - The color index (can be used for future color implementation).
     */
   public DrawCharacterCommand(int x, int y, char character, int colorIndex){
       this.x = x;
       this.y = y;
       this.character = character;
       this.colorIndex = colorIndex;
   }

   /**
     * Executes the draw character command by drawing the specified character at the given coordinates
     * on the provided screen.TerminalScreen.
     *
     * @param screen - The screen.TerminalScreen instance where the character will be drawn.
     */

    @Override
    public void execute(TerminalScreen screen, byte[] data) {
        if (!screen.isSetup()){
            throw new IllegalArgumentException("Screen not set up yet. Please set up the screen first.");
        }
        // If the data provided is not of the expected length, throw an exception.
        if (data.length != 4) {
            throw new IllegalArgumentException("Invalid data length. Expected 4 bytes (x, y, character, colorIndex).");
        }

        // Extract the necessary values from the data array
        int x = data[0];
        int y = data[1];
        char character = (char) data[2];
        int colorIndex = data[3];

        screen.drawCharacter(x, y, character, colorIndex);

        System.out.println("Character '" + character + "' drawn at (" + x + ", " + y + ") with color index " + colorIndex);
    }
}
