package com.health.beefit.activities.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.health.beefit.BuildConfig
import com.health.beefit.R
import com.health.beefit.adapters.PlansMessageAdapter
import com.health.beefit.data.Message
import com.health.beefit.data.StoreItem
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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

    val API_KEY = BuildConfig.API_KEY
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

            val generativeModel = GenerativeModel(
                // The Gemini 1.5 models are versatile and work with both text-only and multimodal prompts
                modelName = "gemini-1.5-flash",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                apiKey = API_KEY,
                generationConfig = generationConfig {
                    temperature = 0f
                    topK = 32
                    topP = 1f
                    maxOutputTokens = 8192
                }
            )

            lifecycleScope.launch {
                try {
                    // Add "Typing..." message before making the API call
                    addToMsgList("Typing...", Message.SENT_BY_BOT)

                    // Make the API call
                    val response = generativeModel.generateContent(question)

                    // Replace "Typing..." message with the actual response
                    response.text?.let { it1 ->
                        messageList.removeAt(messageList.size - 1)
                        addToMsgList(it1, Message.SENT_BY_BOT)
                    }
                } catch (e: Exception) {
                    Log.e("API RESPONSE ERROR", "An error occurred: ${e.message}", e)
                    Toast.makeText(requireContext(), "Something is wrong! Please try again!", Toast.LENGTH_SHORT).show()
                }
            }
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

    private fun callAPI(question: String) {
        //using okhttp

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