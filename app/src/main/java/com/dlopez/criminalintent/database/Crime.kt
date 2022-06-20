package com.dlopez.criminalintent.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

/**
 * annotating your model class to make it a database entity
 */
@Parcelize
@Entity
data class Crime(
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val isSolved: Boolean,
    val suspect: String = ""
) : Parcelable

/**
 * UUID is a utility class included in the Android framework. It provides an easy way to generate
 * universally unique ID values. In the constructor, you generate a random unique ID by calling
 * UUID.randomUUID().
 */

/**
 * Initializing the Date variable using the default Date constructor sets date to the current date. This will
 * be the default date for a crime.
 */

/**
 * The primary key in a database is a column
 * that holds data that is unique for each entry, or row, so that it can be used to look up individual entries.
 * The id property is unique for every Crime, so by adding @PrimaryKey to this property you will be able
 * to query a single crime from the database using its id.
 * */

/*@Entity
data class Crime(@PrimaryKey val id: UUID = UUID.randomUUID(),
                 var title: String = "",
                 var date: Date = Date(),
                 var isSolved: Boolean = false,
                 var requiresPolice: Boolean = false)*/
