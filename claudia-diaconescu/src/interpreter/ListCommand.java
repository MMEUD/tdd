package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class ListCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("List command");
    return true;
  }

  @Override
  public String commandSyntax() {
    return "list {namespace_name}";
  }
}
