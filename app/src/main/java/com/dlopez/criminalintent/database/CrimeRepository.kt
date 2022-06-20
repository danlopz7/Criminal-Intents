package com.dlopez.criminalintent.database

import android.content.Context
import androidx.room.Room
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import java.util.*

private const val DATABASE_NAME = "crime-database"

class CrimeRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    //ESTO SE PUEDE HACER DESDE LA CLASE DE LA BASE DE DATOS
    //==========================================================

    private val database: CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            CrimeDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2)
        .build()
    //createFromAsset(DATABASE_NAME)

    //==========================================================

    //todavia se necesita hacer cambios a la dependencia del dao accedido desde DDBB
    //inyeccion de dependencias necesita
    private val crimeDao = database.crimeDao()

    //suspend fun getCrimes(): List<Crime> = crimeDao.getCrimes()
    fun getCrimes2(): Flow<List<Crime>> {
        return flow {
            crimeDao.getCrimes()
        }
    }

    fun getCrimes(): Flow<List<Crime>> = crimeDao.getCrimes()

    suspend fun getCrime(id: UUID): Crime = crimeDao.getCrime(id)

    //suspend fun updateCrime(crime: Crime) = crimeDao.updateCrime(crime)
    fun updateCrime(crime: Crime) {
        coroutineScope.launch {
            crimeDao.updateCrime(crime)
        }
    }

    suspend fun addCrime(crime: Crime) = crimeDao.addCrime(crime)
    suspend fun deleteCrime(id: UUID) = crimeDao.deleteCrime(id)

    suspend fun deleteCrime2(crime: Crime) = crimeDao.deleteCrime2(crime)


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