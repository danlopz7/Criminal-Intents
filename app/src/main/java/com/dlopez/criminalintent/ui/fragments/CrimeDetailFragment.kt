package com.dlopez.criminalintent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.databinding.FragmentCrimeBinding
import com.dlopez.criminalintent.ui.viewmodels.CrimeDetailViewModel
import com.dlopez.criminalintent.ui.viewmodels.CrimeDetailViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "CrimeDetailFragment"

class CrimeDetailFragment: Fragment() {

    //nullable backing property
    private var _binding: FragmentCrimeBinding? = null
    //cast the binding property to be non-null.
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val args: CrimeDetailFragmentArgs by navArgs()

    private val crimeDetailViewModel: CrimeDetailViewModel by viewModels {
        CrimeDetailViewModelFactory(args.crimeId)
    }

    /*private val crimeDetailViewModel by viewModels<CrimeDetailViewModel> {
        CrimeDetailViewModelFactory(args.crimeId)
    }*/

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCrimeBinding.inflate(inflater, container, false)
        //val view = inflater.inflate(R.layout.fragment_crime, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //listener to the editText, it'll be invoked whenever the text in the EditText is changed
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crimeDetailViewModel.updateCrime { oldCrime: Crime ->
                    oldCrime.copy(title = text.toString())
                }
                //crime = crime.copy(title = text.toString())
                //text is provided as a CharSequence
                //doOnTextChanged() function is actually a Kotlin extension function on the EditText class
            }
            btnCrimeDate.apply {
                //text = crime.date.toString()
                isEnabled = false
            }
            checkboxCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(isSolved = isChecked)
                }
                //crime = crime.copy(isSolved = isChecked)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect {crime ->
                    crime?.let {
                        updateUI(it)
                    }
                }
            }
        }
    }

    private fun updateUI(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            btnCrimeDate.text = crime.date.toString()
            checkboxCrimeSolved.isChecked = crime.isSolved
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}