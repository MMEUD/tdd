package context;

import interpreter.CommandInterpreter;
import interpreter.CommandLine;
import sortStrategy.QuickSortStrategy;
import sortStrategy.SortedList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Main {

  public static void main(String args[]) {

    SortedList list = new SortedList(new QuickSortStrategy());
    list.add("Gugu");
    list.add("Gaga");
    list.add("Aga");
    list.add("CILI");
    list.add("BALA");
    list.sort();


    try {
      for(int i=0; i<list.size(); i++) {
        System.out.println(list.get(i));
      }

    } catch(IndexOutOfBoundsException e) { System.out.println("OUT of bound");}


    Context context = Context.getInstance();
    context.promptCurrentNamespaceName();

    // read context from console and interpret them
    CommandLine.prompt();

    boolean execute = true;
    while (execute) {
      try {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String commandline = bufferedReader.readLine();

        CommandInterpreter commandInterpreter = new CommandInterpreter(commandline);

        execute = commandInterpreter.interpret();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}

