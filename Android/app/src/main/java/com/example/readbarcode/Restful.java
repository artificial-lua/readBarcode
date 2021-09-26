package com.example.readbarcode;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

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
    static String str;
    android.os.Handler h;
    Context cont;

    String gp;

    String query;
    public Restful(Handler h){
        this.host = Hosts.url;
        this.path = Hosts.path;
        this.port = Hosts.port;
        this.cont = cont;
        this.h = h;
    }

    public void get(int choose, String query) {
        log("function get()");
        this.c = choose;
        this.query = query;
        this.gp = "get";
        Connection conn = new Connection();
        conn.start();
    }

    class Connection extends Thread{

        @Override
        public void run(){
            super.run();
            try{
                if (gp == "get"){
                    log("run - get");
                    HttpURLConnection conn;
                    URL url = new URL("http", "barcode.project-geek.cc", Hosts.port, path[c] + query);

                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    log("HttpURLConnection Config Complete");

                    InputStream is = conn.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    log("Start Connection");
                    BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    log("End Connection");
                    String result;
                    log("Start Parsing Packet");
                    while((result = br.readLine()) != null){
                        sb.append(result + "\n");
                    }
                    log("End Parsing Packet");

                    str = sb.toString();
                    h.sendEmptyMessage(1);
                    log("end get");
                }
            }catch (Exception e){
                System.out.println(e);
            }
        }

    }


    public static String getStr(){
        return str;
    }

    public void test(){
        System.out.println("url:" + Hosts.url);
    }

    public void log(String s){
        System.out.println(s);
    }
}
