package com.mycompany.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;


public class ListContextAdapter extends RecyclerView.Adapter<ListContextAdapter.ViewHolder> {

    private Context mContext;
    private int mSelect = -1;
    private LinkedList<ListContext> mData;
    private ClickItem mClickItem;

    public interface ClickItem{
        void clickItem(View view, int position);
    }

    public void setClickItem(ClickItem clickItem){this.mClickItem = clickItem;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_para,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public ListContextAdapter(LinkedList<ListContext> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.paraName.setText(mData.get(position).getName());
        holder.paraValue.setText(mData.get(position).getValueOld());
        if(mClickItem != null){
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickItem.clickItem(view,position);
                }
            });
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView paraName;
        TextView paraValue;
        LinearLayout linearLayout;
        public ViewHolder(View itemview){
            super(itemview);
            paraValue = itemview.findViewById(R.id.para_value);
            paraName = itemview.findViewById(R.id.para_name);
            linearLayout = itemview.findViewById(R.id.ll_item);
        }
    }
}
