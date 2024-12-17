package validators;

public interface CommandValidator {

    // Method to validate setup command
    boolean isValidSetupCommand(String command);

    // Method to validate drawCharacter command
    boolean isValidDrawCharacterCommand(String command);

    // Method for drawLine command
    boolean isValidDrawLineCommand(String command);

    // Method for renderText command
    boolean isValidRenderTextCommand(String command);
}
