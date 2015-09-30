package software.spartacus.com.shotclockscorekeeper;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class PlayMatchActivity extends FragmentActivity {

    private static final String TAG = "PlayMatchActivity";

    public static final String EXTRA_MATCH = "match";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().hide();

        if (savedInstanceState == null) {
            Match match = getIntent().getParcelableExtra(EXTRA_MATCH);
            if (match == null) {
                Log.e(TAG, "Activity requires a match");
                finish();
                return;
            }

            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, PlayMatchFragment.newInstance(match))
                    .commit();
        }
    }
}
