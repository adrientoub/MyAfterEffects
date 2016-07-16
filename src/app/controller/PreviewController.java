/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package app.controller;

// General utilities

import app.framework.Application;
import app.framework.Controller;
import app.model.PreviewModel;
import app.view.PreviewView;

// Framework
// Models
// Views

/**
 * Example controller using the MVC micro-framework presented in
 * {@link app.framework}.
 */
public final class PreviewController extends Controller<PreviewModel, PreviewView> {
  /**
   * Initialize a new {@link PreviewController} instance for the specified
   * {@link Application}.
   *
   * @param application The {@link Application} that the {@link PreviewController}
   *                    is associated with.
   */
  public PreviewController(final Application application) {
    super(application);
  }

  public void clear() {
    this.model().clear();
  }
}
