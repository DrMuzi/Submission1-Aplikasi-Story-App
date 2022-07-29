package com.dimas.coderstory.ui.main

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimas.coderstory.R
import com.dimas.coderstory.adapter.ListStoryAdapter
import com.dimas.coderstory.data.response.ListStoryItem
import com.dimas.coderstory.databinding.ActivityMainBinding
import com.dimas.coderstory.ui.detail.DetailActivity
import com.dimas.coderstory.ui.login.LoginActivity
import com.dimas.coderstory.ui.story.StoryActivity
import com.dimas.coderstory.ui.factory.FactoryStoryVM
import com.dimas.coderstory.data.result.Result


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainVM: MainVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            binding.rvStories.layoutManager = GridLayoutManager(this, 2)
        } else {
            binding.rvStories.layoutManager = LinearLayoutManager(this)
        }

        title = "Coder Story Apps"
        setupViewModel()
    }

    private fun setupViewModel() {
        val factoryStoryVM: FactoryStoryVM = FactoryStoryVM.getInstance(this)
        mainVM = ViewModelProvider(
            this,
            factoryStoryVM
        )[MainVM::class.java]

        mainVM.isLogin().observe(this){
            if (!it){
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

        mainVM.getName().observe(this) {
            binding.tvWelcomeName.text = it
        }

        mainVM.getToken().observe(this){ token ->
            if (token.isNotEmpty()){
                mainVM.getStories(token).observe(this){ result ->
                    if (result != null){
                        when(result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                val stories = result.data.listStory
                                val listStoryAdapter = ListStoryAdapter(stories as ArrayList<ListStoryItem>)
                                binding.rvStories.adapter = listStoryAdapter

                                listStoryAdapter.setOnItemClickCallback(object : ListStoryAdapter.OnItemClickCallback {
                                    override fun onItemClicked(data: ListStoryItem) {
                                        showSelectedStory(data)
                                    }
                                })
                            }
                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                Toast.makeText(
                                    this,
                                    "Failure : " + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showSelectedStory(story: ListStoryItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_STORY, story)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.item_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                mainVM.logout()
                true
            }
            R.id.add_story -> {
                startActivity(Intent(this, StoryActivity::class.java))
                true
            }
            R.id.setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> true
        }
    }
}


