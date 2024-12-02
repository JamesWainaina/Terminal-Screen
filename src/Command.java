/**
 * The Command interface represents a generic command that can be executed
 * on a TerminalScreen. All specific commands such as drawing characters,
 * rendering text, or clearing the screen will implement this interface.
 */

public interface Command{
      /**
       * Executes the command on the given TerminalScreen.
       *
       * @param screen The TerminalScreen on which the command should operate.
       * @param data
       */
    void execute(TerminalScreen screen, byte[] data);
}

