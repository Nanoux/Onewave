package com.nanowheel.nanoux.nanowheel.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.nanowheel.nanoux.nanowheel.MainActivity;
import com.nanowheel.nanoux.nanowheel.R;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.Util;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetBatteryBarConfigureActivity WidgetBatteryBarConfigureActivity}
 */
public class WidgetBatteryBar extends AppWidgetProvider {

    private static final String OnClick = "ClickTag";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //ComponentName thisWidget = new ComponentName(context, WidgetBatteryBar.class);
        //int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_battery_bar);
        int progress;
        if (BluetoothService.mService != null && BluetoothService.mService.mOWDevice.getReadCharacteristics().get(4).value.get() != null){
            progress = Util.parseI(BluetoothService.mService.mOWDevice.getReadCharacteristics().get(4).value.get());
        }else{
            progress = 0;
        }
        int textC = WidgetBatteryBarConfigureActivity.loadTitlePref(context,appWidgetId,WidgetBatteryBarConfigureActivity.PREFIX_TEXT);
        int textColor = WidgetBatteryBarConfigureActivity.colors[textC];

        int backC = WidgetBatteryBarConfigureActivity.loadTitlePref(context,appWidgetId,WidgetBatteryBarConfigureActivity.PREFIX_BACK);
        int backColor = WidgetBatteryBarConfigureActivity.colors[backC];

        views.setProgressBar(R.id.batteryBarW,100,progress,false);
        views.setTextViewText(R.id.batteryTextW,progress+"%");

        views.setTextColor(R.id.batteryTextW,textColor);
        if (backC != 4) {
            views.setInt(R.id.batteryBarBackW, "setVisibility", View.VISIBLE);
            views.setInt(R.id.batteryBarBackW, "setColorFilter", backColor);
        }else{
            views.setInt(R.id.batteryBarBackW, "setVisibility", View.INVISIBLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//remove true to launch app on click for older versions without .startForegroundService
            views.setOnClickPendingIntent(R.id.batteryBarBackW, getPendingSelfIntent(context, OnClick));
        }else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.batteryBarBackW, pendingIntent);
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
            WidgetBatteryBarConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetBatteryBarConfigureActivity.PREFIX_TEXT);
            WidgetBatteryBarConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetBatteryBarConfigureActivity.PREFIX_BACK);
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
        Intent intent = new Intent(context, WidgetBatteryBar.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

