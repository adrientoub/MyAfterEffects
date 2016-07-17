
package model;

import framework.Application;
import framework.Model;
import framework.Timeline;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

// Framework

public final class TimelineModel extends Model {
  private Collection<Timeline> timelines = new LinkedHashSet<>();

  public TimelineModel(final Application application) {
    super(application);
    this.on("menu:new", (File f) -> add(new Timeline(f.getName())));
  }

  public Timeline[] timelines() {
    return this.timelines.toArray(new Timeline[this.timelines.size()]);
  }

  public void add(final Timeline timeline) {
    if (timeline == null) {
      throw new NullPointerException();
    }

    this.timelines.add(timeline);
    this.emit("timelines:changed", timeline);
  }

  public void remove(final Timeline timeline) {
    if (timeline == null) {
      throw new NullPointerException();
    }

    this.timelines.remove(timeline);
    this.emit("timelines:changed", timeline);
  }

  public void clear() {
    if (this.timelines.isEmpty()) {
      return;
    }

    this.timelines.clear();
    this.emit("timelines:changed");
  }
}
