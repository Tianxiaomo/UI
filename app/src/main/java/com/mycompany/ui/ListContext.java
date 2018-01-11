package com.mycompany.ui;

import android.view.LayoutInflater;

/**
 * Created by qkz on 2017/12/11.
 */

public class ListContext {
    private String name = null;
    private String value = null;

    public ListContext(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public String getValue(){
        return value;
    }
}
