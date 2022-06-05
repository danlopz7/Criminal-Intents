package com.dlopez.criminalintent.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlopez.criminalintent.Crime
import com.dlopez.criminalintent.CrimeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import kotlin.random.Random

private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    //se deberia inyectar la dependencia del repositorio
    private val crimeRepository = CrimeRepository.get()

    private val _crimes: MutableStateFlow<List<Crime>> = MutableStateFlow(emptyList())
    val crimes: StateFlow<List<Crime>>
        get() = _crimes.asStateFlow()

    init {
        viewModelScope.launch{
            crimeRepository.getCrimes().collect {
                _crimes.value = it
            }
        }
    }

    /* suspend fun loadCrimes(): List<Crime> {
         return crimeRepository.getCrimes()
     }*/
}