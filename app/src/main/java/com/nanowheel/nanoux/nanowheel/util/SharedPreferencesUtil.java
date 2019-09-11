package com.nanowheel.nanoux.nanowheel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

@SuppressWarnings("WeakerAccess")
public class SharedPreferencesUtil{

    public static final String METRIC_UNITS = "metricUnits";
    public static final String LOG_TRIPS = "mapTraceMode";
    public static final String FLOAT_STAT = "floatingStatus";
    public static final String APP_THEME = "appTheme";
    //private static final String DEBUG_WINDOW = "debugWindow";
    private static final String OW_MAC_ADDRESS = "ow_mac_address";
    private static final String OW_MAC_NAME = "ow_mac_name";
    public static final String STATUS_MODE = "status_mode";
    private static final String FLOAT_POS_X = "floatPosX";
    private static final String FLOAT_POS_Y = "floatPosY";
    private static final String FLOAT_EXTEND = "floatExtended";
    private static final String CHARGEDISTVAL = "charge_distance_value";
    private static final String CHARGEDISTBAT = "change_distance_battery_level";
    public static final String RECORD_SPEED = "alltime_record_speed";
    public static final String RECORD_DIST = "alltime_record_distance";
    public static final String TOTAL_DIST = "alltime_distance";
    private static final String DIALOG_ACCEPTED = "data_dialog";
    private static final String PINT_DIALOG_ACCEPTED = "pint_data_dialog";
    private static final String CHARGING_NOT = "warningCharge";
    private static final String CHARGING_NOT_VALUE = "warningChargeValue";
    private static final String TURN_NOT = "warningTurn";
    private static final String TURN_NOT_VALUE = "warningTurnThresh";
    private static final String BATT1_NOT = "warningBatt1";
    private static final String BATT1_NOT_VALUE = "warningBattThresh1";
    private static final String BATT2_NOT = "warningBatt2";
    private static final String BATT2_NOT_VALUE = "warningBattThresh2";
    private static final String SPEED_NOT = "warningSpeed";
    private static final String SPEED_NOT_VALUE = "warningSpeedThresh";
    private static final String SPEED2_NOT = "warningSpeed2";
    private static final String SPEED2_NOT_VALUE = "warningSpeedThresh2";
    private static final String REGEN_NOT = "warningRegen";
    private static final String REGEN_NOT_VALUE = "warningRegenThresh";
    private static final String SENSOR_NOT = "notificationSensor";
    private static final String SPEED_REC_NOT = "notificationSpeed";
    private static final String METHOD_BATTERY = "batteryMethod";
    private static final String DEFAULT_BATTERY = "batteryDefault";
    private static final String VOLTS_BATTERY = "batteryVoltage";
    private static final String CELLS_BATTERY = "batteryCells";
    private static final String TWO_X_BATTERY = "batteryTwoX";
    //private static final String CHALLENGE_EPOCH = "callengeEpoch";
    private static final String LAST_UPDATE_DAY = "lastUpdateDay";
    private static final String UPDATE_DATA = "updateData";
    private static final String UPDATE_VERSION = "updateVersion";
    private static final String UPDATE_AVAILABLE = "updateAvailable";
    private static final String UPDATE_URL = "updateURL";

    private SharedPreferences androidSharedPreferences;


    public SharedPreferencesUtil(Context context) {
        androidSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferencesUtil instance = null;
    public static SharedPreferencesUtil getPrefs(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesUtil(context);
        }
        return instance;
    }

    public void putInt(int value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void putBool(boolean value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putFloat(float value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public void putLong(long value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public void putString(String value, String key) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setStatusMode(int status){
        putInt(status,STATUS_MODE);
    }
    public int getStatusMode(){
        return androidSharedPreferences.getInt(STATUS_MODE,0);
    }

    public int getAppTheme() {
        return Integer.parseInt(androidSharedPreferences.getString(APP_THEME, "3"));
    }

    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        androidSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    //public Boolean isDebugging() {
    //    return androidSharedPreferences.getBoolean(DEBUG_WINDOW, false);
    //}

    public void removeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        androidSharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }


    public void saveMacAddress(String macAdress, String macAddressName) {
        SharedPreferences.Editor editor = androidSharedPreferences.edit();
        editor.putString(OW_MAC_ADDRESS, macAdress);
        editor.putString(OW_MAC_NAME,macAddressName);
        editor.commit();
    }

    public String getMacAddress(){
        return androidSharedPreferences.getString(OW_MAC_ADDRESS,"");
    }

    public boolean isMetric() {
        return androidSharedPreferences.getBoolean(METRIC_UNITS, false);
    }

    public boolean isMap() {
        return androidSharedPreferences.getBoolean(LOG_TRIPS, false);
    }

    public boolean isFloatStat() {
        return androidSharedPreferences.getBoolean(FLOAT_STAT, false);
    }

    public boolean isFloatExtend() {
        return androidSharedPreferences.getBoolean(FLOAT_EXTEND, true);
    }

    public float[] getFloatPos() {
        return new float[] {androidSharedPreferences.getFloat(FLOAT_POS_X, 0),androidSharedPreferences.getFloat(FLOAT_POS_Y, 100)};
    }

    public void setFloatPos(float x, float y){
        putFloat(x,FLOAT_POS_X);
        putFloat(y,FLOAT_POS_Y);
    }

    public void setMap(boolean bool) { putBool(bool,LOG_TRIPS); }

    public void setFloatExtend(boolean bool) { putBool(bool,FLOAT_EXTEND); }

    public void setFloatStat(boolean bool) { putBool(bool,FLOAT_STAT); }


    public boolean getChargeWarning(){
        return androidSharedPreferences.getBoolean(CHARGING_NOT,true);
    }
    public int getChargeWarningValue(){
        String temp = androidSharedPreferences.getString(CHARGING_NOT_VALUE,"100%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getTurnWarning(){
        return androidSharedPreferences.getBoolean(TURN_NOT,true);
    }
    public int getTurnWarningValue(){
        String temp = androidSharedPreferences.getString(TURN_NOT_VALUE,"50%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getBatt1Warning(){
        return androidSharedPreferences.getBoolean(BATT1_NOT,true);
    }
    public int getBatt1WarningValue(){
        String temp = androidSharedPreferences.getString(BATT1_NOT_VALUE,"10%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getBatt2Warning(){
        return androidSharedPreferences.getBoolean(BATT2_NOT,false);
    }
    public int getBatt2WarningValue(){
        String temp = androidSharedPreferences.getString(BATT2_NOT_VALUE,"5%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getSpeedWarning2(){
        return androidSharedPreferences.getBoolean(SPEED2_NOT,false);
    }
    public int getSpeedWarningValue2(){
        String temp = androidSharedPreferences.getString(SPEED2_NOT_VALUE,"488");
        return Util.parseI(temp);
    }
    public boolean getSpeedWarning(){
        return androidSharedPreferences.getBoolean(SPEED_NOT,true);
    }
    public int getSpeedWarningValue(){
        String temp = androidSharedPreferences.getString(SPEED_NOT_VALUE,"413");
        return Util.parseI(temp);
    }
    public boolean getRegenWarning(){
        return androidSharedPreferences.getBoolean(REGEN_NOT,true);
    }
    public int getRegenWarningValue(){
        String temp = androidSharedPreferences.getString(REGEN_NOT_VALUE,"99%");
        temp = temp.substring(0,temp.length()-1);
        return Util.parseI(temp);
    }
    public boolean getSensorWarning(){
        return androidSharedPreferences.getBoolean(SENSOR_NOT,true);
    }
    public boolean getSpeedRecordNot(){
        return androidSharedPreferences.getBoolean(SPEED_REC_NOT,true);
    }

    public int getSpeedRecord(){
        return androidSharedPreferences.getInt(RECORD_SPEED,0);
    }
    public void setSpeedRecord(int speed){
        putInt(speed,RECORD_SPEED);
    }
    public int getDistanceRecord(){
        return androidSharedPreferences.getInt(RECORD_DIST,0);
    }
    public void setDistanceRecord(int dist){
        putInt(dist,RECORD_DIST);
    }
    public int getDistanceTotal(){
        return androidSharedPreferences.getInt(TOTAL_DIST,0);
    }
    public void setDistanceTotal(int dist){
        putInt(dist,TOTAL_DIST);
    }

    public float getChargeDistRev(){
        return androidSharedPreferences.getFloat(CHARGEDISTVAL,0);
    }
    public void setChargeDistRev(float a){
        putFloat(a,CHARGEDISTVAL);
    }
    public float getChargeDistBat(){
        return androidSharedPreferences.getFloat(CHARGEDISTBAT,0);
    }
    public void setChargeDistBat(float a){
        putFloat(a,CHARGEDISTBAT);
    }
    public boolean getDialogAccepted(){
        return androidSharedPreferences.getBoolean(DIALOG_ACCEPTED,false);
    }
    public void setDialogAccepted(boolean a){
        putBool(a,DIALOG_ACCEPTED);
    }
    public boolean getPintDialogAccepted(){
        return androidSharedPreferences.getBoolean(PINT_DIALOG_ACCEPTED,false);
    }
    public void setPintDialogAccepted(boolean a){
        putBool(a,PINT_DIALOG_ACCEPTED);
    }

    public boolean getIsBatteryDefault(){
        return androidSharedPreferences.getString(METHOD_BATTERY, "batteryDefault").equals(DEFAULT_BATTERY);
    }
    public boolean getIsBatteryVoltage(){
        return androidSharedPreferences.getString(METHOD_BATTERY, "batteryDefault").equals(VOLTS_BATTERY);
    }
    public boolean getIsBatteryCells(){
        return androidSharedPreferences.getString(METHOD_BATTERY, "batteryDefault").equals(CELLS_BATTERY);
    }
    public boolean getIsBatteryTwoX(){
        return androidSharedPreferences.getString(METHOD_BATTERY, "batteryDefault").equals(TWO_X_BATTERY);
    }

    public int getLastUpdateDay(){ return androidSharedPreferences.getInt(LAST_UPDATE_DAY,-1);}
    public void setLastUpdateDay(int day){
        putInt(day,LAST_UPDATE_DAY);
    }

    public String getLastUpdateVersion(){ return androidSharedPreferences.getString(UPDATE_VERSION,"1.0");}
    public void setLastUpdateVersion(String version){
        putString(version,UPDATE_VERSION);
    }

    public boolean isUpdateAvailable(){ return androidSharedPreferences.getBoolean(UPDATE_AVAILABLE,false);}
    public void setUpdateAvailable(boolean available){
        putBool(available,UPDATE_AVAILABLE);
    }

    public boolean getUpdateOverData(){
        return androidSharedPreferences.getBoolean(UPDATE_DATA,true);
    }

    public String getLastUpdateURL(){ return androidSharedPreferences.getString(UPDATE_URL,"");}
    public void setLastUpdateURL(String url){
        putString(url,UPDATE_URL);
    }

}