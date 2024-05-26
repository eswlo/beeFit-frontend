package com.health.beefit.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.health.beefit.R
import com.health.beefit.activities.WorkoutActivity
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
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private var userId: String? = null
    //    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve userId from passed in argument
        arguments?.let {
            userId = it.getString(ARG_USERID)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val accountClickableCardView = view.findViewById<CardView>(R.id.accountClickableCardView)
        val rewardsClickableCardView = view.findViewById<CardView>(R.id.rewardsClickableCardView)
        val notificationClickableCardView = view.findViewById<CardView>(R.id.notificationClickableCardView)
        val privacyClickableCardView = view.findViewById<CardView>(R.id.privacyClickableCardView)
        val supportClickableCardView = view.findViewById<CardView>(R.id.supportClickableCardView)
        val aboutClickableCardView = view.findViewById<CardView>(R.id.aboutClickableCardView)

        accountClickableCardView.setOnClickListener {
            val fragment = SettingsAccountFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        rewardsClickableCardView.setOnClickListener {
            val fragment = SettingsRewardsFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        notificationClickableCardView.setOnClickListener {
            val fragment = SettingsAccountFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        privacyClickableCardView.setOnClickListener {
            val fragment = SettingsAccountFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        supportClickableCardView.setOnClickListener {
            val fragment = SettingsAccountFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }
        aboutClickableCardView.setOnClickListener {
            val fragment = SettingsAccountFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.settingsFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
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
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERID, userId)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}