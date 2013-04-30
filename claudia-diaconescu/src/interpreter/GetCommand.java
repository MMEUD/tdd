package interpreter;

import context.Context;
import context.Property;

/**
 * Reads the parameter with the name {parameter_name} from the current namespace
 * and displays "{namespace_name} : {parameter_name}  = {parameter_value}" <br/>
 *
 * <b>Command syntax:</b> "get {parameter_name}"
 *
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class GetCommand extends CommandLine {
  @Override
  public boolean interpret() {
    System.out.println("GET command");

    if (this.hasParameters()) {
      // get the context instance
      Context context = Context.getInstance();
      Property commandParameter = this.getFirstCommandParameter();

      try {
        Property property = context.getCurrentNamespace().getPropertyByName(commandParameter.getName());

        // displays current namespace name
        context.promptCurrentNamespace(property);

      } catch (Exception e) {
        System.out.println("No property with '" + commandParameter.getName()
            + "' name defined in current namespace.");
      }

    } else {
      this.promptCommandSyntax();
    }

    return true;
  }

  @Override
  public String commandSyntax() {
    return "get {parameter_name}";
  }
}
