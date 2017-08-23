package com.ericwyn.seanote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ericwyn.seanote.R;
import com.ericwyn.seanote.adapter.MainRvAdapter;
import com.ericwyn.seanote.entity.Note;
import com.ericwyn.seanote.server.DataServer;
import com.ericwyn.seanote.server.SeafileServer;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.ericwyn.seanote.activity.PreviewActivity.CREATE_NOTE_POSITION;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG="MainActivity";
    private static final int REQUEST_CODE_PRE_ACT=10086;
    private static boolean repoIdCheckFlag=false;
    private RecyclerView notesRecyclerView;
    private MainRvAdapter mainRvAdapter;

    private List<Note> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_act_toolbar_title);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PreviewActivity.class);
                intent.putExtra("create",true);
                intent.putExtra("dir","");
                startActivityForResult(intent,REQUEST_CODE_PRE_ACT);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        notesRecyclerView=(RecyclerView)findViewById(R.id.noteRecyclerView_MainAct);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));//这里用线性显示 类似于listview
        data=DataServer.loadData();
        mainRvAdapter=new MainRvAdapter(data);
        mainRvAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent=new Intent(MainActivity.this,PreviewActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("position",i);
                bundle.putString("obj", JSON.toJSON((Note)baseQuickAdapter.getItem(i)).toString());
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE_PRE_ACT);
            }
        });
        mainRvAdapter.openLoadAnimation();
        mainRvAdapter.setUpFetchEnable(true);
        mainRvAdapter.setUpFetching(true);
        mainRvAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {
                mainRvAdapter.setNewData(DataServer.loadData());
            }
        });

        notesRecyclerView.setAdapter(mainRvAdapter);

        //验证repo_id 是否是错误的或者不存在的
        checkoutRepoID();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE_PRE_ACT){
            if(resultCode==RESULT_OK){
                int position=data.getIntExtra("position",-1);
                Log.d(TAG,"得到了返回");
                if(position!=-1){
                    if(position==CREATE_NOTE_POSITION){
                        Note note_temp=JSON.parseObject(data.getStringExtra("obj"),Note.class);
                        this.data.add(0,note_temp);
                        synchronized (this){
                            mainRvAdapter.notifyDataSetChanged();
                        }
                    }else {
                        Note note_temp=JSON.parseObject(data.getStringExtra("obj"),Note.class);
                        this.data.remove(position);
                        this.data.add(position,note_temp);
                        synchronized (this){
                            mainRvAdapter.notifyDataSetChanged();
                        }
                    }
                }else {
//                    this.data=DataServer.loadData();
//                    synchronized (this){
//                        mainRvAdapter.notifyDataSetChanged();
//                    }
                    Log.d(TAG,"无notifyData,没有获取position");
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkoutRepoID(){
        if(!repoIdCheckFlag){
            if(!SeafileServer.getRepo_id().equals("null")){
                SeafileServer.getApi().getLibraryInfo(SeafileServer.getClient(), SeafileServer.getToken(), SeafileServer.getRepo_id(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this,R.string.network_error,Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.isSuccessful()){
                            Log.d(TAG,"repo_id验证成功");
//                        Toast.makeText(MainActivity.this,R.string.)
                        }else {
                            Looper.prepare();
                            Toast.makeText(MainActivity.this,R.string.main_act_lib_expired,Toast.LENGTH_SHORT).show();
                            SeafileServer.createSeanoteLib(MainActivity.this);
                            Looper.loop();
                        }
                    }
                });
            }else {
                SeafileServer.createSeanoteLib(MainActivity.this);
            }
            repoIdCheckFlag=true;
        }
    }

}
