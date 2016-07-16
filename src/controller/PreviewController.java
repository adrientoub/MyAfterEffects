/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package controller;

// General utilities

import framework.Application;
import framework.Controller;
import model.PreviewModel;
import view.PreviewView;

public final class PreviewController extends Controller<PreviewModel, PreviewView> {

  public PreviewController(final Application application) {
    super(application);
  }

  public void clear() {
    this.model().clear();
  }
}
