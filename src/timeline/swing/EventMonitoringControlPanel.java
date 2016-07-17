/*
 *  File: EventMonitoringControlPanel.java 
 *  Copyright (c) 2004-2008  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package timeline.swing;

import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.BoxTimeScaleRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultGridRenderer;
import de.jaret.util.ui.timebars.swing.renderer.DefaultTimeScaleRenderer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Control panel for the event monitoring example.
 * 
 * @author Peter Kliem
 * @version $Id: EventMonitoringControlPanel.java 974 2009-12-22 22:15:29Z kliem $
 */
public class EventMonitoringControlPanel extends JPanel {
    TimeBarViewer _viewer;
    JSlider _timeScaleSlider;
    JSlider _rowHeigthSlider;
    TimeBarMarkerImpl _marker;

    public EventMonitoringControlPanel(TimeBarViewer viewer, TimeBarMarkerImpl marker, int initalSecondsDisplayed) {
        _viewer = viewer;
        _marker = marker;
        this.setPreferredSize(new Dimension(1000, 100));
        setLayout(new FlowLayout());
        createControls(initalSecondsDisplayed);
    }

    /**
     * 
     */
    private void createControls(int initialSeconds) {
        final double min = 1; // minimum value for seconds displayed
        final double max = 3 * 365 * 24 * 60 * 60; // max nummber of seconds displayed (3 years in seconds)
        final double slidermax = 1000; // slider maximum (does not really matter)

        _timeScaleSlider = new JSlider(0, (int) slidermax);

        _timeScaleSlider.setPreferredSize(new Dimension(_timeScaleSlider.getPreferredSize().width * 4, _timeScaleSlider
                .getPreferredSize().height));
        add(_timeScaleSlider);

        final double b = 1.0 / 100.0; // additional factor to reduce the absolut values in the exponent
        final double faktor = (min - max) / (1 - Math.pow(2, slidermax * b)); // factor for the exp function
        final double c = (min - faktor);

        int initialSliderVal = calcInitialSliderVal(c, b, faktor, initialSeconds);
        _timeScaleSlider.setValue((int) (slidermax- initialSliderVal));


        _timeScaleSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                final double x = slidermax - (double) _timeScaleSlider.getValue(); // reverse x
                double seconds = c + faktor * Math.pow(2, x * b); // calculate the seconds to display
                if (_viewer.isDisplayed(_marker.getDate())) {
                    _viewer.setSecondsDisplayed((int) seconds, _marker.getDate());
                }
            }
        });

        _rowHeigthSlider = new JSlider(10, 300);
        _rowHeigthSlider.setValue(_viewer.getRowHeight());
        _rowHeigthSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                _viewer.setRowHeight(_rowHeigthSlider.getValue());
            }
        });
        add(_rowHeigthSlider);

        final JCheckBox markerInDiagramAreaCheck = new JCheckBox("Allow Marker drag in DiagramArea");
        markerInDiagramAreaCheck.setSelected(_viewer.getMarkerDraggingInDiagramArea());
        markerInDiagramAreaCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                _viewer.setMarkerDraggingInDiagramArea(markerInDiagramAreaCheck.isSelected());
            }
        });
        add(markerInDiagramAreaCheck);

        BoxTimeScaleRenderer btscr = new BoxTimeScaleRenderer();
        _viewer.setTimeScaleRenderer(btscr);
        if (_viewer.getGridRenderer() instanceof DefaultGridRenderer) {
            ((DefaultGridRenderer) _viewer.getGridRenderer()).setTickProvider(btscr);
        }

        _viewer.getRegionRectEnable();
    }

    private int calcInitialSliderVal(double c, double b, double faktor, int seconds) {

        double x = 1 / b * log2((seconds - c) / faktor);

        return (int) x;
    }

    private double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

}