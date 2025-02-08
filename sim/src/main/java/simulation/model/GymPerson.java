package simulation.model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Represents a person in a gym motivation simulation.
 * <p>
 * This class models an individual's interest in gym activities and their state
 * transitions
 * based on social influence from neighbors. Each person has a current state,
 * interest level,
 * and random factors that affect their motivation and state changes.
 * </p>
 * 
 * @see State
 * @see Person
 */
public class GymPerson implements Person {

    private State state;
    private State nextState;
    private final ArrayList<GymPerson> neighbors;
    private double motivation = 0.0;
    private double discipline = 0.0;

    private final Random random = new Random();

    /**
     * Creates a new GymPerson with the specified initial state.
     * 
     * @param state the initial State of the person
     */
    public GymPerson(State state) {
        this.neighbors = new ArrayList<>();
        this.state = state;
        this.nextState = state;
        setInitialMotivation();
    }

    private void setInitialMotivation() {
        switch (this.state) {
            case UNINTERESED -> motivation = random.nextDouble() * 30;
            case INTERESTED -> motivation = 50 + random.nextDouble() * 20;
            case GYMBRO -> motivation = 60 + random.nextDouble() * 10;
            case COMPETITIVE -> motivation = 80 + random.nextDouble() * 10;
            case ABANDONED -> motivation = random.nextDouble() * 30;
        }
    }

    /**
     * Adds a neighboring person who can influence this person's state.
     * 
     * @param neighbor the GymPerson to add as a neighbor
     */
    @Override
    public void addNeighbor(Object neighbor) {
        this.neighbors.add((GymPerson) neighbor);
    }

    /**
     * @return the current State of this person
     */
    @Override
    public State getState() {
        return this.state;
    }

    /**
     * @return the next State this person will transition to
     */
    @Override
    public State getNextState() {
        return this.nextState;
    }

    /**
     * Updates the current state to the previously calculated next state.
     */
    @Override
    public void updateState() {
        this.state = this.nextState;
    }

    /**
     * Calculates and sets the next state based on neighbors' influence and random
     * factors.
     */
    @Override
    public void updateNextState() {
        this.nextState = calculateNextState();
    }

    /**
     * Calculates the next state based on current interest level and neighbors'
     * states.
     * This method follows a model similar to the SIR model with added entropy.
     * 
     * @return the State to transition to next
     */
    private State calculateNextState() {
        // Environmental factors
        double competitivenessFactor = getGymEnvironmentCompetitiveness(); // 0-1
        double socialSupport = calculateSocialSupport(); // 0-1

        // Update motivation and discipline
        updateMotivationAndDiscipline(competitivenessFactor, socialSupport);

        // State transition probabilities
        double stateChangeProb = random.nextDouble();

        switch (this.state) {
            case UNINTERESED -> {
                if (stateChangeProb < 0.25) {
                    if (motivation > 40 && random.nextDouble() <= getPercentNeighbors(State.INTERESTED)+random.nextGaussian()) {
                        return State.INTERESTED;
                    }
                }
            }

            case INTERESTED -> {
                if (stateChangeProb < 0.6) {
                    if ((motivation < 30 ) || random.nextDouble() <= (getPercentNeighbors(State.UNINTERESED)-getPercentNeighbors(State.GYMBRO)+getPercentNeighbors(State.INTERESTED)+getPercentNeighbors(State.COMPETITIVE)+getPercentNeighbors(State.ABANDONED))) {
                        return State.UNINTERESED;
                    }
                    if (motivation > 70 && discipline > 50
                            && random.nextDouble()-0.15 <= (getPercentNeighbors(State.GYMBRO)+getPercentNeighbors(State.INTERESTED)+getPercentNeighbors(State.COMPETITIVE))) {
                        if(random.nextBoolean()) {
                            return State.GYMBRO;
                        } else {
                            return State.COMPETITIVE;
                        }
                    }
                }
            }

            case GYMBRO -> {
                if (stateChangeProb < 0.6) {
                    if ((motivation < 40) || random.nextDouble()-0.2 <= (getPercentNeighbors(State.INTERESTED)+getPercentNeighbors(State.COMPETITIVE)-(getPercentNeighbors(State.GYMBRO)))) {
                        return State.INTERESTED;
                    }
                    if (motivation > 80 && discipline > 80
                            && random.nextDouble() <= (getPercentNeighbors(State.COMPETITIVE)+getPercentNeighbors(State.GYMBRO))) {
                        return State.COMPETITIVE;
                    }
                    if (random.nextDouble() <= getPercentNeighbors(State.ABANDONED)+0.25*getPercentNeighbors(State.UNINTERESED)+getPercentNeighbors(State.COMPETITIVE)-0.5*getPercentNeighbors(State.GYMBRO)-0.25*getPercentNeighbors(State.INTERESTED)) {
                        return State.ABANDONED;
                    }
                }
            }

            case COMPETITIVE -> {
                if (stateChangeProb < 0.95) {
                    if (motivation > 50 && random.nextDouble()-0.25 <= (getPercentNeighbors(State.GYMBRO)-1.5*getPercentNeighbors(State.INTERESTED)+getPercentNeighbors(State.ABANDONED)-getPercentNeighbors(State.COMPETITIVE))) {
                        return State.GYMBRO;
                    }
                    if (motivation < 40 && random.nextDouble()-0.1 <= (getPercentNeighbors(State.ABANDONED)+2*getPercentNeighbors(State.COMPETITIVE))) {
                        return State.ABANDONED;
                    }
                }
            }

            case ABANDONED -> {
                if (stateChangeProb < 0.1) {
                    if (motivation > 70 && random.nextDouble() <= 0.1*(getPercentNeighbors(State.INTERESTED)+getPercentNeighbors(State.GYMBRO)-getPercentNeighbors(State.ABANDONED)-getPercentNeighbors(State.COMPETITIVE))) {
                        return State.UNINTERESED;
                    }
                }
            }
        }
        return this.state;
    }

    private void updateMotivationAndDiscipline(double competitivenessFactor, double socialSupport) {
        // Base fluctuation
        double motivationChange = (random.nextGaussian() * 5);
        double disciplineChange = (random.nextGaussian() * 3);

        // Social comparison effect
        if (competitivenessFactor > 0.7) {
            // High competitiveness can demotivate some people
            if (discipline < 50) {
                motivationChange -= 10 * (1 - discipline / 100);
            } else {
                motivationChange += 5 * (discipline / 100);
            }
        }

        // Social support boost
        motivationChange += socialSupport * 10;
        disciplineChange += socialSupport * 5;

        // State-specific modifiers
        switch (this.state) {
            case UNINTERESED -> {
                motivationChange -= 0.5;
                disciplineChange -= 0;
            }
            case INTERESTED -> {
                motivationChange += 2;
                disciplineChange += 1;
            }
            case GYMBRO -> {
                motivationChange += 2;
                disciplineChange += 3;
            }
            case COMPETITIVE -> {
                motivationChange -= 5;
                disciplineChange += 5;
            }
            case ABANDONED -> {
                motivationChange += 1;
                disciplineChange -= 1;
            }
        }

        // Apply changes with bounds
        motivation = Math.max(0, Math.min(100, motivation + motivationChange));
        discipline = Math.max(0, Math.min(100, discipline + disciplineChange));
    }

    private double calculateSocialSupport() {
        double totalSupport = 0.0;
        for (GymPerson neighbor : neighbors) {
            switch (neighbor.getState()) {
                case GYMBRO -> totalSupport += 1;
                case COMPETITIVE -> totalSupport += 0.5;
                case INTERESTED -> totalSupport += 0.75;
                case UNINTERESED, ABANDONED -> totalSupport -= 0.1;
            }
        }
        return totalSupport / neighbors.size();
    }

    private double getGymEnvironmentCompetitiveness() {

        long competitivePeople = getNumNeighbors(State.COMPETITIVE);
        long gymBros = getNumNeighbors(State.GYMBRO);

        double competitiveRatio = (competitivePeople + (gymBros * 0.5)) / (double) NEIGHBORHOOD_SIZE;

        double randomFactor = 0.8 + (random.nextDouble() * 0.4);

        return Math.min(1.0, Math.max(0.0, competitiveRatio * randomFactor));
    }

    public void setNeighbors(ArrayList<GymPerson> neighbors) {
        this.neighbors.clear();
        this.neighbors.addAll(neighbors);
    }

    public long getNumNeighbors(State state) {
        long count = neighbors.stream().filter(p -> p.getState() == (State) state).count();
        return count;
    }

    public double getPercentNeighbors(State state) {
        long count = getNumNeighbors(state);
        double percent = count / NEIGHBORHOOD_SIZE;
        return percent;
    }
}