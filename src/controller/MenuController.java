/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package controller;

import framework.Application;
import framework.Controller;
import framework.Timeline;
import model.MenuModel;
import model.TimelineModel;
import view.MenuView;
import view.TimelineView;

public final class MenuController extends Controller<MenuModel, MenuView> {

  public MenuController(final Application application) {
    super(application);
  }

  public void handle(String name) {
    switch (name) {
      case "Exit":
        this.model().exit(0);
        break;
      case "New":
        try {
          this.model().newFile();
        } catch (IllegalArgumentException e) {
          System.err.println(e.getMessage());
        }
        break;
    }
  }
}
