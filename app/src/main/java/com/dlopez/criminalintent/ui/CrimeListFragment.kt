package com.dlopez.criminalintent.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlopez.criminalintent.Crime
import com.dlopez.criminalintent.databinding.FragmentCrimeListBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var adapter: CrimeListAdapter? = CrimeListAdapter(emptyList())
    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val crimeListViewModel by viewModels<CrimeListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)

        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.crimeRecyclerView.adapter = adapter
        //binding.crimeRecyclerView.adapter.notifyItemMoved(0, 5)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //to run your code in a coroutine, you use a coroutine builder
        //launch is a coroutine builder: a function that creates a new coroutine
        //launch  is an extension to a class called coroutineScope
        viewLifecycleOwner.lifecycleScope.launch {
            //repeatOnLifecycle is itself a suspending function
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                //val crimes = crimeListViewModel.loadCrimes()
                crimeListViewModel.crimes.collect {
                    updateUI(it)
                }
                //updateUI(crimes)
            }
        }
        /*lifecycleScope.launch {

        }*/

        //lifecycle.currentState
        //val crimes = crimeListViewModel.crimes
        //updateUI(crimes)
        /*crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    Log.i(TAG, "Got crimes ${crimes.size}")
                    updateUI(crimes)
                }
            })*/
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeListAdapter(crimes)
        binding.crimeRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}


//RecyclerView expects an itemView to be wrapped in an instance of ViewHolder
//A ViewHolder stores a reference to an item's view
/*private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener {

    private lateinit var crime: Crime
    private val titleTextView = itemView.findViewById(R.id.crime_title) as TextView
    private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
    private val solvedImageView: ImageView = itemView.findViewById(R.id.crime_solved)

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
    }

    fun bind(crime: Crime) {
        this.crime = crime
        titleTextView.text = this.crime.title
        dateTextView.text =
            SimpleDateFormat("EEEE, MMM dd, yyyy.", Locale.US).format(this.crime.date)
        //dateTextView.text = this.crime.date.toString()
        solvedImageView.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

}

//A recyclerView creates ViewHolders, which brings their itemViews along**
//A recyclerView asks the adapter to create a new ViewHolder
//who or what creates crimeHolder instances? can I assume the view hierarchy passed in constructor to contain the child widget I expect?
private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
        val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
        return CrimeHolder(view)
    }

    override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
        val crime = crimes[position]
        holder.bind(crime)
    }

    override fun getItemCount(): Int {
        return crimes.size
    }
}*/
