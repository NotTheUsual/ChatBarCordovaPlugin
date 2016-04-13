package com.megaphone.cordova.chat;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class ChatPlugin extends CordovaPlugin {

  private static final String ACTION_SHOW_BAR      = "showBar";
  private static final String ACTION_HIDE_BAR      = "hideBar";
  private static final String ACTION_HIDE_KEYBOARD = "hideKeyboard";

  private FrameLayout _myLayout;

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals(ACTION_SHOW_BAR)) {
      this.showBar(callbackContext);
      return true;
    } else if (action.equals(ACTION_HIDE_BAR)) {
      this.hideBar(callbackContext);
      return true;
    } else if (action.equals(ACTION_HIDE_KEYBOARD)) {
      this.hideKeyboard(callbackContext);
      return true;
    }
    return false;
  }

  public void showBar(final CallbackContext callbackContext) {
    final EditText myEditText = new EditText(cordova.getActivity());
    myEditText.setHint("Message");
    myEditText.setEms(10);
    myEditText.setInputType(myEditText.getInputType() | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          sendResult(Response.FOCUS, callbackContext);
        }
      }
    });

    FrameLayout.LayoutParams editTextParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    editTextParams.gravity = Gravity.BOTTOM | Gravity.START;

    Button myButton = new Button(cordova.getActivity());
    myButton.setText("Press Me");
    myButton.setBackgroundColor(Color.parseColor("#e040fb"));
    myButton.setTextColor(Color.WHITE);
    myButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //////////////////////////

        // String text = myEditText.getText().toString();
        // JSONObject response = new JSONObject();
        // try {
        //   response.put("action", "message");
        //   response.put("message", text);
        // } catch (JSONException e) {
        //   e.printStackTrace();
        // }

        // PluginResult result = new PluginResult(PluginResult.Status.OK, response);
        // result.setKeepCallback(true);
        // callbackContext.sendPluginResult(result);
        // myEditText.setText("");

        //////////////////////////

        String text = myEditText.getText().toString();
        sendResult(Response.MESSAGE, text, callbackContext);
        myEditText.setText("");
      }
    });

    FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    buttonParams.gravity = Gravity.BOTTOM | Gravity.END;

    final FrameLayout myLayout = new FrameLayout(cordova.getActivity());
    myLayout.setBackgroundColor(Color.WHITE);
    myLayout.setPadding(16, 16, 16, 16);
    final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.BOTTOM | Gravity.START;

    myLayout.addView(myEditText, editTextParams);
    myLayout.addView(myButton, buttonParams);

    this._myLayout = myLayout;

    // this.root.addView(myLayout);
    cordova.getActivity().runOnUiThread(new Runnable() {
      public void run() {
        cordova.getActivity().addContentView(myLayout, layoutParams);
        // PluginResult result = new PluginResult(PluginResult.Status.OK, "Shown");
        // result.setKeepCallback(true);
        // callbackContext.sendPluginResult(result);
        sendResult(Response.NORMAL, callbackContext);
      }
    });
  }

  public void hideBar(final CallbackContext callbackContext) {
    final FrameLayout myLayout = _myLayout;
    cordova.getActivity().runOnUiThread(new Runnable() {
      public void run() {
        ViewGroup parent = (ViewGroup)myLayout.getParent();
        parent.removeView(myLayout);
        callbackContext.success("Hidden");
      }
    });
  }

  public void hideKeyboard(final CallbackContext callbackContext) {
    //https://github.com/driftyco/ionic-plugin-keyboard/blob/master/src/android/IonicKeyboard.java
    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        InputMethodManager inputManager = (InputMethodManager)cordova.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focussedView = cordova.getActivity().getCurrentFocus();

        if (focussedView == null) {
          callbackContext.error("No current focus");
        } else {
          inputManager.hideSoftInputFromWindow(focussedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
          callbackContext.success();
        }
      }
    });
  }

  private void sendResult(Response action, CallbackContext callbackContext) {
    sendResult(action, null, callbackContext);
  }

  private void sendResult(Response action, String message, CallbackContext callbackContext) {
    JSONObject response = new JSONObject();
    try {
      response.put("action", action.name);
      if (action.equals(Response.MESSAGE)) {
        response.put("message", message);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    PluginResult result = new PluginResult(PluginResult.Status.OK, response);
    result.setKeepCallback(true);
    callbackContext.sendPluginResult(result);
  }

  private enum Response {
    NORMAL("normal"), MESSAGE("message"), FOCUS("focus");
    public String name;

    Response(String name) {
      this.name = name;
    }
  }
}
