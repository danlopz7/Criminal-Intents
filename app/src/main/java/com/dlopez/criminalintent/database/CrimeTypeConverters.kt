package com.dlopez.criminalintent.database

import androidx.room.TypeConverter
import java.util.*

/**
 * creating a type converter so that your database can handle your model data.
 */
class CrimeTypeConverters {

    //convert to store
    @TypeConverter
    fun fromDate(date: Date): Long{
        return date.time
    }

    //back to original type
    @TypeConverter
    fun toDate(millisSinceEpoch: Long): Date {
        return Date(millisSinceEpoch)
    }
}