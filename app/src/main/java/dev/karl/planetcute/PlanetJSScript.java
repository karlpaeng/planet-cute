package dev.karl.planetcute;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PlanetJSScript {
    private final Context planet_context;

    public PlanetJSScript(Context context) {
        this.planet_context = context;
    }


    @JavascriptInterface
    public void postMessage(String name, String data) {
        Map<String, Object> eventValue = new HashMap<>();
        if ("openWindow".equals(name)) {
            try {
                JSONObject extLink = new JSONObject(data); // Assuming the data is a JSON string
                Intent newWindow = new Intent(Intent.ACTION_VIEW);
                newWindow.setData(Uri.parse(extLink.getString("url")));
                newWindow.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                planet_context.startActivity(newWindow);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            eventValue.put(name, data);
        }
    }
}
