package context;

import sortStrategy.QuickSortStrategy;
import sortStrategy.SortedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * This is a singleton that keeps the namespaces instances read from the command line.
 *
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Context {
  private static Context contextInstance = new Context();

  private Namespace currentNamespace;
  private HashMap<String,Namespace> namespaces;

  /**
   * Private constructor inside singleton.
   */
   private Context() {
     System.out.println("Init context.");
     this.namespaces = new HashMap<String, Namespace>();
     // sets default namespace to "general"
     setDefaultNamespace("general");
     this.namespaces.put(this.getCurrentNamespace().getName(), this.getCurrentNamespace());
   }

  /**
   * Gets the singleton context instance
   * @return
   */
  public static Context getInstance() {
    return contextInstance;
  }

  /**
   * Gets current namespace
   * @return
   */
  public Namespace getCurrentNamespace() {
    return currentNamespace;
  }

  /**
   * Sets current namespace
   * @param currentNamespace
   */
  public void setCurrentNamespace(Namespace currentNamespace) {
    this.currentNamespace = currentNamespace;
    if(!this.namespaces.containsKey(currentNamespace.getName())) {
      this.namespaces.put(currentNamespace.getName(), currentNamespace);
    }
  }

  /**
   * Sets default namespace to the given name
   */
  private void setDefaultNamespace(String defaultNamespaceName) {
    setCurrentNamespace(new Namespace(defaultNamespaceName));
  }

  /**
   * Returns a namespace by it's name from the context
   *
   * @param namespaceName The namespace name
   * @return The found namespace, if any
   * @throws Exception if no namespace with the given name found in the context
   */
  public Namespace getNamespaceByName(String namespaceName) throws Exception{
    if(namespaces.containsKey(namespaceName)) {
      return namespaces.get(namespaceName);
    } else {
      throw new Exception("No namespace with the given name found: " + namespaceName);
    }
  }

  /**
   * Returns a namespace by it's name from the context or cretes it if there is no namespace
   * defined with that name.
   *
   * @param namespaceName The namespace name
   * @return The found namespace, if any
   * @throws Exception if no namespace with the given name found in the context
   */
  public Namespace getAddNamespaceByName(String namespaceName) {
    if(namespaces.containsKey(namespaceName)) {
      return namespaces.get(namespaceName);
    } else {
      Namespace namespace = new Namespace(namespaceName);
      this.namespaces.put(namespaceName, namespace);
      return namespace;
    }
  }

  /**
   * Returns the list of namespaces from the context, alphabetically ordered by their names.
   * @return an ordered list of namespaces
   */
  public List<Namespace> getOrderedNamespaces() {
    SortedList sortedList = new SortedList(this.namespaces.values(), new QuickSortStrategy());
    sortedList.sort();
    return sortedList.getList();
  }


  /**
   * Displays current namespace name.
   */
  public void promptCurrentNamespaceName() {
    System.out.println("Current namespace: " + getCurrentNamespace().getName());
  }

  /**
   * Displays the given property from current namespace
   *
   * @param property
   */
  public void promptCurrentNamespace(Property property) {
    System.out.println(getCurrentNamespace().getName() + " : " + property.getName() + " = " + property.getValue());
  }

  /**
   * Displays all properties alphabetically ordered by name from the given namespace.
   *
   * @param namespace
   */
  public void promptNamespaceProperties(Namespace namespace) {
    List properties = namespace.getOrderedProperties();
    for(Iterator it=properties.listIterator(); it.hasNext();) {
      Property property = (Property)it.next();
      System.out.println(namespace.getName() + " : " + property.getName() + " = " + property.getValue());
    }
  }
}
