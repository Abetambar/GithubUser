package com.example.githubuser.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.adapter.ListUserItemAdapter
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.model.FavoriteViewModel
import com.example.githubuser.response.UserResponseItem
import kotlinx.coroutines.Delay


class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private lateinit var listUserAdapter: ListUserItemAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = resources.getString(R.string.favorit_user)
        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        listUserAdapter = ListUserItemAdapter()
        listUserAdapter.setOnItemClickCallback(object : ListUserItemAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserResponseItem){
                val toUserDetailActivity = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                toUserDetailActivity.putExtra(UserDetailActivity.EXTRA_LOGIN, data.login)
                toUserDetailActivity.putExtra(UserDetailActivity.EXTRA_ID, data.id)
                toUserDetailActivity.putExtra(UserDetailActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                startActivity(toUserDetailActivity)
            }
        })

        favoriteBinding.apply {
            if (applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvFavorite.layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
            } else {
                rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            }
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = listUserAdapter
        }

        favoriteViewModel.getAllFavoriteUser()?.observe(this) {
            if (it.isNotEmpty()) {
                val changeToList = changeList(it)
                listUserAdapter.setList(changeToList)
                favoriteBinding.imgMain.visibility = View.GONE
            } else {
                val changeToList = changeList(it)
                listUserAdapter.setList(changeToList)
                favoriteBinding.imgMain.visibility = View.VISIBLE
            }
        }
    }

    private fun changeList(favorite: List<FavoriteUser>): ArrayList<UserResponseItem> {
        val listUsers = ArrayList<UserResponseItem>()
        for (user in favorite){
            val userMap = UserResponseItem(
                user.username, user.id, user.avatarUrl
            )
            listUsers.add(userMap)
        }
        return listUsers
    }

}