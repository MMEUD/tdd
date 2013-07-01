package interpreter;

import context.Property;

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
   * @param commandLineStr String containing the expression entered in command line
   *                    (containing commands and commands' parameters)
   */
  public CommandInterpreter(String commandLineStr) {
    StringTokenizer st = new StringTokenizer(commandLineStr, " ");
    if(st.hasMoreElements()) {
      // get command from command line
      String command = st.nextToken();
      Property commandParameter = null;

      // get parameter name from command line
      if(st.hasMoreElements()) {
        commandParameter = new Property(st.nextToken());
        // get parameter value from command line
        if(st.hasMoreElements()) {
          commandParameter.setValue(st.nextToken());
        }
      }

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
        return;
      }

      // add to the command the command parameter read from the command line
      this.commandLine.addCommandParameter(commandParameter);

    } else {
      // no command entered
      System.out.println("Enter a command:");
      return;
    }
  }

  @Override
  public boolean interpret() {
    boolean execute = true;
    if(this.commandLine != null) {
      execute = this.commandLine.interpret();
    }
    if(execute) {
      CommandLine.prompt();
    }
    return execute;
  }

  @Override
  public String commandSyntax() {
    return ""; // not valid this case
  }



}
