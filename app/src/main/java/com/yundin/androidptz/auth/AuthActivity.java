package com.yundin.androidptz.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yundin.androidptz.R;
import com.yundin.androidptz.device_list.ListActivity;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        SharedPreferences sp = getSharedPreferences("authData", MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        EditText loginEditText = findViewById(R.id.login);
        EditText passwordEditText = findViewById(R.id.password);
        Button authButton = findViewById(R.id.auth_button);

        String login = sp.getString("login", "");
        String password = sp.getString("password", "");
        if(!login.equals("")){
            loginEditText.setText(login);
        }
        if(!password.equals("")){
            passwordEditText.setText(password);
        }

        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                spEditor.putString("login", login);
                spEditor.putString("password", password);

                spEditor.apply();
                Intent intent = new Intent(AuthActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
