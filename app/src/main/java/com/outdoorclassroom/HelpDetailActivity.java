package com.outdoorclassroom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpDetailActivity extends AppCompatActivity {

    public static final String Q_ID = "q_id";
    public static final String A_ID = "a_id";
    public static final String I_ID = "i_id";

    TextView qText, aText;
    ImageView imageView;
    String question, answer, imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);
        setTitle(R.string.help_activity);
        if(getSupportActionBar() != null){
            Log.d("Logo check", "Reached");
            getSupportActionBar().setIcon(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
        }

        qText = (TextView) findViewById(R.id.help_question);
        aText = (TextView) findViewById(R.id.help_answer);
        imageView = (ImageView) findViewById(R.id.help_image);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            question = extras.getString(Q_ID);
            answer = extras.getString(A_ID);
            imgName = extras.getString(I_ID);

            qText.setText(question);
            aText.setText(answer);
            if (!imgName.equals("")) {
                int imgID = getResources().getIdentifier(
                        imgName,
                        "drawable",
                        getPackageName());
                imageView.setImageResource(imgID);
            }

        }
    }
}
