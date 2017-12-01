package com.infinityapps007.ragstoriches.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import static com.infinityapps007.ragstoriches.Util.Utili.getReverserLinkedHashMap;
import static com.infinityapps007.ragstoriches.Util.Utili.toTitleCase;


/**
 * Created by Magic Lenz on 6/1/2017.
 */

public class Search extends Fragment {

    LinkedHashMap<String, Data> hmap = new LinkedHashMap<String, Data>();
    private RecyclerView recyclerView;
    private Adapter mAdapter;

    private static final String TAG = "search";
    View view;
    MaterialSearchBar materialSearchBar;
    private DatabaseReference mDatabaseRef;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.search,
                container, false);
        materialSearchBar = view.findViewById(R.id.searchBar);
        materialSearchBar.enableSearch();
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("scores");
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.e(TAG, "beforeTextChanged: " + charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.toString().isEmpty()){
                    return ;
                }
                charSequence = toTitleCase(charSequence);
                Log.e(TAG, "onTextChanged: " + charSequence);
                Query query = mDatabaseRef.orderByChild("person_name").startAt(charSequence + "")
                        .endAt(charSequence + "\uf8ff");
                query.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        getData(dataSnapshot);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.e(TAG, "afterTextChanged: ");
            }
        });
        return view;
    }

    public void backPressed() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, new Categories());
        transaction.commit();
    }

    private void getData(DataSnapshot dataSnapshot) {
        hmap.clear();
        Log.e("getdata", "" + dataSnapshot.getValue());
        for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
            Data myData = singleSnapshot.getValue(Data.class);
            String key = singleSnapshot.getKey();
            hmap.put(key, myData);

        }

        Log.e(TAG, "getData: SIZE HASHMAP "+hmap.size() );
        hmap = getReverserLinkedHashMap(hmap);
        mAdapter = new Adapter(hmap, getActivity());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        Log.e(TAG, "getData: notified "+hmap.size() );

    }



}
