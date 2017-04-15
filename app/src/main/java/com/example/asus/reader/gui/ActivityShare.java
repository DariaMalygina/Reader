package com.example.asus.reader.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.reader.R;
import com.example.asus.reader.db.Item;
import com.example.asus.reader.xml.MessengerIntentServiceXml;

import java.util.ArrayList;


public class ActivityShare extends AppCompatActivity {

    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        String urlChannel = getIntent().getDataString();
        final MessengerIntentServiceXml messenger = new MessengerIntentServiceXml(ConstantsWorkService.ACTION_OPEN_EXTERNAL_FEED);
        startService(messenger.OpenItemsMessageToService(urlChannel, ActivityShare.this));

    }



    @Override
    protected void onResume() {
        super.onResume();
        final IntentFilter filterOpenItems = new IntentFilter(ConstantsWorkService.FILTER_OPEN_EXTERNAL_FEED);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverOpenItems, filterOpenItems);

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverOpenItems);
    }

    final private BroadcastReceiver receiverOpenItems = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarShare);
            final int resultCode = intent.getIntExtra(ConstantsWorkService.EXTENDED_DATA_STATUS, 0);
            final ViewStub stubError = (ViewStub) findViewById(R.id.errorListViewShare);
            final ViewStub stubEmpty = (ViewStub) findViewById(R.id.emptyListViewShare);
            switch (resultCode) {
                case ConstantsWorkService.STATUS_RUNNING:
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    break;
                case ConstantsWorkService.STATUS_FINISHED:
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                    items = intent.getParcelableArrayListExtra(ConstantsWorkService.EXTRA_PARCELABLE_ARRAY_LIST);
                    if(items.size() == 0) {
                        stubEmpty.inflate();
                    }
                    else {
                        String nameFeed = intent.getStringExtra(ConstantsWorkService.EXTRA_NAME_FEED);
                        TextView title = (TextView) findViewById(R.id.shareNameFeed);
                        title.setText(nameFeed);
                        createListView();
                    }
                    break;
                case ConstantsWorkService.STATUS_ERROR:
                    progressBar.setVisibility(ProgressBar.VISIBLE);
                    stubError.inflate();
                    break;
            }


        }
    };


    private void createListView()
    {
        ItemAdapter adapter;
        final ListView listView;
        final ViewStub stubEmpty = (ViewStub) findViewById(R.id.emptyListViewShare);
        if(items != null && items.size() > 0) {
            adapter = new ItemAdapter(ActivityShare.this, items);
            listView = (ListView) findViewById(R.id.listViewShare);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                    final Item item = (Item) parent.getItemAtPosition(position);
                    final String pageUrl = item.getUrlItem();
                    //открыть сайт
                    if(!pageUrl.equals("")) {
                        final Intent intent = new Intent(ActivityShare.this, ActivityWeb.class);
                        intent.putExtra(ActivityWeb.PAGE_URL, pageUrl);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), R.string.toast_not_available_url, Toast.LENGTH_SHORT).show();
                    }

                }


            });

        }
        else{
            stubEmpty.inflate();
        }
    }

}
