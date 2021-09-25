package com.example.readbarcode;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Restful  extends Thread{
    String host;
    String[] path;
    int port;
    int c;
    String str;
    android.os.Handler h;

    String gp;

    String query;
    public Restful(String str, Handler h){
        this.host = Hosts.url;
        this.path = Hosts.path;
        this.port = Hosts.port;

        this.str = str;
        this.h = h;
    }

    public void get(int c, String query) {
        this.c = c;
        this.query = query;
        this.gp = "GET";
        this.start();
    }

    @Override
    public void run(){
        super.run();
        try{
            if (this.gp == "get"){
                HttpURLConnection conn;
                URL url = new URL("http", Hosts.url, Hosts.port, path[c] + query);

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String result;
                while((result = br.readLine()) != null){
                    sb.append(result + "\n");
                }

                this.str = sb.toString();
                this.h.sendEmptyMessage(0);
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }



    public void test(){
        System.out.println("url:" + Hosts.url);
    }
}
