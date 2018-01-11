package com.mycompany.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.LinkedList;
import java.util.List;

/*
public class ListContextAdapter extends ArrayAdapter<ListContext> {

    private int resourceId;

    public ListContextAdapter(Context context, int textViewResourceId, List<ListContext> object){
        super(context,textViewResourceId,object);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView , ViewGroup parent){
        ListContext listContext = getItem(position);

        View view;
        ViewHolder viewHolder;
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.paraName = view.findViewById(R.id.para_name);
            viewHolder.paraValue = view.findViewById(R.id.para_value);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.paraName.setText(listContext.getName());
        viewHolder.paraValue.setText(listContext.getValue());
        return view;
    }


    class ViewHolder{
        TextView paraName;
        TextView paraValue;
    }
}
*/

public class ListContextAdapter extends BaseAdapter {

    private Context mContext;
    private LinkedList<ListContext> mData;

    public ListContextAdapter() {
    }

    public ListContextAdapter(LinkedList<ListContext> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.para_item, parent, false);
            holder = new ViewHolder();
            holder.paraName =  convertView.findViewById(R.id.para_name);
            holder.paraValue =  convertView.findViewById(R.id.para_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.paraName.setText(mData.get(position).getName());
        holder.paraValue.setText(mData.get(position).getValue());
        return convertView;
    }

    //添加一个元素
    public void add(ListContext data) {
        if (mData == null) {
            mData = new LinkedList<>();
        }
        mData.add(data);
        notifyDataSetChanged();
    }

    //往特定位置，添加一个元素
    public void add(int position,ListContext data){
        if (mData == null) {
            mData = new LinkedList<>();
        }
        mData.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(ListContext data) {
        if(mData != null) {
            mData.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if(mData != null) {
            mData.remove(position);
        }
        notifyDataSetChanged();
    }

    public void change(int position,ListContext data){
        remove(position);
        add(position,data);
    }

    public void clear() {
        if(mData != null) {
            mData.clear();
        }
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView paraName;
        TextView paraValue;
    }
}
