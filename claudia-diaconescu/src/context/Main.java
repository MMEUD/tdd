package context;

import interpreter.CommandInterpreter;
import interpreter.CommandLine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author <a href="mailto:c.diaconescu@moodmedia.ro">Claudia Diaconescu</a>
 * @version $Revision:$
 */
public class Main {

  public static void main(String args[]) {

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

