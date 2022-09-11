package ru.bitte.lab5.commands;

import ru.bitte.lab5.CollectionKeeper;
import ru.bitte.lab5.Terminal;
import ru.bitte.lab5.route.Route;

import java.util.Objects;

/**
 * An object of this class is used in {@link Terminal} as a command that creates and adds a new {@link Route} object to
 * the maintained collection of objects. The object of this class is used by supplying a {@code Route} object through
 * the method of the parent {@link ElementCommand} class and then running by the {@code run()} method.
 * @see ElementCommand#passElement(Route)
 * @implNote An element command
 */
public class AddCommand extends ElementCommand {
    private final CollectionKeeper collection;

    /**
     * Constructs a {@code AddCommand} object.
     * @param collection the reference to a collection keeper of elements
     */
    public AddCommand(CollectionKeeper collection) {
        super("add", "add a new element to the collection");
        this.collection = collection;
    }

    @Override
    public void run() {
        collection.addElement(Objects.requireNonNull(getElement()));
        System.out.printf("Added %s to the collection\n", getElement().getName());
    }
}
