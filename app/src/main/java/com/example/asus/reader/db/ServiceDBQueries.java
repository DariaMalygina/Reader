package com.example.asus.reader.db;


import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.asus.reader.gui.ConstantsWorkService;

import java.util.ArrayList;

public final class ServiceDBQueries extends IntentService {

    public ServiceDBQueries()
    {
        super("SServiceDBQueries");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if(intent != null)
        {
            final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            final String action = intent.getAction();
            switch (action)
            {
                case ConstantsWorkService.ACTION_DOWNLOAD_FEEDS:
                {
                    final MessengerIntentServiceDB messenger = new MessengerIntentServiceDB(ConstantsWorkService.FILTER_DOWNLOAD_FEEDS);
                    try {
                        messenger.statusRunning(broadcastManager);
                        final DatabaseHelper db = new DatabaseHelper(this);
                        final ArrayList<Feed> feeds = db.getAllFeeds();
                        if (null != feeds) {
                            messenger.statusFinishedDownloadFeeds(broadcastManager, feeds);
                        }
                    } catch (final Exception e) {
                        messenger.statusError(broadcastManager);
                    }

                }break;
                case ConstantsWorkService.ACTION_DELETE_FEED:
                {
                    final MessengerIntentServiceDB messenger = new MessengerIntentServiceDB(ConstantsWorkService.FILTER_DELETE_FEED);
                    final  ArrayList<Feed> oneFeed = intent.getParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST);
                    final int position = intent.getIntExtra(ConstantsWorkService.EXTRA_POSITION_ARRAY_LIST, 0);
                    try {
                        messenger.statusRunning(broadcastManager);
                        final DatabaseHelper db = new DatabaseHelper(this);
                        if(oneFeed != null) {
                            String urlFeed = oneFeed.get(0).getUrlFeed();
                            if (db.deleteFeedByUrl(oneFeed.get(0).getUrlFeed()) == DatabaseHelper.ERROR_OPERATION) {
                                messenger.statusError(broadcastManager);
                            } else {
                                db.deleteItemsByUrlFeed(urlFeed);
                                messenger.statusFinishedDeleteFeed(broadcastManager, position);
                            }
                        }
                    } catch (final Exception e) {
                        messenger.statusError(broadcastManager);
                    }
                }break;
                case ConstantsWorkService.ACTION_DOWNLOAD_ITEMS:
                {
                    final MessengerIntentServiceDB messenger = new MessengerIntentServiceDB(ConstantsWorkService.FILTER_DOWNLOAD_ITEMS);
                    try {
                        messenger.statusRunning(broadcastManager);
                        final String urlFeed = intent.getStringExtra(ConstantsWorkService.EXTRA_URL_FEED);
                        if (urlFeed != null) {
                            final DatabaseHelper db = new DatabaseHelper(this);
                            final ArrayList<Item> items = db.getItemByUrlFeed(urlFeed);
                            if (items != null) {
                                messenger.statusFinishedDownloadItems(broadcastManager, items);
                            }
                            else {
                                messenger.statusError(broadcastManager);
                            }
                        }
                        else {
                            messenger.statusError(broadcastManager);
                        }
                    } catch (final Exception e) {
                        messenger.statusError(broadcastManager);
                    }

                }break;

            }
        }

        this.stopSelf();
    }

}