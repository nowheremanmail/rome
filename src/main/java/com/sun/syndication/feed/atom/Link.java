/*
 * Copyright 2004 Sun Microsystems, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.sun.syndication.feed.atom;

import java.io.Serializable;

import com.sun.syndication.feed.impl.ObjectBean;

/**
 * Bean for link elements of Atom feeds.
 * <p>
 * 
 * @author Alejandro Abdelnur
 * @author Dave Johnson (updated for Atom 1.0)
 */
public class Link implements Cloneable, Serializable {

    private final ObjectBean _objBean;

    private String _href;
    private String _hrefResolved;
    private String _rel = "alternate";
    private String _type;
    private String _hreflang;
    private String _title;
    private long _length;

    /**
     * Default constructor. All properties are set to <b>null</b>.
     * <p>
     * 
     */
    public Link() {
        this._objBean = new ObjectBean(this.getClass(), this);
    }

    /**
     * Creates a deep 'bean' clone of the object.
     * <p>
     * 
     * @return a clone of the object.
     * @throws CloneNotSupportedException thrown if an element of the object
     *             cannot be cloned.
     * 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return this._objBean.clone();
    }

    /**
     * Indicates whether some other object is "equal to" this one as defined by
     * the Object equals() method.
     * <p>
     * 
     * @param other he reference object with which to compare.
     * @return <b>true</b> if 'this' object is equal to the 'other' object.
     * 
     */
    @Override
    public boolean equals(final Object other) {
        return this._objBean.equals(other);
    }

    /**
     * Returns a hashcode value for the object.
     * <p>
     * It follows the contract defined by the Object hashCode() method.
     * <p>
     * 
     * @return the hashcode of the bean object.
     * 
     */
    @Override
    public int hashCode() {
        return this._objBean.hashCode();
    }

    /**
     * Returns the String representation for the object.
     * <p>
     * 
     * @return String representation for the object.
     * 
     */
    @Override
    public String toString() {
        return this._objBean.toString();
    }

    /**
     * Returns the link rel.
     * <p>
     * 
     * @return the link rel, <b>null</b> if none.
     * 
     */
    public String getRel() {
        return this._rel;
    }

    /**
     * Sets the link rel.
     * <p>
     * 
     * @param rel the link rel,, <b>null</b> if none.
     * 
     */
    public void setRel(final String rel) {
        // TODO add check, ask P@ about the check
        this._rel = rel;
    }

    /**
     * Returns the link type.
     * <p>
     * 
     * @return the link type, <b>null</b> if none.
     * 
     */
    public String getType() {
        return this._type;
    }

    /**
     * Sets the link type.
     * <p>
     * 
     * @param type the link type, <b>null</b> if none.
     * 
     */
    public void setType(final String type) {
        this._type = type;
    }

    /**
     * Returns the link href.
     * <p>
     * 
     * @return the link href, <b>null</b> if none.
     * 
     */
    public String getHref() {
        return this._href;
    }

    /**
     * Sets the link href.
     * <p>
     * 
     * @param href the link href, <b>null</b> if none.
     * 
     */
    public void setHref(final String href) {
        this._href = href;
    }

    public void setHrefResolved(final String hrefResolved) {
        this._hrefResolved = hrefResolved;
    }

    public String getHrefResolved() {
        return this._hrefResolved != null ? this._hrefResolved : this._href;
    }

    /**
     * Returns the link title.
     * <p>
     * 
     * @return the link title, <b>null</b> if none.
     * 
     */
    public String getTitle() {
        return this._title;
    }

    /**
     * Sets the link title.
     * <p>
     * 
     * @param title the link title, <b>null</b> if none.
     * 
     */
    public void setTitle(final String title) {
        this._title = title;
    }

    /**
     * Returns the hreflang
     * <p>
     * 
     * @return Returns the hreflang.
     * @since Atom 1.0
     */
    public String getHreflang() {
        return this._hreflang;
    }

    /**
     * Set the hreflang
     * <p>
     * 
     * @param hreflang The hreflang to set.
     * @since Atom 1.0
     */
    public void setHreflang(final String hreflang) {
        this._hreflang = hreflang;
    }

    /**
     * Returns the length
     * <p>
     * 
     * @return Returns the length.
     */
    public long getLength() {
        return this._length;
    }

    /**
     * Set the length
     * <p>
     * 
     * @param length The length to set.
     */
    public void setLength(final long length) {
        this._length = length;
    }
}