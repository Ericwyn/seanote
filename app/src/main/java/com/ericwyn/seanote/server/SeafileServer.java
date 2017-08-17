package com.ericwyn.seanote.server;

import com.ericwyn.seafile.SeafileApi;

import okhttp3.Callback;
import okhttp3.OkHttpClient;

/**
 * some SefailServer
 * Created by ericwyn on 17-8-17.
 */

public class SeafileServer {
    public static OkHttpClient client=new OkHttpClient();
    private static SeafileApi api;
    private static String server_url;

    public static void checkServer(String server_url, Callback callback){
        String fileServerUrl;
        if(server_url.endsWith("8000")){
            fileServerUrl=server_url.replaceAll("8000","8002");
        }else {
            fileServerUrl=server_url+"/seafhttp";
        }
        api=new SeafileApi(server_url,fileServerUrl);
        api.getServerInformation(client,callback);
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
}
