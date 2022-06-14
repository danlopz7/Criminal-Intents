package com.dlopez.criminalintent.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface CrimeDao {

    @Query("SELECT * FROM crime")
    //fun getCrimes(): List<Crime>
    //suspend fun getCrimes(): List<Crime>
    fun getCrimes(): Flow<List<Crime>>

    @Query("SELECT * FROM crime WHERE id=(:id)")
    suspend fun getCrime(id: UUID): Crime
    //fun getCrime(id: UUID): Crime?

    //suspend modifier so that you can call it from a coroutine scope
    @Update
    suspend fun updateCrime(crime: Crime)

    @Insert
    suspend fun addCrime(crime: Crime)

    /*@Delete(entity = Crime::class)
    suspend fun deleteCrime(crimeId: UUID)*/

    @Query("DELETE FROM crime WHERE id=(:crimeId)")
    suspend fun deleteCrime(crimeId: UUID)

    @Delete
    suspend fun deleteCrime2(crime: Crime)
}