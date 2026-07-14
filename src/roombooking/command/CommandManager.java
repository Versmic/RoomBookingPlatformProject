package roombooking.command;

import java.util.Stack;

public class CommandManager {
    private Stack<Command> commandHistory;
    private Stack<Command> undoHistory;
    private int maxHistorySize;
    
    public CommandManager() {
        this.commandHistory = new Stack<>();
        this.undoHistory = new Stack<>();
        this.maxHistorySize = 100;
    }
    
    public CommandManager(int maxHistorySize) {
        this();
        this.maxHistorySize = maxHistorySize;
    }
    
    public boolean executeCommand(Command command) {
        if (command == null) {
            return false;
        }
        
        boolean success = command.execute();
        if (success) {
            commandHistory.push(command);
            undoHistory.clear();
            
            while (commandHistory.size() > maxHistorySize) {
                commandHistory.remove(0);
            }
        }
        return success;
    }
    
    public boolean undoLastCommand() {
        if (commandHistory.isEmpty()) {
            return false;
        }
        
        Command command = commandHistory.pop();
        boolean success = command.undo();
        if (success) {
            undoHistory.push(command);
        } else {
            commandHistory.push(command);
        }
        return success;
    }
    
    public boolean redoLastCommand() {
        if (undoHistory.isEmpty()) {
            return false;
        }
        
        Command command = undoHistory.pop();
        boolean success = command.execute();
        if (success) {
            commandHistory.push(command);
        }
        return success;
    }
    
    public void clearHistory() {
        commandHistory.clear();
        undoHistory.clear();
    }
    
    public int getCommandCount() {
        return commandHistory.size();
    }
    
    public Stack<Command> getCommandHistory() {
        return commandHistory;
    }
}
