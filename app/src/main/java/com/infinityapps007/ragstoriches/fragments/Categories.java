package com.infinityapps007.ragstoriches.fragments;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.infinityapps007.ragstoriches.MainActivity;
import com.infinityapps007.ragstoriches.R;
import com.infinityapps007.ragstoriches.Util.SharedPrefManager;


/**
 * Created by Magic Lenz on 6/1/2017.
 */

public class Categories extends Fragment implements View.OnClickListener {

    View view;
    CardView artist, politician, innovator, business;
    public static final int CATEGORIE_BUSINESS = 0;
    public static final int CATEGORIE_ARTIST = 1;
    public static final int CATEGORIE_SPORTS = 3;
    public static final int CATEGORIE_SCIENTIST = 2;
    public static final int CATEGORIE_RECENT_DATA = -1;

    ImageView img_sports;
    private static String TAG = Categories.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.categories,
                container, false);

        ImageView adsfree = (ImageView) view.findViewById(R.id.ads_free);
        adsfree.setOnClickListener(this);
        ImageView search = (ImageView) view.findViewById(R.id.search);
        search.setOnClickListener(this);
        artist = view.findViewById(R.id.artist);
        img_sports = view.findViewById(R.id.img_sports);
        artist.setOnClickListener(this);
        politician = view.findViewById(R.id.sports);
        politician.setOnClickListener(this);
        innovator = view.findViewById(R.id.inovator);
        innovator.setOnClickListener(this);
        business = view.findViewById(R.id.business);
        business.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sports:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Bundle args = new Bundle();
                    args.putInt(getString(R.string.category), CATEGORIE_SPORTS);
                    Fragment fragment = new com.infinityapps007.ragstoriches.fragments.List();
                    fragment.setArguments(args);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame, fragment).addToBackStack(null).
                            commit();
                }
                break;

            case R.id.artist:
                Bundle args2 = new Bundle();
                args2.putInt(getString(R.string.category), CATEGORIE_ARTIST);
                Fragment fragment2 = new com.infinityapps007.ragstoriches.fragments.List();
                fragment2.setArguments(args2);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment2).addToBackStack(null).commit();

                break;
            case R.id.inovator:
                Bundle args3 = new Bundle();
                args3.putInt(getString(R.string.category), CATEGORIE_SCIENTIST);
                Fragment fragment3 = new com.infinityapps007.ragstoriches.fragments.List();
                fragment3.setArguments(args3);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment3).addToBackStack(null).commit();
                break;
            case R.id.business:
                Bundle args4 = new Bundle();
                args4.putInt(getString(R.string.category), CATEGORIE_BUSINESS);
                Fragment fragment4 = new com.infinityapps007.ragstoriches.fragments.List();
                fragment4.setArguments(args4);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment4).addToBackStack(null).commit();

                break;
            case R.id.search:
                Fragment fragment7 = new com.infinityapps007.ragstoriches.fragments.Search();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame, fragment7).addToBackStack(null).commit();

                break;
            case R.id.ads_free:
                MainActivity.bp.purchase(getActivity(),"com.infinityapps007.ragstoriches.inapppurchase");
                break;

        }
    }


}
