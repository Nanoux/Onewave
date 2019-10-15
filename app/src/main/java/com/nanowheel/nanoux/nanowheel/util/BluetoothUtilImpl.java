package com.nanowheel.nanoux.nanowheel.util;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import com.nanowheel.nanoux.nanowheel.Widgets.WidgetUpdater;
import com.nanowheel.nanoux.nanowheel.model.OWDevice;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.security.MessageDigest;
import java.io.ByteArrayInputStream;
import java.security.DigestInputStream;

import androidx.databinding.ObservableField;


@SuppressWarnings("ConstantConditions")
public class BluetoothUtilImpl implements BluetoothUtil{

    //private static final String TAG = BluetoothUtilImpl.class.getSimpleName();

    //private static final int REQUEST_ENABLE_BT = 1;

    private Queue<BluetoothGattCharacteristic> characteristicReadQueue = new LinkedList<>();
    private Queue<BluetoothGattDescriptor> descriptorWriteQueue = new LinkedList<>();
    private Queue<Integer> customQueue = new LinkedList<>();
    private android.bluetooth.BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothGatt mGatt;
    private BluetoothGattService owGatService;

    private Map<String, String> mScanResults = new HashMap<>();

    private Application application;
    private OWDevice mOWDevice;

    private ScanSettings settings;
    private boolean mScanning;
    private boolean isObfucked = false;
    private boolean sendKey = true;
    public static ObservableField<String> isOWFound = new ObservableField<>();

    private static  ByteArrayOutputStream inkey = new ByteArrayOutputStream();


    @Override
    public void init(Application app, OWDevice mOWDevice) {
        this.application = app;
        this.mOWDevice = mOWDevice;

        final BluetoothManager manager = (BluetoothManager) application.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = manager.getAdapter();
        //if (mBluetoothAdapter == null){
            //bluetooth not supported
        //}
        mOWDevice.bluetoothLe.set("On");
    }

    /*public void readChar(String id){
        Log.w("TESTING","CALLED!");
        BluetoothGattCharacteristic c = owGatService.getCharacteristic(UUID.fromString(id));
        if (c != null) {
            Log.w("TESTING","FOUND!");
            if (isCharacteristicReadable(c)) {
                Log.w("TESTING","READABLE!");
                mGatt.readCharacteristic(c);
            }
        }
    }*/

    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                NotificationBuilder.createScanningNot(application,"Onewheel Found, Connecting...",true);
                isOWFound.set("true");
                gatt.discoverServices();
                Battery.initStateTwoX(SharedPreferencesUtil.getPrefs(application));
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                if (BluetoothService.isExist) {
                    SharedPreferencesUtil.getPrefs(application).setStatusMode(1);
                }else{
                    SharedPreferencesUtil.getPrefs(application).setStatusMode(0);
                }
                Log.w("TESTING","DISCONNECTED " + status);
                isOWFound.set("false");
                if (gatt.getDevice().getAddress().equals(mOWDevice.deviceMacAddress.get())) {
                    onOWStateChangedToDisconnected(application);
                    //UndiscoverServices();
                }
            }
        }



        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status){
            owGatService = gatt.getService(UUID.fromString(OWDevice.OnewheelServiceUUID));

            if (owGatService == null) {
                Log.e("BLUEOTTH ERROR","GAT SERVICE IS NULL!");
                //NotificationBuilder.generalNotification(context,"GAT SERVICE IS NULL");
                return;
            }

            mGatt = gatt;
            mOWDevice.isConnected.set(true);
            String deviceMacAddress = mGatt.getDevice().toString();
            String deviceMacName = mGatt.getDevice().getName();
            mOWDevice.deviceMacAddress.set(deviceMacAddress);
            mOWDevice.deviceMacName.set(deviceMacName);
            SharedPreferencesUtil.getPrefs(application).saveMacAddress(
                    mOWDevice.deviceMacAddress.get(),
                    mOWDevice.deviceMacName.get()
            );

            scanLeDevice(false); // We can stop scanning...

            //mGatt.readCharacteristic(owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicFirmwareRevision)));
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {//make delay to avoid weird disconnection
                @Override
                public void run() {
                    mGatt.readCharacteristic(owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicFirmwareRevision)));
                }
            },500);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic c, int status) {
            String characteristic_uuid = c.getUuid().toString();
            if(characteristicReadQueue.size() > 0) {
                characteristicReadQueue.remove();
            }

            if (characteristic_uuid.equals(OWDevice.OnewheelCharacteristicFirmwareRevision)) {

                int version = Util.unsignedShort(c.getValue());
                if(version >= 4034) {
                    isObfucked = true;
                    //owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicFirmwareRevision)).setValue(version+"");

                    BluetoothGattCharacteristic gC = owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicUartSerialRead));
                    gatt.setCharacteristicNotification(gC, true);
                    BluetoothGattDescriptor descriptor = gC.getDescriptor(UUID.fromString(OWDevice.OnewheelConfigUUID));
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    gatt.writeDescriptor(descriptor);
                } else {
                    isObfucked = false;
                    whenActuallyConnected();
                }

            }


            mOWDevice.processUUID(c);

            mOWDevice.setBatteryRemaining();

            // Callback to make sure the queue is drained
            if (characteristicReadQueue.size() > 0) {
                gatt.readCharacteristic(characteristicReadQueue.element());
            }


        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic c) {
            //Timber.d( "BluetoothGattCallback.onCharacteristicChanged: CharacteristicUuid=" + c.getUuid().toString());

            // https://github.com/ponewheel/android-ponewheel/issues/86
            //if (c.getUuid().toString().equals(OWDevice.OnewheelCharacteristicUartSerialRead)){
                //testy do
            //}
            if (c.getUuid().toString().equals(OWDevice.OnewheelCharacteristicUartSerialRead) && isObfucked) {

                try {
                    inkey.write(c.getValue());

                    if (inkey.toByteArray().length >= 20 && sendKey) {
                        Log.d("GEMINI","GEMINI Step #2: convert inkey=" + Arrays.toString(inkey.toByteArray()));
                        // Do the magic, write the characteristic...
                        ByteArrayOutputStream outkey = new ByteArrayOutputStream();
                        outkey.write(Util.StringToByteArrayFastest("43:52:58"));

                        // Take almost all of the bytes from the input array. This is almost the same as the last part as
                        // we are ignoring the first 3 and the last bytes.
                        byte[] arrayToMD5_part1 = Arrays.copyOfRange(inkey.toByteArray(), 3, 19);
                        byte[] arrayToMD5_part2 = Util.StringToByteArrayFastest("D9255F0F23354E19BA739CCDC4A91765");

                        // New byte array we are going to MD5 hash. Part of the input string, part of this static string.
                        ByteBuffer arrayToMD5 = ByteBuffer.allocate(arrayToMD5_part1.length + arrayToMD5_part2.length);
                        arrayToMD5.put(arrayToMD5_part1);
                        arrayToMD5.put(arrayToMD5_part2);

                        // Start prepping the MD5 hash
                        MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
                        DigestInputStream digestInputStream = new DigestInputStream(new ByteArrayInputStream(arrayToMD5.array()), localMessageDigest);

                        // This is actually the byte that represents a space character. ¯\_(ツ)_/¯
                        byte[] arrayOfByte = new byte[] { 101 };
                        while (digestInputStream.read(arrayOfByte) != -1) { }
                        digestInputStream.close();
                        byte[] md5Hash = localMessageDigest.digest();

                        // Add it to the 3 bytes we already have.
                        outkey.write(md5Hash);

                        // Validate the check byte.
                        byte checkByte = 0;
                        int j = outkey.toByteArray().length;
                        int i = 0;
                        while (i < j)
                        {
                            checkByte = ((byte)(outkey.toByteArray()[i] ^ checkByte));
                            i += 1;
                        }
                        outkey.write(checkByte);

                        // Finally, write out to the OW serial UART characteristic
                        Log.d("GEMINI","GEMINI Step #3: write outkey=" + Arrays.toString(outkey.toByteArray()));
                        BluetoothGattCharacteristic lc = owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicUartSerialWrite));
                        lc.setValue(outkey.toByteArray());


                        boolean worked = gatt.writeCharacteristic(lc);
                        if(!worked){
                            sendKey = true;
                        }else{
                            sendKey = false;
                            gatt.setCharacteristicNotification(c,false);
                        }
                        outkey.reset();

                    }
                } catch (Exception e) {
                    Log.e("GEMINI","Exception with GEMINI obfuckstation:" + e.getMessage());
                }

            }

            mOWDevice.processUUID(c);

            mOWDevice.setBatteryRemaining();

            if (mOWDevice.characteristics.get(c.getUuid().toString()).state == 5){
                Log.w("TESTING","HUZZAH! CHANGED");
            }
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            //Timber.i( "onCharacteristicWrite: " + status);
            //mOWDevice.processUUID(characteristic);
            if (characteristic.getUuid().toString().equals(OWDevice.OnewheelCharacteristicUartSerialWrite)){
                //WERE CONNECTED MOTHER FUCKERS!
                whenActuallyConnected();
            }
            if (characteristic.getUuid().toString().equals(OWDevice.OnewheelCharacteristicUNKNOWN2)){
                if (customQueue.size() > 0){
                    customQueue.remove();
                    if (customQueue.size() > 0){
                        writeCustomRidemode(customQueue.element());
                    }else{
                        invalidateCustomRidemode();
                    }
                }
            }
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {

            if (isObfucked && descriptor.getCharacteristic().getUuid().toString().equals(OWDevice.OnewheelCharacteristicUartSerialRead)){
                Log.d("GEMINI", "GEMINI step #1: trigger sending the 20 byte input key over multiple serial ble notifications");
                //owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicFirmwareRevision)).setValue(mOWDevice.characteristics.get(OWDevice.OnewheelCharacteristicFirmwareRevision).value.get());
                gatt.writeCharacteristic(owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicFirmwareRevision)));
            }

            OWDevice.DeviceCharacteristic dc = mOWDevice.characteristics.get(descriptor.getCharacteristic().getUuid().toString());
            if (dc != null) {
                if (dc.state == 0 || dc.state == 1) {
                    gatt.setCharacteristicNotification(owGatService.getCharacteristic(UUID.fromString(dc.uuid.get())), true);
                }
            }
            if (descriptorWriteQueue.size() > 0){
                descriptorWriteQueue.remove();
                if (descriptorWriteQueue.size() > 0){
                    gatt.writeDescriptor(descriptorWriteQueue.element());
                }else{
                    if(characteristicReadQueue.size() > 0){
                        gatt.readCharacteristic(characteristicReadQueue.element());
                    }
                }
            }
        }


    };

    private void whenActuallyConnected(){
        for(OWDevice.DeviceCharacteristic dc : mOWDevice.getNotifyCharacteristics()){
            if (dc.state == 0){
                BluetoothGattCharacteristic gC = owGatService.getCharacteristic(UUID.fromString(dc.uuid.get()));
                //gatt.setCharacteristicNotification(gC,true);
                BluetoothGattDescriptor descriptor = gC.getDescriptor(UUID.fromString(OWDevice.OnewheelConfigUUID));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                descriptorWriteQueue.add(descriptor);
                if (descriptorWriteQueue.size() == 1) {
                    mGatt.writeDescriptor(descriptor);
                }
                characteristicReadQueue.add(gC);
            }
        }
        for(OWDevice.DeviceCharacteristic dc : mOWDevice.getReadCharacteristics()){
            if (dc.state == 3){
                BluetoothGattCharacteristic gC = owGatService.getCharacteristic(UUID.fromString(dc.uuid.get()));
                if (gC != null) {
                    characteristicReadQueue.add(gC);
                }
            }
        }
        mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicStance).value.set(null);
        mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicCarvability).value.set(null);
        mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicAggressiveness).value.set(null);

        SharedPreferencesUtil.getPrefs(application).setStatusMode(2);
        WidgetUpdater.setupLog(application);
    }


    private void scanLeDevice(final boolean enable) {
        //Timber.d("scanLeDevice enable = " + enable);
        if (enable) {
            mScanning = true;
            List<ScanFilter> filters_v2 = new ArrayList<>();
            ScanFilter scanFilter = new ScanFilter.Builder()
                    .setServiceUuid(ParcelUuid.fromString(OWDevice.OnewheelServiceUUID))
                    .build();
            filters_v2.add(scanFilter);
            //c03f7c8d-5e96-4a75-b4b6-333d36230365
            if (mBluetoothLeScanner != null) {
                mBluetoothLeScanner.startScan(filters_v2, settings, mScanCallback);
            }else{
                BluetoothService.killService(application,false);
            }
        } else {
            mScanning = false;
            if (mBluetoothLeScanner == null || !mBluetoothAdapter.isEnabled())
                return;
            mBluetoothLeScanner.stopScan(mScanCallback);
            // added 10/23 to try cleanup
            mBluetoothLeScanner.flushPendingScanResults(mScanCallback);
        }
        //mainActivity.invalidateOptionsMenu();
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            String deviceName = result.getDevice().getName();
            String deviceAddress = result.getDevice().getAddress();

            //Timber.i( "ScanCallback.onScanResult: " + mScanResults.entrySet());
            if (!mScanResults.containsKey(deviceAddress)) {
                mScanResults.put(deviceAddress, deviceName);


                if (deviceName != null && (deviceName.startsWith("ow") || deviceName.startsWith("Onewheel"))) {
                    //Timber.i("Looks like we found our OW device (" + deviceName + ") discovering services!");
                    connectToDevice(result.getDevice());
                }

            } else {
                //Timber.d("onScanResult: mScanResults already had our key, still connecting to OW services or something is up with the BT stack.");
                //  Timber.d("onScanResult: mScanResults already had our key," + "deviceName=" + deviceName + ",deviceAddress=" + deviceAddress);
                // still connect
                connectToDevice(result.getDevice());
            }


        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            //for (ScanResult sr : results) {
            //Timber.i("ScanCallback.onBatchScanResults.each:" + sr.toString());
            //}
        }

        @Override
        public void onScanFailed(int errorCode) {
            //Timber.e( "ScanCallback.onScanFailed:" + errorCode);
            Log.w("SCAN","IT FAILED");
        }
    };


    private void connectToDevice(BluetoothDevice device) {
        //Timber.d( "connectToDevice:" + device.getName());
        device.connectGatt(application, false, mGattCallback);

        //device.connectGatt(context,true,)
    }


    //public void connectToGatt(BluetoothGatt gatt) {
    //    gatt.connect();
    //}

    private void onOWStateChangedToDisconnected(Context context) {
        Log.d("BLUETOOH","DISCONNECTED");


        BluetoothService.killService(context,true);
    }

    /*private void UndiscoverServices(){
        mGatt = null;
        owGatService = null;
        mOWDevice.isConnected.set(false);
    }

    public static boolean isCharacteristicWriteable(BluetoothGattCharacteristic c) {
        return (c.getProperties() & (BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0;
    }
    public static boolean isCharacteristicReadable(BluetoothGattCharacteristic c) {
        return ((c.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ) != 0);
    }
    public static boolean isCharacteristicNotifiable(BluetoothGattCharacteristic c) {
        return (c.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0;
    }*/


    // Helpers
    /*private static int unsignedByte(byte var0) {
        return var0 & 255;
    }

    public static int unsignedShort(byte[] var0) {
        // Short.valueOf(ByteBuffer.wrap(v_bytes).getShort()) also works
        int var1;
        if(var0.length < 2) {
            var1 = -1;
        } else {
            var1 = (unsignedByte(var0[0]) << 8) + unsignedByte(var0[1]);
        }

        return var1;
    }*/

    public boolean isObfucked(){
        return isObfucked;
    }


    /*@Override
    public boolean isConnected() {
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled();
    }*/


    /*@Override
    public void reconnect(MainActivity activity) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }*/

    @Override
    public void stopScanning() {
        scanLeDevice(false);
        if (mGatt != null) {
            mGatt.disconnect();
            mGatt.close();
            mGatt = null;
        }
        mOWDevice.isConnected.set(false);
        this.mScanResults.clear();
        descriptorWriteQueue.clear();
        // Added stuff 10/23 to clean fix
        owGatService = null;
        // Added more 3/12/2018
        this.characteristicReadQueue.clear();
        inkey.reset();
        isOWFound.set("false");
        sendKey = true;
    }

    /*@Override
    public boolean isScanning() {
        return mScanning;
    }*/


    private static int retrys = 0;
    @Override
    public void startScanning() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()){
            Toast.makeText(application,"Enable Bluetooth to Connect",Toast.LENGTH_LONG).show();

            retrys = 0;
            stopScanning();
            disconnect();
            BluetoothService.killService(application,false);
            return;
        }

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        scanLeDevice(true);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mScanning && (isOWFound.get()== null || isOWFound.get().equals("false"))){
                    if (retrys < 2){
                        retrys++;
                        stopScanning();
                        disconnect();

                        startScanning();

                        Log.w("ONEWAVE","REBOOTING SCAN");
                    }else{
                        retrys = 0;
                        stopScanning();
                        disconnect();
                        BluetoothService.killService(application,false);
                    }
                }else{
                    retrys = 0;
                }
            }
        }, 30000);
    }


    @Override
    public void disconnect() {
        scanLeDevice(false);
        if (mGatt != null) {
            mGatt.disconnect();
            mGatt.close();
            mGatt = null;
        }
        this.mScanResults.clear();
        descriptorWriteQueue.clear();
        // Added stuff 10/23 to clean fix
        owGatService = null;
        inkey.reset();
        isOWFound.set("false");
        sendKey = true;
    }

    @Override
    public BluetoothGattCharacteristic getCharacteristic(String uuidLookup) {
        return owGatService.getCharacteristic(UUID.fromString(uuidLookup));
    }

    @Override
    public void writeCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        mGatt.writeCharacteristic(bluetoothGattCharacteristic);
    }

    public void refreshSomeDiagnotics(){
        if (mOWDevice != null && owGatService != null && mGatt != null) {
            for (OWDevice.DeviceCharacteristic dc : mOWDevice.getReadCharacteristics()) {
                if (dc.state == 4) {
                    BluetoothGattCharacteristic gC = owGatService.getCharacteristic(UUID.fromString(dc.uuid.get()));
                    if (gC != null) {
                        characteristicReadQueue.add(gC);
                        if (characteristicReadQueue.size() == 1) {
                            mGatt.readCharacteristic(characteristicReadQueue.element());
                        }
                    }
                }
            }
        }
    }

    public void refreshPeriodicDiagnostics(){
        if (mOWDevice != null && owGatService != null && mGatt != null) {
            for (OWDevice.DeviceCharacteristic dc : mOWDevice.getReadCharacteristics()) {
                if (dc.state == 5) {
                    BluetoothGattCharacteristic gC = owGatService.getCharacteristic(UUID.fromString(dc.uuid.get()));
                    if (gC != null) {
                        characteristicReadQueue.add(gC);
                        if (characteristicReadQueue.size() == 1) {
                            mGatt.readCharacteristic(characteristicReadQueue.element());
                        }
                    }
                }
            }
        }
    }

    public void killDiagnotics(){
        if (mOWDevice != null && owGatService != null && mGatt != null) {
            for (OWDevice.DeviceCharacteristic dc : mOWDevice.getNotifyCharacteristics()) {
                if (dc.state == 1) {
                    BluetoothGattCharacteristic gC = owGatService.getCharacteristic(UUID.fromString(dc.uuid.get()));
                    mGatt.setCharacteristicNotification(gC, false);
                }
            }
        }
    }

    /*public void killCustomNotifications(){
        if (mGatt != null){
            mGatt.setCharacteristicNotification(owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicUNKNOWN2)),false);
        }
    }*/

    private void invalidateCustomRidemode(){
        if (mGatt != null) {
            mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicStance).value.set(null);
            mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicCarvability).value.set(null);
            mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicAggressiveness).value.set(null);

            BluetoothGattCharacteristic gC = owGatService.getCharacteristic(UUID.fromString(OWDevice.OnewheelCharacteristicUNKNOWN2));
            mGatt.setCharacteristicNotification(gC, true);
            BluetoothGattDescriptor descriptor = gC.getDescriptor(UUID.fromString(OWDevice.OnewheelConfigUUID));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mGatt.writeDescriptor(descriptor);
        }
    }

    private void writeCustomRidemode(int which){
        BluetoothGattCharacteristic lc;
        lc = getCharacteristic(OWDevice.OnewheelCharacteristicUNKNOWN2);
        int val = 0;
        if (which == 0){
            val = Util.parseI(mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicStance).value.get());
        }else if (which == 1){
            val = Util.parseI(mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicCarvability).value.get());
        }else if (which == 2){
            val = Util.parseI(mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicAggressiveness).value.get());
        }
        lc.setValue(new byte[] { (byte) which, (byte) val });
        lc.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        writeCharacteristic(lc);
    }

    public void writeCustomRidemode(){
        customQueue.clear();
        customQueue.add(0);
        customQueue.add(1);
        customQueue.add(2);
        writeCustomRidemode(0);
    }
}