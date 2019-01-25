package com.example.timothyyirenkyi.journalapp.Data;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

public class UriConverter {
    @TypeConverter
    public static Uri fromString(String value) {
        return value == null ? null : Uri.parse(value);
    }

    @TypeConverter
    public static String toString(Uri uri) {
        return uri == null ? null: uri.toString();
    }
}
