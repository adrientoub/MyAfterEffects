/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package model;

import framework.Application;
import framework.Model;
import framework.Timeline;

import java.util.Collection;
import java.util.LinkedHashSet;

// Framework

public final class TimelineModel extends Model {
  private Collection<Timeline> timelines = new LinkedHashSet<>();

  public TimelineModel(final Application application) {
    super(application);
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
