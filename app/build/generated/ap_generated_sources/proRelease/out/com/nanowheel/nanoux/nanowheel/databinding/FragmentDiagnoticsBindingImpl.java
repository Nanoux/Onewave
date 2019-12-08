package com.nanowheel.nanoux.nanowheel.databinding;
import com.nanowheel.nanoux.nanowheel.R;
import com.nanowheel.nanoux.nanowheel.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentDiagnoticsBindingImpl extends FragmentDiagnoticsBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.speed_record, 19);
        sViewsWithIds.put(R.id.distance_record, 20);
        sViewsWithIds.put(R.id.distance_total, 21);
        sViewsWithIds.put(R.id.pitch, 22);
        sViewsWithIds.put(R.id.pitch_text, 23);
        sViewsWithIds.put(R.id.roll, 24);
        sViewsWithIds.put(R.id.roll_text, 25);
        sViewsWithIds.put(R.id.refresh_button, 26);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView1;
    @NonNull
    private final android.widget.TextView mboundView10;
    @NonNull
    private final android.widget.TextView mboundView11;
    @NonNull
    private final android.widget.TextView mboundView12;
    @NonNull
    private final android.widget.TextView mboundView13;
    @NonNull
    private final android.widget.TextView mboundView14;
    @NonNull
    private final android.widget.TextView mboundView15;
    @NonNull
    private final android.widget.TextView mboundView16;
    @NonNull
    private final android.widget.TextView mboundView17;
    @NonNull
    private final android.widget.TextView mboundView18;
    @NonNull
    private final android.widget.TextView mboundView2;
    @NonNull
    private final android.widget.TextView mboundView3;
    @NonNull
    private final android.widget.TextView mboundView4;
    @NonNull
    private final android.widget.TextView mboundView5;
    @NonNull
    private final android.widget.TextView mboundView6;
    @NonNull
    private final android.widget.ImageView mboundView7;
    @NonNull
    private final android.widget.ImageView mboundView8;
    @NonNull
    private final android.widget.ImageView mboundView9;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentDiagnoticsBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 27, sIncludes, sViewsWithIds));
    }
    private FragmentDiagnoticsBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 18
            , (android.widget.TextView) bindings[20]
            , (android.widget.TextView) bindings[21]
            , (android.widget.ImageView) bindings[22]
            , (android.widget.TextView) bindings[23]
            , (com.google.android.material.button.MaterialButton) bindings[26]
            , (android.widget.ImageView) bindings[24]
            , (android.widget.TextView) bindings[25]
            , (android.widget.TextView) bindings[19]
            );
        this.mboundView0 = (android.widget.RelativeLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        this.mboundView10 = (android.widget.TextView) bindings[10];
        this.mboundView10.setTag(null);
        this.mboundView11 = (android.widget.TextView) bindings[11];
        this.mboundView11.setTag(null);
        this.mboundView12 = (android.widget.TextView) bindings[12];
        this.mboundView12.setTag(null);
        this.mboundView13 = (android.widget.TextView) bindings[13];
        this.mboundView13.setTag(null);
        this.mboundView14 = (android.widget.TextView) bindings[14];
        this.mboundView14.setTag(null);
        this.mboundView15 = (android.widget.TextView) bindings[15];
        this.mboundView15.setTag(null);
        this.mboundView16 = (android.widget.TextView) bindings[16];
        this.mboundView16.setTag(null);
        this.mboundView17 = (android.widget.TextView) bindings[17];
        this.mboundView17.setTag(null);
        this.mboundView18 = (android.widget.TextView) bindings[18];
        this.mboundView18.setTag(null);
        this.mboundView2 = (android.widget.TextView) bindings[2];
        this.mboundView2.setTag(null);
        this.mboundView3 = (android.widget.TextView) bindings[3];
        this.mboundView3.setTag(null);
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.mboundView6 = (android.widget.TextView) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView7 = (android.widget.ImageView) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView8 = (android.widget.ImageView) bindings[8];
        this.mboundView8.setTag(null);
        this.mboundView9 = (android.widget.ImageView) bindings[9];
        this.mboundView9.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x40000L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
        if (BR.owdevice == variableId) {
            setOwdevice((com.nanowheel.nanoux.nanowheel.model.OWDevice) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setOwdevice(@Nullable com.nanowheel.nanoux.nanowheel.model.OWDevice Owdevice) {
        updateRegistration(13, Owdevice);
        this.mOwdevice = Owdevice;
        synchronized(this) {
            mDirtyFlags |= 0x2000L;
        }
        notifyPropertyChanged(BR.owdevice);
        super.requestRebind();
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 1 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 2 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 3 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 4 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 5 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 6 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 7 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 8 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 9 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 10 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 11 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 12 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 13 :
                return onChangeOwdevice((com.nanowheel.nanoux.nanowheel.model.OWDevice) object, fieldId);
            case 14 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 15 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 16 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
            case 17 :
                return onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue((androidx.databinding.ObservableField<java.lang.String>) object, fieldId);
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x40L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x80L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x100L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x200L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x400L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x800L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdevice(com.nanowheel.nanoux.nanowheel.model.OWDevice Owdevice, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x4000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x8000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x10000L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue(androidx.databinding.ObservableField<java.lang.String> OwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x20000L;
            }
            return true;
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangObjectNullStringFormatJavaLangString34sOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangString = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusError = null;
        int owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueJavaLangStringTrueViewVISIBLEViewINVISIBLE = 0;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNullBooleanTrueOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNullJavaLangStringStringFormatJavaLangString34sFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = null;
        int owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueJavaLangStringTrueViewVISIBLEViewINVISIBLE = 0;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value = null;
        boolean owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueJavaLangStringTrue = false;
        boolean owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNull = false;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTemp = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1 = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValueGet = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueGet = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValueGet = null;
        java.util.Map<java.lang.String, com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic> owdeviceCharacteristics = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeed = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHours = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperature = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueGet = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCells = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValueGet = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValueGet = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTemp = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2 = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValueGet = null;
        float floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = 0f;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue = null;
        boolean owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueJavaLangStringTrue = false;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue = null;
        boolean owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNullBooleanTrueOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNull = false;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevision = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueGet = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueGet = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValueGet = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevision = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue = null;
        java.lang.String stringFormatJavaLangString34sFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValueGet = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpm = null;
        float floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = 0f;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValueGet = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueGet = null;
        boolean owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueJavaLangStringTrue = false;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue = null;
        boolean owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangObjectNull = false;
        java.lang.String stringFormatJavaLangString34sOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValueGet = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice owdevice = mOwdevice;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometer = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValueGet = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValueGet = null;
        float floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue = 0f;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHours = null;
        java.lang.String owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueGet = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value = null;
        int owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueJavaLangStringTrueViewVISIBLEViewINVISIBLE = 0;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemaining = null;
        boolean owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNull = false;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps = null;
        com.nanowheel.nanoux.nanowheel.model.OWDevice.DeviceCharacteristic owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltage = null;
        androidx.databinding.ObservableField<java.lang.String> owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue = null;

        if ((dirtyFlags & 0x7ffffL) != 0) {



                if (owdevice != null) {
                    // read owdevice.characteristics
                    owdeviceCharacteristics = owdevice.characteristics;
                }

            if ((dirtyFlags & 0x43000L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusError = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicStatusError);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusError != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusError.value;
                    }
                    updateRegistration(12, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValue.get();
                    }


                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError).value.get() == "true"
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueJavaLangStringTrue = (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueGet) == ("true");
                if((dirtyFlags & 0x43000L) != 0) {
                    if(owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueJavaLangStringTrue) {
                            dirtyFlags |= 0x400000L;
                    }
                    else {
                            dirtyFlags |= 0x200000L;
                    }
                }


                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueJavaLangStringTrueViewVISIBLEViewINVISIBLE = ((owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueJavaLangStringTrue) ? (android.view.View.VISIBLE) : (android.view.View.INVISIBLE));
            }
            if ((dirtyFlags & 0x42020L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryTemp)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTemp = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicBatteryTemp);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTemp != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryTemp).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTemp.value;
                    }
                    updateRegistration(5, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryTemp).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValue.get();
                    }
            }
            if ((dirtyFlags & 0x52000L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1)
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1 = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicPad1);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1 != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1).value
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1.value;
                    }
                    updateRegistration(16, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value);


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1).value.get()
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueGet = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1Value.get();
                    }


                    // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1).value.get() == "true"
                    owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueJavaLangStringTrue = (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueGet) == ("true");
                if((dirtyFlags & 0x52000L) != 0) {
                    if(owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueJavaLangStringTrue) {
                            dirtyFlags |= 0x4000000L;
                    }
                    else {
                            dirtyFlags |= 0x2000000L;
                    }
                }


                    // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
                    owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueJavaLangStringTrueViewVISIBLEViewINVISIBLE = ((owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueJavaLangStringTrue) ? (android.view.View.VISIBLE) : (android.view.View.INVISIBLE));
            }
            if ((dirtyFlags & 0x42200L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicSpeed)
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeed = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicSpeed);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeed != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicSpeed).value
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeed.value;
                    }
                    updateRegistration(9, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue);


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicSpeed).value.get()
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValueGet = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValue.get();
                    }
            }
            if ((dirtyFlags & 0x62000L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripTotalAmpHours)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHours = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicTripTotalAmpHours);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHours != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripTotalAmpHours).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHours.value;
                    }
                    updateRegistration(17, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripTotalAmpHours).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValue.get();
                    }
            }
            if ((dirtyFlags & 0x42010L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTemperature)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperature = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicTemperature);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperature != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTemperature).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperature.value;
                    }
                    updateRegistration(4, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTemperature).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValue.get();
                    }
            }
            if ((dirtyFlags & 0x42001L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryCells)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCells = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicBatteryCells);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCells != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryCells).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCells.value;
                    }
                    updateRegistration(0, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryCells).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValue.get();
                    }
            }
            if ((dirtyFlags & 0x42800L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicMotorTemp)
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTemp = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicMotorTemp);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTemp != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicMotorTemp).value
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTemp.value;
                    }
                    updateRegistration(11, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue);


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicMotorTemp).value.get()
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValueGet = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValue.get();
                    }
            }
            if ((dirtyFlags & 0x42002L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2)
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2 = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicPad2);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2 != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2).value
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2.value;
                    }
                    updateRegistration(1, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value);


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2).value.get()
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueGet = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2Value.get();
                    }


                    // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2).value.get() == "true"
                    owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueJavaLangStringTrue = (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueGet) == ("true");
                if((dirtyFlags & 0x42002L) != 0) {
                    if(owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueJavaLangStringTrue) {
                            dirtyFlags |= 0x40000000L;
                    }
                    else {
                            dirtyFlags |= 0x20000000L;
                    }
                }


                    // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
                    owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueJavaLangStringTrueViewVISIBLEViewINVISIBLE = ((owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueJavaLangStringTrue) ? (android.view.View.VISIBLE) : (android.view.View.INVISIBLE));
            }
            if ((dirtyFlags & 0x46000L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicFirmwareRevision)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevision = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicFirmwareRevision);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevision != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicFirmwareRevision).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevision.value;
                    }
                    updateRegistration(14, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicFirmwareRevision).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValue.get();
                    }
            }
            if ((dirtyFlags & 0x42400L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicHardwareRevision)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevision = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicHardwareRevision);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevision != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicHardwareRevision).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevision.value;
                    }
                    updateRegistration(10, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicHardwareRevision).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValue.get();
                    }
            }
            if ((dirtyFlags & 0x42004L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicSpeedRpm)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpm = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicSpeedRpm);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpm != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicSpeedRpm).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpm.value;
                    }
                    updateRegistration(2, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicSpeedRpm).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValue.get();
                    }
            }
            if ((dirtyFlags & 0x42008L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer)
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometer = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicOdometer);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometer != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometer.value;
                    }
                    updateRegistration(3, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue);


                    if (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue != null) {
                        // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get()
                        owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueGet = owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue.get();
                    }


                    // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get() != null
                    owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangObjectNull = (owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueGet) != (null);
                if((dirtyFlags & 0x42008L) != 0) {
                    if(owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangObjectNull) {
                            dirtyFlags |= 0x100000L;
                    }
                    else {
                            dirtyFlags |= 0x80000L;
                    }
                }
            }
            if ((dirtyFlags & 0x4a000L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripRegenAmpHours)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHours = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicTripRegenAmpHours);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHours != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripRegenAmpHours).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHours.value;
                    }
                    updateRegistration(15, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripRegenAmpHours).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValue.get();
                    }
            }
            if ((dirtyFlags & 0x42100L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryRemaining)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemaining = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicBatteryRemaining);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemaining != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryRemaining).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemaining.value;
                    }
                    updateRegistration(8, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryRemaining).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValue.get();
                    }
            }
            if ((dirtyFlags & 0x42080L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicCurrentAmps);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps.value;
                    }
                    updateRegistration(7, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue.get();
                    }
            }
            if ((dirtyFlags & 0x420c0L) != 0) {

                    if (owdeviceCharacteristics != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage)
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltage = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicBatteryVoltage);
                    }


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltage != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltage.value;
                    }
                    updateRegistration(6, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue);


                    if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue != null) {
                        // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get()
                        owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue.get();
                    }


                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() == null
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNull = (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueGet) == (null);
                if((dirtyFlags & 0x420c0L) != 0) {
                    if(owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNull) {
                            dirtyFlags |= 0x10000000L;
                    }
                    else {
                            dirtyFlags |= 0x8000000L;
                    }
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x100000L) != 0) {

                // read String.format("%3.4s", owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get())
                stringFormatJavaLangString34sOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue = java.lang.String.format("%3.4s", owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueGet);
        }
        if ((dirtyFlags & 0x8000000L) != 0) {

                if (owdeviceCharacteristics != null) {
                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps)
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicCurrentAmps);
                }


                if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps != null) {
                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps.value;
                }
                updateRegistration(7, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue);


                if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue != null) {
                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get()
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue.get();
                }


                // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get() == null
                owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNull = (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueGet) == (null);
        }

        if ((dirtyFlags & 0x42008L) != 0) {

                // read owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get() != null ? String.format("%3.4s", owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get()) : " "
                owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangObjectNullStringFormatJavaLangString34sOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangString = ((owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangObjectNull) ? (stringFormatJavaLangString34sOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValue) : (" "));
        }
        if ((dirtyFlags & 0x420c0L) != 0) {

                // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() == null ? true : owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get() == null
                owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNullBooleanTrueOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNull = ((owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNull) ? (true) : (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNull));
            if((dirtyFlags & 0x420c0L) != 0) {
                if(owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNullBooleanTrueOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNull) {
                        dirtyFlags |= 0x1000000L;
                }
                else {
                        dirtyFlags |= 0x800000L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x800000L) != 0) {

                // read Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get())
                floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue = java.lang.Float.parseFloat(owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueGet);
                if (owdeviceCharacteristics != null) {
                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps)
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps = owdeviceCharacteristics.get(com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicCurrentAmps);
                }


                if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps != null) {
                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmps.value;
                }
                updateRegistration(7, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue);


                if (owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue != null) {
                    // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get()
                    owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueGet = owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue.get();
                }


                // read Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get())
                floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = java.lang.Float.parseFloat(owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueGet);


                // read (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get())) * (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get()))
                floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = (floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValue) * (floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue);


                // read String.format("%3.4s", (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get())) * (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get())))
                stringFormatJavaLangString34sFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = java.lang.String.format("%3.4s", floatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue);
        }

        if ((dirtyFlags & 0x420c0L) != 0) {

                // read owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() == null ? true : owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get() == null ? " " : String.format("%3.4s", (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get())) * (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get())))
                owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNullBooleanTrueOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNullJavaLangStringStringFormatJavaLangString34sFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue = ((owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNullBooleanTrueOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNull) ? (" ") : (stringFormatJavaLangString34sFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue));
        }
        // batch finished
        if ((dirtyFlags & 0x42200L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicSpeedValueGet);
        }
        if ((dirtyFlags & 0x420c0L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView10, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueJavaLangObjectNullBooleanTrueOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueJavaLangObjectNullJavaLangStringStringFormatJavaLangString34sFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueFloatParseFloatOwdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValue);
        }
        if ((dirtyFlags & 0x62000L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView11, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripTotalAmpHoursValueGet);
        }
        if ((dirtyFlags & 0x4a000L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView12, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTripRegenAmpHoursValueGet);
        }
        if ((dirtyFlags & 0x42001L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView13, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryCellsValueGet);
        }
        if ((dirtyFlags & 0x42010L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView14, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicTemperatureValueGet);
        }
        if ((dirtyFlags & 0x42800L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView15, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicMotorTempValueGet);
        }
        if ((dirtyFlags & 0x42020L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView16, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryTempValueGet);
        }
        if ((dirtyFlags & 0x42400L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView17, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicHardwareRevisionValueGet);
        }
        if ((dirtyFlags & 0x46000L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView18, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicFirmwareRevisionValueGet);
        }
        if ((dirtyFlags & 0x42004L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView2, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicSpeedRpmValueGet);
        }
        if ((dirtyFlags & 0x42008L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView3, owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangObjectNullStringFormatJavaLangString34sOwdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicOdometerValueJavaLangString);
        }
        if ((dirtyFlags & 0x42100L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView4, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryRemainingValueGet);
        }
        if ((dirtyFlags & 0x42040L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView5, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicBatteryVoltageValueGet);
        }
        if ((dirtyFlags & 0x42080L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView6, owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicCurrentAmpsValueGet);
        }
        if ((dirtyFlags & 0x52000L) != 0) {
            // api target 1

            this.mboundView7.setVisibility(owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad1ValueJavaLangStringTrueViewVISIBLEViewINVISIBLE);
        }
        if ((dirtyFlags & 0x42002L) != 0) {
            // api target 1

            this.mboundView8.setVisibility(owdeviceCharacteristicsGetOwdeviceMockOnewheelCharacteristicPad2ValueJavaLangStringTrueViewVISIBLEViewINVISIBLE);
        }
        if ((dirtyFlags & 0x43000L) != 0) {
            // api target 1

            this.mboundView9.setVisibility(owdeviceCharacteristicsGetOwdeviceOnewheelCharacteristicStatusErrorValueJavaLangStringTrueViewVISIBLEViewINVISIBLE);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryCells).value
        flag 1 (0x2L): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2).value
        flag 2 (0x3L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicSpeedRpm).value
        flag 3 (0x4L): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value
        flag 4 (0x5L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTemperature).value
        flag 5 (0x6L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryTemp).value
        flag 6 (0x7L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value
        flag 7 (0x8L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value
        flag 8 (0x9L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryRemaining).value
        flag 9 (0xaL): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicSpeed).value
        flag 10 (0xbL): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicHardwareRevision).value
        flag 11 (0xcL): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicMotorTemp).value
        flag 12 (0xdL): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError).value
        flag 13 (0xeL): owdevice
        flag 14 (0xfL): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicFirmwareRevision).value
        flag 15 (0x10L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripRegenAmpHours).value
        flag 16 (0x11L): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1).value
        flag 17 (0x12L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicTripTotalAmpHours).value
        flag 18 (0x13L): null
        flag 19 (0x14L): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get() != null ? String.format("%3.4s", owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get()) : " "
        flag 20 (0x15L): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get() != null ? String.format("%3.4s", owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicOdometer).value.get()) : " "
        flag 21 (0x16L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
        flag 22 (0x17L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicStatusError).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
        flag 23 (0x18L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() == null ? true : owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get() == null ? " " : String.format("%3.4s", (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get())) * (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get())))
        flag 24 (0x19L): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() == null ? true : owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get() == null ? " " : String.format("%3.4s", (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get())) * (Float.parseFloat(owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get())))
        flag 25 (0x1aL): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
        flag 26 (0x1bL): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad1).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
        flag 27 (0x1cL): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() == null ? true : owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get() == null
        flag 28 (0x1dL): owdevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryVoltage).value.get() == null ? true : owdevice.characteristics.get(OWDevice.OnewheelCharacteristicCurrentAmps).value.get() == null
        flag 29 (0x1eL): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
        flag 30 (0x1fL): owdevice.characteristics.get(OWDevice.MockOnewheelCharacteristicPad2).value.get() == "true" ? View.VISIBLE : View.INVISIBLE
    flag mapping end*/
    //end
}