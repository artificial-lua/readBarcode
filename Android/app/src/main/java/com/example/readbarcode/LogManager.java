package com.example.readbarcode;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {
    // 로그를 관리, view에 표시해주는 클래스입니다
    // 로그는 JSONArray 형태의 String으로 저장합니다
    // 데이터 저장은 SharedPreferences를 이용합니다

    // 전역변수 선언 start
    // 화면에 표시할 내용 View 관련
    private static TextView barcodeStringView;
    private static TextView dateTimeView;
    private static TextView titleView;

    private static int currentIndex;
    private static int firstIndex;
    private static final int lastIndex = 0;

    // 로그를 담을 JSONArray 객체
    private static JSONArray logArray;
    public static JSONObject currentJson;

    // SharedPreferences Key 목록
    public static final String TEST_KEY = "testkey"; // SharedPreferences 테스트용
    public static final String LOG_KEY = "logArray";

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_NEXT = 1;
    public static final int MODE_PREV = 2;

    public static Context context;

    public static TextToSpeech tts;

    // sendLog() 에서 쓰이는 AlretDialog Listener
    public static DialogInterface.OnClickListener send, cancel;
    static View dialogView;

    // 전역변수 선언 end


    public LogManager(){
        // constructor
    }


    // TTS 셋팅
    public static void setTts(TextToSpeech _tts){
        tts = _tts;
    }

    // View 셋팅 관련
    public static void setView(TextView _barcodeStringView, TextView _dateTimeView, TextView _titleView){
        barcodeStringView = _barcodeStringView;
        dateTimeView = _dateTimeView;
        titleView = _titleView;
    }

    public static void setContext(Context _context){
        context = _context;
    }

    // log Array 셋팅
    public static void setLogArray(JSONArray _logArray){
        logArray = _logArray;
    }

    // LogArray String을 SharedPreferences 를 이용해 저장
    public static void saveLog(Context context, String key, String value){
        // SharedPreferences 객체 생성
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = editor = sharedPreferences.edit();

        // 데이터 저장
        editor.putString(key, value);
        editor.commit();
    }

    // LogArray String을 SharedPreferences 를 이용해 저장 Overloading
    public static void saveLog(String key){
        // SharedPreferences 객체 생성
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = editor = sharedPreferences.edit();

        String value = logArray.toString();

        // 데이터 저장
        editor.putString(key, value);
        editor.commit();
    }

    // SharedPreferences에 저장된 LogArray String을 불러옴
    public static String getLog(Context context, String key){
        // SharedPreferences 에 저장해 놓은 로그 불러오기 (JSONArray 형식의 String)
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(key, null);

        return value;
    }


    public static void settingLogArray(){
        String logString = LogManager.getLog(context, LogManager.LOG_KEY);

        try {
            if (logString == null ){
                logArray = new JSONArray();
            } else{
                logArray = new JSONArray(logString);
            }
        } catch (Exception e){
            Log.d("LogManager", "setLogArray() Error " + e.getMessage());
        }
    }

    public static JSONObject setLogJsonObject(String barcodeString, String dateTime, String title, boolean error, String kind, String id){
        JSONObject jobject = new JSONObject();

        try {
            jobject.put("barcode", barcodeString);
            jobject.put("datetime", dateTime);
            jobject.put("title", title);
            jobject.put("error", error);
            jobject.put("kind", kind);
            jobject.put("id", id);
        } catch (Exception e){
            Log.d("setLogJsonObject", e.getMessage());
            return null;
        }

        return jobject;
    }

    public static void addToLogArray(JSONObject jsonObject){
        logArray.put(jsonObject);
    }


    public static void setLogToView(int mode){
        // Log Data(JSON)를 화면에 보이게 함

        firstIndex = logArray.length() - 1; // 가장 최신의 Log 부터 보여줌

        // Setting Index
        if (mode == MODE_DEFAULT) {
            // MODE_DEFAULT => currentIndex 의 변화 없음, 화면 초기화
            currentIndex = firstIndex;
        } else if (mode == MODE_NEXT){
            // MODE_NEXT -> currentIndex - 1 을 해줘서 다음 페이지를 보여줌
            currentIndex -= 1;
            if (currentIndex <= lastIndex) currentIndex = lastIndex; // Index가 0 미만으로 내려가는걸 방지함

        } else if (mode == MODE_PREV){
            // MODE_PREV => currentIndex + 1 을 해줘서 다음 페이지를 보여줌
            currentIndex += 1;
            if (currentIndex >= firstIndex) currentIndex = firstIndex; // Index가 firstIndex(최대 Index)를 넘어가는걸 방지함
        }

        // setView
        try{
            currentJson = logArray.getJSONObject(currentIndex);

            barcodeStringView.setText(currentJson.getString("barcode"));
            dateTimeView.setText(currentJson.getString("datetime"));
            titleView.setText(currentJson.getString("title"));


            // 상품명이 없습니다
            String titleToSpeak;
            Log.d("LogTitle", currentJson.getString("title") + " 텍스트 없음");
            if (currentJson.getBoolean("error")){
                titleToSpeak = "상품명이 없습니다";
                titleView.setText(titleToSpeak);
            } else {
                titleToSpeak = currentJson.getString("title");
            }

            // 날짜 포맷 바꾸기
            String longDateTime = currentJson.getString("datetime");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sdf.parse(longDateTime);
            
            sdf = new SimpleDateFormat("hh:mm MM월 dd일");
            String shortDateTime = sdf.format(dt);
            Log.d("shortDateTime" , shortDateTime);

            // title / dateTime 읽어주기
            tts.speak(titleToSpeak, TextToSpeech.QUEUE_FLUSH, null);
            tts.speak(shortDateTime, TextToSpeech.QUEUE_ADD, null);

        } catch(Exception e){
            Log.d("setLogToView", e.getMessage());
        }


    }

    public static void sendLog(){

        /*

        LongPress 시 sendLog() 실행
        sendLog() 에선 AlretDialog 실행
        AlertDialog에서 EditText로 상품명 입력받고 "전송"버튼 누름

        전송 버튼 누르면 서버로 전송 -> 이 부분은 DialogInterface.OnClickListener에 구현

         */

        /*
        Server send params
        id : user
        password : user
        barcode : string
        title : 입력한 것
         */

        send = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 키보드 내리기
                InputMethodManager immhide = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                // 서버 전송 부분
                    EditText sendLogTitle = dialogView.findViewById(R.id.send_log_title);
                    String sendTitle = sendLogTitle.getText().toString();
                    Log.d("sendLog()", sendTitle + " send");
                    barcode_reg(sendTitle);
                // ----

            }
        };

        cancel = new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 키보드 내리기
                InputMethodManager immhide = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);


                // 취소 작동 부분
                // ----
            }
        };

        dialogView = (View) View.inflate(context, R.layout.log_dialog, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("상품명 전송");
        dialog.setView(dialogView);
        dialog.setPositiveButton("전송", send);
        dialog.setNegativeButton("취소", cancel);
        dialog.show();

        // 키보드 올라오기
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);


    }

    private static void barcode_reg(String sendTitle){
//        log("Action - barcode_reg");
        class Parser extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                String responseStr = Restful.getStr();

//                log("msg is " + msg.toString());
//                log("str is " + responseStr);
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        try{
            String params = "?id=" + UserInfo.ID +
                    "&password=" + UserInfo.PW +
                    "&barcode=" + currentJson.getString("barcode") +
                    "&title=" + sendTitle;

            rest.get(4, params);

        } catch (Exception e){
            Log.d("LogManager/barcode_reg()", e.getMessage());
        }

    }



}


