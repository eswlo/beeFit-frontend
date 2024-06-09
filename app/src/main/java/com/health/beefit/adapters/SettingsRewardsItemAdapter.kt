package com.health.beefit.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.health.beefit.R
import com.health.beefit.data.SettingsRewardsItem
import com.health.beefit.data.StringIntPair
import com.health.beefit.data.UpdateRewards
import com.health.beefit.data.UserData
import com.health.beefit.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsRewardsItemAdapter(private val context: Context,
                                 private val dataList: ArrayList<SettingsRewardsItem>,
                                 private val apiService: ApiService,
                                 private val userId: String): RecyclerView.Adapter<SettingsRewardsItemAdapter.ViewHolderClass>() {

    private var onItemClick: ((SettingsRewardsItem) -> Unit)? = null
    private var userData: UserData? = null

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

            //set item click event
            holder.itemView.setOnClickListener {
                onItemClick?.invoke(currentItem)
                val confirmBox = AlertDialog.Builder(context)
                    .setTitle("Use the Reward")
                    .setMessage("Do you want to use this ${currentItem.itemBrand} reward?")
                    .setPositiveButton("Yes") { dialog, which ->
                        updateRewardCount(currentItem.itemBrand, currentItem.itemDescription, currentItem.itemCount, holder)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        Toast.makeText(context, "No reward used.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .create()
                confirmBox.show()
            }
        }
    }

    private fun updateRewardCount(itemBrand: String, itemDescription: String, itemCount: Int, holder: ViewHolderClass) {
        apiService.getOneUserById(userId).enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                userData = response.body()
                var mutableRewardsMap = userData!!.rewards.toMutableMap() // Convert to MutableMap
                var updatedItemCount = itemCount - 1
                if (updatedItemCount > 0) {
                    mutableRewardsMap[itemBrand] = StringIntPair(itemDescription, updatedItemCount)
                } else if (updatedItemCount == 0) {
                    mutableRewardsMap.remove(itemBrand)
                }
                val updateRewards = UpdateRewards(mutableRewardsMap)
                val callUpdateRewards = apiService.updateRewards(userId!!, updateRewards)

                callUpdateRewards.enqueue(object : Callback<UserData> {
                    override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                        if(response.isSuccessful) {
                            Toast.makeText(context, "Reward Used Successfully!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Somethings went wrong! Please try again", Toast.LENGTH_SHORT).show()
                        }
                        holder.rvCount.text = updatedItemCount.toString()
                    }
                    override fun onFailure(call: Call<UserData>, t: Throwable) {
                        Log.e("NetworkError", "Failed to add redeemed reward: ${t.message}")
                    }
                })
            }
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                println("can't get userData")
            }
        })
    }

    class ViewHolderClass(itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvImage: ImageView = itemView.findViewById(R.id.settingsRewardImageView)
        val rvDescription: TextView = itemView.findViewById(R.id.settingsRewardText)
        val rvTerm: TextView = itemView.findViewById(R.id.settingsRewardTermTextView)
        val rvCount: TextView = itemView.findViewById(R.id.settingsRewardCountNumber)
    }
}