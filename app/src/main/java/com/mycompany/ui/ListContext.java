package com.mycompany.ui;

/**
 * Created by qkz on 2017/12/11.
 */

public class ListContext {
    private String valueNew = null;
    private String name = null;
    private String valueOld = null;

    public ListContext(String name, String value){
        this.name = name;
        this.valueOld = value;
        this.valueNew = value;
    }

    public ListContext(String name,String valueNew, String value){
        this.name = name;
        this.valueOld = value;
        this.valueNew = valueNew;
    }

    public String getName() {
        return name;
    }
    public String getValueOld(){
        return valueOld;
    }
    public String getValueNew() {
        return valueNew;
    }
}
