/*
 *  File: EventMonitoringExample.java 
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
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

import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.TimeBarMarkerImpl;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import timeline.model.CollectingTimeBarNode;
import timeline.model.EventInterval;
import timeline.model.EventTimeBarRow;
import timeline.model.ModelCreator;

/**
 * Example showing events (non modificable).
 * 
 * @author Peter Kliem
 * @version $Id: EventMonitoringExample.java 1073 2010-11-22 21:25:33Z kliem $
 */
public class EventMonitoringExample {
    TimeBarViewer _tbv;
    TimeBarMarkerImpl _tm;
    
    private final static boolean HIERARCHICAL = false;

    public static void main(String[] args) {
        /*EventMonitoringExample example = new EventMonitoringExample();
        example.run();*/
    }

    public EventMonitoringExample() {
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
    

    
    
    


    
    
}
