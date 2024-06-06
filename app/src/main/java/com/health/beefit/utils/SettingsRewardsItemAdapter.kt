package com.health.beefit.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.health.beefit.R
import com.health.beefit.data.SettingsRewardsItem

class SettingsRewardsItemAdapter(private val dataList: ArrayList<SettingsRewardsItem>): RecyclerView.Adapter<SettingsRewardsItemAdapter.ViewHolderClass>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.settings_rewards_item_layout, parent, false)
        return ViewHolderClass(itemView)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
    override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
        if(dataList.isEmpty()) {
            holder.rvImage.setImageResource(R.drawable.empty)
            holder.rvDescription.text = "You don't have any rewards"
            holder.rvTerm.text = ""
            holder.rvCount.text = ""
        } else {
            val currentItem = dataList[position]
            holder.rvImage.setImageResource(currentItem.itemImage)
            holder.rvDescription.text = currentItem.itemDescription
            holder.rvCount.text = currentItem.itemCount.toString()
        }
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.settingsRewardImageView)
        val rvDescription: TextView = itemView.findViewById(R.id.settingsRewardText)
        val rvTerm: TextView = itemView.findViewById(R.id.settingsRewardTermTextView)
        val rvCount: TextView = itemView.findViewById(R.id.settingsRewardCountNumber)
    }
}