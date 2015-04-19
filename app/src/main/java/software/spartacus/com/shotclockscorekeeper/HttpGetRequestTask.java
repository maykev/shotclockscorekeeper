package software.spartacus.com.shotclockscorekeeper;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class HttpGetRequestTask extends AsyncTask<String, Void, JSONObject> {
    private HttpGetRequestCompleted listener;

    public HttpGetRequestTask(HttpGetRequestCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        JSONObject matches = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty("Accept", "application/json");

            try {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String json;
                while ((json = r.readLine()) != null) {
                    stringBuilder.append(json);
                }
                matches = new JSONObject(stringBuilder.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();

            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return matches;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        listener.onHttpGetRequestCompleted(json);
    }
}
