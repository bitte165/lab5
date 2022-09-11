package ru.bitte.lab5.commands;

import ru.bitte.lab5.CollectionKeeper;
import ru.bitte.lab5.Terminal;
import ru.bitte.lab5.route.Route;

import java.util.ArrayList;
import java.util.Collections;

/**
 * An object of this class is used in {@link Terminal} as a command that prints all the unique distance values of the
 * elements of the maintained collection. The object of this class is used by running the {@code run()} method.
 * @implNote A no-argument command
 */
public class PrintUniqueCommand extends Command {
    private final CollectionKeeper collection;

    /**
     * Constructs a {@code PrintUniqueCommand} object.
     * @param collection the reference to a collection keeper of elements
     */
    public PrintUniqueCommand(CollectionKeeper collection) {
        super("print_unique_distance", "output all the unique \"distance\" field values");
        this.collection = collection;
    }

    @Override
    public void run() {
        // extracts the unique distance values
        ArrayList<Integer> values = new ArrayList<>();
        for (Route element : collection.copyCollection()) {
            if (!(values.contains(element.getDistance()))) { // doesn't add a value if it's already present
                values.add(element.getDistance());
            }
        }
        Collections.sort(values);
        for (int i=0; i < values.size() - 1; i++) {
            System.out.print(values.get(i) + ", ");
        }
        // prints the last entry without a comma at the end
        System.out.println(values.get(values.size() - 1));
    }
}
