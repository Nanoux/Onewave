package com.nanowheel.nanoux.nanowheel;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;

import java.util.Locale;

import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;

import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicTiltAnglePitch;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicTiltAngleRoll;

@SuppressWarnings("ConstantConditions")
public class FragmentDiagnotics extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private com.nanowheel.nanoux.nanowheel.databinding.FragmentDiagnoticsBinding mBinding;
    private View mView;
    private Observable.OnPropertyChangedCallback OWcallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (BluetoothService.isOWDevice.get().equals("true")) {
                setBinding();
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_diagnotics,container,false);
        //mBinding.setOwdevice(BluetoothService.mService.mOWDevice);
        //View view = inflater.inflate(R.layout.fragment_diagnotics, container, false);

        mView = mBinding.getRoot();

        if (BluetoothService.mService != null) {
            setBinding();
        }
        BluetoothService.isOWDevice.addOnPropertyChangedCallback(OWcallback);
        SharedPreferencesUtil test = SharedPreferencesUtil.getPrefs(getActivity());
        test.registerListener(this);

        displaySpeedRecord();
        displayDistanceRecord();
        displayDistanceTotal();

        setupCallbacks();

        //BluetoothService.getBluetoothUtil().refreshDiagnotics();

        BluetoothService.isOWDevice.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (BluetoothService.isOWDevice.get().equals("true")){
                    setBinding();
                }
            }
        });

        mView.findViewById(R.id.refresh_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothService.getBluetoothUtil().refreshSomeDiagnotics();
            }
        });

        return mBinding.getRoot();
        //return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mView = null;
        BluetoothService.getBluetoothUtil().killDiagnotics();
        BluetoothService.isOWDevice.removeOnPropertyChangedCallback(OWcallback);
    }

    @Override
    public void onResume() {
        super.onResume();
        BluetoothService.getBluetoothUtil().refreshSomeDiagnotics();
    }

    @Override
    public void onPause() {
        super.onPause();
        BluetoothService.getBluetoothUtil().killDiagnotics();
    }

    private void setBinding(){
        if (mView != null && mBinding != null){
            mBinding.setOwdevice(BluetoothService.mService.mOWDevice);
        }
    }

    private void displaySpeedRecord(){
        if (mView != null) {
            final TextView speed = mView.findViewById(R.id.speed_record);
            speed.post(new Runnable() {
                @Override
                public void run() {
                    if(SharedPreferencesUtil.getPrefs(getContext()).isMetric()) {
                        speed.setText(String.format(Locale.getDefault(), "%.2f",Util.rpmToKilometersPerHour(SharedPreferencesUtil.getPrefs(mView.getContext()).getSpeedRecord(),0)));
                    }else{
                        speed.setText(String.format(Locale.getDefault(), "%.2f",Util.rpmToMilesPerHour(SharedPreferencesUtil.getPrefs(mView.getContext()).getSpeedRecord(),0)));
                    }
                }
            });
        }
    }
    private void displayDistanceRecord(){
        if (mView != null) {
            final TextView distance = mView.findViewById(R.id.distance_record);
            distance.post(new Runnable() {
                @Override
                public void run() {
                    if(SharedPreferencesUtil.getPrefs(getContext()).isMetric()) {
                        distance.setText(String.format(Locale.getDefault(), "%.2f",Util.revolutionsToKilometers(SharedPreferencesUtil.getPrefs(mView.getContext()).getDistanceRecord(),0)));
                    }else{
                        distance.setText(String.format(Locale.getDefault(), "%.2f",Util.revolutionsToMiles(SharedPreferencesUtil.getPrefs(mView.getContext()).getDistanceRecord(),0)));
                    }
                }
            });
        }
    }
    private void displayDistanceTotal() {
        if (mView != null) {
            final TextView distance = mView.findViewById(R.id.distance_total);
            distance.post(new Runnable() {
                @Override
                public void run() {
                    if (SharedPreferencesUtil.getPrefs(getContext()).isMetric()) {
                        distance.setText(String.format(Locale.getDefault(), "%60.0f", Util.milesToKilometers(SharedPreferencesUtil.getPrefs(mView.getContext()).getDistanceTotal())));
                    } else {
                        distance.setText(String.format(Locale.getDefault(), "%60.0f", SharedPreferencesUtil.getPrefs(mView.getContext()).getDistanceTotal() + 0f));
                    }
                }
            });
        }
    }

    private void setupCallbacks(){
        if (BluetoothService.mService == null){
            return;
        }
        BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicTiltAnglePitch).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int raw = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicTiltAnglePitch).value.get());
                final float angle;
                if (raw <= 1800){
                    angle = (1800f - raw) / 10f;
                }else {
                    angle = (raw - 1800f) / 10f * -1f;
                }
                if (mView != null) {
                    final TextView pitch = mView.findViewById(R.id.pitch_text);
                    pitch.post(new Runnable() {
                        @Override
                        public void run() {
                            pitch.setText(angle + "");
                        }
                    });
                }

                float imgAngle = angle;
                if (imgAngle < 0){
                    imgAngle += 360f;
                }
                final float imgAngleF = imgAngle;
                if (mView != null) {
                    final ImageView pitchI = mView.findViewById(R.id.pitch);
                    pitchI.post(new Runnable() {
                        @Override
                        public void run() {
                            pitchI.setRotation(imgAngleF);
                        }
                    });
                }
            }
        });
        BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicTiltAngleRoll).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int raw = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicTiltAngleRoll).value.get());
                final float angle;
                if (raw <= 1800){
                    angle = (1800f - raw) / 10f;
                }else {
                    angle = (raw - 1800f) / 10f * -1f;
                }
                if (mView != null) {
                    final TextView roll = mView.findViewById(R.id.roll_text);
                    roll.post(new Runnable() {
                        @Override
                        public void run() {
                            roll.setText(angle + "");
                        }
                    });
                }

                float imgAngle = angle;
                if (imgAngle < 0){
                    imgAngle += 360f;
                }
                final float imgAngleF = imgAngle;
                if (mView != null) {
                    final ImageView rollI = mView.findViewById(R.id.roll);
                    rollI.post(new Runnable() {
                        @Override
                        public void run() {
                            rollI.setRotation(imgAngleF);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case SharedPreferencesUtil.STATUS_MODE:
                int hi = SharedPreferencesUtil.getPrefs(getContext()).getStatusMode();
                if (hi == 2 && mView != null){
                    BluetoothService.getBluetoothUtil().refreshSomeDiagnotics();
                }
                break;

            case SharedPreferencesUtil.RECORD_DIST:
                displayDistanceRecord();
                break;
            case SharedPreferencesUtil.RECORD_SPEED:
                displaySpeedRecord();
                break;
            case SharedPreferencesUtil.TOTAL_DIST:
                displayDistanceRecord();
                break;
        }

    }
}
