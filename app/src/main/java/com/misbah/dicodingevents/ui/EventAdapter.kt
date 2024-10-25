package com.misbah.dicodingevents.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.misbah.dicodingevents.data.local.entity.EventEntity
import com.misbah.dicodingevents.ui.EventAdapter.MyViewHolder
import com.misbah.dicodingevents.databinding.ItemRowEventsBinding
import com.misbah.dicodingevents.ui.activity.DetailActivity
import com.misbah.dicodingevents.ui.activity.DetailActivity.Companion.EXTRA_ID

class EventAdapter : ListAdapter<EventEntity, MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowEventsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
    }

    class MyViewHolder(
        private val binding: ItemRowEventsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventEntity) {
            binding.tvEventName.text = event.title
            binding.tvEventSummary.text = event.summary
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(binding.imgEventPhoto)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(EXTRA_ID, event.id)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: EventEntity, newItem: EventEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}