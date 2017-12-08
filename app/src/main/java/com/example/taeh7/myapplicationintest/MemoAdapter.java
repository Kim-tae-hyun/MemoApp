package com.example.taeh7.myapplicationintest;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by taeh7 on 2017-11-05.
 */

public class MemoAdapter extends BaseAdapter {
    ArrayList<MemoItem> items = new ArrayList<MemoItem>();
    ArrayList<Boolean> checkboxstate = new ArrayList<Boolean>();

    Context mContext;
    private boolean mClick = false;
    public  DBHelper dbHelper;


    public MemoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }



    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(MemoItem item) {
        items.add(item);
        checkboxstate.add(false);
    }

    public void clear() {
        items.clear();
    }

    public void ChangeVisibility(boolean mClick){
       this.mClick= mClick;
    }

    public boolean getVisibility(){

        return mClick;
    }

    public void Remove(int i){
        dbHelper.Delete_DB(i);
        items.remove(i);
        checkboxstate.remove(i);


    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*
        // UNIT TEST FOR CHECKBOX
        MemoItemView itemView = new MemoItemView(mContext);

        if(itemView.checkBox.isChecked()){
            Log.d("Unit TEST","unit test for checkBox2 : true");
        }else{
            Log.d("Unit TEST","unit test for checkBox2 : false");
        }

        */

        View gridView;
        if(convertView==null){

            gridView=new View(mContext);

            gridView=inflater.inflate(R.layout.memo_item, null);


        }else{
            gridView = (View) convertView;

        }

        TextView textView =(TextView)gridView.findViewById(R.id.textView);
        textView.setTypeface(Typeface.createFromAsset(gridView.getContext().getAssets(), "HoonMakdaeyunpilR.ttf"));

        String contents = items.get(position).getContents();
        int a = contents.indexOf('\n');
            Log.d("start","in");
            Log.d("start",Integer.toString(a));
            if (a > 0) {
                Log.d("contents :::",contents);
                contents = contents.substring(0, a);
                Log.d("contents :::",contents);
                Log.d("contents :::",contents);
                contents = contents + " ...";

            }
            else if(contents.length() >=13) {

                contents = contents.substring(0, 13);
                contents = contents + " ...";

            }

        textView.setText(contents);
        TextView textView2 =(TextView)gridView.findViewById(R.id.textView2);
        textView2.setText(items.get(position).getTimestamp());
        CheckBox checkBox=(CheckBox)gridView.findViewById(R.id.checkBox3);
        checkBox.setChecked(false);
        checkBox.setVisibility(View.GONE);

        if(mClick==true){
            checkBox.setVisibility(View.VISIBLE);


            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()){
                        checkboxstate.set(position,true);
                        v.setSelected(true);


                    }
                    else{
                        checkboxstate.set(position,false);
                        v.setSelected(false);
                    }
                }
            });
        }
        else{
            checkBox.setVisibility(View.GONE);
        }



        return gridView;
    }

    public void multi_delete(){

        for(int i = 0 ; i < checkboxstate.size(); ){
            if(checkboxstate.get(i)==true){
                // UNIT TEST FOR CHECKBOX (false)
                //Log.d("Unit TEST","unit test for checkBox : true");
                Remove(i);

            }
            else{
                i++;
            }
        }


        for(int i = 0 ; i< items.size(); i++){
            Log.d(null,"for loop items : " +i);
        }




    }

    public void setDB(DBHelper dbHelper){
        this.dbHelper=dbHelper;
    }

}
