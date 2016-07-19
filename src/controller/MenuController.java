
package controller;

import de.jaret.util.date.JaretDate;
import framework.Application;
import framework.Controller;
import manager.Media;
import model.MenuModel;
import model.TimelineModel;
import view.MenuView;
import view.TimelineView;

import java.util.ArrayList;

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
      case "Render":
        /* TODO Adrien */
        ArrayList<Media> medias = TimelineModel.GetMediasAtFrame(TimelineModel.getMarkerTime());
        for (Media m : medias)
          System.out.println(m.getName());
        break;
    }
  }
}
