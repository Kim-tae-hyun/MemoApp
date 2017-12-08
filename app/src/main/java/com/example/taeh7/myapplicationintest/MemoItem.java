package com.example.taeh7.myapplicationintest;

/**
 * Created by taeh7 on 2017-11-05.
 */

public class MemoItem {

    String contents;
    String timestamp;




    public MemoItem(String contents, String timestamp){
        this.contents=contents;
        this.timestamp=timestamp;
    }

    public String getContents(){

        return contents;
    }


    public String getTimestamp(){
        return timestamp;
    }





}
