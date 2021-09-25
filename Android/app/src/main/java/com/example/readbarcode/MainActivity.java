package com.example.readbarcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_condition;

    Button btn_user_reg;
    Button btn_user_reload;
    Button btn_user_edit;
    Button btn_barcode_reg;
    Button btn_barcode_search;
    Button btn_barcode_good;
    Button btn_barcode_bad;

    EditText ed_id;
    EditText ed_pw;
    EditText ed_barcode;
    EditText ed_title;

    Handler h;
    String hand_str;

    class Parser extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg){
            super.handleMessage(msg);
            log("msg is " + msg.toString());
            log("str is " + hand_str);
        }
    }

    private void view_conn(){
        log("start View Connect");
        btn_user_reg = (Button) findViewById(R.id.user_reg);
        btn_user_reload = (Button) findViewById(R.id.user_reload);
        btn_user_edit = (Button) findViewById(R.id.user_edit);
        btn_barcode_reg = (Button) findViewById(R.id.barcode_reg);
        btn_barcode_search = (Button) findViewById(R.id.barcode_search);
        btn_barcode_good = (Button) findViewById(R.id.barcode_rait_good);
        btn_barcode_bad = (Button) findViewById(R.id.barcode_rait_bad);
        log("Button Connected");

        ed_id = (EditText) findViewById(R.id.ed_id);
        ed_pw = (EditText) findViewById(R.id.ed_pw);
        ed_barcode = (EditText) findViewById(R.id.ed_barcode);
        ed_title = (EditText) findViewById(R.id.ed_title);
        log("Edit Text Connected");




        btn_condition = (Button) findViewById(R.id.condition_check);
        btn_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log("Action - click condition");
                Restful rest = new Restful(hand_str, h);
                rest.get(0, "");
            }
        });
        log("Condition Connected");;
        log("View Connect Complete");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        log("On Create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log("Set content view");

        view_conn();

        Restful rest = new Restful(hand_str, h);
        rest.test();
    }

    public void log(String s){
        System.out.println(s);
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}