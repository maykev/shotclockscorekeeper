package software.spartacus.com.shotclockscorekeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import android.graphics.Color;
import android.widget.ListAdapter;

import software.spartacus.com.shotclockscorekeeper.CustomListAdapter;
import software.spartacus.com.shotclockscorekeeper.R;

public class StartMatchActivity extends Activity {
    String[] matches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_match);

        ListView listViewMatches = (ListView)findViewById(R.id.listViewMatches);

//        adapter = new CustomListAdapter(this, R.layout.custom_list, list);
//        listViewMatches.setAdapter(adapter);
//
//        matches = new String[] {"Kevin May vs Beau Runningen", "Ernesto Dominguez vs Louis Ulrich" };
//        ListAdapter = new ArrayAdapter<String>(this, androidresource.layout.simple_list_item, matches);
    }
}
