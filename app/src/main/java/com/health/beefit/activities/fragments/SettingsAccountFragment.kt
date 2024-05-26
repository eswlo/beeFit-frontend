package com.health.beefit.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.health.beefit.R
import com.health.beefit.data.UserData
import com.health.beefit.utils.ApiService
import com.health.beefit.utils.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USERID = "userId"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsAccountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsAccountFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_settings_account, container, false)

        // Find the TextView where you want to display the user's first name & point
        val accountFNTextView = view.findViewById<TextView>(R.id.accountFNTextView)
        val accountLNTextView = view.findViewById<TextView>(R.id.accountLNTextView)
        val accountUNTextView = view.findViewById<TextView>(R.id.accountUNTextView)
        val accountEMTextView = view.findViewById<TextView>(R.id.accountEMTextView)
        val accountPNTextView = view.findViewById<TextView>(R.id.accountPNTextView)
        val accountEPTextView = view.findViewById<TextView>(R.id.accountEPTextView)

        if (userId != null) {
            val call = apiService.getOneUserById(userId!!)
            apiService.getOneUserById(userId!!).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    userData = response.body()
                    // Display user first name if userData is not null
                    userData?.let {
                        accountFNTextView.text = "First Name: " + it.firstName
                        accountLNTextView.text = "Last Name: " + it.lastName
                        accountUNTextView.text = "User Name: " + it.userName
                        accountEMTextView.text = "Email: " + it.email
                        accountPNTextView.text = "Phone Number: " + it.phoneNumber
                        accountEPTextView.text = "Earned Points: " + it.earnedPoints.toString()
                    }
                }

                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Handle network error
                }
            })
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
         * @return A new instance of fragment SettingsAccountFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
            SettingsAccountFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERID, userId)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}