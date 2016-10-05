package model;

import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.misc.Pair;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import filters.*;
import filters.Transparency;
import framework.Application;
import framework.Model;
import manager.Media;
import manager.Timeline;
import timeline.EventInterval;

import java.awt.*;
import java.util.*;

// Framework

public final class TimelineModel extends Model {
  private static Collection<Timeline> timelines = new LinkedHashSet<>();
  private static TimeBarMarker marker = new TimeBarMarkerImpl(true, new JaretDate(0, 0, 0, 0, 0, 0));
  static TimeBarViewer _tbv;
  HashMap<String, Filter> hashtable;

  public TimelineModel(final Application application) {
    super(application);
    this.on("media:new", this::add);

    initFilters();
  }

  private void initFilters() {
    hashtable = new HashMap<>();
    hashtable.put("Binarize", new Binarize());
    hashtable.put("Otsu Binarize", new OtsuBinarize());
    hashtable.put("ChromaKey", new ChromaKey(Color.green, 3));
    hashtable.put("Grayscale", new Grayscale());
    hashtable.put("LowPass", new LowPass());
    hashtable.put("Cartoonify", new Cartoonify());
    hashtable.put("Detect Borders", new BorderDetection());
    hashtable.put("Sepia", new Sepia());
    hashtable.put("Sharpen", new Sharpen());
    hashtable.put("Transparency", new Transparency());
    hashtable.put("Quantize", new QuantizeColors());
  }

  public static ArrayList<Pair<Long, Media>> getMediasAtDate(JaretDate date) {
    ArrayList<Pair<Long, Media>> pairs = new ArrayList<>();
    TimeBarModel model = getTbv().getModel();

    for (int r = 0; r < model.getRowCount(); r++) {
      TimeBarRow row = model.getRow(r);
      ArrayList<Interval> intervals = (ArrayList<Interval>)row.getIntervals(date);

      /* If that video is present on that date, add it to the list */
      if (!intervals.isEmpty()) {
        EventInterval i = (EventInterval)intervals.get(0);
        Media m = i.getMedia();
        pairs.add(new Pair<>(date.diffMilliSeconds(i.getBegin()), m));
      }
    }
    return pairs;
  }

  public static JaretDate getTimelineEnd() {
    JaretDate last = new JaretDate(0, 0, 0, 0, 0, 0);
    TimeBarModel model = getTbv().getModel();

    for (int r = 0; r < model.getRowCount(); r++) {
      TimeBarRow row = model.getRow(r);
      ArrayList<Interval> intervals = (ArrayList<Interval>)row.getIntervals();

      /* If that video is present on that date, add it to the list */
      if (!intervals.isEmpty()) {
        for (Interval i : intervals)
        if (i.getEnd().diffMilliSeconds(last) > 0)
          last = i.getEnd();
      }
    }
    return last;
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

  public HashMap<String, Filter> getHashtable() {
    return hashtable;
  }
}
