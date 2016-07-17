/*
 *  File: CollectingTimeBarRow.java 
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

import de.jaret.util.date.Interval;
import de.jaret.util.ui.timebars.model.AbstractTimeBarRowModel;
import de.jaret.util.ui.timebars.model.TimeBarNode;
import de.jaret.util.ui.timebars.model.TimeBarNodeListener;
import de.jaret.util.ui.timebars.model.TimeBarRowHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * An implementation of a TimeBarNode that adds up all children. The node itself can carry it's own intervals.
 * 
 * @author Peter Kliem
 * @version $Id: CollectingTimeBarNode.java 801 2008-12-27 22:44:54Z kliem $
 */
public class CollectingTimeBarNode extends AbstractTimeBarRowModel implements TimeBarNode {
    /** children. */
    protected List<TimeBarNode> _children = new ArrayList<TimeBarNode>();

    protected List<Interval> _ownIntervals = new ArrayList<Interval>();

    /** level of the node. */
    protected int _level = -1;

    /** NodeListeners regsitered. */
    protected List<TimeBarNodeListener> _nodeListeners;

    /** true if all intervals of the children shall be collected. */
    protected boolean _collectChildIntervals = true;

    /**
     * Constructor.
     * 
     * @param header required header
     */
    public CollectingTimeBarNode(TimeBarRowHeader header) {
        super(header);
    }

    public void addInterval(Interval interval) {
        _ownIntervals.add(interval);
        updateMinMax();
    }

    /**
     * {@inheritDoc}
     */
    public List<TimeBarNode> getChildren() {
        return _children;
    }

    /**
     * {@inheritDoc}
     */
    public void addNode(TimeBarNode node) {
        node.setLevel(getLevel() + 1); // set the level of the added node to be under the current node
        _children.add(node);
        fireNodeAdded(node);
    }

    /**
     * {@inheritDoc}
     */
    public void remNode(TimeBarNode node) {
        if (_children.remove(node)) {
            fireNodeRemoved(node);
        }
    }

    /**
     * {@inheritDoc}
     */
    public int getLevel() {
        return _level;
    }

    /**
     * {@inheritDoc}
     */
    public void setLevel(int level) {
        _level = level;
        if (_children != null) {
            for (TimeBarNode node : _children) {
                node.setLevel(level + 1);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void addTimeBarNodeListener(TimeBarNodeListener tbnl) {
        if (_nodeListeners == null) {
            _nodeListeners = new Vector<TimeBarNodeListener>();
        }
        _nodeListeners.add(tbnl);
        super.addTimeBarRowListener(tbnl);
    }

    /**
     * {@inheritDoc}
     */
    public void removeTimeBarNodeListener(TimeBarNodeListener tbnl) {
        if (_nodeListeners == null) {
            return;
        }
        _nodeListeners.remove(tbnl);
        super.remTimeBarRowListener(tbnl);
    }

    /**
     * Inform listeners about a new child.
     * 
     * @param newNode new child node
     */
    protected void fireNodeAdded(TimeBarNode newNode) {
        if (_nodeListeners != null) {
            for (TimeBarNodeListener listener : _nodeListeners) {
                listener.nodeAdded(this, newNode);
            }
        }
    }

    /**
     * Inform listeners about a removed child.
     * 
     * @param removedNode removed child node
     */
    protected void fireNodeRemoved(TimeBarNode removedNode) {
        if (_nodeListeners != null) {
            for (TimeBarNodeListener listener : _nodeListeners) {
                listener.nodeRemoved(this, removedNode);
            }
        }
    }

    @Override
    public List<Interval> getIntervals() {
        List<Interval> result = new ArrayList<Interval>();
        result.addAll(_ownIntervals);
        if (_collectChildIntervals) {
            for (TimeBarNode child : _children) {
                result.addAll(child.getIntervals());
            }
        }

        return result;
    }

    public boolean getCollectChildIntervals() {
        return _collectChildIntervals;
    }

    public void setCollectChildIntervals(boolean collectChildIntervals) {
        if (_collectChildIntervals != collectChildIntervals) {
            _collectChildIntervals = collectChildIntervals;
            fireRowDataChanged();
        }
    }

}
