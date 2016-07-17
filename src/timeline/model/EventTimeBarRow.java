/*
 *  File: EventTimeBarRow.java 
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

import de.jaret.util.ui.timebars.model.DefaultTimeBarRowModel;
import de.jaret.util.ui.timebars.model.TimeBarRowHeader;

/**
 * Simple extension of the time bar row model holding an additional expanded status.
 * 
 * @author kliem
 * @version $Id: EventTimeBarRow.java 801 2008-12-27 22:44:54Z kliem $
 */
public class EventTimeBarRow extends DefaultTimeBarRowModel {
    protected boolean _expanded = false;

    public boolean isExpanded() {
        return _expanded;
    }

    public void setExpanded(boolean expanded) {
        _expanded = expanded;
    }

    public EventTimeBarRow(TimeBarRowHeader header) {
        super(header);
    }
}
