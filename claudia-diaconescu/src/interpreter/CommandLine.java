package interpreter;

import context.Property;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public abstract class CommandLine {

  /* Parameter entered in command line*/
  Property commandParameters[] = null;
  private int commandParameterNo = 0;

  /**
   * This method must be implemented by each command implementation
   * @return true if the command is interpreted and executed and false if the programs must exits.
   */
  public abstract boolean interpret();


  /**
   * Returns the correct syntax of the command. Must be implemented by each command implementation.
   */
  public abstract String commandSyntax();

  /**
   * Writes context prompt.
   */
  public static void prompt() {
    System.out.print(">");
  }

  public void promptCommandSyntax() {
    System.out.println("The command syntax is: '" + commandSyntax() + "'");
  }

  /**
   * Returns command line parameter
   * @return
   */
  public Property[] getCommandParameters() {
    return commandParameters;
  }

  /**
   * Return first parameter from the command line. If no parameter is entered will return null.
   * @return first parameter from the command line or null if no parameter is entered.
   */
  public Property getFirstCommandParameter() {
    if(hasParameters()) {
      return commandParameters[0];
    } else {
      return null;
    }
  }

  /**
   * Verifies if the command has parameters or not.
   * @return true if command has parameters and false otherwise.
   */
  public boolean hasParameters() {
    return commandParameters != null;
  }

  /**
   * Sets command line parameter
   * @param commandParameter
   */
  public void addCommandParameter(Property commandParameter){
    if(commandParameter != null) {
      if(commandParameters == null) {
        commandParameters = new Property[3];
      }
      commandParameters[commandParameterNo++] = commandParameter;
    }
  }
}
