package simulation.model;

/**
 * Represents a person in the simulation.
 * <p>
 * This interface defines the basic methods that any person in the simulation must implement.
 * It includes methods for managing neighbors, retrieving current and next states, and updating states.
 * </p>
 */
public interface Person {
    public final int NEIGHBORHOOD_SIZE = 8;

    /**
     * Adds a neighbor to this person.
     *
     * @param neighbor the neighbor to be added
     */
    void addNeighbor(Object neighbor);

    /**
     * Gets the current state of this person.
     *
     * @return the current state
     */
    State getState();

    /**
     * Gets the next state of this person.
     *
     * @return the next state
     */
    State getNextState();

    /**
     * Updates the current state of this person to the next state.
     */
    void updateState();

    /**
     * Updates the next state of this person based on the current state and neighbors.
     */
    void updateNextState();
}
