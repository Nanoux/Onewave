package com.nanowheel.nanoux.nanowheel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.nanowheel.nanoux.nanowheel.TripDatabase.FragmentMap;
import com.nanowheel.nanoux.nanowheel.Widgets.WidgetUpdater;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.HTTPUtil;
import com.nanowheel.nanoux.nanowheel.util.NotificationBuilder;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,SharedPreferences.OnSharedPreferenceChangeListener{

    Context mContext;
    int frag;
    BottomNavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;
        SharedPreferencesUtil test = SharedPreferencesUtil.getPrefs(this);
        test.registerListener(this);


        if (BuildConfig.FLAVOR == "pro"){//this makes loads map fragment faster no touchy!
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        MapView mv = new MapView(getApplicationContext());
                        mv.onCreate(null);
                        mv.onPause();
                        mv.onDestroy();
                    }catch (Exception ignored){

                    }
                }
            }).start();
        }

        //boolean isDark = SharedPreferencesUtil.getPrefs().isDarkNightMode();
        updateTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        navigation = findViewById(R.id.bottom_navigation);

        if (savedInstanceState != null && SharedPreferencesUtil.getPrefs(this).getDialogAccepted() && SharedPreferencesUtil.getPrefs(this).getPintDialogAccepted()){
            if (savedInstanceState.getBoolean("should_scan")){
                StartService();
            }
            frag = savedInstanceState.getInt("active_frag");
            switch (frag){
                case 0:
                    loadFragment(new FragmentOverview(),false);
                    navigation.setSelectedItemId(R.id.overview_button);
                    break;
                case 1:
                    loadFragment(new FragmentDiagnotics(),false);
                    navigation.setSelectedItemId(R.id.diagnotics_button);
                    break;
                case 2:
                    loadFragment(new FragmentMap(),false);
                    navigation.setSelectedItemId(R.id.map_button);
                    break;
                case 3:
                    loadFragment(new FragmentSettings(),false);
                    navigation.setSelectedItemId(R.id.settings_button);
                    break;
            }

        }else {
            if (intent.getBooleanExtra("should_scan",true)) {
                StartService();
                loadFragment(new FragmentOverview(),false);
                navigation.setSelectedItemId(R.id.overview_button);
                frag = 0;
            }else{
                loadFragment(new FragmentSettings(),false);
                navigation.setSelectedItemId(R.id.settings_button);
                frag = 3;
            }
        }

        navigation.setOnNavigationItemSelectedListener(this);
        /*if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !isDark) {
            View view = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR | SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(uiOptions);
        }*/

        if (SharedPreferencesUtil.getPrefs(mContext).isUpdateAvailable()){
            navigation.showBadge(R.id.settings_button);
        }else{
            navigation.removeBadge(R.id.settings_button);
        }
        checkUpdates(false);

        Util.isDark(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mContext = null;
        SharedPreferencesUtil test = SharedPreferencesUtil.getPrefs(this);
        test.removeListener(this);
    }

    private boolean loadFragment(Fragment fragment, boolean animate){
        if (fragment != null){
            androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if (animate) {
                ft.setCustomAnimations(R.anim.slide_in, R.anim.slide_out);
            }

            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
            return true;
        }

        return false;
    }

    FragmentMap mapFrag;
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        Fragment fragment = null;
        switch(menuItem.getItemId()){
            case R.id.overview_button:
                if (frag != 0) {
                    fragment = new FragmentOverview();
                    frag = 0;
                    mapFrag = null;
                }
                break;

            case R.id.diagnotics_button:
                if (frag != 1) {
                    fragment = new FragmentDiagnotics();
                    frag = 1;
                    mapFrag = null;
                }
                break;

            case R.id.map_button:
                if (frag != 2) {
                    mapFrag = new FragmentMap();
                    fragment = mapFrag;
                    frag = 2;

                }
                break;

            case R.id.settings_button:
                if (frag != 3) {
                    fragment = new FragmentSettings();
                    frag = 3;
                    mapFrag = null;
                }
                break;
        }
        return loadFragment(fragment, true);
    }


    final int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 2442;
    final int MY_PERMISSIONS_SYSTEM_ALERT_WINDOW = 2443;
    public void StartService(){
        if (!SharedPreferencesUtil.getPrefs(this).getPintDialogAccepted()){
            final RelativeLayout layout = findViewById(R.id.pint_disclaimer);
            layout.setVisibility(View.VISIBLE);
            Button button = findViewById(R.id.pint_disclaimer_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    layout.startAnimation(out);
                    SharedPreferencesUtil.getPrefs(mContext).setPintDialogAccepted(true);
                    StartService();
                }
            });
        }else if (!SharedPreferencesUtil.getPrefs(this).getDialogAccepted()){
            final RelativeLayout layout = findViewById(R.id.disclaimer);
            layout.setVisibility(View.VISIBLE);
            Button button = findViewById(R.id.disclaimer_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out);
                    out.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layout.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    layout.startAnimation(out);
                    SharedPreferencesUtil.getPrefs(mContext).setDialogAccepted(true);
                    StartService();
                }
            });
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    BluetoothService.createService(this);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_ACCESS_FINE_LOCATION);
                }
            } else {
                BluetoothService.createService(this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                BluetoothService.createService(mContext);
            } //else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            //}
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("active_frag",frag);
        outState.putBoolean("should_scan",false);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SharedPreferencesUtil.METRIC_UNITS:
                if (BluetoothService.isExist) {
                    BluetoothService.mService.mOWDevice.refreshUnits(this);
                }
                WidgetUpdater.updateSpeed(getApplication());
                WidgetUpdater.updateRange(getApplication());
                break;

            case SharedPreferencesUtil.APP_THEME:
                updateTheme();
                break;

            case SharedPreferencesUtil.LOG_TRIPS:
                WidgetUpdater.updateLog(getApplication());
                if (!SharedPreferencesUtil.getPrefs(mContext).isMap()) {
                    WidgetUpdater.endLog();
                }
                if (BuildConfig.FLAVOR == "pro") {
                    if (frag == 2) {
                        WidgetUpdater.checkLogWarning(frag,mapFrag);
                    }
                    //if (frag == 3){
                        //loadFragment(new FragmentSettings(),false);
                    //}
                }
                break;

            case SharedPreferencesUtil.FLOAT_STAT:
                if (SharedPreferencesUtil.getPrefs(mContext).isFloatStat()){
                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(this)) {
                            Intent intentSet = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intentSet, MY_PERMISSIONS_SYSTEM_ALERT_WINDOW);
                        }
                    }*/
                    NotificationBuilder.createBubble(this);
                }
                break;

            case SharedPreferencesUtil.METHOD_BATTERY:
                if (BluetoothService.isExist) {
                    BluetoothService.mService.mOWDevice.forceBatteryRemaining();
                }
                break;

        }

    }
    void updateTheme(){
        int val = SharedPreferencesUtil.getPrefs(mContext).getAppTheme();
        int action;
        if (val == 1){
            action = AppCompatDelegate.MODE_NIGHT_NO;
        }else if (val == 2){
            action = AppCompatDelegate.MODE_NIGHT_YES;
        }else{
            if (android.os.Build.VERSION.SDK_INT >= 29/*Build.VERSION_CODES.Q*/){
                action = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            }else{
                action = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
            }
        }
        AppCompatDelegate.setDefaultNightMode(action);
    }

    public void checkUpdates(boolean override){
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        int index = BuildConfig.VERSION_NAME.indexOf('-');
        final String version;
        version = BuildConfig.VERSION_NAME.substring(0,index);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!SharedPreferencesUtil.getPrefs(mContext).getUpdateOverData()){
            if (cm != null && cm.isActiveNetworkMetered()){
                return;
            }
        }
        if (cm != null && (day != SharedPreferencesUtil.getPrefs(mContext).getLastUpdateDay() || !version.equals(SharedPreferencesUtil.getPrefs(mContext).getLastUpdateVersion()))){
            check(day,version);
        }else if (override){
            check(day,version);
        }
    }

    void check(final int day, final String version){
        HTTPUtil.checkVersion(getApplicationContext(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    SharedPreferencesUtil.getPrefs(mContext).setLastUpdateDay(day);
                    SharedPreferencesUtil.getPrefs(mContext).setLastUpdateVersion(version);

                    JSONObject obj = new JSONObject(response);
                    if (!obj.getString("tag_name").equals(version)){
                        SharedPreferencesUtil.getPrefs(mContext).setUpdateAvailable(true);
                        JSONArray arr = obj.getJSONArray("assets");
                        SharedPreferencesUtil.getPrefs(mContext).setLastUpdateURL(arr.getJSONObject(0).getString("browser_download_url"));
                        navigation.showBadge(R.id.settings_button);
                    }else{
                        SharedPreferencesUtil.getPrefs(mContext).setUpdateAvailable(false);
                        SharedPreferencesUtil.getPrefs(mContext).setLastUpdateURL("");
                        navigation.removeBadge(R.id.settings_button);
                    }
                }catch (Exception e){
                    if (e.getMessage() != null) {
                        Log.w("Github dun fucked up", e.getMessage());
                    }else{
                        Log.w("Github dun fucked up", e.toString());
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (BuildConfig.FLAVOR == "pro") {
            boolean bool = WidgetUpdater.backProcess(frag,mapFrag,this);
            if (!bool){
                super.onBackPressed();
            }
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mContext = this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PERMISSIONS_SYSTEM_ALERT_WINDOW) {

            //Check if the permission is granted or not.
            //if (!(Build.VERSION.SDK_INT >= 23 && Settings.canDrawOverlays(this))) {
            //    SharedPreferencesUtil.getPrefs(mContext).setFloatStat(false);
            //}
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


}
