package timeline.handler;

import app.MyAfterEffectsApp;
import de.jaret.util.date.Interval;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;
import de.jaret.util.ui.timebars.model.TimeBarRow;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import framework.Application;
import timeline.EventInterval;
import view.TimelineView;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Damien on 20/07/2016.
 */
public class DropListener implements DropTargetListener {

  private Application app;
  private TimeBarViewer _tbv;
  private DefaultTimeBarRowModel beforeDragRow;

  public DropListener(Application application) {
    this.app = application;
    this._tbv = ((MyAfterEffectsApp)this.app).getTimelineView().getTimeBarViewer();
  }

  public void setOldRow(DefaultTimeBarRowModel row) {
    this.beforeDragRow = row;
  }

  private EventInterval correctDates(EventInterval interval, JaretDate curDate) {
    int secs = interval.getSeconds();
    return new EventInterval(curDate.copy(), curDate.copy().advanceSeconds(secs), interval.getMedia());
  }

  @Override
  public void dragEnter(DropTargetDragEvent dtde) {
  }

  @Override
  public void dragOver(DropTargetDragEvent dtde) {
    try {
      EventInterval interval = (EventInterval)(dtde.getTransferable().getTransferData(DataFlavor.imageFlavor));

      JaretDate curDate = _tbv.dateForXY(dtde.getLocation().x, dtde.getLocation().y);
      EventInterval ghost = correctDates(interval, curDate);

    TimeBarRow overRow = _tbv.getRowForXY(dtde.getLocation().x, dtde.getLocation().y);
    if (overRow != null) {
      _tbv.highlightRow(overRow);

      // tell the timebar viewer
      ArrayList<Interval> list = new ArrayList<>();
      ArrayList<Integer> listOffset = new ArrayList<>();
      list.add(ghost);
      listOffset.add(0);
      // there could be a check whether dropping is allowed at the current location
      List<Interval> intervals = _tbv.getDelegate().getIntervalsAt(dtde.getLocation().x, dtde.getLocation().y);
      if (intervals.size() == 0 && _tbv.getRowForXY(dtde.getLocation().x, dtde.getLocation().y) != null) {// dropAllowed(_draggedJobs, overRow)) {
        dtde.acceptDrag(DnDConstants.ACTION_MOVE);
        _tbv.setGhostIntervals(list, listOffset);
        _tbv.setGhostOrigin(dtde.getLocation().x, dtde.getLocation().y);
      } else {
        dtde.rejectDrag();
        _tbv.setGhostIntervals(null, null);
      }
    } else {
      _tbv.deHighlightRow();
    }
    } catch (UnsupportedFlavorException | IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void dropActionChanged(DropTargetDragEvent dtde) {

  }

  @Override
  public void dragExit(DropTargetEvent dte) {
    _tbv.deHighlightRow();
  }

  @Override
  public void drop(DropTargetDropEvent dtde) {
    try {
      EventInterval interval = (EventInterval)(dtde.getTransferable().getTransferData(DataFlavor.imageFlavor));
      System.out.println("Dropped!");
      Point p = dtde.getLocation();

      DefaultTimeBarRowModel timeBarRow = (DefaultTimeBarRowModel)(_tbv.getRowForXY(p.x, p.y));
      JaretDate curDate = _tbv.dateForXY(dtde.getLocation().x, dtde.getLocation().y);
      timeBarRow.addInterval(correctDates(interval, curDate));
      beforeDragRow.remInterval(interval);
      _tbv.setGhostIntervals(null, null);
      dtde.dropComplete(true);
      dtde.getDropTargetContext().dropComplete(true);
      this.app.radio().emit("timeline:changed", ((MyAfterEffectsApp)this.app).getTimelineView().getMarkerTime());

    } catch (UnsupportedFlavorException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
