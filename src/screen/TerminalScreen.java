package screen;

/**
 * screen.TerminalScreen class represents a screen in memory that can be manipulated with various commands.
 * The screen is modeled as a 2d array of characters, and the class provided methods to setup,
 * clear, and draw characters and text, among other operations. 
 * The screen also supports cursor movement and line drawing.
 */

public class TerminalScreen {
    // Instance variables to hold the screen dimensions, color mode, and screen buffer
    private int width;
    private int height;
    private int colorMode;  // The color mode (0: monochrome, 1: 16 colors, 2: 256 colors)
    private char[][] screenBuffer; //hold the terminal's current display
    private int[][] colorBuffer; // this buffer stores the color index for each character
    private int cursorX;
    private int cursorY;
    private boolean isSetup;
    private static TerminalScreen instance;

     /**
     * Constructor for initializing the screen.TerminalScreen object with given dimensions and color mode.
     * Initially, the screen is not set up until setupScreen() is called.
     *
     * @param width - The width of created the screen (in characters).
     * @param height - The height of the screen (in characters).
     * @param colorMode - The color mode of the screen (0: monochrome, 1: 16 colors, 2: 256 colors).
     */

     private TerminalScreen(int width, int height, int colorMode){
         this.width = width;
         this.height = height;
         this.colorMode = colorMode;
         this.screenBuffer = new char[height][width];
         this.colorBuffer = new int[height][width];
         this.cursorX = 0; // top-left position
         this.cursorY = 0;
         this.isSetup = false;
     }

     // ensures only one instance of the TerminalScreen class
    // exist during the lifetime of the program.
     public static TerminalScreen getInstance(int width, int height, int colorMode){
         if (instance == null){
             synchronized (TerminalScreen.class){
                 if (instance == null){
                     instance = new TerminalScreen(width, height, colorMode);
                 }
             }
         }
         return instance;
     }

     // overload getInstance without parameters for subsequent calls
    // Return the already created instance of the screen
    public  static  TerminalScreen getInstance(){
        if (instance == null){
            throw new IllegalStateException("Screen has not been initialized yet. Call getInstance(int, int, int) first.\"");
        }
        return instance;
    }


      /**
     * Set up the screen by initializing the buffer and setting the dimensions and color mode.
     * This must be called before performing any operations on the screen.
     *
     * @param width - The width of the screen (in characters).
     * @param height - The height of the screen (in characters).
     * @param colorMode - The color mode (0 for monochrome, 1 for 16 colors, 2 for 256 colors).
     */

     public void setupScreen(int width, int height, int colorMode){
         this.colorMode = colorMode;
         this.screenBuffer = new char[height][width];
         this.colorBuffer = new int[height][width];
         this.cursorX = 0;
         this.cursorY = 0;
         this.isSetup = true;
         this.height = height;
         this.width = width;

         // Initialize screenBuffer and colorBuffer
         for (int i = 0; i < height; i++){
             for (int j = 0; j < width; j++){
                 if (i == 0 || i == height - 1){
                     screenBuffer[i][j] = '-';
                 } else if (j == 0 || j == width - 1){
                     screenBuffer[i][j] = '|';
                 }else {
                     screenBuffer[i][j] = ' ';
                     colorBuffer[i][j] = 0;
                 }
             }
         }
     }

     /**
     * Check if the screen has been set up correctly.
     *
     * @return true if the screen has been set up, false otherwise.
     */

     public boolean isSetup(){
         return this.isSetup;
     }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Gets the current color mode of the screen.
     *
     * @return the current color mode (0: monochrome, 1: 16 colors, 2: 256 colors).
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */
    public int getColorMode(){
        return colorMode;
    }


    /**
     * Clear the screen by resetting all characters in the screen buffer to space (' ').
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */

     public void clearScreen(){
         if (!isSetup){
             throw new IllegalStateException("Screen not set up yet.Please set up the screen first.");
         }
         // fill the screen buffer with spaces
         for (int i = 0; i < height; i++){
             for (int j = 0; j < width; j++){
                 screenBuffer[i][j] = ' ';
                 colorBuffer[i][j] = 0;
             }
         }
     }
    
     /**
     * Draw a character at a specific coordinate (x, y) on the screen.
     * 
     * @param x - The x-coordinate (horizontal position).
     * @param y - The y-coordinate (vertical position).
     * @param c - The character to draw.
     * @param colorIndex - The color index.
     * 
     * @throws IllegalStateException if the screen has not been set up yet.
     */

     public void drawCharacter(int x, int y, char c, int colorIndex){
         if (!isSetup){
             throw new IllegalStateException("Screen not set up yet.Please set up the screen first.");
         }

         // ensure coordinates are within bounds
         if (x >= 0 && x < width && y >= 0 && y < height){
             // place the character at the screen at the given position

             screenBuffer[y][x] = c;
             System.out.println("screenBuffer" + screenBuffer[y][x]);
             colorBuffer[y][x] = colorIndex;
             System.out.println("color buffer " + colorBuffer[y][x]);

         } else{
             System.out.println("Invalid coordinates: (" + x + "," + y + ")");
         }
     }


      /**
     * Render the screen by printing the current state of the screen buffer to the console.
     * Each row of the screen buffer is printed line by line.
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */

     public void renderScreen(){
         if (!isSetup){
             throw new IllegalStateException("Screen not set up yet. Please set up the screen first.");
         }


         // print each row of the screen buffer
         for (int i = 0; i < height; i++){
             StringBuilder line = new StringBuilder();
             for (int j = 0; j < width; j++){
                 int colorIndex = colorBuffer[i][j];
                 line.append(screenBuffer[i][j]);
                 applyColor(colorIndex);
                 System.out.print(screenBuffer[i][j]);
             }
             resetColor();
             System.out.println();
         }
     }

    /**
     * Apply the color to the terminal based on the color index.
     * This method simulates color application on the terminal.
     *
     * @param colorIndex - The color index to apply.
     */
    public void applyColor(int colorIndex){
        switch (colorIndex){
            case 1:
                System.out.print("\033[31m");  //red color
                break;
            case 2:
                System.out.print("\033[32m"); // Green color
                break;
            case 3:
                System.out.print("\033[33m"); //Yellow color
                break;
            case 4:
                System.out.print("\033[34m"); // Blue color
                break;
            case 5:
                System.out.print("\033[35m"); // Magenta color
                break;
            case 6:
                System.out.print("\033[36m"); // cyan color
                break;
            case 7:
                System.out.print("\033[37m"); // white color
                break;
            case 8:
                System.out.print("\033[90m"); // bright black(gray)
                break;
            case 9:
                System.out.print("\033[91m"); // bright red
                break;
            case 10:
                System.out.print("\033[92m"); // bright green
                break;
            default:
                System.out.print("\033[0m"); // Reset to default color
                break;
        }
    }

    /**
     * Reset the terminal color back to the default color.
     */
    private void resetColor() {
        System.out.print("\033[0m"); // Reset color (ANSI escape code)
    }


    /**
     * Move the cursor to a specific coordinate (x, y) on the screen.
     * This action does not draw anything but simply moves the cursor position.
     *
     * @param x - The x-coordinate (horizontal position).
     * @param y - The y-coordinate (vertical position).
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */

     public void moveCursor(int x, int y){
         if (!isSetup){
             throw new IllegalStateException("Screen not set up yet. Please set up the screen first.");
         }

         // Ensure the cursor is within bounds
         if (x >= 0 && x < width && y >= 0 && y < height) {
                this.cursorX = x;
                this.cursorY = y;
         }else{
             System.out.println("Invalid cursor coordinates: (" + x + ", " + y + ")");
         }
     }

     /**
     * Draw a character at the current cursor position.
     * This function allows for drawing directly where the cursor is located.
     *
     * @param c - The character to draw.
     * @param colorIndex - The color index
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */
       
     public void drawAtCursor(char c, int colorIndex){
         if (!isSetup){
             throw new IllegalStateException("Screen not set up yet.Please set up the screen first.");
         }

         // draw the character at the cursor's current position
         drawAtCursor(c, colorIndex);
     }

     /**
     * Render a string of text starting from a specific position on the screen.
     *
     * @param x - The starting x-coordinate (horizontal position).
     * @param y - The starting y-coordinate (vertical position).
     * @param colorIndex - The color index
     * @param text - The text string to render on the screen.
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */

     public void renderText(int x, int y, int colorIndex, String text){
         if (!isSetup){
             throw new IllegalStateException("Screen not set up yet. Please set up the screen first.");
         }

         //render each character in the string starting from the given position
         if (y < 0 || y >= height){
             System.out.println("Invalid starting y-coordinate for rendering text.");
             return;
         }

         // check if the x-coordinate is within the valid range
         if(x < 0 || x >= width){
             System.out.println("Invalid starting x-coordinate for rendering text.");
             return;
         }
         for (int i = 0; i < text.length(); i++){
             if (x + i < width){
                 drawCharacter(x + i, y, text.charAt(i), colorIndex);
             }
         }
     }


     /**
     * Draw a line from one point (x1, y1) to another point (x2, y2).
     * The line is drawn using a specific character and color index.
     *
     * @param x1 - Starting x-coordinate.
     * @param y1 - Starting y-coordinate.
     * @param x2 - Ending x-coordinate.
     * @param y2 - Ending y-coordinate.
     * @param colorIndex - The color index
     * @param character - The character to use for drawing the line.
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */

     public void drawLine(int x1, int y1, int x2, int y2, int colorIndex, char character){
         if (!isSetup){
             throw new IllegalStateException("Screen not set up yet.Please set up the screen first.");
         }

         // Bresenham's line algorithm
         int dx = Math.abs(x2 - x1); // direction in the x direction
         int dy = Math.abs(y2 - y1); // direction in the y direction
         int sx = (x1 < x2) ? 1 : -1;// error handling
         int sy = (y1 < y2) ? 1 : -1; // error handling
         int err = dx - dy;

         while (true){
             drawCharacter(x1, y1, character, colorIndex);
             if (x1 == x2  && y1 == y2) break;
             int e2 = err * 2;
             if (e2 > -dy) {
                 err -= dy;
                 x1 += sx;
             }
             if (e2 < dx) {err += dx; y1 += sy;}
         }
     }


    /**
     * This method simulates the end-of-file (EOF) behavior in the terminal.
     * It indicates that no more commands will be processed.
     */
     public void endOfFile() {
         System.out.println("End of file reached.Stopping command process.");
     }

    /**
     * Sets the color mode for the screen.
     *
     * @param colorMode - The color mode to set (0: monochrome, 1: 16 colors, 2: 256 colors).
     *
     * @throws IllegalStateException if the screen has not been set up yet.
     */
    public void setColorMode(int colorMode){
        if (!isSetup){
            throw new IllegalStateException("Screen not set up yet. Please set up the screen first.");
        }

        this.colorMode = colorMode;
    }
}

