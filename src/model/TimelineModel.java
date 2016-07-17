
package model;

import framework.Application;
import framework.Model;
import manager.Timeline;
import manager.Video;

import java.util.Collection;
import java.util.LinkedHashSet;

// Framework

public final class TimelineModel extends Model {
  private Collection<Timeline> timelines = new LinkedHashSet<>();

  public TimelineModel(final Application application) {
    super(application);
    this.on("video:new", (Video v) -> add(v));
  }

  public Timeline[] timelines() {
    return this.timelines.toArray(new Timeline[this.timelines.size()]);
  }

  public void add(Video v) {
    this.emit("timeline:new", new Timeline(v));
    /*if (timeline == null) {
      throw new NullPointerException();
    }

    this.timelines.add(timeline);
    this.emit("timelines:changed", timeline);*/
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
