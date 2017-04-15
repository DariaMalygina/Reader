package com.example.asus.reader.gui;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.reader.R;
import com.example.asus.reader.db.Feed;
import com.example.asus.reader.db.MessengerIntentServiceDB;
import com.example.asus.reader.db.ServiceDBQueries;
import com.example.asus.reader.xml.ServiceProcessingXml;

import java.util.ArrayList;

public class ActivityFeed extends AppCompatActivity {

    private FeedAdapter adapter;
    private ArrayList<Feed> feeds;
    static public final long SEC_30 = 30000;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        final Intent startService = new Intent(this, ServiceDBQueries.class);
        startService.setAction(ConstantsWorkService.ACTION_DOWNLOAD_FEEDS);
        startService(startService);

        Intent intent = new Intent(ActivityFeed.this, ServiceProcessingXml.class);
        intent.setAction(ConstantsWorkService.ACTION_UPDATE_ITEMS_ALL_FEEDS);
        PendingIntent pending = PendingIntent.getService(ActivityFeed.this, 0, intent, 0);
        AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        long t = System.currentTimeMillis() + SEC_30*3;
        alarm.setInexactRepeating(AlarmManager.RTC, t, 30*1000, pending);

    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        setContentView(R.layout.activity_feed);
        final Intent startService = new Intent(this, ServiceDBQueries.class);
        startService.setAction(ConstantsWorkService.ACTION_DOWNLOAD_FEEDS);
        startService(startService);

    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter filterDelete = new IntentFilter(ConstantsWorkService.FILTER_DELETE_FEED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverDeleteFeed, filterDelete);

        final IntentFilter filterDownload = new IntentFilter(ConstantsWorkService.FILTER_DOWNLOAD_FEEDS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverDownloadFeeds, filterDownload);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverDeleteFeed);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverDownloadFeeds);
    }


    final private BroadcastReceiver receiverDeleteFeed = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarFeeds);
            final ViewStub stubEmpty = (ViewStub) findViewById(R.id.emptyListViewFeeds);

            final int resultCode = intent.getIntExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, 0);
            switch (resultCode) {
                case ConstantsWorkService.STATUS_RUNNING:
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    break;
                case ConstantsWorkService.STATUS_FINISHED:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    final int position = intent.getIntExtra(ConstantsWorkService.EXTRA_POSITION_ARRAY_LIST,0);
                    if(adapter != null) {
                        adapter.remove(adapter.getItem(position));
                        adapter.notifyDataSetChanged();
                        if(adapter.getCount() == 0) {
                            stubEmpty.inflate();
                        }
                    }
                    Toast.makeText(getApplicationContext(), R.string.toast_feed_successfully_delete_feed_by_url, Toast.LENGTH_SHORT).show();
                    break;
                case ConstantsWorkService.STATUS_ERROR:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.toast_error_delete_feed_by_url, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    final private BroadcastReceiver receiverDownloadFeeds = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarFeeds);
            final int resultCode = intent.getIntExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, 0);
            final ViewStub stubError = (ViewStub) findViewById(R.id.errorListViewFeeds);
            switch (resultCode) {
                case ConstantsWorkService.STATUS_RUNNING:
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    break;
                case ConstantsWorkService.STATUS_FINISHED:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    feeds = intent.getParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST);
                    createListView();
                    break;
                case ConstantsWorkService.STATUS_ERROR:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    stubError.inflate();
                    break;
            }


        }
    };

    private void createListView()
    {
        final ViewStub stubEmpty = (ViewStub) findViewById(R.id.emptyListViewFeeds);
        final ListView listView;
        if(feeds != null && feeds.size() > 0) {
            adapter = new FeedAdapter(ActivityFeed.this, feeds);
            listView = (ListView) findViewById(R.id.listViewFeeds);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                    final Feed item = (Feed) parent.getItemAtPosition(position);
                    final Intent intent = new Intent(ActivityFeed.this, ActivityItems.class);
                    intent.putExtra(ActivityItems.URL_FEED_EXTRA_MESSAGE, item.getUrlFeed());
                    startActivity(intent);
                }


            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, final long id) {
                    final Feed item = (Feed) parent.getItemAtPosition(position);
                    final AlertDialog.Builder ad = new AlertDialog.Builder(ActivityFeed.this);
                    ad.setTitle(R.string.alert_dialog_title_for_item_list_view);
                    ad.setMessage(item.getNameFeed());
                    ad.setPositiveButton(R.string.alert_dialog_title_button_yes_for_item_list_view, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int arg1) {
                            final MessengerIntentServiceDB messenger = new MessengerIntentServiceDB(ConstantsWorkService.ACTION_DELETE_FEED);
                            startService(messenger.DeleteFeedMessageToService(item,position,ActivityFeed.this));
                        }
                    });
                    ad.setNegativeButton(R.string.alert_dialog_title_button_no_for_item_list_view, new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int arg1) {}
                    });
                    ad.show();
                    return true;
                }

            });

        }
        else{
            stubEmpty.inflate();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionNewChannel:
                final Intent intent = new Intent(this, ActivityAddingFeed.class);
                startActivity(intent);
                return true;
            case R.id.actionSettings:
                final Intent intentSettings = new Intent(this, ActivitySettings.class);
                startActivity(intentSettings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
