package com.jakfromspace.kotoandroid;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Coded by JAKfromSpace on 7/29/2018 for Koto.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private ArrayList<KotoItem> kotoItemArrayList;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener{
        void onItemClick(int pos);
        void onDeleteClick(int pos);
    }
    public void setOnItemClick(OnItemClickListener listener){
        mlistener = listener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView mtextView1, mtextView2, mtextView3;
        ImageView delete;

        public RecyclerViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.itemimageview);
            mtextView1 = itemView.findViewById(R.id.itemtextmain);
            mtextView2 = itemView.findViewById(R.id.itemtextamount);
            mtextView3 = itemView.findViewById(R.id.itemdate);
            delete = itemView.findViewById(R.id.itemDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onItemClick(pos);
                        }
                    }
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(pos);
                        }
                    }
                }
            });
        }
    }

    public RecyclerAdapter(ArrayList<KotoItem> recItems){
        kotoItemArrayList = recItems;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.overview_item, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view, mlistener);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        KotoItem currentItem =  kotoItemArrayList.get(position);

        //holder.imageView.setImageResource(currentItem.getImageRes());
        holder.mtextView1.setText(currentItem.getTextMain());
        holder.mtextView2.setText(currentItem.getTextSubAmount()+" bdt");
        holder.mtextView3.setText(currentItem.getDateTaken());
    }

    @Override
    public int getItemCount() {
        return kotoItemArrayList.size();
    }

}
