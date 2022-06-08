package com.dlopez.criminalintent.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.database.CrimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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