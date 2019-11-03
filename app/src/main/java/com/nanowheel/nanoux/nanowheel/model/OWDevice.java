package com.nanowheel.nanoux.nanowheel.model;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableDouble;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import android.location.Address;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nanowheel.nanoux.nanowheel.DeviceInterface;
import com.nanowheel.nanoux.nanowheel.util.Battery;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.BluetoothUtil;
import com.nanowheel.nanoux.nanowheel.util.NotificationBuilder;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;
import com.nanowheel.nanoux.nanowheel.R;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


// https://github.com/wasabeef/awesome-android-ui/blob/master/pages/Progress.md

// http://blog.davidvassallo.me/2015/09/02/ble-health-devices-first-steps-with-android/
// https://github.com/alt236/Bluetooth-LE-Library---Android
// https://github.com/Fakher-Hakim/android-BluetoothLeGatt
// https://developer.android.com/tools/data-binding/guide.html
// https://github.com/iDevicesInc/SweetBlue
// https://www.evilsocket.net/2015/01/29/nike-fuelband-se-ble-protocol-reversed/
// mapping to google maps with geojson? https://developers.google.com/maps/documentation/android-api/utility/geojson#style-feature
// OW stats: 58VDC charger, 3.5Amp with 130Wh (LiFEPO4 Nano-phosphate Litium) and 500W motor
// Other stats: Likely 7500 mah = 7500/58 is 130Wh
// Calculations: 130wh/48v = 2.7AH  - a 2.7AH battery would take 54 minutes to charge.... (2.7/3.5 = 0.9 x 60 minutes)
// AMP hours = a battery with 1 amp-hour supplies 1 amp to load for 1 hour. 2 amps for 1/2 hour, etc
// I (current measured in Amps) = V (Volts) / R (resistance,ohms)
// The consumed power of motor is P (input power, measured in Watts) = I (Amps) * V (applied Voltage)
// Should show stats with,
// - Speed
// - Consumed power of motor (W)
// - Consumed power total
// 12.8V 6.9Ah 88.32Wh



/**
 * Created by kwatts on 3/23/16.
 * Modifed a metric fuckton by Nanoux ever since
 */

@SuppressWarnings("ConstantConditions")
public class OWDevice extends BaseObservable implements DeviceInterface {
    private static final String NAME = "ONEWHEEL";

    //public static final SimpleDateFormat OLD_SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);
    //public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);

    private static final String KEY_MOTOR_TEMP = "motor_temp";
    private static final String KEY_CONTROLLER_TEMP = "controller_temp";
    private static final String KEY_SPEED_MAX = "speed_max";
    private static final String KEY_SPEED = "speed";

    private static final String KEY_HARDWARE_REVISION = "hardware_revision";
    private static final String KEY_FIRMWARE_REVISION = "firmware_revision";
    private static final String KEY_LIFETIME_ODOMETER = "lifetime_odometer";
    private static final String KEY_LIGHTING_MODE = "lighting_mode";
    private static final String KEY_BATTERY_INITIAL = "battery_initial";
    //public static final String KEY_LAST_ERROR_CODE = "last_error_code";
    private static final String KEY_BATTERY = "battery";
    private static final String KEY_RIDER_DETECTED = "rider_detected";
    private static final String KEY_RIDER_DETECTED_PAD_1 = "rider_detected_pad1";
    private static final String KEY_RIDER_DETECTED_PAD_2 = "rider_detected_pad2";
    private static final String KEY_ODOMETER = "odometer";
    private static final String KEY_ODOMETER_CHARGE = "odometer_charge";
    private static final String KEY_ODOMETER_RANGE = "odometer_range";
    private static final String KEY_ODOMETER_TIRE_REVS = "odometer_tire_revs";
    private static final String KEY_TRIP_AMPS = "trip_amps";
    private static final String KEY_TRIP_AMPS_REGEN = "trip_amps_regen";
    private static final String KEY_SPEED_RPM = "speed_rpm";
    private static final String KEY_BATTERY_VOLTAGE = "battery_voltage";
    private static final String KEY_BATTERY_CELLS = "battery_cells";
    private static final String KEY_CURRENT_AMPS = "current_amps";
    private static final String KEY_TILT_ANGLE_PITCH = "tilt_angle_pitch";
    private static final String KEY_TILT_ANGLE_ROLL = "tilt_angle_roll";
    private static final String KEY_RIDE_MODE = "ride_mode";
    private static final String KEY_BATTERY_TEMP = "battery_temp";
    private static final String KEY_SERIAL_READ = "serial_read";
    private static final String KEY_SERIAL_NUMBER = "serial_number";
    private static final String KEY_SERIAL_NUMBER_BATT = "serial_number_battery";
    private static final String KEY_AMP_HOURS_LIFE = "amp_hours_lifetime";
    //public static final String KEY_SAFETY_HEADROOM = "safety_headroom";
    private static final String KEY_CHARGING = "charging";
    private static final String KEY_CUSTOM_NAME = "custom_name";
    private static final String KEY_STANCE = "stance";
    private static final String KEY_CARVABILITY = "carvability";
    private static final String KEY_AGGRESSIVENESS = "aggressiveness";
    //public static final String UNKNOWN1 = "unknown1";
    private static final String KEY_CUSTOM_SHAPING = "unknown2";
    //public static final String UNKNOWN3 = "unknown3";
    //public static final String UNKNOWN4 = "unknown4";

    private SparseArray<String> ERROR_CODE_MAP = new SparseArray<>();

    {
        ERROR_CODE_MAP.append(1, "ErrorBMSLowBattery");
        ERROR_CODE_MAP.append(2, "ErrorVoltageLow");
        ERROR_CODE_MAP.append(3, "ErrorVoltageHigh");
        ERROR_CODE_MAP.append(4, "ErrorFallDetected");
        ERROR_CODE_MAP.append(5, "ErrorPickupDetected");
        ERROR_CODE_MAP.append(6, "ErrorOverCurrentDetected");
        ERROR_CODE_MAP.append(7, "ErrorOverTemperature");
        ERROR_CODE_MAP.append(8, "ErrorBadGyro");
        ERROR_CODE_MAP.append(9, "ErrorBadAccelerometer");
        ERROR_CODE_MAP.append(10, "ErrorBadCurrentSensor");
        ERROR_CODE_MAP.append(11, "ErrorBadHallSensors");
        ERROR_CODE_MAP.append(12, "ErrorBadMotor");
        ERROR_CODE_MAP.append(13, "ErrorOvercurrent13");
        ERROR_CODE_MAP.append(14, "ErrorOvercurrent14");
        ERROR_CODE_MAP.append(15, "ErrorRiderDetectZone");
        ERROR_CODE_MAP.append(16, "ErrorRadioDesync");
        ERROR_CODE_MAP.append(17, "ErrorControllerTemp (I think)");
        ERROR_CODE_MAP.append(18, "ErrorMotorTemp (I think)");
        ERROR_CODE_MAP.append(19, "ErrorBatteryTemp (I think)");
        ERROR_CODE_MAP.append(20, "You dun $%*&ed up");
    }

    public final ObservableField<Boolean> isConnected = new ObservableField<>();
    //public final ObservableField<Boolean> showDebugWindow = new ObservableField<>();

    public final ObservableField<Boolean> isOneWheelPlus = new ObservableField<>();

    private final ObservableInt speedRpm = new ObservableInt();
    private final ObservableDouble maxSpeedRpm = new ObservableDouble();
    private final ObservableInt maxTiltAnglePitch = new ObservableInt();
    private final ObservableInt maxTiltAngleRoll = new ObservableInt();
    private final ObservableInt lifetimeOdometer = new ObservableInt();
    private final ObservableInt lightMode = new ObservableInt();

    private double[] ampCells = new double[16];
    private double[] batteryVoltageCells = new double[16];

    private boolean updateBatteryChanges = true;





    public static final String OnewheelServiceUUID = "e659f300-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelConfigUUID= "00002902-0000-1000-8000-00805f9b34fb";

    public static final String OnewheelCharacteristicSerialNumber = "e659F301-ea98-11e3-ac10-0800200c9a66"; //2085
    public static final String OnewheelCharacteristicRidingMode = "e659f302-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicBatteryRemaining = "e659f303-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicBatteryLow5 = "e659f304-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicBatteryLow20 = "e659f305-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicBatterySerial = "e659f306-ea98-11e3-ac10-0800200c9a66"; //22136
    public static final String OnewheelCharacteristicTiltAnglePitch = "e659f307-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicTiltAngleRoll = "e659f308-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicTiltAngleYaw = "e659f309-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicTemperature = "e659f310-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicStatusError = "e659f30f-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicBatteryCells = "e659f31b-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicBatteryTemp = "e659f315-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicBatteryVoltage = "e659f316-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicCurrentAmps = "e659f312-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicCustomName = "e659f3fd-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicFirmwareRevision = "e659f311-ea98-11e3-ac10-0800200c9a66"; //3034
    public static final String OnewheelCharacteristicHardwareRevision = "e659f318-ea98-11e3-ac10-0800200c9a66"; //2206
    public static final String OnewheelCharacteristicLastErrorCode = "e659f31c-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicLifetimeAmpHours = "e659f31a-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicLifetimeOdometer = "e659f319-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicLightingMode = "e659f30c-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicLightsBack = "e659f30e-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicLightsFront = "e659f30d-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicOdometer = "e659f30a-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicSafetyHeadroom = "e659f317-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicSpeedRpm = "e659f30b-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicTripRegenAmpHours = "e659f314-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicTripTotalAmpHours = "e659f313-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicUartSerialRead = "e659f3fe-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicUartSerialWrite = "e659f3ff-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicUNKNOWN1 = "e659f31d-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicUNKNOWN2 = "e659f31e-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicUNKNOWN3 = "e659f31f-ea98-11e3-ac10-0800200c9a66";
    public static final String OnewheelCharacteristicUNKNOWN4 = "e659f320-ea98-11e3-ac10-0800200c9a66";

    // These 'dummy' fields don't really sync with the device, but maintain consistency throughout the app.
    public static final String MockOnewheelCharacteristicMotorTemp = "MockOnewheelCharacteristicMotorTemp";
    public static final String MockOnewheelCharacteristicOdometer = "MockOnewheelCharacteristicOdometer";
    public static final String MockOnewheelCharacteristicOdometerCharge = "MockOnewheelCharacteristicOdometerCharge";
    public static final String MockOnewheelCharacteristicOdometerRange = "MockOnewheelCharacteristicOdometerRange";
    public static final String MockOnewheelCharacteristicSpeed = "MockOnewheelCharacteristicSpeed";
    public static final String MockOnewheelCharacteristicMaxSpeed = "MockOnewheelCharacteristicMaxSpeed";
    public static final String MockOnewheelCharacteristicPad1 = "MockOnewheelCharacteristicPad1";
    public static final String MockOnewheelCharacteristicPad2 = "MockOnewheelCharacteristicPad2";
    public static final String MockOnewheelCharacteristicCharging = "MockOnewheelCharacteristicCharging";
    public static final String MockOnewheelCharacteristicBatteryInitial = "MockOnewheelCharacteristicBatteryInitial";
    public static final String MockOnewheelCharacteristicStance = "MockOnewheelCharacteristicStance";
    public static final String MockOnewheelCharacteristicCarvability = "MockOnewheelCharacteristicCarvability";
    public static final String MockOnewheelCharacteristicAggressiveness = "MockOnewheelCharacteristicAggressiveness";


    private Address gpsLocation;

    public boolean isLit;

    public void setGpsLocation(Address gpsLocation) {
        this.gpsLocation = gpsLocation;
    }

    public Address getGpsLocation() {
        return gpsLocation;
    }

/*
0x0000 = e659F301-ea98-11e3-ac10-0800200c9a66 (OnewheelServiceUUID)
0x001a = e659F301-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicSerialNumber)
0x001d = e659f302-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicRidingMode) y
0x0021 = e659f303-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicBatteryRemaining) y
0x0025 = e659f304-ea98-11e3-ac10-0800200c9a66  batt 5
0x0029 = e659f305-ea98-11e3-ac10-0800200c9a66  batt 20
0x003d = e659f306-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicBatterySerial)
0x0041 = 659f307-ea98-11e3-ac10-0800200c9a66  (OnewheelCharacteristicTiltAnglePitch) y
0x0045 = e659f308-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicTiltAngleRoll)  y
0x0049 = e659f309-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicTiltAngleYaw)
0x003e = e659f30a-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicOdometer) y
0x0041 = e659f30b-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicSpeed) y
0x0045 = e659f30c-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicLightingMode) y
0x0049 = e659f30d-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicLightsFront)
0x004d = e659f30e-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicLightsBack)
0x0051 = e659f30f-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicStatusError) y
^ is misnamed, is an array containing riderDetected, frontPad1, frontPad2, icsuFault, icsvFault, isCharging, bmsCtrlComms, brokenCapacitor in that order
0x0055 = e659f310-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicTemperature)
0x0059 = e659f311-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicFirmwareRevision)
0x005d = e659f312-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicCurrentAmps) y
0x0061 = e659f313-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicTripTotalAmpHours) y
0x0065 = e659f314-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicTripRegenAmpHours) y
0x0069 = e659f315-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicBatteryTemp)
0x006d = e659f316-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicBatteryVoltage) y
0x0071 = e659f317-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicSafetyHeadroom)
0x0075 = e659f318-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicHardwareRevision)
0x0079 = e659f319-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicLifetimeOdometer) y
0x007d = e659f31a-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicLifetimeAmpHours)
0x0081 = e659f31b-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicBatteryCells) ys
0x0085 = e659f31c-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicLastErrorCode) ys
0x0089 = e659f31d-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicUNKNOWN1)
0x009d = e659f31e-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicUNKNOWN2) y/n
0x0101 = e659f31f-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicUNKNOWN3)
0x0105 = e659f320-ea98-11e3-ac10-0800200c9a66 (OnewheelCharacteristicUNKNOWN4)
0x0045=00 then lights are OFF
0x0045=01 is default lights
0x0045=02 is manual mode for lights
In manual mode (0x0045=02) 0x0049 is front lights and 0x004d is back lights
For both, the first byte is the level of light for white and second byte for red. Levels are 00 (off) to 75 (super bright)
SETS FRONT TO BRIGHT RED AND BACK TO BRIGHT WHITE:
gatttool --device=D0:39:72:BE:0A:32 --char-write-req --value=0002 --handle=0x0045
gatttool --device=D0:39:72:BE:0A:32 --char-write-req --value=0075 --handle=0x0049
gatttool --device=D0:39:72:BE:0A:32 --char-write-req --value=7500 --handle=0x004d
*/

    public static class DeviceCharacteristic {
        public final ObservableField<String> uuid = new ObservableField<>();
        public final ObservableField<String> key = new ObservableField<>();
        public final ObservableField<String> value = new ObservableField<>();
               final ObservableField<String> ui_name = new ObservableField<>();
        public final int state;

        DeviceCharacteristic(String uuid, String key, String ui_name, int state) {
            this.uuid.set(uuid);
            this.key.set(key);
            this.ui_name.set(ui_name);
            this.state = state;
        }
    }

    public List<DeviceCharacteristic> deviceReadCharacteristics = new ArrayList<>();
    public List<DeviceCharacteristic> deviceNotifyCharacteristics = new ArrayList<>();
    public Map<String, DeviceCharacteristic> characteristics = new HashMap<>();

    public List<DeviceCharacteristic> getReadCharacteristics() {
        return deviceReadCharacteristics;
    }
    public List<DeviceCharacteristic> getNotifyCharacteristics() {
        return deviceNotifyCharacteristics;
    }

    public DeviceCharacteristic getDeviceCharacteristicByKey(String key) {
        for (DeviceCharacteristic dc : deviceReadCharacteristics) {
            if (dc.key.get().equals(key)) {
                return dc;
            }
        }
        for (DeviceCharacteristic dc : deviceNotifyCharacteristics) {
            if (dc.key.get().equals(key)) {
                return dc;
            }
        }
        return null;
    }

    private Context context;

    public OWDevice(Context context){
        this.context = context;
    }


    /* This method is the main dictionary for the BLE Device. It contains the map for each
       Device attribute and contains the UUID, value, and display string for the UI.
     */
    public void setupCharacteristics(Context context) {
        deviceReadCharacteristics.clear();
        deviceNotifyCharacteristics.clear();

        //DO NOT TOUCH THESE, THEY ARE THE MARK OF THE DEVIL
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicFirmwareRevision, KEY_FIRMWARE_REVISION,   "FIRMWARE REVISION",-1));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicUartSerialRead,           KEY_SERIAL_READ,                   "Serial Read",-1));

        //Notification Characteristics. Max number of these allowed is 15... Usually. some devices are lower/higher and I have no way to tell. Thanks android.
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicRidingMode,        KEY_RIDE_MODE,               "Ride Mode",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicBatteryRemaining,        KEY_BATTERY,               "Battery %",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicOdometer,        KEY_ODOMETER_TIRE_REVS,               "Odometer Revs",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicSpeedRpm,        KEY_SPEED_RPM,               "Speed RPM",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicLightingMode,        KEY_LIGHTING_MODE,               "Lighting Mode",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicStatusError,        KEY_RIDER_DETECTED,               "Status & Errors",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicLifetimeOdometer,        KEY_LIFETIME_ODOMETER,               "Lifetime Odometer",0));

        //Notification Characteristics when the diagnotics page is open
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicTiltAnglePitch, KEY_TILT_ANGLE_PITCH,   "Pitch",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicTiltAngleRoll, KEY_TILT_ANGLE_ROLL,   "Roll",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicUNKNOWN2, KEY_CUSTOM_SHAPING,   "Custom Shaping Bullshit",0));

        //Additional Characteristics needed to track battery usage
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicCurrentAmps, KEY_CURRENT_AMPS,   "Current Amps",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicBatteryCells, KEY_BATTERY_CELLS,   "Battery Cell Voltages",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicBatteryVoltage, KEY_BATTERY_VOLTAGE,   "Battery Voltage",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicTripTotalAmpHours, KEY_TRIP_AMPS,   "Trip Amp Hours",0));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicTripRegenAmpHours, KEY_TRIP_AMPS_REGEN,   "Trip Amp Hours Regen",0));

        //state 1 is for things to sub and unsub from dynamically, not implemented anymore

        //Fake Characteristics for convenience only
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicSpeed,        KEY_SPEED,               "Speed",2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicPad1,         KEY_RIDER_DETECTED_PAD_1,"Pad 1", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicPad2,         KEY_RIDER_DETECTED_PAD_2,"Pad 2", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicMaxSpeed,     KEY_SPEED_MAX,           "Trip Top Speed", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicOdometer,     KEY_ODOMETER,            "Odometer", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicCharging,     KEY_CHARGING,            "Charging", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicMotorTemp,    KEY_MOTOR_TEMP,          "Motor Temp", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicOdometerCharge,     KEY_ODOMETER_CHARGE,            "Odometer Charge", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicOdometerRange,     KEY_ODOMETER_RANGE,            "Odometer Range", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicBatteryInitial,     KEY_BATTERY_INITIAL,            "Battery Initial", 2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicStance,        KEY_STANCE,               "Stance",2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicCarvability,        KEY_CARVABILITY,               "Carvability",2));
        deviceNotifyCharacteristics.add(new DeviceCharacteristic(MockOnewheelCharacteristicAggressiveness,        KEY_AGGRESSIVENESS,               "Aggressiveness",2));

        //Read these once on startup
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicHardwareRevision, KEY_HARDWARE_REVISION,   "HARDWARE REVISION",3));
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicSerialNumber, KEY_SERIAL_NUMBER,   "Serial Number",3));
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicBatterySerial, KEY_SERIAL_NUMBER_BATT,   "Serial Number Battery",3));
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicCustomName, KEY_CUSTOM_NAME,   "Custom Name",3));

        //Read these for the diagnotics page
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicTemperature, KEY_CONTROLLER_TEMP,   "Controller Temp",4));
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicLifetimeAmpHours, KEY_AMP_HOURS_LIFE,   "Lifetime Amp Hours",4));

        //Read these once a minute
        deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicBatteryTemp, KEY_BATTERY_TEMP,   "Battery Temperature",5));
        //deviceReadCharacteristics.add(new DeviceCharacteristic(OnewheelCharacteristicLastErrorCode, KEY_LAST_ERROR_CODE,   "Last Error Code",5));

        characteristics.clear();
        for (DeviceCharacteristic deviceNotifyCharacteristic : deviceNotifyCharacteristics) {
            characteristics.put(deviceNotifyCharacteristic.uuid.get(), deviceNotifyCharacteristic);
        }
        for (DeviceCharacteristic deviceReadCharacteristic : deviceReadCharacteristics) {
            characteristics.put(deviceReadCharacteristic.uuid.get(), deviceReadCharacteristic);
        }

        refreshCharacteristics(context);
    }

    private void refreshCharacteristics(Context context) {
        boolean isMetric = SharedPreferencesUtil.getPrefs(context).isMetric();
        characteristics.get(MockOnewheelCharacteristicSpeed).ui_name.set(isMetric ? "(KMH)" : "(MPH)");
        characteristics.get(MockOnewheelCharacteristicOdometer).ui_name.set("TRIP ODOMETER " + (isMetric ? "(KM)" : "(MILES)"));
        characteristics.get(OnewheelCharacteristicLifetimeOdometer).ui_name.set("LIFETIME ODOMETER " + (isMetric ? "(KM)" : "(MILES)"));
        characteristics.get(OnewheelCharacteristicTemperature).ui_name.set("CONTROLLER TEMP " + (isMetric ? "(C)" : "(F)"));
        characteristics.get(MockOnewheelCharacteristicMotorTemp).ui_name.set("MOTOR TEMP " + (isMetric ? "(C)" : "(F)"));
    }

    //private final double KmToMi = 0.62137119;

    public void refreshUnits(Context context){
        refreshCharacteristics(context);
        double KmToMi = 0.62137119;


        if (SharedPreferencesUtil.getPrefs(context).isMetric()){
            String value = getDeviceCharacteristicByKey(KEY_ODOMETER).value.get();
            if(value != null){ getDeviceCharacteristicByKey(KEY_ODOMETER).value.set((Util.parseD(value)/KmToMi) + ""); }

            String value2 = getDeviceCharacteristicByKey(KEY_ODOMETER_CHARGE).value.get();
            if(value2 != null){ getDeviceCharacteristicByKey(KEY_ODOMETER_CHARGE).value.set((Util.parseD(value2)/KmToMi) + ""); }

            String value8 = getDeviceCharacteristicByKey(KEY_ODOMETER_RANGE).value.get();
            if(value8 != null){ getDeviceCharacteristicByKey(KEY_ODOMETER_RANGE).value.set((Util.parseD(value8)/KmToMi) + ""); }

            String value3 = characteristics.get(MockOnewheelCharacteristicSpeed).value.get();
            if(value3 != null){ characteristics.get(MockOnewheelCharacteristicSpeed).value.set((Util.parseD(value3)/KmToMi) + "");}

            String value4 = characteristics.get(MockOnewheelCharacteristicMaxSpeed).value.get();
            if(value4 != null){ characteristics.get(MockOnewheelCharacteristicMaxSpeed).value.set((Util.parseD(value4)/KmToMi) + "");}

            String value5 = characteristics.get(OnewheelCharacteristicBatteryTemp).value.get();
            if(value5 != null){ characteristics.get(OnewheelCharacteristicBatteryTemp).value.set(Util.far2cel(Util.parseD(value5)) + "");}

            String value6 = characteristics.get(OnewheelCharacteristicTemperature).value.get();
            if(value6 != null){ characteristics.get(OnewheelCharacteristicTemperature).value.set(Util.far2cel(Util.parseD(value6)) + "");}

            String value7 = characteristics.get(MockOnewheelCharacteristicMotorTemp).value.get();
            if(value7 != null){ characteristics.get(MockOnewheelCharacteristicMotorTemp).value.set(Util.far2cel(Util.parseD(value7)) + "");}

            //deviceCharacteristic.value.set(String.format(Locale.getDefault(), "%.2f", isMetric ? (double) temp : cel2far(temp)));
            characteristics.get(OnewheelCharacteristicLifetimeOdometer).value.set(((int) Util.milesToKilometers(lifetimeOdometer.get()))+"");
        }else{
            String value = getDeviceCharacteristicByKey(KEY_ODOMETER).value.get();
            if(value != null){ getDeviceCharacteristicByKey(KEY_ODOMETER).value.set((Util.parseD(value)*KmToMi) + ""); }

            String value2 = getDeviceCharacteristicByKey(KEY_ODOMETER_CHARGE).value.get();
            if(value2 != null){ getDeviceCharacteristicByKey(KEY_ODOMETER_CHARGE).value.set((Util.parseD(value2)*KmToMi) + ""); }

            String value8 = getDeviceCharacteristicByKey(KEY_ODOMETER_RANGE).value.get();
            if(value8 != null){ getDeviceCharacteristicByKey(KEY_ODOMETER_RANGE).value.set((Util.parseD(value8)*KmToMi) + ""); }

            String value3 = characteristics.get(MockOnewheelCharacteristicSpeed).value.get();
            if(value3 != null){ characteristics.get(MockOnewheelCharacteristicSpeed).value.set((Util.parseD(value3)*KmToMi) + "");}

            String value4 = characteristics.get(MockOnewheelCharacteristicMaxSpeed).value.get();
            if(value4 != null){ characteristics.get(MockOnewheelCharacteristicMaxSpeed).value.set((Util.parseD(value4)*KmToMi) + "");}

            String value5 = characteristics.get(OnewheelCharacteristicBatteryTemp).value.get();
            if(value5 != null){ characteristics.get(OnewheelCharacteristicBatteryTemp).value.set(Util.cel2far(Util.parseD(value5)) + "");}

            String value6 = characteristics.get(OnewheelCharacteristicTemperature).value.get();
            if(value6 != null){ characteristics.get(OnewheelCharacteristicTemperature).value.set(Util.cel2far(Util.parseD(value6)) + "");}

            String value7 = characteristics.get(MockOnewheelCharacteristicMotorTemp).value.get();
            if(value7 != null){ characteristics.get(MockOnewheelCharacteristicMotorTemp).value.set(Util.cel2far(Util.parseD(value7)) + "");}

            characteristics.get(OnewheelCharacteristicLifetimeOdometer).value.set(lifetimeOdometer.get()+"");
        }
    }


    // Status fields
    public boolean lightState = false;
    public final ObservableField<String> bluetoothLe = new ObservableField<>();
    public final ObservableField<String> bluetoothStatus = new ObservableField<>();
    public final ObservableField<String> deviceMacName = new ObservableField<>();
    public final ObservableField<String> deviceMacAddress = new ObservableField<>();
    public final ObservableField<String> log = new ObservableField<>();

    public static float m14598a(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        byte[] value = bluetoothGattCharacteristic.getValue();
        if (value == null || value.length != 2) {
            return 0.0f;
        }
        return (float) ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).getChar();
    }

    @Override
    public void processUUID(BluetoothGattCharacteristic incomingCharacteristic) {
        String incomingUuid = incomingCharacteristic.getUuid().toString();
        byte[] incomingValue = incomingCharacteristic.getValue();


        DeviceCharacteristic dc = characteristics.get(incomingUuid);

        if (dc == null){
            Log.w("ONEWAVE","RECIEVED UNKNOWN CHARACTERISTIC " + incomingUuid);
            return;
        }

        String dev_uuid = dc.uuid.get();

        if(dev_uuid != null && dev_uuid.equals(incomingUuid)) {
            switch(dev_uuid) {
                case OnewheelCharacteristicHardwareRevision:
                    int hver = Util.unsignedShort(incomingValue);
                    dc.value.set(Integer.toString(hver));
                    Battery.setHardware(hver);
                    break;
                case OnewheelCharacteristicFirmwareRevision:
                    dc.value.set(Integer.toString(Util.unsignedShort(incomingValue)));
                    break;
                case OnewheelCharacteristicLifetimeOdometer:
                    processLifetimeOdometer(incomingValue, dc);
                    break;
                case OnewheelCharacteristicBatterySerial:
                    // Battery: Lithium Iron Phosphate (LiFePo4) 48V
                    //batterySerialNumber.set(Integer.toString(unsignedShort(c_value)));
                    break;
                case OnewheelCharacteristicLightingMode:
                    processLightingMode(incomingValue, dc);
                    break;
                case OnewheelCharacteristicLastErrorCode:
                    processErrorCode(incomingCharacteristic, dc);
                    break;
                case OnewheelCharacteristicBatteryVoltage:
                    processBatteryVoltage(incomingValue, dc);
                    break;
                case OnewheelCharacteristicBatteryRemaining:
                    processBatteryRemaining(incomingCharacteristic, dc);
                    break;
                case OnewheelCharacteristicTiltAnglePitch:
                    processPitch(incomingValue, dc);
                    break;
                case OnewheelCharacteristicTiltAngleRoll:
                    processRoll(incomingValue, dc);
                    break;
                case OnewheelCharacteristicTiltAngleYaw:
                    dc.value.set(Integer.toString(Util.unsignedShort(incomingValue)));
                    break;
                case OnewheelCharacteristicStatusError:
                    processStatusError(incomingValue, dc);
                    break;
                case OnewheelCharacteristicOdometer:
                    processOdometer(incomingValue, dc);
                    break;
                case OnewheelCharacteristicSpeedRpm:
                    processSpeedRpm(incomingValue, dc);
                    break;
                case OnewheelCharacteristicCurrentAmps:
                    processCurrentAmps(incomingValue, dc);
                    break;
                case OnewheelCharacteristicBatteryCells:
                    processBatteryCellsVoltage(incomingValue, dc);
                    break;
                case OnewheelCharacteristicTemperature:
                    processControllerAndMotorTemp(incomingCharacteristic);
                    break;
                case OnewheelCharacteristicBatteryTemp:
                    processBatteryTemp(incomingCharacteristic, dc);
                    break;
                case OnewheelCharacteristicSafetyHeadroom:
                    dc.value.set(incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0).toString());
                    Log.w("THINGY",dc.value.get());
                    break;
                case OnewheelCharacteristicTripTotalAmpHours:
                    processTripTotalAmpHours(incomingValue, dc);
                    break;
                case OnewheelCharacteristicTripRegenAmpHours:
                    processTripRegenHours(incomingValue, dc);
                    break;
                case OnewheelCharacteristicLifetimeAmpHours:
                    dc.value.set(Integer.toString(Util.unsignedShort(incomingValue)));
                    break;
                case OnewheelCharacteristicRidingMode:
                    processRidingMode(incomingValue, dc, incomingCharacteristic);
                    break;
                case OnewheelCharacteristicCustomName:
                    dc.value.set(String.valueOf(incomingValue));
                    break;
                case OnewheelCharacteristicUNKNOWN1:
                    dc.value.set(Integer.toString(Util.unsignedShort(incomingValue)));
                    break;
                case OnewheelCharacteristicUNKNOWN2:
                    dc.value.set(incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0).toString()+"  "+incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 1).toString());
                    processCustomRidemode(incomingCharacteristic,dc);
                    break;
                case OnewheelCharacteristicUNKNOWN3:
                    dc.value.set(Integer.toString(Util.unsignedShort(incomingValue)));
                    break;
                case OnewheelCharacteristicUNKNOWN4:
                    dc.value.set(Integer.toString(Util.unsignedShort(incomingValue)));
                    break;
                default:
                    processUnknownUuid(incomingUuid, incomingValue);
                    //Log.w("UNKNOIWN UUID",incomingUuid);
            }
        }else{
            Log.w("TESSTING",incomingUuid);
        }

    }

    private void processBatteryVoltage(byte[] incomingValue, DeviceCharacteristic dc) {
        //double d_value = Double.valueOf((double) c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1) / 10.0D);
        int d_volts = Util.unsignedShort(incomingValue);
        double d_value = ((double) d_volts / 10.0D);

        dc.value.set(Double.toString(d_value));
        updateBatteryChanges |= Battery.setOutput(d_value);
    }

    private void processBatteryRemaining(BluetoothGattCharacteristic incomingCharacteristic, DeviceCharacteristic dc) {
        int batteryLevel = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1);

        //dc.value.set(Integer.toString(batteryLevel));
        updateBatteryChanges |= Battery.setRemaining(batteryLevel);
    }

    private void processPitch(byte[] incomingValue, DeviceCharacteristic dc) {

        int i_tiltAnglePitch = Util.unsignedShort(incomingValue);

        if (i_tiltAnglePitch > maxTiltAnglePitch.get()) {
            maxTiltAnglePitch.set(i_tiltAnglePitch);

        }
        dc.value.set(Integer.toString(i_tiltAnglePitch));
    }

    private void processRoll(byte[] incomingValue, DeviceCharacteristic dc) {
        int i_tiltAngleRoll = Util.unsignedShort(incomingValue);

        if (i_tiltAngleRoll > maxTiltAngleRoll.get()) {
            maxTiltAngleRoll.set(i_tiltAngleRoll);

        }
        dc.value.set(Integer.toString(i_tiltAngleRoll));
    }

    private void processStatusError(byte[] incomingValue, DeviceCharacteristic dc) {
        DeviceStatus deviceStatus = DeviceStatus.from(incomingValue);
        //charging.set(Boolean.toString(deviceStatus.charging));
        //bmsCtrlComms.set(Boolean.toString(deviceStatus.bmsCtrlComms));
        //icsuFault.set(Boolean.toString(deviceStatus.icsuFault));
        //icsvFault.set(Boolean.toString(deviceStatus.icsvFault));
        Log.w("TESTING","GPT BOARD STATUS");

        characteristics.get(MockOnewheelCharacteristicPad1).value.set(Boolean.toString(deviceStatus.riderDetectPad1));
        characteristics.get(MockOnewheelCharacteristicPad2).value.set(Boolean.toString(deviceStatus.riderDetectPad2));
        characteristics.get(MockOnewheelCharacteristicCharging).value.set(Boolean.toString(deviceStatus.charging));
        dc.value.set(Boolean.toString(deviceStatus.riderDetected));
//        for (DeviceCharacteristic dc2 : deviceNotifyCharacteristics) {
//            // TODO 'charging' is commented out, I think @kwatkins said its not working
//            if (dc2.key.get().equals("charging")) {
//                dc2.value.set(Boolean.toString(deviceStatus.charging));
//            }
//        }
        NotificationBuilder.sensorWarning(context);
        if (deviceStatus.charging){
            updateChargeDistCharging();
        }
    }

    private void processOdometer(byte[] incomingValue, DeviceCharacteristic dc) {
        int i_odometer = Util.unsignedShort(incomingValue);
        DeviceCharacteristic dc_odometer = getDeviceCharacteristicByKey(KEY_ODOMETER);
        if (dc_odometer != null) {
            if (SharedPreferencesUtil.getPrefs(context).isMetric()) {
                //dc_odometer.value.set(String.format(Locale.getDefault(),"%3.2f", revolutionsToKilometers((double) i_odometer)));
                dc_odometer.value.set(Util.revolutionsToKilometers((double) i_odometer) + "");
            } else {
                //dc_odometer.value.set(String.format(Locale.getDefault(),"%3.2f", revolutionsToMiles((double) i_odometer)));
                dc_odometer.value.set(Util.revolutionsToMiles((double) i_odometer) + "");
            }
        }
        updateChargeDist((float)i_odometer);

        dc.value.set(Integer.toString(i_odometer));

        if (i_odometer > SharedPreferencesUtil.getPrefs(context).getDistanceRecord()){
            SharedPreferencesUtil.getPrefs(context).setDistanceRecord(i_odometer);
        }
    }

    private void processSpeedRpm(byte[] incomingValue, DeviceCharacteristic dc) {
        int i_speedRpm = Util.unsignedShort(incomingValue);
        speedRpm.set(i_speedRpm);
        dc.value.set(Integer.toString(i_speedRpm));
        updateBatteryChanges |= Battery.setSpeedRpm(i_speedRpm);
        DeviceCharacteristic speedCharacteristic = characteristics.get(MockOnewheelCharacteristicSpeed);
        DeviceCharacteristic maxSpeedCharacteristic = characteristics.get(MockOnewheelCharacteristicMaxSpeed);
        setFormattedSpeedWithMetricPreference(speedCharacteristic, i_speedRpm);
        if (i_speedRpm > maxSpeedRpm.get()) {
            setFormattedSpeedWithMetricPreference(maxSpeedCharacteristic, i_speedRpm);
            maxSpeedRpm.set(i_speedRpm);
        }
        //SharedPreferencesUtil.getPrefs().setSpeedRecord(0);
        NotificationBuilder.speedNotification(context);
        NotificationBuilder.speedWarning(context);
        NotificationBuilder.speedWarning2(context);
        if (i_speedRpm > SharedPreferencesUtil.getPrefs(context).getSpeedRecord()){
            SharedPreferencesUtil.getPrefs(context).setSpeedRecord(i_speedRpm);
        }
    }

    private void processControllerAndMotorTemp(BluetoothGattCharacteristic incomingCharacteristic) {
        int controllerTemp = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        int motorTemp = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1);

        setFormattedTempWithMetricPreference(characteristics.get(OnewheelCharacteristicTemperature), controllerTemp);
//        Timber.d("controllerTemp = " + controllerTemp);
        setFormattedTempWithMetricPreference(characteristics.get(MockOnewheelCharacteristicMotorTemp), motorTemp);
    }

    private void setFormattedTempWithMetricPreference(DeviceCharacteristic deviceCharacteristic, int temp) {
        boolean isMetric = SharedPreferencesUtil.getPrefs(context).isMetric();
        deviceCharacteristic.value.set(String.format(Locale.getDefault(), "%.2f", isMetric ? (double) temp : Util.cel2far(temp)));
    }

    private void setFormattedSpeedWithMetricPreference(DeviceCharacteristic deviceCharacteristic, double speedRpm) {
        boolean isMetric = SharedPreferencesUtil.getPrefs(context).isMetric();
        deviceCharacteristic.value.set(String.format(Locale.getDefault(), "%3.2f", isMetric ? Util.rpmToKilometersPerHour((double) speedRpm) : Util.rpmToMilesPerHour((double) speedRpm)));
    }

    private void processBatteryTemp(BluetoothGattCharacteristic incomingCharacteristic, DeviceCharacteristic dc) {
        int batteryTemp = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);

        //Timber.d("batteryTemp = " + batteryTemp);

        setFormattedTempWithMetricPreference(dc, batteryTemp);
        updateBatteryChanges |= Battery.setBatteryTemp(batteryTemp);
    }

    private void processCustomRidemode(BluetoothGattCharacteristic incomingCharacteristic, DeviceCharacteristic dc){
        int which = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 0);
        int val = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 1);

        if (which == 0){
            characteristics.get(MockOnewheelCharacteristicStance).value.set(val+"");
        }else if (which == 1){
            characteristics.get(MockOnewheelCharacteristicCarvability).value.set(val+"");
        }else if (which == 2){
            characteristics.get(MockOnewheelCharacteristicAggressiveness).value.set(val+"");
        }

        //if (characteristics.get(MockOnewheelCharacteristicStance).value.get() != null && characteristics.get(MockOnewheelCharacteristicCarvability).value.get() != null && characteristics.get(MockOnewheelCharacteristicAggressiveness).value.get() != null){
            //BluetoothService.getBluetoothUtil().killCustomNotifications();
        //}
    }

    private void processUnknownUuid(String incomingUuid, byte[] incomingValue) {
        StringBuilder sb = new StringBuilder();
        for (byte b : incomingValue) {
            sb.append(String.format("%02x", b));
        }
        //this.unknownUUID.set(c_uuid);
        //this.unknownValue.set("hex:" + sb.toString() + " (" + Integer.toString(unsignedShort(c_value)) + ")");
        //EventBus.getDefault().post(new DeviceStatusEvent("UNKNOWN " + incomingUuid + ":" +
        //"hex:" + sb.toString() + " (" + Integer.toString(unsignedShort(incomingValue)) + ")"));
        //Timber.i( "UNKNOWN Device characteristic:" + incomingUuid + " value=" + sb.toString() + "|" + Integer.toString(unsignedShort(incomingValue)));
        //Log.w("UUID incomingUuid","VALUE: "+sb.toString()+ "|" + Integer.toString(Util.unsignedShort(incomingValue)));
    }

    private void processTripRegenHours(byte[] incomingValue, DeviceCharacteristic dc) {
        int i_tripregenamp = Util.unsignedShort(incomingValue);
        double d_tripregenamp = ((double) i_tripregenamp / 50.0D);
        dc.value.set(Double.toString(d_tripregenamp));
        updateBatteryChanges |= Battery.setRegenAmpHrs(d_tripregenamp);
    }

    private void processTripTotalAmpHours(byte[] incomingValue, DeviceCharacteristic dc) {
        //tripTotalAmpHours.set(c.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1).toString());
        //tripTotalAmpHours.set(Integer.toString(unsignedShort(c_value)));
        //int i_cells_2 = unsignedByte(c_value[0]);
        //int camps2 = unsignedByte(c_value[1]);
        //double d_camps2 = (double)camps2 / 50.0D;
        //tripTotalAmpHours.set("batteryVoltageCells:" + i_cells_2 + " value:" + d_camps2);
        int i_amphours = Util.unsignedShort(incomingValue);
        double d_amphours = ((double) i_amphours / 50.0D);
        dc.value.set(Double.toString(d_amphours));
        updateBatteryChanges |= Battery.setUsedAmpHrs(d_amphours);
    }

    private void processRidingMode(byte[] incomingValue, DeviceCharacteristic dc, BluetoothGattCharacteristic incomingCharacteristic) {

        int ridemode = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1);
        String rideMode1 = Integer.toString(ridemode);
        if (Util.parseI(rideMode1) < 4){
            isOneWheelPlus.set(false);
        }else{
            isOneWheelPlus.set(true);
        }
        //Timber.d("rideMode1 = " + rideMode1);

        dc.value.set(rideMode1);
    }

    private void processLifetimeOdometer(byte[] incomingValue, DeviceCharacteristic dc) {
        int i_lifetime = Util.unsignedShort(incomingValue);
        lifetimeOdometer.set(i_lifetime);
        SharedPreferencesUtil.getPrefs(context).setDistanceTotal(i_lifetime);
        if (SharedPreferencesUtil.getPrefs(context).isMetric()) {
            dc.value.set(String.format(Locale.getDefault(),"%.2f", Util.milesToKilometers(i_lifetime)));
        } else {
            dc.value.set(Integer.toString(i_lifetime));
        }
    }

    private void processLightingMode(byte[] incomingValue, DeviceCharacteristic dc) {
        switch (Util.unsignedShort(incomingValue)) {
            case 0:
                lightState = false;
                dc.value.set("0 (Off)");
                isLit = false;
                break;
            case 1:
                lightState = true;
                dc.value.set("1 (On)");
                isLit = true;
                break;
            case 2:
                lightState = false;
                dc.value.set("2 (Off)");
                isLit = false;
                break;
            case 3:
                lightState = false;
                dc.value.set("3 (Off)");
                isLit = false;
        }
    }

    private void processErrorCode(BluetoothGattCharacteristic incomingCharacteristic, DeviceCharacteristic dc) {
        int error_code  = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 0);
        int error_code2  = incomingCharacteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_SINT8, 1);

        //dc.value.set(ERROR_CODE_MAP.get(error_code, " UNKNOWN") + " " + error_code + ":" + error_code2 + "");
        dc.value.set(error_code + "   " + error_code2);
        //dc.value.set(""+new String(incomingCharacteristic.getValue()));
    }

    private void processBatteryCellsVoltage(byte[] incomingValue, DeviceCharacteristic dc) {
        int cellIdentifier = Util.unsignedByte(incomingValue[0]);
        double volts = 0.0;
        int count = 0;

        if(cellIdentifier < batteryVoltageCells.length && cellIdentifier >= 0) {
            int var3 = Util.unsignedByte(incomingValue[1]);
            batteryVoltageCells[cellIdentifier] = (double)var3 / 50.0D;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < batteryVoltageCells.length; ++i) {
            if (batteryVoltageCells[i] < 0.1) {
                stringBuilder.append("----");
            } else {
                count++;
                volts+=batteryVoltageCells[i];
                stringBuilder.append(String.format(Locale.ENGLISH, "%.02f",
                    batteryVoltageCells[i]));
            }

            if ((i+1) % 4 == 0 && i != batteryVoltageCells.length-1) {
                stringBuilder.append("\n");
            } else if(i != batteryVoltageCells.length - 1) {
                stringBuilder.append(',');
            }

        }
        String batteryCellsVoltage = stringBuilder.toString();
        dc.value.set(batteryCellsVoltage);
        if (Battery.checkCells(count)) {
            updateBatteryChanges |= Battery.setCells(volts);
        }
    }

    private void processCurrentAmps(byte[] incomingValue, DeviceCharacteristic dc) {
        float incoming = ByteBuffer.wrap(incomingValue).getShort();
        float multiplier;
        // TODO reference datasheet of chips/sensors
        if (isOneWheelPlus.get() == null || isOneWheelPlus.get()) {
            multiplier = 1.8f;
        } else {
            multiplier = 0.9f;
        }
        final float amps = incoming / 1000.0f * multiplier;
        dc.value.set(String.format(Locale.ENGLISH, "%.2f",amps));
        updateBatteryChanges |= Battery.setAmps(amps);
    }

    public void forceBatteryRemaining() {
        updateBatteryChanges=true;
    }

    public void setBatteryRemaining() {
        SharedPreferencesUtil prefs = SharedPreferencesUtil.getPrefs(context);

        if (updateBatteryChanges) {
            DeviceCharacteristic dc = characteristics.get(OnewheelCharacteristicBatteryRemaining);
            DeviceCharacteristic mock = characteristics.get(MockOnewheelCharacteristicBatteryInitial);
            int remaining = 0;

            if (prefs.getIsBatteryOutput()) {
                remaining = Battery.getRemainingOutput();
            } else if (prefs.getIsBatteryCells()) {
                remaining = Battery.getRemainingCells();
            } else if (prefs.getIsBatteryTwoX()) {
                remaining = Battery.getRemainingTwoX();
                Battery.saveStateTwoX(prefs);
            } else {
                remaining = Battery.getRemainingDefault();
            }

            dc.value.set(Integer.toString(remaining));
            if (mock.value.get() == null) {
                mock.value.set(Integer.toString(remaining));
            }

            calcRange(remaining);

            updateBatteryChanges = false;
        }

    }


    private float lastDist = -1;
    private void updateChargeDist( float dist){
        //SharedPreferencesUtil.getPrefs().setChargeDistRev(0);
        if (lastDist < 0){
            if (characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get() != null){
                float battery = Util.parseF(characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
                //float dist = revs;
                if (SharedPreferencesUtil.getPrefs(context).getChargeDistBat() >= battery){
                    lastDist = dist;
                    SharedPreferencesUtil.getPrefs(context).setChargeDistBat(battery);
                    //SharedPreferencesUtil.getPrefs().setChargeDistRev(SharedPreferencesUtil.getPrefs().getChargeDistRev()+dist);
                }else{
                    lastDist = dist;
                    SharedPreferencesUtil.getPrefs(context).setChargeDistRev(dist);
                    SharedPreferencesUtil.getPrefs(context).setChargeDistBat(battery);
                }
                if (SharedPreferencesUtil.getPrefs(context).isMetric()) {
                    characteristics.get(MockOnewheelCharacteristicOdometerCharge).value.set(Util.revolutionsToKilometers(SharedPreferencesUtil.getPrefs(context).getChargeDistRev()) + "");
                }else{
                    characteristics.get(MockOnewheelCharacteristicOdometerCharge).value.set(Util.revolutionsToMiles(SharedPreferencesUtil.getPrefs(context).getChargeDistRev()) + "");
                }
            }
        }else{
            float battery = Util.parseF(characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get());
            //float dist = revs;

            SharedPreferencesUtil.getPrefs(context).setChargeDistBat(battery);
            SharedPreferencesUtil.getPrefs(context).setChargeDistRev(SharedPreferencesUtil.getPrefs(context).getChargeDistRev()+(dist-lastDist));
            lastDist = dist;

            if (SharedPreferencesUtil.getPrefs(context).isMetric()) {
                characteristics.get(MockOnewheelCharacteristicOdometerCharge).value.set(Util.revolutionsToKilometers(SharedPreferencesUtil.getPrefs(context).getChargeDistRev()) + "");
            }else{
                characteristics.get(MockOnewheelCharacteristicOdometerCharge).value.set(Util.revolutionsToMiles(SharedPreferencesUtil.getPrefs(context).getChargeDistRev()) + "");
            }
        }
    }
    private void updateChargeDistCharging(){
        SharedPreferencesUtil.getPrefs(context).setChargeDistRev(0);
    }


    private int battStart = -1;
    private int battStartCalc = -1;
    private float rStart = -1;
    private void calcRange(int battery){
        if (battery > battStart){
            battStart = battery;
        }
        if (battStart >= 0 && battery < battStart && rStart < 0){
            if (characteristics.get(OnewheelCharacteristicOdometer).value.get() != null) {
                rStart = Util.parseF(characteristics.get(OnewheelCharacteristicOdometer).value.get());
                battStartCalc = battery;
            }
        }
        if (battStartCalc >= 0 && battery < battStartCalc && rStart >= 0) {
            if (characteristics.get(OnewheelCharacteristicOdometer).value.get() != null) {
                float revs = Util.parseF(characteristics.get(OnewheelCharacteristicOdometer).value.get()) - rStart;
                float battGone = battStartCalc - battery;
                float revsRem = revs * battery / battGone;

                if (SharedPreferencesUtil.getPrefs(context).isMetric()) {
                    characteristics.get(MockOnewheelCharacteristicOdometerRange).value.set(Util.revolutionsToKilometers(revsRem) + "");
                } else {
                    characteristics.get(MockOnewheelCharacteristicOdometerRange).value.set(Util.revolutionsToMiles(revsRem) + "");
                }
            }
        }
        if (characteristics.get(MockOnewheelCharacteristicCharging).value.get() != null && characteristics.get(MockOnewheelCharacteristicCharging).value.get().equals("true")){
            battStart = -1;
            battStartCalc = -1;
            rStart = -1;

            characteristics.get(MockOnewheelCharacteristicOdometerRange).value.set(0 + "");
        }
    }




    private static String[] modesList = {"Uh Oh","Classic","Extreme","Elevated","Sequoia","Cruz","Mission","Elevated","Delirium","Custom"};
    private static String[] modeSpeeds = {"Uh oh","12","15","15","12","15","19","19","20",""};
    public void speedChange(View view,int ID, int ID2){
        final TextView speedText = view.findViewById(ID);
        String value = characteristics.get(MockOnewheelCharacteristicSpeed).value.get();
        if(value == null)  return;
        final int disVal = Math.round(Util.parseF(value));
        speedText.post(new Runnable() {
            @Override
            public void run() {
                speedText.setText(disVal+"");
            }
        });

        final ProgressBar speedometer = view.findViewById(ID2);
        int tOOM = (int)(Util.parseF(value) * 100);
        if (tOOM > 2500){
            tOOM = 2500;
        }
        final int result = tOOM;
        speedometer.post(new Runnable() {
            @Override
            public void run() {
                speedometer.setProgress(result);
            }
        });
    }
    public void modeChange(View view, int ID, int ID2){
        final TextView textMode = view.findViewById(ID);
        String value = characteristics.get(OnewheelCharacteristicRidingMode).value.get();
        if(value == null)  return;
        final int disVal = Math.round(Util.parseF(value));
        textMode.post(new Runnable() {
            @Override
            public void run() {
                textMode.setText(modesList[disVal]);
            }
        });

        final TextView textSpeed = view.findViewById(R.id.textModeSpeed);
        textSpeed.post(new Runnable() {
            @Override
            public void run() {
                textSpeed.setText(modeSpeeds[disVal]);
            }
        });

        final ImageButton imgButton = view.findViewById(R.id.buttonCustomMode);
        boolean visible = false;
        if (disVal == 9){//custom ridemode edit button
            visible = true;
        }
        final boolean visible2 = visible;
        imgButton.post(new Runnable() {
            @Override
            public void run() {
                if (visible2) {
                    imgButton.setVisibility(View.VISIBLE);
                }else{
                    imgButton.setVisibility(View.GONE);
                }
            }
        });
    }
    public void batteryChange(Context context){

        String value = characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get();
        if(value == null)  return;
        final int percent = Util.parseI(value);
        BluetoothService.updateNotification(percent,context);
    }
    public void batteryChange(View view,int ID, int ID2){
        final TextView batText = view.findViewById(ID);
        String value = characteristics.get(OnewheelCharacteristicBatteryRemaining).value.get();
        if(value == null)  return;
        final int percent = Util.parseI(value);
        batText.post(new Runnable() {
            @Override
            public void run() {
                batText.setText(percent + "%");
            }
        });

        final ProgressBar batBar = view.findViewById(ID2);
        batBar.post(new Runnable() {
            @Override
            public void run() {
                batBar.setProgress(percent);
            }
        });
    }
    public void maxSpeedChange(View view,int ID){
        final TextView test = view.findViewById(ID);
        String value = characteristics.get(MockOnewheelCharacteristicMaxSpeed).value.get();
        if(value == null)  return;
        final String disVal = String.format(Locale.getDefault(), "%.2f",Util.parseF(value));
        test.post(new Runnable() {
            @Override
            public void run() {
                test.setText(disVal+"");
            }
        });
    }
    public void odometerChange(View view,int ID){
        final TextView total = view.findViewById(ID);
        String value = characteristics.get(MockOnewheelCharacteristicOdometer).value.get();
        if(value == null)  return;
        final String disVal = String.format(Locale.getDefault(), "%.2f",Util.parseF(value));
        total.post(new Runnable() {
            @Override
            public void run() {
                total.setText(disVal);
            }
        });
    }
    public void chargeOdometerChange(View view,int ID){
        final TextView total = view.findViewById(ID);
        String value = characteristics.get(MockOnewheelCharacteristicOdometerCharge).value.get();
        if(value == null)  return;
        final String disVal = String.format(Locale.getDefault(), "%.2f",Util.parseF(value));
        total.post(new Runnable() {
            @Override
            public void run() {
                total.setText(disVal);
            }
        });
    }
    public void rangeOdometerChange(View view,int ID) {
        final TextView total = view.findViewById(ID);
        String value = characteristics.get(MockOnewheelCharacteristicOdometerRange).value.get();
        if (value == null) return;
        final String disVal = String.format(Locale.getDefault(), "%.2f", Util.parseF(value));
        total.post(new Runnable() {
            @Override
            public void run() {
                total.setText(disVal);
            }
        });
    }
    public void lifeOdometerChange(View view,int ID){
        final TextView total = view.findViewById(ID);
        String value = characteristics.get(OnewheelCharacteristicLifetimeOdometer).value.get();
        if(value == null)  return;
        final String disVal = (int)Util.parseF(value) + "";
        total.post(new Runnable() {
            @Override
            public void run() {
                total.setText(disVal);
            }
        });
    }
    public void unitsChange (View view,int ID){
        final TextView speedUnits = view.findViewById(ID);
        String units = "Km/Hr";
        if (!SharedPreferencesUtil.getPrefs(view.getContext()).isMetric()){
            units = "Mi/Hr";
        }
        final String result = units;
        speedUnits.post(new Runnable() {
            @Override
            public void run() {
                speedUnits.setText(result);
            }
        });
    }








    /* These are helper methods used to set BLE Device characteristic values */
    /*public void setCharacteristicValue(BluetoothGattService gattService, BluetoothGatt gatt, String k, int v) {
        DeviceCharacteristic dc = getDeviceCharacteristicByKey(k);
        if (dc != null) {
            BluetoothGattCharacteristic lc;
            lc = gattService.getCharacteristic(UUID.fromString(dc.uuid.get()));
            if (lc != null) {
                ByteBuffer var2 = ByteBuffer.allocate(2);
                var2.putShort((short) v);
                lc.setValue(var2.array());
                lc.setWriteType(2);
                gatt.writeCharacteristic(lc);
                //EventBus.getDefault().post(new DeviceStatusEvent("SET " + k + " TO " + v));
            }
        }
    }*/

    //Needed for Gemini, kick off the key/challenge workflow
    public void sendKeyChallengeForGemini(BluetoothUtil bluetoothUtil) {
        BluetoothGattCharacteristic lc;
        lc = bluetoothUtil.getCharacteristic(OnewheelCharacteristicFirmwareRevision);
        //lc.setValue(new byte[] { 16, 38 });
        bluetoothUtil.writeCharacteristic(lc);
    }

    public void setLights(BluetoothUtil bluetoothUtil, int state) {

        lightMode.set(state);
        BluetoothGattCharacteristic lc;

        ByteBuffer v = ByteBuffer.allocate(2);
        lc = bluetoothUtil.getCharacteristic(OnewheelCharacteristicLightingMode);

        v.putShort((short) state);
        if (lc != null) {
            lc.setValue(v.array());
            lc.setWriteType(2);
            bluetoothUtil.writeCharacteristic(lc);
            //EventBus.getDefault().post(new DeviceStatusEvent("LIGHTS SET TO STATE:" + state));
        }

    }
/*
    private final ObservableInt frontLightsWhite = new ObservableInt();
    private final ObservableInt frontLightsRed = new ObservableInt();
    private final ObservableInt backLightsWhite = new ObservableInt();
    private final ObservableInt backLightsRed = new ObservableInt();

    public void setCustomLights(BluetoothUtil bluetoothUtil, int position, int color, int colorLevel) {
        BluetoothGattCharacteristic lc;
        // front lights
        if (position == 0) {
            if (color == 0) {
                frontLightsWhite.set(colorLevel);
            }
            if (color == 1) {
                frontLightsRed.set(colorLevel);
            }
            lc = bluetoothUtil.getCharacteristic(OnewheelCharacteristicLightsFront);
            if (lc != null) {
                // lc.setValue(new byte[] { (byte)frontLightsWhite.get(), (byte) frontLightsRed.get() });
                int x = frontLightsWhite.get();
                int y = frontLightsRed.get();
                lc.setValue(new byte[] { (byte) x, (byte) y });
                lc.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                bluetoothUtil.writeCharacteristic(lc);
            }

        }

        if (position == 1) {
            if (color == 0) {
                backLightsWhite.set(colorLevel);
            }
            if (color == 1) {
                backLightsRed.set(colorLevel);
            }

            lc = bluetoothUtil.getCharacteristic(OnewheelCharacteristicLightsBack);
            if (lc != null) {
                int x = backLightsWhite.get();
                int y = backLightsRed.get();
                lc.setValue(new byte[] { (byte) x, (byte) y });
                lc.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
                bluetoothUtil.writeCharacteristic(lc);
            }

        }
    }*/

    public void setRideMode(BluetoothUtil bluetoothUtil, int ridemode) {
        //Timber.d("setRideMode() called for gatt:" + ridemode);
        BluetoothGattCharacteristic lc = bluetoothUtil.getCharacteristic(OnewheelCharacteristicRidingMode);
        if (lc != null) {
            ByteBuffer var2 = ByteBuffer.allocate(2);
            var2.putShort((short) ridemode);
            lc.setValue(var2.array());
            lc.setWriteType(2);
            bluetoothUtil.writeCharacteristic(lc);
            //setDeviceCharacteristicDisplay("ride_mode","ridemode: " + ridemode);
        }
    }

    /*public void setCustom(BluetoothUtil bluetoothUtil, int val){
        BluetoothGattCharacteristic lc = bluetoothUtil.getCharacteristic(OnewheelCharacteristicUNKNOWN2);
        if (lc != null) {
            lc.setValue(new byte[] { (byte) 2, (byte) val });
            lc.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);

            /*ByteBuffer var2 = ByteBuffer.allocate(2);
            var2.putShort((short) val);
            lc.setValue(var2.array());
            lc.setWriteType(2);*/
            //bluetoothUtil.writeCharacteristic(lc);
            //setDeviceCharacteristicDisplay("ride_mode","ridemode: " + ridemode);
        //}
    //}


    /*@Override
    public String getCSVHeader() {
        StringBuilder headers = new StringBuilder();
        for(DeviceCharacteristic dc : this.deviceNotifyCharacteristics) {
            headers.append(',').append(dc.key.get());
        }
        return "time" + headers.toString() + '\n';
    }

    @Override
    public String toCSV() {
        String dateTimeString = SIMPLE_DATE_FORMAT.format(new Date());
        String header = String.format(Locale.US, "%s", dateTimeString);
        StringBuilder values = new StringBuilder();
        for(DeviceCharacteristic dc : this.deviceNotifyCharacteristics) {
            values.append(',').append(dc.value.get());
        }

        if (gpsLocation != null) {
            values.append(",LOC=(").append(gpsLocation.getLongitude() + "," + gpsLocation.getLatitude()).append(")");
        }
        return header + values.toString() + '\n';
    }*/

    @Override
    public String getName() { return NAME; }



}
