package com.graypn.demo_userid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.graypn.uid.UidPersistenceHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView tvUid = (TextView) findViewById(R.id.tv_uid);

        findViewById(R.id.btn_get_uid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = UidPersistenceHelper.getUid();
                tvUid.setText(uid);

                Log.i(TAG, "uid: " + uid);
            }
        });
    }
}
