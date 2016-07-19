package model;

import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.misc.Pair;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import framework.Application;
import framework.Model;
import manager.Media;
import manager.Timeline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.function.Consumer;

// Framework

public final class TimelineModel extends Model {
  private static Collection<Timeline> timelines = new LinkedHashSet<>();
  private static TimeBarMarker marker = new TimeBarMarkerImpl(true, null);
  static TimeBarViewer _tbv;

  public TimelineModel(final Application application) {
    super(application);
    this.on("media:new", this::add);
  }

  public static Timeline[] timelines() {
    return timelines.toArray(new Timeline[timelines.size()]);
  }

  public static ArrayList<Pair<Long, Media>> GetMediasAtFrame(JaretDate date) {
    ArrayList<Pair<Long, Media>> pairs = new ArrayList<>();
    TimeBarModel model = getTbv().getModel();

    for (int r = 0; r < model.getRowCount(); r++) {
      System.out.println("in row");
      TimeBarRow row = model.getRow(r);
      ArrayList<Interval> intervals = (ArrayList<Interval>)row.getIntervals(date);

      /* If that video is present on that date, add it to the list */
      if (!intervals.isEmpty()) {
        Interval i = intervals.get(0);
        Media m = timelines()[r].getMedia();
        pairs.add(new Pair<>(date.diffMilliSeconds(i.getBegin()), m));
      }
    }
    return pairs;
  }

  private static void forEachInterval(TimeBarModel model, Consumer<Interval> consumer) {
    for (int r = 0; r < model.getRowCount(); r++) {
      TimeBarRow row = model.getRow(r);
      Iterator it = row.getIntervals().iterator();
      while (it.hasNext()) {

        Interval interval = (Interval) it.next();
        consumer.accept(interval);
      }
    }
  }

  private static double getIntervalSum(TimeBarRow row) {
    double result = 0;
    Iterator it = row.getIntervals().iterator();
    while (it.hasNext()) {
      Interval interval = (Interval) it.next();
      result += interval.getEnd().diffMinutes(interval.getBegin());
    }

    return result;
  }

  public void add(Media media) {
    Timeline t = new Timeline(media);
    timelines.add(t);
    this.emit("timeline:new", t);
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

  public void setMarker(TimeBarMarker m) {
    this.emit("marker:changed", m.getDate());
    marker = m;
  }

  public static JaretDate getMarkerTime() {
    return marker.getDate();
  }

    public static TimeBarViewer getTbv() {
        return _tbv;
    }

    public void setTbv(TimeBarViewer _tbv) {
        this._tbv = _tbv;
    }
}
