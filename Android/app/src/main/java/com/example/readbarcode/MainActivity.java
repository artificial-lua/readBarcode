package com.example.readbarcode;

import static android.speech.tts.TextToSpeech.ERROR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    // 선언부
    Button btn_condition;
    Button btn_user_reg;
    Button btn_user_reload;
    Button btn_user_edit;
    Button btn_barcode_capture;
    Button btn_read_title;
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
    String barcodeFormat;
    String barcodeString;
    String barcodeid;

    TextToSpeech tts;
    Save save;

    Boolean was_run;
    Boolean debug;

    // ViewPager2 관련
    ViewPager2 viewPager2;
    MyViewPagerAdapter myViewPagerAdapter;

    private String getId(){
        return this.id;
    }
    private void setId(String id){
        this.id = id;
        if(test)
            ed_id.setText(id);
    }
    private String getPw(){
        return this.pw;
    }
    private void setPw(String pw){
        this.pw = pw;
        if(test)
            ed_pw.setText(pw);
    }
    private String getBc(){
        return this.bc;
    }
    private void setBc(String bc){
        this.bc = bc;
        if(test)
            ed_barcode.setText(bc);
    }
    private String getTt(){
        return this.tt;
    }
    private void setTt(String tt){
        this.tt = tt;
        if(test)
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



    // 바코드 캡처 기능
    private void barcode_capture(){
        // 여기에 바코드 카메라로 캡쳐 기능을 넣으시오
        // 캡쳐된 넘버는 따로 어디로 받아놨는지 표시할 수 있도록

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("바코드 또는 QR코드를 인식합니다");
        integrator.setCaptureActivity(ZxingActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

    }
    // 캡처가 완료되면 여기로 옵니다
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (test) {
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    barcodeFormat = result.getFormatName();
                    barcodeString = result.getContents();
                    setBc(barcodeString);
                    log(barcodeString);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }else{
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    barcodeFormat = result.getFormatName();
                    barcodeString = result.getContents();
                    setBc(barcodeString);
                    ((EditText)findViewById(R.id.main_barcode)).setText(barcodeString);
                    barcode_search();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        }
    }


    private boolean error_check(JSONObject json) throws JSONException {
        if(json.getBoolean("error")){
            log(json.getString("message"));
            return true;
        }
        return false;
    }
    // 서버 컨디션을 체크하는 함수
    private void condition_check(){
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
                    save.setString("id", getId());
                    save.setString("pw", getPw());
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

    // String을 인자로 받아 TTS 실행
    private void stringTTS(String stringToSpeak){
        // tts 실행
        tts.speak(stringToSpeak, TextToSpeech.QUEUE_FLUSH, null);
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
                if (test){

                    String responseStr = Restful.getStr();

                    log("msg is " + msg.toString());
                    log("str is " + responseStr);

                    try {
                        JSONObject result = parser(responseStr);
                        setTt(result.getString("title"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        JSONObject result = parser(Restful.getStr());
                        if (result != null) {
                            stringTTS(result.getString("title"));
                            ((EditText)findViewById(R.id.main_name)).setText(result.getString("title"));
                        }
                        else {
                            stringTTS("검색 결과가 없습니다.");
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        String params = "?id=" + getId() +
                "&password=" + getPw() +
                "&barcode=" + getBc();

        log(params);

        rest.get(5, params);

    }
    private void barcode_rating(boolean gb){
        log("Action - barcode_rating");
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
                "&rating=" + gb;
        rest.get(6, params);
    }


    // 테스트가 아닐 때 초기 시작 메소드
    private void onStarts(){
//        save = new Save(this);
//        was_run = save.getBoolean("was_run");
//
//        if (!was_run){ // 처음 앱 실행 시 시작하는 구문
//            user_reg();
//            save.setBoolean("was_run", true);
//        }else{
//            log(getId() + getPw());
//            setId(save.getString("id"));
//            setPw(save.getString("pw"));
//        }

        UserInfo userInfo = new UserInfo(this);
        userInfo.wasRun();

        // tts 초기화
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if ( i != ERROR){
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });

        LogManager.setTts(tts);

        // viewpager2 연결
        viewPager2 = findViewById(R.id.main_viewpager);
        myViewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(myViewPagerAdapter);

        // viewpager2 세로모드
        viewPager2.setOrientation(ViewPager2.ORIENTATION_VERTICAL);

        // Log Gesture Detect 관련
        /// GestureListener 객체 생성 후 GestureDetector에 등록
        LogGestureListener gestureListener = new LogGestureListener();
        GestureDetector logGestureDetector = new GestureDetector(this, gestureListener);

        /// GestureListener에 ViewPager 연결
        gestureListener.setViewPager(viewPager2);

        /// 제스쳐 이벤트 등록
        viewPager2.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                logGestureDetector.onTouchEvent(motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    // 스크롤 끝날 때 실행되는 부분
                    LogGestureListener.isDistanceXOver20 = (Math.abs(LogGestureListener.distX) > 20? true : false);
                    LogGestureListener.onScrollEnd(); // 스크롤 끝났을때 실행 메소드

                    LogGestureListener.isScrollStart = false;

                    Log.d("LogGestureListenr", "MotionEvent Action UP");
                }
                return false;
            }
        });

        LogManager.setContext(this);

    }

    // 테스트가 아닐 때 초기 시작시 뷰에 할당합니다.
    private void view_conn(){
        setContentView(R.layout.main);
        /*
        // capture_layout.xml 로 이동, CaptureFragment에 적용
        LinearLayout main_layout = (LinearLayout) findViewById(R.id.main_layout);
        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_capture();
            }
        });

         */
    }

    
    // 초기 시작시 실행
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        test = false;
        debug = true;

        //
        if(test){
            testing(); // testing() 함수는 테스트시만 실행합시다.
        }else{
            view_conn();
            onStarts();
        }
    }

    
    /*여기부터는 테스트를 위한 부분임*/
    TextView log_text;
    Boolean test;

    public void testing(){
        save = new Save(this);
        // tts 초기화
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if ( i != ERROR){
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });
        was_run = save.getBoolean("was_run");

        test = true;


        setContentView(R.layout.activity_main);

        btn_condition = (Button) findViewById(R.id.condition_check);
        btn_user_reg = (Button) findViewById(R.id.user_reg);
        btn_user_reload = (Button) findViewById(R.id.user_reload);
        btn_user_edit = (Button) findViewById(R.id.user_edit);
        btn_barcode_capture = (Button) findViewById(R.id.barcode_capture);
        btn_read_title = (Button) findViewById(R.id.read_title);
        btn_barcode_reg = (Button) findViewById(R.id.barcode_reg);
        btn_barcode_search = (Button) findViewById(R.id.barcode_search);
        btn_barcode_good = (Button) findViewById(R.id.barcode_rating_good);
        btn_barcode_bad = (Button) findViewById(R.id.barcode_rating_bad);

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
                condition_check();
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
        btn_barcode_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_capture();
            }
        });
        btn_read_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stringTTS(getTt());
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
                barcode_rating(true);
            }
        });
        btn_barcode_bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_rating(false);
            }
        });

        log_text = (TextView)findViewById(R.id.text_log);
        log_text.setMovementMethod(new ScrollingMovementMethod());
    }

    public String getLogs(){
        return log_text.getText().toString();
    }

    public void log(String s){
        if(debug){
            System.out.println(s);
        }
        if (test){
            System.out.println(s);
            log_text.setText(getLogs() + "\n" + s);
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
        }
    }
}