package simulation.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Controller class for managing a grid of GymPerson objects in a gym motivation simulation.
 * <p>
 * This class represents a grid where each cell contains a GymPerson with different
 * states of gym motivation. It handles the initialization of the grid with different
 * distributions of states and manages the interactions between neighboring persons.
 * </p>
 * 
 * @see GymPerson
 * @see State
 */
public class Grid {

    private final GymPerson[][] persons;
    private final int rows;
    private final int cols;
    private static final Random random = new Random();

    /**
     * Constructs a new Grid with specified dimensions.
     *
     * @param rows number of rows in the grid
     * @param cols number of columns in the grid
     */
    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.persons = new GymPerson[rows][cols];
    }

    /**
     * Initializes the grid with percentages of different states.
     * Remaining cells are set to UNINTERESTED state.
     *
     * @param percentInterested percentage of INTERESTED persons
     * @param percentGymBro percentage of GYMBRO persons
     * @param percentCompetitive percentage of COMPETITIVE persons
     * @param percentAbandoned percentage of ABANDONED persons
     */
    public void initialize(double percentInterested, double percentGymBro, double percentCompetitive,
            double percentAbandoned) {
        List<int[]> coordinates = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                coordinates.add(new int[] { row, col });
            }
        }

        Collections.shuffle(coordinates, random);

        int totalCells = rows * cols;
        int numInterested = (int) (totalCells * percentInterested / 100);
        int numGymBro = (int) (totalCells * percentGymBro / 100);
        int numCompetitive = (int) (totalCells * percentCompetitive / 100);
        int numAbandoned = (int) (totalCells * percentAbandoned / 100);

        for (int i = 0; i < totalCells; i++) {
            int[] coord = coordinates.get(i);
            int row = coord[0];
            int col = coord[1];
            if (i < numInterested) {
                persons[row][col] = new GymPerson(State.INTERESTED);
            } else if (i < numInterested + numGymBro) {
                persons[row][col] = new GymPerson(State.GYMBRO);
            } else if (i < numInterested + numGymBro + numCompetitive) {
                persons[row][col] = new GymPerson(State.COMPETITIVE);
            } else if (i < numInterested + numGymBro + numCompetitive + numAbandoned) {
                persons[row][col] = new GymPerson(State.ABANDONED);
            } else {
                persons[row][col] = new GymPerson(State.UNINTERESED);
            }
        }

        initializeGrid();
    }

    /**
     * Sets up the neighborhood relationships between persons in the grid.
     */
    private void initializeGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                persons[row][col].setNeighbors((ArrayList<GymPerson>) createNeighbors(row, col));
            }
        }
    }

    /**
     * Creates a list of neighboring persons for a given position.
     *
     * @param row the row index of the person
     * @param col the column index of the person
     * @return List of neighboring GymPerson objects
     */
    private List<GymPerson> createNeighbors(int row, int col) {
        List<GymPerson> neighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0)
                    continue;
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                    neighbors.add(persons[newRow][newCol]);
                }
            }
        }
        return neighbors;
    }

    /**
     * Updates the states of all persons in the grid based on their neighbors' influence.
     * First calculates next states for all persons, then updates all states simultaneously.
     */
    public void updateGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                persons[row][col].updateState();
                persons[row][col].updateNextState();
            }
        }
    }

    /**
     * @return the number of rows in the grid
     */
    public int getRows() {
        return rows;
    }

    /**
     * @return the number of columns in the grid
     */
    public int getCols() {
        return cols;
    }

    /**
     * Gets the current state of a person at the specified position.
     *
     * @param row the row index of the person
     * @param col the column index of the person
     * @return the current State of the person
     */
    public State getState(int row, int col) {
        return persons[row][col].getState();
    }

    /**
     * @return the 2D array of all GymPerson objects in the grid
     */
    public GymPerson[][] getPersons() {
        return persons;
    }

    /**
     * Initializes the grid with specific numbers of each state.
     * Remaining cells are set to UNINTERESTED state.
     *
     * @param numInterested number of INTERESTED persons
     * @param numGymBro number of GYMBRO persons
     * @param numCompetitive number of COMPETITIVE persons
     * @param numAbandoned number of ABANDONED persons
     */
    public void initialize(int numInterested, int numGymBro, int numCompetitive, int numAbandoned) {
        List<int[]> coordinates = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                coordinates.add(new int[] { row, col });
            }
        }

        Collections.shuffle(coordinates, random);

        int totalCells = rows * cols;
        int remainingCells = totalCells - (numInterested + numGymBro + numCompetitive + numAbandoned);

        for (int i = 0; i < totalCells; i++) {
            int[] coord = coordinates.get(i);
            int row = coord[0];
            int col = coord[1];
            if (i < numInterested) {
                persons[row][col] = new GymPerson(State.INTERESTED);
            } else if (i < numInterested + numGymBro) {
                persons[row][col] = new GymPerson(State.GYMBRO);
            } else if (i < numInterested + numGymBro + numCompetitive) {
                persons[row][col] = new GymPerson(State.COMPETITIVE);
            } else if (i < numInterested + numGymBro + numCompetitive + numAbandoned) {
                persons[row][col] = new GymPerson(State.ABANDONED);
            } else if (i < numInterested + numGymBro + numCompetitive + numAbandoned + remainingCells) {
                persons[row][col] = new GymPerson(State.UNINTERESED);
            }
        }

        initializeGrid();
    }

    /**
     * Initializes the grid with a single person of specified state in the center,
     * surrounded by UNINTERESTED persons.
     *
     * @param centralState the state to set for the central person
     */
    public void initialize(State centralState) {
        int centerRow = rows / 2;
        int centerCol = cols / 2;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (row == centerRow && col == centerCol) {
                    persons[row][col] = new GymPerson(centralState);
                } else {
                    persons[row][col] = new GymPerson(State.UNINTERESED);
                }
            }
        }

        initializeGrid();
    }

    /**
     * @return the number of INTERESTED persons in the grid
     */
    public int getNumInterested() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (persons[row][col].getState() == State.INTERESTED) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return the number of GYMBRO persons in the grid
     */
    public int getNumGymBro() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (persons[row][col].getState() == State.GYMBRO) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return the number of COMPETITIVE persons in the grid
     */
    public int getNumCompetitive() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (persons[row][col].getState() == State.COMPETITIVE) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return the number of ABANDONED persons in the grid
     */
    public int getNumAbandoned() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (persons[row][col].getState() == State.ABANDONED) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * @return the number of UNINTERESTED persons in the grid
     */
    public int getNumUninterested() {
        int count = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (persons[row][col].getState() == State.UNINTERESED) {
                    count++;
                }
            }
        }
        return count;
    }
}