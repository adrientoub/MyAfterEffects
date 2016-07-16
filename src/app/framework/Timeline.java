package app.framework;

/**
 * Created by Damien on 16/07/2016.
 */
public class Timeline {

    private final String description;

    public Timeline(final String description) {
        if (description == null) {
            throw new NullPointerException();
        }

        this.description = description;
    }

    public String description() {
        return this.description;
    }

    @Override
    public String toString() {
        return this.description;
    }

}
