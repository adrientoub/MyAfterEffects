/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.controller;

// General utilities

// Framework
import app.framework.Application;
import app.framework.Controller;

// Models
import app.framework.Timeline;
import app.model.TimelineModel;

// Views
import app.view.TimelineView;

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
