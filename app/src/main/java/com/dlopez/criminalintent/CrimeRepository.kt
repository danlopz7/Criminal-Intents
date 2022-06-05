package com.dlopez.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.dlopez.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(context: Context) {

    //ESTO SE PUEDE HACER DESDE LA CLASE DE LA BASE DE DATOS
    //==========================================================

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset(DATABASE_NAME)
        .build()

    //==========================================================

    //todavia se necesita hacer cambios a la dependencia del dao accedido desde DDBB
    private val crimeDao = database.crimeDao()


    //suspend fun getCrimes(): List<Crime> = crimeDao.getCrimes()
    fun getCrimes(): Flow<List<Crime>> = crimeDao.getCrimes()
    suspend fun getCrime(id: UUID): Crime = crimeDao.getCrime(id)


    companion object {
        private var INSTANCE: CrimeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}