package com.dlopez.criminalintent.ui.fragments

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.*

private const val TAG = "DatePickerFragment"

class DatePickerFragment : DialogFragment() {

    private val args: DatePickerFragmentArgs by navArgs()
    var initialYear = 0
    var initialMonth = 0
    var initialDay = 0
    var initialHour = 0
    var initialMinute = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        getDateTimeCalendar()

        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                val resultDate = GregorianCalendar(year, month, day, initialHour, initialMinute).time
                setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to resultDate))
            }

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }

    private fun getDateTimeCalendar() {
        // Use the current date as the default date in the pickerDialog
        val calendar = Calendar.getInstance()
        calendar.time = args.crimeDate

        initialYear = calendar.get(Calendar.YEAR)
        initialMonth = calendar.get(Calendar.MONTH)
        initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        initialHour = calendar.get(Calendar.HOUR)
        initialMinute = calendar.get(Calendar.MINUTE)

    }

    companion object {
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"
    }
}
