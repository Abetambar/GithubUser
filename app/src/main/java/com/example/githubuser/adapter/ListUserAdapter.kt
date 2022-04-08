package com.example.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuser.databinding.ItemRowUserBinding
import com.example.githubuser.response.UserResponseItem
import com.example.githubuser.helper.UserDiffUtils

class ListUserItemAdapter : RecyclerView.Adapter<ListUserItemAdapter.GithubListUserItemViewHolder>() {

    private var oldGithubListUser = ArrayList<UserResponseItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(newUserList: ArrayList<UserResponseItem>){
        val diffUtil = UserDiffUtils(oldGithubListUser, newUserList)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldGithubListUser = newUserList
        diffResult.dispatchUpdatesTo(this)
    }


    inner class GithubListUserItemViewHolder(private val itemRowUserBinding: ItemRowUserBinding): RecyclerView.ViewHolder(itemRowUserBinding.root) {

        fun bind(user: UserResponseItem) {

            itemRowUserBinding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(user)
            }

            itemRowUserBinding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .circleCrop()
                    .into(imgItemPhoto)
                tvItemName.text = user.login
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubListUserItemViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GithubListUserItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GithubListUserItemViewHolder, position: Int) {
        holder.bind(oldGithubListUser[position])
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(oldGithubListUser[position]) }
    }

    override fun getItemCount(): Int = oldGithubListUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: UserResponseItem)
    }
}
