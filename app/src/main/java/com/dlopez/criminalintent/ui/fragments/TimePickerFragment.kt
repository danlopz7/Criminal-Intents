package com.dlopez.criminalintent.ui.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.*

private const val TAG = "TimePickerFragment"

class TimePickerFragment : DialogFragment() {

    private val args: TimePickerFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val calendar = Calendar.getInstance()
        calendar.time = args.crimeDate
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val initialHour = calendar.get(Calendar.HOUR)
        val initialMinute = calendar.get(Calendar.MINUTE)

        val timeListener =
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
                val date = GregorianCalendar(year, month, day, hour, minute).time
                setFragmentResult(REQUEST_KEY_DATE2, bundleOf(BUNDLE_KEY_DATE2 to date))
            }

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            false
        )
    }

    companion object {
        const val REQUEST_KEY_DATE2 = "REQUEST_KEY_DATE2"
        const val BUNDLE_KEY_DATE2 = "BUNDLE_KEY_DATE2"
    }
}
