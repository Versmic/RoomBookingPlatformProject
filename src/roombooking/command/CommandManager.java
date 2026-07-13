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

        commandHistory.push(command);
        undoHistory.clear(); 

        return true;
  }

  public Stack<Command> getCommandHistory() {
        return commandHistory;
  }
}
