/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package controller;

import framework.Application;
import framework.Controller;
import model.MenuModel;
import view.MenuView;

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
