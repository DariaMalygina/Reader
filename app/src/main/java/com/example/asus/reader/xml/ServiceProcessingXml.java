package com.example.asus.reader.xml;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;

import com.example.asus.reader.db.DatabaseHelper;
import com.example.asus.reader.db.Feed;
import com.example.asus.reader.db.Item;
import com.example.asus.reader.gui.ConstantsWorkService;

import java.util.ArrayList;


public final class ServiceProcessingXml extends IntentService {

    public ServiceProcessingXml()
    {
        super("ServiceProcessingXml");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if(intent != null)
        {
            final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            final String action = intent.getAction();
            switch (action)
            {
                case ConstantsWorkService.ACTION_ADD_NEW_FEED:
                {
                    final MessengerIntentServiceXml messenger = new MessengerIntentServiceXml(ConstantsWorkService.FILTER_ADD_FEED);
                    messenger.statusRunning(broadcastManager);

                    final ArrayList<Feed> feedEntity = intent.getParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST);
                    final Feed feed = new Feed();
                    ArrayList<Item> items;
                    feed.setUrlFeed(feedEntity.get(0).getUrlFeed());

                    if(checkInternet()) {
                        try {

                            final DatabaseHelper db = new DatabaseHelper(this);
                            //добавать, если канала нет в базе
                            Feed feedInput = db.getFeedByUrl(feed.getUrlFeed());
                            if (feedInput != null && feedInput.getUrlFeed().equals("")) {
                                Parser parser = new Parser();
                                feedInput = parser.getFeed(feed.getUrlFeed());
                                if(feedInput != null) {
                                    items = parser.getItems(feedInput.getUrlFeed());
                                    if(items != null) {
                                        if (db.addFeed(feedInput) == DatabaseHelper.ERROR_OPERATION) {
                                            messenger.statusError(broadcastManager);
                                        } else {
                                            if (db.addArrayListItem(items) == DatabaseHelper.ERROR_OPERATION) {
                                                messenger.statusError(broadcastManager);
                                            }
                                            messenger.statusFinishedAddToBD(broadcastManager, feed.getNameFeed());

                                        }
                                    }else {
                                        messenger.statusError(broadcastManager);
                                    }
                                }
                                else {
                                    messenger.statusIncorrectUrl(broadcastManager);
                                }
                            } else {
                                messenger.statusFeedAlreadyExists(broadcastManager);
                            }

                        } catch (final Exception e) {
                            messenger.statusError(broadcastManager);
                        }
                    }
                    else messenger.statusFinishedNoInternet(broadcastManager);



                }break;
                case ConstantsWorkService.ACTION_OPEN_EXTERNAL_FEED:
                {
                    final MessengerIntentServiceXml messenger = new MessengerIntentServiceXml(ConstantsWorkService.FILTER_OPEN_EXTERNAL_FEED);
                    messenger.statusRunning(broadcastManager);

                    final String urlFeed = intent.getStringExtra(ConstantsWorkService.EXTRA_URL_FEED);
                    final Feed feed;
                    ArrayList<Item> items;

                    if(checkInternet()) {
                        try {
                            if (urlFeed != null) {
                                Parser parser = new Parser();
                                feed = parser.getFeed(urlFeed);
                                if(feed != null) {
                                    items = parser.getItems(urlFeed);
                                    if(items != null) {
                                        messenger.statusFinishedOpenItems(broadcastManager, items, feed.getNameFeed());
                                    }else {
                                        messenger.statusError(broadcastManager);
                                    }
                                }
                                else {
                                    messenger.statusIncorrectUrl(broadcastManager);
                                }
                            } else {
                                messenger.statusError(broadcastManager);
                            }

                        } catch (final Exception e) {
                            messenger.statusError(broadcastManager);
                        }
                    }
                    else messenger.statusFinishedNoInternet(broadcastManager);

                }break;
                case ConstantsWorkService.ACTION_UPDATE_ITEMS_ALL_FEEDS: {
                    final MessengerIntentServiceXml messenger = new MessengerIntentServiceXml(ConstantsWorkService.FILTER_UPDATE_ITEMS_ALL_FEEDS);
                    messenger.statusRunning(broadcastManager);

                    //проверить флаг false - не обновляли из за отключенного интернета
                    //if (!false)
                    //обновить true
                    if (checkInternet()) {
                        try {
                            final DatabaseHelper db = new DatabaseHelper(this);
                            ArrayList<Item> items;
                            ArrayList<Feed> feeds;
                            feeds = db.getAllFeeds();
                            if(feeds != null) {
                                Parser parser = new Parser();
                                for(int i = 0, size = feeds.size(); i < size; i++){
                                    items = parser.getItems(feeds.get(i).getUrlFeed());
                                    if(items != null){
                                        db.deleteItemsByUrlFeed(feeds.get(i).getUrlFeed());
                                        db.addArrayListItem(items);
                                    }
                                }
                            }

                        } catch (final Exception e) {
                            messenger.statusError(broadcastManager);
                        }
                    }/*else{
                        //сохранить false
                    }*/
            }break;

            }
        }

        this.stopSelf();
    }

    private boolean checkInternet() {
        final ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
