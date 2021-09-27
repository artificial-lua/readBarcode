package com.example.readbarcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // 선언부
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

    String id;
    String pw;
    String bc;
    String tt;
    String kind;
    String barcodeid;

    private String getId(){
        return this.id;
    }
    private void setId(String id){
        this.id = id;
        ed_id.setText(id);
    }
    private String getPw(){
        return this.pw;
    }
    private void setPw(String pw){
        this.pw = pw;
        ed_pw.setText(pw);
    }
    private String getBc(){
        return this.bc;
    }
    private void setBc(String bc){
        this.bc = bc;
        ed_barcode.setText(bc);
    }
    private String getTt(){
        return this.tt;
    }
    private void setTt(String tt){
        this.tt = tt;
        ed_title.setText(tt);
    }
    private String getKind(){
        return this.kind;
    }
    private void setKind(String kind){
        this.kind = kind;
    }
    public void setBarcodeid(String barcodeid) {
        this.barcodeid = barcodeid;
    }
    public String getBarcodeid() {
        return barcodeid;
    }

    private JSONObject parser(String json){
        try {
            JSONObject response = new JSONObject(json);
            if(response.getBoolean("error")){
                log("is error");
                throw new Exception();
            }else{
                return new JSONObject(response.getString("result"));
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    

    // 서버 컨디션을 체크하는 함수
    private void condition_ckeck(){
        log("Action - check condition");
        class Parser extends Handler{
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);
                log("msg is " + msg.toString());
                log("str is " + Restful.getStr());
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);
        rest.get(0, "");
    }
    // 서버에 유저를 등록하는 함수
    private void user_reg(){
        log("Action - user_reg");
        class Parser extends Handler{
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                String responseStr = Restful.getStr();

                log("msg is " + msg.toString());
                log("str is " + responseStr);

                try {
                    JSONObject result = parser(responseStr);
                    setId(result.getString("id"));
                    setPw(result.getString("password"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        String params = "?password=testpassword123";

        rest.get(1, params);
    }
    // 유저 아이디와 비밀번호를 복구할 때 user_reload()를 사용합니다. 테스트용 함수
    private void user_reload(){
        setId(getId());
        setPw(getPw());
    }
    // 유저 id와 pw를 변경해주는 함수
    // id는 1회만 변경 가능함
    private void user_edit(){
        log("Action - user_edit");
        class Parser extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                String responseStr = Restful.getStr();

                log("msg is " + msg.toString());
                log("str is " + responseStr);
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        String params = "?id=" + getId() + "&password=" + getPw();
        if (getId() != ed_id.getText().toString()) {
            params += "&edit-id=" + ed_id.getText().toString();
        }
        if (getPw() != ed_pw.getText().toString()) {
            params += "&edit-password=" + ed_pw.getText().toString();
        }

        rest.get(3, params);
    }
    private void barcode_reg(){
        log("Action - barcode_reg");
        class Parser extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                String responseStr = Restful.getStr();

                log("msg is " + msg.toString());
                log("str is " + responseStr);
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        String params = "?id=" + getId() +
                "&password=" + getPw() +
                "&barcode=" + ed_barcode.getText().toString() +
                "&title=" + ed_title.getText().toString();
        rest.get(4, params);
    }
    private void barcode_search(){
        log("Action - barcode_search");
        class Parser extends Handler{
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                String responseStr = Restful.getStr();

                log("msg is " + msg.toString());
                log("str is " + responseStr);

                try {
                    JSONObject result = parser(responseStr);
                    setTt(result.getString("title"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        String params = "?id=" + getId() +
                "&password=" + getPw() +
                "&barcode=" + ed_barcode.getText().toString();

        rest.get(5, params);

    }
    private void barcode_rait(boolean gb){
        log("Action - barcode_rait");
        class Parser extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                String responseStr = Restful.getStr();

                log("msg is " + msg.toString());
                log("str is " + responseStr);
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        String params = "?id=" + getId() +
                "&password=" + getPw() +
                "&kind=" + getKind() +
                "&barcodeid=" + 1 + //이부분 이부분 미우 중요함 바꿔야함 바코드 id를 search에서 받아왔을 때 가능한거임 실제로는 버튼만으로 작동하면 안된다
                "&rait=" + gb;
        rest.get(6, params);

    }

    // 초기 시작시 뷰에 할당합니다.
    private void view_conn(){
        setContentView(R.layout.activity_main);

        btn_condition = (Button) findViewById(R.id.condition_check);
        btn_user_reg = (Button) findViewById(R.id.user_reg);
        btn_user_reload = (Button) findViewById(R.id.user_reload);
        btn_user_edit = (Button) findViewById(R.id.user_edit);
        btn_barcode_reg = (Button) findViewById(R.id.barcode_reg);
        btn_barcode_search = (Button) findViewById(R.id.barcode_search);
        btn_barcode_good = (Button) findViewById(R.id.barcode_rait_good);
        btn_barcode_bad = (Button) findViewById(R.id.barcode_rait_bad);

        ed_id = (EditText) findViewById(R.id.ed_id);
        ed_pw = (EditText) findViewById(R.id.ed_pw);
        ed_barcode = (EditText) findViewById(R.id.ed_barcode);
        ed_title = (EditText) findViewById(R.id.ed_title);

        kind = "raw";


        // 각 함수를 버튼에 할당합니다.
        // 이후 버튼이 아니라 액션에 할당할 수 있도록 합시다.
        btn_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                condition_ckeck();
            }
        });
        btn_user_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_reg();
            }
        });
        btn_user_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_reload();
            }
        });
        btn_user_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_edit();
            }
        });
        btn_barcode_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_reg();
            }
        });
        btn_barcode_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_search();
            }
        });
        btn_barcode_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_rait(true);
            }
        });
        btn_barcode_bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_rait(false);
            }
        });
    }

    
    // 초기 시작시 실행
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view_conn();
        testing(); // testing() 함수는 테스트시만 실행합시다.
    }

    
    /*여기부터는 테스트를 위한 부분임*/
    TextView log_text;
    Boolean test;

    public void testing(){
        test = true;
        log_text = (TextView)findViewById(R.id.text_log);
        log_text.setMovementMethod(new ScrollingMovementMethod());
    }

    public String getLogs(){
        return log_text.getText().toString();
    }

    public void setLogs(String s){
        log_text.setText(s);
    }

    public void log(String s){
        if (test){
            System.out.println(s);
            setLogs(getLogs() + "\n" + s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }
}