package com.outdoorclassroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {
    static final String LANDMARK = "Landmark"; //key?
    Landmark landmark = new Landmark();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Bundle data = getIntent().getExtras();
        landmark = data.getParcelable(LANDMARK);

        TextView title = (TextView) findViewById(R.id.lm_title);
        TextView info = (TextView) findViewById(R.id.lm_info);

        title.setText(landmark.getName());
        info.setText(landmark.getInfo());

    }
}
