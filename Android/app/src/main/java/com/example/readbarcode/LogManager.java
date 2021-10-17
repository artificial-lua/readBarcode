package com.example.readbarcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

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

    // SharedPreferences Key 목록
    public static final String TEST_KEY = "testkey"; // SharedPreferences 테스트용
    public static final String LOG_KEY = "logArray";

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_NEXT = 1;
    public static final int MODE_PREV = 2;
    // 전역변수 선언 end


    public LogManager(){
        // constructor
    }

    // View 셋팅 관련
    public static void setView(TextView _barcodeStringView, TextView _dateTimeView, TextView _titleView){
        barcodeStringView = _barcodeStringView;
        dateTimeView = _dateTimeView;
        titleView = _titleView;
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

    // SharedPreferences에 저장된 LogArray String을 불러옴
    public static String getLog(Context context, String key){
        // SharedPreferences 에 저장해 놓은 로그 불러오기 (JSONArray 형식의 String)
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String value = sharedPreferences.getString(key, null);

        return value;
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
            JSONObject jsonObject = logArray.getJSONObject(currentIndex);

            barcodeStringView.setText(jsonObject.getString("barcode"));
            dateTimeView.setText(jsonObject.getString("datetime"));
            titleView.setText(jsonObject.getString("title"));
        } catch(Exception e){
            Log.d("setLogToView", e.getMessage());
        }


    }



}


