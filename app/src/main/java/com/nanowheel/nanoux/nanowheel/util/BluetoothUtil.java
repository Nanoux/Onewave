package com.nanowheel.nanoux.nanowheel.util;

import android.app.Application;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;

import com.nanowheel.nanoux.nanowheel.model.OWDevice;

public interface BluetoothUtil {
    void init(Application app, OWDevice mOWDevice);
    //void reconnect(MainActivity activity);
    void stopScanning();
    void disconnect();
    //boolean isConnected();
    //boolean isScanning();
    boolean isObfucked();
    void startScanning();
    BluetoothGattCharacteristic getCharacteristic(String onewheelCharacteristicLightingMode);
    void writeCharacteristic(BluetoothGattCharacteristic ch);
    //void killCustomNotifications();
    void writeCustomRidemode();
    void refreshSomeDiagnotics();
    void refreshPeriodicDiagnostics();
    void killDiagnotics();
}
