package com.mrpteam.amozesh.Models;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrpteam.amozesh.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class catadapter extends RecyclerView.Adapter<catadapter.ViewHolder> {
    private final LayoutInflater inflater;

    public interface OnItemClickListener {
        void onItemClick(CatModel image);
    }

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    private ArrayList<CatModel> catlists;
    private static Context context;
    OnItemClickListener  listner;
    Typeface myTypeface;

    // Constructor of the class
    public catadapter(Context context, ArrayList<CatModel> itemList, OnItemClickListener onItemClickListener) {
        this.catlists = itemList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listner=onItemClickListener;

    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return catlists == null ? 0 : catlists.size();
    }


    // specify the row layout file and click for each row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = null;
        rootView = inflater.inflate(R.layout.cat_row, parent, false);

        return new ViewHolder(rootView);
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView titletxt = holder.titletxt;
        ImageView imgimg = holder.imgimg;
        titletxt.setText(catlists.get(listPosition).getName());
        int id = catlists.get(listPosition).getId();
        String img = catlists.get(listPosition).getImg();
        holder.id=id;
        holder.img=img;
        try {
            Picasso.with(context)
                    .load(catlists.get(listPosition).getImg())
                    .into(imgimg);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    // Static inner class to initialize the views of rows
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titletxt;
        public String img;
        public ImageView imgimg;
        public int id;

        public ViewHolder(View itemView) {
            super(itemView);
            titletxt = (TextView) itemView.findViewById(R.id.titleTxt);
            imgimg =  itemView.findViewById(R.id.img);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(catlists.get(getAdapterPosition()));

                }
            });


        }
        @Override
        public void onClick(View view) {

            Log.d("onclick", "onClick " + getLayoutPosition() + " " + id);

        }
    }
}
