package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public abstract class CommandLine {
  /**
   * This method must be implemented by each command implementation
   */
  public abstract boolean interpret();

  /**
   * Writes context prompt.
   */
  public static void prompt() {
    System.out.print(">");
  }
}
