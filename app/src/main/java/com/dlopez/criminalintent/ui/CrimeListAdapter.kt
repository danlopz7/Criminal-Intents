package com.dlopez.criminalintent.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.databinding.ListItemCrimeBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

//adapter provides a crimeID
class CrimeListAdapter(
    private val crimes: List<Crime>,
    // this variable should hold a function. variableName : functionType definition " () -> Unit "
    // function parameter " () " and return type " -> Unit "
    private var onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    class CrimeHolder(private val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = crime.date.toString()
            //SimpleDateFormat("EEE, d MMM yyyy", Locale.US).format(crime.date)
            //DateFormat.getDateInstance().format(crime.date)
            //"EEEE, MMM dd, yyyy."
            binding.root.setOnClickListener {
                //passing this crime Id back to CrimeListFragment
                //this is the lambda expression that is invoked when the user presses the root view
                //passing in the id from the crime
                onCrimeClicked(crime.id)
            }

            binding.crimeSolved.visibility = if (crime.isSolved) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime, onCrimeClicked)
    }

    override fun getItemCount() = crimes.size
}

//RecyclerView expects an itemView to be wrapped in an instance of ViewHolder
//A ViewHolder stores a reference to an item's view

//A recyclerView creates ViewHolders, which brings their itemViews along**
//A recyclerView asks the adapter to create a new ViewHolder
