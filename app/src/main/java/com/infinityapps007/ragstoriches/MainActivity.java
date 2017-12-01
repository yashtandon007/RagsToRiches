package com.infinityapps007.ragstoriches;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.FirebaseDatabase;
import com.infinityapps007.ragstoriches.Util.CustumDialog_Rate;
import com.infinityapps007.ragstoriches.Util.SharedPrefManager;
import com.infinityapps007.ragstoriches.fragments.About_Us;
import com.infinityapps007.ragstoriches.fragments.Categories;
import com.infinityapps007.ragstoriches.fragments.List;
import com.infinityapps007.ragstoriches.fragments.Search;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    public static BillingProcessor bp;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean(getString(R.string.Notification_Already), false)) {
            Log.e("not", "asas");
            registerNotification();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(getString(R.string.Notification_Already), true);
            editor.apply();
        }
        if (SharedPrefManager.get_Disk_Cahce_Value(getApplicationContext()) == true) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvHXawbS2E9jVhoHQUiJ7ugGS6bv8JeQMnjdqkIjRcG/j/4dyiM9yglI6GJ8d2iCSqwILlUDhNRj4mhrRoRpy430/gofSgaB1XCS/wZ+VR/CrCoM5znf0dWHf9FTvthWW7jaLFpWhiWHDIMx8+yREFu3wb7a5uRhdWMa5Ch5UuB1/sUL4jcIJD7XFIQ8IE/XPccpDOZu4sDzQRv9+6gkSIBw3GsmHK1v6pYM84yWTL+v6ckRDfPJEcM1RQLwjfhoMYr6toBz+eXcscHzhmVoNRPvMwwLrfoIz1UPgYDCH5HGPlmpbnrb9l5UqHfQ2TR533bw5q8SZwX94n1gRUb/ESwIDAQAB", this);
        MobileAds.initialize(this, getString(R.string.adid));
        final AdView adView = (AdView) findViewById(R.id.adView
        );

        if (!SharedPrefManager.adsfree(getApplicationContext())) {
            adView.loadAd(MyApplication.getAdRequest());
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (!SharedPrefManager.adsfree(getApplicationContext())) {
                        adView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        if (getIntent().getIntExtra("category", 0) == Categories.CATEGORIE_RECENT_DATA) {
            Fragment fragment = new com.infinityapps007.ragstoriches.fragments.List();
            Bundle args = new Bundle();
            args.putInt(getString(R.string.category), Categories.CATEGORIE_RECENT_DATA);
            fragment.setArguments(args);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame, fragment).commit();
            //Initializing the bottomNavigationVie
//            bottomNavigationView.setSelectedItemId(R.id.action_recent);
        } else {

            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame, new Categories()).commit();
            //Initializing the bottomNavigationView
            setUpBottomNavigation(manager);
        }

    }

    private void registerNotification() {

        Intent notifyIntent = new Intent(this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast
                (this, 212, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 41);
        calendar.set(Calendar.SECOND, 1);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


    }


    private void setUpBottomNavigation(final FragmentManager manager) {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()) {

                            case R.id.action_home:
                                selectedFragment = new Categories();
                                break;
                            case R.id.action_recent:

                                Fragment fragment = new com.infinityapps007.ragstoriches.fragments.List();
                                Bundle args = new Bundle();
                                args.putInt(getString(R.string.category), Categories.CATEGORIE_RECENT_DATA);
                                fragment.setArguments(args);
                                selectedFragment = fragment;
                                break;
                            case R.id.action_settings:
                                selectedFragment = null;
                                break;

                        }

                        if (selectedFragment != null) {
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.frame, selectedFragment);
                            transaction.commit();
                        } else {
                            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        }
                        return true;
                    }
                });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (currentFragment instanceof List) {
                ((List) currentFragment).backPressed();
            } else if (currentFragment instanceof About_Us) {

                ((About_Us) currentFragment).backPressed();
            } else if (currentFragment instanceof Search) {

                ((Search) currentFragment).backPressed();
            } else {
                CustumDialog_Rate custumDialog_rate = new CustumDialog_Rate(this);
                custumDialog_rate.show();
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        CustumDialog_Rate custumDialog_rate = new CustumDialog_Rate(this);
        custumDialog_rate.show();

    }

    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {

        Toast.makeText(this, "Enjoy Ads Free Version..restart app", Toast.LENGTH_SHORT).show();
        SharedPrefManager.setAdsFreeValue(1, getApplicationContext());
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }


}
