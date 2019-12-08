package com.nanowheel.nanoux.nanowheel.TripDatabase;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.google.android.gms.maps.model.LatLng;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TripDAO_Impl implements TripDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfTrip;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfTrip;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfTrip;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public TripDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrip = new EntityInsertionAdapter<Trip>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `trips`(`timeStamp`,`positions`,`levels`,`revs`,`times`,`revStamp`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trip value) {
        stmt.bindLong(1, value.getTimeStamp());
        final String _tmp;
        _tmp = TripDAO.Converters.stringFromLat(value.getPositions());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp);
        }
        final String _tmp_1;
        _tmp_1 = TripDAO.Converters.stringFromInt(value.getLevels());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = TripDAO.Converters.stringFromInt(value.getRevs());
        if (_tmp_2 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp_2);
        }
        final String _tmp_3;
        _tmp_3 = TripDAO.Converters.stringFromLong(value.getTimes());
        if (_tmp_3 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, _tmp_3);
        }
        stmt.bindLong(6, value.getRevStamp());
      }
    };
    this.__deletionAdapterOfTrip = new EntityDeletionOrUpdateAdapter<Trip>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `trips` WHERE `timeStamp` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trip value) {
        stmt.bindLong(1, value.getTimeStamp());
      }
    };
    this.__updateAdapterOfTrip = new EntityDeletionOrUpdateAdapter<Trip>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `trips` SET `timeStamp` = ?,`positions` = ?,`levels` = ?,`revs` = ?,`times` = ?,`revStamp` = ? WHERE `timeStamp` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trip value) {
        stmt.bindLong(1, value.getTimeStamp());
        final String _tmp;
        _tmp = TripDAO.Converters.stringFromLat(value.getPositions());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, _tmp);
        }
        final String _tmp_1;
        _tmp_1 = TripDAO.Converters.stringFromInt(value.getLevels());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, _tmp_1);
        }
        final String _tmp_2;
        _tmp_2 = TripDAO.Converters.stringFromInt(value.getRevs());
        if (_tmp_2 == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, _tmp_2);
        }
        final String _tmp_3;
        _tmp_3 = TripDAO.Converters.stringFromLong(value.getTimes());
        if (_tmp_3 == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, _tmp_3);
        }
        stmt.bindLong(6, value.getRevStamp());
        stmt.bindLong(7, value.getTimeStamp());
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM trips";
        return _query;
      }
    };
  }

  @Override
  public void addTrip(final Trip trip) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTrip.insert(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delTrip(final Trip trip) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTrip.handle(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateTrip(final Trip trip) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfTrip.handle(trip);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Trip>> getTrips() {
    final String _sql = "select * from trips";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"trips"}, false, new Callable<List<Trip>>() {
      @Override
      public List<Trip> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfTimeStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timeStamp");
          final int _cursorIndexOfPositions = CursorUtil.getColumnIndexOrThrow(_cursor, "positions");
          final int _cursorIndexOfLevels = CursorUtil.getColumnIndexOrThrow(_cursor, "levels");
          final int _cursorIndexOfRevs = CursorUtil.getColumnIndexOrThrow(_cursor, "revs");
          final int _cursorIndexOfTimes = CursorUtil.getColumnIndexOrThrow(_cursor, "times");
          final int _cursorIndexOfRevStamp = CursorUtil.getColumnIndexOrThrow(_cursor, "revStamp");
          final List<Trip> _result = new ArrayList<Trip>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Trip _item;
            _item = new Trip();
            final long _tmpTimeStamp;
            _tmpTimeStamp = _cursor.getLong(_cursorIndexOfTimeStamp);
            _item.setTimeStamp(_tmpTimeStamp);
            final ArrayList<LatLng> _tmpPositions;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPositions);
            _tmpPositions = TripDAO.Converters.latFromString(_tmp);
            _item.setPositions(_tmpPositions);
            final ArrayList<Integer> _tmpLevels;
            final String _tmp_1;
            _tmp_1 = _cursor.getString(_cursorIndexOfLevels);
            _tmpLevels = TripDAO.Converters.intFromString(_tmp_1);
            _item.setLevels(_tmpLevels);
            final ArrayList<Integer> _tmpRevs;
            final String _tmp_2;
            _tmp_2 = _cursor.getString(_cursorIndexOfRevs);
            _tmpRevs = TripDAO.Converters.intFromString(_tmp_2);
            _item.setRevs(_tmpRevs);
            final ArrayList<Long> _tmpTimes;
            final String _tmp_3;
            _tmp_3 = _cursor.getString(_cursorIndexOfTimes);
            _tmpTimes = TripDAO.Converters.longFromString(_tmp_3);
            _item.setTimes(_tmpTimes);
            final int _tmpRevStamp;
            _tmpRevStamp = _cursor.getInt(_cursorIndexOfRevStamp);
            _item.setRevStamp(_tmpRevStamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
