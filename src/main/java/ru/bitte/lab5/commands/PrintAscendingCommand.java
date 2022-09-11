package ru.bitte.lab5.commands;

import ru.bitte.lab5.CollectionKeeper;
import ru.bitte.lab5.Terminal;
import ru.bitte.lab5.route.Route;

import java.util.TreeSet;

/**
 * An object of this class is used in {@link Terminal} as a command that prints all the elements in the maintained
 * collection in the ascending order (sorting by the distance value). The object of this class is used by running
 * the {@code run()} method.
 * @implNote A no-argument command
 */
public class PrintAscendingCommand extends Command {
    private final CollectionKeeper collection;

    /**
     * Constructs a {@code PrintAscendingCommand} object.
     * @param collection the reference to a collection keeper of elements
     */
    public PrintAscendingCommand(CollectionKeeper collection) {
        super("print_ascending", "output the collection elements in the ascending order");
        this.collection = collection;
    }

    @Override
    public void run() {
        System.out.println("Collection elements in the ascending order:");
        TreeSet<Route> mirror = collection.copyMirror();
        for (Route element : mirror) {
            System.out.println(element.format());
        }
    }
}
