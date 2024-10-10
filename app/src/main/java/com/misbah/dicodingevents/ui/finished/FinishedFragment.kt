package com.misbah.dicodingevents.ui.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.misbah.dicodingevents.data.response.ListEventsItem
import com.misbah.dicodingevents.databinding.FragmentFinishedBinding
import com.misbah.dicodingevents.ui.EventAdapter

class FinishedFragment : Fragment() {

    private val finishedViewModel: FinishedViewModel by viewModels()
    private var _binding: FragmentFinishedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textViewHeading: TextView = binding.textFinished
        val textViewHeadingDesc: TextView = binding.textFinishedDesc

        finishedViewModel.textHeading.observe(viewLifecycleOwner) {
            textViewHeading.text = it
        }

        finishedViewModel.textHeadingDesc.observe(viewLifecycleOwner) {
            textViewHeadingDesc.text = it
        }

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFinished.layoutManager = layoutManager

        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishedViewModel.listEvents.observe(viewLifecycleOwner) {
            setEventData(it)
        }
    }

    private fun setEventData(data: List<ListEventsItem>) {
        val adapter = EventAdapter()
        adapter.submitList(data)

        binding.rvFinished.adapter = adapter
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