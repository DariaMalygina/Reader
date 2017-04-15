package com.example.asus.reader.xml;

import com.example.asus.reader.db.Feed;
import com.example.asus.reader.db.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

final class AtomParser {

    private final static String ATOM_TITLE = "title";
    private final static String ATOM_ENTRY = "entry";
    private final static String ATOM_ENTRY_TITLE = "title";
    private final static String ATOM_ENTRY_PUBLISHED = "published";
    private final static String ATOM_ENTRY_LINK = "link";
    private final static String ATOM_ENTRY_LINK_REL = "rel";
    private final static String ATOM_ENTRY_LINK_ALTERNATE = "alternate";
    private final static String ATOM_ENTRY_LINK_HREF = "href";
    private final static String ATOM_ENTRY_CONTENT = "content";
    private final static String NS_ATOM = "http://www.w3.org/2005/Atom";

    Feed parseFeed(final XmlPullParser parser) {
        if(parser == null)
            return null;

        Feed feed = new Feed();
        try {

            for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (isAtomElement(parser, ATOM_TITLE))
                        feed.setNameFeed(parser.nextText());
                    else if (isAtomElement(parser, ATOM_ENTRY))
                        break;
                }

            }
        }catch (XmlPullParserException e) {
            return null;
        }catch (IOException e) {
            return null;
        }
        return feed;

    }

    ArrayList<Item> parseItems(final XmlPullParser parser, final String url) {
        if(parser == null)
            return null;

        Item item = null;
        ArrayList<Item> items = new ArrayList<>();
        try {

            for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (isAtomElement(parser, ATOM_ENTRY)) {
                        item = new Item();
                    } else if (isAtomElement(parser, ATOM_ENTRY_TITLE)) {
                        if (item!=null)
                            item.setTitleItem(parser.nextText());
                    } else if (isAtomElement(parser, ATOM_ENTRY_LINK)) {
                        String rel = parser.getAttributeValue(null, ATOM_ENTRY_LINK_REL);
                        if (rel == null || rel.equals(ATOM_ENTRY_LINK_ALTERNATE)) {
                            if (item!=null)
                                item.setUrlItem(parser.getAttributeValue(null, ATOM_ENTRY_LINK_HREF));
                        }
                    } else if (isAtomElement(parser, ATOM_ENTRY_CONTENT)) {
                        if (item!=null)
                            item.setTextItem(parser.nextText());
                    }
                    else if (isAtomElement(parser, ATOM_ENTRY_PUBLISHED)) {
                        if (item!=null)
                            item.setDateItem(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (isAtomElement(parser, ATOM_ENTRY)&& item != null) {
                        item.setUrlFeed(url);
                        items.add(item);
                        item = null;
                    }
                }
            }
        }catch (XmlPullParserException e) {
            return null;
        }catch (IOException e) {
            return null;
        }
        return items;
    }

    private static boolean isAtomElement(XmlPullParser parser, String name) {
        return parser.getName().equals(name) && parser.getNamespace().equals(NS_ATOM);
    }

}
