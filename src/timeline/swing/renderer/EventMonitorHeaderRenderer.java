/*
 *  File: EventMonitorHeaderRenderer.java 
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
package timeline.swing.renderer;

import de.jaret.util.ui.timebars.model.TimeBarRowHeader;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import de.jaret.util.ui.timebars.swing.renderer.HeaderRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * A header renderer that renders the label always on top of the supplied space and doing a gradient fill of the
 * background.
 * 
 * @author kliem
 * @version $Id: EventMonitorHeaderRenderer.java 805 2008-12-31 10:01:13Z kliem $
 */
public class EventMonitorHeaderRenderer implements HeaderRenderer {
    private HeaderComponent _headerComponent;

    public EventMonitorHeaderRenderer() {
        _headerComponent = new HeaderComponent();
    }

    /**
     * {@inheritDoc}
     */
    public JComponent getHeaderRendererComponent(TimeBarViewer tbv, TimeBarRowHeader value, boolean isSelected) {
        _headerComponent.setSelected(isSelected);
        _headerComponent.setTimeBarRowHeader(value);
        _headerComponent.setTimeBarViewer(tbv);
        return _headerComponent;
    }

    /**
     * {@inheritDoc}
     */
    public int getWidth() {
        return 100;
    }

    /**
     * The Component to be painted as the header.
     * 
     * @author kliem
     * @version $Id: EventMonitorHeaderRenderer.java 805 2008-12-31 10:01:13Z kliem $
     */
    public class HeaderComponent extends JComponent {
        private final static int MARGIN = 3;

        private TimeBarRowHeader _header;
        private boolean _selected;
        private TimeBarViewer _tbv;

        public HeaderComponent() {
            setLayout(null);
            setOpaque(false);
        }

        public void setTimeBarViewer(TimeBarViewer tbv) {
            _tbv = tbv;
        }

        public void setTimeBarRowHeader(TimeBarRowHeader header) {
            _header = header;
        }

        public String getToolTipText() {
            return "<html><b>" + _header.getLabel() + "</b></html>";
        }

        public void setSelected(boolean selected) {
            _selected = selected;
        }

        /**
         * {@inheritDoc} The header renderer tries to get as much space as possible.
         */
        public Dimension getPreferredSize() {
            return new Dimension(100, 1000);
        }

        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            int height = getHeight();
            int width = getWidth();

            // Colors
            int startGray = 200;
            int endGray = 230;
            Color sColor = new Color(startGray, startGray, startGray);
            Color eColor = new Color(endGray, endGray, endGray);

            // background plain fill
            graphics.setColor(Color.RED);
            graphics.fillRect(0, 0, width, height);

            // background gradient fill
            Graphics2D g2 = (Graphics2D) graphics.create();
            GradientPaint gradientPaint = new GradientPaint(width / 2, 0, sColor, width / 2, height / 2, eColor, false);
            g2.setPaint(gradientPaint);
            g2.fillRect(0, 0, width, height);

            graphics.setColor(Color.BLACK);

            // row grid
            if (_tbv.getDrawRowGrid()) {
                graphics.drawLine(0, height - 1, width, height - 1);
            }

            // text
            Rectangle2D rect = graphics.getFontMetrics().getStringBounds("Q", graphics);
            int textHeight = (int) rect.getHeight();
            graphics.drawString(_header.getLabel(), MARGIN, MARGIN + textHeight);

            // selection
            if (_selected) {
                graphics.setColor(Color.BLUE);
                g2 = (Graphics2D) graphics;
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                graphics.fillRect(0, 0, width, height);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        }
    }
}
