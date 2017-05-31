package com.outdoorclassroom.dummy;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.outdoorclassroom.App;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    /*
    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }
    */

    static {
        // add 2 walks (manually)
        // to be changed to utilise a csv reader for class params
        //addItem(new DummyItem("1", "EHHW", "ehhw", "Default description"));
        readCsvDesc("WalkDescriptionsTest.csv");
    }

    public static void readCsvDesc (String filename) {
        try {
            AssetManager am = App.getContext().getAssets();
            InputStream is = am.open(filename);  //error for EHHWv2.csv
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(is, Charset.forName("UTF-8"))
            );

            String line = "";
            br.readLine();  //skip first line containing headers
            int countID = 1;

            while ( (line = br.readLine()) != null) {
                String[] tokens = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                addItem(new DummyItem(String.valueOf(countID), tokens[0], tokens[1], tokens[2]));
                countID++;
            }

        } catch (Exception e) {
            Log.d("CSV Desc Read", e.toString());
        }
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /*
    private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }
    */

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String walk_name;
        public final String img_name;
        public final String desc;

        public DummyItem(String id, String walk_name, String img_name, String desc) {
            this.id = id;
            this.walk_name = walk_name;
            this.img_name = img_name;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return walk_name;
        }
    }

}
