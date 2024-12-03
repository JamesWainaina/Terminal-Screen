package commands;

import screen.TerminalScreen;

/**
 * commands.Command to draw a character at the current cursor position on the terminal screen.
 * This command takes the character to draw and the color index as arguments.
 */

public class DrawAtCursorCommand implements Command{
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
     * This method will use the screen.TerminalScreen's drawAtCursor method to draw the character
     * at the current cursor position.
     *
     * @param terminalScreen - The TerminalScreen instance where the drawing takes place.
     */

    @Override
    public void execute(TerminalScreen terminalScreen, byte[] data) {
        // check if the screen is set up before drawing
        if (!terminalScreen.isSetup()){
            throw new IllegalArgumentException("Screen is not set ip.Please set up the screen first.");
        }

        // the data array can be used to pass character and colorIndex
        // Assuming the first byte is the color index and the second byte is the character.
        char character = (char) data[0];
        int colorIndex = data[1];

        // draw the character at the cursor's current position
        terminalScreen.drawAtCursor(character, colorIndex);
    }
}

