package com.example.asus.reader.gui;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.asus.reader.R;

public final class ActivitySettings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    //private static final String KEY_LIST_PREFERENCE = "updateFrequency";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /*if (key.equals(KEY_LIST_PREFERENCE)) {
            //изменить будильник
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
