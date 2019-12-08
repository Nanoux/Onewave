package com.nanowheel.nanoux.nanowheel.TripDatabase;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.nanowheel.nanoux.nanowheel.R;

public class FragmentMap extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map,null);

        Button but = view.findViewById(R.id.button_plug);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.nanowheel.nanoux.nanowheel.pro"));
                try{
                    startActivity(intent);
                }
                catch(Exception e){
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.nanowheel.nanoux.nanowheel.pro"));
                }
            }
        });

        return view;
    }
}
