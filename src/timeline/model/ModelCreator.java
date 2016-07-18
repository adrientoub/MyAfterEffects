/*
 *  File: ModelCreator.java 
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
package timeline.model;

import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.*;

import java.util.Random;

/**
 * Simple model creator creating a hierachcial or a flat model and adds some events.
 * 
 * @author kliem
 * @version $Id: ModelCreator.java 801 2008-12-27 22:44:54Z kliem $
 */
public class ModelCreator {

    public static TimeBarModel createFlatModel() {

        JaretDate start = new JaretDate();
        start.setDateTime(1, 1, 2009, 0, 0, 0);
        JaretDate end = new JaretDate();
        end.setDateTime(1, 2, 2009, 0, 0, 0);

        DefaultTimeBarModel model = new DefaultTimeBarModel();
        
        DefaultRowHeader header = new DefaultRowHeader("Cat1");
        EventTimeBarRow row = new EventTimeBarRow(header);
        
        // kat1
        EventInterval interval = new EventInterval(start.copy(), end.copy());
        interval.setTitle("long running");
        row.addInterval(interval);
    
        interval = new EventInterval(start.copy().advanceHours(10), start.copy().advanceHours(15));
        interval.setTitle("short1.1");
        row.addInterval(interval);

        interval = new EventInterval(start.copy().advanceHours(17), start.copy().advanceHours(20));
        interval.setTitle("short1.2");
        row.addInterval(interval);

        model.addRow(row);
        
        // kat2
        row = new EventTimeBarRow(new DefaultRowHeader("Cat2"));
        interval = new EventInterval(start.copy().advanceHours(11), start.copy().advanceHours(14));
        interval.setTitle("short2.1");
        row.addInterval(interval);
        
        interval = new EventInterval(start.copy().advanceHours(16), start.copy().advanceHours(18));
        interval.setTitle("short2.2");
        row.addInterval(interval);
        
        model.addRow(row);

        // kat5
        row = new EventTimeBarRow(new DefaultRowHeader("Cat5"));

        for (int i = 0; i < 20; i++) {
            interval = new EventInterval(start.copy().advanceHours(i * 3), start.copy().advanceHours(i * 3 + 3));
            interval.setTitle("short5.1." + i);
            row.addInterval(interval);
        }

        model.addRow(row);
        

        return model;

    }

    
}
