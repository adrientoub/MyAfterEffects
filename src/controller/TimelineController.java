
package controller;

import de.jaret.util.ui.timebars.TimeBarMarker;
import filters.Filter;
import framework.Application;
import framework.Controller;
import manager.Timeline;
import model.TimelineModel;
import view.TimelineView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class TimelineController extends Controller<TimelineModel, TimelineView> {

  public TimelineController(final Application application) {
    super(application);

    this.on("timeline:clear", (data) -> this.clear());
  }

  public void clear() {
    this.model().clear();
  }

  public void setMarker(TimeBarMarker marker) {
    this.model().setMarker(marker);
  }

  public ArrayList<Filter> getFilters() {
    ArrayList<Filter> filters = new ArrayList<>();
    Iterator<HashMap.Entry<String, Filter>> it = this.model().getHashtable().entrySet().iterator();
    while (it.hasNext()) {
      HashMap.Entry<String, Filter> entry = it.next();
      filters.add(entry.getValue());
    }
    return filters;
  }
}
