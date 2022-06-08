package com.dlopez.criminalintent.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.database.CrimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    //StateFlow class does a good job of providing consumers with the freshest data.
    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
    //expose the state of the detail screen as a StateFlow holding a crime
    val crime: StateFlow<Crime?>
    //Represents this mutable state flow as a read-only state flow.
        get() = _crime.asStateFlow()

    init {
        viewModelScope.launch {
            _crime.value = crimeRepository.getCrime(crimeId)
        }
    }

    //you need a way to send user input back up to the CrimeDetailViewModel
    //In the lambda expression have the CrimeDetailViewModel provide the latest crime available,
    // and the CrimeDetailFragment update it. This will allow you to safely expose the crime as StateFlow
    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime: Crime? ->
            oldCrime?.let {
                onUpdate(it)
            }
        }
    }

    //perfect place to save changes to the crime
    //access the latest value from the crime StateFlow and save it to the database
    override fun onCleared() {
        super.onCleared()
        /*viewModelScope.launch {
            crime.value?.let {
                crimeRepository.updateCrime(it)
            }
        }*/
        crime.value?.let {
            crimeRepository.updateCrime(it)
        }
    }

}

class CrimeDetailViewModelFactory(
    private val crimeId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}