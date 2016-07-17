/*
 *  File: EventInterval.java 
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

import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

/**
 * Simple interval extension that holds a titel string.
 * 
 * @author kliem
 * @version $Id: EventInterval.java 801 2008-12-27 22:44:54Z kliem $
 */
public class EventInterval extends IntervalImpl {
    private String _title;

    public EventInterval(JaretDate from, JaretDate to) {
        super(from, to);
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    @Override
    public String toString() {
        return _title + ":" + super.toString();
    }

}
