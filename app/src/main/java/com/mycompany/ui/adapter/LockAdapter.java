package com.mycompany.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mycompany.ui.R;

import java.util.List;

/**
 * Created by qkz on 2018/1/28.
 */

public class LockAdapter extends RecyclerView.Adapter<LockAdapter.ViewHolder> {


    List<LockInfo> listLockInfo;
    ClickItem mClickItem;
    private static Context context;

    public LockAdapter(Context context, List<LockInfo> listLockInfo){
        this.context = context;
        this.listLockInfo = listLockInfo;
    }

    public void setClickItem(ClickItem clickItem){this.mClickItem = clickItem;}

    public interface ClickItem{
        void clickItem(View view, int position);
    }

    public void setDataList(List<LockInfo> lockInfoList){
        listLockInfo.clear();
        listLockInfo.addAll(lockInfoList);
        super.notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lock,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        LockInfo lockInfo = listLockInfo.get(position);

        holder.tvLock.setText(lockInfo.getOption());
        holder.llLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickItem != null) {
                    mClickItem.clickItem(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {return listLockInfo == null ? 0 : listLockInfo.size();}

     class ViewHolder extends RecyclerView.ViewHolder{
//        @BindView(R.id.tv_oclock_time)
        TextView tvLock;
        LinearLayout llLock;
        public ViewHolder(View itemview){
            super(itemview);
//            ButterKnife.bind(this,itemview);
            tvLock = itemview.findViewById(R.id.item_lock_tv);
            llLock = itemview.findViewById(R.id.item_lock_ll);
        }
    }
}
