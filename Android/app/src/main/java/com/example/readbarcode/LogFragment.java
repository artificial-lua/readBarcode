package com.example.readbarcode;

import static android.speech.tts.TextToSpeech.ERROR;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
/*
log 항목들
barcode : barcode string
datetime : 날짜 및 시간
title : 상품명
error : error 여부
kind : 추후 서버에서 raw data / processed data 등등 해당 정보의 카테고리를 나누기 위해
존재하는 필드명으로 나중에 해당 응답에 대해 평가하기 위해 존재함
id : d바코드가 저장된 db의 번호 - primary key

log 를 읽어오는 동작
log를 여는 순간 해당 로그의 값을 tts로 읽어줘야함
 - title
 - datetime
좌 <- 우 방향 스크롤 시 이전 로그 읽어옴
좌 -> 우 방향 스크롤 시 다음 로그 읽어옴


 */

public class LogFragment extends Fragment {

    static JSONArray logArray;

    // View 관련
    View rootView;
    TextView barcodeStringView;
    TextView dateTimeView;
    TextView titleView;

    TextToSpeech tts;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if ( i != ERROR){
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });

        // LogManager에 Context Set
        LogManager.setContext(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.log_layout, container, false);

        barcodeStringView = rootView.findViewById(R.id.log_barcode_string_text_view);
        dateTimeView = rootView.findViewById(R.id.log_datetime_text_view);
        titleView = rootView.findViewById(R.id.log_title_text_view);

        // LogManager에 View 연결
        LogManager.setView(barcodeStringView, dateTimeView, titleView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
//        setLogArray();
//        testRun();

        // 화면 전환 TTS
        LogManager.tts.speak("로그화면", TextToSpeech.QUEUE_FLUSH, null);


        LogManager.settingLogArray();
        LogManager.setLogToView(LogManager.MODE_DEFAULT);



    }

    @Override
    public void onPause() {
        super.onPause();
        if(tts.isSpeaking()){
            LogManager.tts.speak("", TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    // Setting logArray
    private void setLogArray(){
        String logString = LogManager.getLog(getActivity(), LogManager.LOG_KEY);

        try {
            logArray = new JSONArray(logString);
            LogManager.setLogArray(logArray);
        } catch (Exception e){
            Log.d("LogFragment", "setLogArray() Error " + e.getMessage());
        }
    }

    // Save logArray
    private void saveLogArray(){
        String logArrayString = logArray.toString();
        LogManager.saveLog(getActivity(), LogManager.LOG_KEY, logArrayString);
    }

    // test
    private void testRun(){
        String dummyData = "[" +
                "{'barcode':'1234567', 'datetime':'2021-10-01', 'title':'테스트품목1', 'error':false, 'kind':'test', 'id':'0'},"+
                "{'barcode':'1234568', 'datetime':'2021-10-02', 'title':'테스트품목2', 'error':false, 'kind':'test', 'id':'1'},"+
                "{'barcode':'1234569', 'datetime':'2021-10-03', 'title':'테스트품목3', 'error':false, 'kind':'test', 'id':'2'}]";

        try{
            // dummy data 저장 후 불러오기
            LogManager.saveLog(getActivity(), LogManager.TEST_KEY, dummyData);
            String logString = LogManager.getLog(getActivity(), LogManager.TEST_KEY);
            logArray = new JSONArray(logString);
            LogManager.setLogArray(logArray);

            // Log 화면에 셋팅
            LogManager.setLogToView(LogManager.MODE_DEFAULT);

        } catch (Exception e){
            Log.d("LogFragment", "testRun Error " + e.getMessage());
        }
    }
}
