package interpreter;

import context.Context;
import context.Namespace;
import context.Property;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * If no namespace name is given as parameter the program reads all the files with
 * extension .properties located in the current directory and stores all the properties of a file
 * in the namespace {name_of_the_file} and then displays for each file
 * "{namespace_of_the_file} : loaded X parameters." , where X is the number of parameters loaded  <br/> <br/>
 * <p/>
 * If a namespace name is given as parameter the program reads the properties of a the text
 * file {namespace_name}.properties located in the current directory and stores them
 * in the namespace {namespace_name} and then displays
 * "{namespace_name} : loaded X parameters." , where X is the number of parameters loaded <br/> <br/>
 * <p/>
 * <b>Command syntax:</b> "load [{namespace_name}]"
 *
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
        Namespace namespace = loadNamespace(namespaceName);

        System.out.println(
            namespaceName + " : " + "loaded " + namespace.getPropertiesSize() + " parameters");

        // displays all properties from the namespace
        Context.getInstance().promptNamespaceProperties(namespace);

      } catch (Exception e) {
        System.out.println(
            "There is no properties file defined for the following namespace: " + namespaceName);
      }


    } else {
      String currentDirPath = System.getProperty("user.dir");
      File currentDir = new File(currentDirPath + "/properties");
      try {
        List<File> propertiesFiles = LoadCommand.getFileListing(currentDir);

        for(File file : propertiesFiles) {
          String namespaceName = file.getName().substring(0, file.getName().indexOf('.'));
          System.out.println("Namespace:" + namespaceName);

          Namespace namespace = loadNamespace(namespaceName);

          System.out.println(
              namespaceName + " : " + "loaded " + namespace.getPropertiesSize() + " parameters");

          // displays all properties from the namespace
          Context.getInstance().promptNamespaceProperties(namespace);

        }
      } catch (FileNotFoundException e) {e.printStackTrace();}
    }
    return true;
  }

  @Override
  public String commandSyntax() {
    return "load {namespace_name}";
  }

  /**
   * Loads a namespace from the specified properties file
   * and also add the namespace to the context instance.
   *
   * @param namespaceName the name of the namespace to be loaded from properties files
   * @return A instance of the loaded namespace
   */
  private Namespace loadNamespace(String namespaceName) throws FileNotFoundException{
    Properties properties = loadProperties(namespaceName + ".properties");

    // add the namespace to the context instance
    Namespace namespace = Context.getInstance().getAddNamespaceByName(namespaceName);

    Enumeration e = properties.propertyNames();

    while (e.hasMoreElements()) {
      String propertyName = (String) e.nextElement();
      String propertyValue = properties.getProperty(propertyName);
      Property property = new Property(propertyName, propertyValue);
      namespace.setProperty(property);
    }

    return namespace;
  }

  /**
   * Load a properties file and create a Properties instance with all properties gotten from file.
   *
   * @param propertiesFileName The name of the properties' file (without extension)
   * @return A <link>Properties</link> instance with loaded properties
   * @throws FileNotFoundException if no properties file found for the given parameter
   */
  private Properties loadProperties(String propertiesFileName) throws FileNotFoundException {
    Properties properties = new Properties();
    String currentDir = System.getProperty("user.dir");
    File f = new File(currentDir + "/properties/" + propertiesFileName);

    if (f.exists()) {
      InputStream in = this.getClass().getResourceAsStream("../" + propertiesFileName);
      try {
        properties.load(in);
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return properties;
    } else {
      throw new FileNotFoundException("No properties file defined:" + propertiesFileName);
    }
  }

  /**
   * Recursively walk a directory tree and return a List of all files found.
   *
   * @param dir is a valid directory, which can be read.
   */
  static private List<File> getFileListing(File dir) throws FileNotFoundException {
    List<File> result = new ArrayList<File>();
    File[] filesAndDirs = dir.listFiles();
    List<File> filesDirs = Arrays.asList(filesAndDirs);
    for (File file : filesDirs) {
      if (!file.isDirectory()) {
        if(file.getPath().endsWith(".properties")) {
          result.add(file); // add only if not directory and if it's a properties file
        }
      } else {
        result.addAll(getFileListing(file)); // recursive call if directory
      }
    }
    return result;

  }
}
