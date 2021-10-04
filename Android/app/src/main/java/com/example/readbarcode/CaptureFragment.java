package com.example.readbarcode;

import static android.speech.tts.TextToSpeech.ERROR;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.Locale;

public class CaptureFragment extends Fragment {

    View view;
    IntentIntegrator integrator;

    TextToSpeech tts;

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
            }
        }
    }
}
