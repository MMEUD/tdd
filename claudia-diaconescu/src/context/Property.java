package context;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Property {
  private String name;
  private String value;

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
}
