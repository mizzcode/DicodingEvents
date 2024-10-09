package com.misbah.dicodingevents.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misbah.dicodingevents.data.response.ListEventsItem
import com.misbah.dicodingevents.databinding.ItemRowEventsBinding

class EventAdapter : ListAdapter<ListEventsItem, EventAdapter.EventViewHolder>(DIFF_CALLBACK) {
    class EventViewHolder(private val binding: ItemRowEventsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Log.d("EventAdapter", "Binding event: ${event.name}")
            binding.tvEventName.text = event.name
            binding.tvEventDescription.text = event.description
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(binding.imgEventPhoto)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val binding = ItemRowEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}