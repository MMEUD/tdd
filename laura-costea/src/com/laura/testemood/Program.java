package com.laura.testemood;

import java.io.IOException;

import com.laura.testemood.command.*;

public class Program {
	private static final java.io.Console console = System.console();
	private Context ctx = new Context();

	public static void main(String... mainArgs) {
		Program program = new Program();
		program.run(mainArgs);
	}

	public void run(String[] mainArgs) {
		try {
			System.out.println(getInitMessage());

			while (true) {
				String cmdLine = readCommandLine();

				if (cmdLine.trim().matches("exit")) {
					System.exit(0);
				} else {
					String args[] = CommandLineParser.parseArgs(cmdLine);
					if (args.length > 0) {

						ICommand cmd = CommandFactory.createCommand(args);
						ctx.setArgs(args);

						if (cmd != null)
							System.out.println(cmd.execute(ctx));
						else
							System.out.println("Command not defined");
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Coudn't read command line!");
			e.printStackTrace();
		}

	}

	private String getInitMessage() {
		return "Current namespace: " + ctx.getNamespace();

	}

	private String readCommandLine() throws IOException {
		String line = console.readLine();
		return line;
	}

}
