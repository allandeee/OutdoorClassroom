package com.outdoorclassroom;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    private List<QuestionAnswer> questionAnswers = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuAnAdapter quAnAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cust_tb);
        setSupportActionBar(toolbar);
        setTitle(R.string.help_activity);

        recyclerView = (RecyclerView) findViewById(R.id.help_list);

        quAnAdapter = new QuAnAdapter(questionAnswers);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(quAnAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                QuestionAnswer questionAnswer = questionAnswers.get(position);
                Intent intent = new Intent(HelpActivity.this, HelpDetailActivity.class);
                Bundle extras = new Bundle();
                extras.putString(HelpDetailActivity.Q_ID, questionAnswer.getQuestion());
                extras.putString(HelpDetailActivity.A_ID, questionAnswer.getAnswer());
                extras.putString(HelpDetailActivity.I_ID, questionAnswer.getImgName());
                extras.putString(HelpDetailActivity.N_ID, questionAnswer.getNav());
                intent.putExtras(extras);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), questionAnswer.getQuestion() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        prepareQList();
    }

    private void prepareQList() {
        String filename = "help_info.csv";

        try {
            AssetManager am = App.getContext().getAssets();
            InputStream is = am.open(filename);  //error for EHHWv2.csv
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            CSVReader reader = new CSVReader(br);
            reader.readNext();  //skip first line containing headers
            String [] tokens;

            while ((tokens = reader.readNext()) != null) {
                questionAnswers.add(
                        new QuestionAnswer(tokens[0],tokens[1],tokens[2],tokens[3])
                );
            }
            quAnAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            Log.d("CSV Help Read", e.toString());
        }
    }

    public class QuAnAdapter extends RecyclerView.Adapter<QuAnAdapter.MyViewHolder> {

        private List<QuestionAnswer> questionAnswers;

        public QuAnAdapter(List<QuestionAnswer> questionAnswers) {
            this.questionAnswers = questionAnswers;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.help_list_content, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            QuestionAnswer questionAnswer = questionAnswers.get(position);
            String q = questionAnswer.getQuestion();
            holder.question.setText(q);
        }

        @Override
        public int getItemCount() {
            return questionAnswers.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView question;

            public MyViewHolder (View view) {
                super(view);
                question = (TextView) view.findViewById(R.id.question);
            }
        }

    }


}
