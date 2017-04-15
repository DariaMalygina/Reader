package com.example.asus.reader.db;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.asus.reader.gui.ConstantsWorkService;

import java.util.ArrayList;

public final class MessengerIntentServiceDB {
    private final String action;

    public MessengerIntentServiceDB(final String action)
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

    void statusFinishedDownloadFeeds(final LocalBroadcastManager broadcast, final ArrayList<Feed> feedList)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_FINISHED);
        intent.putParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST, feedList);
        broadcast.sendBroadcast(intent);
    }

    void statusFinishedDownloadItems(final LocalBroadcastManager broadcast, final ArrayList<Item> items)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_FINISHED);
        intent.putParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST, items);
        broadcast.sendBroadcast(intent);
    }

    void statusFinishedDeleteFeed(final LocalBroadcastManager broadcast,  final int position)
    {
        final Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, ConstantsWorkService.STATUS_FINISHED);
        intent.putExtra(ConstantsWorkService.EXTRA_POSITION_ARRAY_LIST, position);
        broadcast.sendBroadcast(intent);
    }

    public Intent DeleteFeedMessageToService(final Feed item, final int position, final Context context)
    {
        final ArrayList<Feed> oneFeed = new ArrayList<>();
        final Intent intent = new Intent(context, ServiceDBQueries.class);
        oneFeed.add(item);
        intent.putParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST, oneFeed);
        intent.putExtra(ConstantsWorkService.EXTRA_POSITION_ARRAY_LIST, position);
        intent.setAction(action);
        return intent;
    }

    public Intent DownloadItemsMessageToService(final String urlFeed, final Context context)
    {
        final Intent intent = new Intent(context, ServiceDBQueries.class);
        intent.putExtra(ConstantsWorkService.EXTRA_URL_FEED, urlFeed);
        intent.setAction(action);
        return intent;
    }

}
