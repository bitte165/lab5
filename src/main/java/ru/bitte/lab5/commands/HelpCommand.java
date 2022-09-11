package ru.bitte.lab5.commands;


import ru.bitte.lab5.Terminal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/**
 * An object of this class is used in {@link Terminal} as a command that outputs the list of available commands and
 * their descriptions. The object of this class is used by running the {@code run()} method.
 * @implNote A no-argument command
 */
public class HelpCommand extends Command {
    private final HashMap<String, Command> commands;

    /**
     * Constructs a {@code HelpCommand} object.
     * @param commands the reference to a {@link HashMap} holding the commands
     */
    public HelpCommand(HashMap<String, Command> commands) {
        super("help", "list all available commands");
        this.commands = commands;
    }

    @Override
    public void run() {
        ArrayList<Command> commandsObjects = new ArrayList<>(commands.values());
        commandsObjects.sort(Comparator.comparing(Command::getName));
        System.out.println("Available commands:");
        Iterator<Command> iterator = commandsObjects.iterator();
        while (iterator.hasNext()) {
            Command command = iterator.next();
            System.out.print(" - " + command.getName());
            if (command instanceof ArgumentCommand) { // appends the argument name if it's an argument command
                System.out.print(" " + ((ArgumentCommand) command).getArgumentName());
            } else if (command instanceof IDCommand) { // appends the word "id" if it's an id command
                System.out.print(" id");
            }
            if (command instanceof ElementCommand) { // appends "{element}" if it's an element command
                System.out.print(" {element}");
            }
            // prints a period after the last command
            if (!iterator.hasNext()) {
                System.out.println(" : " + command.getDescription() + ".");
            } else {
                System.out.println(" : " + command.getDescription() + ",");
            }
        }
        System.out.println("Note: the \"{element}\" commands should not be provided an element argument. Instead, " +
                "an element constructor is called.");
    }
}
