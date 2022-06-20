package com.dlopez.criminalintent.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dlopez.criminalintent.R
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.databinding.FragmentCrimeBinding
import com.dlopez.criminalintent.ui.viewmodels.CrimeDetailViewModel
import com.dlopez.criminalintent.ui.viewmodels.CrimeDetailViewModelFactory
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "CrimeDetailFragment"

class CrimeDetailFragment : Fragment() {

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

    //to have a contact back from the list of contacts. ActivityResults API
    //ActivityResultContracts.PickContact() will send the user to an activity where they can select
    //a contact and receive a URI back
    //registerForActivityResult as expecting a result from the started activity
    //to start working it use selectSuspect.launch(intent)
    private val selectSuspect = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { uri: Uri? ->
        // Handle the result
        uri?.let {
            parseContactSelection(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

            checkboxCrimeSolved.setOnCheckedChangeListener { _, isChecked ->
                crimeDetailViewModel.updateCrime { oldCrime: Crime ->
                    oldCrime.copy(isSolved = isChecked)
                }
            }

            crimeSuspect.setOnClickListener {
                //selecting a contact requires no input, so pass null into the launch()
                selectSuspect.launch(null)
            }

            val selectSuspectIntent = selectSuspect.contract.createIntent(
                requireContext(),
                null
            )
            //with this line, the device wont crash if there's no contacts app
            crimeSuspect.isEnabled = canResolveIntent(selectSuspectIntent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeDetailViewModel.crime.collect { crime ->
                    crime?.let {
                        updateUI(it)
                    }
                }
            }
        }

        setFragmentResultListener(DatePickerFragment.REQUEST_KEY_DATE) { requestKey, bundle ->
            val newDate = bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
            crimeDetailViewModel.updateCrime {
                it.copy(date = newDate)
            }
            viewLifecycleOwner.lifecycleScope.launch {
                delay(50)
                navigateToTimePickerDialog(newDate)
            }
        }

        setFragmentResultListener(TimePickerFragment.REQUEST_KEY_DATE2) { requestKey, bundle ->
            val newDate = bundle.getSerializable(TimePickerFragment.BUNDLE_KEY_DATE2) as Date
            crimeDetailViewModel.updateCrime {
                it.copy(date = newDate)
            }
        }
    }

    private fun updateUI(crime: Crime) {
        binding.apply {
            if (crimeTitle.text.toString() != crime.title) {
                crimeTitle.setText(crime.title)
            }
            btnCrimeDate.text =
                SimpleDateFormat("EEE, d MMM yyyy HH:mm a", Locale.US).format(crime.date)
            btnCrimeTime.text = SimpleDateFormat("K:mm a, z", Locale.US).format(crime.date)

            btnCrimeDate.setOnClickListener {
                findNavController().navigate(CrimeDetailFragmentDirections.selectDate(crime.date))
            }

            btnCrimeTime.setOnClickListener {
                navigateToTimePickerDialog(crime.date)
            }

            checkboxCrimeSolved.isChecked = crime.isSolved

            crimeReport.setOnClickListener {
                val reportIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    //text for the report
                    putExtra(Intent.EXTRA_TEXT, getCrimeReport(crime))
                    //string for the subject of the report
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect))
                    //Any activity responding to this intent will know these constants and what to
                    //do with the associated values.
                }
                //startActivity(reportIntent)
                //create a chooser to be shown every time an implicit intent is used to start an activity
                val chooserIntent = Intent.createChooser(
                    reportIntent,
                    getString(R.string.send_report)
                )
                startActivity(chooserIntent)
            }

            crimeSuspect.text = crime.suspect.ifEmpty {
                getString(R.string.crime_suspect_text)
            }
        }
    }

    private fun navigateToTimePickerDialog(newDate: Date) {
        findNavController().navigate(CrimeDetailFragmentDirections.selectTime(newDate))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_crime -> {
                deleteCrime()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteCrime() {
        lateinit var currentCrime: Crime
        crimeDetailViewModel.crime.value?.let {
            currentCrime = it
        }
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            viewLifecycleOwner.lifecycleScope.launch {
                crimeDetailViewModel.deleteCrime2(currentCrime)
            }
            findNavController().navigate(CrimeDetailFragmentDirections.returnToListFragment())
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${currentCrime.title}?")
        builder.setMessage("Are you sure you want to delete the current crime?")
        builder.create().show()
    }

    private fun getCrimeReport(crime: Crime): String {
        val solvedString = if (crime.isSolved) {
            getString(R.string.crime_report_solved)
        } else {
            getString(R.string.crime_report_unsolved)
        }
        val dateString = crime.date.toString()
        //.format(DATE_FORMAT, crime.date).toString()

        val suspectText = if (crime.suspect.isBlank()) {
            getString(R.string.crime_report_no_suspect)
        } else {
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        //pass in the format string and four other strings in the order in which they should replace the placeholders
        return getString(
            R.string.crime_report,
            crime.title, dateString, solvedString, suspectText
        )
    }

    //Pulling the contact’s name out
    private fun parseContactSelection(contactUri: Uri) {
        val queryFields = arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
        val queryCursor = requireActivity().contentResolver
            .query(contactUri, queryFields, null, null, null)

        queryCursor?.use { cursor ->
            if (cursor.moveToFirst()) {
                val suspect = cursor.getString(0)
                crimeDetailViewModel.updateCrime { oldCrime ->
                    oldCrime.copy(suspect = suspect)
                }
            }
        }
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        //intent.addCategory(Intent.CATEGORY_HOME)
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}