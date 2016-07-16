/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package controller;

import framework.Application;
import framework.Controller;
import framework.Timeline;
import model.TimelineModel;
import view.TimelineView;

public final class TabbedController extends Controller<TimelineModel, TimelineView> {

  public TabbedController(final Application application) {
    super(application);
  }
}
