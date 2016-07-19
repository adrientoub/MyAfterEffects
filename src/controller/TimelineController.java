
package controller;

import de.jaret.util.ui.timebars.TimeBarMarker;
import framework.Application;
import framework.Controller;
import manager.Timeline;
import model.TimelineModel;
import view.TimelineView;

public final class TimelineController extends Controller<TimelineModel, TimelineView> {

  public TimelineController(final Application application) {
    super(application);

    this.on("timeline:clear", (data) -> this.clear());
  }

  public void clear() {
    this.model().clear();
  }

  public void setMarker(TimeBarMarker marker) {
    this.model().setMarker(marker);
  }
}
