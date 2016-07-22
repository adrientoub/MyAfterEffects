
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
import filters.*;
import framework.Application;
import manager.Media;
import manager.Sequence;
import manager.Timeline;
import framework.View;
import manager.Video;
import model.TimelineModel;
import timeline.EventInterval;
import timeline.EventMonitoringControlPanel;
import timeline.EventMonitorHeaderRenderer;
import timeline.EventRenderer;
import timeline.handler.MarkerListener;
import timeline.handler.DropListener;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public final class TimelineView extends View<TimelineModel, TimelineController> {

  TimeBarViewer _tbv;
  TimeBarMarkerImpl _tm;
  TimeBarModel flatModel;
  private boolean stopped = true;
  EventInterval selected = null;
  /* Used fro Drag and Drop */
  private DropListener dropListener;
  private Point position;

  public TimelineView(final Application application) {
    super(application);

    this.model(new TimelineModel(application));
    this.controller(new TimelineController(application));

    this.on("timeline:new", (Timeline t) -> addRow(t.getMedia()));
  }

  public TimeBarViewer getTimeBarViewer() {
    return _tbv;
  }

  private void addRow(Media media) {
    JaretDate start = new JaretDate();
    start.setDateTime(0, 0, 0, 0, 0, 0);

    DefaultRowHeader header = new DefaultRowHeader(media.getName());
    DefaultTimeBarRowModel row = new DefaultTimeBarRowModel(header);

    EventInterval interval = new EventInterval(start.copy(), start.copy().advanceMillis(media.getDuration()), media);
    row.addInterval(interval);

    ((DefaultTimeBarModel)flatModel).addRow(row);
    _tbv.setModel(flatModel);
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
    _tbv.addTimeBarChangeListener(new MarkerListener(this.application()));

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

    // add a popup menu for EventIntervals
    JPopupMenu menu = new JPopupMenu("Operations");
    JMenu submenu = addFiltersMenu();

    _tbv.addMouseListener(new MouseAdapter() {
      @Override
      public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        position = e.getPoint();
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
    menu.add(new createSequenceAction(_tbv));
    menu.add(new cleanRectAction(_tbv));
    _tbv.setBodyContextMenu(menu);

    // add a popup menu for the header
    Action action = new AbstractAction("HeaderAction") {
      public void actionPerformed(ActionEvent e) {
        System.out.println("run " + position);

        TimeBarRow lol =  _tbv.getRowForXY(position.x, position.y);
        _tbv.getDelegate().rowRemoved(_tbv.getModel(), lol);
      }
    };
    menu = new JPopupMenu("Operations");
    menu.add(action);
    _tbv.setHeaderContextMenu(menu);

    // add a popup menu for the time scale
    action = new AbstractAction("Move marker here") {
      public void actionPerformed(ActionEvent e) {
        _tm.setDate(_tbv.getDelegate().dateForCoord(position.x, position.y));
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
            DnDConstants.ACTION_MOVE, dgl);

    DropTarget d = new DropTarget();
    dropListener = new DropListener(this.application());
    try {
      d.addDropTargetListener(dropListener);
    } catch (TooManyListenersException e) {
      e.printStackTrace();
    }
    _tbv.setDropTarget(d);

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
          if (f instanceof ChromaKey) {
            int threshold = 3;
            JFrame colors = new JFrame("Pick a color");
            colors.setLayout(new BoxLayout(colors.getContentPane(), BoxLayout.PAGE_AXIS));
            JColorChooser tcc = new JColorChooser(Color.green);
            colors.add(tcc, BorderLayout.PAGE_START);

            JButton select = new JButton("Select");

            JSpinner thresholdSpinner = new JSpinner(new SpinnerNumberModel(3, 0, 255, 1));

            select.addActionListener(e1 -> {
              System.out.println(thresholdSpinner.getValue());
              selected.addFilter(new ChromaKey(tcc.getColor(), (int)thresholdSpinner.getValue()));
              TimelineView.this.emit("filter:applied", TimelineView.this.getMarkerTime());
              colors.dispatchEvent(new WindowEvent(colors, WindowEvent.WINDOW_CLOSING));
            });

            colors.add(thresholdSpinner, BoxLayout.Y_AXIS);
            colors.add(new JLabel("Threshold:"), BoxLayout.Y_AXIS);
            colors.add(select, BoxLayout.Y_AXIS);

            colors.pack();
            colors.setVisible(true);
          }
          else {
            String filterString = ((JMenuItem) e.getSource()).getText();
            selected.addFilter(filters.stream().filter(choice -> choice.getClass().getSimpleName().equals(filterString)).findFirst().get());
            TimelineView.this.emit("filter:applied", TimelineView.this.getMarkerTime());
          }
        }
      });
    }
    return submenu;
  }

  public void setMarker(TimeBarMarker marker) {
    this.controller().setMarker(marker);
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

  class createSequenceAction extends AbstractAction {
    public createSequenceAction(TimeBarViewer tbv) {
      super("Create sequence");
      _tbv = tbv;
    }

    /* TODO : cast to Video fail if media is a sequence */
    public void actionPerformed(ActionEvent event) {
      TBRect rect = _tbv.getRegionRect();
      if (rect != null) {
        /* Should only contains at most one */
        ArrayList<Interval> intervals = (ArrayList<Interval>) rect.startRow.getIntervals(rect.startDate, rect.endDate);
        if (intervals.stream().count() > 0 && intervals.stream().findFirst().isPresent()) {
          EventInterval interval = (EventInterval)intervals.stream().findFirst().get();
          int firstFrame, lastFrame;

          /* First : [ |---]---| */
          if (interval.getBegin().diffMilliSeconds(rect.startDate) > 0) {
            firstFrame = interval.getMedia().getFrameFromMilliseconds(rect.endDate.diffMilliSeconds(interval.getBegin()));
            lastFrame = interval.getMedia().getFrameFromMilliseconds(interval.getEnd().diffMilliSeconds(interval.getBegin()));

            System.out.println(firstFrame);
            System.out.println(lastFrame);

            EventInterval newMiddleInterval = new EventInterval(rect.endDate, interval.getEnd(),
                    new Sequence((Media) interval.getMedia().clone(), firstFrame, lastFrame));

            /* Add new interval to the timeline */
            ((DefaultTimeBarRowModel)rect.startRow).addInterval(newMiddleInterval);

            interval.setEnd(rect.endDate);
            System.out.println("[ ---]---");
          }
          /* Second : |---[---| ] */
          else if (rect.endDate.diffMilliSeconds(interval.getEnd()) > 0) {
            firstFrame = interval.getMedia().getFrameFromMilliseconds(rect.startDate.diffMilliSeconds(interval.getBegin()));
            lastFrame = interval.getMedia().getFrameFromMilliseconds(interval.getEnd().diffMilliSeconds(interval.getBegin()));

            System.out.println(firstFrame);
            System.out.println(lastFrame);

            EventInterval newMiddleInterval = new EventInterval(rect.startDate, interval.getEnd(),
                    new Sequence((Video)interval.getMedia().clone(), firstFrame, lastFrame));

            /* Add new interval to the timeline */
            ((DefaultTimeBarRowModel)rect.startRow).addInterval(newMiddleInterval);

            interval.setEnd(rect.startDate);
            System.out.println("---[--- ]");
          }
          /* Third : |-[----]-| */
          else {
            System.out.println("---[---]---");
          /* Will be messy if rectangle is before start of video */
            firstFrame = interval.getMedia().getFrameFromMilliseconds(rect.startDate.diffMilliSeconds(interval.getBegin()));
            lastFrame = interval.getMedia().getFrameFromMilliseconds(rect.endDate.diffMilliSeconds(interval.getBegin()));

            System.out.println(firstFrame);
            System.out.println(lastFrame);

            EventInterval newMiddleInterval = new EventInterval(rect.startDate, rect.endDate,
                    new Sequence((Video)interval.getMedia().clone(), firstFrame, lastFrame));

            firstFrame = interval.getMedia().getFrameFromMilliseconds(rect.endDate.diffMilliSeconds(interval.getBegin()));
            lastFrame = interval.getMedia().getFrameFromMilliseconds(interval.getEnd().diffMilliSeconds(interval.getBegin()));

            System.out.println(firstFrame);
            System.out.println(lastFrame);

            EventInterval newEndInterval = new EventInterval(rect.endDate, interval.getEnd(),
                    new Sequence((Video)interval.getMedia().clone(),
                            firstFrame,
                            lastFrame));

          /* Add new interval to the timeline */
            ((DefaultTimeBarRowModel) rect.startRow).addInterval(newMiddleInterval);
            ((DefaultTimeBarRowModel) rect.startRow).addInterval(newEndInterval);

          /* Split the first interval */
            interval.setEnd(rect.startDate);

          }
          /* Clear the rectangle selection */
          _tbv.clearRegionRect();
        }
      }
    }
  }

  class cleanRectAction extends AbstractAction {
    public cleanRectAction(TimeBarViewer tbv) {
      super("Clean rectangle selection");
      _tbv = tbv;
    }
    public void actionPerformed(ActionEvent event) {
      _tbv.clearRegionRect();
    }
  }

  class TimeBarViewerDragGestureListener implements DragGestureListener {

    public void dragGestureRecognized(DragGestureEvent e) {
      Component c = e.getComponent();
      System.out.println(e.getDragOrigin());

      boolean markerDragging = _tbv.getDelegate().isMarkerDraggingInProgress();
      if (markerDragging) {
        return;
      }

      if(_tbv.getDelegate().getRegionRect() != null)
        return;

      List<Interval> intervals = _tbv.getDelegate().getIntervalsAt(e.getDragOrigin().x, e.getDragOrigin().y);
      if (intervals.size() > 0) {
        Interval interval = intervals.get(0);
        e.startDrag(null, new Transferable() {
          @Override
          public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[0];
          }

          @Override
          public boolean isDataFlavorSupported(DataFlavor flavor) {
            return false;
          }

          @Override
          public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            return interval;
          }
        });
        dropListener.setOldRow((DefaultTimeBarRowModel)_tbv.getRowForXY(e.getDragOrigin().x, e.getDragOrigin().y));
        TimelineView.this.selected = (EventInterval)interval;
        //beforeDragRow.remInterval(interval);
        return;
      }
    }
  }
}
