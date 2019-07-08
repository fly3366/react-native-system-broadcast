package top.litop.RroadCast;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;


import android.util.Log;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

import static android.content.ContentValues.TAG;

/**
 * SplashScreen
 * 启动屏
 * from：http://www.devio.org
 * Author:CrazyCodeBoy
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class RroadCastModule extends ReactContextBaseJavaModule {
    public RroadCastModule(ReactApplicationContext reactContext) {
        super(reactContext);

    }

    @Override
    public String getName() {
        return "RNBroadCast";
    }

    /**
     * SendEvent
     */
    @ReactMethod
    public void sendEvent(String eventName, String json) {
        try {
            this.sendBroadCast(eventName, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * ReceiveEvent
     */
    @ReactMethod
    public void receiveEvent(boolean state) {
        if (state) this.ON();
        else this.OFF();
    }

    public void sendBroadCast(String action, String json) throws JSONException {

        JSONObject jsonObject = new JSONObject(json);

        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtras(fromJson(jsonObject));
        getReactApplicationContext().sendBroadcast(intent);
    }

    Activity mActivity = getCurrentActivity();

    receiveBroadCast rb = new receiveBroadCast();



    public void ON() {
        getReactApplicationContext().registerReceiver(rb,null);

    }

    public void OFF() {
        getReactApplicationContext().unregisterReceiver(rb);
    }


    public Bundle fromJson(JSONObject s) {
        Bundle bundle = new Bundle();

        for (Iterator<String> it = s.keys(); it.hasNext(); ) {
            String key = it.next();
            JSONArray arr = s.optJSONArray(key);
            Double num = s.optDouble(key);
            String str = s.optString(key);

            if (arr != null && arr.length() <= 0)
                bundle.putStringArray(key, new String[]{});

            else if (arr != null && !Double.isNaN(arr.optDouble(0))) {
                double[] newarr = new double[arr.length()];
                for (int i = 0; i < arr.length(); i++)
                    newarr[i] = arr.optDouble(i);
                bundle.putDoubleArray(key, newarr);
            } else if (arr != null && arr.optString(0) != null) {
                String[] newarr = new String[arr.length()];
                for (int i = 0; i < arr.length(); i++)
                    newarr[i] = arr.optString(i);
                bundle.putStringArray(key, newarr);
            } else if (!num.isNaN())
                bundle.putDouble(key, num);

            else if (str != null)
                bundle.putString(key, str);

            else
                System.err.println("unable to transform json to bundle " + key);
        }

        return bundle;
    }


    class receiveBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            WritableMap idData = Arguments.createMap();
            Bundle bd = intent.getExtras();
            Set<String> st = bd.keySet();
            for (String str : st) {
                idData.putString(str, String.valueOf(bd.get(str)));
            }
            sendEventToJs(RroadCastModule.this.getReactApplicationContext(), intent.getAction(), idData);
        }

        public void sendEventToJs(ReactContext reactContext, String eventName, WritableMap params) {
            Log.d(TAG, "sendEventToJs: "+eventName+"==="+params);
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }

}


