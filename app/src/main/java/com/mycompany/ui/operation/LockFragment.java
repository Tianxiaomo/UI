package com.mycompany.ui.operation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clj.fastble.BleManager;
import com.clj.fastble.data.BleDevice;
import com.mycompany.ui.Dialog.AllDialog;
import com.mycompany.ui.R;
import com.mycompany.ui.adapter.LockAdapter;
import com.mycompany.ui.adapter.LockInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class LockFragment extends Fragment {
    @BindView(R.id.fragment_lock_recyclerview)
    RecyclerView rv_LockSet;

    private AllDialog allDialog;
    private List<LockInfo> listLockInfo = new ArrayList<>();

    public LockFragment(){

    }

    public static LockFragment newInstance(BleDevice device) {
        LockFragment fragment = new LockFragment();
        Bundle args = new Bundle();
        args.putParcelable("bledevice",device);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            BleDevice bleDevice = getArguments().getParcelable("bledevice");
            allDialog = new AllDialog(getActivity(),bleDevice);
        }
        init();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        BleManager.getInstance().disconnectAllDevice();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_lock, container, false);
        RecyclerView reviewOclock = (RecyclerView)view.findViewById(R.id.fragment_lock_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        reviewOclock.setLayoutManager(linearLayoutManager);
        LockAdapter lockAdapter = new LockAdapter(getActivity(),listLockInfo);
        reviewOclock.setAdapter(lockAdapter);
        lockAdapter.setClickItem(new LockAdapter.ClickItem() {
            @Override
            public void clickItem(View view, int position) {
                allDialog.dialog(position);
//                switch (position){
//                    case 0:
//                        allDialog.addcardDialog();
//                        break;
//                    case 1:
//                        allDialog.delectcardDialog();
//                        break;
//                    case 2:
//                        allDialog.addpwDialog();
//                        break;
//                    case 3:
//                        allDialog.delectpwDialog();
//                        break;
//                    case 4:
//                        electricDialog();
//                        break;
//                    case 5:
//                        rssiDialog();
//                        break;
//                    default:
//                        break;
                }
        });
        return view;
    }

    //重新加载toolbar上的布局menu_home

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }


    private void init(){
        LockInfo lockInfo = new LockInfo(getResources().getString(R.string.add_password));
        listLockInfo.add(lockInfo);
        lockInfo = new LockInfo(getResources().getString(R.string.timing));
        listLockInfo.add(lockInfo);
        lockInfo = new LockInfo(getResources().getString(R.string.electric));
        listLockInfo.add(lockInfo);
        lockInfo = new LockInfo(getResources().getString(R.string.rssi));
        listLockInfo.add(lockInfo);
    }
}
