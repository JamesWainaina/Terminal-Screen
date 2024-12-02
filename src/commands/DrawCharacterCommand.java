package commands;

/**
 * The DrawCharacterCommand class implements the Command interface and is responsible for drawing a
 * specific character at a given (x, y) coordinate on the TerminalScreen.
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
     * on the provided TerminalScreen.
     *
     * @param screen - The TerminalScreen instance where the character will be drawn.
     */
   @Override
   public void execute(TerminalScreen screen){
       screen.drawCharacter(x, y, character, colorIndex);
        System.out.println("Character '" + character + "' drawn at (" + x + ", " + y + ") with color index " + colorIndex);
   }
}
