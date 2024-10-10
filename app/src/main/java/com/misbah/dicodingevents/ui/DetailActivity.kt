package com.misbah.dicodingevents.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.misbah.dicodingevents.databinding.ActivityDetailBinding
import com.misbah.dicodingevents.ui.upcoming.UpcomingViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_ID = "extra_id"
    }
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.hide()

        val upComingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UpcomingViewModel::class.java]

        val id = intent.getIntExtra(EXTRA_ID, 0)

        upComingViewModel.getEventById(id)

        upComingViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        upComingViewModel.event.observe(this) { event ->
            Glide.with(this@DetailActivity)
                .load(event.imageLogo)
                .into(binding.imgEventPhoto)

            binding.btnCategory.text = event.category
            binding.tvTitleEvent.text = event.name
            binding.tvOwner.text = "Diselenggarakan oleh: ${event.ownerName}"
            binding.tvEndTime.text = event.endTime
            binding.tvQuotaRemaining.text = String.format("%s Peserta", event.quota - event.registrants)

            Glide.with(this@DetailActivity)
                .load(event.imageLogo)
                .into(binding.imgEventPhotoDesc)

            binding.tvDesc.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY).toString()
            binding.btnRegister.setOnClickListener {
                val link = event.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }
    }
}