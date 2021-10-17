package com.example.readbarcode;

import static android.speech.tts.TextToSpeech.ERROR;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class CaptureFragment extends Fragment {


    EditText mainBarcode;
    EditText mainName;

    View view;
    IntentIntegrator integrator;

    TextToSpeech tts;

    String formatName;
    String barcodeStr;

    String errorMessage;

    UserInfo userInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // tts
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if ( i != ERROR){
                    tts.setLanguage(Locale.KOREA);
                }
            }
        });

        userInfo = new UserInfo(getActivity());


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.capture_layout, container, false);

        LinearLayout main_layout = (LinearLayout) view.findViewById(R.id.main_layout);
        main_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barcode_capture();
            }
        });


        mainBarcode = (EditText) view.findViewById(R.id.main_barcode);
        mainName = (EditText) view.findViewById(R.id.main_name);


        return view;
    }

    // 바코드 캡처 기능
    private void barcode_capture(){
        // 여기에 바코드 카메라로 캡쳐 기능을 넣으시오
        // 캡쳐된 넘버는 따로 어디로 받아놨는지 표시할 수 있도록

        integrator = new IntentIntegrator(getActivity()).forSupportFragment(this);
        integrator.setPrompt("바코드 또는 QR코드를 인식합니다");
        integrator.setCaptureActivity(ZxingActivity.class);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();

    }

    // 캡처가 완료되면 여기로 옵니다
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                // 캡처 오류, 취소 등
            } else {
                // 캡처 성공 내용
                // 받아온 바코드 데이터 변수에 저장
                formatName = result.getFormatName();
                barcodeStr = result.getContents();

                // Set Barcode Number
                mainBarcode.setText(barcodeStr);

                barcode_search();

            }
        }
    }

    // JSON Parser
    private JSONObject parser(String json){
        try {
            JSONObject response = new JSONObject(json);
            if(response.getBoolean("error")){
//                log("is error");
//                throw new Exception();
                errorMessage = response.getString("message");
                return null;
            }else{

                return new JSONObject(response.getString("result"));
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    // get barcode data
    private void barcode_search(){
//        log("Action - barcode_search");
        class Parser extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);
//                if (test){
//
//                    String responseStr = Restful.getStr();
//
//                    log("msg is " + msg.toString());
//                    log("str is " + responseStr);
//
//                    try {
//                        JSONObject result = parser(responseStr);
//                        setTt(result.getString("title"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else{
//                    try {
//                        JSONObject result = parser(Restful.getStr());
//                        if (result != null) {
//                            stringTTS(result.getString("title"));
//                            ((EditText)findViewById(R.id.main_name)).setText(result.getString("title"));
//                        }
//                        else {
//                            stringTTS("검색 결과가 없습니다.");
//                        }
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//                }

                try {
                    JSONObject result = parser(Restful.getStr());
                    if (result != null) {

                        String resultTitle = result.getString("title");

                        tts.speak(resultTitle, TextToSpeech.QUEUE_FLUSH, null);
                        mainName.setText(resultTitle);
                    }
                    else {

                        tts.speak("검색 결과가 없습니다.", TextToSpeech.QUEUE_FLUSH, null);
                        mainName.setText("검색 결과가 없습니다. " + errorMessage);
                    }

                } catch (JSONException e){
                    mainName.setText(e.getMessage());
                    tts.speak("검색 과정 오류 발생", TextToSpeech.QUEUE_FLUSH, null);
                    e.printStackTrace();
                }
            }
        }
        Parser h = new Parser();
        Restful rest = new Restful(h);

        String params = "?id=" + userInfo.ID +
                "&password=" + userInfo.PW +
                "&barcode=" + barcodeStr;

//        log(params);

        rest.get(5, params);

    }
}
