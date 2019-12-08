package com.nanowheel.nanoux.nanowheel.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.nanowheel.nanoux.nanowheel.MainActivity;
import com.nanowheel.nanoux.nanowheel.R;
import com.nanowheel.nanoux.nanowheel.model.OWDevice;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetSpeedGaugeConfigureActivity WidgetBatteryBarConfigureActivity}
 */
public class WidgetSpeedGauge extends AppWidgetProvider {

    private static final String OnClick = "ClickTag";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //ComponentName thisWidget = new ComponentName(context, WidgetSpeedGauge.class);
        //int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_speed_gauge);
        int speed;

        if (BluetoothService.mService != null && BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicSpeed).value.get() != null){
            speed = Math.round(Util.parseF(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicSpeed).value.get()));
        }else{
            speed = 0;
        }
        int textC = WidgetSpeedGaugeConfigureActivity.loadTitlePref(context,appWidgetId,WidgetSpeedGaugeConfigureActivity.PREFIX_TEXT);
        int textColor = WidgetSpeedGaugeConfigureActivity.colors[textC];

        int backC = WidgetSpeedGaugeConfigureActivity.loadTitlePref(context,appWidgetId,WidgetSpeedGaugeConfigureActivity.PREFIX_BACK);
        int backColor = WidgetSpeedGaugeConfigureActivity.colors[backC];

        views.setTextViewText(R.id.speedTextW,speed+"");
        if (SharedPreferencesUtil.getPrefs(context).isMetric()){
            views.setTextViewText(R.id.speedUnitTextW,"kph");
        }else{
            views.setTextViewText(R.id.speedUnitTextW,"mph");
        }

        views.setTextColor(R.id.speedTextW,textColor);
        views.setTextColor(R.id.speedUnitTextW,textColor);
        if (backC != 4) {
            views.setInt(R.id.speedGaugeBackW, "setVisibility", View.VISIBLE);
            views.setInt(R.id.speedGaugeBackW, "setColorFilter", backColor);
        }else{
            views.setInt(R.id.speedGaugeBackW, "setVisibility", View.INVISIBLE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//remove true to launch app on click for older versions without .startForegroundService
            views.setOnClickPendingIntent(R.id.speedGaugeBackW, getPendingSelfIntent(context, OnClick));
            if (backC == 4){
                views.setOnClickPendingIntent(R.id.speedTextW, getPendingSelfIntent(context, OnClick));
            }
        }else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.speedGaugeBackW, pendingIntent);
            if (backC == 4){
                views.setOnClickPendingIntent(R.id.speedTextW, pendingIntent);
            }
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so updateBattery all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //--WidgetBatteryBarConfigureActivity.deleteTitlePref(context, appWidgetId);
            WidgetSpeedGaugeConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetSpeedGaugeConfigureActivity.PREFIX_TEXT);
            WidgetSpeedGaugeConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetSpeedGaugeConfigureActivity.PREFIX_BACK);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (OnClick.equals(intent.getAction())){
            BluetoothService.createService(context);
        }
    }

    static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, WidgetSpeedGauge.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

