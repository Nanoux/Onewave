package com.nanowheel.nanoux.nanowheel.TripDatabase;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TripViewModel extends AndroidViewModel {
    private TripRepository mRepository;

    private LiveData<List<Trip>> mAllWords;

    public TripViewModel (Application application) {
        super(application);
        mRepository = new TripRepository(application);
        mAllWords = mRepository.getTrips();

        /*Trip trip = new Trip();
        trip.setTimeStamp((long)(Math.random() * 1000));
        ArrayList<Integer> revs = new ArrayList<>();
        ArrayList<Integer> levels = new ArrayList<>();
        ArrayList<Long> times = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            times.add(i * 1000L + trip.getTimeStamp());
            revs.add(i * 100);
            levels.add(i * 10);
        }
        trip.setTimes(times);
        trip.setRevs(revs);
        trip.setLevels(levels);
        trip.setRevStamp(0);
        addTrip(trip);*/
    }

    LiveData<List<Trip>> getAllTrips() { return mAllWords; }

    void addTrip(Trip trip) { mRepository.addTrip(trip); }
    void updateTrip(Trip trip) { mRepository.updateTrip(trip); }
    void delTrip(Trip trip) { mRepository.delTrip(trip); }
    void delAll() { mRepository.delAll(); }
}
