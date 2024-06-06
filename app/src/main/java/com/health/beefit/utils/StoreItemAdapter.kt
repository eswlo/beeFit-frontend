package com.health.beefit.utils

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.health.beefit.R
import com.health.beefit.data.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreItemAdapter (private val context: Context,
                        private val dataList: ArrayList<StoreItem>,
                        private val earnedPoints: Int,
                        private val apiService: ApiService,
                        private var userData: UserData,
                        private val userId: String): RecyclerView.Adapter<StoreItemAdapter.ViewHolderClass>(){

    private var onItemClick: ((StoreItem) -> Unit)? = null


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
        holder.rvMaxPoints.text = currentItem.itemMaxPoints.toString()
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

        //set item click event
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(currentItem)

            if (earnedPoints >= currentItem.itemMaxPoints) {
                val confirmBox = AlertDialog.Builder(context)
                    .setTitle("Redeem Points for Reward")
                    .setMessage("Do you want to redeem your points for an ${currentItem.itemBrand} reward?")
                    .setPositiveButton("Yes") { dialog, which ->
                        val updatedEP = earnedPoints - currentItem.itemMaxPoints
                        updatePointsAndRewards(updatedEP, currentItem.itemBrand)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        Toast.makeText(context, "No redemption done.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .create()
                confirmBox.show()
            } else {
                Toast.makeText(context, "Sorry, you don't have enough points!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePointsAndRewards(updatedEarnedPoints: Int, rewardBrand: String) {
        var mutableRewardsMap = mutableMapOf<String, Number>()
        if ((userData!!.rewards.isEmpty()) || !userData!!.rewards.containsKey(rewardBrand)) {
            mutableRewardsMap = userData!!.rewards.toMutableMap() // Convert to MutableMap
            mutableRewardsMap[rewardBrand] = 1 // Add or update the reward
        } else {
            mutableRewardsMap[rewardBrand] = userData!!.rewards[rewardBrand]!!.toInt() + 1
        }
        val updatePoints = UpdatePoints(updatedEarnedPoints)
        val updateRewards = UpdateRewards(mutableRewardsMap)
        val callUpdatePoints = apiService.updateEarnedPoints(userId!!, updatePoints)
        val callUpdateRewards = apiService.updateRewards(userId!!, updateRewards)

        callUpdatePoints.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if(response.isSuccessful) {
                    Toast.makeText(context, "Points redeemed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Somethings went wrong! Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("NetworkError", "Failed to update earned points: ${t.message}")
            }
        })

        callUpdateRewards.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if(response.isSuccessful) {
                    Toast.makeText(context, "Reward Added Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Somethings went wrong! Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("NetworkError", "Failed to add redeemed reward: ${t.message}")
            }
        })
    }

    class ViewHolderClass (itemView: View): RecyclerView.ViewHolder(itemView) {
        val rvImage:ImageView = itemView.findViewById(R.id.rewardImageView)
        val rvDescription:TextView = itemView.findViewById(R.id.rewardText)
        val rvMaxPoints:TextView = itemView.findViewById(R.id.rewardProgressMaxPoints)
        val rvRewardProgressBar:ProgressBar = itemView.findViewById(R.id.rewardProgressBar)
        val rvRewardProgressText:TextView = itemView.findViewById(R.id.rewardProgressTextView)
    }
}