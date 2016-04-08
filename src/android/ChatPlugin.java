package com.megaphone.cordova.chat;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
// import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class ChatPlugin extends CordovaPlugin {

  private FrameLayout _myLayout;

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("showBar")) {
      this.showBar(callbackContext);
      return true;
    } else if (action.equals("hideBar")) {
      this.hideBar(callbackContext);
      return true;
    } else if (action.equals("hideKeyboard")) {
      this.hideKeyboard(callbackContext);
      return true;
    }
    return false;
  }

  public void showBar(final CallbackContext callbackContext) {
    EditText myEditText = new EditText(cordova.getActivity());
    myEditText.setHint("Message");
    myEditText.setEms(10);
    myEditText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
    FrameLayout.LayoutParams editTextParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    editTextParams.gravity = Gravity.BOTTOM | Gravity.START;

    Button myButton = new Button(cordova.getActivity());
    myButton.setText("Press Me");
    myButton.setBackgroundColor(Color.parseColor("#e040fb"));
    myButton.setTextColor(Color.WHITE);
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
        callbackContext.success("Shown");
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

  public void hideKeyboard(CallbackContext callbackContext) {
    callbackContext.success("Shown Keyboard");
  }
}
