package interpreter;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class SaveCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("SAVE command");
    return true;
  }

  @Override
  public String commandSyntax() {
    return "save {namespace_name}";
  }
}
