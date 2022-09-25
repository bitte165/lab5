package ru.bitte.lab5.commands;

import ru.bitte.lab5.Terminal;
import ru.bitte.lab5.exceptions.CommandParsingException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * An object of this class is used in {@link Terminal} as a command that reads and executes a script from a file.
 * The file must contain commands in the same format as they are run in the terminal, with a single command on a line
 * in the file. If a command in the file cannot be run, the execution moves onto the next command in the file.
 * The object of this class is used by supplying the name of the file containing a script through the setter method of
 * the parent class {@link ArgumentCommand} and running by the {@code run()} method.
 * @see ArgumentCommand#passArgument(String)
 * @implNote An argument command
 */
public class ExecuteScriptCommand extends ArgumentCommand {
    private final Terminal terminal;
    private int recursionDepth = 0;

    /**
     * Constructs a {@code ExecuteScriptCommand} object.
     * @param terminal a reference to a terminal instance to be used as an interpreter
     */
    public ExecuteScriptCommand(Terminal terminal) {
        super("execute_script", "file_name", "read and run a script from the specified file");
        this.terminal = terminal;
    }

    @Override
    public void run() {
        recursionDepth++;
        if (recursionDepth <= 2) {
            System.out.printf("Executing %s...\n", getArgument());
            try (Scanner script = new Scanner(new File(getArgument()))) {
                // scans the next line in the file
                while (script.hasNextLine()) {
                    String commandRaw = script.nextLine();
                    try {
                        // parses the command using the terminal and runs it
                        Command command = terminal.parseCommand(commandRaw);
                        terminal.addToHistory(command.getName());
                        command.run();
                    } catch (CommandParsingException e) {
                        // skips the command if it can't be executed (caught an exception while parsing)
                        System.out.println("Can't parse the following command: " + commandRaw);
                        System.out.println("Moving onto the next one...");
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("No file with such a name found. Please try again with an existing file.");
            }
            System.out.println("Finished the script execution.");
            recursionDepth--;
        } else {
            System.out.println("Can't execute several self-executing scripts, the recursion is too deep!");
            System.out.println("Skipping this script call...");
            recursionDepth--;
        }
    }
}
