package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class ExitCommand extends CommandLine {

  @Override
  public boolean interpret() {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String commandSyntax() {
    return "exit";
  }
}
