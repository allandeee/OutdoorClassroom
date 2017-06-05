package com.outdoorclassroom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.outdoorclassroom.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Walks. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link WalkDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class WalkListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.cust_tb);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalkListActivity.this, MapsActivity.class));
                Snackbar.make(view, "Go to Map", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        View recyclerView = findViewById(R.id.walk_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.walk_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
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
                startActivity(new Intent(WalkListActivity.this, HelpActivity.class));
                return true;
            default:
                Toast.makeText(getBaseContext(), "Invalid Input", Toast.LENGTH_SHORT).show();
                return true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.walk_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).walk_name);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(WalkDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        WalkDetailFragment fragment = new WalkDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.walk_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, WalkDetailActivity.class);
                        intent.putExtra(WalkDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
