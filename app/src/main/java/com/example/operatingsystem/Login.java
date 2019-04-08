package com.example.operatingsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText text=findViewById(R.id.editText);
        final EditText text1=findViewById(R.id.editText2);
        Button button=findViewById(R.id.button5);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String all=text.getText().toString();
                String user=text1.getText().toString();
                Intent intent=new Intent(Login.this,MainActivity.class);
                intent.putExtra("all",all);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
    }
}
