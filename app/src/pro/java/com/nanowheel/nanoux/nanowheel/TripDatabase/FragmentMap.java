package com.nanowheel.nanoux.nanowheel.TripDatabase;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.nanowheel.nanoux.nanowheel.R;
import com.nanowheel.nanoux.nanowheel.util.SharedPreferencesUtil;
import com.nanowheel.nanoux.nanowheel.util.Util;

import java.util.Iterator;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProviders;

public class FragmentMap extends Fragment implements OnMapReadyCallback{

    private static MapView mMapView;
    View mView;
    static GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private int DEFAULT_ZOOM = 12;

    private static TripViewModel mTripViewModel;
    public static SelectionTracker mSelectionTracker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mView = rootView;

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        mMapView = (MapView) rootView.findViewById(R.id.mapView);

        if (googleMap == null) {
            mMapView.onCreate(savedInstanceState);
        }
        mMapView.getMapAsync(this);

        mMapView.onResume(); // needed to get the map to display immediately //maybe

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerview);
        final TripListAdapter adapter = new TripListAdapter(getActivity(),this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        TripKeyProvider keyProvider = new TripKeyProvider(recyclerView);


        mSelectionTracker = new SelectionTracker.Builder(
                "trip selector",
                recyclerView,
                keyProvider,
                new TripListAdapter.TripDetailsLookup(recyclerView),
                StorageStrategy.createLongStorage())
                .withSelectionPredicate(new TripListAdapter.Predicate())
                .build();

        mSelectionTracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onSelectionChanged() {
                if (mSelectionTracker.hasSelection()){
                    showBar();
                }else{
                    hideBar();
                }
            }
        });

        mTripViewModel = ViewModelProviders.of(this).get(TripViewModel.class);

        mTripViewModel.getAllTrips().observe(this, new Observer<List<Trip>>() {
            @Override
            public void onChanged(@Nullable final List<Trip> trips) {
                // Update the cached copy of the words in the adapter.
                adapter.setTrips(trips);
            }
        });

        recyclerView.addOnScrollListener(TripListAdapter.scrollListen);



        if (!SharedPreferencesUtil.getPrefs(getContext()).isMap()){
            MaterialCardView lay = rootView.findViewById(R.id.warningBanner);
            lay.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public void mapWarning(boolean isLog){
        if (!SharedPreferencesUtil.getPrefs(getContext()).isMap()){
            MaterialCardView lay = mView.findViewById(R.id.warningBanner);
            lay.setVisibility(View.VISIBLE);
        }else{
            MaterialCardView lay = mView.findViewById(R.id.warningBanner);
            lay.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onMapReady(GoogleMap mMap){
        googleMap = mMap;


        setDeviceLocation();

        int style;
        if (Util.isDark(getContext())){
            style = R.raw.style_dark;
        }else{
            style = R.raw.style_light;
        }
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), style));
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        mView = null;
        mMapView = null;
        googleMap = null;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void setDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null && googleMap != null) {
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(location.getLatitude(),
                                                location.getLongitude()), DEFAULT_ZOOM));
                            }
                        }
                    });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void showBar(){
        RelativeLayout header = mView.findViewById(R.id.header);
        if (header.getVisibility() == View.INVISIBLE) {
            Animation in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
            header.startAnimation(in);
        }
        header.setVisibility(View.VISIBLE);

        View.OnClickListener backListen = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideBar();
                mSelectionTracker.clearSelection();
            }
        };

        ImageButton back = mView.findViewById(R.id.back);
        back.setOnClickListener(backListen);

        View.OnClickListener delListen = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mView.getContext())
                        .setTitle("Delete Trips")
                        .setMessage("Would you really like to delete these trips?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                hideBar();
                                Iterator iterator = mSelectionTracker.getSelection().iterator();
                                while (iterator.hasNext()){
                                    long hello = (Long)iterator.next();
                                    long stamp = mTripViewModel.getAllTrips().getValue().get(mTripViewModel.getAllTrips().getValue().size() -1 - (int)hello).getTimeStamp();
                                    Trip del = new Trip();
                                    del.setTimeStamp(stamp);
                                    mTripViewModel.delTrip(del);
                                }
                                mSelectionTracker.clearSelection();
                                TripListAdapter.showLines((RecyclerView)mView.findViewById(R.id.recyclerview));
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        };

        ImageButton del = mView.findViewById(R.id.delete);
        del.setOnClickListener(delListen);
    }

    public void hideBar(){
        if (mView != null) {
            final RelativeLayout header = mView.findViewById(R.id.header);
            Animation out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
            out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    header.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            header.startAnimation(out);
        }
    }
}
