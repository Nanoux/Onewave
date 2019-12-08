package com.nanowheel.nanoux.nanowheel;

import android.content.DialogInterface;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.nanowheel.nanoux.nanowheel.model.OWDevice;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.BluetoothUtilImpl;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;

import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicOdometerCharge;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicOdometerRange;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicMaxSpeed;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicOdometer;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.MockOnewheelCharacteristicSpeed;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicBatteryRemaining;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicLifetimeOdometer;
import static com.nanowheel.nanoux.nanowheel.model.OWDevice.OnewheelCharacteristicRidingMode;

@SuppressWarnings("ConstantConditions")
public class FragmentOverview extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private View viewFrag;
    private Observable.OnPropertyChangedCallback OWcallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            if (BluetoothService.isOWDevice.get().equals("true")) {
                setupUI();
                setStatusText();
            }
        }
    };
    private Observable.OnPropertyChangedCallback foundCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable sender, int propertyId) {
            setStatusText();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_overview, container, false);

        viewFrag = view;

        SharedPreferencesUtil test = SharedPreferencesUtil.getPrefs(getActivity());
        test.registerListener(this);

        updateScanUI(false);

        if (BluetoothService.mService != null) {
            setupUI();
        }
        setStatusText();
        BluetoothService.isOWDevice.addOnPropertyChangedCallback(OWcallback);
        BluetoothUtilImpl.isOWFound.addOnPropertyChangedCallback(foundCallback);

        view.findViewById(R.id.scanButton).setOnClickListener(mOnClickListener);
        view.findViewById(R.id.cancelButton).setOnClickListener(mOnClickListener2);
        view.findViewById(R.id.rideModeLayout).setOnClickListener(dialogListener);
        view.findViewById(R.id.buttonCustomMode).setOnClickListener(dialogListener2);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BluetoothService.isOWDevice.removeOnPropertyChangedCallback(OWcallback);
        BluetoothUtilImpl.isOWFound.removeOnPropertyChangedCallback(foundCallback);
    }

    private void setupUI() {
        if (viewFrag == null) {
            return;
        }
        final View view = viewFrag;
        BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicSpeed).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.speedChange(view, R.id.speedText, R.id.speedometer);
            }
        });

        BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicRidingMode).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.modeChange(view, R.id.textMode, R.id.textModeSpeed);
            }
        });
        BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicBatteryRemaining).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.batteryChange(view, R.id.batteryText, R.id.batteryBar);
            }
        });

        BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicMaxSpeed).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.maxSpeedChange(view, R.id.speedTop);
            }
        });
        BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicOdometer).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.odometerChange(view, R.id.textViewTripNum);
            }
        });
        BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicOdometerCharge).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.chargeOdometerChange(view, R.id.textViewChargeNum);
            }
        });
        BluetoothService.mService.mOWDevice.characteristics.get(MockOnewheelCharacteristicOdometerRange).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.rangeOdometerChange(view, R.id.textViewRangeNum);
            }
        });
        BluetoothService.mService.mOWDevice.characteristics.get(OnewheelCharacteristicLifetimeOdometer).value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BluetoothService.mService.mOWDevice.lifeOdometerChange(view, R.id.textViewTotalNum);
            }
        });

        refreshUI(view);


        /*TextView test = view.findViewById(R.id.batteryText);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NotificationBuilder.test(view.getContext());
            }
        });*/
    }

    private void setStatusText(){
        if (BluetoothUtilImpl.isOWFound.get() != null && BluetoothUtilImpl.isOWFound.get().equals("true")) {
            final TextView text = viewFrag.findViewById(R.id.statusText);
            text.post(new Runnable() {
                @Override
                public void run() {
                    text.setText("OW Found, Connecting...");
                }
            });
        }else{
            final TextView text = viewFrag.findViewById(R.id.statusText);
            text.post(new Runnable() {
                @Override
                public void run() {
                    text.setText("Scanning...");
                }
            });
        }
    }

    private void refreshUI(View view) {
        BluetoothService.mService.mOWDevice.speedChange(view, R.id.speedText, R.id.speedometer);
        BluetoothService.mService.mOWDevice.modeChange(view, R.id.textMode, R.id.textModeSpeed);
        BluetoothService.mService.mOWDevice.batteryChange(view, R.id.batteryText, R.id.batteryBar);
        BluetoothService.mService.mOWDevice.maxSpeedChange(view, R.id.speedTop);
        BluetoothService.mService.mOWDevice.odometerChange(view, R.id.textViewTripNum);
        BluetoothService.mService.mOWDevice.chargeOdometerChange(view, R.id.textViewChargeNum);
        BluetoothService.mService.mOWDevice.rangeOdometerChange(view, R.id.textViewRangeNum);
        BluetoothService.mService.mOWDevice.lifeOdometerChange(view, R.id.textViewTotalNum);
        BluetoothService.mService.mOWDevice.unitsChange(view, R.id.speedUnits);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(SharedPreferencesUtil.STATUS_MODE)) {
            //int hi = sharedPreferences.getInt(key, 0);
            updateScanUI(true);

        }

    }

    private void updateScanUI(boolean animate) {
        int status = SharedPreferencesUtil.getPrefs(getContext()).getStatusMode();

        final View start = viewFrag.findViewById(R.id.startlayout);
        final View scan = viewFrag.findViewById(R.id.scanlayout);
        final View main = viewFrag.findViewById(R.id.mainlayout);

        Animation in;
        Animation out;
        if (getActivity() != null) {
            in = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
            out = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        } else {
            animate = false;
            in = null;
            out = null;
        }

        if (status == 0) {
            if (animate) {
                fadeLayouts(start, scan, main, in, out);
            } else {
                start.setVisibility(View.VISIBLE);
                scan.setVisibility(View.INVISIBLE);
                main.setVisibility(View.INVISIBLE);
            }
        } else if (status == 1) {
            if (animate) {
                fadeLayouts(scan, start, main, in, out);
            } else {
                start.setVisibility(View.INVISIBLE);
                scan.setVisibility(View.VISIBLE);
                main.setVisibility(View.INVISIBLE);
            }
        } else {
            if (animate) {
                fadeLayouts(main, scan, start, in, out);
            } else {
                start.setVisibility(View.INVISIBLE);
                scan.setVisibility(View.INVISIBLE);
                main.setVisibility(View.VISIBLE);
            }
        }
    }

    private static void fadeLayouts(View on, final View off1, final View off2, Animation in, Animation out) {
        on.setVisibility(View.VISIBLE);
        on.startAnimation(in);
        if (off1.getVisibility() == View.VISIBLE)
            off1.startAnimation(out);
        if (off2.getVisibility() == View.VISIBLE)
            off2.startAnimation(out);
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                off1.setVisibility(View.INVISIBLE);
                off2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            BluetoothService.createService(v.getContext());
            setStatusText();
        }
    };
    private View.OnClickListener mOnClickListener2 = new View.OnClickListener() {
        public void onClick(View v) {
            BluetoothService.killService(v.getContext(), false);
        }
    };

    private View.OnClickListener dialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View view2) {
            //double mph = Util.rpmToMilesPerHour(mOWDevice.speedRpm.get());
            //if (mph < 4) {
            if (BluetoothService.mService.mOWDevice.onewheelType.get() == 1) {
                DialogRideMode bottomSheetFragment = new DialogRideMode();
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            } else if (BluetoothService.mService.mOWDevice.onewheelType.get() == 0){
                DialogRideModeOld bottomSheetFragment = new DialogRideModeOld();
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            }else{
                DialogRideModePint bottomSheetFragment = new DialogRideModePint();
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
            /*}else{
                Toast.makeText(getActivity(),"Can't Change Ridemode at speed",Toast.LENGTH_LONG).show();
            }*/
        }
    };
    /*

    Carvability - 1
    -5 to 5
    -100 to 100

    Stance - 0
    -1.0 to 3.0
    -20 to 60

    Aggressiveness - 2
    1 to 11
    -80 to 127


     */

    private View.OnClickListener dialogListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view2) {

            //BluetoothService.mService.testing();

            LayoutInflater inflator = getLayoutInflater();
            View diagView = inflator.inflate(R.layout.dialog_custom_ridemode,null);

            final SeekBar barStance = diagView.findViewById(R.id.bar_stance);
            final SeekBar barCarvability = diagView.findViewById(R.id.bar_carvability);
            final SeekBar barAggressiveness = diagView.findViewById(R.id.bar_aggressiveness);

            final TextView textStance = diagView.findViewById(R.id.value_stance);
            final TextView textCarvability = diagView.findViewById(R.id.value_carvability);
            final TextView textAggressiveness = diagView.findViewById(R.id.value_aggressiveness);

            int valStance = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicStance).value.get());
            int valCarvability = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicCarvability).value.get());
            int valAggressiveness = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicAggressiveness).value.get());

            barStance.setProgress(Math.round(valStance/4f)+5);
            barCarvability.setProgress(Math.round(valCarvability/10f)+10);
            barAggressiveness.setProgress(Math.round(valAggressiveness/10f)+8);

            barStance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textStance.setText(((progress*4-20)/20f)+"");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            barCarvability.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    textCarvability.setText(((progress*10)-100)+"");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            barAggressiveness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    int val = ((progress*10)-80);
                    if (val >= 120){
                        val = 127;
                    }
                    textAggressiveness.setText(val+"");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            textStance.setText((valStance/20f)+"");
            textCarvability.setText(valCarvability+"");
            textAggressiveness.setText(valAggressiveness+"");

            new AlertDialog.Builder(getContext())
                    //.setTitle("Edit Custom Ridemode")

                    .setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int resStance = ((barStance.getProgress()*4-20));
                            int resCarvability = (barCarvability.getProgress()*10)-100;
                            int resAggressiveness = (barAggressiveness.getProgress()*10)-80;

                            if (resAggressiveness >= 120){
                                resAggressiveness = 127;
                            }

                            BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicStance).value.set(resStance+"");
                            BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicCarvability).value.set(resCarvability+"");
                            BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.MockOnewheelCharacteristicAggressiveness).value.set(resAggressiveness+"");

                            BluetoothService.getBluetoothUtil().writeCustomRidemode();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .setView(diagView)
                    .show();

        };

    };

}
