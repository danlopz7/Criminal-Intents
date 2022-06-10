package com.dlopez.criminalintent.ui.fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.Time
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import java.util.*

private const val TAG = "TimePickerFragment"

class TimePickerFragment() : DialogFragment()
   // TimePickerDialog.OnTimeSetListener
{

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
                //listener("$hour:$minute")
                setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to date))
            }

        return TimePickerDialog(
            requireContext(),
            timeListener,
            initialHour,
            initialMinute,
            false
        )

        /*val dialog = TimePickerDialog(activity as Context, this, initialHour, initialMinute, false)
        return dialog*/
    }

    companion object {
        const val REQUEST_KEY_DATE = "REQUEST_KEY_DATE"
        const val BUNDLE_KEY_DATE = "BUNDLE_KEY_DATE"
    }

    //1 create a listener
    /*override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener("$hourOfDay:$minute")
        setFragmentResult(REQUEST_KEY_DATE, bundleOf(BUNDLE_KEY_DATE to listener))
        //val resultTime = GregorianCalendar(hourOfDay, minute)
    }*/
}
