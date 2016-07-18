
package view;

import controller.TimelineController;
import de.jaret.util.date.Interval;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.mod.DefaultIntervalModificator;
import de.jaret.util.ui.timebars.model.*;
import de.jaret.util.ui.timebars.strategy.IIntervalSelectionStrategy;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultHierarchyRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTitleRenderer;
import framework.Application;
import manager.Timeline;
import framework.View;
import model.TimelineModel;
import timeline.model.*;
import timeline.swing.*;

import timeline.swing.renderer.EventMonitorHeaderRenderer;
import timeline.swing.renderer.EventRenderer;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public final class TimelineView extends View<TimelineModel, TimelineController> {

  private final static boolean HIERARCHICAL = false;

  TimeBarViewer _tbv;
  TimeBarMarkerImpl _tm;
  TimeBarModel flatModel;

  public TimelineView(final Application application) {
    super(application);

    this.model(new TimelineModel(application));
    this.controller(new TimelineController(application));

    this.on("timeline:new", (Timeline t) -> addRow(t.getVideo().getName()));
  }

  public void addRow(String name) {
    JaretDate start = new JaretDate();
    start.setDateTime(1, 1, 2009, 0, 0, 0);
    JaretDate end = new JaretDate();
    end.setDateTime(1, 2, 2009, 0, 0, 0);
    DefaultRowHeader header = new DefaultRowHeader(name);
    EventTimeBarRow row = new EventTimeBarRow(header);

    // kat1
    EventInterval interval = new EventInterval(start.copy(), end.copy());
    interval.setTitle(name);
    row.addInterval(interval);

    ((DefaultTimeBarModel)flatModel).addRow(row);
  }

  public JPanel render() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setSize(1400, 600);

    HierarchicalTimeBarModel hierarchicalModel = ModelCreator.createHierarchicalModel();
    flatModel = ModelCreator.createFlatModel();

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
        System.out.println("Marker drag started "+marker);
      }

      public void markerDragStopped(TimeBarMarker marker) {
        System.out.println("Marker drag stopped "+marker);
      }

    });


    // do not allow row selections
    _tbv.getSelectionModel().setRowSelectionAllowed(false);

    // register additional renderer
    _tbv.registerTimeBarRenderer(EventInterval.class, new EventRenderer());

    // add a marker
    _tm = new TimeBarMarkerImpl(true, _tbv.getModel().getMinDate().copy().advanceHours(3));
    _tm.setDescription("Timebarmarker");
    _tbv.addMarker(_tm);

    // do not show the root node
    _tbv.setHideRoot(true);

    // add a popup menu for EventIntervals
    Action action = new AbstractAction("IntervalAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    JPopupMenu pop = new JPopupMenu("Operations");
    pop.add(action);
    _tbv.registerPopupMenu(EventInterval.class, pop);

    // add a popup menu for the body
    final Action bodyaction = new AbstractAction("BodyAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    pop = new JPopupMenu("Operations");
    pop.add(bodyaction);
    pop.add(new RunMarkerAction(_tbv));

    // add the rem selection action
    pop.add(new ResetRegionSelectionAction(_tbv));

    _tbv.setBodyContextMenu(pop);

    // add a popup menu for the hierarchy
    action = new AbstractAction("HierarchyAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    pop = new JPopupMenu("Operations");
    pop.add(action);
    _tbv.setHierarchyContextMenu(pop);

    // add a popup menu for the header
    action = new AbstractAction("HeaderAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    pop = new JPopupMenu("Operations");
    pop.add(action);
    _tbv.setHeaderContextMenu(pop);

    // add a popup menu for the time scale
    action = new AbstractAction("TimeScaleAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    pop = new JPopupMenu("Operations");
    pop.add(action);
    _tbv.setTimeScaleContextMenu(pop);

    // add a popup menu for the title area
    action = new AbstractAction("TitleAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    pop = new JPopupMenu("Operations");
    pop.add(action);
    _tbv.setTitleContextMenu(pop);

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
    new Thread() {
      @Override
      public void run() {
        startChanging(flatModel);
      }
    }.start();

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

  class ResetRegionSelectionAction extends AbstractAction {
    TimeBarViewer _tbv;
    public ResetRegionSelectionAction(TimeBarViewer tbv) {
      super("Remove selection");
      _tbv = tbv;
    }
    public void actionPerformed(ActionEvent e) {
      _tbv.clearRegionRect();
    }

  }
  class RunMarkerAction extends AbstractAction {
    TimeBarViewer _tbv;
    public RunMarkerAction(TimeBarViewer tbv) {
      super("Run marker");
      _tbv = tbv;
    }
    public void actionPerformed(ActionEvent e) {
      _tm.setDate(_tbv.getModel().getMinDate().copy());

      final Timer timer = new Timer(40, null);
      ActionListener al = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
          _tm.setDate(_tm.getDate().copy().advanceMillis(40));
          if (_tm.getDate().compareTo(_tbv.getModel().getMaxDate())>0) {
            timer.stop();
          }
        }

      };

      timer.addActionListener(al);
      timer.setRepeats(true);
      timer.setDelay(40);
      timer.start();
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

  private static void startChanging(TimeBarModel model) {
    long delay = 3000;
    for (int r = 0; r < model.getRowCount(); r++) {
      TimeBarRow row = model.getRow(r);
      Iterator it = row.getIntervals().iterator();
      while (it.hasNext()) {
        Interval interval = (Interval) it.next();
        double minutes = interval.getEnd().diffMinutes(interval.getBegin());
        JaretDate date = interval.getEnd().copy();
        date.backMinutes(minutes / 4);
        interval.setEnd(date);
        System.out.println("Changed interval " + interval);
        try {
          Thread.sleep(delay / 2);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
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
}
