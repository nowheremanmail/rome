/*
 * Point.java
 *
 * Created on 8. februar 2007, 10:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.rometools.feed.module.georss.geometries;

/**
 * Point object, contains a position
 *
 * @author runaas
 */
public final class Point extends AbstractGeometricPrimitive {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Position pos;

    /** Creates a new instance of Point */
    public Point() {

    }

    public Point(final Position pos) {
        this.pos = pos;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        final Point retval = (Point) super.clone();
        if (pos != null) {
            retval.pos = (Position) pos.clone();
        }
        return retval;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        return getPosition().equals(((Point) obj).getPosition());
    }

    /**
     * Get the position
     *
     * @return the position
     */
    public Position getPosition() {
        if (pos == null) {
            pos = new Position();
        }
        return pos;
    }

    /**
     * Set the position
     *
     * @param pos the new position
     */
    public void setPosition(final Position pos) {
        this.pos = pos;
    }
}
