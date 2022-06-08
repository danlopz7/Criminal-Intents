package com.dlopez.criminalintent.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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
            //it: UUID
            findNavController().navigate(
                CrimeListFragmentDirections.showCrimeDetail(crimeId)
            )
            //R.id.action_crimeListFragment_to_crimeDetailFragment)
        }
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
