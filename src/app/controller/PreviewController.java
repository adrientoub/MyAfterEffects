/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.controller;

// General utilities

import app.framework.Application;
import app.framework.Controller;
import app.model.PreviewModel;
import app.view.PreviewView;

public final class PreviewController extends Controller<PreviewModel, PreviewView> {

  public PreviewController(final Application application) {
    super(application);
  }

  public void clear() {
    this.model().clear();
  }
}
