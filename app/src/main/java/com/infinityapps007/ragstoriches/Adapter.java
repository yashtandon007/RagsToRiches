package com.infinityapps007.ragstoriches;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.infinityapps007.ragstoriches.Util.Data;
import com.infinityapps007.ragstoriches.Util.Data_DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Magic Lenz on 6/14/2017.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    private String[] mKeys;
    private LinkedHashMap<String,Data> hmap;
   // private HashMap<,String,Data> list_filter;
    Activity activity;
    private Typeface type3;


//    @Override
//    public Filter getFilter() {
//
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//
//                String charString = charSequence.toString();
//
//                if (charString.isEmpty()) {
//
//                    list_filter = list;
//                } else {
//
//                    ArrayList<Data> filteredList = new ArrayList<>();
//
//                    for (Data dataa : list) {
//
//                        if (dataa.person_name.toLowerCase().contains(charString)) {
//
//                            filteredList.add(dataa);
//                        }
//                    }
//
//                    list_filter = filteredList;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = list_filter;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                list_filter = (ArrayList<Data>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView image;
        FrameLayout lineralyout;
        CircleImageView circularIMageView;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.item_tv);
            image = (ImageView) view.findViewById(R.id.item_iv);
            lineralyout = (FrameLayout) view.findViewById(R.id.item_layout);
            circularIMageView = view.findViewById(R.id.icon_center);
            name.setTypeface(type3);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            width = width / 2;
            int decreasePerc = (int) (width * (0.4));
            int widthActual = width - decreasePerc;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthActual, widthActual);
            params.gravity = Gravity.CENTER;
            circularIMageView.setLayoutParams(params);
        }
    }

    public Adapter(LinkedHashMap<String,Data> mData, Activity activity) {
        this.hmap = mData;
        mKeys = mData.keySet().toArray(new String[mData.size()]);
      //  this.list_filter = moviesList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        type3 = Typeface.createFromAsset(activity.getAssets(), "fonts/COMICBD.TTF");

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        final String key = mKeys[position];
        final Data myData =  hmap.get(key);

        Picasso.with(activity).load(myData.person_icon_pathname).placeholder(R.drawable.person).into(holder.circularIMageView);

        holder.name.setText(myData.person_name);
        holder.lineralyout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Data_DetailActivity data_detailActivity = new Data_DetailActivity();
                data_detailActivity.person_category = myData.person_category;
                data_detailActivity.person_heading = myData.person_heading;
                data_detailActivity.person_icon_pathname = myData.person_icon_pathname;
                data_detailActivity.person_name = myData.person_name;
                data_detailActivity.id = key;
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("obj",data_detailActivity);
                activity.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return hmap.size();
    }

}
