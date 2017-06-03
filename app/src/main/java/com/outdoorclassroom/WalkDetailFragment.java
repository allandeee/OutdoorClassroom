package com.outdoorclassroom;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.outdoorclassroom.dummy.DummyContent;

/**
 * A fragment representing a single Walk detail screen.
 * This fragment is either contained in a {@link WalkListActivity}
 * in two-pane mode (on tablets) or a {@link WalkDetailActivity}
 * on handsets.
 */
public class WalkDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WalkDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.walk_name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.walk_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            String res = mItem.img_name;

            int imgID = getResources().getIdentifier(
                    res,
                    "drawable",
                    getContext().getPackageName());
            ( (ImageView) rootView.findViewById(R.id.walk_image) ).setImageResource(imgID);
            ( (TextView) rootView.findViewById(R.id.walk_detail) ).setText(mItem.desc);
            // add a TextView for description
        }

        return rootView;
    }
}
