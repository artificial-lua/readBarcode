package com.example.readbarcode;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {
    Context context;
    Save save;

    public static String ID;
    public static String PW;

    static Boolean was_run;

    // 생성자
    public UserInfo(Context context){
        this.context = context;
        save = new Save(context);

    }

    // JSON Parser
    private JSONObject parser(String json){
        try {
            JSONObject response = new JSONObject(json);
            if(response.getBoolean("error")){
//                log("is error");
                throw new Exception();
            }else{
                return new JSONObject(response.getString("result"));
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void wasRun(){
        was_run = save.getBoolean("was_run");

        if (!was_run){ // 처음 앱 실행 시 시작하는 구문
            user_reg();
            save.setBoolean("was_run", true);
        }else{
            this.ID = save.getString("id");
            this.PW = save.getString("pw");
        }
    }

    private void user_reg(){
//        log("Action - user_reg");
        class Parser extends Handler {
            @Override
            public void handleMessage(@NonNull Message msg){
                super.handleMessage(msg);

                String responseStr = Restful.getStr();

//                log("msg is " + msg.toString());
//                log("str is " + responseStr);

                try {
                    JSONObject result = parser(responseStr);
//                    setId(result.getString("id"));
//                    setPw(result.getString("password"));
//                    save.setString("id", getId());
//                    save.setString("pw", getPw());
                    ID = result.getString("id");
                    PW = result.getString("password");
                    save.setString("id", ID);
                    save.setString("pw", PW);

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

}
