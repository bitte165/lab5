package ru.bitte.lab5.commands;

import ru.bitte.lab5.CollectionKeeper;
import ru.bitte.lab5.Terminal;

/**
 * An object of this class is used in {@link Terminal} as a command that outputs all the elements in the maintained
 * representation in the standard string representation. The object of this class is used by running
 * the {@code run()} method.
 * @implNote A no-argument command
 */
public class ShowCommand extends Command {
    private final CollectionKeeper collection;

    /**
     * Constructs a {@code ShowCommand} object.
     * @param collection the reference to a collection keeper of elements
     */
    public ShowCommand(CollectionKeeper collection) {
        super("show", "output all of the collection elements in the string representation");
        this.collection = collection;
    }

    @Override
    public void run() {
        collection.copyCollection().forEach(System.out::println);
    }
}
