package com.dlopez.criminalintent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.dlopez.criminalintent.Crime
import com.dlopez.criminalintent.databinding.FragmentCrimeBinding
import java.util.*

class CrimeDetailFragment: Fragment() {

    //property for the Crime instance
    private lateinit var crime: Crime
    //nullable backing property
    private var _binding: FragmentCrimeBinding? = null
    //cast the binding property to be non-null.
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //crime = Crime()
        crime = Crime(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            isSolved = false
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCrimeBinding.inflate(layoutInflater, container, false)
        //val view = inflater.inflate(R.layout.fragment_crime, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            //listener to the editText, it'll be invoked whenever the text in the EditText is changed
            crimeTitle.doOnTextChanged { text, _, _, _ ->
                crime = crime.copy(title = text.toString())
                //text is provided as a CharSequence
                //doOnTextChanged() function is actually a Kotlin extension function on the EditText class
            }

            btnCrimeDate.apply {
                text = crime.date.toString()
                isEnabled = false
            }

            checkboxCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crime = crime.copy(isSolved = isChecked)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}