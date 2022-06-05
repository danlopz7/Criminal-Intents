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
                crimeListViewModel.crimes.collect {
                    updateUI(it)
                }
                //updateUI(crimes)
            }
        }
        //lifecycleScope.launch {
        //lifecycle.currentState
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
