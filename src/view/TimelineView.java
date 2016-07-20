
package view;

import controller.TimelineController;
import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.model.*;
import de.jaret.util.ui.timebars.strategy.IIntervalSelectionStrategy;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTitleRenderer;
import filters.Binarize;
import filters.ChromaKey;
import filters.Filter;
import filters.Grayscale;
import framework.Application;
import manager.Media;
import manager.Timeline;
import framework.View;
import model.TimelineModel;
import timeline.model.*;
import timeline.swing.*;
import timeline.swing.renderer.EventMonitorHeaderRenderer;
import timeline.swing.renderer.EventRenderer;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public final class TimelineView extends View<TimelineModel, TimelineController> {

  TimeBarViewer _tbv;
  TimeBarMarkerImpl _tm;
  TimeBarModel flatModel;
  private boolean stopped = true;
  EventInterval selected = null;

  public TimelineView(final Application application) {
    super(application);

    this.model(new TimelineModel(application));
    this.controller(new TimelineController(application));

    this.on("timeline:new", (Timeline t) -> addRow(t.getMedia()));
  }

  private void addRow(Media media) {
    JaretDate start = new JaretDate();
    start.setDateTime(0, 0, 0, 0, 0, 0);

    DefaultRowHeader header = new DefaultRowHeader(media.getName());
    EventTimeBarRow row = new EventTimeBarRow(header);

    EventInterval interval = new EventInterval(start.copy(), start.copy().advanceMillis(media.getDuration()), media);
    interval.setTitle(media.getName());
    row.addInterval(interval);

    ((DefaultTimeBarModel)flatModel).addRow(row);
    _tbv.setModel(flatModel);
    _tbv.setHierarchyWidth(0);
    _tbv.setTimeScalePosition(TimeBarViewer.TIMESCALE_POSITION_TOP);
    _tbv.setYAxisWidth(100);
    _tm.setDate(start);
  }

  public void setMarkerTime(JaretDate date) {
    ((TimeBarMarkerImpl)_tbv.getMarkers().get(0)).setDate(date);
  }

  public JPanel render() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setSize(1400, 600);

    flatModel = new DefaultTimeBarModel();

    _tbv = new TimeBarViewer();

    _tbv.setModel(flatModel);
    _tbv.setHierarchyWidth(0);
    _tbv.setTimeScalePosition(TimeBarViewer.TIMESCALE_POSITION_TOP);
    _tbv.setYAxisWidth(100);
    panel.add(_tbv, BorderLayout.CENTER);

    // allow marker grabbing in the diagram area
    _tbv.setMarkerDraggingInDiagramArea(true);

    // enable region selection
    _tbv.setRegionRectEnable(true);

    // draw row grid
    _tbv.setDrawRowGrid(true);

    // setup header renderer
    _tbv.setHeaderRenderer(new EventMonitorHeaderRenderer());


    // set a name for the viewer and setup the default title renderer
    _tbv.setName("MyAfterEffects");
    _tbv.setTitleRenderer(new DefaultTitleRenderer());


    // selection strategy: shortest first
    _tbv.getDelegate().setIntervalSelectionStrategy(new IIntervalSelectionStrategy() {
      public Interval selectInterval(List<Interval> intervals) {
        Interval result = null;
        for (Interval interval : intervals) {
          if (result == null || interval.getSeconds() < result.getSeconds()) {
            result = interval;
          }
        }
        return result;
      }
    });

      // in general draw overlapping
      _tbv.setDrawOverlapping(true);

      // allow different row heights
      _tbv.getTimeBarViewState().setUseVariableRowHeights(true);

      // add a double click listener for checking on the header
      _tbv.addMouseListener(new MouseAdapter() {

        public void mouseClicked(MouseEvent e) {
          if (e.getClickCount() == 2) {
            Point origin = e.getPoint();
            if (_tbv.getDelegate().getYAxisRect().contains(origin)) {
              TimeBarRow row = _tbv.getRowForXY(origin.x, origin.y);
              if (row != null) {
                  System.out.println("Index in " + (origin.y / _tbv.getRowHeight() - 1));
                if (row instanceof EventTimeBarRow) {
                  EventTimeBarRow erow = (EventTimeBarRow) row;
                  if (!erow.isExpanded()) {
                    // expand
                    _tbv.getTimeBarViewState().setDrawOverlapping(row, false);
                    _tbv.getTimeBarViewState().setRowHeight(row, calculateRowHeight(_tbv.getDelegate(), _tbv.getTimeBarViewState(), row));
                    erow.setExpanded(true);
                  } else {
                    // fold
                    _tbv.getTimeBarViewState().setDrawOverlapping(row, true);
                    _tbv.getTimeBarViewState().setRowHeight(row, _tbv.getTimeBarViewState().getDefaultRowHeight());
                    erow.setExpanded(false);
                  }
                }
              }
            }
          }
        }

        /**
         * Calculate the optimal row height
         * @param delegate
         * @param timeBarViewState
         * @param row
         * @return
         */
        public int calculateRowHeight(TimeBarViewerDelegate delegate,
                                      ITimeBarViewState timeBarViewState, TimeBarRow row) {
          int maxOverlap = timeBarViewState.getDefaultRowHeight();
          int height = delegate.getMaxOverlapCount(row) * maxOverlap;
          return height;
        }



      });

    // change listener
    _tbv.addTimeBarChangeListener(new ITimeBarChangeListener() {

      public void intervalChangeCancelled(TimeBarRow row, Interval interval) {
        System.out.println("CHANGE CANCELLED " + row + " " + interval);
      }

      public void intervalChangeStarted(TimeBarRow row, Interval interval) {
        System.out.println("CHANGE STARTED " + row + " " + interval);
      }

      public void intervalChanged(TimeBarRow row, Interval interval, JaretDate oldBegin, JaretDate oldEnd) {
        System.out.println("CHANGE DONE " + row + " " + interval);
      }

      public void intervalIntermediateChange(TimeBarRow row, Interval interval, JaretDate oldBegin,
                                             JaretDate oldEnd) {
        System.out.println("CHANGE INTERMEDIATE " + row + " " + interval);
      }

      public void markerDragStarted(TimeBarMarker marker) {
        TimelineView.this.controller().setMarker(marker);
      }

      public void markerDragStopped(TimeBarMarker marker) {
        TimelineView.this.controller().setMarker(marker);
      }

    });


    // do not allow row selections
    _tbv.getSelectionModel().setRowSelectionAllowed(false);

    // register additional renderer
    _tbv.registerTimeBarRenderer(EventInterval.class, new EventRenderer());

    // add a marker
    _tm = new TimeBarMarkerImpl(true, _tbv.getModel().getMinDate().copy());
    _tm.setDescription("Timebarmarker");
    _tbv.addMarker(_tm);

    // do not show the root node
    _tbv.setHideRoot(true);

    /* TODO CLEAN DIRTY CODE */
    // add a popup menu for EventIntervals
    Action action = new AbstractAction("IntervalAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    JPopupMenu menu = new JPopupMenu("Operations");
    JMenu submenu = new JMenu("Filters");

    HashMap<String, Filter> hashtable = new HashMap<>();
    ArrayList<Filter> filters = new ArrayList<>();
    hashtable.put("Binarize", new Binarize());
    hashtable.put("ChromaKey", new ChromaKey(Color.green));
    hashtable.put("Grayscale", new Grayscale());

    filters.add(new Binarize());
    filters.add(new ChromaKey(Color.green));
    filters.add(new Grayscale());

    for (Filter f : filters) {
      submenu.add(f.getClass().getSimpleName());

      JMenuItem item = submenu.getItem(submenu.getItemCount() - 1);
      item.addActionListener(e -> {
        if (selected != null) {
          String filterString = ((JMenuItem)e.getSource()).getText();
          System.out.println(filterString);
          selected.addFilter(hashtable.get(filterString));
        }
      });
    }


    _tbv.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        List<Interval> intervals = _tbv.getDelegate().getIntervalsAt(e.getX(), e.getY());
        if (intervals.size() > 0)
           selected = (EventInterval) intervals.get(0);
        else
          selected = null;
      }
    });
    menu.add(submenu);

    _tbv.registerPopupMenu(EventInterval.class, menu);

    // add a popup menu for the body
    final Action bodyaction = new AbstractAction("BodyAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    menu = new JPopupMenu("Operations");
    menu.add(bodyaction);
    menu.add(new RunMarkerAction(_tbv));
    menu.add(new PauseMarkerAction(_tbv));

    _tbv.setBodyContextMenu(menu);

    // add a popup menu for the hierarchy
    action = new AbstractAction("HierarchyAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    menu = new JPopupMenu("Operations");
    menu.add(action);
    _tbv.setHierarchyContextMenu(menu);

    // add a popup menu for the header
    action = new AbstractAction("HeaderAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    menu = new JPopupMenu("Operations");
    menu.add(action);
    _tbv.setHeaderContextMenu(menu);

    // add a popup menu for the time scale
    action = new AbstractAction("TimeScaleAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    menu = new JPopupMenu("Operations");
    menu.add(action);
    _tbv.setTimeScaleContextMenu(menu);

    // add a popup menu for the title area
    action = new AbstractAction("TitleAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    menu = new JPopupMenu("Operations");
    menu.add(action);
    _tbv.setTitleContextMenu(menu);

    // add dnd support
    DragSource dragSource = DragSource.getDefaultDragSource();
    DragGestureListener dgl = new TimeBarViewerDragGestureListener();
    DragGestureRecognizer dgr = dragSource.createDefaultDragGestureRecognizer(_tbv._diagram,
            DnDConstants.ACTION_COPY, dgl);


    // add the control panel
    EventMonitoringControlPanel cp = new EventMonitoringControlPanel(_tbv, _tm, 100); // TODO
    panel.add(cp, BorderLayout.SOUTH);

    // go!
    panel.setVisible(true);
    //panel.add
    new Thread() {
      @Override
      public void run() {
        //startChanging(flatModel);
      }
    }.start();

      this.model().setTbv(_tbv);
    return panel;
  }

  public void setEndDate(TimeBarViewer tbv, JaretDate endDate) {
    int secondsDisplayed = tbv.getSecondsDisplayed();
    JaretDate startDate = endDate.copy().advanceSeconds(-secondsDisplayed);
    tbv.setStartDate(startDate);
  }


  boolean isInRange(JaretDate date, double min, double max) {
    int secondsDisplayed = _tbv.getSecondsDisplayed();
    JaretDate minDate = _tbv.getStartDate().copy().advanceSeconds(min*secondsDisplayed);
    JaretDate maxDate = _tbv.getStartDate().copy().advanceSeconds(max*secondsDisplayed);
    return minDate.compareTo(date)>0 && maxDate.compareTo(date)<0;
  }

  class RunMarkerAction extends AbstractAction {
    TimeBarViewer _tbv;
    public RunMarkerAction(TimeBarViewer tbv) {
      super("Play video");
      _tbv = tbv;
    }
    public void actionPerformed(ActionEvent e) {

      final Timer timer = new Timer(40, null);
      ActionListener al = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          JaretDate deltaDate = _tm.getDate().copy().advanceMillis(100);
          _tm.setDate(deltaDate);
          TimelineView.this.emit("marker:changed", deltaDate);
          if (_tm.getDate().compareTo(_tbv.getModel().getMaxDate())> 0 || TimelineView.this.stopped) {
            TimelineView.this.stopped = true;
            timer.stop();
          }
        }

      };

      timer.addActionListener(al);
      timer.setRepeats(true);
      timer.setDelay(40);
      TimelineView.this.stopped = false;
      timer.start();
    }

  }

  class PauseMarkerAction extends AbstractAction {
    public PauseMarkerAction(TimeBarViewer tbv) {
      super("Pause video");
      _tbv = tbv;
    }
    public void actionPerformed(ActionEvent e) {
      TimelineView.this.stopped = true;
    }
  }

  class TimeBarViewerDragGestureListener implements DragGestureListener {
    public void dragGestureRecognized(DragGestureEvent e) {
      Component c = e.getComponent();
      System.out.println("component " + c);
      System.out.println(e.getDragOrigin());

      boolean markerDragging = _tbv.getDelegate().isMarkerDraggingInProgress();
      if (markerDragging) {
        return;
      }

      List<Interval> intervals = _tbv.getDelegate().getIntervalsAt(e.getDragOrigin().x, e.getDragOrigin().y);
      System.out.println("Drag");
      if (intervals.size() > 0) {
        Interval interval = intervals.get(0);
        e.startDrag(null, new StringSelection("Drag " + ((EventInterval) interval).getTitle()));
        return;
      }
      Point origin = e.getDragOrigin();
      if (_tbv.getDelegate().getYAxisRect().contains(origin)) {
        TimeBarRow row = _tbv.getRowForXY(origin.x, origin.y);
        if (row != null) {
          e.startDrag(null, new StringSelection("Drag ROW " + row));
        }
      }

    }
  }
}
