package com.example.readbarcode;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;


public class LogGestureListener extends GestureDetector.SimpleOnGestureListener {

    public static TextView textView;
    private static ViewPager2 viewPager2;

    public static boolean isScrollStart;
    public static boolean isDistanceXOver20;
    public static float distX;
    public static float absDistanceX;


    public LogGestureListener(){
        // constructor

    }

    public void setTextView(TextView _textView){
        textView = _textView;
    }

    public void setViewPager(ViewPager2 _viewPager2){
        viewPager2 = _viewPager2;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        // LogFragment를 길게 눌렀을 경우
        if (viewPager2.getCurrentItem() == 1){
            Log.d("LogGestureDetector", "onLongPress");
        }

        // TODO
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        boolean result =  super.onScroll(e1, e2, distanceX, distanceY);
        absDistanceX = Math.abs(distanceX);

        isScrollStart = true;
        this.distX = distanceX;

        if (absDistanceX > 20){
            // 20이상 스크롤 되었을 시 할 일
            Log.d("LogGestureDetector", "onScroll absDistance Over 50");

        }

        return result;
    }

    // 스크롤이 끝나면 실행되는 메소드
    public static void onScrollEnd(){

        if (viewPager2.getCurrentItem() == 1 && isScrollStart && isDistanceXOver20){

            if (distX >= 0){
                // NEXT
                LogManager.setLogToView(LogManager.MODE_NEXT);
            } else if (distX < 0){
                // PREV
                LogManager.setLogToView(LogManager.MODE_PREV);
            }
        }

    }
}
