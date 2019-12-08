package com.nanowheel.nanoux.nanowheel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;

@SuppressWarnings("ConstantConditions")
public class DialogRideModeOld extends BottomSheetDialogFragment {
    public DialogRideModeOld() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_old, container, false);

        view.findViewById(R.id.sheet_classic_old).setOnClickListener(modeListener1);
        view.findViewById(R.id.sheet_extreme_old).setOnClickListener(modeListener2);
        view.findViewById(R.id.sheet_elevated_old).setOnClickListener(modeListener3);

        return view;
    }
    private View.OnClickListener modeListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),1);
            getDialog().dismiss();
        }
    };
    private View.OnClickListener modeListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),2);
            getDialog().dismiss();
        }
    };
    private View.OnClickListener modeListener3 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),3);
            getDialog().dismiss();
        }
    };
}
