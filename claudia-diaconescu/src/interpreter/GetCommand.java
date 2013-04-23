package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class GetCommand extends CommandLine {
  @Override
  public boolean interpret() {
    System.out.println("GET command");
    return true;
  }
}
