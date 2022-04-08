package com.example.githubuser.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListUserItemAdapter
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.helper.SettingPreferences
import com.example.githubuser.helper.ThemeViewModelFactory
import com.example.githubuser.model.MainViewModel
import com.example.githubuser.response.UserResponseItem

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var listUserAdapter: ListUserItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = SettingPreferences.getInstance(dataStore)
        mainViewModel = ViewModelProvider(this, ThemeViewModelFactory(pref))[MainViewModel::class.java]
        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        supportActionBar?.title = resources.getString(R.string.activity_main)

        listUserAdapter = ListUserItemAdapter()
        listUserAdapter.setOnItemClickCallback(object : ListUserItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponseItem){
                val toUserDetailActivity = Intent(this@MainActivity, UserDetailActivity::class.java)
                    toUserDetailActivity.putExtra(UserDetailActivity.EXTRA_LOGIN, data.login)
                    toUserDetailActivity.putExtra(UserDetailActivity.EXTRA_ID, data.id)
                    toUserDetailActivity.putExtra(UserDetailActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(toUserDetailActivity)
            }
        })

        activityMainBinding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvGithubUser.layoutManager = GridLayoutManager(this@MainActivity, 2)
            } else{
                rvGithubUser.layoutManager = LinearLayoutManager(this@MainActivity)
            }
            rvGithubUser.setHasFixedSize(true)
            rvGithubUser.adapter = listUserAdapter

            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            val searchView = activityMainBinding.edtText
            searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
            searchView.queryHint = resources.getString(R.string.search_hint)
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    githubSearchUser()
                    return true
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })

        }

        mainViewModel.getGithubUserItem().observe(this) {
            if (it.isNotEmpty()) {
                listUserAdapter.setList(it)
                activityMainBinding.imgMain.visibility = View.GONE
            }else {
                activityMainBinding.apply {
                    rvGithubUser.visibility = View.GONE
                    imgMain.setImageResource(R.drawable.not_found)
                    imgMain.visibility = View.VISIBLE
                }
            }
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

    }

    private fun githubSearchUser(){
        activityMainBinding.apply {
            val edtText = edtText.query.toString()
            if (edtText.isEmpty())return
            mainViewModel.setGithubUserItem(edtText)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        activityMainBinding.apply {
            when (isLoading) {
                true -> {
                    imgMain.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    rvGithubUser.visibility = View.GONE
                }
                else -> {
                    imgMain.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    rvGithubUser.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favorite_menu -> {
                val mainToFavorite = Intent(this, FavoriteActivity::class.java)
                    startActivity(mainToFavorite)
            }
            R.id.setting_menu -> {
                val mainToSetting = Intent(this, ThemeActivity::class.java)
                startActivity(mainToSetting)
            }

        }
        return super.onOptionsItemSelected(item)
    }
}