package com.example.asamir.iraqproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.asamir.iraqproject.AddFormData.OfflineSurvayActivity;
import com.example.asamir.iraqproject.AddFormData.SurvayScreen;
import com.example.asamir.iraqproject.util.ConnectivityHelper;

public class SendingCompleteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_complete);
    }

    public void goToMain(View view) {
        if (ConnectivityHelper.isConnectedToNetwork(SendingCompleteActivity.this))
        {
            startActivity(new Intent(SendingCompleteActivity.this,SurvayScreen.class));
            finish();
        }else {
            startActivity(new Intent(SendingCompleteActivity.this,OfflineSurvayActivity.class));
            finish();
        }

    }
}
