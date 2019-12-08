package com.nanowheel.nanoux.nanowheel;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Created by kwatts on 4/22/16.
 */
public interface DeviceInterface {
    void processUUID(BluetoothGattCharacteristic c);
    String getName();
}
