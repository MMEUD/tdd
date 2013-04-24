package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class SetCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("SET command");
    return true;
  }
}
