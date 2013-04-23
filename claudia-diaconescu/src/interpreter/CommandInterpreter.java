package interpreter;

import org.omg.CORBA.StringHolder;

import java.util.StringTokenizer;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class CommandInterpreter extends CommandLine {

  private CommandLine commandLine;

  static final String COMMAND_NS = "ns";
  static final String COMMAND_SET = "set";
  static final String COMMAND_GET = "get";
  static final String COMMAND_SAVE = "save";
  static final String COMMAND_LOAD = "load";
  static final String COMMAND_LIST = "list";
  static final String COMMAND_EXIT = "exit";


  /**
   * Interprets the given command line and instantiate the appropriate command type.
   * @param commandLine
   */
  public CommandInterpreter(String commandLine) {
    StringTokenizer st = new StringTokenizer(commandLine, " ");
    if(st.hasMoreElements()) {
      String command = st.nextToken();

      if(command.equals(COMMAND_NS)) {
        this.commandLine = new NsCommand();
      } else if(command.equals(COMMAND_SET)) {
        this.commandLine =  new SetCommand();
      } else if (command.equals(COMMAND_GET)) {
        this.commandLine = new GetCommand();
      } else if (command.equals(COMMAND_SAVE)) {
        this.commandLine = new SaveCommand();
      } else if (command.equals(COMMAND_LOAD)) {
        this.commandLine = new LoadCommand();
      } else if (command.equals(COMMAND_LIST)) {
        this.commandLine = new ListCommand();
      } else if (command.equals(COMMAND_EXIT)) {
        this.commandLine = new ExitCommand();
      } else {
        System.out.println("Invalid command. Enter a valid command:");
      }

    } else {
      System.out.println("Enter a command:");
      return;
    }

  }

  @Override
  public boolean interpret() {
    boolean execute = true;
    if(commandLine != null) {
      execute = commandLine.interpret();
    }
    if(execute) {
      CommandLine.prompt();
    }
    return execute;
  }
}
