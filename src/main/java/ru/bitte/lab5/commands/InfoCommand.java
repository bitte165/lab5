package ru.bitte.lab5.commands;

import ru.bitte.lab5.CollectionKeeper;
import ru.bitte.lab5.Terminal;
import ru.bitte.lab5.route.Route;

import java.util.Collections;
import java.util.Set;

/**
 * An object of this class is used in {@link Terminal} as a command that outputs information about the current state
 * of the maintained collection. The information includes the type of the collection's elements, the creation date, the
 * current number of elements in it, the maximum and the minimum distances among the elements in the collection.
 * The object of this class is used by running the {@code run()} method.
 * @implNote A no-argument command
 */
public class InfoCommand extends Command {
    private final CollectionKeeper collection;

    /**
     * Constructs a {@code InfoCommand} object.
     * @param collection the reference to a collection keeper of elements
     */
    public InfoCommand(CollectionKeeper collection) {
        super("info", "output information about the collection");
        this.collection = collection;
    }

    @Override
    public void run() {
        Set<Route> elements = collection.copySorted();
        System.out.println("Information about this collection:");
        System.out.println("Type: " + collection.getCollectionType());
        System.out.println("Creation date: " + collection.getCreationDate().toString());
        System.out.println("Number of elements: " + collection.getCollectionSize());
        System.out.println("Max distance: " + Collections.max(elements).toString());
        System.out.println("Min distance: " + Collections.min(elements).toString());
    }
}
