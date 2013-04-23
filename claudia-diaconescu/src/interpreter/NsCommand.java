package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class NsCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("NS command");
    return true;
  }
}
