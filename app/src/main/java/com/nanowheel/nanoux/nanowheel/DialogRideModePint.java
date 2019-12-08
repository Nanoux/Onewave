package com.nanowheel.nanoux.nanowheel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.nanowheel.nanoux.nanowheel.model.OWDevice;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.Util;

@SuppressWarnings("ConstantConditions")
public class DialogRideModePint extends BottomSheetDialogFragment {

    public DialogRideModePint() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_pint, container, false);

        view.findViewById(R.id.sheet_redwood).setOnClickListener(modeListener5);
        view.findViewById(R.id.sheet_pacific).setOnClickListener(modeListener6);
        view.findViewById(R.id.sheet_elevated).setOnClickListener(modeListener7);
        view.findViewById(R.id.sheet_skyline).setOnClickListener(modeListener8);

        return view;
    }

    private View.OnClickListener modeListener5 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),5);
            getDialog().dismiss();
        }
    };
    private View.OnClickListener modeListener6 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),6);
            getDialog().dismiss();
        }
    };
    private View.OnClickListener modeListener7 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),7);
            getDialog().dismiss();
        }
    };
    private View.OnClickListener modeListener8 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),8);
            getDialog().dismiss();
        }
    };
}
