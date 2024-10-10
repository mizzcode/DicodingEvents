package com.misbah.dicodingevents.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.misbah.dicodingevents.data.response.ListEventsItem
import com.misbah.dicodingevents.databinding.FragmentUpcomingBinding
import com.misbah.dicodingevents.ui.EventAdapter

class UpcomingFragment : Fragment() {

    private val upcomingViewModel: UpcomingViewModel by viewModels()
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

        val textViewHeading: TextView = binding.textUpcoming
        val textViewHeadingDesc: TextView = binding.textUpcomingDesc

        upcomingViewModel.textHeading.observe(viewLifecycleOwner) {
            textViewHeading.text = it
        }

        upcomingViewModel.textHeadingDesc.observe(viewLifecycleOwner) {
            textViewHeadingDesc.text = it
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvUpcoming.layoutManager = layoutManager

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        upcomingViewModel.listEvents.observe(viewLifecycleOwner) {
            setEventData(it)
        }
    }

    private fun setEventData(data: List<ListEventsItem>) {
        val adapter = EventAdapter()
        adapter.submitList(data)

        binding.rvUpcoming.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}