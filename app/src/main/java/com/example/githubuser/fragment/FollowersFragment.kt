package com.example.githubuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.FollowAdapter
import com.example.githubuser.databinding.FragmentFollowersBinding
import com.example.githubuser.activity.UserDetailActivity
import com.example.githubuser.model.FollowersViewModel


class FollowersFragment : Fragment() {

    private var _fragmentFollowersBinding: FragmentFollowersBinding? = null
    private val fragmentFollowersBinding get() = _fragmentFollowersBinding!!
    private val followersViewModel: FollowersViewModel by viewModels()
    private lateinit var followAdapter: FollowAdapter
    private lateinit var url:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = arguments
        url = args?.getString(UserDetailActivity.EXTRA_LOGIN).toString()

        _fragmentFollowersBinding = FragmentFollowersBinding.inflate(layoutInflater, container, false)
        return fragmentFollowersBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followAdapter = FollowAdapter()

        fragmentFollowersBinding.apply {
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.setHasFixedSize(true)
            rvFollowers.adapter = followAdapter

        }

        followersViewModel.setFollowersDetailUser(url)

        followersViewModel.getFollowersDetailUser().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                followAdapter.setListFollow(it)
            }
        }

        followersViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentFollowersBinding.apply {
            when (isLoading) {
                true -> {
                    progressBar.visibility = View.VISIBLE
                    rvFollowers.visibility = View.GONE
                }
                else -> {
                    progressBar.visibility = View.GONE
                    rvFollowers.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentFollowersBinding = null
    }
}