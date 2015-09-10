package com.thinkfaster.service.server;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by brekol on 12.05.15.
 */
public class GameService extends AbstractServerService {
    private static final String TAG = "GameService";

    public String getTimeTillNextGame() {
        try {

            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI(getLink("get/nextgametime")));
            httpGet.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpGet);
            Log.i(TAG, ">> Response" + response);

        } catch (URISyntaxException e) {
            Log.e(TAG, ">> Error", e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, ">> Error", e);
        } catch (ClientProtocolException e) {
            Log.e(TAG, ">> Error", e);
        } catch (IOException e) {
            Log.e(TAG, ">> Error", e);
        }
        return "";
    }
}
