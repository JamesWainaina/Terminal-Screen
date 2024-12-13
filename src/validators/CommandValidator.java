package validators;

public interface CommandValidator {

    // Method to validate setup command
    boolean isValidSetupCommand(String command);

    // Method to validate drawCharacter command
    boolean isValidDrawCharacterCommand(String command);

    // Method to validate any other generic command
    boolean isValidCommand(String command);
}
