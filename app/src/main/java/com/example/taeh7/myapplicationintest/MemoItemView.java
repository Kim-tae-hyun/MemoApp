package com.example.taeh7.myapplicationintest;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by taeh7 on 2017-11-05.
 */

public class MemoItemView extends LinearLayout  {
    Context mContext;

    TextView textView;
    TextView textView2;
    CheckBox checkBox;



    public MemoItemView(Context context){
        super(context);

        mContext = context;
        init();
    }

    public MemoItemView(Context context, AttributeSet attrs){
        super(context, attrs);

        mContext = context;
        init();
    }

    private void init() {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.memo_item,this,true);



        textView = (TextView)findViewById(R.id.textView);
        textView2 = (TextView)findViewById(R.id.textView2);
        checkBox = (CheckBox)findViewById(R.id.checkBox3);

/*
        // UNIT TEST FOR CHECKBOX (false)
        if(checkBox.isChecked()){
            Log.d("Unit TEST","unit test for checkBox1 : true");
        }else{
            Log.d("Unit TEST","unit test for checkBox1 : false");
        }
*/

    }






}

