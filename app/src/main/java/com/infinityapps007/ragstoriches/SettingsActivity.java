package com.infinityapps007.ragstoriches;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;

import com.infinityapps007.ragstoriches.Util.SharedPrefManager;

import java.util.List;


public class SettingsActivity extends AppCompatPreferenceActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setupActionBar();
        SwitchPreference switchPreference = (SwitchPreference) getPreferenceManager().findPreference(getString(R.string.key_night_mode));
        switchPreference.setChecked(SharedPrefManager.getThemeValue(getApplicationContext()));
        switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPrefManager.setThemeValue(Boolean.parseBoolean(newValue.toString()),getApplicationContext());
                return true;
            }

        });

        SwitchPreference switchPreference_cache = (SwitchPreference) getPreferenceManager().findPreference(getString(R.string.key_disk_cache));
        switchPreference_cache.setChecked(SharedPrefManager.get_Disk_Cahce_Value(getApplicationContext()));
        switchPreference_cache.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SharedPrefManager.set_Disk_Cache_Value(Boolean.parseBoolean(newValue.toString()),getApplicationContext());
                return true;
            }

        });

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }



}
