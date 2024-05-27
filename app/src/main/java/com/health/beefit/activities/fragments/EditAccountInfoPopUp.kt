package com.health.beefit.activities.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.health.beefit.R
import com.health.beefit.data.UpdateAccountInfo
import com.health.beefit.data.UserData
import com.health.beefit.utils.ApiService
import com.health.beefit.utils.NetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_USERID = "userID"
private const val ARG_USERFN = "userFN"
private const val ARG_USERLN = "userLN"
private const val ARG_USERPN = "userPN"

class EditAccountInfoPopUp : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var userID: String? = null
    private var userFN: String? = null
    private var userLN: String? = null
    private var userPN: String? = null
    private lateinit var apiService: ApiService
    private lateinit var fragmentContext: Context // Store the context reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userID = it.getString(ARG_USERID)
            userFN = it.getString(ARG_USERFN)
            userLN = it.getString(ARG_USERLN)
            userPN = it.getString(ARG_USERPN)
        }

        apiService = NetworkService.apiService
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context // Store the context when fragment is attached
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_account_info_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editFirstName = view.findViewById<EditText>(R.id.editFirstName)
        val editLastName = view.findViewById<EditText>(R.id.editLastName)
        val editPhoneNumber = view.findViewById<EditText>(R.id.editPhoneNumber)
        val confirmEditInfoBtn = view.findViewById<Button>(R.id.confirmEditInfoBtn)

        editFirstName.hint = userFN
        editLastName.hint = userLN
        editPhoneNumber.hint = userPN

        confirmEditInfoBtn.setOnClickListener {
            var updatedFN = editFirstName.text.toString()
            var updatedLN = editLastName.text.toString()
            var updatedPN = editPhoneNumber.text.toString()


            if (updatedFN == "") {
                updatedFN = userFN!!
            }
            if (updatedLN == "") {
                updatedLN = userLN!!
            }
            if (updatedPN == "") {
                updatedPN = userPN!!
            }


            val call = apiService.getOneUserById(userID!!)
            val updatedAccountInfo = UpdateAccountInfo(updatedFN, updatedLN, updatedPN)
            apiService.updateAccountInfo(userID!!, updatedAccountInfo).enqueue(object : Callback<UserData> {
                override fun onResponse(call: Call<UserData>, response: Response<UserData>) {
                    if (response.code() == 200) {
                        Toast.makeText(fragmentContext, "Your account info has been updated", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(fragmentContext, "Update failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<UserData>, t: Throwable) {
                    // Handle network error
                }
            })
            dismiss()
        }
    }

    companion object {
        @JvmStatic fun newInstance(userID: String, userFN: String, userLN: String, userPN: String) =
                EditAccountInfoPopUp().apply {
                    arguments = Bundle().apply {
                        putString(ARG_USERID, userID)
                        putString(ARG_USERFN, userFN)
                        putString(ARG_USERLN, userLN)
                        putString(ARG_USERPN, userPN)
                    }
                }
    }
}