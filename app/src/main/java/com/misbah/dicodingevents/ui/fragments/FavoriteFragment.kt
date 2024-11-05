package com.misbah.dicodingevents.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.misbah.dicodingevents.R
import com.misbah.dicodingevents.databinding.FragmentFavoriteBinding
import com.misbah.dicodingevents.ui.EventAdapter
import com.misbah.dicodingevents.ui.EventViewModel

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val eventViewModel: EventViewModel by viewModels()

        val eventAdapter = EventAdapter()

        binding.textFavorite.text = getString(R.string.favorite)
        binding.textFavoriteDesc.text = getString(R.string.recommendation_event)

        eventViewModel.getEventFavorite().observe(viewLifecycleOwner) { event ->
            if (event != null) {
                eventAdapter.submitList(event)
            }
        }

        binding.rvFavorite.apply {
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