package software.spartacus.com.shotclockscorekeeper;

import org.json.JSONObject;

public interface HttpRequestCompleted {
    void onHttpRequestCompleted(JSONObject json);
}
