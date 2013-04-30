package interpreter;

import context.Context;
import context.Namespace;

/**
 * Set the current namespace to {namespace_name}
 * and displays "Current namespace : {namespace_name}"  <br/>
 *
 * <b>Command syntax:</b>  "ns {namespace_name}"
 *
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class NsCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("NS command");

    if (this.hasParameters()) {
      // the parameter gotten from the ns command line represents a namespace name
      String namespaceName = this.getFirstCommandParameter().getName();

      // get the context instance
      Context context = Context.getInstance();
      context.setCurrentNamespace(new Namespace(namespaceName));

      // displays current namespace name
      context.promptCurrentNamespaceName();

    } else {
      this.promptCommandSyntax();
    }
    return true;
  }

  @Override
  public String commandSyntax() {
    return "ns {namespace_name}";
  }
}
