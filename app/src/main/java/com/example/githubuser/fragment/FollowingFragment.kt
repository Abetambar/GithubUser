package com.example.githubuser.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.adapter.FollowAdapter
import com.example.githubuser.databinding.FragmentFollowingBinding
import com.example.githubuser.activity.UserDetailActivity
import com.example.githubuser.model.FollowingViewModel

class FollowingFragment : Fragment() {

    private var _fragmentFollowingBinding: FragmentFollowingBinding? = null
    private val fragmentFollowingBinding get() = _fragmentFollowingBinding!!
    private val followingViewModel: FollowingViewModel by viewModels()
    private lateinit var followAdapter: FollowAdapter
    private lateinit var url: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = arguments
        url = args?.getString(UserDetailActivity.EXTRA_LOGIN).toString()

        _fragmentFollowingBinding =
            FragmentFollowingBinding.inflate(layoutInflater, container, false)

        return fragmentFollowingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followAdapter = FollowAdapter()

        fragmentFollowingBinding.apply {
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.setHasFixedSize(true)
            rvFollowing.adapter = followAdapter
        }

        followingViewModel.setFollowingUser(url)
        followingViewModel.getFollowingUser().observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                followAdapter.setListFollow(it)
            }
        }

        followingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        fragmentFollowingBinding.apply {
            when (isLoading) {
                true -> {
                    progressBar.visibility = View.VISIBLE
                    rvFollowing.visibility = View.GONE
                }
                else -> {
                    progressBar.visibility = View.GONE
                    rvFollowing.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _fragmentFollowingBinding = null
    }

}