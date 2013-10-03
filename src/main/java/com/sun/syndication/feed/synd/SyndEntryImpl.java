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
package com.sun.syndication.feed.synd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom2.Element;

import com.sun.syndication.feed.CopyFrom;
import com.sun.syndication.feed.impl.CopyFromHelper;
import com.sun.syndication.feed.impl.ObjectBean;
import com.sun.syndication.feed.module.DCModule;
import com.sun.syndication.feed.module.DCModuleImpl;
import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.module.SyModule;
import com.sun.syndication.feed.module.SyModuleImpl;
import com.sun.syndication.feed.module.impl.ModuleUtils;
import com.sun.syndication.feed.synd.impl.URINormalizer;

/**
 * Bean for entries of SyndFeedImpl feeds.
 * <p>
 * 
 * @author Alejandro Abdelnur
 * 
 */
public class SyndEntryImpl implements Serializable, SyndEntry {
    private final ObjectBean _objBean;
    private String _uri;
    private String _link;
    private Date _updatedDate;
    private SyndContent _title;
    private SyndContent _description;
    private List<SyndLink> _links;
    private List<SyndContent> _contents; // deprecated by Atom 1.0
    private List<Module> _modules;
    private List<SyndEnclosure> _enclosures;
    private List<SyndPerson> _authors;
    private List<SyndPerson> _contributors;
    private SyndFeed _source;
    private List<Element> _foreignMarkup;
    private Object wireEntry; // com.sun.syndication.feed.atom.Entry or
                              // com.sun.syndication.feed.rss.Item

    // ISSUE: some converters assume this is never null
    private List<SyndCategory> _categories = new ArrayList<SyndCategory>();

    private static final Set<String> IGNORE_PROPERTIES = new HashSet<String>();

    /**
     * Unmodifiable Set containing the convenience properties of this class.
     * <p>
     * Convenience properties are mapped to Modules, for cloning the convenience
     * properties can be ignored as the will be copied as part of the module
     * cloning.
     */
    public static final Set<String> CONVENIENCE_PROPERTIES = Collections.unmodifiableSet(IGNORE_PROPERTIES);

    static {
        IGNORE_PROPERTIES.add("publishedDate");
        IGNORE_PROPERTIES.add("author");
    }

    /**
     * For implementations extending SyndEntryImpl to be able to use the
     * ObjectBean functionality with extended interfaces.
     * <p>
     * 
     * @param beanClass
     * @param convenienceProperties set containing the convenience properties of
     *            the SyndEntryImpl (the are ignored during cloning, check
     *            CloneableBean for details).
     * 
     */
    protected SyndEntryImpl(final Class beanClass, final Set convenienceProperties) {
        this._objBean = new ObjectBean(beanClass, this, convenienceProperties);
    }

    /**
     * Default constructor. All properties are set to <b>null</b>.
     * <p>
     * 
     */
    public SyndEntryImpl() {
        this(SyndEntry.class, IGNORE_PROPERTIES);
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
        if (other == null) {
            return false;
        }
        // while ObjectBean does this check this method does a cast to obtain
        // the foreign markup
        // so we need to check before doing so.
        if (!(other instanceof SyndEntryImpl)) {
            return false;
        }
        // can't use foreign markup in equals, due to JDOM equals impl
        final List<Element> fm = getForeignMarkup();
        setForeignMarkup(((SyndEntryImpl) other).getForeignMarkup());
        final boolean ret = this._objBean.equals(other);
        // restore foreign markup
        setForeignMarkup(fm);
        return ret;
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
     * Returns the entry URI.
     * <p>
     * How the entry URI maps to a concrete feed type (RSS or Atom) depends on
     * the concrete feed type. This is explained in detail in Rome
     * documentation, <a
     * href="http://wiki.java.net/bin/edit/Javawsxml/Rome04URIMapping">Feed and
     * entry URI mapping</a>.
     * <p>
     * The returned URI is a normalized URI as specified in RFC 2396bis.
     * <p>
     * 
     * @return the entry URI, <b>null</b> if none.
     * 
     */
    @Override
    public String getUri() {
        return this._uri;
    }

    /**
     * Sets the entry URI.
     * <p>
     * How the entry URI maps to a concrete feed type (RSS or Atom) depends on
     * the concrete feed type. This is explained in detail in Rome
     * documentation, <a
     * href="http://wiki.java.net/bin/edit/Javawsxml/Rome04URIMapping">Feed and
     * entry URI mapping</a>.
     * <p>
     * 
     * @param uri the entry URI to set, <b>null</b> if none.
     * 
     */
    @Override
    public void setUri(final String uri) {
        this._uri = URINormalizer.normalize(uri);
    }

    /**
     * Returns the entry title.
     * <p>
     * 
     * @return the entry title, <b>null</b> if none.
     * 
     */
    @Override
    public String getTitle() {
        if (this._title != null) {
            return this._title.getValue();
        }
        return null;
    }

    /**
     * Sets the entry title.
     * <p>
     * 
     * @param title the entry title to set, <b>null</b> if none.
     * 
     */
    @Override
    public void setTitle(final String title) {
        if (this._title == null) {
            this._title = new SyndContentImpl();
        }
        this._title.setValue(title);
    }

    /**
     * Returns the entry title as a text construct.
     * <p>
     * 
     * @return the entry title, <b>null</b> if none.
     * 
     */
    @Override
    public SyndContent getTitleEx() {
        return this._title;
    }

    /**
     * Sets the entry title as a text construct.
     * <p>
     * 
     * @param title the entry title to set, <b>null</b> if none.
     * 
     */
    @Override
    public void setTitleEx(final SyndContent title) {
        this._title = title;
    }

    /**
     * Returns the entry link.
     * <p>
     * 
     * @return the entry link, <b>null</b> if none.
     * 
     */
    @Override
    public String getLink() {
        return this._link;
    }

    /**
     * Sets the entry link.
     * <p>
     * 
     * @param link the entry link to set, <b>null</b> if none.
     * 
     */
    @Override
    public void setLink(final String link) {
        this._link = link;
    }

    /**
     * Returns the entry description.
     * <p>
     * 
     * @return the entry description, <b>null</b> if none.
     * 
     */
    @Override
    public SyndContent getDescription() {
        return this._description;
    }

    /**
     * Sets the entry description.
     * <p>
     * 
     * @param description the entry description to set, <b>null</b> if none.
     * 
     */
    @Override
    public void setDescription(final SyndContent description) {
        this._description = description;
    }

    /**
     * Returns the entry contents.
     * <p>
     * 
     * @return a list of SyndContentImpl elements with the entry contents, an
     *         empty list if none.
     * 
     */
    @Override
    public List<SyndContent> getContents() {
        return this._contents == null ? (this._contents = new ArrayList<SyndContent>()) : this._contents;
    }

    /**
     * Sets the entry contents.
     * <p>
     * 
     * @param contents the list of SyndContentImpl elements with the entry
     *            contents to set, an empty list or <b>null</b> if none.
     * 
     */
    @Override
    public void setContents(final List<SyndContent> contents) {
        this._contents = contents;
    }

    /**
     * Returns the entry enclosures.
     * <p>
     * 
     * @return a list of SyndEnclosure elements with the entry enclosures, an
     *         empty list if none.
     * 
     */
    @Override
    public List<SyndEnclosure> getEnclosures() {
        return this._enclosures == null ? (this._enclosures = new ArrayList<SyndEnclosure>()) : this._enclosures;
    }

    /**
     * Sets the entry enclosures.
     * <p>
     * 
     * @param enclosures the list of SyndEnclosure elements with the entry
     *            enclosures to set, an empty list or <b>null</b> if none.
     * 
     */
    @Override
    public void setEnclosures(final List<SyndEnclosure> enclosures) {
        this._enclosures = enclosures;
    }

    /**
     * Returns the entry published date.
     * <p>
     * This method is a convenience method, it maps to the Dublin Core module
     * date.
     * <p>
     * 
     * @return the entry published date, <b>null</b> if none.
     * 
     */
    @Override
    public Date getPublishedDate() {
        return getDCModule().getDate();
    }

    /**
     * Sets the entry published date.
     * <p>
     * This method is a convenience method, it maps to the Dublin Core module
     * date.
     * <p>
     * 
     * @param publishedDate the entry published date to set, <b>null</b> if
     *            none.
     * 
     */
    @Override
    public void setPublishedDate(final Date publishedDate) {
        getDCModule().setDate(publishedDate);
    }

    /**
     * Returns the entry categories.
     * <p>
     * 
     * @return a list of SyndCategoryImpl elements with the entry categories, an
     *         empty list if none.
     * 
     */
    @Override
    public List<SyndCategory> getCategories() {
        return this._categories;
    }

    /**
     * Sets the entry categories.
     * <p>
     * This method is a convenience method, it maps to the Dublin Core module
     * subjects.
     * <p>
     * 
     * @param categories the list of SyndCategoryImpl elements with the entry
     *            categories to set, an empty list or <b>null</b> if none.
     * 
     */
    @Override
    public void setCategories(final List<SyndCategory> categories) {
        this._categories = categories;
    }

    /**
     * Returns the entry modules.
     * <p>
     * 
     * @return a list of ModuleImpl elements with the entry modules, an empty
     *         list if none.
     * 
     */
    @Override
    public List<Module> getModules() {
        if (this._modules == null) {
            this._modules = new ArrayList<Module>();
        }
        if (ModuleUtils.getModule(this._modules, DCModule.URI) == null) {
            this._modules.add(new DCModuleImpl());
        }
        return this._modules;
    }

    /**
     * Sets the entry modules.
     * <p>
     * 
     * @param modules the list of ModuleImpl elements with the entry modules to
     *            set, an empty list or <b>null</b> if none.
     * 
     */
    @Override
    public void setModules(final List<Module> modules) {
        this._modules = modules;
    }

    /**
     * Returns the module identified by a given URI.
     * <p>
     * 
     * @param uri the URI of the ModuleImpl.
     * @return The module with the given URI, <b>null</b> if none.
     */
    @Override
    public Module getModule(final String uri) {
        return ModuleUtils.getModule(getModules(), uri);
    }

    /**
     * Returns the Dublin Core module of the feed.
     * 
     * @return the DC module, it's never <b>null</b>
     * 
     */
    private DCModule getDCModule() {
        return (DCModule) getModule(DCModule.URI);
    }

    @Override
    public Class getInterface() {
        return SyndEntry.class;
    }

    @Override
    public void copyFrom(final CopyFrom obj) {
        COPY_FROM_HELPER.copy(this, obj);
    }

    private static final CopyFromHelper COPY_FROM_HELPER;

    static {
        final Map basePropInterfaceMap = new HashMap();
        basePropInterfaceMap.put("uri", String.class);
        basePropInterfaceMap.put("title", String.class);
        basePropInterfaceMap.put("link", String.class);
        basePropInterfaceMap.put("uri", String.class);
        basePropInterfaceMap.put("description", SyndContent.class);
        basePropInterfaceMap.put("contents", SyndContent.class);
        basePropInterfaceMap.put("enclosures", SyndEnclosure.class);
        basePropInterfaceMap.put("modules", Module.class);

        final Map basePropClassImplMap = new HashMap();
        basePropClassImplMap.put(SyndContent.class, SyndContentImpl.class);
        basePropClassImplMap.put(SyndEnclosure.class, SyndEnclosureImpl.class);
        basePropClassImplMap.put(DCModule.class, DCModuleImpl.class);
        basePropClassImplMap.put(SyModule.class, SyModuleImpl.class);

        COPY_FROM_HELPER = new CopyFromHelper(SyndEntry.class, basePropInterfaceMap, basePropClassImplMap);
    }

    /**
     * Returns the links
     * <p>
     * 
     * @return Returns the links.
     */
    @Override
    public List<SyndLink> getLinks() {
        return this._links == null ? (this._links = new ArrayList<SyndLink>()) : this._links;
    }

    /**
     * Set the links
     * <p>
     * 
     * @param links The links to set.
     */
    @Override
    public void setLinks(final List<SyndLink> links) {
        this._links = links;
    }

    /**
     * Returns the updatedDate
     * <p>
     * 
     * @return Returns the updatedDate.
     */
    @Override
    public Date getUpdatedDate() {
        return this._updatedDate == null ? null : new Date(this._updatedDate.getTime());
    }

    /**
     * Set the updatedDate
     * <p>
     * 
     * @param updatedDate The updatedDate to set.
     */
    @Override
    public void setUpdatedDate(final Date updatedDate) {
        this._updatedDate = new Date(updatedDate.getTime());
    }

    @Override
    public List<SyndPerson> getAuthors() {
        return this._authors == null ? (this._authors = new ArrayList<SyndPerson>()) : this._authors;
    }

    /*
     * (non-Javadoc)
     * @see com.sun.syndication.feed.synd.SyndEntry#setAuthors(java.util.List)
     */
    @Override
    public void setAuthors(final List<SyndPerson> authors) {
        this._authors = authors;
    }

    /**
     * Returns the entry author.
     * <p>
     * This method is a convenience method, it maps to the Dublin Core module
     * creator.
     * <p>
     * 
     * @return the entry author, <b>null</b> if none.
     * 
     */
    @Override
    public String getAuthor() {
        String author;

        // Start out looking for one or more authors in _authors. For non-Atom
        // feeds, _authors may actually be null.
        if (this._authors != null && this._authors.size() > 0) {
            author = this._authors.get(0).getName();
        } else {
            author = getDCModule().getCreator();
        }
        if (author == null) {
            author = "";
        }

        return author;
    }

    /**
     * Sets the entry author.
     * <p>
     * This method is a convenience method, it maps to the Dublin Core module
     * creator.
     * <p>
     * 
     * @param author the entry author to set, <b>null</b> if none.
     * 
     */
    @Override
    public void setAuthor(final String author) {
        // Get the DCModule so that we can check to see if "creator" is already
        // set.
        final DCModule dcModule = getDCModule();
        final String currentValue = dcModule.getCreator();

        if (currentValue == null || currentValue.length() == 0) {
            getDCModule().setCreator(author);
        }
    }

    @Override
    public List<SyndPerson> getContributors() {
        return this._contributors == null ? (this._contributors = new ArrayList<SyndPerson>()) : this._contributors;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.sun.syndication.feed.synd.SyndEntry#setContributors(java.util.List)
     */
    @Override
    public void setContributors(final List<SyndPerson> contributors) {
        this._contributors = contributors;
    }

    @Override
    public SyndFeed getSource() {
        return this._source;
    }

    @Override
    public void setSource(final SyndFeed source) {
        this._source = source;
    }

    /**
     * Returns foreign markup found at channel level.
     * <p>
     * 
     * @return list of JDOM nodes containing channel-level foreign markup, an
     *         empty list if none.
     * 
     */
    @Override
    public List<Element> getForeignMarkup() {
        return this._foreignMarkup == null ? (this._foreignMarkup = new ArrayList<Element>()) : this._foreignMarkup;
    }

    /**
     * Sets foreign markup found at channel level.
     * <p>
     * 
     * @param foreignMarkup list of JDOM nodes containing channel-level foreign
     *            markup, an empty list if none.
     * 
     */
    @Override
    public void setForeignMarkup(final List<Element> foreignMarkup) {
        this._foreignMarkup = foreignMarkup;
    }

    @Override
    public Object getWireEntry() {
        return this.wireEntry;
    }

    public void setWireEntry(final Object wireEntry) {
        this.wireEntry = wireEntry;
    }

    @Override
    public SyndLink findRelatedLink(final String relation) {
        for (final SyndLink l : getLinks()) {
            if (relation.equals(l.getRel())) {
                return l;
            }
        }
        return null;
    }
}