package ru.bitte.lab5;

import org.xml.sax.SAXException;
import ru.bitte.lab5.commands.*;
import ru.bitte.lab5.exceptions.*;
import ru.bitte.lab5.route.Coordinates;
import ru.bitte.lab5.route.Location;
import ru.bitte.lab5.route.Route;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The class {@code Terminal} serves as the main logic part of the program. An object of this class carries out the task
 * of running the command line interface, including handling commands input by the user by parsing and calling them,
 * maintaining the collection keeper, holding the command objects, remembering the last used commands, and generating
 * new objects of the {@link Route} class. Before using the terminal, an instance of it must be instantiated by
 * supplying a {@link File} object of a file containing the initial {@code Route} objects to be put in the collection.
 * After that, the interactive command line interface can be called by the method {@code start()}. In this mode, the
 * user can call the available commands the list of which is described in the table below.
 * <br>
 * <table border="1">
 *     <caption>Available commands</caption>
 *   <tr>
 *      <td> add {element} </td> <td> add a new element to the collection </td>
 *   </tr>
 *   <tr>
 *     <td> add_if_min {element} </td>
 *     <td> add a new element to the collection if its value is less than that of the minimum element in the collection </td>
 *   </tr>
 *   <tr>
 *     <td> clear </td> <td> clear the collection </td>
 *   </tr>
 *   <tr>
 *     <td> execute_script file_name </td> <td> read and run a script from the specified file </td>
 *   </tr>
 *   <tr>
 *     <td> exit </td> <td> exit the program without saving the collection </td>
 *   </tr>
 *   <tr>
 *     <td> filter_contains_name name </td> <td> output elements the names of which contain a given substring </td>
 *   </tr>
 *   <tr>
 *     <td> help </td> <td> list all available commands </td>
 *   </tr>
 *   <tr>
 *     <td> history </td> <td> output the last 15 used commands (without their arguments) </td>
 *   </tr>
 *   <tr>
 *     <td> info </td> <td> output information about the collection </td>
 *   </tr>
 *   <tr>
 *     <td> print_ascending </td> <td> output the collection elements in the ascending order </td>
 *   </tr>
 *   <tr>
 *     <td> print_unique_distance </td> <td> output all the unique "distance" field values </td>
 *   </tr>
 *   <tr>
 *     <td> remove_by_id id </td> <td> remove an element of the specified ID from the collection </td>
 *   </tr>
 *   <tr>
 *     <td> remove_greater {element} </td>
 *     <td> remove all of the collection elements the distance of which exceeds the given element's </td>
 *   </tr>
 *   <tr>
 *     <td> save </td> <td> save the collection to a file </td>
 *   </tr>
 *   <tr>
 *     <td> show </td> <td> output all of the collection elements in the string representation </td>
 *   </tr>
 *   <tr>
 *     <td> update id {element} </td> <td>update the values of the collection element provided by the ID </td>
 *   </tr>
 * </table>
 */
public class Terminal {
    private final Scanner in;
    @SuppressWarnings("FieldCanBeLocal")
    private final CollectionKeeper collection;
    private final Map<String, Command> commands;
    private final Deque<String> history;

    /**
     * Constructs an instance of the {@code Terminal} class, reading {@link Route} elements from an XML file and adding
     * them to a collection.
     * @param file the {@code File} object representing an XML file that contains properly formatted representations
     * of {@link Route} objects, i.e. elements, that get put into a collection and get later interacted with by commands.
     * @throws ElementParsingInFileException if an element couldn't be properly parsed from a file, including logical
     * and formatting errors
     * @throws IOException if an input/output exception occurred
     * @throws SAXException if a SAX exception occurred
     * @throws ParserConfigurationException if a parser configuration exception occurred
     * @throws TransformerConfigurationException if an XML transformer exception occurred
     */
    public Terminal(File file) throws IOException, SAXException, ParserConfigurationException,
            ElementParsingInFileException, TransformerConfigurationException {
        in = new Scanner(System.in);
        Parser parser = new Parser();
        // initialize the collection keeper with a file
        collection = new CollectionKeeper(parser.readFromFile(file));
        // initialize the commands by first getting them in a hashset and then adding to a hashmap in a loop
        history = new ArrayDeque<>(15);
        commands = new HashMap<>();
        HashSet<Command> tempComs = new HashSet<>();
        tempComs.add(new AddCommand(collection));
        tempComs.add(new AddIfMinCommand(collection));
        tempComs.add(new ClearCommand(collection));
        tempComs.add(new ExecuteScriptCommand(this));
        tempComs.add(new ExitCommand());
        tempComs.add(new FilterCommand(collection));
        tempComs.add(new HelpCommand(commands));
        tempComs.add(new HistoryCommand(history));
        tempComs.add(new InfoCommand(collection));
        tempComs.add(new PrintAscendingCommand(collection));
        tempComs.add(new PrintUniqueCommand(collection));
        tempComs.add(new RemoveByIDCommand(collection));
        tempComs.add(new RemoveGreaterCommand(collection));
        tempComs.add(new SaveCommand(collection, parser));
        tempComs.add(new ShowCommand(collection));
        tempComs.add(new UpdateCommand(collection));
        tempComs.forEach(command -> commands.put(command.getName(), command));
    }

    /**
     * Starts the interactive terminal which reads user-input commands.
     */
    public void start() {
        System.out.println("Welcome to Lab5! See \"help\" for the list of commands.");
        System.out.print("> "); // writes the first arrow
        while (in.hasNextLine()) {
             String input = in.nextLine();
             Command command;
             try {
                 command = parseCommand(input);
             } catch (CommandParsingException e) {
                 /* if the exception message is empty, that means the input was empty and so a new arrow is printed
                 without anything else. otherwise, the error message is printed plus an arrow on a new line */
                 System.out.print(e.getMessage().equals("") ? "> " : e.getMessage() + "\n> ");
                 continue;
             }
             command.run();
             addToHistory(command.getName());
             if (command instanceof ExitCommand) {
                 break;
             }
             System.out.print("> ");
         }
        in.close();
    }

    /**
     * Reads a string containing a command input by the user and parses it, returning a prepared-to-be-run command
     * object upon success.
     * @param rawCommand a string containing a call to a command
     * @return an object of a command prepared to be run
     * @throws CommandParsingException if a nonexistent/incorrectly used command was called
     */
    public Command parseCommand(String rawCommand) throws CommandParsingException {
        // the messages provided in exceptions are printed in the terminal when they're caught
        String[] splitCommand = rawCommand.strip().split(" "); // split the command into an array by spaces
        if (splitCommand.length == 1 && splitCommand[0].equals("")) { // used for skipping line when entering whitespace
            throw new CommandParsingException("");
        }
        String commandName = splitCommand[0]; // get the command name
        if (!commands.containsKey(commandName)) { // verifies that a used command exists
            throw new CommandParsingException("Unknown command. Please see \"help\" for the list of commands.");
        }
        if (splitCommand.length > 2) { // verifies the number of arguments
            throw new CommandParsingException("Too many arguments provided. Please see \"help\" on command usage.");
        }
        // if an argument was provided, it's set, and if it wasn't, the argument variable is set to an empty string
        String argument = splitCommand.length == 2 ? splitCommand[1] : "";
        Command command = commands.get(commandName); // retrieves a command object
        // makes sure an argument-requiring command has been provided one
        if (argument.equals("") && (command instanceof ArgumentCommand || command instanceof IDCommand)) {
            throw new CommandParsingException("Missing command argument. Please see \"help\" on command usage.");
        }
        // makes sure an argument-non-requiring command isn't provided one
        if (!argument.equals("") && !(command instanceof ArgumentCommand || command instanceof IDCommand)) {
            throw new CommandParsingException("Incorrect command usage. Please see \"help\" on command usage.");
        }
        // give an id command its id
        if (command instanceof IDCommand && isInteger(argument)) { // makes sure the argument is an integer
            ((IDCommand) command).passID(Integer.parseInt(argument));
        } else if (command instanceof IDCommand && !isInteger(argument)) { // throw an exception if it isn't
            throw new CommandParsingException("Incorrect ID command usage. Please see \"help\" on command usage.");
        }
        // give an argument command its argument
        if (command instanceof ArgumentCommand) {
            ((ArgumentCommand) command).passArgument(argument);
        }
        // get a route element, pass it to an element command and handle exceptions with it
        if (command instanceof ElementCommand) {
            try {
                Route element = generateRoute();
                System.out.printf("Successfully generated a Route object entitled \"%s\"\n", element.getName());
                ((ElementCommand) command).passElement(element);
            } catch (ElementParsingFromCommandException e) {
                throw new CommandParsingException("Error creating a Route object from user inputs. Reason: " +
                        e.getMessage() + "\nPlease try again.");
            } catch (ElementConstructionException e) {
                throw new CommandParsingException("Error initializing a Route object. Reason: " + e.getMessage());
            }
        }
        return command;
    }

    /**
     * Adds the name of a recently called command to the history of commands. The list of these commands contains the
     * names of the last 15 used commands and is managed by the FIFO principle, where the oldest entry is the first to
     * be removed when the capacity reaches 15.
     * @param command the name of a command
     */
    public void addToHistory(String command) {
        if (history.size() == 15) {
            // if the capacity reaches 15, remove the oldest element
            history.removeFirst();
        }
        history.add(command);
    }

    /**
     * Returns an object of the {@link Route} class, assembled from inputs given by the user. The inputs are entered
     * interactively upon the method being called.
     * @return An object of the {@code Route} class
     * @throws ElementParsingFromCommandException if an incorrect input was entered by the user
     * @throws ElementConstructionException if there was a trouble constructing objects from user input
     * @see Route#Route(String, Coordinates, Location, Location)  Route
     * @see Location#Location(Long, Long, float, String)  Location
     */
    public Route generateRoute() throws ElementParsingFromCommandException, ElementConstructionException {
        // reader for coordinates that separates values by a comma
        Scanner inSep = new Scanner(System.in).useDelimiter("[\\s,]+");
        System.out.println("Assembling a Route object...");
        // get name
        System.out.print("Enter a name (can't be empty): ");
        String name = in.nextLine();
        // get coordinates
        Coordinates coords;
        System.out.println("Enter X and Y coordinates of the current position separated by a comma:");
        long[] coordsXY = readXY(inSep);
        coords = new Coordinates(coordsXY[0], coordsXY[1]);
        verifyEndOfLine(inSep);
        // get from
        Location from = newLocation(inSep, "starting");
        // starting point?
        // get to
        Location to = newLocation(inSep, "destination");
        return new Route(name, coords, from, to);
    }

    // a method for reading the first two values input on a line separated by commas as long
    private long[] readXY(Scanner in) throws ElementParsingFromCommandException {
        long[] xy = new long[2];
        for (int i = 0; i < 2; i++) {
            if (in.hasNextLong()) {
                xy[i] = in.nextLong();
            } else {
                throw new ElementParsingFromCommandException("Error reading X and Y values");
            }
        }
        return xy;
    }

    // verifies that there are no unexpected characters at the end of an input line
    private void verifyEndOfLine(Scanner in) throws ElementParsingFromCommandException {
        if (!in.nextLine().strip().equals("")) {
            throw new ElementParsingFromCommandException("Unexpected characters detected at the end of input");
        }
    }

    // constructs a new location object
    private Location newLocation(Scanner in, String kind) throws ElementParsingFromCommandException {
        System.out.println("Enter X, Y and Z coordinates of the " + kind + " point separated by a comma:");
        long[] locXY = readXY(in); // get the first two values in the same way as coordinates (long values)
        float locZ; // define a special value for z since it's a float
        // read z and throw an error if it's not a float
        if (in.hasNextFloat()) {
            locZ = in.nextFloat();
        } else {
            throw new ElementParsingFromCommandException("Couldn't read the Z value from input");
        }
        verifyEndOfLine(in);
        System.out.println("Enter that location's name (leave blank for the default):");
        String name = in.nextLine();
        return new Location(locXY[0], locXY[1], locZ, name);
    }

    // checks if a string is an integer
    private static boolean isInteger(String s) {
        Scanner sc = new Scanner(s.strip());
        if(!sc.hasNextInt()) return false;
        sc.nextInt();
        return !sc.hasNext();
    }
}
