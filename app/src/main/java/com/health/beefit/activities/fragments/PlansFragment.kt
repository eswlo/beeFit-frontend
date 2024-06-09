package com.health.beefit.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.health.beefit.R
import com.health.beefit.adapters.PlansMessageAdapter
import com.health.beefit.data.Message
import com.health.beefit.data.StoreItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PlansFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlansFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // for adapter & recyclerview
    private lateinit var recyclerView: RecyclerView
    private lateinit var instructionTextView: TextView
    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageButton
    private lateinit var messageList: ArrayList<Message>
    private lateinit var messageAdapter: PlansMessageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_plans, container, false)
        messageList = arrayListOf<Message>()
        messageAdapter = PlansMessageAdapter(messageList)

        recyclerView = view.findViewById(R.id.planRecyclerView)
        val llm = LinearLayoutManager(requireContext())
        llm.stackFromEnd = true
        recyclerView.layoutManager = llm

        instructionTextView = view.findViewById(R.id.instruction_PlanRecyclerView)
        messageEditText = view.findViewById(R.id.enterMsgEditText)
        sendButton = view.findViewById(R.id.sendBtn)

        sendButton.setOnClickListener {
            val question = messageEditText.text.toString().trim()
            addToMsgList(question, Message.SENT_BY_ME)
            messageEditText.setText("")
            instructionTextView.visibility = View.GONE



        }

        recyclerView.adapter = messageAdapter

        return view
    }

    private fun addToMsgList(msg: String, sentBy: String) {
        requireActivity().runOnUiThread {
            messageList.add(Message(msg, sentBy))
            messageAdapter.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(messageAdapter.itemCount)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PlansFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlansFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}