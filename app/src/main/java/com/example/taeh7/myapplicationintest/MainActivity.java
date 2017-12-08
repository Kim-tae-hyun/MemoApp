package com.example.taeh7.myapplicationintest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static android.widget.AbsListView.CHOICE_MODE_MULTIPLE;
import static android.widget.Toast.makeText;
import static com.example.taeh7.myapplicationintest.R.id.fab;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView listView;
    MemoAdapter adapter;
    Boolean menuVisible = false;
    Translate translate;
    CheckTypesTask task;
    public  DBHelper dbHelper;
    FloatingActionButton floatingActionButton;
    Toolbar toolbar;
    Drawable drawable;
    private long backKeyPressedTime = 0;
    Toast toast;
    String mode="";





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001){
            try {
                mode = data.getStringExtra("mode");
                String contents = data.getStringExtra("contents");
                String timestamp = data.getStringExtra("timestamp");
                int position = data.getIntExtra("position",0);

                if (mode != null && mode.equals("create")) {
                    MemoItem item = new MemoItem(contents, timestamp);
                    adapter.addItem(item);

                    if(dbHelper == null){
                        dbHelper = new DBHelper(this,"MemoDB",null,1);
                    }
                    dbHelper.addMemoItem(item);
                }
                else if(mode != null && mode.equals("modify")){
                    MemoItem item = (MemoItem) adapter.getItem(position);
                    item.contents=contents;
                    item.timestamp=timestamp;
                    dbHelper.Update_DB(position,contents,timestamp);
                }
                else if(mode != null && mode.equals("delete")){
                    adapter.Remove(position);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            adapter.notifyDataSetChanged();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(0xFF4EA1D8);
        setSupportActionBar(toolbar);


        dbHelper = new DBHelper(this,"MemoDB",null,1);
        translate = new Translate();
        translate.setDbHelper(dbHelper);

        listView=(ListView)findViewById(R.id.listView);
        adapter = new MemoAdapter(this);
        adapter.setDB(dbHelper);
        listView.setChoiceMode(CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MemoItem item = (MemoItem) adapter.getItem(position);
                String contents = item.getContents();
                String timestamp = item.getTimestamp();
                Intent intent = new Intent(getApplicationContext(),MemoInputActivity.class);
                intent.putExtra("mode","modify");
                intent.putExtra("contents",contents);
                intent.putExtra("timestamp",timestamp);
                intent.putExtra("position",position);
                startActivityForResult(intent,1001);
            }
        });

        translate.SetAdapter(adapter);
        Cursor cursor = dbHelper.load_DB();
        for( int i = 0; i <cursor.getCount(); i++){
            cursor.moveToNext();//0부터니까
            String memo_contents = cursor.getString(0);
            String memo_timestamp = cursor.getString(1);
            MemoItem item =new MemoItem(memo_contents,memo_timestamp);
            adapter.addItem(item);
        }
        adapter.notifyDataSetChanged();

        floatingActionButton = (FloatingActionButton)findViewById(fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MemoInputActivity.class);
                intent.putExtra("mode","create");
                //intent.putExtra("position",positionincreate);
                //positionincreate++;
                startActivityForResult(intent,1001);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //초록색 안드로이드 인듯
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if(adapter.getVisibility()==true){
                fordelete(false,View.VISIBLE,drawable);

            }
            else {

                if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                    backKeyPressedTime = System.currentTimeMillis();
                    showGuide();
                    return;
                }
                if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                    finish();
                }
                toast.cancel();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);

        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_delete).setVisible(menuVisible);

        return true;

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


        }

        if(id == R.id.action_delete){

            AlertDialog.Builder d = new AlertDialog.Builder(this);
            d.setTitle("삭제");
            d.setMessage("선택한 메모를 삭제하시겠습니까?");

            d.setPositiveButton("예", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // process전체 종료
                    adapter.multi_delete();
                    fordelete(false,View.VISIBLE,drawable);
                    Toast.makeText(getApplicationContext(),"선택한 메모가 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                }
            });
            d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            d.show();

        }

        return super.onOptionsItemSelected(item);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();


        if (id == R.id.Delete) {
            drawable = toolbar.getNavigationIcon();
            fordelete(true,View.GONE,null);

        } else if (id == R.id.Manual) {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();     //닫기
                }
            });
            alert.setTitle("메뉴얼");
            alert.setMessage("1. 뒤로버튼 클릭시 자동저장.\n" +
                    "2. 작성된 메모 클릭 시 수정가능.\n" +
                    "3. 기본 포트번호는 5010으로 지정됨.\n" +
                    "4. connect 클릭 시 PC와 연결가능.");

            alert.show();



        } else if (id == R.id.Setting) {

            EditText editText1;
            EditText editText2;


            // Context 얻고, 해당 컨텍스트의 레이아웃 정보 얻기
            Context context = getApplicationContext();
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(LAYOUT_INFLATER_SERVICE);
            Cursor cursor = dbHelper.load_IPPORT();
            Log.d(null,"2");
            cursor.moveToNext();
            Log.d(null,"3");
            String IPNumber = cursor.getString(0);
            Log.d(null,"4");
            String PortNumber = cursor.getString(1);

            // 레이아웃 설정
            View layout = inflater.inflate(R.layout.alert_input, (ViewGroup)findViewById(R.id.popup_root));
            editText1=(EditText)layout.findViewById(R.id.alert_editText1);
            editText2=(EditText)layout.findViewById(R.id.alert_editText2);
            editText1.setText(PortNumber);
            editText2.setText(IPNumber);

            AlertDialog dialog = create_inputDialog(editText1, editText2);

            // Input 소프트 키보드 보이기
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            // AlertDialog에 레이아웃 추가
            dialog.setView(layout);
            dialog.show();

        } else if (id == R.id.Connect) {

            //0번째 데이터 베이스에서 값이 있으면 connect 셋팅할 필요 x
            Log.d(null,"1");
            Cursor cursor = dbHelper.load_IPPORT();
            Log.d(null,"2");
            cursor.moveToNext();
            Log.d(null,"3");
            String IPNumber = cursor.getString(0);
            Log.d(null,"4");
            String PortNumber = cursor.getString(1);
            Log.d(null,"5");

            // if ( IPNumber == null && PortNumber == null)
            if(IPNumber.equals("null")){
                makeText(getApplicationContext(),"설정을 확인하세요",Toast.LENGTH_LONG).show();
            }
            else {

                task = new CheckTypesTask();
                task.Setcontext(getApplicationContext());
                task.execute();

                translate.SettingPORTIP(IPNumber,PortNumber);
                Log.d(null,"6");
                translate.start();
                translate.worker.start();
                Log.d(null,"7");


               }
        }

        else {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }


    private AlertDialog create_inputDialog(final EditText editText1, final EditText editText2) {
        AlertDialog dialogBox = new AlertDialog.Builder(this)

                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       String PortNumber = editText1.getText().toString();
                       String IPNumber = editText2.getText().toString();
                        dbHelper.Update_IPPORT(0,IPNumber,PortNumber);
                    makeText(getApplicationContext(),"설정 되었습니다.",Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 아니오 버튼 눌렀을때 액션 구현
                    }
                }).create();
        return dialogBox;
    }


    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        Context context;

        ProgressDialog asyncDialog = new ProgressDialog(
                MainActivity.this);



        @Override
        protected void onPreExecute() {
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("연결중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                for (int i = 0; i < 5; i++) {
                    if(!(translate.Checkvariable() ==null)){
                        break;
                    }
                    Thread.sleep(1200);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();

            if(translate.Checkvariable()!=null){
                makeText(context,"연결성공",Toast.LENGTH_LONG).show();
                translate.Check=null;


            }
            else{
                makeText(context,"연결실패",Toast.LENGTH_LONG).show();

            }
            asyncDialog.setCanceledOnTouchOutside(true);
            super.onPostExecute(result);
        }


        public void Setcontext(Context context){
            this.context=context;
        }
    }


    public void fordelete(boolean value, int View, Drawable drawable){

        adapter.ChangeVisibility(value);
        menuVisible = !menuVisible;
        toolbar.setNavigationIcon(drawable);
        floatingActionButton.setVisibility(View);
        invalidateOptionsMenu();
        adapter.notifyDataSetChanged();

    }

    public void showGuide() { toast = Toast.makeText(getApplicationContext(), "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT); toast.show(); }




}


