package com.infinityapps007.ragstoriches;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.infinityapps007.ragstoriches.Util.Data_DetailActivity;
import com.infinityapps007.ragstoriches.Util.SharedPrefManager;
import com.infinityapps007.ragstoriches.fragments.Categories;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.infinityapps007.ragstoriches.Util.CustumDialog_Rate.app_exited;

public class DetailsActivity extends AppCompatActivity
        implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    TextView detailName;
    TextView detailHeader;
    ImageView detailIv;
    TextView detailPara;
    InterstitialAd mInterstitialAd2,mInterstitialAd3;
    FloatingActionButton night_mode, share_story;
   // RelativeLayout rl_bg;
    private DatabaseReference mDatabaseRef;
    private static boolean isNightMode_enabled;
    private String image_path;
    NestedScrollView scrollView;
    FloatingActionMenu mFloatingActionButton;
    private static int counter_detail_Activity = 0;
    private static InterstitialAd mInterstitialAd_detail_Activity;
    CircleImageView circleImageView;

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    ImageView topBannerImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (SharedPrefManager.getThemeValue(getApplicationContext()) == false ){
            setTheme(R.style.daymode);
            Log.e("theme", "daymode");

        } else {
            setTheme(R.style.nightMode);
            Log.e("theme", "nightMode");

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_material);
        bindActivity();

        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        if (!SharedPrefManager.adsfree(getApplicationContext())) {
            showAdsCode();
        }

        night_mode.setOnClickListener(this);
        share_story.setOnClickListener(this);
        Typeface type3 = Typeface.createFromAsset(getAssets(), "fonts/COMICBD.TTF");
        Typeface type4 = Typeface.createFromAsset(getAssets(), "fonts/COMIC.TTF");


        mInterstitialAd2 = new InterstitialAd(this);
        mInterstitialAd3 = new InterstitialAd(this);
        mInterstitialAd2.setAdUnitId(getString(R.string.interstitial));
        mInterstitialAd3.setAdUnitId(getString(R.string.interstitial));
        mInterstitialAd3.loadAd(MyApplication.getAdRequest());

        detailHeader.setTypeface(type4);
        detailName.setTypeface(type3);
        mTitle.setTypeface(type3);
        detailPara.setTypeface(type4);

        Data_DetailActivity obj = getIntent().getParcelableExtra("obj");
        changeTopImageByCategory(Integer.parseInt(obj.person_category));
        Log.e("fb", "key >> " + obj.id);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("stories");

        mDatabaseRef.keepSynced(true);
        Query query = mDatabaseRef.orderByChild("person_id").equalTo(obj.id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    Log.e("fb", "Data >> " + dataSnapshot1.getValue());
                    String person_story = dataSnapshot1.child("person_story").getValue(String.class);
                    Log.e("fb", "person_story >> " + person_story);
                    detailPara.setText(getFromattedString(person_story));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        detailHeader.setText(getFromattedString(obj.person_heading));
        detailName.setText(getFromattedString(obj.person_name));
        mTitle.setText(getFromattedString(obj.person_name));
        Log.e("fb", "key >> " + obj.id);

        image_path = obj.person_icon_pathname;
        Picasso.with(this)
                .load(obj.person_icon_pathname)
                .into(detailIv);
        Picasso.with(this)
                .load(obj.person_icon_pathname)
                .into(circleImageView);


    }

    private void changeTopImageByCategory(int category) {

        switch (category){
            case Categories.CATEGORIE_ARTIST:
                topBannerImage.setImageResource(R.drawable.categories_actor);
                break;
            case Categories.CATEGORIE_SPORTS:
                topBannerImage.setImageResource(R.drawable.categories_sports);

                break;
            case Categories.CATEGORIE_BUSINESS:
                topBannerImage.setImageResource(R.drawable.categories_business_bg);
                break;
            case Categories.CATEGORIE_SCIENTIST:
                topBannerImage.setImageResource(R.drawable.categories_scientist);

                break;
            case Categories.CATEGORIE_RECENT_DATA:
                topBannerImage.setImageResource(R.drawable.preview);

                break;
        }
    }

    private void bindActivity() {
        circleImageView = (CircleImageView) findViewById(R.id.circular_Image);
        mFloatingActionButton = (FloatingActionMenu) findViewById(R.id.floating_menu);
        scrollView = (NestedScrollView) findViewById(R.id.scroolView);
        night_mode = (FloatingActionButton) findViewById(R.id.menu_item_night_mode);
        share_story = (FloatingActionButton) findViewById(R.id.menu_item_share_story);
       // rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        detailHeader = (TextView) findViewById(R.id.detail_header);
        detailIv = (ImageView) findViewById(R.id.detail_iv);
        topBannerImage = (ImageView) findViewById(R.id.topbannerImage);
        detailPara = (TextView) findViewById(R.id.detail_para);
        detailName = (TextView) findViewById(R.id.detail_name);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTitle = (TextView) findViewById(R.id.main_textview_title);
        mTitleContainer = (LinearLayout) findViewById(R.id.main_linearlayout_title);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.main_appbar);

    }

    private void showAdsCode() {

        if (counter_detail_Activity == 2) {
            if (mInterstitialAd_detail_Activity.isLoaded()) {
                mInterstitialAd_detail_Activity.show();
            }
            counter_detail_Activity = 0;
        } else {
            mInterstitialAd_detail_Activity = new InterstitialAd(this);
            mInterstitialAd_detail_Activity.setAdUnitId(getString(R.string.interstitial));
            mInterstitialAd_detail_Activity.loadAd(MyApplication.getAdRequest());
            counter_detail_Activity++;
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(app_exited == false){

                            mInterstitialAd3.show();
                        }
                    }
                },15000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getFromattedString(String val) {
        Log.e("forma", "bBEFORE >>> " + val);
        val = val.replaceAll("\\\\n", "\n");
        Log.e("forma", "AFTER >>> " + val);
        return val;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_item_night_mode:

                if (SharedPrefManager.getThemeValue(getApplicationContext()) == false) {
                    SharedPrefManager.setThemeValue(true, getApplicationContext());

                } else {
                    SharedPrefManager.setThemeValue(false, getApplicationContext());

                }

                recreate();


                break;
            case R.id.menu_item_share_story:
                try {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "" + getString(R.string.app_name));
                    String sAux = "\nShared from Success Stories of Inspirational Peoples\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                    sAux = sAux + detailName.getText() + "\n";
                    sAux = sAux + "" + detailPara.getText() + "\n";
                    i.putExtra(Intent.EXTRA_STREAM, image_path);

                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    //e.toString();
                }
                break;

        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }


}
