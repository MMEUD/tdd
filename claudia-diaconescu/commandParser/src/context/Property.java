package context;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Property implements Comparable {
  private String name;
  private String value;

  public Property(String name) {
    this.name = name;
  }

  public Property(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Gets property name
   * @return property name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets property name
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets property value
   * @return property value
   */
  public String getValue() {
    return value;
  }

  /**
   * Sets property value
   * @param value
   */
  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int compareTo(Object o) {
    if (o == null ) throw new IllegalArgumentException("Parameter can not be null!");
    if (!(o instanceof Property)) throw new IllegalArgumentException("Parameter must be of Property type!");
    return this.getName().compareTo(((Property)o).getName());
  }

}
