package interpreter;

import context.Context;
import context.Namespace;
import context.Property;

import java.util.Iterator;
import java.util.List;

/**
 * If no namespace name is given as parameter reads all properties from all namespaces ordering alphabetically the namespaces and the parameters
 * and displays "{namespace_name} : {parameter_name} = {parameter_value}" <br/> <br/>
 *
 * If a namespace name is given as parameter reads the properties of the namespace {namespace_name}
 * and displays "{namespace_name} : {parameter_name} = {parameter_value}"  <br/>
 *
 * <b>Command syntax:</b> "list [{namespace_name}]"
 *
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class ListCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("List command");

    // get the context instance
    Context context = Context.getInstance();

    if (this.hasParameters()) {
      Property commandParameter = this.getFirstCommandParameter();

      try {
        Namespace namespace = context.getNamespaceByName(commandParameter.getName());
        // displays namespace' properties
        context.promptNamespaceProperties(namespace);

      } catch (Exception e) {
        System.out.println("No namespace with '" + commandParameter.getName()
            + "' name defined in current context.");
      }

    } else {
      List<Namespace> orderedNamespaces = context.getOrderedNamespaces();

      for(Iterator it=orderedNamespaces.listIterator(); it.hasNext();) {
        Namespace namespace = (Namespace)it.next();
        // displays namespace' properties
        context.promptNamespaceProperties(namespace);
      }

    }

    return true;
  }

  @Override
  public String commandSyntax() {
    return "list [{namespace_name}]";
  }
}
