package com.misbah.dicodingevents.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.misbah.dicodingevents.databinding.ActivityDetailBinding

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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@DetailActivity)
        val eventViewModel: EventViewModel by viewModels { factory }

        val id = intent.getIntExtra(EXTRA_ID, 0)

        eventViewModel.getEventById(id).observe(this) { event ->
            Glide.with(this@DetailActivity)
                .load(event.imageLogo)
                .into(binding.imgEventPhoto)

            binding.btnCategory.text = event.category
            binding.tvTitleEvent.text = event.title
            binding.tvOwner.text = "Diselenggarakan oleh: ${event.ownerName}"
            binding.tvEndTime.text = event.endTime
            binding.tvQuotaRemaining.text = String.format("%s Peserta",
                event.registrants?.let { event.quota?.minus(it) } ?: 0
            )

            Glide.with(this@DetailActivity)
                .load(event.imageLogo)
                .into(binding.imgEventPhotoDesc)

            binding.tvDesc.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY)
            binding.btnRegister.setOnClickListener {
                val link = event.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }
    }
}