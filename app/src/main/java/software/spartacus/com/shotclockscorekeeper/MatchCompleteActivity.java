package software.spartacus.com.shotclockscorekeeper;

import software.spartacus.com.shotclockscorekeeper.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;

public class MatchCompleteActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_complete);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
