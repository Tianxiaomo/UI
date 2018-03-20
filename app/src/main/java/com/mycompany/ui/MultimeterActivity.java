package com.mycompany.ui;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mycompany.ui.Utils.Book;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qkz on 2018/1/22.
 */

public class MultimeterActivity extends AppCompatActivity{
    @BindView(R.id.btn_multi)
    TextView btnMulti;


    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private LocalReceiver localReceiver;
    private LocalBroadcastManager localBroadcastManager;

    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.layout_multimeter);

        ButterKnife.bind(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver,intentFilter);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.mycompany.ui.LOCAL_BROADCAST");
        localReceiver = new LocalReceiver();
        localBroadcastManager.registerReceiver(localReceiver,intentFilter);

        dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,5);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        localBroadcastManager.unregisterReceiver(localReceiver);
    }

    @OnClick({R.id.btn_multi,R.id.btn_add,R.id.btn_delete,R.id.btn_change,R.id.btn_add_litepal})
    public void onViewClicked(View view){
        switch (view.getId()){
            case R.id.btn_multi:
                dbHelper.getWritableDatabase();
                break;
            case R.id.btn_add:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                //组装数据
                contentValues.put("name","The Da Vinci Code");
                contentValues.put("author","Dan Brown");
                contentValues.put("pages",454);
                contentValues.put("price",199.1);
                db.insert("Book",null,contentValues);
                contentValues.clear();
                //组装第二条
                contentValues.put("name","The Da dadf Code");
                contentValues.put("author","Dan asdf");
                contentValues.put("pages",524);
                contentValues.put("price",13.1);
                db.insert("Book",null,contentValues);
                Toast.makeText(this,"add content",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_delete:
                Connector.getDatabase();
                break;
            case R.id.btn_change:
                SQLiteDatabase db_1 = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("price",1.11);
                db_1.update("Book",values,"name = ?",new String[] {"The Da Vinci Code"});
                Toast.makeText(this,"change content",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_add_litepal:
                Book book = new Book();
                book.setAuthor("hgh");
                book.setName("me");
                book.setPages(100);
                book.setPrice(10.9);
                book.save();
//改
//                Book book1 = new Book();
//                book1.setPrice(100.9);
//                book.updateAll("name = ?","me");

                //删
//                DataSupport.deleteAll(Book.class,"price < ?","100");
                //插
                List<Book> books = DataSupport.findAll(Book.class);
                //查特定列
                List<Book> books1 = DataSupport.select("name","author").find(Book.class);
                //查指定结果
                List<Book> books2 = DataSupport.where("pages > ?","400").find(Book.class);
                //排序，order desc降序，asc升序
                List<Book> books3 = DataSupport.order("parice desc").find(Book.class);
                //查 2,3,4条
                List<Book> books4 = DataSupport.limit(3).offset(1).find(Book.class);
                break;
            default:
                break;
        }
    }


    public void saveSharedPreference(){
        SharedPreferences.Editor data = getSharedPreferences("data",MODE_PRIVATE).edit();
//        SharedPreferences data = getPreferences(MODE_PRIVATE); //类名
//        SharedPreferences data = PreferenceManager.getDefaultSharedPreferences(this); //包名
        data.putBoolean("switch",true);
        data.putFloat("switch", (float) 1.1);
        data.apply();
    }

    public void loadSharedPreference(){
        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        float Switch = pref.getFloat("switch",0);
        Toast.makeText(this,Switch+"",Toast.LENGTH_LONG).show();
    }

    public void save(){
        String string = "data to save !!!";
        FileOutputStream out = null;
        BufferedWriter writer = null;

        try {
            out = openFileOutput("data",Context.MODE_APPEND);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(string);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String load(){
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();

        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null){
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    class NetworkChangeReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            Toast.makeText(context,"network changes",Toast.LENGTH_LONG).show();
            abortBroadcast();
        }
    }

    class LocalReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context,"local",Toast.LENGTH_LONG).show();
        }
    }
}
