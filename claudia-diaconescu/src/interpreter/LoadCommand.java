package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class LoadCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("LOAD command");
    return true;
  }
}
