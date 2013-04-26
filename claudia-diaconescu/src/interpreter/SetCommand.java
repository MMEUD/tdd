package interpreter;

import context.Context;
import context.Namespace;
import context.Property;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class SetCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("SET command");

    if (this.hasParameters()) {
      // get the context instance
      Context context = Context.getInstance();
      Property property = this.getFirstCommandParameter();
      context.getCurrentNamespace().addProperty(property);

      // displays current namespace name
      context.promptCurrentNamespace(property);

    } else {
      this.promptCommandSyntax();
    }
    return true;
  }

  @Override
  public String commandSyntax() {
    return "set {parameter_name} {parameter_value}";
  }
}
