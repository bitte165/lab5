package ru.bitte.lab5.commands;

import ru.bitte.lab5.CollectionKeeper;
import ru.bitte.lab5.Terminal;
import ru.bitte.lab5.route.Route;

import java.util.Objects;
import java.util.TreeSet;

/**
 * An object of this class is used in {@link Terminal} as a command that filters the elements of the collection by their
 * names and prints the filtered elements' formatted representations. The object of this class is used by supplying the
 * string that must be contained in the filtered elements' names through the setter method of the parent class
 * {@link ArgumentCommand} and running by the {@code run()} method.
 * @see ArgumentCommand#passArgument(String)
 * @implNote An argument command
 */
public class FilterCommand extends ArgumentCommand {
    private final CollectionKeeper collection;

    /**
     * Constructs a {@code FilterCommand} object.
     * @param collection the reference to a collection keeper of elements
     */
    public FilterCommand(CollectionKeeper collection) {
        super("filter_contains_name", "name", "output elements the names of which " +
                "contain a given substring");
        this.collection = collection;
    }

    @Override
    public void run() {
        System.out.printf("Elements in the collection containing \"%s\" in the name:\n", getArgument());
        TreeSet<Route> filteredRoutes = collection.filterByString(Objects.requireNonNull(getArgument()));
        for (Route element : filteredRoutes) {
            System.out.println(element.format());
        }
    }
}
