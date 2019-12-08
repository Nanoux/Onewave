package com.nanowheel.nanoux.nanowheel.Widgets;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanowheel.nanoux.nanowheel.R;

import androidx.appcompat.app.AlertDialog;

/**
 * The configuration screen for the {@link WidgetLogToggle WidgetBatteryBar} AppWidget.
 */
public class WidgetLogToggleConfigureActivity extends Activity {

    private static final String PREFS_NAME = "WidgetLogToggle";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    public static final String PREFIX_TEXT = "_text";
    public static final String PREFIX_BACK = "_back";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    //static View mView;

    //MaterialButton whiteT, blackT, whiteB,blackB;
    static int textC,backC;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = WidgetLogToggleConfigureActivity.this;

            // When the button is clicked, store the string locally
            //--String widgetText = mAppWidgetText.getText().toString();
            //--saveTitlePref(context, mAppWidgetId, widgetText);
            saveColor(context,mAppWidgetId,PREFIX_TEXT,textC);
            saveColor(context,mAppWidgetId,PREFIX_BACK,backC);

            // It is the responsibility of the configuration activity to updateBattery the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            WidgetLogToggle.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public WidgetLogToggleConfigureActivity() {
        super();
    }


    // Write the prefix to the SharedPreferences object for this widget
    static void saveColor(Context context, int appWidgetId,String type, int color) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId + type, color);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadTitlePref(Context context, int appWidgetId, String type) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getInt(PREF_PREFIX_KEY + appWidgetId + type, 0);
    }


    static void deleteTitlePref(Context context, int appWidgetId, String type) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId + type);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        textC = 0;
        backC = 1;

        setContentView(R.layout.widget_log_toggle_configure);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    public void showPopupBack(View view){
        final View v = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setBackColor(which,v);
            }
        });
        builder.show();
    }

    public void showPopupText(View view){
        final View v = view;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setTextColor(which,v);
            }
        });
        builder.show();
    }
    //public static String[] ids = {"b","w","l","b","t"};
    public static int[] colors = {Color.BLACK, Color.WHITE,Color.parseColor("#0277bd"),Color.parseColor("#80d8ff"),Color.TRANSPARENT};
    public static String[] names = {"Black","White","Blue","Light Blue","Transparent"};
    static void setTextColor(int which, View v){
        WidgetLogToggleConfigureActivity.textC = which;
        TextView hi = v.getRootView().findViewById(R.id.logTogTextC);
        hi.setTextColor(colors[which]);
        ImageButton hi2 = v.getRootView().findViewById(R.id.logTogButtonC);
        hi2.setImageTintList(ColorStateList.valueOf(colors[which]));
        TextView hello = v.getRootView().findViewById(R.id.add_button_text);
        hello.setText(names[which]);
    }
    static void setBackColor(int which, View v){
        WidgetLogToggleConfigureActivity.backC = which;
        ImageView hi = v.getRootView().findViewById(R.id.logTogBackC);
        hi.setImageTintList(ColorStateList.valueOf(colors[which]));
        TextView hello = v.getRootView().findViewById(R.id.add_button_back);
        hello.setText(names[which]);
    }
}

