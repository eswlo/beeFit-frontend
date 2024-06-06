package com.health.beefit.utils

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.health.beefit.R
import com.health.beefit.data.StoreItem

class StoreItemAdapter (private val dataList: ArrayList<StoreItem>, private val earnedPoints: Int): RecyclerView.Adapter<StoreItemAdapter.ViewHolderClass>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.store_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        val currentItem = dataList[position]
        holder.rvImage.setImageResource(currentItem.itemImage)
        holder.rvDescription.text = currentItem.itemDescription

        // Set the max of the progress bar using the above points
        holder.rvRewardProgressBar.max = currentItem.itemMaxPoints
        // Set up animation
        println(earnedPoints.toString())
        ObjectAnimator.ofInt(holder.rvRewardProgressBar, "progress", 0, earnedPoints).setDuration(1000).start()


        val remainingPoints = (currentItem.itemMaxPoints - earnedPoints)
        // Set the text of the TextView
        if (remainingPoints > 0) {
            holder.rvRewardProgressText.text = "$remainingPoints points away!"
        } else {
            holder.rvRewardProgressText.text = "0 points away!"
        }
    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvImage:ImageView = itemView.findViewById(R.id.rewardImageView)
        val rvDescription:TextView = itemView.findViewById(R.id.rewardText)
        val rvRewardProgressBar:ProgressBar = itemView.findViewById(R.id.rewardProgressBar)
        val rvRewardProgressText:TextView = itemView.findViewById(R.id.rewardProgressTextView)
    }
}