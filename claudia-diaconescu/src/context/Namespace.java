package context;

import sortStrategy.QuickSortStrategy;
import sortStrategy.SortedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Namespace implements Comparable {
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
   * Add / set a property to the internal properties' list
   * @param property
   */
  public void setProperty(Property property) {
    if(properties.containsKey(property.getName())) {
      // update property value
      properties.get(property.getName()).setValue(property.getValue());
    } else {
      // insert property
      this.properties.put(property.getName(), property);
    }
  }

  /**
   * Return a property by its' name from the current namespace
   * @param propertyName
   * @throws Exception when no property with the given name is found in naspace
   */
  public Property getPropertyByName(String propertyName) throws Exception {
    if(properties.containsKey(propertyName)) {
      return properties.get(propertyName);
    } else {
      throw new Exception("Undefined property:"  + propertyName);
    }
  }


  /**
   * Return the name of the namespace
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the list of properties from the namespace, alphabetically ordered by their names.
   * @return an ordered list of properties
   */
  public List<Property> getOrderedProperties() {
    SortedList sortedList = new SortedList(this.properties.values(), new QuickSortStrategy());
    sortedList.sort();
    return sortedList.getList();
  }

  public int getPropertiesSize() {
    return this.properties.size();
  }

  @Override
  public int compareTo(Object o) {
    if (o == null ) throw new IllegalArgumentException("Parameter can not be null!");
    if (!(o instanceof Namespace)) throw new IllegalArgumentException("Parameter must be of Namespace type!");
    return this.getName().compareTo(((Namespace)o).getName());
  }
}
