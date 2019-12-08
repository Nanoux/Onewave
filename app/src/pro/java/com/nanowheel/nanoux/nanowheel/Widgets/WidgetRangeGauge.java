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
import com.nanowheel.nanoux.nanowheel.model.OWDevice;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;

import java.util.Locale;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetRangeGaugeConfigureActivity WidgetRangeGaugeConfigureActivity}
 */
public class WidgetRangeGauge extends AppWidgetProvider {

    private static final String OnClick = "ClickTag";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //ComponentName thisWidget = new ComponentName(context, WidgetRangeGauge.class);
        //int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_range_gauge);
        float range;
        if (BluetoothService.mService != null && BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometerRange).value.get() != null){
            range = Util.parseF(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometerRange).value.get());
        }else{
            range = 0;
        }
        int textC = WidgetRangeGaugeConfigureActivity.loadTitlePref(context,appWidgetId,WidgetRangeGaugeConfigureActivity.PREFIX_TEXT);
        int textColor = WidgetRangeGaugeConfigureActivity.colors[textC];

        int backC = WidgetRangeGaugeConfigureActivity.loadTitlePref(context,appWidgetId,WidgetRangeGaugeConfigureActivity.PREFIX_BACK);
        int backColor = WidgetRangeGaugeConfigureActivity.colors[backC];

        views.setTextViewText(R.id.rangeTextW,String.format(Locale.getDefault(),"%3.1f",range));
        if (SharedPreferencesUtil.getPrefs(context).isMetric()){
            views.setTextViewText(R.id.rangeUnitTextW,"km");
        }else{
            views.setTextViewText(R.id.rangeUnitTextW,"mi");
        }

        views.setTextColor(R.id.rangeTextW,textColor);
        views.setTextColor(R.id.rangeUnitTextW,textColor);
        if (backC != 4) {
            views.setInt(R.id.rangeGaugeBackW, "setVisibility", View.VISIBLE);
            views.setInt(R.id.rangeGaugeBackW, "setColorFilter", backColor);
        }else{
            views.setInt(R.id.rangeGaugeBackW, "setVisibility", View.INVISIBLE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//remove true to launch app on click for older versions without .startForegroundService
            views.setOnClickPendingIntent(R.id.rangeGaugeBackW, getPendingSelfIntent(context, OnClick));
            if (backC == 4){
                views.setOnClickPendingIntent(R.id.rangeTextW, getPendingSelfIntent(context, OnClick));
            }
        }else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.rangeGaugeBackW, pendingIntent);
            if (backC == 4){
                views.setOnClickPendingIntent(R.id.rangeTextW, pendingIntent);
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
            //--WidgetRangeGaugeConfigureActivity.deleteTitlePref(context, appWidgetId);
            WidgetRangeGaugeConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetRangeGaugeConfigureActivity.PREFIX_TEXT);
            WidgetRangeGaugeConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetRangeGaugeConfigureActivity.PREFIX_BACK);
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
        Intent intent = new Intent(context, WidgetRangeGauge.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

}

