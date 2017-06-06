package com.outdoorclassroom;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpDetailActivity extends AppCompatActivity {

    public static final String Q_ID = "q_id";
    public static final String A_ID = "a_id";
    public static final String I_ID = "i_id";
    public static final String N_ID = "b_id";

    TextView qText, aText;
    ImageView imageView;
    String question, answer, imgName, nav;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cust_tb);
        setSupportActionBar(toolbar);
        setTitle(R.string.help_activity);

        qText = (TextView) findViewById(R.id.help_question);
        aText = (TextView) findViewById(R.id.help_answer);
        imageView = (ImageView) findViewById(R.id.help_image);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            question = extras.getString(Q_ID);
            answer = extras.getString(A_ID);
            imgName = extras.getString(I_ID);
            nav = extras.getString(N_ID);

            qText.setText(question);
            aText.setText(answer);
            if (!imgName.equals("")) {
                int imgID = getResources().getIdentifier(
                        imgName,
                        "drawable",
                        getPackageName());
                imageView.setImageResource(imgID);
            }

            // can place other navigation settings, for now only map or main
            intent = new Intent(HelpDetailActivity.this, MapsActivity.class);
            switch (nav) {
                case "main":
                    intent = new Intent(HelpDetailActivity.this, MainActivity.class);
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.home));
                    break;
                case "map":
                    break;
                default:
                    break;
            }
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });

        }
    }
}
