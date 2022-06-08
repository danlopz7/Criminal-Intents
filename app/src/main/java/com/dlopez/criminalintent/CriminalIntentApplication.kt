package com.dlopez.criminalintent

import android.app.Application
import com.dlopez.criminalintent.database.CrimeRepository

/**
 *  allows you to access lifecycle information about the application itself.
 */
class CriminalIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}