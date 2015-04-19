package software.spartacus.com.shotclockscorekeeper;

import org.json.JSONObject;

public interface HttpGetRequestCompleted {
    void onHttpGetRequestCompleted(JSONObject json);
}
