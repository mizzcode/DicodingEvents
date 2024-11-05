package com.misbah.dicodingevents.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.misbah.dicodingevents.R
import com.misbah.dicodingevents.databinding.ActivityDetailBinding
import com.misbah.dicodingevents.ui.EventViewModel
import org.koin.android.ext.android.inject

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_ID = "extra_id"
    }
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

        val eventViewModel: EventViewModel by inject()

        val id = intent.getIntExtra(EXTRA_ID, 0)

        eventViewModel.getEventById(id).observe(this) { event ->
            binding.fabFav.setImageResource(if (event.isFavorite) R.drawable.baseline_favorite_24 else R.drawable.baseline_favorite_border_24)

            Glide.with(this@DetailActivity)
                .load(event.imageLogo)
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Menyimpan gambar di disk cache
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@DetailActivity, "Tidak ada koneksi internet", Toast.LENGTH_SHORT).show()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressBar.visibility = View.GONE
                        return false
                    }

                })
                .into(binding.imgEventPhoto)

            binding.fabFav.setOnClickListener {
                if (event.isFavorite) {
                    binding.fabFav.setImageResource(R.drawable.baseline_favorite_border_24)
                    eventViewModel.deleteEventFromFavorite(event)
                    Toast.makeText(this@DetailActivity, "Berhasil menghapus event ini dari favorite", Toast.LENGTH_SHORT).show()
                } else {
                    binding.fabFav.setImageResource(R.drawable.baseline_favorite_24)
                    eventViewModel.saveEventToFavorite(event)
                    Toast.makeText(this@DetailActivity, "Berhasil menambahkan event ini ke favorite", Toast.LENGTH_SHORT).show()
                }
            }

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

            @RequiresApi(Build.VERSION_CODES.N)
            binding.tvDesc.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY)
            binding.btnRegister.setOnClickListener {
                val link = event.link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
        }
    }
}