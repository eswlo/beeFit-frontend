package com.health.beefit.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.health.beefit.R
import com.health.beefit.data.SettingsRewardsItem
import com.health.beefit.data.StoreItem
import com.health.beefit.data.UserData
import com.health.beefit.utils.ApiService
import com.health.beefit.utils.NetworkService
import com.health.beefit.utils.SettingsRewardsItemAdapter
import com.health.beefit.utils.StoreItemAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USERID = "userId"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsRewardsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsRewardsFragment : Fragment() {
    private var userId: String? = null
    //    private var param2: String? = null
    private lateinit var apiService: ApiService
    private var userData: UserData? = null

    // for adapter & recyclerview
    private lateinit var recyclerView: RecyclerView
    private val dataList: ArrayList<SettingsRewardsItem> = arrayListOf()
    private val brandList: ArrayList<String> = arrayListOf()
    private val imageList: ArrayList<Int> = arrayListOf()
    private val descriptionList: ArrayList<String> = arrayListOf()
    private val countList: ArrayList<Int> = arrayListOf()

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
        val view = inflater.inflate(R.layout.fragment_settings_rewards, container, false)

        recyclerView = view.findViewById(R.id.settingsRewardsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.hasFixedSize(true)

        if (userId != null) {
            val call = apiService.getOneUserById(userId!!)
            call.enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    userData = response.body()
                    populateLists(userData!!)
                    getData()
                }
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Handle network error
                }
            })
        }
        return view
    }

    private fun populateLists(userData: UserData) {
        val rewards = userData.rewards
        if (rewards.isNotEmpty()) {
            for ((key, value) in rewards) {
                val resourceId = resources.getIdentifier(key, "drawable", requireContext().packageName)
                brandList.add(key)
                imageList.add(resourceId)
                descriptionList.add(value.description)
                countList.add(value.count)
            }
        }
    }

    private fun getData() {
        for (i in imageList.indices) {
            val settingsRewardsItem = SettingsRewardsItem(brandList[i], imageList[i], descriptionList[i], countList[i])
            dataList.add(settingsRewardsItem)
        }
        recyclerView.adapter = SettingsRewardsItemAdapter(dataList)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param userId Parameter 1.
//         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsRewardsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userId: String) =
            SettingsRewardsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERID, userId)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}