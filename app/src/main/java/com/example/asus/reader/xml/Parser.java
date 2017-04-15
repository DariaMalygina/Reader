package com.example.asus.reader.xml;


import android.net.ParseException;
import android.util.Xml;

import com.example.asus.reader.db.Feed;
import com.example.asus.reader.db.Item;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


final class Parser {
    private final static String ATOM = "feed";
    private final static String RSS = "rss";

    Feed getFeed(final String url)
    {
        Feed feed = null;
        if(url == null)
            return null;

        InputStream stream;


        try {
            stream = getInputStream(url);
            if(stream == null)
                return null;

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(stream, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.START_TAG)
                eventType = parser.next();
            if (parser.getName().equals(RSS)) {
                RssParser rssParser = new RssParser();
                feed = rssParser.parseFeed(parser);
                if(feed != null){
                    feed.setUrlFeed(url);
                }

            } else if (parser.getName().equals(ATOM)) {
                AtomParser atomParser = new AtomParser();
                feed = atomParser.parseFeed(parser);
                if(feed != null){
                    feed.setUrlFeed(url);
                }
            }
            stream.close();

        } catch (XmlPullParserException e) {
            return null;
        }catch (ParseException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return feed;
    }

    final ArrayList<Item> getItems(final String url)
    {
        ArrayList<Item> items = new ArrayList<>();

        if(url == null)
            return null;

        InputStream stream;


        try {
            stream = getInputStream(url);
            if(stream == null)
                return null;

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(stream, null);

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.START_TAG)
                eventType = parser.next();
            if (parser.getName().equals(RSS)) {
                RssParser rssParser = new RssParser();
                items = rssParser.parseItems(parser, url);

            } else if (parser.getName().equals(ATOM)) {
                AtomParser atomParser = new AtomParser();
                items = atomParser.parseItems(parser, url);
            }
            stream.close();

        } catch (XmlPullParserException e) {
            return null;
        }catch (ParseException e) {
            return null;
        } catch (IOException e) {
            return null;
        }

        return items;
    }



    private InputStream getInputStream(final String urlChannel) {
        try {
            final URL url = new URL(urlChannel);
            return url.openConnection().getInputStream();
        } catch (MalformedURLException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
