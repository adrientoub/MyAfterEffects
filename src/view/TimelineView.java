
package view;

import controller.TimelineController;
import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarker;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.model.*;
import de.jaret.util.ui.timebars.strategy.IIntervalSelectionStrategy;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTitleRenderer;
import filters.*;
import framework.Application;
import manager.Media;
import manager.Timeline;
import framework.View;
import model.TimelineModel;
import timeline.EventInterval;
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
    DefaultTimeBarRowModel row = new DefaultTimeBarRowModel(header);

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

  public JaretDate getMarkerTime() {
    return ((TimeBarMarkerImpl)_tbv.getMarkers().get(0)).getDate();
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
    _tbv.getSelectionModel().setRowSelectionAllowed(true);

    // register additional renderer
    _tbv.registerTimeBarRenderer(EventInterval.class, new EventRenderer());

    // add a marker
    _tm = new TimeBarMarkerImpl(true, _tbv.getModel().getMinDate().copy());
    _tm.setDescription("Timebarmarker");
    _tbv.addMarker(_tm);

    // do not show the root node
    _tbv.setHideRoot(true);

    // add a popup menu for EventIntervals
    JPopupMenu menu = new JPopupMenu("Operations");
    JMenu submenu = addFiltersMenu();

    DropTarget d = new DropTarget();
    try {
      d.addDropTargetListener(new DropTargetListener() {
        @Override
        public void dragEnter(DropTargetDragEvent dtde) {

        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {

        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {

        }

        @Override
        public void dragExit(DropTargetEvent dte) {

        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
          System.out.println("Dropped!");
        }
      });
    } catch (TooManyListenersException e) {
      e.printStackTrace();
    }
    _tbv.setDropTarget(d);
    _tbv.addMouseListener(new MouseAdapter() {

      @Override
      public void mouseDragged(MouseEvent e) {
        System.out.println("Loooldragged");
        super.mouseDragged(e);
      }

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
    menu = new JPopupMenu("Operations");
    menu.add(new RunMarkerAction(_tbv));
    menu.add(new PauseMarkerAction(_tbv));
    _tbv.setBodyContextMenu(menu);

    // add a popup menu for the header
    Action action = new AbstractAction("HeaderAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + getValue(NAME));
      }
    };
    menu = new JPopupMenu("Operations");
    menu.add(action);
    _tbv.setHeaderContextMenu(menu);

    // add a popup menu for the time scale
    action = new AbstractAction("Move marker here") {
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
    this.model().setTbv(_tbv);
    return panel;
  }

  private JMenu addFiltersMenu() {
    JMenu submenu = new JMenu("Filters");

    ArrayList<Filter> filters = this.controller().getFilters();

    for (Filter f : filters) {
      submenu.add(f.getClass().getSimpleName());

      JMenuItem item = submenu.getItem(submenu.getItemCount() - 1);
      item.addActionListener(e -> {
        if (selected != null) {
          String filterString = ((JMenuItem)e.getSource()).getText();
          System.out.println(filterString);
          selected.addFilter(filters.stream().filter(choice -> choice.getClass().getSimpleName().equals(filterString)).findFirst().get());
          TimelineView.this.emit("filter:applied", TimelineView.this.getMarkerTime());
        }
      });
    }
    return submenu;
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
