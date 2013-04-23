package context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Namespace {
  private String name;
  private List<Property> properties;

  /**
   * Creates a namespacxe by name.
   * @param name
   */
  public Namespace(String name) {
    this.name = name;
    this.properties = new ArrayList<Property>();
  }


  /**
   * Adds a property to the internal properties' list
   * @param property
   */
  public void addProperty(Property property) {
    this.properties.add(property);
  }


  public String getName() {
    return name;
  }
}
