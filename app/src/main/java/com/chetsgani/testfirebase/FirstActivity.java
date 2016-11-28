package com.chetsgani.testfirebase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FirstActivity extends AppCompatActivity {
    private RadioGroup usergroup;
    private RadioButton admin_rb_btn;
    private RadioButton user_rb_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        usergroup = (RadioGroup) findViewById(R.id.user_selection_group);
        admin_rb_btn = (RadioButton) findViewById(R.id.admin_radio);
        user_rb_btn = (RadioButton) findViewById(R.id.user_radio);

        if (!NotifyListener.notificationlistener) {
        /*startService(new Intent(FirstActivity.this, NotifyListener.class));*/
        }

        usergroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (admin_rb_btn.isChecked()) {
                    Intent intent = new Intent(FirstActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    if (user_rb_btn.isChecked()) {
                        Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
}
