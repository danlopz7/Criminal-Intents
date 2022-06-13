package com.dlopez.criminalintent.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.databinding.ListItemCrimeBinding
import java.text.SimpleDateFormat
import java.util.*

//difference here is we implement ListAdapter.. not used by now
class CrimeListAdapter2(val onCrimeClicked: (crimeId: UUID) -> Unit) :
    ListAdapter<Crime, CrimeListAdapter2.CrimeHolder2>(DiffCallback) {

    class CrimeHolder2(val binding: ListItemCrimeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime, onCrimeClicked: (crimeId: UUID) -> Unit) {
            //
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text =
                SimpleDateFormat("EEEE, MMM dd, yyyy.", Locale.US).format(crime.date)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder2 {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder2(binding)
    }

    override fun onBindViewHolder(holder: CrimeHolder2, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem, onCrimeClicked)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Crime>() {
        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean {
            return oldItem.title == newItem.title
        }
    }
}


//changing lambda expression to CrimeHolder class instead of binding function inside crimeHolder
/*class CrimeListAdapter(
    private val crimes: List<Crime>,
    //this variable should hold a function. "variableName : functionType definition () -> Unit"
    //function parameter " () " and return type " -> Unit "
    private var onCrimeClicked: (crimeId: UUID) -> Unit
) : RecyclerView.Adapter<CrimeListAdapter.CrimeHolder>() {

    class CrimeHolder(private val binding: ListItemCrimeBinding,  private var onCrimeClicked: (crimeId: UUID) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun bind(crime: Crime) {
            binding.crimeTitle.text = crime.title
            binding.crimeDate.text = SimpleDateFormat("EEE, d MMM yyyy", Locale.US).format(crime.date)
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
        return CrimeHolder(binding, onCrimeClicked)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }

    override fun getItemCount() = crimes.size
}*/