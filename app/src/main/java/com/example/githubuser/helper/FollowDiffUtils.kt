package com.example.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.githubuser.response.FollowUserResponse

class FollowDiffUtils(
    private val oldList: ArrayList<FollowUserResponse>,
    private val newList: ArrayList<FollowUserResponse>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }
            oldList[oldItemPosition].login != newList[newItemPosition].login -> {
                false
            }
            oldList[oldItemPosition].avatarUrl != newList[newItemPosition].avatarUrl -> {
                false
            }
            else -> true
        }
    }
}