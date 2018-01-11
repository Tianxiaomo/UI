package com.mycompany.ui;

/**
 * Created by qkz on 2017/12/16.
 */

public class GetParaSplit {
    private String getpara;
    GetParaSplit(String getpara){
        this.getpara = getpara;
    }
    public String GetPara(String para) {
        int i=0;
        String getgrape[] = getpara.split(" ");
        for (String element : getgrape){
            if(para.equals(getgrape[i]))break;
            i++;
        }
        return getgrape[i+1];
    }
}
