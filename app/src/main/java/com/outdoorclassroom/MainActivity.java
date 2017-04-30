package com.outdoorclassroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
            case R.id.browser:
                Toast.makeText(getBaseContext(), "Browser to be implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info:
                Toast.makeText(getBaseContext(), "Database to be implemented", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(getBaseContext(), "Please select one of the options", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
