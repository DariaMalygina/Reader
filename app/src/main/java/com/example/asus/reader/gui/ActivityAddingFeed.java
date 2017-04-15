package com.example.asus.reader.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.asus.reader.R;
import com.example.asus.reader.db.Feed;
import com.example.asus.reader.xml.MessengerIntentServiceXml;

@SuppressWarnings("ALL")
public class ActivityAddingFeed extends AppCompatActivity {

    private static final String STATE_URL = "com.example.asus.reader.gui.STATE_URL";
    private EditText urlFeed;
    final private Feed feed = new Feed();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null)
            urlFeed.setText(savedInstanceState.getString(STATE_URL));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_feed);
        urlFeed = (EditText) findViewById(R.id.editTextUrlFeed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter filter = new IntentFilter(ConstantsWorkService.FILTER_ADD_FEED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverAddFeed, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverAddFeed);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_URL, urlFeed.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null)
            urlFeed.setText(savedInstanceState.getString(STATE_URL));
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void onClickAddNewFeed(final View view)
    {
        if ( urlFeed.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), R.string.toast_check_edit_text, Toast.LENGTH_SHORT).show();
        } else {
            feed.setUrlFeed(urlFeed.getText().toString());
            final MessengerIntentServiceXml messenger = new MessengerIntentServiceXml(ConstantsWorkService.ACTION_ADD_NEW_FEED);
            startService(messenger.AddFeedMessageToService(feed,ActivityAddingFeed.this));
        }
    }

    final private BroadcastReceiver receiverAddFeed = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarAddingFeed);
            final int resultCode = intent.getIntExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, 0);
            switch (resultCode) {
                case ConstantsWorkService.STATUS_RUNNING:
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    break;
                case ConstantsWorkService.STATUS_FINISHED_ADD_TO_DB:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.toast_feed_successfully_add, Toast.LENGTH_SHORT).show();
                    final Intent intentStart = new Intent(ActivityAddingFeed.this, ActivityFeed.class);
                    intentStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentStart);
                    break;

                case ConstantsWorkService.STATUS_FEED_ALREADY_EXISTS:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.toast_warning_feed_exists, Toast.LENGTH_SHORT).show();
                    break;
                case ConstantsWorkService.STATUS_INCORRECT_URL:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.toast_incorrect_url_add_feed, Toast.LENGTH_SHORT).show();
                    break;
                case ConstantsWorkService.STATUS_ERROR:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.toast_error_add_feed, Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };
}
