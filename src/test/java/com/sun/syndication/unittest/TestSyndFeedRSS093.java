/*
 * Created on Jun 24, 2004
 *
 */
package com.sun.syndication.unittest;

import java.util.Date;
import java.util.List;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.io.impl.DateParser;

/**
 * @author pat
 * 
 */
public class TestSyndFeedRSS093 extends TestSyndFeedRSS092 {

    public TestSyndFeedRSS093() {
        super("rss_0.93");
    }

    protected TestSyndFeedRSS093(final String type) {
        super(type);
    }

    protected TestSyndFeedRSS093(final String feedType, final String feedFileName) {
        super(feedType, feedFileName);
    }

    @Override
    protected void _testItem(final int i) throws Exception {
        super._testItem(i);
        final List<SyndEntry> items = this.getCachedSyndFeed().getEntries();
        final SyndEntry entry = items.get(i);
        final Date d = DateParser.parseRFC822("Mon, 0" + (i + 1) + " Jan 2001 00:00:00 GMT");
        assertEquals(entry.getPublishedDate(), d);
        _testDescriptionType(entry, i);
    }

    protected void _testDescriptionType(final SyndEntry entry, final int i) throws Exception {
        assertProperty(entry.getDescription().getType(), "channel.item[" + i + "].description^type");
    }

    @Override
    public void testEntryPublishedDate() throws Exception {
        assertEquals(DateParser.parseRFC822("Mon, 01 Jan 2001 00:00:00 GMT"), getEntryPublishedDate(this.getCachedSyndFeed().getEntries().get(0)));
        assertEquals(DateParser.parseRFC822("Tue, 02 Jan 2001 00:00:00 GMT"), getEntryPublishedDate(this.getCachedSyndFeed().getEntries().get(1)));
    }

}