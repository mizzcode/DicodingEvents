package com.misbah.dicodingevents.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.misbah.dicodingevents.R
import com.misbah.dicodingevents.data.Result
import com.misbah.dicodingevents.databinding.FragmentUpcomingBinding
import com.misbah.dicodingevents.ui.EventAdapter
import com.misbah.dicodingevents.ui.EventViewModel

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventViewModel: EventViewModel by viewModels()

        val eventAdapter = EventAdapter()

        binding.textUpcoming.text = getString(R.string.app_name)
        binding.textUpcomingDesc.text = getString(R.string.recommendation_event)

        eventViewModel.getEventsActive().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val data = result.data
                        eventAdapter.submitList(data)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "onViewCreated: ${result.error}")
                    }
                }
            }
        }

        binding.rvUpcoming.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = eventAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}