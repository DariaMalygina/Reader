package com.example.asus.reader.xml;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.asus.reader.db.Feed;
import com.example.asus.reader.db.Item;
import com.example.asus.reader.gui.ConstantsWorkService;

import java.util.ArrayList;


public final class MessengerIntentServiceXml {
    private final String action;

    public MessengerIntentServiceXml(final String action)
    {
        this.action = action;
    }

    void statusRunning(final LocalBroadcastManager broadcast)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_RUNNING);
        broadcast.sendBroadcast(intent);
    }

    void statusError(final LocalBroadcastManager broadcast)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_ERROR);
        broadcast.sendBroadcast(intent);
    }

    void statusFinishedAddToBD(final LocalBroadcastManager broadcast, final String nameFeed)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_FINISHED_ADD_TO_DB);
        intent.putExtra(ConstantsWorkService.EXTRA_NAME_FEED, nameFeed);
        broadcast.sendBroadcast(intent);
    }



    void statusFeedAlreadyExists(final LocalBroadcastManager broadcast)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_FEED_ALREADY_EXISTS);
        broadcast.sendBroadcast(intent);
    }



    void statusFinishedOpenItems(final LocalBroadcastManager broadcast, final ArrayList<Item> items, final String nameFeed)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_FINISHED);
        intent.putParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST, items);
        intent.putExtra(ConstantsWorkService.EXTRA_NAME_FEED, nameFeed);
        broadcast.sendBroadcast(intent);
    }

    void statusFinishedNoInternet(final LocalBroadcastManager broadcast)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_NO_INTERNET);
        broadcast.sendBroadcast(intent);
    }

    void statusIncorrectUrl(final LocalBroadcastManager broadcast)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_INCORRECT_URL);
        broadcast.sendBroadcast(intent);
    }

    public Intent AddFeedMessageToService(final Feed feed, final Context context)
    {
        final ArrayList<Feed> oneFeed = new ArrayList<>();
        final Intent intent = new Intent(context, ServiceProcessingXml.class);
        oneFeed.add(feed);
        intent.putParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST, oneFeed);
        intent.setAction(action);
        return intent;
    }

    public Intent OpenItemsMessageToService(final String urlFeed, final Context context)
    {
        final Intent intent = new Intent(context, ServiceProcessingXml.class);
        intent.putExtra(ConstantsWorkService.EXTRA_URL_FEED, urlFeed);
        intent.setAction(action);
        return intent;
    }

}
