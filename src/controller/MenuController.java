
package controller;

import de.jaret.util.misc.Pair;
import framework.Application;
import framework.Controller;
import manager.Media;
import model.MenuModel;
import model.TimelineModel;
import view.MenuView;

import java.io.IOException;
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
        } catch (IllegalArgumentException | IOException e) {
          System.err.println(e.getMessage());
        }
        break;
      case "Render":
        /* TODO Adrien */
        ArrayList<Pair<Long, Media>> medias = TimelineModel.getMediasAtDate(TimelineModel.getMarkerTime());
        for (Pair<Long, Media> p : medias)
          System.out.println(p.getLeft() + " " + p.getRight().getName());
        break;
    }
  }
}
