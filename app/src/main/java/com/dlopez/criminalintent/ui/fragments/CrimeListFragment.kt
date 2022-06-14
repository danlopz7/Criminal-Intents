package com.dlopez.criminalintent.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlopez.criminalintent.R
import com.dlopez.criminalintent.database.Crime
import com.dlopez.criminalintent.databinding.FragmentCrimeListBinding
import com.dlopez.criminalintent.ui.CrimeListAdapter
import com.dlopez.criminalintent.ui.viewmodels.CrimeListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    private var adapter: CrimeListAdapter? = null
    private var _binding: FragmentCrimeListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val crimeListViewModel by viewModels<CrimeListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrimeListBinding.inflate(inflater, container, false)
        binding.crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.crimeRecyclerView.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            //repeatOnLifecycle is itself a suspending function
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                crimeListViewModel.crimes.collect {
                    updateUI(it)
                }
            }
        }
    }

    private fun updateUI(crimes: List<Crime>) {
        adapter = CrimeListAdapter(crimes) { crimeId: UUID ->
            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(crimeId)
            )
        }
        binding.crimeRecyclerView.adapter = adapter
        if (crimeListViewModel.crimes.value.isEmpty()) {
            binding.crimeRecyclerView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.crimeRecyclerView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //function callback responsible for creating the menu
    //its not invoked automatically, you explicitly tell the system that this fragment should receive
    //a call to this function by setting setHasOptionMenu(hasMenu: Boolean) function in onCreate()
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)
    }

    //function callback to respond to the selection of an action item
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                showNewCrime()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showNewCrime() {
        viewLifecycleOwner.lifecycleScope.launch {
            val newCrime = Crime(
                id = UUID.randomUUID(),
                title = "",
                date = Date(),
                isSolved = false
            )
            crimeListViewModel.addCrime(newCrime)
            findNavController().navigate(CrimeListFragmentDirections.showCrimeDetail(newCrime.id))
        }
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }


/*private fun updateUI2(crimes: List<Crime>) {
    var adapter2 = binding.crimeRecyclerView.adapter as CrimeListAdapter2
    adapter2.submitList(crimes)
    adapter2.onCrimeClicked()

    adapter2 = CrimeListAdapter2 {
        findNavController().navigate(
            CrimeListFragmentDirections.ShowCrimeDetail(it)
        )
    }
}*/
}
