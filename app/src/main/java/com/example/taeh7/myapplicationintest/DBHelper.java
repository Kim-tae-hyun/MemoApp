package com.example.taeh7.myapplicationintest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by taeh7 on 2017-11-19.
 */

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    int _id=1;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;


    }

    /**
     *  Database가 존재하지 않을 때, 딱 한번 실행된다.
     *  DB를 만드는 역할을 한다.
     *  @param db
     */




    @Override
    public void onCreate(SQLiteDatabase db) {

        String tableName = "Memo_table";
        //String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, contents text, timestamp text)";
        String sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY, contents text, timestamp text)";


        db.execSQL(sql);
        tableName="Memo_IPPORT";
        sql = "create table if not exists " + tableName + "(_id integer PRIMARY KEY autoincrement, ip text, port text)";

        db.execSQL(sql);
        sql = "insert into Memo_IPPORT(ip, port) values(?, ?)";

        String IPNumber= "null";
        String PortNumber = "null";

        db.execSQL(sql,
                new Object[]{
                        IPNumber,
                        PortNumber
                });


    }

    /**
     * * Application의 버전이 올라가서
     * * Table 구조가 변경되었을 때 실행된다.
     * * @param db
     * * @param oldVersion
     * * @param newVersion
     * */



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Toast.makeText(context,"버전이 올라갔습니다.",Toast.LENGTH_LONG).show();
    }





    public void addMemoItem(MemoItem memoItem){

        //1. 쓸 수 있는 DB 객체를 가져온다.
        SQLiteDatabase db = getWritableDatabase();
        //SELECT MAX(id) FROM <tablename>
        String sql = null;

        Cursor cursor=null;
        if(db != null){
             sql = "select contents, timestamp from Memo_table";
            cursor = db.rawQuery(sql,null);

        }
        cursor.moveToNext();
        _id =cursor.getCount();
        _id++;


        //2. MemoItem Data를 Insert 한다.
        // _id는 자동으로 증가하기 때문에 넣지 않는다.

        sql = "insert into Memo_table(_id, contents, timestamp) values(?, ?, ?)";

        db.execSQL(sql,
                new Object[]{
                        _id,
                        memoItem.getContents(),
                        memoItem.getTimestamp()
                });

        _id++;
        Log.d("addMemoItem _id","addMemoItem _id : "+_id);


    }


    public Cursor load_DB(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor=null;
        if(db != null){
            String sql = "select contents, timestamp from Memo_table";
            cursor = db.rawQuery(sql,null);

        }
        return cursor;

    }


    public Cursor load_IPPORT(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor=null;
        if(db != null){
            String sql = "select ip, port from Memo_IPPORT";
            cursor = db.rawQuery(sql,null);

        }
        return cursor;

    }

    public void Delete_DB(int position){
        SQLiteDatabase db = getWritableDatabase();
        position= position+1;
        String number = Integer.toString(position);
        Log.d("Delete_DB","number : "+position);



        if(db != null){
            String sql = "delete from Memo_table where _id = "+number;
            db.execSQL(sql);
            //UNIT TEST Multi Delete (false)
            //Log.d("Unit TEST ","Delete after _id : "+_id);


            //CheckBoxstate 와 DB 의 Index 를 맞추기 위해 autoincrement 를 제거하고 이와 같은 sql 문을 추가함.
            //UNIT TEST Multi Delete (true)
           sql = "update Memo_table" + " set _id = _id - 1 where _id > "+number;
            // PRIMARY KEY 를 수정하는 과정
            db.execSQL(sql);

            Cursor cursor=null;
            if(db != null){
                sql = "select contents, timestamp from Memo_table";
                cursor = db.rawQuery(sql,null);

            }
            cursor.moveToNext();
            _id =cursor.getCount();
            //UNIT TEST Multi Delete
            //Log.d("Unit TEST ","Delete after _id : "+_id);
            //END
            _id++;

        }
    }





    public void Update_DB(int position, String contents, String timestamp){
        SQLiteDatabase db = getWritableDatabase();
        position= position+1;
        String number = Integer.toString(position);
        if(db != null){



            String sql = "update Memo_table set contents = '"+contents +"' where _id = "+number ;
            db.execSQL(sql);
            sql = "update Memo_table set timestamp = '"+timestamp +"' where _id = "+number ;
            db.execSQL(sql);

        }
    }

    public void Update_IPPORT(int position, String ip, String port){
        SQLiteDatabase db = getWritableDatabase();
        String number = Integer.toString(position+1);
        if(db != null){



            String sql = "update Memo_IPPORT set ip = '"+ip +"' where _id = "+number ;
            db.execSQL(sql);
            sql = "update Memo_IPPORT set port = '"+port +"' where _id = "+number ;
            db.execSQL(sql);

        }
    }


}