package com.example.fanyishuo.zailai.diergebao;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fanyishuo.zailai.MainActivity;
import com.example.fanyishuo.zailai.R;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * Created by fanyishuo on 2017/8/9.
 */

public class SecondActiovity extends AppCompatActivity implements View.OnClickListener{
    private EditText et_phone_number;
    private EditText et_verification_code;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secondactivity);
        TextView tv_get_verification_code = (TextView) findViewById(R.id.tv_get_verification_code);
        TextView tv_verification_code = (TextView) findViewById(R.id.tv_verification_code);
        tv_get_verification_code.setOnClickListener(this);
        tv_verification_code.setOnClickListener(this);

        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_verification_code = (EditText) findViewById(R.id.et_verification_code);

       SMSSDK.registerEventHandler(eh);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_get_verification_code:
                SMSSDK.getVerificationCode("86", et_phone_number.getText().toString().trim(), new OnSendMessageHandler() {
                    @Override
                    public boolean onSendMessage(String s, String s1) {
                        return false;
                    }
                });

                break;
            case R.id.tv_verification_code:
                SMSSDK.submitVerificationCode("86", et_phone_number.getText().toString().trim(), et_verification_code.getText().toString().trim());
                break;

        }

    }
    EventHandler eh = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码正确的回调
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //跳转到第一个页面
                            Intent intent=new Intent(SecondActiovity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SecondActiovity.this, "验证成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SecondActiovity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
                Log.e("tag", ((Throwable) data).getMessage().toString());
                //获取验证码成功
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SecondActiovity.this, "失败", Toast.LENGTH_SHORT).show();

                    }
                });
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }
}
