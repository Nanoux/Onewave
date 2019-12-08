package com.nanowheel.nanoux.nanowheel.TripDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;
import androidx.recyclerview.widget.RecyclerView;

public class TripKeyProvider extends ItemKeyProvider {
    TripKeyProvider(RecyclerView view){
        super(ItemKeyProvider.SCOPE_MAPPED);
    }

    @Nullable
    @Override
    public Object getKey(int position) {
        return (long) position;
    }

    @Override
    public int getPosition(@NonNull Object key) {
        long value = (long)key;
        return (int) value;
    }
}
