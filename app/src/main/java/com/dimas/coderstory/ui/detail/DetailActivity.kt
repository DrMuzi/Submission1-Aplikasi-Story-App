package com.dimas.coderstory.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dimas.coderstory.R
import com.bumptech.glide.Glide
import com.dimas.coderstory.data.response.ListStoryItem
import com.dimas.coderstory.databinding.ActivityDetailBinding
import com.dimas.coderstory.utils.setLocalDateFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Story Detail"

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)
        binding.apply {
            tvUsername.text = story?.name
            tvCreatedAt.setLocalDateFormat(story?.createdAt.toString())
            tvDescription.text = story?.description
        }
        Glide.with(this)
            .load(story?.photoUrl)
            .into(binding.imgAvatar)
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
    }
}