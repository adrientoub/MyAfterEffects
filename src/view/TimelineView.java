/**
 * Copyright (C) 2015 Kasper Kronborg Isager.
 */
package view;

// General utilities

import controller.TimelineController;
import de.jaret.util.date.Interval;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.*;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import framework.Application;
import framework.Timeline;
import framework.View;
import model.TimelineModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.*;

public final class TimelineView extends View<TimelineModel, TimelineController> {

  JList<Timeline> todosList;
  public static final java.util.List _headerList = new ArrayList();

  public TimelineView(final Application application) {
    super(application);

    this.model(new TimelineModel(application));
    this.controller(new TimelineController(application));

    todosList = new JList<>(this.model().timelines());
    this.on("timelines:changed", (Timeline T) -> todosList.setListData(this.model().timelines()));
  }

  public JPanel render() {
    JPanel panel = new JPanel(new BorderLayout());
    //f.setSize(300, 330);
    //f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    TimeBarModel model = createRandomModel(10, 120, 50);
    TimeBarViewer tbv = new TimeBarViewer(model);

    panel.add(tbv, BorderLayout.CENTER);

    // model will be changed by the main thread
    //startChanging(model);
    return panel;
    /*JPanel viewPanel = new JPanel(new BorderLayout());
    viewPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

    JScrollPane todosPane = new JScrollPane(todosList);
    viewPanel.add(todosPane, BorderLayout.CENTER);

    JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    actionsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
    viewPanel.add(actionsPanel, BorderLayout.SOUTH);

    return viewPanel;*/
  }

  private static void startChanging(TimeBarModel model) {
    long delay = 800;
    for (int r = 0; r < model.getRowCount(); r++) {
      TimeBarRow row = model.getRow(r);
      double sum = getIntervalSum(row);
      DefaultRowHeader header = (DefaultRowHeader) _headerList.get(r);
      header.setLabel("R" + r + "(" + sum + ")");
      System.out.println("Changed header " + r);
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    for (int r = 0; r < model.getRowCount(); r++) {
      TimeBarRow row = model.getRow(r);
      Iterator it = row.getIntervals().iterator();
      while (it.hasNext()) {
        Interval interval = (Interval) it.next();
        double minutes = interval.getEnd().diffMinutes(interval.getBegin());
        JaretDate date = interval.getEnd().copy();
        date.backMinutes(minutes / 4);
        interval.setEnd(date);
        double sum = getIntervalSum(row);
        DefaultRowHeader header = (DefaultRowHeader) _headerList.get(r);
        header.setLabel("R" + r + "(" + sum + ")");
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

  public static TimeBarModel createRandomModel(int rows, int averageLengthInMinutes, int countPerRow) {
    DefaultTimeBarModel model = new DefaultTimeBarModel();

    for (int row = 0; row < rows; row++) {
      DefaultRowHeader header = new DefaultRowHeader("r" + row);
      _headerList.add(header);
      DefaultTimeBarRowModel tbr = new DefaultTimeBarRowModel(header);
      JaretDate date = new JaretDate();
      for (int i = 0; i < countPerRow; i++) {
        IntervalImpl interval = new IntervalImpl();
        int length = averageLengthInMinutes / 2 + (int) (Math.random() * (double) averageLengthInMinutes);
        interval.setBegin(date.copy());
        date.advanceMinutes(length);
        interval.setEnd(date.copy());

        tbr.addInterval(interval);

        int pause = (int) (Math.random() * (double) averageLengthInMinutes / 5);
        date.advanceMinutes(pause);
      }
      model.addRow(tbr);
    }

    System.out.println("Created " + (rows * countPerRow) + " Intervals");

    return model;
  }
}
