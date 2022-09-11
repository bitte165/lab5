package ru.bitte.lab5.commands;

import ru.bitte.lab5.Terminal;

/**
 * An object of this class is used in {@link Terminal} as a command that exits the program execution. In reality, this
 * command itself does not exit the program, but it being run indicates to the terminal the intention to finish the
 * program execution. The object of this class is used by running the {@code run()} method.
 * @implNote A no-argument command
 */
public class ExitCommand extends Command {
    /**
     * Constructs a {@code ExitCommand} object.
     */
    public ExitCommand() {
        super("exit", "exit the program without saving the collection");
    }

    @Override
    public void run() {
        System.out.println("Exiting...");
    }
}
