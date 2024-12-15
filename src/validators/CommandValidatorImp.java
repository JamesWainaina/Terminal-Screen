package validators;


public class CommandValidatorImp implements CommandValidator {

    @Override
    public boolean isValidSetupCommand(String command) {
        // Validate setup command format (e.g., '0x1:60,20,0')
        String regex = "^0x1:[0-9]+,[0-9]+,[0-9]+$";
        return command.matches(regex);
    }

    @Override
    public boolean isValidDrawCharacterCommand(String command) {
        // Validate drawCharacter command format (e.g., '0x2:row,column,character,colorIndex')
        String regex = "^0x2:[0-9]+,[0-9]+,[A-Za-z],[0-9]+$";
        return command.matches(regex);
    }


    @Override
    public boolean isValidDrawLineCommand(String command) {
        String regex = "^0x3:[0-9]+,[0-9]+,[0-9]+,[0-9]+,[0-9]+$";
        return command.matches(regex);
    }

}
