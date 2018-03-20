package com.mycompany.ui.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.mycompany.ui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qkz on 2018/2/28.
 */

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

    List<BleDevice> listDevideInfo;
    ClickItem mClickItem;

    public DeviceAdapter(){
        listDevideInfo = new ArrayList<>();
    }

    public void setClickItem(ClickItem clickItem){this.mClickItem = clickItem;}

    public interface ClickItem{
        void clickItem(View view, int position);
    }

    public void setDataList(List<BleDevice> deviceInfoList){
        listDevideInfo.clear();
        listDevideInfo.addAll(deviceInfoList);
        super.notifyDataSetChanged();
    }

    public void addDevice(BleDevice bleDevice) {
        removeDevice(bleDevice);
        listDevideInfo.add(bleDevice);
    }

    public void removeDevice(BleDevice bleDevice) {
        for (int i = 0; i < listDevideInfo.size(); i++) {
            BleDevice device = listDevideInfo.get(i);
            if (bleDevice.getKey().equals(device.getKey())) {
                listDevideInfo.remove(i);
            }
        }
    }

    public void clearScanDevice() {
        for (int i = 0; i < listDevideInfo.size(); i++) {
            BleDevice device = listDevideInfo.get(i);
            if (!BleManager.getInstance().isConnected(device)) {
                listDevideInfo.remove(i);
            }
        }
    }

    public BleDevice getScanDevice(int position){
        if(position < listDevideInfo.size()){
            return listDevideInfo.get(position);
        }
        return null;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ble,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        BleDevice deviceInfo = listDevideInfo.get(position);
        final BluetoothDevice device = deviceInfo.getDevice();
        holder.tvSimpleDevicesName.setText(device.getName());
        holder.tvSimpleDevicesMac.setText(device.getAddress());
        holder.llSimpleDevicesRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickItem != null) {
                    mClickItem.clickItem(v,position);
                }
            }
        });
     }



    @Override
    public int getItemCount() {return listDevideInfo == null ? 0 : listDevideInfo.size();}

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvSimpleDevicesName;
        TextView tvSimpleDevicesMac;
        LinearLayout llSimpleDevicesRoot;
        public ViewHolder(View itemview) {
            super(itemview);
            tvSimpleDevicesName = itemview.findViewById(R.id.item_ble_tv_name);
            tvSimpleDevicesMac = itemview.findViewById(R.id.item_ble_tv_mac);
            llSimpleDevicesRoot = itemview.findViewById(R.id.item_lock_ll);
        }
    }
}
