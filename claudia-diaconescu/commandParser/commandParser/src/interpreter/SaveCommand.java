package interpreter;

import context.Context;
import context.Namespace;
import context.Property;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * If no namespace name is given as parameter  the program stores the properties of each namespace
 * into a file with the name {namespace_name}.properties located in the current directory
 * and displays for each file "{namespace_name} : saved X parameters." , where X is the number
 * of parameters saved <br/> <br/>
 * <p/>
 * If a namespace name is given as parameter the program stores the properties of the namespace
 * {namespace_name} in the text file with the name {namespace_name}.properties located in the current directory
 * And displays "{namespace_name} : saved X parameters." , where X is the number of parameters saved
 * <br/> <br/>
 * <p/>
 * <b>Command syntax:</b> "save [{namespace_name}]"
 *
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class SaveCommand extends CommandLine {

  @Override
  public boolean interpret() {
    System.out.println("SAVE command");

    try {
      if (this.hasParameters()) {
        // the parameter gotten from the ns command line represents a namespace name
        String namespaceName = this.getFirstCommandParameter().getName();

        Namespace namespace = Context.getInstance().getNamespaceByName(namespaceName);
        saveNamespace(namespace);

      } else {
        // get all namespaces from the context
        List<Namespace> namespaces = Context.getInstance().getOrderedNamespaces();

        // and saves them into properties files
        for(Namespace namespace : namespaces) {
          saveNamespace(namespace);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return true;
  }

  @Override
  public String commandSyntax() {
    return "save {namespace_name}";
  }

  /**
   * Saves a namespace in a properties file having the name of the namespace and
   * the '.properties' extension.
   *
   * @param namespace The instance of the namespace to be saved
   */
  private void saveNamespace(Namespace namespace) throws FileNotFoundException,
      UnsupportedEncodingException {

    List<Property> properties = namespace.getOrderedProperties();

    String filePath = System.getProperty("user.dir") + "/properties/"
        + namespace.getName() + ".properties";
    PrintWriter writer = new PrintWriter(filePath, "UTF-8");

    for (Property property : properties) {
      writer.println(property.getName() + " = " + property.getValue());
    }

    writer.close();

    System.out.println(
        namespace.getName() + " : " + "saved " + namespace.getPropertiesSize() + " parameters");
  }
}
