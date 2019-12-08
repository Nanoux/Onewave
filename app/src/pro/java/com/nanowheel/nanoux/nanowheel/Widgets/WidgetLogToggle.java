package com.nanowheel.nanoux.nanowheel.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.view.View;
import android.widget.RemoteViews;

import com.nanowheel.nanoux.nanowheel.MainActivity;
import com.nanowheel.nanoux.nanowheel.R;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WidgetLogToggleConfigureActivity WidgetBatteryBarConfigureActivity}
 */
public class WidgetLogToggle extends AppWidgetProvider {

    private static final String OnClick = "ClickTag";
    private static final String idsList = "idsIDlol";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //ComponentName thisWidget = new ComponentName(context, WidgetLogToggle.class);
        //int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_log_toggle);
        int textC = WidgetLogToggleConfigureActivity.loadTitlePref(context,appWidgetId,WidgetLogToggleConfigureActivity.PREFIX_TEXT);
        int textColor = WidgetLogToggleConfigureActivity.colors[textC];

        int backC = WidgetLogToggleConfigureActivity.loadTitlePref(context,appWidgetId,WidgetLogToggleConfigureActivity.PREFIX_BACK);
        int backColor = WidgetLogToggleConfigureActivity.colors[backC];

        views.setTextColor(R.id.logTogTextW,textColor);
        if (backC != 4) {
            views.setInt(R.id.logTogBackW, "setVisibility", View.VISIBLE);
            views.setInt(R.id.logTogBackW, "setColorFilter", backColor);
        }else{
            views.setInt(R.id.logTogBackW, "setVisibility", View.INVISIBLE);
        }

        boolean isLog = SharedPreferencesUtil.getPrefs(context).isMap();
        Bitmap omfg;
        if (isLog){
            omfg = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_box_checked);
        }else {
            omfg = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_box);
        }
        omfg = omfg.copy(Bitmap.Config.ARGB_8888, true);
        Canvas can = new Canvas(omfg);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(textColor, PorterDuff.Mode.SRC_IN));
        can.drawBitmap(omfg,0,0,paint);
        views.setImageViewBitmap(R.id.logTogButtonW,omfg);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//remove true to launch app on click for older versions without .startForegroundService
            views.setOnClickPendingIntent(R.id.logTogButtonW, getPendingSelfIntent(context, OnClick,appWidgetManager));
        }else {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.logTogButtonW, pendingIntent);
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
            WidgetLogToggleConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetLogToggleConfigureActivity.PREFIX_TEXT);
            WidgetLogToggleConfigureActivity.deleteTitlePref(context,appWidgetId,WidgetLogToggleConfigureActivity.PREFIX_BACK);
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

            SharedPreferencesUtil.getPrefs(context).setMap(!SharedPreferencesUtil.getPrefs(context).isMap());

            int[] ids = intent.getIntArrayExtra(idsList);

            Intent intentUpdate = new Intent(context, WidgetLogToggle.class);
            intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            try {
                PendingIntent intentP = PendingIntent.getBroadcast(context, 0, intentUpdate, PendingIntent.FLAG_UPDATE_CURRENT);
                intentP.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }

    static PendingIntent getPendingSelfIntent(Context context, String action,AppWidgetManager manager) {

        int[] ids = manager.getAppWidgetIds(new ComponentName(context, WidgetLogToggle.class));
        Intent intent = new Intent(context, WidgetLogToggle.class);
        intent.setAction(action);
        intent.putExtra(idsList,ids);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*if (App.INSTANCE == null)
            App.INSTANCE = new App();

        int[] ids = manager.getAppWidgetIds(new ComponentName(context, WidgetLogToggle.class));

        Intent intentUpdate = new Intent(context, WidgetLogToggle.class);
        intentUpdate.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intentUpdate.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intentUpdate.putExtra("map",SharedPreferencesUtil.getPrefs().isMap());
        return PendingIntent.getBroadcast(context, 0, intentUpdate, 0);*/
    }
}

