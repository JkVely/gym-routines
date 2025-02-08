package simulation.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import simulation.model.State;

public class GymPersonView extends Rectangle{
    private static final Color UNINTERESED_COLOR = Color.web("#060270");
    private static final Color INTERESTED_COLOR = Color.web("#FE9900");
    private static final Color GYMBRO_COLOR = Color.web("#E4080A");
    private static final Color COMPETITIVE_COLOR = Color.web("#7DDA58");
    private static final Color ABANDONED_COLOR = Color.web("#98F5F9");

    public GymPersonView(double width, double height, State state) {
        super(width, height);
        updateColor(state);
    }

    public final void updateColor(State state) {
        switch (state) {
            case UNINTERESED -> setFill(UNINTERESED_COLOR);
            case INTERESTED -> setFill(INTERESTED_COLOR);
            case GYMBRO -> setFill(GYMBRO_COLOR);
            case COMPETITIVE -> setFill(COMPETITIVE_COLOR);
            case ABANDONED -> setFill(ABANDONED_COLOR);
        }
    }
}
