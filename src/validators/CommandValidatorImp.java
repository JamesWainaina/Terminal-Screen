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
        // Validate drawLine command format (e.g., '0x3:x1,y1,x2,y2,colorIndex,character')
        String regex = "^0x3:[0-9]+,[0-9]+,[0-9]+,[0-9]+,[0-9]+,[-.\\/|\\\\]$";
        return command.matches(regex);
    }


    @Override
    public boolean isValidRenderTextCommand(String command) {
        String regex = "^0x4:[0-9]+,[0-9]+,[\\w\\s\\p{Punct}]+,[0-9]+$";
        return command.matches(regex);
    }

    @Override
    public boolean isValidMoveCursorCommand(String command) {
        String regex = "^0x5:[0-9]+,[0-9]+$";
        return command.matches(regex);
    }

    @Override
    public boolean isValidDrawAtCursorCommand(String command) {
        String regex = "^0x6:[A-Za-z],[0-9]+$";
        return command.matches(regex);
    }

    @Override
    public boolean isValidClearScreenCommand(String command) {
        String regex = "^0x7$";
        return command.matches(regex);
    }

    @Override
    public boolean isValidEndOfFileCommand(String command) {
        // Trim spaces before validation
        command = command.trim();
        String regex = "^0xFF$";
        return command.matches(regex);
    }


}
