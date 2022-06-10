package com.dlopez.criminalintent.ui.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.*

class DialogFragment: DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val args: DialogFragmentArgs by navArgs()
    var initialYear = 0
    var initialMonth = 0
    var initialDay = 0
    var initialHour = 0
    var initialMinute = 0

    var savedYear = 0
    var savedMonth = 0
    var savedDay = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getDateTimeCalendar()
        DatePickerDialog(requireContext(), this, initialYear, initialMonth, initialDay).show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedYear = year
        savedMonth = month
        savedDay = dayOfMonth

        TimePickerDialog(requireContext(), this, initialHour, initialMinute, false).show()

        //getDateTimeCalendar()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        val resultDate =
            GregorianCalendar(savedYear, savedMonth, savedDay, savedHour, savedMinute).time
        setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to resultDate))
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