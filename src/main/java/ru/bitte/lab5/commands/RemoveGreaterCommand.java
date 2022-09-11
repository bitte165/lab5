package ru.bitte.lab5.commands;

import ru.bitte.lab5.CollectionKeeper;
import ru.bitte.lab5.Terminal;
import ru.bitte.lab5.route.Route;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * An object of this class is used in {@link Terminal} as a command that the elements of the collection the distance of
 * which is greater than the distance of the provided element. The object of this class is used by supplying a
 * {@code Route} object through the method of the parent {@link ElementCommand} class and then running by
 * the {@code run()} method.
 * @see ElementCommand#passElement(Route)
 * @implNote An element command
 */
public class RemoveGreaterCommand extends ElementCommand {
    private final CollectionKeeper collection;

    /**
     * Constructs a {@code RemoveGreaterCommand} object.
     * @param collection the reference to a collection keeper of elements
     */
    public RemoveGreaterCommand(CollectionKeeper collection) {
        super("remove_greater", "remove all of the collection elements " +
                "the distance of which exceeds the given element's");
        this.collection = collection;
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        // gets the elements the distance of which is greater than of the provided one
        ArrayList<Route> greaterElements = collection.getElementsGreaterThan(getElement());
        int n = greaterElements.size();
        System.out.printf("Are you sure you want to remove %d elements from the collection? [Y/n]: ", n);
        String response = in.nextLine();
        if (response.equals("Y")) {
            // removes the elements
            for (Route element : collection.copyCollection()) {
                if (greaterElements.contains(element)) {
                    collection.removeElement(element);
                }
            }
            System.out.printf("Successfully removed %d elements.\n", n);
        } else {
            System.out.println("Canceled the command.");
        }
    }
}

