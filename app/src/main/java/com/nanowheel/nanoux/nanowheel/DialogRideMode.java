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
public class DialogRideMode extends BottomSheetDialogFragment {
    public DialogRideMode() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

            view.findViewById(R.id.sheet_sequoia).setOnClickListener(modeListener4);
            view.findViewById(R.id.sheet_cruz).setOnClickListener(modeListener5);
            view.findViewById(R.id.sheet_mission).setOnClickListener(modeListener6);
            view.findViewById(R.id.sheet_elevated).setOnClickListener(modeListener7);
            view.findViewById(R.id.sheet_delirium).setOnClickListener(modeListener8);
            view.findViewById(R.id.sheet_custom).setOnClickListener(modeListener9);

        return view;
    }




    private View.OnClickListener modeListener4 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(),4);
            getDialog().dismiss();
        }
    };
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
    private View.OnClickListener modeListener9 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.OnewheelCharacteristicFirmwareRevision).value.get()) >= 4034) {
                BluetoothService.mService.mOWDevice.setRideMode(BluetoothService.getBluetoothUtil(), 9);
            }else{
                Toast.makeText(getContext(),"Custom Ridemode only available on Gemini Firmware.",Toast.LENGTH_LONG).show();
            }
            getDialog().dismiss();
        }
    };
}
