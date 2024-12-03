package commands;

import screen.TerminalScreen;

/**
 * The commands.Command interface represents a generic command that can be executed
 * on a screen.TerminalScreen. All specific commands such as drawing characters,
 * rendering text, or clearing the screen will implement this interface.
 */

public interface Command{
      /**
       * Executes the command on the given screen.TerminalScreen.
       *
       * @param screen The screen.TerminalScreen on which the command should operate.
       * @param data
       */
    void execute(TerminalScreen screen, byte[] data);
}

