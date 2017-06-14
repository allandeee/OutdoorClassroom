package com.outdoorclassroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cust_tb);
        setSupportActionBar(toolbar);
        setTitle(R.string.about);

        TextView details = (TextView) findViewById(R.id.group_details);
        TextView names = (TextView) findViewById(R.id.developers);

        String[] detArray = getResources().getStringArray(R.array.details);
        StringBuilder builder1 = new StringBuilder();
        for (String name : detArray) {
            builder1.append(name + "\n");
        }
        details.setText(builder1.toString());

        String[] devArray = getResources().getStringArray(R.array.developers);
        StringBuilder builder = new StringBuilder();
        for (String name : devArray) {
            builder.append(name + "\n");
        }
        names.setText(builder.toString());
    }
}
