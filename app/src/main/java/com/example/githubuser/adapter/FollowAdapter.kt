package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ItemRowUserBinding
import com.example.githubuser.response.FollowUserResponse
import com.example.githubuser.helper.FollowDiffUtils

class FollowAdapter: RecyclerView.Adapter<FollowAdapter.FollowViewHolder>() {

    private var oldGithubFollowList = ArrayList<FollowUserResponse>()

    fun setListFollow(newUserList: ArrayList<FollowUserResponse>){
        val diffUtil = FollowDiffUtils(oldGithubFollowList, newUserList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldGithubFollowList= newUserList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class FollowViewHolder(private val itemRowUserBinding: ItemRowUserBinding) : RecyclerView.ViewHolder(itemRowUserBinding.root) {
        fun bind(followers: FollowUserResponse) {
            itemRowUserBinding.apply {
                Glide.with(itemView)
                    .load(followers.avatarUrl)
                    .circleCrop()
                    .into(imgItemPhoto)
                tvItemName.text = followers.login
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowViewHolder, position: Int) {
        holder.bind(oldGithubFollowList[position])
    }

    override fun getItemCount(): Int = oldGithubFollowList.size

}