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

public class WidgetVoltageGauge extends AppWidgetProvider {

    private static final String OnClick = "ClickTag";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //ComponentName thisWidget = new ComponentName(context, WidgetVoltageGauge.class);
        //int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_voltage_gauge);
        float voltage;
        if (BluetoothService.mService != null && BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() != null){
            voltage = Util.parseF(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get());
        }else{
            voltage = 0;
        }
        int textC = WidgetVoltageGaugeConfigureActivity.loadTitlePref(context,appWidgetId,WidgetVoltageGaugeConfigureActivity.PREFIX_TEXT);
        int textColor = WidgetVoltageGaugeConfigureActivity.colors[textC];

        int backC = WidgetVoltageGaugeConfigureActivity.loadTitlePref(context,appWidgetId,WidgetVoltageGaugeConfigureActivity.PREFIX_BACK);
        int backColor = WidgetVoltageGaugeConfigureActivity.colors[backC];

        views.setTextViewText(R.id.voltageTextW,String.format(Locale.getDefault(),"%3.1f",voltage));
        /*if (SharedPreferencesUtil.getPrefs(context).isMetric()){
            views.setTextViewText(R.id.voltageUnitTextW,"km");
        }else{
            views.setTextViewText(R.id.voltageUnitTextW,"mi");
        }*/

        views.setTextColor(R.id.voltageTextW,textColor);
        views.setTextColor(R.id.voltageUnitTextW,textColor);
        if (backC != 4) {
            views.setInt(R.id.voltageGaugeBackW, "setVisibility", View.VISIBLE);
            views.setInt(R.id.voltageGaugeBackW, "setColorFilter", backColor);
        }else{
            views.setInt(R.id.voltageGaugeBackW, "setVisibility", View.INVISIBLE);
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//remove true to launch app on click for older versions without .startForegroundService
            views.setOnClickPendingIntent(R.id.voltageGaugeBackW, getPendingSelfIntent(context, OnClick));
            if (backC == 4){
                views.setOnClickPendingIntent(R.id.voltageTextW, getPendingSelfIntent(context, OnClick));
            }
        }else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.voltageGaugeBackW, pendingIntent);
            if (backC == 4){
                views.setOnClickPendingIntent(R.id.voltageTextW, pendingIntent);
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
            //--WidgetVoltageGaugeConfigureActivity.deleteTitlePref(context, appWidgetId);
            WidgetVoltageGaugeConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetVoltageGaugeConfigureActivity.PREFIX_TEXT);
            WidgetVoltageGaugeConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetVoltageGaugeConfigureActivity.PREFIX_BACK);
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
        Intent intent = new Intent(context, WidgetVoltageGauge.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
