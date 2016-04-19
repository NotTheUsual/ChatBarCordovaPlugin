package com.megaphone.cordova.chat;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ChatPlugin extends CordovaPlugin {

  private static final String ACTION_SHOW_BAR         = "showBar";
  private static final String ACTION_HIDE_BAR         = "hideBar";
  private static final String ACTION_HIDE_KEYBOARD    = "hideKeyboard";
  private static final String ACTION_SHOW_MESSAGE_BAR = "showNewMessageBar";
  private static final String ACTION_HIDE_MESSAGE_BAR = "hideNewMessageBar";

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
    } else if (action.equals(ACTION_SHOW_MESSAGE_BAR)) {
      this.showNewMessageBar(args, callbackContext);
      return true;
    } else if (action.equals(ACTION_HIDE_MESSAGE_BAR)) {
      this.hideNewMessageBar(callbackContext);
      return true;
    }
    return false;
  }

  public void showBar(final CallbackContext callbackContext) {
    final EditText myEditText = new EditText(cordova.getActivity());
    myEditText.setHint("Message");
    myEditText.setEms(10);
    myEditText.setPadding(16, 18, 16, 18);
    myEditText.setInputType(myEditText.getInputType() | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

    GradientDrawable editTextBackground = new GradientDrawable();
    editTextBackground.setColor(Color.WHITE);
    editTextBackground.setCornerRadius(8);
    editTextBackground.setStroke(2, Color.parseColor("#c0c0c0"));
    myEditText.setBackground(editTextBackground);

    myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          sendResult(Response.FOCUS, callbackContext);
        }
      }
    });

    LinearLayout.LayoutParams editTextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
    editTextParams.gravity = Gravity.BOTTOM | Gravity.START;
    editTextParams.setLayoutDirection(LinearLayout.HORIZONTAL);

    Button myButton = new Button(cordova.getActivity());
    myButton.setText("SEND");
    GradientDrawable buttonBackground = new GradientDrawable();
    buttonBackground.setColor(Color.parseColor("#e040fb"));
    buttonBackground.setCornerRadius(8);
    myButton.setBackground(buttonBackground);
    myButton.setTextColor(Color.WHITE);
    myButton.setPadding(32, 16, 32, 16);
    myButton.setId(1000);
    myButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = myEditText.getText().toString();
        sendResult(Response.MESSAGE, text, callbackContext);
        myEditText.setText("");
      }
    });

    LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    buttonParams.gravity = Gravity.BOTTOM;
    buttonParams.setMargins(20, 0, 0, 3);
    buttonParams.setLayoutDirection(LinearLayout.HORIZONTAL);

    final LinearLayout myLayout = new LinearLayout(cordova.getActivity());
    myLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
    myLayout.setPadding(16, 16, 16, 16);
    myLayout.setOrientation(LinearLayout.HORIZONTAL);
    myLayout.setGravity(Gravity.BOTTOM);
    final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
    layoutParams.setLayoutDirection(LinearLayout.HORIZONTAL);

    myLayout.addView(myEditText, editTextParams);
    myLayout.addView(myButton, buttonParams);

    final FrameLayout frame = new FrameLayout(cordova.getActivity());
    final FrameLayout.LayoutParams frameLayout = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    frameLayout.gravity = Gravity.BOTTOM | Gravity.START;

    frame.addView(myLayout, layoutParams);

    this._myLayout = frame;

    cordova.getActivity().runOnUiThread(new Runnable() {
      public void run() {
        cordova.getActivity().addContentView(frame, frameLayout);
        sendResult(Response.NORMAL, callbackContext);
      }
    });
  }

  public void hideBar(final CallbackContext callbackContext) {
    final FrameLayout myLayout = _myLayout;
    if (myLayout == null) return;
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

  public void showNewMessageBar(JSONArray args, final CallbackContext callbackContext) {
    // try {
    //   int messageCount = args.getInt(0);
    //   String messageBody = (messageCount == 1) ? " New Message" : " New Messages";
    //   String message = String.format("%d%s", messageCount, messageBody);
    //   Toast toast = Toast.makeText(cordova.getActivity(), message, Toast.LENGTH_SHORT);
    //   toast.show();
    // } catch (JSONException e) {
    //   e.printStackTrace();
    // }

    Toast toast = Toast.makeText(cordova.getActivity(), "New Message!", Toast.LENGTH_SHORT);
    toast.show();
  }

  public void hideNewMessageBar(final CallbackContext callbackContext) {
    // Log.d("ChatPlugin", "hide new message bar");
    return;
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
