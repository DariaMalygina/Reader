package com.example.asus.reader.xml;


import com.example.asus.reader.db.Feed;
import com.example.asus.reader.db.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


final class RssParser {

    private final static String RSS_TITLE = "title";
    private final static String RSS_ITEM = "item";
    private final static String RSS_ITEM_TITLE = "title";
    private final static String RSS_ITEM_PUB_DATE = "pubDate";
    private final static String RSS_ITEM_LINK = "link";
    private final static String RSS_ITEM_LINK_REL = "rel";
    private final static String RSS_ITEM_LINK_PAYMENT = "payment";
    private final static String RSS_ITEM_LINK_HREF = "href";
    private final static String RSS_ITEM_DESCRIPTION = "description";

    Feed parseFeed(final XmlPullParser parser) {
        if(parser == null)
            return null;

        Feed feed = new Feed();
        String name;
        try {

            for (int eventType = parser.getEventType(); eventType != XmlPullParser.END_DOCUMENT; eventType = parser.next()) {
                if (eventType != XmlPullParser.START_TAG)
                    continue;
                name = parser.getName();
                if (name.equals(RSS_TITLE)) {
                    feed.setNameFeed(parser.nextText());
                    break;
                } else if (name.equals(RSS_ITEM)) {
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
                    if (parser.getName().equals(RSS_ITEM)) {
                        item = new Item();
                    } else if (parser.getName().equals(RSS_ITEM_TITLE)) {
                        if (item!=null)
                            item.setTitleItem(parser.nextText());
                    } else if (parser.getName().equalsIgnoreCase(RSS_ITEM_LINK)) {
                        String rel = parser.getAttributeValue(null, RSS_ITEM_LINK_REL);
                        if (rel != null && rel.equalsIgnoreCase(RSS_ITEM_LINK_PAYMENT)) {
                            if(item!=null)
                                item.setUrlItem(parser.getAttributeValue(null, RSS_ITEM_LINK_HREF));
                        } else {
                            if(item!=null)
                                item.setUrlItem(parser.nextText());
                        }
                    } else if (parser.getName().equals(RSS_ITEM_DESCRIPTION)) {
                        if (item!=null)
                            item.setTextItem(parser.nextText());
                    }
                    else if (parser.getName().equals(RSS_ITEM_PUB_DATE)) {
                        if (item!=null)
                            item.setDateItem(parser.nextText());
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (parser.getName().equals(RSS_ITEM) && item != null) {
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
}
