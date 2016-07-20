package timeline.handler;

import app.MyAfterEffectsApp;
import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.model.ITimeBarChangeListener;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import framework.Application;
import view.TimelineView;

/**
 * Created by Damien on 20/07/2016.
 */
public class MarkerListener implements ITimeBarChangeListener {

    private Application app;

    public MarkerListener(Application application) {
        this.app = application;
    }

    public void intervalChangeCancelled(TimeBarRow row, Interval interval) {
    }

    public void intervalChangeStarted(TimeBarRow row, Interval interval) {
    }

    public void intervalChanged(TimeBarRow row, Interval interval, JaretDate oldBegin, JaretDate oldEnd) {
    }

    public void intervalIntermediateChange(TimeBarRow row, Interval interval, JaretDate oldBegin,
                                           JaretDate oldEnd) {
    }

    public void markerDragStarted(TimeBarMarker marker) {
    }

    public void markerDragStopped(TimeBarMarker marker) {
        ((MyAfterEffectsApp)app).getTimelineView().setMarker(marker);
    }

}
