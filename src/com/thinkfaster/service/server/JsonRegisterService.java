package com.thinkfaster.service.server;

import android.util.Log;
import com.google.gson.Gson;
import com.thinkfaster.model.json.DeviceParameters;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by brekol on 06.05.15.
 */
public class JsonRegisterService {

    private static final String TAG = "JsonRegisterService";

    public void sendRegistrationIdToServer(String registrationId) {

        try {

            DeviceParameters deviceParameters = new DeviceParameters();
            deviceParameters.setRegistrationId(registrationId);

            HttpPost post = new HttpPost();
            post.setURI(new URI("http://192.168.0.191:8080/newgameuser"));
            post.setHeader(new BasicHeader("Content-Type", "application/json; charset=utf-8"));
            post.setEntity(new StringEntity(new Gson().toJson(deviceParameters, DeviceParameters.class)));

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(post);
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
    }
}
