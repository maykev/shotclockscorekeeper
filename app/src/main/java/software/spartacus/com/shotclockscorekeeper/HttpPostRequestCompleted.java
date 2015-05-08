package software.spartacus.com.shotclockscorekeeper;

import org.json.JSONObject;

public interface HttpPostRequestCompleted {
    void onHttpPostRequestCompleted(JSONObject json);
}
