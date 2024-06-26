package com.health.beefit.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.health.beefit.R
import com.health.beefit.activities.WorkoutActivity
import com.health.beefit.api.ApiService
import com.health.beefit.api.NetworkService
import com.health.beefit.data.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USERID = "userId"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private var userId: String? = null
//    private var param2: String? = null
    private lateinit var apiService: ApiService
    private var userData: UserData? = null


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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the TextView where you want to display the user's first name & point
        val userFNTextView = view.findViewById<TextView>(R.id.userFNText)
        val userPointTextView = view.findViewById<TextView>(R.id.pointsText)

        if (userId != null) {
            val call = apiService.getOneUserById(userId!!)
            apiService.getOneUserById(userId!!).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    userData = response.body()
                    // Display user first name if userData is not null
                    userData?.let {
                        userFNTextView.text = it.firstName + "!" // Set the text of the TextView
                        userPointTextView.text = it.earnedPoints.toString() + " Points" // Set the text of the TextView
                    }
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Handle network error
                }
            })
        }

        val startWorkoutBtn = view.findViewById<Button>(R.id.startWorkoutButton)

        // Set up a click listener for the startWorkoutButton
        startWorkoutBtn.setOnClickListener {
            val intent = Intent(activity, WorkoutActivity::class.java)
            intent.putExtra("userId", userId)
            intent.putExtra("earnedPoints", userData!!.earnedPoints!!.toInt())
            startActivity(intent)
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId Parameter 1.
//         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERID, userId)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}