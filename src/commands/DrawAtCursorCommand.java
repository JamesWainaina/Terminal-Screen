package commands;

import Command;
import TerminalScreen;

/**
 * Command to draw a character at the current cursor position on the terminal screen.
 * This command takes the character to draw and the color index as arguments.
 */

public class DrawAtCursorCommand impliments Command{
    private char character;
    private int colorIndex;
    
    /**
     * Constructor to initialize the DrawAtCursorCommand.
     *
     * @param character - The character to draw at the cursor's current position.
     * @param colorIndex - The color index for the character.
     */

    public DrawAtCursorCommand(char character, int colorIndex){
        this.character = character;
        this.colorIndex = colorIndex;
    }
        /**
     * Executes the draw at cursor command.
     * This method will use the TerminalScreen's drawAtCursor method to draw the character.
     *
     * @param terminalScreen - The TerminalScreen instance where the drawing takes place.
     */

    @Override
    public void execute(TerminalScreen terminalScreen){
        // check if the screen is set up before drawing
        if(!terminalScreen.isSetup()){
            throw new illegalStateExeption("Screen is not set up. Please set up the screen first.");
        }
        // draw the character at the cursors's current position
        terminalScreen.drawAtCursor(character,colorIndex);
   }
}

