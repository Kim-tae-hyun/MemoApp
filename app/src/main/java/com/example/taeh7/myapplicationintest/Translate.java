package com.example.taeh7.myapplicationintest;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by taeh7 on 2017-11-17.
 */

public class Translate extends AppCompatActivity {

    private Socket socket;
    String PortNumber = null;
    String IPNumber = null;
    MemoAdapter adapter;
    Boolean Check = null;
    public DBHelper dbHelper;
    String content = "";
    Thread worker;


    public void start() {


        worker = new Thread() {

        public void run() {
            /*//Unit TEST IP 와 PORT 를 확인 (true)
            Log.d("Unit TEST ", " IP number : " + IPNumber);
            Log.d("Unit TEST ", " PortNumber : " + PortNumber);
            */
            //DataInputStream in = null;
            BufferedReader in=null;
            Log.d(TAG, "Thread run method before create socket");

            socket = new Socket();
            SocketAddress addr;

            try {
                addr = new InetSocketAddress(IPNumber, Integer.parseInt(PortNumber));
                Log.d(TAG, "create socket");


                socket.setSoTimeout(2000);
                socket.connect(addr, 3000);
                Check = true;


            } catch (Exception e) {

                e.printStackTrace();
                Check = false;
            }

            if (Check) {


                try {
                    //Unit TEST for Streamdata (false)
                    //in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
                    // Unit TEST for Streamdata (true)
                    in=new BufferedReader(new InputStreamReader(socket.getInputStream(),"EUC_KR"));


                } catch (IOException e1) {
                    e1.printStackTrace();
                }


                String firstLine = null;
                try {
                    firstLine = in.readLine();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                String inputstring;
                inputstring = firstLine.substring(firstLine.indexOf(".txt") + 4);

                try {

                    do {
                        inputstring += "\n";
                        content += inputstring;

                    } while ((inputstring = in.readLine()) != null);

                    //Log.d("Unit TEST ","content is : "+ content);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                final String finalCompleted = content;
                content = "";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd. aa hh시 mm분");
                        Date date = new Date();
                        String timestamp = dateFormat.format(date);
                        String content = finalCompleted;

                        MemoItem item = new MemoItem(content, timestamp);
                        dbHelper.addMemoItem(item);
                        adapter.addItem(item);
                        adapter.notifyDataSetChanged();
                    }
                });


                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            } else {
                Check = null;
            }

        }

    };

}



    public void SettingPORTIP(String IPNumber,String PortNumber){
        this.PortNumber = PortNumber;
        this.IPNumber = IPNumber;
    }

    public void SetAdapter(MemoAdapter adapter){
        this.adapter=adapter;
    }


    public Boolean Checkvariable(){
        return Check;
    }

    public void setDbHelper(DBHelper dbHelper){
        this.dbHelper=dbHelper;
    }









}

