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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ChatPlugin extends CordovaPlugin {

  private static final String ACTION_SHOW_BAR      = "showBar";
  private static final String ACTION_HIDE_BAR      = "hideBar";
  private static final String ACTION_HIDE_KEYBOARD = "hideKeyboard";

  private FrameLayout _myLayout;
  private EditText _myEditText;

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

  // public void showBar(final CallbackContext callbackContext) {
  //   final EditText myEditText = new EditText(cordova.getActivity());
  //   myEditText.setHint("Message");
  //   myEditText.setEms(10);
  //   myEditText.setInputType(myEditText.getInputType() | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
  //   myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
  //     @Override
  //     public void onFocusChange(View v, boolean hasFocus) {
  //       if (hasFocus) {
  //         sendResult(Response.FOCUS, callbackContext);
  //       }
  //     }
  //   });

  //   FrameLayout.LayoutParams editTextParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
  //   editTextParams.gravity = Gravity.BOTTOM | Gravity.START;

  //   Button myButton = new Button(cordova.getActivity());
  //   myButton.setText("SEND");
  //   // myButton.setBackgroundColor(Color.parseColor("#e040fb"));
  //   GradientDrawable background = new GradientDrawable();
  //   background.setColor(Color.parseColor("#e040fb"));
  //   background.setCornerRadius(8);
  //   myButton.setBackground(background);
  //   myButton.setTextColor(Color.WHITE);
  //   myButton.setPadding(32, 16, 32, 16);
  //   myButton.setOnClickListener(new View.OnClickListener() {
  //     @Override
  //     public void onClick(View v) {
  //       String text = myEditText.getText().toString();
  //       sendResult(Response.MESSAGE, text, callbackContext);
  //       myEditText.setText("");
  //     }
  //   });

  //   FrameLayout.LayoutParams buttonParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
  //   buttonParams.gravity = Gravity.BOTTOM | Gravity.END;

  //   final FrameLayout myLayout = new FrameLayout(cordova.getActivity());
  //   myLayout.setBackgroundColor(Color.WHITE);
  //   myLayout.setPadding(16, 16, 16, 16);
  //   final FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
  //   layoutParams.gravity = Gravity.BOTTOM | Gravity.START;

  //   myLayout.addView(myEditText, editTextParams);
  //   myLayout.addView(myButton, buttonParams);

  //   this._myLayout = myLayout;

  //   cordova.getActivity().runOnUiThread(new Runnable() {
  //     public void run() {
  //       cordova.getActivity().addContentView(myLayout, layoutParams);
  //       sendResult(Response.NORMAL, callbackContext);
  //     }
  //   });
  // }






  // public void showBar(final CallbackContext callbackContext) {
  //   final EditText myEditText = new EditText(cordova.getActivity());
  //   myEditText.setHint("Message");
  //   myEditText.setEms(10);
  //   myEditText.setInputType(myEditText.getInputType() | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

  //   GradientDrawable editTextBackground = new GradientDrawable();
  //   editTextBackground.setColor(Color.WHITE);
  //   editTextBackground.setCornerRadius(8);
  //   editTextBackground.setStroke(2, Color.parseColor("#c0c0c0"));
  //   myEditText.setBackground(editTextBackground);

  //   myEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
  //     @Override
  //     public void onFocusChange(View v, boolean hasFocus) {
  //       if (hasFocus) {
  //         sendResult(Response.FOCUS, callbackContext);
  //       }
  //     }
  //   });

  //   RelativeLayout.LayoutParams editTextParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
  //   editTextParams.addRule(RelativeLayout.ALIGN_PARENT_START);
  //   editTextParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

  //   Button myButton = new Button(cordova.getActivity());
  //   myButton.setText("SEND");
  //   // myButton.setBackgroundColor(Color.parseColor("#e040fb"));
  //   GradientDrawable buttonBackground = new GradientDrawable();
  //   buttonBackground.setColor(Color.parseColor("#e040fb"));
  //   buttonBackground.setCornerRadius(8);
  //   myButton.setBackground(buttonBackground);
  //   myButton.setTextColor(Color.WHITE);
  //   myButton.setPadding(32, 16, 32, 16);
  //   myButton.setId(1000);
  //   myButton.setOnClickListener(new View.OnClickListener() {
  //     @Override
  //     public void onClick(View v) {
  //       String text = myEditText.getText().toString();
  //       sendResult(Response.MESSAGE, text, callbackContext);
  //       myEditText.setText("");
  //     }
  //   });

  //   RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
  //   buttonParams.addRule(RelativeLayout.ALIGN_PARENT_END);
  //   buttonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

  //   editTextParams.addRule(RelativeLayout.LEFT_OF, myButton.getId());

  //   final RelativeLayout myLayout = new RelativeLayout(cordova.getActivity());
  //   myLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
  //   myLayout.setPadding(16, 16, 16, 16);
  //   final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
  //   // layoutParams.gravity = Gravity.BOTTOM | Gravity.START;
  //   layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START);
  //   layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

  //   myLayout.addView(myButton, buttonParams);
  //   myLayout.addView(myEditText, editTextParams);

  //   this._myLayout = myLayout;

  //   cordova.getActivity().runOnUiThread(new Runnable() {
  //     public void run() {
  //       cordova.getActivity().addContentView(myLayout, layoutParams);
  //       sendResult(Response.NORMAL, callbackContext);
  //     }
  //   });
  // }


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

    this._myEditText = myEditText;

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
    // buttonParams.setMarginStart(20);
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
    cordova.getActivity().runOnUiThread(new Runnable() {
      public void run() {
        ViewGroup parent = (ViewGroup)myLayout.getParent();
        parent.removeView(myLayout);
        callbackContext.success("Hidden");
      }
    });
  }

  public void hideKeyboard(final CallbackContext callbackContext) {
    final EditText myEditText = this._myEditText;
    //https://github.com/driftyco/ionic-plugin-keyboard/blob/master/src/android/IonicKeyboard.java
    cordova.getThreadPool().execute(new Runnable() {
      public void run() {
        InputMethodManager inputManager = (InputMethodManager)cordova.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View focussedView = cordova.getActivity().getCurrentFocus();

        if (focussedView == null) {
          callbackContext.error("No current focus");
        } else {
          inputManager.hideSoftInputFromWindow(focussedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
          // myEditText.clearFocus();
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
