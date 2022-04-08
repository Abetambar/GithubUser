package com.example.githubuser.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubuser.R
import com.example.githubuser.adapter.SectionPagerAdapter
import com.example.githubuser.databinding.ActivityUserDetailBinding
import com.example.githubuser.model.UserDetailViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

class UserDetailActivity : AppCompatActivity() {

    private lateinit var userDetailActivityBinding: ActivityUserDetailBinding
    private val detailViewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDetailActivityBinding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(userDetailActivityBinding.root)

        supportActionBar?.title = resources.getString(R.string.activity_detail)

        val loginUser = intent.getStringExtra(EXTRA_LOGIN).toString()
        val idUser = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL).toString()
        val mBundle = Bundle()
        mBundle.putString(EXTRA_LOGIN, loginUser)

        detailViewModel.setGithubUserDetail(loginUser)
        detailViewModel.getGithubUserDetail().observe(this) {
            if (it != null) {
                userDetailActivityBinding.apply {
                    tvDetailName.text = it.name
                    tvDetailUsername.text = it.login
                    tvDetailRepo.text =
                        resources.getString(R.string.repository, textFormat(it.publicRepos))
                    tvDetailFollowers.text =
                        resources.getString(R.string.followers, textFormat(it.followers))
                    tvDetailFollowing.text =
                        resources.getString(R.string.following, textFormat(it.following))
                    Glide.with(this@UserDetailActivity)
                        .load(it.avatarUrl)
                        .circleCrop()
                        .into(imgDetail)
                }
            }
        }

        var isFavorite = false
        CoroutineScope(Dispatchers.IO).launch{
            val verify = detailViewModel.checkUser(idUser)
            if (verify != null) {
                if (verify > 0) {
                    isFavorite = true
                    userDetailActivityBinding.btnFavorite.setImageResource(R.drawable.ic_favorite_pink)
                } else {
                    isFavorite = false
                    userDetailActivityBinding.btnFavorite.setImageResource(R.drawable.ic_favorite_grey)
                }
            }
        }

        userDetailActivityBinding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                detailViewModel.addToFavorite(idUser, loginUser, avatarUrl)
                Toast.makeText(this@UserDetailActivity, "Berhasil ditambahkan", Toast.LENGTH_SHORT)
                    .show()
            } else {
                detailViewModel.deleteFromFavorite(idUser)
                Toast.makeText(this@UserDetailActivity, "Berhasil dihapus", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        val sectionPagerAdapter = SectionPagerAdapter(this, mBundle)
        val viewPager: ViewPager2 = userDetailActivityBinding.viewPager
        viewPager.adapter = sectionPagerAdapter
        val tabLayout: TabLayout = userDetailActivityBinding.tabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        supportActionBar?.elevation = 0f

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.option_menu, menu)
        menu.findItem(R.id.favorite_menu).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting_menu -> {
                val mainToSetting = Intent(this, ThemeActivity::class.java)
                startActivity(mainToSetting)
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun textFormat(value: Int?): String {
        val text = when {
            value == null -> {
                "0"
            }
            abs(value / 1000) > 1 -> {
                (value / 1000).toString() + "k"
            }
            else -> {
                value.toString()
            }
        }
        return text
    }

    private fun showLoading(isLoading: Boolean) {
        userDetailActivityBinding.apply {
            when (isLoading) {
                true -> {
                    tvDetailName.visibility = View.GONE
                    imgDetail.visibility = View.GONE
                    tvDetailUsername.visibility = View.GONE
                    card.visibility = View.GONE
                    tabLayout.visibility = View.GONE
                    viewPager.visibility = View.GONE
                    btnFavorite.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                }
                else -> {
                    tvDetailName.visibility = View.VISIBLE
                    imgDetail.visibility = View.VISIBLE
                    tvDetailUsername.visibility = View.VISIBLE
                    card.visibility = View.VISIBLE
                    tabLayout.visibility = View.VISIBLE
                    viewPager.visibility = View.VISIBLE
                    btnFavorite.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}