package com.health.beefit.activities.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import com.health.beefit.R
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
 * Use the [RewardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RewardsFragment : Fragment() {
    private var userId: String? = null
    //    private var param2: String? = null
    private lateinit var apiService: ApiService
    private var userData: UserData? = null

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
        val view = inflater.inflate(R.layout.fragment_rewards, container, false)
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
                        val earnedPoints = it.earnedPoints!!.toInt()
                        var remainingPoints = (arcPoints - earnedPoints)
                        // Set the text of the TextView
                        if (remainingPoints > 0) {
                            arcRewardProgressTextView.text = "$remainingPoints points away!"
                        } else {
                            arcRewardProgressTextView.text = "0 points away!"
                        }
                    }
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Handle network error
                }
            })
        }


        // Get the progress bar
        val arcRewardProgressBar = view.findViewById<ProgressBar>(R.id.arcRewardProgressBar)

        // Set the max of the progress bar using the above points
        arcRewardProgressBar.max = arcPoints
        // Set up animation
        ObjectAnimator.ofInt(arcRewardProgressBar, "progress", 0, 50).setDuration(1000).start()


        return view
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
            RewardsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERID, userId)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}