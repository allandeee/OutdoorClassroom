package com.outdoorclassroom;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cust_tb);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.browser);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, WalkListActivity.class));
            }
        });
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
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                return true;
            default:
                Toast.makeText(getBaseContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.map:
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
                break;
            case R.id.browser:
                startActivity(new Intent(MainActivity.this, WalkListActivity.class));
                //Toast.makeText(getBaseContext(), "Browser to be implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.info:
                startActivity(new Intent(MainActivity.this, HelpActivity.class));
                break;
            default:
                Toast.makeText(getBaseContext(), "Please select one of the options", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
