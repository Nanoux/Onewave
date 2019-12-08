package com.nanowheel.nanoux.nanowheel.TripDatabase;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.nanowheel.nanoux.nanowheel.model.OWDevice;
import com.nanowheel.nanoux.nanowheel.util.BluetoothService;
import com.nanowheel.nanoux.nanowheel.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class TripRepository {

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private ArrayList<com.google.android.gms.maps.model.LatLng> positions;
    private ArrayList<Integer> levels;
    private ArrayList<Integer> revs;
    private ArrayList<Long> times;
    private long epoch = -1;
    private int revEpoch = -1;
    //public static AppDatabase appDatabase;

    private LatLng lastPos = new LatLng(0,0);

    private TripDAO tripDAO;
    private LiveData<List<Trip>> trips;
    private Context context;

    public TripRepository(Application application){
        AppDatabase db = AppDatabase.getDatabase(application);
        tripDAO = db.tripDAO();
        trips = tripDAO.getTrips();
        context = application.getApplicationContext();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application.getApplicationContext());

    }
    private void createLocationRequest() {
        final LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        final LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                lastPos = new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude());
            }
        };

        task.addOnSuccessListener( new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                if (!(Looper.myLooper() != null && (Build.VERSION.SDK_INT < Build.VERSION_CODES.M||Looper.myLooper().isCurrentThread()))){
                    Looper.prepare();
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback,
                                null /* Looper */);
                    }
                }else{
                    mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                            mLocationCallback,
                            null /* Looper */);
                }
            }
        });
    }

    private void stopLocationUpdates() {
        final LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                lastPos = new LatLng(locationResult.getLastLocation().getLatitude(),locationResult.getLastLocation().getLongitude());
            }
        };
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    LiveData<List<Trip>> getTrips(){
        return trips;
    }

    void addTrip (Trip trip){
        new insertAsyncTask(tripDAO,0).execute(trip);
    }
    void updateTrip (Trip trip){
        new insertAsyncTask(tripDAO,1).execute(trip);
    }
    void delTrip (Trip trip){
        new insertAsyncTask(tripDAO,2).execute(trip);
    }
    void delAll (){
        new insertAsyncTask(tripDAO,3).execute(new Trip());
    }

    public void startLog(){
        if (epoch < 0) {
            createLocationRequest();

            positions = new ArrayList<>();
            levels = new ArrayList<>();
            revs = new ArrayList<>();
            times = new ArrayList<>();
            epoch = System.currentTimeMillis();

            Trip trip = new Trip();
            trip.setTimeStamp(epoch);
            trip.setPositions(positions);
            trip.setLevels(levels);
            trip.setPositions(positions);
            trip.setTimes(times);
            trip.setRevStamp(revEpoch);

            addTrip(trip);
        }
    }

    public void markLog(){
        final int battery = Util.parseI(BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.OnewheelCharacteristicBatteryRemaining).value.get());
        String dist = BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.OnewheelCharacteristicOdometer).value.get();
        final int sp;
        if (dist != null) {
            sp = Util.parseI(dist);
        }else{
            sp = 0;
        }
        /*try {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        markLog(battery,sp,new LatLng(location.getLatitude(), location.getLongitude()));
                    }
                }
            });
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
            markLog(battery,sp,new LatLng(0,0));
        }*/
        markLog(battery,sp,lastPos);
    }

    private void markLog(int battery, int sp, LatLng lat){
        levels.add(battery);
        revs.add(sp);
        times.add(System.currentTimeMillis() - epoch);
        positions.add(lat);

        Trip trip = new Trip();
        trip.setTimeStamp(epoch);
        trip.setPositions(positions);
        trip.setLevels(levels);
        trip.setRevs(revs);
        trip.setTimes(times);
        trip.setRevStamp(revEpoch);

        updateTrip(trip);


    }

    public void endLog(){
        stopLocationUpdates();

        if (times == null || times.size() < 2){
            Trip trip = new Trip();
            trip.setTimeStamp(epoch);
            delTrip(trip);
        }

        positions = null;
        levels = null;
        revs = null;
        times = null;
        epoch = -1;
        revEpoch = -1;
    }

    public void revLog(){
        if (revEpoch < 0) {
            String dist = BluetoothService.mService.mOWDevice.characteristics.get(OWDevice.OnewheelCharacteristicOdometer).value.get();
            if (dist != null) {
                revEpoch = Util.parseI(dist);
            } else {
                revEpoch = -1;
            }
        }
    }

    private static class insertAsyncTask extends AsyncTask<Trip, Void, Void> {

        private TripDAO mAsyncTaskDao;
        private int which;

        insertAsyncTask(TripDAO dao, int which) {
            mAsyncTaskDao = dao;
            this.which = which;
        }

        @Override
        protected Void doInBackground(final Trip... params) {
            switch(which) {
                case 0:
                    mAsyncTaskDao.addTrip(params[0]);
                    break;
                case 1:
                    mAsyncTaskDao.updateTrip(params[0]);
                    break;
                case 2:
                    mAsyncTaskDao.delTrip(params[0]);
                    break;
                case 3:
                    mAsyncTaskDao.deleteAll();
                    break;

            }
            return null;
        }
    }

}

