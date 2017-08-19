package com.ericwyn.seanote.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ericwyn.seafile.SeafileApi;
import com.ericwyn.seafile.jsonObject.Library;
import com.ericwyn.seanote.R;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * some SefailServer
 * Created by ericwyn on 17-8-17.
 */

public class SeafileServer {
    private static OkHttpClient client=new OkHttpClient();
    private static SeafileApi api;
    private static final String TAG="SeafileServer";

    private static String server_url;
    private static String file_server_url;
    private static String userMail;
    private static String password;
    private static String token;
    //is createSeaNoteLibrary?
    private static String repo_id;


    private static SharedPreferences initdata;

    public static void checkServer(String server_url, Callback callback){
        String fileServerUrl;
        if(server_url.endsWith("8000")){
            fileServerUrl=server_url.replaceAll("8000","8002");
        }else {
            fileServerUrl=server_url+"/seafhttp";
        }
        api=new SeafileApi(server_url,fileServerUrl);
        try {
            api.getServerInformation(client,callback);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
    }

    public static void getUserToken(String account, String pw, Callback callback){
        if(server_url!=null){
            api.obtainAuthToken(client,account,pw,callback);
        }
    }

    public static void saveServerUrl(String s_url){
        server_url=s_url;
    }

    public static String getServer_url() {
        return server_url;
    }

    /**
     * 新建一个 seanote 专用的library
     * @return
     */
    public static void createSeanoteLib(final Context context){
//        loadInitData(context);
        api.listLibraries(client, token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(context,context.getText(R.string.network_error),Toast.LENGTH_SHORT).show();
                Looper.loop();
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG,"token="+token);
                final List<Library> libraries= JSON.parseArray(response.body().string(), Library.class);
                boolean createLibFlag=true;
                for (Library library:libraries){
                    if(library.getName().equals("seanote")){
                        Log.d(TAG,"repo_id = "+library.getId());
                        initdata.edit().putString("repo_id",library.getId()).apply();
                        createLibFlag=false;
                        Looper.prepare();
                        Toast.makeText(context,context.getText(R.string.main_act_createlib_havelib),Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;
                    }
                }
                if(createLibFlag){
                    //create a new Library of seanote
                    try {
                        api.createNewLibrary(client, token, "seanote", "auto create seanote lib by Seanote", null, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Looper.prepare();
                                Toast.makeText(context,context.getText(R.string.network_error),Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if(response.isSuccessful()){
                                    String res=response.body().string();
                                    initdata.edit().putBoolean("have_seanote_lib",true).apply();
                                    initdata.edit().putString("repo_id", JSON.parseObject(res).getString("repo_id")).apply();
                                    Log.d(TAG,"repo_id = "+JSON.parseObject(res).getString("repo_id"));
                                    Looper.prepare();
                                    Toast.makeText(context,context.getText(R.string.main_act_createlib_success),Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }else {
                                    Looper.prepare();
                                    Toast.makeText(context,context.getText(R.string.main_act_createlib_err),Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG,e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * 查找关键的初始化参数，如果查找成功的话就赋值各个static 变量
     *
     * @param context
     * @return
     */
    public static boolean findInitData(Context context){
        if(initdata == null){
            initdata = context.getSharedPreferences("initdata", MODE_PRIVATE);
        }
        token=initdata.getString("token","null");
        if(!token.equals("null")){
            loadInitData(context);
            return true;
        }else {
            return false;
        }
    }

    public static void loadInitData(Context context){
        if(initdata==null){
            initdata=context.getSharedPreferences("initdata",MODE_PRIVATE);
        }
        token=initdata.getString("token","get token form sp error");
        server_url=initdata.getString("server_url","get server_url form sp error");
        userMail=initdata.getString("mail","get mail form sp error");
        password=initdata.getString("pw","get pw form sp error");

        repo_id=initdata.getString("repo_id","null");

        if(server_url.endsWith("/")){
            server_url=server_url.substring(0,server_url.length()-1);
        }
        if(!server_url.endsWith("error")){
            if(server_url.matches("^[http|https](.+?):[0-9]{0,6}$")){
                String[] temp=server_url.split(":");
                file_server_url=temp[0]+":8082";
            }else {
                file_server_url=server_url+"/seafhttp";
            }
        }
        if(api==null){
            api=new SeafileApi(server_url,file_server_url);
        }

        Log.d(TAG,"success to find the initData , an set the static parameter");
    }

    /**
     * InitAct 中 保存各种关键参数
     *
     * @param server_url_
     * @param mail_
     * @param pw_
     * @param token_
     */
    public static void saveInitData(String server_url_,String mail_,String pw_,String token_,Context context){
        SharedPreferences.Editor editor=initdata.edit();
        editor.putString("server_url",server_url_);
        editor.putString("mail",mail_);
        editor.putString("pw",pw_);
        editor.putString("token",token_);
        editor.apply();
        loadInitData(context);
        Log.d(TAG,"saveInitData");
    }


    public static String getToken() {
        return token;
    }

    public static SeafileApi getApi() {
        return api;
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static String getRepo_id() {
        return repo_id;
    }
}
