/*
 *  File: EventRenderer.java 
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
package timeline;

import de.jaret.util.date.Interval;
import de.jaret.util.swing.GraphicsHelper;
import de.jaret.util.ui.timebars.TimeBarViewerDelegate;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.TimeBarRenderer;
import timeline.EventInterval;

import javax.swing.*;
import java.awt.*;

/**
 * Renderer for events in the event monitoring example.
 * 
 * @author Peter Kliem
 * @version $Id: EventRenderer.java 869 2009-07-07 19:32:45Z kliem $
 */
public class EventRenderer implements TimeBarRenderer {
    EventRendererComponent _eventComponent;

    public EventRenderer() {
        _eventComponent = new EventRendererComponent();
    }

    public JComponent getTimeBarRendererComponent(TimeBarViewer tbv, Interval value, boolean isSelected,
                                                  boolean overlapping) {
        if (value instanceof EventInterval) {
            _eventComponent.setEventInterval((EventInterval) value);
            _eventComponent.setSelected(isSelected);
            return _eventComponent;
        } else {
            throw new RuntimeException("unsupported " + value.getClass().getName());
        }
    }

    /**
     * Rendering jcomponet for an event.
     * 
     * @author kliem
     * @version $Id: EventRenderer.java 869 2009-07-07 19:32:45Z kliem $
     */
    public class EventRendererComponent extends JComponent {
        EventInterval _interval;
        boolean _selected;
        
        
        public EventRendererComponent() {
            setLayout(null);
            setOpaque(false);
        }

        public void setEventInterval(EventInterval interval) {
            _interval = interval;
        }

        public String getToolTipText() {
            return "<html><b>" + _interval.getTitle() + "</b></html>";
        }

        public void setSelected(boolean selected) {
            _selected = selected;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int height = getHeight();
            int width = getWidth();

            int y = height / 3;
            int bheight = height / 3;
            int yEnd = y + bheight;

            // balken
            if (_selected) {
                g.setColor(Color.BLUE);
            } else {
                g.setColor(Color.YELLOW);
            }
            g.fillRect(0, y, width - 1, height / 3);
            // Rahmen
            g.setColor(Color.BLACK);
            g.drawRect(0, y, width - 1, height / 3);

            // Balkenbeschriftung
            GraphicsHelper.drawStringCenteredVCenter(g, _interval.getTitle(), 0, width, height / 2);
        }

        public boolean contains(int x, int y) {
            if (y >= getHeight() / 3 && y <= getHeight() / 3 + getHeight() / 3) {
                return true;
            } else {
                return false;
            }
        }
    }
    /**
     * {@inheritDoc} Simple default implementation.
     */
	public Rectangle getPreferredDrawingBounds(Rectangle intervalDrawingArea,
                                               TimeBarViewerDelegate delegate, Interval interval,
                                               boolean selected, boolean overlap) {
		return intervalDrawingArea;
	}

}
