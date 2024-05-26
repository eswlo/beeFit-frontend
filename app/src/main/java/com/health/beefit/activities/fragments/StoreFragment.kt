package com.health.beefit.activities.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.health.beefit.R
import com.health.beefit.data.UpdatePoints
import com.health.beefit.data.UpdateRewards
import com.health.beefit.data.UserData
import com.health.beefit.utils.ApiService
import com.health.beefit.utils.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USERID = "userId"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StoreFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StoreFragment : Fragment() {
    private var userId: String? = null
    //    private var param2: String? = null
    private lateinit var apiService: ApiService
    private var userData: UserData? = null
    private var earnedPoints = 0

    // Set the points needed for arc'teryx reward
    private val arcPoints = 200


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve userId from passed in argument
        arguments?.let {
            userId = it.getString(ARG_USERID)
//            param2 = it.getString(ARG_PARAM2)
        }
        // Initialize ApiService
        apiService = NetworkService.apiService
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store, container, false)
        // Get the text field for displaying how many points needed for arc'teryx reward
        val arcRewardProgressTextView = view.findViewById<TextView>(R.id.arcRewardProgressTextView)

        // get UserData from server
        if (userId != null) {
            val call = apiService.getOneUserById(userId!!)
            apiService.getOneUserById(userId!!).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    userData = response.body()
                    // Display user first name if userData is not null
                    userData?.let {
                        earnedPoints = it.earnedPoints!!.toInt()
                        var remainingPoints = (arcPoints - earnedPoints)
                        // Set the text of the TextView
                        if (remainingPoints > 0) {
                            arcRewardProgressTextView.text = "$remainingPoints points away!"
                        } else {
                            arcRewardProgressTextView.text = "0 points away!"
                        }
                    }

                    // Get the progress bar
                    val arcRewardProgressBar = view.findViewById<ProgressBar>(R.id.arcRewardProgressBar)

                    // Set the max of the progress bar using the above points
                    arcRewardProgressBar.max = arcPoints
                    // Set up animation
                    println(earnedPoints.toString())
                    ObjectAnimator.ofInt(arcRewardProgressBar, "progress", 0, earnedPoints).setDuration(1000).start()
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Handle network error
                }
            })
        }



        //Set up arc'teryx reward redeem btn listener
        val arcRewardImageButton = view.findViewById<ImageButton>(R.id.arcRewardImageButton)
        arcRewardImageButton.setOnClickListener {
            if (earnedPoints >= arcPoints) {
                val confirmBox = AlertDialog.Builder(requireContext())
                    .setTitle("Redeem Points for Reward")
                    .setMessage("Do you want to redeem your points for an Arc'teryx reward?")
                    .setPositiveButton("Yes") { dialog, which ->
                        earnedPoints -= arcPoints
                        updatePointsAndRewards(earnedPoints, "arc'teryx")
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        Toast.makeText(requireContext(), "No redemption done.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .create()
                confirmBox.show()
            } else {
                Toast.makeText(requireContext(), "Sorry, you don't have enough points!", Toast.LENGTH_SHORT).show()
            }
        }
        return view
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
                    Toast.makeText(requireContext(), "Points redeemed successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Somethings went wrong! Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("NetworkError", "Failed to update earned points: ${t.message}")
            }
        })

        callUpdateRewards.enqueue(object : Callback<UserData> {
            override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                if(response.isSuccessful) {
                    Toast.makeText(requireContext(), "Reward Added Successfully!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Somethings went wrong! Please try again", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserData>, t: Throwable) {
                Log.e("NetworkError", "Failed to add redeemed reward: ${t.message}")
            }
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId Parameter 1.
        //         * @param param2 Parameter 2.
         * @return A new instance of fragment RewardsFragment.
         */
        @JvmStatic
        fun newInstance(userId: String) =
            StoreFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERID, userId)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}