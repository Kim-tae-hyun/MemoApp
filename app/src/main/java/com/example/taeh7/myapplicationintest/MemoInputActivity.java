package com.example.taeh7.myapplicationintest;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by taeh7 on 2017-11-05.
 */

public class MemoInputActivity extends AppCompatActivity {

    String mode;
    EditText editText;
    TextView textView2;
    TextView textView;
    int position;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd. aa hh시 mm분");
    InputMethodManager imm;
    private Typeface mTypeface;
    ImageView imageView;


    @Override
    public void onBackPressed() {
        String contents=null;
        String timestamp=null;

        if(reCall(contents, timestamp)==false ){
            Toast.makeText(this, "작성된 내용이 없어 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
        else if(editText.getVisibility() ==View.GONE){

        }
        else {
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

        }
        super.onBackPressed();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_input);

        mTypeface = Typeface.createFromAsset(getAssets(), "HoonMakdaeyunpilR.ttf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        setGlobalFont(root);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF4EA1D8));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editText = (EditText)findViewById(R.id.editText);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView = (TextView) findViewById(R.id.TextView);


        imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCall();
            }
        });

        Intent intent = getIntent();
        mode = intent.getStringExtra("mode");


        //Unit TEST 메모장 작성시 (true)
        if (mode != null && mode.equals("create")) {

            Date date = new Date();
            String timestamp = dateFormat.format(date);
            textView2.setText(timestamp);
            editText.setVisibility(View.VISIBLE);
            editText.setMovementMethod(new ScrollingMovementMethod());
            textView.setVisibility(View.GONE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            imageView.setVisibility(View.GONE);

        }
        else if(mode.equals("modify")) {
            String contents = intent.getStringExtra("contents");
            String timestamp = intent.getStringExtra("timestamp");
            position = intent.getIntExtra("position",0);


            textView.setText(contents);
            textView.setMovementMethod(new ScrollingMovementMethod());
            editText.setText(contents);
            editText.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setVisibility(View.VISIBLE);
                    textView.setVisibility(View.GONE);
                    editText.requestFocus();
                    editText.setSelection(editText.length());
                    editText.setMovementMethod(new ScrollingMovementMethod());
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


                    imageView.setVisibility(View.GONE);
                }
            });
            textView2.setText(timestamp);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.input_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home ){
            String contents=null;
            String timestamp=null;

            if(reCall(contents, timestamp)==false ){
                Toast.makeText(this, "작성된 내용이 없어 저장되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            }
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean reCall(String contents, String timestamp){

        contents = editText.getText().toString();
        String test=contents;
        test = contents.trim();
        timestamp = textView2.getText().toString();
        //Unit TEST 메모장에 생성 후 내용이 없을시 저장되지 않는다. (true)
        if(test.getBytes().length <=0){

            return false;
        }else {
            Intent intent = new Intent();
            intent.putExtra("mode", mode);
            intent.putExtra("contents", contents);
            intent.putExtra("timestamp", timestamp);
            intent.putExtra("position", position);
            setResult(Activity.RESULT_OK, intent);
            return true;
        }
    }

    public void DialogCall (){
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("삭제하시겠습니까?");
        d.setPositiveButton("예", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (mode != null && mode.equals("create")) {

                    finish();
                }
                //Unit TEST 기존 메모장을 클릭하여 삭제 (true)
                else if(mode != null && mode.equals("modify")) {
                    Intent intent = new Intent();
                    intent.putExtra("mode", "delete");
                    intent.putExtra("contents", "");
                    intent.putExtra("timestamp", "");
                    intent.putExtra("position", position);

                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }
                Toast.makeText(getApplicationContext(),"삭제되었습니다.",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        d.show();
    }


    void setGlobalFont(ViewGroup root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView)child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup)child);
        }
    }
}