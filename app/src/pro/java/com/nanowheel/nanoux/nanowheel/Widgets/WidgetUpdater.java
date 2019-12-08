package com.nanowheel.nanoux.nanowheel.Widgets;

import android.app.Application;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.recyclerview.widget.RecyclerView;

import com.nanowheel.nanoux.nanowheel.TripDatabase.FragmentMap;
import com.nanowheel.nanoux.nanowheel.R;
import com.nanowheel.nanoux.nanowheel.TripDatabase.TripListAdapter;
import com.nanowheel.nanoux.nanowheel.TripDatabase.TripRepository;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;

public class WidgetUpdater {

    private static TripRepository repo;

    public static void updateBattery(Application app){
        int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app, WidgetBatteryBar.class));
        if (ids.length > 0) {
            Intent intent = new Intent(app, WidgetBatteryBar.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            app.sendBroadcast(intent);
        }
    }

    public static void updateSpeed(Application app){
        int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app, WidgetSpeedGauge.class));
        if (ids.length > 0) {
            Intent intent = new Intent(app, WidgetSpeedGauge.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            app.sendBroadcast(intent);
        }
    }

    public static void updateRange(Application app){
        int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app, WidgetRangeGauge.class));
        if (ids.length > 0) {
            Intent intent = new Intent(app, WidgetRangeGauge.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            app.sendBroadcast(intent);
        }
    }

    public static void updateLog(Application app){
        int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app, WidgetLogToggle.class));
        if (ids.length > 0) {
            Intent intent = new Intent(app, WidgetLogToggle.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            app.sendBroadcast(intent);
        }
    }

    public static void updateVoltage(Application app){
        int[] ids = AppWidgetManager.getInstance(app).getAppWidgetIds(new ComponentName(app, WidgetVoltageGauge.class));
        if (ids.length > 0) {
            Intent intent = new Intent(app, WidgetVoltageGauge.class);
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            app.sendBroadcast(intent);
        }
    }

    public static void setupLog(Application app){
        if (SharedPreferencesUtil.getPrefs(app.getApplicationContext()).isMap()) {
            if (repo == null) {
                repo = new TripRepository(app);
            }
            repo.startLog();
        }
    }

    public static void markLog(Context context){
        if (repo != null && SharedPreferencesUtil.getPrefs(context).isMap())
            repo.markLog();
    }

    public static void endLog(){
        if (repo != null)
            repo.endLog();
    }

    public static void revLog(Context context){
        if (repo != null && SharedPreferencesUtil.getPrefs(context).isMap())
            repo.revLog();
    }

    public static boolean backProcess(int id, FragmentMap frag, Context con){

        if (id == 2 && frag != null){
            if (frag.getView().findViewById(R.id.chart_layout).getVisibility() == View.VISIBLE){
                View layout = frag.getView().findViewById(R.id.chart_layout);
                if (layout.getVisibility() == View.VISIBLE){
                    Animation out = AnimationUtils.loadAnimation(con, R.anim.fade_out);
                    layout.startAnimation(out);
                }
                layout.setVisibility(View.INVISIBLE);
                TripListAdapter.showLines((RecyclerView)frag.getView().findViewById(R.id.recyclerview));
                return true;
            }else if (FragmentMap.mSelectionTracker != null && FragmentMap.mSelectionTracker.hasSelection()){
                FragmentMap.mSelectionTracker.clearSelection();
                frag.hideBar();
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    public static void checkLogWarning(int id, FragmentMap frag){
        if (id == 2 && frag != null){
            frag.mapWarning(SharedPreferencesUtil.getPrefs(frag.getContext()).isMap());
        }
    }

}
