package com.infinityapps007.ragstoriches.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.infinityapps007.ragstoriches.Adapter;
import com.infinityapps007.ragstoriches.MyApplication;
import com.infinityapps007.ragstoriches.R;
import com.infinityapps007.ragstoriches.Util.Data;
import com.infinityapps007.ragstoriches.Util.SharedPrefManager;
import com.infinityapps007.ragstoriches.Util.Utili;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.infinityapps007.ragstoriches.Util.Utili.getReverserLinkedHashMap;


/**
 * Created by Magic Lenz on 6/1/2017.
 */

public class List extends Fragment {

    LinkedHashMap<String, Data> hmap = new LinkedHashMap<String, Data>();

    private RecyclerView recyclerView;
    private ProgressBar pb;

    private DatabaseReference mDatabaseRef;
    private Adapter mAdapter;
    private RelativeLayout offline_view;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private static String  TAG =List.class.getSimpleName();
    int category;
    View view;
    ImageView toolbar_image;
    Typeface tf;
    private static int counter=0;
    private static InterstitialAd mInterstitialAd;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.list,
                container, false);
        tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/COMIC.TTF");

        if(!SharedPrefManager.adsfree(getActivity().getApplicationContext())){
            showAdsCode();
        }

        Bundle args = getArguments();
        category = args.getInt(getString(R.string.category));
        Log.e("cat",""+category);
        setUpToolBarImage(view,category);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               initList();
            }
        });
        offline_view  = view.findViewById(R.id.offline_view);
        pb = view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        initList();
        return view;
    }

    private void showAdsCode() {
        if(counter==1){
            if(mInterstitialAd.isLoaded()){
                mInterstitialAd.show();
            }
            counter=0;
        }else{
            mInterstitialAd = new InterstitialAd(getActivity());
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial));
            mInterstitialAd.loadAd(MyApplication.getAdRequest());
            counter++;
        }
    }

    private void setUpToolBarImage(View view, int category) {


        CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTypeface(tf);
        collapsingToolbarLayout.setExpandedTitleTypeface(tf);
        ImageView toolbar_cover = view.findViewById(R.id.list_toolbar_coverimage);
        Toolbar toolbar = this.view.findViewById(R.id.toolbar);
        toolbar_image = this.view.findViewById(R.id.toolbar_image);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               backPressed();
            }
        });
        switch (category){
            case Categories.CATEGORIE_ARTIST:
                toolbar_image.setImageResource(R.drawable.categories_actor);
                toolbar_cover.setImageResource(R.color.red_700);
                collapsingToolbarLayout.setTitle(Html.fromHtml("Artist"));
                break;
            case Categories.CATEGORIE_SPORTS:
                toolbar_image.setImageResource(R.drawable.categories_sports);
                toolbar_cover.setImageResource(R.color.blue_700);
                collapsingToolbarLayout.setTitle("Sportsman");

                break;
            case Categories.CATEGORIE_BUSINESS:
                toolbar_image.setImageResource(R.drawable.categories_business_bg);
                toolbar_cover.setImageResource(R.color.green_700);
                collapsingToolbarLayout.setTitle("Businessman");

                break;
            case Categories.CATEGORIE_SCIENTIST:
                toolbar_image.setImageResource(R.drawable.categories_scientist);
                toolbar_cover.setImageResource(R.color.brown_700);
                collapsingToolbarLayout.setTitle("Scientists");

                break;
            case Categories.CATEGORIE_RECENT_DATA:
                toolbar_image.setImageResource(R.drawable.preview);
                toolbar_cover.setImageResource(R.color.brown_700);
                collapsingToolbarLayout.setTitle("Latest Stories");
                break;
        }
    }

    private void initList() {

        if(!Utili.isNetworkAvailable(getActivity())) {
            Toast.makeText(getActivity(), "no connection available..", Toast.LENGTH_SHORT).show();
        }
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
            //checkViewVisibilityForOffline();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef.keepSynced(true);
        if(category == Categories.CATEGORIE_RECENT_DATA){
            queryDataByTimestamp();
        }else{
            queryDataByCategory();
        }
    }

    private void queryDataByTimestamp() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("scores");
        Query query = mDatabaseRef.orderByChild("timestamp"). limitToLast(10);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;

                hmap.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Data myData = singleSnapshot.getValue(Data.class);


                    String key = singleSnapshot.getKey();
                        hmap.put(key,myData);

                }
                hmap = getReverserLinkedHashMap(hmap);

                mAdapter = new Adapter(hmap,getActivity());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void queryDataByCategory() {

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("scores");
        Query query = mDatabaseRef.orderByChild("timestamp");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkViewVisibilityForOffline() {

        pb.setVisibility(View.VISIBLE);
        if(Utili.isNetworkAvailable(getActivity())){

            offline_view.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            pb.setVisibility(View.GONE);
            recyclerView.setVisibility(View.INVISIBLE);
            offline_view.setVisibility(View.VISIBLE);
            if(mSwipeRefreshLayout.isRefreshing()){
                mSwipeRefreshLayout.setRefreshing(false);
            }
            return;
        }
        if(mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getData(DataSnapshot dataSnapshot) {
        hmap.clear();

        Log.e("getdata",""+dataSnapshot.getValue());
            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                Data myData = singleSnapshot.getValue(Data.class);
                if(myData.person_category.equalsIgnoreCase(category+"")){
                    String key = singleSnapshot.getKey();
                    hmap.put(key,myData);

                }

            }

        hmap = getReverserLinkedHashMap(hmap);
        mAdapter = new Adapter(hmap,getActivity());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        pb.setVisibility(View.GONE);
    }



    public void backPressed() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, new Categories());
        transaction.commit();
    }

}
