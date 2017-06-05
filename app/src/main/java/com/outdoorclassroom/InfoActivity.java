package com.outdoorclassroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {
    static final String LANDMARK = "Landmark"; //key?
    Landmark landmark = new Landmark();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cust_tb);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.info_activity));

        Bundle data = getIntent().getExtras();
        landmark = data.getParcelable(LANDMARK);

        TextView title = (TextView) findViewById(R.id.lm_title);
        TextView info = (TextView) findViewById(R.id.lm_info);
        ImageView img = (ImageView) findViewById(R.id.lm_image);

        title.setText(landmark.getName());
        info.setText(landmark.getInfo());
        if (!landmark.getImgName().equals("")) {

            String res = landmark.getImgName();

            int imgID = getResources().getIdentifier(
                    res,
                    "drawable",
                    getPackageName());
            img.setImageResource(imgID);
        }

    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                startActivity(new Intent(InfoActivity.this, HelpActivity.class));
                return true;
            default:
                Toast.makeText(getBaseContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                return true;
        }
    }
}
