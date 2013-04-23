package context;

import java.util.List;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Context {
  private static Context contextInstance = new Context();

  private Namespace currentNamespace;
  private List<Namespace> namespaceList;

  /**
   * Private constructor inside singleton.
   */
   private Context() {
     System.out.println("Init context.");
     // sets default namespace to "general"
     setDefaultNamespace();
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
  }

  private void setDefaultNamespace() {
    setCurrentNamespace(new Namespace("general"));
  }

  public void promptNamespace() {
    System.out.println("Current namespace:" + getCurrentNamespace().getName());
  }
}
