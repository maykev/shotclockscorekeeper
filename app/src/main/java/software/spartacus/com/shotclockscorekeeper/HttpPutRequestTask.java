package software.spartacus.com.shotclockscorekeeper;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpPutRequestTask extends AsyncTask<String, Void, Boolean> {
    private HttpPutRequestCompleted listener;

    public HttpPutRequestTask(HttpPutRequestCompleted listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        Boolean success = false;
        HttpURLConnection urlConnection = null;
        URL url = null;
        OutputStreamWriter outputStreamWriter = null;

        try {
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("PUT");

            outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(params[1]);
            outputStreamWriter.flush();

            int statusCode = urlConnection.getResponseCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                success = true;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();

            try {
                outputStreamWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    @Override
    protected void onPostExecute(Boolean success) {
        listener.onHttpPutRequestCompleted(success);
    }
}
