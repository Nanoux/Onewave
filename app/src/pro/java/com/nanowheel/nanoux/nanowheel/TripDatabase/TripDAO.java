package com.nanowheel.nanoux.nanowheel.TripDatabase;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverter;
import androidx.room.Update;

@Dao
public interface TripDAO {

    @Insert
    void addTrip(Trip trip);

    @Query("select * from trips")
    LiveData<List<Trip>> getTrips();

    @Delete
    void delTrip(Trip trip);

    @Query("DELETE FROM trips")
    void deleteAll();

    @Update
    void updateTrip(Trip trip);

    class Converters {
        @TypeConverter
        public static ArrayList<Integer> intFromString(String value) {
            Type listType = new TypeToken<ArrayList<Integer>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String stringFromInt(ArrayList<Integer> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }

        @TypeConverter
        public static ArrayList<Long> longFromString(String value) {
            Type listType = new TypeToken<ArrayList<Long>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String stringFromLong(ArrayList<Long> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }

        @TypeConverter
        public static ArrayList<LatLng> latFromString(String value) {
            Type listType = new TypeToken<ArrayList<LatLng>>() {}.getType();
            return new Gson().fromJson(value, listType);
        }

        @TypeConverter
        public static String stringFromLat(ArrayList<LatLng> list) {
            Gson gson = new Gson();
            String json = gson.toJson(list);
            return json;
        }
    }

}
