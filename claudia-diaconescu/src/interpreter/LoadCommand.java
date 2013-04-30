package interpreter;

import context.Context;
import context.Namespace;
import context.Property;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class LoadCommand extends CommandLine {


  @Override
  public boolean interpret() {
    System.out.println("LOAD command");

    if (this.hasParameters()) {
      // the parameter gotten from the ns command line represents a namespace name
      String namespaceName = this.getFirstCommandParameter().getName();


      try {
        Properties properties = loadProperties(namespaceName + ".properties");

        Namespace namespace = Context.getInstance().getAddNamespaceByName(namespaceName);

        Enumeration e = properties.propertyNames();

        while(e.hasMoreElements()) {
          String propertyName = (String)e.nextElement();
          String propertyValue = properties.getProperty(propertyName);
          Property property = new Property(propertyName, propertyValue);
          namespace.setProperty(property);
        }

        System.out.println(namespaceName + " : " + "loaded " + namespace.getPropertiesSize() +  " parameters");

        // displays all properties from the namespace
        Context.getInstance().promptNamespaceProperties(namespace);

      } catch (Exception e) {
        System.out.println(
            "There is no properties file defined for the following namespace: " + namespaceName);
      }


    } else {
      this.promptCommandSyntax();
    }
    return true;
  }

  @Override
  public String commandSyntax() {
    return "load {namespace_name}";
  }

  /**
   * @param propertiesFile
   * @return
   * @throws Exception
   */
  public Properties loadProperties(String propertiesFile) throws Exception {
    Properties properties = new Properties();
    String currentDir = System.getProperty("user.dir");
    //File f = new File(currentDir + "/properties/" + propertiesFile);

    File f = new File("../properties/" + propertiesFile);

    // todo test
    //if (f.exists()) {
      InputStream in = getClass().getResourceAsStream("../properties/" + propertiesFile);
      try {
        properties.load(in);
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return properties;
//    } else {
//      throw new Exception("No properties file defined:" + propertiesFile);
//    }
  }
}
