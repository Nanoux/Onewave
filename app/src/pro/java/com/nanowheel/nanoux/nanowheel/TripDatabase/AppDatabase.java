package com.nanowheel.nanoux.nanowheel.TripDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Trip.class},version = 3, exportSchema = false)
@TypeConverters({TripDAO.Converters.class})
abstract class AppDatabase extends RoomDatabase {

    abstract TripDAO tripDAO();

    private static AppDatabase INSTANCE;

    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "word_database")
                            .addMigrations(MIGRATION_1_2)
                            .build();

                }
            }
        }
        return INSTANCE;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE trips " + " ADD COLUMN positions TEXT");
            database.execSQL("ALTER TABLE trips " + " ADD COLUMN levels TEXT");
            database.execSQL("ALTER TABLE trips " + " ADD COLUMN revs TEXT");
            database.execSQL("ALTER TABLE trips " + " ADD COLUMN times TEXT");
        }
    };
}
