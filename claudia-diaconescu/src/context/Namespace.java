package context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Namespace {
  private String name;
  private HashMap<String,Property> properties;

  /**
   * Creates a namespacxe by name.
   * @param name
   */
  public Namespace(String name) {
    this.name = name;
    this.properties = new HashMap<String,Property>();
  }


  /**
   * Adds a property to the internal properties' list
   * @param property
   */
  public void addProperty(Property property) {
    if(properties.containsKey(property.getName())) {
      // update property value
      properties.get(property.getName()).setValue(property.getValue());
    } else {
      // insert property
      this.properties.put(property.getName(), property);
    }
  }


  public String getName() {
    return name;
  }
}
