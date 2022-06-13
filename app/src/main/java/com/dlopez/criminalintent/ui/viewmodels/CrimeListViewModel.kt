package com.dlopez.criminalintent.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.database.CrimeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    //se deberia inyectar la dependencia del repositorio
    private val crimeRepository = CrimeRepository.get()

    //StateFlow class does a good job of providing consumers with the freshest data.
    //private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(null)
    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init {
        //use launch builder with normal Flow, and launchWhenStarted with StateFlow and collectLatest
        //you need a coroutineScope when trying to read from the stream of values within the Flow
        //to access the values within the Flow, you must observe it using the collect function, which
        //is a suspending function, so it needs to be called within a coroutine scope.
        viewModelScope.launch{
            crimeRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }

    suspend fun addCrime(crime: Crime) = crimeRepository.addCrime(crime)

    /* suspend fun loadCrimes(): List<Crime> {
         return crimeRepository.getCrimes()
     }*/
}