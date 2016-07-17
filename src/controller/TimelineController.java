
package controller;

import framework.Application;
import framework.Controller;
import framework.Timeline;
import model.TimelineModel;
import view.TimelineView;

import java.io.File;

public final class TimelineController extends Controller<TimelineModel, TimelineView> {

  public TimelineController(final Application application) {
    super(application);

    this.on("timeline:clear", (data) -> this.clear());
  }

  public void create(final Timeline timeline) throws NullPointerException {
    if (timeline == null) {
      throw new NullPointerException();
    }

    // Update the model with the newly created todo item.
    this.model().add(timeline);
  }

  public void clear() {
    this.model().clear();
  }
}
