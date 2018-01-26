package com.example.anthony.instantpick;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

public class LoginActivity extends AppCompatActivity {
    String a="1";
    String b="0";
    ToggleButton togHr;
    ToggleButton togSt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        togHr = (ToggleButton) findViewById(R.id.togHr);
        togSt = (ToggleButton) findViewById(R.id.togSt);
    }

    public static class ip{
        public static String c = "192.168.43.167";
    }

    public void onRegister(View v) {
        if (v.getId() == R.id.tregister) {
            Intent i = new Intent(LoginActivity.this, Signup.class);
            startActivity(i);
        }
    }

    public void onTogSt(View v){
        Intent i = new Intent(LoginActivity.this,SigninActivity.class);
        i.putExtra("1",a);
        startActivity(i);
        togHr.setChecked(false);
    }

    public void onTogHr(View v){
        Intent i = new Intent(LoginActivity.this,SigninActivity.class);
        i.putExtra("0",b);
        startActivity(i);
        togSt.setChecked(false);
    }

}
