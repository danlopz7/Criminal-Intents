package com.dlopez.criminalintent.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.dlopez.criminalintent.Crime
import com.dlopez.criminalintent.databinding.ListItemCrime2Binding
import com.dlopez.criminalintent.databinding.ListItemCrimeBinding
import java.text.SimpleDateFormat
import java.util.*

//RecyclerView expects an itemView to be wrapped in an instance of ViewHolder
//A ViewHolder stores a reference to an item's view
class CrimeHolder(val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(crime: Crime) {
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text =
            SimpleDateFormat("EEEE, MMM dd, yyyy.", Locale.US).format(crime.date)

        //binding.crimeDate.text = crime.date.toString()

        binding.root.setOnClickListener {
            Toast.makeText(binding.root.context, "${crime.title} clicked!", Toast.LENGTH_SHORT)
                .show()
        }
        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}

//A recyclerView creates ViewHolders, which brings their itemViews along**
//A recyclerView asks the adapter to create a new ViewHolder
//who or what creates crimeHolder instances? can I assume the view hierarchy passed in constructor to contain the child widget I expect?
class CrimeListAdapter(private val crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        /*holder.apply {
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = crime.date.toString()
        }*/
        holder.bind(crime)
    }

    override fun getItemCount() = crimes.size
}
